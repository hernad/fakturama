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

package com.sebulli.fakturama;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.actions.OpenBrowserEditorAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.preferences.PreferencesInDatabase;

/**
 * The earlyStartup Member is called after the Fakturama application is started.
 * 
 * @author Gerd Bartelt
 */
public class Startup implements IStartup {

	/**
	 * called after startup of the application
	 */
	@Override
	public void earlyStartup() {

		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				// If the data base is connected and if it is not new, then some preferences are loaded
				// from the data base.
				if (!Data.INSTANCE.getNewDBCreated())
					if (DataBaseConnectionState.INSTANCE.isConnected())
						PreferencesInDatabase.loadPreferencesFromDatabase();
				
				// Opens the web browser editor.
				if (window != null) {
					OpenBrowserEditorAction action = new OpenBrowserEditorAction(false);
					action.run();
				}
				
				// Check, if this is a newer Software, so do some update
				Updater updater = new Updater();
				updater.checkVersion();

				
			}
		});
	}

}
