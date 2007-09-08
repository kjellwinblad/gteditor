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

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.help.HelpBroker;
import javax.help.HelpSet;

import org.grapheditor.editor.GraphEditorPane;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.BaseException;

/**
 * This implements the design pattern singelton. It contains global properties
 * and settings for the program. It has methods that outomatically saves and
 * loads the properties till and from a properties file.
 * 
 * @author kjellw
 * 
 */
public class GlobalProperties {

	public static final String PROGRAM_NAME = "Graph Editor";

	public static final String PROPERTIES_DIR = ".grapheditor";

	public static final String HELPSET_DESTINATION = "GraphEditor.hs";

	public static final double SCALE_INTERVALL = 0.2;

	private static GlobalProperties instance = null;

	private int numberOfLatestFilesSaved = 6;

	private LinkedList<File> latestFiles;

	private LinkedList<Boolean> displayQuickMenuList = new LinkedList<Boolean>();

	private transient List<ChangeDisplayQuickMenuListListener> changeDisplayQuickMenuListListeners = new LinkedList<ChangeDisplayQuickMenuListListener>();

	private boolean antialiasing = true;

	private boolean doubleBuffering = true;
	
	private boolean gridEnabled = false;
	
	private int gridMode = GraphEditorPane.CROSS_GRID_MODE;
	
	private double gridSize = 25.0;
	
	private boolean gridVisable = true;
	
	private Color gridColor = Color.GRAY;

	private String UITheme = null;

	private transient List<LatestFilesPropertyChangeListener> latestFilesListeners = new LinkedList<LatestFilesPropertyChangeListener>();

	private transient HelpSet helpSet;

	private transient HelpBroker helpBroker;

	/**
	 * @return the helpBroker
	 */
	public HelpBroker getHelpBroker() {
		return helpBroker;
	}

	private GlobalProperties() {
		latestFiles = new LinkedList<File>();
		createHelpSet(HELPSET_DESTINATION);
	}

	public static GlobalProperties getInstance() {

		if (instance == null) {
			if (!createInstanceFromFile())
				instance = new GlobalProperties();
		}

		return instance;
	}

	private static boolean createInstanceFromFile() {

		XStream xstream = new XStream();

		File propertiesFile = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator
				+ "properties.xml");

		if (!propertiesFile.exists()) {
			// There is no properties file.
			return false;
		}

		try {
			FileReader reader = new FileReader(propertiesFile);

			instance = (GlobalProperties) xstream.fromXML(reader);
			reader.close();
		} catch (Exception e) {
			System.err
					.println("Not possible to read properties file, because of the following reason:"
							+ e.getMessage());
			return false;
		}

		instance.initializeNotSerializeFeelds();

		return true;

	}

	private void initializeNotSerializeFeelds() {
		latestFilesListeners = new LinkedList<LatestFilesPropertyChangeListener>();
		changeDisplayQuickMenuListListeners = new LinkedList<ChangeDisplayQuickMenuListListener>();

		List<File> latestFilesTemp = new LinkedList<File>();
		for (File f : latestFiles) {
			latestFilesTemp.add(f);
		}

		createHelpSet(HELPSET_DESTINATION);

	}

	/**
	 * Create a help set to use as help information in this application
	 * 
	 * The code is influenced from the demo code that follows with the Jave Help
	 * system distribution.
	 * 
	 */
	private void createHelpSet(String helpSetName) {

		ClassLoader loader = this.getClass().getClassLoader();
		URL url;
		try {
			url = HelpSet.findHelpSet(loader, helpSetName);

			HelpSet helpSet = new HelpSet(loader, url);
			helpBroker = helpSet.createHelpBroker();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void addLatestFilesPropertyChangeListener(
			LatestFilesPropertyChangeListener l) {
		latestFilesListeners.add(l);

	}

	public void addChangeDisplayQuickMenuListListener(
			ChangeDisplayQuickMenuListListener l) {
		changeDisplayQuickMenuListListeners.add(l);

	}

	/**
	 * Returns a copy of he list with latest files
	 * 
	 * @return the latestFiles
	 */
	public List<File> getLatestFiles() {

		return latestFiles;
	}

	/**
	 * @param latestFiles
	 *            the latestFiles to set
	 */
	public void addFileToLatestFiles(File file) {

		if (latestFiles.size() >= getNumberOfLatestFilesSaved()) {
			long size = latestFiles.size();

			for (long n = getNumberOfLatestFilesSaved() - 1; n < size; n++) {
				// latestFiles.remove(n);
				latestFiles.removeLast();
			}
		}

		latestFiles.addFirst(file);
		// Fire acion

		saveToPropertiesFile();

		for (LatestFilesPropertyChangeListener l : latestFilesListeners)
			l.newFileOpened(file);
	}

	/**
	 * @return the numberOfLatestFilesSaved
	 */
	public int getNumberOfLatestFilesSaved() {
		return numberOfLatestFilesSaved;
	}

	/**
	 * @param numberOfLatestFilesSaved
	 *            the numberOfLatestFilesSaved to set
	 */
	public void setNumberOfLatestFilesSaved(int numberOfLatestFilesSaved) {
		this.numberOfLatestFilesSaved = numberOfLatestFilesSaved;
	}

	public boolean saveToPropertiesFile() {

		XStream xstream = new XStream();
		File propertiesDir = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator);
		File propertiesFile = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator
				+ "properties.xml");
		if (!propertiesDir.exists()) {
			if (!propertiesDir.mkdir())
				return false;
		}

		if (!propertiesFile.exists()) {
			// There is no properties file.
			try {
				propertiesFile.createNewFile();
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
				return false;
			}
		}

		try {
			FileWriter writer = new FileWriter(propertiesFile);

			xstream.toXML(getInstance(), writer);
			writer.close();
		} catch (Exception e) {
			System.err
					.println("Not possible to write properties file, because of the followin reason:"
							+ e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * @return the helpSet
	 */
	public HelpSet getHelpSet() {
		return helpSet;
	}

	/**
	 * @return the displayQuickMenuList
	 */
	public LinkedList<Boolean> getDisplayQuickMenuList() {
		return displayQuickMenuList;
	}

	/**
	 * @param displayQuickMenuList
	 *            the displayQuickMenuList to set
	 */
	public void setDisplayQuickMenuList(LinkedList<Boolean> displayQuickMenuList) {
		this.displayQuickMenuList = displayQuickMenuList;
		for (ChangeDisplayQuickMenuListListener l : changeDisplayQuickMenuListListeners) {
			l.quickMenuListChanged();
		}
		saveToPropertiesFile();
	}

	/**
	 * @return the antialiasing
	 */
	public boolean isAntialiasing() {
		return antialiasing;
	}

	/**
	 * @param antialiasing
	 *            the antialiasing to set
	 */
	public void setAntialiasing(boolean antialiasing) {
		this.antialiasing = antialiasing;
		saveToPropertiesFile();
	}

	/**
	 * @return the doubleBuffering
	 */
	public boolean isDoubleBuffering() {
		return doubleBuffering;
	}

	/**
	 * @param doubleBuffering
	 *            the doubleBuffering to set
	 */
	public void setDoubleBuffering(boolean doubleBuffering) {
		this.doubleBuffering = doubleBuffering;
		saveToPropertiesFile();
	}

	/**
	 * @return the uITheme
	 */
	public String getUITheme() {
		return UITheme;
	}

	/**
	 * @param theme
	 *            the uITheme to set
	 */
	public void setUITheme(String theme) {
		UITheme = theme;
		saveToPropertiesFile();
	}

	/**
	 * @return the gridColor
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * @param gridColor the gridColor to set
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
		saveToPropertiesFile();
	}

	/**
	 * @return the gridEnabled
	 */
	public boolean isGridEnabled() {
		return gridEnabled;
	}

	/**
	 * @param gridEnabled the gridEnabled to set
	 */
	public void setGridEnabled(boolean gridEnabled) {
		this.gridEnabled = gridEnabled;
		saveToPropertiesFile();
	}

	/**
	 * @return the gridMode
	 */
	public int getGridMode() {
		return gridMode;
	}

	/**
	 * @param gridMode the gridMode to set
	 */
	public void setGridMode(int gridMode) {
		this.gridMode = gridMode;
		saveToPropertiesFile();
	}

	/**
	 * @return the gridSize
	 */
	public double getGridSize() {
		return gridSize;
	}

	/**
	 * @param gridSize the gridSize to set
	 */
	public void setGridSize(double gridSize) {
		this.gridSize = gridSize;
		saveToPropertiesFile();
	}

	/**
	 * @return the gridVisable
	 */
	public boolean isGridVisable() {
		return gridVisable;
	}

	/**
	 * @param gridVisable the gridVisable to set
	 */
	public void setGridVisable(boolean gridVisable) {
		this.gridVisable = gridVisable;
		saveToPropertiesFile();
	}

}
