package org.wuqispank.test.level1.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.DefaultRequestWrapper;
import org.wuqispank.model.IRequestListener;
import org.wuqispank.model.IRequestManager;
import org.wuqispank.model.IRequestWrapper;

public class RequestListenerTest {

	List<IRequestWrapper> list1 = new ArrayList<IRequestWrapper>();
	List<IRequestWrapper> list2 = new ArrayList<IRequestWrapper>();
	
	/**
	 * IRequestWrapper instances are distributed to both
	 * a) wuqiSpank, to display "SQL Sequence Diagrams"
	 * b) to be written to InfluxDB, so that SQL metrics can be displayed in grafana.
	 * 
	 * This test demonstrates the API the sends a single IRequestWrapper to two different places.
	 * @throws WuqispankException 
	 */
	@Test
	public void canDistributeRequestsToMultipleListerners() throws WuqispankException {
		
		IRequestManager requestManager = DefaultFactory.getFactory().getRequestManager();
		
		IRequestListener requestListener1 = new IRequestListener() {
			public void add(IRequestWrapper rw) {
				list1.add(rw);
			}
		};
		
		IRequestListener requestListener2 = new IRequestListener() {
			public void add(IRequestWrapper rw) {
				list2.add(rw);
			}
		};
		requestManager.registerListener(requestListener1);
		requestManager.registerListener(requestListener2);
		
		IRequestWrapper rw = new DefaultRequestWrapper();
		requestManager.add(rw);
		
		assertEquals(
				"Should have found a single IRequestWrapper when added using DefaultRequestManager",
				1,
				list1.size() );
		assertEquals(
				"Should have found a specific IRequestWrapper instnace when added using DefaultRequestManager",
				rw,
				list1.get(0) );

		assertEquals(
				"Should have found a single IRequestWrapper when added using DefaultRequestManager",
				1,
				list2.size() );
		assertEquals(
				"Should have found a specific IRequestWrapper instnace when added using DefaultRequestManager",
				rw,
				list2.get(0) );
		
		
	}

}
