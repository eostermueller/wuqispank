package org.wuqispank.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;

public class DefaultSqlWrapper implements ISqlWrapper, java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(DefaultSqlWrapper.class);

	private static final long UNINIT = -1;
	
//	private ITraceEvent m_sqlEvent = null;
//	private ITraceEvent m_entryEvent = null;
//	private ITraceEvent m_exitEvent = null;
	private ISqlModel m_sqlModel = null;
	private int m_sequence =  -1;
	private IStackTrace m_stackTrace = null;
	
	private long m_agentEntryTimeMillis = UNINIT;
	private long m_agentExitTimeMillis = UNINIT;
	private long m_lousyDateTimeMillis = UNINIT;

	private String m_sqlText;

	@Override
	public int getSequence() {
		return m_sequence;
	}

	@Override
	public void setSequence(int sequence) {
		this.m_sequence = sequence;
	}


//	public void setSqlEvent(ITraceEvent sqlEvent) throws WuqispankException {
//		if (sqlEvent==null) {
//			throw new WuqispankException("Received a null sql event");
//		} 
//		
//		this.m_sqlEvent = sqlEvent;
//		if (sqlEvent.getEventType()!=ITraceEvent.EventType.ARG) {
//			throw new WuqispankException("Current implementation requires the SQL to be in the ARG trace event.  Instead, found [" + sqlEvent.toString() + "]");
//		} else {
//			setSqlText(sqlEvent.getValue());
//		}
//		
//	}
	@Override
	public void setSqlText(String val) {
		m_sqlText = val;
		setSqlModel(DefaultFactory.getFactory().getSqlModel());
		getSqlModel().setColumnCount(17);

		ISqlParser parser = DefaultFactory.getFactory().getSqlParser();
		try {
			parser.setSqlModel(getSqlModel());
			parser.parse(val);
		} catch (SqlParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getSqlText() {
		return m_sqlText;
	}

	@Override
	public ISqlModel getSqlModel() {
		return m_sqlModel;
	}

	@Override
	public void setSqlModel(ISqlModel val) {
		m_sqlModel = val;
	}
	public IStackTrace getStackTrace() {
		return m_stackTrace;
	}

	@Override
	public long getAgentEntryTimeMillis() {
		return m_agentEntryTimeMillis;
	}

	@Override
	public void setAgentEntryTimeMillis(long val) {
		m_agentEntryTimeMillis = val;
	}

	@Override
	public long getAgentExitTimeMillis() {
		return m_agentExitTimeMillis;
	}

	@Override
	public void setAgentExitTimeMillis(long val) {
		m_agentExitTimeMillis = val;
	}

	@Override
	public void setStackTrace(IStackTrace val) {
		m_stackTrace = val;
	}

	@Override
	public void setLousyDateTimeMillis(long val) {
		m_lousyDateTimeMillis = val;
		
	}

	@Override
	public long getLousyDateTimeMillis() {
		return m_lousyDateTimeMillis;
	}
	

}
