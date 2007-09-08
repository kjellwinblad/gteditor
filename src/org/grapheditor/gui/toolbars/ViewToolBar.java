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
package org.grapheditor.gui.toolbars;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import org.grapheditor.gui.menus.ViewMenu;

/**
 * Tool br with some quick buttons to do some view changes.
 * 
 * @author kjellw
 * 
 */
public class ViewToolBar extends JToolBar {

	private JButton zoomInButton = null;

	private JButton zoomOutButton = null;

	private JButton standardZoomLevelButton = null;

	private ViewMenu viewMenu;

	/**
	 * This method initializes
	 * 
	 */
	public ViewToolBar(ViewMenu viewMenu) {
		super();
		this.viewMenu = viewMenu;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setName("View quickmenu");
		this.add(getZoomInButton());
		this.add(getZoomOutButton());
		this.add(getStandardZoomLevelButton());

	}

	/**
	 * This method initializes zoomInButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getZoomInButton() {
		if (zoomInButton == null) {
			zoomInButton = new JButton();
			zoomInButton.addActionListener(viewMenu.getZoomInAction());
			zoomInButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/viewmag+.png")));
		}
		return zoomInButton;
	}

	/**
	 * This method initializes zoomOutButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getZoomOutButton() {
		if (zoomOutButton == null) {
			zoomOutButton = new JButton();
			zoomOutButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/viewmag-.png")));
			zoomOutButton.addActionListener(viewMenu.getZoomOutAction());
		}
		return zoomOutButton;
	}

	/**
	 * This method initializes standardZoomLevelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStandardZoomLevelButton() {
		if (standardZoomLevelButton == null) {
			standardZoomLevelButton = new JButton();
			standardZoomLevelButton.setIcon(new ImageIcon(getClass()
					.getResource(
							"/org/grapheditor/gui/icons/16x16/viewmag1.png")));
			standardZoomLevelButton.addActionListener(viewMenu
					.getDefaultZommLevelAction());
		}
		return standardZoomLevelButton;
	}

}
