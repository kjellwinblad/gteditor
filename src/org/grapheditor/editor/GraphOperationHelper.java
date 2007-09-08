/*
 * Copyright (C) 2007  Kjell Winblad (kjellwinblad@gmail.com)
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.VertexInformation.ErrorWhenFetchingVertexInformation;
import org.grapheditor.editor.importers.MathematicaImporter.InvalidFile;
import org.grapheditor.gui.CancelAbleProcess;
import org.grapheditor.gui.CancelAbleProcessDialog;
import org.grapheditor.gui.StatusNotifier;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Port;

/**
 * A class that is only visible in this package. It is used by the
 * GraphEditorPane to perorm operations on itself. It contains methods to perorm
 * various operations on the graph.
 * 
 * @author kjellw
 */
class GraphOperationHelper {

	private GraphEditorPane graphPane;

	private boolean readyForOperation = true;

	private InformationPane informationPane;

	private SelectVertexKeyAdapter vertexSelectKeyAdapter;

	public GraphOperationHelper(GraphEditorPane graphPane,
			InformationPane informationPane) {
		this.graphPane = graphPane;
		this.informationPane = informationPane;
		vertexSelectKeyAdapter = new SelectVertexKeyAdapter();
		graphPane.addKeyListener(vertexSelectKeyAdapter);
	}

	synchronized public boolean isReadyForOperation() {
		return readyForOperation;
	}

	/**
	 * @param readyForOperation
	 *            the readyForOperation to set
	 */
	synchronized private void setReadyForOperation(boolean readyForOperation) {

		this.readyForOperation = readyForOperation;

	}

	/**
	 * Does a complete bopartite graph operation
	 * 
	 */
	public void doCompleteBipartiteGraphOperation() {
		if (isReadyForOperation())
			setReadyForOperation(false);
		else
			return;

		new Thread(new Runnable() {
			public void run() {
				try {
					List<DefaultGraphCell>[] vertexLists = getTwoVertexListsFromUser();
					final List<DefaultGraphCell> list1 = vertexLists[0];
					final List<DefaultGraphCell> list2 = vertexLists[1];
					// Start to connect all vetices in one graph with the
					// vertices in the other

					new CancelAbleProcessDialog(
							graphPane.getMainFrame(),
							new CancelAbleProcess() {

								public void setStatusNotifier(StatusNotifier n) {

								}

								public void run() {
									for (DefaultGraphCell graphCell : list1) {

										for (DefaultGraphCell connectGraphCell : list2) {
											graphPane.connect((Port) graphCell
													.getChildAt(0),
													(Port) connectGraphCell
															.getChildAt(0));
										}

									}
									setReadyForOperation(true);
								}

							},
							"Performs creation of complete bipartite graph. This can take long time depending on the graph sizes.");

				} catch (CancelException e) {
					setReadyForOperation(true);
					// e.printStackTrace();
				}

			}
		}).start();

	}

	private List<DefaultGraphCell>[] getTwoVertexListsFromUser()
			throws CancelException {
		MessageProvider completeBipartiteGraphOperationProvider = new MessageProvider(
				"Select first vertex set and press enter. Press ESC to cancel.");
		completeBipartiteGraphOperationProvider.setSpecialColor(new Color(180,
				255, 180));
		informationPane.putMessage(completeBipartiteGraphOperationProvider);
		List<DefaultGraphCell> vertexSetA = null;
		List<DefaultGraphCell> vertexSetB = null;
		try {
			vertexSetA = selectVertexSet();
			if (vertexSetA.size() == 0)
				throw new CancelException();
			completeBipartiteGraphOperationProvider
					.setMessage("Select second vertex set and press enter. Press ESC to cancel.");
			graphPane.clearSelection();
			vertexSetB = selectVertexSet(new HashSet<Object>(vertexSetA));
			if (vertexSetB.size() == 0)
				throw new CancelException();
		} catch (CancelException e) {
			informationPane.popMessage();
			throw new CancelException();
		}

		informationPane.popMessage();

		List[] sets = { vertexSetA, vertexSetB };
		return sets;

	}

	private class CancelException extends Exception {
	};

	private enum KeyType {
		NO_OPTION_KEY, CANCEL_KEY, APROVE_KEY
	}

	private List<DefaultGraphCell> selectVertexSet(
			Set<Object> notAbleToSelectSet) throws CancelException {
		// Sets the not able to select sets so it is not posible to selects
		// cells
		// in it

		Hashtable<Object, Object> nestedMap = new Hashtable<Object, Object>();
		for (Object o : notAbleToSelectSet) {
			Hashtable<Object, Object> map = new Hashtable<Object, Object>();
			if (o instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) o;
				GraphEditorConstants.setSelectable(map, false);
			}
			nestedMap.put(o, map);
		}
		graphPane.getGraphLayoutCache().edit(nestedMap);

		KeyType keyPressed = vertexSelectKeyAdapter.getKeyAlternative();

		if (keyPressed == KeyType.CANCEL_KEY)
			throw new CancelException();

		// Vertex set is selected and approve key is typed
		Object[] selectedVertices = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		List<DefaultGraphCell> selectedVertexList = null;

		if (selectedVertices == null || selectedVertices.length == 0)
			selectedVertexList = new LinkedList<DefaultGraphCell>();
		else {
			selectedVertexList = new LinkedList();
			for (Object o : selectedVertices) {
				if (o instanceof DefaultGraphCell) {
					DefaultGraphCell cell = (DefaultGraphCell) o;
					selectedVertexList.add(cell);
				}

			}

		}
		// Make it possible to select cells in it again
		nestedMap = new Hashtable<Object, Object>();
		for (Object o : notAbleToSelectSet) {
			Hashtable<Object, Object> map = new Hashtable<Object, Object>();
			if (o instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) o;
				GraphEditorConstants.setSelectable(map, true);
			}
			nestedMap.put(o, map);
		}
		graphPane.getGraphLayoutCache().edit(nestedMap);

		return selectedVertexList;
	}

	private List<DefaultGraphCell> selectVertexSet() throws CancelException {
		return selectVertexSet(new HashSet<Object>());
	}

	class SelectVertexKeyAdapter extends KeyAdapter {
		private KeyType keyIsTyped = KeyType.NO_OPTION_KEY;

		boolean keyAlternativeRequested = false;

		public void keyReleased(KeyEvent e) {
			if (!keyAlternativeRequested)
				return;
			if ((e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
				
				keyIsTyped = KeyType.CANCEL_KEY;
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER)
				keyIsTyped = KeyType.APROVE_KEY;
		}

		synchronized KeyType getTypedKey() {
			return keyIsTyped;
		}

		synchronized void setTypedKey(KeyType k) {
			keyIsTyped = k;
		}

		synchronized public KeyType getKeyAlternative() {
			keyAlternativeRequested = true;
			while (getTypedKey() == KeyType.NO_OPTION_KEY)
				Thread.yield();
			KeyType type = getTypedKey();
			setTypedKey(KeyType.NO_OPTION_KEY);
			keyAlternativeRequested = false;
			return type;
		}
	}

	public void doGraphCartesianProductOperation() {
		if (isReadyForOperation())
			setReadyForOperation(false);
		else
			return;

		new Thread(new Runnable() {
			public void run() {
				try {
					List<DefaultGraphCell>[] vertexLists = getTwoVertexListsFromUser();
					final List<DefaultGraphCell> list1 = vertexLists[0];
					final List<DefaultGraphCell> list2 = vertexLists[1];
					// Start to connect all vetices in one graph with the
					// vertices in the other

					new CancelAbleProcessDialog(
							graphPane.getMainFrame(),
							new CancelAbleProcess() {

								public void setStatusNotifier(StatusNotifier n) {

								}

								public void run() {
									doGraphCartesianProductOperation(list1,
											list2);
									setReadyForOperation(true);
								}

							},
							"Performs graph cartesian product. This can take long time depending on the graph sizes.");

				} catch (CancelException e) {

					// e.printStackTrace();
					setReadyForOperation(true);
				}

			}

		}).start();

	}

	private class VertexTuple {
		private VertexInformation v1;

		private VertexInformation v2;

		public VertexTuple(VertexInformation v1, VertexInformation v2) {
			try {
				v1.fetchInformationFromJGraph();
				v2.fetchInformationFromJGraph();
			} catch (ErrorWhenFetchingVertexInformation e) {

				e.printStackTrace();
			}
			this.v1 = v1;
			this.v2 = v2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof VertexTuple) {
				VertexTuple o = (VertexTuple) obj;
				return (((o.getV1() == v1) && (o.getV2() == v2)) || ((o.getV1() == v2) && (o
						.getV2() == v1)));

			}
			return false;
		}

		@Override
		public int hashCode() {

			return v1.hashCode() + v2.hashCode();
		}

		/**
		 * @return the v1
		 */
		public VertexInformation getV1() {
			return v1;
		}

		/**
		 * @return the v2
		 */
		public VertexInformation getV2() {
			return v2;
		}

	}

	private void doGraphCartesianProductOperation(List<DefaultGraphCell> list1,
			List<DefaultGraphCell> list2) {

		// Finds the origo of the resulting graph
		Object[] vertices = graphPane.getGraphLayoutCache().getCells(false,
				true, false, false);
		Rectangle2D rec = graphPane.getCellBounds(vertices);

		Point2D.Double origo = new Point2D.Double(30, rec.getMaxY() + 30);
		// Prev origo is the top left corner of the bounding sphere of the first
		// vertex list operator
		vertices = list2.toArray();
		rec = graphPane.getCellBounds(vertices);
		Point2D.Double graph2Origo = new Point2D.Double(rec.getX(), rec.getY());

		vertices = list1.toArray();
		rec = graphPane.getCellBounds(vertices);
		Point2D.Double graph1Origo = new Point2D.Double(rec.getX(), rec.getY());

		// Two maps. One from the objects in the v1 cell of the tuples in
		// newVertexCell
		// to all VertexTuple's that has the object as v1 element
		// And another from the objects in the v2 cell of the tuples in
		// newVertexCell
		// to all VertexTuple's that has the object as v2 element
		Map<VertexInformation, Set<VertexInformation>> leftMapping = new HashMap<VertexInformation, Set<VertexInformation>>();
		Map<VertexInformation, Set<VertexInformation>> rightMapping = new HashMap<VertexInformation, Set<VertexInformation>>();

		// Vertex mapping is a mapping from the new vertex identification to a
		// "fysical" vertex.
		Map<VertexTuple, DefaultGraphCell> vertexMapping = new HashMap<VertexTuple, DefaultGraphCell>();

		for (DefaultGraphCell graphCell : list1) {
			for (DefaultGraphCell connectGraphCell : list2) {
				Object v1Object = graphCell.getUserObject();
				if (v1Object instanceof VertexInformation) {
					VertexInformation v1Vertex = (VertexInformation) v1Object;

					Object v2Object = connectGraphCell.getUserObject();
					if (v2Object instanceof VertexInformation) {
						VertexInformation v2Vertex = (VertexInformation) v2Object;

						VertexTuple newVertexTupe = new VertexTuple(v1Vertex,
								v2Vertex);

						Set<VertexInformation> leftSet = leftMapping
								.get(v1Vertex);
						if (leftSet == null)
							leftSet = new HashSet<VertexInformation>();

						leftSet.add(v2Vertex);
						leftMapping.put(v1Vertex, leftSet);

						Set<VertexInformation> rightSet = rightMapping
								.get(v2Vertex);
						if (rightSet == null)
							rightSet = new HashSet<VertexInformation>();
						rightSet.add(v1Vertex);
						rightMapping.put(v2Vertex, rightSet);

						// Create the "fysical" vertex
						DefaultGraphCell newGraphCell = graphPane
								.addDefaultVertexAt(origo.getX()
										+ v1Vertex.getXPosition()
										+ v2Vertex.getXPosition()
										- graph1Origo.getX()
										- graph2Origo.getX(), origo.getY()
										+ v1Vertex.getYPosition()
										+ v2Vertex.getYPosition()
										- graph1Origo.getY()
										- graph2Origo.getY());
						vertexMapping.put(newVertexTupe, newGraphCell);

					}

				}
			}

		}
		// Now create the right connection based on the condision:
		// any two vertices (u,u') and (v,v') are adjacent in G x H if and only
		// if either
		// u = v and u' is adjacent with v' , or
		// u' = v' and u is adjacent with v.
		cartesianProductConnectCorrectVertices(leftMapping, vertexMapping);
		cartesianProductConnectCorrectVertices(rightMapping, vertexMapping);

	}

	private void cartesianProductConnectCorrectVertices(
			Map<VertexInformation, Set<VertexInformation>> mapping,
			Map<VertexTuple, DefaultGraphCell> vertexMapping) {
		Set<Map.Entry<VertexInformation, Set<VertexInformation>>> entrySet = mapping
				.entrySet();
		for (Map.Entry<VertexInformation, Set<VertexInformation>> entry : entrySet) {
			// VertexTuple

			VertexInformation lookUpVertex = entry.getKey();
			Set<VertexInformation> intrestingVertices = entry.getValue();
			for (VertexInformation testIfNeighborInSet : intrestingVertices) {
				Set<EdgeInformation> edges = graphPane.getJGraphTGraph()
						.edgesOf(testIfNeighborInSet);

				for (EdgeInformation edge : edges) {
					VertexInformation neightbor;
					neightbor = graphPane.getJGraphTGraph().getEdgeSource(edge);
					if (neightbor == lookUpVertex)
						neightbor = graphPane.getJGraphTGraph().getEdgeTarget(
								edge);
					if (intrestingVertices.contains(neightbor)) {
						// Shall create a edge between the cooresponding
						// vertices
						VertexTuple newConnectionVertex1 = new VertexTuple(
								lookUpVertex, neightbor);
						VertexTuple newConnectionVertex2 = new VertexTuple(
								lookUpVertex, testIfNeighborInSet);
						graphPane.connect((Port) vertexMapping.get(
								newConnectionVertex1).getChildAt(0),
								(Port) vertexMapping.get(newConnectionVertex2)
										.getChildAt(0));
					}

				}
			}
		}

	}

	public void doGraphCatergorialProductOperation() {
		if (isReadyForOperation())
			setReadyForOperation(false);
		else
			return;

		new Thread(new Runnable() {
			public void run() {
				try {
					List<DefaultGraphCell>[] vertexLists = getTwoVertexListsFromUser();
					final List<DefaultGraphCell> list1 = vertexLists[0];
					final List<DefaultGraphCell> list2 = vertexLists[1];
					// Start to connect all vetices in one graph with the
					// vertices in the other

					new CancelAbleProcessDialog(
							graphPane.getMainFrame(),
							new CancelAbleProcess() {

								public void setStatusNotifier(StatusNotifier n) {

								}

								public void run() {
									doGraphCatergorialProductOperation(list1,
											list2);
									setReadyForOperation(true);
								}

							},
							"Performs graph catergorial product. This can take long time depending on the graph sizes.");

				} catch (CancelException e) {
					// e.printStackTrace();
					setReadyForOperation(true);
				}

			}

		}).start();

	}

	private void doGraphCatergorialProductOperation(
			List<DefaultGraphCell> list1, List<DefaultGraphCell> list2) {

		// Finds the origo of the resulting graph
		Object[] vertices = graphPane.getGraphLayoutCache().getCells(false,
				true, false, false);
		Rectangle2D rec = graphPane.getCellBounds(vertices);

		Point2D.Double origo = new Point2D.Double(30, rec.getMaxY() + 30);
		// Prev origo is the top left corner of the bounding sphere of the first
		// vertex list operator
		vertices = list2.toArray();
		rec = graphPane.getCellBounds(vertices);
		Point2D.Double graph2Origo = new Point2D.Double(rec.getX(), rec.getY());

		vertices = list1.toArray();
		rec = graphPane.getCellBounds(vertices);
		Point2D.Double graph1Origo = new Point2D.Double(rec.getX(), rec.getY());

		// Vertex mapping is a mapping from the new vertex identification to a
		// "fysical" vertex.
		Map<VertexTuple, DefaultGraphCell> vertexMapping = new HashMap<VertexTuple, DefaultGraphCell>();
		Set<VertexInformation> graph1Vertices = new HashSet<VertexInformation>();
		Set<VertexInformation> graph2Vertices = new HashSet<VertexInformation>();

		for (DefaultGraphCell graphCell : list1) {
			for (DefaultGraphCell connectGraphCell : list2) {
				Object v1Object = graphCell.getUserObject();
				if (v1Object instanceof VertexInformation) {
					VertexInformation v1Vertex = (VertexInformation) v1Object;

					Object v2Object = connectGraphCell.getUserObject();
					if (v2Object instanceof VertexInformation) {
						VertexInformation v2Vertex = (VertexInformation) v2Object;
						graph1Vertices.add(v1Vertex);
						graph2Vertices.add(v2Vertex);
						VertexTuple newVertexTupe = new VertexTuple(v1Vertex,
								v2Vertex);

						// Create the "fysical" vertex
						DefaultGraphCell newGraphCell = graphPane
								.addDefaultVertexAt(origo.getX()
										+ v1Vertex.getXPosition()
										+ v2Vertex.getXPosition()
										- graph1Origo.getX()
										- graph2Origo.getX(), origo.getY()
										+ v1Vertex.getYPosition()
										+ v2Vertex.getYPosition()
										- graph1Origo.getY()
										- graph2Origo.getY());
						vertexMapping.put(newVertexTupe, newGraphCell);

					}

				}
			}

		}

		Set<Map.Entry<VertexTuple, DefaultGraphCell>> tuples = vertexMapping
				.entrySet();
		for (Map.Entry<VertexTuple, DefaultGraphCell> entry : tuples) {
			VertexTuple vertexTuple = entry.getKey();
			DefaultGraphCell vertexTupleCell = entry.getValue();

			Set<EdgeInformation> edgesV1 = graphPane.getJGraphTGraph().edgesOf(
					vertexTuple.getV1());
			for (EdgeInformation v1edge : edgesV1) {
				VertexInformation neightbor1;
				neightbor1 = graphPane.getJGraphTGraph().getEdgeSource(v1edge);
				if (neightbor1 == vertexTuple.getV1())
					neightbor1 = graphPane.getJGraphTGraph().getEdgeTarget(
							v1edge);

				if (graph1Vertices.contains(neightbor1)) {
					// Shall create a edge between the cooresponding vertices
					Set<EdgeInformation> edgesV2 = graphPane.getJGraphTGraph()
							.edgesOf(vertexTuple.getV2());
					for (EdgeInformation v2edge : edgesV2) {
						VertexInformation neightbor2;
						neightbor2 = graphPane.getJGraphTGraph().getEdgeSource(
								v2edge);
						if (neightbor2 == vertexTuple.getV2())
							neightbor2 = graphPane.getJGraphTGraph()
									.getEdgeTarget(v2edge);
						if (graph2Vertices.contains(neightbor2)) {
							// Shall create a edge between the cooresponding
							// vertices
							VertexTuple connectToId = new VertexTuple(
									neightbor1, neightbor2);
							DefaultGraphCell connectToCell = vertexMapping
									.get(connectToId);
							graphPane.connect((Port) vertexTupleCell
									.getChildAt(0), (Port) connectToCell
									.getChildAt(0));

						}

					}

				}

			}

		}

	}

	public void makeSubgraphCompleteOperation() {
		if (isReadyForOperation())
			setReadyForOperation(false);
		else
			return;

		new Thread(new Runnable() {
			public void run() {
				try {

					final List<DefaultGraphCell> vertexList = new LinkedList<DefaultGraphCell>();
					Object[] selectedVertices = graphPane
							.getSelectionCells(graphPane.getGraphLayoutCache()
									.getCells(false, true, false, false));

					for (Object o : selectedVertices)
						vertexList.add((DefaultGraphCell) o);

					if (vertexList.size() == 0)
						throw new CancelException();

					// Start to connect all vetices in one graph with the
					// vertices in the other

					new CancelAbleProcessDialog(
							graphPane.getMainFrame(),
							new CancelAbleProcess() {

								public void setStatusNotifier(StatusNotifier n) {

								}

								public void run() {
									for (DefaultGraphCell v1 : vertexList) {
										for (DefaultGraphCell v2 : vertexList) {
											graphPane.connect((Port) v1
													.getChildAt(0), (Port) v2
													.getChildAt(0));
										}
									}
									setReadyForOperation(true);
								}

							},
							"Make subgraph complete. This can take long time depending on the graph size.");

				} catch (CancelException e) {
					setReadyForOperation(true);
				}

			}

		}).start();
	}

	public void placeSelectedVerticesInCircle() {
		Object[] selectedVertices = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		if (selectedVertices == null || selectedVertices.length == 0)
			return;
		// Place the circle within the bounds of the cellse
		Rectangle2D bounds = graphPane.getCellBounds(selectedVertices);
		double centerX = bounds.getCenterX();
		double centerY = bounds.getCenterY();
		double radius = (bounds.getWidth() > bounds.getHeight() ? bounds
				.getWidth() / 2 : bounds.getHeight() / 2);

		double piT2 = Math.PI * 2;
		double distanceBetweenVertices = piT2 / selectedVertices.length;
		double place = 0;
		Map<Object, Object> nestadMap = new HashMap<Object, Object>();

		for (Object selectedVertex : selectedVertices) {
			if (selectedVertex instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) selectedVertex;
				Rectangle2D prevBoundes = GraphEditorConstants.getBounds(cell
						.getAttributes());
				Map<Object, Object> attrMap = new HashMap<Object, Object>();
				GraphEditorConstants.setBounds(attrMap, new Rectangle2D.Double(
						Math.cos(place) * radius + centerX, Math.sin(place)
								* radius + centerY, prevBoundes.getWidth(),
						prevBoundes.getHeight()));
				nestadMap.put(cell, attrMap);
			}

			place = place + distanceBetweenVertices;
		}
		graphPane.getGraphLayoutCache().edit(nestadMap);
	}

	public void mirrorSelectedVerticesVertical() {

		Object[] selectedVertices = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		if (selectedVertices == null || selectedVertices.length == 0)
			return;
		// Place the circle within the bounds of the cellse
		Rectangle2D bounds = graphPane.getCellBounds(selectedVertices);

		double zeroPointPlusMaxPoint = bounds.getMinX() + bounds.getMaxX();

		Map<Object, Object> nestadMap = new HashMap<Object, Object>();

		for (Object selectedVertex : selectedVertices) {
			if (selectedVertex instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) selectedVertex;
				Rectangle2D prevBoundes = GraphEditorConstants.getBounds(cell
						.getAttributes());
				double prevX = prevBoundes.getX();
				double prevY = prevBoundes.getY();
				Map<Object, Object> attrMap = new HashMap<Object, Object>();
				GraphEditorConstants
						.setBounds(attrMap,
								new Rectangle2D.Double(zeroPointPlusMaxPoint
										- prevX - prevBoundes.getWidth(),
										prevY, prevBoundes.getWidth(),
										prevBoundes.getHeight()));
				nestadMap.put(cell, attrMap);
			}
		}
		graphPane.getGraphLayoutCache().edit(nestadMap);

	}

	public void mirrorSelectedVerticesHorizontal() {
		Object[] selectedVertices = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		if (selectedVertices == null || selectedVertices.length == 0)
			return;
		// Place the circle within the bounds of the cellse
		Rectangle2D bounds = graphPane.getCellBounds(selectedVertices);

		double zeroPointPlusMaxPoint = bounds.getMinY() + bounds.getMaxY();

		Map<Object, Object> nestadMap = new HashMap<Object, Object>();

		for (Object selectedVertex : selectedVertices) {
			if (selectedVertex instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) selectedVertex;
				Rectangle2D prevBoundes = GraphEditorConstants.getBounds(cell
						.getAttributes());
				double prevX = prevBoundes.getX();
				double prevY = prevBoundes.getY();
				Map<Object, Object> attrMap = new HashMap<Object, Object>();
				GraphEditorConstants.setBounds(attrMap, new Rectangle2D.Double(
						prevX, zeroPointPlusMaxPoint - prevY
								- prevBoundes.getHeight(), prevBoundes
								.getWidth(), prevBoundes.getHeight()));
				nestadMap.put(cell, attrMap);
			}
		}
		graphPane.getGraphLayoutCache().edit(nestadMap);
		
	}

	public void rotateSelectedVertices() {

		Object[] selectedVertices = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		if (selectedVertices == null || selectedVertices.length == 0)
			return;
		RotateDialog.show(graphPane, selectedVertices);

	}

	public void splitSelectedEdges(int verticesBetween) {
		if (verticesBetween < 1)
			return;

		Object[] selectedEdges = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, false, false, true));
		for (Object o : selectedEdges) {
			if (o instanceof DefaultEdge) {
				DefaultEdge edge = (DefaultEdge) o;
				splitEdge(edge, verticesBetween);
			}

		}

	}

	private void splitEdge(DefaultEdge edge, int verticesBetween) {

		List<Object> modifiedCells = new LinkedList<Object>();
		DefaultGraphCell source = graphPane.getSource(edge);
		modifiedCells.add(source);
		DefaultGraphCell dest = graphPane.getDest(edge);
		modifiedCells.add(dest);
		Object[] edgeCell = { edge };
		graphPane.getGraphLayoutCache().remove(edgeCell);
		Rectangle2D bounds = GraphEditorConstants.getBounds(source
				.getAttributes());
		Point2D sourcePos = new Point2D.Double(bounds.getCenterX(), bounds
				.getCenterY());
		bounds = GraphEditorConstants.getBounds(dest.getAttributes());
		Point2D destPos = new Point2D.Double(bounds.getCenterX(), bounds
				.getCenterY());

		double correctTermX = bounds.getWidth() / 2;
		double correctTermY = bounds.getHeight() / 2;

		double deltaX = destPos.getX() - sourcePos.getX();
		double deltaY = destPos.getY() - sourcePos.getY();

		double xStepSize = deltaX / (verticesBetween + 1);
		double yStepSize = deltaY / (verticesBetween + 1);

		double insertPosX = xStepSize + sourcePos.getX();
		double insertPosY = yStepSize + sourcePos.getY();
		DefaultGraphCell prevCell = source;
		DefaultGraphCell currentCell = null;
		for (int n = 0; n < verticesBetween; n++) {
			// Insert vertex
			currentCell = graphPane.addDefaultVertexAt(insertPosX
					- correctTermX + bounds.getCenterX() - bounds.getX(),
					insertPosY - correctTermY + bounds.getCenterY()
							- bounds.getY());
			modifiedCells.add(currentCell);
			// Connect
			modifiedCells.add(graphPane.connect((Port) prevCell.getChildAt(0),
					(Port) currentCell.getChildAt(0)));

			prevCell = currentCell;
			// Increase insert pos
			insertPosX = insertPosX + xStepSize;
			insertPosY = insertPosY + yStepSize;
		}
		modifiedCells.add(graphPane.connect((Port) currentCell.getChildAt(0),
				(Port) dest.getChildAt(0)));
		graphPane.setSelectionCells(modifiedCells.toArray());

	}

	public void selectReachable() {
		if (graphPane.isSelectionEmpty())
			return;
		Object selectedVertices[] = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));

		// A set with vertices that have to be traversed
		Set<DefaultGraphCell> notTraversed = new HashSet<DefaultGraphCell>();
		for (Object o : selectedVertices)
			notTraversed.add((DefaultGraphCell) o);

		// The set with vertices that have been traversed
		Set<DefaultGraphCell> traversed = new HashSet<DefaultGraphCell>();

		Object selectedEdges[] = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, false, false, true));

		for (Object o : selectedEdges) {
			if (o instanceof DefaultEdge) {
				DefaultEdge edge = (DefaultEdge) o;
				Object source = edge.getSource();
				if (source instanceof DefaultPort) {
					DefaultPort port = (DefaultPort) source;
					Object newNotTraversed = port.getParent();
					if (newNotTraversed instanceof DefaultGraphCell) {
						DefaultGraphCell vertex = (DefaultGraphCell) newNotTraversed;
						notTraversed.add(vertex);
					}
				}

			}
		}

		travererse(null, traversed, notTraversed);

		graphPane.setSelectionCells(traversed.toArray());

	}

	private void travererse(DefaultGraphCell traverseStart,
			Set<DefaultGraphCell> traversed, Set<DefaultGraphCell> notTraversed) {

		DefaultGraphCell traverseCell = traverseStart;
		if (traverseCell == null) {
			Iterator<DefaultGraphCell> iter = notTraversed.iterator();
			if (!iter.hasNext())
				return;
			traverseCell = iter.next();
			traversed.add(traverseCell);
			notTraversed.remove(traverseCell);
		} else {

		}
		traversed.add(traverseCell);
		notTraversed.remove(traverseCell);

		Object child = traverseCell.getChildAt(0);
		if (child instanceof DefaultPort) {
			DefaultPort port = (DefaultPort) child;
			Set<Object> edges = port.getEdges();
			for (Object edgeObject : edges) {
				if (edgeObject instanceof DefaultEdge) {
					DefaultEdge edge = (DefaultEdge) edgeObject;
					if (edge.getSource() == child) {
						// Traverse dest
						DefaultGraphCell cell = (DefaultGraphCell) ((DefaultPort) edge
								.getTarget()).getParent();
						if (!traversed.contains(cell)) {
							travererse(cell, traversed, notTraversed);
						}
					} else {
						// Traverse source
						DefaultGraphCell cell = (DefaultGraphCell) ((DefaultPort) edge
								.getSource()).getParent();
						if (!traversed.contains(cell)) {
							travererse(cell, traversed, notTraversed);
						}
					}

				}
			}
		}
		travererse(null, traversed, notTraversed);

	}

	/**
	 * Selects all vertices and edges in the graph
	 */
	public void selectAll() {
		Object cells[] = graphPane.getGraphLayoutCache().getCells(false, true,
				false, true);
		graphPane.setSelectionCells(cells);
	}

	/**
	 * Selects all neighour vertices of selected verices
	 * 
	 */
	public void selectNeighbour() {
		Object cells[] = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		List<Object> allSelections = new LinkedList<Object>();
		for (Object c : cells) {
			allSelections.add(c);
			List<Object> neighbours = graphPane.getGraphLayoutCache()
					.getNeighbours(c, null, false, true);
			allSelections.addAll(neighbours);

		}
		graphPane.setSelectionCells(allSelections.toArray());
	}

	/**
	 * Select the compliment of selected verices and deselect all previus
	 * selected vertices.
	 */
	public void selectOther() {
		Object selectedCells[] = graphPane.getSelectionCells(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		HashSet<Object> allCellsSet = new HashSet<Object>();

		Object allCells[] = graphPane.getGraphLayoutCache().getCells(false,
				true, false, false);
		for (Object c : allCells)
			allCellsSet.add(c);

		for (Object c : selectedCells)
			allCellsSet.remove(c);

		graphPane.setSelectionCells(allCellsSet.toArray());
	}
	
	 
		public void expandByFactor(double factor) {
			Object vertexCells[] = graphPane.getSelectionCells(graphPane
					.getGraphLayoutCache().getCells(false, true, false, false));

			Rectangle2D areaBounds = graphPane.getCellBounds(vertexCells);
			Hashtable<Object, Object> nestedMap = new Hashtable<Object, Object>();
			for (Object o : vertexCells) {
				Hashtable<Object, Object> map = new Hashtable<Object, Object>();
				if (o instanceof DefaultGraphCell) {
					DefaultGraphCell cell = (DefaultGraphCell) o;
					Rectangle2D bounds =
						GraphEditorConstants.getBounds(cell.getAttributes());
					Rectangle2D newBounds = new Rectangle2D.Double(
							areaBounds.getX() + (bounds.getX()-areaBounds.getX())*factor,
							areaBounds.getY() + (bounds.getY()-areaBounds.getY())*factor,
							bounds.getWidth(),
							bounds.getHeight());
					GraphEditorConstants.setBounds(map, newBounds);
					
				}
				nestedMap.put(o, map);
			}
			graphPane.getGraphLayoutCache().edit(nestedMap);
		}

}