package org.wuqispank.web.msgs;

import java.io.File;
import java.io.Serializable;

import org.intrace.client.connection.HostPort;


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
			error = "In web.xml, found property named  <param-name>org.intrace.jdbc.IJdbcProvider</param-name> with null or empty value\n" +
					"Was expecting something like <param-value>org.intrace.jdbc.HsqldbProvider</param-value>";
		} else {
			error = "In web.xml, found property named  <param-name>org.intrace.jdbc.IJdbcProvider</param-name> with value [" + propertyVal + "]\n" + 
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

}
