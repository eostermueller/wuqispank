package org.wuqispank;

import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEvent.EventType;
import org.headlessintrace.client.request.ICompletedRequestCallback;
import org.headlessintrace.client.request.IRequest;

public class UrlExtractor implements ICompletedRequestCallback {

	ICompletedRequestCallback m_callback = null;
	public UrlExtractor() {
		this(null);
	}
	public UrlExtractor(ICompletedRequestCallback val) {
		m_callback = val;
	}
	@Override
	public void requestCompleted(IRequest events) {
		extractUrl(events);
		if (m_callback!=null)
		m_callback.requestCompleted(events);
	}
	private void extractUrl(IRequest events) {
		for(ITraceEvent e : events.getEvents()) {
			if ("HttpServlet".equals(e.getClassName() )
				&& "javax.servlet.http".equals(e.getPackageName() ) ) {
				//System.out.println("HttpServlet raw [" + e.getRawEventData() + "]");
				
				events.getEvents().remove(e);
				
				if (e.getEventType().equals(EventType.ARG) ) {
					
					if (e.getValue().contains("POST")
							|| e.getValue().contains("GET"))
						events.setUrl(e.getRawEventData());
					else if (e.getValue().contains("HTTP")) 
						events.setUrl(e.getValue());
				} 
			}
		}
		
	}

}
