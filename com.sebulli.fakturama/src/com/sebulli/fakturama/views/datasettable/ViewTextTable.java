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
import com.sebulli.fakturama.actions.NewTextAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetText;

/**
 * View with the table of all texts
 * 
 * @author Gerd Bartelt
 * 
 */
public class ViewTextTable extends ViewDataSetTable {

	// ID of this view
	public static final String ID = "com.sebulli.fakturama.views.datasettable.viewTextTable";

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Add the action to create a new entry
		addNewAction = new NewTextAction();

		// Mark the columns that are used by the search function.
		searchColumns = new String[2];
		searchColumns[0] = "name";
		searchColumns[1] = "text";

		super.createPartControl(parent,DataSetText.class, false, true, ContextHelpConstants.TEXT_TABLE_VIEW);

		// Name of this view
		this.setPartName(_("Texts"));

		// Create the context menu
		super.createDefaultContextMenu();

		// Name of the editor
		editor = "Text";

		// Get the column width from the preferences
		int cw_name = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_TEXTS_NAME");
		int cw_text = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_TEXTS_TEXT");
		
		// Create the table columns
		// new TableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, "ID", 30, 0, true, "id");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Name"), cw_name, false, "name");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Text"), cw_text, false, "text");

		// Set the input of the table viewer and the tree viewer
		tableViewer.setInput(Data.INSTANCE.getTexts());
		topicTreeViewer.setInput(Data.INSTANCE.getTexts());

	}

}
