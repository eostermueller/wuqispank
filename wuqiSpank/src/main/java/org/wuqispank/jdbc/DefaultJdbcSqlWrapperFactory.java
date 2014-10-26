package org.wuqispank.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEvent.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ISqlWrapperFactory;
import org.wuqispank.model.IStackTrace;
import org.wuqispank.web.EventCollector;

/** 
 * This class forms a dividing line between InTrace and Wuqispank
 * where ITraceEvents are translated into ISqlWrapper classes.
 * 
 * Why?
 * 
 * 1) Minimize wuqispank dependencies on intrace.
 * 2) Don\"t want to persist/export event details (like class names) that get outdated as JDBC drivers mature over time.
 *    To avoid persisting such details, they are purged from the object graphs in this and other ISqlWrapperFactory implementors.
 * 
 * @author erikostermueller
 *
 */
public class DefaultJdbcSqlWrapperFactory implements ISqlWrapperFactory {
	static Logger LOG = LoggerFactory.getLogger(DefaultJdbcSqlWrapperFactory.class);
	List<ITraceEvent> m_events = new ArrayList<ITraceEvent>();

	@Override
	public int getMinNumEventsPerSql() {
		return 4;//Which 3? Entry, Arg, Return, Exit.
	}

	@Override
	public void add(ITraceEvent iTraceEvent) {
		m_events.add(iTraceEvent);
	}

	/**
	 * SQL text is taken from the first ARG event in the given IRequestWrapper.
	 * The following are not currently supported:
	 * <ul>
	 * 			<li><a href="http://docs.oracle.com/javase/7/docs/api/java/sql/Connection.html#prepareCall(java.lang.String)">Connection#prepareCall()</a>.  There are a few overloaded versions of this -- none of them are supported.</li>
     *          <li><a href="http://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html#executeBatch()">java.sql.Statement#executeBatch()</a></li>
	 * </ul>
	 * The following methods are all supported.
	 * <ul>
	 *      <li>java.sql.Connection#prepareStatement(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#execute(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#executeQuery(...).  All overloaded versions of this method are supported.</li>
	 *      <li>java.sql.Statement#executeUpdate(...).  All overloaded versions of this method are supported.</li>
	 * </ul>
	 */
	public ISqlWrapper createSqlWrapper(IRequestWrapper rqWrap) throws WuqispankException {
		if (getList().size() < this.getMinNumEventsPerSql() ) {
			LOG.warn(DefaultFactory.RESEARCH_EYE_CATCHER + "Expected to find at least [" + this.getMinNumEventsPerSql() + "] events for SQL but only found [" + getList().size() + "] to make up a sql statement.");
			int i = 0;
			for(ITraceEvent event : getList()) {
				LOG.warn(DefaultFactory.RESEARCH_EYE_CATCHER + "Event [" + i++ + "] Text: [" + event.getRawEventData() + "]"); 
			}
		}
		
		ISqlWrapper dsw = rqWrap.createBlankSqlWrapper();

		ITraceEvent entryEvent = getList().get(0);
		if (entryEvent.getEventType() != EventType.ENTRY) 
			throw new WuqispankException("Error!  Was expecting an entry event but instead got [" + entryEvent.getRawEventData() + "]");
		
		ITraceEvent sqlEvent = getList().get(1);
		if (sqlEvent.getEventType() != EventType.ARG) 
			throw new WuqispankException("Error!  Was expecting the first ARG event but instead got [" + sqlEvent.getRawEventData() + "]");

		ITraceEvent exitEvent = getList().get(getList().size()-1);
		if (exitEvent==null || EventType.EXIT != exitEvent.getEventType()) {
			String raw = "Last event was null";
			if (exitEvent!=null)  {
				raw = exitEvent.getRawEventData();
			}
			throw new WuqispankException("Error!  Was expecting an EXIT.  Total event count[" + getList().size() + "] Raw event [" + raw + "] Full Event dump for this SQL [" + getList().toString() +  "]");
		}
		
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
