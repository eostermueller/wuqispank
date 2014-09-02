package org.wuqispank.web;

import java.util.List;

import org.apache.wicket.util.io.IClusterable;
import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.request.IRequest;

public class WicketRequest implements IRequest, IClusterable {

	private IRequest m_request;

	public List<ITraceEvent> getEvents() {
		return m_request.getEvents();
	}

	public String getThreadId() {
		return m_request.getThreadId();
	}

	public String getUniqueId() {
		return m_request.getUniqueId();
	}

	public void setEvents(List<ITraceEvent> arg0) {
		m_request.setEvents(arg0);
	}

	public void setThreadId(String arg0) {
		m_request.setThreadId(arg0);
	}

	public void setUniqueId(String arg0) {
		m_request.setUniqueId(arg0);
	}

	public WicketRequest(IRequest request) {
		m_request = request;
	}

	@Override
	public String getUrl() {
		return m_request.getUrl();
	}

	@Override
	public void setUrl(String val) {
		m_request.setUrl(val);
		
	}

	@Override
	public String getHttpResponseCode() {
		return m_request.getHttpResponseCode();
	}

	@Override
	public void setHttpResponseCode(String val) {
		m_request.setHttpResponseCode(val);
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInitialized(boolean val) {
		// TODO Auto-generated method stub
		
	}
}
