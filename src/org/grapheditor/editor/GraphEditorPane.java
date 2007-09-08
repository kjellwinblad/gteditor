/*
 * Copyright (C) 2007  Kjell Winblad (kjellw@cs.umu.se)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.grapheditor.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.EditorGraph;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.listeners.GraphPropertiesChangeListener;
import org.grapheditor.editor.listeners.InsertModeChangeListener;
import org.grapheditor.editor.listeners.UndoAndRedoAbleListener;
import org.grapheditor.properties.GlobalProperties;
import org.grapheditor.properties.GraphProperties;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.Port;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;

/**
 * This class represents a graph editor pane used to edit and view graphs. It
 * extends JGraph which is the main component in the JGraph library. It
 * customizes the default JGraph behavor to fit in the graph editor program.
 * 
 * To do that it adds event listeners for keyboard events and marquee events. It
 * also changes the way vertices look by changeing the CellViewFactory. It has
 * methods to make it easy to perorm cut, copy and paste actions to make them
 * easy to perform. It adds several methods to make the handling of graph theory
 * graphs easier. For example it has methods like addDefaultVertexAt(double,
 * double) to add a verex at a custom position.
 * 
 * It also provides anumber of operation that can be performed on the graph. The
 * code for these operations is in the class GraphOperationHelper to make this
 * class simpler and more readable.
 * 
 * The class replaces JGraph default graph model for JGraphT JGraphModelAdapter
 * Use the method getJGraphTGraph to get the JGraphT version of the current
 * graph in the editor.
 * 
 * To make it posible for the GraphEditorPane to produce messages for the user
 * the constructor takes an object with the InformationPane interface to put
 * messages to. This is used by some operations.
 * 
 * Use the method getJGraphTGraph to get the JGraphT version of the current
 * graph in the editor.
 * 
 * @author kjellw
 */
public class GraphEditorPane extends JGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7610898466035692785L;

	private JGraphModelAdapter jgAdapter;

	/**
	 * Insert mode is a mode where you can put out vertices just by klicking in
	 * the graph area and
	 */
	private boolean insertMode = true;

	private Action cut, copy, paste;

	private GraphEditorUndoManager undoManager;

	private GraphProperties graphProperties = new GraphProperties();

	private List<GraphPropertiesChangeListener> graphPropertiesChangeListeners = new LinkedList<GraphPropertiesChangeListener>();

	private List<InsertModeChangeListener> insertModeListeners = new LinkedList<InsertModeChangeListener>();

	private ListenableDirectedGraph<VertexInformation, EdgeInformation> jGraphTgraph;

	private GraphEditorPaneMouseAndMotionListener mouseListener;

	private JFrame mainFrame;

	private InformationPane informationPane;

	private MessageProvider currentStatusMessageProvider = new MessageProvider(
			"");

	private GraphOperationHelper graphOperationHelper;

	/**
	 * Returns the underlying JGraphT graph
	 * 
	 * @return the jGraphTgraph
	 */
	public ListenableDirectedGraph<VertexInformation, EdgeInformation> getJGraphTGraph() {
		return jGraphTgraph;
	}

	/**
	 * Constructs a graph editor pane with an empty graph.
	 * 
	 * @param mainFrame
	 *            is the parent frame that contains this graph
	 * @param informationPane
	 *            is an information pane that is used by the graph editor
	 */
	public GraphEditorPane(JFrame mainFrame, InformationPane informationPane) {
		super();

		this.informationPane = informationPane;
		informationPane.putMessage(currentStatusMessageProvider);
		graphOperationHelper = new GraphOperationHelper(this, informationPane);
		this.mainFrame = mainFrame;
		MouseListener ml = getMouseListeners()[0];
		MouseMotionListener mml = getMouseMotionListeners()[0];
		mouseListener = new GraphEditorPaneMouseAndMotionListener(this, ml, mml);
		this.removeMouseListener(ml);
		this.removeMouseMotionListener(mml);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		this.setDisconnectable(false);
		this.setPortsVisible(true);


		// Ugly
		this.setEditClickCount(20);

		init();
		setGridProps();

		
		Color background = this.getBackground();
		Color marqueeColor = new Color(255 - background.getRed(), 255 - background.getGreen(), 255 - background.getBlue());
		this.setMarqueeColor(marqueeColor);
	}

	public void setGridProps() {
		GlobalProperties prop = GlobalProperties.getInstance();
		setGridEnabled(prop.isGridEnabled());
		setGridVisible((prop.isGridEnabled()? prop.isGridVisable() : false));
		setGridMode(prop.getGridMode());
		setGridSize(prop.getGridSize());
		
	}

	/**
	 * Initialize an empty graph. Sets necessary listeners etc for the graph to
	 * behave accurate.
	 * 
	 */
	private void init() {
		// create a JGraphT graph
		jGraphTgraph = new EditorGraph<VertexInformation, EdgeInformation>(
				EdgeInformation.class);

		// create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<VertexInformation, EdgeInformation>(
				jGraphTgraph);

		GraphLayoutCache layoutCache = new GraphLayoutCache(jgAdapter,
				new GraphEditorCellViewFactory());
		setGraphLayoutCache(layoutCache);

		this.setModel(jgAdapter);

		undoManager = new GraphEditorUndoManager(this);
		getModel().addUndoableEditListener(undoManager);

		copy = javax.swing.TransferHandler.getCopyAction();
		cut = javax.swing.TransferHandler.getCutAction();
		paste = javax.swing.TransferHandler.getPasteAction();

		this.setMarqueeHandler(new MarqueeHandler(this));

		this.removeKeyListener(this.getKeyListeners()[0]);

		this.addKeyListener(new KeyHandler(this));

		final Action statusUpdateAction = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				// Show new graph information...

				if (!isReadyForOperation())
					return;

				Object[] vertexObjects = null;
				Object[] edgeObjects = null;
				try {
					vertexObjects = getGraphLayoutCache().getCells(false, true,
							false, false);
					edgeObjects = getGraphLayoutCache().getCells(false, false,
							false, true);
				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return;
				}
				Object[] selectedVertexObjects = getSelectionCells(vertexObjects);
				Object[] selectedEdgeObjects = getSelectionCells(edgeObjects);
				int vertices = vertexObjects.length;
				int edges = edgeObjects.length;
				int selectedVertices = selectedVertexObjects.length;
				int selectedEdges = selectedEdgeObjects.length;

				currentStatusMessageProvider.setMessage(vertices + "/" + edges
						+ " vertices/edges, " + selectedVertices + "/"
						+ selectedEdges + " selected");

			}

		};

		getModel().addGraphModelListener(new GraphModelListener() {

			public void graphChanged(GraphModelEvent e) {
				statusUpdateAction.actionPerformed(new ActionEvent(
						"model changed", 0, "modelChange"));
			}

		});
		getSelectionModel().addGraphSelectionListener(
				new GraphSelectionListener() {

					public void valueChanged(GraphSelectionEvent e) {
						statusUpdateAction.actionPerformed(new ActionEvent(
								"model changed", 0, "modelChange"));

					}

				});

	}

	/**
	 * Adds a vertex at the specified positon (x, y). Uses the GraphProperties
	 * object for the graph to associate the vertex with the default properties
	 * 
	 * Creates a VertexInformation object and sets it as the user object of the
	 * vertex cell.
	 * 
	 * @param x-coordinate
	 * @param y-coordinate
	 * @return
	 */
	public DefaultGraphCell addDefaultVertexAt(double x, double y) {

		VertexInformation newVertex = new VertexInformation();
		DefaultGraphCell vertexCell = new DefaultGraphCell(newVertex);
		newVertex.setJGraphVertex(vertexCell);
		vertexCell.setUserObject(newVertex);
		Map attrs = vertexCell.getAttributes();
		GraphConstants.setOpaque(attrs, false);
		GraphConstants.setSizeable(attrs, false);
		GraphConstants.setBorderColor(attrs, Color.black);
		GraphEditorConstants.setShape(attrs, getGraphProperties()
				.getDefaultInsertShape());

		GraphEditorConstants.setUseGraphBackground(attrs, getGraphProperties()
				.isDefaultUseGraphBackground());
		GraphEditorConstants.setBackground(attrs, getGraphProperties()
				.getDefaultVertexBackgroundColor());
		GraphEditorConstants.setForeground(attrs, getGraphProperties()
				.getDefaultVertexForegroundColor());

		Rectangle2D newBounds = new Rectangle2D.Double((x - 9) / getScale(),
				(y - 9) / getScale(), 18, 18);
		GraphConstants.setBounds(attrs, newBounds);
		DefaultPort port = new DefaultPort();
		vertexCell.add(port);

		port.setParent(vertexCell);

		getGraphLayoutCache().insert(vertexCell);

		return vertexCell;
	}

	/**
	 * Adds a vertex cell from the information given in the vertex parameter
	 * 
	 * @param vertex
	 */
	public void addVertex(VertexInformation vertex) {

		DefaultGraphCell vertexCell = new DefaultGraphCell(vertex);
		vertex.setJGraphVertex(vertexCell);
		vertexCell.setUserObject(vertex);
		Map attrs = vertexCell.getAttributes();
		GraphConstants.setOpaque(attrs, false);
		GraphConstants.setSizeable(attrs, false);
		GraphConstants.setBorderColor(attrs, Color.black);
		GraphEditorConstants.setShape(attrs, vertex.getShape());
		GraphEditorConstants.setUseGraphBackground(attrs, vertex
				.isUseGraphBackround());
		GraphEditorConstants.setBackground(attrs, vertex.getBackgroundColor());
		GraphEditorConstants.setForeground(attrs, vertex.getForegroundColor());
		Rectangle2D newBounds = new Rectangle2D.Double(vertex.getXPosition(),
				vertex.getYPosition(), 18, 18);

		GraphConstants.setBounds(attrs, newBounds);
		DefaultPort port = new DefaultPort();
		vertexCell.add(port);
		port.setParent(vertexCell);

		getGraphLayoutCache().insert(vertexCell);
	}

	/**
	 * The method sets the position on a vertex. It is based on the
	 * JGraphAdapterDemo class in the JGraphT library.
	 * 
	 * @param vertex
	 * @param x
	 * @param y
	 */
	private void positionVertexAt(VertexInformation vertex, double x, double y) {
		DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
		AttributeMap attr = cell.getAttributes();
		Rectangle2D newBounds = new Rectangle2D.Double(x, y, 18, 18);

		GraphConstants.setBounds(attr, newBounds);

		AttributeMap cellAttr = new AttributeMap();
		cellAttr.put(cell, attr);
		jgAdapter.edit(cellAttr, null, null, null);
	}

	/**
	 * This method adds l to the list of InsertModeChangeListeners that listens
	 * for Input mode change.
	 * 
	 * @param l
	 */
	public void addInsertModeChangeListener(InsertModeChangeListener l) {
		insertModeListeners.add(l);
	}

	/**
	 * @return the insertMode
	 */
	public boolean isInsertMode() {
		return insertMode;
	}

	/**
	 * Change if insert mode is on if the insertMode parameter is true sets it
	 * to off othevice.
	 * 
	 * @param insertMode
	 *            the insertMode to set
	 */
	public void setInsertMode(boolean insertMode) {

		for (InsertModeChangeListener l : insertModeListeners) {
			l.newInsertModeEvent(insertMode);
		}

		
		this.setPortsVisible(insertMode);

		this.insertMode = insertMode;

		mouseListener.setInsertMode(insertMode);
		
		
		
	}

	/**
	 * Insert an edge between the source and the target port.
	 * 
	 * @param source
	 * @param target
	 */
	public DefaultEdge connect(Port source, Port target) {
		// Construct Edge with no label

		// TODO how to do support for multigraph
		if (source == target)
			return null;

		if (source instanceof DefaultPort) {
			DefaultPort defSourcePort = (DefaultPort) source;
			if (target instanceof DefaultPort) {
				DefaultPort defTargetPort = (DefaultPort) target;

				Set<Object> sourceEdges = defSourcePort.getEdges();
				for (Object edge : sourceEdges) {
					if (edge instanceof DefaultEdge) {
						DefaultEdge testEdge = (DefaultEdge) edge;
						if (((testEdge.getSource() == source) && (testEdge
								.getTarget() == target))
								|| ((testEdge.getSource() == target) && (testEdge
										.getTarget() == source))) {

							return null;
						}

					}
				}

			}
		}

		DefaultEdge edge = new DefaultEdge();
		if (getModel().acceptsSource(edge, source)
				&& getModel().acceptsTarget(edge, target)) {
			/*
			 * Creates edge attributes based on the edge information
			 */
			edge.getAttributes().applyMap(createEdgeAttributes());
			// Insert the Edge and its Attributes
			getGraphLayoutCache().insertEdge(edge, source, target);
			// ((EdgeInformation)edge.getUserObject()).setJGraphEdge(edge);
			return edge;
		}
		return null;
	}

	private Map createEdgeAttributes() {
		Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLabelAlongEdge(map, true);
		GraphConstants.setDisconnectable(map, false);
		// Add a label along edge attribute

		return map;
	}

	/**
	 * If vertices is selected then remove all selected verteces and edges and
	 * all edges that is asociated with the selected edges. If only edges is
	 * selected then remove all selected edges.
	 * 
	 */
	public void removeSelected() {
		if (!isSelectionEmpty()) {
			Object[] cells = getSelectionCells();
			cells = getDescendants(cells);
			getGraphLayoutCache().remove(cells);

		}

	}

	/**
	 * 
	 * Performs cut action on the the graph. E is the event that makes the cut
	 * event to happen.
	 * 
	 * @param e
	 */
	public void cut(ActionEvent e) {
		if (!isSelectionEmpty()) {
			ActionEvent event = new ActionEvent(this, e.getID(), e
					.getActionCommand(), e.getModifiers());
			cut.actionPerformed(event);
		}
	}

	public void paste(ActionEvent e) {
		ActionEvent event = new ActionEvent(this, e.getID(), e
				.getActionCommand(), e.getModifiers());
		paste.actionPerformed(event);
	}

	public void copy(ActionEvent e) {
		ActionEvent event = new ActionEvent(this, e.getID(), e
				.getActionCommand(), e.getModifiers());
		copy.actionPerformed(event);

	}

	/**
	 * Overrides the default cloneCells method so it copies the user objects
	 * also.
	 * 
	 */
	public Map cloneCells(Object[] cells) {

		Map cellMap = super.cloneCells(cells);
		Set cellsToClone = cellMap.entrySet();
		Iterator iter = cellsToClone.iterator();
		Object obj, cell;
		LinkedList<DefaultEdge> edgeCells = new LinkedList<DefaultEdge>();
		LinkedList<DefaultGraphCell> vertexCells = new LinkedList<DefaultGraphCell>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			obj = entry.getValue();
			cell = entry.getKey();
			if (obj instanceof DefaultGraphCell) {
				DefaultGraphCell clonedCell = (DefaultGraphCell) obj;
				if (clonedCell.getUserObject() instanceof VertexInformation) {
					vertexCells.add(clonedCell);
				} else if (clonedCell.getUserObject() instanceof EdgeInformation) {
					edgeCells.add((DefaultEdge) clonedCell);
				}

			}
		}

		for (DefaultGraphCell vertexCell : vertexCells) {
			VertexInformation cellObject = (VertexInformation) vertexCell
					.getUserObject();
			VertexInformation clonedCellObject = (VertexInformation) cellObject
					.clone();
			vertexCell.setUserObject(clonedCellObject);
			clonedCellObject.setJGraphVertex(vertexCell);
		}

		for (DefaultEdge edgeCell : edgeCells) {
			EdgeInformation cellObject = (EdgeInformation) edgeCell
					.getUserObject();
			EdgeInformation clonedCellObject = (EdgeInformation) cellObject
					.clone();
			edgeCell.setUserObject(clonedCellObject);
			clonedCellObject.setJGraphEdge(edgeCell);
		}

		return cellMap;
	}

	/**
	 * Undo the last registered undoable event on the graph
	 */
	public void undo() {
		if (undoManager.canUndo())
			undoManager.undo();

	}

	/**
	 * Redo the last undid event on the graph
	 */
	public void redo() {
		if (undoManager.canRedo())
			undoManager.redo();

	}

	/**
	 * Returns the JGraphModelAdapter used as model for this component
	 * 
	 * @return
	 */
	public JGraphModelAdapter<VertexInformation, EdgeInformation> getJGraphTModelAdapter() {
		return jgAdapter;

	}

	/**
	 * Return true if the current graph has been changed
	 * 
	 * @return the changed
	 */
	public boolean isChanged() {
		/*
		 * If its posible to undo then the graph is changed.
		 */
		return undoManager.canUndo();
	}

	/**
	 * Zoom in the graph the amunt that is specified in the
	 * GlobalProperties.SCALE_INTERVALL constant.
	 */
	public void zoomIn() {
		double scale = getScale() * (1.0 + GlobalProperties.SCALE_INTERVALL);
		this.setScale(scale);
		graphProperties.setZoomLevel(scale);
		graphProperties.fireChangedEvent();
	}

	/**
	 * Zoom out the graph the amunt that is specified in the
	 * GlobalProperties.SCALE_INTERVALL constant.
	 * 
	 */
	public void zoomOut() {
		double scale = getScale() * (1.0 - GlobalProperties.SCALE_INTERVALL);
		this.setScale(scale);
		graphProperties.setZoomLevel(scale);
		graphProperties.fireChangedEvent();
	}

	/**
	 * Sets to default zoom level. This means that the scale factor is set to
	 * 1.0.
	 */
	public void setDefaultZoomLevel() {
		if (getScale() != 1.0) {
			double scale = 1.0;
			this.setScale(scale);
			graphProperties.setZoomLevel(scale);
			graphProperties.fireChangedEvent();
		}

	}

	/**
	 * This method returns the GraphProperties object for this graph component.
	 * 
	 * @return the graphProperties
	 */
	public GraphProperties getGraphProperties() {
		return graphProperties;
	}

	/**
	 * Sets the GraphProperties to another. This is used when a graph is loaded
	 * from a file.
	 * 
	 * @param graphProperties
	 *            the graphProperties to set
	 */
	public void setGraphProperties(GraphProperties graphProperties) {
		this.graphProperties = graphProperties;
		for (GraphPropertiesChangeListener l : graphPropertiesChangeListeners)
			l.graphPropertiesChanged();
	}

	public void addGraphPropertiesChangeListener(GraphPropertiesChangeListener l) {
		graphPropertiesChangeListeners.add(l);
	}

	public void addUndoAndRedoAbleListener(UndoAndRedoAbleListener l) {
		undoManager.addUndoAndRedoAbleListener(l);

	}

	/**
	 * Returns the center of the graph. That is the center of the bounding
	 * rectangle of the graph
	 * 
	 * @return
	 */
	public Point2D.Double getGraphCenterPoint() {
		Object[] vertices = getGraphLayoutCache().getCells(false, true, false,
				false);
		Rectangle2D rec = getCellBounds(vertices);

		return new Point2D.Double(rec.getCenterX(), rec.getCenterY());
	}

	/**
	 * @return the mainFrame
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * Returns the graph cell that is the destination of the edge specified as a
	 * parameter
	 * 
	 * @param edge
	 * @return
	 */
	protected DefaultGraphCell getDest(DefaultEdge edge) {
		Object port = edge.getSource();
		if (port instanceof DefaultPort) {
			DefaultPort portIns = (DefaultPort) port;

			if (portIns.getRoot() instanceof DefaultGraphCell) {
				DefaultGraphCell destVertex = (DefaultGraphCell) portIns
						.getRoot();
				return destVertex;
			}

		}
		return null;
	}

	/**
	 * Returns the graph cell that is the source of the edge specified as
	 * 
	 * @param edge
	 * @return
	 */
	protected DefaultGraphCell getSource(DefaultEdge edge) {
		Object port = edge.getTarget();
		if (port instanceof DefaultPort) {
			DefaultPort portIns = (DefaultPort) port;

			if (portIns.getRoot() instanceof DefaultGraphCell) {
				DefaultGraphCell destVertex = (DefaultGraphCell) portIns
						.getRoot();
				return destVertex;
			}

		}
		return null;
	}

	/**
	 * This returns false if an operation is pending so it isn't posible to
	 * start a new one.
	 * 
	 * @return
	 */
	synchronized public boolean isReadyForOperation() {
		return graphOperationHelper.isReadyForOperation();
	}

	/**
	 * Removes the selected edges and inserts verticesBetween vertices between
	 * the vertices that is connected by the edge.
	 * 
	 * @param verticesBetween
	 */
	public void splitSelectedEdges(int verticesBetween) {
		graphOperationHelper.splitSelectedEdges(verticesBetween);
	}

	/**
	 * Does a complete bopartite graph operation. Uses the InformationPane to
	 * instruct the user on how to select vertices
	 * 
	 */
	public void doCompleteBipartiteGraphOperation() {
		graphOperationHelper.doCompleteBipartiteGraphOperation();
	}

	/**
	 * Does a graph cartesian prduct. Uses the InformationPane to instruct the
	 * user on how to select vertices.
	 */
	public void doGraphCartesianProductOperation() {
		graphOperationHelper.doGraphCartesianProductOperation();

	}

	/**
	 * Does a graph catergorial prduct. Uses the InformationPane to instruct the
	 * user on how to select vertices.
	 */
	public void doGraphCatergorialProductOperation() {
		graphOperationHelper.doGraphCatergorialProductOperation();

	}

	/**
	 * Makes the selected part of the graph complete.
	 */
	public void makeSubgraphCompleteOperation() {
		graphOperationHelper.makeSubgraphCompleteOperation();

	}

	/**
	 * Places the selected vertices in a circle.
	 * 
	 */
	public void placeSelectedVerticesInCircle() {
		graphOperationHelper.placeSelectedVerticesInCircle();

	}

	/**
	 * Mirror the selected vertices on a vertical axis
	 * 
	 */
	public void mirrorSelectedVerticesVertical() {
		graphOperationHelper.mirrorSelectedVerticesVertical();

	}

	/**
	 * Mirror the selcted vertices on a horizental axis
	 */
	public void mirrorSelectedVerticesHorizontal() {
		graphOperationHelper.mirrorSelectedVerticesHorizontal();

	}

	/**
	 * Rotate the selected vertices. Brings up a rotation dialog so the user can
	 * interactivly rotate the vertices with.
	 * 
	 */
	public void rotateSelectedVertices() {
		graphOperationHelper.rotateSelectedVertices();

	}

	/**
	 * This method is used to tell the undo manager to igonore next undoable
	 * edit
	 * 
	 */
	void ignoreUndoableOnNextEdit() {
		undoManager.ignoreUndoableOnNextEdit();

	}

	/**
	 * Selects all vertices and edges in the graph
	 */
	public void selectAll() {
		graphOperationHelper.selectAll();
	}

	/**
	 * Selects all neighour vertices of selected verices
	 * 
	 */
	public void selectNeighbour() {
		graphOperationHelper.selectNeighbour();
	}

	/**
	 * Select the compliment of selected verices and deselect all previus
	 * selected vertices.
	 */
	public void selectOther() {
		graphOperationHelper.selectOther();
	}

	/**
	 * Selects reachable vertices in the graph
	 */
	public void selectReachable() {
		graphOperationHelper.selectReachable();
	}
	public void expandByFactor(double factor) {
		graphOperationHelper.expandByFactor(factor);
	
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		Color background = bg;
		Color marqueeColor = new Color(255 - background.getRed(), 255 - background.getGreen(), 255 - background.getBlue());
		this.setMarqueeColor(marqueeColor);
		
	}

}