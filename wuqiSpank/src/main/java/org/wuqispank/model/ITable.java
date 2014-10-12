package org.wuqispank.model;

import java.util.List;

public interface ITable extends IBaseTable {
	
//	List<IColumn> getColumns();

	void setSchema(String schema);

	String getSchema();

	IColumn findColumn(String nameCriteria);

	void setAlias(String alias);

	String getAlias();

	void addColumn(IColumn val);

	int getWhereClauseColumnCount();
	
	public boolean shouldBeCached();
	public void setShouldBeCached(boolean val);

	public abstract IColumn getColumn(int val);
	
}
