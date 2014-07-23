package org.wuqispank.test.level1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.intrace.client.DefaultFactory;
import org.intrace.client.IntraceException;
import org.intrace.client.filter.ContiguousEventFilter;
import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEvent.EventType;
import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.ICompletedRequestCallback;
import org.intrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;

public class ContiguousEventFilterTest {
	private IRequest m_outputRequest1 = null;
	private IRequest m_inputRequest1 = null;

	private IRequest m_outputRequest2 = null;
	private IRequest m_inputRequest2 = null;

	private IRequest m_outputRequest3 = null;
	private IRequest m_inputRequest3 = null;

	private IRequest m_outputRequest4 = null;
	private IRequest m_inputRequest4 = null;

	private IRequest m_outputRequest5 = null;
	private IRequest m_inputRequest5 = null;

	private IRequest m_outputRequest6 = null;
	private IRequest m_inputRequest6 = null;
	
	private IRequest m_outputRequest7 = null;
	private IRequest m_inputRequest7 = null;

	private ITraceEventParser m_eventParser;

	@Before 
	public void setup() throws IntraceException {
		m_eventParser = DefaultFactory.getFactory().getEventParser();
		m_inputRequest1 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest1.setEvents(getTestEvents(m_myInputEventStrings));
		
		m_inputRequest2 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest2.setEvents(getTestEvents(m_myInputEventStringsWithExtraEvents));

		m_inputRequest3 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest3.setEvents(getTestEvents(m_multipleArguments));
		
		m_inputRequest4 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest4.setEvents(getTestEvents(m_multipleRequests));

		m_inputRequest5 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest5.setEvents(getTestEvents(m_nothingToDelete));
		
		m_inputRequest6 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest6.setEvents(getTestEvents(m_embeddedAndUnwantedEvents));
		
		m_inputRequest7 = DefaultFactory.getFactory().getRequest();		
		m_inputRequest7.setEvents(getTestEvents(m_simpleOverride));
		
		
	}
	@Test
	public void canFilterOutJdbcWrapperClasses() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest1 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("executeQuery");
		
		
		contiguousEventFilter.requestCompleted(m_inputRequest1);
		
		assertNotNull(m_outputRequest1);
		assertEquals("Didn't filter out the right number of events.", 4, m_outputRequest1.getEvents().size());
		
		for(ITraceEvent e : m_outputRequest1.getEvents()) {
			assertEquals("executeQuery", e.getMethodName());
			assertEquals("jdbcStatement", e.getClassName() );
			assertEquals("org.hsqldb.jdbc", e.getPackageName() );
				
		}
		assertEquals(EventType.ENTRY, m_outputRequest1.getEvents().get(0).getEventType());
		assertEquals(EventType.ARG, m_outputRequest1.getEvents().get(1).getEventType());
		assertEquals(EventType.RETURN, m_outputRequest1.getEvents().get(2).getEventType());
		assertEquals(EventType.EXIT, m_outputRequest1.getEvents().get(3).getEventType());
		
	}
	
	@Test
	public void canFilterOutJdbcWrapperClassesWithExtraneousEvents() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest2 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("executeQuery");
		
		contiguousEventFilter.requestCompleted(m_inputRequest2);
		
		assertNotNull(m_outputRequest2);
		assertEquals("Didn't filter out the right number of events", 4, m_outputRequest2.getEvents().size());
		
		int count = 0;
		for(ITraceEvent e : m_outputRequest2.getEvents()) {
			assertEquals("executeQuery", e.getMethodName());
			assertEquals("jdbcStatement", e.getClassName() );
			assertEquals("org.hsqldb.jdbc", e.getPackageName() );
		}
		
		assertEquals(EventType.ENTRY, m_outputRequest2.getEvents().get(0).getEventType());
		assertEquals(EventType.ARG, m_outputRequest2.getEvents().get(1).getEventType());
		assertEquals(EventType.RETURN, m_outputRequest2.getEvents().get(2).getEventType());
		assertEquals(EventType.EXIT, m_outputRequest2.getEvents().get(3).getEventType());
		
	}
	@Test
	public void canFilterOutJdbcWrapperClassesWithMultipleArguments() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest3 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("prepareStatement");
		
		contiguousEventFilter.requestCompleted(m_inputRequest3);
		
		assertNotNull(m_outputRequest3);
		assertEquals("Didn't filter out the right number of events", 7, m_outputRequest3.getEvents().size());
		
		for(ITraceEvent e : m_outputRequest3.getEvents()) {
			assertEquals("prepareStatement", e.getMethodName());
			assertEquals("Jdbc4Connection", e.getClassName() );
			assertEquals("org.postgresql.jdbc4", e.getPackageName() );
		}
		
		assertEquals(EventType.ENTRY, m_outputRequest3.getEvents().get(0).getEventType());
		assertEquals(EventType.ARG, m_outputRequest3.getEvents().get(1).getEventType());
		assertEquals(EventType.ARG, m_outputRequest3.getEvents().get(2).getEventType());
		assertEquals(EventType.ARG, m_outputRequest3.getEvents().get(3).getEventType());
		assertEquals(EventType.ARG, m_outputRequest3.getEvents().get(4).getEventType());
		assertEquals(EventType.RETURN, m_outputRequest3.getEvents().get(5).getEventType());
		assertEquals(EventType.EXIT, m_outputRequest3.getEvents().get(6).getEventType());
		
	}
	@Test
	public void canFilterOutJdbcWrapperClassesWithMultipleRequests() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest4 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("prepareStatement");
		contiguousEventFilter_x.keepMethod("executeQuery");
		
		
		contiguousEventFilter.requestCompleted(m_inputRequest4);
		
		assertNotNull(m_outputRequest4);
		assertEquals("Didn't filter out the right number of events", 18, m_outputRequest4.getEvents().size());
		
		
	}
	@Test
	public void canFilterOutNothing() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest5 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("prepareStatement");
		contiguousEventFilter_x.keepMethod("executeQuery");
		
		
		contiguousEventFilter.requestCompleted(m_inputRequest5);
		
		assertNotNull(m_outputRequest5);
		assertEquals("Didn't filter out the right number of events", 21, m_outputRequest5.getEvents().size());
		
		
	}
	@Test
	public void canDealWitEmbeddedElements() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest6 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("prepareStatement");
		contiguousEventFilter_x.keepMethod("executeQuery");
		
		
		contiguousEventFilter.requestCompleted(m_inputRequest6);
		
		assertNotNull(m_outputRequest6);
		assertEquals("Didn't filter out the right number of events", 15, m_outputRequest6.getEvents().size());
		//assertEquals("Didn't filter out the right number of events", 21, m_outputRequest6.getEvents().size());
		
		
	}
	/**
	 * ,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Arg: -1"
	 */
	@Test
	public void canFilterOutOverriddenMethod() {
		
		ICompletedRequestCallback testCallback = new ICompletedRequestCallback() {
			@Override
			public void requestCompleted(IRequest val) {
				m_outputRequest7 = val;
			}
		};
		
		ICompletedRequestCallback contiguousEventFilter = new ContiguousEventFilter(testCallback);
		ContiguousEventFilter contiguousEventFilter_x = (ContiguousEventFilter) contiguousEventFilter;
		contiguousEventFilter_x.keepMethod("prepareStatement");
		
		
		contiguousEventFilter.requestCompleted(m_inputRequest7);
		
		assertNotNull(m_outputRequest7);
		assertEquals("Didn't filter out the right number of events.", 6, m_outputRequest7.getEvents().size());
		
		for(ITraceEvent e : m_outputRequest7.getEvents()) {
			assertEquals("prepareStatement", e.getMethodName());
			assertEquals("PhysicalConnection", e.getClassName() );
			assertEquals("oracle.jdbc.driver", e.getPackageName() );
				
		}
		assertEquals(EventType.ENTRY, m_outputRequest7.getEvents().get(0).getEventType());
		assertEquals(EventType.ARG, m_outputRequest7.getEvents().get(1).getEventType());
		assertEquals(EventType.ARG, m_outputRequest7.getEvents().get(2).getEventType());
		assertEquals(EventType.ARG, m_outputRequest7.getEvents().get(3).getEventType());
		assertEquals(EventType.RETURN, m_outputRequest7.getEvents().get(4).getEventType());
		assertEquals(EventType.EXIT, m_outputRequest7.getEvents().get(5).getEventType());
		
	}
	
	private List<ITraceEvent> getTestEvents(String[] myInputEventStrings) throws IntraceException {
		List<ITraceEvent> myEvents = new ArrayList<ITraceEvent>();

		for(String eventString : myInputEventStrings)
			myEvents.add( createEvent(eventString) );
		
		return myEvents;
	}
	private ITraceEvent createEvent(String eventString) throws IntraceException {
		
		return m_eventParser.createEvent(eventString, 0);
	}
	String[] m_myInputEventStrings = {
   			  "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
	};
	String[] m_myInputEventStringsWithExtraEvents = {
 			  "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: {"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Arg: SELECT FOO FROM BAR"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: {"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: {"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:close: {"
			, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:close: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: }"
			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: }"
	};
	
	/**
	 * My initial go at this didn't take into account that some methods will
	 * have multiple arguments, like the four Arg lines, below.
	 */
	String[] m_multipleArguments = {
			"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: {:279",
				"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Arg (this):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
				"[18:13:07.280]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.281]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: }:281~"
			};
	
	/*
	 * big combo, three sets of events, to validate one loop itration to the next.
	 */
	/**
	 * My initial go at this didn't take into account that some methods will
	 * have multiple arguments, like the four Arg lines, below.
	 */
	String[] m_multipleRequests = {
			"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: {:279",
				"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Arg (this):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
				"[18:13:07.280]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.281]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: }:281~"
				//break
	 			, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: {"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Arg: SELECT FOO FROM BAR"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: {"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Arg: SELECT FOO FROM BAR"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: Return: FOO"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:executeQuery: }"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: Return: FOO"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:executeQuery: }"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: {"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: {"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:close: {"
				, "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcStatement:close: }"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: }"
				, "[18:07:53.681]:[67]:org.apache.commons.dbcp.DelegatingStatement:close: }",
				//break
				"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: {:279",
				"[18:13:07.279]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Arg (this):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
				"[18:13:07.280]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.281]:[36]:org.apache.tomcat.dbcp.dbcp.DelegatingConnection:prepareStatement: }:281~"
	};
	
	String[] m_nothingToDelete = {
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: {:38",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (sql):      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetType): 1003",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetConcurrency): 1007",
				"[18:13:07.279]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Arg (resultSetHoldability): 2",
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: Return:      select PRODUCTID, NAME, DESCN, CATEGORY from PRODUCT where CATEGORY = ?",   
				"[18:13:07.280]:[36]:org.postgresql.jdbc4.Jdbc4Connection:prepareStatement: }:41",
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
	
	/**
	 * The following shows two calls with the same method name from the same class.
	 * The first call has one argument.
	 * The second call has three arguments.
	 * 
	 */
	String[] m_simpleOverride = {
			"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: {:3409"
				,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Arg: select acbslimit0_.J_BFAI as J1_6_, acbslimit0_.J_BFCI as J2_6_, acbslimit0_.J_BFDI as J3_6_, acbslimit0_.J_CQJC as J4_6_, acbslimit0_.J_BFEI as J5_6_, acbslimit0_.J_BFFC as J6_6_, acbslimit0_.J_BFGI as J7_6_, acbslimit0_.J_BFS8 as J8_6_, acbslimit0_.J_BFXL as J9_6_, acbslimit0_.J_BGQL as J10_6_, acbslimit0_.J_BFBI as J11_6_, acbslimit0_.J_BFMC as J12_6_, acbslimit0_.J_BGA_ as J13_6_, acbslimit0_.J_BGB_ as J14_6_, acbslimit0_.J_BGC_ as J15_6_, acbslimit0_.J_BGD_ as J16_6_, acbslimit0_.J_BGOP as J17_6_, acbslimit0_.DSDUR8 as DSDUR18_6_, acbslimit0_.J_BFVC as J19_6_, acbslimit0_.J_CQKI as J20_6_, acbslimit0_.J_CQLT as J21_6_, acbslimit0_.J_CQMN as J22_6_, acbslimit0_.J_RAKT as J23_6_, acbslimit0_.J_BFAI as formula3_, acbslimit0_.J_BFCI as formula4_, acbslimit0_.J_BFDI as formula5_, acbslimit0_.J_CQJC as formula6_, acbslimit0_.J_BFEI as formula7_, acbslimit0_.J_BFFC as formula8_, acbslimit0_.J_BFGI as formula9_ from BSDTADLS.J_CALM acbslimit0_ where acbslimit0_.J_BFAI='VY' and acbslimit0_.J_BFCI='0001248295' and acbslimit0_.J_CQJC='600' and acbslimit0_.J_BFDI='00000000' and acbslimit0_.J_BFEI='00' and acbslimit0_.J_BFFC='00' and acbslimit0_.J_BFGI='VYCUST2'"
				,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: {:3498"
				,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Arg: select acbslimit0_.J_BFAI as J1_6_, acbslimit0_.J_BFCI as J2_6_, acbslimit0_.J_BFDI as J3_6_, acbslimit0_.J_CQJC as J4_6_, acbslimit0_.J_BFEI as J5_6_, acbslimit0_.J_BFFC as J6_6_, acbslimit0_.J_BFGI as J7_6_, acbslimit0_.J_BFS8 as J8_6_, acbslimit0_.J_BFXL as J9_6_, acbslimit0_.J_BGQL as J10_6_, acbslimit0_.J_BFBI as J11_6_, acbslimit0_.J_BFMC as J12_6_, acbslimit0_.J_BGA_ as J13_6_, acbslimit0_.J_BGB_ as J14_6_, acbslimit0_.J_BGC_ as J15_6_, acbslimit0_.J_BGD_ as J16_6_, acbslimit0_.J_BGOP as J17_6_, acbslimit0_.DSDUR8 as DSDUR18_6_, acbslimit0_.J_BFVC as J19_6_, acbslimit0_.J_CQKI as J20_6_, acbslimit0_.J_CQLT as J21_6_, acbslimit0_.J_CQMN as J22_6_, acbslimit0_.J_RAKT as J23_6_, acbslimit0_.J_BFAI as formula3_, acbslimit0_.J_BFCI as formula4_, acbslimit0_.J_BFDI as formula5_, acbslimit0_.J_CQJC as formula6_, acbslimit0_.J_BFEI as formula7_, acbslimit0_.J_BFFC as formula8_, acbslimit0_.J_BFGI as formula9_ from BSDTADLS.J_CALM acbslimit0_ where acbslimit0_.J_BFAI='VY' and acbslimit0_.J_BFCI='0001248295' and acbslimit0_.J_CQJC='600' and acbslimit0_.J_BFDI='00000000' and acbslimit0_.J_BFEI='00' and acbslimit0_.J_BFFC='00' and acbslimit0_.J_BFGI='VYCUST2'"
				,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Arg: -1"
				,"[04:04:43.380]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Arg: -1"
				,"[04:04:43.719]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Return: oracle.jdbc.driver.OraclePreparedStatementWrapper@15c115c1"
				,"[04:04:43.720]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: }:3535~oracle.jdbc.driver.PhysicalConnection.prepareStatement(PhysicalConnection.java:3535),oracle.jdbc.driver.PhysicalConnection.prepareStatement(PhysicalConnection.java:3409),org.apache.tomcat.dbcp.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),org.apache.tomcat.dbcp.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:37),java.lang.reflect.Method.invoke(Method.java:611),org.hibernate.engine.jdbc.internal.proxy.ConnectionProxyHandler.continueInvocation(ConnectionProxyHandler.java:138),org.hibernate.engine.jdbc.internal.proxy.AbstractProxyHandler.invoke(AbstractProxyHandler.java:81),$Proxy60.prepareStatement(Unknown Source),org.hibernate.engine.jdbc.internal.StatementPreparerImpl$5.doPrepare(StatementPreparerImpl.java:147),org.hibernate.engine.jdbc.internal.StatementPreparerImpl$StatementPreparationTemplate.prepareStatement(StatementPreparerImpl.java:166),org.hibernate.engine.jdbc.internal.StatementPreparerImpl.prepareQueryStatement(StatementPreparerImpl.java:145),org.hibernate.loader.Loader.prepareQueryStatement(Loader.java:1720),org.hibernate.loader.Loader.executeQueryStatement(Loader.java:1697),org.hibernate.loader.Loader.doQuery(Loader.java:832),org.hibernate.loader.Loader.doQueryAndInitializeNonLazyCollections(Loader.java:293),org.hibernate.loader.Loader.doList(Loader.java:2382),org.hibernate.loader.Loader.doList(Loader.java:2368),org.hibernate.loader.Loader.listIgnoreQueryCache(Loader.java:2198),org.hibernate.loader.Loader.list(Loader.java:2193),org.hibernate.loader.hql.QueryLoader.list(QueryLoader.java:470),org.hibernate.hql.internal.ast.QueryTranslatorImpl.list(QueryTranslatorImpl.java:355),org.hibernate.engine.query.spi.HQLQueryPlan.performList(HQLQueryPlan.java:195),org.hibernate.internal.SessionImpl.list(SessionImpl.java:1244),org.hibernate.internal.QueryImpl.list(QueryImpl.java:101),com.fisglobal.phx.acbs.AcbsDataReader.getEntities(AcbsDataReader.java:5720),com.fisglobal.phx.acbs.AcbsDataReader.getOverallLimitForFacility(AcbsDataReader.java:1176),com.fisglobal.phx.acbs.AcbsDataReader.populateFacilityData(AcbsDataReader.java:1135),com.fisglobal.phx.acbs.AcbsDataReader.processFacility(AcbsDataReader.java:1124),com.fisglobal.phx.acbs.AcbsDataReader.getAcbsFacilityByID(AcbsDataReader.java:1105),com.fisglobal.phx.acbs.AcbsDataReader.getAccountStructureByCustomerID(AcbsDataReader.java:762),com.fisglobal.phx.common.svc.lendingdata.LendingDataReaderWrapper.getAccountStructureByCustomerID(LendingDataReaderWrapper.java:157),com.fisglobal.phx.common.svc.lendingdata.LendingDataServiceComponent.getAccountStructuresByCustomerId(LendingDataServiceComponent.java:394),com.fisglobal.phx.common.svc.lendingdata.ServiceMediator.accountStructureInquiry(ServiceMediator.java:210),com.fisglobal.phx.common.svc.lendingdata.ServiceMediator.execute(ServiceMediator.java:69),com.fisglobal.phx.svc.ServiceMediatorBase.mediate(ServiceMediatorBase.java:88),com.fis.service.provider.service.ServiceProvider.execute(ServiceProvider.java:182),com.fis.service.provider.core.Version2RequestProcessor.processRequest(Version2RequestProcessor.java:443),com.fis.service.provider.server.FrontController.invokeRequestProcessor(FrontController.java:451),com.fis.service.provider.server.FrontServlet.doPost(FrontServlet.java:349),javax.servlet.http.HttpServlet.service(HttpServlet.java:647),javax.servlet.http.HttpServlet.service(HttpServlet.java:728),org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305),org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210),org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222),org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123),org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171),org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99),org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118),org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:408),org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1009),org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589),org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:310),java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:897),java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:919)"
				,"[04:04:43.720]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: Return: oracle.jdbc.driver.OraclePreparedStatementWrapper@15c115c1"
				,"[04:04:43.720]:[1394]:oracle.jdbc.driver.PhysicalConnection:prepareStatement: }:3409~oracle.jdbc.driver.PhysicalConnection.prepareStatement(PhysicalConnection.java:3409),org.apache.tomcat.dbcp.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),org.apache.tomcat.dbcp.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source),sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:37),java.lang.reflect.Method.invoke(Method.java:611),org.hibernate.engine.jdbc.internal.proxy.ConnectionProxyHandler.continueInvocation(ConnectionProxyHandler.java:138),org.hibernate.engine.jdbc.internal.proxy.AbstractProxyHandler.invoke(AbstractProxyHandler.java:81),$Proxy60.prepareStatement(Unknown Source),org.hibernate.engine.jdbc.internal.StatementPreparerImpl$5.doPrepare(StatementPreparerImpl.java:147),org.hibernate.engine.jdbc.internal.StatementPreparerImpl$StatementPreparationTemplate.prepareStatement(StatementPreparerImpl.java:166),org.hibernate.engine.jdbc.internal.StatementPreparerImpl.prepareQueryStatement(StatementPreparerImpl.java:145),org.hibernate.loader.Loader.prepareQueryStatement(Loader.java:1720),org.hibernate.loader.Loader.executeQueryStatement(Loader.java:1697),org.hibernate.loader.Loader.doQuery(Loader.java:832),org.hibernate.loader.Loader.doQueryAndInitializeNonLazyCollections(Loader.java:293),org.hibernate.loader.Loader.doList(Loader.java:2382),org.hibernate.loader.Loader.doList(Loader.java:2368),org.hibernate.loader.Loader.listIgnoreQueryCache(Loader.java:2198),org.hibernate.loader.Loader.list(Loader.java:2193),org.hibernate.loader.hql.QueryLoader.list(QueryLoader.java:470),org.hibernate.hql.internal.ast.QueryTranslatorImpl.list(QueryTranslatorImpl.java:355),org.hibernate.engine.query.spi.HQLQueryPlan.performList(HQLQueryPlan.java:195),org.hibernate.internal.SessionImpl.list(SessionImpl.java:1244),org.hibernate.internal.QueryImpl.list(QueryImpl.java:101),com.fisglobal.phx.acbs.AcbsDataReader.getEntities(AcbsDataReader.java:5720),com.fisglobal.phx.acbs.AcbsDataReader.getOverallLimitForFacility(AcbsDataReader.java:1176),com.fisglobal.phx.acbs.AcbsDataReader.populateFacilityData(AcbsDataReader.java:1135),com.fisglobal.phx.acbs.AcbsDataReader.processFacility(AcbsDataReader.java:1124),com.fisglobal.phx.acbs.AcbsDataReader.getAcbsFacilityByID(AcbsDataReader.java:1105),com.fisglobal.phx.acbs.AcbsDataReader.getAccountStructureByCustomerID(AcbsDataReader.java:762),com.fisglobal.phx.common.svc.lendingdata.LendingDataReaderWrapper.getAccountStructureByCustomerID(LendingDataReaderWrapper.java:157),com.fisglobal.phx.common.svc.lendingdata.LendingDataServiceComponent.getAccountStructuresByCustomerId(LendingDataServiceComponent.java:394),com.fisglobal.phx.common.svc.lendingdata.ServiceMediator.accountStructureInquiry(ServiceMediator.java:210),com.fisglobal.phx.common.svc.lendingdata.ServiceMediator.execute(ServiceMediator.java:69),com.fisglobal.phx.svc.ServiceMediatorBase.mediate(ServiceMediatorBase.java:88),com.fis.service.provider.service.ServiceProvider.execute(ServiceProvider.java:182),com.fis.service.provider.core.Version2RequestProcessor.processRequest(Version2RequestProcessor.java:443),com.fis.service.provider.server.FrontController.invokeRequestProcessor(FrontController.java:451),com.fis.service.provider.server.FrontServlet.doPost(FrontServlet.java:349),javax.servlet.http.HttpServlet.service(HttpServlet.java:647),javax.servlet.http.HttpServlet.service(HttpServlet.java:728),org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305),org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210),org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222),org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123),org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171),org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99),org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118),org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:408),org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1009),org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589),org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:310),java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:897),java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:919)"
	};
	
}
