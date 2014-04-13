package org.wuqispank.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.intrace.client.model.ITraceEvent;
import org.intrace.client.model.ITraceEvent.EventType;
import org.intrace.client.request.IRequest;
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
		return aggregateTableCount;
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

//		if (getSqlModel()==null)
//			throw new WuqispankException("found null sql model");

		if (getObservationMgr()==null)
			throw new WuqispankException("found null sql observation mgr");
		
		//val.getSqlModel().setObservationMgr( this.getObservationMgr() );

		getSql().add(val);
		getObservationMgr().addNewSql();
		
	}
	@Override
	public int getSqlStatementCount() {
		return 42;
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

	private void loadEvents(List<ITraceEvent> allEvents) throws WuqispankException {
		
		for(int i = 0; i < allEvents.size(); /* inline increment, below */) {
			
			ISqlWrapperFactory sqlWrapperFactory = getSqlWrapperFactory(allEvents,i);
			
			if (sqlWrapperFactory!=null) {
				if ( allEvents.size() >= i+ sqlWrapperFactory.getNumEventsPerSql() ) {
					for (int j=0; j < sqlWrapperFactory.getNumEventsPerSql(); j++ )
						sqlWrapperFactory.add(allEvents.get(i+j));
					
					//ISqlModel model = DefaultFactory.getFactory().getSqlModel();
					//model.setObservationMgr(this.getObservationMgr());
					
					addSqlWrapper(sqlWrapperFactory.createSqlWrapper( this ) );
					//getSql().add( sqlWrapperFactory.createSqlWrapper() );
					i+=sqlWrapperFactory.getNumEventsPerSql();
				} else
					throw new WuqispankException("Factory [" + sqlWrapperFactory.getClass().getName() + "] was selected to parse event [" + allEvents.get(i).getRawEventData() + "] but the remaining number of events [" + allEvents.size() + "] wasnt' enough [" + i+ sqlWrapperFactory.getNumEventsPerSql() + "] to create an sql object");
			} else
				i++;//silently ignore unrecognized event, because future enhancements may display non-sql events.
		}
//			if (allEvents.get(i).getEventType().equals(EventType.ENTRY)) {
//				
//				if (allEvents.size() >= i+3) {
//					ISqlWrapper sql =  createSqlWrapper(allEvents.get(i), allEvents.get(i+1), allEvents.get(i+2) );
//					getSql().add(sql);
//					i+=3;
//				} else {
//					throw new WuqispankException("Found an ENTRY event and expecting two additional events but did not find those two i[" + i + "] allEvents.size [" + allEvents.size() + "]");
//				}
//				
//			} else {
//				i++;
//			}
		
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
