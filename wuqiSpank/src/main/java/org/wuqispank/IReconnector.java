package org.wuqispank;

import org.headlessintrace.client.connection.IConnection;

public interface IReconnector extends Runnable {

	void disconnectAll();

	IConnection getConnection() throws WuqispankException;

}
