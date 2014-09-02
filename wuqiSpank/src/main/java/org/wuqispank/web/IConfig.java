package org.wuqispank.web;

import java.io.File;

import org.headlessintrace.client.connection.HostPort;
import org.headlessintrace.jdbc.IJdbcProvider;

public interface IConfig {
	HostPort getInTraceAgent();
	int getCircularBufferSize();
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
}