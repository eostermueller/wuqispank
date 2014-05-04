package org.wuqispank.test.level1.foundationdb;

import static org.junit.Assert.*;

import java.util.List;

import org.intrace.client.DefaultFactory;
import org.intrace.client.model.ITraceEvent;
import org.junit.Test;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.foundationdb.FoundationDBSqlParser;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class SimpleColumnCountSqlParseTest {
	private static String EVENT_INSERT_SQL = "INSERT INTO Event (name, description, date, location) VALUES(?, ?, ?, ?)"; 
	private static String EVENT_SELECT_SQL = "SELECT name, description, date, location from Event where location = ?"; 
	private static String EVENT_DELETE_SQL = "DELETE FROM Event WHERE name = ?"; 
	private static String EVENT_UPDATE_SQL = "update Event set description=? where name=?"; 

	@Test
	public void canAssociateColumnsWithTables() throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("select X.chicago from USA X");
		assertEquals("Count of SQL columns incorrect when parsing the simplest SQL statement ever", 1, model.getColumnCount() );
		ITable shouldBeUsa = model.getTable(0);
		assertEquals("Count of SQL columns incorrect when parsing the simplest SQL statement ever", 1, shouldBeUsa.getWhereClauseColumnCount() );
		
		assertEquals("SQL column name incorrectly parsed for simplest SQL statement ever", "chicago", shouldBeUsa.getColumn(0).getName() );
	}
	@Test
	public void canParseTwoTableNames() throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("select a.chicago, b.copenhagen from USA a, Denmark b");
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 2, model.getTableCount() );
		
		ITable shouldBeUsa = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "USA".toLowerCase(), shouldBeUsa.getName() );
		assertEquals("Couldn't not find column name from table", "chicago", shouldBeUsa.getColumn(0).getName());

		ITable shouldBeDenmark = model.getTable(1);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "Denmark".toLowerCase(), shouldBeDenmark.getName() );
		assertEquals("Couldn't not find column name from table", "copenhagen", shouldBeDenmark.getColumn(0).getName());
	}

//	@Test
//	public void canParseSubSelectTableName() throws SqlParseException {
//		ISqlParser parser = new FoundationDBSqlParser();
//		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
//		parser.setSqlModel(model);
//		parser.parse("select chicago, Copenhagen from (select * from USA) t, Denmark");
//		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 2, model.getTables().size());
//		
//		ITable shouldBeChicago = model.getTables().get(0);
//		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "USA", shouldBeChicago.getName() );
//
//		ITable shouldBeDenmark = model.getTables().get(1);
//		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "Denmark", shouldBeDenmark.getName() );
//		
//	}
	
	@Test
	public void canParseTableNameFor_INSERT () throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_INSERT_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 1, model.getTableCount());
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest INSERT SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
	}
	@Test
	public void canParseTableNameFor_DELETE () throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_DELETE_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest DELETE SQL statement ever", 1, model.getTableCount());
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest DELETE SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
	}
	@Test
	public void canParseTableNameFor_UPDATE() throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_UPDATE_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest UPDATE SQL statement ever", 1, model.getTableCount() );
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest UPDATE SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
	}
	@Test
	public void canParseTableNameFor_SELECT() throws SqlParseException {
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_SELECT_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
	}
}
