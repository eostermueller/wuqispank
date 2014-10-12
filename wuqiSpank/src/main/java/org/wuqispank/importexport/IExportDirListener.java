package org.wuqispank.importexport;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.model.IRequestRepository;


public interface IExportDirListener extends Runnable {
	public void init() throws IOException, ParserConfigurationException;
	public void setImportExportMgr(IImportExportMgr val);

}
