package org.wuqispank.influxdb;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestListener;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.web.IConfig;



/*
 * Need to implement stats using this:
 * https://dropwizard.github.io/metrics/3.1.0/getting-started/
 * http://stackoverflow.com/questions/22927429/measure-number-of-events-in-the-past-x-minutes-hours
 * 
 * wicket approach to Updating page:
 * http://www.wicket-library.com/wicket-examples-6.0.x/ajax/clock?0
 * 
 */
public class DefaultInfluxdbWriter implements IRequestListener, IInfluxdbWriter {
	private static final int UNINITIALIZED = -1;
	private static final long RECONNECT_DELAY = 2000; //never attempt reconnect more frequently than ever 2 seconds.
	private InfluxDB influxDB = null;
	static AtomicInteger successCount = new AtomicInteger();
	static AtomicInteger failureCount = new AtomicInteger();
	public static int getSuccessCount() {
		return successCount.get();
	}
	public static int getFailureCount() {
		return failureCount.get();
	}
	
	public InfluxDB getInfluxDB() {
		return influxDB;
	}
	public void setInfluxDB(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}
	public void setConfig(IConfig config) {
		this.config = config;
	}
	long lastConnectionAttempt = UNINITIALIZED;
	private IConfig config = null;
	//private boolean connected = false;
	
	void initInfluxdb() {
		
		if (this.lastConnectionAttempt == UNINITIALIZED || 
				((System.currentTimeMillis() - this.lastConnectionAttempt) > RECONNECT_DELAY) ) {
			try {
				this.lastConnectionAttempt = System.currentTimeMillis();
				this.setInfluxDB( InfluxDBFactory.connect(
						this.getConnectionString(this.getConfig()), 
						this.getConfig().getInfluxDbUser(),
						this.getConfig().getInfluxDbPassword() )
						);
				
				this.getInfluxDB().createDatabase(getConfig().getInfluxdbDbName());
				
				this.getInfluxDB().enableBatch(
						this.getConfig().getInfluxdbBatchSize(), 
						this.getConfig().getInfluxDbWriteIntervalSeconds(), 
						TimeUnit.SECONDS);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private IConfig getConfig() {
		return this.config;
	}

	/**
	 * builds a string like 'http://localhost:8086' based on configuration from the web.xml file.
	 * The string will be used here:
	 * <pre>
			import org.influxdb.InfluxDB;
			import org.influxdb.InfluxDBFactory;	 
			
	 		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
	  </pre>
	 * @param config
	 * @return
	 */
	public String getConnectionString(IConfig config) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("http://")
			.append(config.getInfluxDbHost() )
			.append(":")
			.append(""+config.getInfluxDbPort() ); //convert the int to string
		
		return sb.toString();
	}
	/**
	 * when wuqiSpank detects a new request, is it passed into this method.
	 */
	@Override
	public void add(IRequestWrapper rw) throws WuqispankException {
		for(ISqlWrapper sw : rw.getSql() ) {
			writeSqlMetric(rw.getUniqueId(),sw);
		}
		
	}
	private void writeSqlMetric(String parentId, ISqlWrapper sw) {
		
		
		try {
			Point p = Point.measurement( "ws.sql." + sw.getUnique() )
	                .time(sw.getAgentEntryTimeMillis(), TimeUnit.MILLISECONDS)
	                .field("value", sw.getAgentExitTimeMillis() - sw.getAgentEntryTimeMillis())
	                .tag("webRqId",parentId)
	                .tag("appSrv","myAppSrv" )
		            .tag("instance","myInstance" )
		            .tag("webCtx","myWebCtx" )
		            .tag("dbSrv", "myDbSrv" )                
	                .tag("sqlType",sw.getSqlModel().getSqlType().toString())
	                .build();
			
			this.getInfluxDB().write(
						getConfig().getInfluxdbDbName(),
						getConfig().getInfluxdbRetentionPolicy(),
						p
					);
			successCount.incrementAndGet();
		} catch (Exception e) {
			failureCount.incrementAndGet();
			e.printStackTrace();
		}
	}

}
