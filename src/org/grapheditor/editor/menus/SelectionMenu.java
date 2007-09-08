/**
 * 
 */
package org.grapheditor.editor.menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.grapheditor.editor.GraphEditorPane;

import java.awt.event.KeyEvent;

/**
 * This menu is used for som selection commands. For example to select all
 * neighbour vertices to vselected vertices.
 * 
 * @author kjellw
 * 
 */
public class SelectionMenu extends JMenu {

	private JMenuItem selectAllSelectionMenuItem = null;

	private JMenuItem selectNeighbourSelectionMenuItem = null;

	private JMenuItem selectOtherSelectionMenuItem = null;

	private GraphEditorPane graphPane;

	private JMenuItem jMenuItem = null;

	/**
	 * This method initializes
	 * 
	 */
	public SelectionMenu(GraphEditorPane pane) {
		super();
		graphPane = pane;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("Selection");
		this.setMnemonic(KeyEvent.VK_S);
		this.add(getSelectAllSelectionMenuItem());
		this.add(getSelectNeighbourSelectionMenuItem());
		this.add(getJMenuItem());
		this.add(getSelectOtherSelectionMenuItem());

	}

	/**
	 * This method initializes selectAllSelectionMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSelectAllSelectionMenuItem() {
		if (selectAllSelectionMenuItem == null) {
			selectAllSelectionMenuItem = new JMenuItem();
			selectAllSelectionMenuItem.setText("Select all");
			selectAllSelectionMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							graphPane.selectAll();
						}
					});
		}
		return selectAllSelectionMenuItem;
	}

	/**
	 * This method initializes selectNeighbourSelectionMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSelectNeighbourSelectionMenuItem() {
		if (selectNeighbourSelectionMenuItem == null) {
			selectNeighbourSelectionMenuItem = new JMenuItem();
			selectNeighbourSelectionMenuItem.setText("Select neighbour ");
			selectNeighbourSelectionMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							graphPane.selectNeighbour();
						}
					});
		}
		return selectNeighbourSelectionMenuItem;
	}

	/**
	 * This method initializes selectOtherSelectionMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSelectOtherSelectionMenuItem() {
		if (selectOtherSelectionMenuItem == null) {
			selectOtherSelectionMenuItem = new JMenuItem();
			selectOtherSelectionMenuItem.setText("Select complement");
			selectOtherSelectionMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							graphPane.selectOther();
						}
					});
		}
		return selectOtherSelectionMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Select reachable");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphPane.selectReachable();
				}
			});
		}
		return jMenuItem;
	}

	public void menuSelectionChanged(boolean value) {
		super.menuSelectionChanged(value);
		if (value) {
			Object[] selectedVertices = graphPane.getSelectionCells(graphPane
					.getGraphLayoutCache().getCells(false, true, false, false));

			boolean verticesSelected = !((selectedVertices == null) || (selectedVertices.length == 0));
			getSelectNeighbourSelectionMenuItem().setEnabled(verticesSelected);
			getJMenuItem().setEnabled(verticesSelected);
			getSelectOtherSelectionMenuItem().setEnabled(verticesSelected);
		}
	}

}
