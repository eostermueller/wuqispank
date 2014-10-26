package org.wuqispank.importexport;

import java.io.File;
import java.nio.file.Path;

import org.wuqispank.model.IRequestRepository;

public interface IImportExportMgr {
	void addImporter(IFileImporter val);
	void setRepo(IRequestRepository val);
	IRequestRepository getRepo();
	void importAtSystemStartup() throws WuqiSpankFileImportException;
	File getExportDir();
	void setExportDir(File val);
	String getHumanReadableSupportedFileTypes();
	boolean importDataFileOfAnyType(Path val, boolean runtime)
			throws WuqiSpankFileImportException;
}
