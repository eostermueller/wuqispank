package org.wuqispank.web.test.tableaccesstimeline;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

public class XmlSwimlanes_1 extends HttpServlet {
    private MXGraphContext m_graphContext;
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {

    	MXGraphSwimlaneHtmlLoader swimlaneLoader = new MXGraphSwimlaneHtmlLoader();
    	//swimlaneLoader.setJavaScript("/js/graph.js");
    	swimlaneLoader.setJavaScript("/js/XmlSwimlanes_1.js");
    	swimlaneLoader.setGraphXml(this.getSwimlaneGraphXml());
    	resp.getWriter().println( swimlaneLoader.getHtml(req.getContextPath() ) );
    }

    public MXGraphContext getGraphContext() {
    	return m_graphContext;
    	
    }
    private void drawTableHeader(StringWriter sw) throws IOException {
    	MXGraphContext mxGraphContext = new MXGraphContext();
    	mxGraphContext.setId(8);
    	
    	
    	ITable table = DefaultFactory.getFactory().getTable("CUST_CUST_REL");
    	
    	ITableHeaderRenderer thr = DefaultFactory.getFactory().getTableHeaderRenderer();
    	thr.setGraphContext(mxGraphContext);
    	thr.setParentId_1(1);
    	thr.setParentId_2(3);
    	thr.setTableHeaderConfiguration( DefaultFactory.getFactory().getTableHeaderConfiguration());
    	thr.setTable(table);
    	thr.setLeftToRightPosition(0);
    	//thr.setWriter(sw);
    	thr.render(sw);
    	
    	table = DefaultFactory.getFactory().getTable("ACCOUNT");
    	
    	thr = new DefaultTableHeaderRenderer();
    	thr.setGraphContext(mxGraphContext);
    	thr.setParentId_1(1);
    	thr.setParentId_2(3);
    	thr.setTable(table);
    	thr.setTableHeaderConfiguration( DefaultFactory.getFactory().getTableHeaderConfiguration());
    	thr.setLeftToRightPosition(1);
    	
    	thr.render(sw);

    	table = DefaultFactory.getFactory().getTable("ACCT_ACCT_REL");
    	thr = new DefaultTableHeaderRenderer();
    	thr.setGraphContext(mxGraphContext);
    	thr.setParentId_1(1);
    	thr.setParentId_2(3);
    	thr.setTable(table);
    	thr.setTableHeaderConfiguration( DefaultFactory.getFactory().getTableHeaderConfiguration());
    	thr.setLeftToRightPosition(2);
    	
    	thr.render(sw);
    	
    }
	private String getSwimlaneGraphXml() throws IOException {
		StringWriter sw = new StringWriter();
		sw.append("<mxGraphModel>");
		sw.append("  <root>");
		sw.append("    <mxCell id=\"0\"/>");
		sw.append("    <mxCell id=\"1\" parent=\"0\">");
		sw.append("      <mxRectangle width=\"901\" height=\"360\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		//sb.append("    <mxCell id=\"2\" value=\"Tables (header)\" style=\"swimlane;whiteSpace=wrap;rotation=-90\" vertex=\"1\" connectable=\"0\" parent=\"1\">");
		sw.append("    <mxCell id=\"2\" value=\"Tables (header)\" vertex=\"1\" connectable=\"0\" parent=\"1\">");
		sw.append("      <mxGeometry width=\"640\" height=\"110\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		//sb.append("    <mxCell id=\"3\" style=\"swimlane;whiteSpace=wrap;rotation=-90\" vertex=\"1\" connectable=\"0\" parent=\"2\">");
		sw.append("    <mxCell id=\"3\" vertex=\"1\" connectable=\"0\" parent=\"2\">");
		sw.append("      <mxGeometry x=\"22\" width=\"618\" height=\"110\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		sw.append("    <mxCell id=\"4\" value=\"SQL\" vertex=\"1\" connectable=\"0\" parent=\"1\">");
		sw.append("      <mxGeometry y=\"110\" width=\"640\" height=\"140\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		sw.append("    <mxCell id=\"5\" vertex=\"1\" connectable=\"0\" parent=\"4\">");
		sw.append("      <mxGeometry x=\"22\" width=\"618\" height=\"140\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		sw.append("    <mxCell id=\"6\" value=\"Tables (footer)\" vertex=\"1\" connectable=\"0\" parent=\"1\">");
		sw.append("      <mxGeometry y=\"250\" width=\"640\" height=\"110\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		sw.append("    <mxCell id=\"7\" vertex=\"1\" connectable=\"0\" parent=\"6\">");
		sw.append("      <mxGeometry x=\"22\" width=\"618\" height=\"110\" as=\"geometry\"/>");
		sw.append("    </mxCell>");
		
		drawTableHeader(sw);
		
		sw.append("  </root>");
		sw.append("</mxGraphModel>");
		return sw.toString();
	}

}
