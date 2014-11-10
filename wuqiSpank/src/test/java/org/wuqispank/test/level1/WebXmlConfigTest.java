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
}
