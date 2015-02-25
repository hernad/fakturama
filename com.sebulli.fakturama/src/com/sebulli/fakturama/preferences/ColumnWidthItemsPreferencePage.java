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
public class ColumnWidthItemsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ColumnWidthItemsPreferencePage() {
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
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_POS", _("Pos."), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_OPT", _("Opt."), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_QTY", _("Qty."), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_QUNIT", _("Qty. Unit"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_ITEMNO", _("Item No."), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_PICTURE", _("Picture"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_NAME", _("Name"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_DESCRIPTION", _("Description"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_VAT", _("VAT"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_UPRICE", _("U.Price"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_DISCOUNT", _("Discount"), getFieldEditorParent()));
		//T: Preference page "Column width" - Use the same text as in the heading of the corresponding table
		addField(new IntegerFieldEditor("COLUMNWIDTH_ITEMS_PRICE", _("Price"), getFieldEditorParent()));
	
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
		setDescription(_("Column width of the item table"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_POS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_OPT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_QTY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_QUNIT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_ITEMNO", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_PICTURE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_NAME", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_DESCRIPTION", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_VAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_UPRICE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_DISCOUNT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_ITEMS_PRICE", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.put("COLUMNWIDTH_ITEMS_POS", "40");
		node.put("COLUMNWIDTH_ITEMS_OPT", "40");
		node.put("COLUMNWIDTH_ITEMS_QTY", "60");
		node.put("COLUMNWIDTH_ITEMS_QUNIT", "60");
		node.put("COLUMNWIDTH_ITEMS_ITEMNO", "80");
		node.put("COLUMNWIDTH_ITEMS_PICTURE", "64");
		node.put("COLUMNWIDTH_ITEMS_NAME", "100");
		node.put("COLUMNWIDTH_ITEMS_DESCRIPTION", "100");
		node.put("COLUMNWIDTH_ITEMS_VAT", "50");
		node.put("COLUMNWIDTH_ITEMS_UPRICE", "85");
		node.put("COLUMNWIDTH_ITEMS_DISCOUNT", "60");
		node.put("COLUMNWIDTH_ITEMS_PRICE", "85");
	}

}
