package org.wuqispank.web;

import java.io.File;

import org.headlessintrace.client.connection.HostPort;
import org.headlessintrace.jdbc.IJdbcProvider;

import com.codahale.metrics.health.HealthCheckRegistry;

public interface IConfig {
	HostPort getInTraceAgent();
	int getCircularBufferSize();
	int getNumberOfRequestsToRemoveAtOnce();
	File getExportDir();
	int getMaxRowCountOfHeterogenousGroup();
	int getMaxRowCountOfHomogeneousGroup();
	String getMxGraphFolderName();
	int getXSpaceBetwenTableLanes();
	int getXStartLeftMostTableLane();
	int getXNegOffset();
	int getTableMarkerX();
	int getTableMarkerY();
	int getTableMarkerWidth();
	int getTableMarkerHeight();
	int getEvenOddRowWidth();
	int getEvenOddRowHeight();
	int getTableMarkerYOffset();
	int getWidthOfVerticalTableLane();
	int getXStartLeftMostTableLabel();
	int getHeightOfVerticalTableLane();
	String getJdbcProvider();
	boolean isNativeSQL();
	int getReconnectIntervalInSeconds();
	boolean shouldTableBeCached(String tableName);
	void setTableShouldBeCached(String tableName);
	String getRawSqlStmtDelimiter();
	String getRawSqlRequestDelimiter();
	long getExportDirListenerIntervalInSeconds();
	boolean isGrowthTable(String tableName);
	void setGrowthTable(String tableName);
	int getInfluxDbPort();
	String getInfluxDbHost();
	int getInfluxDbWriteIntervalSeconds();
	int getHealthCheckIntervalSeconds();
	String getInfluxdbDbName();
	String getInfluxDbUser();
	String getInfluxDbPassword();
	int getInfluxdbBatchSize();
	String getInfluxdbRetentionPolicy();
	Runnable getHealthChecker(HealthCheckRegistry registry);
	String getGrafanaHost();
	int getGrafanaPort();
	int getGrafanaHealthCheckTimeoutInMs();
}
