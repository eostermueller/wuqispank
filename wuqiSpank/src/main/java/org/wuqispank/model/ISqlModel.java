package org.wuqispank.model;

import java.util.Iterator;
import java.util.List;

public interface ISqlModel {
	public static String NOT_SPECIFIED = "<notspecified>";
	public static int NOT_INITIALIZED = -1;

	public boolean isParsedSuccessfully();
	void setTables(List<ITable> t);

	void setColumnCount(int columnCount);

	int getColumnCount();

	int getTableCount();
	ITable findTable(String nameCriteria);
	void addSelectListColumn(String tableName, String columnName);
	ITable findTableGivenAlias(String aliasCriteria);
	List<IColumn> getSelectListColumns();
	void postProcess();
	IColumn findSelectListColumn(String columnName, String tableAlias);
	IColumn addWhereClauseColumn(String tableAlias, String columnName);

	void addTable(ITable val);

	ITable getTable(int val);

	void addBinaryOperatorExpression(IBinaryOperatorExpression val);

	Iterator<IBinaryOperatorExpression> getBinaryOperatorExpressionsIterator();

	void setObservationMgr(IModelObservationMgr observationMgr);

	String getHumanReadableTableNames();

	boolean matchesTablesOf(ISqlModel val);

	boolean tableExists(ITable table);

	ITable[] getTablesWithoutJoins();

	SqlType getSqlType();

	void setSqlType(SqlType val);
	Throwable getParseException();
	void setParseException(Throwable val);
	void setParser(Class clazz);
	/**
	 * Fuller featured parsers are can extract more data items, but they're also more vulnerable
	 * to parsing errors on vendor-specific SQL.
	 * @return
	 */
	Class getParser();
	List<ITable> toTableList(String[] ary);
	List<ITable> getTables();
}
