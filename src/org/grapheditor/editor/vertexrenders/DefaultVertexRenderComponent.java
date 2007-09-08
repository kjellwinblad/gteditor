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
package org.grapheditor.editor.vertexrenders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.GraphEditorVertexView;
import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexRenderer;

/**
 * This class extends the class VertexRender in the JGraph components to be a
 * vertex render for default vertex type. Depending on the settings of the
 * default vertex render diffrent render components are returned.
 * 
 * @author kjellw
 * 
 */
public class DefaultVertexRenderComponent extends VertexRenderer implements
		SimpleDrawAble {

	Shape shape;

	private boolean selectable;

	private boolean selected;

	private boolean focus;

	private boolean preview;

	private Color background;

	static DefaultVertexRenderComponent defaultRender = new DefaultVertexRenderComponent();

	private static CircleVertexRenderComponent circleVertexRenderComponent = new CircleVertexRenderComponent(
			defaultRender);

	private static SquareVertexRenderComponent squareVertexRenderComponent = new SquareVertexRenderComponent(
			defaultRender);

	private static TriangleVertexRenderComponent triangleVertexRenderComponent = new TriangleVertexRenderComponent(
			defaultRender);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.CellViewRenderer#getRendererComponent(org.jgraph.JGraph,
	 *      org.jgraph.graph.CellView, boolean, boolean, boolean)
	 */
	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview) {

		Map<Object, Object> atributes = ((DefaultGraphCell) view.getCell())
				.getAttributes();

		Shape shape = GraphEditorConstants.getShape(atributes);

		if (shape == Shape.DEFAULT)
			if (graph instanceof GraphEditorPane) {
				GraphEditorPane pane = (GraphEditorPane) graph;
				shape = pane.getGraphProperties().getDefaultShape();
			}

		if (shape == null || shape == Shape.CIRCLE)
			return circleVertexRenderComponent.getRendererComponent(graph,
					view, sel, focus, preview);
		else if (shape == Shape.SQUARE)
			return squareVertexRenderComponent.getRendererComponent(graph,
					view, sel, focus, preview);
		else if (shape == Shape.TRIANGLE)
			return triangleVertexRenderComponent.getRendererComponent(graph,
					view, sel, focus, preview);
		else
			return super.getRendererComponent(graph, view, sel, focus, preview);
	}

	public CellViewRenderer getRenderer(GraphEditorVertexView view, JGraph graph) {
		Map<Object, Object> atributes = view.getAllAttributes();

		Shape shape = GraphEditorConstants.getShape(atributes);

		if (shape == Shape.DEFAULT)
			if (graph instanceof GraphEditorPane) {
				GraphEditorPane pane = (GraphEditorPane) graph;
				shape = pane.getGraphProperties().getDefaultShape();
			}

		if (shape == null || shape == Shape.CIRCLE)
			return circleVertexRenderComponent;
		else if (shape == Shape.SQUARE)
			return squareVertexRenderComponent;

		else
			return this;
	}

	protected void fetchSelectable(Map attrs) {
		selectable = GraphEditorConstants.isSelectable(attrs);
	}

	/**
	 * @return the selectable
	 */
	public boolean isSelectable() {
		return selectable;
	}

	protected void paintSelectable(Graphics g) {
		if (!isSelectable()) {
			Graphics2D g2 = (Graphics2D) g;

			int width = (int) getBounds().getWidth();
			int height = (int) getBounds().getHeight();
			g2.setColor(Color.RED);

			g2.drawLine(0, 0, width, height);

			g2.drawLine(width, 0, 0, height);
		}
		// TODO Auto-generated method stub

	}

	public void simpleDraw(Graphics g) {
		paintComponent(g);

	}

}
