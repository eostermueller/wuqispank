package org.wuqispank;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.foundationdb.FoundationDBSqlParser;
import org.wuqispank.model.DefaultBinaryOperatorExpression;
import org.wuqispank.model.DefaultColumn;
import org.wuqispank.model.DefaultModelObservationMgr;
import org.wuqispank.model.DefaultRequestWrapper;
import org.wuqispank.model.DefaultSqlModel;
import org.wuqispank.model.DefaultSqlStatsObserver;
import org.wuqispank.model.DefaultSqlWrapper;
import org.wuqispank.model.DefaultStackTrace;
import org.wuqispank.model.DefaultTable;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.IModelObservationMgr;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlStatsObserver;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.IStackTrace;
import org.wuqispank.model.ITable;
import org.wuqispank.model.InMemoryRequstRepo;
import org.wuqispank.ta_OLD.DefaultTableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.DefaultTableHeaderRenderer;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.IFactory;
import org.wuqispank.web.msgs.AmericanEnglishMessages;
import org.wuqispank.web.msgs.IMessages;


/**
 * This is my low-complexity approach to dependency injection.
 * This class decides on default implementations.
 * If, application wide, you want to override a default, you do this:
 * 1) At application startup time, instantiate your own implementation of IFactory (probably extending DefaultFactory).
 * 2) Pass an instance of your new factory into DefaultFactory.setFactory(), as detailed here:
   <PRE>
   
   IFactory myCustomFactory = new DefaultFactory() {
		@Override
		public ITraceEventParser getEventParser() {
			return new MyCustomTraceEventParser();
		}
   };
   DefaultFactory.setFactory(myCustomFactory);
   </PRE>
 * Then, subsequent calls to DefaultFactory.getFactory() will return myCustomFactory.
 * USE CASE:  the headless InTrace client relies on the text of an event to be formatted in a very specific way.
 * Let's say that multiple text formats must be supported.  If so, just create the necessary implementation of ITraceEventParser
 * and configure the right one (as described above) at startup.
 * @author erikostermueller
 *
 */

public class DefaultFactory implements IFactory {
	private static IMessages msgs = new AmericanEnglishMessages();
	private static IConfig m_config = null;
	private static IFactory INSTANCE = new DefaultFactory();
	
	@Override
	public IRequestWrapper getRequestWrapper() {
		IRequestWrapper requestWrapper = new DefaultRequestWrapper();
		requestWrapper.setModelObservationMgr( this.getModelObservationMgr());
		return new DefaultRequestWrapper();
	}
	@Override
	public IMessages getMessages() {
		return msgs;
	}
	@Override
	public IConfig getConfig() {
		return m_config;
	}
	@Override
	public void setConfig(IConfig config) {
		this.m_config = config;
	}
	/**
	 * This method was designed to be called just one time at application startup.
	 * This could be done in a single static block of a single class, that perhaps
	 * grabbed the name of the Factory impl from a -D parameter or similar.
	 * If this method detects that its already been called, it throws a runtime exception.
	 * I did that so the developer will know whether the "worst case" has happened (ie, when multiple code locations are trying to set the factory).
	 * @param myFactory
	 */
	public static void setFactory(IFactory myFactory) {
		if (INSTANCE==null) {
			INSTANCE = myFactory;
		} else {
			throw new RuntimeException("Factory has already been set to value [" + INSTANCE.getClass().getName() + "]");
		}
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public static IFactory getFactory() {
		if( INSTANCE==null) {
			return new DefaultFactory();
		}
		return INSTANCE;
	}
	public ISqlWrapper getSqlWrapper() {
		return new DefaultSqlWrapper();
	}
	@Override
	public ISqlModel getSqlModel() {
		return new DefaultSqlModel();
	}
	@Override
	public ITable getTable() {
		return new DefaultTable();
	}
	@Override
	public ITable getTable(String tableName) {
		return new DefaultTable(tableName);
	}
	@Override
	public IColumn getColumn() {
		return new DefaultColumn();
	}
	@Override
	public ISqlParser getSqlParser() {
		return new FoundationDBSqlParser();
	}
	@Override
	public IStackTrace getStackTrace() {
		return new DefaultStackTrace();
	}
	@Override
	public ITableHeaderConfiguration getTableHeaderConfiguration() {
		return new DefaultTableHeaderConfiguration();
	}
	@Override
	public ITableHeaderRenderer getTableHeaderRenderer() {
		return new DefaultTableHeaderRenderer();
	}
	@Override
	public IBinaryOperatorExpression getBinaryOperatorExpression() {
		return new DefaultBinaryOperatorExpression();
	}
	@Override
	public IModelObservationMgr getModelObservationMgr() {
		return new DefaultModelObservationMgr();
	}
	@Override
	public ISqlStatsObserver getSqlStatsCounter() {
		
		return new DefaultSqlStatsObserver();
	}
	@Override
	public IRequestRepository createRepo() {
		return new InMemoryRequstRepo();
	}
	@Override
	public IRequestExporter getRequestExporter() throws ParserConfigurationException {
		return new DefaultRequestExporter();
	}
	@Override
	public IRequestImporter getRequestImporter() throws ParserConfigurationException {
		return new DefaultRequestImporter();
	}
	
}
