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
public class ColumnWidthVouchersPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ColumnWidthVouchersPreferencePage() {
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
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_DONOTBOOK", _("Book"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_DATE", _("Date"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_VOUCHER", _("Voucher"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_DOCUMENT", _("Document"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_SUPPLIER", _("Supplier"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_VOUCHERS_TOTAL", _("Total"), getFieldEditorParent()));
	
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
		setDescription(_("Column width of the voucher table"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_DONOTBOOK", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_DATE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_VOUCHER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_DOCUMENT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_SUPPLIER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_VOUCHERS_TOTAL", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		
		node.put("COLUMNWIDTH_VOUCHERS_DONOTBOOK", "20");
		node.put("COLUMNWIDTH_VOUCHERS_DATE", "80");
		node.put("COLUMNWIDTH_VOUCHERS_VOUCHER", "100");
		node.put("COLUMNWIDTH_VOUCHERS_DOCUMENT", "150");
		node.put("COLUMNWIDTH_VOUCHERS_SUPPLIER", "200");
		node.put("COLUMNWIDTH_VOUCHERS_TOTAL", "80");
	}

}
