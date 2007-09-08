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
package org.grapheditor.gui.menus;

import java.awt.Component;

import javax.swing.JMenu;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.menus.OperationsMenu;
import org.grapheditor.editor.menus.SelectedVertexColorMenu;
import org.grapheditor.editor.menus.ShapeSelectionMenu;

/**
 * This menu extends OperationMenu that is in the org.grapheditor.editor.menus
 * package to add a submenu to modify selected vertices.
 * 
 * @author kjellw
 */
public class OperationsMenuWithVertexModification extends OperationsMenu {

	private JMenu modifySelectedVerticesMenu;

	private GraphEditorPane graphPane;

	public OperationsMenuWithVertexModification(GraphEditorPane graphPane) {
		super(graphPane);
		this.graphPane = graphPane;
		int insertPos = getLastSelectedVertexOperationPos();
		this.add(getModifySelectedVerticesMenu(), insertPos + 2);
	}

	private JMenu getModifySelectedVerticesMenu() {
		if (modifySelectedVerticesMenu == null) {
			modifySelectedVerticesMenu = new JMenu("Modify selected vertices");
			modifySelectedVerticesMenu.add(new SelectedVertexColorMenu(
					graphPane));
			modifySelectedVerticesMenu.add(new ShapeSelectionMenu(graphPane));
		}
		return modifySelectedVerticesMenu;
	}

	public void menuSelectionChanged(boolean value) {
		super.menuSelectionChanged(value);
		if (value) {
			Object[] selectedVertices = graphPane.getSelectionCells(graphPane
					.getGraphLayoutCache().getCells(false, true, false, false));

			boolean verticesSelected = !((selectedVertices == null) || (selectedVertices.length == 0));

			getModifySelectedVerticesMenu().setEnabled(verticesSelected);

		}
	}

}
