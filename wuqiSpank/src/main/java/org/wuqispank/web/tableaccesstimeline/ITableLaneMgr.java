package org.wuqispank.web.tableaccesstimeline;

import org.wuqispank.model.ITable;
import org.wuqispank.web.IConfig;

import com.mxgraph.model.mxICell;

/**
 * keeps column start/stop x,y positions for each table-specific vertical lane
 * @author erikostermueller
 *
 */
public interface ITableLaneMgr {

	void setConfig(IConfig config);

	IConfig getConfig();

	void setTableLaneOrder(String[] reorderTables);

	String[] getTableLaneOrder();
	
	void setLanes(mxICell[] val);
	mxICell[] getLanes();

	Object getLane(ITable table);

	ITable[] getTableLaneOrder2();

}
