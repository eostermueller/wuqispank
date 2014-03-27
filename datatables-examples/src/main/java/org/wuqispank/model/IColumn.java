package org.wuqispank.model;

public interface IColumn {
	String getName();
	void setName(String val);
	void setTableAlias(String tableAlias);
	String getTableAlias();
	void setTable(ITable table);
	ITable getTable();
	boolean equals(String columnCriteria, String tableNameCriteria);
	boolean isDetached();
}
