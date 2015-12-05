package org.wuqispank.model;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.wuqispank.WuqispankException;
import org.wuqispank.web.WuqispankApp;

/**
 * Each IRequestWrapper is stored in two different data structures:
 * 1) one for efficient iterating
 * 2) the other for efficient keyed access.
 * 
 *  
 * @author erikostermueller
 *
 */
public class InMemoryRequstRepo implements IRequestRepository {
	private int numRequestsToDeleteAtOnce = 0;
	private AtomicInteger sqlCount = new AtomicInteger();
	private AtomicInteger countOfDiscardedRequests = new AtomicInteger();
	private AtomicInteger countOfTotalRequests = new AtomicInteger();
	private int servletRqCapacity = 0;
	public void setNumRequestsToDeleteAtOnce(int val) {
		this.numRequestsToDeleteAtOnce = val;
	}
	@Override
	public int getCountOfDiscardedRequests() {
		return this.countOfDiscardedRequests.get();
	}
	@Override
	public int getSqlCount() {
		return this.sqlCount.get();
	}
	
	public int getNumRequestsToDeleteAtOnce() {
		return this.numRequestsToDeleteAtOnce;
	}
	@Override
	public int getServletRequestCapacity() {
		return this.servletRqCapacity;
	}
	public InMemoryRequstRepo(int servletRqCapacity, int val) {
		this.servletRqCapacity = servletRqCapacity;
		this.m_requestQueue = new LinkedBlockingQueue<IRequestWrapper>(this.servletRqCapacity);
		this.setNumRequestsToDeleteAtOnce(val);
	}
	LinkedBlockingQueue<IRequestWrapper> m_requestQueue =  null;
	private ConcurrentMap<String, IRequestWrapper> m_hashMap = new ConcurrentHashMap<String, IRequestWrapper>();
	private static final long ONE_MEGABYTE = 1048576;

	private void removeOldestOne() {
		IRequestWrapper rw = this.m_requestQueue.remove();
		if (rw!=null) {
			this.countOfDiscardedRequests.incrementAndGet();
			/**
			 * 
			 */
			for(int i = 0; i < rw.getSqlCount();i++)
				this.sqlCount.decrementAndGet();
			
			this.m_hashMap.remove(rw.getUniqueId());
		}
	}
	@Override
	public void add(IRequestWrapper val) throws WuqispankException {

		if (this.m_requestQueue.remainingCapacity() > 0) {
			this.addWithoutConditions(val);
		} else {
			for(int i = 0; i < this.numRequestsToDeleteAtOnce; i++)
				this.removeOldestOne();
			this.addWithoutConditions(val);
		}
			
	}
	private void addWithoutConditions(IRequestWrapper val) throws WuqispankException {
		boolean rc = this.m_requestQueue.offer(val);
		if (rc ==true) {
			m_hashMap.put(val.getUniqueId(), val);
			this.countOfTotalRequests.incrementAndGet();
			this.sqlCount.addAndGet(val.getSqlCount());
		} else
			throw new WuqispankException();
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
	public int getCount() {
		return this.m_hashMap.size();
	}

	@Override
	public void remove(String key) {
			IRequestWrapper rw = get(key);
			if (rw!=null) {
				m_requestQueue.remove(rw);
				m_hashMap.remove(key);
			}
//			for(int i = 0; i < rw.getSqlCount();i++)
//				this.sqlCount.decrementAndGet();
		}
	
	@Override
	public int getPercentageFull() {
		int pct = (int)
					(
							(double)WuqispankApp.getRepo().size() / 
							(double)WuqispankApp.getRepo().getServletRequestCapacity()
					);
		return pct;
	}

	/**
	 * This will always be equal or larger than this.size(),
	 * Unlike the count returned by this method,  this.size() reflects requests that have been discarded
	 * to avoid reaching the servletRqCapacity set in the ctor. 
	 * @return
	 */
	@Override
	public int getCountOfTotalRequests() {
		return this.countOfTotalRequests.get();
	}
	@Override
	public int size() {
		return m_hashMap.size();
	}
	/**
	 * Stolen from here:
	 * http://stackoverflow.com/questions/12807797/java-get-available-memory
	 * @return
	 */
	@Override
	public long getFreeMemoryInMb() {
		long allocatedMemory      = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
		long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;
		
		long mbFree = 0;
		if (presumableFreeMemory <= ONE_MEGABYTE) {
			mbFree = 0;
		} else {
			mbFree = (long)((double)presumableFreeMemory / (double)ONE_MEGABYTE );
		}
		
		return mbFree;
		
	}
	
}
