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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.grapheditor.editor.ExpandDialog;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.SplitSelectedEdgesDialog;
import org.grapheditor.gui.GraphStatisticsDialog;
import org.jgraph.plugins.layouts.LayoutDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;

/**
 * This menu shows menu items to perform all avable operations on the graph.
 * This is a sumenu to all menus that is displayed when the user clicks the
 * right button in the graph editor pane.
 * 
 * @author kjellw
 * 
 */
public class OperationsMenu extends JMenu {

	private JMenuItem completeBipartiteGraphMenuItem = null;

	private GraphEditorPane graphPane;

	private JMenuItem jMenuItem = null;

	private JMenuItem jMenuItem1 = null;

	private JMenuItem jMenuItem2 = null;

	private JSeparator jSeparator1 = null;

	private JMenuItem jMenuItem3 = null;

	private JMenuItem jMenuItem4 = null;

	private JMenuItem jMenuItem5 = null;

	private JMenuItem jMenuItem6 = null;

	private JMenuItem jMenuItem7 = null;

	private JSeparator jSeparator3 = null;

	private JSeparator jSeparator = null;

	private JSeparator jSeparator2 = null;

	private JMenuItem jMenuItem8 = null;

	private JSeparator jSeparator4;

	private JMenuItem splitSelectedEdgesMenu;

	private int lastSelectedVertexOperationPos;

	private JMenuItem jMenuItem9 = null;

	/**
	 * This method initializes
	 * 
	 */
	public OperationsMenu(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Operations");
		int count = 0;
		this.setMnemonic(KeyEvent.VK_O);
		this.add(getCompleteBipartiteGraphMenuItem());
		this.add(getJMenuItem());
		this.add(getJMenuItem1());
		this.add(getJSeparator3());
		this.add(getJMenuItem2());
		this.add(getJSeparator1());
		this.add(getJMenuItem6());
		this.add(getJMenuItem5());
		this.add(getJMenuItem4());
		lastSelectedVertexOperationPos = this.getMenuComponentCount();
		this.add(getJMenuItem7());
		this.add(getJMenuItem9());
		this.add(getJSeparator2());
		this.add(getSplitSelectedEdgesMenu());
		this.add(getJSeparator());
		this.add(getJMenuItem8());
		this.add(getJSeparator4());
		this.add(getJMenuItem3());

	}

	private JMenuItem getSplitSelectedEdgesMenu() {
		if (splitSelectedEdgesMenu == null) {
			splitSelectedEdgesMenu = new JMenuItem("Subdivide selected edges...");
			splitSelectedEdgesMenu
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (graphPane.getSelectionCells(graphPane
									.getGraphLayoutCache().getCells(false,
											false, false, true)).length > 0)
								SplitSelectedEdgesDialog.show(graphPane);

						}
					});
		}
		return splitSelectedEdgesMenu;
	}

	private JSeparator getJSeparator4() {
		if (jSeparator4 == null) {
			jSeparator4 = new JSeparator();
		}
		return jSeparator4;
	}

	/**
	 * This method initializes completeBipartiteGraphMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCompleteBipartiteGraphMenuItem() {
		if (completeBipartiteGraphMenuItem == null) {
			completeBipartiteGraphMenuItem = new JMenuItem(
					"Complete bipartite graph");
			completeBipartiteGraphMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (graphPane.isReadyForOperation()) {
								graphPane.doCompleteBipartiteGraphOperation();
							} else {
								JOptionPane
										.showMessageDialog(
												getRootPane(),
												"Graph pane not ready for operation.\nAn operation is pending.",
												"Can not perform operation",
												JOptionPane.INFORMATION_MESSAGE);
							}
						}
					});
		}
		return completeBipartiteGraphMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem("Graph Cartesian Product");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (graphPane.isReadyForOperation()) {
						graphPane.doGraphCartesianProductOperation();
					} else {
						JOptionPane
								.showMessageDialog(
										getRootPane(),
										"Graph pane not ready for operation.\nAn operation is pending.",
										"Can not perform operation",
										JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jMenuItem1
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem("Graph Catergorial Product");
			jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (graphPane.isReadyForOperation()) {
						graphPane.doGraphCatergorialProductOperation();
					} else {
						JOptionPane
								.showMessageDialog(
										getRootPane(),
										"Graph pane not ready for operation.\nAn operation is pending.",
										"Can not perform operation",
										JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			jMenuItem2 = new JMenuItem();
			jMenuItem2.setText("Make selected subgraph complete");
			jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (graphPane.isReadyForOperation()) {
						graphPane.makeSubgraphCompleteOperation();
					} else {
						JOptionPane
								.showMessageDialog(
										getRootPane(),
										"Graph pane not ready for operation.\nAn operation is pending.",
										"Can not perform operation",
										JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return jMenuItem2;
	}

	/**
	 * This method initializes jSeparator1
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator1() {
		if (jSeparator1 == null) {
			jSeparator1 = new JSeparator();
		}
		return jSeparator1;
	}

	/**
	 * This method initializes jMenuItem3
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			jMenuItem3 = new JMenuItem();
			jMenuItem3.setText("Calculate graph statistics");
			jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					GraphStatisticsDialog.show(graphPane);
				}
			});
		}
		return jMenuItem3;
	}

	/**
	 * This method initializes jMenuItem4
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem4() {
		if (jMenuItem4 == null) {
			jMenuItem4 = new JMenuItem();
			jMenuItem4.setText("Mirror selected vertices horizontal ");
			jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.mirrorSelectedVerticesHorizontal();
				}
			});
		}
		return jMenuItem4;
	}

	/**
	 * This method initializes jMenuItem5
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem5() {
		if (jMenuItem5 == null) {
			jMenuItem5 = new JMenuItem();
			jMenuItem5.setText("Mirror selected vertices vertical");
			jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.mirrorSelectedVerticesVertical();
				}
			});
		}
		return jMenuItem5;
	}

	/**
	 * This method initializes jMenuItem6
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem6() {
		if (jMenuItem6 == null) {
			jMenuItem6 = new JMenuItem();
			jMenuItem6.setText("Place selected vertices in circle");
			jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.placeSelectedVerticesInCircle();
				}
			});
		}
		return jMenuItem6;
	}

	/**
	 * This method initializes jMenuItem7
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem7() {
		if (jMenuItem7 == null) {
			jMenuItem7 = new JMenuItem();
			jMenuItem7.setText("Rotate selected vertices...");
			jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.rotateSelectedVertices();
				}
			});
		}
		return jMenuItem7;
	}

	/**
	 * This method initializes jSeparator3
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator3() {
		if (jSeparator3 == null) {
			jSeparator3 = new JSeparator();
		}
		return jSeparator3;
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
	 * This method initializes jSeparator2
	 * 
	 * @return javax.swing.JSeparator
	 */
	private JSeparator getJSeparator2() {
		if (jSeparator2 == null) {
			jSeparator2 = new JSeparator();
		}
		return jSeparator2;
	}

	/**
	 * This method initializes jMenuItem8
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem8() {
		if (jMenuItem8 == null) {
			jMenuItem8 = new JMenuItem();
			jMenuItem8.setText("Apply graph loyout algorithm...");
			jMenuItem8.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new LayoutDialog(graphPane.getMainFrame(), graphPane)
							.setVisible(true);

				}

			});
		}
		return jMenuItem8;
	}

	public void setVisible(boolean show) {
		super.setVisible(show);

	}

	public void menuSelectionChanged(boolean value) {
		super.menuSelectionChanged(value);
		if (value) {
			Object[] selectedVertices = graphPane.getSelectionCells(graphPane
					.getGraphLayoutCache().getCells(false, true, false, false));
			Object[] selectedEdges = graphPane.getSelectionCells(graphPane
					.getGraphLayoutCache().getCells(false, false, false, true));

			boolean verticesSelected = !((selectedVertices == null) || (selectedVertices.length == 0));

			getJMenuItem7().setEnabled(verticesSelected);
			getJMenuItem6().setEnabled(verticesSelected);
			getJMenuItem5().setEnabled(verticesSelected);
			getJMenuItem4().setEnabled(verticesSelected);
			getJMenuItem2().setEnabled(verticesSelected);
			getJMenuItem9().setEnabled(verticesSelected);
			boolean edgesSelected = !((selectedEdges == null) || (selectedEdges.length == 0));
			splitSelectedEdgesMenu.setEnabled(edgesSelected);

		}
	}

	/**
	 * @return the lastSelectedVertexOperationPos
	 */
	public int getLastSelectedVertexOperationPos() {
		return lastSelectedVertexOperationPos;
	}

	/**
	 * @param lastSelectedVertexOperationPos
	 *            the lastSelectedVertexOperationPos to set
	 */
	public void setLastSelectedVertexOperationPos(
			int lastSelectedVertexOperationPos) {
		this.lastSelectedVertexOperationPos = lastSelectedVertexOperationPos;
	}

	/**
	 * This method initializes jMenuItem9	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem9() {
		if (jMenuItem9 == null) {
			jMenuItem9 = new JMenuItem();
			jMenuItem9.setText("Expand selected by factor...");
			jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ExpandDialog.showExpandDialog(graphPane);
				}
			});
		}
		return jMenuItem9;
	}

}
