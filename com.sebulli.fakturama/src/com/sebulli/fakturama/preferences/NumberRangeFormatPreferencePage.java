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
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the number settings
 * 
 * @author Gerd Bartelt
 */
public class NumberRangeFormatPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public NumberRangeFormatPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.NUMBERRANGE_FORMAT_PREFERENCE_PAGE);

		//T: Preference page "Number Range Format" - Label "Format of the Customer ID"
		addField(new StringFieldEditor("NUMBERRANGE_CONTACT_FORMAT", _("Format customer ID:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the item No."
		addField(new StringFieldEditor("NUMBERRANGE_PRODUCT_FORMAT", _("Format item No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the invoice No."
		addField(new StringFieldEditor("NUMBERRANGE_INVOICE_FORMAT", _("Format invoice No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the delivery note No."
		addField(new StringFieldEditor("NUMBERRANGE_DELIVERY_FORMAT", _("Format delivery n.No..:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the offer No."
		addField(new StringFieldEditor("NUMBERRANGE_OFFER_FORMAT", _("Format offer No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the order No."
		addField(new StringFieldEditor("NUMBERRANGE_ORDER_FORMAT", _("Format order No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the confirmation No."
		addField(new StringFieldEditor("NUMBERRANGE_CONFIRMATION_FORMAT", _("Format confirmation No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the credit No."
		addField(new StringFieldEditor("NUMBERRANGE_CREDIT_FORMAT", _("Format credit No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the dunning No."
		addField(new StringFieldEditor("NUMBERRANGE_DUNNING_FORMAT", _("Format dunning No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Format" - Label "Format of the dunning No."
		addField(new StringFieldEditor("NUMBERRANGE_PROFORMA_FORMAT", _("Format proforma invoice No.:"), getFieldEditorParent()));
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Number Range Format" - Title with an example of the format
		setDescription(_("Number range format for documents\n\nExample:  INV{6nr}\nNumber will be displayed with 6 digits and a leading \"INV\" : INV000001\n"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {

		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CONTACT_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_PRODUCT_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_INVOICE_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_DELIVERY_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_OFFER_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_ORDER_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CREDIT_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CONFIRMATION_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_DUNNING_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_PROFORMA_FORMAT", write);
		
		}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Customer" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_CONTACT_FORMAT", _("CUST{6nr}"));
		//Preference page "Number Range Format" - Default value: Abbreviation for "Product/Item" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_PRODUCT_FORMAT", (""));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Invoice" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_INVOICE_FORMAT", _("INV{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Delivery Note" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_DELIVERY_FORMAT", _("D_O{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Offer" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_OFFER_FORMAT", _("LO{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Order" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_ORDER_FORMAT", _("PO{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Credit Note" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_CREDIT_FORMAT", _("CN{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Confirmation" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_CONFIRMATION_FORMAT", _("CONF{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Dunning" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_DUNNING_FORMAT", _("DUNNING{6nr}"));
		//T: Preference page "Number Range Format" - Default value: Abbreviation for "Dunning" with {6nr} for a 6 digits number
		node.put("NUMBERRANGE_PROFORMA_FORMAT", _("PROFORMA{6nr}"));
		
	}

}
