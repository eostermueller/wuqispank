package org.wuqispank.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.wuqispank.DefaultFactory;
import org.wuqispank.DynaTracePurePathImporter;
import org.wuqispank.IRequestImporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.xml.sax.SAXException;

public class DynaTracePurePathServlet extends HttpServlet {
	static Logger LOG = LoggerFactory.getLogger(DynaTracePurePathServlet.class);
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
    {
		try {
			IRequestImporter importer = DefaultFactory.getFactory().getDynaTracePurePathImporter();
			//req.getInputStream().close();
			
			LOG.debug("Path [" + req.getContextPath() + "]");
			LOG.debug("Type [" + req.getContentType() + "]");
			LOG.debug("Length [" + req.getContentLength() + "]");
			LOG.debug("Available? [" + req.getInputStream().available() + "]");
			
			
			importer.setInputStream( req.getInputStream() );
			IRequestWrapper[] rqWrapperList = importer.importRq();
			
			resp.setContentType("text/xml");
			resp.getWriter().println("<dtAddPurePathRs>");
			for(IRequestWrapper r : rqWrapperList) {
				resp.getWriter().print("<uniqueId>");
				resp.getWriter().print(r.getUniqueId());
				resp.getWriter().println("</uniqueId>");
				WuqispankApp.getRepo().add(r);
			}
			resp.getWriter().println("</dtAddPurePathRs>");
			resp.getWriter().flush();
			resp.getWriter().close();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WuqispankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
