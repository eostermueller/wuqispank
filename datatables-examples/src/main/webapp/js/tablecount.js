/**
  * @stolen from http://stackoverflow.com/questions/332422/how-do-i-get-the-name-of-an-objects-type-in-javascript
  */
// Object.prototype.getName = function() { 
//    var funcNameRegex = /function (.{1,})\(/;
//    var results = (funcNameRegex).exec((this).constructor.toString());
//    return (results && results.length > 1) ? results[1] : "";
// };
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
//			style[mxConstants.STYLE_FONTSIZE] = 10;
			delete style[mxConstants.STYLE_ROUNDED];
//			style[mxConstants.STYLE_VERTICAL_LABEL_POSITION] = mxConstants.ALIGN_TOP;
//			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_BOTTOM;
			style[mxConstants.STYLE_FILLCOLOR] = '#EDEDED';
			style[mxConstants.STYLE_STROKECOLOR] = '#FFFFFF';
			style[mxConstants.STYLE_FONTCOLOR] = '#FFFFFF';
			style[mxConstants.STYLE_FONTSIZE] = 36;
  			style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
  			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
  			style[mxConstants.STYLE_LABEL_POSITION] = mxConstants.ALIGN_CENTER;

			//ETO
			//style[mxConstants.STYLE_ROTATION] = 0;
			//delete style[mxConstants.STYLE_VERTICAL_ALIGN];

			graph.getStylesheet().putCellStyle('wsTableHeader', style);
											
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
			style[mxConstants.STYLE_STROKECOLOR] = '#EDEDED';

			
			style = mxUtils.clone(style);
			style[mxConstants.STYLE_DASHED] = true;
			style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_OPEN;
			style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_OVAL;
			graph.getStylesheet().putCellStyle('crossover', style);


			style = new Object();
			style[mxConstants.STYLE_EDGE] = mxConstants.EDGESTYLE_ORTHOGONAL;
			style[mxConstants.STYLE_ENDARROW] = 'none';
			style[mxConstants.STYLE_STARTARROW] = 'none';
			style[mxConstants.STYLE_ROUNDED] = true;
			style[mxConstants.STYLE_FONTCOLOR] = 'black';
			style[mxConstants.STYLE_STROKECOLOR] = 'black';
			style[mxConstants.STYLE_DASHED] = 1;
			graph.getStylesheet().putCellStyle('wsJoinEdge', style);


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
//			style[mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
			style[mxConstants.STYLE_FILLCOLOR] = '#CCE5FF';
			//style[mxConstants.STYLE_SHADOW] = 1;

			graph.getStylesheet().putCellStyle('wsRowOdd', style);

			style = mxUtils.clone(style);
			delete style[mxConstants.STYLE_GRADIENTCOLOR];
			delete style[mxConstants.STYLE_FILLCOLOR];
			graph.getStylesheet().putCellStyle('wsRowEven', style);

			//style = mxUtils.clone(style);
			var xstyle = new Object();
			//style[mxConstants.STYLE_FILLCOLOR] = '#FAFAFA';
			//style[mxConstants.STYLE_STROKECOLOR] = 'none';
			xstyle[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
			xstyle[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
			xstyle[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
			xstyle[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
			xstyle[mxConstants.STYLE_GRADIENTCOLOR] = '#FAFAFA';
			xstyle[mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
			xstyle[mxConstants.STYLE_SWIMLANE_FILLCOLOR] = '#ffffff';
			xstyle[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
			xstyle[mxConstants.STYLE_FONTCOLOR] = '#000000';
			xstyle[mxConstants.STYLE_STROKEWIDTH] = '2';
			xstyle[mxConstants.STYLE_STARTSIZE] = '28';
			xstyle[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			xstyle[mxConstants.STYLE_FONTSIZE] = '12';
			xstyle[mxConstants.STYLE_FONTSTYLE] = 1;
			// Looks better without opacity if shadow is enabled
			//style[mxConstants.STYLE_OPACITY] = '80';
			xstyle[mxConstants.STYLE_SHADOW] = 1;			
			//graph.getStylesheet().putCellStyle('wsVerticalTableLane');

			// #### START
			var style = new Object();
				style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
				style[mxConstants.STYLE_STARTSIZE] = 22;
				style[mxConstants.STYLE_HORIZONTAL] = false;
				style[mxConstants.STYLE_FONTCOLOR] = 'black';
				style[mxConstants.STYLE_STROKECOLOR] = 'black';

				style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
				style[mxConstants.STYLE_FONTSIZE] = 10;
				//style[mxConstants.STYLE_ROUNDED] = true;
				style[mxConstants.STYLE_HORIZONTAL] = true;
				style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
				delete style[mxConstants.STYLE_STARTSIZE];
				style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'none';

			style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
			style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
			style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
			style[mxConstants.STYLE_GRADIENTCOLOR] = '#FAFAFA';
			style[mxConstants.STYLE_FILLCOLOR] = '#FFFFFF';
			style[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
			style[mxConstants.STYLE_FONTCOLOR] = '#000000';
			style[mxConstants.STYLE_STROKEWIDTH] = '2';
			style[mxConstants.STYLE_STARTSIZE] = '28';
			style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
			style[mxConstants.STYLE_FONTSIZE] = '12';
			style[mxConstants.STYLE_FONTSTYLE] = 1;
			style[mxConstants.STYLE_SHADOW] = 1;	
			style[mxConstants.STYLE_FOLDABLE] = 0;			
				graph.getStylesheet().putCellStyle('wsVerticalTableLane', style);			
			// #### END


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
				// graph.isValidTarget = function(cell)
				// {
				// 	var style = this.getModel().getStyle(cell);
					
				// 	return !this.getModel().isEdge(cell) && !this.isSwimlane(cell) &&
				// 		(style == null || !(style == 'tableHeader' || style.indexOf('tableHeader') == 0));
				// };
				
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
				var rc = true;
				if (graph.isSwimlane(vertex)) {
					rc = false;
				} 
				// else {
					var style = this.getGraph().getModel().getStyle(vertex);
					console.log("isVertexIgnored style [" + style + "] id [" + vertex.id + "]");
					// console.log("Type of style [" + style.getName() + "]");
					// if (style.indexOf('wsRow') == 0) {
					// 	console.log("Found wsRow style");
					// }

				// }
				//return !graph.isSwimlane(vertex);
				return rc;
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
			/** START OF HOVER ********************************************************/
				// Changes fill color to red on mouseover
				function updateStyle(state, hover)
				{
					if (hover)
					{
						//state.style[mxConstants.STYLE_FILLCOLOR] = '#ff0000';
						  state.style[mxConstants.STYLE_FILLCOLOR] = '#EDEDED';
					}
					
					// Sets rounded style for both cases since the rounded style
					// is not set in the default style and is therefore inherited
					// once it is set, whereas the above overrides the default value
					state.style[mxConstants.STYLE_ROUNDED] = (hover) ? '1' : '0';
					state.style[mxConstants.STYLE_STROKEWIDTH] = (hover) ? '10' : '1';


					/* The following code worked nothing like I wanted.
					 * When you hovered over something, then the entire tablecount graph disappered
					 * and was replaced by a tiny little message box/windows.
					 */
					//var content = document.createElement('div');
					//mxUtils.write(content, "This is my content");
					
					// Note that we're using the container scrollbars for the graph so that the
					// container extends to the parent div inside the window
					//var wnd = new mxWindow('Scrollable, resizable, given height', container, 50, 50, 220, 224, true, true);					
				
					//wnd = new mxWindow('Scrollable, resizable, auto height', content, 300, 50, 200, null, true, true);
					//wnd.setMaximizable(true);
					//wnd.setScrollable(true);
					//wnd.setResizable(true);
					//wnd.setVisible((hover) ? true : false);					
					
				};				
				
				graph.addMouseListener(
				{
				    currentState: null,
				    previousStyle: null,
				    mouseDown: function(sender, me)
				    {
				        if (this.currentState != null)
				        {
				        	this.dragLeave(me.getEvent(), this.currentState);
				        	this.currentState = null;
				        }
				    },
				    mouseMove: function(sender, me)
				    {
				        if (this.currentState != null && me.getState() == this.currentState)
				        {
				            return;
				        }

				        var tmp = graph.view.getState(me.getCell());

				        // Ignores everything but vertices
				        if (graph.isMouseDown || (tmp != null && !
				            graph.getModel().isVertex(tmp.cell)))
				        {
				        	tmp = null;
				        }

				        if (tmp != this.currentState)
				        {
				            if (this.currentState != null)
				            {
				                this.dragLeave(me.getEvent(), this.currentState);
				            }

				            this.currentState = tmp;

				            if (this.currentState != null)
				            {
				                this.dragEnter(me.getEvent(), this.currentState);
				            }
				        }
				    },
				    mouseUp: function(sender, me) { },
				    dragEnter: function(evt, state)
				    {
				        if (state != null)
				        {
				        	this.previousStyle = state.style;
				        	state.style = mxUtils.clone(state.style);
				        	updateStyle(state, true);
				        	state.shape.apply(state);
				        	state.shape.reconfigure();
				        }
				    },
				    dragLeave: function(evt, state)
				    {
				        if (state != null)
				        {
				        	state.style = this.previousStyle;
				        	updateStyle(state, false);
				        	state.shape.apply(state);
				        	state.shape.reconfigure();
				        }
				    }
				});
			
			
			/** END   OF HOVER ********************************************************/
			
			// Gets the default parent for inserting new cells. This
			// is normally the first child of the root (ie. layer 0).
			var parent = graph.getDefaultParent();
			loadSwimlaneXml(container, graph, xml);
				// Implements a properties panel that uses
				// mxCellAttributeChange to change properties
				graph.getSelectionModel().addListener(mxEvent.CHANGE, function(sender, evt)
				{
					selectionChanged(graph);
				});

				selectionChanged(graph);



		}
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

                                                //debugger;
                                                decoder.decode(node, graph.getModel());
                                                graph.resizeContainer = false;

                                        }
                                })(divs[i]);
                        }
                }
        }

	}
	
