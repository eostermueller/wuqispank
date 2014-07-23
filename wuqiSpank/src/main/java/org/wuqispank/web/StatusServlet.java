package org.wuqispank.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intrace.client.ITraceWriter;
import org.intrace.client.connection.DefaultConnectionList;
import org.intrace.client.connection.IConnection;
import org.intrace.client.connection.IConnectionList;
import org.intrace.client.request.DefaultRequestSeparator;
import org.intrace.client.request.IRequest;
import org.intrace.client.request.RequestConnection;
import org.intrace.client.request.RequestWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusServlet extends HttpServlet {
	Logger m_logger = LoggerFactory.getLogger(HomePage.class);

	private static final String INDENT = "/t/t/t";
	public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws IOException {
		PrintWriter out = rs.getWriter();
		
		IConnectionList connList = DefaultConnectionList.getSingleton();
		out.println("<p>"); out.println("Connection count: " + connList.size()); 
		for(IConnection conn : connList.getConnections()) {
			dispConnection(out, conn);
		}
		out.close();
	}
	private void dispConnection(PrintWriter out, IConnection conn) {
		out.println("<div style=\"margin-left: 4em;\">");
		out.println("<p>"); out.println("======================");
		out.println("<p>"); out.println(" connection:" + conn.getHostPort());
		out.println("<p>"); out.println(" is connected?:" + conn.isConnected());
		out.println("<p>"); out.println(" hash:" + conn.hashCode());
		if (conn instanceof RequestConnection) {
			RequestConnection requestConnection = (RequestConnection) conn;
			ITraceWriter traceWriter = requestConnection.getTraceWriter();//foo
			if (traceWriter instanceof RequestWriter) {
				RequestWriter rw = (RequestWriter)traceWriter;
				DefaultRequestSeparator drs = (DefaultRequestSeparator) rw.getRequestSeparator();
				//out.println("<p>"); out.println("request count:" + rw.getCompletedRequestQueue().size() );
				out.println("<p>"); out.println("request count:" + WuqispankApp.getRepo().size() );
				out.println("<p>"); out.println("event count:" + drs.eventCounter.get());
				
				out.println("<p>"); out.println("inflight request count:" + drs.getInFlightRequests().size());
			}
			
		} else {
			out.println("  RequestConnection not found!  Instead found [" + conn.getClass().getName() + "]");
		}
		out.println("</div>");

		
	}
}