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
package org.grapheditor.gui.toolbars;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.listeners.UndoAndRedoAbleListener;
import org.grapheditor.gui.menus.EditMenu;

/**
 * A toolbar with quick buttons to perform some edit operations.
 * 
 * @author kjellw
 * 
 */
public class EditToolBar extends JToolBar {

	private JButton undoButton = null;

	private JButton redoButton = null;

	private JButton copyButton = null;

	private JButton cutButton = null;

	private JButton pasteButton = null;

	private EditMenu editMenu;

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public EditToolBar(GraphEditorPane graphPane, EditMenu editMenu) {
		super();
		this.graphPane = graphPane;
		this.editMenu = editMenu;
		initialize();
		graphPane.addUndoAndRedoAbleListener(new UndoAndRedoAbleListener() {

			public void canNotRedo() {
				getRedoButton().setEnabled(false);

			}

			public void canNotUndo() {
				getUndoButton().setEnabled(false);

			}

			public void canRedo() {
				getRedoButton().setEnabled(true);

			}

			public void canUndo() {
				getUndoButton().setEnabled(true);

			}

		});

	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setName("Edit quickmenu");
		this.add(getUndoButton());
		this.add(getRedoButton());
		this.add(getCopyButton());
		this.add(getCutButton());
		this.add(getPasteButton());

	}

	/**
	 * This method initializes undoButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton();
			undoButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/undo.png")));
			undoButton.addActionListener(editMenu.getUndoAction());
		}
		return undoButton;
	}

	/**
	 * This method initializes redoButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRedoButton() {
		if (redoButton == null) {
			redoButton = new JButton();
			redoButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/redo.png")));
			redoButton.addActionListener(editMenu.getRedoAction());
		}
		return redoButton;
	}

	/**
	 * This method initializes copyButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCopyButton() {
		if (copyButton == null) {
			copyButton = new JButton();
			copyButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editcopy.png")));
			copyButton.addActionListener(editMenu.getCopyAction());
		}
		return copyButton;
	}

	/**
	 * This method initializes cutButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCutButton() {
		if (cutButton == null) {
			cutButton = new JButton();
			cutButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editcut.png")));
			cutButton.addActionListener(editMenu.getCutAction());
		}
		return cutButton;
	}

	/**
	 * This method initializes pasteButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPasteButton() {
		if (pasteButton == null) {
			pasteButton = new JButton();
			pasteButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/editpaste.png")));
			pasteButton.setName("Edit quickmenu");
			pasteButton.addActionListener(editMenu.getPasteAction());
		}
		return pasteButton;
	}

}
