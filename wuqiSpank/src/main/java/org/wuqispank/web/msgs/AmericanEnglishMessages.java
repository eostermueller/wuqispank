package org.wuqispank.web.msgs;

import java.io.File;
import java.io.Serializable;

import org.headlessintrace.client.connection.HostPort;
import org.wuqispank.web.WebXmlConfigImpl;


public class AmericanEnglishMessages implements IMessages, java.io.Serializable {

	@Override
	public String getInvalidAgentHostName(String value, String parmName,
			String className) {
		return "Received [" + value + "].  Missing or blank init-param [" + parmName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidAgentPortNumber(Exception e, String value, String parmName,String className) {
		return "Was expecting a numeric TCP port number for web.xml init-param [" + parmName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";
	}

	@Override
	public String getInvalidCircularRequestBufferSize(Exception e, String value, String parmName,String className) {
		return "Received [" + value + "] and was expecting a numeric value for the size of the circular buffer for requests.  init-param [" + parmName + "] in web.xml for [" + className + "] Exception [" + e.toString() + "]";
	}
	@Override
	public String getConnectionError(HostPort agent) {
		return "Error connecting to [" + agent.toString() + "]";
	}

	@Override
	public String getExportDirNotSet(String webXmlExportDir) {
		return "Wuqispank can\"t export files because the web.xml property [" + webXmlExportDir + "] is not set";
	}
	@Override
	public String getBypassImportMessage(String webXmlExportDirProperty, String exportDir) {
		return "Bypassing the import of persisted Wuqispank traces.  The web.xml property [" + webXmlExportDirProperty + "] either wasn\"t set or the specified folder [" + exportDir + "] doesn\"t exist.";
	}

	@Override
	public String getHowToSpecifyExportDir() {
		return "To create an export directory, add the following to wuqispank\"s web.xml and restart wuqispank.  Wuqispank will attempt to create this folder if it doesn\"t already exist.\n";
	}

	@Override
	public String getExportSuccessfulMsg(String fileName) {
		return "The request was successfully exported to [" + fileName + "] on the wuqispank server";
	}

	@Override
	public String getEmptySqlError(String sql, String uniqueId, int sequence) {
		return "Found null/blank/empty sql statement [" + sql + "] in request with unique id [" + uniqueId + "] and seq [" + sequence + "]";
	}

	@Override
	public String getJdbcConfigurationError(String propertyVal) {
		String error = null;
		if (propertyVal == null || propertyVal.trim().equals("")) {
			error = "In web.xml, found property named  <param-name>org.headlessintrace.jdbc.IJdbcProvider</param-name> with null or empty value\n" +
					"Was expecting something like <param-value>org.headlessintrace.jdbc.HsqldbProvider</param-value>";
		} else {
			error = "In web.xml, found property named  <param-name>org.headlessintrace.jdbc.IJdbcProvider</param-name> with value [" + propertyVal + "]\n" + 
					"Unable able to load java class [" + propertyVal + "].  Probably a classpath issue.";
		}
		return error;
	}

	@Override
	public String getFailureLoadingExportFiles() {
		return "Failed to load export files at wuqiSpank startup.";
	}
	@Override
	public String getInvalidReconnectInterval(Exception e, String value, String parmName,String className) {
		return "Was expecting the number of seconds between wuqiSpank attempts to reconnect to SUT.  Was expecting this in web.xml init-param [" + parmName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";
	}

	/**
	 * I created this to display on the Table Access Timeline.
	 * The text is perpendicular to the horizon and displays once per row.
	 */
	@Override
	public Object getSQL() {
		return "SQL";
	}

	@Override
	public Object getStackTrace() {
		return "stack\ntrace";
	}

	@Override
	public String getMissingSqlText() {
		return "Sql text presented for parsing was null or zero length.";
	}

	@Override
	public String getImportExportInitError() {
		return "Unable to import from file";
	}

	@Override
	public String getStartupBanner1() {
		return "#########################################";
	}

	private static final String LOG_PREFIX = "##    ";
	@Override
	public String getStartupBanner2(File exportDir) {
		return LOG_PREFIX + "wuqiSpank started.\n"
				+ LOG_PREFIX + "export-dir=" + exportDir.getAbsolutePath();
	}

	@Override
	public String getStartupBannerGeneric(String val) {
		return LOG_PREFIX + val;
	}

	@Override
	public String missingSqlModel(String str) {
		return "ISqlParser#setModel() must be set before calling ISqlParser#parse().  For sql [" + str + "]";
	}
	@Override
	public String getInvalidColumnMessage() {
		return "Attempt to add a null column.";
	}

	@Override
	public String getInvalidNumberOfRequestsToRemoveAtOnce(
			Exception e,
			String value,
			String parmName, 
			String className) {
		
		String error = "Found web.xml configuration [" + value + "] for property [" + parmName + "].  Was expecting a numeric value that says how many requests will be discarded (yes, data loss) at a time.  " +
				"This discard happens when the stored request count exceeds the value specified by the web.xml parameter [" + WebXmlConfigImpl.WEB_XML_CIRCULAR_REQUEST_BUFFER_SIZE + "].   for [" + className + "] Exception [" + e.toString() + "]";
		return error;
	}

	@Override
	public String getInvalidInfluxDbHostName(String value,
			String paramName, String className) {
		return "Received [" + value + "].  Missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidInfluxDbWriteIntervalSeconds(Exception e,
			String value, String paramName,
			String className) {
		return "Was expecting the InfluxDb write interval (measured in seconds) to be numericto be found in the web.xml init-param [" + paramName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";
	}

	@Override
	public String getInvalidInfluxDbPortNumber(Exception e,
			String value, String paramName,
			String className) {
		return "Was expecting the numeric InfluxDb TCP port number -- between 1 and 65535 -- (for HTTP communications) to be numeric and to be found in the web.xml init-param [" + paramName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";
	}

	@Override
	public String getInvalidInfluxDbDbName(String value,
			String paramName, String className) {
		return "Received [" + value + "].  Missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidInfluxDbUser(String value,
			String paramName, String className) {
		return "Received [" + value + "].  Missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidInfluxDbPassword(String value,
			String paramName, String className) {
		return "Received [" + value + "].  Missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidInfluxdbBatchSize(Exception e,
		String value, String paramName,
		String className) {
		return "Was expecting the numeric InfluxDb batch size to be numeric.  This determines how many web requests should be sent to the influxedb in a single HTTP write operation.  This parameter should found in the web.xml init-param [" + paramName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";	
	}

	@Override
	public String getInvalidInfluxDbRetentionPolicy(
			String influxdbRetentionPolicy,
			String paramName, String className) {
		return "Received [" + influxdbRetentionPolicy + "], so this is a missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]" +
			"see https://influxdb.com/docs/v0.9/administration/administration.html or http://www.kibinlabs.com/influxdb-continuous-queries-and-retention-policies/ for how this parameter is used.";
	}
	@Override
	public String getInfluxdbHealthCheckName() {
		return "InfluxDBHealthCheck";
	}

	@Override
	public String getIntraceStatusConnected() {
		return "Connected";
	}
	@Override
	public String getIntraceStatusDisconnected() {
		return "Disconnected";
	}

	@Override
	public Object getInTraceConnectionHealthLabel() {
		return "InTrace Connection to: ";
	}

	@Override
	public String getInvalidGrafanaHostName(String grafanaHostName,
			String paramName, String className) {
		return "Received [" + grafanaHostName + "].  Missing or blank init-param [" + paramName + "] in web.xml for [" + className + "]";
	}

	@Override
	public String getInvalidGrafanaPortNumber(Exception e,
			String value, String paramName,
			String className) {
		return "Was expecting the numeric Grafana TCP port number -- between 1 and 65535 -- (for HTTP communications) to be numeric and to be found in the web.xml init-param [" + paramName + "], but instead received [" + value + "].  Exception reported by [" + className + "] Exception [" + e.toString() + "]";
	}

	@Override
	public String getInvalidGrafanaHealthCheckTimeoutInMs(Exception e,
			String grafanaHealthCheckTimeoutInMs,
			String webXmlGrafanaHealthcheckTimeoutMs, String canonicalName) {
		// TODO Auto-generated method stub
		return null;
	}


}
