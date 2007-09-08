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
package org.grapheditor.editor.importers;

import java.awt.Frame;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.importers.MathematicaImporter.InvalidFile;
import org.grapheditor.gui.CancelAbleProcess;
import org.grapheditor.gui.CancelAbleProcessDialog;
import org.grapheditor.gui.StatusNotifier;

/**
 * A class with a static method that displays a dialog that let the user deside
 * file to save a mathematica importable file to.
 * 
 * @author kjellw
 * 
 */
public class MathematicaImorterVisual {

	public static void doMathematicaImport(final JFrame parent,
			GraphEditorPane editorPane) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select Mathematica file to import...");
		int option = fileChooser.showOpenDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION) {
			final MathematicaImporter matImp = new MathematicaImporter(
					editorPane);
			new CancelAbleProcessDialog(
					parent,
					new CancelAbleProcess() {

						public void setStatusNotifier(StatusNotifier n) {
							matImp.setStatusNotifier(n);

						}

						public void run() {
							try {
								matImp.importMathematicaFile(fileChooser
										.getSelectedFile(), 100.0);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(parent,
										"Error while opening file, because of the following reason:\n"
												+ e.getMessage(),
										"Unable to open file",
										JOptionPane.ERROR_MESSAGE);
							} catch (InvalidFile e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(parent,
										"Error while opening file, because of the following reason:\n"
												+ e.getMessage(),
										"Unable to open file",
										JOptionPane.ERROR_MESSAGE);
							}

						}

					},
					"Imorts information from Mathematica file. This can take long time depending on the file size");

		}
	}
}
