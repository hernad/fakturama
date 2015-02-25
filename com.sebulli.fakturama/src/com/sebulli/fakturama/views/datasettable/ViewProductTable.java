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
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.actions.NewProductAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetProduct;

/**
 * View with the table of all products
 * 
 * @author Gerd Bartelt
 * 
 */
public class ViewProductTable extends ViewDataSetTable {

	// ID of this view
	public static final String ID = "com.sebulli.fakturama.views.datasettable.viewProductTable";

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Add the action to create a new entry
		addNewAction = new NewProductAction();

		// Mark the columns that are used by the search function.
		searchColumns = new String[4];
		searchColumns[0] = "itemnr";
		searchColumns[1] = "name";
		searchColumns[2] = "description";
		searchColumns[3] = "price1";

		super.createPartControl(parent, DataSetProduct.class, false, true, ContextHelpConstants.PRODUCT_TABLE_VIEW);

		// Name of this view
		this.setPartName(_("Products"));

		// Create the context menu
		super.createDefaultContextMenu();

		// Name of the editor
		editor = "Product";

		// Get the column width from the preferences
		int cw_itemno = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_ITEMNO");
		int cw_name = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_NAME");
		int cw_description = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_DESCRIPTION");
		int cw_quantity = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_QUANTITY");
		int cw_price = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_PRICE");
		int cw_vat = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_PRODUCTS_VAT");

		// Create the table columns
		if (Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_ITEMNR"))
			//T: Used as heading of a table. Keep the word short.
			new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Item No."), cw_itemno, true, "itemnr");

		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Name"), cw_name, false, "name");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Description"), cw_description, false, "description");
		
		if (Activator.getDefault().getPreferenceStore().getBoolean("PRODUCT_USE_QUANTITY"))
			//T: Used as heading of a table. Keep the word short.
			new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Quantity"), cw_quantity, true, "quantity");
		
		//new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("webshopid"), 80, 0, true, "webshopid");

		// Fill the price column with the net or the gross price ( for quantity = 1)
		String priceKey = "";
		if (Activator.getDefault().getPreferenceStore().getInt("DOCUMENT_USE_NET_GROSS") == 1)
			priceKey = "$Price1Gross";
		else
			priceKey = "price1";

		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Price"), cw_price, true, priceKey);
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("VAT"), cw_vat, true, "$vatbyid");

		// Set the input of the table viewer and the tree viewer
		tableViewer.setInput(Data.INSTANCE.getProducts());
		topicTreeViewer.setInput(Data.INSTANCE.getProducts());

	}

}
