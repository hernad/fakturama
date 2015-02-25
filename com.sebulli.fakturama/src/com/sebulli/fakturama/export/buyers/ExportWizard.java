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

package com.sebulli.fakturama.export.buyers;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.sebulli.fakturama.export.ExportWizardPageStartEndDate;

/**
 * Export wizard to export sales
 * 
 * @author Gerd Bartelt
 */
public class ExportWizard extends Wizard implements IExportWizard {

	// The first (and only) page of this wizard
	ExportWizardPageStartEndDate page1;

	/**
	 * Constructor Adds the first page to the wizard
	 */
	public ExportWizard() {
		//T: Title of the export wizard
		setWindowTitle(_("Export"));
		//T: Title of the export wizard
		page1 = new ExportWizardPageStartEndDate(_("Buyers and volume"),
				//T: Text of the export wizard
				_("Select a periode.\nOnly the invoices with a date in this periode will be exported.\nUnpaid invoices won't be exported.")
				, false);
		addPage(page1);
	}


	/**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		Exporter exporter = new Exporter(page1.getStartDate(), page1.getEndDate(), page1.getDoNotUseTimePeriod());
		return exporter.export();
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
