package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.headlessintrace.client.IntraceException;
import org.headlessintrace.client.model.DefaultTraceEventParser;
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
	

	/**
	 * 2011-04-04 19:27:39.92034
	 * 
	 * The following is JavaScript:
console.log( new Date(1301963259920).getMonth() );
console.log( new Date(1301963259920).getDate() );
console.log( new Date(1301963259920).getHours() );
console.log( new Date(1301963259920).getMinutes() );
console.log( new Date(1301963259920).getSeconds() );
console.log( new Date(1301963259920).getMilliseconds() );
console.log( new Date(1301963259920).getDate() );

3   Month
4   Date
19  Hours
27  Minutes
39  Seconds
920 Milliseconds
	 * 
	 */
	@Test
	public void canGetJavaScriptCompatibleDates() {
		long myTimestamp = 1301963259920l;
		Date d = new Date(myTimestamp);

	    validateDate(d);
	    validateTime(d);
	}
	public void validateTime(Date d) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		//int hour       = calendar.get(Calendar.HOUR);        // 12 hour clock
		int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
		int minute     = calendar.get(Calendar.MINUTE);
		int second     = calendar.get(Calendar.SECOND);
		int millisecond= calendar.get(Calendar.MILLISECOND);	
		assertEquals(19,hourOfDay);
		assertEquals(27,minute);
		assertEquals(920,millisecond);
		
	}
	public void validateDate(Date d) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		int year       = calendar.get(Calendar.YEAR);
		int month      = calendar.get(Calendar.MONTH); 
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // Jan = 0, not 1
		int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		int weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);
		assertEquals(2011,year);
		assertEquals(3,month);
		assertEquals(4,dayOfMonth);
		
	}
	@Test
	public void canParseInTraceDateAndTimeFormat() throws IntraceException {
		long myTimestamp = 1301963259920l;
		Date d = new Date(myTimestamp);
		
		Date dateAndInTraceTime = DefaultTraceEventParser.convertInTraceAgentTimeFmt(
					d,		//Current system date.
					"19:27:05.920"); //From InTrace agent.
		validateDate(dateAndInTraceTime);
		validateTime(dateAndInTraceTime);
//		ITraceEvent t = this.parser.createEvent("[19:27:05.920]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
		
	}
	@Test public void canParseTimeFromEvent() throws IntraceException {
		ITraceEvent t = this.parser.createEvent("[19:27:05.920]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
		

		final String SIMPLE_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
		SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN);
		StringBuilder sb = new StringBuilder();
		sb.append(f.format(new Date())).append(" ").append("19:27:05.920");
		assertEquals( "couldn't get right agent date format", sb.toString(), t.getAgentDateTimeString());
		
		
	}
	static public long addCurrDate_ORIG(long time) throws IntraceException {
		Date currentDate = new Date();
		Date objTime = new Date(time);
		
		String delimsRegEx = "[:.]";

		Calendar calTime = new GregorianCalendar();
		calTime.setTime(objTime);
		
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(currentDate);
		
		calendar.set(Calendar.HOUR_OF_DAY,calTime.get(Calendar.HOUR_OF_DAY)); // 24 hour clock
		calendar.set(Calendar.MINUTE,calTime.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND,calTime.get(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND,calTime.get(Calendar.MILLISECOND));
		
		return calendar.getTime().getTime();
	}
	
	public @Test void foo() {
		long myTimestamp = 1301963259920l;
		Date d = new Date(myTimestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
		String outputDate = sdf.format(d);
		assertEquals(outputDate,"2011-04-04 19:27:39.920");
		
	}
	//@Test
	public void XcanFormatInTraceDateTimeFormat_noEventParsing() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss.SSS");	
		//Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
		Calendar calendar = new GregorianCalendar(2011,3,4,19,27,39);
		calendar.set(Calendar.MILLISECOND, 920);
	 
		int year       = calendar.get(Calendar.YEAR);
		int month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
		int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		int weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);
	 
		int hour       = calendar.get(Calendar.HOUR);        // 12 hour clock
		int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
		int minute     = calendar.get(Calendar.MINUTE);
		int second     = calendar.get(Calendar.SECOND);
		int millisecond= calendar.get(Calendar.MILLISECOND);
	 
		System.out.println(sdf.format(calendar.getTime()));
	 
		System.out.println("year \t\t: " + year);
		System.out.println("month \t\t: " + month);
		System.out.println("dayOfMonth \t: " + dayOfMonth);
		System.out.println("dayOfWeek \t: " + dayOfWeek);
		System.out.println("weekOfYear \t: " + weekOfYear);
		System.out.println("weekOfMonth \t: " + weekOfMonth);
	 
		System.out.println("hour \t\t: " + hour);
		System.out.println("hourOfDay \t: " + hourOfDay);
		System.out.println("minute \t\t: " + minute);
		System.out.println("second \t\t: " + second);
		System.out.println("millisecond \t: " + millisecond);		
//		Date d = new Date();
//		
//		Calendar c = new GregorianCalendar(2011,03,04,19,27,39);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
//		sdf.format(d.getTime());
//		assertEquals("2011 04 04 19:27:39", sdf.format(d));
		
//		c.setTime(d);
//		
//		c.set(Calendar.DATE, 4);
//		c.set(Calendar.MONTH, 3);
//		c.set(Calendar.YEAR, 2011);
//		
//		c.set(Calendar.HOUR_OF_DAY, 19);
//		c.set(Calendar.MINUTE,27);
//		c.set(Calendar.SECOND,39);
//		c.set(Calendar.MILLISECOND,920);
//		//assertEquals(d.getTime(),1301963259920l);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
//		assertEquals("2011-04-04 19:27:39.920", sdf.format(d));
		
		
	}
	
	@Test
	public void canFormatInTraceDateTimeFormat_noEventParsing() {
		long myTimestamp = 1301963259920l;
		Date d = new Date(myTimestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
		String outputDate = sdf.format(d);
		assertEquals(outputDate,"2011-04-04 19:27:39.920");
	}
	@Test
	public void canParseInTraceTimeFormat_noEventParsing() {
		//15:41:05.294
		SimpleDateFormat df1 = new SimpleDateFormat("kk:mm:ss.SSS");
		try {
			Date d = df1.parse("19:27:05.920");
		    validateTime(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
 	 * @throws WuqispankException
	 */
	//@Test
	public void canAssignDateFromEvent() throws WuqispankException {
		
		IRequestWrapper requestWrapper = DefaultFactory.getFactory().getRequestWrapper();
		IRequest request = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		request.setEvents( Arrays.asList(e_entry));
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
//	@Test void canGetTimestampFromEvent()  {
//		//e_entry = parser.createEvent("[15:41:05.294]:[97]
//		assertEquals("Could not find timestamp for event", "2353",e_entry.getAgentTimeMillis())
//	}
	
	
	
}
