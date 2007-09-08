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
import org.grapheditor.editor.listeners.GraphChangeListener;
import org.grapheditor.editor.listeners.GraphPropertiesChangeListener;
import org.grapheditor.editor.listeners.GraphSaveListener;
import org.grapheditor.gui.menus.FileMenu;
import org.grapheditor.properties.GraphProperties;

/**
 * Tool bar with some quick buttons to do some file operations.
 * 
 * @author kjellw
 * 
 */
public class FileToolBar extends JToolBar {

	private JButton newGraphButton = null;

	private JButton saveGraphButton = null;

	private FileMenu fileMenu;

	private JButton saveButton = null;

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public FileToolBar(GraphEditorPane graphPane, FileMenu fileMenu) {
		super();
		this.graphPane = graphPane;
		this.fileMenu = fileMenu;
		initialize();
		GraphPropertiesChangeListener l = new MainGraphPropertiesChangeListener();
		l.graphPropertiesChanged();
		graphPane.addGraphPropertiesChangeListener(l);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setName("File quickmenu");
		this.add(getNewGraphButton());
		this.add(getSaveButton());
		this.add(getSaveGraphButton());

	}

	/**
	 * This method initializes newGraphButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNewGraphButton() {
		if (newGraphButton == null) {
			newGraphButton = new JButton();
			newGraphButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/filenew.png")));
			newGraphButton.addActionListener(fileMenu.getNewAction());
		}
		return newGraphButton;
	}

	/**
	 * This method initializes saveGraphButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveGraphButton() {
		if (saveGraphButton == null) {
			saveGraphButton = new JButton();
			saveGraphButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/fileopen.png")));
			saveGraphButton.addActionListener(fileMenu.getOpenAction());
		}
		return saveGraphButton;
	}

	/**
	 * This method initializes saveButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/filesave.png")));
			saveButton.addActionListener(fileMenu.getSaveAction());
		}
		return saveButton;
	}

	private class MainGraphPropertiesChangeListener implements
			GraphPropertiesChangeListener {

		/**
		 * This shall happen when the properties of a graph is changed
		 */
		public void graphPropertiesChanged() {
			final GraphProperties prop = graphPane.getGraphProperties();

			prop.addGraphChangeListener(new GraphChangeListener() {

				public void graphChanged() {
					getSaveButton().setEnabled(true);

				}

			});

			prop.addSaveListener(new GraphSaveListener() {

				public void graphSaved() {
					getSaveButton().setEnabled(false);

				}

			});
		}
	}
}
