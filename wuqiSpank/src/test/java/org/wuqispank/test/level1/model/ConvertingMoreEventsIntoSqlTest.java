package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.intrace.client.IntraceException;
import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.jdbc.DefaultJdbcSqlWrapperFactory;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ISqlWrapperFactory;
import org.wuqispank.model.ITable;

/**
 * TODO:  currently, this test starts with data from hsqldb events.....need to support other DB's
 * @author erikostermueller
 *
 */
public class ConvertingMoreEventsIntoSqlTest {
	ITraceEventParser m_eventParser = org.intrace.client.DefaultFactory.getFactory().getEventParser();
	List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
	ITraceEvent e_entry = null;
	ITraceEvent e_arg = null;
	ITraceEvent e_exit = null;
	ITraceEvent e_exit_other = null;
	
	String[] m_oneRequest = {
		 "[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38"
		,"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?"   
		,"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003"
		,"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007"
		,"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2"
		,"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?"   
		,"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41"
	};
	String[] m_embeddedAndUnwantedEvents = {
			"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select         BILLADDR1, BILLADDR2, BILLCITY, BILLCOUNTRY, BILLSTATE,         BILLTOFIRSTNAME, BILLTOLASTNAME, BILLZIP, SHIPADDR1,         SHIPADDR2, SHIPCITY, SHIPCOUNTRY, SHIPSTATE, SHIPTOFIRSTNAME,         SHIPTOLASTNAME, SHIPZIP, CARDTYPE, COURIER, CREDITCARD,         EXPRDATE, LOCALE, ORDERDATE, ORDERS.ORDERID, TOTALPRICE,         USERID, STATUS     from ORDERS, ORDERSTATUS where ORDERS.ORDERID = ? and ORDERS.ORDERID = ORDERSTATUS.ORDERID"   
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: {:28"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (connection): org.postgresql.jdbc4.Jdbc4Connection@2276517f"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (sql):      select         BILLADDR1, BILLADDR2, BILLCITY, BILLCOUNTRY, BILLSTATE,         BILLTOFIRSTNAME, BILLTOLASTNAME, BILLZIP, SHIPADDR1,         SHIPADDR2, SHIPCITY, SHIPCOUNTRY, SHIPSTATE, SHIPTOFIRSTNAME,         SHIPTOLASTNAME, SHIPZIP, CARDTYPE, COURIER, CREDITCARD,         EXPRDATE, LOCALE, ORDERDATE, ORDERS.ORDERID, TOTALPRICE,         USERID, STATUS     from ORDERS, ORDERSTATUS where ORDERS.ORDERID = ? and ORDERS.ORDERID = ORDERSTATUS.ORDERID"   
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (isCallable): false"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (rsType): 1003"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (rsConcurrency): 1007"
				,"[12:46:56.741]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: Arg (rsHoldability): 2"
				,"[12:46:56.742]:[46]:org.postgresql.jdbc4.Jdbc4Statement:<init>: }:29~org.postgresql.jdbc4.Jdbc4Statement.<init>(Jdbc4Statement.java:29),org.postgresql.jdbc4.Jdbc4PreparedStatement.<init>(Jdbc4PreparedStatement.java:21),org.postgresql.jdbc4.Jdbc4PreparedStatement.<init>(Jdbc4PreparedStatement.java:16),org.postgresql.jdbc4.Jdbc4Connection.prepareStatement(Jdbc4Connection.java:39),org.postgresql.jdbc3.AbstractJdbc3Connection.prepareStatement(AbstractJdbc3Connection.java:274),org.postgresql.jdbc2.AbstractJdbc2Connection.prepareStatement(AbstractJdbc2Connection.java:301),org.apache.tomcat.dbcp.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),org.apache.tomcat.dbcp.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),com.ibatis.sqlmap.engine.execution.SqlExecutor.executeQuery(SqlExecutor.java:118),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.sqlExecuteQuery(GeneralStatement.java:174),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.executeQueryWithCallback(GeneralStatement.java:142),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.executeQueryForObject(GeneralStatement.java:84),com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate.queryForObject(SqlMapExecutorDelegate.java:312),com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate.queryForObject(SqlMapExecutorDelegate.java:297),com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl.queryForObject(SqlMapSessionImpl.java:69),com.ibatis.sqlmap.engine.impl.SqlMapClientImpl.queryForObject(SqlMapClientImpl.java:46),com.ibatis.dao.client.template.SqlMapDaoTemplate.queryForObject(SqlMapDaoTemplate.java:151),com.ibatis.jpetstore.persistence.sqlmapdao.OrderSqlMapDao.getOrder(OrderSqlMapDao.java:27),sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method),sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.lang.reflect.Method.invoke(Method.java:606),com.ibatis.dao.engine.impl.DaoProxy.invoke(DaoProxy.java:53),com.sun.proxy.$Proxy9.getOrder(Unknown Source),com.ibatis.jpetstore.service.OrderService.getOrder(OrderService.java:72),com.ibatis.jpetstore.presentation.OrderBean.viewOrder(OrderBean.java:169),sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method),sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.lang.reflect.Method.invoke(Method.java:606),com.ibatis.struts.BeanAction.execute(BeanAction.java:138),org.apache.struts.action.RequestProcessor.processActionPerform(RequestProcessor.java:484),org.apache.struts.action.RequestProcessor.process(RequestProcessor.java:274),org.apache.struts.action.ActionServlet.process(ActionServlet.java:1482),org.apache.struts.action.ActionServlet.doGet(ActionServlet.java:507),javax.servlet.http.HttpServlet.service(HttpServlet.java:621),javax.servlet.http.HttpServlet.service(HttpServlet.java:728),org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305),org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210),org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222),org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123),org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472),org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171),org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99),org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118),org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:407),org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1004),org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589),org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:310),java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145),java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)"
				,"[12:46:56.742]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select         BILLADDR1, BILLADDR2, BILLCITY, BILLCOUNTRY, BILLSTATE,         BILLTOFIRSTNAME, BILLTOLASTNAME, BILLZIP, SHIPADDR1,         SHIPADDR2, SHIPCITY, SHIPCOUNTRY, SHIPSTATE, SHIPTOFIRSTNAME,         SHIPTOLASTNAME, SHIPZIP, CARDTYPE, COURIER, CREDITCARD,         EXPRDATE, LOCALE, ORDERDATE, ORDERS.ORDERID, TOTALPRICE,         USERID, STATUS     from ORDERS, ORDERSTATUS where ORDERS.ORDERID = ? and ORDERS.ORDERID = ORDERSTATUS.ORDERID   "
				,"[12:46:56.742]:[46]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41~org.postgresql.jdbc4.Jdbc4Connection.prepareStatement(Jdbc4Connection.java:41),org.postgresql.jdbc3.AbstractJdbc3Connection.prepareStatement(AbstractJdbc3Connection.java:274),org.postgresql.jdbc2.AbstractJdbc2Connection.prepareStatement(AbstractJdbc2Connection.java:301),org.apache.tomcat.dbcp.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),org.apache.tomcat.dbcp.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),com.ibatis.sqlmap.engine.execution.SqlExecutor.executeQuery(SqlExecutor.java:118),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.sqlExecuteQuery(GeneralStatement.java:174),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.executeQueryWithCallback(GeneralStatement.java:142),com.ibatis.sqlmap.engine.mapping.statement.GeneralStatement.executeQueryForObject(GeneralStatement.java:84),com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate.queryForObject(SqlMapExecutorDelegate.java:312),com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate.queryForObject(SqlMapExecutorDelegate.java:297),com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl.queryForObject(SqlMapSessionImpl.java:69),com.ibatis.sqlmap.engine.impl.SqlMapClientImpl.queryForObject(SqlMapClientImpl.java:46),com.ibatis.dao.client.template.SqlMapDaoTemplate.queryForObject(SqlMapDaoTemplate.java:151),com.ibatis.jpetstore.persistence.sqlmapdao.OrderSqlMapDao.getOrder(OrderSqlMapDao.java:27),sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method),sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.lang.reflect.Method.invoke(Method.java:606),com.ibatis.dao.engine.impl.DaoProxy.invoke(DaoProxy.java:53),com.sun.proxy.$Proxy9.getOrder(Unknown Source),com.ibatis.jpetstore.service.OrderService.getOrder(OrderService.java:72),com.ibatis.jpetstore.presentation.OrderBean.viewOrder(OrderBean.java:169),sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method),sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.lang.reflect.Method.invoke(Method.java:606),com.ibatis.struts.BeanAction.execute(BeanAction.java:138),org.apache.struts.action.RequestProcessor.processActionPerform(RequestProcessor.java:484),org.apache.struts.action.RequestProcessor.process(RequestProcessor.java:274),org.apache.struts.action.ActionServlet.process(ActionServlet.java:1482),org.apache.struts.action.ActionServlet.doGet(ActionServlet.java:507),javax.servlet.http.HttpServlet.service(HttpServlet.java:621),javax.servlet.http.HttpServlet.service(HttpServlet.java:728),org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305),org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210),org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222),org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123),org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472),org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171),org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99),org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118),org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:407),org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1004),org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589),org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:310),java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145),java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)"
			
	};
	
	private List<ITraceEvent> getTestEvents(String[] myInputEventStrings) throws IntraceException {
		List<ITraceEvent> myEvents = new ArrayList<ITraceEvent>();

		for(String eventString : myInputEventStrings)
			myEvents.add( createEvent(eventString) );
		
		return myEvents;
	}
	private ITraceEvent createEvent(String eventString) throws IntraceException {
		
		return m_eventParser.createEvent(eventString, 0);
	}
	
	@Before
	public void setup() throws IntraceException {
		
	}
	private org.wuqispank.web.IFactory m_wuqiSpankFactory = org.wuqispank.DefaultFactory.getFactory();
	
	@Test
	public void canAssembleEventsIntoRequest() throws WuqispankException, IntraceException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.intrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( getTestEvents(m_oneRequest) );
		try {
			requestWrapper.setRequest(request);
		} catch(Exception e) {
			fail("Should not receive exception [" + e.toString() + "]");
		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created just a single SQL statement", 1,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't correctly find the event with the SQL statement", "select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?", sqlWrapper.getSqlText() );
		
	}
	
	@Test
	public void canCopeWithEmbeddedEvents() throws WuqispankException, IntraceException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.intrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( getTestEvents(this.m_embeddedAndUnwantedEvents) );
		requestWrapper.setRequest(request);
		
//		try {
//			requestWrapper.setRequest(request);
//		} catch(Exception e) {
//			fail("Should not receive exception [" + e.toString() + "]");
//		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created just a single SQL statement", 1,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't correctly find the event with the SQL statement", "select         BILLADDR1, BILLADDR2, BILLCITY, BILLCOUNTRY, BILLSTATE,         BILLTOFIRSTNAME, BILLTOLASTNAME, BILLZIP, SHIPADDR1,         SHIPADDR2, SHIPCITY, SHIPCOUNTRY, SHIPSTATE, SHIPTOFIRSTNAME,         SHIPTOLASTNAME, SHIPZIP, CARDTYPE, COURIER, CREDITCARD,         EXPRDATE, LOCALE, ORDERDATE, ORDERS.ORDERID, TOTALPRICE,         USERID, STATUS     from ORDERS, ORDERSTATUS where ORDERS.ORDERID = ? and ORDERS.ORDERID = ORDERSTATUS.ORDERID", sqlWrapper.getSqlText() );
		
	}
	
	
}
