package org.wuqispank.test.level1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.intrace.client.IntraceException;
import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.IRequest;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wuqispank.DefaultFactory;
import org.wuqispank.IRequestExporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.xml.sax.SAXException;

public class TestExporter {

	private static final String EXPORT_ROOT_TAG_NAME = "WuqispankExport";
	private static final String EXPORT_SINGLE_REQUEST_TAG_NAME = "Rq";
	private static final String EXPORT_STACK_TRACE_TAG_NAME = "StackTrace";
	private static final String EXPORT_ID_TAG_NAME = "id";
	private static final String EXPORT_SQL_TAG_NAME = "Sql";
	private static final String EXPORT_SQL_STATEMENT_TAG_NAME = "StmtText";
	private static final String EXPORT_DATE_TIME_ATTR_NAME="lousyDateTimeMs";
	private static final String EXPECTED_SQL = "INSERT INTO Location (name, address) VALUES(?, ?)";
	private static final String EXPORT_THREAD_ID_TAG_NAME = "threadId";

	@Test 
	public void canSerializeRequestToXml() throws WuqispankException, ParserConfigurationException, SAXException, IOException, TransformerException, IntraceException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IRequestExporter re = DefaultFactory.getFactory().getRequestExporter();
		re.setOutputStream(baos);
		re.export( getRequestWrapper("a","05") );

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(baos.toByteArray()));
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		assertEquals("Root tag name of exported file is wrong", EXPORT_ROOT_TAG_NAME, root.getNodeName());
		NodeList nList = root.getElementsByTagName(EXPORT_SINGLE_REQUEST_TAG_NAME);
		assertEquals("didn't get the right number of [" + EXPORT_SINGLE_REQUEST_TAG_NAME + "] nodes",1,nList.getLength());
		 
		Node nNode = nList.item(0);
 
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			assertEquals(
					"couldn't find uniqueId for request", "a",
					eElement.getAttribute(EXPORT_ID_TAG_NAME) );

			assertEquals(
					"couldn't find threadId for request", "myThreadId",
					eElement.getAttribute(EXPORT_THREAD_ID_TAG_NAME) );

			NodeList eventList = eElement.getElementsByTagName(EXPORT_SQL_TAG_NAME);
			assertEquals("didn't get the right number of [" + EXPORT_SQL_TAG_NAME + "] nodes",1,eventList.getLength());
			
			validateSql(eventList);
			
			
		} else
			fail("Did not find [" + EXPORT_SINGLE_REQUEST_TAG_NAME + "]");
		
	}
	
	private void validateSql(NodeList eventList) {
		
		for(int i = 0; i< eventList.getLength();i++) {
			Node eventNode = eventList.item(i);
			if (eventNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventNode;

				assertEquals(
						"couldn't find stack trace in exported xml", Arrays.toString(m_expectedStackTraceElements),
						eElement.getElementsByTagName(EXPORT_STACK_TRACE_TAG_NAME).item(0).getTextContent() );
				
				assertEquals(
						"couldn't find sql statement in exported xml", EXPECTED_SQL,
						eElement.getElementsByTagName(EXPORT_SQL_STATEMENT_TAG_NAME).item(0).getTextContent() );
				
				/**
				 * Any valid date must be larger/newer than  
				 * this arbitrary one in the past: March 25, 2014.
				 */
				long newlyCreatedDateTimeStamp = Long.parseLong(eElement.getAttribute(EXPORT_DATE_TIME_ATTR_NAME));
				assertTrue(
						"couldn't find dateTime for request", 
						(newlyCreatedDateTimeStamp > 1395743319411L) );
				
				
			}
		}
			
		
	}

	//@Test
	public void canSerializeRepoToXml() throws WuqispankException, IntraceException {
		
		IRequestWrapper rw1 = getRequestWrapper("a","05");
		IRequestWrapper rw2 = getRequestWrapper("b","06");
		
		IRequestRepository m_repo = DefaultFactory.getFactory().createRepo();
		m_repo.add(rw1);
		m_repo.add(rw2);
		
	}
	
	
	private IRequestWrapper getRequestWrapper(String uniqueId, String second) throws WuqispankException, IntraceException {
		IRequestWrapper rw = DefaultFactory.getFactory().getRequestWrapper();
		
		IRequest req = org.intrace.client.DefaultFactory.getFactory().getRequest();
		req.setThreadId("myThreadId");
		req.setUniqueId(uniqueId);
		ITraceEventParser parser = org.intrace.client.DefaultFactory.getFactory().getEventParser();
		List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
		ITraceEvent t0 = parser.createEvent("[15:41:" + second + ".294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
		ITraceEvent t1 = parser.createEvent("[15:41:" + second + ".532]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: " + EXPECTED_SQL, 0);
		ITraceEvent t2 = parser.createEvent("[15:41:" + second + ".639]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: }~org.hsqldb.jdbc.jdbcConnection.getAutoCommit(Unknown Source),org.apache.commons.dbcp.DelegatingConnection.getAutoCommit(DelegatingConnection.java:337),org.apache.commons.dbcp.PoolableConnectionFactory.passivateObject(PoolableConnectionFactory.java:688),org.apache.commons.pool.impl.GenericObjectPool.addObjectToPool(GenericObjectPool.java:1379),org.apache.commons.pool.impl.GenericObjectPool.returnObject(GenericObjectPool.java:1342),org.apache.commons.dbcp.PoolableConnection.close(PoolableConnection.java:90),org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.close(PoolingDataSource.java:191),org.springframework.jdbc.datasource.DataSourceUtils.doReleaseConnection(DataSourceUtils.java:333),org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection(DataSourceUtils.java:294),org.springframework.jdbc.datasource.DataSourceTransactionManager.doCleanupAfterCompletion(DataSourceTransactionManager.java:324),org.springframework.transaction.support.AbstractPlatformTransactionManager.cleanupAfterCompletion(AbstractPlatformTransactionManager.java:1011),org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:804),org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:723),org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:393),org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:120),org.springpringframework.jdbc.datasource.DataSourceTransactionManager.doCleanupAfterCompletion(DataSourceTransactionManager.java:324),org.springframework.transaction.support.AbstractPlatformTransactionManager.cleanupAfterCompletion(AbstractPlatformTransactionManager.java:1011),org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:804),org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:723),org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:393),org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:120),org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:172),org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:202),$Proxy6.save(Unknown Source),example.webapp.servlet.HelloWorld.doGet(HelloWorld.java:34),javax.servlet.http.HttpServlet.service(HttpServlet.java:668),javax.servlet.http.HttpServlet.service(HttpServlet.java:770),org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:669),org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:455),org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:137),org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:560),org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:231),org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1072),org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:382),org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:193),org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1006),org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:135),org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:116),org.eclipse.jetty.server.Server.handle(Server.java:365),org.eclipse.jetty.server.AbstractHttpConnection.handleRequest(AbstractHttpConnection.java:485),org.eclipse.jetty.server.BlockingHttpConnection.handleRequest(BlockingHttpConnection.java:53),org.eclipse.jetty.server.AbstractHttpConnection.headerComplete(AbstractHttpConnection.java:926),org.eclipse.jetty.server.AbstractHttpConnection$RequestHandler.headerComplete(AbstractHttpConnection.java:988),org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:635),org.eclipse.jetty.http.HttpParser.parseAvailable(HttpParser.java:235),org.eclipse.jetty.server.BlockingHttpConnection.handle(BlockingHttpConnection.java:72),org.eclipse.jetty.server.bio.SocketConnector$ConnectorEndPoint.run(SocketConnector.java:264),org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:608),org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)",0);
		ITraceEvent t3_requestCompletion = parser.createEvent("[15:41:" + second + ".999]:[203]:example.webapp.servlet.HelloWorld:doGet: }:50", 0);
		myCriteriaList.add(t0);myCriteriaList.add(t1);myCriteriaList.add(t2);myCriteriaList.add(t3_requestCompletion);
		
		req.setEvents(myCriteriaList);
		rw.setRequest(req);
		
		return rw;
	}
	private String[] m_expectedStackTraceElements = {
			"org.hsqldb.jdbc.jdbcConnection.getAutoCommit(Unknown Source)",
			"org.apache.commons.dbcp.DelegatingConnection.getAutoCommit(DelegatingConnection.java:337)",
			"org.apache.commons.dbcp.PoolableConnectionFactory.passivateObject(PoolableConnectionFactory.java:688)",
			"org.apache.commons.pool.impl.GenericObjectPool.addObjectToPool(GenericObjectPool.java:1379)",
			"org.apache.commons.pool.impl.GenericObjectPool.returnObject(GenericObjectPool.java:1342)",
			"org.apache.commons.dbcp.PoolableConnection.close(PoolableConnection.java:90)",
			"org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.close(PoolingDataSource.java:191)",
			"org.springframework.jdbc.datasource.DataSourceUtils.doReleaseConnection(DataSourceUtils.java:333)",
			"org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection(DataSourceUtils.java:294)",
			"org.springframework.jdbc.datasource.DataSourceTransactionManager.doCleanupAfterCompletion(DataSourceTransactionManager.java:324)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.cleanupAfterCompletion(AbstractPlatformTransactionManager.java:1011)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:804)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:723)",
			"org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:393)",
			"org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:120)",
			"org.springpringframework.jdbc.datasource.DataSourceTransactionManager.doCleanupAfterCompletion(DataSourceTransactionManager.java:324)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.cleanupAfterCompletion(AbstractPlatformTransactionManager.java:1011)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:804)",
			"org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:723)",
			"org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:393)",
			"org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:120)",
			"org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:172)",
			"org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:202)",
			"$Proxy6.save(Unknown Source)",
			"example.webapp.servlet.HelloWorld.doGet(HelloWorld.java:34)",
			"javax.servlet.http.HttpServlet.service(HttpServlet.java:668)",
			"javax.servlet.http.HttpServlet.service(HttpServlet.java:770)",
			"org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:669)",
			"org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:455)",
			"org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:137)",
			"org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:560)",
			"org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:231)",
			"org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1072)",
			"org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:382)",
			"org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:193)",
			"org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1006)",
			"org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:135)",
			"org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:116)",
			"org.eclipse.jetty.server.Server.handle(Server.java:365)",
			"org.eclipse.jetty.server.AbstractHttpConnection.handleRequest(AbstractHttpConnection.java:485)",
			"org.eclipse.jetty.server.BlockingHttpConnection.handleRequest(BlockingHttpConnection.java:53)",
			"org.eclipse.jetty.server.AbstractHttpConnection.headerComplete(AbstractHttpConnection.java:926)",
			"org.eclipse.jetty.server.AbstractHttpConnection$RequestHandler.headerComplete(AbstractHttpConnection.java:988)",
			"org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:635)",
			"org.eclipse.jetty.http.HttpParser.parseAvailable(HttpParser.java:235)",
			"org.eclipse.jetty.server.BlockingHttpConnection.handle(BlockingHttpConnection.java:72)",
			"org.eclipse.jetty.server.bio.SocketConnector$ConnectorEndPoint.run(SocketConnector.java:264)",
			"org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:608)",
			"org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)"			
	};

}
