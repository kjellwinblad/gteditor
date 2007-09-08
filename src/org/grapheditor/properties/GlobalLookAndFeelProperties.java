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

import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.grapheditor.editor.GraphEditorPane;

/**
 * This is a property change module which implements the PropertiesModule
 * interface. It is used to change the settings for the global look and feel of
 * the program.
 * 
 * @author kjellw
 * 
 */
public class GlobalLookAndFeelProperties extends JPanel implements
		PropertiesModule {

	private static final long serialVersionUID = 1L;

	private JLabel jLabel = null;

	private GraphEditorPane graphPane;

	private ButtonGroup lookAndFeelGroup;

	protected boolean lookAndFeelChanged;

	protected String lookAndFeelClassName;

	/**
	 * This is the default constructor
	 */
	public GlobalLookAndFeelProperties(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#getPropertiesDescription()
	 */
	public String getPropertiesDescription() {
		// TODO Auto-generated method stub
		return "Select the look and feel for the program";
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
		return "Look and feel";
	}

	public String toString() {
		return getPropertiesName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.grapheditor.editor.properties.PropertiesModule#save()
	 */
	public void save() {
		if (lookAndFeelChanged) {
			GlobalProperties.getInstance().setUITheme(lookAndFeelClassName);
		}

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabel = new JLabel();
		jLabel
				.setText("<html>Select the <i>look and feel</i> you want to use:<br>(You have to restart the program for the change to take effect.)</html>");
		this.setSize(300, 200);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(jLabel, null);
		addLookAndFellChooses();
	}

	private void addLookAndFellChooses() {

		lookAndFeelGroup = new ButtonGroup();

		// Create the radio buttons.

		final LookAndFeelInfo[] lookAndFeels = UIManager
				.getInstalledLookAndFeels();

		String currentLookAndFeel = null;
		LookAndFeel current = UIManager.getLookAndFeel();

		if (current == null)
			currentLookAndFeel = UIManager.getSystemLookAndFeelClassName();
		else
			currentLookAndFeel = current.getClass().getCanonicalName();

		for (int n = 0; n < lookAndFeels.length; n++) {
			final int iter = n;
			JRadioButton lookAndFeelItem = new JRadioButton(lookAndFeels[n]
					.getName());
			add(lookAndFeelItem);
			// ThemeChangeListener l = new ThemeChangeListener(lookAndFeels[n]
			// .getClassName(), graphPane.getMainFrame());
			// lookAndFeelItem.addActionListener(l);
			lookAndFeelItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					lookAndFeelChanged = true;
					lookAndFeelClassName = lookAndFeels[iter].getClassName();

				}

			});

			if (lookAndFeels[n].getClassName().equals(currentLookAndFeel)) {
				lookAndFeelItem.setSelected(true);
			}

			lookAndFeelGroup.add(lookAndFeelItem);

		}

	}

}
