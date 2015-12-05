package org.wuqispank;

import org.headlessintrace.client.connection.ConnectionException;
import org.headlessintrace.client.connection.ConnectionTimeout;
import org.headlessintrace.client.connection.DefaultConnectionList;
import org.headlessintrace.client.connection.IConnection;
import org.headlessintrace.client.request.BadCompletedRequestListener;
import org.headlessintrace.client.request.RequestConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.web.EventCollector;

public class DefaultReconnector implements IReconnector {
	static Logger LOG = LoggerFactory.getLogger(DefaultReconnector.class);

	/**
	 * Recreates a connection (a socket) with Headless Intrace, but 
	 * only if the connection has been broken.
	 */
	@Override
	public void run() {
		for(IConnection con : DefaultConnectionList.getSingleton().getConnections()) {
			if (con instanceof RequestConnection) {
				RequestConnection reqCon = (RequestConnection)con;
				if (!reqCon.isConnected()) {
					connect(reqCon);
					LOG.debug("Reconnected? [" + reqCon.isConnected() + "] [" + con.getHostPort().toString3() + "] ");
				}
			}
		}
	}
	@Override
	public void disconnectAll() {
		for(IConnection con : DefaultConnectionList.getSingleton().getConnections()) {
			if (con instanceof RequestConnection) {
				RequestConnection reqCon = (RequestConnection)con;
				if (reqCon.isConnected()) {
					reqCon.disconnect();
					LOG.debug("Reconnected? [" + reqCon.isConnected() + "] [" + con.getHostPort().toString3() + "] ");
				}
			}
		}
		
	}

	private void connect(RequestConnection reqCon) {
		try {
			if (!reqCon.isConnected())
				reqCon.connect(reqCon.getHostPort(), reqCon.getCommandArray() );
		} catch (ConnectionTimeout e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadCompletedRequestListener e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	@Override
	public IConnection getConnection() throws WuqispankException {
		IConnection singleConn = null;
		int connectionCount = 0;
		
		for(IConnection con : DefaultConnectionList.getSingleton().getConnections()) {
			connectionCount++;
			if (con instanceof RequestConnection) {
				singleConn = con;
			}
		}
		if (connectionCount !=1) {
			throw new WuqispankException("Was expecting exactly 1 connection to intrace-agent.jar, but instead found [" + connectionCount + "]");
		}
		return singleConn;
		
	}

}
