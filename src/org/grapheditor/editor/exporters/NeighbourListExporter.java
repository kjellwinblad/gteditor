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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.VertexInformation.ErrorWhenFetchingVertexInformation;
import org.grapheditor.gui.StatusNotifier;
import org.jgrapht.ListenableGraph;

/**
 * The neighbor list exporter exports file to the same file format as the
 * Mathematica exporter with the differense that it doesnt have the to numbers
 * that represents tha position for each vertex.
 * 
 * @author kjellw
 * 
 */
public class NeighbourListExporter {

	public void export(File file, GraphEditorPane graphPane) throws IOException {
		Writer writer = new FileWriter(file);
		export(writer, graphPane);
		writer.close();
	}

	public void export(Writer writer, GraphEditorPane graphPane)
			throws IOException {

		ListenableGraph<VertexInformation, EdgeInformation> graph = graphPane
				.getJGraphTGraph();

		// Start to write the vertices
		Set<VertexInformation> vertexSet = graph.vertexSet();
		HashMap<VertexInformation, Set<VertexInformation>> vertexNeighboursMap = new HashMap<VertexInformation, Set<VertexInformation>>();

		for (VertexInformation vertex : vertexSet) {
			try {
				vertex.fetchInformationFromJGraph();

				vertexNeighboursMap.put(vertex,
						new HashSet<VertexInformation>());
			} catch (ErrorWhenFetchingVertexInformation e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Write the edges
		Set<EdgeInformation> edgeSet = graph.edgeSet();

		for (EdgeInformation edge : edgeSet) {

			VertexInformation sourceVertex = graph.getEdgeSource(edge);
			VertexInformation targetVertex = graph.getEdgeTarget(edge);

			vertexNeighboursMap.get(sourceVertex).add(targetVertex);
			vertexNeighboursMap.get(targetVertex).add(sourceVertex);

		}
		HashMap<VertexInformation, Integer> indexInformationMap = new HashMap<VertexInformation, Integer>();

		ArrayList<Entry<VertexInformation, Set<VertexInformation>>> infoList = new ArrayList<Entry<VertexInformation, Set<VertexInformation>>>();
		int count = 0;
		for (Entry<VertexInformation, Set<VertexInformation>> entry : vertexNeighboursMap
				.entrySet()) {
			infoList.add(entry);
			indexInformationMap.put(entry.getKey(), count);
			count++;
		}

		// Every vertex has now an index

		// Export to the writer
		int index = 1;
		for (Entry<VertexInformation, Set<VertexInformation>> entry : infoList) {
			VertexInformation vertex = entry.getKey();
			// index
			writer.append((new Integer(index).toString()) + " ");
			// Write all neighbour index
			Set<VertexInformation> neighbours = entry.getValue();

			for (VertexInformation neighbour : neighbours) {
				int id = indexInformationMap.get(neighbour) + 1;
				writer.append((new Integer(id).toString()));
			}
			if (index != infoList.size())
				writer.append('\n');
			index++;
		}

	}

	public void setStatusNotifier(StatusNotifier n) {
		// TODO Auto-generated method stub

	}
}
