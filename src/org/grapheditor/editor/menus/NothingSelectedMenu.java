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

import java.awt.Component;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.listeners.InsertModeChangeListener;

/**
 * This menu is showed when the user right click in the graph pane and nothing
 * is selected. It has menu items to paste, insert vertex perform operations
 * etc.
 * 
 * @author kjellw
 * 
 */
public class NothingSelectedMenu extends JPopupMenu {

	private JMenuItem pasteItem = null;

	private JSeparator jSeparator = null;

	private JMenuItem insertVertexItem = null;

	private JCheckBoxMenuItem insertModeItem = null;

	private GraphEditorPane graphPane;

	private int graphXpos;

	private int graphYpos;

	private JSeparator jSeparator1 = null;

	private JSeparator jSeparator2 = null;

	private OperationsMenu operationsMenu = null;

	/**
	 * This method initializes
	 * 
	 */
	public NothingSelectedMenu(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
		graphPane.addInsertModeChangeListener(new InsertModeChangeListener() {
			public void newInsertModeEvent(boolean insertMode) {
				insertModeItem.setSelected(insertMode);
			}
		});
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.add(getPasteItem());
		this.add(getJSeparator());
		this.add(getInsertVertexItem());
		this.add(getJSeparator2());
		this.add(getOperationsMenu());
		this.add(getJSeparator1());
		this.add(getInsertModeItem());

	}

	/**
	 * This method initializes pasteItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteItem() {
		if (pasteItem == null) {
			pasteItem = new JMenuItem();
			pasteItem.setText("Paste");
			pasteItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.paste(e);
				}
			});
		}
		return pasteItem;
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
	 * This method initializes insertVertexItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getInsertVertexItem() {
		if (insertVertexItem == null) {
			insertVertexItem = new JMenuItem();
			insertVertexItem.setText("Insert vertex");
			insertVertexItem
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
						
							graphPane.addDefaultVertexAt(graphXpos, graphYpos);
						}
					});
		}
		return insertVertexItem;
	}

	/**
	 * This method initializes insertModeItem
	 * 
	 * @return javax.swing.JCheckBoxMenuItem
	 */
	private JCheckBoxMenuItem getInsertModeItem() {
		if (insertModeItem == null) {
			insertModeItem = new JCheckBoxMenuItem();
			insertModeItem.setText("Insert mode");
			insertModeItem.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					graphPane.setInsertMode(((JCheckBoxMenuItem) e.getSource())
							.isSelected());
				}
			});
			insertModeItem.setSelected(true);
		}
		return insertModeItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
	 */
	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
		graphXpos = x;
		graphYpos = y;
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
	 * This method initializes jSeparator2
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator2() {
		if (jSeparator2 == null) {
			jSeparator2 = new JSeparator();
		}
		return jSeparator2;
	}

	/**
	 * This method initializes operationsMenu
	 * 
	 * @return org.grapheditor.gui.menus.OperationsMenu
	 */
	private OperationsMenu getOperationsMenu() {
		if (operationsMenu == null) {
			operationsMenu = new OperationsMenu(graphPane);
		}
		return operationsMenu;
	}

}
