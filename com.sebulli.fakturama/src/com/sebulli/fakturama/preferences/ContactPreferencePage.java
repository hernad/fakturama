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
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the contact settings
 * 
 * @author Gerd Bartelt
 */
public class ContactPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ContactPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.CONTACT_PREFERENCE_PAGE);

		//T: Preference page "Contact" - Label checkbox "Use delivery address"
		addField(new BooleanFieldEditor("CONTACT_USE_DELIVERY", _("Use delivery address"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use bank account"
		addField(new BooleanFieldEditor("CONTACT_USE_BANK", _("Use bank account"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use miscellaneous"
		addField(new BooleanFieldEditor("CONTACT_USE_MISC", _("Use page miscellaneous"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use page notice"
		addField(new BooleanFieldEditor("CONTACT_USE_NOTE", _("Use page notice"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use gender"
		addField(new BooleanFieldEditor("CONTACT_USE_GENDER", _("Use gender"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use title"
		addField(new BooleanFieldEditor("CONTACT_USE_TITLE", _("Use title"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label format of the name
		addField(new RadioGroupFieldEditor("CONTACT_NAME_FORMAT", _("Format of the name:"), 2, new String[][] { { _("First name Last name"), "0" },
				{ _("Last name, First name"), "1" } }, getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use company field"
		addField(new BooleanFieldEditor("CONTACT_USE_COMPANY", _("Use company field"), getFieldEditorParent()));

		//T: Preference page "Contact" - Label checkbox "Use Country Field"
		addField(new BooleanFieldEditor("CONTACT_USE_COUNTRY", _("Use country field"), getFieldEditorParent()));

	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Contact" - Title"
		setDescription(_("Contact Settings"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_DELIVERY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_BANK", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_MISC", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_NOTE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_GENDER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_TITLE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_NAME_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_COMPANY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("CONTACT_USE_COUNTRY", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("CONTACT_USE_DELIVERY", true);
		node.putBoolean("CONTACT_USE_BANK", false);
		node.putBoolean("CONTACT_USE_MISC", true);
		node.putBoolean("CONTACT_USE_NOTE", true);
		node.putBoolean("CONTACT_USE_GENDER", true);
		node.putBoolean("CONTACT_USE_TITLE", false);
		node.put("CONTACT_NAME_FORMAT", "0");
		node.putBoolean("CONTACT_USE_COMPANY", true);
		node.putBoolean("CONTACT_USE_COUNTRY", true);
	}

}
