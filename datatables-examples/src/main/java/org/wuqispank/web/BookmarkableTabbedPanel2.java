/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wuqispank.web;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarkableTabbedPanel2<T extends ITab> extends TabbedPanel
{
	private static final Logger log = LoggerFactory.getLogger(BookmarkableTabbedPanel2.class);

	private static final long serialVersionUID = 6715403003765254144L;

	private PageParameters pageParameters;
	private String tabParameterName;
	private int defaultTabIndex = 0;

	/**
	 * Using this constructor the following defaults take effect:
	 * <ul>
	 * <li>tabParameterName = component id</li>
	 * <li>defaultTabIndex = 0</li>
	 * </ul>
	 * 
	 * @param id
	 *            component id
	 * @param tabs
	 *            list of ITab objects used to represent tabs
	 * @param pageParameters
	 *            Container for parameters to a requested page. A parameter for the selected tab
	 *            will be inserted.
	 */
	public BookmarkableTabbedPanel2(String id, List<ITab> tabs, PageParameters pageParameters)
	{
		super(id, tabs);
		this.pageParameters = pageParameters;
		tabParameterName = "tab";
		if (pageParameters.getPosition(tabParameterName) >= 0)
		{
			String tab = pageParameters.get(tabParameterName).toString();
			try
			{
				int tabIndex = Integer.parseInt(tab);
				if (tabIndex >= 0 && tabIndex < tabs.size())
				{
					setSelectedTab(tabIndex);
				}
				else
				{
					setSelectedTab(defaultTabIndex);
				}

			}
			catch (NumberFormatException e)
			{
				setSelectedTab(defaultTabIndex);
			}
		}
		else
			setSelectedTab(defaultTabIndex);
	}

	@Override
	protected WebMarkupContainer newLink(String linkId, int index)
	{
		PageParameters localParameters = new PageParameters(pageParameters);
		log.debug("Entering newLink");
		WebMarkupContainer link;


		if (index == defaultTabIndex)
			localParameters.remove(tabParameterName); // keep URLs short
		else
		{
			localParameters.set(tabParameterName, "" + index);
			log.debug("passed index does not match default tab index.  Added to link [" +
				tabParameterName + "] [" + index + "]");
		}
		link = new BookmarkablePageLink<Object>(linkId, getPage().getClass(), localParameters);

		if (index == getSelectedTab())
			setSelectedTab(index);
		log.debug("Exiting newLink.  created link [" + localParameters.toString() + "]");

		return link;
	}
}
