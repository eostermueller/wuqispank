package org.wuqispank.importexport;

import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

public abstract class AbstractRequestImporter implements IRequestImporter {

	private InputStream m_dataFileToImport;
	private String m_requestIdPrefix;

	public AbstractRequestImporter() {
		super();
	}

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
	public String getRequestIdPrefix() {
		return m_requestIdPrefix;
	}

	@Override
	public void setRequestIdPrefix(String val) {
		m_requestIdPrefix = val;
	}

}