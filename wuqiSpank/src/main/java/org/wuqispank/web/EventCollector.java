package org.wuqispank.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.client.IntraceException;
import org.headlessintrace.client.connection.Callback;
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
import org.headlessintrace.client.request.ICompletedRequestCallback;
import org.headlessintrace.client.request.IRequest;
import org.headlessintrace.client.request.RequestConnection;
import org.headlessintrace.jdbc.IJdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.health.DefaultTcpHealthCheck;
import org.wuqispank.importexport.IExportDirListener;
import org.wuqispank.importexport.IImportExportMgr;
import org.wuqispank.model.IRequestListener;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.util.GroupNameThreadFactory;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;


public class EventCollector implements ServletContextListener, ICompletedRequestCallback, IRequestListener {
	static Logger LOG = LoggerFactory.getLogger(EventCollector.class);
	RequestConnection m_requestConnection = null;
	ScheduledExecutorService wuqiSpankHealthCheckerScheduler = null; 
	ScheduledExecutorService exportDirListenerScheduler = null;
	ScheduledExecutorService inTraceReconnectorScheduler = null;
	/**
	 *   F I L T E R
	 */
	ITraceEventParser m_parser = null;
	
	ITraceEvent m_requestStart = null;
	ITraceEvent m_requestCompletion = null;
	private IRequestRepository m_repo = null;
	Callback m_connectionStatusCallback = new Callback();
	
	public EventCollector() throws IntraceException {
		m_parser = org.headlessintrace.client.DefaultFactory.getFactory().getEventParser();
		m_requestStart = m_parser.createEvent("[15:47:00.999]:[203]:javax.servlet.http.HttpServlet:service: {:50", 0);
		m_requestCompletion = m_parser.createEvent("[15:47:00.999]:[203]:javax.servlet.http.HttpServlet:service: }:250", 0);
	}

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
	private static final String DEFAULT_CONTEXT = "wuqiSpank";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LOG.info("wuqiSpank shutdown in progress (contextDestroyed)" );
		//System.out.println("ServletContextListener destroyed");
		wuqiSpankHealthCheckerScheduler.shutdown();
		exportDirListenerScheduler.shutdown();
		inTraceReconnectorScheduler.shutdown();
		DefaultFactory.getFactory().getReconnector().disconnectAll();
	}
	/**
	 * When new files show up in the export-dir, import them.
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 */
	protected void initExportDirListener() throws IOException, ParserConfigurationException {
		GroupNameThreadFactory threadFactory = new GroupNameThreadFactory("wuqiSpankExportDirListener");
		exportDirListenerScheduler = Executors.newScheduledThreadPool(1, threadFactory);
		
		IExportDirListener listener = DefaultFactory.getFactory().getExportDirListener();
		listener.setImportExportMgr(DefaultFactory.getFactory().getImportExportManager());
	    listener.init();

	       /**
	         * Parameters :
	         * 1. Object of Runnable
	         * 2. Initial Delay
	         * 3. Delay between successive execution
	         * 4. Time Unit
	         */
	        exportDirListenerScheduler.scheduleAtFixedRate(
	        		listener, 
	        		1,
	        		DefaultFactory.getFactory().getConfig().getExportDirListenerIntervalInSeconds(), 
	        		TimeUnit.SECONDS);
	}
	protected void initHealthChecks() {
		HealthCheck intraceHealthCheck = DefaultFactory.getFactory().getInTraceHealthCheck();
		DefaultFactory.getFactory().getHealthCheckRegistry().register("wuqiSpankInTraceHealthChecker",intraceHealthCheck); 
		
		DefaultTcpHealthCheck grafanaHealthCheck = DefaultFactory.getFactory().getTcpHealthCheck();
		grafanaHealthCheck.setHost( DefaultFactory.getFactory().getConfig().getGrafanaHost());
		grafanaHealthCheck.setPort( DefaultFactory.getFactory().getConfig().getGrafanaPort() );
		grafanaHealthCheck.setTimeoutInMs(DefaultFactory.getFactory().getConfig().getGrafanaHealthCheckTimeoutInMs() );
		DefaultFactory.getFactory().getHealthCheckRegistry().register("wuqiSpankGrafanaHealthChecker",grafanaHealthCheck); 
		
		GroupNameThreadFactory threadFactory = new GroupNameThreadFactory("wuqiSpankHealthChecker");
		wuqiSpankHealthCheckerScheduler = Executors.newScheduledThreadPool(1, threadFactory);
		
        HealthCheckRegistry registry = DefaultFactory.getFactory().getHealthCheckRegistry();
        Runnable runnable = DefaultFactory.getFactory().getHealthChecker(registry);

        LOG.debug("HealthCheck interval (seconds): [" + DefaultFactory.getFactory().getConfig().getHealthCheckIntervalSeconds() + "]");
       /**
         * Parameters :
         * 1. Object of Runnable
         * 2. Initial Delay
         * 3. Delay between successive execution
         * 4. Time Unit
         */
        wuqiSpankHealthCheckerScheduler.scheduleAtFixedRate(
        		runnable, 
        		1,
        		DefaultFactory.getFactory().getConfig().getHealthCheckIntervalSeconds(), 
        		TimeUnit.SECONDS);
	}
	/**
	 * All connections to the SUT (system under test), even the very first, are made by the DefaultConnector
	 */
	protected void initReconnector() {
		
		
		GroupNameThreadFactory threadFactory = new GroupNameThreadFactory("wuqiSpankReconnector");
		inTraceReconnectorScheduler = Executors.newScheduledThreadPool(1, threadFactory);

		Runnable runnable = DefaultFactory.getFactory().getReconnector();

       /**
         * Parameters :
         * 1. Object of Runnable
         * 2. Initial Delay
         * 3. Delay between successive execution
         * 4. Time Unit
         */
        inTraceReconnectorScheduler.scheduleAtFixedRate(
        		runnable, 
        		1,
        		DefaultFactory.getFactory().getConfig().getReconnectIntervalInSeconds(), 
        		TimeUnit.SECONDS);
 
		
	}

	/**
	 * wuqispank-specific web.xml configuration errors will be thrown from this method.
	 * http://mail-archives.apache.org/mod_mbox/tomcat-users/200404.mbox/%3C9C5166762F311146951505C6790A9CF8013E01A8@US-VS1.corp.mpi.com%3E
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		DefaultFactory.getFactory().getRequestManager().registerListener(this);
		LOG.debug("Entering contextInitialized");
		
		//System.out.println("!!Entering contextInitialized");
		
		IConfig config = new WebXmlConfigImpl(servletContextEvent.getServletContext());
		DefaultFactory.getFactory().setConfig(config);//make this available GLOBALLY to the rest of the program.
		m_repo = DefaultFactory.getFactory().createRepo();

		 IImportExportMgr iem = DefaultFactory.getFactory().getImportExportManager();
		 iem.setRepo( getRepo() );
		 iem.setExportDir( DefaultFactory.getFactory().getConfig().getExportDir() );
		
		try {
			m_requestConnection = getRequestConnection();
			
			DefaultConnectionList.getSingleton().add(config.getInTraceAgent(), m_requestConnection);
			
			servletContextEvent.getServletContext().setAttribute(WUQISPANK_REPO, this);
			LOG.debug("Just called ServletContext#setAttribute(" + this.WUQISPANK_REPO + "," + this.hashCode() + ")");
			//LOG.debug("WuqispankApp.getRepo() [" + WuqispankApp.getRepo().hashCode() + "]");
			
			this.initReconnector();
			this.initExportDirListener();
			this.initHealthChecks();
			
		} catch (IntraceException e) {
			e.printStackTrace();
		} catch (WuqispankException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			 
			 iem.importAtSystemStartup();
			
		} catch (Exception e) {
			LOG.error( DefaultFactory.getFactory().getMessages().getImportExportInitError() );
			e.printStackTrace();
		}
		
		System.out.println(DefaultFactory.getFactory().getMessages().getStartupBanner1());
		System.out.println(DefaultFactory.getFactory().getMessages().getStartupBanner2(config.getExportDir()));
		System.out.println(DefaultFactory.getFactory().getMessages().getStartupBanner1());
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
			output.append(className.trim());
		}
		for(String className : provider.getStatementPackageAndClass() ) {
			output.append("|"); // the use of | is documented here: https://github.com/mchr3k/org.intrace/issues/21
			output.append(className.trim());
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
				LOG.debug("Adding full request with event count ["  + request.getEvents().size() + "]");
				requestWrapper.setRequest(request);
				DefaultFactory.getFactory().getRequestManager().add(requestWrapper);
			} catch (WuqispankException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} //else there was a round trip to the SUT without SQL events.
		
	}
	
	@Override
	public void add(IRequestWrapper requestWrapper) throws WuqispankException {
		getRepo().add(requestWrapper);
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
}

