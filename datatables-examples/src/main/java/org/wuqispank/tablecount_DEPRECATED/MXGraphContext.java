package org.wuqispank.tablecount_DEPRECATED;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxSwimlaneManager;

public class MXGraphContext {
	private int m_id = 0;
	public void setId(int val) {
		m_id = val;
	}
	public int incrementAndGetId() {
		return ++m_id;
	}
	public void flush() {
        // Creates the graph on the server-side
        mxCodec codec = new mxCodec();
        mxGraph graph = new mxGraph();
        //setStyleSheet(graph);
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
        		
//        		mxSwimlaneManager swimMgr = new mxSwimlaneManager(graph);
//        		swimMgr.setHorizontal(true);
//        		
//        		
//            	Object pool = graph.insertVertex(parent, null, "pool", 0, 0, 100, 150, WUQISPANK_SWIMLANE);
//        		
//            	Object lane1 = graph.insertVertex(pool, null, "lane1", 5, 5, 100, 50, WUQISPANK_SWIMLANE);
//            	Object lane2 = graph.insertVertex(pool, null, "lane2", 10, 10, 100, 50, WUQISPANK_SWIMLANE);
        		
//                Object v1 = graph.insertVertex(lane2, null, "Hello", 20, 20, 80, 30,"tableHeader");
//                Object v2 = graph.insertVertex(lane2, null, "World", 200, 150, 80, 30,"tableHeader");
//                graph.insertEdge(parent, null, "", v1, v2);
        }
        finally
        {
                graph.getModel().endUpdate();
        }

        // Turns the graph into XML data
        //return mxXmlUtils.getXml(codec.encode(graph.getModel()));
		
	}

}
