package org.wuqispank.model;

import java.util.Iterator;

import org.wuqispank.WuqispankException;


/**
 * 
 * Here are some requirements for implementors of this interface
 * 1) Display an ordered list of most recent requests (nay, all requests) --> requires an iterator.
 * 2) return a single request given a key.
 * 3) Under the covers, it must be able to write code to "age off" older request.
 * @author erikostermueller
 *
 * 
 * @NAIVE_ASSUMPTION_1
 * The first implementation of this will be a thread-safe in-memory impl.
 * Many months later, an implementation will be coded that persists request data to disk.
 * "getIterator()" naively assumes that there is enough memory available to get all requests ever stored.
 * "getIterator()" is good enough for the in-memory implementation and will obviously need to
 * be re-factored when we back the repository with persistence.
 * 
 * The in-memory implementation will need to behave like a "circular buffer",
 * where older requests are deleted.  The methods in this interface don\"t provide
 * any of that functionality.
 * 
 * The implementors of this will need some load testing to insure thread safety.
 * @author erikostermueller
 *
 */
public interface IRequestRepository {
	void add(IRequestWrapper val) throws WuqispankException;
	IRequestWrapper get(String key);
	Iterator<IRequestWrapper> getIterator();
	void remove(String key);
	int size();
	int getSqlCount();
	int getServletRequestCapacity();
	int getPercentageFull();
	long getFreeMemoryInMb();
	int getCountOfDiscardedRequests();
	int getCountOfTotalRequests();
}
