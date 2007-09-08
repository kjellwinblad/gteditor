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

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.grapheditor.gui.AboutDialog;
import org.grapheditor.properties.GlobalProperties;

/**
 * A menu where you can choose to open the about menu and the help menu.
 * 
 * @author kjellw
 * 
 */
public class HelpAboutMenu extends JMenu {

	private JMenuItem helpHelpMenuItem = null;

	private JMenuItem aboutHelpMenuItem = null;

	private Frame mainFrame;

	/**
	 * This method initializes
	 * 
	 */
	public HelpAboutMenu(Frame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Help");
		this.add(getHelpHelpMenuItem());
		this.add(getAboutHelpMenuItem());

	}

	/**
	 * This method initializes helpHelpMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getHelpHelpMenuItem() {
		if (helpHelpMenuItem == null) {
			helpHelpMenuItem = new JMenuItem();
			helpHelpMenuItem.setText("Help...");
			helpHelpMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							GlobalProperties.getInstance().getHelpBroker()
									.setDisplayed(true);
							GlobalProperties.getInstance().getHelpBroker()
									.setCurrentID("introduction");
						}
					});
		}
		return helpHelpMenuItem;
	}

	/**
	 * This method initializes aboutHelpMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutHelpMenuItem() {
		if (aboutHelpMenuItem == null) {
			aboutHelpMenuItem = new JMenuItem();
			aboutHelpMenuItem.setText("About...");
			aboutHelpMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							AboutDialog.showAboutDialog(mainFrame);
						}
					});
		}
		return aboutHelpMenuItem;
	}

}
