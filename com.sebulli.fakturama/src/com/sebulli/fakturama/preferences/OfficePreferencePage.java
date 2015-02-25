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

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.OSDependent;
import com.sebulli.fakturama.office.OfficeStarter;

/**
 * Preference page for the Office settings
 * 
 * @author Gerd Bartelt
 */
public class OfficePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public OfficePreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.OPENOFFICE_PREFERENCE_PAGE);

		String defaultValue = Activator.getDefault().getPreferenceStore().getDefaultString("OPENOFFICE_PATH");
		if (!defaultValue.isEmpty())
			//T: Preference page "Office" - Label: Example of the default path. Format: (e.g. PATH).
			//T: Only the "e.g." is translated
			defaultValue = " (" + _("e.g.:") + " " + defaultValue + ")";

		if (OSDependent.isOOApp())
			//T: Preference page "Office" - Label: Office App
			addField(new AppFieldEditor("OPENOFFICE_PATH", _("Office App"), getFieldEditorParent()));
		else
			//T: Preference page "Office" - Label: Office folder
			addField(new DirectoryFieldEditor("OPENOFFICE_PATH", _("Office folder") + defaultValue, getFieldEditorParent()));

		//T: Preference page "Office" - Label: Export documents as ODT or as PDF / only ODT/PDF or both
		addField(new RadioGroupFieldEditor("OPENOFFICE_ODT_PDF", _("Export document as ODT or PDF:"), 3, new String[][] { 
				//T: Preference page "Office" - Label: Export documents as ODT or as PDF / only ODT/PDF or both
				{ _("only as ODT"), "ODT" },
				//T: Preference page "Office" - Label: Export documents as ODT or as PDF / only ODT/PDF or both
				{ _("only as PDF"), "PDF" },
				//T: Preference page "Office" - Label: Export documents as ODT or as PDF / only ODT/PDF or both
				{ _("ODT and PDF"), "ODT+PDF" } },
				getFieldEditorParent()));

		//T: Preference page "Office" 
		addField(new StringFieldEditor("OPENOFFICE_ODT_PATH_FORMAT", _("Format and path of .odt files."), getFieldEditorParent()));
		//T: Preference page "Office" 
		addField(new StringFieldEditor("OPENOFFICE_PDF_PATH_FORMAT", _("Format and path of .pdf files."), getFieldEditorParent()));

		
		//T: Preference page "Office" - Label checkbox "Start Office in a new thread"
		addField(new BooleanFieldEditor("OPENOFFICE_START_IN_NEW_THREAD", _("Start Office in a new thread"), getFieldEditorParent()));


	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Office" - Title"
		setDescription(_("Office Settings"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID);

		//PreferencesInDatabase.syncWithPreferencesFromDatabase("OPENOFFICE_PATH", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("OPENOFFICE_ODT_PDF", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("OPENOFFICE_START_IN_NEW_THREAD", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("OPENOFFICE_ODT_PATH_FORMAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("OPENOFFICE_PDF_PATH_FORMAT", write);
		
		// Set the default value
		// Search for the Office installation only, if there is no
		// path set.
		String oOHome = Activator.getDefault().getPreferenceStore().getString("OPENOFFICE_PATH");
		String defaultOOHome;

		if (!write) {
			if (oOHome.isEmpty()){
				defaultOOHome = OfficeStarter.getHome();
				if (defaultOOHome.isEmpty())
					defaultOOHome = OSDependent.getOODefaultPath();
			}
			else {
				defaultOOHome = OSDependent.getOODefaultPath();
			}
			node.put("OPENOFFICE_PATH", defaultOOHome);
		}
		
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		
		node.put("OPENOFFICE_ODT_PDF", "ODT+PDF");
		node.put("OPENOFFICE_ODT_PATH_FORMAT", "ODT/{yyyy}/{doctype}/{docname}_{address}.odt");
		node.put("OPENOFFICE_PDF_PATH_FORMAT", "PDF/{yyyy}/{doctype}/{docname}_{address}.pdf");
		node.putBoolean("OPENOFFICE_START_IN_NEW_THREAD", true);

	}

}
