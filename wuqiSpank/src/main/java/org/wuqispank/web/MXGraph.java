package org.wuqispank.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.wicketstuff.datatables.JQueryDatatable;

public class MXGraph extends WebMarkupContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -806456499827479511L;
	public MXGraph(String id)
	{
		super(id);
		setOutputMarkupId(true);

		add(new AttributeModifier("class", Model.of("display")));
	}
	@Override
	public void renderHead(IHeaderResponse response)
	{
		renderDemoCSS(response);
		renderBasicJS(response);
		//window.onload = function () {var foo=\"" + bar + "\"}"
		StringBuilder js = new StringBuilder();
		js.append("mxBasePath = \"../../mxGraph-2_4_0_4\";");
		response.render(JavaScriptHeaderItem.forScript(js, getId() + "_mxGraph1"));

		js = new StringBuilder();
		//js.append("");
		//response.re
		response.render(OnLoadHeaderItem.forScript("main(document.getElementById(\"graphContainer\"));"));
	}
	private void renderDemoCSS(IHeaderResponse response)
	{
//		response.render(CssHeaderItem.forUrl("css/demo_page.css", "screen"));
//		response.render(CssHeaderItem.forUrl("css/demo_table_wuqispank.css", "screen"));
		
	}
	private void renderBasicJS(IHeaderResponse response)
	{
		final Class<JQueryDatatable> _ = JQueryDatatable.class;
		
		response.render( JavaScriptHeaderItem.forUrl("mxGraph-2_4_0_4/js/mxClient.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/graph.js"));
	}
	

}
