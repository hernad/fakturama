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
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the webshop settings
 * 
 * @author Gerd Bartelt
 */
public class WebShopAuthorizationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public WebShopAuthorizationPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.WEBSHOP_AUTHORIZATION_PREFERENCE_PAGE);

		//T: Preference page "Web Shop Import" - Label checkbox "web shop enabled"
		addField(new BooleanFieldEditor("WEBSHOP_AUTHORIZATION_ENABLED", _("Web shop is password protected"), getFieldEditorParent()));
		
		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_AUTHORIZATION_USER", _("Username"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		StringFieldEditor passwordEditor = new StringFieldEditor("WEBSHOP_AUTHORIZATION_PASSWORD", _("Password"), getFieldEditorParent());
		passwordEditor.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwordEditor);

	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Web Shop Authorization" - Title
		setDescription(_("Authorization for password protected web shops"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_AUTHORIZATION_ENABLED", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_AUTHORIZATION_USER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_AUTHORIZATION_PASSWORD", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("WEBSHOP_AUTHORIZATION_ENABLED", false);
		node.put("WEBSHOP_AUTHORIZATION_USER", "user");
		node.put("WEBSHOP_AUTHORIZATION_PASSWORD", "password");
	}
	
}
