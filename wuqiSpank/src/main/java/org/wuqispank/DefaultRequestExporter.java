package org.wuqispank;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;

/**
 * <WuqispankExport>
 * 		<Rq id='a3f20' threadId='myThreadId' >
 * 			<Sql seq='0' beginTs="" endTs="">
 * 					<StmtText/>
 * 					<StackTrace/>
 * 			</Sql>
 * 			<Sql seq='1' beginTs="" endTs="">
 * 					<StmtText/>
 * 					<StackTrace/>
 * 			</Sql>
 * </WuqispankExport>
 * 
 * @author erikostermueller
 *
 */
public class DefaultRequestExporter implements IRequestExporter {
	DocumentBuilderFactory m_docFactory = null;
	DocumentBuilder m_docBuilder = null;
	Document m_doc = null;
	private OutputStream m_outputStream = null;

	public DefaultRequestExporter() throws ParserConfigurationException {
		m_docFactory = DocumentBuilderFactory.newInstance();
		m_docBuilder = m_docFactory.newDocumentBuilder();
		// root elements
		m_doc = m_docBuilder.newDocument();
		Element rootElement = m_doc.createElement(EXPORT_ROOT_TAG_NAME);
		m_doc.appendChild(rootElement);		
	}

	@Override
	public void export(IRequestWrapper rw) throws TransformerException  {

		Element requestElement = m_doc.createElement(EXPORT_SINGLE_REQUEST_TAG_NAME);
		m_doc.getDocumentElement().appendChild(requestElement);		
		
		requestElement.setAttribute(EXPORT_ATTRIBUTE_ID_TAG_NAME, rw.getUniqueId());
		requestElement.setAttribute(EXPORT_ATTRIBUTE_THREAD_ID_TAG_NAME, rw.getRequest().getThreadId());
		
		for(ISqlWrapper sql : rw.getSql())
			writeSql(requestElement, sql);
		
		writeToOutput();
	} 
	private void writeToOutput() throws TransformerException {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(m_doc);
		StreamResult result = new StreamResult(getOutputStream());
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);		
	}

	private void writeSql(Element requestEle, ISqlWrapper sqlModel) {
		
		Element sqlEle = m_doc.createElement(EXPORT_SQL_TAG_NAME);
		requestEle.appendChild(sqlEle);
		
		sqlEle.setAttribute(ATTRIBUTE_NAME_SEQUENCE, String.valueOf(sqlModel.getSequence()));
		sqlEle.setAttribute(ATTRIBUTE_NAME_ENTRY_TIME, String.valueOf(sqlModel.getAgentEntryTimeMillis()));
		sqlEle.setAttribute(ATTRIBUTE_NAME_EXIT_TIME, String.valueOf(sqlModel.getAgentExitTimeMillis()));
		sqlEle.setAttribute(ATTRIBUTE_NAME_CLIENT_DATE_TIME, String.valueOf(sqlModel.getLousyDateTimeMillis()));
		
		Element sqlStmt = m_doc.createElement(EXPORT_SQL_STMT_TAG_NAME);
		sqlStmt.appendChild(m_doc.createTextNode(sqlModel.getSqlText()));
		sqlEle.appendChild(sqlStmt);

		Element stackTraceEle = m_doc.createElement(EXPORT_STACK_TRACE_TAG_NAME);
		//String stackTrace = Arrays.toString(sqlModel.getExitEvent().getStackTrace());
		String stackTrace = Arrays.toString(sqlModel.getStackTrace().getStackTraceElements());
		stackTraceEle.appendChild(m_doc.createTextNode(stackTrace));
		sqlEle.appendChild(stackTraceEle);
	}

	@Override
	public OutputStream getOutputStream() {
		return m_outputStream;
	}

	@Override
	public void setOutputStream(OutputStream val) {
		m_outputStream = val;
		
	}
}
