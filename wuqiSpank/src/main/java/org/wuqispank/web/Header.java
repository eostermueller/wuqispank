package org.wuqispank.web;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;

public class Header extends Panel {
	/**
	 * Construct.
	 * 
	 * @param id
	 *            id of the component
	 * @param exampleTitle
	 *            title of the example
	 * @param page
	 *            The example page
	 */
	public Header(String id, String exampleTitle, WebPage page)
	{
		super(id);
		this.setOutputMarkupId(true);
		this.setOutputMarkupPlaceholderTag(true);

		//add(new DebugBar("debug"));
		add(new Label("exampleTitle", exampleTitle));

//        // add the clock component
        HealthCheckLabel healthCheckLabel = new HealthCheckLabel("healthCheckLabel");
//        
        add(healthCheckLabel);

        // add the ajax behavior which will keep updating the component every 5
        // seconds
        //healthCheckLabel.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
	}

}
