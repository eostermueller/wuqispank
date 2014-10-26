package org.wuqispank.importexport;

import java.io.File;
import java.nio.file.Path;

import org.wuqispank.WuqispankException;

public class WuqiSpankFileImportException extends WuqispankException {

	private File m_exportDir;
	private Path m_file;
	private Class<? extends IFileImporter> m_myClass;

	public WuqiSpankFileImportException(Throwable e) {
		super(e);
	}

	public void setExportDir(File val) {
		m_exportDir = val;
		
	}

	public void setFileName(Path val) {
		m_file = val;
		
	}

	public void setImporter(Class<? extends IFileImporter> val) {
		m_myClass = val;
		
	}

}
