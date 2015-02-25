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
import org.eclipse.jface.preference.IntegerFieldEditor;
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
public class WebShopImportPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public WebShopImportPreferencePage() {
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.WEBSHOP_IMPORT_PREFERENCE_PAGE);

		//T: Preference page "Web Shop Import" - Label checkbox "web shop enabled"
		addField(new BooleanFieldEditor("WEBSHOP_ENABLED", _("Web shop enabled"), getFieldEditorParent()));
		
		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_URL", _("Webshop URL"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_USER", _("Username"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		StringFieldEditor passwordEditor = new StringFieldEditor("WEBSHOP_PASSWORD", _("Password"), getFieldEditorParent());
		passwordEditor.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwordEditor);

		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_PRODUCT_CATEGORY", _("Products in category:"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_CONTACT_CATEGORY", _("Customers in category:"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		addField(new StringFieldEditor("WEBSHOP_SHIPPING_CATEGORY", _("Shippings in category:"), getFieldEditorParent()));

		//T: Preference page "Web Shop Import" - Label
		addField(new BooleanFieldEditor("WEBSHOP_NOTIFY_PROCESSING", _("Notify customer on 'In Work'"), getFieldEditorParent()));
		//T: Preference page "Web Shop Import" - Label
		addField(new BooleanFieldEditor("WEBSHOP_NOTIFY_SHIPPED", _("Notify customer on 'Shipped'"), getFieldEditorParent()));
		//T: Preference page "Web Shop Import" - Label
		addField(new IntegerFieldEditor("WEBSHOP_MAX_PRODUCTS", _("Maximum products:"), getFieldEditorParent()));
		//T: Preference page "Web Shop Import" - Label
		addField(new BooleanFieldEditor("WEBSHOP_ONLY_MODIFIED_PRODUCTS", _("Import only modified products"), getFieldEditorParent()));
		//T: Preference page "Web Shop Import" - Label
		addField(new BooleanFieldEditor("WEBSHOP_USE_EAN_AS_ITEMNR", _("Import EAN as item number"), getFieldEditorParent()));

	
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page "Web Shop Import" - Title
		setDescription(_("Import from web shop"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_ENABLED", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_URL", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_USER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_PASSWORD", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_PRODUCT_CATEGORY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_CONTACT_CATEGORY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_SHIPPING_CATEGORY", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_NOTIFY_PROCESSING", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_NOTIFY_SHIPPED", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_MAX_PRODUCTS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_ONLY_MODIFIED_PRODUCTS", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("WEBSHOP_USE_EAN_AS_ITEMNR", write);
		
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("WEBSHOP_ENABLED", true);
		
		//T: Preference page "Web Shop Import" - Country specific URL of demo shop
		node.put("WEBSHOP_URL", _("fakturama.sebulli.com/shop/admin/fakturama_connector.php"));
		node.put("WEBSHOP_USER", "user");
		node.put("WEBSHOP_PASSWORD", "password");
		//T: Preference page "Web Shop Import" - Default value "Product Category"
		node.put("WEBSHOP_PRODUCT_CATEGORY", _("Shop"));
		//T: Preference page "Web Shop Import" - Default value "Contact Category"
		node.put("WEBSHOP_CONTACT_CATEGORY", _("Shop Customer"));
		//T: Preference page "Web Shop Import" - Default value "Shipping Category"
		node.put("WEBSHOP_SHIPPING_CATEGORY", _("Shop"));
		node.putBoolean("WEBSHOP_NOTIFY_PROCESSING", false);
		node.putBoolean("WEBSHOP_NOTIFY_SHIPPED", true);
		node.put("WEBSHOP_MAX_PRODUCTS", "1000");
		node.putBoolean("WEBSHOP_ONLY_MODIFIED_PRODUCTS", false);
		node.putBoolean("WEBSHOP_USE_EAN_AS_ITEMNR", false);
		
	}
	
}
