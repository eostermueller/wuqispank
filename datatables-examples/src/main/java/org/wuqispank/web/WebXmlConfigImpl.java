package org.wuqispank.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.intrace.client.connection.HostPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.web.msgs.IMessages;

public class WebXmlConfigImpl implements IConfig {
	static Logger LOG = LoggerFactory.getLogger(BackgroundSqlCollector.class);
	
	private static final String WEB_XML_INTRACE_AGENT_HOST = "intrace-agent-host";
	private static final String WEB_XML_INTRACE_AGENT_PORT = "intrace-agent-port";
	private static final String WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE = "circular-request-buffer-size";
	public static final String WEB_XML_EXPORT_DIR = "export-dir";
	private IMessages msgs = null;
	private IMessages getMsgs() {
		return msgs;
	}

	private void setMsgs(IMessages msgs) {
		this.msgs = msgs;
	}

	private static final int DEFAULT_CIRCULAR_BUFFER_SIZE = 10000;
	
	private ServletContext m_servletContext;

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
			sb.append("\t\t<param-value>/home/myUser/myFolder</param-value>\n");
			sb.append("<context-param>\n");
			
			LOG.warn(msgs.getHowToSpecifyExportDir() + sb.toString());
		}
		File exportDir = new File(exportDirName);
		if (!exportDir.exists())
			exportDir.mkdirs();
		return exportDir;
	}

}
