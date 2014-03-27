package org.wuqispank.test.level1.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.web.IFactory;

public class TestTableAndColumnModel {
	private IFactory factory = DefaultFactory.getFactory();
	@Test
	public void canAddFindTable_caseInsensitive() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table = factory.getTable();
		table.setName("USA");
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
	
	/**
	 * Create a model for this SQL, and validate the parts of the model assembled under the covers.
	 * This test creates a model of this sql:  SELECT S.SanFrancisco from USA S"
	 */
	@Test
	public void canUseTableAliasToAssociateColumnWithTable() {
		ISqlModel sqlModel = factory.getSqlModel();
		ITable table = factory.getTable();
		table.setName("USA");
		table.setAlias("S");
		sqlModel.addTable(table);
		sqlModel.addSelectListColumn("S","SanFrancisco");
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it", 1, sqlModel.getTableCount()); //bean-style getter for Wicket
		ITable shouldBeUsa = sqlModel.findTable("Usa");
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it (with the original name)", "USA".toLowerCase(), shouldBeUsa.getName() );
		assertEquals("Sanity check failed..big problem.  Just added a table and can't find it (with the original alias)", "S", shouldBeUsa.getAlias() );

		assertEquals("Sanity check failed, couldn't find IColumn added to a simple List", 1, sqlModel.getSelectListColumns().size());
		
		IColumn col = sqlModel.getSelectListColumns().get(0);
		assertEquals("Sanity check failed, couldn't find IColumn name added to a simple List", "SanFrancisco".toLowerCase(), col.getName());
		assertEquals("Sanity check failed, couldn't find IColumn name added to a simple List", "S", col.getTableAlias());
		
		sqlModel.postProcess();
		assertEquals("Column was not correctly associated with its table", 1, shouldBeUsa.getWhereClauseColumnCount() );
		
		IColumn myColAssociatedWithTheTableThatOwnsIt = shouldBeUsa.getColumn(0);
		assertEquals("Could not find column in the parent table's list of columns", "SanFrancisco".toLowerCase(), myColAssociatedWithTheTableThatOwnsIt.getName());
	}

}
