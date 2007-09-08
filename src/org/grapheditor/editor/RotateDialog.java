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
package org.grapheditor.editor;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JRootPane;

import org.grapheditor.editor.GraphEditorPane;
import org.jgraph.graph.DefaultGraphCell;

import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;

/**
 * This is a dialog to rotate vertices in a graphPane. It is used by the rotate
 * selected vertices operation.
 * 
 * @author kjellw
 */
public class RotateDialog extends JDialog {

	private GraphEditorPane graphPane;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private Frame parent;

	private JPanel jPanel2 = null;

	private JSlider jSlider = null;

	private JSpinner jSpinner = null;

	private JPanel jPanel3 = null;

	private Object[] selectedVertices;

	private Map<Object, Map> orginalBoundsMap;

	private Rectangle2D rotateBounds;

	// private double centerX;
	// private double centerY;
	private Rectangle2D sampleBounds;

	private HashMap<Object, Object> currentChangeMap;

	public RotateDialog(final GraphEditorPane graphPane,
			Object[] selectedVertices) {
		super(graphPane.getMainFrame());
		this.parent = graphPane.getMainFrame();
		this.selectedVertices = selectedVertices;
		this.graphPane = graphPane;
		createOrginalBoundsMap(selectedVertices);
		parent.setEnabled(false);
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				setVisible(false);
				graphPane.ignoreUndoableOnNextEdit();
				graphPane.getGraphLayoutCache().edit(orginalBoundsMap);
				parent.setEnabled(true);
				dispose();
			}

		});
		initialize();
		pack();
	}

	private void createOrginalBoundsMap(Object[] selectedVertices2) {
		Map<Object, Map> nestadMap = new HashMap<Object, Map>();
		Rectangle2D prevBounds = null;
		for (Object selectedVertex : selectedVertices) {
			if (selectedVertex instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) selectedVertex;
				prevBounds = GraphEditorConstants.getBounds(cell
						.getAttributes());
				Map<Object, Object> attrMap = new HashMap<Object, Object>();
				GraphEditorConstants.setBounds(attrMap, prevBounds);
				nestadMap.put(cell, attrMap);
			}

		}
		this.rotateBounds = graphPane.getCellBounds(selectedVertices2);

		this.sampleBounds = prevBounds;

		this.orginalBoundsMap = nestadMap;

	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("Rotate");
		this.setSize(new Dimension(270, 100));
		this.setContentPane(getJPanel());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				parent.setEnabled(true);
			}

		});

	}

	public static void show(GraphEditorPane graphPane, Object[] selectedVertices) {
		new RotateDialog(graphPane, selectedVertices).setVisible(true);
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
			jPanel.add(getJPanel2(), BorderLayout.CENTER);
			jPanel.add(getJPanel1(), BorderLayout.SOUTH);
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
					graphPane.ignoreUndoableOnNextEdit();
					graphPane.getGraphLayoutCache().edit(orginalBoundsMap);
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
					setVisible(false);
					graphPane.ignoreUndoableOnNextEdit();
					graphPane.getGraphLayoutCache().edit(orginalBoundsMap);
					graphPane.getGraphLayoutCache().edit(currentChangeMap);
					parent.setEnabled(true);
					dispose();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(getJPanel2(), BoxLayout.Y_AXIS));
			jPanel2.setBorder(BorderFactory.createTitledBorder(null,
					"Select rotation angle:",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanel2.add(getJSlider(), null);
			jPanel2.add(getJPanel3(), null);
			jPanel2.add(Box.createGlue());
		}
		return jPanel2;
	}

	/**
	 * This method initializes jSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setMinimum(-360);
			jSlider.setMinorTickSpacing(0);
			jSlider.setMaximum(360);
			jSlider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					jSpinner.setValue((jSlider.getValue() == 0 ? 0 : jSlider
							.getValue()));
					rotationChanged((jSlider.getValue() == 0 ? 0 : jSlider
							.getValue()));
				}

			});
		}
		return jSlider;
	}

	private void rotationChanged(double angle) {
		// Place the circle within the bounds of the cellse

		double radAngle = (angle / 360) * (2 * Math.PI);
		double centerX = rotateBounds.getCenterX();// -
		// sampleBounds.getWidth();
		double centerY = rotateBounds.getCenterY();// -
		// sampleBounds.getHeight();

		currentChangeMap = new HashMap<Object, Object>();

		for (Object selectedVertex : selectedVertices) {
			if (selectedVertex instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) selectedVertex;
				Rectangle2D prevBoundes = GraphEditorConstants
						.getBounds(orginalBoundsMap.get(cell));
				double prevX = prevBoundes.getX() + prevBoundes.getWidth() / 2
						- centerX;
				double prevY = prevBoundes.getY() + prevBoundes.getHeight() / 2
						- centerY;
				double initAngle = Math.atan2(prevY, prevX);
				double radius = Math.sqrt(prevX * prevX + prevY * prevY);
				Map<Object, Object> attrMap = new HashMap<Object, Object>();
				GraphEditorConstants.setBounds(attrMap, new Rectangle2D.Double(
						Math.cos(radAngle + initAngle) * (radius) + centerX
								- prevBoundes.getWidth() / 2, Math.sin(radAngle
								+ initAngle)
								* (radius)
								+ centerY
								- prevBoundes.getHeight()
								/ 2, prevBoundes.getWidth(), prevBoundes
								.getHeight()));
				currentChangeMap.put(cell, attrMap);
			}

		}
		graphPane.ignoreUndoableOnNextEdit();
		graphPane.getGraphLayoutCache().edit(currentChangeMap);

	}

	/**
	 * This method initializes jSpinner
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getJSpinner() {
		if (jSpinner == null) {
			jSpinner = new JSpinner();
			SpinnerNumberModel model = new SpinnerNumberModel(0, -360, 360, 1);
			jSpinner.setModel(model);
			jSpinner.setPreferredSize(new Dimension(70, 20));
			jSpinner.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {

					if (jSpinner.getValue() instanceof Integer) {
						double value = (Integer) jSpinner.getValue();
						long roundValue = Math.round(value);
						int useValue = new Long(roundValue).intValue();

						jSlider.setValue(useValue);
					}

				}

			});

		}
		return jSpinner;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new FlowLayout());
			jPanel3.add(getJSpinner(), null);
		}
		return jPanel3;
	}

}
