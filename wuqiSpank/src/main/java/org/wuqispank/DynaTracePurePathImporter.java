package org.wuqispank;


import java.io.BufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.headlessintrace.client.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.IStackTrace;
import org.wuqispank.web.EventCollector;
import org.xml.sax.SAXException;

public class DynaTracePurePathImporter implements IRequestImporter {
	static Logger LOG = LoggerFactory.getLogger(DynaTracePurePathImporter.class);

	private static final String DYNATRACE_PUREPATH_XML_METHOD = "method";
	private static final String DYNATRACE_PUREPATH_XML_ARGUMENT = "argument";
	private InputStream m_wuqiSpankExportXml;

	@Override
	public InputStream getInputStream() {
		return m_wuqiSpankExportXml;
	}

	@Override
	public void setInputStream(InputStream val) {
		

		//m_wuqiSpankExportXml = new BufferedInputStream(val);
		//BufferedInputStream b = new BufferedInputStream(val);
		//this.m_wuqiSpankExportXml = new DataInputStream(b);
	
		//m_wuqiSpankExportXml.mark(0);
		this.m_wuqiSpankExportXml = val;
	}
	private void importSql(IRequestWrapper rqWrap, NodeList sqlList) throws WuqispankException {
		
		for(int i = 0; i< sqlList.getLength();i++) {
			Node eventNode = sqlList.item(i);
			if (eventNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventNode;

				String currentAttrName = null;
				String tmpString = null;
				long tmpLong = -1;
				tmpString = eElement.getAttribute(DYNATRACE_PUREPATH_XML_METHOD);
				if (tmpString.startsWith("executeQuery")
						//|| tmpString.startsWith("execute")
						|| tmpString.startsWith("executeUpdate")) {
					
					tmpString = eElement.getAttribute(DYNATRACE_PUREPATH_XML_ARGUMENT);
					if (tmpString !=null && !"".equals(tmpString.trim())) {
						ISqlWrapper sqlWrapper = rqWrap.createBlankSqlWrapper();
						rqWrap.addSqlWrapper(sqlWrapper);
						sqlWrapper.setSqlText(tmpString);
					} else {
						LOG.debug("Got some quesitonable data from DynaTrace PurePath XML argument attribute [" + tmpString + "]");
					}
				}
			}
		}
			
		
	}
	public static String extractTextChildren(Element parentNode) {
		StringBuilder sb = new StringBuilder();
	    NodeList childNodes = parentNode.getChildNodes();
	    for (int i = 0; i < childNodes.getLength(); i++) {
	      Node node = childNodes.item(i);
	      if (node.getNodeType() == Node.TEXT_NODE) {
	        sb.append(node.getNodeValue() );
	      }
	    }
	    return sb.toString();
	}
	/**
	 * Will always return an array of 0 or 1 items.
	 */
	@Override
	public IRequestWrapper[] importRq() throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		if (getInputStream()==null) {
			Exception e = new Exception();//stack trace might be helpful to caller;
			throw new WuqispankException("Before calling DefaultimportRq.importRq, you must call setInputStream() with the XML to be imported.",e);
		}
		//getInputStream().reset();
		//String xmlInputString = new Scanner(this.getInputStream(),"UTF-8").useDelimiter("\\A").next();
		//Document doc = dBuilder.parse(xmlInputString);
		
		//int contentLength = this.getInputStream()
		//int contentLength = getInputStream()
//		InputStream is = this.getInputStream();
//		byte[] byteArray = new byte[contentLength]; 
//		is.read(byteArray,0,contentLength-1);

//		int c = -1;
//		StringBuffer readBuffer = new StringBuffer();
//		while ((c=getInputStream().read()) != -1){
//		   readBuffer.append((char) c);
//		}
//		String inString = readBuffer.toString();		
		
		Document doc = dBuilder.parse(this.getInputStream());
//		Document doc = dBuilder.parse(readBuffer.toString() );

		//InputSource input = XmlMimeEntityHandler.getInputSource(contentTypeValue, in);
		
		
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		
		IRequest rq = org.headlessintrace.client.DefaultFactory.getFactory().getRequest();
		IRequestWrapper rqWrapper = DefaultFactory.getFactory().getRequestWrapper();
		rqWrapper.setRequest(rq);
		
		NodeList nList = null;
		
		nList = root.getElementsByTagName("enhancedsamplingnodeinfo");
		if (nList !=null && nList.getLength()==0) 
			nList = root.getElementsByTagName("nodeinfo");
		

		importSql(rqWrapper,nList);
		List<IRequestWrapper> results = new ArrayList<IRequestWrapper>();
		results.add(rqWrapper);
		
		IRequestWrapper prototype[]={};
		return results.toArray(prototype);
	}


}
