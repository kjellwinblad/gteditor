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

import java.util.LinkedList;

import org.grapheditor.properties.GraphProperties;

/**
 * This class represents a graph file and is used by GraphSaver and GraphLoader
 * classes.
 * 
 * @author kjellw
 * 
 */
public class GraphFile {
	private GraphProperties properties;

	private LinkedList<VertexInformation> vertexList;

	private LinkedList<EdgeInformation> edgeList;

	/**
	 * @return the edgeList
	 */
	public LinkedList<EdgeInformation> getEdgeList() {
		return edgeList;
	}

	/**
	 * @param edgeList
	 *            the edgeList to set
	 */
	public void setEdgeList(LinkedList<EdgeInformation> edgeList) {
		this.edgeList = edgeList;
	}

	/**
	 * @return the vertexList
	 */
	public LinkedList<VertexInformation> getVertexList() {
		return vertexList;
	}

	/**
	 * @param vertexList
	 *            the vertexList to set
	 */
	public void setVertexList(LinkedList<VertexInformation> vertexList) {
		this.vertexList = vertexList;
	}

	/**
	 * @return the properties
	 */
	public GraphProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(GraphProperties properties) {
		this.properties = properties;
	}

}
