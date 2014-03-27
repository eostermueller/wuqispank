package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.intrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.InMemoryRequstRepo;

public class TestRequestRepository {
	private static final String SIMPLE_KEY_1 = "a";
	private static final String SIMPLE_KEY_2 = "b";
	private static final String SIMPLE_KEY_3 = "c";

	@Test
	public void canAddRequestToInMemoryRepo_trivial() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo();
		
		repo.add( createRequest(SIMPLE_KEY_1) ); 
		
		IRequestWrapper rq = repo.get(SIMPLE_KEY_1);
		assertNotNull("Just added a request wrapper to in-memory repo, but received null trying to retrieve it.", rq);
		
		assertEquals("Just added a request wrapper to in-memory repo, but can't find it.", SIMPLE_KEY_1, rq.getUniqueId());
		
		
	}
	@Test
	public void canRemoveRequestFromInMemoryRepo_trivial() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo();
		
		repo.add( createRequest(SIMPLE_KEY_1) ); 
		
		IRequestWrapper rq = repo.get(SIMPLE_KEY_1);
		assertNotNull("Just added a request wrapper to in-memory repo, but received null trying to retrieve it.", rq);
		
		assertEquals("Just added a request wrapper to in-memory repo, but can't find it.", SIMPLE_KEY_1, rq.getUniqueId());
		
		repo.remove(SIMPLE_KEY_1);
		
		IRequestWrapper removed = repo.get(SIMPLE_KEY_1);
		
		assertNull("Just deleted a RequestWrapper from in-memory repo, but was still able to retrieve the object.", removed);
		
		
		
	}
	
	@Test public void canIterateInOrderOverMultipleRequestWrappers() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo();
		repo.add( createRequest(SIMPLE_KEY_1) ); 
		repo.add( createRequest(SIMPLE_KEY_2) ); 
		repo.add( createRequest(SIMPLE_KEY_3) );
		
		Iterator<IRequestWrapper> itr = repo.getIterator();
		
		int itrCount = 0;
		while(itr.hasNext()) {
			
			IRequestWrapper rw = itr.next();
			switch(++itrCount) {
			case 1:
				assertEquals("RequestWrapper iteration is not in expected order", SIMPLE_KEY_1,rw.getUniqueId());
				break;
			case 2:
				assertEquals("RequestWrapper iteration is not in expected order", SIMPLE_KEY_2,rw.getUniqueId());
				break;
			case 3:
				assertEquals("RequestWrapper iteration is not in expected order", SIMPLE_KEY_3,rw.getUniqueId());
				break;
			default:
				fail("Bug in this test that iterates over request wrappers");
			}
		}
		assertEquals("Did not find correct number of request wrappers when iterating", 3, itrCount);
		
	}
	public static IRequestWrapper createRequest(String key) throws WuqispankException {
		IRequest rq = org.intrace.client.DefaultFactory.getFactory().getRequest();
		rq.setUniqueId(key);
		
		IRequestWrapper rqWrap = org.wuqispank.DefaultFactory.getFactory().getRequestWrapper();
		rqWrap.setRequest(rq);
		return rqWrap;
		
	}

}
