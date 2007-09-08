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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

/**
 * This class represents a mouse motion listener for a JGraph component that has
 * the properties of the default one plus the additional future that it is
 * possible to merge two vertices if the graph is not in insert mode and they
 * are dragged togheter.
 * 
 * @author kjellw
 * 
 */
public class GraphEditorPaneMouseAndMotionListener implements MouseListener,
		MouseMotionListener {

	private GraphEditorPane graphPane;

	private boolean mousePressed = false;

	private boolean insertMode = true;

	private boolean cellMoving = false;

	private PortView movingPortView;

	private MouseListener orginalMouseListener;

	private MouseMotionListener orginalMotionListener;

	private boolean mergeCells;

	private PortView mergePairToMergeTo;

	private PortView mergePairMerge;

	public GraphEditorPaneMouseAndMotionListener(GraphEditorPane pane,
			MouseListener ml, MouseMotionListener mml) {
		this.orginalMotionListener = mml;
		this.orginalMouseListener = ml;
		this.graphPane = pane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		orginalMouseListener.mouseClicked(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		orginalMouseListener.mouseEntered(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

		orginalMouseListener.mouseExited(e);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		graphPane.setJumpToDefaultPort(false);
		PortView result;
		try {
			// Find a Port View in Model Coordinates and Remember
			result = graphPane.getPortViewAt(e.getPoint().getX(), e.getPoint()
					.getY());
		} finally {
			graphPane.setJumpToDefaultPort(true);
		}
		if ((result != null) && !insertMode) {
			if ((result.getCell() instanceof DefaultGraphCell)
					&& !(result.getCell() instanceof DefaultEdge)) {

				cellMoving = true;
				movingPortView = result;
			}
		}
		orginalMouseListener.mousePressed(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// What is at the current position?

		if (!insertMode) {

			if (mergeCells) {

				if ((mergePairToMergeTo.getCell() instanceof DefaultGraphCell)
						&& !(mergePairToMergeTo.getCell() instanceof DefaultEdge)) {
					// Merge the cells

					List<Object> incomingEdgesToBeTransferd = graphPane
							.getGraphLayoutCache().getIncomingEdges(
									mergePairMerge.getCell(), null, true, true);
					List<Port> incomingNaighbours = new LinkedList<Port>();
					List<Object> outgoingEdgesToBeTransferd = graphPane
							.getGraphLayoutCache().getOutgoingEdges(
									mergePairMerge.getCell(), null, true, true);
					List<Port> outgoingNaighbours = new LinkedList<Port>();

					for (Object o : incomingEdgesToBeTransferd) {
						
						if (o instanceof DefaultEdge) {
							DefaultEdge edge = (DefaultEdge) o;
							if (edge.getSource() instanceof DefaultPort) {
								Port port = (Port) edge.getSource();
								incomingNaighbours.add(port);

							}
						}
					}
					for (Object o : outgoingEdgesToBeTransferd) {

						if (o instanceof DefaultEdge) {
							DefaultEdge edge = (DefaultEdge) o;
							if (edge.getTarget() instanceof Port) {
								Port port = (Port) edge.getTarget();
								outgoingNaighbours.add(port);

							}
						}
					}
					graphPane.getGraphLayoutCache().remove(
							incomingEdgesToBeTransferd.toArray());
					graphPane.getGraphLayoutCache().remove(
							outgoingEdgesToBeTransferd.toArray());
					Object[] array = new Object[2];
					array[0] = ((DefaultPort) mergePairMerge.getCell())
							.getRoot();
					array[1] = mergePairMerge.getCell();
					graphPane.getGraphLayoutCache().remove(array);
					for (Port cell : outgoingNaighbours) {
						graphPane.connect((Port) mergePairToMergeTo.getCell(),
								cell);
					}
					for (Port cell : incomingNaighbours) {
						graphPane.connect(cell, (Port) mergePairToMergeTo
								.getCell());
					}

					mergeCells = false;
				}
			}

		}
		cellMoving = false;
		orginalMouseListener.mouseReleased(e);
	}

	/**
	 * @return the insertMode
	 */
	public boolean isInsertMode() {
		return insertMode;
	}

	/**
	 * @param insertMode
	 *            the insertMode to set
	 */
	public void setInsertMode(boolean insertMode) {
		this.insertMode = insertMode;
	}

	public void mouseDragged(MouseEvent e) {
		// What is at the current position?
		if (!insertMode && cellMoving) {

			graphPane.setJumpToDefaultPort(false);
			PortView result;
			try {
				// Find a Port View in Model Coordinates and Remember
				result = graphPane.getPortViewAt(e.getPoint().getX(), e
						.getPoint().getY());

			} finally {
				graphPane.setJumpToDefaultPort(true);
			}

			if ((result != null) && (movingPortView != null)
					&& (result != movingPortView)) {

				mergePairToMergeTo = result;
				mergePairMerge = movingPortView;
				mergeCells = true;
			} else {
				mergeCells = false;
			}

		}
		orginalMotionListener.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {

		orginalMotionListener.mouseMoved(e);
	}

}
