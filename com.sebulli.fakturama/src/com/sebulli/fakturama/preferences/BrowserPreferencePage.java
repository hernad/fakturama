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
public class BrowserPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public BrowserPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.BROWSER_PREFERENCE_PAGE);

		//T: Preference page "Webbrowser" - URL of the start page
		addField(new StringFieldEditor("GENERAL_WEBBROWSER_URL", _("URL web browser"), getFieldEditorParent()));
		
		//T: Preference page "Webbrowser" 
		addField(new ComboFieldEditor("BROWSER_TYPE", _("Type of web browser:"), new String[][] { { "---", "0" }, { "WebKit", "1" }, { "Mozilla", "2" }
			 }, getFieldEditorParent()));
		
		//T: Preference page "Webbrowser" 
		addField(new BooleanFieldEditor("BROWSER_SHOW_URL_BAR", _("Show the URL bar"), getFieldEditorParent()));

	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "General" - Title"
		setDescription(_("General Settings"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("GENERAL_WEBBROWSER_URL", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("BROWSER_TYPE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("BROWSER_SHOW_URL_BAR", write);
		
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
//		node.put("GENERAL_WEBBROWSER_URL", OpenBrowserEditorAction.FAKTURAMA_PROJECT_URL);
		node.put("GENERAL_WEBBROWSER_URL", "");
		node.put("BROWSER_TYPE", "0");
		node.putBoolean("BROWSER_SHOW_URL_BAR", true);
	}


}
