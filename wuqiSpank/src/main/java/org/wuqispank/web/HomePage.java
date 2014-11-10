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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.headlessintrace.client.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.datatables.DemoDatatable;
import org.wuqispank.model.IRequestWrapper;

public class HomePage extends BasePage
{
	Logger logger = LoggerFactory.getLogger(HomePage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @NAIVE_ASSUMPTION_1
	 * This ctor makes naive assumption that _all_ the requests in the repository
	 * will fit into memory.
	 * This is an ok assumption as long as the implementor of IRequestRepository
	 * is a circular buffer.
	 * 
	 * But when we move to requests persisted on the hard disk,
	 * we\"ll be able to store many more requests than can fit into memory.
	 */
	public HomePage()
	{
		WebMarkupContainer table = new DemoDatatable("table");
		add(table);

		Iterator<IRequestWrapper> itr = WuqispankApp.getRepo().getIterator();
		List<IRequestWrapper> rows = new ArrayList<IRequestWrapper>();
		
		while( itr.hasNext() ) 
			rows.add(itr.next());
		
		ListView<IRequestWrapper> lv = new ListView<IRequestWrapper>("rows", rows)
		{
			private static final long serialVersionUID = 23522777779L;

			@Override
			protected void populateItem(ListItem<IRequestWrapper> item)
			{
				IRequestWrapper request = item.getModelObject();
				item.add(RequestDetail.link("details", request, "foo"));
				item.add(new Label("col.agent.time", new PropertyModel(request,"agentDateTimeString" ) ));
				item.add(new Label("col.sql.statement.count", 	new PropertyModel(request,"sqlStatementCount" ) ));
				item.add(new Label("col.table.count", 	new PropertyModel(request,"tableCount" ) ));
				item.add(new Label("col.column.count", 	new PropertyModel(request,"columnCount" ) ));
				item.add(new Label("col.thread.id", 	new PropertyModel(request,"request.threadId" ) ));
				item.add(new Label("col.url", 	        new PropertyModel(request,"request.url" ) ));

			}
		};

		table.add(lv);
	}

}
