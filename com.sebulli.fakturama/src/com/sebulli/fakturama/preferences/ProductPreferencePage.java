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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the product settings
 * 
 * @author Gerd Bartelt
 */
public class ProductPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ProductPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.PRODUCT_PREFERENCE_PAGE);

		//T: Preference page "Product" - Label "Use item No."
		addField(new BooleanFieldEditor("PRODUCT_USE_ITEMNR", _("Use item No."), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use Quantity Unit"
		addField(new BooleanFieldEditor("PRODUCT_USE_QUNIT", _("Use Quantity Unit"), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use description"
		addField(new BooleanFieldEditor("PRODUCT_USE_DESCRIPTION", _("Use description"), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use gross or net or both"
		addField(new RadioGroupFieldEditor("PRODUCT_USE_NET_GROSS", _("Enter price as net or gross:"), 3, new String[][] { 
				{ _("Net"), "1" },
				{ _("Gross"), "2" },
				//T: Preference page "Product" - Label "Use both: net and gross"
				{ _("Net and Gross"), "0" } }, getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use scaled prices"
		addField(new ComboFieldEditor("PRODUCT_SCALED_PRICES", _("Use scaled prices:"), new String[][] { { "--", "1" }, { "2", "2" }, { "3", "3" }, { "4", "4" },
				{ "5", "5" } }, getFieldEditorParent()));

		//T: Preference page "Product" - Label "Possibility to select the VAT"
		addField(new BooleanFieldEditor("PRODUCT_USE_VAT", _("Select VAT"), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use weight"
		addField(new BooleanFieldEditor("PRODUCT_USE_WEIGHT", _("Use weight"), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use quantity"
		addField(new BooleanFieldEditor("PRODUCT_USE_QUANTITY", _("Use quantity"), getFieldEditorParent()));

		//T: Preference page "Product" - Label "Use product picture"
		addField(new BooleanFieldEditor("PRODUCT_USE_PICTURE", _("Use product picture"), getFieldEditorParent()));
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Product" - Title"
		setDescription(_("Product Settings"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_ITEMNR", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_QUNIT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_DESCRIPTION", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_NET_GROSS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_SCALED_PRICES", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_VAT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_WEIGHT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_QUANTITY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("PRODUCT_USE_PICTURE", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("PRODUCT_USE_ITEMNR", true);
		node.putBoolean("PRODUCT_USE_QUNIT", false);
		node.putBoolean("PRODUCT_USE_DESCRIPTION", true);
		node.put("PRODUCT_USE_NET_GROSS", "2");
		node.put("PRODUCT_SCALED_PRICES", "1");
		node.putBoolean("PRODUCT_USE_VAT", true);
		node.putBoolean("PRODUCT_USE_WEIGHT", false);
		node.putBoolean("PRODUCT_USE_QUANTITY", true);
		node.putBoolean("PRODUCT_USE_PICTURE", true);
	}

}
