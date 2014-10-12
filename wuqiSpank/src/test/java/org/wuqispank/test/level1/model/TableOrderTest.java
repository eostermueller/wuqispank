package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.model.DefaultSqlStatsObserver.TableStats;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.IModelObservationMgr;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlStatsObserver;
import org.wuqispank.model.ITable;
import org.wuqispank.model.ITableOrderMgr;

/**
 * The TableCountGraph depicts rows of sql statements (in order of execution) executed by the SUT.
 * There is one column for each RDBMS table accessed.
 * Which table should be in the leftmost column?  The rightmost?
 * 
 * @author erikostermueller
 *
 */
public class TableOrderTest {
	ISqlStatsObserver m_sqlStats_evenTableCount = null;
	private ISqlStatsObserver m_sqlStats_oddTableCount; 
	
	
	

	/**
	 *   Given this order: <PRE>
			lefttable-1
			righttable-1
			lefttable-5
			righttable-5
			lefttable-13
			righttable-13
			lefttable-14
			righttable-14
			lefttable-15
			righttable-15
			</PRE>
			reorder so the largest ones are in the center of the list
			and the smallest numbers are either at the beginning or end of the list.
	 */
	@Test
	public void canOrderUsingCenterHeavyAlgo_tableCount_Even() {
		
		String[] expectedOrder = { 
			    "lefttable-1",  //Tables with the __lowest__ averages of being joined.
			    "lefttable-5",
			    "lefttable-13",
			    "lefttable-14",
			    "lefttable-15",  //Tables with the highest averages of being joined.
			    "righttable-15", //Tables with the highest averages of being joined. 
			    "righttable-14",
			    "righttable-13",
			    "righttable-5",
			    "righttable-1"  //Tables with the __lowest__ averages of being joined.
			    };
		
		
		ITableOrderMgr tableOrderMgr = DefaultFactory.getFactory().getTableOrderMgr();
		String[] centerHeavyOrder = tableOrderMgr.reorderTables(m_sqlStats_evenTableCount);
		
		assertArrayEquals(expectedOrder, centerHeavyOrder);

	}
	
	/**
	 * Asserts that we've got the same order that we verified in TestObservationManager.
	 */
	@Test
	public void canRetainSanity_even() {
		String[] expectedOrder = {
                "lefttable-1",
                "righttable-1",
               "lefttable-5",
               "righttable-5",
               "lefttable-13",
               "righttable-13",
               "lefttable-14",
               "righttable-14",
               "lefttable-15",
               "righttable-15"
		};
		ITableOrderMgr tableOrderMgr = DefaultFactory.getFactory().getTableOrderMgr();
		String[] centerHeavyOrder = tableOrderMgr.getOldOrder(m_sqlStats_evenTableCount);
		
		assertArrayEquals(expectedOrder, centerHeavyOrder);
	}
	/**
	 * Asserts that we've got the same order that we verified in TestObservationManager.
	 */
	@Test
	public void canRetainSanity_odd() {
		String[] expectedOrder = {
                "lefttable-1",
                "righttable-1",
               "lefttable-5",
               "righttable-5",
               "lefttable-13",
               "lefttable-14",
               "righttable-14",
               "lefttable-15",
               "righttable-15"
		};
		ITableOrderMgr tableOrderMgr = DefaultFactory.getFactory().getTableOrderMgr();
		String[] centerHeavyOrder = tableOrderMgr.getOldOrder(m_sqlStats_oddTableCount);
		
		assertArrayEquals(expectedOrder, centerHeavyOrder);
	}
	
	@Test
	public void canOrderUsingCenterHeavyAlgo_tableCount_Odd() {
		String[] expectedOrder = { 
			    "lefttable-1",  //Tables with the __lowest__ averages of being joined.
			    "lefttable-5",
			    "lefttable-13",
			    "righttable-14",
			    "righttable-15", //Tables with the highest averages of being joined. 
			    "lefttable-15",  //Tables with the highest averages of being joined.
			    "lefttable-14",
			    "righttable-5",
			    "righttable-1"  //Tables with the __lowest__ averages of being joined.
			    };
		
		
		ITableOrderMgr tableOrderMgr = DefaultFactory.getFactory().getTableOrderMgr();
		String[] centerHeavyOrder = tableOrderMgr.reorderTables(m_sqlStats_oddTableCount);
		
		assertArrayEquals(expectedOrder, centerHeavyOrder);
		
	}
	
	@Before 
	public void setup_odd() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		m_sqlStats_oddTableCount = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(m_sqlStats_oddTableCount);
		observationMgr.registerNewSqlListener(m_sqlStats_oddTableCount);
		observationMgr.registerNewTableListener(m_sqlStats_oddTableCount);
		
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
		createJoin(sqlModel1, "leftTable-13", "rightTable-1", 13); observationMgr.addNewSql();
	}
	/**
	 * This scenario was taken from TestObservationManager
	 */
	@Before
	public void setup_even() {
		IModelObservationMgr observationMgr = DefaultFactory.getFactory().getObservationMgr();
		
		m_sqlStats_evenTableCount = DefaultFactory.getFactory().getSqlStatsCounter();
		observationMgr.registerNewJoinListener(m_sqlStats_evenTableCount);
		observationMgr.registerNewSqlListener(m_sqlStats_evenTableCount);
		observationMgr.registerNewTableListener(m_sqlStats_evenTableCount);
		
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
		
		
	}
	private void createJoin(ISqlModel sqlModel, String leftTableName, String rightTableName ,int numJoins) {

		ITable leftTable = DefaultFactory.getFactory().getTable(leftTableName);
		
		IColumn leftColumn = DefaultFactory.getFactory().getColumn();
		leftColumn.setTable(leftTable);

		ITable rightTable = DefaultFactory.getFactory().getTable(rightTableName);
		
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
