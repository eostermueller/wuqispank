package org.wicketstuff.datatables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

/** Wrapper class to create a JQuery table from Datatables.net
 * The package of this class must match the resource files in this jar:
 * 	./datatables-examples/target/datatables-examples-6.11.0/WEB-INF/lib/wicketstuff-datatables-6.11.0.jar 
 * @stolenFrom: https://github.com/wicketstuff/core/wiki/DataTables
 * @author erikostermueller
 *
 */
public class JQueryDatatable extends WebMarkupContainer
{

	private static final long serialVersionUID = -4387194295178034384L;

	public JQueryDatatable(String id)
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

		StringBuilder js = new StringBuilder();
		js.append("$(document).ready( function() {\n");
		//js.append("	debugger;");
		js.append("	var oTable = $('#" + getMarkupId() + "').dataTable( {\n");
		//js.append("		\"bJQueryUI\": true,\n");
		js.append( horizontalScrollConfiguration() );
		js.append( columnConfiguration() );
//		js.append("		\"sPaginationType\": \"full_numbers\"\n");
		js.append("	} );\n");
		js.append("var keys = new KeyTable( {");
		js.append("\"table\": document.getElementById('" + getMarkupId() + "'),");
		js.append("\"datatable\": oTable");
		js.append("} );");

		/* Event listener for zero-based column four:  4, null */
		/* Event listener for all columns:  null, null */
		js.append("	keys.event.focus( null, null, function( nNode, x, y) {");
		
		    
		js.append("    fnMessage( 'data:'+getText(nNode) );");
		js.append("	} );");				
		
		
		js.append("} );");
		
		/*
		  <pre> 
		  var keys = new KeyTable( {
					"table": document.getElementById('example'),
					"datatable": oTable
				} );
		  </pre>
		 */

		response.render(JavaScriptHeaderItem.forScript(js, getId() + "_datatables"));
	}

	
	/**
	 * Column Configuration
	 * "sClass": "center"
	 */
	private String columnConfiguration() {
		StringBuilder sb = new StringBuilder();
		sb.append("	\"aoColumns\": [");
		sb.append("	{ \"sWidth\": \"7.5%\" },  ");
		sb.append("	{ \"sWidth\": \"7.5%\" },  ");
		sb.append("	{ \"sWidth\": \"30%\" },  ");
		sb.append("	{ \"sWidth\": \"30%\" },  ");
		sb.append("	{ \"sWidth\": \"25%\" },  ");
		sb.append("	{ \"sWidth\": \"10%\" },  ");
		//sb.append( "null,");

//		sb.append("	{ \"sWidth\": \"10%\" },  ");
//		sb.append("	{ \"sWidth\": \"10%\" },  ");
//		sb.append("	{ \"sWidth\": \"30%\" },  ");
//		sb.append("	{ \"sWidth\": \"10%\" },  ");
//		sb.append("	{ \"sWidth\": \"10%\" },  ");
		
		sb.append("],");
//		sb.append("\"aoColumnDefs\": [ ");
//		sb.append("{ \"sClass\": \"center\", \"aTargets\": [ 0 ] },");
//		sb.append("{ \"sClass\": \"center\", \"aTargets\": [ 1 ] },");
		//sb.append("{ \"bSearchable\": true, \"bVisible\": false, \"aTargets\": [ 4 ] },");
		
//		sb.append("{ \"sClass\": \"center\", \"aTargets\": [ 5 ] } ], \n");

		return sb.toString();
		// ;
		//return "		\"aoColumnDefs\": [ { \"sClass\": \"center\", \"aTargets\": [ 0 ] },{ \"sClass\": \"center\", \"aTargets\": [ 1 ] }, { \"bSearchable\": true, \"bVisible\": false, \"aTargets\": [ 4 ] },{ \"sClass\": \"center\", \"aTargets\": [ 5 ] } ], \n";
	}
	
	/**
	 * http://datatables.net/examples/basic_init/scroll_x.html
	 * @return
	 */
	private String horizontalScrollConfiguration() {
		//return "	\"sScrollX\": \"100%\", \"sScrollXInner\": \"110%\", \"bScrollCollapse\": true,";
		return "";
	}

	private void renderDemoCSS(IHeaderResponse response)
	{
		/**
		 * Now using a wuqispank-customized version of demo_table_jui.css, which was pulled from wicketstuff-datatables-6.12.0.jar
		 */
		final Class<JQueryDatatable> _ = JQueryDatatable.class;
		//response.render(CssHeaderItem.forUrl("style.css", "screen"));
		response.render(CssHeaderItem.forUrl("css/demo_page.css", "screen"));
		response.render(CssHeaderItem.forUrl("css/demo_table_wuqispank.css", "screen"));
		
	}

	private String getJUITheme()
	{
		return "smoothness";
		//		return "ui-lightness";
	}

	private void renderBasicJS(IHeaderResponse response)
	{
		final Class<JQueryDatatable> _ = JQueryDatatable.class;
		
		//response.render( JavaScriptHeaderItem.forUrl("js/jquery.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/jquery-1.10.2.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/jquery.dataTables.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/KeyTable.js"));
		response.render( JavaScriptHeaderItem.forUrl("js/wuqispank.js"));
		
	}
}
