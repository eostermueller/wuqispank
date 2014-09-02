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
//import org.headlessintrace.client.DefaultFactory;
import org.wuqispank.model.ITable;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.DefaultTableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.MXGraphContext;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxSwimlaneManager;

/**
 * 
 * First example to use the mxGraph java API.
 * The swimlanes start out as unorganized rectangles.
 * ...but then they clean up once you collapse and expand.
 * @author erikostermueller
 *
 */
public class XmlSwimlanes_4 extends HttpServlet {
    private static final String WUQISPANK_SWIMLANE = "swimlane";
	private MXGraphContext m_graphContext;
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {

    	MXGraphSwimlaneHtmlLoader_4 swimlaneLoader = new MXGraphSwimlaneHtmlLoader_4();
    	swimlaneLoader.setJavaScript("/js/XmlSwimlanes_4.js");
    	swimlaneLoader.setGraphXml(this.getSwimlaneGraphXml());
    	resp.getWriter().println( swimlaneLoader.getHtml(req.getContextPath() ) );
    }

    public MXGraphContext getGraphContext() {
    	return m_graphContext;
    	
    }
    private void drawTableHeader(StringWriter sw) throws IOException {
    	sw.write(createGraph());
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
		StringWriter sw = new StringWriter();
//		sw.append("<mxGraphModel>");
//		sw.append("  <root>");
		
		drawTableHeader(sw);
		
//		sw.append("  </root>");
//		sw.append("</mxGraphModel>");
		return sw.toString();
	}

}
