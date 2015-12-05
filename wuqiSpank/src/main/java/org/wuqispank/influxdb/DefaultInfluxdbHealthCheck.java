package org.wuqispank.influxdb;

import java.io.PrintWriter;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.web.EventCollector;

import com.codahale.metrics.health.HealthCheck;

public class DefaultInfluxdbHealthCheck extends HealthCheck {
	static Logger LOG = LoggerFactory.getLogger(DefaultInfluxdbHealthCheck.class);	
	/**
	 * Use of the string 'unknown' to indicate an 'unhealthy' ping came from this test case:
	 * https://github.com/influxdb/influxdb-java/blob/master/src/test/java/org/influxdb/InfluxDBTest.java
	 */
	private static final String INFLUXDB_UNKNOWN_VERSION = "unknown";
	private static final int UNINIT = -1;
	private static final String UNIT_STRING = "<uninit>";
	private InfluxDB influxdb = null;
    private String host = null;
    private int port = UNINIT;
	public DefaultInfluxdbHealthCheck(InfluxDB val, String hostName, int port) {
		this.influxdb = val;
		this.host = hostName;
		this.port = port;
	}

	@Override
	protected Result check() throws Exception {
		String id = org.wuqispank.health.Result.format(
				org.wuqispank.health.Result.INFLUXDB,
				this.host,
				this.port);

		Result result = Result.unhealthy(id); //assume the worst
		try {
			Pong pong = this.influxdb.ping();
			if (pong != null &&
					pong.getVersion() !=null &&
					!pong.getVersion().equalsIgnoreCase(INFLUXDB_UNKNOWN_VERSION)) {
				LOG.debug("InfluxDB [" + this.influxdb.describeDatabases().toString() + "] is healthy [" + this.influxdb.version() + "]");
				result  = Result.healthy(id, pong.getVersion() ); 
			}
			
		} catch (Throwable e) {
			LOG.debug("InfluxDB is sick");
			//LOG.debug( this.influxdb.describeDatabases().toString() );
//			e.printStackTrace();
		}
			
		return result;
	}


}
