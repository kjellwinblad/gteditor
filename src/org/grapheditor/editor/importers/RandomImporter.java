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
package org.grapheditor.editor.importers;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.gui.StatusNotifier;
import org.jgraph.graph.Port;

/**
 * This importer imports a graph with specified number of verices connected by
 * specified numer of edges randomly
 * 
 * @author kjellw
 * 
 */
public class RandomImporter {

	public class InvalidParameters extends Exception {
	}

	private GraphEditorPane graphPane;

	private StatusNotifier statusNotifier;

	public RandomImporter(GraphEditorPane graphPane) {
		this.graphPane = graphPane;
	}

	void importGraph(int vertices, int edges, int distanseBetweenVertices)
			throws InvalidParameters {

		int perimeter = vertices * distanseBetweenVertices;
		double radie = (double) perimeter / (2 * Math.PI);
		Point2D.Double centerPoint = new Point2D.Double(radie + 30, radie + 30);

		double stepSize = (2 * Math.PI) / vertices;
		double distanceFromBegining = 0;

		HashMap<Port, HashSet<Port>> portConnectins = new HashMap<Port, HashSet<Port>>();

		for (int n = 0; n < vertices; n++) {
			double xpos = Math.cos(distanceFromBegining) * radie
					+ centerPoint.getX();
			double ypos = Math.sin(distanceFromBegining) * radie
					+ centerPoint.getY();

			Port port = (Port) (graphPane.addDefaultVertexAt(xpos, ypos)
					.getChildAt(0));
			HashSet<Port> connections = new HashSet<Port>();
			portConnectins.put(port, connections);

			distanceFromBegining = distanceFromBegining + stepSize;
			if (statusNotifier != null)
				statusNotifier.setStatusMessage((n + 1) + "/" + vertices
						+ " vertices and " + 0 + "/" + edges
						+ " edges inserted");
			if (Thread.interrupted())
				return;
		}

		ArrayList<Port> ports = new ArrayList<Port>(portConnectins.keySet());
		// Shall now put out edges randomly between the ports
		int randomNumber = vertices;
		int avableConnections = vertices - 1;
		int firstPortNr;
		int secondPortNr;
		Port firstPort;
		Port secondPort;
		HashSet<Port> firstPortConections;
		HashSet<Port> secondPortConections;
		for (int n = 0; n < edges; n++) {
			do {
				firstPortNr = (int) (Math.floor((double) randomNumber
						* Math.random()));
				secondPortNr = (int) (Math.floor((double) randomNumber
						* Math.random()));

				while (secondPortNr == firstPortNr) {
					secondPortNr = (int) (Math.floor((double) randomNumber
							* Math.random()));

				}

				firstPort = ports.get(firstPortNr);
				secondPort = ports.get(secondPortNr);
				firstPortConections = portConnectins.get(firstPort);
				secondPortConections = portConnectins.get(secondPort);
			} while (firstPortConections.contains(secondPort));

			firstPortConections.add(secondPort);
			if (firstPortConections.size() == avableConnections) {
				// Remove the port it is not usable anymore
				ports.remove(firstPortNr);
				randomNumber--;
				if (firstPortNr < secondPortNr)
					secondPortNr--;
			} else {
				// Update the number of connections
				portConnectins.put(firstPort, firstPortConections);
			}
			secondPortConections.add(firstPort);
			// The same with the second port
			if (secondPortConections.size() == avableConnections) {
				// Remove the port it is not usable anymore
				ports.remove(secondPortNr);
				randomNumber--;
			} else {
				// Update the number of connections
				portConnectins.put(secondPort, secondPortConections);
			}

			// Establish the connection
			graphPane.connect(firstPort, secondPort);
			if (statusNotifier != null)
				statusNotifier.setStatusMessage(vertices + "/" + vertices
						+ " vertices and " + (n + 1) + "/" + edges
						+ " inserted");
			if (Thread.interrupted())
				return;
		}

	}

	public static int getAllowedEdgesFor(int value) {
		int totalNumber = 0;
		for (int n = 1; n < value; n++)
			totalNumber = n + totalNumber;

		return totalNumber;

	}

	/**
	 * @param statusNotifier
	 *            the statusNotifier to set
	 */
	public void setStatusNotifier(StatusNotifier statusNotifier) {
		this.statusNotifier = statusNotifier;
	}

}
