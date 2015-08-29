package org.wuqispank.model;

public interface IColumn {
	String getName();
	void setName(String val);
	
	/**
	 * Much of the column parsing happens before the table's
	 * alias has been tied back to the actual table name.
	 * 
	 * This code stores the table alias (and a and b in 'WHERE a.id = b.id')
	 * in {@link IColumn#setTableAlias(String)} where it can later be
	 * tied back to the table name, in {@link IColumn#getTable()#getTableAlias()}
	 * 
	 * @param tableAlias
	 */
	void setTableAlias(String tableAlias);
	String getTableAlias();
	void setTable(ITable table);
	ITable getTable();
	boolean equals(String columnCriteria, String tableNameCriteria);
	boolean isDetached();
	boolean validate();
}
