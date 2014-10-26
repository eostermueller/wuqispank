package org.wuqispank.importexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.WuqispankException;
import org.xml.sax.SAXException;

public abstract class AbstractFileImporter implements IFileImporter {
	private InputStream m_dataFileToImport;

	@Override
	public InputStream getInputStream() {
		return m_dataFileToImport;
	}

	@Override
	public void setInputStream(InputStream val) {
		m_dataFileToImport = val;
	}

	@Override
	public abstract String getPathMatcherText();
	
	@Override
	public PathMatcher getDataFilePathMatcher() {
		return FileSystems.getDefault().getPathMatcher(this.getPathMatcherText()); 
	}
	@Override
	abstract public void importFile(IImportExportMgr iem, File fileToImport) throws SAXException, IOException, ParserConfigurationException, WuqispankException;

}
