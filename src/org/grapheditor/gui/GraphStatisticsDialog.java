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
package org.grapheditor.gui;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.VertexInformation.ErrorWhenFetchingVertexInformation;
import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.grapheditor.properties.GlobalProperties;

import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.w3c.dom.html.HTMLStyleElement;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

import javax.swing.JLabel;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * This class represents a dialog that shows statistics about the current graph.
 * 
 * @author kjellw
 */
public class GraphStatisticsDialog extends JDialog {

	private JPanel rootPanel = null;

	private JPanel okPanel = null;

	private JPanel titlePanel = null;

	private JPanel informationPanel = null;

	private JButton okejButton = null;

	private JLabel jLabel = null;

	private JScrollPane jScrollPane = null;

	private JTextPane informationTextPane = null;

	private StringBuffer htmlTextInfo = new StringBuffer(); // @jve:decl-index=0:

	private GraphEditorPane graphPane;

	private GraphStatisticsDialog(GraphEditorPane graphPane) {
		super(graphPane.getMainFrame());
		this.graphPane = graphPane;
		initialize();
		jScrollPane.getViewport().setViewPosition(new Point(0, 0));
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(450, 500));
		this.setTitle("Graph Statistics");
		this.setContentPane(getRootPanel());

	}

	public static void show(GraphEditorPane graphPane) {

		new GraphStatisticsDialog(graphPane).setVisible(true);
	}

	/**
	 * This method initializes rootPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRootPanel() {
		if (rootPanel == null) {
			rootPanel = new JPanel();
			rootPanel.setLayout(new BorderLayout());
			rootPanel.add(getOkPanel(), BorderLayout.SOUTH);
			rootPanel.add(getTitlePanel(), BorderLayout.NORTH);
			rootPanel.add(getInformationPanel(), BorderLayout.CENTER);
		}
		return rootPanel;
	}

	/**
	 * This method initializes okPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOkPanel() {
		if (okPanel == null) {
			okPanel = new JPanel();
			okPanel.setLayout(new BoxLayout(getOkPanel(), BoxLayout.X_AXIS));
			okPanel.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			okPanel.add(Box.createGlue());
			okPanel.add(getOkejButton());

		}
		return okPanel;
	}

	/**
	 * This method initializes titlePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			jLabel = new JLabel();
			jLabel.setText("Graph Statistics");
			jLabel.setFont(new Font("Dialog", Font.BOLD, 24));
			titlePanel = new JPanel();
			titlePanel.setLayout(new GridBagLayout());
			titlePanel.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			titlePanel.add(jLabel, new GridBagConstraints());
		}
		return titlePanel;
	}

	/**
	 * This method initializes informationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInformationPanel() {
		if (informationPanel == null) {
			informationPanel = new JPanel();
			informationPanel.setLayout(new BorderLayout());
			informationPanel.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return informationPanel;
	}

	/**
	 * This method initializes okejButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkejButton() {
		if (okejButton == null) {
			okejButton = new JButton();
			okejButton.setText("OK");
			okejButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return okejButton;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setAutoscrolls(false);
			jScrollPane.setBorder(null);
			jScrollPane.setViewportView(getInformationTextPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes informationTextPane
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getInformationTextPane() {
		if (informationTextPane == null) {
			informationTextPane = new JTextPane();
			informationTextPane.setAutoscrolls(false);
			HTMLDocument htmlDocument = new HTMLDocument();

			informationTextPane.setEditable(false);
			informationTextPane.setEditorKit(new HTMLEditorKit());

			AsUndirectedGraph<VertexInformation, EdgeInformation> graph = new AsUndirectedGraph<VertexInformation, EdgeInformation>(
					graphPane.getJGraphTGraph());

			BreadthFirstIterator<VertexInformation, EdgeInformation> iter = new BreadthFirstIterator<VertexInformation, EdgeInformation>(
					graph);
			InfoCatcherTraversalListener infoListener = new InfoCatcherTraversalListener();
			iter.addTraversalListener(infoListener);
			while (iter.hasNext()) {
				iter.next();
			}
			List<InfoCatcherTraversalListener.GraphInformation> connectedComponents = infoListener
			.getComponents();
			htmlTextInfo.append("<html><head</head><body>");
			htmlTextInfo.append("<b>Whole graph:</b><br>");
			htmlTextInfo.append("<i>Number of connected components:</i> <b>"
					+ connectedComponents.size() + "</b><br>");
			htmlTextInfo.append(infoListener.getWholeGraph());


			htmlTextInfo.append("<br>");
			int c = 1;
			if (connectedComponents.size() > 1)
				for (InfoCatcherTraversalListener.GraphInformation info : connectedComponents) {
					htmlTextInfo.append("<b>Connected component numer " + c
							+ ":</b></b><br>");
					htmlTextInfo.append(info);
					htmlTextInfo.append("</b><br>");
					c++;
				}
			htmlTextInfo.append("</body></html>");
			informationTextPane.setText(htmlTextInfo.toString());
			informationTextPane.setCaretPosition(0);
			informationTextPane.addHyperlinkListener(new HyperlinkListener() {

				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						try {
							BrowserLauncher luncher = new BrowserLauncher();
							luncher.openURLinBrowser(e.getURL().toString());
						} catch (BrowserLaunchingInitializingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedOperatingSystemException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
			});

		}
		return informationTextPane;
	}

	private class InfoCatcherTraversalListener implements
			TraversalListener<VertexInformation, EdgeInformation> {

		public class GraphInformation {
			private int edges = 0;

			private int vertices = 0;

			Map<Shape, Integer> shapeOccuensMap = new HashMap<Shape, Integer>();

			public GraphInformation() {
				for (Shape s : Shape.values()) {
					shapeOccuensMap.put(s, 0);
				}
			}

			/**
			 * @return the edges
			 */
			public int getEdges() {
				return edges;
			}

			/**
			 * @param edges
			 *            the edges to set
			 */
			public void setEdges(int edges) {
				this.edges = edges;
			}

			/**
			 * @return the vertices
			 */
			public int getVertices() {
				return vertices;
			}

			/**
			 * @param vertices
			 *            the vertices to set
			 */
			public void setVertices(int vertices) {
				this.vertices = vertices;
			}

			/**
			 * @return the shapeOccuensMap
			 */
			public Map<Shape, Integer> getShapeOccuensMap() {
				return shapeOccuensMap;
			}

			@Override
			public String toString() {
				StringBuffer htmlInfoBuffer = new StringBuffer();
				htmlInfoBuffer.append("<i>Number of vertices: </i><b>"
						+ vertices + "</b><br>");
				htmlInfoBuffer.append("<i>Number of edges: </i><b>" + edges / 2
						+ "</b><br>");
				htmlInfoBuffer.append("<i>Average vertex degree: </i><b>"
						+ (vertices > 0 ? (double) (edges) / vertices : 0)
						+ "</b><br>");
				for (Shape s : Shape.values()) {
					htmlInfoBuffer.append("<i>Number of vertices with shape "
							+ s + ": </i><b>" + shapeOccuensMap.get(s)
							+ "</b><br>");
				}

				return htmlInfoBuffer.toString();
			}
		}

		LinkedList<GraphInformation> components = new LinkedList<GraphInformation>();

		GraphInformation wholeGraph = new GraphInformation();

		public void connectedComponentFinished(
				ConnectedComponentTraversalEvent e) {

		}

		public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			components.add(new GraphInformation());

		}

		public void edgeTraversed(
				EdgeTraversalEvent<VertexInformation, EdgeInformation> e) {
			wholeGraph.setEdges(wholeGraph.getEdges() + 1);
			components.getLast().setEdges(components.getLast().getEdges() + 1);
		}

		public void vertexFinished(VertexTraversalEvent<VertexInformation> e) {

		}

		public void vertexTraversed(VertexTraversalEvent<VertexInformation> e) {
			try {
				e.getVertex().fetchInformationFromJGraph();
			} catch (ErrorWhenFetchingVertexInformation e1) {
			}
			Shape s = e.getVertex().getShape();

			wholeGraph.getShapeOccuensMap().put(s,
					wholeGraph.getShapeOccuensMap().get(s) + 1);
			components.getLast().getShapeOccuensMap().put(s,
					components.getLast().getShapeOccuensMap().get(s) + 1);
			wholeGraph.setVertices(wholeGraph.getVertices() + 1);
			components.getLast().setVertices(
					components.getLast().getVertices() + 1);

		}

		/**
		 * @return the components
		 */
		public LinkedList<GraphInformation> getComponents() {
			return components;
		}

		/**
		 * @return the wholeGraph
		 */
		public GraphInformation getWholeGraph() {
			return wholeGraph;
		}

	}

}
