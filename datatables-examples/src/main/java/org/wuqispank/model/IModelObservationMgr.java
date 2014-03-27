package org.wuqispank.model;

import java.io.Serializable;
import java.util.Observer;

public interface IModelObservationMgr extends Serializable {

	void registerNewTableListener(Observer sqlStatsCounter);

	void registerNewJoinListener(Observer sqlStatsCounter);

	public void addNewJoin(IBinaryOperatorExpression expression);

	public abstract void addNewTable(ITable table);

	void addNewSql();

	void registerNewSqlListener(ISqlStatsObserver sqlStatsCounter);

}
