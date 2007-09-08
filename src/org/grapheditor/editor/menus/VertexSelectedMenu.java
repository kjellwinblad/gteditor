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

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.SplitSelectedEdgesDialog;
import org.grapheditor.editor.listeners.InsertModeChangeListener;

import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;

/**
 * This menu is displayed when vertices is selected in the graph. It contains
 * menu items such as cut, copy, paste, delete, perform operation etc.
 * 
 * @author kjellw
 * 
 */
public class VertexSelectedMenu extends JPopupMenu {

	private JMenuItem CopyItem = null;

	private JMenuItem CutItem = null;

	private JMenuItem PasteItem = null;

	GraphEditorPane graphPane;

	private JSeparator jSeparator = null;

	private JMenuItem deleteItem = null;

	private JSeparator jSeparator1 = null;

	private JCheckBoxMenuItem insertModeItem = null;

	private ShapeSelectionMenu shapeSelectionMenu = null;

	private JSeparator jSeparator2 = null;

	private SelectedVertexColorMenu selectedVertexColorMenu = null;

	private JSeparator jSeparator3 = null;

	private JSeparator jSeparator31 = null;

	private JMenuItem jMenuItem = null;

	private boolean edgesSelected;

	private JSeparator jSeparator4 = null;

	private OperationsMenu operationsMenu = null;

	/**
	 * This method initializes
	 * 
	 */
	public VertexSelectedMenu(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		this.edgesSelected = edgesSelected;
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
		this.add(getCopyItem());
		this.add(getCutItem());
		this.add(getPasteItem());
		this.add(getJSeparator());
		this.add(getDeleteItem());
		this.add(getJSeparator1());
		this.add(getShapeSelectionMenu());
		this.add(getJSeparator2());
		this.add(getSelectedVertexColorMenu());
		this.add(getJSeparator31());
		this.add(getJMenuItem());
		this.add(getJSeparator3());
		this.add(getOperationsMenu());
		this.add(getJSeparator4());
		this.add(getInsertModeItem());

	}

	/**
	 * This method initializes CopyItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyItem() {
		if (CopyItem == null) {
			CopyItem = new JMenuItem();
			CopyItem.setText("Copy");
			CopyItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.copy(e);
				}
			});
		}
		return CopyItem;
	}

	/**
	 * This method initializes CutItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutItem() {
		if (CutItem == null) {
			CutItem = new JMenuItem();
			CutItem.setText("Cut");
			CutItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.cut(e);
				}
			});
		}
		return CutItem;
	}

	/**
	 * This method initializes PasteItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteItem() {
		if (PasteItem == null) {
			PasteItem = new JMenuItem();
			PasteItem.setText("Paste");
			PasteItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.paste(e);
				}
			});
		}
		return PasteItem;
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
	 * This method initializes deleteItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getDeleteItem() {
		if (deleteItem == null) {
			deleteItem = new JMenuItem();
			deleteItem.setText("Delete");
			deleteItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.removeSelected();
				}
			});
		}
		return deleteItem;
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

	/**
	 * This method initializes shapeSelectionMenu
	 * 
	 * @return org.grapheditor.gui.menus.ShapeSelectionMenu
	 */
	private ShapeSelectionMenu getShapeSelectionMenu() {
		if (shapeSelectionMenu == null) {
			shapeSelectionMenu = new ShapeSelectionMenu(graphPane);
		}
		return shapeSelectionMenu;
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
	 * This method initializes selectedVertexColorMenu
	 * 
	 * @return org.grapheditor.gui.menus.SelectedVertexColorMenu
	 */
	private SelectedVertexColorMenu getSelectedVertexColorMenu() {
		if (selectedVertexColorMenu == null) {
			selectedVertexColorMenu = new SelectedVertexColorMenu(graphPane);
		}
		return selectedVertexColorMenu;
	}

	/**
	 * This method initializes jSeparator3
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator3() {
		if (jSeparator3 == null) {
			jSeparator3 = new JSeparator();
		}
		return jSeparator3;
	}

	/**
	 * This method initializes jSeparator31
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator31() {
		if (jSeparator31 == null) {
			jSeparator31 = new JSeparator();
		}
		return jSeparator31;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem("Subdivide edge(s)");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SplitSelectedEdgesDialog.show(graphPane);
				}
			});
		}
		return jMenuItem;
	}

	/**
	 * @return the edgesSelected
	 */
	public boolean isEdgesSelected() {
		return edgesSelected;
	}

	/**
	 * @param edgesSelected
	 *            the edgesSelected to set
	 */
	public void setEdgesSelected(boolean edgesSelected) {
		this.edgesSelected = edgesSelected;

		getJMenuItem().setVisible(this.edgesSelected);
		getJSeparator31().setVisible(this.edgesSelected);

	}

	/**
	 * This method initializes jSeparator4
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator4() {
		if (jSeparator4 == null) {
			jSeparator4 = new JSeparator();
		}
		return jSeparator4;
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
