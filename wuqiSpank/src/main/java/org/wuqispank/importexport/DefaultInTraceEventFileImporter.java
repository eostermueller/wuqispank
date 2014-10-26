package org.wuqispank.importexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.client.connection.DefaultConnectionList;
import org.headlessintrace.client.connection.IConnection;
import org.headlessintrace.client.request.RequestConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.WuqispankException;
import org.xml.sax.SAXException;

public class DefaultInTraceEventFileImporter extends AbstractFileImporter {
	static Logger LOG = LoggerFactory.getLogger(DefaultInTraceEventFileImporter.class);

	@Override
	public String getPathMatcherText() {
		return "glob:**.inTrace";
	}

	@Override
	public void importFile(IImportExportMgr iem, File fileToImport)
			throws SAXException, IOException, ParserConfigurationException,
			WuqispankException {
		this.setInputStream( new FileInputStream(fileToImport) );

		List<IConnection> connections = 			
				DefaultConnectionList.getSingleton().getConnections();

		if (connections.size() == 1) {
			if (!(connections.get(0) instanceof RequestConnection)) {
				LOG.error("Aborting import of inTrace log file.  Expecting an instance of RequestConnection.  Instead, found [" + connections.get(0).getClass().getName() + "]");
				return;
			}
		} else {
			LOG.error("Aborting import of inTrace log file.  Expecting a single connection, but instead found [" + connections.size() + "]"); 
			return;
		}
		RequestConnection myConnection = (RequestConnection)connections.get(0);

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()));
	 
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			myConnection.getTraceWriter().writeTraceEvent(line, myConnection.getHostPort());
		br.close();		
		
	}

}
