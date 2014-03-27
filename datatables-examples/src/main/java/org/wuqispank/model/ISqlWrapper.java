package org.wuqispank.model;

import java.util.List;

import org.intrace.client.model.ITraceEvent;
import org.wuqispank.WuqispankException;

public interface ISqlWrapper {
	String getSqlText();

//	ITraceEvent getSqlEvent();
//	void setSqlEvent(ITraceEvent val) throws WuqispankException;
//
//	ITraceEvent getEntryEvent();
//	void setEntryEvent(ITraceEvent val) throws WuqispankException;
//	
//	ITraceEvent getExitEvent();
//	void setExitEvent(ITraceEvent val) throws WuqispankException;

	
	ISqlModel getSqlModel();
	void setSqlModel(ISqlModel val);

	void setSequence(int sequence);

	int getSequence();
	//List<String> validate();

	void setSqlText(String val);
	
	long getAgentEntryTimeMillis();
	void setAgentEntryTimeMillis(long val);
	
	long getAgentExitTimeMillis();
	void setAgentExitTimeMillis(long val);

	void setStackTrace(IStackTrace stackTrace);
	IStackTrace getStackTrace();

	void setLousyDateTimeMillis(long clientDateTimeMillis);
	long getLousyDateTimeMillis();

}
