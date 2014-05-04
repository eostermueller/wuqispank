package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.wuqispank.DefaultFactory;
import org.junit.Test;
import org.wuqispank.model.DefaultSqlStatsObserver.TableStats;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.IModelObservationMgr;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlStatsObserver;
import org.wuqispank.model.ITable;

public class ObservationManagerTest {

	@Test
	public void canDetectSingleNewTable() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsObserver = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewTableListener(sqlStatsObserver);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ITable table1_1 = DefaultFactory.getFactory().getTable();
		table1_1.setName("table1_1");
		sqlModel1.addTable(table1_1);
		assertEquals(
				"SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsObserver.getTableCount(table1_1) );
	}
	@Test
	public void canCountMultipleNewTables() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsObserver = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewTableListener(sqlStatsObserver);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ITable table1_1 = DefaultFactory.getFactory().getTable();
		table1_1.setName("table1_1");
		sqlModel1.addTable(table1_1);
		assertEquals(
				"SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsObserver.getTableCount(table1_1) );
		
		/**
		 * 
		 * 
		 * Now add the 2nd table
		 * from a different model
		 */
		ISqlModel sqlModel2 = DefaultFactory.getFactory().getSqlModel();
		sqlModel2.setObservationMgr(observationMgr);
		
		/**
		 * doesn't matter whether I add the table to the
		 * first model or the second model...they get tallied as a 
		 * single total across all ISqlModel instances that were 
		 * registered with setObservationMgr();
		 * This will allow us to tally metrics across multiple SQL statement.
		 */
		sqlModel2.addTable(table1_1);
		sqlModel1.addTable(table1_1);
		sqlModel2.addTable(table1_1);
		assertEquals(
				"SqlStatsCounter didn't tally the correct number of tables added", 
				4, 
				sqlStatsObserver.getTableCount(table1_1) );

	}

	@Test
	public void canDetectSingleNewJoin() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsCounter = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ITable leftTable = DefaultFactory.getFactory().getTable();
		leftTable.setName("leftTable");
		IColumn leftColumn = DefaultFactory.getFactory().getColumn();
		leftColumn.setTable(leftTable);

		ITable rightTable = DefaultFactory.getFactory().getTable();
		rightTable.setName("rightTable");
		IColumn rightColumn = DefaultFactory.getFactory().getColumn();
		rightColumn.setTable(rightTable);
		
		IBinaryOperatorExpression expr = DefaultFactory.getFactory().getBinaryOperatorExpression();
		expr.setLeftColumn(leftColumn);
		expr.setRightColumn(rightColumn);
		sqlModel1.addBinaryOperatorExpression(expr);
		
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsCounter.getJoinCount(rightTable) );
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsCounter.getJoinCount(leftTable) );
	}
	@Test
	public void canDetectMultipleNewJoins() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsCounter = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ITable leftTable = DefaultFactory.getFactory().getTable();
		leftTable.setName("leftTable");
		IColumn leftColumn = DefaultFactory.getFactory().getColumn();
		leftColumn.setTable(leftTable);

		ITable rightTable = DefaultFactory.getFactory().getTable();
		rightTable.setName("rightTable");
		IColumn rightColumn = DefaultFactory.getFactory().getColumn();
		rightColumn.setTable(rightTable);
		
		IBinaryOperatorExpression expr = DefaultFactory.getFactory().getBinaryOperatorExpression();
		expr.setLeftColumn(leftColumn);
		expr.setRightColumn(rightColumn);
		sqlModel1.addBinaryOperatorExpression(expr);
		
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsCounter.getJoinCount(rightTable) );
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				1, 
				sqlStatsCounter.getJoinCount(leftTable) );
		
		/**
		 * Now use a second model, but keep the same tally.
		 * Just as with new tables, doesn't matter whether we're
		 * adding to sqlModel1 or sqlModel2.
		 * A single tally is kept of all table additions.
		 * This will allow us to tally metrics across multiple SQL statement.
		 */
		ISqlModel sqlModel2 = DefaultFactory.getFactory().getSqlModel();
		sqlModel2.setObservationMgr(observationMgr);

		sqlModel1.addBinaryOperatorExpression(expr);
		sqlModel2.addBinaryOperatorExpression(expr);
		sqlModel1.addBinaryOperatorExpression(expr);
		sqlModel2.addBinaryOperatorExpression(expr);
		sqlModel1.addBinaryOperatorExpression(expr);
		sqlModel2.addBinaryOperatorExpression(expr);
		
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				7, 
				sqlStatsCounter.getJoinCount(rightTable) );
		assertEquals("SqlStatsCounter didn't tally the correct number of tables added", 
				7, 
				sqlStatsCounter.getJoinCount(leftTable) );
		
	}
	@Test
	public void canGetCountOfZero() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsObserver = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewTableListener(sqlStatsObserver);
		
		ITable table1_1 = DefaultFactory.getFactory().getTable();
		table1_1.setName("table1_1");
		assertEquals(
				"SqlStatsCounter didn't tally the correct number of tables added", 
				0, 
				sqlStatsObserver.getTableCount(table1_1) );
	}
	@Test
	public void canCalculateAverageJoinPerSqlStatement() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsCounter = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		observationMgr.registerNewSqlListener(sqlStatsCounter);
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ISqlModel sqlModel2 = DefaultFactory.getFactory().getSqlModel();
		sqlModel2.setObservationMgr(observationMgr);

		/**
		 * Just two invocations for this pair of columns
		 */
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); 
		observationMgr.addNewSql();
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); 
		observationMgr.addNewSql();
		
		ITable rightTable_5 = DefaultFactory.getFactory().getTable("rightTable-5"); 
		assertEquals("wrong average of joins per sql", 
				5,
				sqlStatsCounter.getAverageJoinsPerSql(rightTable_5),
				.5);
		
	}	
	@Test
	public void canCalculateManyAverages_JoinCountPerSqlStatement() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsCounter = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		observationMgr.registerNewSqlListener(sqlStatsCounter);
		observationMgr.registerNewTableListener(sqlStatsCounter);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ISqlModel sqlModel2 = DefaultFactory.getFactory().getSqlModel();
		sqlModel2.setObservationMgr(observationMgr);

		/**
		 * Just two invocations for this pair of columns
		 */
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); observationMgr.addNewSql();
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); observationMgr.addNewSql();
		
		/**  eleven SQL statements for this pair of columns
		 * Build up a history of SQL invocations for this pair of tables. 
		 */
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();

		createJoin(sqlModel1, "leftTable-14", "rightTable-14", 14); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-15", "rightTable-15", 15); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-13", "rightTable-13", 13); observationMgr.addNewSql();
		
		
		ITable rightTable_1 = DefaultFactory.getFactory().getTable("rightTable-1"); 
		assertEquals("wrong average of joins per sql", 1,sqlStatsCounter.getAverageJoinsPerSql(rightTable_1),.5);

		ITable rightTable_5 = DefaultFactory.getFactory().getTable("rightTable-5"); 
		assertEquals("wrong average of joins per sql", 5,sqlStatsCounter.getAverageJoinsPerSql(rightTable_5),.5);

		ITable rightTable_13 = DefaultFactory.getFactory().getTable("rightTable-13"); 
		assertEquals("wrong average of joins per sql", 13,sqlStatsCounter.getAverageJoinsPerSql(rightTable_13),.5);

		ITable rightTable_14 = DefaultFactory.getFactory().getTable("rightTable-14"); 
		assertEquals("wrong average of joins per sql", 14,sqlStatsCounter.getAverageJoinsPerSql(rightTable_14),.5);

		ITable rightTable_15 = DefaultFactory.getFactory().getTable("rightTable-15"); 
		assertEquals("wrong average of joins per sql", 15,sqlStatsCounter.getAverageJoinsPerSql(rightTable_15),.5);
	}	
	
	@Test
	public void canSortTablesByAverageJoinCount() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		ISqlStatsObserver sqlStatsCounter = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(sqlStatsCounter);
		observationMgr.registerNewSqlListener(sqlStatsCounter);
		observationMgr.registerNewTableListener(sqlStatsCounter);
		
		ISqlModel sqlModel1 = DefaultFactory.getFactory().getSqlModel();
		sqlModel1.setObservationMgr(observationMgr);
		
		ISqlModel sqlModel2 = DefaultFactory.getFactory().getSqlModel();
		sqlModel2.setObservationMgr(observationMgr);

		/**
		 * Just two invocations for this pair of columns
		 */
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); observationMgr.addNewSql();
		createJoin(sqlModel2, "leftTable-5", "rightTable-5", 5); observationMgr.addNewSql();
		
		/**  eleven SQL statements for this pair of columns
		 * Build up a history of SQL invocations for this pair of tables. 
		 */
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-1", "rightTable-1", 1); observationMgr.addNewSql();

		createJoin(sqlModel1, "leftTable-14", "rightTable-14", 14); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-15", "rightTable-15", 15); observationMgr.addNewSql();
		createJoin(sqlModel1, "leftTable-13", "rightTable-13", 13); observationMgr.addNewSql();
		
		System.out.println("Left compareTo(right) [" + "left".compareTo("right") + "]");
		int myCounter = 0;
		Iterator<TableStats> itr = sqlStatsCounter.getOrderedTables().values().iterator();
		while (itr.hasNext()) {
			TableStats tableStats = itr.next();
			System.out.println("table count [" + tableStats.getAverageJoinsPerSql() + "]");
			switch(myCounter++) {
				case 0:
					assertEquals("Was expecting the table with smallest average", "lefttable-1",tableStats.getTable().getName());
					assertEquals("Was expecting the table with smallest average", 1,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 1:
					assertEquals("Was expecting the table with smallest average", "righttable-1",tableStats.getTable().getName());
					assertEquals("Was expecting the table with smallest average", 1,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 2:
					assertEquals("Was expecting the table with 2nd smallest average", "lefttable-5",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 2nd smallest average", 5,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 3:
					assertEquals("Was expecting the table with 2nd smallest average", "righttable-5",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 2nd smallest average", 5,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 4:
					assertEquals("Was expecting the table with 3rd largest average", "lefttable-13",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 3rd smallest average", 13,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 5:
					assertEquals("Was expecting the table with 3rd smallest average", "righttable-13",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 3rd smallest average", 13,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 6:
					assertEquals("Was expecting the table with 2nd largest average", "lefttable-14",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 2nd largest average", 14,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 7:
					assertEquals("Was expecting the table with 2nd largest average", "righttable-14",tableStats.getTable().getName());
					assertEquals("Was expecting the table with 2nd largest", 14,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 8:
					assertEquals("Was expecting the table with largest average", "lefttable-15",tableStats.getTable().getName());
					assertEquals("Was expecting the table with largest average", 15,tableStats.getAverageJoinsPerSql(), .5);
					break;
				case 9:
					assertEquals("Was expecting the table with largest average", "righttable-15",tableStats.getTable().getName());
					assertEquals("Was expecting the table with largest", 15,tableStats.getAverageJoinsPerSql(), .5);
					break;
			}
		}		
		
	}
	private void createJoin(ISqlModel sqlModel, String leftTableName, String rightTableName ,int numJoins) {

		ITable leftTable = DefaultFactory.getFactory().getTable();
		leftTable.setName(leftTableName);
		IColumn leftColumn = DefaultFactory.getFactory().getColumn();
		leftColumn.setTable(leftTable);

		ITable rightTable = DefaultFactory.getFactory().getTable();
		rightTable.setName(rightTableName);
		IColumn rightColumn = DefaultFactory.getFactory().getColumn();
		rightColumn.setTable(rightTable);
		
		for(int i = 0; i < numJoins; i++) {
			IBinaryOperatorExpression expr = DefaultFactory.getFactory().getBinaryOperatorExpression();
			expr.setLeftColumn(leftColumn);
			expr.setRightColumn(rightColumn);
			sqlModel.addBinaryOperatorExpression(expr);
		}
	}
}

 