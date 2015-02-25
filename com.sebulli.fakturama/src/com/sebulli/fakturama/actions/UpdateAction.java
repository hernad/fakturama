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

package com.sebulli.fakturama.actions;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.action.Action;

import com.sebulli.fakturama.backup.BackupManager;

/**
 * This action opens the calculator in a view.
 * 
 * @author Gerd Bartelt
 */
public class UpdateAction extends Action {

	//T: Text of the action to check for updates
	public final static String ACTIONTEXT = _("Check for Updates"); 

	/**
	 * Constructor
	 */
	public UpdateAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Check for Updates") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_P2_UPDATE);

		// Associate the action with a predefined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_P2_UPDATE);

		// sets a default 16x16 pixel icon.
		// setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/...png"));
	}

	/**
	 * Run the action
	 * 
	 * Check for new updates
	 */
	@Override
	public void run() {

		// Create a backup
		BackupManager.createBackup();

		// Check for updates
       // P2Util.checkForUpdates();
	}
}
