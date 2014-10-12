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
import org.wuqispank.importexport.IRequestExporter;
import org.wuqispank.model.IRequestWrapper;

public class TatImageRqServlet extends HttpServlet {
    private static final String URL_PARAM_RQ_ID = "rqid";
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
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		
	}
}
