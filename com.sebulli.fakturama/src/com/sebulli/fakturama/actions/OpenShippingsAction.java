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
import com.sebulli.fakturama.views.datasettable.ViewShippingTable;

/**
 * This action opens the shippings in a table view.
 * 
 * @author Gerd Bartelt
 */
public class OpenShippingsAction extends Action {

	//T: Text of the action to open the shippings
	public final static String ACTIONTEXT = _("Shippings"); 

	/**
	 * Constructor
	 */
	public OpenShippingsAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Open a list with all shipping methods") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_SHIPPINGS);

		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_SHIPPINGS);

		// sets a default 16x16 pixel icon.
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/shipping_16.png"));
	}

	/**
	 * Run the action
	 * 
	 * Open the shippings in an table view and close the other table views.
	 */
	@Override
	public void run() {
		ViewManager.showView(ViewShippingTable.ID);
	}
}
