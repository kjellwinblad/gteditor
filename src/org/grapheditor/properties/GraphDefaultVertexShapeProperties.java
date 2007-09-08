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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import org.grapheditor.editor.GraphEditorConstants;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.graph.VertexInformation.Shape;
import org.jgraph.graph.CellView;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a property change module which implements the PropertiesModule
 * interface. It is used to change the default vertex shape of graphs.
 * 
 * @author kjellw
 * 
 */
public class GraphDefaultVertexShapeProperties extends JPanel implements
		PropertiesModule {

	private JPanel jPanel = null;

	private JComboBox jComboBox = null;

	private GraphEditorPane graphPane;

	private JPanel jPanel2;

	public GraphDefaultVertexShapeProperties(GraphEditorPane graphPane) {
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
					"Select default vertex shape:",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
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
			List<Object> list = new LinkedList<Object>();
			for (Shape s : Shape.values()) {
				if (!(s.toString().equals("DEFAULT")))
					list.add(s);
			}
			jComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
			jComboBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (jComboBox.getSelectedItem() == null)
						return;
					Shape item = (Shape) jComboBox.getSelectedItem();


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
					.getDefaultShape());
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
		Shape shape = ((Shape) jComboBox.getSelectedItem());
		graphPane.getGraphProperties().setDefaultShape(shape);
		graphPane.repaint();
	}

	public String toString() {
		return getPropertiesName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		for (Component c : getComponents())
			c.setEnabled(enabled);

		super.setEnabled(enabled);
	}

}
