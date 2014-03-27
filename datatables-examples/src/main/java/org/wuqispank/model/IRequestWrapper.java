package org.wuqispank.model;

import java.io.Serializable;
import java.util.List;

import org.intrace.client.request.IRequest;
import org.wuqispank.WuqispankException;

public interface IRequestWrapper extends Serializable {

	ISqlModel getSqlModel();
	
	/**
	 * Metrics aggregated for all SQL statements in this request.
	 * @param val
	 */
	void setSqlMetrics(ISqlModel val);

	void setRequest(IRequest request) throws WuqispankException;

	IRequest getRequest();
	
	List<ISqlWrapper> getSql();

	void setColumnCount(int aggregateColumnCount);

	int getColumnCount();

	void setTableCount(int aggregateTableCount);

	int getTableCount();

	int getSqlCount();

	String getTinyId();

	int getSqlStatementCount();

	IModelObservationMgr getModelObservationMgr();
	void setModelObservationMgr(IModelObservationMgr val);

	String getUniqueId();
	void setUniqueId(String val);

}