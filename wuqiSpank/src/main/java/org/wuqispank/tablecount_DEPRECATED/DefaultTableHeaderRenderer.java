package org.wuqispank.tablecount_DEPRECATED;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.wuqispank.model.ITable;
import org.wuqispank.ta_OLD.ITableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;

public class DefaultTableHeaderRenderer implements ITableHeaderRenderer {
	
	private ITable m_table;
	
	/**
	 * This is a zero based number showing how close to the left of the screen the table will be drawn.
	 * There will be one column for each table, like this:
	 * 0  1  2
	 * |  |  |
	 * |  |  |
	 * |  |  |
	 * 0  1  2

	 * 0 = all the way to the left.
	 * 1 = 1 from the left
	 */
	private int m_leftToRightPosition;
	private ITableHeaderConfiguration m_tableHeaderConfiguration;
	private int m_parentId_1;
	private int m_parentId_2;
	private MXGraphContext m_graphContext = null;
	
	@Override
	public MXGraphContext getGraphContext() {
		return m_graphContext;
	}
//	public void setGraphContext(MXGraphContext mxgc) {
//		m_graphContext = mxgc;
//	}

	@Override
	public void setTableHeaderConfiguration(ITableHeaderConfiguration val){
		m_tableHeaderConfiguration = val;
	}
	@Override
	public ITableHeaderConfiguration getTableHeaderConfiguration() {
		return m_tableHeaderConfiguration;
	}
	@Override
	public void setTable(ITable table) {
		m_table = table;
	}
	@Override
	public void setLeftToRightPosition(int i) {
		m_leftToRightPosition = i;
	}
	@Override
	public void setParentId_1(int i) {
		m_parentId_1 = i;
	}
	@Override
	public int getParentId_1() {
		return m_parentId_1;
	}
	@Override
	public void setParentId_2(int i) {
		m_parentId_2 = i;
	}
	@Override
	public void render(Writer writer) throws IOException {
		int tableHeaderX = this.getTableHeaderConfiguration().getLeftMarginPx();
		tableHeaderX += (getLeftToRightPosition() +1) * getTableHeaderConfiguration().getSpaceBetweenTablesPx();
		
		int idHeaderCircle = getGraphContext().incrementAndGetId();
		int idFooterCircle = getGraphContext().incrementAndGetId();
		int idEdge = getGraphContext().incrementAndGetId();
		
		writer.write("    <mxCell id=\""+idHeaderCircle+"\" value=\""  + getTable().getName() + "\" style=\"state\" vertex=\"1\" connectable=\"0\" parent=\""+getParentId_2()+"\">");
		writer.write("      <mxGeometry x=\"" + String.valueOf(tableHeaderX) + "\" y=\"30\" width=\"60\" height=\"60\" as=\"geometry\"/>");
		writer.write("    </mxCell>");
		
		writer.write("    <mxCell id=\""+idFooterCircle+"\" value=\""  + getTable().getName() + "\" style=\"state\" vertex=\"1\" connectable=\"0\" parent=\"7\">");
		writer.write("      <mxGeometry x=\"" + String.valueOf(tableHeaderX) + "\" y=\"30\" width=\"60\" height=\"60\" as=\"geometry\"/>");
		writer.write("    </mxCell>");
		writer.write("    <mxCell id=\""+idEdge+"\" style=\"endArrow=none;\" edge=\"1\" parent=\""+getParentId_1()+"\" source=\""+idHeaderCircle+"\" target=\""+idFooterCircle+"\">");
		writer.write("      <mxGeometry relative=\"1\" as=\"geometry\"/>");
		writer.write("    </mxCell>");
		
		//getWriter().flush();
	}
	private int getParentId_2() {
		return m_parentId_2;
	}
	private ITable getTable() {
		return m_table;
	}
	public int getLeftToRightPosition() {
		return m_leftToRightPosition;
	}
	public void setGraphContext(MXGraphContext val) {
		m_graphContext = val;
		
	}
	

}
