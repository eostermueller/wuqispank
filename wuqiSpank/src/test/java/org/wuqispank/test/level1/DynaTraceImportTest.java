package org.wuqispank.test.level1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.IRequestImporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.xml.sax.SAXException;

public class DynaTraceImportTest {
	private static final String EXPECTED_UNIQUE_ID = "ce23db0a-fb2b-49fa-a2b3-62ff831eedb0";
	private static final String EXPECTED_SQL_0 = "INSERT INTO Location (name, address) VALUES(?,?)";
	private static final String EXPECTED_SQL_1 = "INSERT INTO Event (name, description, date,location) VALUES(?, ?, ?, ?)";

	@Test
	public void canImportFromDynaTracePurePathXml() throws ParserConfigurationException, SAXException, IOException, WuqispankException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(getXml().getBytes());
		IRequestImporter ri = DefaultFactory.getFactory().getDynaTracePurePathImporter();
		ri.setInputStream(bais);
		IRequestWrapper[] requests = ri.importRq();
		
		assertEquals("Expected to have unmarshalled a single rq from xml, but found wrong count of rq",1,requests.length);
		
		IRequestWrapper rq = requests[0];
		
		assertNotNull("Expected to have unmarshalled a single non-null rq from xml file.  Instead found null",rq);
		assertNotNull("unique id shouldn't have been null", rq.getUniqueId() );
		assertTrue("unique id should have been longer than 15 characters", rq.getUniqueId().length() > 15 );
		
//		assertEquals("Unable to unmarshall correct unique id from xml", EXPECTED_UNIQUE_ID, rq.getUniqueId() );
		List<ISqlWrapper> sql = rq.getSql();
		
		assertEquals("Unable to unmarshall correct number of sql statements from xml file", 2, sql.size());
		
		ISqlWrapper sql0 = sql.get(0);
		ISqlWrapper sql1 = sql.get(1);
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_0, sql0.getSqlText() );
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_1, sql1.getSqlText() );
		
	}
	
	public String getXml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<purepath_execution_tree>");
		sb.append("  <enhancedsamplingnodeinfo level=\"28\" method=\"executeQuery()\"");
		sb.append("  error_type=\"\"");
		sb.append("  argument=\"INSERT INTO Location (name, address) VALUES(?,?)\"");
		sb.append("  exec_total_ms=\"0.6401429176330566\"");
		sb.append("  breakdown=\"CPU Total: 0.183 ms, Sync Total: 0 ms, Wait Total: 0 ms, Suspension Total: 0 ms\"");
		sb.append("  class=\"$Proxy159\" api=\"JDBC\"");
		sb.append("  agent=\"UI-NG-WS_ACBS-PHX[DEVWASQPX8Node03Cell-server1]@devwasqpx8:9644\"");
		sb.append("  elapsed_time_ms=\"32.70489427540451\" />");
		sb.append("  <enhancedsamplingnodeinfo level=\"28\" method=\"executeQuery()\"");
		sb.append("  error_type=\"\"");
		sb.append("  argument=\"INSERT INTO Event (name, description, date,location) VALUES(?, ?, ?, ?)\"");
		sb.append("  exec_total_ms=\"0.6401429176330566\"");
		sb.append("  breakdown=\"CPU Total: 0.183 ms, Sync Total: 0 ms, Wait Total: 0 ms, Suspension Total: 0 ms\"");
		sb.append("  class=\"$Proxy159\" api=\"JDBC\"");
		sb.append("  agent=\"UI-NG-WS_ACBS-PHX[DEVWASQPX8Node03Cell-server1]@devwasqpx8:9644\"");
		sb.append("  elapsed_time_ms=\"32.70489427540451\" />");
		sb.append("</purepath_execution_tree>");
		return sb.toString();
	}

}
