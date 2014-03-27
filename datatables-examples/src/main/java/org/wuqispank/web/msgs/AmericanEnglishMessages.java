package org.wuqispank.web.msgs;

import java.io.File;
import java.io.Serializable;

import org.intrace.client.connection.HostPort;


public class AmericanEnglishMessages implements IMessages {

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
		return "Wuqispank can't export files because the web.xml property [" + webXmlExportDir + "] is not set";
	}
	@Override
	public String getBypassImportMessage(String webXmlExportDirProperty, String exportDir) {
		return "Bypassing the import of persisted Wuqispank traces.  The web.xml property [" + webXmlExportDirProperty + "] either wasn't set or the specified folder [" + exportDir + "] doesn't exist.";
	}

	@Override
	public String getHowToSpecifyExportDir() {
		return "To create an export directory, add the following to wuqispank's web.xml and restart wuqispank.  Wuqispank will attempt to create this folder if it doesn't already exist.\n";
	}

	@Override
	public String getExportSuccessfulMsg(String fileName) {
		return "The request was successfully exported to [" + fileName + "] on the wuqispank server";
	}
}
