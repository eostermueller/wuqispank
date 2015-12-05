package org.wuqispank.web.msgs;

import java.io.File;
import java.io.Serializable;

import org.headlessintrace.client.connection.HostPort;

public interface IMessages {
	String getInvalidAgentHostName(String value, String parmName, String className);

	public abstract String getInvalidAgentPortNumber(Exception e, String value,
			String parmName, String className);

	String getInvalidCircularRequestBufferSize(Exception e, String value,
			String parmName, String className);

	public String getConnectionError(HostPort agent);

	String getExportDirNotSet(String webXmlExportDir);

	String getHowToSpecifyExportDir();

	Serializable getExportSuccessfulMsg(String fileName);

	String getBypassImportMessage(String webXmlExportDirProperty, String exportDir);

	String getEmptySqlError(String sql, String uniqueId, int sequence);
	
	String getJdbcConfigurationError(String  propertyVal);

	String getFailureLoadingExportFiles();

	String getInvalidReconnectInterval(Exception e,
			String reconnectIntervalInSeconds, String webXmlReconnectInterval,
			String canonicalName);

	Object getSQL();

	Object getStackTrace();

	String getMissingSqlText();

	String getImportExportInitError();

	String getStartupBanner1();

	String getStartupBanner2(File exportDir);

	String getStartupBannerGeneric(String vals);

	String missingSqlModel(String str);

	String getInvalidColumnMessage();

	String getInvalidNumberOfRequestsToRemoveAtOnce(Exception e,
			String numRequestsToRemoveAtOnce,
			String web_XML_NUM_REQUESTS_TO_REMOVE_AT_ONCE, String canonicalName);

	String getInvalidInfluxDbHostName(String influxdbHostName,
			String webXmlInfluxedbHost, String canonicalName);

	String getInvalidInfluxDbPortNumber(Exception e, String influxdbPort,
			String webXmlInfluxedbPort, String canonicalName);

	String getInvalidInfluxDbWriteIntervalSeconds(Exception e,
			String influxDbWriteIntervalSeconds,
			String web_XML_INFLUXDB_WRITE_INTERVAL_SECONDS, String canonicalName);

	String getInvalidInfluxDbDbName(String influxdbDbName,
			String webXmlInfluxdbDbName, String canonicalName);

	String getInvalidInfluxDbUser(String influxdbDbUser,
			String webXmlInfluxdbUser, String canonicalName);

	String getInvalidInfluxDbPassword(String influxdbPassword,
			String webXmlInfluxdbPassword, String canonicalName);

	String getInvalidInfluxdbBatchSize(Exception e, String influxdbBatchSize,
			String web_XML_INFLUXDB_BATCH_SIZE, String canonicalName);

	String getInvalidInfluxDbRetentionPolicy(String influxdbRetentionPolicy,
			String webXmlInfluxdbRetentionPolicy, String canonicalName);

	String getInfluxdbHealthCheckName();
	String getIntraceStatusConnected();

	String getIntraceStatusDisconnected();

	Object getInTraceConnectionHealthLabel();

	String getInvalidGrafanaHostName(String grafanaHostName,
			String webXmlGrafanaHost, String canonicalName);

	String getInvalidGrafanaPortNumber(Exception e, String grafanaPort,
			String webXmlGrafanaPort, String canonicalName);

	String getInvalidGrafanaHealthCheckTimeoutInMs(Exception e,
			String grafanaHealthCheckTimeoutInMs,
			String webXmlGrafanaHealthcheckTimeoutMs, String canonicalName);
	

}
