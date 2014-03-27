package org.wuqispank.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEvent.EventType;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ISqlWrapperFactory;
import org.wuqispank.model.IStackTrace;

/** 
 * This class forms a dividing line between InTrace and Wuqispank
 * where ITraceEvents are translated into ISqlWrapper classes.
 * 
 * Why?
 * 
 * 1) Minimize wuqispank dependencies on intrace.
 * 2) Don't want to persist/export event details (like class names) that get outdated as JDBC drivers mature over time.
 *    To avoid persisting such details, they are purged from the object graphs in this and other ISqlWrapperFactory implementors.
 * 
 * @author erikostermueller
 *
 */
public class DefaultJdbcSqlWrapperFactory implements ISqlWrapperFactory {
	List<ITraceEvent> m_events = new ArrayList<ITraceEvent>();

	@Override
	public int getNumEventsPerSql() {
		return 3;//Which 3? Entry, Arg, Exit.
	}

	@Override
	public void add(ITraceEvent iTraceEvent) {
		m_events.add(iTraceEvent);
	}

	@Override
	public ISqlWrapper createSqlWrapper() throws WuqispankException {
		if (getList().size() != this.getNumEventsPerSql() ) {
			throw new WuqispankException("Didn't receive that number of events I was looking for.");
		}
		ISqlWrapper dsw = DefaultFactory.getFactory().getSqlWrapper();
		ITraceEvent entryEvent = getList().get(0);
		if (entryEvent.getEventType() != EventType.ENTRY) 
			throw new WuqispankException("Error!  Was expecting an entry event but instead got [" + entryEvent.getRawEventData() + "]");
		
		ITraceEvent sqlEvent = getList().get(1);
		if (sqlEvent.getEventType() != EventType.ARG) 
			throw new WuqispankException("Error!  Was expecting an ARG event but instead got [" + sqlEvent.getRawEventData() + "]");
		
		ITraceEvent exitEvent = getList().get(2);
		if (exitEvent.getEventType() != EventType.EXIT) 
			throw new WuqispankException("Error!  Was expecting an exit event but instead got [" + entryEvent.getRawEventData() + "]");
		
		dsw.setSqlText( sqlEvent.getValue() );
		dsw.setAgentEntryTimeMillis(entryEvent.getAgentTimeMillis());
		dsw.setAgentExitTimeMillis(exitEvent.getAgentTimeMillis());
		dsw.setLousyDateTimeMillis( exitEvent.getClientDateTimeMillis() );
		IStackTrace stackTrace = DefaultFactory.getFactory().getStackTrace();
		stackTrace.setStackTraceElements(exitEvent.getStackTrace());
		
		dsw.setStackTrace(stackTrace);

		if (!entryEvent.getClassName().equals(sqlEvent.getClassName())) {
			throw new WuqispankException("Entry event class [" + entryEvent.getClassName() + 
					"] does not match Arg class name [" + sqlEvent.getClassName() + "]");
		}
		
		if (!sqlEvent.getClassName().equals(exitEvent.getClassName())) {
			throw new WuqispankException("Arg event class [" + sqlEvent.getClassName() + 
					"] does not match Exit class name [" + exitEvent.getClassName() + "]");
		}
		
		return dsw;
	}

	@Override
	public List<ITraceEvent> getList() {
		return m_events;
	}

}
