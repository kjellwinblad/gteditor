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

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a message provider. It is possible to set message and
 * message color to it and listeners that listens for messages that is set to
 * the provider.
 * 
 * @author kjellw
 * 
 */
public class MessageProvider {
	private String message = null;

	private boolean useSpecialColor = false;

	private Color specialColor = Color.gray;

	List<MessageListener> messageListeners = new LinkedList<MessageListener>();

	public MessageProvider() {

	}

	public MessageProvider(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		if (message == null)
			return "";
		else
			return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
		for (MessageListener l : messageListeners) {
			l.messageChanged(message);
		}
	}

	public void addMessageListener(MessageListener l) {
		messageListeners.add(l);
		l.messageChanged(getMessage());
		if (isUseSpecialColor())
			l.colorChanged(specialColor);
	}

	public void removeMessageListener(MessageListener l) {
		messageListeners.remove(l);

	}

	/**
	 * @return the specialColor
	 */
	public Color getSpecialColor() {
		return specialColor;
	}

	/**
	 * @param specialColor
	 *            the specialColor to set
	 */
	public void setSpecialColor(Color specialColor) {
		this.useSpecialColor = true;
		this.specialColor = specialColor;
		for (MessageListener l : messageListeners) {
			l.colorChanged(this.specialColor);
		}

	}

	/**
	 * @return the useSpecialColor
	 */
	public boolean isUseSpecialColor() {
		return useSpecialColor;
	}

	/**
	 * @param useSpecialColor
	 *            the useSpecialColor to set
	 */
	public void setUseSpecialColor(boolean useSpecialColor) {
		this.useSpecialColor = useSpecialColor;
	}

}
