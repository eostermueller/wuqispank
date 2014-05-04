package org.wuqispank;

import java.io.IOException;
import java.io.InputStream;





import javax.xml.parsers.ParserConfigurationException;

import org.wuqispank.model.IRequestWrapper;
import org.xml.sax.SAXException;

public interface IRequestImporter {
	IRequestWrapper[] importRq() throws SAXException, IOException, ParserConfigurationException, WuqispankException;
	InputStream getInputStream();
	void setInputStream(InputStream val);

}
