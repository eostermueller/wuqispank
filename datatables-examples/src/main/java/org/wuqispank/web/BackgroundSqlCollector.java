package org.wuqispank.web;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.intrace.client.IntraceException;
import org.intrace.client.connection.ConnectState;
import org.intrace.client.connection.ConnectionException;
import org.intrace.client.connection.ConnectionTimeout;
import org.intrace.client.connection.DefaultConnectionList;
import org.intrace.client.connection.HostPort;
import org.intrace.client.connection.IConnection;
import org.intrace.client.connection.IConnectionList;
import org.intrace.client.connection.Callback;
import org.intrace.client.connection.command.ClassInstrumentationCommand;
import org.intrace.client.connection.command.IAgentCommand;
import org.intrace.client.filter.IncludeAnyOfTheseEventsFilterExt;
import org.intrace.client.model.FixedLengthQueue;
import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.BadCompletedRequestListener;
import org.intrace.client.request.DefaultRequestSeparator;
import org.intrace.client.request.ICompletedRequestCallback;
import org.intrace.client.request.IRequest;
import org.intrace.client.request.RequestConnection;
import org.intrace.client.request.RequestWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.IRequestExporter;
import org.wuqispank.IRequestImporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.web.msgs.IMessages;
import org.xml.sax.SAXException;


public class BackgroundSqlCollector implements ServletContextListener, ICompletedRequestCallback {
	private IRequestRepository m_repo = DefaultFactory.getFactory().createRepo();
	static Logger LOG = LoggerFactory.getLogger(BackgroundSqlCollector.class);
	Callback testCallback = null;
	public IRequestRepository getRepo() {
		return m_repo;
	}
	public IRequestWrapper getRequest(String uniqueIdCriteria) {
		IRequestWrapper rc = getRepo().get(uniqueIdCriteria);
		LOG.debug("getRequest(): [" + rc + "]");
		return rc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4953960682833247515L;
	public static final String WUQISPANK_REPO = "org.wuqispankSingletonInMemoryRepository";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed");
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
	
	protected void loadExportedRequests() throws ParserConfigurationException, SAXException, IOException, WuqispankException {
		IConfig config = DefaultFactory.getFactory().getConfig();
		
		if (config.getExportDir() !=null && config.getExportDir().exists()) {
			IRequestImporter importer = DefaultFactory.getFactory().getRequestImporter();
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
		
	}
	/**
	 * wuqispank-specific web.xml configuration errors will be thrown from this method.
	 * http://mail-archives.apache.org/mod_mbox/tomcat-users/200404.mbox/%3C9C5166762F311146951505C6790A9CF8013E01A8@US-VS1.corp.mpi.com%3E
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		LOG.debug("Entering contextInitialized");
		
		IConfig config = new WebXmlConfigImpl(servletContextEvent.getServletContext());
		DefaultFactory.getFactory().setConfig(config);//make this available GLOBALLY to the rest of the program.
		
		try {
			loadExportedRequests();
		} catch (Exception e) {
			LOG.error("Error importing exported requests");
			e.printStackTrace();
			if (e instanceof WuqispankException) {
				WuqispankException we = (WuqispankException) e;
				Throwable trigger = we.getCause();
				LOG.error("Cause:");
				trigger.printStackTrace();
			}
		}
		
		//m_requestQueue = new FixedLengthQueue<IRequestWrapper>(config.getCircularBufferSize());
		//m_requestQueue = new ConcurrentLinkedDeque<IRequestWrapper>();
		
		ClassInstrumentationCommand cic = new ClassInstrumentationCommand();
		IAgentCommand commandArray[] = { };
		
		/**
		 *   F I L T E R
		 */
		ITraceEventParser parser = org.intrace.client.DefaultFactory.getFactory().getEventParser();
		List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
		try {
			ITraceEvent t0 = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
			ITraceEvent t1 = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: INSERT INTO Location (name, address) VALUES(?, ?)", 0);
			ITraceEvent t2 = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: }", 0);
			ITraceEvent t3_requestCompletion = parser.createEvent("[15:47:00.999]:[203]:example.webapp.servlet.HelloWorld:doGet: }:50", 0);
			myCriteriaList.add(t0);myCriteriaList.add(t1);myCriteriaList.add(t2);myCriteriaList.add(t3_requestCompletion);
			IncludeAnyOfTheseEventsFilterExt filter = new IncludeAnyOfTheseEventsFilterExt(); 
			filter.setFilterCriteria(myCriteriaList);

			/**
			 *  C O N N E C T
			 */
			//
			RequestConnection requestConnection = new RequestConnection(1000);//but we'll override this 1000 in a minute, hold on.
			LOG.debug("New RequestConnection[" + requestConnection.hashCode() + "]");
			requestConnection.setCompletedRequestCallback(this);
			
			testCallback = new Callback();
			requestConnection.addCallback(testCallback);
			requestConnection.getTraceWriter().setTraceFilterExt(filter);
			requestConnection.setRequestCompletionEvent(t3_requestCompletion);
			
			//boolean rc = requestConnection.connect(config.getInTraceAgent(), commandArray);
			boolean rc = requestConnection.connect(config.getInTraceAgent(), commandArray);
			LOG.debug("New RequestConnection[" + requestConnection.hashCode() + "] success [" + rc + "] callback [" + testCallback.getConnectState().toString() + "] msgs [" + testCallback.getMessages().toString() + "]");
			
			if (rc) {
				//DefaultConnectionList.getSingleton().add(config.getInTraceAgent(), myCast);
				DefaultConnectionList.getSingleton().add(config.getInTraceAgent(), requestConnection);
			} else {
				LOG.error( DefaultFactory.getFactory().getMessages().getConnectionError(config.getInTraceAgent()) );
			}
			
			servletContextEvent.getServletContext().setAttribute(WUQISPANK_REPO, this);
		} catch (ConnectionTimeout e) {
			// 
			e.printStackTrace();
		} catch (IntraceException e) {
			// 
			e.printStackTrace();
		} catch (ConnectionException e) {
			// 
			e.printStackTrace();
		} catch (BadCompletedRequestListener e) {
			// 
			e.printStackTrace();
		}
		
	}
	
	/**
	 * After a web request is completed, this method will be invoked
	 * and we'll stored the data (all the SQL statements) from the request.
	 */
	@Override
	public void requestCompleted(IRequest request) {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		
		try {
			requestWrapper.setRequest(request);
			getRepo().add(requestWrapper);
		} catch (WuqispankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
