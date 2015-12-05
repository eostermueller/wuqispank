package org.wuqispank.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.wuqispank.WuqispankException;

public class DefaultRequestManager implements IRequestManager {
	List<IRequestListener> listeners = new CopyOnWriteArrayList<IRequestListener>();

	@Override
	public void registerListener(IRequestListener requestListener1) {
		this.listeners.add(requestListener1);
	}

	@Override
	public void add(IRequestWrapper rw) throws WuqispankException {
		for(IRequestListener listener : this.listeners) {
			listener.add(rw);
		}
		
	}
	
	

}
