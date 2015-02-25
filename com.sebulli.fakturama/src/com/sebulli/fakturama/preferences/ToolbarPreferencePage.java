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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.actions.CreateOODocumentAction;
import com.sebulli.fakturama.actions.NewContactAction;
import com.sebulli.fakturama.actions.NewExpenditureVoucherAction;
import com.sebulli.fakturama.actions.NewProductAction;
import com.sebulli.fakturama.actions.NewReceiptVoucherAction;
import com.sebulli.fakturama.actions.OpenBrowserEditorAction;
import com.sebulli.fakturama.actions.OpenCalculatorAction;
import com.sebulli.fakturama.actions.OpenParcelServiceAction;
import com.sebulli.fakturama.actions.WebShopImportAction;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * Preference page for the contact settings
 * 
 * @author Gerd Bartelt
 */
public class ToolbarPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Constructor
	 */
	public ToolbarPreferencePage() {
		super(GRID);
	}

	/**
	 * Creates the page's field editors.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {

		//T: Preference page "toolbar" 
		String showIcon = _("Show icon: ");

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.TOOLBAR_PREFERENCE_PAGE);

		addField(new BooleanFieldEditor("TOOLBAR_SHOW_WEBSHOP", showIcon + WebShopImportAction.ACTIONTEXT, getFieldEditorParent()));

		addField(new BooleanFieldEditor("TOOLBAR_SHOW_PRINT", showIcon + CreateOODocumentAction.ACTIONTEXT, getFieldEditorParent()));

		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_SAVE", showIcon + _("Save"), getFieldEditorParent()));

		// Get all documents
		for (int i=1; i<= DocumentType.MAXID; i++) {
			addField(new BooleanFieldEditor("TOOLBAR_SHOW_DOCUMENT_NEW_" + DocumentType.getTypeAsString(i).toUpperCase(), showIcon + DocumentType.getNewText(i), getFieldEditorParent()));
		}
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_NEW_PRODUCT", showIcon + NewProductAction.ACTIONTEXT, getFieldEditorParent()));

		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_NEW_CONTACT", showIcon + NewContactAction.ACTIONTEXT, getFieldEditorParent()));
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_NEW_EXPENDITUREVOUCHER", showIcon + NewExpenditureVoucherAction.ACTIONTEXT, getFieldEditorParent()));
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_NEW_RECEIPTVOUCHER", showIcon + NewReceiptVoucherAction.ACTIONTEXT, getFieldEditorParent()));
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_OPEN_PARCELSERVICE", showIcon + OpenParcelServiceAction.ACTIONTEXT, getFieldEditorParent()));
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_OPEN_BROWSER", showIcon + OpenBrowserEditorAction.ACTIONTEXT, getFieldEditorParent()));
		
		//T: Preference page "toolbar" 
		addField(new BooleanFieldEditor("TOOLBAR_SHOW_OPEN_CALCULATOR", showIcon + OpenCalculatorAction.ACTIONTEXT, getFieldEditorParent()));
		
	}

	/**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//T: Preference page Title"
		setDescription(_("Toolbar Icons"));
	}

	/**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
	public static void syncWithPreferencesFromDatabase(boolean write) {
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_WEBSHOP", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_PRINT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_SAVE", write);
		// Get all documents
		for (int i=1; i<= DocumentType.MAXID; i++) {
			PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_DOCUMENT_NEW_" + DocumentType.getTypeAsString(i).toUpperCase(), write);
		}
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_NEW_PRODUCT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_NEW_CONTACT", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_NEW_EXPENDITUREVOUCHER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_NEW_RECEIPTVOUCHER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_OPEN_PARCELSERVICE", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_OPEN_BROWSER", write);
		PreferencesInDatabase.syncWithPreferencesFromDatabase("TOOLBAR_SHOW_OPEN_CALCULATOR", write);
	}

	/**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
	public static void setInitValues(IEclipsePreferences node) {
		node.putBoolean("TOOLBAR_SHOW_WEBSHOP", true);
		node.putBoolean("TOOLBAR_SHOW_PRINT", true);
		node.putBoolean("TOOLBAR_SHOW_SAVE", true);
		// Get all documents
		for (int i=1; i<DocumentType.MAXID; i++) {
			node.putBoolean("TOOLBAR_SHOW_DOCUMENT_NEW_" + DocumentType.getTypeAsString(i).toUpperCase(), 
					i==3 || i ==5 || i == 6);
		}
		node.putBoolean("TOOLBAR_SHOW_NEW_PRODUCT", true);
		node.putBoolean("TOOLBAR_SHOW_NEW_CONTACT", true);
		node.putBoolean("TOOLBAR_SHOW_NEW_EXPENDITUREVOUCHER", true);
		node.putBoolean("TOOLBAR_SHOW_NEW_RECEIPTVOUCHER", true);
		node.putBoolean("TOOLBAR_SHOW_OPEN_PARCELSERVICE", true);
		node.putBoolean("TOOLBAR_SHOW_OPEN_BROWSER", true);
		node.putBoolean("TOOLBAR_SHOW_OPEN_CALCULATOR", true);
	}

}
