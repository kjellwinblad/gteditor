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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuListener;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;



/**
 * This class represents a group of Menu items. Each one represents a color. It
 * is posible to add color selection listeners to it to listen for a color
 * selection and to get the menu items so they can be included in a menu.
 * 
 * @author kjellw
 * 
 */
public class ColorChooserMenuItemGroup {

	private List<Component> menuItems = new LinkedList<Component>();

	private List<ColorSelectedListener> colorSelectionListerners = new LinkedList<ColorSelectedListener>();

	private Component pane;

	public ColorChooserMenuItemGroup(final Component pane) {
		this.pane = pane;
		SortedMap<String, Color> colorMap = new TreeMap<String, Color>();
		colorMap.put("Red", Color.RED);
		colorMap.put("orange", Color.ORANGE);
		colorMap.put("yellow", Color.YELLOW);
		colorMap.put("green", Color.GREEN);
		colorMap.put("blue", Color.BLUE);
		colorMap.put("Black", Color.BLACK);
		colorMap.put("Grey", Color.GRAY);
		colorMap.put("White", Color.WHITE);

		Set<Entry<String, Color>> colorEntries = colorMap.entrySet();

		// Add all color menu items
		for (final Entry<String, Color> colorEntry : colorEntries) {
			JMenuItem item = new JMenuItem(colorEntry.getKey());
			item.setBackground(colorEntry.getValue());
			menuItems.add(item);
			item.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					for (ColorSelectedListener l : colorSelectionListerners) {
						l.colorSelected(colorEntry.getValue());
					}

				}

			});
		}

		menuItems.add(new JSeparator());
		JMenuItem item = new JMenuItem("Select color...");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JColorChooser colorChooser = new JColorChooser();
				Color color = colorChooser.showDialog(pane, "Choose color",
						Color.BLACK);
				if (color != null) {
					for (ColorSelectedListener l : colorSelectionListerners) {
						l.colorSelected(color);
					}
				}
			}

		});
		menuItems.add(item);
	}

	public void addColorSelectedListener(ColorSelectedListener l) {
		colorSelectionListerners.add(l);
	}

	/**
	 * @return the menuItems
	 */
	public List<Component> getMenuItems() {
		return menuItems;
	}

}
