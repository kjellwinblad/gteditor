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

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.grapheditor.properties.GlobalProperties;

/**
 * 
 * This is a class that only contain an static main method to start the
 * application. It simply starts the application.
 * 
 * @author Kjell Winblad
 * 
 */
public class GraphEditor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String uiClassName = GlobalProperties.getInstance().getUITheme();
			if (null != uiClassName) {
				try {
					UIManager.setLookAndFeel((LookAndFeel) Class.forName(
							uiClassName).newInstance());
				} catch (Exception e) {
					System.err.println("Could not load theme: " + uiClassName);
				}
			}
			MainWindow window = new MainWindow();

			window.setVisible(true);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e.getMessage(),
					"An error has occured", JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();

		}

	}

}
