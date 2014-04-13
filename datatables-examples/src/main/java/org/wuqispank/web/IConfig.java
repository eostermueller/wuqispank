package org.wuqispank.web;

import java.io.File;

import org.intrace.client.connection.HostPort;

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
}
