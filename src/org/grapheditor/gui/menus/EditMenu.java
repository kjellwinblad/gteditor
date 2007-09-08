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

import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.listeners.InsertModeChangeListener;
import org.grapheditor.editor.listeners.UndoAndRedoAbleListener;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import javax.swing.ImageIcon;

/**
 * The edit menu that is added to the main menu. There are menu items to undo,
 * redo, cut, copy, paste and to select the insert mode etc.
 * 
 * @author kjellw
 * 
 */
public class EditMenu extends JMenu {

	private JMenuItem undoEditMenuItem = null;

	private JMenuItem redoEditMenuItem = null;

	private JSeparator jSeparator = null;

	private JMenuItem cutEditMenuItem = null;

	private JMenuItem copyEditMenuItem = null;

	private JMenuItem pasteEditMenuItem = null;

	private JCheckBoxMenuItem insertModeEditCheckBoxMenuItem = null;

	private JSeparator jSeparator1 = null;

	private GraphEditorPane graphPane;

	private ActionListener undoAction;

	private ActionListener redoAction;

	private ActionListener cutAction; // @jve:decl-index=0:

	private ActionListener copyAction;

	private ActionListener pasteAction; // @jve:decl-index=0:

	/**
	 * Used to indicate that no call to graphPane.setinserMode shall be done the
	 * next time the selected status of the insertModeItem is changed.
	 */
	private boolean dontCall = false;

	/**
	 * This method initializes
	 * 
	 */
	public EditMenu(GraphEditorPane pane) {
		super();
		graphPane = pane;

		initialize();

		graphPane.addInsertModeChangeListener(new InsertModeChangeListener() {
			public void newInsertModeEvent(boolean insertMode) {
				insertModeEditCheckBoxMenuItem.setSelected(insertMode);
			}
		});

		graphPane.addUndoAndRedoAbleListener(new UndoAndRedoAbleListener() {

			public void canNotRedo() {
				getRedoEditMenuItem().setEnabled(false);

			}

			public void canNotUndo() {
				getUndoEditMenuItem().setEnabled(false);

			}

			public void canRedo() {
				getRedoEditMenuItem().setEnabled(true);

			}

			public void canUndo() {
				getUndoEditMenuItem().setEnabled(true);

			}

		});
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Edit");
		this.setMnemonic(KeyEvent.VK_E);
		this.setActionCommand("");
		this.add(getUndoEditMenuItem());
		this.add(getRedoEditMenuItem());
		this.add(getJSeparator());
		this.add(getCutEditMenuItem());
		this.add(getCopyEditMenuItem());
		this.add(getPasteEditMenuItem());
		this.add(getJSeparator1());
		this.add(getInsertModeEditCheckBoxMenuItem());

	}

	/**
	 * This method initializes undoEditMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getUndoEditMenuItem() {
		if (undoEditMenuItem == null) {
			undoEditMenuItem = new JMenuItem();
			undoEditMenuItem.setText("Undo");
			undoEditMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/undo.png")));
			undoAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.undo();
				}
			};
			undoEditMenuItem.addActionListener(undoAction);
		}
		return undoEditMenuItem;
	}

	/**
	 * This method initializes redoEditMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRedoEditMenuItem() {
		if (redoEditMenuItem == null) {
			redoEditMenuItem = new JMenuItem();
			redoEditMenuItem.setText("Redo");
			redoEditMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/redo.png")));
			redoAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.redo();
				}
			};
			redoEditMenuItem.addActionListener(redoAction);
		}
		return redoEditMenuItem;
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
	 * This method initializes cutEditMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutEditMenuItem() {
		if (cutEditMenuItem == null) {
			cutEditMenuItem = new JMenuItem();
			cutEditMenuItem.setText("Cut");
			cutEditMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editcut.png")));
			cutAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.cut(e);

				}
			};
			cutEditMenuItem.addActionListener(cutAction);
		}
		return cutEditMenuItem;
	}

	/**
	 * This method initializes copyEditMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyEditMenuItem() {
		if (copyEditMenuItem == null) {
			copyEditMenuItem = new JMenuItem();
			copyEditMenuItem.setText("Copy");
			copyEditMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editcopy.png")));
			copyAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.copy(e);
				}
			};
			copyEditMenuItem.addActionListener(copyAction);
		}
		return copyEditMenuItem;
	}

	/**
	 * This method initializes pasteEditMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteEditMenuItem() {
		if (pasteEditMenuItem == null) {
			pasteEditMenuItem = new JMenuItem();
			pasteEditMenuItem.setText("Paste");
			pasteEditMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editpaste.png")));
			pasteAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.paste(e);
				}
			};
			pasteEditMenuItem.addActionListener(pasteAction);
		}
		return pasteEditMenuItem;
	}

	/**
	 * This method initializes insertModeEditCheckBoxMenuItem
	 * 
	 * @return javax.swing.JCheckBoxMenuItem
	 */
	private JCheckBoxMenuItem getInsertModeEditCheckBoxMenuItem() {
		if (insertModeEditCheckBoxMenuItem == null) {
			insertModeEditCheckBoxMenuItem = new JCheckBoxMenuItem();
			insertModeEditCheckBoxMenuItem.setText("Insert mode");
			insertModeEditCheckBoxMenuItem.setSelected(true);
			insertModeEditCheckBoxMenuItem
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							graphPane.setInsertMode(((JCheckBoxMenuItem) e
									.getSource()).isSelected());
						}
					});
		}
		return insertModeEditCheckBoxMenuItem;
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
	 * @return the copyAction
	 */
	public ActionListener getCopyAction() {
		return copyAction;
	}

	/**
	 * @return the cutAction
	 */
	public ActionListener getCutAction() {
		return cutAction;
	}

	/**
	 * @return the pasteAction
	 */
	public ActionListener getPasteAction() {
		return pasteAction;
	}

	/**
	 * @return the redoAction
	 */
	public ActionListener getRedoAction() {
		return redoAction;
	}

	/**
	 * @return the undoAction
	 */
	public ActionListener getUndoAction() {
		return undoAction;
	}

}
