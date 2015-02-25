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

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.logger.Logger;

/**
 * Shows a view
 * 
 * @author Gerd Bartelt
 */
public class ViewManager {

	/**
	 * Shows a new view
	 * 
	 * @param viewId
	 *            ID of the view
	 */
	public static void showView(String viewId) {
		// Show a new view
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
		}
		catch (PartInitException e) {
			Logger.logError(e, "Error showing view " + viewId);
		}

	}

}
