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

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.EdgeInformation.ErrorWhenFetchingEdgeInformation;
import org.grapheditor.editor.graph.VertexInformation.ErrorWhenFetchingVertexInformation;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Objects of this class saves a graphs from a GraphEditorPanee to a xml-file
 * representation. The file representation consist of first some information 
 * about the graph, a number of items represending vertices and then a list of 
 * edges that conects vertices. The information of the grap is represented by
 * GraphProperties, the vertices by VertexInformation and edges by
 * EdgeInformation. The elements represnding the objects EdgeInformation, 
 * VertexInformation and GraphProperties in the xml-file is the non transient 
 * fields in the classes.
 * @author kjellw
 * 
 */
public class GraphSaver {

	/*
	 * The graph pane that this saver can save to
	 */
	private GraphEditorPane paneToSave;

	/**
	 * @param paneToSave
	 */
	public GraphSaver(GraphEditorPane paneToSave) {
		this.paneToSave = paneToSave;
	}

	/**
	 * Write the graph representation with the specified writer
	 * 
	 * @param writer
	 */
	public void write(Writer writer) {
		// Used to vonvert objects to xml
		XStream xstream = new XStream();

		ListenableGraph<VertexInformation, EdgeInformation> graph = paneToSave
				.getJGraphTGraph();

		// Start to write the vertices
		Set<VertexInformation> vertexSet = graph.vertexSet();
		LinkedList<VertexInformation> vertexList = new LinkedList<VertexInformation>(
				vertexSet);

		for (VertexInformation vertex : vertexList) {
			try {
				vertex.fetchInformationFromJGraph();
			} catch (ErrorWhenFetchingVertexInformation e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * Save the vertex set
		 */

		xstream.alias("vertex", VertexInformation.class);

		// Write the edges
		Set<EdgeInformation> edgeSet = graph.edgeSet();
		LinkedList<EdgeInformation> edgeList = new LinkedList<EdgeInformation>(
				edgeSet);

		for (EdgeInformation edge : edgeList) {
			try {
				edge.fetchInformationFromJGraphT(graph);
			} catch (ErrorWhenFetchingEdgeInformation e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * Save the edge set
		 */

		xstream.alias("edge", EdgeInformation.class);

		GraphFile file = new GraphFile();
		file.setProperties(paneToSave.getGraphProperties());
		file.setVertexList(vertexList);
		file.setEdgeList(edgeList);

		xstream.alias("graph", GraphFile.class);
		xstream.toXML(file, writer);
	}

}
