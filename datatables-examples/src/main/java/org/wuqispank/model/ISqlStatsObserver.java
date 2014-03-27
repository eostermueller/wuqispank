package org.wuqispank.model;

import java.util.Map;
import java.util.Observer;

public interface ISqlStatsObserver extends Observer {

	int getJoinCount(ITable table);
	int getTableCount(ITable table);
	Map<String, DefaultSqlStatsObserver.TableStats> getAllTables();
	double getAverageJoinsPerSql(ITable tableCriteria);

}
