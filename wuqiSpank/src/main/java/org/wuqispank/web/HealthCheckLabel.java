package org.wuqispank.web;

import java.util.Map;
import java.util.SortedMap;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.wuqispank.DefaultFactory;


/**
 * How to get something to fade:
 * https://mail-archives.apache.org/mod_mbox/wicket-users/200805.mbox/%3C9168cd300804301831r51e0e51i8c3181c399dedfaf@mail.gmail.com%3E
 * 
 * Wed, September 5, 2015	9:23:53pm
 * 8115853				vacancies for in-mem requests					[doc]
 * 0					discards										[doc]
 * healthy/23535 		localhost:9123 				intrace-agent.jar 	[doc]
 * healthy/25321		localhost:8086 				influxdb			[doc]
 * healthy				localhost:3000 				grafana				[doc] [link]
					


EEE, MMM d, ''yy hh:mm:ss a z

 * 
 * 
 * 
 * @author erikostermueller
 *
 */
public class HealthCheckLabel extends Label {
//    private static Behavior feedbackSelfDisappear = new
//    		  Behavior() {
//    	         private static final long serialVersionUID = 1L;
//    	         @Override public void renderHead(Component c, IHeaderResponse response) {
//    	             //renderHead(c, response);
//    	             response.render(OnDomReadyHeaderItem.forScript("window.setTimeout(Effect.Fade('healthCheckLabel'),5000)") );
//    	         }
//    	     };		

	public HealthCheckLabel(String id) {
		super(id, new HealthCheckModel() );
//		this.add(feedbackSelfDisappear);
	}
	private static final long _1L = 98749871L;
	/**
	 * 
	 */
	private static final long serialVersionUID = _1L;
	
	private static class HealthCheckModel extends AbstractReadOnlyModel<String>  {
		private static final String HTML_LINE_BREAK = "<br/>";
		private SortedMap<String, org.wuqispank.health.Result> healthCheckResults = null;

		private HealthCheckModel() {
			this.healthCheckResults = DefaultFactory.getFactory().getHealthCheckResults();
		}
		@Override
		public String getObject() {
			StringBuilder sb = new StringBuilder();
			int resultCounter  = 0;
//			for(Map.Entry<String,org.wuqispank.health.Result> entry : healthCheckResults.entrySet()) {
//				
//				if (resultCounter++>0)
//					sb.append(HTML_LINE_BREAK);
//				
//				sb.append(entry.getKey())
//					.append(":")
//					.append( entry.getValue().getMessage() );
//							
//			}
			//return sb.toString();
			return ""+System.currentTimeMillis();
		}
		
	}

}
