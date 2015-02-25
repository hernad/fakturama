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

package com.sebulli.fakturama.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all the expand bars
 * 
 * @author Gerd Bartelt
 */
public class ExpandBarManager {

	// List with all expand bars
	List<ExpandBar> expandBars = new ArrayList<ExpandBar>();

	/**
	 * Constructor Clears the list.
	 */
	public ExpandBarManager() {
		expandBars.clear();
	}

	/**
	 * Add a new expand bar
	 * 
	 * @param expandBar
	 *            A new expand bar
	 */
	public void addExpandBar(ExpandBar expandBar) {
		expandBars.add(expandBar);
	}

	/**
	 * Collapse the other expand bars
	 * 
	 * @param expandBar
	 *            Do not collapse this expand bar
	 */
	public void collapseOthers(ExpandBar expandBar) {

		for (ExpandBar nextExpandBar : expandBars) {
			if (nextExpandBar != expandBar)
				nextExpandBar.collapse(true);
		}
	}

}
