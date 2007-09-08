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
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.gui.StatusNotifier;
import org.jgraph.graph.Port;

/**
 * The objects of this class is used to import mathematica importable files to a
 * GraphEditorPane
 * 
 * @author kjellw
 * 
 */
public class MathematicaImporter {
	public class InvalidFile extends Exception {
		private String message;

		public void setMessage(String mess) {
			message = mess;
		}

		public String getMessage() {
			return message;
		}
	}

	private GraphEditorPane graphPane;

	private StatusNotifier statusNotifier;

	/**
	 * Constructor. Takes a GraphEditorPane to import to as parameter.
	 * 
	 * @param graphPane
	 */
	public MathematicaImporter(GraphEditorPane graphPane) {
		this.graphPane = graphPane;
	}

	/**
	 * 
	 * @param file
	 * @param expandConstant
	 * @throws IOException
	 * @throws InvalidFile
	 */
	public void importMathematicaFile(File file, double expandConstant)
			throws IOException, InvalidFile {
		FileReader reader = new FileReader(file);
		importMathematica(reader, expandConstant);

		reader.close();
	}

	public void importMathematica(Reader reader, double expandConstant)
			throws InvalidFile {

		class VertexInfo {
			private Double position;

			private HashSet<Integer> neghtbours;

			public VertexInfo(Point2D.Double pos, HashSet<Integer> neghtbours) {
				this.position = pos;
				this.neghtbours = neghtbours;
			}

			public Point2D.Double getPosition() {
				return position;
			}

			public HashSet<Integer> getNeghtbours() {
				return neghtbours;
			}
		}

		HashMap<Integer, VertexInfo> vertexIdInformationMap = new HashMap<Integer, VertexInfo>();
		Scanner lineScanner = new Scanner(reader);
		// try {
		// System .out.println(" " + ((char)reader.read()) + "< ");
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		Scanner infoScanner;
		// 1 read the information into the map
		int linec = 0;
		double xmin = 0;
		double ymin = 0;
		while (lineScanner.hasNextLine()) {

			linec++;
			infoScanner = new Scanner(lineScanner.nextLine());
			HashSet<Integer> neightboursSet = null;
			double xpos;
			double ypos;

			int id;
			try {

				id = infoScanner.nextInt();
				xpos = new java.lang.Double(infoScanner.next());
				if (xmin > xpos)
					xmin = xpos;
				ypos = new java.lang.Double(infoScanner.next());
				if (ymin > ypos)
					ymin = ypos;
				neightboursSet = new HashSet<Integer>();
				while (infoScanner.hasNext()) {
					int neightbourId = infoScanner.nextInt();
					neightboursSet.add(neightbourId);
				}
			} catch (Exception e) {
				e.printStackTrace();
				InvalidFile f = new InvalidFile();
				f
						.setMessage("Corrupt file. Does not contain information in the Mathematica format");
				throw f;
			}
			vertexIdInformationMap.put(id, new VertexInfo(new Point2D.Double(
					xpos, ypos), neightboursSet));
			if (Thread.interrupted())
				return;
		}
		double moveConstant = 0;
		if (xmin < ymin)
			moveConstant = xmin;
		else
			moveConstant = ymin;
		if (moveConstant < 0)
			moveConstant = -moveConstant;
		else
			moveConstant = 0;

		final double totalMoveConstant = moveConstant * expandConstant + 30;
		HashMap<Integer, Port> idPortMap = new HashMap<Integer, Port>();

		// 2 Create the graph
		for (Entry<Integer, VertexInfo> entry : vertexIdInformationMap
				.entrySet()) {
			int vertexId = entry.getKey();
			Port sourcePort = null;
			VertexInfo sourceInfo = entry.getValue();
			Point2D.Double pos = sourceInfo.getPosition();
			if (idPortMap.containsKey(vertexId)) {
				System.out.println("contains key " + vertexId);
				sourcePort = idPortMap.get(vertexId);
			} else {
				System.out.println("contains Not key " + vertexId);
				sourcePort = (Port) (graphPane.addDefaultVertexAt(
						totalMoveConstant + expandConstant * pos.getX(),
						totalMoveConstant + expandConstant * pos.getY())
						.getChildAt(0));
				idPortMap.put(vertexId, sourcePort);
			}
			// Connect the source port with all its neightbours
			HashSet<Integer> neightbours = entry.getValue().getNeghtbours();
			Port destPort;
			for (int neighbour : neightbours) {
				VertexInfo destInfo = vertexIdInformationMap.get(neighbour);
				if (idPortMap.containsKey(neighbour)) {
					destPort = idPortMap.get(neighbour);
					System.out.println("contains key " + neighbour);
				} else {
					// Create the destPort
					Point2D.Double destPos = destInfo.getPosition();
					destPort = (Port) (graphPane
							.addDefaultVertexAt(totalMoveConstant
									+ expandConstant * destPos.getX(),
									totalMoveConstant + expandConstant
											* destPos.getY()).getChildAt(0));
					idPortMap.put(neighbour, destPort);
					System.out.println("contains Not key " + neighbour);
				}
				destInfo.getNeghtbours().remove(vertexId);
				System.out.println("connection: source " + vertexId + " "
						+ neighbour);
				graphPane.connect(sourcePort, destPort);
			}
			sourceInfo.getNeghtbours().clear();
			if (Thread.interrupted())
				return;
		}

	}

	/**
	 * @param statusNotifier
	 *            the statusNotifier to set
	 */
	public void setStatusNotifier(StatusNotifier statusNotifier) {
		this.statusNotifier = statusNotifier;
	}
}
