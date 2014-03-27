package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.intrace.client.IntraceException;
import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;

/**
 * TODO:  currently, this test starts with data from hsqldb events.....need to support other DB's
 * @author erikostermueller
 *
 */
public class TestRequestStatistics {
	ITraceEvent m_insertSqlEvent = null;
	private static String EVENT_INSERT_SQL = "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: INSERT INTO Event (name, description, date, location) VALUES(?, ?, ?, ?)"; 
	ITraceEvent m_selectSqlEvent = null;
	private static String EVENT_SELECT_SQL = "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: SELECT name, description, date, location from EVENT where location = ?"; 
	ITraceEvent m_deleteSqlEvent = null;
	private static String EVENT_DELETE_SQL = "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: DELETE FROM Event WHERE name = ?"; 
	ITraceEvent m_updateSqlEvent = null;
	private static String EVENT_UPDATE_SQL = "[18:07:53.681]:[67]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Arg: update EVENT set description=? where name=?"; 
	private org.wuqispank.web.IFactory m_wuqiSpankFactory = org.wuqispank.DefaultFactory.getFactory();	
	IRequest m_request = null;
	ITraceEvent t_enter = null;
	ITraceEvent t_exit = null;
	@Before
	public void setupEvents() throws IntraceException {
		ITraceEventParser eventParser = null;
		org.intrace.client.IFactory inTraceFactory = org.intrace.client.DefaultFactory.getFactory();
		org.wuqispank.web.IFactory wuqiSpankFactory = org.wuqispank.DefaultFactory.getFactory();
		m_request = inTraceFactory.getRequest();
		eventParser = inTraceFactory.getEventParser();
		m_insertSqlEvent = eventParser.createEvent(EVENT_INSERT_SQL,0);
		m_selectSqlEvent = eventParser.createEvent(EVENT_SELECT_SQL,0);
		m_deleteSqlEvent = eventParser.createEvent(EVENT_DELETE_SQL,0);
		m_updateSqlEvent = eventParser.createEvent(EVENT_UPDATE_SQL,0);

			t_enter = eventParser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
			t_exit = eventParser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: }",0);
		
	}

	/**
	 * Right now, it takes 3 events to model a sql statement.
	 * @param e
	 * @return
	 */
	private List<ITraceEvent> wrap(ITraceEvent e) {
		List<ITraceEvent> list = new ArrayList<ITraceEvent>();
		list.add(t_enter);
		list.add(e);
		list.add(t_exit);
		return list;
	}
	@Test
	public void canParseTableNamesIntoModelFrom_INSERT() throws WuqispankException {
		List<ITraceEvent> events = new ArrayList<ITraceEvent>();
		
		events.addAll(wrap(m_insertSqlEvent));
		events.addAll(wrap(m_insertSqlEvent));
		events.addAll(wrap(m_insertSqlEvent));
		events.addAll(wrap(m_selectSqlEvent));
		events.addAll(wrap(m_selectSqlEvent));
		events.addAll(wrap(m_selectSqlEvent));
		events.addAll(wrap(m_selectSqlEvent));
		events.addAll(wrap(m_selectSqlEvent));
		events.addAll(wrap(m_updateSqlEvent));
		events.addAll(wrap(m_deleteSqlEvent));
		events.addAll(wrap(m_deleteSqlEvent));
		
		m_request.setEvents(events);
		
		IRequestWrapper requestWrapper = m_wuqiSpankFactory.getRequestWrapper();
		requestWrapper.setRequest(m_request);
		
		assertEquals("Aggregate stats are wrong.  Couldn't find right number of SQL statements",11, requestWrapper.getSqlCount());
//		assertEquals("Aggregate stats are wrong.  Couldn't find right number of Tables",1, requestWrapper.getTableCount() + "]");
//		assertEquals("Aggregate stats are wrong.  Couldn't find right number of Columns",33, requestWrapper.getColumnCount() + "]");
	}
}
		
