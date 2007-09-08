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
package org.grapheditor.gui.menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JViewport;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.gui.MainWindow;
import org.grapheditor.properties.GlobalProperties;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 * This menu has menu items to shange the view. To either zoom in or out or to
 * display the center point of the graph in the center point of the window
 * 
 * @author kjellw
 * 
 */
public class ViewMenu extends JMenu {

	private JMenuItem zoomInViewMenuItem = null;

	private JMenuItem zoomOutViewMenuItem = null;

	private JSeparator jSeparator = null;

	private JMenuItem centerViewMenuItem = null;

	private GraphEditorPane graphPane;

	private JMenuItem defaultZoomMenuItem = null;

	ActionListener zoomInAction;

	ActionListener zoomOutAction;

	ActionListener defaultZommLevelAction;

	private MainWindow mainFrame;

	/**
	 * This method initializes
	 * 
	 * @param mainFrame
	 * 
	 */
	public ViewMenu(GraphEditorPane pane, MainWindow mainFrame) {
		super();
		graphPane = pane;
		this.mainFrame = mainFrame;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("View");
		this.setMnemonic(KeyEvent.VK_V);
		this.add(getZoomInViewMenuItem());
		this.add(getZoomOutViewMenuItem());
		this.add(getDefaultZoomMenuItem());
		this.add(getJSeparator());
		this.add(getCenterViewMenuItem());

	}

	/**
	 * This method initializes zoomInViewMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getZoomInViewMenuItem() {
		if (zoomInViewMenuItem == null) {
			zoomInViewMenuItem = new JMenuItem();
			zoomInViewMenuItem.setText("Zoom in");
			zoomInViewMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/viewmag+.png")));
			zoomInAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.zoomIn();
				}
			};
			zoomInViewMenuItem.addActionListener(zoomInAction);
		}
		return zoomInViewMenuItem;
	}

	/**
	 * This method initializes zoomOutViewMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getZoomOutViewMenuItem() {
		if (zoomOutViewMenuItem == null) {
			zoomOutViewMenuItem = new JMenuItem();
			zoomOutViewMenuItem.setText("Zoom out");
			zoomOutViewMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/viewmag-.png")));
			zoomOutAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.zoomOut();
				}
			};
			zoomOutViewMenuItem.addActionListener(zoomOutAction);
		}
		return zoomOutViewMenuItem;
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
	 * This method initializes centerViewMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCenterViewMenuItem() {
		if (centerViewMenuItem == null) {
			centerViewMenuItem = new JMenuItem();
			centerViewMenuItem.setText("Center");
			centerViewMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JViewport viewport = mainFrame
									.getGraphPaneScrollPane().getViewport();
							viewport.setViewPosition(new Point(
									(int) graphPane.getGraphCenterPoint()
											.getX()
											- (int) (viewport.getSize()
													.getHeight() / 2),
									(int) graphPane.getGraphCenterPoint()
											.getY()
											- (int) (viewport.getSize()
													.getHeight() / 2)));
						}

					});
		}
		return centerViewMenuItem;
	}

	/**
	 * This method initializes defaultZoomMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getDefaultZoomMenuItem() {
		if (defaultZoomMenuItem == null) {
			defaultZoomMenuItem = new JMenuItem();
			defaultZoomMenuItem.setText("Default");
			defaultZoomMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/viewmag1.png")));
			defaultZommLevelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.setDefaultZoomLevel();
				}
			};
			defaultZoomMenuItem.addActionListener(defaultZommLevelAction);
		}
		return defaultZoomMenuItem;
	}

	/**
	 * @return the defaultZommLevelAction
	 */
	public ActionListener getDefaultZommLevelAction() {
		return defaultZommLevelAction;
	}

	/**
	 * @return the zoomInAction
	 */
	public ActionListener getZoomInAction() {
		return zoomInAction;
	}

	/**
	 * @return the zoomOutAction
	 */
	public ActionListener getZoomOutAction() {
		return zoomOutAction;
	}

}
