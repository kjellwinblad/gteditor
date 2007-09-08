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
package org.grapheditor.editor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/** 
 * This class is a keyhandeler that is added to the graph component
 * GraphEditorPane to add some key operations.
 * 
 * These key operations are: * All selected nodes are deleted when bckspace or
 * delete is pressed. * The insert mode off/on is toggled when you press the
 * space key. * The insert mode off/on is changed as long the a button is
 * pressed.
 * 
 * @author kjellw
 * 
 */
public class KeyHandler extends KeyAdapter {

	private GraphEditorPane graphPane;

	private Timer switchModeTimer = new Timer();

	/**
	 * @param graphPane
	 */
	public KeyHandler(GraphEditorPane graphPane) {
		super();
		this.graphPane = graphPane;
	}

	// Information if the user has started to press the fast switch mode key
	private static boolean startPress = false;

	/**
	 * Is called when a key is pressed in the asociated graph pane
	 * 
	 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_DELETE
				|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			/*
			 * If it is the delete key the selected objects in the graph shall
			 * be removed.
			 */
			graphPane.removeSelected();
		} else if (e.getKeyCode() == KeyEvent.VK_A) {

			if (startPress) {
				// Starts a timer that will change mode if not key is pressed
				// again. This is to determine if the key is down.
				switchModeTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						graphPane.setInsertMode(!graphPane.isInsertMode());
						// The user has stoped to press
						startPress = false;
					}

				}, 200);

			}

		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			graphPane.setInsertMode(!graphPane.isInsertMode());
		}

		// if(startPress){
		// graphPane.setInsertMode(!graphPane.isInsertMode());
		// //The user has stoped to press
		// startPress=false;
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_A) {
			switchModeTimer.cancel();
			switchModeTimer = new Timer();
			if (!startPress) {
				graphPane.setInsertMode(!graphPane.isInsertMode());
				startPress = true;
			}

		}

		// if(e.isAltGraphDown() || (e.isControlDown() &&
		// (e.isAltDown()||e.isMetaDown()))){
		//		
		// if(!startPress){
		// graphPane.setInsertMode(!graphPane.isInsertMode());
		// startPress = true;
		// }
		//			
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
