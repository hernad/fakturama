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

import java.util.GregorianCalendar;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.export.ExportWizardPageStartEndDate;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Export wizard to export sales
 * 
 * @author Gerd Bartelt
 */
public class ExportWizard extends Wizard implements IExportWizard {

	// The 3 pages of this wizard
	ExportWizardPageStartEndDate page1;
	ExportOptionPage page2;
	AccountSettingsPage page3;

	/**
	 * Constructor Adds the first page to the wizard
	 */
	public ExportWizard() {
		//T: Title of the export wizard
		setWindowTitle(_("Export"));
		//T: Title of the export wizard
		page1 = new ExportWizardPageStartEndDate(_("List of account entries as a Table"),
				//T: Text of the export wizard
				_("Select a periode.\nOnly the account entries  in this periode will be exported."),
				false);
		//T: Title of the export wizard
		page2 = new ExportOptionPage(_("List of account entries as Table"),
				//T: Text of the export wizard
				_("Set some export options."));
		//T: Title of the export wizard
		page3 = new AccountSettingsPage(_("Account settings"),
				//T: Text of the export wizard
				_("Set the start value of this account."));

		addPage(page1);
		addPage(page2);
		addPage(page3);
	}

	/**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		GregorianCalendar accountDate = page3.getDate();
				
		String datePropertyKey = "export_account_date_"  + page2.getSelectedAccount().toLowerCase();
		if (!datePropertyKey.isEmpty()) {
			String datePropertyValue = DataUtils.getDateAndTimeAsString(accountDate);
			Data.INSTANCE.setProperty(datePropertyKey, datePropertyValue);
		}

		String valuePropertyKey = "export_account_value_"  + page2.getSelectedAccount().toLowerCase();
		if (!valuePropertyKey.isEmpty()) {
			String valuePropertyValue = page3.getValue().getValueAsString();
			Data.INSTANCE.setProperty(valuePropertyKey, valuePropertyValue);
		}
		
		Exporter exporter = new Exporter(page1.getStartDate(), page1.getEndDate(),
				page1.getDoNotUseTimePeriod());
		return exporter.export(page2.getSelectedAccount(), page3.getDate(), 
				page3.getValue().getValueAsDouble());
	}

	/**
	 * Initializes this creation wizard using the passed workbench and object
	 * selection.
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
