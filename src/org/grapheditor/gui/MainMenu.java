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
package org.grapheditor.gui;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.menus.OperationsMenu;
import org.grapheditor.editor.menus.SelectionMenu;
import org.grapheditor.gui.menus.FileMenu;
import org.grapheditor.gui.menus.EditMenu;
import org.grapheditor.gui.menus.OperationsMenuWithVertexModification;
import org.grapheditor.gui.menus.ViewMenu;
import org.grapheditor.gui.menus.PropertiesMenu;
import org.grapheditor.gui.menus.HelpAboutMenu;

/**
 * This is the main menu in the program. It contains a lot of submenus to let
 * the user select what he/she wants to do with the program.
 * 
 * @author kjellw
 * 
 */
public class MainMenu extends JMenuBar {

	private FileMenu fileMenu = null;

	private EditMenu editMenu = null;

	private SelectionMenu selectionMenu = null;

	private ViewMenu viewMenu = null;

	private PropertiesMenu propertiesMenu = null;

	private HelpAboutMenu helpAboutMenu = null;

	private GraphEditorPane graphPane;

	private MainWindow mainFrame;

	private List<JToolBar> toolBars;

	private OperationsMenuWithVertexModification operationsMenu = null;

	/**
	 * This method initializes
	 * 
	 */
	public MainMenu(GraphEditorPane pane, MainWindow mainFrame) {
		super();
		this.toolBars = toolBars;
		this.mainFrame = mainFrame;
		graphPane = pane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.add(getFileMenu());
		this.add(getEditMenu());
		this.add(getViewMenu());
		this.add(getSelectionMenu());
		this.add(getOperationsMenuWithVertexModification());
		this.add(getPropertiesMenu());
		this.add(getHelpAboutMenu());

	}

	/**
	 * This method initializes fileMenu
	 * 
	 * @return org.grapheditor.gui.menus.FileMenu
	 */
	protected FileMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new FileMenu(graphPane, mainFrame);
		}
		return fileMenu;
	}

	/**
	 * This method initializes editMenu
	 * 
	 * @return org.grapheditor.gui.menus.EditMenu
	 */
	public EditMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new EditMenu(graphPane);
		}
		return editMenu;
	}

	/**
	 * This method initializes selectionMenu
	 * 
	 * @return org.grapheditor.gui.menus.SelectionMenu
	 */
	private SelectionMenu getSelectionMenu() {
		if (selectionMenu == null) {
			selectionMenu = new SelectionMenu(graphPane);
		}
		return selectionMenu;
	}

	/**
	 * This method initializes viewMenu
	 * 
	 * @return org.grapheditor.gui.menus.ViewMenu
	 */
	public ViewMenu getViewMenu() {
		if (viewMenu == null) {
			viewMenu = new ViewMenu(graphPane, mainFrame);
		}
		return viewMenu;
	}

	/**
	 * This method initializes propertiesMenu
	 * 
	 * @return org.grapheditor.gui.menus.PropertiesMenu
	 */
	public PropertiesMenu getPropertiesMenu() {
		if (propertiesMenu == null) {
			propertiesMenu = new PropertiesMenu(mainFrame, graphPane);
		}
		return propertiesMenu;
	}

	/**
	 * This method initializes helpAboutMenu
	 * 
	 * @return org.grapheditor.gui.menus.HelpAboutMenu
	 */
	private HelpAboutMenu getHelpAboutMenu() {
		if (helpAboutMenu == null) {
			helpAboutMenu = new HelpAboutMenu(mainFrame);
		}
		return helpAboutMenu;
	}

	/**
	 * This method initializes OperationsMenuWithVertexModification
	 * 
	 * @return org.grapheditor.gui.menus.OperationsMenuWithVertexModification
	 */
	private OperationsMenuWithVertexModification getOperationsMenuWithVertexModification() {
		if (operationsMenu == null) {
			operationsMenu = new OperationsMenuWithVertexModification(graphPane);
		}
		return operationsMenu;
	}

}
