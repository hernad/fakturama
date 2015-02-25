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

package com.sebulli.fakturama.editors;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;

/**
 * Creates an action contribution item from an action and set the mode to
 * "FORCE_TEXT"
 * 
 * @author Gerd Bartelt
 */
public class NewDocumentActionContributionItem extends ActionContributionItem {

	public NewDocumentActionContributionItem(IAction action) {
		super(action);

		// Show the name of the action under the icon.
		setMode(ActionContributionItem.MODE_FORCE_TEXT);
	}

}
