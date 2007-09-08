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
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.Font;
import java.awt.Color;

/**
 * This is a property change module which implements the PropertiesModule
 * interface. It is used to change which vertex type that will be inserted by
 * default.
 * 
 * @author kjellw
 */
public class GraphInsertVertexShapeProperties extends JPanel implements
		PropertiesModule {

	private GraphEditorPane graphPane;

	private GraphDefaultVertexShapeProperties graphDefaultVertexShapeProperties = null;

	private JPanel jPanel = null;

	private JComboBox jComboBox = null;

	private JPanel jPanel2;

	public GraphInsertVertexShapeProperties(GraphEditorPane graphPane) {
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
		this.add(getJPanel2(), BorderLayout.CENTER);
		// this.add(getGraphDefaultVertexShapeProperties(), BorderLayout.SOUTH);

	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Shape:",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel2.setLayout(new FlowLayout());

		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Select default insert vertex shape:",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel.add(getJComboBox(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			// jComboBox.setRenderer(new SpecialComboBoxRender());
			jComboBox.setModel(new DefaultComboBoxModel(Shape.values()));

			jComboBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					if (jComboBox.getSelectedItem() == null)
						return;
					Shape item = (Shape) jComboBox.getSelectedItem();

					if (item == Shape.DEFAULT) {
						getJPanel2().setVisible(false);
						getJPanel2().removeAll();
						getJPanel2().setLayout(new BorderLayout());
						getGraphDefaultVertexShapeProperties().setVisible(true);
						getJPanel2().add(
								getGraphDefaultVertexShapeProperties(),
								BorderLayout.CENTER);
						getJPanel2().setVisible(true);
						return;
					} else {
						getGraphDefaultVertexShapeProperties()
								.setVisible(false);
						getJPanel2().setLayout(new FlowLayout());
						getJPanel2().setVisible(true);
					}

					String className = (item.toString().charAt(0) + item
							.toString().toLowerCase().substring(1));
					Class<VertexRenderer> renderClass = null;
					try {
						renderClass = (Class<VertexRenderer>) Class
								.forName("org.grapheditor.editor.vertexrenders."
										+ className + "VertexRenderComponent");
					} catch (ClassNotFoundException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					VertexRenderer render = null;
					try {
						render = renderClass.newInstance();
					} catch (InstantiationException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IllegalAccessException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}

					VertexView view = new VertexView();
					getJPanel2().removeAll();
					getJPanel2().setVisible(false);
					render.setPreferredSize(new Dimension(20, 20));
					getJPanel2().add(
							render.getRendererComponent(null, view, false,
									false, true), BorderLayout.CENTER);
					getJPanel2().setVisible(true);

				}

			});
			jComboBox.setSelectedItem(graphPane.getGraphProperties()
					.getDefaultInsertShape());
			jComboBox.setEditable(false);

		}
		return jComboBox;
	}

	public String getPropertiesDescription() {
		// TODO Auto-generated method stub
		return "Select the default shape of vertices.";
	}

	public Component getPropertiesEditor() {
		// TODO Auto-generated method stub
		return this;
	}

	public String getPropertiesName() {
		// TODO Auto-generated method stub
		return "Default vertex shape";
	}

	public void save() {
		getGraphDefaultVertexShapeProperties().save();
		graphPane.getGraphProperties().setDefaultInsertShape(
				(Shape) jComboBox.getSelectedItem());
		graphPane.repaint();
	}

	public String toString() {
		return getPropertiesName();
	}

	/**
	 * This method initializes graphDefaultVertexShapeProperties
	 * 
	 * @return org.grapheditor.editor.properties.GraphDefaultVertexShapeProperties
	 */
	private GraphDefaultVertexShapeProperties getGraphDefaultVertexShapeProperties() {
		if (graphDefaultVertexShapeProperties == null) {
			graphDefaultVertexShapeProperties = new GraphDefaultVertexShapeProperties(
					graphPane);
			graphDefaultVertexShapeProperties.setVisible(false);
		}
		return graphDefaultVertexShapeProperties;
	}

}
