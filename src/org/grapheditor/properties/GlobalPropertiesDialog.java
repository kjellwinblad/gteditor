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

import org.grapheditor.editor.GraphEditorPane;

/**
 * A dialog that extends PropertiesDialog and is used for global settings.
 * 
 * @author kjellw
 * 
 */
public class GlobalPropertiesDialog extends PropertiesDialog {

	public GlobalPropertiesDialog(Frame parent, GraphEditorPane graphPane) {
		super(parent, graphPane);
		setTitle("Global Properties");
		addPropertiesModule(new GlobalGraphDrawingProperties(graphPane));
		addPropertiesModule(new GlobalLookAndFeelProperties(graphPane));
		
		addPropertiesModule(new GlobalGraphGridProperties(graphPane));
		setSize(630, 400);
		
	}

}
