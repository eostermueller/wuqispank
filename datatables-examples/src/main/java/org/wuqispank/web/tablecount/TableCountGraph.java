package org.wuqispank.web.tablecount;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.tablecount_DEPRECATED.MXGraphContext;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxSwimlaneManager;

public class TableCountGraph extends WebMarkupContainer {
    /**
	 * 
	 */
	private static final String STYLE_WUQISPANK_SWIMLANE = "swimlane";
	private MXGraphContext m_graphContext;
	private static final String STYLE_WUQISPANK_JOIN_EDGE = "wsJoinEdge";
	public static final String STYLE_WUQISPANK_ROW_ODD = "wsRowOdd";
	public static final String STYLE_WUQISPANK_ROW_EVEN = "wsRowEven";
	public static final String STYLE_WUQISPANK_TABLE_HEADER = "wsTableHeader";
	public static final String STYLE_WUQISPANK_TABLE_VERTICAL = "tableVertical";
	private static final String STYLE_WUQISPANK_SPHERE = "shape=image;verticalLabelPosition=bottom;verticalAlign=top;image=/wuqispank/images/stock_draw-sphere.png";
	private static final String STYLE_WUQISPANK_VERTICAL_TABLE_LANE = "wsVerticalTableLane";
    private static final String WUQISPANK_SWIMLANE = "swimlane";

	
	public TableCountGraph(String id, IRequestWrapper iRequestWrapper) {
		super(id);
		setMxGraphFolder("mxGraph-2_4_0_4");
		setRequestWrapper(iRequestWrapper);
		// TODO Auto-generated constructor stub
	}
	private IRequestWrapper requestWrapper = null;
    public IRequestWrapper getRequestWrapper() {
		return requestWrapper;
	}
	public void setRequestWrapper(IRequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
	}
	private String m_mxGraphFolder = null;
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
//		sb.append("        <script type='text/javascript'>");
//		sb.append("                mxBasePath = '../" + getMxGraphFolder() +  "';");
//		sb.append("        </script>");
//		sb.append("        <script type='text/javascript' src='" + contextPath + "/" + getMxGraphFolder() +  "/js/mxClient.js'></script>");
//		sb.append("        <script type='text/javascript' src='" + contextPath + getJavaScript() + "'></script>");

		response.getResponse().write( "<script type='text/javascript'>");
		response.getResponse().write( "mxBasePath ='../../" + getMxGraphFolder() + "';");
		response.getResponse().write( "</script>");
		
		response.render( JavaScriptHeaderItem.forUrl(getMxGraphFolder() + "/js/mxClient.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/tablecount.js"));
		
	}
	/**
	 * @see org.apache.wicket.Component#onRender()
	 */
	@Override
	protected void onRender()
	{
		getResponse().write("<div id='graphContainer'");
		getResponse().write("        style='position:relative;overflow:hidden;top:10px;left:20px;bottom:36px;right:250px;height:500;width:500'>");
		getResponse().write("</div>");		
		getResponse().write("        <!-- Contains a graph description which will be converted. -->");
		getResponse().write("        <div class='mxgraph' style='position:relative;overflow:hidden;border:6px solid gray;' >");
		getResponse().write("        </div>");
		getResponse().write("");
		getResponse().write("        <script type='text/javascript'>");
		getResponse().write("                	swimlaneGraphLoader(document.getElementById('graphContainer'), '" + createGraph()  + "');");
		getResponse().write("        </script>");
		
	}
    /**
     * Puts the O-->O graph onto s simple swimlange.
     * @param request
     * @return
     */
    String createGraph()
    {
            // Creates the graph on the server-side
            mxCodec codec = new mxCodec();
            mxGraph graph = new mxGraph();
            setStyleSheet(graph);
            Object parent = graph.getDefaultParent();

            graph.getModel().beginUpdate();
            try
            {
            		
            		mxSwimlaneManager swimMgr = new mxSwimlaneManager(graph);
            		swimMgr.setHorizontal(true);
            		
            		
                	Object pool = graph.insertVertex(parent, null, "pool", 0, 0, 100, 150, WUQISPANK_SWIMLANE);
            		
                	Object lane1 = graph.insertVertex(pool, null, "lane1", 5, 5, 100, 50, WUQISPANK_SWIMLANE);
                	Object lane2 = graph.insertVertex(pool, null, "lane2", 10, 10, 100, 50, WUQISPANK_SWIMLANE);
            		
                    Object v1 = graph.insertVertex(lane2, null, "Hello", 20, 20, 80, 30,"tableHeader");
                    Object v2 = graph.insertVertex(lane2, null, "World", 200, 150, 80, 30,"tableHeader");
                    graph.insertEdge(parent, null, "", v1, v2);
            }
            finally
            {
                    graph.getModel().endUpdate();
            }

            // Turns the graph into XML data
            return mxXmlUtils.getXml(codec.encode(graph.getModel()));
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
        stylesheet.putCellStyle(WUQISPANK_SWIMLANE, style);


    }

    
	private String getSwimlaneGraphXml() throws IOException {
		StringBuilder sw = new StringBuilder();
		
		//drawTableHeader(sw);
		
		return sw.toString();
	}

}
