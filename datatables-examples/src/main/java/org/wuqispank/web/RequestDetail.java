package org.wuqispank.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.intrace.client.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.wicketstuff.datatables.DemoDatatable;
import org.wicketstuff.datatables.JQueryDatatable;
import org.wicketstuff.progressbar.ProgressBar;
//import org.wicketstuff.progressbar.examples.TaskServiceProgressExamplePage.DummyTask;
import org.wicketstuff.progressbar.spring.AsynchronousExecutor;
import org.wicketstuff.progressbar.spring.ITaskService;
import org.wicketstuff.progressbar.spring.Task;
import org.wicketstuff.progressbar.spring.TaskProgressionModel;
import org.wicketstuff.progressbar.spring.TaskService;
import org.wuqispank.DefaultFactory;
import org.wuqispank.IRequestExporter;
import org.wuqispank.WuqispankException;
import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.web.msgs.IMessages;
import org.wuqispank.web.tablecount.TableCountGraph;

public class RequestDetail extends AuthenticatedWebPage {
//	ProgressBar bar = null;
//	private final ITaskService taskService = new TaskService(new AsynchronousExecutor());
//	final TaskProgressionModel progressionModel = new TaskProgressionModel()
//	{
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		protected ITaskService getTaskService()
//		{
//			return getTaskService();
//		}
//	};
//	public ITaskService getTaskService() {
//		return taskService;
//	}
	private IRequestWrapper requestWrapper = null;
    private IRequestWrapper getRequestWrapper() {
		return requestWrapper;
	}
	private void setRequestWrapper(IRequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
	}

	private BookmarkableTabbedPanel2 m_pageTabbedPanel = null;

	private static final Logger LOG = LoggerFactory.getLogger(RequestDetail.class);
	private static final long serialVersionUID = 1L;
	private BookmarkableTabbedPanel2<ITab> m_tabbedPanel;
	private PageParameters m_localPageParameters;

	private BookmarkableTabbedPanel2<ITab> getTabbedPanel()
	{
		return m_tabbedPanel;
	}

	private void setTabbedPanel(BookmarkableTabbedPanel2<ITab> tabbedPanel)
	{
		m_tabbedPanel = tabbedPanel;
	}

	/**
	 * Constructor
	 * @throws WuqispankException 
	 */
	public RequestDetail(PageParameters parameters) 
	{

		setDefaultModel(new Model<String>("tabpanel"));
		setLocalPageParameters(parameters);

		StringValue uniqueIdOfSelectedRequest = getLocalPageParameters().get("id");
		if (uniqueIdOfSelectedRequest!=null) {
			LOG.debug("Looking for uniqueId [" + uniqueIdOfSelectedRequest + "]");
			IRequestWrapper r = getRequest(uniqueIdOfSelectedRequest.toString());
			if (r!= null) {
				setRequestWrapper(r);				
			} else {
				LOG.error("No request found with unique id [" + uniqueIdOfSelectedRequest + "]");
			}

		} else {
			LOG.error("Error.  Missing id parameter in [" + parameters.toString() + "]");
		}
		
		// create links used to switch between css variations
		//addCssSwitchingLinks();
		//add( new Label("sqlStatementCount", getModelRequest().getEvents().size()));

	     // Add a FeedbackPanel for displaying our messages
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);

        // Add a form with an onSubmit implementation that sets a message
        add(new Form("form") {
            protected void onSubmit() {
            	File exportedFile = null;
				try {
	            	IRequestExporter re = DefaultFactory.getFactory().getRequestExporter();
	            	IMessages msgs = DefaultFactory.getFactory().getMessages();
	            	exportedFile = File.createTempFile(
	            			"export-", 
	            			IRequestExporter.FILE_NAME_EXTENSION, 
	            			WuqispankApp.getConfig().getExportDir() );
	            	
	            	re.setOutputStream(new FileOutputStream(exportedFile));
					re.export(getRequestWrapper());
	                info(msgs.getExportSuccessfulMsg(exportedFile.getAbsolutePath()));
				} catch (Exception e) {
					LOG.error("Unable to export to file [" + exportedFile.getAbsolutePath() + "]");
					e.printStackTrace();
				}
            	
            }
        });
        
		Label label = new Label("sqlStatementCount", new PropertyModel(getRequestWrapper(), "sqlStatementCount"));
		
		add(label);
//		initProgressBar();

		// create a list of ITab objects used to feed the tabbed panel
		final List<ITab> tabs = new ArrayList<ITab>();

		tabs.add(new AbstractTab(new Model<String>("first tab"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new TabPanel1(panelId);
			}

		});

		tabs.add(new AbstractTab(new Model<String>("second tab"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new TabPanel2(panelId);
			}

		});

		tabs.add(new AbstractTab(new Model<String>("third tab"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new TabPanel3(panelId);
			}

		});

		setTabbedPanel(new BookmarkableTabbedPanel2<ITab>("tabs", tabs, getLocalPageParameters()));
		getTabbedPanel().add(AttributeModifier.replace("class", RequestDetail.this.getDefaultModel()));
		add(getTabbedPanel());
		// WicketHierarchyPrinter.print(this, true, true);
		
		
	}

//	private void initProgressBar() {
//		add(bar = new ProgressBar("bar", progressionModel)
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onFinished(AjaxRequestTarget target)
//			{
//				ITaskService taskService = getTaskService();
//				// finish the task!
//				taskService.finish(progressionModel.getTaskId());
//				// Hide progress bar after finish
//				setVisible(false);
//				// Add some JavaScript after finish
//				target.appendJavaScript("alert('Task done and finished!')");
//
//				// re-enable button
//				//Component button = form.get("submit");
//				//button.setEnabled(true);
//				//target.add(button);
//			}
//		});
//		// Hide progress bar initially
//		bar.setVisible(false);
//		
//	}
	private void setLocalPageParameters(PageParameters parameters)
	{
		m_localPageParameters = parameters;
	}

	private PageParameters getLocalPageParameters()
	{
		if (getPageParameters().getAllNamed().size() > 0)
		{
			return getPageParameters();
		}
		else
		{
			return m_localPageParameters;
		}
	}

	/**
	 * Creates an external page link
	 * 
	 * @param name
	 * @param book
	 *            The name of the link component to create The book to link to
	 * @param noBookTitle
	 *            The title to show if book is null
	 * @return The external page link
	 */
	public static BookmarkablePageLink<Void> link(final String name, final IRequestWrapper request,
		final String noBookTitle)
	{
		final BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(name,
			RequestDetail.class);

		if (request != null)
		{
			link.getPageParameters().add("id", request.getRequest().getUniqueId());
			//link.add(new Label("title", new Model<IRequestWrapper>(request)));
			link.add(new Label("title", request.getTinyId()) );
			LOG.debug("In static method, added book model and id [" + request.getRequest().getUniqueId() + "]");
		}
		else
		{
			link.add(new Label("title", noBookTitle));
			link.setEnabled(false);
		}

		return link;
	}

	static IRequestWrapper getRequest(String id)
	{
		return WuqispankApp.getRepo().get(id);
		//return BackgroundSqlCollector.getRequest(id);
//		User user = getSession().getUser();
//		IRequest rc = null;
//		for (IRequest book : user.getBooks())
//		{
//			if (id.equals(book.getId()))
//			{
//				rc = book;
//			}
//		}
//		return rc;

	}


	private void addCssSwitchingLinks()
	{
		add(new CssSwitchingLink("var0", "tabpanel"));
		add(new CssSwitchingLink("var1", "tabpanel1"));
		add(new CssSwitchingLink("var2", "tabpanel2"));
		add(new CssSwitchingLink("var3", "tabpanel3"));
		add(new CssSwitchingLink("var4", "tabpanel4"));
	}

	protected class CssSwitchingLink extends Link<Void>
	{
		private static final long serialVersionUID = 1L;

		private final String clazz;

		/**
		 * @param id
		 * @param clazz
		 */
		public CssSwitchingLink(String id, String clazz)
		{
			super(id);
			this.clazz = clazz;
		}

		/**
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick()
		{
			RequestDetail.this.setDefaultModelObject(clazz);
		}

		/**
		 * @see org.apache.wicket.markup.html.link.Link#isEnabled()
		 */
		@Override
		public boolean isEnabled()
		{
			return !RequestDetail.this.getDefaultModelObjectAsString().equals(clazz);
		}

	}

	/**
	 * Panel representing the content panel for the first tab
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private class TabPanel1 extends Panel
	{

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param id
		 *            component id
		 */
		public TabPanel1(String id)
		{
			super(id);
			
			if (getRequestWrapper()==null) {
				String error ="Error in TabPanel1 ctor for id [" + id + "], and no request found."; 
				LOG.error(error);
				throw new RuntimeException(error);
			} 
			
			IRequest request = getRequestWrapper().getRequest();
			if (request ==null) {
				LOG.error("Error.  In TabPanel1 ctor for id [" + id + "], and no request found.");
			} else {
				LOG.debug("Found unique id in tab panel 1 [" + request.getUniqueId() + "]");
				WebMarkupContainer table = new JQueryDatatable("sqlTable");
				//WebMarkupContainer table = new DemoDatatable("sqlTable");
				
				add(table);

				List<ISqlWrapper> rows = getRequestWrapper().getSql();
				ListView<ISqlWrapper> lv = new ListView<ISqlWrapper>("sqlRows", rows)
				{
					private static final long serialVersionUID = 23522777779L;

					@Override
					protected void populateItem(ListItem<ISqlWrapper> item)
					{
						ISqlWrapper sql = item.getModelObject();
						item.add(new Label("col.table.count", 	new PropertyModel(sql,"sqlModel.tableCount" ) ));
						item.add(new Label("col.field.count", 	new PropertyModel(sql,"sqlModel.columnCount" ) ));
						item.add(new Label("col.table.names", 	new PropertyModel(sql,"sqlModel.humanReadableTableNames" ) ));
						item.add(new Label("col.sql.text",    	new PropertyModel(sql,"sqlText") ));
						item.add(new Label("col.stack.trace",   new PropertyModel(sql,"stackTrace") ));
						item.add(new Label("col.sql.sequence",  new PropertyModel(sql,"sequence") ));
					}
				};
				table.add(lv);
			}

		}

		private void initPage()
		{
//			add(new Label("title", getBook().getTitle()));
//			add(new Label("author", getBook().getAuthor()));
//			add(new Label("fiction", Boolean.toString(getBook().getFiction())));

		}

	}
	private static class DummyTask extends Task
	{
		private final int iterations;

		public DummyTask(int iterations)
		{
			this.iterations = iterations;
		}

		@Override
		protected void run()
		{
			for (int i = 0; i < iterations; i++)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
				updateProgress(i, iterations);
				if (isCancelled())
					return;
			}
		}
	}

	/**
	 * Panel representing the content panel for the second tab
	 * 
	 * @author Erik Ostermueller
	 * 
	 */
	private class TabPanel2 extends Panel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param id
		 *            component id
		 */
		public TabPanel2(String id)
		{
			super(id);
			
			if (getRequestWrapper()==null) {
				String error ="Error in TabPanel1 ctor for id [" + id + "], and no request found."; 
				LOG.error(error);
				throw new RuntimeException(error);
			} 
			
			IRequest request = getRequestWrapper().getRequest();
			if (request ==null) {
				LOG.error("Error.  In TabPanel1 ctor for id [" + id + "], and no request found.");
			} else {
				LOG.debug("Found unique id in tab panel 1 [" + request.getUniqueId() + "]");
				WebMarkupContainer tableCountGraph = new TableCountGraph("tableCountGraph", getRequestWrapper());
				
				
				//WebMarkupContainer table = new DemoDatatable("sqlTable");
				
				add(tableCountGraph);

//				List<ISqlWrapper> rows = getRequestWrapper().getSql();
//				ListView<ISqlWrapper> lv = new ListView<ISqlWrapper>("sqlRows", rows)
//				{
//					private static final long serialVersionUID = 23522777779L;
//
//					@Override
//					protected void populateItem(ListItem<ISqlWrapper> item)
//					{
//						ISqlWrapper sql = item.getModelObject();
//						item.add(new Label("col.table.count", 	new PropertyModel(sql,"sqlModel.tableCount" ) ));
//						item.add(new Label("col.field.count", 	new PropertyModel(sql,"sqlModel.columnCount" ) ));
//						item.add(new Label("col.table.names", 	new PropertyModel(sql,"sqlModel.tableNames" ) ));
//						item.add(new Label("col.sql.text",    	new PropertyModel(sql,"sqlText") ));
//						item.add(new Label("col.stack.trace",   new PropertyModel(sql,"stackTrace") ));
//						item.add(new Label("col.sql.sequence",  new PropertyModel(sql,"sequence") ));
//					}
//				};
//				tableCountGraph.add(lv);
			}

			
		}

	}

	/**
	 * Panel representing the content panel for the third tab
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private class TabPanel3 extends Panel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param id
		 *            component id
		 */
		public TabPanel3(String id)
		{
			super(id);

			LOG.debug("Local id [" + id + "] super id [" + getId() + "] " + "pageParams[" +
				getPageParameters().toString() + "]");
			String uniqueId = getPageParameters().get("id").toString();
			if (uniqueId != null && !uniqueId.trim().equals(""))
			{
				//setModelRequest(RequestDetail.getRequest(uniqueId) );
				initPage();
			}
			else
			{
				LOG.error("Book [" + uniqueId + "] not found");
			}

		}

		private void initPage()
		{
//			add(new Label("title", getRequest().getTitle()));
//			add(new Label("author", getRequest().getAuthor()));
//			add(new Label("fiction", Boolean.toString(getRequest().getFiction())));

		}

	}

}