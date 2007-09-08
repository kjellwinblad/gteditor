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
package org.grapheditor.editor.exporters;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.importers.MathematicaImporter;
import org.grapheditor.editor.importers.MathematicaImporter.InvalidFile;
import org.grapheditor.gui.CancelAbleProcess;
import org.grapheditor.gui.CancelAbleProcessDialog;
import org.grapheditor.gui.StatusNotifier;

/**
 * This class purpose is to make it easy to show a dialog that lets the user
 * deside a eps-file to export the an eps-image to.
 * 
 * @author kjellw
 * 
 */
public class EPSExporterVisual {

	public static void doMathematicaExport(final JFrame parent,
			final GraphEditorPane editorPane) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select file to export to...");
		int option = fileChooser.showSaveDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION) {
			final EPSExporter epsExp = new EPSExporter();
			new CancelAbleProcessDialog(
					parent,
					new CancelAbleProcess() {

						public void setStatusNotifier(StatusNotifier n) {
							epsExp.setStatusNotifier(n);

						}

						public void run() {
							try {
								
								epsExp.export(fileChooser.getSelectedFile(),
										editorPane);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(parent,
										"Error while exporting graph, because of the following reason:\n"
												+ e.getMessage(),
										"Unable to open file",
										JOptionPane.ERROR_MESSAGE);
							}

						}

					},
					"Exports information to eps file. This can take long time depending on the graph size");

		}
	}
}
