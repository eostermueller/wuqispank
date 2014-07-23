package org.wuqispank.web.test.tableaccesstimeline;

public class MXGraphHtmlLoader {
	private String m_xml = null;
	public void setGraphXml(String xml) {
		m_xml = xml;
	}
	private String getGraphHtml() {
		String tmp = m_xml.replaceAll("<", "&lt;"); 
		tmp = tmp.replaceAll(">", "&gt;"); 
		return tmp.toString();
	}
	public String getHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("");
		sb.append("        <script type=\"text/javascript\">");
		sb.append("                mxBasePath = \"../src\";");
		sb.append("        </script>");
		sb.append("        <script type=\"text/javascript\" src=\"../mxGraph-2_4_0_4/js/mxClient.js\"></script>");
		sb.append("        <script type=\"text/javascript\" src=\"../js/graph.js\"></script>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("        <!-- Contains a graph description which will be converted. -->");
		sb.append("        This graph is embedded in the page.");
		sb.append("        <div class=\"mxgraph\" style=\"position:relative;overflow:hidden;border:6px solid gray;\" >");
		sb.append( getGraphHtml() );
		sb.append("        </div>");
		sb.append("");
		sb.append("        This graph is embedded in the page.");
		sb.append("        <script type=\"text/javascript\">");
		sb.append("                graphLoader();");
		sb.append("        </script>");
		sb.append("</body>");
		sb.append("</html>");		
		return sb.toString();
	}

}
