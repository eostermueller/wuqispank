//NEW STYLE
package org.wuqispank.web.test.tableaccesstimeline;

public class MXGraphSwimlaneHtmlLoader {
	private String m_mxGraphFolder = null;
	public void setMxGraphFolder(String val) {
		m_mxGraphFolder = val;
	}
	private String getMxGraphFolder() {
		if (m_mxGraphFolder==null) {
			throw new RuntimeException("Must first call setMxGraphFolder() -- sample value mxGraph-2_4_0_4");
		}
		return m_mxGraphFolder;
	}
	
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
		sb.append("        <script type='text/javascript'>");
		sb.append("                mxBasePath = '../" + getMxGraphFolder() +  "';");
		sb.append("        </script>");
		sb.append("        <script type='text/javascript' src='" + contextPath + "/" + getMxGraphFolder() +  "/js/mxClient.js'></script>");
		sb.append("        <script type='text/javascript' src='" + contextPath + getJavaScript() + "'></script>");
		sb.append("        <style>");
		sb.append("        canvas.myDownload {");
		sb.append("            position: fixed;");
		sb.append("            top: 30px;");
		sb.append("            right: 5px;");
		sb.append("            color: red;");
		sb.append("        }");
		sb.append("        </style>");
		sb.append("</head>");
		sb.append("<body>");
        sb.append("<!-- Creates a container for the graph -->");
        sb.append("<div id='graphContainer'");
        sb.append("        style='position:absolute;overflow:hidden;top:36px;left:60px;bottom:36px;right:0px;height:500;width:500'>");
        sb.append("</div>");		
		sb.append("        <!-- Contains a graph description which will be converted. -->");
		//sb.append("        This graph is embedded in the page.");
		sb.append("        <div class='mxgraph' style='position:relative;overflow:hidden;border:6px solid gray;' >");
		sb.append("        </div>");
		sb.append("");
		//sb.append("        This graph is embedded in the page.");
		sb.append("        <script type='text/javascript'>");
		//sb.append("					mxLog.show();");
		//sb.append("                	swimlaneGraphLoader(document.getElementById('graphContainer'), '" + getGraphXml()  + "');");
		sb.append("                	var svgText = '" + getGraphXml()  + "';");
		sb.append("						var svgText2   = '<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\"> <foreignObject width=\"100%\" height=\"100%\"><div xmlns=\"http://www.w3.org/1999/xhtml\" style=\"font-size:40px\"><em>I</em> like <span style=\"color:white; text-shadow:0 0 2px blue;\">cheese</span></dev><foreignObject></svg>';");
        
		sb.append("                	swimlaneGraphLoader(document.getElementById('graphContainer'), svgText);");
		sb.append("        </script>");
		sb.append(" <canvas class='myDownload' width=600 height=800 id='theCanvas'></canvas>");
		sb.append("</body>");
		sb.append("</html>");		
		return sb.toString();
	}

}
