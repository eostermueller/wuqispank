package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.headlessintrace.client.IntraceException;
import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEventParser;
import org.headlessintrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlStatsObserver;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;
import org.wuqispank.model.SqlType;

/**
 * TODO:  currently, this test starts with data from hsqldb events.....need to support other DB's
 * @author erikostermueller
 *
 */
public class SqlParseWithIntraceEventTest {
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
	ITraceEvent t_return = null;
	ITraceEvent t_exit = null;
	@Before
	public void setupEvents() throws IntraceException {
		ITraceEventParser eventParser = null;
		org.headlessintrace.client.IFactory inTraceFactory = org.headlessintrace.client.DefaultFactory.getFactory();
		org.wuqispank.web.IFactory wuqiSpankFactory = org.wuqispank.DefaultFactory.getFactory();
		m_request = inTraceFactory.getRequest();
		eventParser = inTraceFactory.getEventParser();
		m_insertSqlEvent = eventParser.createEvent(EVENT_INSERT_SQL,0);
		m_selectSqlEvent = eventParser.createEvent(EVENT_SELECT_SQL,0);
		m_deleteSqlEvent = eventParser.createEvent(EVENT_DELETE_SQL,0);
		m_updateSqlEvent = eventParser.createEvent(EVENT_UPDATE_SQL,0);
		
		t_enter = eventParser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: {",0);
		
		t_return = eventParser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: Return: FOO",0);
		t_exit = eventParser.createEvent("[15:41:05.294]:[97]:org.hsqldb.jdbc.jdbcConnection:prepareStatement: }",0);
		
			
	}

	@Test
	public void canParseTableNamesIntoModelFrom_INSERT() throws WuqispankException {
		List<ISqlWrapper> sqlStatements = getRequestWrapper(m_insertSqlEvent).getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		ISqlModel model = sqlStatement.getSqlModel();
		assertEquals("SQL parser didn't find enough table names in the SQL statement", 1, model.getTableCount());
		ITable myTable = model.getTable(0);
		
		assertEquals( "Couldn't parse table name from INSERT statement", "EVENT", myTable.getName().toUpperCase() );
	}
	@Test
	public void canParseTableNamesIntoModelFrom_SELECT() throws WuqispankException {
		List<ISqlWrapper> sqlStatements = getRequestWrapper(m_selectSqlEvent).getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		ISqlModel model = sqlStatement.getSqlModel();
		assertEquals("SQL parser didn't find enough table names in the SELECT SQL statement", 1, model.getTableCount() );
		ITable myTable = model.getTable(0);
		
		assertEquals( "Couldn't parse table name from SELECT statement", "EVENT", myTable.getName().toUpperCase() );
	}
	@Test
	public void canCollectStatsFrom_SELECT_loadedFromInTraceEvent() throws WuqispankException {
		IRequestWrapper rq = getRequestWrapper(m_selectSqlEvent);

		List<ISqlWrapper> sqlStatements = rq.getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		ISqlModel model = sqlStatement.getSqlModel();
		assertEquals("SQL parser didn't find enough table names in the SELECT SQL statement", 1, model.getTableCount() );
		ITable myTable = model.getTable(0);
		
		assertEquals( "Couldn't parse table name from SELECT statement", "EVENT", myTable.getName().toUpperCase() );
		
		ISqlStatsObserver stats = rq.getSqlStats();
		
		ITable tableLocation = DefaultFactory.getFactory().getTable();
		tableLocation.setName("event");
		assertEquals("stats didn't compute -- no table count",1,stats.getTableCount(tableLocation));
	}
	@Test
	public void canParseTableNamesIntoModelFrom_DELETE() throws WuqispankException {
		List<ISqlWrapper> sqlStatements = getRequestWrapper(m_deleteSqlEvent).getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		assertEquals("SQL parser didn't find enough table names in the DELETE SQL statement", 1, sqlStatement.getSqlModel().getTableCount() );
		ITable myTable = sqlStatement.getSqlModel().getTable(0);
		
		assertEquals( "Couldn't parse table name from DELETE statement", "EVENT", myTable.getName().toUpperCase() );
	}
	@Test
	public void canParseTableNamesIntoModelFrom_UPDATE() throws WuqispankException {
		List<ISqlWrapper> sqlStatements = getRequestWrapper(m_updateSqlEvent).getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		
		assertEquals("SQL parser didn't find enough table names in the UPDATE SQL statement", 1, sqlStatement.getSqlModel().getTableCount());
		ITable myTable = sqlStatement.getSqlModel().getTable(0);
		
		assertEquals( "Couldn't parse table name from UPDATE statement", "EVENT", myTable.getName().toUpperCase() );
	}
	@Test
	public void canDetectStatementType() throws WuqispankException {
		// U P D A T E
		List<ISqlWrapper> sqlStatements = getRequestWrapper(m_updateSqlEvent).getSql();
		ISqlWrapper sqlStatement = sqlStatements.get(0);
		
		assertEquals("SQL parser couldn't detect statement type of UPDATE", SqlType.UPDATE, sqlStatement.getSqlModel().getSqlType() );

		// I N S E R T
		sqlStatements = getRequestWrapper(m_insertSqlEvent).getSql();
		sqlStatement = sqlStatements.get(0);
		
		assertEquals("SQL parser couldn't detect statement type of INSERT", SqlType.INSERT, sqlStatement.getSqlModel().getSqlType() );
		
		// D E L E T E
		sqlStatements = getRequestWrapper(m_deleteSqlEvent).getSql();
		sqlStatement = sqlStatements.get(0);
		
		assertEquals("SQL parser couldn't detect statement type of DELETE", SqlType.DELETE, sqlStatement.getSqlModel().getSqlType() );
		
		// S E L E C T
		sqlStatements = getRequestWrapper(m_selectSqlEvent).getSql();
		sqlStatement = sqlStatements.get(0);
		
		assertEquals("SQL parser couldn't detect statement type of SELECT", SqlType.SELECT, sqlStatement.getSqlModel().getSqlType() );
		
	}
	
	private IRequestWrapper getRequestWrapper(ITraceEvent event) throws WuqispankException {

		IRequestWrapper requestWrapper = m_wuqiSpankFactory.getRequestWrapper();
		
		List<ITraceEvent> events = new ArrayList<ITraceEvent>();
		events.add(t_enter);
		events.add(event);
		events.add(t_return);
		events.add(t_exit);
		m_request.setEvents(events);

		requestWrapper.setRequest(m_request);
		
		return requestWrapper;
	}
	

}
