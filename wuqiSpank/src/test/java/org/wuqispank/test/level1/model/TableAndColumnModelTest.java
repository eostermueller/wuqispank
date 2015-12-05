package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.IModelObservationMgr;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.model.SqlType;
import org.wuqispank.web.IFactory;

public class TableAndColumnModelTest {
	private IFactory factory = DefaultFactory.getFactory();
	@Test
	public void canAddFindTable_caseInsensitive() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table = factory.getTable("USA");
		sqlModel.addTable(table);
		
		ITable shouldBeUSA = sqlModel.findTable("uSa");
		assertEquals("Explicitly added a table, the 'find' operation can't find the table","USA".toLowerCase(),shouldBeUSA.getName()); 
	}
	@Test
	public void canIdentifyMissingTable() {
		ISqlModel sqlModel = factory.getSqlModel();
		
		ITable shouldBeNull = sqlModel.findTable("uSa");
		assertNull("'find' operation should have returned null when searching for non-existent table",shouldBeNull); 
	}
	@Test
	public void canFlagTableAsShouldBeCached() {
		ITable table = DefaultFactory.getFactory().getTable("name doesn't Matter");
		table.setShouldBeCached(true);
		assertTrue("Just flagged that this table should be cached, but it didnt' work", table.shouldBeCached());
	}
	
	/**
	 * Create a model for this SQL, and validate the parts of the model assembled under the covers.
	 * This test creates a model of this sql:  SELECT S.SanFrancisco from USA S"
	 */
	@Test
	public void canUseTableAliasToAssociateColumnWithTable() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table = factory.getTable("USA");
		
		table.setAlias("S");
		sqlModel.addTable(table);
		sqlModel.addSelectListColumn("S","SanFrancisco");
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it", 1, sqlModel.getTableCount()); //bean-style getter for Wicket
		ITable shouldBeUsa = sqlModel.findTable("Usa");
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it (with the original name)", "USA".toLowerCase(), shouldBeUsa.getName() );
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it (with the original alias)", "s", shouldBeUsa.getAlias() );

		assertEquals("Sanity check failed, couldn't find IColumn added to a simple List", 1, sqlModel.getSelectListColumns().size());
		
		IColumn col = sqlModel.getSelectListColumns().get(0);
		assertEquals("Sanity check failed, couldn't find IColumn name added to a simple List", "SanFrancisco".toLowerCase(), col.getName());
		assertEquals("Sanity check failed, couldn't find IColumn name added to a simple List", "S", col.getTableAlias());
		
		sqlModel.postProcess();
		assertEquals("Column was not correctly associated with its table", 1, shouldBeUsa.getWhereClauseColumnCount() );
		
		IColumn myColAssociatedWithTheTableThatOwnsIt = shouldBeUsa.getColumn(0);
		assertEquals("Could not find column in the parent table's list of columns", "SanFrancisco".toLowerCase(), myColAssociatedWithTheTableThatOwnsIt.getName());
	}
	@Test
	public void canDetermineWhetherTwoSqlHitSameTables() {
		
		IModelObservationMgr obs = DefaultFactory.getFactory().getObservationMgr();
		ISqlModel sqlModel_1 = factory.getSqlModel();
		sqlModel_1.setObservationMgr(obs);
		
		
		ITable table_1 = factory.getTable("USA");
		
		sqlModel_1.addTable(table_1);

		ISqlModel sqlModel_2 = factory.getSqlModel();
		sqlModel_2.setObservationMgr(obs);
		ITable table_2 = factory.getTable("USA");
		
		sqlModel_2.addTable(table_2);
		
		assertTrue("Unable to dectect that two sql stmts used same set of tables",sqlModel_1.matchesTablesOf(sqlModel_2));
		
		
	}
	@Test
	public void canDetermineWhetherTwoSqlHitSameTables_caseDifferences() {
		ISqlModel sqlModel_1 = factory.getSqlModel();
		ITable table_1 = factory.getTable("USA");
		
		sqlModel_1.addTable(table_1);

		ISqlModel sqlModel_2 = factory.getSqlModel();
		ITable table_2 = factory.getTable("usA");
		sqlModel_2.addTable(table_2);
		
		assertTrue("Unable to dectect that two sql stmts used same set of tables",sqlModel_1.matchesTablesOf(sqlModel_2));
		
		
	}
	@Test
	public void canDetermineWhetherTwoSqlHitSameTables_multipleTables() {
		ISqlModel sqlModel_a = factory.getSqlModel();
		ITable table_a1 = factory.getTable("USA");
		
		sqlModel_a.addTable(table_a1);
		ITable table_a2 = factory.getTable("Mexico");
		
		sqlModel_a.addTable(table_a2);

		ISqlModel sqlModel_b = factory.getSqlModel();
		ITable table_b1 = factory.getTable("USA");
		
		sqlModel_b.addTable(table_b1);
		ITable table_b2 = factory.getTable("Mexico");
		
		sqlModel_b.addTable(table_b2);
		
		assertTrue("Unable to dectect that two sql stmts used same set of tables",sqlModel_a.matchesTablesOf(sqlModel_b));
		
	}
	@Test
	public void canDetermineWhetherTwoSqlHitDifferentTables_multipleTables() {
		ISqlModel sqlModel_a = factory.getSqlModel();
		ITable table_a1 = factory.getTable("USA");
		
		sqlModel_a.addTable(table_a1);
		ITable table_a2 = factory.getTable("Mexico");
		
		sqlModel_a.addTable(table_a2);

		ISqlModel sqlModel_b = factory.getSqlModel();
		ITable table_b1 = factory.getTable("USA");
		sqlModel_b.addTable(table_b1);
		ITable table_b2 = factory.getTable("Canada");
		sqlModel_b.addTable(table_b2);
		
		assertFalse("Unable to dectect that two sql stmts used different set of tables",sqlModel_a.matchesTablesOf(sqlModel_b));
		
	}
	@Test
	public void canDetermineWhetherTwoSqlHitDifferentTables() {
		ISqlModel sqlModel_1 = factory.getSqlModel();
		ITable table_1 = factory.getTable("venus");
		sqlModel_1.addTable(table_1);

		ISqlModel sqlModel_2 = factory.getSqlModel();
		ITable table_2 = factory.getTable("mars");
		
		sqlModel_2.addTable(table_2);
		
		assertFalse("Unable to dectect that two sql stmts used same set of tables",sqlModel_1.matchesTablesOf(sqlModel_2));
		
		
	}
	@Test
	public void canDetactTableCountMisMatch() {
		ISqlModel sqlModel_a = factory.getSqlModel();
		ITable table_a1 = factory.getTable("USA");
		
		sqlModel_a.addTable(table_a1);
		ITable table_a2 = factory.getTable("Mexico");
		
		sqlModel_a.addTable(table_a2);

		ISqlModel sqlModel_b = factory.getSqlModel();
		ITable table_b1 = factory.getTable("USA");
		sqlModel_b.addTable(table_b1);
		
		assertFalse("Unable to dectect that two sql stmts are different",sqlModel_a.matchesTablesOf(sqlModel_b));
	}
	@Test
	public void canDetectTableWithoutJoin_simple() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table_1 = factory.getTable("USA");
		
		sqlModel.addTable(table_1);
		
		ITable[] tablesWithoutJoins = sqlModel.getTablesWithoutJoins();
		
		assertEquals("Should have found exactly 1 table.  This table is not joined with any other table", 1,tablesWithoutJoins.length);
		assertEquals("Should have found exactly 1 table.  This table is not joined with any other table", "usa",tablesWithoutJoins[0].getName());
		
	}
	@Test
	public void canDetectTableWithoutJoin_slightOverlap() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table_1 = factory.getTable("USA");
		sqlModel.addTable(table_1);
		ITable table_2 = factory.getTable("Mexico");
		sqlModel.addTable(table_2);
		
		ITable table_3 = factory.getTable("Canada");
		
		IBinaryOperatorExpression xpr = DefaultFactory.getFactory().getBinaryOperatorExpression();
		IColumn leftCol = DefaultFactory.getFactory().getColumn();
		leftCol.setTable(table_2);
		IColumn rightCol = DefaultFactory.getFactory().getColumn();
		rightCol.setTable(table_3);
		xpr.setLeftColumn(leftCol);
		xpr.setRightColumn(rightCol);

		sqlModel.addBinaryOperatorExpression(xpr);
		
		ITable[] tablesWithoutJoins = sqlModel.getTablesWithoutJoins();
		
		assertEquals("Should have found exactly 1 table.  This table is not joined with any other table", 1,tablesWithoutJoins.length);
		assertEquals("Should have found exactly 1 table.  This table is not joined with any other table", "usa",tablesWithoutJoins[0].getName());
	}
	@Test
	public void canDetectTableWithoutJoin_zeroResults() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table_2 = factory.getTable("Mexico");
		sqlModel.addTable(table_2);
		
		ITable table_3 = factory.getTable("Canada");
		sqlModel.addTable(table_3);
		
		IBinaryOperatorExpression xpr = DefaultFactory.getFactory().getBinaryOperatorExpression();
		IColumn leftCol = DefaultFactory.getFactory().getColumn();
		leftCol.setTable(table_2);
		IColumn rightCol = DefaultFactory.getFactory().getColumn();
		rightCol.setTable(table_3);
		
		xpr.setLeftColumn(leftCol);
		xpr.setRightColumn(rightCol);
		sqlModel.addBinaryOperatorExpression(xpr);
		
		ITable[] tablesWithoutJoins = sqlModel.getTablesWithoutJoins();
		
		assertEquals("Should have found exactly 0 table because both tables in the model are assigned inside joins", 0,tablesWithoutJoins.length);
	}
	@Test
	public void canDetectTableWithoutJoin_zeroResultsAndNotAddedToModelSeparately() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table_2 = factory.getTable("Mexico");
		
		ITable table_3 = factory.getTable("Canada");
		
		IBinaryOperatorExpression xpr = DefaultFactory.getFactory().getBinaryOperatorExpression();
		IColumn leftCol = DefaultFactory.getFactory().getColumn();
		leftCol.setTable(table_2);
		IColumn rightCol = DefaultFactory.getFactory().getColumn();
		leftCol.setTable(table_3);

		xpr.setLeftColumn(leftCol);
		xpr.setRightColumn(rightCol);
		sqlModel.addBinaryOperatorExpression(xpr);
		
		ITable[] tablesWithoutJoins = sqlModel.getTablesWithoutJoins();
		
		assertEquals("Should have found exactly 0 table because both tables in the model are assigned inside joins", 0,tablesWithoutJoins.length);
	}
	@Test 
	public void canSortTableNamesAlphbeticallyByTableName() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table_1 = factory.getTable("USA");
		sqlModel.addTable(table_1);
		ITable table_2 = factory.getTable("Canada");
		sqlModel.addTable(table_2);
		ITable table_3 = factory.getTable("Mexico");
		sqlModel.addTable(table_3);
		
		List<ITable> tables = sqlModel.getSortedTables();
		assertEquals("tables didn't get sorted alphbetically by table name", "canada", tables.get(0).getName() ); 
		assertEquals("tables didn't get sorted alphbetically by table name", "mexico", tables.get(1).getName() ); 
		assertEquals("tables didn't get sorted alphbetically by table name", "usa", tables.get(2).getName() ); 
		
		
		
	}
	@Test 
	public void canBuildKeyWithAlphaSortedTables() {
		ISqlModel sqlModel = factory.getSqlModel();
		sqlModel.setSqlType(SqlType.SELECT);
		ITable table_1 = factory.getTable("USA");
		sqlModel.addTable(table_1);
		ITable table_2 = factory.getTable("Canada");
		sqlModel.addTable(table_2);
		ITable table_3 = factory.getTable("Mexico");
		sqlModel.addTable(table_3);
		
		String key = sqlModel.getKey();
		assertTrue("could not find key with tables alpha sorted", key.startsWith("SELECT.canada~mexico~usa")); 
		
	}

}
