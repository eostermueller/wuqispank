package org.wuqispank.influxdb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.wuqispank.DefaultFactory;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.WebXmlConfigImpl;

import com.codahale.metrics.health.HealthCheck;



public class InfluxdbServletContextListener implements ServletContextListener {

	DefaultInfluxdbWriter influxdbWriter = new DefaultInfluxdbWriter(); 
	
	/**
	 * This load-at-web-startup class registers for new web/sql events
	 * so they can be written to influxdb for performance analysis (by grafana).
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		IConfig config = new WebXmlConfigImpl(servletContextEvent.getServletContext());
		
		this.influxdbWriter.setConfig(config);
		
		this.influxdbWriter.initInfluxdb();

		DefaultFactory.getFactory().getRequestManager().registerListener(this.influxdbWriter);
		
		HealthCheck influxdbHealthCheck = new DefaultInfluxdbHealthCheck( 
				this.influxdbWriter.getInfluxDB(), 
				config.getInfluxDbHost(), 
				config.getInfluxDbPort() ); 
		DefaultFactory
			.getFactory()
			.getHealthCheckRegistry()
			.register(
					DefaultFactory.getFactory().getMessages().getInfluxdbHealthCheckName(),
					influxdbHealthCheck
					);
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	

}
