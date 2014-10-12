package com.wuqispank.test.importexport.level1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.importexport.IRequestImporter;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.test.level1.TestServletContext;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.IFactory;
import org.wuqispank.web.WebXmlConfigImpl;
import org.xml.sax.SAXException;

public class ImportRawSqlTest {
	private static final String EXPECTED_SQL_0 = "INSERT INTO Location (name, address) VALUES(?,?)";
	private static final String EXPECTED_SQL_1 = "INSERT INTO Event (name, description, date,location) VALUES(?, ?, ?, ?)";
	private static final String EXPECTED_SQL_2 = "INSERT INTO Organization (hierarchyLevel, id) VALUES(?,?)";
	private static final String EXPECTED_SQL_3 = "INSERT INTO History (name, description, date,location) VALUES(?, ?, ?, ?)";

	@Test
	public void canDetectDefaultRawSqlStmtDelimiter() {
		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		
		String sqlStmtDelimiter = config.getRawSqlStmtDelimiter();
		assertEquals("For an import file with raw SQL statements, couldn't find the right default delimeter",
				sqlStmtDelimiter, WebXmlConfigImpl.RAW_SQL_STMT_DELIMITER);
	}
	@Test
	public void canDetectRawRequestDelimiter() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_RAW_SQL_REQUEST_DELIMITER, "!");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		
		String requestDelimiter = config.getRawSqlRequestDelimiter();
		assertEquals("For an import file with raw SQL statements, couldn't find the right default delimeter to separate multiple requests",
				requestDelimiter,"!");
	}
	@Test
	public void canDetectRawSqlStmtDelimiter() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_RAW_SQL_STMT_DELIMITER, "@");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		
		String sqlStmtDelimiter = config.getRawSqlStmtDelimiter();
		assertEquals("For an import file with raw SQL statements, couldn't find the right default delimeter",
				sqlStmtDelimiter, "@");
	}
	@Test
	public void canDetectDefaultRawRequestDelimiter() {
		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		
		String requestDelimiter = config.getRawSqlRequestDelimiter();
		assertEquals("For an import file with raw SQL statements, couldn't find the right default delimeter to separate multiple requests",
				requestDelimiter,WebXmlConfigImpl.RAW_SQL_REQUEST_DELIMITER);
	}
	
	@Test
	public void canImportTwoSqlStmtFromRawText() throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory factory = new DefaultFactory();
		factory.setConfig(config);
		
		
		ByteArrayInputStream bais = new ByteArrayInputStream(getRawText().getBytes());
		IRequestImporter ri = DefaultFactory.getFactory().getRawSqlTextRequestImporter();
		ri.setRequestIdPrefix("testA");
		ri.setInputStream(bais);
		IRequestWrapper[] requests = ri.importRq();
		
		assertEquals("Expected to have unmarshalled a single rq from xml, but found wrong count of rq",1,requests.length);
		
		IRequestWrapper rq = requests[0];
		
		assertNotNull("Expected to have unmarshalled a single non-null rq from xml file.  Instead found null",rq);
		
		assertEquals("Unable to unmarshall correct unique id from xml", "testA-1", rq.getUniqueId() );
		List<ISqlWrapper> sql = rq.getSql();
		
		assertEquals("Unable to unmarshall correct number of sql statements from xml file", 2, sql.size());
		
		ISqlWrapper sql0 = sql.get(0);
		ISqlWrapper sql1 = sql.get(1);
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_0, sql0.getSqlText() );
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_1, sql1.getSqlText() );
		
	}
	@Test
	public void canImportTwoRequestsFromRawText() throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory factory = new DefaultFactory();
		factory.setConfig(config);
		
		
		ByteArrayInputStream bais = new ByteArrayInputStream(getRawTextForTwoRequests().getBytes() );
		IRequestImporter ri = DefaultFactory.getFactory().getRawSqlTextRequestImporter();
		ri.setRequestIdPrefix("testA");
		ri.setInputStream(bais);
		IRequestWrapper[] requests = ri.importRq();
		
		assertEquals("Expected to have unmarshalled a single rq from xml, but found wrong count of rq",2,requests.length);
		
		IRequestWrapper rq = requests[0];
		
		assertNotNull("Expected to have unmarshalled a single non-null rq from xml file.  Instead found null",rq);
		
		assertEquals("Unable to unmarshall correct unique id from xml", "testA-1", rq.getUniqueId() );
		List<ISqlWrapper> sql = rq.getSql();
		
		assertEquals("Unable to unmarshall correct number of sql statements from xml file", 2, sql.size());
		
		ISqlWrapper sql0 = sql.get(0);
		ISqlWrapper sql1 = sql.get(1);
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_0, sql0.getSqlText() );
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_1, sql1.getSqlText() );
		//##################
		IRequestWrapper rq2 = requests[1];
		
		assertNotNull("Expected to have unmarshalled two non-null rq from raw text.  Instead found null",rq2);
		
		assertEquals("Unable to unmarshall correct unique id from xml", "testA-2", rq2.getUniqueId() );
		sql = rq2.getSql();
		
		assertEquals("Unable to unmarshall correct number of sql statements from xml file", 2, sql.size());
		
		ISqlWrapper sql2 = sql.get(0);
		ISqlWrapper sql3 = sql.get(1);
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_2, sql2.getSqlText() );
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_3, sql3.getSqlText() );
		
	}
	public String getRawText() {
		StringBuilder sb = new StringBuilder();
		sb.append(EXPECTED_SQL_0);
		sb.append(WebXmlConfigImpl.RAW_SQL_STMT_DELIMITER); 
		sb.append(EXPECTED_SQL_1);
		return sb.toString();
	}
	public String getRawTextForTwoRequests() {
		StringBuilder sb = new StringBuilder();
		sb.append(EXPECTED_SQL_0);
		sb.append(WebXmlConfigImpl.RAW_SQL_STMT_DELIMITER); 
		sb.append(EXPECTED_SQL_1);
		sb.append(WebXmlConfigImpl.RAW_SQL_REQUEST_DELIMITER); //this is the delimiter to separate SQL from two different 'requests'
		sb.append(EXPECTED_SQL_2);
		sb.append(WebXmlConfigImpl.RAW_SQL_STMT_DELIMITER); 
		sb.append(EXPECTED_SQL_3);
		return sb.toString();
	}

}
