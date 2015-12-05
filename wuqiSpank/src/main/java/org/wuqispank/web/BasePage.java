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


import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

public class BasePage extends WebPage
{
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public BasePage()
	{
		this(new PageParameters());
	}

	/**
	 * Constructor
	 * 
	 * @param pageParameters
	 */
	public BasePage(final PageParameters pageParameters)
	{
		super(pageParameters);
		this.setStatelessHint ( true );
		final String packageName = getClass().getPackage().getName();
		add(new Header("mainNavigation", Strings.afterLast(packageName, '.'), this));
	}


	/**
	 * Construct.
	 * 
	 * @param model
	 */
	public BasePage(IModel<?> model)
	{
		super(model);
	}
	@Override
	public void renderHead(IHeaderResponse response)
	{
		renderBasicJS(response);
	}
	
	private void renderBasicJS(IHeaderResponse response)
	{
		response.render( JavaScriptHeaderItem.forUrl("js/wuqispank.js"));
		response.render( CssContentHeaderItem.forUrl("css/HealthCheckStatus.css"));
		
	}

}
