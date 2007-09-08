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
package org.grapheditor.properties;

import java.awt.Component;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;

import org.grapheditor.editor.GraphEditorPane;

import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.BorderLayout;

/**
 * This is a property change module which implements the PropertiesModule
 * interface. It is used to change the global properties for how the graph is
 * drawn. The graph can be drawn with antialias and double buffering.
 * 
 * @author kjellw
 * 
 */
public class GlobalGraphDrawingProperties extends JPanel implements
		PropertiesModule {

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JCheckBox antialiasCheckBox = null;

	private JPanel jPanel11 = null;

	private JCheckBox doubleBufferingCheckBox = null;

	private GraphEditorPane graphPane;

	/**
	 * This method initializes
	 * 
	 */
	public GlobalGraphDrawingProperties(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJPanel(), BorderLayout.NORTH);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#getPropertiesDescription()
	 */
	public String getPropertiesDescription() {
		// TODO Auto-generated method stub
		return "Properties for how the graph is painted in the graph windows";
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
		// TODO Auto-generated method stub
		return "Graph drawing";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#save()
	 */
	public void save() {
		GlobalProperties.getInstance().setAntialiasing(
				antialiasCheckBox.isSelected());
		graphPane.setAntiAliased(antialiasCheckBox.isSelected());
		GlobalProperties.getInstance().setDoubleBuffering(
				doubleBufferingCheckBox.isSelected());
		graphPane.setDoubleBuffered(doubleBufferingCheckBox.isSelected());
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
			jPanel.setName("jPanel");
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel11(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.X_AXIS));
			jPanel1.setBorder(BorderFactory.createTitledBorder(null,
					"Antialias:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel1.add(getAntialiasCheckBox(), null);
			jPanel1.add(Box.createGlue());
		}
		return jPanel1;
	}

	/**
	 * This method initializes antialiasCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getAntialiasCheckBox() {
		if (antialiasCheckBox == null) {
			antialiasCheckBox = new JCheckBox();
			antialiasCheckBox.setText("Enable antialias on graph");
			antialiasCheckBox.setSelected(GlobalProperties.getInstance()
					.isAntialiasing());
		}
		return antialiasCheckBox;
	}

	/**
	 * This method initializes jPanel11
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jPanel11 = new JPanel();
			jPanel11.setLayout(new BoxLayout(getJPanel11(), BoxLayout.X_AXIS));
			jPanel11.setBorder(BorderFactory.createTitledBorder(null,
					"Double buffering:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel11.add(getDoubleBufferingCheckBox(), null);
			jPanel11.add(Box.createGlue());
		}
		return jPanel11;
	}

	/**
	 * This method initializes doubleBufferingCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getDoubleBufferingCheckBox() {
		if (doubleBufferingCheckBox == null) {
			doubleBufferingCheckBox = new JCheckBox();
			doubleBufferingCheckBox.setText("Enable double buffering on graph");
			doubleBufferingCheckBox.setSelected(GlobalProperties.getInstance()
					.isDoubleBuffering());
		}
		return doubleBufferingCheckBox;
	}

	public String toString() {
		return getPropertiesName();
	}

}
