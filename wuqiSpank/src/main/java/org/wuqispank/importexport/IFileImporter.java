package org.wuqispank.importexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.PathMatcher;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.WuqispankException;
import org.xml.sax.SAXException;

public interface IFileImporter {
	InputStream getInputStream();
	void setInputStream(InputStream val);
	public PathMatcher getDataFilePathMatcher();
	public String getPathMatcherText();
	void importFile(IImportExportMgr iem, File fileToImport) throws SAXException, IOException, ParserConfigurationException, WuqispankException;

}
