package org.wuqispank.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.headlessintrace.client.model.ITraceEvent.EventType;
import org.headlessintrace.client.request.IRequest;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.jdbc.DefaultJdbcSqlWrapperFactory;

/**
 * The super class summarizes SQL metrics for all statements in this request.
 * @author erikostermueller
 *
 */
public class DefaultRequestWrapper implements IRequestWrapper {
	private ISqlStatsObserver m_sqlStats = null;
	private int m_sequence = 0;
	@Override
	public ISqlStatsObserver getSqlStats() {
		return m_sqlStats;
	}
	public void setSqlStats(ISqlStatsObserver sqlStats) {
		this.m_sqlStats = sqlStats;
	}
	public DefaultRequestWrapper() {
		m_sqlStatements = new ArrayList<ISqlWrapper>();
		
		setObservationMgr( DefaultFactory.getFactory().getObservationMgr() );
		setSqlStats( DefaultFactory.getFactory().getSqlStatsCounter() );

		getObservationMgr().registerNewTableListener(getSqlStats());
		getObservationMgr().registerNewJoinListener(getSqlStats());
		getObservationMgr().registerNewSqlListener(getSqlStats());
	}
	private IRequest m_request = null;
	private ISqlModel m_sqlModel = null;
	private long m_monitoredTimestamp = ISqlModel.NOT_INITIALIZED;
	
	public long getMonitoredTimestamp() {
		return m_monitoredTimestamp;
	}
	public void setMonitoredTimestamp(long monitoredTimestamp) {
		this.m_monitoredTimestamp = monitoredTimestamp;
	}

	private IModelObservationMgr m_modelObservationMgr = null;
	@Override
	public IModelObservationMgr getObservationMgr() {
		return m_modelObservationMgr;
	}
	@Override
	public void setObservationMgr(IModelObservationMgr modelObservationMgr) {
		this.m_modelObservationMgr = modelObservationMgr;
	}

	private List<ISqlWrapper> m_sqlStatements = null;
	private static final String TINY_ID_DELIM = "-";
	private static final int INDEX_ERROR = -3;
	private int aggregateTableCount = ISqlModel.NOT_INITIALIZED;
	@Override
	public String getTinyId() {
		return getRequest().getUniqueId().substring(0,3) + TINY_ID_DELIM + getRequest().getUniqueId().substring(getRequest().getUniqueId().length()-4); 
	}
	@Override
	public String getUniqueId() {
		return getRequest().getUniqueId();
	}
	@Override
	public int getTableCount() {
		return this.getSqlStats().getOrderedTables().size();
	}
	@Override
	public void setTableCount(int aggregateTableCount) {
		this.aggregateTableCount = aggregateTableCount;
	}
	@Override
	public int getColumnCount() {
		return aggregateColumnCount;
	}
	@Override
	public void setColumnCount(int aggregateColumnCount) {
		this.aggregateColumnCount = aggregateColumnCount;
	}
	@Override
	public void addSqlWrapper(ISqlWrapper val) throws WuqispankException {
		val.setSequence(m_sequence++);

		if (getObservationMgr()==null)
			throw new WuqispankException("found null sql observation mgr");

		getSql().add(val);
		getObservationMgr().addNewSql();
		
	}
	@Override
	public int getSqlStatementCount() {
		return this.getSqlCount();
	}

	private int aggregateColumnCount = ISqlModel.NOT_INITIALIZED;
	
	public List<ISqlWrapper> getSql() {
		return m_sqlStatements;
	}

	@Override
	public ISqlWrapper createBlankSqlWrapper() throws WuqispankException {
		if (this.getObservationMgr()==null)
			throw new WuqispankException("CODE ERROR.  Improper initialization, DefaultRequestWrapper must have a non-null ModelObservationManager.");
		ISqlWrapper sqlWrapper = DefaultFactory.getFactory().getSqlWrapper();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		model.setObservationMgr(this.getObservationMgr());
		sqlWrapper.setSqlModel( model );
		return sqlWrapper;
		
	}
	@Override
	public void setRequest(IRequest request) throws WuqispankException {
		loadEvents(request.getEvents());
		this.m_request = request;
		calculateStats();
	}

	/**
	 * Expecting nice, clean lists of events, each with matching ENTRY and EXIT.
	 * There should not be any \"nested\" events.  ContiguousEventFilter should have gotten rid of all of those.
	 * 
	 * @param allEvents
	 * @throws WuqispankException
	 */
	private void loadEvents(List<ITraceEvent> allEvents) throws WuqispankException {
		
		int firstEntry = INDEX_ERROR;
		int firstExit = INDEX_ERROR;
		for(int i = 0; i < allEvents.size(); /* increment below */) {
			
			firstEntry = getIndexOfNext(EventType.ENTRY,allEvents,i);
			if (firstEntry!=INDEX_ERROR) {
				firstExit = getIndexOfNext(EventType.EXIT,allEvents,firstEntry, allEvents.get(firstEntry).getMethodName());
				if (firstExit!=INDEX_ERROR) {
					ISqlWrapperFactory sqlWrapperFactory = new DefaultJdbcSqlWrapperFactory();
					for(int j = firstEntry; j <=firstExit; j++) {
						sqlWrapperFactory.add(allEvents.get(j));
					}
					addSqlWrapper(sqlWrapperFactory.createSqlWrapper( this ) );
					i = firstExit+1;
				} else {
					throw new WuqispankException("Unmatched Event Error.  Can\"t find EXIT event. firstEntry [" + firstEntry + "] total event count [" + allEvents.size() + "] all event dump [" + allEvents.toString() + "]");
				}
			} else {
				throw new WuqispankException("Unmatched Event Error.  Can\"t find ENTRY event. firstExit [" + firstExit + "] total event count [" + allEvents.size() + "] all event dump [" + allEvents.toString() + "]");
			}
		}	
	}
	private int getIndexOfNext(EventType eventType, List<ITraceEvent> allEvents,
			int firstEntry, String methodName) {
		int rc = INDEX_ERROR;
		for(int i = firstEntry; i < allEvents.size();i++) {
			if (allEvents.get(i).getEventType()==eventType && allEvents.get(i).getMethodName().equals(methodName))  {
				rc = i;
				break;
			}
		}
		return rc;
	}
	private int getIndexOfNext(EventType eventType, List<ITraceEvent> events, int current) {
		int rc = INDEX_ERROR;
		for(int i = current; i < events.size();i++) {
			if (events.get(i).getEventType()==eventType ){ 
				rc = i;
				break;
			}
		}
		return rc;
	}
	private ISqlWrapperFactory getSqlWrapperFactory(List<ITraceEvent> allEvents, int i) {
		ISqlWrapperFactory rc = null;
		if (allEvents.get(i).getEventType().equals(EventType.ENTRY)) {
			rc = new DefaultJdbcSqlWrapperFactory();
		}
		return rc;
	}
//	private ISqlWrapper createSqlWrapper(
//				ITraceEvent entryEvent, 
//				ITraceEvent argEvent,
//				ITraceEvent exitEvent) throws WuqispankException {
//		ISqlWrapper sqlWrapper = DefaultFactory.getFactory().getSqlWrapper();
//		sqlWrapper.setEntryEvent(entryEvent);
//		sqlWrapper.setSqlEvent(argEvent);
//		sqlWrapper.setExitEvent(exitEvent);
//		List errors = sqlWrapper.validate();
//		if (errors.size() > 0) {
//			throw new WuqispankException(  errors.toString() );
//		}
//		
//		return sqlWrapper;
//		
//	}
	
	private void calculateStats() {
		int colCount = 0;
		int sqlCount = 0;
		int tableCount = 0;
		for( ISqlWrapper sqlWrapper : getSql() ) {
			colCount += sqlWrapper.getSqlModel().getColumnCount();
			sqlCount ++;
			tableCount += sqlWrapper.getSqlModel().getTableCount();
		}
		this.setColumnCount(colCount);
		this.setTableCount(tableCount);
	}
	@Override
	public int getSqlCount() {
		return getSql().size();
	}
	@Override
	public IRequest getRequest() {
		return m_request;
	}

	@Override
	public ISqlModel getSqlModel() {
		return m_sqlModel;
	}

	@Override
	public void setSqlMetrics(ISqlModel val) {
		m_sqlModel = val;
	}
	@Override
	public void setUniqueId(String val) {
		getRequest().setUniqueId(val);
	}

//	@Override
//	public int getSqlStatementCount() {
//		//return this.getRequest().getEvents().size();
//		return this.getSqlCount();
//	}
	

}
