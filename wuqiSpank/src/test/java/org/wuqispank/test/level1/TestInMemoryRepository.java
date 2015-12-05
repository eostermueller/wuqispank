package org.wuqispank.test.level1;

import static org.junit.Assert.*;

import org.headlessintrace.client.request.IRequest;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.InMemoryRequstRepo;

public class TestInMemoryRepository {

	/**
	 * This is a real important test case.
	 * It shows that we can put a limit to the number of requests
	 * stored in the in-memory repo.
	 * 
	 * Without this, wuqiSpank would run out of memory real quick.
	 * @throws WuqispankException
	 */
	@Test
	public void canLimitCountOfRequests() throws WuqispankException {
		InMemoryRequstRepo repo = new InMemoryRequstRepo(6,3);
		
		IRequestWrapper r1 = DefaultFactory.getFactory().getRequestWrapper();
		r1.setUniqueId("1");
		IRequestWrapper r2 = DefaultFactory.getFactory().getRequestWrapper();
		r2.setUniqueId("2");
		IRequestWrapper r3 = DefaultFactory.getFactory().getRequestWrapper();
		r3.setUniqueId("3");
		IRequestWrapper r4 = DefaultFactory.getFactory().getRequestWrapper();
		r4.setUniqueId("4");
		IRequestWrapper r5 = DefaultFactory.getFactory().getRequestWrapper();
		r5.setUniqueId("5");
		IRequestWrapper r6 = DefaultFactory.getFactory().getRequestWrapper();
		r6.setUniqueId("6");
		IRequestWrapper r7 = DefaultFactory.getFactory().getRequestWrapper();
		r7.setUniqueId("7");
		
		repo.add( r1 );
		assertEquals("Expected to see the 1 IRequestWrappers that we put in.", 1, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("1") );
		
		repo.add( r2 );
		assertEquals("Expected to see the 2 IRequestWrappers that we put in.", 2, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("2") );
		
		repo.add( r3 );
		assertEquals("Expected to see the 3 IRequestWrappers that we put in.", 3, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("3") );
		
		repo.add( r4 );
		assertEquals("Expected to see the 4 IRequestWrappers that we put in.", 4, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("4") );
		
		repo.add( r5 );
		assertEquals("Expected to see the 5 IRequestWrappers that we put in.", 5, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("5") );
		
		repo.add( r6 );
		assertEquals("Expected to see the 6 IRequestWrappers that we put in.", 6, repo.getCount() );
		assertNotNull("COuldn't find the request we just put into the in-memory repo", repo.get("6") );
		
		/**
		 * The meaning of "InMemoryRequstRepo(6,3)"
		 * 6 is the limit that will be kept.  To make room for the subsequent request,
		 * the 3 oldest ones should be removed.
		 */
		repo.add( r7 );
		assertNotNull( repo.get("7") );
		assertEquals("3 should be taken out for the 6+1 element.  (6+1)-3=4",4,repo.getCount() );
		
		assertNull("this older ele should have been removed to make way for a new one", repo.get("1") );
		assertNull("this older ele should have been removed to make way for a new one", repo.get("2") );
		assertNull("this older ele should have been removed to make way for a new one", repo.get("3") );
		
		assertNotNull("this newer ele should have been remained even though older ones were removed to make way for a new one", repo.get("4") );
		assertNotNull("this newer ele should have been remained even though older ones were removed to make way for a new one", repo.get("5") );
		assertNotNull("this newer ele should have been remained even though older ones were removed to make way for a new one", repo.get("6") );
		assertNotNull("this newer ele should have been remained even though older ones were removed to make way for a new one", repo.get("7") );
	}

}
