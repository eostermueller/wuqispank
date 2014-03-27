package org.wuqispank.test.mxgraph;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;

public class OrthogonalEdgeTest extends JFrame
{
        private static final long serialVersionUID = -2707712944901661771L;

        /**
         * 		Simple swing drawing that shows how to draw the 3 types of edges used in this image:
         * 		/Users/erikostermueller/Documents/eto-perf-book/wuqispank/tableAccess_images/orthogonal-edges_many-ports.png
         * 
         * 			+----+				  +----+
         *          |    |				  |    |
         *          +----+				  +----+
         *             |                    |
         *             +--------------------+
         * 
         *             +--------------------+
         *             |					|
         * 			+----+				  +----+
         *          |    |				  |    |
         *          +----+				  +----+
         *          
         * 			+----+				  +----+
         *          |    |----------------|    |
         *          +----+				  +----+
         *          
         *          
         */
        public OrthogonalEdgeTest()
        {
                super("Hello, World!");

                mxGraph graph = new mxGraph();
                Object parent = graph.getDefaultParent();

                graph.getModel().beginUpdate();
                try
                {
                		/**
                		 * Use case:  Draw two boxes on a horizontal line.
                		 * 				Attached them with an edge that is below that horizontal line.
                		 */
                        Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
                        Object v2 = graph.insertVertex(parent, null, "World!", 140, 20, 80, 30);
                        mxICell cell = (mxICell)graph.insertEdge(parent, null, "Edge", v1, v2, "edgeStyle=elbowEdgeStyle;elbow=horizontal;endArrow=none;endFill=0;dashed=1");
                        mxPoint edgePoint = new mxPoint(150, 60);
                        List<mxPoint> myList = new ArrayList<mxPoint>();
                        myList.add(edgePoint);
                        cell.getGeometry().setPoints(myList);

                        
                		/**
                		 * Use case:  Draw two boxes on a horizontal line.
                		 * 				Attached them with an edge that is above that horizontal line.
                		 */
                        Object v3 = graph.insertVertex(parent, null, "Hello", 20, 100, 80, 30);
                        Object v4 = graph.insertVertex(parent, null, "World!", 160, 100, 80, 30);
                        mxICell cell2 = (mxICell)graph.insertEdge(parent, null, "Edge", v3, v4, "edgeStyle=elbowEdgeStyle;elbow=horizontal;endArrow=none;endFill=0;dashed=1");
                        mxPoint edgePoint2 = new mxPoint(150, 80);
                        List<mxPoint> myList2 = new ArrayList<mxPoint>();
                        myList2.add(edgePoint2);
                        cell2.getGeometry().setPoints(myList2);
 
                   		/**
                		 * Use case:  Draw two boxes on a horizontal line.
                		 * 				Attached them with an edge that on the horizontal line.
                		 */
                         Object v5 = graph.insertVertex(parent, null, "Hello", 20, 150, 80, 30);
                        Object v6 = graph.insertVertex(parent, null, "World!", 160, 150, 80, 30);
                        mxICell cell3 = (mxICell)graph.insertEdge(parent, null, "Edge", v5, v6, "edgeStyle=elbowEdgeStyle;elbow=horizontal;endArrow=none;endFill=0;dashed=1");
                        
                }
                finally
                {
                        graph.getModel().endUpdate();
                }

                mxGraphComponent graphComponent = new mxGraphComponent(graph);
                getContentPane().add(graphComponent);
        }

        public static void main(String[] args)
        {
        		OrthogonalEdgeTest frame = new OrthogonalEdgeTest();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 320);
                frame.setVisible(true);
        }
}