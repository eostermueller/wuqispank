package org.wuqispank.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.wuqispank.DefaultFactory;
import org.wuqispank.IRequestExporter;
import org.wuqispank.model.IRequestWrapper;

public class SqlDetailRqServlet extends HttpServlet {
    private static final String URL_PARAM_RQ_ID = "rqid";
    private static final String URL_PARAM_SQL_SEQ = "sqlseq";
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
    	doGetOrPost(req,resp);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
    	doGetOrPost(req,resp);
    	
    }
	private void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		
		/**
		 * Required parameter1: rqid
		 */
		String rqId = req.getParameter(URL_PARAM_RQ_ID);
		
		if (rqId == null || "".equals(rqId.trim()) )
			throw new ServletException("wuqiSpank API WsSqlDetailRq expected URL parameter named [" + URL_PARAM_RQ_ID + "]");
		
		/**
		 * Required parameter2: sqlseq
		 */
		String strSqlSeq = req.getParameter(URL_PARAM_SQL_SEQ);
		if (strSqlSeq == null || "".equals(strSqlSeq.trim()) )
			throw new ServletException("wuqiSpank API WsSqlDetailRq expected numeric URL parameter named [" + URL_PARAM_SQL_SEQ + "]");
		
		int sqlSequence = -1;
		try {
			sqlSequence = Integer.parseInt(strSqlSeq);
		} catch (NumberFormatException nfe) {
			throw new ServletException("wuqiSpank API WsSqlDetailRq expected a numeric URL parameter named [" + URL_PARAM_SQL_SEQ+ "].  Instead, received a non-numeric [" + strSqlSeq + "]");
		}
		
		IRequestWrapper rqWrap = WuqispankApp.getRepo().get(rqId);
		if (rqWrap==null) {
			throw new ServletException("wuqiSpank API WsSqlDetailRq did not find request with [" + URL_PARAM_RQ_ID + "=" + rqId + "]"); 			
		}
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IRequestExporter re;
		try {
			re = DefaultFactory.getFactory().getRequestExporter();
			//re.setOutputStream(baos);
			re.setOutputStream(resp.getOutputStream());
			re.export( rqWrap, sqlSequence );
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		
	}
}
