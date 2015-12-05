package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;


import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.request.IRequest;
import org.headlessintrace.client.request.IRequestSeparator;
//import org.headlessintrace.client.test.TestUtil;
//import org.headlessintrace.client.test.level1.request.TestMultiThreadedRequestSeparator;
import org.junit.Test;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.InMemoryRequstRepo;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadedRequestRepositoryTest {
	public static AtomicInteger atomicAddCount = new AtomicInteger();
	public static AtomicInteger atomicGetCount = new AtomicInteger();
	public static AtomicInteger atomicRemoveCount = new AtomicInteger();
	public static AtomicInteger atomicExceptionCount = new AtomicInteger();
	public static AtomicInteger atomicIterationCount = new AtomicInteger();

	@Test
	public void test() {
		IRequestRepository repo = new InMemoryRequstRepo(16384,256);
		
		//int numThreads = 51;
		//int iterations = 1013;		
		int numThreads = 20;
		int iterations = 400;		
	    int batchSize = 50;
	     CountDownLatch startSignal = new CountDownLatch(1);
	     CountDownLatch doneSignal = new CountDownLatch(numThreads);
	     
		for (int i = 0; i < numThreads; i++) {
			new Thread(new RequestWrapperGenerator(startSignal, doneSignal, repo,iterations, batchSize, String.valueOf(Character.toChars(65+i)))).start();
		}
	     startSignal.countDown();      // let the games begin!
	     try {
	    	 //System.out.println("Waiting to finish");
			doneSignal.await();
			//System.out.println("Finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           // wait for all to finish
	     assertEquals("didn't find expected number of exceptions", 0,atomicExceptionCount.get());
	     
	     assertEquals("didn't find expected number of adds", numThreads*iterations*batchSize,atomicAddCount.get());
	     assertEquals("didn't find expected number of gets", numThreads*iterations*batchSize,atomicGetCount.get());
	     assertEquals("didn't find expected number of removes", numThreads*iterations*batchSize,atomicRemoveCount.get());
	     
	     //note that batchSize is not part of this next one:
	     assertEquals("didn't find expected number of iterations", numThreads*iterations,atomicIterationCount.get());
	     
	}

}
class RequestWrapperGenerator implements Runnable {
   private CountDownLatch m_startSignal = null;
   private CountDownLatch m_doneSignal = null;
	private int m_iterations = -1;
	private IRequestSeparator m_requestSeparator = null;
	PrintStream m_out = null;
	private IRequestRepository m_repo = null;
	private int m_batchSize = 0;
	private String m_keyPrefix = null;
	public RequestWrapperGenerator(CountDownLatch startSignal, CountDownLatch doneSignal, 
			IRequestRepository repo, int iterations, int batchSize, 
			String keyPrefix) {

		m_iterations = iterations;
		m_repo = repo;
		this.m_startSignal = startSignal;
		this.m_doneSignal = doneSignal;
		this.m_batchSize = batchSize;
		this.m_keyPrefix = keyPrefix;
	}
	private void createAndAdd() throws WuqispankException {
		for (int i = 0;i <m_batchSize; i++) {
			m_repo.add( createRequest(m_keyPrefix+i));
			MultiThreadedRequestRepositoryTest.atomicAddCount.getAndIncrement();
		}
	}
	private void get() throws WuqispankException {
		for (int i = 0;i <m_batchSize; i++) {
			IRequestWrapper rw = m_repo.get(m_keyPrefix+i);
			if (rw.getUniqueId().equals(m_keyPrefix+i)) {
				MultiThreadedRequestRepositoryTest.atomicGetCount.getAndIncrement();
			}
		}
	}
	
	public static IRequestWrapper createRequest(String key) throws WuqispankException {
		IRequest rq = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		rq.setUniqueId(key);
		
		IRequestWrapper rqWrap = org.wuqispank.DefaultFactory.getFactory().getRequestWrapper();
		rqWrap.setRequest(rq);
		return rqWrap;
		
	}
    /**
     * make sure the iterator finds everything we've added.
     * Yes, the iterator will (will/should/must) contain other objects from other threads,
     * but this just validates we can find objects from the current thread.
     */
	private void iterate() {
		
		Iterator<IRequestWrapper> itr = m_repo.getIterator();
		int itrCount = 0;
		while(itr.hasNext()) {
			
			IRequestWrapper rw = itr.next();
			if (rw.getUniqueId().startsWith(m_keyPrefix)) 
				itrCount++;
			
		}
		
		if (itrCount==m_batchSize) 
			MultiThreadedRequestRepositoryTest.atomicIterationCount.getAndIncrement();
		
		
	}
	
	private void remove() {
		for (int i = 0;i <m_batchSize; i++) {
			m_repo.remove( m_keyPrefix+i);
			IRequestWrapper rw = m_repo.get(m_keyPrefix+i);
			if (rw==null) 
				MultiThreadedRequestRepositoryTest.atomicRemoveCount.getAndIncrement();
		}
	}
    public void run() {
        try {
			m_startSignal.await();

			for(int i = 0; i < m_iterations; i++) {
				try {

					createAndAdd();
					//Perform the following operations
					//on the objects created by this thread
					get();
					iterate();
					remove();
					
				} catch (Exception e) {
					MultiThreadedRequestRepositoryTest.atomicExceptionCount.incrementAndGet();
					e.printStackTrace();
				}
			}
			
		    m_doneSignal.countDown();

		} catch (InterruptedException e1) {
			MultiThreadedRequestRepositoryTest.atomicExceptionCount.incrementAndGet();
			e1.printStackTrace();
		}
   	
    }

}
