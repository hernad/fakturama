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

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Preference page for the document settings
 * 
 * @author Gerd Bartelt
 */
public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public GeneralPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.GENERAL_PREFERENCE_PAGE);

		//T: Preference page "General"
		addField(new BooleanFieldEditor("GENERAL_COLLAPSE_EXPANDBAR",_("Navigation view: Collapse expand bar"), getFieldEditorParent()));

		//T: Preference page "General"
		addField(new BooleanFieldEditor("GENERAL_CLOSE_OTHER_EDITORS",_("Close other editors"), getFieldEditorParent()));

		//T: Preference page "General"
		addField(new StringFieldEditor("GENERAL_CURRENCY", _("Currency"), getFieldEditorParent()));
		
		//T: Preference page "General"
		addField(new BooleanFieldEditor("GENERAL_HAS_THOUSANDS_SEPARATOR", _("Thousands separator"), getFieldEditorParent()));

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
		PreferencesInDatabase.syncWithPreferencesFromDatabase("GENERAL_COLLAPSE_EXPANDBAR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("GENERAL_CLOSE_OTHER_EDITORS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("GENERAL_CURRENCY", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("GENERAL_COLLAPSE_EXPANDBAR", false);
		node.putBoolean("GENERAL_CLOSE_OTHER_EDITORS", false);

		//Set the currency symbol of the default local
		String currency = "$";
		try {
			NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
			currency = numberFormatter.getCurrency().getSymbol();
		}
		catch (Exception e) {
		}
		node.put("GENERAL_CURRENCY", currency);
	}

	/**
	 * Update the currency Symbol for the whole application
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		DataUtils.updateCurrencySymbol();
		return super.performOk();
	}

	/**
	 * Update the currency Symbol for the whole application
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply() {
		DataUtils.updateCurrencySymbol();
		super.performApply();
	}

	/**
	 * Update the currency Symbol for the whole application
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		DataUtils.updateCurrencySymbol();
		super.performDefaults();
	}

}
