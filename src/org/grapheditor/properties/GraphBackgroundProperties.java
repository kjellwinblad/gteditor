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
package org.grapheditor.properties;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JColorChooser;

import org.grapheditor.editor.GraphEditorPane;

/**
 * This is a property change module wich implements the PropertiesModule
 * interface. It is used to change the graph properties for the backround color.
 * 
 * @author kjellw
 * 
 */
public class GraphBackgroundProperties extends JPanel implements
		PropertiesModule {

	private JColorChooser jColorChooser = null;

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public GraphBackgroundProperties(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.add(getJColorChooser(), null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#getPropertiesDescription()
	 */
	public String getPropertiesDescription() {
		// TODO Auto-generated method stub
		return "Change the settings for the background color of the graph";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#getPropertiesEditor()
	 */
	public Component getPropertiesEditor() {
		// TODO Auto-generated method stub
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#getPropertiesName()
	 */
	public String getPropertiesName() {
		return "Background properties";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#save()
	 */
	public void save() {
		graphPane.setBackground(jColorChooser.getColor());
		if (!graphPane.getGraphProperties().getBackgroundColor().equals(
				jColorChooser.getColor())) {
			graphPane.getGraphProperties().setBackgroundColor(
					jColorChooser.getColor());
		}

	}

	public String toString() {
		return getPropertiesName();
	}

	/**
	 * This method initializes jColorChooser
	 * 
	 * @return javax.swing.JColorChooser
	 */
	private JColorChooser getJColorChooser() {
		if (jColorChooser == null) {
			jColorChooser = new JColorChooser();
			jColorChooser.setColor(graphPane.getGraphProperties()
					.getBackgroundColor());
		}
		return jColorChooser;
	}

}
