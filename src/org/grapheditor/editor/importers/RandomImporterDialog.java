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
package org.grapheditor.editor.importers;

import java.awt.Frame;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.importers.RandomImporter.InvalidParameters;
import org.grapheditor.gui.CancelAbleProcess;
import org.grapheditor.gui.CancelAbleProcessDialog;
import org.grapheditor.gui.StatusNotifier;

import javax.swing.JSlider;
import java.awt.Font;
import java.awt.Color;

/**
 * This class represents a dialog that lets the user choose a number of vertices
 * and a number of edges to import randomly using the RandomImporter class.
 * 
 * @author kjellw
 * 
 */
public class RandomImporterDialog extends JDialog {

	private Frame parent;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private JPanel jPanel3 = null;

	private JPanel jPanel4 = null;

	private JSpinner vertexSpinner = null;

	private JSpinner edgeSpinner = null;

	private JPanel jPanel5 = null;

	private JLabel jLabel = null;

	private JLabel edgeInformationLabel = null;

	private GraphEditorPane graphPane;

	private JPanel jPanel6 = null;

	private JSlider jSlider = null;

	private JLabel distanceLabel = null;

	public RandomImporterDialog(Frame parent, GraphEditorPane graphPane) {
		this.parent = parent;
		parent.setEnabled(false);
		this.graphPane = graphPane;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		initialize();
		setSize(400, 300);
		// pack();
	}

	/**
	 * this.setTitle("Random Imort Dialog"); This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("Import random graph dialog");
		this.setContentPane(getJPanel());

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
			jPanel.add(getJPanel5(), BorderLayout.NORTH);
			jPanel.add(getJPanel2(), BorderLayout.SOUTH);
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
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.Y_AXIS));
			jPanel1.add(getJPanel3(), null);
			jPanel1.add(getJPanel4(), null);
			jPanel1.add(getJPanel6(), null);
			jPanel1.add(Box.createGlue());
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(getJPanel2(), BoxLayout.X_AXIS));
			jPanel2.add(Box.createGlue());
			jPanel2.add(getJButton(), null);
			jPanel2.add(getJButton1(), null);
		}
		return jPanel2;
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
					dispose();
					parent.setEnabled(true);
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
			jButton1.setText("Import");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final RandomImporter imp = new RandomImporter(graphPane);
					parent.setEnabled(true);
					new CancelAbleProcessDialog(
							parent,
							new CancelAbleProcess() {

								public void setStatusNotifier(StatusNotifier n) {
									imp.setStatusNotifier(n);

								}

								public void run() {

									try {
										imp.importGraph((Integer) vertexSpinner
												.getValue(),
												(Integer) edgeSpinner
														.getValue(), jSlider
														.getValue());
									} catch (InvalidParameters e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}

							},
							"Loading the the random graph. This can take long time if the graph contains much information.");

					setVisible(false);
					dispose();

				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jLabel = new JLabel();
			jLabel.setText("(max = 1000)");
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BoxLayout(getJPanel3(), BoxLayout.X_AXIS));
			jPanel3.setBorder(BorderFactory.createTitledBorder(null,
					"Number of vertices:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel3.add(getVertexSpinner(), null);
			jPanel3.add(Box.createGlue());
			jPanel3.add(jLabel, null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			edgeInformationLabel = new JLabel();
			edgeInformationLabel.setText("(max = 45)");
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BoxLayout(getJPanel4(), BoxLayout.X_AXIS));
			jPanel4.setBorder(BorderFactory.createTitledBorder(null,
					"Number of edges:", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel4.add(getEdgeSpinner(), null);
			jPanel4.add(Box.createGlue());
			jPanel4.add(edgeInformationLabel, null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes vertexSpinner
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getVertexSpinner() {
		if (vertexSpinner == null) {
			final int min = 0;
			final int max = 1000;
			SpinnerModel model = new SpinnerNumberModel(10, // initial value
					min, // min
					max, // max
					1); // step
			vertexSpinner = new JSpinner(model);
			vertexSpinner.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					SpinnerModel model = vertexSpinner.getModel();
					if (model instanceof SpinnerNumberModel) {
						SpinnerNumberModel numberModel = (SpinnerNumberModel) model;
						int value = (Integer) numberModel.getValue();

						
						if ((min <= value) && (value <= max)) {
							int newMaxEdgeValue = RandomImporter
									.getAllowedEdgesFor(value);
							int defaultEdge;
							if ((Integer) edgeSpinner.getValue() < newMaxEdgeValue)
								defaultEdge = (Integer) edgeSpinner.getValue();
							else
								defaultEdge = newMaxEdgeValue;
							SpinnerModel edgeModel = new SpinnerNumberModel(
									defaultEdge, // initial value
									0, // min
									newMaxEdgeValue, // max
									1);
							edgeSpinner.setModel(edgeModel);
							edgeInformationLabel.setText("(max = "
									+ newMaxEdgeValue + ")");

						} else {
							model.setValue(0);
						}
					}

				}

			});
		}
		return vertexSpinner;
	}

	/**
	 * This method initializes edgeSpinner
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getEdgeSpinner() {
		if (edgeSpinner == null) {
			SpinnerModel model = new SpinnerNumberModel(45, // initial value
					0, // min
					45, // max
					1); // step
			edgeSpinner = new JSpinner(model);

		}
		return edgeSpinner;
	}

	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new BorderLayout());
			jPanel5.add(getJPanel1(), BorderLayout.NORTH);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			distanceLabel = new JLabel();
			distanceLabel.setText(" 45 pixels");
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BoxLayout(getJPanel6(), BoxLayout.X_AXIS));
			jPanel6.setBorder(BorderFactory.createTitledBorder(null,
					"Approximate distance between vertices in pixels",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel6.add(getJSlider(), null);
			jPanel6.add(distanceLabel, null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			final int minDistance = 20;
			final int maxDistance = 1000;
			final int initDistance = 50;
			jSlider = new JSlider(JSlider.HORIZONTAL, minDistance, maxDistance,
					initDistance);
			jSlider.setMajorTickSpacing(100);
			jSlider.setMinorTickSpacing(50);
			jSlider.setPaintTicks(true);
			jSlider.setPaintLabels(true);
			jSlider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					if (jSlider.getValue() < 100)
						distanceLabel.setText(" " + jSlider.getValue()
								+ " pixels");
					else
						distanceLabel.setText(jSlider.getValue() + " pixels");

				}

			});
		}
		return jSlider;
	}
}
