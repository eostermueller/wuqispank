package org.wuqispank.web.tablecount;

import org.wuqispank.web.IConfig;

import com.mxgraph.view.mxGraph;

public class GraphContext implements java.io.Serializable {
	ITableLaneMgr m_tableLaneMgr;
	IConfig m_config;
	private transient mxGraph m_graph;
	Object m_masterPool;
	public ITableLaneMgr getTableLaneMgr() {
		return m_tableLaneMgr;
	}
	public void setTableLaneMgr(ITableLaneMgr tableLaneMgr) {
		this.m_tableLaneMgr = tableLaneMgr;
	}
	public IConfig getConfig() {
		return m_config;
	}
	public void setConfig(IConfig config) {
		this.m_config = config;
	}
	public mxGraph getGraph() {
		return m_graph;
	}
	public void setGraph(mxGraph graph) {
		this.m_graph = graph;
	}
	public Object getMasterPool() {
		return m_masterPool;
	}
	public void setMasterPool(Object masterPool) {
		this.m_masterPool = masterPool;
	}
}