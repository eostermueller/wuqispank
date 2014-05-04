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

	function swimlaneGraphLoader(container, xml)
	{
		//debugger;
		//var graph = new mxGraph(container);
		//graph.setGraphContainer(container);
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
			mxGraph.prototype.swimlaneSelectionEnabled = true;

			// Creates a wrapper editor around a new graph inside
			// the given container using an XML config for the
			// keyboard bindings
			var config = mxUtils.load(
				'/wuqispank/mxGraph-2_2_0_2/editors/config/keyhandler-commons.xml').
					getDocumentElement();
			var editor = new mxEditor(config);
			editor.setGraphContainer(container);
			var graph = editor.graph;
			//debugger;
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

			graph.getStylesheet().putCellStyle('tableHeader', style);
											
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

			style = new Object();
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
			style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
			style[mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
			style[mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
			style[mxConstants.STYLE_SWIMLANE_FILLCOLOR] = '#FFFFFF';
			style[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
			style[mxConstants.STYLE_FONTCOLOR] = '#000000';
			style[mxConstants.STYLE_STROKEWIDTH] = '2';
			style[mxConstants.STYLE_STARTSIZE] = '28';
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			style[mxConstants.STYLE_FONTSIZE] = '12';
			style[mxConstants.STYLE_FONTSTYLE] = 1;
			style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'none';
			//style[mxConstants.STYLE_IMAGE] = 'images/icons48/table.png';
			// Looks better without opacity if shadow is enabled
			//style[mxConstants.STYLE_OPACITY] = '80';
			style[mxConstants.STYLE_SHADOW] = 1;
			graph.getStylesheet().putCellStyle('swimlane', style);

			style = new Object();
			style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
			style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
			style[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
			style[mxConstants.STYLE_FONTCOLOR] = '#000000';
			style[mxConstants.STYLE_STROKEWIDTH] = '2';
			style[mxConstants.STYLE_STARTSIZE] = '28';
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			style[mxConstants.STYLE_FONTSIZE] = '12';
			style[mxConstants.STYLE_FONTSTYLE] = 1;
			style[mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
			style[mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
			//style[mxConstants.STYLE_SHADOW] = 1;

			graph.getStylesheet().putCellStyle('wuqispankRowOdd', style);

			style = mxUtils.clone(style);
			delete style[mxConstants.STYLE_GRADIENTCOLOR];
			delete style[mxConstants.STYLE_FILLCOLOR];
			graph.getStylesheet().putCellStyle('wuqispankRowEven', style);


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
						(style == null || !(style == 'tableHeader' || style.indexOf('tableHeader') == 0));
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
					console.log("isPool cell [ "+ cell.id + "][" + (parent != null && model.getParent(parent) == model.getRoot()) + "]");
					//console.log("isPool cell [ "+ cell.id + "]");
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
					console.log("Style is [" + style + "] for cell [" + cell + "]");
					return style;
				};

				// Keeps widths on collapse/expand					
				var foldingHandler = function(sender, evt)
				{
					console.log("foldingHandler{")
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
			loadSwimlaneXml(container, graph, xml);

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
				
			// 	var tableOneHeader = graph.insertVertex(laneTablesHeader, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'tableHeader');
			// 	tableOneHeader.setConnectable(false);
			// 	//STYLE_LABEL_POSITION -- for horizontal labels
			// 	//var label21 = graph.insertVertex(tableOneHeader, null, 'CUST_CUST_REL', 0.5, 1, 0, 0, 'tabelLabel', true);
			// 	//label21.setConnectable(false);
				
			// 	var tableOneFooter = graph.insertVertex(laneTablesFooter, null, 'CUST_CUST_REL', 50, 30, 60, 60, 'tableHeader');
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
	function loadSwimlaneXml(container, graph, xml) {

        if (mxClient.isBrowserSupported())
        {
                var divs = document.getElementsByTagName('*');

                for (var i = 0; i < divs.length; i++)
                {
                        if (divs[i].className.toString().indexOf('mxgraph') >= 0)
                        {
                                (function(container)
                                {
                                        //debugger;
                                        //var xml = mxUtils.getTextContent(container);
                                        var xmlDocument = mxUtils.parseXml(xml);

                                        if (xmlDocument.documentElement != null && xmlDocument.documentElement.nodeName == 'mxGraphModel')
                                        {
                                                var decoder = new mxCodec(xmlDocument);
                                                var node = xmlDocument.documentElement;

                                                container.innerHTML = '';

                                                //var graph = new mxGraph(container);
                                                graph.centerZoom = false;
                                                graph.setTooltips(false);
                                                //graph.setEnabled(false);
                                                graph.setEnabled(true);

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

                                                debugger;
                                                decoder.decode(node, graph.getModel());
                                                graph.resizeContainer = false;

                                        }
                                })(divs[i]);
                        }
                }
        }

	}
	
