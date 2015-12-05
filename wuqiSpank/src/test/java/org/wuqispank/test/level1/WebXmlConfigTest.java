package org.wuqispank.test.level1;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.model.ITable;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.IFactory;
import org.wuqispank.web.WebXmlConfigImpl;
import org.wuqispank.web.WebXmlConfigurationException;

public class WebXmlConfigTest {

	@Test
	public void canGetCircularBufferSize() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put("circular-request-buffer-size", "500");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		int i = config.getCircularBufferSize();
		assertEquals("Couldn't not find circular buffer size", 500,i);
	}
	@Test
	public void canGetNumberOfRequestsToRemoveAtOneTime() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_NUM_REQUESTS_TO_REMOVE_AT_ONCE, "321");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		int i = config.getNumberOfRequestsToRemoveAtOnce();
		assertEquals("Couldn't not find right number of requests to remove at one time", 321,i);
	}
	@Test
	public void canGetInfluxDbWriteIntervalSeconds() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_WRITE_INTERVAL_SECONDS, "25");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		int i = config.getInfluxDbWriteIntervalSeconds();
		assertEquals("Couldn't not find right number of requests to remove at one time", 25,i);
	}
	
	@Test
	public void canDetectInvalidCircularBufferSize() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put("circular-request-buffer-size", "non-numeric-should-cause-exception");		
		ServletContext ctx = new TestServletContext(ht);
		IConfig config = new WebXmlConfigImpl(ctx);
		try {
			int i = config.getCircularBufferSize();
			fail("Should have received an exception, invalid non-numeric data were configured.");
		} catch (WebXmlConfigurationException wxce) {
			//only way to pass this test
			assertNotNull("Whoops.  Didn't receive a proper exception", wxce);
		}
	}
	@Test
	public void canFlagTablesAsShouldBeCached() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_CACHED_TABLES, "foo,bar");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		ITable fooTable = testFactory.getTable("foo");
		assertTrue("Table foo was not correctly marked as 'should be cached'", fooTable.shouldBeCached());
		ITable barTable = testFactory.getTable("bar");
		assertTrue("Table bar was not correctly marked as 'should be cached'", barTable.shouldBeCached());
		ITable helloWorldTable = testFactory.getTable("helloWorld");
		assertFalse("Table helloWorld incorrectly marked as 'should be cached'", helloWorldTable.shouldBeCached());
		
	}
	@Test
	public void canDocumentDefaultHealthCheckInterval() {
		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		
		assertEquals("Couldn't not find default value for health check interval",10,config.getHealthCheckIntervalSeconds());
	}
	@Test
	public void canDocumentHealthCheckInterval() {
		
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_HEALTHCHECK_INTERVAL, "5");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		
		assertEquals("Couldn't not find default value for health check interval",5,config.getHealthCheckIntervalSeconds());
	}
	@Test
	public void canFlagGrowthTables() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_GROWTH_TABLES, "foo,bar");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		ITable fooTable = testFactory.getTable("foo");
		assertTrue("Table foo was not correctly marked as 'should be growth'", fooTable.isGrowthTable());
		ITable barTable = testFactory.getTable("bar");
		assertTrue("Table bar was not correctly marked as 'should be growth'", barTable.isGrowthTable());
		ITable helloWorldTable = testFactory.getTable("helloWorld");
		assertFalse("Table helloWorld incorrectly marked as 'growth table'", helloWorldTable.isGrowthTable());
		
	}
	@Test
	public void canGetGrafanaPort() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_GRAFANA_PORT, "5301");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right port number for grafana from the config object",5301,config.getGrafanaPort());
	}
	@Test
	public void canGetGrafanaHealthCheckTimeoutInMs() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_GRAFANA_HEALTHCHECK_TIMEOUT_MS, "5301");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find healthcheck timeout in ms for grafana",5301,config.getGrafanaHealthCheckTimeoutInMs() );
	}
	@Test
	public void canGetGrafanaHost() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_GRAFANA_HOST, "myhost.com");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right hostname for grafana from the config object","myhost.com",config.getGrafanaHost());
	}
	@Test
	public void canGetInfluxDbPort() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_PORT, "5301");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right port number for influxdb from the config object",5301,config.getInfluxDbPort());
	}
	@Test
	public void canGetInfluxDbHost() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_HOST, "myhost.com");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right hostname for influxdb from the config object","myhost.com",config.getInfluxDbHost());
	}
	@Test
	public void canGetInfluxDbName() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_DB_NAME, "myinfluxdb");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right influxdb dbname from the config object","myinfluxdb",config.getInfluxdbDbName());
	}
	@Test
	public void canGetInfluxUser() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_USER, "myuser");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right influxdb user from the config object","myuser",config.getInfluxDbUser());
	}
	@Test
	public void canGetInfluxPassword() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_PASSWORD, "mypassword");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right influxdb password from the config object","mypassword",config.getInfluxDbPassword());
	}
	@Test
	public void canGetInfluxBatchSize() {
		Map<String,String> ht = new Hashtable<String,String>();
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_BATCH_SIZE, "500");		
		ServletContext ctx = new TestServletContext(ht);
		
		IConfig config = new WebXmlConfigImpl(ctx);
		IFactory testFactory = new DefaultFactory();
		testFactory.setConfig(config);
		assertEquals("Couldn't find right influxdb batch size from the config object",500,config.getInfluxdbBatchSize());
	}
}
