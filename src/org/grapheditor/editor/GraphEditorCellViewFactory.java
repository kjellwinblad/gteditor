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
package org.grapheditor.editor;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

/**
 * This class extends DefaultCellViewFactory in JGraph and overrides the method
 * createVertexView to return a cutom vertex view.
 * 
 * @author kjellw
 * 
 */
public class GraphEditorCellViewFactory extends DefaultCellViewFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.DefaultCellViewFactory#createVertexView(java.lang.Object)
	 */
	@Override
	protected VertexView createVertexView(Object cell) {
		// TODO Auto-generated method stub
		return new GraphEditorVertexView(cell);
	}

}
