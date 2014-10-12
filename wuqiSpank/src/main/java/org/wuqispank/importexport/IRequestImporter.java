package org.wuqispank.importexport;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;









import java.nio.file.PathMatcher;

import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.xml.sax.SAXException;

public interface IRequestImporter {
	IRequestWrapper[] importRq() throws SAXException, IOException, ParserConfigurationException, WuqispankException;
	InputStream getInputStream();
	void setInputStream(InputStream val);
	public String getRequestIdPrefix();
	void setRequestIdPrefix(String string);
	public PathMatcher getDataFilePathMatcher();
	public String getPathMatcherText();
	public boolean isRuntimeImporter();
}
