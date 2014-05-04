package org.wuqispank.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;

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

		//add(new DebugBar("debug"));
		add(new Label("exampleTitle", exampleTitle));

	}

}
