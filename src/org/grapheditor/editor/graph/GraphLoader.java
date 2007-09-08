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

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.properties.GraphProperties;
import org.jgraph.graph.Port;
import org.jgrapht.ext.JGraphModelAdapter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.BaseException;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Object of this class can read graphs into a graph pane from the
 * xml-description of graphs.
 * 
 * @author kjellw
 */
public class GraphLoader {

	GraphEditorPane graphPane;

	public class IncorcectGraphXMLFileException extends Exception {
	};

	/**
	 * @param reader
	 */
	public GraphLoader(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
	}

	public void load(Reader reader, File file)
			throws IncorcectGraphXMLFileException {
		// Used to load objects from xml
		XStream xstream = new XStream(new DomDriver());

		xstream.alias("vertex", VertexInformation.class);
		xstream.alias("edge", EdgeInformation.class);
		xstream.alias("graph", GraphFile.class);
		GraphFile graphFile = null;
		try {
			graphFile = (GraphFile) xstream.fromXML(reader);
		} catch (BaseException e) {
			// Could not read the XML file
			throw new IncorcectGraphXMLFileException();
		}
		// An hash wich makes it fast to find vertices
		HashMap<Integer, VertexInformation> vertexHash = new HashMap<Integer, VertexInformation>();

		LinkedList<VertexInformation> vertexList = graphFile.getVertexList();
		for (VertexInformation vertex : vertexList) {
			graphPane.addVertex(vertex);
			vertexHash.put(vertex.getVertexIndex(), vertex);
		}

		/*
		 * Load all edges
		 */

		LinkedList<EdgeInformation> edgeList = graphFile.getEdgeList();

		JGraphModelAdapter<VertexInformation, EdgeInformation> model = graphPane
				.getJGraphTModelAdapter();
		for (EdgeInformation edge : edgeList) {
			VertexInformation sInfo = vertexHash.get(edge.getSourceVertexId());
			VertexInformation tInfo = vertexHash.get(edge.getTargetVertexId());

			graphPane.connect((Port) model.getVertexCell(sInfo).getChildAt(0),
					(Port) model.getVertexCell(tInfo).getChildAt(0));
		}
		for (VertexInformation vertex : vertexList)
			vertex.generateNewVertexIndex();

		GraphProperties prop = graphFile.getProperties();
		prop.initializeNotSerializeFeelds();

		graphPane.setScale(prop.getZoomLevel());
		graphPane.setBackground(prop.getBackgroundColor());
		graphPane.setGraphProperties(prop);
		prop.setSavedAs(file);

	}

}
