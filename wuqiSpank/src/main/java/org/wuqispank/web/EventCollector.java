package org.wuqispank.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.headlessintrace.client.IntraceException;
import org.headlessintrace.client.connection.Callback;
import org.headlessintrace.client.connection.ConnectionException;
import org.headlessintrace.client.connection.ConnectionTimeout;
import org.headlessintrace.client.connection.DefaultCallback;
import org.headlessintrace.client.connection.DefaultConnectionList;
import org.headlessintrace.client.connection.IConnectionStateCallback;
import org.headlessintrace.client.connection.command.ClassInstrumentationCommand;
import org.headlessintrace.client.connection.command.IAgentCommand;
import org.headlessintrace.client.filter.ContiguousEventFilter;
import org.headlessintrace.client.filter.ITraceFilterExt;
import org.headlessintrace.client.filter.IncludeAnyOfTheseEventsFilterExt;
import org.headlessintrace.client.filter.IncludeThisMethodFilterExt;
import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEventParser;
import org.headlessintrace.client.request.BadCompletedRequestListener;
import org.headlessintrace.client.request.ICompletedRequestCallback;
import org.headlessintrace.client.request.IRequest;
import org.headlessintrace.client.request.RequestConnection;
import org.headlessintrace.jdbc.IJdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.IReconnector;
import org.wuqispank.IRequestExporter;
import org.wuqispank.IRequestImporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.web.IFactory;


public class EventCollector implements ServletContextListener, ICompletedRequestCallback {
	RequestConnection m_requestConnection = null;
	/**
	 *   F I L T E R
	 */
	ITraceEventParser m_parser = null;
	
	public EventCollector() throws IntraceException {
		m_parser = org.headlessintrace.client.DefaultFactory.getFactory().getEventParser();
		m_requestStart = m_parser.createEvent("[15:47:00.999]:[203]:javax.servlet.http.HttpServlet:service: {:50", 0);
		m_requestCompletion = m_parser.createEvent("[15:47:00.999]:[203]:javax.servlet.http.HttpServlet:service: }:250", 0);
	}
	ITraceEvent m_requestStart = null;
	ITraceEvent m_requestCompletion = null;
	private IRequestRepository m_repo = DefaultFactory.getFactory().createRepo();
	static Logger LOG = LoggerFactory.getLogger(EventCollector.class);
	Callback m_connectionStatusCallback = new Callback();
	public IRequestRepository getRepo() {
		return m_repo;
	}
	public IRequestWrapper getRequest(String uniqueIdCriteria) {
		IRequestWrapper rc = getRepo().get(uniqueIdCriteria);
		LOG.debug("getRequest(): [" + rc + "]");
		return rc;
	}

	private static final long serialVersionUID = -4953960682833247515L;
	public static final String WUQISPANK_REPO = "org.wuqispankSingletonInMemoryRepository";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed");
	}
	/**
	 * All connections to the SUT (system under test), even the very first, are made by the DefaultConnector
	 */
	protected void initReconnector() {
		
		GroupNameThreadFactory threadFactory = new GroupNameThreadFactory("wuqiSpankReconnector");
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, threadFactory);
		
        
       Runnable runnable = DefaultFactory.getFactory().getReconnector();

       /**
         * Parameters :
         * 1. Object of Runnable
         * 2. Initial Delay
         * 3. Delay between successive execution
         * 4. Time Unit
         */
        scheduler.scheduleAtFixedRate(
        		runnable, 
        		1,
        		DefaultFactory.getFactory().getConfig().getReconnectIntervalInSeconds(), 
        		TimeUnit.SECONDS);
 
		
	}
	
	protected FilenameFilter getExportFilenameFilter() {
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				boolean rc = false;
				if (name.endsWith(IRequestExporter.FILE_NAME_EXTENSION))
					rc = true;
				else
					rc = false;
				return rc;
			}
		};
		return filter;
	}
	
	protected void loadExportedRequests() {
		IConfig config = DefaultFactory.getFactory().getConfig();
		try {
			if (config.getExportDir() !=null && config.getExportDir().exists()) {
				IRequestImporter importer = DefaultFactory.getFactory().getDynaTracePurePathImporter();
				File[] filesToImport = config.getExportDir().listFiles( getExportFilenameFilter()  );
				for(File fileToImport : filesToImport) {
					importer.setInputStream( new FileInputStream(fileToImport) );
					IRequestWrapper[] requests = importer.importRq();
					for(IRequestWrapper rq : requests)
						getRepo().add(rq);
				}
			} else {
				String msg = DefaultFactory.getFactory().getMessages().getBypassImportMessage(WebXmlConfigImpl.WEB_XML_EXPORT_DIR, config.getExportDir().getAbsolutePath());
				LOG.warn(msg);
			}
			
		} catch (Exception e) {
			LOG.error(DefaultFactory.getFactory().getMessages().getFailureLoadingExportFiles(), e);
			LOG.error(e.getMessage());
		}
		
		
	}
	/**
	 * wuqispank-specific web.xml configuration errors will be thrown from this method.
	 * http://mail-archives.apache.org/mod_mbox/tomcat-users/200404.mbox/%3C9C5166762F311146951505C6790A9CF8013E01A8@US-VS1.corp.mpi.com%3E
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		LOG.debug("Entering contextInitialized");
		System.out.println("!!Entering contextInitialized");
		
		IConfig config = new WebXmlConfigImpl(servletContextEvent.getServletContext());
		DefaultFactory.getFactory().setConfig(config);//make this available GLOBALLY to the rest of the program.
		
		loadExportedRequests(); //continue even if this fails.
		
		try {
			m_requestConnection = getRequestConnection();
			
			DefaultConnectionList.getSingleton().add(config.getInTraceAgent(), m_requestConnection);
			
			servletContextEvent.getServletContext().setAttribute(WUQISPANK_REPO, this);
			
			this.initReconnector();
			
		} catch (IntraceException e) {
			e.printStackTrace();
		} catch (WuqispankException e) {
			e.printStackTrace();
		}
		
		
//		try {
//			
//			m_requestConnection = getRequestConnection();
//					
//			this.m_requestConnection.setCommandArray( this.getCommandArray());
//			this.m_requestConnection.setHostPort(config.getInTraceAgent());
//			
//			boolean rc = m_requestConnection.connect(config.getInTraceAgent(), getCommandArray() );
//			LOG.debug("New RequestConnection[" + m_requestConnection.hashCode() + "] success [" + rc + "] callback [" + m_connectionStatusCallback.getConnectState().toString() + "] msgs [" + m_connectionStatusCallback.getMessages().toString() + "]");
//			
//			if (rc) {
//				DefaultConnectionList.getSingleton().add(config.getInTraceAgent(), m_requestConnection);
//			} else {
//				LOG.error( DefaultFactory.getFactory().getMessages().getConnectionError(config.getInTraceAgent()) );
//			}
//			
//			servletContextEvent.getServletContext().setAttribute(WUQISPANK_REPO, this);
//		} catch (WuqispankException we) {
//				System.out.println(we.getMessage());
//				System.out.println( we.getCause().getMessage());
//				we.getCause().printStackTrace();
//		} catch (ConnectionTimeout e) {
//			// 
//			e.printStackTrace();
//		} catch (IntraceException e) {
//			// 
//			e.printStackTrace();
//		} catch (ConnectionException e) {
//			// 
//			e.printStackTrace();
//		} catch (BadCompletedRequestListener e) {
//			// 
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Startup configuration of the connection to the InTrace agent.
	 * @return
	 * @throws IntraceException
	 * @throws WuqispankException
	 */
	protected RequestConnection getRequestConnection() throws IntraceException, WuqispankException {
		RequestConnection requestConnection = new RequestConnection(1000);//but we\"ll override this 1000 in a minute, hold on.

		requestConnection.setCompletedRequestCallback(getCompletedRequestCallback() );

		requestConnection.addCallback(getConnectionStatusCallback());
		requestConnection.getTraceWriter().setTraceFilterExt(getFilter());

		requestConnection.setRequestStartFilter(this.getStartFilter());
		requestConnection.setRequestCompletionFilter(this.getCompletionFilter());
		
		requestConnection.addCallback( new ProgressMeterCallback() );

		requestConnection.setCommandArray( this.getCommandArray());
		requestConnection.setHostPort(DefaultFactory.getFactory().getConfig().getInTraceAgent());
		
		return requestConnection;
	}
	private ICompletedRequestCallback getCompletedRequestCallback() {
		ContiguousEventFilter cef = new ContiguousEventFilter(this);
		cef.keepMethod("prepareStatement");
		cef.keepMethod("executeQuery");
		cef.keepMethod("service");
		return new org.wuqispank.UrlExtractor(cef);

	}
	private IConnectionStateCallback getConnectionStatusCallback() {
		return m_connectionStatusCallback;
	}
	ITraceFilterExt getCompletionFilter() {
		IncludeThisMethodFilterExt methodFilter = new IncludeThisMethodFilterExt();
		methodFilter.setFilterCriteria(m_requestCompletion);
		return methodFilter;
	}
	ITraceFilterExt getStartFilter() {
		IncludeThisMethodFilterExt startMethodFilter = new IncludeThisMethodFilterExt();
		startMethodFilter.setFilterCriteria(m_requestStart);
		return startMethodFilter;
	}	
	
	/**
	 * Startup configuration.  If making changes here, consider whether this.getFilter() also needs corresponding changes.
	 * @return
	 * @throws WuqispankException
	 */
	protected IAgentCommand[] getCommandArray() throws WuqispankException {
		StringBuilder output = new StringBuilder();
		output.append(this.m_requestCompletion.getPackageAndClass());
		
		IJdbcProvider provider = DefaultFactory.getFactory().getJdbcProvider();
		for(String className : provider.getConnectionPackageAndClass()) {
			output.append("|");  // the use of | is documented here: https://github.com/mchr3k/org.intrace/issues/21
			output.append(className);
		}
		for(String className : provider.getStatementPackageAndClass() ) {
			output.append("|"); // the use of | is documented here: https://github.com/mchr3k/org.intrace/issues/21
			output.append(className);
		}
		ClassInstrumentationCommand cicRC = new ClassInstrumentationCommand();
		cicRC.setIncludeClassRegEx(output.toString());
		
		IAgentCommand[] ary = {cicRC};
		return ary;
	}
	/**
	 * After a web request is completed, this method will be invoked
	 * and we\"ll stored the data (all the SQL statements) from the request.
	 */
	@Override
	public void requestCompleted(IRequest request) {
		
		if (request.getEvents().size()>0) {
			IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
			
			try {
				LOG.warn("Event count ["  + request.getEvents().size() + "]");
				requestWrapper.setRequest(request);
				getRepo().add(requestWrapper);
			} catch (WuqispankException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} //else there was a round trip to the SUT without SQL events.
		
	}
	/**
	 * 
	 * If making changes here, also consider making changes to this.getCommandArray().
	 * <p>
	 * This method filters for method names only, because its a touch more efficient.
	 * We\"ve already configured InTrace to give us the correct class names,
	 * so would double validate that it sent us events specifically for that set of classes?
	 * <ul>
	 *      <li>java.sql.Connection#prepareStatement(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#execute(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#executeQuery(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#executeUpdate(...).  All overloaded versions of this method are supported.</li>
	 * </ul>
	 * @return
	 * @throws IntraceException 
	 */
	private ITraceFilterExt getNativeFilter() throws IntraceException {
		IncludeAnyOfTheseEventsFilterExt filter = new IncludeAnyOfTheseEventsFilterExt(); 
			List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
			myCriteriaList.add(m_requestStart);
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:nativeSQL: {:50", 0) );
			
			filter.setFilterCriteria(myCriteriaList);			
		
		return filter;
	}
	private ITraceFilterExt getFilter() throws IntraceException {
		IncludeAnyOfTheseEventsFilterExt filter = new IncludeAnyOfTheseEventsFilterExt(); 
			List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
			myCriteriaList.add(m_requestStart);
			
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:prepareStatement: {:50", 0) );
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:execute: {:50", 0) );
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:executeQuery: {:50", 0) );
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:executeUpdate: {:50", 0) );
			myCriteriaList.add( m_parser.createEvent("[15:47:00.999]:[203]:com.foo.Bar:createResultSet: {:50", 0) );

			filter.setFilterCriteria(myCriteriaList);			
		
		return filter;
	}
	class ProgressMeterCallback extends DefaultCallback {

		@Override
		public void setProgress(Map<String, String> progress) {
			String result = progress.get("NUM_PROGRESS_DONE");
			System.out.print(".");
			if (result!=null && result.equals("true")) {
				LOG.warn("Agent Instrumentation complete.");
				System.out.println("!!instru complete");
			}
		}
	}
    /**
 * A thread factory that names each thread with the provided group name.
 * <p>
 * Except lines with "elarson modified", copied from code 
 * Executors$DefaultThreadFactory -- Doug Lea, Copyright Oracle, et al.
 */
static class GroupNameThreadFactory implements ThreadFactory {
    private String groupname; /* elarson modified */
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    GroupNameThreadFactory(String groupname) {
        this.groupname = groupname; /* elarson modified */
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                              Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" +
                       this.groupname + /* elarson modified */
                       poolNumber.getAndIncrement() +
                     "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                              namePrefix + threadNumber.getAndIncrement(),
                              0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}	
}

