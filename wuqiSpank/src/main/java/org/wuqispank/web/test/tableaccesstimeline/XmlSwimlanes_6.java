package org.wuqispank.web.test.tableaccesstimeline;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wuqispank.DefaultFactory;
//import org.intrace.client.DefaultFactory;
import org.wuqispank.model.ITable;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.DefaultTableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.MXGraphContext;

import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxSwimlaneManager;

/**
 * Expand/collapse works!
 * The shapes for the table names are now round as I wanted them (instead of square).
 * 
 * @author erikostermueller
 *
 */
public class XmlSwimlanes_6 extends HttpServlet {
    private static final String STYLE_WUQISPANK_SWIMLANE = "swimlane";
	private MXGraphContext m_graphContext;
	private String STYLE_WUQISPANK_ROW_ODD = "wuqispankRowOdd";
	private String STYLE_WUQISPANK_ROW_EVEN = "wuqispankRowEven";
	private String STYLE_WUQISPANK_TABLE_HEADER = "tableHeader";
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {

    	MXGraphSwimlaneHtmlLoader_6 swimlaneLoader = new MXGraphSwimlaneHtmlLoader_6();
    	swimlaneLoader.setJavaScript("/js/XmlSwimlanes_6.js");
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
            		
                	Object pool = graph.insertVertex(parent, null, "pool", 0, 0, 100, 150, STYLE_WUQISPANK_SWIMLANE);
            		
                	Object lane1 = graph.insertVertex(pool, null, "", 5, 5, 100, 50, STYLE_WUQISPANK_SWIMLANE);
                	Object tableHeader1 = graph.insertVertex(pool,  null, "", 5, 5, 50, 50, STYLE_WUQISPANK_TABLE_HEADER);
                	
                	insertPopulatedLane(graph, pool, false);
                	insertPopulatedLane(graph, pool, false);
                	insertPopulatedLane(graph, pool, false);

                	Object lane4 = graph.insertVertex(pool, null, "", 10, 10, 100, 50, STYLE_WUQISPANK_SWIMLANE);

                    mxStackLayout layout = new mxStackLayout(graph, false) {
                    	public mxStackLayout init() {
                    		this.resizeParent = true;
                    		this.y0 = 0;
                    		this.x0 = 0;
                    		return this;
                    	}
                    }.init();
                    layout.execute(pool);
                	
            		
                    
            }
            finally
            {
                    graph.getModel().endUpdate();
            }

            // Turns the graph into XML data
            return mxXmlUtils.getXml(codec.encode(graph.getModel()));
    }
    private void insertPopulatedLane(mxGraph graph, Object pool, boolean collapsed) {
       	mxICell lane2 = (mxICell)graph.insertVertex(pool, null, "", 10, 10, 100, 50, STYLE_WUQISPANK_SWIMLANE);
       	
       	lane2.setCollapsed(collapsed);
       	
       	int x = 0;
    	int width = 400;
    	//mxCell mySwimLine = (mxCell)lane2;
    	//int width = (int) (mySwimLine.getGeometry().getWidth() - 15);
        Object alternating1 = graph.insertVertex(lane2, null, null, x,    0, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating2 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating3 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating4 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating5 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating6 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating7 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating8 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        Object alternating9 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_ODD);
        Object alternating10 = graph.insertVertex(lane2, null, null, x,  100, width, 50,STYLE_WUQISPANK_ROW_EVEN);
        
        mxStackLayout layout = new mxStackLayout(graph, false) {
        	public mxStackLayout init() {
        		this.resizeParent = true;
        		this.y0 = 0;
        		this.x0 = 0;
        		return this;
        	}
        }.init();
        layout.execute(lane2);
		
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
