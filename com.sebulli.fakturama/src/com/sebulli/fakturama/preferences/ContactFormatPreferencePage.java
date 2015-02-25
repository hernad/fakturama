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
 * Preference page for the greetings
 * 
 * @author Gerd Bartelt
 */
public class ContactFormatPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ContactFormatPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.CONTACT_FORMAT_PREFERENCE_PAGE);

		//T: Preference page "Contact Format" - label "Common Salutation"
		addField(new StringFieldEditor("CONTACT_FORMAT_GREETING_COMMON", _("Common Salutation"), getFieldEditorParent()));

		//T: Preference page "Contact Format" - label "Salutation for men"
		addField(new StringFieldEditor("CONTACT_FORMAT_GREETING_MR", _("Salutation Men"), getFieldEditorParent()));

		//T: Preference page "Contact Format" - label "Salutation for woman"
		addField(new StringFieldEditor("CONTACT_FORMAT_GREETING_MS", _("Salutation Women"), getFieldEditorParent()));

		//T: Preference page "Contact Format" - label "Salutation for companies"
		addField(new StringFieldEditor("CONTACT_FORMAT_GREETING_COMPANY", _("Salutation Company"), getFieldEditorParent()));

		//T: Preference page "Contact Format" - label "Format of the address field"
		addField(new StringFieldEditor("CONTACT_FORMAT_ADDRESS", _("Address Field"), getFieldEditorParent()));

		//T: Preference page "Contact Format" - label "List of the countries whose names are not printed in the address label"
		addField(new StringFieldEditor("CONTACT_FORMAT_HIDE_COUNTRIES", _("Hide this Countries"), getFieldEditorParent()));

	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

		//T: Preference page "Contact Format" - Title of this page with an example
		//T: how to format the address field. Use \n to separate lines.
		setDescription(_("Format of the address field\n\nExample:\nDear Mr. {title} {firstname} {lastname}\n"));

	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_GREETING_COMMON", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_GREETING_MR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_GREETING_MS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_GREETING_COMPANY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_ADDRESS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_FORMAT_HIDE_COUNTRIES", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		
		//T: Preference page "Contact Format" - Example format Strings (Common Salutation)
		node.put("CONTACT_FORMAT_GREETING_COMMON", _("Dear Sir or Madam"));

		//T: Preference page "Contact Format" - Example format Strings (Salutation Men) - do not translate the placeholders
		node.put("CONTACT_FORMAT_GREETING_MR", _("Dear Mr. {firstname} {lastname}"));

		//T: Preference page "Contact Format" - Example format Strings (Salutation Women) - do not translate the placeholders
		node.put("CONTACT_FORMAT_GREETING_MS", _("Dear Ms. {firstname} {lastname}"));

		//T: Preference page "Contact Format" - Example format Strings (Salutation Company)
		node.put("CONTACT_FORMAT_GREETING_COMPANY", _("Dear Sir or Madam"));
		
		//T: Preference page "Contact Format" - Example format Strings (Address format)
		node.put("CONTACT_FORMAT_ADDRESS", "{company}<br>{title} {firstname} {lastname}<br>{street}<br>{countrycode}{zip} {city}<br>{country}");
		
		//T: Preference page "Contact Format" - Example format Strings (Hidden countries)
		//T: Separate the country by a comma. 
		//T: If the county name is one in this list, is won't be displayed in the address
		//T: field. E.g. for a German language you should enter "Deutschland,Germany".
		//T: There should be at least 2 names, separated by a comma. So that the user
		//T: can see the format. Even if 2 countries don't make much sense like 
		//T: USA,U.S.A. for the English language.
		node.put("CONTACT_FORMAT_HIDE_COUNTRIES", _("USA,U.S.A."));

	}

}
