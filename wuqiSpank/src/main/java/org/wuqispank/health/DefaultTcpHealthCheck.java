package org.wuqispank.health;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.codahale.metrics.health.HealthCheck;

public class DefaultTcpHealthCheck extends HealthCheck {

	private int timeoutInMs;
	private int port;
	private String host;

	/*
	 * Socket testing technique stolen from here:
	 * http://stackoverflow.com/questions/1273453/test-socket-connection-with-java
	 * (non-Javadoc)
	 * @see com.codahale.metrics.health.HealthCheck#check()
	 */
	@Override
	protected Result check() throws Exception {
		boolean healthy = false;
		Result result = null;
		  Socket socket = new Socket();
	        InetSocketAddress endPoint = new InetSocketAddress( 
	        			getHost(), 
	        			getPort() );

	        if ( endPoint.isUnresolved() ) {

	            System.out.println("Failure " + endPoint );

	        } else try { 

	            socket.connect(  endPoint , getTimeoutInMs() );
	            healthy = true;

	        } catch( IOException ioe ) {

	        	healthy = false;

	        } finally {

	            if ( socket != null ) try {
	                socket.close();
	            } catch( IOException ioe ) {}

	        }
			String id = org.wuqispank.health.Result.format(
					org.wuqispank.health.Result.GRAFANA,
					this.getHost(),
					this.getPort() );
					
			if (healthy) {
				result = HealthCheck.Result.healthy( id );
			} else {
				result = HealthCheck.Result.unhealthy( id );
			}
			return result;
	     }

	public int getTimeoutInMs() {
		return this.timeoutInMs;
	}
	public void setTimeoutInMs(int val) {
		this.timeoutInMs = val;
	}

	public int getPort() {
		return this.port;
	}
	public void setPort(int val) {
		this.port = val;
	}

	public String getHost() {
		return this.host;
	}
	public void setHost(String val) {
		this.host = val;
	}
	
	

}
