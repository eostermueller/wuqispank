package org.wuqispank.model;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

/**
 * Each IRequestWrapper is stored in two different data structures:
 * 1) one for efficient iterating
 * 2) the other for efficient keyed access.
 * 
 * Sync blocks are used to make sure that both data structures remain consistent with each other.
 *  
 * @author erikostermueller
 *
 */
public class InMemoryRequstRepo implements IRequestRepository {
	private Object m_sync = new Object();
	ConcurrentLinkedDeque<IRequestWrapper> m_requestQueue =  new ConcurrentLinkedDeque<IRequestWrapper>();
	private ConcurrentMap<String, IRequestWrapper> m_hashMap = new ConcurrentHashMap<String, IRequestWrapper>();

	@Override
	public void add(IRequestWrapper val) {

		synchronized (m_sync) {
			m_requestQueue.add(val);
			m_hashMap.put(val.getUniqueId(), val);
		}
	}

	@Override
	public IRequestWrapper get(String key) {
		return m_hashMap.get(key);
	}

	/**
	 * @NAIVE_ASSUMPTION_1
	 */
	@Override
	public Iterator<IRequestWrapper> getIterator() {
		return m_requestQueue.iterator();
	}

	@Override
	public void remove(String key) {
		synchronized (m_sync) {
			IRequestWrapper rw = get(key);
			if (rw!=null) {
				m_requestQueue.remove(rw);
				m_hashMap.remove(key);
			}
		}
	}

	@Override
	public int size() {
		return m_hashMap.size();
	}
}
