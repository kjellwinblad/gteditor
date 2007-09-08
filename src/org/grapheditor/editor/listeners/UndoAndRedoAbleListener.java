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
package org.grapheditor.editor.listeners;

/**
 * This listener listens for changes of the status of the redo and undo manager.
 * It can be added to a GraphEditorPane.
 * 
 * @author kjellw
 * 
 */
public interface UndoAndRedoAbleListener {

	/**
	 * This is set by the undo redo manger when its not posible to undo anymore
	 */
	void canNotUndo();

	/**
	 * This is set by the undo redo manger when its posible to undo again
	 */
	void canUndo();

	void canNotRedo();

	void canRedo();

}
