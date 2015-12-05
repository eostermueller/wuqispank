package org.wuqispank.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.SortedMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.headlessintrace.client.ITraceWriter;
import org.headlessintrace.client.connection.DefaultConnectionList;
import org.headlessintrace.client.connection.IConnection;
import org.headlessintrace.client.connection.IConnectionList;
import org.headlessintrace.client.request.DefaultRequestSeparator;
import org.headlessintrace.client.request.IRequest;
import org.headlessintrace.client.request.RequestConnection;
import org.headlessintrace.client.request.RequestWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.health.Result;
import org.wuqispank.influxdb.DefaultInfluxdbWriter;

/**
 * Create the following message:
 * 
 <WuqiSpankStatusRs>
  <InfluxdbStatus>
    <Healthy />
    <Host>localhost</Host>
    <Port>8086</Port>
    <SuccessfulSqlWrites>0</SuccessfulSqlWrites>
    <FailedSqlWrites>0</FailedSqlWrites>
  </InfluxdbStatus>
  <InTraceStatus>
    <Healthy />
    <Host>localhost</Host>
    <Port>9123</Port>
    <ServletRequestCount>0</ServletRequestCount>
    <InFlightRequestCount>0</InFlightRequestCount>
    <EventCount>0</EventCount>
    <SqlCount>0</SqlCount>
  </InTraceStatus>
	<GrafanaStatus>
		<Unhealthy/>
		<Host>localhost</Host>
		</Port>8123</Port>
	</GrafanaStatus>
  <WuqiSpankServerDate>Mon, Sep 28, '15 08:30:38 PM CDT</WuqiSpankServerDate>
</WuqiSpankStatusRs>

 * @author erikostermueller
 *
 */
public class StatusServlet extends HttpServlet {
	Logger LOG = LoggerFactory.getLogger(HomePage.class);

	private static final String INDENT = "/t/t/t";

	private static final int UNINIT = -1;

	private static final String UNINIT_STRING = "<uninitialized>";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy hh:mm:ss a z");
	
	public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws IOException {
		try {
			SortedMap<String, org.wuqispank.health.Result> healthResults 
			= DefaultFactory.getFactory().getHealthCheckResults();
		
			rs.getWriter().append("<WuqiSpankStatusRs>\n");
		
			for(org.wuqispank.health.Result result : healthResults.values() ) {
				
				if (result.getMessage()!=null) {
					if (result.getMessage().startsWith(Result.INFLUXDB.toString())) {
						getInfluxdbStatus(result, rs.getWriter());
					} else if (result.getMessage().startsWith(Result.INTRACE.toString())) {
						getInTraceStatus( result, rs.getWriter());
					} else if (result.getMessage().startsWith(Result.GRAFANA.toString())) {
						getGrafanaStatus( result, rs.getWriter());
					}
				} else {
					LOG.error("Invalid HealthCheck result.  Expecting output of org.wuqispank.health.Result.format()");
				}
			}
			String dateTime = this.simpleDateFormat.format( new Date());
			rs.getWriter().append("<WuqiSpankServerDate>").append(dateTime).append("</WuqiSpankServerDate>\n");
			rs.getWriter().append("</WuqiSpankStatusRs>\n");
			rs.getWriter().close();
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	public void getGrafanaStatus(org.wuqispank.health.Result result,
			PrintWriter printWriter) {
		
		printWriter.append("<GrafanaStatus>\n");
		printWriter.append(result.isHealthy() ? "<Healthy/>" : "<Sick/>\n");			
		printWriter.append("<Host>").append(result.getHost()).append("</Host>\n");
		printWriter.append("<Port>").append(""+result.getPort()).append("</Port>\n");
		printWriter.append("</GrafanaStatus>\n");
		
		
	}
	
	public void getInfluxdbStatus(org.wuqispank.health.Result result,
			PrintWriter printWriter) {
		
		printWriter.append("<InfluxdbStatus>\n");
		printWriter.append(result.isHealthy() ? "<Healthy/>" : "<Sick/>\n");			
		printWriter.append("<Host>").append(result.getHost()).append("</Host>\n");
		printWriter.append("<Port>").append(""+result.getPort()).append("</Port>\n");
		printWriter.append("<SuccessfulSqlWrites>").append(""+DefaultInfluxdbWriter.getSuccessCount()).append("</SuccessfulSqlWrites>\n");
		printWriter.append("<FailedSqlWrites>").append(""+DefaultInfluxdbWriter.getFailureCount()).append("</FailedSqlWrites>\n");
		
		printWriter.append("</InfluxdbStatus>\n");
		
		
	}
	/**
	 * This method adds this XML to the given StringBuilder:
	 <InTraceStatus>
		<Unhealthy/>
		<Host>localhost</Host>
		</Port>8123</Port>
		<ServletRequestCount>3252</ServletRequestCount>
		<SqlCount>52355<SqlCount>
		<InTraceEventCount>52355</InTraceEventCount>
	</InTraceStatus>

	 */
	private void getInTraceStatus(Result result, PrintWriter printWriter ) {
		IConnection conn = null;
		boolean healthy = false;
		String host = UNINIT_STRING;
		int port = UNINIT;
		try {
			conn = DefaultFactory.getFactory().getReconnector().getConnection();
			if (conn!=null || conn instanceof RequestConnection) {
				RequestConnection requestConnection = (RequestConnection) conn;
				healthy = conn.isConnected();
				host = conn.getHostPort().hostNameOrIpAddress;
				port = conn.getHostPort().port;
				printWriter.append("<InTraceStatus>\n");
				printWriter.append(healthy ? "<Healthy/>" : "<Sick/>\n");
				printWriter.append("<Host>").append(host).append("</Host>\n");
				printWriter.append("<Port>").append(""+port).append("</Port>\n");
				printWriter.append("<ServletRequestCount>").append(""+WuqispankApp.getRepo().getCountOfTotalRequests()).append("</ServletRequestCount>\n");
				printWriter.append("<InMemoryRepositorySize>").append(""+WuqispankApp.getRepo().size()).append("</InMemoryRepositorySize>\n");
				printWriter.append("<InFlightRequestCount>").append(""+getInTraceInFlightRequests(requestConnection)).append("</InFlightRequestCount>\n");
				printWriter.append("<EventCount>").append(""+this.getInTraceEventCount(requestConnection)).append("</EventCount>\n");
				printWriter.append("<SqlCount>").append(""+WuqispankApp.getRepo().getSqlCount()).append("</SqlCount>\n");
				printWriter.append("<PercentFull>").append(""+WuqispankApp.getRepo().getPercentageFull()).append("</PercentFull>\n");
				printWriter.append("<FreeMemoryInMb>").append(""+WuqispankApp.getRepo().getFreeMemoryInMb()).append("</FreeMemoryInMb>\n");
				printWriter.append("<DiscardedRequests>").append(""+WuqispankApp.getRepo().getCountOfDiscardedRequests()).append("</DiscardedRequests>\n");
				
				
				
				printWriter.append("</InTraceStatus>\n");
			}
		} catch (WuqispankException we) {
			we.printStackTrace();
		}
	}
	private int getInTraceInFlightRequests(RequestConnection requestConnection) {
		int rc = UNINIT;
		ITraceWriter traceWriter = requestConnection.getTraceWriter();
		if (traceWriter instanceof RequestWriter) {
			RequestWriter rw = (RequestWriter)traceWriter;
			DefaultRequestSeparator drs = (DefaultRequestSeparator) rw.getRequestSeparator();
			rc = drs.getInFlightRequests().size();
		}
		return rc;
	}
	private int getInTraceEventCount(RequestConnection requestConnection) {
		int rc = UNINIT;
		ITraceWriter traceWriter = requestConnection.getTraceWriter();
		if (traceWriter instanceof RequestWriter) {
			RequestWriter rw = (RequestWriter)traceWriter;
			DefaultRequestSeparator drs = (DefaultRequestSeparator) rw.getRequestSeparator();
			rc = drs.eventCounter.get();
		}
			
		return rc;
	}
}