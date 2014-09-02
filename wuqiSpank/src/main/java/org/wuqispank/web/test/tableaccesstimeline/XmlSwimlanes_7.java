package org.wuqispank.web.test.tableaccesstimeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wuqispank.tablecount_DEPRECATED.MXGraphContext;

import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxSwimlaneManager;
//import org.headlessintrace.client.DefaultFactory;

/**
 * Expand/collapse works!
 * The shapes for the table names are now round as I wanted them (instead of square).
 * 
 * @author erikostermueller
 *
 */
public class XmlSwimlanes_7 extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2046662393293433060L;
	private static final String STYLE_WUQISPANK_SWIMLANE = "swimlane";
	private MXGraphContext m_graphContext;
	private static final String STYLE_WUQISPANK_JOIN_EDGE = "wsJoinEdge";
	public static final String STYLE_WUQISPANK_ROW_ODD = "wsRowOdd";
	public static final String STYLE_WUQISPANK_ROW_EVEN = "wsRowEven";
	public static final String STYLE_WUQISPANK_TABLE_HEADER = "wsTableHeader";
	public static final String STYLE_WUQISPANK_TABLE_VERTICAL = "tableVertical";
	private static final String STYLE_WUQISPANK_SPHERE = "shape=image;verticalLabelPosition=bottom;verticalAlign=top;image=/wuqispank/images/stock_draw-sphere.png";
	private static final String STYLE_WUQISPANK_VERTICAL_TABLE_LANE = "wsVerticalTableLane";
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {

    	MXGraphSwimlaneHtmlLoader_7 swimlaneLoader = new MXGraphSwimlaneHtmlLoader_7();
//    	swimlaneLoader.setMxGraphFolder("mxGraph-2_4_0_4");
    	swimlaneLoader.setMxGraphFolder("mxGraph-2_2_0_2");    	
    	swimlaneLoader.setJavaScript("/js/XmlSwimlanes_7.js");
    	swimlaneLoader.setGraphXml(this.createGraph());
    	resp.getWriter().println( swimlaneLoader.getHtml(req.getContextPath() ) );
    }

    public MXGraphContext getGraphContext() {
    	return m_graphContext;
    	
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
            		
                	Object pool = graph.insertVertex(parent, null, "pool", 0, 0, 100, 65, STYLE_WUQISPANK_SWIMLANE);
            		
                	Object tableHeaderLane = graph.insertVertex(pool, null, "", 0, 0, 125, 100, STYLE_WUQISPANK_SWIMLANE);

                	Object[] spheresAndEdges = insertPopulatedLane(graph, pool, false);
                	insertPopulatedLane(graph, pool, false);
                	insertPopulatedLane(graph, pool, false);

                	Object tableFooterLane = graph.insertVertex(pool, null, "", 0, 0, 125, 75, STYLE_WUQISPANK_SWIMLANE);
                    int gap = 85;
                    int tableX = gap;
                	insertTableHeader(graph,pool, tableHeaderLane, tableFooterLane, "CUST_ACCT_REL", "8", tableX);
                	tableX += gap;
                	insertTableHeader(graph,pool, tableHeaderLane, tableFooterLane, "CUST_CUST_REL", "113", tableX);
                	tableX += gap;
                	insertTableHeader(graph,pool, tableHeaderLane, tableFooterLane, "PRODUCT", "1", tableX);
                	tableX += gap;
                	insertTableHeader(graph,pool, tableHeaderLane, tableFooterLane, "ORG_HIERARCHY", "7", tableX);
                	
                    mxStackLayout layout = new wsStackLayout(graph, false);
                    layout.execute(pool);
                    //whiteSpace=wrap;strokeColor=none
                	graph.orderCells(false, spheresAndEdges);
            }
            finally
            {
                    graph.getModel().endUpdate();
            }

            // Turns the graph into XML data
            return mxXmlUtils.getXml(codec.encode(graph.getModel()));
    }
    private void insertTableHeader(mxGraph graph, Object pool, Object tablesHeaderLane, Object tablesFooterLane, String tableName, String tableCount, int x) {
    	
    	mxICell table1Header = (mxICell)graph.insertVertex(tablesHeaderLane,  null, tableCount, x, 0, 50, 50, STYLE_WUQISPANK_TABLE_HEADER);
    	mxICell table1Footer = (mxICell)graph.insertVertex(tablesFooterLane,  null, tableCount, x, 0, 50, 50, STYLE_WUQISPANK_TABLE_HEADER);
        //Object edge = graph.insertEdge(pool, null, "", table1Header, table1Footer);
        //graph.orderCells(true,new Object[]{ edge });
    	//
    	mxICell table1HeaderLabel = (mxICell)graph.insertVertex(tablesHeaderLane, null, tableName, x, 0, 20, 20, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=30");
    	mxICell table1FooterLabel = (mxICell)graph.insertVertex(tablesFooterLane, null, tableName, x, 0, 20, 20, "whiteSpace=wrap;strokeColor=none;fillColor=none;labelBackgroundColor=none;opacity=30;textOpacity=30");
		
	}

	private Object[] insertPopulatedLane(mxGraph graph, Object pool, boolean collapsed) {
       	mxICell lane2 = (mxICell)graph.insertVertex(pool, null, "", 10, 10, 100, 50, STYLE_WUQISPANK_SWIMLANE);
        final mxICell[] myVerticalTableLanes = insertVerticalTableLanes(graph, lane2);
       	
       	lane2.setCollapsed(collapsed);
       	
       	int x = 0;
    	int width = 400;
    	int x_sphere = 100;
    	int y_sphere = 15;
        Object alternating1 = graph.insertVertex(lane2, null, null, x,    0, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object v1 = (mxICell)graph.insertVertex(alternating1, null, null, x_sphere,    y_sphere, 20, 20,STYLE_WUQISPANK_SPHERE);

        Object v3 = graph.insertVertex(alternating1, null, null, 270,    y_sphere, 20, 20,STYLE_WUQISPANK_SPHERE);
        Object v4 = graph.insertVertex(alternating1, null, null, 355,    y_sphere, 20, 20,STYLE_WUQISPANK_SPHERE);
        Object e1 = graph.insertEdge(pool, null, "", v1, v3, STYLE_WUQISPANK_JOIN_EDGE);
        Object e2 = graph.insertEdge(pool, null, "", v1, v4, STYLE_WUQISPANK_JOIN_EDGE);
        Object[] spheresAndEdges = { v1, v3, v4, e1, e2 };
        
        Object alternating2 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating3 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating4 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating5 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating6 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating7 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating8 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating9 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating10 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        mxICell[] cells = {(mxICell)alternating1, (mxICell)alternating2,(mxICell)alternating3, (mxICell)alternating4, (mxICell)alternating5, (mxICell)alternating6, (mxICell)alternating7, (mxICell)alternating8, (mxICell)alternating9, (mxICell)alternating10 };
        graph.orderCells(true, cells);
        
        mxStackLayout layout = new mxStackLayout(graph, false) {
        	
        	public mxStackLayout init() {
        		this.resizeParent = true;
        		this.y0 = 0;
        		this.x0 = 0;
        		return this;
        	}
        	
        	/**
        	 * Returns true if the given vertex may be moved by the layout.
        	 * 
        	 * @param vertex Object that represents the vertex to be tested.
        	 * @return Returns true if the vertex can be moved.
        	 */
        	public boolean isVertexMovable(Object vertex)
        	{
        		boolean ynMovable = true;
        		for (mxICell cell : myVerticalTableLanes ) {
        			if (vertex == cell) {
        				return false;
        			}
        		}
        		
        		return graph.isCellMovable(vertex);
        	}
        	
        	
        }.init();
        layout.execute(lane2);
        
		return spheresAndEdges;
	}

	private mxICell[] insertVerticalTableLanes(mxGraph graph, Object lane) {
		
		final int xIncrement = 86;
		final int xNegOffset = 15;
		final int xStart = 113;
		
		List<mxICell> myVerticalTableLanes = new ArrayList<mxICell>();
		int x = xStart;
		for (int i = 0; i < 4; i++) {
			mxICell insertVertex = (mxICell)graph.insertVertex(lane, null, null, x-xNegOffset, 0, 30, 450, STYLE_WUQISPANK_VERTICAL_TABLE_LANE);
			myVerticalTableLanes.add(insertVertex);
			x += xIncrement;
		}
		mxICell[] mxICellAry = new mxICell[0]; 
		return myVerticalTableLanes.toArray(mxICellAry);
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
class wsStackLayout extends mxStackLayout {

	public wsStackLayout(mxGraph graph, boolean b) {
		super(graph, b);
		this.resizeParent = true;
		this.y0 = 0;
		this.x0 = 0;
	}
	@Override
	public boolean isVertexIgnored(Object o) {
		boolean rc = true;
		if (getGraph().isSwimlane(o)) {
			rc = false;
		} else {
			mxICell cell = (mxICell)o;
			if (XmlSwimlanes_7.STYLE_WUQISPANK_ROW_ODD.equals(cell.getStyle())) {
				rc = false;
			} else if (XmlSwimlanes_7.STYLE_WUQISPANK_ROW_EVEN.equals(cell.getStyle())) {
				rc = false;
			}
		}
		
		return rc;
		
	}
	
	
}