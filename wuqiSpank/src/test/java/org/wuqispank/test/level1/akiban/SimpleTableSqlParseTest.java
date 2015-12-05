package org.wuqispank.test.level1.akiban;

import static org.junit.Assert.*;

import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.akiban.AkibanSqlParser;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class SimpleTableSqlParseTest {
	private static String EVENT_INSERT_SQL = "INSERT INTO Event (name, description, date, location) VALUES(?, ?, ?, ?)"; 
	private static String EVENT_SELECT_SQL = "SELECT name, description, date, location from Event where location = ?"; 
	private static String EVENT_DELETE_SQL = "DELETE FROM Event WHERE name = ?"; 
	private static String EVENT_UPDATE_SQL = "update Event set description=? where name=?"; 

	@Test
	public void canParseSqlTableNames() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("select chicago from USA");
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 1, model.getTableCount() );
		ITable shouldBeUsa = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "USA".toLowerCase(), shouldBeUsa.getName() );
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTwoTableNames() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("select chicago, copenhagen from USA, Denmark");
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 2, model.getTableCount() );
		
		ITable shouldBeChicago = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "USA".toLowerCase(), shouldBeChicago.getName() );

		ITable shouldBeDenmark = model.getTable(1);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "Denmark".toLowerCase(), shouldBeDenmark.getName() );
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
		assertNull("Whoops.  Found a parse exception for a valid SQL",model.getParseException());
	}

	@Test
	public void canParseSubSelectTableName() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("select chicago, Copenhagen from (select * from USA) t, Denmark");
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 2, model.getTableCount());
		
		ITable shouldBeChicago = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "USA".toLowerCase(), shouldBeChicago.getName() );

		ITable shouldBeDenmark = model.getTable(1);
		assertEquals("SQL table name incorrectly parsed for simplest SQL statement ever", "Denmark".toLowerCase(), shouldBeDenmark.getName() );
		
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTableNameFor_INSERT () throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_INSERT_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SQL statement ever", 1, model.getTableCount() );
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest INSERT SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTableNameFor_DELETE () throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_DELETE_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest DELETE SQL statement ever", 1, model.getTableCount());
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest DELETE SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTableNameFor_UPDATE() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_UPDATE_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest UPDATE SQL statement ever", 1, model.getTableCount() );
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest UPDATE SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTableNameFor_SELECT() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(EVENT_SELECT_SQL);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );
		
		ITable shouldBe_EVENT = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "Event".toLowerCase(), shouldBe_EVENT.getName() );
		
		assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canParseTableAliasesInSelect() throws SqlParseException {
				
				ISqlParser parser = new AkibanSqlParser();
				ISqlModel model = DefaultFactory.getFactory().getSqlModel();
				parser.setSqlModel(model);
				
				parser.parse("select ITEMID, LISTPRICE, UNITCOST, SUPPLIER, I.PRODUCTID, NAME, DESCN, " 
						+ "CATEGORY, STATUS, ATTR1, ATTR2, ATTR3, ATTR4, ATTR5 "
						+ "from ITEM I, PRODUCT P where P.PRODUCTID = I.PRODUCTID and I.PRODUCTID = ?");
				
				assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 2, model.getTableCount() );
				
				ITable criteriaItemTable = DefaultFactory.getFactory().getTable("item");
				ITable locatedItemTable = model.findTable(criteriaItemTable);
				assertNotNull("could not find table named 'item'", locatedItemTable);
				assertEquals("Could not find table alias for 'item' table", "i", locatedItemTable.getAlias());
				
				ITable criteriaProductTable = DefaultFactory.getFactory().getTable("pRoduct");
				ITable locatedProductTable = model.findTable(criteriaProductTable);
				assertNotNull("could not find table named 'product'", model.findTable(locatedProductTable));
				assertEquals("Could not find table alias for 'product' table", "p", locatedProductTable.getAlias());
				
				assertEquals("Should have flagged a successful parse",true,model.isParsedSuccessfully());
	}
	@Test
	public void canDetectFailedParse() {
		final String INVALID_SQL = "ELECT name, description, date, location from Event where location = ?";
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		try {
			parser.parse(INVALID_SQL);
		} catch (SqlParseException e) {
			assertEquals("Failed SQL parse was not correctly flagged", false, model.isParsedSuccessfully());
			assertNotNull("Whoops, exception was not saved for an parse of invalid SQL", model.getParseException());
			assertTrue("Whoops, didn't receive the correct exception",model.getParseException().getMessage().contains("Was expecting one of"));
		}
	}
}
