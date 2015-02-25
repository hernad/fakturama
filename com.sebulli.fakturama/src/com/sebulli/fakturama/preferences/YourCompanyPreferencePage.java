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
 * Preference page for the company settings
 * 
 * @author Gerd Bartelt
 */
public class YourCompanyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public YourCompanyPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.YOUR_COMPANY_PREFERENCE_PAGE);

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_NAME", _("Company Name"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_OWNER", _("Owner"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_STREET", _("Street No."), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_ZIP", _("ZIP"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_CITY", _("City"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_COUNTRY", _("Country"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_TEL", _("Telephone"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_FAX", _("Telefax"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_EMAIL", _("E-Mail"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_WEBSITE", _("Web Site"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_VATNR", _("VAT No."), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_TAXOFFICE", _("Tax Office"), getFieldEditorParent()));

		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_BANKACCOUNTNR", _("Bank Account Nr"), getFieldEditorParent()));
		
		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_BANKCODE", _("Bank Code"), getFieldEditorParent()));
		
		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_BANK", _("Bank"), getFieldEditorParent()));
		
		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_IBAN", _("IBAN"), getFieldEditorParent()));
		
		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_COMPANY_BIC", _("BIC"), getFieldEditorParent()));
		
		//T: Preference page "Your company"
		addField(new StringFieldEditor("YOURCOMPANY_CREDITORID", _("Creditor ID"), getFieldEditorParent()));
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Your company" - Title"
		setDescription(_("Your Company"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_NAME", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_OWNER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_STREET", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_ZIP", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_CITY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_COUNTRY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_TEL", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_FAX", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_EMAIL", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_WEBSITE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_VATNR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_TAXOFFICE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_BANKACCOUNTNR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_BANKCODE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_BANK", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_IBAN", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_COMPANY_BIC", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("YOURCOMPANY_CREDITORID", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
	}

}

