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
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import java.awt.FlowLayout;
import javax.swing.JLabel;

import org.grapheditor.editor.GraphEditorPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is a property change module which implements the PropertiesModule
 * interface. This is used to change the default color for inserted vertices.
 * 
 * @author kjellw
 * 
 */
public class GraphDefaultColorProperties extends JPanel implements
		PropertiesModule {

	private JPanel jPanel = null;

	private JCheckBox jCheckBox = null;

	private JPanel jPanel1 = null;

	private JButton jButton = null;

	private JPanel jPanel2 = null;

	private JButton jButton1 = null;

	private JLabel jLabel = null;

	private JPanel jPanel11 = null;

	private JPanel jPanel21 = null;

	private JLabel jLabel1 = null;

	private JButton jButton11 = null;

	private JButton jButton2 = null;

	private GraphEditorPane graphPane;

	private JColorChooser jColorChooser = null; // @jve:decl-index=0:visual-constraint="233,240"

	/**
	 * This method initializes
	 * 
	 */
	public GraphDefaultColorProperties(GraphEditorPane graph) {
		super();
		this.graphPane = graph;
		initialize();
		GraphProperties gp = graph.getGraphProperties();
		jButton1.setBackground(gp.getDefaultVertexBackgroundColor());
		jButton11.setBackground(gp.getDefaultVertexForegroundColor());

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
		return "Select the default vertex color for the graph";
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
		return "Default vertex color";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#save()
	 */
	public void save() {
		GraphProperties gp = graphPane.getGraphProperties();
		gp.setDefaultUseGraphBackground(jCheckBox.isSelected());
		gp.setDefaultVertexBackgroundColor(jButton1.getBackground());
		gp.setDefaultVertexForegroundColor(jButton11.getBackground());
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
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel11(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText("Use graph background color");
			ActionListener action = new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					getJPanel2().setEnabled(!jCheckBox.isSelected());
					getJButton().setEnabled(!jCheckBox.isSelected());
					getJButton1().setEnabled(!jCheckBox.isSelected());
					jLabel.setEnabled(!jCheckBox.isSelected());
				}

			};
			jCheckBox.addActionListener(action);
			jCheckBox.setSelected(graphPane.getGraphProperties()
					.isDefaultUseGraphBackground());
			if (graphPane.getGraphProperties().isDefaultUseGraphBackground())
				action.actionPerformed(null);

		}
		return jCheckBox;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.Y_AXIS));
			jPanel1.setBorder(BorderFactory.createTitledBorder(null,
					"Vertex background:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel1.add(getJCheckBox(), null);
			jPanel1.add(getJPanel2(), null);
			jPanel1.add(getJButton(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Select new default color...");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color result = getJColorChooser().showDialog(
							getRootPane(),
							"Choose default vertex background color",
							graphPane.getGraphProperties()
									.getDefaultVertexBackgroundColor());
					if (result != null) {
						getJButton1().setBackground(result);
					}
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jLabel = new JLabel();
			jLabel.setText("Current color:");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());
			jPanel2.add(jLabel, null);
			jPanel2.add(getJButton1(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel11
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jPanel11 = new JPanel();
			jPanel11.setLayout(new BoxLayout(getJPanel11(), BoxLayout.Y_AXIS));
			jPanel11.setBorder(BorderFactory.createTitledBorder(null,
					"Vertex foreground:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel11.add(getJPanel21(), null);
			jPanel11.add(getJButton2(), null);
		}
		return jPanel11;
	}

	/**
	 * This method initializes jPanel21
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel21() {
		if (jPanel21 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Current color:");
			jPanel21 = new JPanel();
			jPanel21.setLayout(new FlowLayout());
			jPanel21.add(jLabel1, null);
			jPanel21.add(getJButton11(), null);
		}
		return jPanel21;
	}

	/**
	 * This method initializes jButton11
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton11() {
		if (jButton11 == null) {
			jButton11 = new JButton();
		}
		return jButton11;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Select new default color...");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color result = getJColorChooser().showDialog(
							getRootPane(),
							"Choose default vertex foreground color",
							graphPane.getGraphProperties()
									.getDefaultVertexForegroundColor());
					if (result != null) {
						getJButton11().setBackground(result);
					}
				}
			});
		}
		return jButton2;
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
		}
		return jColorChooser;
	}

}
