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
package org.grapheditor.editor.graph;

import java.awt.Color;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;

/**
 * This class represents information about an edge. The object can fetch
 * information about itself from a JGraphT graph with the method
 * fetchInformationFromJGraphT. It has a source and target vertex id. Object of
 * this class is used to save the edge information to an xml representation
 * directly using XStream
 * 
 * 
 * @author kjellw
 * 
 */
public class EdgeInformation {

	public class ErrorWhenFetchingEdgeInformation extends Exception {
	}

	/*
	 * The jgraph edge that this edge information is asociated with
	 */

	transient private DefaultEdge jGraphEdge;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081969696113643101L;

	private int sourceVertexId;

	private int targetVertexId;

	/**
	 * @param graphEdge
	 */
	public EdgeInformation() {
		super();
	}

	/**
	 * @return the jGraphEdge
	 */
	public DefaultEdge getJGraphEdge() {
		return jGraphEdge;
	}

	/**
	 * @param graphEdge
	 *            the jGraphEdge to set
	 */
	public void setJGraphEdge(DefaultEdge graphEdge) {
		jGraphEdge = graphEdge;
	}

	/**
	 * @return the sourceVertexId
	 */
	public int getSourceVertexId() {
		return sourceVertexId;
	}

	/**
	 * @param sourceVertexId
	 *            the sourceVertexId to set
	 */
	public void setSourceVertexId(int sourceVertexId) {
		this.sourceVertexId = sourceVertexId;
	}

	/**
	 * @return the targetVertexId
	 */
	public int getTargetVertexId() {
		return targetVertexId;
	}

	/**
	 * @param targetVertexId
	 *            the targetVertexId to set
	 */
	public void setTargetVertexId(int targetVertexId) {
		this.targetVertexId = targetVertexId;
	}

	public void fetchInformationFromJGraphT(
			ListenableGraph<VertexInformation, EdgeInformation> g)
			throws ErrorWhenFetchingEdgeInformation {
		// Fetch the id of the source and target vertex
		try {

			sourceVertexId = g.getEdgeSource(this).getVertexIndex();

			targetVertexId = g.getEdgeTarget(this).getVertexIndex();
		} catch (Exception e) {
			e.printStackTrace();
			// throw new ErrorWhenFetchingEdgeInformation();
		}

	}

	public String toString() {
		return "";

	}

	/**
	 * Make a copy of this edge
	 */
	public Object clone() {
		EdgeInformation cloneEdge = new EdgeInformation();
		cloneEdge.setSourceVertexId(sourceVertexId);
		cloneEdge.setTargetVertexId(targetVertexId);
		return cloneEdge;
	}

}
