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
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the document settings
 * 
 * @author Gerd Bartelt
 */
public class DocumentPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	
	/* TRANSLATORS: The placeholder indicates the bug-reporting address
    for this package.  Please add _another line_ saying
    "Report translation bugs to <...>\n" with the address for translation
    bugs (typically your translation team's web or email address).  */

	
	
	/**
	 * Constructor
	 */
	public DocumentPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.DOCUMENT_PREFERENCE_PAGE);

		//T: Preference page "Document" - Label "Format (net or gross) of the price in the item list"
		addField(new RadioGroupFieldEditor("DOCUMENT_USE_NET_GROSS", _("Price in the item list:"), 2, new String[][] { 
					{ _("Net"), "0" },
					{ _("Gross"), "1" } },
				getFieldEditorParent()));
		
		//T: Preference page "Document" - Label "Copy the content of the message field when creating a duplicate of the document."
		addField(new BooleanFieldEditor("DOCUMENT_COPY_MESSAGE_FROM_PARENT", _("Copy message field when creating a duplicate"), getFieldEditorParent()));
		//T: Preference page "Document" - Label "Copy the description in product selection dialog."
		addField(new BooleanFieldEditor("DOCUMENT_COPY_PRODUCT_DESCRIPTION_FROM_PRODUCTS_DIALOG", _("Copy description from product."), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_USE_PREVIEW_PICTURE", _("Display a preview picture"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_USE_ITEM_POS", _("Use item position"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_USE_DISCOUNT_EACH_ITEM", _("Use discount for each item"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_USE_DISCOUNT_ALL_ITEMS", _("Use discount for all items"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_DELIVERY_NOTE_ITEMS_WITH_PRICE", _("Delivery note has items with prices"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_ADD_NR_OF_IMPORTED_DELIVERY_NOTE", _("Add number of imported delivery note"), getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new BooleanFieldEditor("DOCUMENT_CUSTOMER_STATISTICS_DIALOG", _("Show a customer statistics dialog"), getFieldEditorParent()));
		//T: Preference page "Document" - How to compare the address to generate the customer statistics
		addField(new RadioGroupFieldEditor("DOCUMENT_CUSTOMER_STATISTICS_COMPARE_ADDRESS_FIELD", _("Compare:"), 2, new String[][] { 
					//T: Preference page "Document" - How to compare the address to generate the customer statistics
					{ _("Only contact id"), "0" },
					//T: Preference page "Document" - How to compare the address to generate the customer statistics
					{ _("Also address field"), "1" } },
				getFieldEditorParent()));
		//T: Preference page "Document" 
		addField(new ComboFieldEditor("DOCUMENT_MESSAGES", _("No of message fields:"), new String[][] { { "1", "1" }, { "2", "2" }, { "3", "3" }
			 }, getFieldEditorParent()));
		//T: Preference page "Document"
		addField(new StringFieldEditor("DEPOSIT_TEXT", _("Text in the deposit row"), getFieldEditorParent()));
		//T: Preference page "Document"
		addField(new StringFieldEditor("FINALPAYMENT_TEXT", _("Text in the final payment row"), getFieldEditorParent()));
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Document" - Title"
		setDescription(_("Document Settings"));
		
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_USE_ITEM_POS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_USE_NET_GROSS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_COPY_MESSAGE_FROM_PARENT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_COPY_PRODUCT_DESCRIPTION_FROM_PRODUCTS_DIALOG", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_USE_PREVIEW_PICTURE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_USE_DISCOUNT_EACH_ITEM", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_USE_DISCOUNT_ALL_ITEMS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_DELIVERY_NOTE_ITEMS_WITH_PRICE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_ADD_NR_OF_IMPORTED_DELIVERY_NOTE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_CUSTOMER_STATISTICS_DIALOG", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_CUSTOMER_STATISTICS_COMPARE_ADDRESS_FIELD", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DOCUMENT_MESSAGES", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("DEPOSIT_TEXT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("FINALPAYMENT_TEXT", write);
		}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.put("DOCUMENT_USE_NET_GROSS", "1");
		node.putBoolean("DOCUMENT_COPY_MESSAGE_FROM_PARENT", false);
		node.putBoolean("DOCUMENT_COPY_PRODUCT_DESCRIPTION_FROM_PRODUCTS_DIALOG", false);
		node.putBoolean("DOCUMENT_USE_ITEM_POS", false);
		node.putBoolean("DOCUMENT_USE_PREVIEW_PICTURE", true);
		node.putBoolean("DOCUMENT_USE_DISCOUNT_EACH_ITEM", true);
		node.putBoolean("DOCUMENT_USE_DISCOUNT_ALL_ITEMS", true);
		node.putBoolean("DOCUMENT_DELIVERY_NOTE_ITEMS_WITH_PRICE", true);
		node.putBoolean("DOCUMENT_ADD_NR_OF_IMPORTED_DELIVERY_NOTE", true);
		node.putBoolean("DOCUMENT_CUSTOMER_STATISTICS_DIALOG", true);
		node.put("DOCUMENT_CUSTOMER_STATISTICS_COMPARE_ADDRESS_FIELD", "1");
		node.put("DOCUMENT_MESSAGES", "1");
		node.put("DEPOSIT_TEXT", "Deposit");
		node.put("FINALPAYMENT_TEXT", "Finalpayment");
	}
}
