package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.headlessintrace.client.IntraceException;
import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEventParser;
import org.headlessintrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Ignore;
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
public class ConvertingEventsIntoSqlTest {
	ITraceEventParser parser = org.headlessintrace.client.DefaultFactory.getFactory().getEventParser();
	List<ITraceEvent> myCriteriaList = new ArrayList<ITraceEvent>();
	ITraceEvent e_entry = null;
	ITraceEvent e_arg = null;
	ITraceEvent e_return = null;
	ITraceEvent e_exit = null;
	ITraceEvent e_exit_other = null;
	
	@Before
	public void setup() throws IntraceException {
		 e_entry = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
		 e_arg = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: INSERT INTO Location (name, address) VALUES(?, ?)", 0);
		 e_return = parser.createEvent("[18:07:53.682]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Return: INSERT INTO Event (name, description, date, location) VALUES(?, ?, ?, ?)",0);
		 e_exit = parser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: }", 0);
		 e_exit_other = parser.createEvent("[15:47:00.999]:[203]:example.webapp.servlet.HelloWorld:doGet: }:50", 0);
		 
		
	}
	private org.wuqispank.web.IFactory m_wuqiSpankFactory = org.wuqispank.DefaultFactory.getFactory();
	
	@Test
	public void canAssembleEventsIntoRequest() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(e_entry, e_arg, e_return, e_exit));
		try {
			requestWrapper.setRequest(request);
		} catch(Exception e) {
			fail("Should not receive exception [" + e.toString() + "]");
		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created just a single SQL statement", 1,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't find SQL statement", "INSERT INTO Location (name, address) VALUES(?, ?)", sqlWrapper.getSqlText() );
		
	}
	
	@Test
	public void canAssembleEventsIntoRequest_leadingUnknownEvent() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(e_exit_other,e_entry, e_arg, e_return, e_exit));
		try {
			requestWrapper.setRequest(request);
		} catch(Exception e) {
			fail("Should not receive exception [" + e.toString() + "]");
		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created just a single SQL statement", 1,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't find SQL statement", "INSERT INTO Location (name, address) VALUES(?, ?)", sqlWrapper.getSqlText());
	}
	
	/**
	 * This used to work, but it does not now.
	 * After the first full request is processed (first four events), the code
	 * starts to process the last event and says,"they there is a problem here,
	 * this one event needs others to be a complete request"
	 * @throws WuqispankException
	 */
	@Test
	@Ignore
	public void canAssembleEventsIntoRequest_trailingUnknownEvent() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(e_entry, e_arg, e_return, e_exit, e_exit_other));
		try {
			requestWrapper.setRequest(request);
		} catch(Exception e) {
			fail("Should not receive exception [" + e.toString() + "]");
		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created just a single SQL statement", 1,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't find SQL statement", "INSERT INTO Location (name, address) VALUES(?, ?)", sqlWrapper.getSqlText() );
		
	}
	@Test
	public void canAssembleTwoEventsIntoRequest() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(e_entry, e_arg, e_return, e_exit,e_entry, e_arg, e_return, e_exit));
		try {
			requestWrapper.setRequest(request);
		} catch(Exception e) {
			fail("Should not receive exception [" + e.toString() + "]");
		}
		
		List<ISqlWrapper> sqlList = requestWrapper.getSql();
		assertEquals("Should have created two SQL statements", 2,sqlList.size());
		
		ISqlWrapper sqlWrapper = sqlList.get(0);
		assertEquals("Didn't find SQL statement", "INSERT INTO Location (name, address) VALUES(?, ?)", sqlWrapper.getSqlText() );
		sqlWrapper = sqlList.get(1);
		assertEquals("Didn't find SQL statement", "INSERT INTO Location (name, address) VALUES(?, ?)", sqlWrapper.getSqlText() );
		
	}
	@Test
	public void canCaptureMoreThanOneArgumentWithoutBreaking() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(
				e_entry, 
				e_arg, //First argument of a method
				e_arg, //Second argument of same method.  
				e_return,
				e_exit));
		try {
			requestWrapper.setRequest(request);
			assertEquals("Did that extra argument keep us from finding the correct number of tables?",1,requestWrapper.getTableCount());
		} catch(Exception e) {
			fail("whoops.  exception was thrown while processing method call with two arguments.");
		}
		
		
	}
	@Test
	public void canDetectEventOrderProblem_extraEntry() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		Exception yepExceptionWasThrown = null;
		request.setEvents( Arrays.asList(
				e_entry, 
				e_entry, 
				e_arg)); 
		try {
			requestWrapper.setRequest(request);
			fail("whoops.  code should have thrown an exception to flag the 2nd arg event");
		} catch(Exception e) {
			yepExceptionWasThrown = e;
		}
		assertNotNull("Whoops.  Exception should have been thrown to flag that extra arg event", yepExceptionWasThrown);
		
		
	}
	@Test
	public void canDetectEventOrderProblem_extraExitMissingArg() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		Exception yepExceptionWasThrown = null;
		request.setEvents( Arrays.asList(
				e_entry, 
				e_exit,
				e_exit)); 
		try {
			requestWrapper.setRequest(request);
			fail("whoops.  code should have thrown an exception to flag the 2nd arg event");
		} catch(Exception e) {
			yepExceptionWasThrown = e;
		}
		assertNotNull("Whoops.  Exception should have been thrown to flag that extra arg event", yepExceptionWasThrown);
		
		
	}
	@Test
	public void canDetectEventOrderProblem_tooFewEvents_1() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		Exception yepExceptionWasThrown = null;
		request.setEvents( Arrays.asList( e_entry ));
		
		try {
			requestWrapper.setRequest(request);
			fail("whoops.  code should have thrown an exception to flag the 2nd arg event");
		} catch(Exception e) {
			yepExceptionWasThrown = e;
		}
		assertNotNull("Whoops.  Exception should have been thrown to flag that extra arg event", yepExceptionWasThrown);
		
		
	}
	@Test
	public void canDetectEventOrderProblem_tooFewEvents_2() throws WuqispankException {
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		Exception yepExceptionWasThrown = null;
		request.setEvents( Arrays.asList(
				e_entry, 
				e_arg)); 
		try {
			requestWrapper.setRequest(request);
			fail("whoops.  code should have thrown an exception to flag the 2nd arg event");
		} catch(Exception e) {
			yepExceptionWasThrown = e;
		}
		assertNotNull("Whoops.  Exception should have been thrown to flag that extra arg event", yepExceptionWasThrown);
		
	}
	@Test
	public void canDetectMissingExit() throws WuqispankException {
		
		ISqlWrapperFactory factory = new DefaultJdbcSqlWrapperFactory();
		factory.add(e_entry);
		factory.add(e_arg);
		
		IRequestWrapper rqWrap = DefaultFactory.getFactory().getRequestWrapper();
		try {
			ISqlWrapper sqlWrapper = factory.createSqlWrapper(rqWrap);
			fail("Should have received 1 error to indicate that exit method was missing");

		} catch (WuqispankException we) {
			
		}
		
		
	}
	@Test
	public void canDetectIncorrectClassName() throws WuqispankException {
		
		IRequestWrapper rqWrap = DefaultFactory.getFactory().getRequestWrapper();
		ISqlWrapperFactory factory = new DefaultJdbcSqlWrapperFactory();
		factory.add(e_entry);
		factory.add(e_arg);
		factory.add(e_exit_other);
		
		try {
			ISqlWrapper sqlWrapper = factory.createSqlWrapper(rqWrap);
			fail("Should have received 1 error to indicate that the class names did not all match");
		} catch (WuqispankException we) {
			
		}
		
	}
//	@Test void canGetTimestampFromEvent()  {
//		//e_entry = parser.createEvent("[15:41:05.294]:[97]
//		assertEquals("Could not find timestamp for event", "2353",e_entry.getAgentTimeMillis())
//	}
	
	
	
}
