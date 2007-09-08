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
package org.grapheditor.editor.exporters;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.epsgraphics.ColorMode;
import net.sf.epsgraphics.EpsGraphics;
import net.sf.epsgraphics.EpsTools;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.GraphEditorVertexView;
import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.VertexInformation.ErrorWhenFetchingVertexInformation;
import org.grapheditor.editor.vertexrenders.SimpleDrawAble;
import org.grapheditor.gui.StatusNotifier;
import org.grapheditor.properties.GlobalProperties;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.Port;
import org.jgrapht.ListenableGraph;

/**
 * Objects of this class is used to export graphs to a an EPS-image.
 * 
 * @author kjellw
 * 
 */
public class EPSExporter {
	public void export(File file, GraphEditorPane graphPane) throws IOException {

		OutputStream writer = new FileOutputStream(file);
		export(writer, graphPane);
		writer.close();
	}

	public void export(OutputStream writer, GraphEditorPane graphPane)
			throws IOException {

		Object[] vertices = graphPane.getGraphLayoutCache().getCells(false,
				true, false, false);
		Object[] edges = graphPane.getGraphLayoutCache().getCells(false, false,
				false, true);
		CellView[] vertexViews = graphPane.getGraphLayoutCache().getMapping(
				vertices);

		Rectangle2D bounds = graphPane.getCellBounds(graphPane
				.getGraphLayoutCache().getCells(false, true, false, false));
		ColorMode colorMode = ColorMode.COLOR_RGB;

		if (bounds == null) {
			bounds = graphPane.getBounds();
		}
		// File Output stream
		int xmin = (int) ((bounds.getX()));
		int ymin = (int) ((bounds.getY()));
		int xmax = (int) ((bounds.getMaxX()));
		int ymax = (int) ((bounds.getMaxY()));

		EpsGraphics graphics = new EpsGraphics("Graph Editor generated eps",
				writer, 0, 0, (int) bounds.getWidth() + 10, (int) bounds
						.getHeight() + 10, colorMode);

		graphics.translate(-xmin + 5, -ymin + 5);

		Object[] selection = graphPane.getSelectionCells();
		boolean gridVisible = graphPane.isGridVisible();
		boolean doubleBuffered = graphPane.isDoubleBuffered();
		graphPane.setGridVisible(false);
		graphPane.setDoubleBuffered(false);
		graphPane.clearSelection();

		// TODO: Remove the unsupported method stack trace
		// graphPane.print(graphics);
		// Paint vertex
		for (CellView v : vertexViews) {
			if (v instanceof GraphEditorVertexView) {
				GraphEditorVertexView view = (GraphEditorVertexView) v;
				Rectangle2D cellBounds = GraphEditorConstants.getBounds(view
						.getAllAttributes());
				graphics.translate(cellBounds.getX(), cellBounds.getY());
				Component c = view.getRendererComponent(graphPane, false,
						false, true);
				if (c instanceof SimpleDrawAble) {
					SimpleDrawAble simpleDrawAble = (SimpleDrawAble) c;
					simpleDrawAble.simpleDraw(graphics);
				}
				graphics.translate(-cellBounds.getX(), -cellBounds.getY());
			}

		}
		for (Object o : edges) {
			DefaultEdge edge = (DefaultEdge) o;
			CellView sourceView = graphPane.getGraphLayoutCache().getMapping(
					((DefaultPort) edge.getSource()).getRoot(), true);
			CellView targetView = graphPane.getGraphLayoutCache().getMapping(
					((DefaultPort) edge.getTarget()).getRoot(), true);

			EdgeView view = (EdgeView) graphPane.getGraphLayoutCache()
					.getMapping(o, true);

			Point2D sourcePoint2D = sourceView.getPerimeterPoint(view, null,
					new Point2D.Double(targetView.getBounds().getCenterX(),
							targetView.getBounds().getCenterY()));
			Point2D targetPoint2D = targetView.getPerimeterPoint(view, null,
					new Point2D.Double(sourceView.getBounds().getCenterX(),
							sourceView.getBounds().getCenterY()));
			int x1 = (int) Math.round(sourcePoint2D.getX());
			int y1 = (int) Math.round(sourcePoint2D.getY());
			int x2 = (int) Math.round(targetPoint2D.getX());
			int y2 = (int) Math.round(targetPoint2D.getY());

			graphics.drawLine(x1, y1, x2, y2);

		}

		graphPane.setSelectionCells(selection);
		graphPane.setGridVisible(gridVisible);
		graphPane.setDoubleBuffered(doubleBuffered);

		graphics.close();
	}

	public void setStatusNotifier(StatusNotifier n) {
		// TODO Auto-generated method stub

	}
}
