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
package org.grapheditor.gui.menus;

import javax.imageio.ImageIO;
import javax.swing.JMenu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.exporters.EPSExporterVisual;
import org.grapheditor.editor.exporters.MathematicaExporterVisual;
import org.grapheditor.editor.exporters.NeighbourListExporter;
import org.grapheditor.editor.exporters.NeighbourListExporterVisual;
import org.grapheditor.editor.graph.EdgeInformation;
import org.grapheditor.editor.graph.GraphLoader;
import org.grapheditor.editor.graph.GraphSaver;
import org.grapheditor.editor.graph.VertexInformation;
import org.grapheditor.editor.graph.GraphLoader.IncorcectGraphXMLFileException;
import org.grapheditor.editor.importers.MathematicaImorterVisual;
import org.grapheditor.editor.importers.MathematicaImporter;
import org.grapheditor.editor.importers.RandomImporterDialog;
import org.grapheditor.editor.listeners.GraphChangeListener;
import org.grapheditor.editor.listeners.GraphPropertiesChangeListener;
import org.grapheditor.editor.listeners.GraphSaveListener;
import org.grapheditor.editor.listeners.UndoAndRedoAbleListener;
import org.grapheditor.gui.MainWindow;
import org.grapheditor.properties.GlobalProperties;
import org.grapheditor.properties.GraphProperties;
import org.grapheditor.properties.LatestFilesPropertyChangeListener;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.GmlExporter;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.graph.AsUndirectedGraph;
import org.xml.sax.SAXException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.TransformerConfigurationException;

/**
 * This is the file menu that is in the MainMenu. It has otions for file
 * operations like open, save, export, import and new etc.
 * 
 * @author kjellw
 * 
 */
public class FileMenu extends JMenu {

	private JMenuItem newFileMenuItem = null;

	private JSeparator jSeparator = null;

	private JMenuItem openFileMenuItem = null;

	private JMenu openLatestFileMenu = null;

	private JSeparator jSeparator1 = null;

	private JMenuItem saveFileMenuItem = null;

	private JMenuItem saveAsFileMenuItem = null;

	private JSeparator jSeparator2 = null;

	private JMenu exportToFileMenu = null;

	private JMenu importFromFileMenu = null;

	private JSeparator jSeparator3 = null;

	private JMenuItem exitFileMenuItem = null;

	private GraphEditorPane graphPane;

	private JMenuItem dotFileExportMenuItem = null;

	private JFileChooser exporterFileChooser = null;

	private JFileChooser saveAsFileChooser = null; // @jve:decl-index=0:visual-constraint="156,421"

	private JFileChooser openFileChooser = null; // @jve:decl-index=0:visual-constraint="27,175"

	// Actions
	private ActionListener newAction;

	private ActionListener openAction;

	private ActionListener saveAction;

	GlobalProperties globalProperties = GlobalProperties.getInstance(); // @jve:decl-index=0:

	/*
	 * Indicate if the current graph is associated with a file
	 */
	private boolean currentGraphAssociateWithFile = false;

	/*
	 * The file that the current graph is asociated with
	 */
	private File currentGraphsFile; // @jve:decl-index=0:

	private JMenuItem graphMLExportMenuItem = null;

	private JMenuItem gmlExportMenuItem = null;

	private JMenuItem JPEGExportMenuItem = null;

	private JMenuItem PNGExportMenuItem = null;

	private JFileChooser jpegExportFileChooser = null; // @jve:decl-index=0:visual-constraint="243,113"

	private JFileChooser pngExportFileChooser = null; // @jve:decl-index=0:visual-constraint="121,96"

	private MainWindow mainWindow;

	private JMenuItem randomImportMenuItem;

	private JMenuItem mathematicaImportMenuItem;

	private JMenuItem mathematicaExportMenuItem;

	private JMenuItem epsExportMenuItem;

	private JMenuItem neighborListExportMenuItem;

	public FileMenu() {
		super();
		initialize();

	}

	/**
	 * This method initializes
	 * 
	 */
	public FileMenu(GraphEditorPane pane, MainWindow mainWindow) {
		super();
		initialize();
		this.mainWindow = mainWindow;
		graphPane = pane;

		updateOpenLatestMenu();
		globalProperties
				.addLatestFilesPropertyChangeListener(new LatestFilesPropertyChangeListener() {

					public void newFileOpened(File file) {
						updateOpenLatestMenu();

					}

				});
		MainGraphPropertiesChangeListener l = new MainGraphPropertiesChangeListener();
		l.graphPropertiesChanged();
		graphPane.addGraphPropertiesChangeListener(l);

	}

	private void updateOpenLatestMenu() {

		getOpenLatestFileMenu().removeAll();
		addLatestFilesMenuItems();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setText("File");
		this.setMnemonic(KeyEvent.VK_F);
		this.add(getNewFileMenuItem());
		this.add(getJSeparator());
		this.add(getOpenFileMenuItem());
		this.add(getOpenLatestFileMenu());
		this.add(getJSeparator1());
		this.add(getSaveFileMenuItem());
		this.add(getSaveAsFileMenuItem());
		this.add(getJSeparator2());
		this.add(getExportToFileMenu());
		this.add(getImportFromFileMenu());
		this.add(getJSeparator3());
		this.add(getExitFileMenuItem());

	}

	/**
	 * This method initializes newFileMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNewFileMenuItem() {
		if (newFileMenuItem == null) {
			newFileMenuItem = new JMenuItem();
			newFileMenuItem.setText("New");
			newFileMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/filenew.png")));
			newAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// If new is called a new main window is created
					MainWindow newWindow = new MainWindow();
					newWindow.setVisible(true);
				}
			};
			newFileMenuItem.addActionListener(newAction);
		}
		return newFileMenuItem;
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
	 * This method initializes openFileMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOpenFileMenuItem() {
		if (openFileMenuItem == null) {
			openFileMenuItem = new JMenuItem();
			openFileMenuItem.setText("Open...");
			openFileMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/fileopen.png")));
			openAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					open();

				}
			};
			openFileMenuItem.addActionListener(openAction);
		}
		return openFileMenuItem;
	}

	/**
	 * Open a file chooser to choose a file to open
	 * 
	 */
	private void open() {
		/*
		 * Open a save file chooser so the user can choose a file name to save
		 * to
		 */

		int returnVal = getOpenFileChooser().showOpenDialog(getRootPane());
		File selectedFile = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = getOpenFileChooser().getSelectedFile();

			if (graphPane.isChanged()) {
				MainWindow.openWindowWithFile(selectedFile);
				return;
			}
			openFile(selectedFile);
		}

	}

	private void openFile(File file) {
		try {
			FileReader reader = new FileReader(file);
			GraphLoader loader = new GraphLoader(graphPane);
			loader.load(reader, file);
			reader.close();
			currentGraphAssociateWithFile = true;
			currentGraphsFile = file;
			globalProperties.addFileToLatestFiles(file);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IncorcectGraphXMLFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes openLatestFileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getOpenLatestFileMenu() {
		if (openLatestFileMenu == null) {
			openLatestFileMenu = new JMenu();
			openLatestFileMenu.setText("Open latest...");
			openLatestFileMenu.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/fileopen.png")));

		}

		return openLatestFileMenu;
	}

	private void addLatestFilesMenuItems() {
		List<File> fileList = globalProperties.getLatestFiles();

		for (File fileProp : fileList) {
			final File file = fileProp;
			JMenuItem fileItem = new JMenuItem();
			fileItem.setText(file.getName());
			fileItem.setToolTipText(file.getAbsolutePath());
			fileItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (graphPane.isChanged()) {
						MainWindow.openWindowWithFile(file);
					} else {
						openFile(file);
					}

				}

			});
			openLatestFileMenu.add(fileItem);
		}
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
	 * This method initializes saveFileMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveFileMenuItem() {
		if (saveFileMenuItem == null) {
			saveFileMenuItem = new JMenuItem();
			saveFileMenuItem.setText("Save");
			saveFileMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/filesave.png")));
			saveFileMenuItem.setEnabled(false);
			saveAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					save();
				}
			};
			saveFileMenuItem.addActionListener(saveAction);
		}
		return saveFileMenuItem;
	}

	/**
	 * This method initializes saveAsFileMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveAsFileMenuItem() {
		if (saveAsFileMenuItem == null) {
			saveAsFileMenuItem = new JMenuItem();
			saveAsFileMenuItem.setText("Save as...");
			saveAsFileMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/filesaveas.png")));
			saveAsFileMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							saveAs();
						}

					});
		}
		return saveAsFileMenuItem;
	}

	public void save() {
		if (currentGraphAssociateWithFile) {
			saveToFile(currentGraphsFile);
		} else {
			saveAs();
		}
	}

	private void saveAs() {

		int returnVal = getSaveAsFileChooser().showSaveDialog(getRootPane());
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File selectedFile = getSaveAsFileChooser().getSelectedFile();

			if (!selectedFile.getName().matches("(.*)(\\.ged)")) {

				if (selectedFile.getName().matches("\".*\"")) {
					if (selectedFile.getName().length() < 3) {
						JOptionPane
								.showMessageDialog(
										getRootPane(),
										"Can not save to file "
												+ selectedFile
														.getAbsolutePath()
												+ "\nBecause of the following reason:\n"
												+ "File name is too short.",
										"Unable to save file",
										JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						selectedFile = new File(selectedFile.getParent()
								+ File.separator
								+ selectedFile.getName()
										.substring(
												1,
												(int) (selectedFile.getName()
														.length() - 2)));
					}
				} else {
					selectedFile = new File(selectedFile.getAbsoluteFile()
							+ ".ged");
				}
			}
			globalProperties.addFileToLatestFiles(selectedFile);
			saveToFile(selectedFile);
		}
	}

	/**
	 * Tries to save to the specified file
	 */
	private void saveToFile(File file) {
		GraphSaver saver = new GraphSaver(graphPane);
		try {
			FileWriter writer = new FileWriter(file);
			saver.write(writer);
			writer.close();
			currentGraphsFile = file;
			currentGraphAssociateWithFile = true;
			graphPane.getGraphProperties().setSavedAs(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

			JOptionPane.showMessageDialog(getRootPane(),
					"Can not save to file " + file.getAbsolutePath()
							+ "\nBecause of the following reason:\n"
							+ e1.getMessage(), "Unable to save file",
					JOptionPane.ERROR_MESSAGE);
		}
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
	 * This method initializes exportToFileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getExportToFileMenu() {
		if (exportToFileMenu == null) {
			exportToFileMenu = new JMenu();
			exportToFileMenu.setText("Export to...");
			exportToFileMenu.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/fileexport.png")));
			exportToFileMenu.add(getDotFileExportMenuItem());
			exportToFileMenu.add(getGraphMLExportMenuItem());
			exportToFileMenu.add(getGmlExportMenuItem());
			exportToFileMenu.add(getJPEGExportMenuItem());
			exportToFileMenu.add(getPNGExportMenuItem());
			exportToFileMenu.add(getMathematicaExportMenuItem());
			exportToFileMenu.add(getNeighbourListExportMenuItem());
			exportToFileMenu.add(getEPSExportMenuItem());
		}
		return exportToFileMenu;
	}

	private JMenuItem getNeighbourListExportMenuItem() {
		if (neighborListExportMenuItem == null) {
			neighborListExportMenuItem = new JMenuItem();
			neighborListExportMenuItem.setText("Neighbour list...");
			neighborListExportMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					NeighbourListExporterVisual.doNeighborListExport(
							mainWindow, graphPane);

				}

			});
		}
		return neighborListExportMenuItem;
	}

	private JMenuItem getEPSExportMenuItem() {
		if (epsExportMenuItem == null) {
			epsExportMenuItem = new JMenuItem();
			epsExportMenuItem.setText("EPS (Encapsulated PostScript)...");
			epsExportMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					EPSExporterVisual
							.doMathematicaExport(mainWindow, graphPane);

				}

			});
		}
		return epsExportMenuItem;
	}

	private JMenuItem getMathematicaExportMenuItem() {
		if (mathematicaExportMenuItem == null) {
			mathematicaExportMenuItem = new JMenuItem();
			mathematicaExportMenuItem.setText("Mathematica...");
			mathematicaExportMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MathematicaExporterVisual.doMathematicaExport(mainWindow,
							graphPane);

				}

			});
		}
		return mathematicaExportMenuItem;
	}

	/**
	 * This method initializes importFromFileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getImportFromFileMenu() {
		if (importFromFileMenu == null) {
			importFromFileMenu = new JMenu();
			importFromFileMenu.setText("Import from...");
			importFromFileMenu.add(getRandomImportMenuItem());
			importFromFileMenu.add(getMathematicaImportMenuItem());
		}
		return importFromFileMenu;
	}

	private JMenuItem getMathematicaImportMenuItem() {
		if (mathematicaImportMenuItem == null) {
			mathematicaImportMenuItem = new JMenuItem();
			mathematicaImportMenuItem.setText("Mathematica...");
			mathematicaImportMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MathematicaImorterVisual.doMathematicaImport(mainWindow,
							graphPane);

				}

			});
		}
		return mathematicaImportMenuItem;
	}

	private JMenuItem getRandomImportMenuItem() {
		if (randomImportMenuItem == null) {
			randomImportMenuItem = new JMenuItem();
			randomImportMenuItem.setText("Random import...");
			randomImportMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new RandomImporterDialog(mainWindow, graphPane)
							.setVisible(true);

				}

			});
		}
		return randomImportMenuItem;
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
	 * This method initializes exitFileMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitFileMenuItem() {
		if (exitFileMenuItem == null) {
			exitFileMenuItem = new JMenuItem();
			exitFileMenuItem.setText("Exit");
			exitFileMenuItem.setIcon(new ImageIcon(getClass().getResource(
					"/org/grapheditor/gui/icons/16x16/fileclose.png")));
			exitFileMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					mainWindow.exitWindow();
				}

			});
		}
		return exitFileMenuItem;
	}

	/**
	 * This method initializes dotFileExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getDotFileExportMenuItem() {
		if (dotFileExportMenuItem == null) {
			dotFileExportMenuItem = new JMenuItem();
			dotFileExportMenuItem.setText("DOT file...");
			dotFileExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							/*
							 * Open a save file chooser so the user can choose a
							 * file name to save to
							 */
							int returnVal = getExporterFileChooser()
									.showSaveDialog(getRootPane());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File selectedFile = getExporterFileChooser()
										.getSelectedFile();
								DOTExporter<VertexInformation, EdgeInformation> exporter = new DOTExporter<VertexInformation, EdgeInformation>();
								try {
									FileWriter writer = new FileWriter(
											selectedFile);
									UndirectedGraph<VertexInformation, EdgeInformation> ug = new AsUndirectedGraph<VertexInformation, EdgeInformation>(graphPane
											.getJGraphTGraph());
									
									exporter.export(writer, ug);
									writer.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					});
		}
		return dotFileExportMenuItem;
	}

	/**
	 * This method initializes exporterFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getExporterFileChooser() {
		if (exporterFileChooser == null) {
			exporterFileChooser = new JFileChooser();
			exporterFileChooser.setDialogTitle("Choose file to export to");
		}
		return exporterFileChooser;
	}

	/**
	 * This method initializes saveAsFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getSaveAsFileChooser() {
		if (saveAsFileChooser == null) {
			saveAsFileChooser = new JFileChooser();
			saveAsFileChooser.setDialogTitle("Save as...");
			saveAsFileChooser.setAcceptAllFileFilterUsed(true);
			saveAsFileChooser.addChoosableFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().matches(".*.ged");
				}

				@Override
				public String getDescription() {

					return "Graph files (*.ged)";
				}

			});
		}
		return saveAsFileChooser;
	}

	/**
	 * This method initializes openFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getOpenFileChooser() {
		if (openFileChooser == null) {
			openFileChooser = new JFileChooser();
			openFileChooser.setDialogTitle("Open file");

			openFileChooser.addChoosableFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().matches(".*\\.ged");
				}

				@Override
				public String getDescription() {

					return "Graph files (*.ged)";
				}

			});
		}
		return openFileChooser;
	}

	/**
	 * @param currentGraphsFile
	 *            the currentGraphsFile to set
	 */
	public void setCurrentGraphsFile(File currentGraphsFile) {
		if (currentGraphsFile != null)
			this.currentGraphAssociateWithFile = true;
		this.currentGraphsFile = currentGraphsFile;
	}

	/**
	 * This method initializes graphMLExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getGraphMLExportMenuItem() {
		if (graphMLExportMenuItem == null) {
			graphMLExportMenuItem = new JMenuItem();
			graphMLExportMenuItem.setText("GraphML file...");
			graphMLExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							/*
							 * Open a save file chooser so the user can choose a
							 * file name to save to
							 */
							int returnVal = getExporterFileChooser()
									.showSaveDialog(getRootPane());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File selectedFile = getExporterFileChooser()
										.getSelectedFile();
								GraphMLExporter<VertexInformation, EdgeInformation> exporter = new GraphMLExporter<VertexInformation, EdgeInformation>();
								try {
									FileWriter writer = new FileWriter(
											selectedFile);
									UndirectedGraph<VertexInformation, EdgeInformation> ug =
										new AsUndirectedGraph<VertexInformation, EdgeInformation>(graphPane.getJGraphTGraph());
									
									exporter.export(writer, ug);
									writer.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (TransformerConfigurationException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (SAXException e3) {
									// TODO Auto-generated catch block
									e3.printStackTrace();
								}
							}
						}
					});
		}
		return graphMLExportMenuItem;
	}

	/**
	 * This method initializes gmlExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getGmlExportMenuItem() {
		if (gmlExportMenuItem == null) {
			gmlExportMenuItem = new JMenuItem();
			gmlExportMenuItem.setText("GML (Graph Modelling Language) file...");
			gmlExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							/*
							 * Open a save file chooser so the user can choose a
							 * file name to save to
							 */
							int returnVal = getExporterFileChooser()
									.showSaveDialog(getRootPane());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File selectedFile = getExporterFileChooser()
										.getSelectedFile();
								GmlExporter<VertexInformation, EdgeInformation> exporter = new GmlExporter<VertexInformation, EdgeInformation>();
								try {
									FileWriter writer = new FileWriter(
											selectedFile);
									exporter
											.export(
													writer,
													new AsUndirectedGraph<VertexInformation, EdgeInformation>(
															graphPane
																	.getJGraphTGraph()));
									writer.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					});
		}
		return gmlExportMenuItem;
	}

	/**
	 * This method initializes JPEGExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJPEGExportMenuItem() {
		if (JPEGExportMenuItem == null) {
			JPEGExportMenuItem = new JMenuItem();
			JPEGExportMenuItem.setText("JPEG image file...");
			JPEGExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							/*
							 * Open a save file chooser so the user can choose a
							 * file name to save to
							 */
							int returnVal = getJpegExportFileChooser()
									.showSaveDialog(getRootPane());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File selectedFile = getJpegExportFileChooser()
										.getSelectedFile();
								if (!selectedFile
										.getName()
										.matches(
												"(.*)((\\.jpg)|(\\.jpeg)|(\\.JPG)|(\\.JPEG))")) {

									if (selectedFile.getName()
											.matches("\".*\"")) {
										if (selectedFile.getName().length() < 3) {
											JOptionPane
													.showMessageDialog(
															getRootPane(),
															"Can not save to file "
																	+ selectedFile
																			.getAbsolutePath()
																	+ "\nBecause of the following reason:\n"
																	+ "File name is too short.",
															"Unable to save file",
															JOptionPane.ERROR_MESSAGE);
											return;
										} else {
											selectedFile = new File(
													selectedFile.getParent()
															+ File.separator
															+ selectedFile
																	.getName()
																	.substring(
																			1,
																			(int) (selectedFile
																					.getName()
																					.length() - 2)));
										}
									} else {
										selectedFile = new File(selectedFile
												.getAbsoluteFile()
												+ ".jpg");
									}
								}
								Object[] selectionCells = graphPane.getSelectionCells();
								graphPane.setInsertMode(false);
								graphPane.clearSelection();
								BufferedImage img = graphPane.getImage(
										graphPane.getBackground(), 5);
								graphPane.setInsertMode(true);
								graphPane.setSelectionCells(selectionCells);
								try {
									OutputStream out = new FileOutputStream(
											selectedFile);
									ImageIO.write(img, "jpg", out);
									out.flush();
									out.close();
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
							}
						}
					});
		}
		return JPEGExportMenuItem;
	}

	/**
	 * This method initializes PNGExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPNGExportMenuItem() {
		if (PNGExportMenuItem == null) {
			PNGExportMenuItem = new JMenuItem();
			PNGExportMenuItem.setText("PNG image file");
			PNGExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							/*
							 * Open a save file chooser so the user can choose a
							 * file name to save to
							 */
							int returnVal = getPngExportFileChooser()
									.showSaveDialog(getRootPane());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File selectedFile = getPngExportFileChooser()
										.getSelectedFile();
								if (!selectedFile.getName().matches(
										"(.*)((\\.png)|(\\.PNG))")) {

									if (selectedFile.getName()
											.matches("\".*\"")) {
										if (selectedFile.getName().length() < 3) {
											JOptionPane
													.showMessageDialog(
															getRootPane(),
															"Can not save to file "
																	+ selectedFile
																			.getAbsolutePath()
																	+ "\nBecause of the following reason:\n"
																	+ "File name is too short.",
															"Unable to save file",
															JOptionPane.ERROR_MESSAGE);
											return;
										} else {
											selectedFile = new File(
													selectedFile.getParent()
															+ File.separator
															+ selectedFile
																	.getName()
																	.substring(
																			1,
																			(int) (selectedFile
																					.getName()
																					.length() - 2)));
										}
									} else {
										selectedFile = new File(selectedFile
												.getAbsoluteFile()
												+ ".png");
									}
								}

								Object[] selectionCells = graphPane.getSelectionCells();
								graphPane.setInsertMode(false);
								graphPane.clearSelection();
								BufferedImage img = graphPane.getImage(
										graphPane.getBackground(), 5);
								graphPane.setInsertMode(true);
								graphPane.setSelectionCells(selectionCells);

								try {
									OutputStream out = new FileOutputStream(
											selectedFile);
									ImageIO.write(img, "png", out);
									out.flush();
									out.close();
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
							}
						}
					});
		}
		return PNGExportMenuItem;
	}

	/**
	 * This method initializes jpegExportFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getJpegExportFileChooser() {
		if (jpegExportFileChooser == null) {
			jpegExportFileChooser = new JFileChooser();
			jpegExportFileChooser
					.setDialogTitle("Export to JPEG image file...");

			jpegExportFileChooser.addChoosableFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().matches(
							"(.*\\.jpeg)|(.*\\.jpg)|(.*\\.JPEG)|(.*\\.JPG)");
				}

				@Override
				public String getDescription() {

					return "JPEG image files (*.jpg, *.JPG, *.jpeg, *.JPEG)";
				}

			});
		}
		return jpegExportFileChooser;
	}

	/**
	 * This method initializes pngExportFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getPngExportFileChooser() {
		if (pngExportFileChooser == null) {
			pngExportFileChooser = new JFileChooser();
			pngExportFileChooser.setDialogTitle("Export to JPEG image file...");

			pngExportFileChooser.addChoosableFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().matches("(.*\\.png)|(.*\\.PNG)");
				}

				@Override
				public String getDescription() {

					return "PNG image files (*.png, *.PNG)";
				}

			});
		}
		return pngExportFileChooser;
	}

	private class MainGraphPropertiesChangeListener implements
			GraphPropertiesChangeListener {

		/**
		 * This shall happen when the properties of a graph is changed
		 */
		public void graphPropertiesChanged() {
			final GraphProperties prop = graphPane.getGraphProperties();

			prop.addGraphChangeListener(new GraphChangeListener() {

				public void graphChanged() {
					getSaveFileMenuItem().setEnabled(true);
				}

			});

			prop.addSaveListener(new GraphSaveListener() {

				public void graphSaved() {
					getSaveFileMenuItem().setEnabled(false);
				}

			});
		}
	}

	/**
	 * @return the newAction
	 */
	public ActionListener getNewAction() {
		return newAction;
	}

	/**
	 * @return the openAction
	 */
	public ActionListener getOpenAction() {
		return openAction;
	}

	/**
	 * @return the saveAction
	 */
	public ActionListener getSaveAction() {
		return saveAction;
	}

}
