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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.backup.BackupManager;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.views.Calculator;

/**
 * This action opens the calculator in a view.
 * 
 * @author Gerd Bartelt
 */
public class OpenCalculatorAction extends Action {

	//T: Text of the action to open the calculator
	public final static String ACTIONTEXT = _("Calculator"); 

	/**
	 * Constructor
	 */
	public OpenCalculatorAction() {

		super(ACTIONTEXT);

		//T: Tool Tip Text
		setToolTipText(_("Open the calculator") );

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_CALCULATOR);

		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_CALCULATOR);

		// sets a default 16x16 pixel icon.
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/calculator_16.png"));
	}

	/**
	 * Run the action
	 * 
	 * Open the calculators view.
	 */
	@Override
	public void run() {

		BackupManager.createBackup();

		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(Calculator.ID);
		}
		catch (PartInitException e) {
			Logger.logError(e, "Error opening Calculator");
		}
	}
}
