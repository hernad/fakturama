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
 * Preference page for the number settings
 * 
 * @author Gerd Bartelt
 */
public class NumberRangeValuesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public NumberRangeValuesPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.NUMBERRANGE_PREFERENCE_PAGE);

		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_CONTACT_NR", _("Next customer No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_PRODUCT_NR", _("Next item No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_INVOICE_NR", _("Next invoice No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_DELIVERY_NR", _("Next delivery note:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_OFFER_NR", _("Next offer No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_ORDER_NR", _("Next order No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_CONFIRMATION_NR", _("Next confirmation No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_CREDIT_NR", _("Next credit No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_DUNNING_NR", _("Next dunning No.:"), getFieldEditorParent()));
		//T: Preference page "Number Range Values" - Label "next free number"
		addField(new IntegerFieldEditor("NUMBERRANGE_PROFORMA_NR", _("Next proforma invoice No.:"), getFieldEditorParent()));
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Number Range Values" - Title"
		setDescription(_("Next number"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {

		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CONTACT_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_PRODUCT_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_INVOICE_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_DELIVERY_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_OFFER_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_ORDER_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CREDIT_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_CONFIRMATION_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_DUNNING_NR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("NUMBERRANGE_PROFORMA_NR", write);
		
		}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.put("NUMBERRANGE_CONTACT_NR", "1");
		node.put("NUMBERRANGE_PRODUCT_NR", "1");
		node.put("NUMBERRANGE_INVOICE_NR", "1");
		node.put("NUMBERRANGE_DELIVERY_NR", "1");
		node.put("NUMBERRANGE_OFFER_NR", "1");
		node.put("NUMBERRANGE_ORDER_NR", "1");
		node.put("NUMBERRANGE_CREDIT_NR", "1");
		node.put("NUMBERRANGE_CONFIRMATION_NR", "1");
		node.put("NUMBERRANGE_DUNNING_NR", "1");
		node.put("NUMBERRANGE_PROFORMA_NR", "1");
		
	}

}
