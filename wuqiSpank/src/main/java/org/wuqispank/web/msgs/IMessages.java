package org.wuqispank.web.msgs;

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
	

}
