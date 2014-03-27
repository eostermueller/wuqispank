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

public class TestImporter {
	private static final String EXPECTED_UNIQUE_ID = "ce23db0a-fb2b-49fa-a2b3-62ff831eedb0";
	private static final String EXPECTED_SQL_0 = "INSERT INTO Location (name, address) VALUES(?,?)";
	private static final String EXPECTED_SQL_1 = "INSERT INTO Event (name, description, date,location) VALUES(?, ?, ?, ?)";
	private static final long EXPECTED_ENTRY_TIME_0 = 1395747370968L;
	private static final long EXPECTED_ENTRY_TIME_1 = 1395747371009L;

	private static final String[] EXPECTED_STACK_TRACE_0 = {
		"org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource)"
		,"org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281)"
		,"org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313)"
		,"sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source)"
		,"org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)"
	};
	private static final String[] EXPECTED_STACK_TRACE_1 = {
		"org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource)"
		,"org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281)"
		,"org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313)"
		,"sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source)"
		,"sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)"
		,"java.lang.reflect.Method.invoke(Method.java:606)"
		,"org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)"
	};

	@Test
	public void canImportRqFromXml() throws ParserConfigurationException, SAXException, IOException, WuqispankException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(getXml().getBytes());
		IRequestImporter ri = DefaultFactory.getFactory().getRequestImporter();
		ri.setInputStream(bais);
		IRequestWrapper[] requests = ri.importRq();
		
		assertEquals("Expected to have unmarshalled a single rq from xml, but found wrong count of rq",1,requests.length);
		
		IRequestWrapper rq = requests[0];
		
		assertNotNull("Expected to have unmarshalled a single non-null rq from xml file.  Instead found null",rq);
		
		assertEquals("Unable to unmarshall correct unique id from xml", EXPECTED_UNIQUE_ID, rq.getUniqueId() );
		List<ISqlWrapper> sql = rq.getSql();
		
		assertEquals("Unable to unmarshall correct number of sql statements from xml file", 2, sql.size());
		
		ISqlWrapper sql0 = sql.get(0);
		ISqlWrapper sql1 = sql.get(1);
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_0, sql0.getSqlText() );
		assertEquals("Unable to find correctly unmarshalled sql statement", EXPECTED_SQL_1, sql1.getSqlText() );
		
		assertEquals("Unable to find correctly unmarshalled entry time", EXPECTED_ENTRY_TIME_0, sql0.getAgentEntryTimeMillis());
		assertEquals("Unable to find correctly unmarshalled entry time", EXPECTED_ENTRY_TIME_1, sql1.getAgentEntryTimeMillis());
		
		assertEquals("Unable to find correctly unmarshalled exit time", EXPECTED_ENTRY_TIME_0, sql0.getAgentExitTimeMillis());
		assertEquals("Unable to find correctly unmarshalled exit time", EXPECTED_ENTRY_TIME_1, sql1.getAgentExitTimeMillis());
		
		validateStackTrace(sql0.getStackTrace().getStackTraceElements(), EXPECTED_STACK_TRACE_0);
		validateStackTrace(sql1.getStackTrace().getStackTraceElements(), EXPECTED_STACK_TRACE_1);
		
	}
	private void validateStackTrace(StackTraceElement[] actualStackTrace,
			String[] expectedStackTraceElements) {
		
		
		assertTrue("Found zero stack trace elements...smthg wrong here",actualStackTrace.length>0);
		
		assertEquals("Did not find right count of stack trace elements",expectedStackTraceElements.length, actualStackTrace.length );
		int count = 0;
		for(String expectedStackTraceElement : expectedStackTraceElements) {
			assertEquals("Missing expected element in stack trace index [" + count + "]", 
					expectedStackTraceElement,
					actualStackTrace[count++].toString() );
					
		}
	}
	
	
	public String getXml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<WuqispankExport>");
		sb.append("  <Rq id='ce23db0a-fb2b-49fa-a2b3-62ff831eedb0' threadId='myThreadId' >");
		sb.append("    <Sql entryTimeMs='23770916' exitTimeMs='23770918'");
		sb.append("    lousyDateTimeMs='1395747370968' seq='0'>");
		sb.append("      <StmtText>INSERT INTO Location (name, address) VALUES(?,?)</StmtText>");
		sb.append("      <StackTrace>");
		sb.append("      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),");
		sb.append("      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),");
		sb.append("      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),");
		sb.append("      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),");
		sb.append("      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>");
		sb.append("    </Sql>");
		sb.append("    <Sql entryTimeMs='23770922' exitTimeMs='23770925'");
		sb.append("    lousyDateTimeMs='1395747371009' seq='1'>");
		sb.append("      <StmtText>INSERT INTO Event (name, description, date,location) VALUES(?, ?, ?, ?)</StmtText>");
		sb.append("      <StackTrace>");
		sb.append("      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),");
		sb.append("      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),");
		sb.append("      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),");
		sb.append("      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),");
		sb.append("      sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),");
		sb.append("      java.lang.reflect.Method.invoke(Method.java:606),");
		sb.append("      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>");
		sb.append("    </Sql>");
		sb.append("  </Rq>");
		sb.append("</WuqispankExport>");
		return sb.toString();
	}

}
