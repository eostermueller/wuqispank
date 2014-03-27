package org.wuqispank.web;

import java.io.File;

import org.intrace.client.connection.HostPort;

public interface IConfig {
	HostPort getInTraceAgent();
	int getCircularBufferSize();
	File getExportDir();
}
