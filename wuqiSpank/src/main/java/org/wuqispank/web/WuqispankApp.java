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

import javax.servlet.ServletContext;


import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.wuqispank.model.IRequestRepository;
import org.wuqispank.web.EventCollector;

public class WuqispankApp extends WebApplication
{

	@Override
	public Class<? extends Page> getHomePage()
	{
		return HomePage.class;
	}
	protected void init() {
		   super.init();
		   getDebugSettings().setOutputComponentPath(true);
	}

	public static IConfig getConfig() {
		ServletContext servletContext = WebApplication.get().getServletContext();
		IConfig config = new WebXmlConfigImpl(servletContext);
		return config;
	}

	public static IRequestRepository getRepo() {
		ServletContext servletContext = WebApplication.get().getServletContext();
		
		EventCollector eventCollector = (EventCollector)servletContext.getAttribute(EventCollector.WUQISPANK_REPO);
		
		return eventCollector.getRepo();
	}
	
}
