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

package com.sebulli.fakturama.preferences;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the width of the table columns
 * 
 * @author Gerd Bartelt
 */
public class ColumnWidthDialogProductsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ColumnWidthDialogProductsPreferencePage() {
		super(GRID);

	}

	/**
	 * Creates the page's field editors.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {
		
		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.COLUMN_WIDTH_PREFERENCE_PAGE);

		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_PRODUCTS_ITEMNO", _("Item No."), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_PRODUCTS_NAME", _("Name"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_PRODUCTS_DESCRIPTION", _("Description"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_PRODUCTS_PRICE", _("Price"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_PRODUCTS_VAT", _("VAT"), getFieldEditorParent()));
		
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page - Title
		setDescription(_("Column width of the dialog to select a product"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_PRODUCTS_ITEMNO", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_PRODUCTS_NAME", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_PRODUCTS_DESCRIPTION", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_PRODUCTS_PRICE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_PRODUCTS_VAT", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.put("COLUMNWIDTH_DIALOG_PRODUCTS_ITEMNO", "80");
		node.put("COLUMNWIDTH_DIALOG_PRODUCTS_NAME", "150");
		node.put("COLUMNWIDTH_DIALOG_PRODUCTS_DESCRIPTION", "200");
		node.put("COLUMNWIDTH_DIALOG_PRODUCTS_PRICE", "70");
		node.put("COLUMNWIDTH_DIALOG_PRODUCTS_VAT", "40");
	}

}
