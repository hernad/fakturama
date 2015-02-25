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

import com.sebulli.fakturama.views.ViewManager;
import com.sebulli.fakturama.views.datasettable.ViewDocumentTable;

/**
 * This action opens the documents in a table view.
 * 
 * @author Gerd Bartelt
 */
public class OpenDocumentsAction extends Action {

	//T: Text of the action to open the documents
	public final static String  ACTIONTEXT= _("Documents"); 

	/**
	 * Constructor
	 */
	public OpenDocumentsAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Open a list with all the documents") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_DOCUMENTS);

		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_DOCUMENTS);

		// sets a default 16x16 pixel icon.
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/letter_16.png"));
	}

	/**
	 * Run the action
	 * 
	 * Open the contacts in an table view and close the other table views.
	 */
	@Override
	public void run() {
		ViewManager.showView(ViewDocumentTable.ID);
	}
}
