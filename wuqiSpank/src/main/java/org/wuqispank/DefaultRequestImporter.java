package org.wuqispank;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.intrace.client.model.ITraceEventParser;
import org.intrace.client.request.IRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.IStackTrace;
import org.xml.sax.SAXException;

public class DefaultRequestImporter implements IRequestImporter {

	private InputStream m_wuqiSpankExportXml;

	@Override
	public InputStream getInputStream() {
		return m_wuqiSpankExportXml;
	}

	@Override
	public void setInputStream(InputStream val) {
		m_wuqiSpankExportXml = val;
	}
	private void importSql(IRequestWrapper rqWrap, NodeList sqlList) throws WuqispankException {
		
		for(int i = 0; i< sqlList.getLength();i++) {
			Node eventNode = sqlList.item(i);
			if (eventNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventNode;
				
				ISqlWrapper sqlWrapper = rqWrap.createBlankSqlWrapper();

				rqWrap.addSqlWrapper(sqlWrapper);

				String currentAttrName = null;
				String tmpString = null;
				long tmpLong = -1;
				try {
					currentAttrName = IRequestExporter.ATTRIBUTE_NAME_SEQUENCE;
					tmpString = eElement.getAttribute(currentAttrName);		
					tmpLong = Long.parseLong(tmpString);
					sqlWrapper.setSequence((int) tmpLong);
					
					currentAttrName = IRequestExporter.ATTRIBUTE_NAME_CLIENT_DATE_TIME;
					tmpString = eElement.getAttribute(currentAttrName);		
					tmpLong = Long.parseLong(tmpString);
					sqlWrapper.setLousyDateTimeMillis(tmpLong);
					
					currentAttrName = IRequestExporter.ATTRIBUTE_NAME_CLIENT_DATE_TIME;
					tmpString = eElement.getAttribute(currentAttrName);		
					tmpLong = Long.parseLong(tmpString);
					sqlWrapper.setAgentEntryTimeMillis(tmpLong);
					
					currentAttrName = IRequestExporter.ATTRIBUTE_NAME_CLIENT_DATE_TIME;
					tmpString = eElement.getAttribute(currentAttrName);		
					tmpLong = Long.parseLong(tmpString);
					sqlWrapper.setAgentExitTimeMillis(tmpLong);
					
				} catch (NumberFormatException nfe) {
					throw new WuqispankException("Found an invalid numeric for an exported trace for ["  + sqlWrapper.getSequence() + "], child of [id=" + rqWrap.getUniqueId() + "].  Attribute [" + currentAttrName + "] value [" + tmpString + "]", nfe);
				}
				
				String myStackTrace = extractTextChildren((Element)eElement.getElementsByTagName(IRequestExporter.EXPORT_STACK_TRACE_TAG_NAME).item(0));
				if (myStackTrace !=null && myStackTrace.trim().length()>0) {
					if (myStackTrace.charAt(0)=='[')
						myStackTrace = myStackTrace.substring(1);
					
					if (myStackTrace.charAt(myStackTrace.length()-1)==']')
						myStackTrace = myStackTrace.substring(0,myStackTrace.length()-1);
					
					ITraceEventParser eventParser = org.intrace.client.DefaultFactory.getFactory().getEventParser();
					StackTraceElement[] arraySte = eventParser.parseStackTrace(myStackTrace);
					IStackTrace modelStackTrace = DefaultFactory.getFactory().getStackTrace(); 
					modelStackTrace.setStackTraceElements(arraySte);
					sqlWrapper.setStackTrace(modelStackTrace);
				}

				String sql = extractTextChildren((Element)eElement.getElementsByTagName(IRequestExporter.EXPORT_SQL_STMT_TAG_NAME).item(0));
				//String sql = eElement.getElementsByTagName(IRequestExporter.EXPORT_SQL_STMT_TAG_NAME).item(0).getTextContent();
				if (sql.trim().length() >0)
					sqlWrapper.setSqlText( sql );
				else {
					String msg = DefaultFactory.getFactory().getMessages().getEmptySqlError(sql,rqWrap.getUniqueId(), sqlWrapper.getSequence() );
					throw new WuqispankException(msg);
				}
				
				//rqWrap.getSql().add(sqlWrapper);
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
	@Override
	public IRequestWrapper[] importRq() throws SAXException, IOException, ParserConfigurationException, WuqispankException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		if (getInputStream()==null) {
			Exception e = new Exception();//stack trace might be helpful to caller;
			throw new WuqispankException("Before calling DefaultimportRq.importRq, you must call setInputStream() with the XML to be imported.",e);
		}
		Document doc = dBuilder.parse(getInputStream());
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		NodeList nList = root.getElementsByTagName(IRequestExporter.EXPORT_SINGLE_REQUEST_TAG_NAME);
		List<IRequestWrapper> results = new ArrayList<IRequestWrapper>();
		for (int i = 0; i < nList.getLength();i++) {
		 
			Node nNode = nList.item(i);
			//System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				IRequest rq = org.intrace.client.DefaultFactory.getFactory().getRequest();
				IRequestWrapper rqWrapper = DefaultFactory.getFactory().getRequestWrapper();
				rqWrapper.setRequest(rq);
				
				Element eElement = (Element) nNode;
				String uniqueId = eElement.getAttribute(IRequestExporter.EXPORT_ATTRIBUTE_ID_TAG_NAME);
				rq.setUniqueId(uniqueId);
	
				String threadId = eElement.getAttribute(IRequestExporter.EXPORT_ATTRIBUTE_THREAD_ID_TAG_NAME);
				rq.setThreadId(threadId);
				
				NodeList sqlList = eElement.getElementsByTagName(IRequestExporter.EXPORT_SQL_TAG_NAME);
				
				importSql(rqWrapper,sqlList);
				//validateSql(eventList);
				results.add(rqWrapper);
				
			} else
				throw new WuqispankException("Did not find [" + IRequestExporter.EXPORT_SINGLE_REQUEST_TAG_NAME + "]");
		}
		IRequestWrapper prototype[]={};
		return results.toArray(prototype);
	}


}
