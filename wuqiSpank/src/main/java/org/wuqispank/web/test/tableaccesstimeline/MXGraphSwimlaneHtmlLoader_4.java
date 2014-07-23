//NEW STYLE
package org.wuqispank.web.test.tableaccesstimeline;

public class MXGraphSwimlaneHtmlLoader_4 {
	private String m_xml = null;
	private String m_javaScript;
	public void setGraphXml(String xml) {
		m_xml = xml;
	}
	public String getGraphXml() {
		return m_xml;
	}
	public void setJavaScript(String path) {
		m_javaScript = path;
	}
	public String getJavaScript() {
		return m_javaScript;
	}
	public String getHtml(String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("");
		sb.append("        <script type=\"text/javascript\">");
		sb.append("                mxBasePath = \"../mxGraph-2_2_0_2\";");
		sb.append("        </script>");
		sb.append("        <script type=\"text/javascript\" src=\"" + contextPath + "/mxGraph-2_2_0_2/js/mxClient.js\"></script>");
		sb.append("        <script type=\"text/javascript\" src=\"" + contextPath + getJavaScript() + "\"></script>");
		sb.append("</head>");
		sb.append("<body>");
        sb.append("<!-- Creates a container for the graph -->");
        sb.append("<div id=\"graphContainer\"");
        sb.append("        style=\"position:absolute;overflow:hidden;top:36px;left:60px;bottom:36px;right:0px;\">");
        sb.append("</div>");		
		sb.append("        <!-- Contains a graph description which will be converted. -->");
		sb.append("        This graph is embedded in the page.");
		sb.append("        <div class=\"mxgraph\" style=\"position:relative;overflow:hidden;border:6px solid gray;\" >");
		//sb.append( getGraphHtml() );
		sb.append("        </div>");
		sb.append("");
		sb.append("        This graph is embedded in the page.");
		sb.append("        <script type=\"text/javascript\">");
		sb.append("                swimlaneGraphLoader(document.getElementById(\"graphContainer\"), \"" + getGraphXml()  + "\");");
		sb.append("        </script>");
		sb.append("</body>");
		sb.append("</html>");		
		return sb.toString();
	}

}
