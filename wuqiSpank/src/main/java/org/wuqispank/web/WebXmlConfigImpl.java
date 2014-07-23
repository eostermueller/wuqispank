package org.wuqispank.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.intrace.client.connection.HostPort;
import org.intrace.jdbc.IJdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.web.msgs.IMessages;

public class WebXmlConfigImpl implements IConfig, java.io.Serializable, IJdbcProvider {
	static Logger LOG = LoggerFactory.getLogger(WebXmlConfigImpl.class);
	
	private static final String WEB_XML_INTRACE_AGENT_HOST = "intrace-agent-host";
	private static final String WEB_XML_INTRACE_AGENT_PORT = "intrace-agent-port";
	private static final String WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE = "circular-request-buffer-size";
	public static final String WEB_XML_EXPORT_DIR = "export-dir";
	private static final String WEB_XML_RECONNECT_INTERVAL = "reconnect-interval-seconds";
	private IMessages msgs = null;
	private IMessages getMsgs() {
		return msgs;
	}

	private void setMsgs(IMessages msgs) {
		this.msgs = msgs;
	}

	private static final int DEFAULT_CIRCULAR_BUFFER_SIZE = 10000;

	
	private transient ServletContext m_servletContext;

	private ServletContext getServletContext() {
		return m_servletContext;
	}

	private void setServletContext(ServletContext servletContext) {
		this.m_servletContext = servletContext;
	}

	public WebXmlConfigImpl(ServletContext servletContext) {
		setMsgs(org.wuqispank.DefaultFactory.getFactory().getMessages());
		this.m_servletContext = servletContext;
	}

	@Override
	public HostPort getInTraceAgent() {
		String  inTraceAgentHostName = getServletContext().getInitParameter(WEB_XML_INTRACE_AGENT_HOST);
		if ("".trim().equals(inTraceAgentHostName)) {
			throw new WebXmlConfigurationException(msgs.getInvalidAgentHostName(inTraceAgentHostName, WEB_XML_INTRACE_AGENT_HOST, this.getClass().getCanonicalName()));
		}
		HostPort agentToMonitor = null; 
		String inTraceAgentPort = getServletContext().getInitParameter(WEB_XML_INTRACE_AGENT_PORT);
		try {
			int intInTraceAgentPort = Integer.parseInt(inTraceAgentPort);
			agentToMonitor = new HostPort(inTraceAgentHostName, intInTraceAgentPort);
		} catch(Exception e) {
			throw new WebXmlConfigurationException(msgs.getInvalidAgentPortNumber(e, inTraceAgentPort, WEB_XML_INTRACE_AGENT_PORT, this.getClass().getCanonicalName()));
		}
		return agentToMonitor;
	}
	@Override
	public int getReconnectIntervalInSeconds() {
		int intReconnectIntervalSeconds = 10;//default
		String reconnectIntervalInSeconds = getServletContext().getInitParameter(this.WEB_XML_RECONNECT_INTERVAL);
		
		if (reconnectIntervalInSeconds !=null && !"".trim().equals(reconnectIntervalInSeconds)) {
			try {
				intReconnectIntervalSeconds = Integer.parseInt(reconnectIntervalInSeconds);
			} catch(Exception e) {
				throw new WebXmlConfigurationException(msgs.getInvalidReconnectInterval(e, reconnectIntervalInSeconds, WEB_XML_RECONNECT_INTERVAL, this.getClass().getCanonicalName()));
			}
		}
		return intReconnectIntervalSeconds;
	}

	@Override
	public int getCircularBufferSize() {

		int intInTraceAgentPort = -1;
		String inTraceAgentHostName = null;		
		HostPort agentToMonitor = null;

		String circularRequestBufferSize = getServletContext().getInitParameter(WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE);
		int intCircularRequestBufferSize = DEFAULT_CIRCULAR_BUFFER_SIZE;
		try {
			intCircularRequestBufferSize = Integer.parseInt(circularRequestBufferSize);
		} catch(Exception e) {
			throw new WebXmlConfigurationException(msgs.getInvalidCircularRequestBufferSize(e, circularRequestBufferSize, WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE, this.getClass().getCanonicalName() ));
		}
		return intCircularRequestBufferSize;
	}

	@Override
	public File getExportDir() {
		String  exportDirName = getServletContext().getInitParameter(WEB_XML_EXPORT_DIR);
		if (exportDirName==null || exportDirName.trim().length()==0) {
			IMessages msgs = DefaultFactory.getFactory().getMessages();
			String error = msgs.getExportDirNotSet(WEB_XML_EXPORT_DIR);
			LOG.warn(error);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<context-param>\n");
			sb.append("\t\t<param-name>export-dir</param-name>\n");
			sb.append("\t\t<param-value>c:/home/myUser/myFolder</param-value>\n");
			sb.append("<context-param>\n");
			
			LOG.warn(msgs.getHowToSpecifyExportDir() + sb.toString());
		}
		File exportDir = new File(exportDirName);
		if (!exportDir.exists())
			exportDir.mkdirs();
		LOG.debug("[" + WEB_XML_EXPORT_DIR + "] is [" + exportDir.toString() + "] from input parameter [" + exportDirName + "]");
		return exportDir;
	}

	/**
	 * Never should there be more than 20 rows (sql statements)
	 * in a single collapsible group, unless there is a block of 
	 * identical statements (then see getRowCountOfHeterogenousGroup).
	 */
	@Override
	public int getMaxRowCountOfHeterogenousGroup() {
		return 1000;
	}

	/**
	 * If 1000 sql statements execute in uninterrupted sequence,
	 * they should all be in the same collapsible swim lane.
	 */
	@Override
	public int getMaxRowCountOfHomogeneousGroup() {
		return 1000;
	}

	@Override
	public String getMxGraphFolderName() {
		return "mxGraph-2_4_0_4";
	}
	@Override
	public int getXNegOffset() {
		return 15;
	}
	@Override
	public int getTableMarkerX() {
		return 0;
	}
	@Override
	public int getTableMarkerY() {
		return 25;
		//return 50;
	}
	@Override
	public int getTableMarkerYOffset() {
		return 5;
	}
	@Override
	public int getTableMarkerWidth() {
		return 20;
	}
	@Override
	public int getTableMarkerHeight() {
		return 20;
	}

	@Override
	public int getEvenOddRowWidth() {
		return 1200;
	}
	@Override
	public int getEvenOddRowHeight() {
		//return 50;
		return 35;
	}

	@Override
	public int getWidthOfVerticalTableLane() {
		return 20;
	}

	@Override
	public int getXStartLeftMostTableLabel() {
		return 13;
	}
	@Override
	public int getXSpaceBetwenTableLanes() {
		return 70;
	}
	@Override
	public int getXStartLeftMostTableLane() {
		//return 113;
		return 150;
	}
	

	@Override
	public int getHeightOfVerticalTableLane() {
		return 100;
	}

	@Override
	/**
		<param-name>org.intrace.jdbc.IJdbcProvider</param-name>
		<param-value>org.intrace.jdbc.HsqldbProvider</param-value>
	 * 
	 */
	public String getJdbcProvider() {
		String  jdbcProviderName = getServletContext().getInitParameter("org.intrace.jdbc.IJdbcProvider");
		if (jdbcProviderName==null)
			//throw new RuntimeException(this.msgs.getJdbcConfigurationError(jdbcProviderName) );
			throw new WebXmlConfigurationException(this.msgs.getJdbcConfigurationError(jdbcProviderName) );
		
		LOG.debug("org.intrace.jdbc.IJdbcProvider is [" + jdbcProviderName + "] from input parameter [" + jdbcProviderName + "]");
		return jdbcProviderName;
	}

	@Override
	public boolean isNativeSQL() {
		boolean rc = false;
		String  strIsNative = getServletContext().getInitParameter("nativeSQL");
		if (strIsNative!=null) {
			if ("true".equals(strIsNative.toLowerCase()) || "yes".equals(strIsNative.toLowerCase() ) )
				rc  = true;
		}
		LOG.debug("nativeSQL is [" + rc + "] from input parameter [" + strIsNative + "]");
		return rc;
	}

	@Override
	public String[] getStatementPackageAndClass() {
		final String parmName = "java.sql.Statement.implementors";
		String  rc = getServletContext().getInitParameter(parmName);
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc.split(",");
	}

	@Override
	public String[] getConnectionPackageAndClass() {
		final String parmName = "java.sql.Connection.implementors";
		String  rc = getServletContext().getInitParameter(parmName);
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc.split(",");
	}

	@Override
	public String getVersion() {
		final String parmName = "org.intrace.jdbc.IJdbcProvider.version";
		String  rc = getServletContext().getInitParameter(parmName);
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc;
	}



}