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
package org.grapheditor.editor.menus;

import java.awt.Color;
import java.awt.Component;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.VertexInformation;
import org.jgraph.graph.DefaultGraphCell;

/**
 * This menu is used to select the certex color. It is displayed as a submenu
 * under the OperationMenu.
 * 
 * @author kjellw
 * 
 */
public class SelectedVertexColorMenu extends JMenu {

	private JMenuItem jMenuItem = null;

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public SelectedVertexColorMenu(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Select color");
		this.add(getJMenuItem());
		addColorSelectionItems();
		addSelectForegroundMenu();
	}

	private void addSelectForegroundMenu() {
		JMenu foregroundMenu = new JMenu("Select foreground color");
		ColorChooserMenuItemGroup colorChooser = new ColorChooserMenuItemGroup(
				getRootPane());
		for (Component c : colorChooser.getMenuItems()) {
			foregroundMenu.add(c);
		}
		colorChooser.addColorSelectedListener(new ColorSelectedListener() {

			public void colorSelected(Color color) {
				for (Object object : graphPane.getSelectionCells()) {
					Object userObject = ((DefaultGraphCell) object)
							.getUserObject();
					if (userObject instanceof VertexInformation) {

						Map nested = new Hashtable();
						Map atributeMap = new Hashtable();
						GraphEditorConstants.setForeground(atributeMap, color);
						nested.put(object, atributeMap);
						graphPane.getGraphLayoutCache().edit(nested);
					}
				}
				graphPane.repaint();

			}

		});

		add(new JSeparator());
		add(foregroundMenu);
	}

	private void addColorSelectionItems() {
		ColorChooserMenuItemGroup colorChooser = new ColorChooserMenuItemGroup(
				getRootPane());
		for (Component c : colorChooser.getMenuItems()) {
			add(c);
		}
		colorChooser.addColorSelectedListener(new ColorSelectedListener() {

			public void colorSelected(Color color) {
				for (Object object : graphPane.getSelectionCells()) {
					Object userObject = ((DefaultGraphCell) object)
							.getUserObject();
					if (userObject instanceof VertexInformation) {
						Map nested = new Hashtable();
						Map atributeMap = new Hashtable();
						GraphEditorConstants.setBackground(atributeMap, color);
						GraphEditorConstants.setUseGraphBackground(atributeMap,
								false);
						nested.put(object, atributeMap);
						graphPane.getGraphLayoutCache().edit(nested);
					}
				}
				graphPane.repaint();

			}

		});
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Use graph background color");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					for (Object object : graphPane.getSelectionCells()) {
						Object userObject = ((DefaultGraphCell) object)
								.getUserObject();
						if (userObject instanceof VertexInformation) {
							Map nested = new Hashtable();
							Map atributeMap = new Hashtable();
							GraphEditorConstants.setUseGraphBackground(
									atributeMap, true);
							nested.put(object, atributeMap);
							graphPane.getGraphLayoutCache().edit(nested);

							graphPane.repaint();

						}
					}
				}
			});
		}
		return jMenuItem;
	}

}
