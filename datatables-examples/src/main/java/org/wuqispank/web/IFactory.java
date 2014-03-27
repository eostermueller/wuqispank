package org.wuqispank.web;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.IRequestExporter;
import org.wuqispank.IRequestImporter;
import org.wuqispank.db.ISqlParser;
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
import org.wuqispank.ta_OLD.ITableHeaderConfiguration;
import org.wuqispank.ta_OLD.ITableHeaderRenderer;
import org.wuqispank.web.msgs.IMessages;

public interface IFactory {
	IRequestExporter getRequestExporter() throws ParserConfigurationException;
	IRequestImporter getRequestImporter() throws ParserConfigurationException;
	IMessages getMessages();
	IConfig getConfig();
	void setConfig(IConfig config);
	IRequestWrapper getRequestWrapper();
	ISqlWrapper getSqlWrapper();
	ISqlModel getSqlModel();
	ITable getTable();
	ITable getTable(String tableName);
	ISqlParser getSqlParser();
	IColumn getColumn();
	IStackTrace getStackTrace();
	ITableHeaderConfiguration getTableHeaderConfiguration();
	ITableHeaderRenderer getTableHeaderRenderer();
	IBinaryOperatorExpression getBinaryOperatorExpression();
	IModelObservationMgr getModelObservationMgr();
	ISqlStatsObserver getSqlStatsCounter();
	IRequestRepository createRepo();
}
