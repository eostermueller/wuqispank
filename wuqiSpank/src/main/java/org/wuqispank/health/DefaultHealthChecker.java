package org.wuqispank.health;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.collect.Multiset.Entry;

public class DefaultHealthChecker implements Runnable {
	static Logger LOG = LoggerFactory.getLogger(DefaultHealthChecker.class);

	private HealthCheckRegistry registry = null;
	public DefaultHealthChecker(HealthCheckRegistry val) {
		this.registry = val;
	}
	@Override
	public void run() {
		SortedMap<String, HealthCheck.Result> results = this.registry.runHealthChecks();
		
		SortedMap<String, org.wuqispank.health.Result> ws_results = cloneAndConvertHealthCheckResults(results);
		
		DefaultFactory.getFactory().setHealthCheckResults(ws_results);
	}
	public static org.wuqispank.health.Result cloneAndConvertHealthCheckResult(HealthCheck.Result result) {
		String message = "<noInfluxdbHealthMsgFound>";
		if (result.getMessage()!=null && result.getMessage().trim().length() > 0) {  
			message = new String(result.getMessage());
		} else {
			message = result.toString();
		}
		Throwable newError = null;
		if (result.getError() !=null) {
			newError = new Throwable();
			StackTraceElement[] newStackTrace = Arrays.copyOf( result.getError().getStackTrace(), result.getError().getStackTrace().length);
			newError.setStackTrace( newStackTrace );
		}
		org.wuqispank.health.Result localResult = null;
		if (result.isHealthy())
			localResult = org.wuqispank.health.Result.healthy(message);
		else {
			if (newError!=null)
				localResult = org.wuqispank.health.Result.unhealthy(newError);
			else
				localResult = org.wuqispank.health.Result.unhealthy(message);
		}
		return localResult;
		
	}
	public static org.wuqispank.health.Result cloneHealthCheckResult(org.wuqispank.health.Result result) {
		String message = "<noInfluxdbHealthMsgFound>";
		if (result.getMessage()!=null && result.getMessage().trim().length() > 0) {  
			message = new String(result.getMessage());
		} else {
			message = result.toString();
		}
		Throwable newError = null;
		if (result.getError() !=null) {
			newError = new Throwable();
			StackTraceElement[] newStackTrace = Arrays.copyOf( result.getError().getStackTrace(), result.getError().getStackTrace().length);
			newError.setStackTrace( newStackTrace );
		}
		org.wuqispank.health.Result localResult = null;
		if (result.isHealthy())
			localResult = org.wuqispank.health.Result.healthy(message);
		else {
			if (newError!=null)
				localResult = org.wuqispank.health.Result.unhealthy(newError);
			else
				localResult = org.wuqispank.health.Result.unhealthy(message);
		}
		return localResult;
		
	}
	
	public static SortedMap<String, org.wuqispank.health.Result> cloneAndConvertHealthCheckResults(
			SortedMap<String, com.codahale.metrics.health.HealthCheck.Result> healthCheckResults) {

		SortedMap<String, org.wuqispank.health.Result> localSortedMap = new TreeMap<String,org.wuqispank.health.Result>();
		
		if (healthCheckResults!=null) {
			for(Map.Entry<String,HealthCheck.Result> entry : healthCheckResults.entrySet()) {
				String myKey = entry.getKey();
				org.wuqispank.health.Result newCopy = DefaultHealthChecker.cloneAndConvertHealthCheckResult(entry.getValue());
				localSortedMap.put(myKey, newCopy);
			}
		} else {
			LOG.error("Is this a problem?  Found zero health check results in DefaultHealthChecker.cloneHealthCheckResults().");
		}
		return localSortedMap;
	}
	public static SortedMap<String, org.wuqispank.health.Result> cloneHealthCheckResults(
			SortedMap<String, org.wuqispank.health.Result> healthCheckResults) {

		SortedMap<String, org.wuqispank.health.Result> localSortedMap = new TreeMap<String,org.wuqispank.health.Result>();
		
		if (healthCheckResults!=null) {
			for(Map.Entry<String,org.wuqispank.health.Result> entry : healthCheckResults.entrySet()) {
				String myKey = entry.getKey();
				org.wuqispank.health.Result newCopy = DefaultHealthChecker.cloneHealthCheckResult(entry.getValue());
				localSortedMap.put(myKey, newCopy);
			}
		} else {
			LOG.error("Is this a problem?  Found zero health check results in DefaultHealthChecker.cloneHealthCheckResults().");
		}
		return localSortedMap;
	}

}
