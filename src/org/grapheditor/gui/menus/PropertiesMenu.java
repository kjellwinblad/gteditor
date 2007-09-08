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
package org.grapheditor.gui.menus;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.properties.ChangeDisplayQuickMenuListListener;
import org.grapheditor.properties.GlobalProperties;
import org.grapheditor.properties.GlobalPropertiesDialog;
import org.grapheditor.properties.GraphPropertiesDialog;

import javax.swing.JSeparator;

/**
 * This menu shows menu items to select a property dialog to show. There is one
 * for global properties and one for graph properties.
 * 
 * @author kjellw
 * 
 */
public class PropertiesMenu extends JMenu {

	private JMenu showQuickMenusProperitesMenu = null;

	private JMenuItem graphPropertiesMenuItem = null;

	private List<JToolBar> toolBars; // @jve:decl-index=0:

	private GraphEditorPane graphPane;

	private JSeparator jSeparator = null;

	private JSeparator jSeparator1 = null;

	private JMenuItem propertiesMenuItem = null;

	private Frame parentFrame;

	/**
	 * This method initializes
	 * 
	 */
	public PropertiesMenu(Frame parentFrame, GraphEditorPane pane) {
		super();
		graphPane = pane;
		this.parentFrame = parentFrame;
		this.toolBars = toolBars;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Properties");
		this.setMnemonic(KeyEvent.VK_P);
		this.add(getShowQuickMenusProperitesMenu());
		this.add(getJSeparator());
		this.add(getGraphPropertiesMenuItem());
		this.add(getJSeparator1());
		this.add(getPropertiesMenuItem());

	}

	/**
	 * This method initializes showQuickMenusProperitesMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getShowQuickMenusProperitesMenu() {
		if (showQuickMenusProperitesMenu == null) {
			showQuickMenusProperitesMenu = new JMenu();
			showQuickMenusProperitesMenu.setText("Show quickmenus");
		}
		return showQuickMenusProperitesMenu;
	}

	public void setJToolBars(List<JToolBar> toolBarsLoc) {
		this.toolBars = toolBarsLoc;
		getShowQuickMenusProperitesMenu().removeAll();
		int count = 0;
		for (final JToolBar t : toolBars) {
			final int listIndex = count;
			final JCheckBoxMenuItem toolSelector = new JCheckBoxMenuItem();
			toolSelector.setText(t.getName());
			toolSelector.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					t.setVisible(toolSelector.isSelected());

					LinkedList<Boolean> displayList = GlobalProperties
							.getInstance().getDisplayQuickMenuList();
					displayList.set(listIndex, toolSelector.isSelected());
					GlobalProperties.getInstance().setDisplayQuickMenuList(
							displayList);
				}

			});
			boolean visible = GlobalProperties.getInstance()
					.getDisplayQuickMenuList().get(listIndex);
			toolSelector.setSelected(visible);
			toolBars.get(listIndex).setVisible(visible);
			getShowQuickMenusProperitesMenu().add(toolSelector);
			count++;
		}
		GlobalProperties.getInstance().addChangeDisplayQuickMenuListListener(
				new ChangeDisplayQuickMenuListListener() {

					public void quickMenuListChanged() {
						List<Boolean> displayList = GlobalProperties
								.getInstance().getDisplayQuickMenuList();
						int count = 0;
						for (boolean display : displayList) {
							toolBars.get(count).setVisible(display);

							count++;
						}

					}

				});

	}

	/**
	 * This method initializes graphPropertiesMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getGraphPropertiesMenuItem() {
		if (graphPropertiesMenuItem == null) {
			graphPropertiesMenuItem = new JMenuItem();
			graphPropertiesMenuItem.setText("Graph properties...");
			graphPropertiesMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							new GraphPropertiesDialog(parentFrame, graphPane)
									.setVisible(true);
						}
					});
		}
		return graphPropertiesMenuItem;
	}

	/**
	 * This method initializes jSeparator
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator() {
		if (jSeparator == null) {
			jSeparator = new JSeparator();
		}
		return jSeparator;
	}

	/**
	 * This method initializes jSeparator1
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator1() {
		if (jSeparator1 == null) {
			jSeparator1 = new JSeparator();
		}
		return jSeparator1;
	}

	/**
	 * This method initializes propertiesMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPropertiesMenuItem() {
		if (propertiesMenuItem == null) {
			propertiesMenuItem = new JMenuItem();
			propertiesMenuItem.setText("Properties...");
			propertiesMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							new GlobalPropertiesDialog(parentFrame, graphPane)
									.setVisible(true);
						}
					});
		}
		return propertiesMenuItem;
	}

}
