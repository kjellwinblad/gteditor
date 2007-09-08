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
package org.grapheditor.gui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.InformationPane;
import org.grapheditor.editor.MessageListener;
import org.grapheditor.editor.MessageProvider;
import org.grapheditor.editor.listeners.InsertModeChangeListener;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This panel implements information pane so it can get information from a
 * GraphEditoPane. It has one status field that shows informaiton from the
 * associated GraphEditorPane.
 * 
 * @author kjellw
 * 
 */
public class StatusBarPanel extends JPanel implements InformationPane {

	private JLabel insertModeStatusLabel = null;

	private Component box = null;

	private JLabel jLabel1 = null;

	private GraphEditorPane graphPane;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private Stack<MessageProvider> messageQueue = new Stack<MessageProvider>(); // @jve:decl-index=0:

	/**
	 * This method initializes
	 * 
	 */
	public StatusBarPanel() {
		super();
		initialize();
		this.graphPane = null;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		jLabel1 = new JLabel();
		jLabel1.setText("");
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.add(getJPanel(), null);
		this.add(getBox(), null);
		this.add(getJPanel1(), null);

	}

	/**
	 * This method initializes insertModeStatusLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getInsertModeStatusLabel() {
		if (insertModeStatusLabel == null) {
			insertModeStatusLabel = new JLabel();
			insertModeStatusLabel.setText("JLabel");
		}
		return insertModeStatusLabel;
	}

	/**
	 * This method initializes box
	 * 
	 * @return javax.swing.Box
	 */
	private Component getBox() {
		if (box == null) {
			box = Box.createGlue();
		}
		return box;
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
			jPanel.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.LOWERED));
			jPanel.add(jLabel1, BorderLayout.CENTER);
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
					.createBevelBorder(BevelBorder.LOWERED));
			jPanel1.add(getInsertModeStatusLabel(), null);
		}
		return jPanel1;
	}

	public void popMessage() {
		try {
			messageQueue.pop();
		} catch (EmptyStackException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					jLabel1.setText("");
					Color color = UIManager.getColor("Panel.background");
					if (color == null)
						color = Color.LIGHT_GRAY;
					jPanel.setBackground(color);
				}
			});
			return;
		}
		if (messageQueue.size() == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					jLabel1.setText("");
					Color color = UIManager.getColor("Panel.background");
					if (color == null)
						color = Color.LIGHT_GRAY;
					jPanel.setBackground(color);
				}
			});
			return;
		}

		final MessageProvider message = messageQueue.peek();
		message.addMessageListener(messageListener);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel1.setText(message.getMessage());

				if (!message.isUseSpecialColor()) {
					Color color = UIManager.getColor("Panel.background");
					if (color == null)
						color = Color.LIGHT_GRAY;
					jPanel.setBackground(color);
				} else {

					jPanel.setBackground(message.getSpecialColor());

				}
			}
		});

	}

	private class StatusBarMessageListener implements MessageListener {
		public void messageChanged(final String message) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					jLabel1.setText(message);
				}
			});

		}

		public void colorChanged(Color specialColor) {

			jPanel.setBackground(specialColor);

		}
	}

	StatusBarMessageListener messageListener = new StatusBarMessageListener(); // @jve:decl-index=0:

	public void putMessage(MessageProvider messageProvider) {
		try {
			messageQueue.peek().removeMessageListener(messageListener);
		} catch (EmptyStackException e) {

		}
		messageQueue.push(messageProvider);
		messageProvider.addMessageListener(messageListener);
		if (!messageProvider.isUseSpecialColor()) {
			Color color = UIManager.getColor("Panel.background");
			if (color == null)
				color = Color.LIGHT_GRAY;
			jPanel.setBackground(color);
		} else {

			jPanel.setBackground(messageProvider.getSpecialColor());

		}
	}

	/**
	 * @param graphPane
	 *            the graphPane to set
	 */
	public void setGraphPane(GraphEditorPane graphPane) {
		this.graphPane = graphPane;
		InsertModeChangeListener l = new InsertModeChangeListener() {

			public void newInsertModeEvent(boolean insertMode) {
				if (insertMode) {
					insertModeStatusLabel.setText("Insert mode: ON ");
				} else {
					insertModeStatusLabel.setText("Insert mode: OFF");
				}

			}

		};
		graphPane.addInsertModeChangeListener(l);
		l.newInsertModeEvent(graphPane.isInsertMode());
	}

}
