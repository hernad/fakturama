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

import com.sebulli.fakturama.Workspace;

/**
 * This action opens a dialog to select the workspace.
 * 
 * @author Gerd Bartelt
 */
public class SelectWorkspaceAction extends Action {

	//T: Text of the action to select the workspace
	public final static String ACTIONTEXT = _("Select Workspace"); 

	/**
	 * Constructor
	 */
	public SelectWorkspaceAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Select a new Workspace") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_SELECT_WORKSPACE);

		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_SELECT_WORKSPACE);
	}

	/**
	 * Run the action
	 * 
	 * Open a dialog to select a new workspace. If a valid folder is selected, a
	 * request is set. The new workspace is used, after the application has been
	 * restarted.
	 */
	@Override
	public void run() {

		// Select a new workspace
		Workspace.INSTANCE.selectWorkspace();
	}
}
