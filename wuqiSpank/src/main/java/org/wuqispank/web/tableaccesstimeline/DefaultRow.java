package org.wuqispank.web.tableaccesstimeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;
import org.wuqispank.model.SqlType;
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
	private Object getTableMarker(ITable table) {
		IConfig cfg = ctx().getConfig();
		Object rc = m_tableMarkers.get( table.getName() );
		if (rc==null) {
			Object lane = ctx().getTableLaneMgr().getLane(table);
			
			SqlType sqlType = this.getSqlWrapper().getSqlModel().getSqlType();
			boolean isParseException = !this.getSqlWrapper().getSqlModel().isParsedSuccessfully();
			rc = (mxICell)ctx().getGraph().insertVertex(
					lane, null, null, 
					cfg.getTableMarkerX(), 
					cfg.getTableMarkerYOffset()+ (cfg.getEvenOddRowHeight()*getIndex()),
					cfg.getTableMarkerWidth(), 
					cfg.getTableMarkerHeight(),
					TableAccessTimeline.getTableAccessImage( sqlType,isParseException ));
			put(table.getName(),rc);
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
		if (model.isParsedSuccessfully())
			renderSuccess(model);
		else 
			renderException(model);
		
	}
	private void renderException(ISqlModel model) {
		ITableLaneMgr tableLaneMgr = ctx().getTableLaneMgr();
		for(ITable table : tableLaneMgr.getTableLaneOrder2())
			getTableMarker(table);
	}
	private void renderSuccess(ISqlModel model) throws WuqispankException {
		for(Iterator<IBinaryOperatorExpression> itr = model.getBinaryOperatorExpressionsIterator(); itr.hasNext();) {
			IBinaryOperatorExpression joinExpression = (IBinaryOperatorExpression)itr.next();
			Object leftTableMarker = null;
			Object rightTableMarker = null;
			
			IColumn colLeft = joinExpression.getLeftColumn();
			if (colLeft==null)
				joinExpression.setLiteralComparison(true);
			else if (colLeft.getTable()==null)
				throw new WuqispankException("Found null table for \"left column\" in join for sql [" + this.getSqlWrapper().getSqlText() + "].  sql parsed by [" + model.getParser().getName() + "]");
			else
				leftTableMarker = getTableMarker(colLeft.getTable());
			
			IColumn colRight = joinExpression.getRightColumn();
			if (colRight==null)
				joinExpression.setLiteralComparison(true);
			else if (colRight.getTable()==null)
				throw new WuqispankException("Found null table for \"right column\" in join for sql [" + this.getSqlWrapper().getSqlText() + "]  sql parsed by [" + model.getParser().getName() + "");
			else
				rightTableMarker = getTableMarker(colRight.getTable());
			
			if (!joinExpression.isLiteralComparison())
				ctx().getGraph().insertEdge(
						ctx().getMasterPool(), null,"", 
						leftTableMarker, rightTableMarker, 
						TableAccessTimeline.STYLE_WUQISPANK_JOIN_EDGE);
			
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
	/**
	 * The "row lane" is the horizontal bar that shows all table accesses for a single SQL statement.
	 * If there were 4 SQL statements in a web request, there iwll be 4 row lanes.
	 * @param even
	 */
	private void renderRowLane(boolean even) {
		String style = null;
		if (even)
			style = TableAccessTimeline.STYLE_WUQISPANK_ROW_EVEN;
		else 
			style = TableAccessTimeline.STYLE_WUQISPANK_ROW_ODD;
		Object evenOddLane = ctx().getGraph().insertVertex( 
				getGroupLane(), getCombinedId(), null, 
				0, 0, 
				ctx().getConfig().getEvenOddRowWidth(), 
				ctx().getConfig().getEvenOddRowHeight(), style);

		int x = 28;
		int y = 0;
		int width = 25;
		int horizontalGap = 0;
		int height = ctx().getConfig().getEvenOddRowHeight();
    	ctx().getGraph().insertVertex(
    			evenOddLane, null, DefaultFactory.getFactory().getMessages().getSQL(), 
    			x, y, width, height, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=30;fontSize=13");
		
		x += (width + horizontalGap);

    	ctx().getGraph().insertVertex(
    			evenOddLane, null, String.valueOf(getSqlSeq()), 
    			x, y, width, height, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=30;fontSize=18");
		
		x += (width + horizontalGap);
    	ctx().getGraph().insertVertex(
    			evenOddLane, null, DefaultFactory.getFactory().getMessages().getStackTrace(), 
    			x, y, width, height, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=30;fontSize=10");
		
		setRowLane(evenOddLane);
	}

}
