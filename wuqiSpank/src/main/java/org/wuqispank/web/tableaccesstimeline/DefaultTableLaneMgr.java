package org.wuqispank.web.tableaccesstimeline;


import org.wuqispank.DefaultFactory;
import org.wuqispank.model.ITable;
import org.wuqispank.web.IConfig;

import com.mxgraph.model.mxICell;

public class DefaultTableLaneMgr implements ITableLaneMgr, java.io.Serializable {

	private IConfig m_config = null;
	private String[] m_tableOrder;
	private mxICell[] m_tableLanes; 
	@Override
	public void setConfig(IConfig val) {
		m_config = val;
	}
	@Override
	public IConfig getConfig() {
		return m_config;
	}
	@Override
	public void setTableLaneOrder(String[] val) {
		m_tableOrder = val;
		
	}
	@Override
	public String[] getTableLaneOrder() {
		return m_tableOrder;
	}
	@Override
	public ITable[] getTableLaneOrder2() {
		ITable[] tableList = new ITable[m_tableOrder.length];
		for(int i = 0; i < m_tableOrder.length; i++) {
			tableList[i] = DefaultFactory.getFactory().getTable();
			tableList[i].setName(m_tableOrder[i]);
		}
		return tableList;
	}
	@Override
	public void setLanes(mxICell[] val) {
		m_tableLanes = val;
	}
	private int getLaneIndex(ITable criteria) {
		String[] tableNames = getTableLaneOrder();
		int rc = -1;
		for(int i = 0; i < tableNames.length; i++) {
			if (criteria.getName().equals(tableNames[i].toLowerCase())) {
					rc = i;
					break;
			}
		}
		return rc;
	}
	@Override
	public Object getLane(ITable table) {
		int index = getLaneIndex(table);
		Object rc = null;
		if (index>=0)
			//rc = getTableLaneOrder()[index];
			rc = getLanes()[index];
		return rc;
	}
	@Override
	public mxICell[] getLanes() {
		return m_tableLanes;
	}
}
