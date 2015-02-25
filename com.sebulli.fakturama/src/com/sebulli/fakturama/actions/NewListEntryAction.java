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

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.editors.ListEditor;
import com.sebulli.fakturama.editors.UniDataSetEditorInput;
import com.sebulli.fakturama.logger.Logger;

/**
 * This action creates a new list entry in an editor.
 * 
 * @author Gerd Bartelt
 */
public class NewListEntryAction extends NewEditorAction {

	//T: Text of the action to create a new list entry
	public final static String ACTIONTEXT = _("New List Entry"); 

	/**
	 * Constructor
	 */
	public NewListEntryAction() {
		
		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Create a new list entry") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_NEW_LISTENTRY);

		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_NEW_LISTENTRY);

		// sets a default 16x16 pixel icon.
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/list_16.png"));
	}

	/**
	 * Run the action
	 * 
	 * Open a new text editor.
	 */
	@Override
	public void run() {

		// cancel, if the data base is not connected.
		if (!DataBaseConnectionState.INSTANCE.isConnected())
			return;

		// Sets the editors input
		UniDataSetEditorInput input = new UniDataSetEditorInput(category);

		// Open a new text Editor 
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, ListEditor.ID);
		}
		catch (PartInitException e) {
			Logger.logError(e, "Error opening Editor: " + ListEditor.ID);
		}
	}
}
