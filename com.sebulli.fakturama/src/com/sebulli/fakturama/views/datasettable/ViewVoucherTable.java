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

package com.sebulli.fakturama.views.datasettable;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.DataSetVoucher;

/**
 * View with the table of all vouchers
 * 
 * @author Gerd Bartelt
 * 
 */
public abstract class ViewVoucherTable extends ViewDataSetTable {

	protected String customerSupplier = "-";
	
	
	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	protected void createPartControl(Composite parent, String contextHelpId) {


		// Mark the columns that are used by the search function.
		searchColumns = new String[4];
		searchColumns[0] = "name";
		searchColumns[1] = "nr";
		searchColumns[2] = "documentnr";
		searchColumns[3] = "date";

		super.createPartControl(parent,DataSetVoucher.class, false, true, contextHelpId);


		// Create the context menu
		super.createDefaultContextMenu();
		
		// Get the column width from the preferences
		int cw_donotbook = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_DONOTBOOK");
		int cw_date = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_DATE");
		int cw_voucher = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_VOUCHER");
		int cw_document = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_DOCUMENT");
		int cw_supplier = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_SUPPLIER");
		int cw_total = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_VOUCHERS_TOTAL");
		
		// Create the table columns
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, "", cw_donotbook, true, "$donotbook");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Date"), cw_date, true, "date");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Voucher"), cw_voucher, true, "nr");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Document"), cw_document, true, "documentnr");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, customerSupplier, cw_supplier, false, "name");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Total"), cw_total, true, "paid");


	}

}
