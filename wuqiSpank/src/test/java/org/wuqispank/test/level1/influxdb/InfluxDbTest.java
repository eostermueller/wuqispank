package org.wuqispank.test.level1.influxdb;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.wuqispank.influxdb.DefaultInfluxdbWriter;
import org.wuqispank.test.level1.TestServletContext;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.WebXmlConfigImpl;

public class InfluxDbTest {
	@Test
	public void canBuildUrlToConnecToInfluxDb() {

		Map<String,String> ht = new Hashtable<String,String>();
		ServletContext ctx = new TestServletContext(ht);
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_PORT, "8086");		
		ht.put(WebXmlConfigImpl.WEB_XML_INFLUXDB_HOST, "myhost.com");		
		
		IConfig config = new WebXmlConfigImpl(ctx);
		DefaultInfluxdbWriter influxdbWriter = new DefaultInfluxdbWriter();
		
		assertEquals(
				"Unable to create connection string for influxdb",
				"http://myhost.com:8086",
				influxdbWriter.getConnectionString(config) );
	}
	

}
