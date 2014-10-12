package org.wuqispank.importexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.client.request.IRequest;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.web.IConfig;
import org.xml.sax.SAXException;

public class RawSqlTextRequestImporter extends AbstractRequestImporter implements IRequestImporter {

	@Override
	public IRequestWrapper[] importRq() throws SAXException, IOException,
			ParserConfigurationException, WuqispankException {
		
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()));
	 
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line); //possible enhancement:  strip off prefix added by logging infrastructure
		}
		br.close();		

		List<IRequestWrapper> results = new ArrayList<IRequestWrapper>();
		
		String[] requests = sb.toString().split(
				DefaultFactory.getFactory().getConfig().getRawSqlRequestDelimiter() 
				);
		int requestCounter = 1;
		for(String request : requests) {
			
			IRequest rq = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
			IRequestWrapper rqWrapper = DefaultFactory.getFactory().getRequestWrapper();
			rqWrapper.setRequest(rq);
			results.add(rqWrapper);
			
			rq.setUniqueId( this.getRequestIdPrefix() + "-" + requestCounter++);

			importSql(rqWrapper,request);
		}
		
		IRequestWrapper[] r = {};
		return results.toArray(r);
	}
	private void importSql(IRequestWrapper rqWrapper, String semiColonDelimitedSqlStatements) throws WuqispankException {
		String[] sqlStatements = semiColonDelimitedSqlStatements.split(
				DefaultFactory.getFactory().getConfig().getRawSqlStmtDelimiter() 
				);
		
		int sqlCounter = 1;
		for(String sqlStmt : sqlStatements) {
			ISqlWrapper sqlWrapper = rqWrapper.createBlankSqlWrapper();
			sqlWrapper.setSqlText(sqlStmt);
			sqlWrapper.setSequence(sqlCounter++);
			
			rqWrapper.addSqlWrapper(sqlWrapper);
		}
	}
	@Override
	public boolean isRuntimeImporter() {
		return true;
	}
	@Override
	public String getPathMatcherText() {
		return "glob:**.sqltxt";
	}
}