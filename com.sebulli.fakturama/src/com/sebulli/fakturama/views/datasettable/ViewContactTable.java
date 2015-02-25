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
import com.sebulli.fakturama.actions.NewContactAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;

/**
 * View with the table of all contacts
 * 
 * @author Gerd Bartelt
 * 
 */
public class ViewContactTable extends ViewDataSetTable {

	// ID of this view
	public static final String ID = "com.sebulli.fakturama.views.datasettable.viewContactTable";

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Add the action to create a new entry
		addNewAction = new NewContactAction(null);

		// Mark the columns that are used by the search function.
		searchColumns = new String[6];
		searchColumns[0] = "nr";
		searchColumns[1] = "firstname";
		searchColumns[2] = "name";
		searchColumns[3] = "company";
		searchColumns[4] = "zip";
		searchColumns[5] = "city";

		super.createPartControl(parent,DataSetDocument.class, false, true, ContextHelpConstants.CONTACT_TABLE_VIEW);

		// Name of this view
		this.setPartName(_("Contacts"));

		// Create the context menu
		super.createDefaultContextMenu();

		// Name of the editor
		editor = "Contact";

		// Get the column width from the preferences
		int cw_no = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_NO");
		int cw_firstname = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_FIRSTNAME");
		int cw_lastname = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_LASTNAME");
		int cw_company = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_COMPANY");
		int cw_zip = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_ZIP");
		int cw_city = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_CONTACTS_CITY");

		
		// Create the table columns
		// new TableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, "ID", 30, 0, true, "id");
		
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("No."), cw_no, true, "nr");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("First Name"), cw_firstname,  false, "firstname");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Last Name"), cw_lastname, false, "name");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Company"), cw_company, false, "company");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("ZIP"), cw_zip, true, "zip");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("City"), cw_city, false, "city");

		// Set the input of the table viewer and the tree viewer
		tableViewer.setInput(Data.INSTANCE.getContacts());
		topicTreeViewer.setInput(Data.INSTANCE.getContacts());
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


}
