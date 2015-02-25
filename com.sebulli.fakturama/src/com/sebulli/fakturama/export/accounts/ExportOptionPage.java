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

package com.sebulli.fakturama.export.accounts;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sebulli.fakturama.calculate.AccountSummary;

/**
 * Create the 2nd page of the account export wizard. This page is
 * used to select the account.
 * 
 * @author Gerd Bartelt
 */
public class ExportOptionPage extends WizardPage {

	//Control elements
	private Combo comboAccount;
	private ExportOptionPage me = null;
	
	/**
	 * Constructor Create the page and set title and message.
	 */
	public ExportOptionPage(String title, String label) {
		super("ExportOptionPage");
		//T: Title of the Sales Export Wizard Page 1
		setTitle(title);
		setMessage(label );
		me = this;
	}

	/**
	 * Creates the top level control for this dialog page under the given parent
	 * composite.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		// Create the top composite
		Composite top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(top);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(top);
		setControl(top);
		
		// Create the label with the help text
		Label labelDescription = new Label(top, SWT.NONE);
		
		//T: Export Sales Wizard Page
		labelDescription.setText(_("Select an account to export")+":");
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).indent(0, 10).applyTo(labelDescription);

		comboAccount = new Combo(top, SWT.BORDER);
		comboAccount.setToolTipText(labelDescription.getToolTipText());
		comboAccount.setText("");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboAccount);
		comboAccount.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean complete = me.canFlipToNextPage();
				if (complete) {
					if (me.getNextPage() instanceof AccountSettingsPage) {
						
						// Get the next page, the account setting page
						AccountSettingsPage asp = (AccountSettingsPage)(me.getNextPage());
						
						// Set account start date and value
						asp.setAccountStartValues(getSelectedAccount());
					}
				}
					
				me.setPageComplete(complete);
			}
		});
		
		// Collect all account entries
		AccountSummary accountSummary = new AccountSummary();
		accountSummary.collectAccounts();

		// Add all account entries to the combo
		for (String account : accountSummary.getAccounts()) {
			if (!account.isEmpty())
				comboAccount.add(account);
		}
	}

	/**
	 * Returns the selected account
	 * 
	 * @return 
	 * 		The selected account
	 */
	public String getSelectedAccount() {
		return comboAccount.getText();
	}

	@Override
	//public boolean isPageComplete() {
	public boolean canFlipToNextPage() {
		
		//return super.isPageComplete();
		if (comboAccount == null)
			return false;
		
		if (comboAccount.getItemCount() == 0)
			return true;

		// Do not flip to the next page, if no account is selected
		return !comboAccount.getText().isEmpty();
	}
	
	
}
