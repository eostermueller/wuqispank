package org.wuqispank.model;

import org.wuqispank.model.DefaultSqlStatsObserver.TableStats;

public interface ITableOrderMgr {

	String[] reorderTables(ISqlStatsObserver m_sqlStats);

	String[] getOldOrder(ISqlStatsObserver sqlStats);

}
