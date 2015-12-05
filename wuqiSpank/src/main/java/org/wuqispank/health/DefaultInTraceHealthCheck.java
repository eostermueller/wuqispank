package org.wuqispank.health;

import java.util.ArrayList;
import java.util.List;

import org.headlessintrace.client.connection.DefaultConnectionList;
import org.headlessintrace.client.connection.IConnection;
import org.headlessintrace.client.request.RequestConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.influxdb.DefaultInfluxdbHealthCheck;
import org.wuqispank.web.msgs.IMessages;

import com.codahale.metrics.health.HealthCheck;

public class DefaultInTraceHealthCheck extends HealthCheck {
	static Logger LOG = LoggerFactory.getLogger(DefaultInTraceHealthCheck.class);	

	/**
	 * headlessInTrace supports connections from a client to multiple server/port combinations running the intrace-agent.jar.
	 * This class provides the support:
	 * https://github.com/eostermueller/headlessInTraceClient/blob/master/src/main/java/org/headlessintrace/client/connection/DefaultConnectionList.java
	 * 
	 * However, wuqiSpank does not support multiple agents.  
	 * Here are a few wuqiSpank changes required to support multiple agents: 
	 * -- wuqiSpank web.xml has only one parameter for a single host-port combo.  Would need some way to store and read in multiple host-port combos.
	 * -- The following class assumes that threadId is enough of a uniqueId for InTrace events.
	 *    https://github.com/eostermueller/headlessInTraceClient/blob/master/src/main/java/org/headlessintrace/client/request/DefaultRequestSeparator.java
	 *    This code would need to be enhanced to make "host-and-threadId" the unique key instead of just threadId.
	 *    
	 * But I went ahead and coded support in this healthCheck for multiple host-port combos, so its a bit needlessly complex.
	 */
	@Override
	protected Result check() throws Exception {
		Result result =  null;
		int unhealthyCount = 0;
		int totalConnectionCount = 0;
		StringBuilder sb = new StringBuilder();
		List<String> argsForHealthMessage = new ArrayList<String>();
		try {
			IMessages msgs = DefaultFactory.getFactory().getMessages();
			sb.append(msgs.getInTraceConnectionHealthLabel());
					
			int connectionCount = DefaultConnectionList.getSingleton().getConnections().size(); 
			if ( connectionCount==1) {
				
			} else {
				throw new Exception("Found [" + connectionCount + "] InTrace Connections." +
						"Only smart enough to report health on 1 connection.  [" + DefaultConnectionList.getSingleton().toString() + "]");
			}
			IConnection con = DefaultConnectionList.getSingleton().getConnections().get(0);//the only one
			if (con instanceof RequestConnection) {
				if (totalConnectionCount++ > 0)
					sb.append("\n");
				String inTraceStatus = null;
				RequestConnection reqCon = (RequestConnection)con;
				sb.append(" [%s]: %s"); //Display "myserver.com:Connected" 
				argsForHealthMessage.add(reqCon.getHostPort().getHostNameKey());
				if (reqCon.isConnected()) {
					LOG.debug("InTrace at [" + reqCon.getHostPort().getHostNameKey() + "] is connected" );
				} else {
					LOG.debug("InTrace at [" + reqCon.getHostPort().getHostNameKey() + "] is disconnected" );
					unhealthyCount++;
				}
			} else {
				throw new Exception("Unsupport InTrace configuration IConnection must be implemented by RequestConnection");
			}
			
			
			String id = org.wuqispank.health.Result.format(
					org.wuqispank.health.Result.INTRACE,
					con.getHostPort().hostNameOrIpAddress,
					con.getHostPort().port);
					
			if (unhealthyCount==0) {
				result = HealthCheck.Result.healthy( id );
			} else {
				result = HealthCheck.Result.unhealthy( id );
			}
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		return result;
	}

}
