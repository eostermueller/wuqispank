package org.wuqispank.web;

import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.jdbc.IJdbcProvider;
import org.wuqispank.IReconnector;
import org.wuqispank.WuqispankException;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.importexport.IExportDirListener;
import org.wuqispank.importexport.IImportExportMgr;
import org.wuqispank.importexport.IRequestExporter;
import org.wuqispank.importexport.IRequestImporter;
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
import org.wuqispank.ta_OLD.ITableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.web.msgs.IMessages;
import org.wuqispank.web.tableaccesstimeline.GraphContext;
import org.wuqispank.web.tableaccesstimeline.IRow;
import org.wuqispank.web.tableaccesstimeline.IRowGroup;
import org.wuqispank.web.tableaccesstimeline.ITableLaneMgr;

import com.mxgraph.view.mxGraph;

public interface IFactory {
	IRequestExporter getRequestExporter() throws ParserConfigurationException;
	IRequestImporter getRequestImporter() throws ParserConfigurationException;
	IRequestImporter getDynaTracePurePathImporter() throws ParserConfigurationException;
	IMessages getMessages();
	IConfig getConfig();
	void setConfig(IConfig config);
	IRequestWrapper getRequestWrapper();
	ISqlWrapper getSqlWrapper();
	ISqlModel getSqlModel();
	//ITable getTable();
	ITable getTable(String tableName);
	ISqlParser getSqlParser();
	IColumn getColumn();
	IStackTrace getStackTrace();
	ITableHeaderConfiguration getTableHeaderConfiguration();
	ITableHeaderRenderer getTableHeaderRenderer();
	IBinaryOperatorExpression getBinaryOperatorExpression();
	IModelObservationMgr getObservationMgr();
	ISqlStatsObserver getSqlStatsCounter();
	IRequestRepository createRepo();
	ITableOrderMgr getTableOrderMgr();
	ITableLaneMgr getTableLaneMgr();
	IRowGroup createRowGroup(GraphContext val, String string);
	IRow createRow(GraphContext ctx,ISqlWrapper sql, Object groupLane, String string, int i);
	IJdbcProvider getJdbcProvider() throws WuqispankException;
	void setJdbcProvider(IJdbcProvider val);
	IReconnector getReconnector();
	IRequestImporter getRawSqlTextRequestImporter();
	IExportDirListener getExportDirListener();
	IImportExportMgr getImportExportManager();
	void setImportExportManager(IImportExportMgr val);
}
