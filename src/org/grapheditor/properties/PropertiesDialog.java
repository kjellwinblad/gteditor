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

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JRootPane;

import org.grapheditor.editor.GraphEditorPane;

import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;

/**
 * A general properties dialog that are extended by both GlobalPropertiesDialog
 * and GraphPropertiesDialog. It has a list to the left where you can add
 * PropertiesModules, an area where properties modules are displayed, an ok
 * button that saves all modules and closes the dialog and an cancel button that
 * just closes the dialog.
 * 
 * @author kjellw
 * 
 */
public class PropertiesDialog extends JDialog {

	private GraphEditorPane graphPane;

	private JPanel jPanel = null;

	private JSplitPane jSplitPane = null;

	private JScrollPane jScrollPane = null;

	private JList jList = null;

	private JPanel jPanel1 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private List<PropertiesModule> propertiesModules = new LinkedList<PropertiesModule>(); // @jve:decl-index=0:

	private Frame parent;

	public PropertiesDialog(Frame parent, GraphEditorPane graphPane) {
		super(parent);
		this.parent = parent;
		parent.setEnabled(false);
		this.graphPane = graphPane;
		initialize();

	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("Graph Properties");
		this.setContentPane(getJPanel());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {

				parent.setEnabled(true);
			}

		});

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
			jPanel.add(getJSplitPane(), BorderLayout.CENTER);
			jPanel.add(getJPanel1(), BorderLayout.SOUTH);
		}
		return jPanel;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJScrollPane());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
			jList
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							JList selector = (JList) e.getSource();
							PropertiesModule selectedModule = (PropertiesModule) selector
									.getSelectedValue();
							if (selectedModule != null)
								jSplitPane.setRightComponent(selectedModule
										.getPropertiesEditor());
						}
					});
		}
		return jList;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.X_AXIS));
			jPanel1.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			jPanel1.add(Box.createGlue());
			jPanel1.add(getJButton(), null);
			jPanel1.add(getJButton1(), null);
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
			jButton.setText("Cancel");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
					parent.setEnabled(true);
					dispose();
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("OK");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					for (PropertiesModule p : propertiesModules) {
						p.save();
					}
					setVisible(false);
					parent.setEnabled(true);
					dispose();

				}
			});
		}
		return jButton1;
	}

	private boolean firstItem = true;

	public void addPropertiesModule(PropertiesModule module) {
		propertiesModules.add(module);
		jList.setListData(propertiesModules.toArray());

		if (firstItem) {
			jSplitPane.setRightComponent(module.getPropertiesEditor());
			jList.setSelectedValue(module, true);
			firstItem = false;
		}
		pack();

	}

}
