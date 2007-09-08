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
package org.grapheditor.editor.vertexrenders;

import java.awt.Graphics;

/**
 * 
 * This interface is implemented by the vertex render components to show that
 * they have a method to simple draw themself. This method is used by the
 * EPS-file exporter due to the fact that the EPSGraphics class that is used by
 * it does not work with the default draw operation becouse it does not support
 * some drawing operations.
 * 
 * @author kjellw
 */
public interface SimpleDrawAble {

	public void simpleDraw(Graphics g);
}
