package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.headlessintrace.client.request.IRequest;
import org.junit.Before;
import org.junit.Test;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.InMemoryRequstRepo;

public class RequestRepositoryTest {
	private static final String SIMPLE_KEY_1 = "a";
	private static final String SIMPLE_KEY_2 = "b";
	private static final String SIMPLE_KEY_3 = "c";

	@Test
	public void canAddRequestToInMemoryRepo_trivial() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo(16384,256);
		
		repo.add( createRequest(SIMPLE_KEY_1) ); 
		
		IRequestWrapper rq = repo.get(SIMPLE_KEY_1);
		assertNotNull("Just added a request wrapper to in-memory repo, but received null trying to retrieve it.", rq);
		
		assertEquals("Just added a request wrapper to in-memory repo, but can't find it.", SIMPLE_KEY_1, rq.getUniqueId());
		
		
	}
	@Test
	public void canRemoveRequestFromInMemoryRepo_trivial() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo(16384,256);
		
		repo.add( createRequest(SIMPLE_KEY_1) ); 
		
		IRequestWrapper rq = repo.get(SIMPLE_KEY_1);
		assertNotNull("Just added a request wrapper to in-memory repo, but received null trying to retrieve it.", rq);
		
		assertEquals("Just added a request wrapper to in-memory repo, but can't find it.", SIMPLE_KEY_1, rq.getUniqueId());
		int count = repo.size();
		repo.remove(SIMPLE_KEY_1);
		assertEquals("Just deleted a RequestWrapper from in-memory repo, but the count wasn't decremented by 1.", count-1, repo.size());
		
		IRequestWrapper removed = repo.get(SIMPLE_KEY_1);
		
		assertNull("Just deleted a RequestWrapper from in-memory repo, but was still able to retrieve the object.", removed);
		
		
		
	}
	
	@Test public void canIterateInOrderOverMultipleRequestWrappers() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo(16384,256);
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
	@Test
	public void canCountSql() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo(16384,256);
		
		assertEquals("Num sql should have been zero since nothing has happened yet",0,repo.getSqlCount());
		assertEquals("Num requests should have been zero since nothing has happened yet",0,repo.size() );
		
		IRequestWrapper rq = RequestRepositoryTest.createRequestWithSql("a", 5);
		repo.add(rq);
		assertEquals("Just added a fixed number of sql statements and can't retrieve right amount",5,repo.getSqlCount());
		assertEquals("Just added a single request and request count is wrong",1,repo.size() );
	}
	@Test
	public void canCountSqlEvenWhenSomeAreRemoved() throws WuqispankException {
		IRequestRepository repo = new InMemoryRequstRepo(2,1);
		
		assertEquals("Num sql should have been zero since nothing has happened yet",0,repo.getSqlCount());
		assertEquals("Num requests should have been zero since nothing has happened yet",0,repo.size() );
		
		IRequestWrapper rq_a = RequestRepositoryTest.createRequestWithSql("a", 7);
		IRequestWrapper rq_b = RequestRepositoryTest.createRequestWithSql("b", 3);
		IRequestWrapper rq_c = RequestRepositoryTest.createRequestWithSql("c", 1);
		repo.add(rq_a);
		repo.add(rq_b);
		assertEquals("sql count invalid",10,repo.getSqlCount());
		repo.add(rq_c);
		assertEquals("sql count invalid",4,repo.getSqlCount());
		
	}
	public static IRequestWrapper createRequest(String key) throws WuqispankException {
		IRequest rq = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		rq.setUniqueId(key);
		
		IRequestWrapper rqWrap = org.wuqispank.DefaultFactory.getFactory().getRequestWrapper();
		rqWrap.setRequest(rq);
		return rqWrap;
		
	}
	public static IRequestWrapper createRequestWithSql(String key, int numSql) throws WuqispankException {
		IRequestWrapper rq = createRequest(key);
		for(int i = 0; i < numSql; i++) {
			ISqlWrapper sqlWrapper = rq.createBlankSqlWrapper();
			sqlWrapper.setSqlText("select foo from bar");
			rq.addSqlWrapper(sqlWrapper);
		}
		
		return rq;
	}

}
