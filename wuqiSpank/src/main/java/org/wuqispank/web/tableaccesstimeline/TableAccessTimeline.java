package org.wuqispank.web.tableaccesstimeline;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;
import org.wuqispank.model.ITableOrderMgr;
import org.wuqispank.model.SqlType;
import org.wuqispank.web.EventCollector;

import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxSwimlaneManager;

/**
 * Definitions:
 * Group:  A set of rows in the TableCountGraph, represented by multiple
 * stacked rows in a single, collapsable swimlane.  This is one of the main
 * techniques used to minimize the visual distress of when thousands or 10\"s of 1000\"s
 * of SQL statements must be represented for a single web request.
 * Each group\"s collapsible lane is rendered with its own mxGraph vertical lanes, 
 * one for each table used in the request.
 * We\"re aiming to give the impression that each lane spreads vertically down
 * the page across multiple groups.
 * @author erikostermueller
 *
 */
public class TableAccessTimeline extends WebMarkupContainer {
	static Logger LOG = LoggerFactory.getLogger(TableAccessTimeline.class);
	private GraphContext m_ctx = null;
	GraphContext ctx() {
		return m_ctx;
	}
	public static final String STYLE_WUQISPANK_SWIMLANE = "swimlane";
	//private MXGraphContext m_graphContext;
	public static final String STYLE_WUQISPANK_TABLE_LABEL = "tableLabel";
	public static final String STYLE_WUQISPANK_JOIN_EDGE = "wsJoinEdge";
	public static final String STYLE_WUQISPANK_ROW_ODD = "wsRowOdd";
	public static final String STYLE_WUQISPANK_ROW_EVEN = "wsRowEven";
	public static final String STYLE_WUQISPANK_TABLE_HEADER = "wsTableHeader";
	public static final String STYLE_WUQISPANK_TABLE_CACHE_HEADER = "wsTableCacheHeader";
	public static final String STYLE_WUQISPANK_TABLE_GROWTH_HEADER = "wsTableGrowthHeader";
	public static final String STYLE_WUQISPANK_TABLE_VERTICAL = "tableVertical";
	//public static final String STYLE_WUQISPANK_SPHERE = "shape=image;verticalLabelPosition=bottom;verticalAlign=top;image=/wuqiSpank/images/stock_draw-sphere.png";
	public static final String STYLE_WUQISPANK_SPHERE = "shape=image;verticalLabelPosition=bottom;verticalAlign=top;image=/wuqiSpank/images/i_select.png";
	public static final String STYLE_WUQISPANK_VERTICAL_TABLE_LANE = "wsVerticalTableLane";

	
	public TableAccessTimeline(String id, IRequestWrapper iRequestWrapper) {
		super(id);
		setGraphContext( new GraphContext() );
		ctx().setConfig( DefaultFactory.getFactory().getConfig() );

		setMxGraphFolder( ctx().getConfig().getMxGraphFolderName() );
		ctx().setGraph( new mxGraph() );
		setRequestWrapper(iRequestWrapper);
		
	}
	public static String getBaseImage() {
		String baseImage = "shape=image;verticalLabelPosition=bottom;verticalAlign=top;image=/wuqiSpank/images/";
		return baseImage;
	}
	public static String getTableAccessImage(SqlType sqlType, boolean isException) {
		String imageUrl = null;
		
		if (isException)//as an enhancement, would be nice to have image that indicated 
			            //both sql type and exception.
			imageUrl = getBaseImage() + "i_parse_exception.png";
		else 
			imageUrl =  getBaseImage() + "i_" + sqlType.toString().toLowerCase() + ".png";
		
		return imageUrl;
	}
	private void setGraphContext(GraphContext val) {
		m_ctx = val;
	}
	private IRequestWrapper m_requestWrapper = null;
    public IRequestWrapper getRequestWrapper() {
		return m_requestWrapper;
	}
	public void setRequestWrapper(IRequestWrapper requestWrapper) {
		this.m_requestWrapper = requestWrapper;
    	ITableOrderMgr mgr = DefaultFactory.getFactory().getTableOrderMgr();
    	ctx().setTableLaneMgr( DefaultFactory.getFactory().getTableLaneMgr() );
    	ctx().getTableLaneMgr().setTableLaneOrder( mgr.reorderTables(getRequestWrapper().getSqlStats()));
	}
	private String m_mxGraphFolder = null;
	private List<IRowGroup> m_rowGroups = new ArrayList<IRowGroup>();
	public void setMxGraphFolder(String val) {
		m_mxGraphFolder = val;
	}
	private String getMxGraphFolder() {
		if (m_mxGraphFolder==null) {
			throw new RuntimeException("Must first call setMxGraphFolder() -- sample value mxGraph-2_4_0_4");
		}
		return m_mxGraphFolder;
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		renderBasicJS(response);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3791161163999251921L;
	private void renderBasicJS(IHeaderResponse response)
	{
//		sb.append("        <script type=\"text/javascript\">");
//		sb.append("                mxBasePath = \"../" + getMxGraphFolder() +  "\";");
//		sb.append("        </script>");
//		sb.append("        <script type=\"text/javascript\" src=\"" + contextPath + "/" + getMxGraphFolder() +  "/js/mxClient.js\"></script>");
//		sb.append("        <script type=\"text/javascript\" src=\"" + contextPath + getJavaScript() + "\"></script>");

		response.getResponse().write( "<script type=\"text/javascript\">");
		response.getResponse().write( "mxBasePath =\"../../" + getMxGraphFolder() + "\";");
		response.getResponse().write( "</script>");
		
		response.render( JavaScriptHeaderItem.forUrl(getMxGraphFolder() + "/js/mxClient.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/tablecount.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/vkbeautify.0.99.00.beta.js"));
		
	}
	/**
	 * @see org.apache.wicket.Component#onRender()
	 */
	@Override
	protected void onRender()
	{
		try {
			getResponse().write("<div id=\"graphContainer\"");
			getResponse().write("        style=\"position:relative;overflow:hidden;top:10px;left:20px;bottom:36px;right:250px;height:500;width:500\">");
			getResponse().write("</div>");		
			getResponse().write("        <!-- Contains a graph description which will be converted. -->");
			getResponse().write("        <div class=\"mxgraph\" style=\"position:relative;overflow:hidden;border:6px solid gray;\" >");
			getResponse().write("        </div>");
			getResponse().write("");
			getResponse().write("        <script type=\"text/javascript\">");
			getResponse().write("            var svgText = '" + createGraph() + "';");
			getResponse().write("                	swimlaneGraphLoader(document.getElementById(\"graphContainer\"), svgText);");
			getResponse().write("        </script>");
			getResponse().write("     		<td valign=\"top\">");
			getResponse().write("     	<div id=\"properties\"");
			getResponse().write("     		style=\"border: solid 1px black; padding: 10px;\">");
			getResponse().write("     	</div>");
			getResponse().write("     	</td>");
			
		} catch (WuqispankException we) {
			we.printStackTrace();
			getResponse().write(we.getMessage());
		}
		
	}
    /**
     * Puts the O-->O graph onto s simple swimlange.
     * @param request
     * @return
     * @throws WuqispankException 
     */
    String createGraph() throws WuqispankException
    {
            // Creates the graph on the server-side
            mxCodec codec = new mxCodec();
            
            setStyleSheet(ctx().getGraph());
            Object parent = ctx().getGraph().getDefaultParent();

            ctx().getGraph().getModel().beginUpdate();
            try
            {
            		mxSwimlaneManager swimMgr = new mxSwimlaneManager(ctx().getGraph());
            		swimMgr.setHorizontal(true);
                	Object masterPool = ctx().getGraph().insertVertex(parent, null, "pool", 0, 0, 100, 65, STYLE_WUQISPANK_SWIMLANE);
                	ctx().setMasterPool(masterPool);
            		
                	Object tableHeaderLane = ctx().getGraph().insertVertex(
                			ctx().getMasterPool(), 
                			null, "", 
                			0, 0, 
                			125, 100, 
                			STYLE_WUQISPANK_SWIMLANE);
                	
                	createRowGroups();
                	
                	renderRowGroups();
                	
                	Object tableFooterLane = ctx().getGraph().insertVertex(
                			ctx().getMasterPool(), 
                			null, "", 
                			0, 0, 
                			125, 75, 
                			STYLE_WUQISPANK_SWIMLANE);
                	
                	createTableNameLabels(tableHeaderLane,tableFooterLane);
            		layout();
            }
            finally
            {
            	ctx().getGraph().getModel().endUpdate();
            }

            // Turns the graph into XML data
            return mxXmlUtils.getXml(codec.encode(ctx().getGraph().getModel()));
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
		layout.execute(ctx().getMasterPool());
	}
	private void createTableNameLabels(Object tableHeaderLane, Object tableFooterLane) {
		//int gap = 85;
		int gap = ctx().getConfig().getXSpaceBetwenTableLanes();
		
		//int tableX = ctx().getConfig().getXStartLeftMostTableLabel();
		int tableX = ctx().getConfig().getXStartLeftMostTableLane()-100;
		for( ITable table : ctx().getTableLaneMgr().getTableLaneOrder2()) {
			tableX += gap;
			insertTableName(
					ctx().getGraph(),
					ctx().getMasterPool(),
					tableHeaderLane,tableFooterLane,
					table,
					getRequestWrapper().getSqlStats().getTableCount(table),
					tableX
					);
		}
		
	}
	private void insertTableName(mxGraph graph, Object pool, Object tablesHeaderLane, Object tablesFooterLane, ITable table, int tableCount, int x) {
		
		String backgroundCssStyle =  STYLE_WUQISPANK_TABLE_HEADER;
		
		if (table.isGrowthTable()) {
			backgroundCssStyle = this.STYLE_WUQISPANK_TABLE_GROWTH_HEADER;
			LOG.error("Table [" + table.getName() + "] is a growth table.  using style [" + backgroundCssStyle + "]");
		} else if (table.shouldBeCached() ) { 
			backgroundCssStyle = this.STYLE_WUQISPANK_TABLE_CACHE_HEADER;
			LOG.error("Table [" + table.getName() + "] is a cache table.  using style [" + backgroundCssStyle + "]");
		} else {
			LOG.error("Table [" + table.getName() + "] is neither cache nor growth.  using style [" + backgroundCssStyle + "]");
		}
    	
    	mxICell table1Header = (mxICell)graph.insertVertex(tablesHeaderLane,  null, tableCount, x, 0, 50, 50, backgroundCssStyle);
    	mxICell table1Footer = (mxICell)graph.insertVertex(tablesFooterLane,  null, tableCount, x, 0, 50, 50, backgroundCssStyle);
    	mxICell table1HeaderLabel = (mxICell)graph.insertVertex(tablesHeaderLane, null, table.getName().toUpperCase(), x, 0, 20, 20, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=100");
    	mxICell table1FooterLabel = (mxICell)graph.insertVertex(tablesFooterLane, null, table.getName().toUpperCase(), x, 0, 20, 20, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=100");
		
	}

    private void renderRowGroups() throws WuqispankException {
    	int count = 0;
		for(IRowGroup rowGroup : getRowGroups() ) {
			rowGroup.render(count++);
		}
	}
	private List<IRowGroup> getRowGroups() {
		return m_rowGroups;
	}
	/**
     * Wuqispank aims to support the case of 100\"s/1000\"s of sql statements in a single request.
     * When first rendered, the first "Row Group" of sql stmts will be expanded, all others collapsed (Using mxGraph swimlanes).
     * RowGroups will have X SqlStatements in them.
     * X = IConfig#getRowCountOfHeterogenousGroup(),
     * unless there is a large batch of contiguous equivalent sql statements.
     * In this case, many more will be packed into a single group.
     * How many more?  IConfig#getRowCountOfHomogeneousGroup().
     * What determines sql statement equivalence?  ISqlModel#equivalent().
     * 
     * Default values for these parameters: I\"m imaging 50 for hetergeneous and 500 for homogeneous.
     */
	private void createRowGroups() {
    	int totalSqlCount = 0;
    	int equivalentStreak = 0;
    	
    	IRowGroup rowGroup = DefaultFactory.getFactory().createRowGroup( ctx(), getRequestWrapper().getUniqueId() );
    	add(rowGroup);
    	ISqlWrapper previousSql = null;
    	for( ISqlWrapper sql : getRequestWrapper().getSql()) {
    		if (previousSql!=null && previousSql.getSqlModel().matchesTablesOf(sql.getSqlModel()))
    			equivalentStreak++;
    		else
    			equivalentStreak = 0;
    		
    		if (equivalentStreak != 0 && equivalentStreak < ctx().getConfig().getMaxRowCountOfHomogeneousGroup()) {
    			rowGroup.add(sql);
    		} else if (rowGroup.count() < ctx().getConfig().getMaxRowCountOfHeterogenousGroup())
    			rowGroup.add(sql);
    		else {
    			
    			rowGroup = DefaultFactory.getFactory().createRowGroup( ctx(), getRequestWrapper().getUniqueId() );
    			rowGroup.add(sql);
    			this.add(rowGroup);
    		}
    		previousSql = sql;
    	}
	}
	private void add(IRowGroup rowGroup) {
		m_rowGroups.add(rowGroup);
	}
	private void setStyleSheet(mxGraph graph) {

        Hashtable<String, Object> style;
        mxStylesheet stylesheet = graph.getStylesheet();

        // base style
        Hashtable<String, Object> baseStyle = new Hashtable<String, Object>();
        //baseStyle.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");

        // custom vertex style
        style = new Hashtable<String, Object>(baseStyle);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_SWIMLANE);
        stylesheet.putCellStyle(STYLE_WUQISPANK_SWIMLANE, style);

        /*
         * alternating odd-even rows
         */
        style = new Hashtable<String, Object>(baseStyle);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        stylesheet.putCellStyle(STYLE_WUQISPANK_ROW_ODD, style);
        
        
        /*
         * looks like this black is never filled anywhere, but doc for mxConstants.STYLE_GRADIENTCOLOR says I need to set some value,
         * so I chose this which his in my draw.io example
         */
        style = new Hashtable<String, Object>(baseStyle);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        stylesheet.putCellStyle(STYLE_WUQISPANK_ROW_ODD, style);

        style = new Hashtable<String, Object>(baseStyle);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_FILLCOLOR, "FFFFFF"); 
        style.put(mxConstants.STYLE_GRADIENTCOLOR, "CCE5FF"); //This is the blue I had in my Draw.io example
        style.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_EAST);
        stylesheet.putCellStyle(STYLE_WUQISPANK_ROW_EVEN, style);

    }

}
