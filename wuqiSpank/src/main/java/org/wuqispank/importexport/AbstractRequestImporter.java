package org.wuqispank.importexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.web.EventCollector;
import org.xml.sax.SAXException;

public abstract class AbstractRequestImporter extends AbstractFileImporter implements IRequestImporter {
	static Logger LOG = LoggerFactory.getLogger(AbstractRequestImporter.class);

	private String m_requestIdPrefix;

	public AbstractRequestImporter() {
		super();
	}
	@Override
	public String getRequestIdPrefix() {
		return m_requestIdPrefix;
	}

	@Override
	public void setRequestIdPrefix(String val) {
		m_requestIdPrefix = val;
	}
	@Override
	public void importFile(IImportExportMgr iem, File fileToImport) throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		this.setInputStream( new FileInputStream(fileToImport) );
		try {
			this.setRequestIdPrefix(fileToImport.getName());
		} catch(UnsupportedOperationException x) {}
		IRequestWrapper[] requests = this.importRq();
		for(IRequestWrapper rq : requests) {
			String uniqueId = rq.getUniqueId();
			boolean duplicateFound = false;
			if (uniqueId!=null && uniqueId.trim().length() > 0) {

				IRequestWrapper tmp = iem.getRepo().get(uniqueId);
				if (tmp!=null)
					duplicateFound = true;
			}
			LOG.debug("Duplicate found? [" + duplicateFound + "]. uniqueId [" + uniqueId + "] taken from import file [" + fileToImport.toString() + "]");

			if (!duplicateFound) {
				iem.getRepo().add(rq);
				LOG.debug("Just added [id=" + rq.getUniqueId() + "] from import file [" + fileToImport.toString() + "]");
			} else
				LOG.debug("Duplicate request was not imported.  [id=" + uniqueId + "] found in in-memory repo. Import file [" + fileToImport.toString() + "]");

		}
	}

}