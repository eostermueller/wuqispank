package org.wuqispank.web.test.tablecount;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is old-style, before swim lanes.
 * Probably should delete this if not being used by end of March, 2014.
 * http://localhost:8081/wuqispank/test/org.wuqispank.web.test.tap.HardCodedTable
 * @author erikostermueller
 *
 */
public class HardCodedTable extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {
    	MXGraphHtmlLoader mxGraphLoader = new MXGraphHtmlLoader();
    	mxGraphLoader.setGraphXml(getTableXml());

    	PrintWriter pw = resp.getWriter();
    	pw.println( mxGraphLoader.getHtml() );
    }
    
    private String getTableXml() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<mxGraphModel dx='800' dy='800' grid='1' guides='1' tooltips='1' connect='1' fold='1' page='1' pageScale='1' pageWidth='826' pageHeight='1169' style='default-style2'>");
    	sb.append("  <root>");
    	sb.append("    <mxCell id='0'/>");
    	sb.append("    <mxCell id='1' parent='0'/>");
    	sb.append("    <mxCell id='70' value='' style='verticalLabelPosition=bottom;verticalAlign=top;strokeWidth=2;shape=mxgraph.ios7.misc.scroll_(vertical);fillColor=#E6E6E6;shadow=1' parent='1' vertex='1'>");
    	sb.append("      <mxGeometry x='152.5' y='149' width='5' height='624' as='geometry'/>");
    	sb.append("    </mxCell>");
    	sb.append("    <mxCell id='91' value='' style='group' parent='1' vertex='1' connectable='0'>");
    	sb.append("      <mxGeometry x='112.5' y='66' width='82.5' height='80' as='geometry'/>");
    	sb.append("    </mxCell>");
    	sb.append("    <mxCell id='9' value='1' style='ellipse;whiteSpace=wrap;fillColor=#E6E6E6;strokeColor=#E6E6E6;shadow=1;fontColor=#FFFFFF;fontSize=48' parent='91' vertex='1'>");
    	sb.append("      <mxGeometry x='2.5' width='80' height='80' as='geometry'/>");
    	sb.append("    </mxCell>");
    	sb.append("    <mxCell id='74' value='CUST_CUST_REL' style='whiteSpace=wrap;strokeColor=none;fillColor=none' parent='91' vertex='1'>");
    	sb.append("      <mxGeometry y='59' width='80' height='20' as='geometry'/>");
    	sb.append("    </mxCell>");
    	sb.append("  </root>");
    	sb.append("</mxGraphModel>");
    	return sb.toString();
    }

}
