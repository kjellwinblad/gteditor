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
package org.grapheditor.editor.graph;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.graph.EdgeInformation.ErrorWhenFetchingEdgeInformation;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * Ths class is used as vertex representation. It contains all information that
 * is needed to display a vertex.
 * 
 * @author kjellw
 * 
 */
public class VertexInformation {

	public class ErrorWhenFetchingVertexInformation extends Exception {
	}

	private transient DefaultGraphCell jGraphVertex;

	/**
	 * An enumeration with the posible shapes of a vertex
	 */
	public enum Shape {
		DEFAULT, SQUARE, CIRCLE, TRIANGLE
	}// , STAR,, CROSS, SLOPINGCROSS

	private double xPosition, yPosition;

	private boolean useGraphBackround = true;

	private Color backgroundColor = Color.WHITE;

	private Color foregroundColor = Color.BLACK;

	private Shape shape = Shape.CIRCLE;

	private boolean displayLabel = false;

	private String label = "";

	transient private SortedMap<String, String> properties = new TreeMap<String, String>();

	transient private static int vertIndexCount = 0;

	private int vertexIndex;

	/**
	 * 
	 */
	public VertexInformation() {

		vertexIndex = vertIndexCount;
		vertIndexCount++;

		// properties.put("test key", "test value");
	}

	/**
	 * @return the displayLabel
	 */
	public boolean isDisplayLabel() {
		return displayLabel;
	}

	/**
	 * @param displayLabel
	 *            the displayLabel to set
	 */
	public void setDisplayLabel(boolean displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the properties
	 */
	public SortedMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(SortedMap<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * @param shape
	 *            the shape to set
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * @return the xPosition
	 */
	public double getXPosition() {
		return xPosition;
	}

	/**
	 * @param position
	 *            the xPosition to set
	 */
	public void setXPosition(double position) {
		xPosition = position;
	}

	/**
	 * @return the yPosition
	 */
	public double getYPosition() {
		return yPosition;
	}

	/**
	 * @param position
	 *            the yPosition to set
	 */
	public void setYPosition(double position) {
		yPosition = position;
	}

	public String toString() {
		return label;

	}

	public Object clone() {
		VertexInformation vertexInfoClone = new VertexInformation();
		vertexInfoClone.setXPosition(xPosition);
		vertexInfoClone.setYPosition(yPosition);
		vertexInfoClone.setForegroundColor(new Color(foregroundColor.getRGB()));
		vertexInfoClone.setBackgroundColor(new Color(backgroundColor.getRGB()));
		vertexInfoClone.setUseGraphBackround(useGraphBackround);
		vertexInfoClone.setDisplayLabel(displayLabel);
		vertexInfoClone.setLabel(new String(label));
		vertexInfoClone.setShape(shape);
		// Set<Entry<String, String>> entries = properties.entrySet();
		// SortedMap<String, String> newProperties = new TreeMap<String,
		// String>();

		// for(Entry<String, String> entry : entries){
		// newProperties.put(entry.getKey(), entry.getValue());
		// }

		// vertexInfoClone.setProperties(newProperties);
		return vertexInfoClone;

	}

	/**
	 * @return the vertexIndex
	 */
	public int getVertexIndex() {
		return vertexIndex;
	}

	/**
	 * @return the jGraphVertex
	 */
	public DefaultGraphCell getJGraphVertex() {
		return jGraphVertex;
	}

	/**
	 * @param graphVertex
	 *            the jGraphVertex to set
	 */
	public void setJGraphVertex(DefaultGraphCell graphVertex) {
		jGraphVertex = graphVertex;
	}

	public void fetchInformationFromJGraph()
			throws ErrorWhenFetchingVertexInformation {
		// Fetch the id of the source and target vertex
		try {
			Map<Object, Object> map = jGraphVertex.getAttributes();
			Rectangle2D vertexBounds = GraphConstants.getBounds(map);
			shape = GraphEditorConstants.getShape(map);
			backgroundColor = GraphEditorConstants.getBackground(map);
			foregroundColor = GraphEditorConstants.getForeground(map);
			useGraphBackround = GraphEditorConstants.getUseGraphBackground(map);
			xPosition = vertexBounds.getX();
			yPosition = vertexBounds.getY();

		} catch (Exception e) {
			throw new ErrorWhenFetchingVertexInformation();
		}

	}

	public void generateNewVertexIndex() {
		vertexIndex = vertIndexCount;
		vertIndexCount++;
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return the foregroundColor
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor
	 *            the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * @return the useGraphBackround
	 */
	public boolean isUseGraphBackround() {
		return useGraphBackround;
	}

	/**
	 * @param useGraphBackround
	 *            the useGraphBackround to set
	 */
	public void setUseGraphBackround(boolean useGraphBackround) {
		this.useGraphBackround = useGraphBackround;
	}

}
