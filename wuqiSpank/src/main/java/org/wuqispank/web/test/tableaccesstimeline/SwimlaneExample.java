package org.wuqispank.web.test.tableaccesstimeline;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SwimlaneExample extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException    {
//    	resp.getWriter().println("Found context path [" + req.getContextPath() + "]");
    	resp.getWriter().println( getHtml(req.getContextPath() ) );
    }
    private String getHtml(String contextPath) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<html>\n");
    	sb.append("<head>\n");
    	sb.append("	<title>Swimlanes example for mxGraph</title>\n");
    	sb.append("\n");
    	sb.append("	<script type=\"text/javascript\">\n");
    	sb.append("		mxBasePath = \"" + contextPath + "/mxGraph-2_4_0_4\";\n");
    	sb.append("	</script>\n");
    	sb.append("\n");
    	sb.append("	<script type=\"text/javascript\" src=\"" + contextPath + "/mxGraph-2_4_0_4/js/mxClient.js\"></script>\n");
    	sb.append("	<script type=\"text/javascript\" src=\"" + contextPath + "/js/XmlSwimlanes_1.js\"></script>\n");
    	sb.append("	<script type=\"text/javascript\">\n");
    	sb.append("		// Defines an icon for creating new connections in the connection handler.\n");
    	sb.append("		// This will automatically disable the highlighting of the source vertex.\n");
    	sb.append("		mxConnectionHandler.prototype.connectImage = new mxImage(\"images/connector.gif\", 16, 16);\n");
    	sb.append("		\n");
    	sb.append("	</script>\n");
    	sb.append("</head>\n");
    	sb.append("<body onload=\"createSwimlanes(document.getElementById(\"graphContainer\"))\">\n");
    	sb.append("	<div id=\"graphContainer\"\n");
    	sb.append("		style=\"position:absolute;overflow:hidden;top:40px;left:40px;width:600px;height:400px;border: gray dotted 1px;cursor:default;\">\n");
    	sb.append("	</div>\n");
    	sb.append("</body>\n");
    	sb.append("</html>\n");

    	return sb.toString();
    	
    }

}
