package org.wuqispank.importexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.xml.sax.SAXException;

public class DefaultImportExportMgr implements IImportExportMgr {
	static Logger LOG = LoggerFactory.getLogger(DefaultImportExportMgr.class);

	private List<IRequestImporter> m_importers = new CopyOnWriteArrayList<IRequestImporter>();
	private IRequestRepository m_repo = null;
	private File m_exportDir;
	
	@Override
	public File getExportDir() {
		return m_exportDir;
	}
	@Override
	public void setExportDir(File val) {
		this.m_exportDir = val;
	}
	@Override
	public void addImporter(IRequestImporter val) {
		this.m_importers.add(val);
	}
	
	@Override
	public void importAtSystemStartup()  {
		try (DirectoryStream<Path> ds = 
				  Files.newDirectoryStream(FileSystems.getDefault().getPath(getExportDir().getAbsolutePath()))) {
			
			for (Path p : ds) {
				try {
					this.importDataFileOfAnyType(p,true);
				} catch (WuqiSpankFileImportException w) {
					LOG.error(w.getMessage());
					LOG.error(w.getCause().toString());
					w.printStackTrace();
				}
			}

		} catch (IOException e) {
		   e.printStackTrace();
		}		
	}

	@Override
	public void setRepo(IRequestRepository val) {
		this.m_repo = val;
	}

	@Override
	public IRequestRepository getRepo() {
		return m_repo;
	}

	private void importFile(IRequestImporter importer, File fileToImport) throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		importer.setInputStream( new FileInputStream(fileToImport) );
		try {
			importer.setRequestIdPrefix(fileToImport.getName());
		} catch(UnsupportedOperationException x) {}
		IRequestWrapper[] requests = importer.importRq();
		for(IRequestWrapper rq : requests) {
			String uniqueId = rq.getUniqueId();
			boolean duplicateFound = false;
			if (uniqueId!=null && uniqueId.trim().length() > 0) {

				IRequestWrapper tmp = getRepo().get(uniqueId);
				if (tmp!=null)
					duplicateFound = true;
			}
			LOG.debug("Duplicate found? [" + duplicateFound + "]. uniqueId [" + uniqueId + "] taken from import file [" + fileToImport.toString() + "]");

			if (!duplicateFound) {
				getRepo().add(rq);
				LOG.debug("Just added [id=" + rq.getUniqueId() + "] from import file [" + fileToImport.toString() + "]");
			} else
				LOG.debug("Duplicate request was not imported.  [id=" + uniqueId + "] found in in-memory repo. Import file [" + fileToImport.toString() + "]");

		}
	}
	
	@Override
	public String getHumanReadableSupportedFileTypes() {
		StringBuilder sb = new StringBuilder();
		for(IRequestImporter importer : this.m_importers) {
			sb.append(importer.getPathMatcherText());
			sb.append(";");
		}
		return sb.toString();
	}
	
	@Override
	public boolean importDataFileOfAnyType(Path val, boolean systemStartup) throws WuqiSpankFileImportException {
		LOG.debug("##Entering importDataFileOfAnyType [" + val.getFileName().toString() + "] "); 
		int missCount = 0;
		
		Path file = null;
		if (val.getNameCount()==1 && val.getParent()==null) {
			Path parent = this.getExportDir().toPath();
			file = Paths.get(parent.toAbsolutePath().toString(), val.toString());
		} else
			file = val;
			
		for(IRequestImporter importer : this.m_importers) {
 		   LOG.debug("Considering import of file [" + file.getFileName().toString() + "] using importer [" + importer.getClass().getName() + "]"); 
 		   //LOG.error("Is this a full path? [" + file.toString() + "] numParts [" + file.getNameCount() + "] parent [" + file.getParent() +  "]"); 
			if (importer.getDataFilePathMatcher().matches(file)) {
					LOG.debug("File matches");
					try {
						//if (importer.isRuntimeImporter() || systemStartup)
							importFile(importer, file.toFile());
					} catch (SAXException | IOException
							| ParserConfigurationException | WuqispankException e) {
						WuqiSpankFileImportException we = new WuqiSpankFileImportException(e);
						we.setExportDir( this.getExportDir());
						we.setFileName(file);
						we.setImporter(importer.getClass());
						throw we;
					}
				   break;
			} else
				missCount++;
		}
		if (missCount == this.m_importers.size())
			LOG.error("Found file [" + file.toString() + "] in export dir doesn't match any of the supported types [" + this.getHumanReadableSupportedFileTypes() + "].  Export dir [" + this.getExportDir().getAbsolutePath() + "]");

		return false;
	}
}
