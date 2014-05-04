/**
  *  Stole the following code from here:
  *  http://forum.jgraph.com/questions/3617/test-automation-support-for-selenium-need-identifier-in-svg-element-id-or-name
  */
	function addSvgIdForSelenium() {
		var mxCellRendererInitializeShape = mxCellRenderer.prototype.initializeShape;
		mxCellRenderer.prototype.initializeShape = function(state)
		{
		  mxCellRendererInitializeShape.apply(this, arguments);
		
		  if (state.shape.node != null && state.cell.id != null)
		  {
		    state.shape.node.setAttribute('id', 'shape-' + state.cell.id);
		  }
		};
		
		var mxCellRendererInitializeLabel = mxCellRenderer.prototype.initializeLabel;
		mxCellRenderer.prototype.initializeLabel = function(state)
		{
		  mxCellRendererInitializeLabel.apply(this, arguments);
		
		  if (state.text.node != null && state.cell.id != null)
		  {
		    state.text.node.setAttribute('id', 'label-' + state.cell.id);
		  }
		};	
	}

	function swimlaneGraphLoader(container)
	{
		debugger;
		addSvgIdForSelenium();
		// Checks if browser is supported
		if (!mxClient.isBrowserSupported())
		{
			// Displays an error message if the browser is
			// not supported.
			mxUtils.error('Browser is not supported!', 200, false);
		}
		else
		{
			// Creates a wrapper editor around a new graph inside
			// the given container using an XML config for the
			// keyboard bindings
			var config = mxUtils.load(
				'/wuqispank/mxGraph-2_4_0_4/editors/config/keyhandler-commons.xml').
					getDocumentElement();
			var editor = new mxEditor(config);
			editor.setGraphContainer(container);
			var graph = editor.graph;
			var model = graph.getModel();

			// Auto-resizes the container
			graph.border = 80;
			graph.getView().translate = new mxPoint(graph.border/2, graph.border/2);
			graph.setResizeContainer(true);
			graph.graphHandler.setRemoveCellsFromParent(false);

			// Changes the default vertex style in-place
			var style = graph.getStylesheet().getDefaultVertexStyle();
			var labelStyle = mxUtils.clone(style);
			labelStyle[mxConstants.STYLE_ROTATION] = 0;
			labelStyle[mxConstants.STYLE_ROTATABLE] = 0;
			graph.getStylesheet().putCellStyle('tableLabel', labelStyle);

			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
			style[mxConstants.STYLE_FONTSIZE] = 11;
			style[mxConstants.STYLE_STARTSIZE] = 22;
			style[mxConstants.STYLE_HORIZONTAL] = false;
			style[mxConstants.STYLE_FONTCOLOR] = 'black';
			style[mxConstants.STYLE_STROKECOLOR] = 'black';
			delete style[mxConstants.STYLE_FILLCOLOR];

			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
			style[mxConstants.STYLE_FONTSIZE] = 10;
			style[mxConstants.STYLE_ROUNDED] = true;
			style[mxConstants.STYLE_HORIZONTAL] = true;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			delete style[mxConstants.STYLE_STARTSIZE];
			style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'none';
			graph.getStylesheet().putCellStyle('process', style);
			
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
			style[mxConstants.STYLE_FONTSIZE] = 10;
			delete style[mxConstants.STYLE_ROUNDED];
			style[mxConstants.STYLE_VERTICAL_LABEL_POSITION] = mxConstants.ALIGN_TOP;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_BOTTOM;
			//ETO
			//style[mxConstants.STYLE_ROTATION] = 0;
			//delete style[mxConstants.STYLE_VERTICAL_ALIGN];

			graph.getStylesheet().putCellStyle('state', style);
											
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'top';
			style[mxConstants.STYLE_SPACING_TOP] = 40;
			style[mxConstants.STYLE_SPACING_RIGHT] = 64;
			graph.getStylesheet().putCellStyle('condition', style);
							
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_DOUBLE_ELLIPSE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
			style[mxConstants.STYLE_SPACING_TOP] = 28;
			style[mxConstants.STYLE_FONTSIZE] = 14;
			style[mxConstants.STYLE_FONTSTYLE] = 1;
			delete style[mxConstants.STYLE_SPACING_RIGHT];
			graph.getStylesheet().putCellStyle('end', style);
			
			style = graph.getStylesheet().getDefaultEdgeStyle();
			style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
			style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_BLOCK;
			style[mxConstants.STYLE_ROUNDED] = true;
			style[mxConstants.STYLE_FONTCOLOR] = 'black';
			style[mxConstants.STYLE_STROKECOLOR] = 'black';
			
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_DASHED] = true;
			style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_OPEN;
			style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_OVAL;
			graph.getStylesheet().putCellStyle('crossover', style);
					

			// Installs double click on middle control point and
			// changes style of edges between empty and this value
			graph.alternateEdgeStyle = 'elbow=vertical';

				// Adds an option to view the XML of the graph
				
				document.body.appendChild(mxUtils.button('View XML', function()
				{
					var encoder = new mxCodec();
					var node = encoder.encode(graph.getModel());
					mxUtils.popup(mxUtils.getPrettyXml(node), true);
				}));


			// Adds automatic layout and various switches if the
			// graph is enabled
			if (graph.isEnabled())
			{
				// Allows new connections but no dangling edges
				graph.setConnectable(true);
				graph.setAllowDanglingEdges(false);
				
				// End-states are no valid sources
				var previousIsValidSource = graph.isValidSource;
				
				graph.isValidSource = function(cell)
				{
					if (previousIsValidSource.apply(this, arguments))
					{
						var style = this.getModel().getStyle(cell);
						
						return style == null || !(style == 'end' || style.indexOf('end') == 0);
					}

					return false;
				};
				
				// Start-states are no valid targets, we do not
				// perform a call to the superclass function because
				// this would call isValidSource
				// Note: All states are start states in
				// the example below, so we use the state
				// style below
				graph.isValidTarget = function(cell)
				{
					var style = this.getModel().getStyle(cell);
					
					return !this.getModel().isEdge(cell) && !this.isSwimlane(cell) &&
						(style == null || !(style == 'state' || style.indexOf('state') == 0));
				};
				
				// Allows dropping cells into new lanes and
				// lanes into new pools, but disallows dropping
				// cells on edges to split edges
				graph.setDropEnabled(true);
				graph.setSplitEnabled(false);
				
				// Returns true for valid drop operations
				graph.isValidDropTarget = function(target, cells, evt)
				{
					if (this.isSplitEnabled() && this.isSplitTarget(target, cells, evt))
					{
						return true;
					}
					
					var model = this.getModel();
					var lane = false;
					var pool = false;
					var cell = false;
					
					// Checks if any lanes or pools are selected
					for (var i = 0; i < cells.length; i++)
					{
						var tmp = model.getParent(cells[i]);
						lane = lane || this.isPool(tmp);
						pool = pool || this.isPool(cells[i]);
						
						cell = cell || !(lane || pool);
					}
					
					return !pool && cell != lane && ((lane && this.isPool(target)) ||
						(cell && this.isPool(model.getParent(target))));
				};
				
				// Adds new method for identifying a pool
				graph.isPool = function(cell)
				{
					var model = this.getModel();
					var parent = model.getParent(cell);
				
					return parent != null && model.getParent(parent) == model.getRoot();
				};
				
				// Changes swimlane orientation while collapsed
				graph.model.getStyle = function(cell)
				{
					var style = mxGraphModel.prototype.getStyle.apply(this, arguments);
				
					if (graph.isCellCollapsed(cell))
					{
						if (style != null)
						{
							style += ';';
						}
						else
						{
							style = '';
						}
						
						style += 'horizontal=1;align=left;spacingLeft=14;';
					}
					
					return style;
				};

				// Keeps widths on collapse/expand					
				var foldingHandler = function(sender, evt)
				{
					var cells = evt.getProperty('cells');
					
					for (var i = 0; i < cells.length; i++)
					{
						var geo = graph.model.getGeometry(cells[i]);

						if (geo.alternateBounds != null)
						{
							geo.width = geo.alternateBounds.width;
						}
					}
				};

				graph.addListener(mxEvent.FOLD_CELLS, foldingHandler);
			}
			
			// Applies size changes to siblings and parents
			new mxSwimlaneManager(graph);

			// Creates a stack depending on the orientation of the swimlane
			var layout = new mxStackLayout(graph, false);
			
			// Makes sure all children fit into the parent swimlane
			layout.resizeParent = true;
						
			// Applies the size to children if parent size changes
			layout.fill = true;

			// Only update the size of swimlanes
			layout.isVertexIgnored = function(vertex)
			{
				return !graph.isSwimlane(vertex);
			}
			
			// Keeps the lanes and pools stacked
			var layoutMgr = new mxLayoutManager(graph);

			layoutMgr.getLayout = function(cell)
			{
				if (!model.isEdge(cell) && graph.getModel().getChildCount(cell) > 0 &&
					(model.getParent(cell) == model.getRoot() || graph.isPool(cell)))
				{
					layout.fill = graph.isPool(cell);
					
					return layout;
				}
				
				return null;
			};
			
			// Gets the default parent for inserting new cells. This
			// is normally the first child of the root (ie. layer 0).
			var parent = graph.getDefaultParent();
			loadSwimlaneXml(container);

			// // Adds cells to the model in a single step
			// model.beginUpdate();
			// try
			// {
			// 	var poolTablesHeader = graph.insertVertex(parent, null, 'Tables (header)', 0, 0, 640, 0);
			// 	poolTablesHeader.setConnectable(false);

			// 	var laneTablesHeader = graph.insertVertex(poolTablesHeader, null, null, 0, 0, 640, 110);
			// 	laneTablesHeader.setConnectable(false);

			// 	var poolSql = graph.insertVertex(parent, null, 'SQL', 0, 0, 640, 0);
			// 	poolSql.setConnectable(false);

			// 	var laneSql = graph.insertVertex(poolSql, null, null, 0, 0, 640, 140);
			// 	laneSql.setConnectable(false);

			// 	var poolTablesFooter = graph.insertVertex(parent, null, 'Tables (footer)', 0, 0, 640, 0);
			// 	poolTablesFooter.setConnectable(false);

			// 	var laneTablesFooter = graph.insertVertex(poolTablesFooter, null, null, 0, 0, 640, 110);
			// 	laneTablesFooter.setConnectable(false);
				
			// 	var tableOneHeader = graph.insertVertex(laneTablesHeader, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'state');
			// 	tableOneHeader.setConnectable(false);
			// 	//STYLE_LABEL_POSITION -- for horizontal labels
			// 	//var label21 = graph.insertVertex(tableOneHeader, null, 'CUST_CUST_REL', 0.5, 1, 0, 0, 'tabelLabel', true);
			// 	//label21.setConnectable(false);
				
			// 	var tableOneFooter = graph.insertVertex(laneTablesFooter, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'state');
			// 	//var label22 = graph.insertVertex(tableOneFooter, null, 'CUST_CUST_REL', 0.5, 1, 0, 0, 'tableLabel', true);

			// 	tableOneFooter.setConnectable(false);
				
			// 	graph.insertEdge(parent, null, null, tableOneHeader, tableOneFooter, "endArrow=none;");
				

			// }
			// finally
			// {
			// 	// Updates the display
			// 	model.endUpdate();
			// }
		}
	}

	/**  swimlaneSelectionEnabled
		This shows an example of how to set it:
	  *		https://code.google.com/p/v2my/source/browse/trunk/jsi/libs/com/mxgraph/v1_0/view/mxGraph.js?r=91
	  */
	function loadSwimlaneXml() {

        if (mxClient.isBrowserSupported())
        {
                var divs = document.getElementsByTagName('*');

                for (var i = 0; i < divs.length; i++)
                {
                        if (divs[i].className.toString().indexOf('mxgraph') >= 0)
                        {
                                (function(container)
                                {
                                        debugger;
                                        var xml = mxUtils.getTextContent(container);
                                        var xmlDocument = mxUtils.parseXml(xml);

                                        if (xmlDocument.documentElement != null && xmlDocument.documentElement.nodeName == 'mxGraphModel')
                                        {
                                                var decoder = new mxCodec(xmlDocument);
                                                var node = xmlDocument.documentElement;

                                                container.innerHTML = '';

                                                var graph = new mxGraph(container);
                                                graph.centerZoom = false;
                                                graph.setTooltips(false);
                                                graph.setEnabled(false);

                                                // Changes the default style for edges "in-place"
                                                var style = graph.getStylesheet().getDefaultEdgeStyle();
                                                style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;

                                                // Enables panning with left mouse button
                                                graph.panningHandler.useLeftButtonForPanning = true;
                                                graph.panningHandler.ignoreCell = true;
                                                graph.container.style.cursor = 'move';
                                                graph.setPanning(true);

                                                if (divs[i].style.width == '' && divs[i].style.height == '')
                                                {
                                                        graph.resizeContainer = true;
                                                }
                                                 else
                                                {
                                                        // Adds border for fixed size boxes
                                                        graph.border = 20;
                                                }

                                                decoder.decode(node, graph.getModel());
                                                graph.resizeContainer = false;

                                        }
                                })(divs[i]);
                        }
                }
        }

	}
	
	function createWuqispankMXGraphFrame(container) 
	{
		addSvgIdForSelenium();
		// Checks if browser is supported
		if (!mxClient.isBrowserSupported())
		{
			// Displays an error message if the browser is
			// not supported.
			mxUtils.error('Browser is not supported!', 200, false);
		}
		else
		{
			// Creates a wrapper editor around a new graph inside
			// the given container using an XML config for the
			// keyboard bindings
			var config = mxUtils.load(
				'editors/config/keyhandler-commons.xml').
					getDocumentElement();
			var editor = new mxEditor(config);
			editor.setGraphContainer(container);
			var graph = editor.graph;
			var model = graph.getModel();

			// Auto-resizes the container
			graph.border = 80;
			graph.getView().translate = new mxPoint(graph.border/2, graph.border/2);
			graph.setResizeContainer(true);
			graph.graphHandler.setRemoveCellsFromParent(false);

			// Changes the default vertex style in-place
			var style = graph.getStylesheet().getDefaultVertexStyle();
			var labelStyle = mxUtils.clone(style);
			labelStyle[mxConstants.STYLE_ROTATION] = 0;
			labelStyle[mxConstants.STYLE_ROTATABLE] = 0;
			graph.getStylesheet().putCellStyle('tableLabel', labelStyle);

			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
			style[mxConstants.STYLE_FONTSIZE] = 11;
			style[mxConstants.STYLE_STARTSIZE] = 22;
			style[mxConstants.STYLE_HORIZONTAL] = false;
			style[mxConstants.STYLE_FONTCOLOR] = 'black';
			style[mxConstants.STYLE_STROKECOLOR] = 'black';
			delete style[mxConstants.STYLE_FILLCOLOR];

			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
			style[mxConstants.STYLE_FONTSIZE] = 10;
			style[mxConstants.STYLE_ROUNDED] = true;
			style[mxConstants.STYLE_HORIZONTAL] = true;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			delete style[mxConstants.STYLE_STARTSIZE];
			style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'none';
			graph.getStylesheet().putCellStyle('process', style);
			
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
			style[mxConstants.STYLE_FONTSIZE] = 10;
			delete style[mxConstants.STYLE_ROUNDED];
			style[mxConstants.STYLE_VERTICAL_LABEL_POSITION] = mxConstants.ALIGN_TOP;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_BOTTOM;
			//ETO
			//style[mxConstants.STYLE_ROTATION] = 0;
			//delete style[mxConstants.STYLE_VERTICAL_ALIGN];

			graph.getStylesheet().putCellStyle('state', style);
											
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'top';
			style[mxConstants.STYLE_SPACING_TOP] = 40;
			style[mxConstants.STYLE_SPACING_RIGHT] = 64;
			graph.getStylesheet().putCellStyle('condition', style);
							
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_DOUBLE_ELLIPSE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
			style[mxConstants.STYLE_SPACING_TOP] = 28;
			style[mxConstants.STYLE_FONTSIZE] = 14;
			style[mxConstants.STYLE_FONTSTYLE] = 1;
			delete style[mxConstants.STYLE_SPACING_RIGHT];
			graph.getStylesheet().putCellStyle('end', style);
			
			style = graph.getStylesheet().getDefaultEdgeStyle();
			style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
			style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_BLOCK;
			style[mxConstants.STYLE_ROUNDED] = true;
			style[mxConstants.STYLE_FONTCOLOR] = 'black';
			style[mxConstants.STYLE_STROKECOLOR] = 'black';
			
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_DASHED] = true;
			style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_OPEN;
			style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_OVAL;
			graph.getStylesheet().putCellStyle('crossover', style);
					

			// Installs double click on middle control point and
			// changes style of edges between empty and this value
			graph.alternateEdgeStyle = 'elbow=vertical';

				// Adds an option to view the XML of the graph
				
				document.body.appendChild(mxUtils.button('View XML', function()
				{
					var encoder = new mxCodec();
					var node = encoder.encode(graph.getModel());
					mxUtils.popup(mxUtils.getPrettyXml(node), true);
				}));


			// Adds automatic layout and various switches if the
			// graph is enabled
			if (graph.isEnabled())
			{
				// Allows new connections but no dangling edges
				graph.setConnectable(true);
				graph.setAllowDanglingEdges(false);
				
				// End-states are no valid sources
				var previousIsValidSource = graph.isValidSource;
				
				graph.isValidSource = function(cell)
				{
					if (previousIsValidSource.apply(this, arguments))
					{
						var style = this.getModel().getStyle(cell);
						
						return style == null || !(style == 'end' || style.indexOf('end') == 0);
					}

					return false;
				};
				
				// Start-states are no valid targets, we do not
				// perform a call to the superclass function because
				// this would call isValidSource
				// Note: All states are start states in
				// the example below, so we use the state
				// style below
				graph.isValidTarget = function(cell)
				{
					var style = this.getModel().getStyle(cell);
					
					return !this.getModel().isEdge(cell) && !this.isSwimlane(cell) &&
						(style == null || !(style == 'state' || style.indexOf('state') == 0));
				};
				
				// Allows dropping cells into new lanes and
				// lanes into new pools, but disallows dropping
				// cells on edges to split edges
				graph.setDropEnabled(true);
				graph.setSplitEnabled(false);
				
				// Returns true for valid drop operations
				graph.isValidDropTarget = function(target, cells, evt)
				{
					if (this.isSplitEnabled() && this.isSplitTarget(target, cells, evt))
					{
						return true;
					}
					
					var model = this.getModel();
					var lane = false;
					var pool = false;
					var cell = false;
					
					// Checks if any lanes or pools are selected
					for (var i = 0; i < cells.length; i++)
					{
						var tmp = model.getParent(cells[i]);
						lane = lane || this.isPool(tmp);
						pool = pool || this.isPool(cells[i]);
						
						cell = cell || !(lane || pool);
					}
					
					return !pool && cell != lane && ((lane && this.isPool(target)) ||
						(cell && this.isPool(model.getParent(target))));
				};
				
				// Adds new method for identifying a pool
				graph.isPool = function(cell)
				{
					var model = this.getModel();
					var parent = model.getParent(cell);
				
					return parent != null && model.getParent(parent) == model.getRoot();
				};
				
				// Changes swimlane orientation while collapsed
				graph.model.getStyle = function(cell)
				{
					var style = mxGraphModel.prototype.getStyle.apply(this, arguments);
				
					if (graph.isCellCollapsed(cell))
					{
						if (style != null)
						{
							style += ';';
						}
						else
						{
							style = '';
						}
						
						style += 'horizontal=1;align=left;spacingLeft=14;';
					}
					
					return style;
				};

				// Keeps widths on collapse/expand					
				var foldingHandler = function(sender, evt)
				{
					var cells = evt.getProperty('cells');
					
					for (var i = 0; i < cells.length; i++)
					{
						var geo = graph.model.getGeometry(cells[i]);

						if (geo.alternateBounds != null)
						{
							geo.width = geo.alternateBounds.width;
						}
					}
				};

				graph.addListener(mxEvent.FOLD_CELLS, foldingHandler);
			}
			
			// Applies size changes to siblings and parents
			new mxSwimlaneManager(graph);

			// Creates a stack depending on the orientation of the swimlane
			var layout = new mxStackLayout(graph, false);
			
			// Makes sure all children fit into the parent swimlane
			layout.resizeParent = true;
						
			// Applies the size to children if parent size changes
			layout.fill = true;

			// Only update the size of swimlanes
			layout.isVertexIgnored = function(vertex)
			{
				return !graph.isSwimlane(vertex);
			}
			
			// Keeps the lanes and pools stacked
			var layoutMgr = new mxLayoutManager(graph);

			layoutMgr.getLayout = function(cell)
			{
				if (!model.isEdge(cell) && graph.getModel().getChildCount(cell) > 0 &&
					(model.getParent(cell) == model.getRoot() || graph.isPool(cell)))
				{
					layout.fill = graph.isPool(cell);
					
					return layout;
				}
				
				return null;
			};
			
			// Gets the default parent for inserting new cells. This
			// is normally the first child of the root (ie. layer 0).
			var parent = graph.getDefaultParent();

			// Adds cells to the model in a single step
			model.beginUpdate();
			try
			{
				var poolTablesHeader = graph.insertVertex(parent, null, 'Tables (header)', 0, 0, 640, 0);
				poolTablesHeader.setConnectable(false);

				var laneTablesHeader = graph.insertVertex(poolTablesHeader, null, null, 0, 0, 640, 110);
				laneTablesHeader.setConnectable(false);

				var poolSql = graph.insertVertex(parent, null, 'SQL', 0, 0, 640, 0);
				poolSql.setConnectable(false);

				var laneSql = graph.insertVertex(poolSql, null, null, 0, 0, 640, 140);
				laneSql.setConnectable(false);

				var poolTablesFooter = graph.insertVertex(parent, null, 'Tables (footer)', 0, 0, 640, 0);
				poolTablesFooter.setConnectable(false);

				var laneTablesFooter = graph.insertVertex(poolTablesFooter, null, null, 0, 0, 640, 110);
				laneTablesFooter.setConnectable(false);
				
				var tableOneHeader = graph.insertVertex(laneTablesHeader, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'state');
				tableOneHeader.setConnectable(false);
				//STYLE_LABEL_POSITION -- for horizontal labels
				//var label21 = graph.insertVertex(tableOneHeader, null, 'CUST_CUST_REL', 0.5, 1, 0, 0, 'tabelLabel', true);
				//label21.setConnectable(false);
				
				var tableOneFooter = graph.insertVertex(laneTablesFooter, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'state');
				//var label22 = graph.insertVertex(tableOneFooter, null, 'CUST_CUST_REL', 0.5, 1, 0, 0, 'tableLabel', true);

				tableOneFooter.setConnectable(false);
				
				graph.insertEdge(parent, null, null, tableOneHeader, tableOneFooter, "endArrow=none;");
				

			}
			finally
			{
				// Updates the display
				model.endUpdate();
			}
		}
	}
	
/*  Start of test loader
 *
 */
                // Program starts here. Creates a sample graph in the
                // DOM node with the specified ID. This function is invoked
                // from the onLoad event handler of the document (see below).
                function graphLoader(container)
                {
                		addSvgIdForSelenium();
                        debugger;
                        if (mxClient.isBrowserSupported())
                        {
                                var divs = document.getElementsByTagName('*');

                                for (var i = 0; i < divs.length; i++)
                                {
                                        if (divs[i].className.toString().indexOf('mxgraph') >= 0)
                                        {
                                                (function(container)
                                                {
                                                        debugger;
                                                        var xml = mxUtils.getTextContent(container);
                                                        var xmlDocument = mxUtils.parseXml(xml);

                                                        if (xmlDocument.documentElement != null && xmlDocument.documentElement.nodeName == 'mxGraphModel')
                                                        {
                                                                var decoder = new mxCodec(xmlDocument);
                                                                var node = xmlDocument.documentElement;

                                                                container.innerHTML = '';

                                                                var graph = new mxGraph(container);
                                                                graph.centerZoom = false;
                                                                graph.setTooltips(false);
                                                                graph.setEnabled(false);

                                                                // Changes the default style for edges "in-place"
                                                                var style = graph.getStylesheet().getDefaultEdgeStyle();
                                                                style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;

                                                                // Enables panning with left mouse button
                                                                graph.panningHandler.useLeftButtonForPanning = true;
                                                                graph.panningHandler.ignoreCell = true;
                                                                graph.container.style.cursor = 'move';
                                                                graph.setPanning(true);

                                                                if (divs[i].style.width == '' && divs[i].style.height == '')
                                                                {
                                                                        graph.resizeContainer = true;
                                                                }
                                                                 else
                                                                {
                                                                        // Adds border for fixed size boxes
                                                                        graph.border = 20;
                                                                }

                                                                decoder.decode(node, graph.getModel());
                                                                graph.resizeContainer = false;

                                                                // Adds zoom buttons in top, left corner
                                                                var buttons = document.createElement('div');
                                                                buttons.style.position = 'absolute';
                                                                buttons.style.overflow = 'visible';

                                                                var bs = graph.getBorderSizes();
                                                                buttons.style.top = (container.offsetTop + bs.y) + 'px';
                                                                buttons.style.left = (container.offsetLeft + bs.x) + 'px';

                                                                var left = 0;
                                                                var bw = 16;
                                                                var bh = 16;

                                                                if (mxClient.IS_QUIRKS)
                                                                {
                                                                        bw -= 1;
                                                                        bh -= 1;
                                                                }

                                                                function addButton(label, funct)
                                                                {
                                                                        var btn = document.createElement('div');
                                                                        mxUtils.write(btn, label);
                                                                        btn.style.position = 'absolute';
                                                                        btn.style.backgroundColor = 'transparent';
                                                                        btn.style.border = '1px solid gray';
                                                                        btn.style.textAlign = 'center';
                                                                        btn.style.fontSize = '10px';
                                                                        btn.style.cursor = 'hand';
                                                                        btn.style.width = bw + 'px';
                                                                        btn.style.height = bh + 'px';
                                                                        btn.style.left = left + 'px';
                                                                        btn.style.top = '0px';

                                                                        mxEvent.addListener(btn, 'click', function(evt)
                                                                        {
                                                                                funct();
                                                                                mxEvent.consume(evt);
                                                                       });

                                                                        left += bw;

                                                                        buttons.appendChild(btn);
                                                                };

                                                                addButton('+', function()
                                                                {
                                                                        graph.zoomIn();
                                                                });

                                                                addButton('-', function()
                                                                {
                                                                        graph.zoomOut();
                                                                });

                                                                if (container.nextSibling != null)
                                                                {
                                                                        container.parentNode.insertBefore(buttons, container.nextSibling);
                                                                }
                                                                else
                                                                {
                                                                        container.appendChild(buttons);
                                                                }
                                                        }
                                                })(divs[i]);
                                        }
                                }
                        }
                };
                                                                                
/*  End of test loader
 *
 */
		// Program starts here. Creates a sample graph in the
		// DOM node with the specified ID. This function is invoked
		// from the onLoad event handler of the document (see below).
		function main(container)
		{
			debugger;
			// Checks if the browser is supported
			if (!mxClient.isBrowserSupported())
			{
				// Displays an error message if the browser is not supported.
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				// Note that these XML nodes will be enclosing the
				// mxCell nodes for the model cells in the output
				var doc = mxUtils.createXmlDocument();

				var person1 = doc.createElement('Person');
				person1.setAttribute('firstName', 'Daffy');
				person1.setAttribute('lastName', 'Duck');

				var person2 = doc.createElement('Person');
				person2.setAttribute('firstName', 'Bugs');
				person2.setAttribute('lastName', 'Bunny');

				var relation = doc.createElement('Knows');
				relation.setAttribute('since', '1985');
				
				// Creates the graph inside the given container
				var graph = new mxGraph(container);

				// Optional disabling of sizing
				graph.setCellsResizable(false);
				
				// Configures the graph contains to resize and
				// add a border at the bottom, right
				graph.setResizeContainer(true);
				graph.minimumContainerSize = new mxRectangle(0, 0, 500, 380);
				graph.setBorder(60);
				
				// Stops editing on enter key, handles escape
				new mxKeyHandler(graph);

				// Overrides method to disallow edge label editing
				graph.isCellEditable = function(cell)
				{
					return !this.getModel().isEdge(cell);
				};
				
				// Overrides method to provide a cell label in the display
				graph.convertValueToString = function(cell)
				{
					if (mxUtils.isNode(cell.value))
					{
						if (cell.value.nodeName.toLowerCase() == ('person'))
						{
							var firstName = cell.getAttribute('firstName', '');
							var lastName = cell.getAttribute('lastName', '');

							if (lastName != null && lastName.length > 0)
							{
								return lastName + ', ' + firstName;
							}

							return firstName;
						}
						else if (cell.value.nodeName.toLowerCase() == 'knows')
						{
							return cell.value.nodeName + ' (Since '
									+  cell.getAttribute('since', '') + ')';
						}

					}

					return '';
				};

				// Overrides method to store a cell label in the model
				var cellLabelChanged = graph.cellLabelChanged;
				graph.cellLabelChanged = function(cell, newValue, autoSize)
				{
					if (mxUtils.isNode(cell.value) &&
						cell.value.nodeName.toLowerCase() == ('person'))
					{
						var pos = newValue.indexOf(' ');

						var firstName = (pos > 0) ? newValue.substring(0,
								pos) : newValue;
						var lastName = (pos > 0) ? newValue.substring(
								pos + 1, newValue.length) : '';

						// Clones the value for correct undo/redo
						var elt = cell.value.cloneNode(true);

						elt.setAttribute('firstName', firstName);
						elt.setAttribute('lastName', lastName);

						newValue = elt;
						autoSize = true;
					}
					
					cellLabelChanged.apply(this, arguments);
				};

				// Overrides method to create the editing value
				var getEditingValue = graph.getEditingValue;
				graph.getEditingValue = function(cell)
				{
					if (mxUtils.isNode(cell.value) &&
						cell.value.nodeName.toLowerCase() == ('person'))
					{
						var firstName = cell.getAttribute('firstName', '');
						var lastName = cell.getAttribute('lastName', '');

						return firstName + ' ' + lastName;
					}
				};

				// Adds a special tooltip for edges
				graph.setTooltips(true);
				
				var getTooltipForCell = graph.getTooltipForCell;
				graph.getTooltipForCell = function(cell)
				{
					// Adds some relation details for edges
					if (graph.getModel().isEdge(cell))
					{
						var src = this.getLabel(this.getModel().getTerminal(cell, true));
						var trg = this.getLabel(this.getModel().getTerminal(cell, false));

						return src + ' ' + cell.value.nodeName + ' ' +  trg;
					}

					return getTooltipForCell.apply(this, arguments);
				};
				
				// Enables rubberband selection
				new mxRubberband(graph);

				// Adds an option to view the XML of the graph
				/*
				document.body.appendChild(mxUtils.button('View XML', function()
				{
					var encoder = new mxCodec();
					var node = encoder.encode(graph.getModel());
					mxUtils.popup(mxUtils.getPrettyXml(node), true);
				}));
				*/

				// Changes the style for match the markup
				// Creates the default style for vertices
				var style = graph.getStylesheet().getDefaultVertexStyle();
				style[mxConstants.STYLE_STROKECOLOR] = 'gray';
				style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_SHADOW] = true;
				style[mxConstants.STYLE_FILLCOLOR] = '#DFDFDF';
				style[mxConstants.STYLE_GRADIENTCOLOR] = 'white';
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_FONTSIZE] = '12';
				style[mxConstants.STYLE_SPACING] = 4;
		
				// Creates the default style for edges
				style = graph.getStylesheet().getDefaultEdgeStyle();
				style[mxConstants.STYLE_STROKECOLOR] = '#0C0C0C';
				style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
				style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
				style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_FONTSIZE] = '10';
				
				// Gets the default parent for inserting new cells. This
				// is normally the first child of the root (ie. layer 0).
				var parent = graph.getDefaultParent();
								
				// Adds cells to the model in a single step
				graph.getModel().beginUpdate();
				try
				{
					var v1 = graph.insertVertex(parent, null, person1, 40, 40, 80, 30);
					var v2 = graph.insertVertex(parent, null, person2, 200, 150, 80, 30);
					var e1 = graph.insertEdge(parent, null, relation, v1, v2);
				}
				finally
				{
					// Updates the display
					graph.getModel().endUpdate();
				}

				// Implements a properties panel that uses
				// mxCellAttributeChange to change properties
				graph.getSelectionModel().addListener(mxEvent.CHANGE, function(sender, evt)
				{
					selectionChanged(graph);
				});

				selectionChanged(graph);
			}

			/**
			 * Updates the properties panel
			 */
			function selectionChanged(graph)
			{
				var div = document.getElementById('properties');

				// Forces focusout in IE
				graph.container.focus();

				// Clears the DIV the non-DOM way
				div.innerHTML = '';

				// Gets the selection cell
				var cell = graph.getSelectionCell();

				if (cell == null)
				{
					mxUtils.writeln(div, 'Nothing selected.');
				}
				else
				{
					// Writes the title
					var center = document.createElement('center');
					mxUtils.writeln(center, cell.value.nodeName + ' (' + cell.id + ')');
					div.appendChild(center);
					mxUtils.br(div);

					// Creates the form from the attributes of the user object
					var form = new mxForm();
	
					var attrs = cell.value.attributes;
					
					for (var i = 0; i < attrs.length; i++)
					{
						createTextField(graph, form, cell, attrs[i]);
					}
	
					div.appendChild(form.getTable());
					mxUtils.br(div);
				}
			}

			/**
			 * Creates the textfield for the given property.
			 */
			function createTextField(graph, form, cell, attribute)
			{
				var input = form.addText(attribute.nodeName + ':', attribute.nodeValue);

				var applyHandler = function()
				{
					var newValue = input.value || '';
					var oldValue = cell.getAttribute(attribute.nodeName, '');

					if (newValue != oldValue)
					{
						graph.getModel().beginUpdate();
                        
                        try
                        {
                        	var edit = new mxCellAttributeChange(
 		                           cell, attribute.nodeName,
 		                           newValue);
                           	graph.getModel().execute(edit);
                           	graph.updateCellSize(cell);
                        }
                        finally
                        {
                            graph.getModel().endUpdate();
                        }
					}
				}; 

				mxEvent.addListener(input, 'keypress', function (evt)
				{
					// Needs to take shift into account for textareas
					if (evt.keyCode == /*enter*/13 &&
						!mxEvent.isShiftDown(evt))
					{
						input.blur();
					}
				});

				if (mxClient.IS_IE)
				{
					mxEvent.addListener(input, 'focusout', applyHandler);
				}
				else
				{
					// Note: Known problem is the blurring of fields in
					// Firefox by changing the selection, in which case
					// no event is fired in FF and the change is lost.
					// As a workaround you should use a local variable
					// that stores the focused field and invoke blur
					// explicitely where we do the graph.focus above.
					mxEvent.addListener(input, 'blur', applyHandler);
				}
			}
		};
		// Program starts here. Creates a sample graph in the
		// DOM node with the specified ID. This function is invoked
		// from the onLoad event handler of the document (see below).
		function createSwimlanes(container)
		{
			addSvgIdForSelenium();
			// Checks if browser is supported
			if (!mxClient.isBrowserSupported())
			{
				// Displays an error message if the browser is
				// not supported.
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				// Creates a wrapper editor around a new graph inside
				// the given container using an XML config for the
				// keyboard bindings
				var config = mxUtils.load(
					'editors/config/keyhandler-commons.xml').
						getDocumentElement();
				var editor = new mxEditor(config);
				editor.setGraphContainer(container);
				var graph = editor.graph;
				var model = graph.getModel();

				// Auto-resizes the container
				graph.border = 80;
				graph.getView().translate = new mxPoint(graph.border/2, graph.border/2);
				graph.setResizeContainer(true);
				graph.graphHandler.setRemoveCellsFromParent(false);

				// Changes the default vertex style in-place
				var style = graph.getStylesheet().getDefaultVertexStyle();
				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
				style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
				style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
				style[mxConstants.STYLE_FONTSIZE] = 11;
				style[mxConstants.STYLE_STARTSIZE] = 22;
				style[mxConstants.STYLE_HORIZONTAL] = false;
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_STROKECOLOR] = 'black';
				delete style[mxConstants.STYLE_FILLCOLOR];

				style = mxUtils.clone(style);
				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
				style[mxConstants.STYLE_FONTSIZE] = 10;
				style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_HORIZONTAL] = true;
				style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
				delete style[mxConstants.STYLE_STARTSIZE];
				style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'none';
				graph.getStylesheet().putCellStyle('process', style);
				
				style = mxUtils.clone(style);
				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
				style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
				delete style[mxConstants.STYLE_ROUNDED];
				graph.getStylesheet().putCellStyle('state', style);
												
				style = mxUtils.clone(style);
				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
				style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
				style[mxConstants.STYLE_VERTICAL_ALIGN] = 'top';
				style[mxConstants.STYLE_SPACING_TOP] = 40;
				style[mxConstants.STYLE_SPACING_RIGHT] = 64;
				graph.getStylesheet().putCellStyle('condition', style);
								
				style = mxUtils.clone(style);
				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_DOUBLE_ELLIPSE;
				style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
				style[mxConstants.STYLE_SPACING_TOP] = 28;
				style[mxConstants.STYLE_FONTSIZE] = 14;
				style[mxConstants.STYLE_FONTSTYLE] = 1;
				delete style[mxConstants.STYLE_SPACING_RIGHT];
				graph.getStylesheet().putCellStyle('end', style);
				
				style = graph.getStylesheet().getDefaultEdgeStyle();
				style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
				style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_BLOCK;
				style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_STROKECOLOR] = 'black';
				
				style = mxUtils.clone(style);
				style[mxConstants.STYLE_DASHED] = true;
				style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_OPEN;
				style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_OVAL;
				graph.getStylesheet().putCellStyle('crossover', style);
						
				// Installs double click on middle control point and
				// changes style of edges between empty and this value
				graph.alternateEdgeStyle = 'elbow=vertical';

				// Adds automatic layout and various switches if the
				// graph is enabled
				if (graph.isEnabled())
				{
					// Allows new connections but no dangling edges
					graph.setConnectable(true);
					graph.setAllowDanglingEdges(false);
					
					// End-states are no valid sources
					var previousIsValidSource = graph.isValidSource;
					
					graph.isValidSource = function(cell)
					{
						if (previousIsValidSource.apply(this, arguments))
						{
							var style = this.getModel().getStyle(cell);
							
							return style == null || !(style == 'end' || style.indexOf('end') == 0);
						}

						return false;
					};
					
					// Start-states are no valid targets, we do not
					// perform a call to the superclass function because
					// this would call isValidSource
					// Note: All states are start states in
					// the example below, so we use the state
					// style below
					graph.isValidTarget = function(cell)
					{
						var style = this.getModel().getStyle(cell);
						
						return !this.getModel().isEdge(cell) && !this.isSwimlane(cell) &&
							(style == null || !(style == 'state' || style.indexOf('state') == 0));
					};
					
					// Allows dropping cells into new lanes and
					// lanes into new pools, but disallows dropping
					// cells on edges to split edges
					graph.setDropEnabled(true);
					graph.setSplitEnabled(false);
					
					// Returns true for valid drop operations
					graph.isValidDropTarget = function(target, cells, evt)
					{
						if (this.isSplitEnabled() && this.isSplitTarget(target, cells, evt))
						{
							return true;
						}
						
						var model = this.getModel();
						var lane = false;
						var pool = false;
						var cell = false;
						
						// Checks if any lanes or pools are selected
						for (var i = 0; i < cells.length; i++)
						{
							var tmp = model.getParent(cells[i]);
							lane = lane || this.isPool(tmp);
							pool = pool || this.isPool(cells[i]);
							
							cell = cell || !(lane || pool);
						}
						
						return !pool && cell != lane && ((lane && this.isPool(target)) ||
							(cell && this.isPool(model.getParent(target))));
					};
					
					// Adds new method for identifying a pool
					graph.isPool = function(cell)
					{
						var model = this.getModel();
						var parent = model.getParent(cell);
					
						return parent != null && model.getParent(parent) == model.getRoot();
					};
					
					// Changes swimlane orientation while collapsed
					graph.model.getStyle = function(cell)
					{
						var style = mxGraphModel.prototype.getStyle.apply(this, arguments);
					
						if (graph.isCellCollapsed(cell))
						{
							if (style != null)
							{
								style += ';';
							}
							else
							{
								style = '';
							}
							
							style += 'horizontal=1;align=left;spacingLeft=14;';
						}
						
						return style;
					};

					// Keeps widths on collapse/expand					
					var foldingHandler = function(sender, evt)
					{
						var cells = evt.getProperty('cells');
						
						for (var i = 0; i < cells.length; i++)
						{
							var geo = graph.model.getGeometry(cells[i]);

							if (geo.alternateBounds != null)
							{
								geo.width = geo.alternateBounds.width;
							}
						}
					};

					graph.addListener(mxEvent.FOLD_CELLS, foldingHandler);
				}
				
				// Applies size changes to siblings and parents
				new mxSwimlaneManager(graph);

				// Creates a stack depending on the orientation of the swimlane
				var layout = new mxStackLayout(graph, false);
				
				// Makes sure all children fit into the parent swimlane
				layout.resizeParent = true;
							
				// Applies the size to children if parent size changes
				layout.fill = true;

				// Only update the size of swimlanes
				layout.isVertexIgnored = function(vertex)
				{
					return !graph.isSwimlane(vertex);
				}
				
				// Keeps the lanes and pools stacked
				var layoutMgr = new mxLayoutManager(graph);

				layoutMgr.getLayout = function(cell)
				{
					if (!model.isEdge(cell) && graph.getModel().getChildCount(cell) > 0 &&
						(model.getParent(cell) == model.getRoot() || graph.isPool(cell)))
					{
						layout.fill = graph.isPool(cell);
						
						return layout;
					}
					
					return null;
				};
				
				// Gets the default parent for inserting new cells. This
				// is normally the first child of the root (ie. layer 0).
				var parent = graph.getDefaultParent();

				// Adds cells to the model in a single step
				model.beginUpdate();
				try
				{
					var pool1 = graph.insertVertex(parent, null, 'Pool 1', 0, 0, 640, 0);
					pool1.setConnectable(false);

					var lane1a = graph.insertVertex(pool1, null, 'Lane A', 0, 0, 640, 110);
					lane1a.setConnectable(false);

					var lane1b = graph.insertVertex(pool1, null, 'Lane B', 0, 0, 640, 110);
					lane1b.setConnectable(false);

					var lane1c = graph.insertVertex(pool1, null, 'Lane C', 0, 0, 640, 110);
					lane1c.setConnectable(false);

					var pool2 = graph.insertVertex(parent, null, 'Pool 2', 0, 0, 640, 0);
					pool2.setConnectable(false);

					var lane2a = graph.insertVertex(pool2, null, 'Lane A', 0, 0, 640, 140);
					lane2a.setConnectable(false);

					var lane2b = graph.insertVertex(pool2, null, 'Lane B', 0, 0, 640, 110);
					lane2b.setConnectable(false);

					var pool3 = graph.insertVertex(parent, null, 'Pool 3', 0, 0, 640, 0);
					pool3.setConnectable(false);

					var lane3a = graph.insertVertex(pool3, null, 'Lane A', 0, 0, 640, 110);
					lane3a.setConnectable(false);


					
					var start1 = graph.insertVertex(lane1a, null, null, 40, 40, 30, 30, 'state');
					var end1 = graph.insertVertex(lane1a, null, 'A', 560, 40, 30, 30, 'end');
					
					var step1 = graph.insertVertex(lane1a, null, 'Contact\nProvider', 90, 30, 80, 50, 'process');
					var step11 = graph.insertVertex(lane1a, null, 'Complete\nAppropriate\nRequest', 190, 30, 80, 50, 'process');
					var step111 = graph.insertVertex(lane1a, null, 'Receive and\nAcknowledge', 385, 30, 80, 50, 'process');
					
					var start2 = graph.insertVertex(lane2b, null, null, 40, 40, 30, 30, 'state');
					
					var step2 = graph.insertVertex(lane2b, null, 'Receive\nRequest', 90, 30, 80, 50, 'process');
					var step22 = graph.insertVertex(lane2b, null, 'Refer to Tap\nSystems\nCoordinator', 190, 30, 80, 50, 'process');
					
					var step3 = graph.insertVertex(lane1b, null, 'Request 1st-\nGate\nInformation', 190, 30, 80, 50, 'process');
					var step33 = graph.insertVertex(lane1b, null, 'Receive 1st-\nGate\nInformation', 290, 30, 80, 50, 'process');
					
					var step4 = graph.insertVertex(lane2a, null, 'Receive and\nAcknowledge', 290, 20, 80, 50, 'process');
					var step44 = graph.insertVertex(lane2a, null, 'Contract\nConstraints?', 400, 20, 50, 50, 'condition');
					var step444 = graph.insertVertex(lane2a, null, 'Tap for gas\ndelivery?', 480, 20, 50, 50, 'condition');
					
					var end2 = graph.insertVertex(lane2a, null, 'B', 560, 30, 30, 30, 'end');
					var end3 = graph.insertVertex(lane2a, null, 'C', 560, 84, 30, 30, 'end');

					var start3 = graph.insertVertex(lane3a, null, null, 40, 40, 30, 30, 'state');
					//var end4 = graph.insertVertex(lane3a, null, 'A', 560, 40, 30, 30, 'end');
					
					var e = null;
					
					graph.insertEdge(lane1a, null, null, start1, step1);
					graph.insertEdge(lane1a, null, null, step1, step11);
					graph.insertEdge(lane1a, null, null, step11, step111);
					
					graph.insertEdge(lane2b, null, null, start2, step2);
					graph.insertEdge(lane2b, null, null, step2, step22);
					graph.insertEdge(parent, null, null, step22, step3);
					
					graph.insertEdge(lane1b, null, null, step3, step33);
					graph.insertEdge(lane2a, null, null, step4, step44);
					graph.insertEdge(lane2a, null, 'No', step44, step444, 'verticalAlign=bottom');
					graph.insertEdge(parent, null, 'Yes', step44, step111, 'verticalAlign=bottom;horizontal=0;labelBackgroundColor=white;');

					graph.insertEdge(lane2a, null, null, step4, step44);

					graph.insertEdge(parent, null, null, start1, start3);
					
					graph.insertEdge(lane2a, null, 'Yes', step444, end2, 'verticalAlign=bottom');
					e = graph.insertEdge(lane2a, null, 'No', step444, end3, 'verticalAlign=top');
					e.geometry.points = [new mxPoint(step444.geometry.x + step444.geometry.width / 2,
						end3.geometry.y + end3.geometry.height / 2)];
					
					graph.insertEdge(parent, null, null, step1, step2, 'crossover');
					graph.insertEdge(parent, null, null, step3, step11, 'crossover');
					e = graph.insertEdge(lane1a, null, null, step11, step33, 'crossover');
					e.geometry.points = [new mxPoint(step33.geometry.x + step33.geometry.width / 2 + 20,
								step11.geometry.y + step11.geometry.height * 4 / 5)];
					graph.insertEdge(parent, null, null, step33, step4);
					graph.insertEdge(lane1a, null, null, step111, end1);
				}
				finally
				{
					// Updates the display
					model.endUpdate();
				}
			}
		};
		
