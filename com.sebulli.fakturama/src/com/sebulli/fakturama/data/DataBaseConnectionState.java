/* 
 * Fakturama - Free Invoicing Software - http://fakturama.sebulli.com
 * 
 * Copyright (C) 2012 Gerd Bartelt
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Bartelt - initial API and implementation
 */

package com.sebulli.fakturama.data;

/**
 * Thsi singleton stores the state of the connection to the data base in a local
 * variable. A singleton enum is used to read the state, without accessing to
 * the Data class.
 * 
 * @author Gerd Bartelt
 */
public enum DataBaseConnectionState {
	INSTANCE;

	private boolean connected = false;

	/**
	 * Test whether the data base is connected
	 * 
	 * @return True, if the data base is connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Set the state of the connection to the data base to "connected"
	 */
	public void setConnected() {
		connected = true;
	}

}
