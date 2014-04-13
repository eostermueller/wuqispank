package org.wuqispank.web.tablecount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.wuqispank.WuqispankException;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;
import org.wuqispank.web.IConfig;

import com.mxgraph.model.mxICell;

public class DefaultRow implements IRow, java.io.Serializable {

	private static final String SQL_ID_DELIM = "~";
	private Map<String,Object> m_tableMarkers = new HashMap<String,Object>();
	private Object m_groupLane;
	private String m_parentRqId;
	private int m_sqlSeq;
	private void put(String key, Object obj) {
		m_tableMarkers.put(key, obj);
	}
	private Object getTableMarker(ITable val) {
		IConfig cfg = ctx().getConfig();
		Object rc = m_tableMarkers.get( val.getName() );
		if (rc==null) {
			Object lane = ctx().getTableLaneMgr().getLane(val);
			
			
			
			rc = (mxICell)ctx().getGraph().insertVertex(
					lane, null, null, 
					cfg.getTableMarkerX(), 
					cfg.getTableMarkerYOffset()+ (cfg.getEvenOddRowHeight()*getIndex()),
					//cfg.getTableMarkerYOffset()+ (cfg.getTableMarkerY()*getIndex()),
					//cfg.getEvenOddRowHeight()+ (cfg.getTableMarkerY()*getIndex()),
					cfg.getTableMarkerWidth(), 
					cfg.getTableMarkerHeight(), 
					TableCountGraph.STYLE_WUQISPANK_SPHERE);
			put(val.getName(),rc);
		}
		return rc;
	}
	public DefaultRow(GraphContext ctx, ISqlWrapper sql, Object groupLane,String parentRequestId, int sqlSeq) {
		setGraphContext(ctx);
		setSqlWrapper(sql);
		setGroupLane(groupLane);
		setParentRequestId(parentRequestId);
		setSqlSeq(sqlSeq);
	}
	private void setSqlSeq(int sqlSeq) {
		m_sqlSeq = sqlSeq;
	}
	private void setParentRequestId(String parentRequestId) {
		m_parentRqId = parentRequestId;
	}
	private int getSqlSeq() {
		return m_sqlSeq;
	}
	private String getParentRequestId() {
		return m_parentRqId;
	}
	private void setGroupLane(Object val) {
		m_groupLane = val;
	}
	private Object getGroupLane() {
		return m_groupLane;
	}
	private GraphContext m_graphContext;
	private ISqlWrapper m_sqlWrapper;
	private Object m_rowLane;
	private int m_index;

	@Override
	public void setGraphContext(GraphContext val) {
		m_graphContext = val;
	}

	@Override
	public GraphContext ctx() {
		return m_graphContext;
	}

	@Override
	public void setSqlWrapper(ISqlWrapper val) {
		m_sqlWrapper = val;
	}

	@Override
	public ISqlWrapper getSqlWrapper() {
		return m_sqlWrapper;
	}

	@Override 
	public void setRowLane(Object val) {
		m_rowLane = val;
	}
	@Override
	public Object getRowLane() {
		return m_rowLane;
	}
	private int getIndex() {
		return m_index;
	}
	@Override
	public void render(int index) throws WuqispankException {
		
		renderRowLane( (index % 2 ) == 0 ? true : false);
		
		setIndex(index);
		ISqlModel model = getSqlWrapper().getSqlModel();
		for(Iterator<IBinaryOperatorExpression> itr = model.getBinaryOperatorExpressionsIterator(); itr.hasNext();) {
			IBinaryOperatorExpression joinExpression = (IBinaryOperatorExpression)itr.next();
			Object leftTableMarker = null;
			Object rightTableMarker = null;
			
			IColumn colLeft = joinExpression.getLeftColumn();
			if (colLeft==null)
				joinExpression.setLiteralComparison(true);
			else if (colLeft.getTable()==null)
				throw new WuqispankException("Found null table for 'left column' in join for sql [" + this.getSqlWrapper().getSqlText() + "]");
			else
				leftTableMarker = getTableMarker(colLeft.getTable());
			
			IColumn colRight = joinExpression.getRightColumn();
			if (colRight==null)
				joinExpression.setLiteralComparison(true);
			else if (colRight.getTable()==null)
				throw new WuqispankException("Found null table for 'right column' in join for sql [" + this.getSqlWrapper().getSqlText() + "]");
			else
				rightTableMarker = getTableMarker(colRight.getTable());
			
			
			if (!joinExpression.isLiteralComparison())
				ctx().getGraph().insertEdge(ctx().getMasterPool(), null,"", leftTableMarker, rightTableMarker, TableCountGraph.STYLE_WUQISPANK_JOIN_EDGE);
			
		}
		
		/**
		 * Render tables not involved in join expressions.
		 * Example:  select foo from bar.  bang.  Only one table.  No where clause, no join.
		 * Cartesian products would be another example, although a rare one.
		 */
		for( ITable table : model.getTablesWithoutJoins() ) {
			getTableMarker(table);

		}
	}
	private void setIndex(int val) {
		m_index = val;
	}
	private String getCombinedId() {
		return getParentRequestId() + SQL_ID_DELIM + String.valueOf(getSqlSeq()).trim();
	}
	private void renderRowLane(boolean even) {
		String style = null;
		if (even)
			style = TableCountGraph.STYLE_WUQISPANK_ROW_EVEN;
		else 
			style = TableCountGraph.STYLE_WUQISPANK_ROW_ODD;
		Object evenOddLane = ctx().getGraph().insertVertex( 
				getGroupLane(), getCombinedId(), null, 
				0, 0, 
				ctx().getConfig().getEvenOddRowWidth(), 
				ctx().getConfig().getEvenOddRowHeight(), style);
		setRowLane(evenOddLane);
	}

}
