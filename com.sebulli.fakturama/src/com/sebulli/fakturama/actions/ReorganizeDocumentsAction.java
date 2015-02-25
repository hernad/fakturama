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

import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.office.FileOrganizer;

/**
 * This action creates a new contact in an editor.
 * 
 * @author Gerd Bartelt
 */
@SuppressWarnings("restriction")
public class ReorganizeDocumentsAction extends NewEditorAction {

	//T: Text of the action to reorganize all documents
	public final static String ACTIONTEXT = _("Reoganize documents"); 

	/**
	 * Constructor
	 * 
	 */
	public ReorganizeDocumentsAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Reoganize documents") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_REOGANIZE_DOCUMENTS);

		// Associate the action with a predefined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_REOGANIZE_DOCUMENTS);

		// sets a default 16x16 pixel icon.
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/reorganize_16.png"));

	}

	/**
	 * Run the action
	 * 
	 * Reorganize all documents
	 */
	@Override
	public void run() {

		// cancel, if the data base is not connected.
		if (!DataBaseConnectionState.INSTANCE.isConnected())
			return;

		//
		if (Workspace.showMessageBox(SWT.YES | SWT.NO, 
				//T: Title of the message dialog
				_("Warning"), 
				//T: Text of the message dialog to reorganize all documents
				_("All printed documents will be renamed.\nYou should first backup your workspace.\nDo you want to reorganize all documents now ?")) != SWT.YES)
			return;
		
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final StatusLineManager slm = ((WorkbenchWindow)workbenchWindow).getStatusLineManager();


		// Run the reoganization in an extra thread and show the progress in the status bar
		new Thread(new Runnable() {
			public void run() {

				// Do the reorganization
				FileOrganizer.reorganizeDocuments(slm);
				
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	//T: Clear the status bar
				    	slm.setMessage(_("done !"));
				    }
				});

			}
		}).start();
		

		
	}
}
