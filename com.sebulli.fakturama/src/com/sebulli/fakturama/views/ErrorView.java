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

package com.sebulli.fakturama.views;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.sebulli.fakturama.ContextHelpConstants;

/**
 * This class represents the error view of the workbench
 * 
 * @author Gerd Bartelt
 */
public class ErrorView extends ViewPart {

	
	// The top composite
	private Composite top;

	// ID of this view
	public static final String ID = "com.sebulli.fakturama.views.errorView";

	// The text of the view
	private Text errorText;

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Name of this view
		this.setPartName(_("Error"));

		// Create top composite
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.ERROR_VIEW);

		
		// create the label
		Label labelItemNr = new Label(top, SWT.NONE);
		//T: Label of the error view
		labelItemNr.setText(_("Error:"));

		// fill the rest of the view with the text field
		errorText = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(errorText);
	}

	/**
	 * Set the focus to the top composite.
	 * 
	 * @see com.sebulli.fakturama.editors.Editor#setFocus()
	 */
	@Override
	public void setFocus() {
		if(top != null) 
			top.setFocus();
	}

	/**
	 * Set the error text
	 * 
	 * @param errorMessage
	 */
	public void setErrorText(String errorMessage) {
		errorText.setText(errorMessage);
	}
}
