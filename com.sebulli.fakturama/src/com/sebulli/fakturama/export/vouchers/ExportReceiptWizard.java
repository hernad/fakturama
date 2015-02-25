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

package com.sebulli.fakturama.export.vouchers;

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetReceiptVoucher;
import com.sebulli.fakturama.data.DataSetVoucher;
import com.sebulli.fakturama.export.ExportWizardPageStartEndDate;

/**
 * Export wizard to export receipt vouchers
 * 
 * @author Gerd Bartelt
 */
public class ExportReceiptWizard extends Wizard implements IExportWizard {

	// The first (and only) page of this wizard
	ExportWizardPageStartEndDate page1;
	ExportOptionPage page2;

	/**
	 * Constructor Adds the first page to the wizard
	 */
	public ExportReceiptWizard() {
		//T: Title of the export wizard
		setWindowTitle(_("Export"));
		//T: Title of the export wizard
		page1 = new ExportWizardPageStartEndDate(_("List of receipt vouchers as Table"),
				//T: Text of the export wizard
				_("Select a periode.\nOnly the vouchers with a date in this periode will be exported."),
				false);
		//T: Title of the export wizard
		page2 = new ExportOptionPage(_("List of receipt vouchers as Table"),
				//T: Text of the export wizard
				_("Set some export options."));

		addPage(page1);
		addPage(page2);
	}

	/**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean performFinish() {
		Exporter exporter = new Exporter(page1.getStartDate(), page1.getEndDate(),
				page1.getDoNotUseTimePeriod(),
				page2.getShowVoucherSumColumn(),
				page2.getShowZeroVatColumn());
		//T: Title in the exported calc document
		return exporter.export((ArrayList<DataSetVoucher>) getActiveVouchers(), _("Receipt Vouchers"),
					DataSetReceiptVoucher.CUSTOMERSUPPLIER);
	}
	
	/**
	 * Returns all active vouchers
	 * 
	 * @return
	 * 	All active vouchers
	 */
	private ArrayList<?> getActiveVouchers() {
		return Data.INSTANCE.getReceiptVouchers().getActiveDatasets();
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
