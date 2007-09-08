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
import java.util.Map;

import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.jgraph.graph.GraphConstants;

/**
 * This class overrides GraphConstants to add methods for some addisional
 * constants. See
 * 
 * @author kjellw
 * 
 */
public class GraphEditorConstants extends GraphConstants {

	public static final String SHAPE = "shape";

	public static final String USE_GRAPH_BACKGROUND = "use_graph_background";

	/**
	 * Sets the shape attribute in the specified map to the specified value.
	 */
	public static final void setShape(Map map, Shape shape) {
		map.put(SHAPE, shape);
	}

	/**
	 * Returns the shape attribute in the specified map if it exists.
	 */
	public static final Shape getShape(Map map) {
		Object shape = map.get(SHAPE);
		if (null == shape)
			return null;

		if (shape instanceof Shape) {
			return (Shape) shape;

		}
		return null;
	}

	/**
	 * Sets the use backround attribute in the specified map to the specified
	 * value. This is used to set if a custom backround is going to be used or
	 * just the backround of the graph for the vertex that has the attribute.
	 */
	public static final void setUseGraphBackground(Map map,
			boolean useGraphBackround) {
		map.put(USE_GRAPH_BACKGROUND, useGraphBackround);
	}

	/**
	 * Returns the use graph backroun attribute in the specified map if it
	 * exists. If it doesn't exist true is returned.
	 */
	public static final boolean getUseGraphBackground(Map map) {
		Object useGraphBackround = map.get(USE_GRAPH_BACKGROUND);
		if (null == useGraphBackround)
			return true;

		if (useGraphBackround instanceof Boolean) {
			return (Boolean) useGraphBackround;

		}
		return true;
	}

}
