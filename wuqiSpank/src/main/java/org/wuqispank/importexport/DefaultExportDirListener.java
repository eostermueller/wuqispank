package org.wuqispank.importexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.DefaultReconnector;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.web.WebXmlConfigImpl;
import org.xml.sax.SAXException;

public class DefaultExportDirListener implements IExportDirListener {
	static Logger LOG = LoggerFactory.getLogger(DefaultExportDirListener.class);
	
	private WatchService m_watcher = null;
	private IImportExportMgr m_importExportMgr;
	private WatchService getWatchService() {
		return this.m_watcher;
	}
	
	public void init() throws IOException, ParserConfigurationException {
		m_watcher = FileSystems.getDefault().newWatchService();
		LOG.debug("About to init listener for export dir [" + this.m_importExportMgr
				.getExportDir()
				.getAbsolutePath() + "]");
		Path dir = Paths.get( this.m_importExportMgr.getExportDir().getAbsolutePath() );
		dir.register(getWatchService(), StandardWatchEventKinds.ENTRY_CREATE);
	}
	

	@Override
	public void run() {
		
		LOG.debug("Checking for new files in [" + this.m_importExportMgr.getExportDir().getAbsolutePath() + "]");
	    WatchKey key;
	    try {
	        // wait for a key to be available
	        key = getWatchService().take();
	    } catch (InterruptedException ex) {
	        return;
	    }
	 
	    for (WatchEvent<?> event : key.pollEvents()) {
	        // get event type
	        WatchEvent.Kind<?> kind = event.kind();
	 
	        // get file name
	        @SuppressWarnings("unchecked")
	        WatchEvent<Path> ev = (WatchEvent<Path>) event;
	        Path fileName = ev.context();
	 
	        System.out.println(kind.name() + ": " + fileName);
	 
	        if (kind == StandardWatchEventKinds.OVERFLOW) {
	            continue;
	        } else if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                  
	        	try {
	                 LOG.debug(kind.name() + ": " + fileName);	 
	                 LOG.error(kind.name() + ": " + fileName);
	                 this.m_importExportMgr.importDataFileOfAnyType(fileName, false);
	                 
	        	} catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        } else
	        	LOG.error("Received unknown file system change event [" + kind.toString() + "]");
	        	
	    }
	 
	    // IMPORTANT: The key must be reset after processed
	    boolean valid = key.reset();
    }

	@Override
	public void setImportExportMgr(IImportExportMgr val) {
		this.m_importExportMgr = val;
	}
	
}


