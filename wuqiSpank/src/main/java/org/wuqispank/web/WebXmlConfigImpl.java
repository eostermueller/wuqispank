package org.wuqispank.web;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.headlessintrace.client.connection.HostPort;
import org.headlessintrace.jdbc.IJdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.health.DefaultHealthChecker;
import org.wuqispank.web.msgs.IMessages;

import com.codahale.metrics.health.HealthCheckRegistry;

public class WebXmlConfigImpl implements IConfig, java.io.Serializable, IJdbcProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger LOG = LoggerFactory.getLogger(WebXmlConfigImpl.class);
	
	private static final String WEB_XML_INTRACE_AGENT_HOST = "intrace-agent-host";
	private static final String WEB_XML_INTRACE_AGENT_PORT = "intrace-agent-port";
	private static final String WEB_XML_CSV_TABLES_THAT_SHOULD_BE_CACHED = "csv-tables-that-should-be-cached";
	private static final String WEB_XML_CSV_GROWTH_TABLES = "csv-growth-tables";
	public static final String WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE = "circular-request-buffer-size";
	public static final String WEB_XML_EXPORT_DIR = "export-dir";
	private static final String WEB_XML_RECONNECT_INTERVAL = "reconnect-interval-seconds";
	private static final String WEB_XML_EXPORT_DIR_LISTENER_INTERVAL = "export-dir-listener-interval-seconds";
	public static final String WEB_XML_RAW_SQL_REQUEST_DELIMITER = "raw-sql-request-delimiter";
	public static final String WEB_XML_RAW_SQL_STMT_DELIMITER = "raw-sql-stmt-delimiter";
	public static final String WEB_XML_HEALTHCHECK_INTERVAL = "healthcheck-interval-seconds";

	private IMessages msgs = null;
	private IMessages getMsgs() {
		return msgs;
	}

	private void setMsgs(IMessages msgs) {
		this.msgs = msgs;
	}

	private static final int DEFAULT_CIRCULAR_BUFFER_SIZE = 10000;

	public static final String WEB_XML_CACHED_TABLES = "cached-tables";
	public static final String WEB_XML_GROWTH_TABLES = "growth-tables";

	public static final String RAW_SQL_REQUEST_DELIMITER = "~";

	public static final String RAW_SQL_STMT_DELIMITER = ";";

	public static final String WEB_XML_NUM_REQUESTS_TO_REMOVE_AT_ONCE = "number-requests-to-remove-at-once";

	public static final String WEB_XML_GRAFANA_PORT = "grafana-port";
	public static final String WEB_XML_GRAFANA_HOST = "grafana-host";
	public static final String WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS = "grafana-healthcheck-timeout-ms";
	public static final int WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS_DEFAULT = 2000;

	public static final String WEB_XML_INFLUXDB_PORT = "influxdb-port";
	public static final String WEB_XML_INFLUXDB_HOST = "influxdb-host";
	public static final String WEB_XML_INFLUXDB_WRITE_INTERVAL_SECONDS = "influxdb-write-interval-seconds";
	public static final String WEB_XML_INFLUXDB_DB_NAME = "influxdb-dbname";
	public static final String WEB_XML_INFLUXDB_USER = "influxdb-user";
	public static final String WEB_XML_INFLUXDB_PASSWORD = "influxdb-password";
	public static final String WEB_XML_INFLUXDB_BATCH_SIZE = "influxdb-batch-size";
	public static final String WEB_XML_INFLUXDB_RETENTION_POLICY = "influxdb-retention-policy";

	private static final int UNINITIALIZED = -1;




	
	private transient ServletContext m_servletContext;

	private Map<String, Object> m_namesOfTablesThatShouldBeCached = new Hashtable<String,Object>();
	private Map<String, Object> m_namesOfGrowthTables = new Hashtable<String,Object>();

	private ServletContext getServletContext() {
		return m_servletContext;
	}

	private void setServletContext(ServletContext servletContext) {
		this.m_servletContext = servletContext;
	}

	public WebXmlConfigImpl(ServletContext servletContext) {
		setMsgs(org.wuqispank.DefaultFactory.getFactory().getMessages());
		this.m_servletContext = servletContext;
		
		initCachedTables();
		initGrowthTables();
	}


	private void initCachedTables() {
		String temp = this.getServletContext().getInitParameter(WEB_XML_CACHED_TABLES);
		if (temp!=null && temp.trim().length()> 0) {
			String[] tablesThatShouldBeCached = temp.split(",");
			for(String oneTableName : tablesThatShouldBeCached) {
				this.setTableShouldBeCached(oneTableName.trim());
				LOG.info("Web.xml parameter [" + WEB_XML_CACHED_TABLES + "].  Table [" + oneTableName + "] will be marked as 'should be cached.'");
			}
		}
		
	}
	private void initGrowthTables() {
		String temp = this.getServletContext().getInitParameter(WEB_XML_GROWTH_TABLES);
		if (temp!=null && temp.trim().length()> 0) {
			String[] growthTables = temp.split(",");
			for(String oneTableName : growthTables) {
				this.setGrowthTable(oneTableName.trim());
				LOG.info("Web.xml parameter [" + WEB_XML_GROWTH_TABLES + "].  Table [" + oneTableName + "] will be marked as 'growth table.'");
			}
		}
		
	}
	@Override
	public String getInfluxDbHost() {
		String  influxdbHostName = getServletContext().getInitParameter(WEB_XML_INFLUXDB_HOST);
		if (influxdbHostName==null || "".trim().equals(influxdbHostName.trim())) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxDbHostName(influxdbHostName, WEB_XML_INFLUXDB_HOST, this.getClass().getCanonicalName()));
		}
		return influxdbHostName.trim();
	}
	@Override
	public int getInfluxDbPort() {
		String influxdbPort = getServletContext().getInitParameter(WEB_XML_INFLUXDB_PORT);
		int rc = -1;
		try {
			rc = Integer.parseInt(influxdbPort.trim());
		} catch(Exception e) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxDbPortNumber(e, influxdbPort, WEB_XML_INFLUXDB_PORT, this.getClass().getCanonicalName()));
		}
		return rc;
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
		<param-name>org.headlessintrace.jdbc.IJdbcProvider</param-name>
		<param-value>org.headlessintrace.jdbc.HsqldbProvider</param-value>
	 * 
	 */
	public String getJdbcProvider() {
		String  jdbcProviderName = getServletContext().getInitParameter("org.headlessintrace.jdbc.IJdbcProvider");
		if (jdbcProviderName==null)
			//throw new RuntimeException(this.msgs.getJdbcConfigurationError(jdbcProviderName) );
			throw new WebXmlConfigurationException(this.msgs.getJdbcConfigurationError(jdbcProviderName) );
		
		LOG.debug("org.headlessintrace.jdbc.IJdbcProvider is [" + jdbcProviderName + "] from input parameter [" + jdbcProviderName + "]");
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
		final String parmName = "org.headlessintrace.jdbc.IJdbcProvider.version";
		String  rc = getServletContext().getInitParameter(parmName);
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc;
	}

	@Override
	public void setTableShouldBeCached(String tableName) {
		this.m_namesOfTablesThatShouldBeCached.put(tableName, tableName);
	}
	@Override
	public boolean shouldTableBeCached(String tableName) {
		return this.m_namesOfTablesThatShouldBeCached.containsKey(tableName);
	}
	@Override
	public void setGrowthTable(String tableName) {
		this.m_namesOfGrowthTables.put(tableName, tableName);
		
	}
	@Override
	public boolean isGrowthTable(String tableName) {
		return this.m_namesOfGrowthTables.containsKey(tableName);
	}

	@Override
	public String getRawSqlStmtDelimiter() {
		final String parmName = WEB_XML_RAW_SQL_STMT_DELIMITER;
		String  rc = getServletContext().getInitParameter(parmName);
		if (rc==null || rc.trim().equals(""))
				rc = RAW_SQL_STMT_DELIMITER;
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc;
	}

	@Override
	public String getRawSqlRequestDelimiter() {
		final String parmName = WEB_XML_RAW_SQL_REQUEST_DELIMITER;
		String  rc = getServletContext().getInitParameter(parmName);
		if (rc==null || rc.trim().equals(""))
			rc = RAW_SQL_REQUEST_DELIMITER;
		LOG.debug("Found value [" + rc + " for param [" + parmName + "]");
		return rc;
	}

	@Override
	public long getExportDirListenerIntervalInSeconds() {
		int intListenerIntervalSeconds = 1;//default
		String reconnectIntervalInSeconds = getServletContext().getInitParameter(this.WEB_XML_EXPORT_DIR_LISTENER_INTERVAL);
		
		if (reconnectIntervalInSeconds !=null && !"".trim().equals(reconnectIntervalInSeconds)) {
			try {
				intListenerIntervalSeconds = Integer.parseInt(reconnectIntervalInSeconds);
			} catch(Exception e) {
				throw new WebXmlConfigurationException(msgs.getInvalidReconnectInterval(e, reconnectIntervalInSeconds, WEB_XML_EXPORT_DIR_LISTENER_INTERVAL, this.getClass().getCanonicalName()));
			}
		}
		return intListenerIntervalSeconds;
	}

	@Override
	public int getNumberOfRequestsToRemoveAtOnce() {
		int intNumRequestsToRemoveAtOnce = 16384;//default
		String numRequestsToRemoveAtOnce = getServletContext().getInitParameter(this.WEB_XML_NUM_REQUESTS_TO_REMOVE_AT_ONCE);
		
		if (numRequestsToRemoveAtOnce !=null && !"".trim().equals(numRequestsToRemoveAtOnce)) {
			try {
				intNumRequestsToRemoveAtOnce = Integer.parseInt(numRequestsToRemoveAtOnce);
			} catch(Exception e) {
				throw new WebXmlConfigurationException(
						msgs.getInvalidNumberOfRequestsToRemoveAtOnce(
								e, 
								numRequestsToRemoveAtOnce, 
								this.WEB_XML_NUM_REQUESTS_TO_REMOVE_AT_ONCE, 
								this.getClass().getCanonicalName()
							)
						);
			}
		}
		return intNumRequestsToRemoveAtOnce;
	}

	@Override
	public int getInfluxDbWriteIntervalSeconds() {
		int intInfluxDbWriteIntervalSeconds = 10;//default -- same as DynaTrace.
		String influxDbWriteIntervalSeconds = getServletContext().getInitParameter(WebXmlConfigImpl.WEB_XML_INFLUXDB_WRITE_INTERVAL_SECONDS);
		
		if (influxDbWriteIntervalSeconds !=null && !"".trim().equals(influxDbWriteIntervalSeconds)) {
			try {
				intInfluxDbWriteIntervalSeconds = Integer.parseInt(influxDbWriteIntervalSeconds);
			} catch(Exception e) {
				throw new WebXmlConfigurationException(
						msgs.getInvalidInfluxDbWriteIntervalSeconds(
								e, 
								influxDbWriteIntervalSeconds, 
								this.WEB_XML_INFLUXDB_WRITE_INTERVAL_SECONDS, 
								this.getClass().getCanonicalName()
							)
						);
			}
		}
		return intInfluxDbWriteIntervalSeconds;
	}

	@Override
	public String getInfluxdbDbName() {
		String  influxdbDbName = getServletContext().getInitParameter(WebXmlConfigImpl.WEB_XML_INFLUXDB_DB_NAME);
		if (influxdbDbName==null || "".trim().equals(influxdbDbName.trim())) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxDbDbName(influxdbDbName, WebXmlConfigImpl.WEB_XML_INFLUXDB_DB_NAME, this.getClass().getCanonicalName()));
		}
		return influxdbDbName;
	}

	@Override
	public String getInfluxDbUser() {
		String  influxdbDbUser = getServletContext().getInitParameter(WebXmlConfigImpl.WEB_XML_INFLUXDB_USER);
		if (influxdbDbUser==null || "".trim().equals(influxdbDbUser.trim())) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxDbUser(influxdbDbUser, WebXmlConfigImpl.WEB_XML_INFLUXDB_USER, this.getClass().getCanonicalName()));
		}
		return influxdbDbUser;
	}

	@Override
	public String getInfluxDbPassword() {
		String  influxdbPassword = getServletContext().getInitParameter(WebXmlConfigImpl.WEB_XML_INFLUXDB_PASSWORD);
		if (influxdbPassword==null || "".trim().equals(influxdbPassword.trim())) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxDbPassword(influxdbPassword, WebXmlConfigImpl.WEB_XML_INFLUXDB_PASSWORD, this.getClass().getCanonicalName()));
		}
		return influxdbPassword;
	}

	@Override
	public int getInfluxdbBatchSize() {
		String influxdbBatchSize = getServletContext().getInitParameter(this.WEB_XML_INFLUXDB_BATCH_SIZE);
		int rc = UNINITIALIZED;
		try {
			rc = Integer.parseInt(influxdbBatchSize.trim());
		} catch(Exception e) {
			throw new WebXmlConfigurationException(msgs.getInvalidInfluxdbBatchSize(e, influxdbBatchSize, this.WEB_XML_INFLUXDB_BATCH_SIZE, this.getClass().getCanonicalName()));
		}
		return rc;
	}
	@Override
	public String getInfluxdbRetentionPolicy() {
		String  influxdbRetentionPolicy = getServletContext().getInitParameter(WebXmlConfigImpl.WEB_XML_INFLUXDB_RETENTION_POLICY);
		if (influxdbRetentionPolicy==null || "".trim().equals(influxdbRetentionPolicy.trim())) {
			throw new WebXmlConfigurationException(
					msgs.getInvalidInfluxDbRetentionPolicy(
							influxdbRetentionPolicy, 
							WebXmlConfigImpl.WEB_XML_INFLUXDB_RETENTION_POLICY, 
							this.getClass().getCanonicalName() )
			);
		}
		return influxdbRetentionPolicy;
	}

	@Override
	public int getHealthCheckIntervalSeconds() {
		int intHealthcheckIntervalSeconds = 10;//default
		String healthcheckIntervalInSeconds = getServletContext().getInitParameter(this.WEB_XML_HEALTHCHECK_INTERVAL);
		
		if (healthcheckIntervalInSeconds !=null && !"".trim().equals(healthcheckIntervalInSeconds)) {
			try {
				intHealthcheckIntervalSeconds = Integer.parseInt(healthcheckIntervalInSeconds);
			} catch(Exception e) {
				throw new WebXmlConfigurationException(msgs.getInvalidReconnectInterval(e, healthcheckIntervalInSeconds, WEB_XML_RECONNECT_INTERVAL, this.getClass().getCanonicalName()));
			}
		}
		return intHealthcheckIntervalSeconds;
	}
	@Override
	public Runnable getHealthChecker(HealthCheckRegistry registry) {
		return new DefaultHealthChecker( registry );
	}
	@Override
	public String getGrafanaHost() {
		String  grafanaHostName = getServletContext().getInitParameter(WEB_XML_GRAFANA_HOST);
		if (grafanaHostName==null || "".trim().equals(grafanaHostName.trim())) {
			throw new WebXmlConfigurationException(msgs.getInvalidGrafanaHostName(grafanaHostName, WEB_XML_GRAFANA_HOST, this.getClass().getCanonicalName()));
		}
		return grafanaHostName.trim();
	}
	@Override
	public int getGrafanaPort() {
		String grafanaPort = getServletContext().getInitParameter(WEB_XML_GRAFANA_PORT);
		int rc = -1;
		try {
			rc = Integer.parseInt(grafanaPort.trim());
		} catch(Exception e) {
			throw new WebXmlConfigurationException(msgs.getInvalidGrafanaPortNumber(e, grafanaPort, WEB_XML_GRAFANA_PORT, this.getClass().getCanonicalName()));
		}
		return rc;
	}

	@Override
	public int getGrafanaHealthCheckTimeoutInMs() {
		int rc = -1;
		String grafanaHealthCheckTimeoutInMs = getServletContext().getInitParameter(WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS);
		if (grafanaHealthCheckTimeoutInMs==null || grafanaHealthCheckTimeoutInMs.trim().length()==0) {
			rc = WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS_DEFAULT;
		} else {
			try {
				rc = Integer.parseInt(grafanaHealthCheckTimeoutInMs.trim());
			} catch(Exception e) {
				throw new WebXmlConfigurationException(msgs.getInvalidGrafanaHealthCheckTimeoutInMs(e, grafanaHealthCheckTimeoutInMs, WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS, this.getClass().getCanonicalName()));
			}
		}
		return rc;
	}

}
