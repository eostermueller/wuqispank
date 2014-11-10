package org.wuqispank;

import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.jdbc.IJdbcProvider;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.foundationdb.FoundationDBSqlParser;
import org.wuqispank.importexport.DefaultExportDirListener;
import org.wuqispank.importexport.DefaultImportExportMgr;
import org.wuqispank.importexport.DefaultRequestExporter;
import org.wuqispank.importexport.DefaultRequestImporter;
import org.wuqispank.importexport.DynaTracePurePathImporter;
import org.wuqispank.importexport.IExportDirListener;
import org.wuqispank.importexport.IFileImporter;
import org.wuqispank.importexport.IImportExportMgr;
import org.wuqispank.importexport.IRequestExporter;
import org.wuqispank.importexport.IRequestImporter;
import org.wuqispank.importexport.DefaultInTraceEventFileImporter;
import org.wuqispank.importexport.DefaultRawSqlTextRequestImporter;
import org.wuqispank.model.CenterHeavyTableOrderMgr;
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
import org.wuqispank.model.ITableOrderMgr;
import org.wuqispank.model.InMemoryRequstRepo;
import org.wuqispank.ta_OLD.DefaultTableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.tablecount_DEPRECATED.DefaultTableHeaderRenderer;
import org.wuqispank.web.IConfig;
import org.wuqispank.web.IFactory;
import org.wuqispank.web.msgs.AmericanEnglishMessages;
import org.wuqispank.web.msgs.IMessages;
import org.wuqispank.web.tableaccesstimeline.DefaultRow;
import org.wuqispank.web.tableaccesstimeline.DefaultRowGroup;
import org.wuqispank.web.tableaccesstimeline.DefaultTableLaneMgr;
import org.wuqispank.web.tableaccesstimeline.GraphContext;
import org.wuqispank.web.tableaccesstimeline.IRow;
import org.wuqispank.web.tableaccesstimeline.IRowGroup;
import org.wuqispank.web.tableaccesstimeline.ITableLaneMgr;


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
 * Let\"s say that multiple text formats must be supported.  If so, just create the necessary implementation of ITraceEventParser
 * and configure the right one (as described above) at startup.
 * @author erikostermueller
 *
 */

public class DefaultFactory implements IFactory {
	public static final String RESEARCH_EYE_CATCHER = "@#WUQISPANK_RESEARCH#@:";
	private static IMessages msgs = new AmericanEnglishMessages();
	private static IConfig m_config = null;
	private static IFactory INSTANCE = new DefaultFactory();
	private static IImportExportMgr m_importExportMgr = new DefaultImportExportMgr();
	private IJdbcProvider m_jdbcProvider;
	
	@Override
	public IRequestWrapper getRequestWrapper() {
		IRequestWrapper requestWrapper = new DefaultRequestWrapper();
		return requestWrapper;
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
//	@Override
//	public ITable getTable() {
//		return new DefaultTable();
//	}
	@Override
	public ITable getTable(String tableName) {
		ITable t = new DefaultTable(tableName);
		if (this.getConfig()!=null)
			t.setShouldBeCached( this.getConfig().shouldTableBeCached(tableName) );
		if (this.getConfig()!=null)
			t.setGrowthTable( this.getConfig().isGrowthTable(tableName) );
		return t;
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
	public IModelObservationMgr getObservationMgr() {
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
	public IRequestImporter getDynaTracePurePathImporter() throws ParserConfigurationException {
		return new DynaTracePurePathImporter();
	}
	@Override
	public ITableOrderMgr getTableOrderMgr() {
		return new CenterHeavyTableOrderMgr();
	}
	@Override
	public ITableLaneMgr getTableLaneMgr() {
		ITableLaneMgr tlm =new DefaultTableLaneMgr();
		tlm.setConfig(getConfig());
		return tlm;
	}
	@Override
	public IRow createRow(GraphContext ctx, ISqlWrapper sql, Object groupLane, String parentRequestId, int sqlSeq) {
		return new DefaultRow(ctx,sql, groupLane, parentRequestId, sqlSeq);
	}
	@Override
	public IRowGroup createRowGroup(GraphContext val, String string) {
		
		return new DefaultRowGroup(val, string);
	}
	
	@Override 
	public IJdbcProvider getJdbcProvider() throws WuqispankException {
		if (m_jdbcProvider==null) {
			IJdbcProvider newInstance = null;
			String jdbcProviderClassName = null;
			try {
				jdbcProviderClassName = this.getConfig().getJdbcProvider();
				if ("org.wuqispank.web.WebXmlConfigImpl".equals(jdbcProviderClassName)) {
					newInstance = (IJdbcProvider) this.getConfig();
				} else {
					newInstance = (IJdbcProvider) Class.forName( jdbcProviderClassName ).newInstance();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new WuqispankException(this.getMessages().getJdbcConfigurationError(jdbcProviderClassName) ,e );
			}
			m_jdbcProvider = newInstance;
		}
		return m_jdbcProvider;
	}
	@Override 
	public  void setJdbcProvider(IJdbcProvider val) {
		m_jdbcProvider = val;
	}
	@Override
	public IReconnector getReconnector() {
		return new DefaultReconnector();
	}
	@Override
	public IRequestImporter getRequestImporter()
			throws ParserConfigurationException {
		return new DefaultRequestImporter();
	}
	@Override
	public IRequestImporter getRawSqlTextRequestImporter() {
		return new DefaultRawSqlTextRequestImporter();
	}
	@Override
	public IFileImporter getInTraceEventFileImporter() {
		return new DefaultInTraceEventFileImporter();
	}
	@Override
	public IExportDirListener getExportDirListener() {
		return new DefaultExportDirListener();
	}
	
	@Override
	public IImportExportMgr getImportExportManager() {
		return m_importExportMgr;
	}
	@Override
	public void setImportExportManager(IImportExportMgr val) {
		m_importExportMgr = val;
	}
}
