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
package org.grapheditor.editor.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.jgraph.graph.DefaultGraphCell;

/**
 * This menu is used to select the vertex shape of selected vertices.
 * 
 * @author kjellw
 * 
 */
public class ShapeSelectionMenu extends JMenu {

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public ShapeSelectionMenu(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
		addShapeChooseItems();
	}

	private void addShapeChooseItems() {

		Shape selectedShape = null;
		for (Object object : graphPane.getSelectionCells()) {
			Object userObject = ((DefaultGraphCell) object).getUserObject();
			if (userObject instanceof VertexInformation) {
				selectedShape = GraphEditorConstants
						.getShape(((DefaultGraphCell) object).getAttributes());

			}
		}
		if (selectedShape == null)
			selectedShape = Shape.DEFAULT;

		ButtonGroup group = new ButtonGroup();

		for (final Shape shape : Shape.values()) {
			JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(shape
					.toString());
			group.add(menuItem);
			if (shape == selectedShape)
				menuItem.setSelected(true);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					for (Object object : graphPane.getSelectionCells()) {
						Object userObject = ((DefaultGraphCell) object)
								.getUserObject();
						if (userObject instanceof VertexInformation) {

							Map nested = new Hashtable();
							Map atributeMap = new Hashtable();
							GraphEditorConstants.setShape(atributeMap, shape);
							nested.put(object, atributeMap);
							graphPane.getGraphLayoutCache().edit(nested);
							graphPane.repaint();
						}
					}

				}

			});
			this.add(menuItem);

		}

	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Select shape");

	}

}
