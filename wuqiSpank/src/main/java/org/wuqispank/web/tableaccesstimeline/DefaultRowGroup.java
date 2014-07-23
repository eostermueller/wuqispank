package org.wuqispank.web.tableaccesstimeline;

import java.util.ArrayList;
import java.util.List;

import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.ISqlWrapper;

import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

public class DefaultRowGroup implements IRowGroup, java.io.Serializable {

	List<ISqlWrapper> m_sqlList = new ArrayList<ISqlWrapper>();
	private ITableLaneMgr m_tableLaneMgr;
	private GraphContext m_ctx;
	private mxICell m_laneRowGroup;
	private int m_index;
	private String m_requestId;
	public ITableLaneMgr getTableLaneMgr() {
		return m_tableLaneMgr;
	}
	public DefaultRowGroup(GraphContext graphContext, String requestId) {
		setGraphContext(graphContext);
		setRequestId(requestId);
	}
	private void setRequestId(String val) {
		m_requestId = val;
	}
	private String getRequestId() {
		return m_requestId;
	}
	private void setGraphContext(GraphContext val) {
		m_ctx = val;
	}

	private GraphContext ctx() {
		return m_ctx;
	}

	@Override
	public void add(ISqlWrapper val) {
		getSqlList().add(val);
	}

	private List<ISqlWrapper> getSqlList() {
		return m_sqlList;
	}

	@Override
	public int count() {		
		return getSqlList().size();
	}

	@Override
	public void render(int index) throws WuqispankException {
		
		mxICell laneRowGroup = (mxICell)ctx().getGraph().insertVertex(
				ctx().getMasterPool(), 
				getRequestId() + index, "", 10,10,100,50, TableAccessTimeline.STYLE_WUQISPANK_SWIMLANE);
		
		laneRowGroup.setCollapsed(index==0 ? false : true);
		
		setRowGroupLane(laneRowGroup);
		
		mxICell[] lanes = renderVerticalTableLanes();
		ctx().getTableLaneMgr().setLanes(lanes);
		
		int count = 0;
		List<Object> listEvenOddRows = new ArrayList<Object>();
		for(ISqlWrapper sqlWrapper : getSqlList()) {
			IRow row = DefaultFactory.getFactory().createRow(ctx(), sqlWrapper, laneRowGroup, getRequestId(), sqlWrapper.getSequence() );
			row.render(count++);
			listEvenOddRows.add( row.getRowLane() );
		}
		Object[] prototype = {};
		Object[] aryEvenOddRows = listEvenOddRows.toArray(prototype);
		ctx().getGraph().orderCells(true,aryEvenOddRows);
		layout();
	}

	private void layout() {
		mxStackLayout layout = new mxStackLayout(ctx().getGraph(),false) {
			public mxStackLayout init() {
				this.resizeParent = true;
				this.y0 = 0;
				this.x0 = 0;
				return this;
			}
			public boolean isVertexMovable(Object vertex) {
				boolean ynMovable = true;
				for(mxICell cell : ctx().getTableLaneMgr().getLanes() ) {
					if (vertex==cell)
						return false;
				}
				return ctx().getGraph().isCellMovable(vertex);
			}
		}.init();
		layout.execute(getRowGroupLane());
	}
	private void setRowGroupLane(mxICell val) {
		m_laneRowGroup = val;
	}
	private mxICell getRowGroupLane() {
		return m_laneRowGroup;
	}

	private mxICell[] renderVerticalTableLanes() {
		List<mxICell> myVerticalTableLanes = new ArrayList<mxICell>();
		int x = ctx().getConfig().getXStartLeftMostTableLane();
		for(String tableName : ctx().getTableLaneMgr().getTableLaneOrder() ) {
			mxICell insertVertex = (mxICell)ctx().getGraph().insertVertex(
					getRowGroupLane(), 
					null, 
					null, 
					x - ctx().getConfig().getXNegOffset(), 
					0,
					ctx().getConfig().getWidthOfVerticalTableLane(),
					ctx().getConfig().getHeightOfVerticalTableLane(), 
					TableAccessTimeline.STYLE_WUQISPANK_VERTICAL_TABLE_LANE);
			myVerticalTableLanes.add(insertVertex);
			x+=ctx().getConfig().getXSpaceBetwenTableLanes();
		}
		mxICell[] prototype = {};
		return myVerticalTableLanes.toArray(prototype);
		
	}


}
