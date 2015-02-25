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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.IHandlerService;

import com.sebulli.fakturama.ApplicationWorkbenchWindowAdvisor;
import com.sebulli.fakturama.backup.BackupManager;

/**
 * This action opens the calculator in a view.
 * 
 * @author Gerd Bartelt
 */
public class InstallAction extends Action {

	/**
	 * Constructor
	 */
	public InstallAction() {

		//T: Text of the action to open the calculator
		super(_("Install New Software"));

		//T: Tool Tip Text
		setToolTipText(_("Install new features") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_P2_INSTALL);

		// Associate the action with a predefined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_P2_INSTALL);

		// sets a default 16x16 pixel icon.
		// setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/...png"));
	}

	/**
	 * Run the action
	 * 
	 * Install new software
	 */
	@Override
	public void run() {

		// Create a backup
		BackupManager.createBackup();

		IWorkbenchWindow workbenchWindow = ApplicationWorkbenchWindowAdvisor.getActiveWorkbenchWindow();;
		IHandlerService handlerService = (IHandlerService) workbenchWindow.getService(IHandlerService.class);
		try {
			handlerService.executeCommand("org.eclipselabs.p2.rcpupdate.install", null);
		} catch (Exception ex) {
			throw new RuntimeException("org.eclipselabs.p2.rcpupdate.install");
		}

	}
}
