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

package com.sebulli.fakturama.views;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.actions.ICommandIds;
import com.sebulli.fakturama.actions.NewContactAction;
import com.sebulli.fakturama.actions.NewProductAction;
import com.sebulli.fakturama.actions.OpenBrowserEditorAction;
import com.sebulli.fakturama.actions.OpenCalculatorAction;
import com.sebulli.fakturama.actions.OpenContactsAction;
import com.sebulli.fakturama.actions.OpenDocumentsAction;
import com.sebulli.fakturama.actions.OpenExpenditureVouchersAction;
import com.sebulli.fakturama.actions.OpenListsAction;
import com.sebulli.fakturama.actions.OpenParcelServiceAction;
import com.sebulli.fakturama.actions.OpenPaymentsAction;
import com.sebulli.fakturama.actions.OpenProductsAction;
import com.sebulli.fakturama.actions.OpenReceiptVouchersAction;
import com.sebulli.fakturama.actions.OpenShippingsAction;
import com.sebulli.fakturama.actions.OpenTextsAction;
import com.sebulli.fakturama.actions.OpenVatsAction;
import com.sebulli.fakturama.actions.WebShopImportAction;

/**
 * This class represents the navigation view of the workbench
 * 
 * @author Gerd Bartelt
 */
public class NavigationView extends ViewPart implements ICommandIds {

	// The top composite
	private Composite top;
	
	// ID of this view
	public static final String ID = "com.sebulli.fakturama.navigationView"; //$NON-NLS-1$

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create a new expand bar manager.
		ExpandBarManager expandBarManager = new ExpandBarManager();
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.NAVIGATION_VIEW);

		// Create the first expand bar "Import"
		//T: Title of an expand bar in the navigations view
		final ExpandBar bar1 = new ExpandBar(expandBarManager, top, SWT.NONE, _("Import"), "/icons/16/import_16.png" ,
				_("Import data into Fakturama"));

		bar1.addAction(new WebShopImportAction());
		// Create the 2nd expand bar "Data"
		//T: Title of an expand bar in the navigations view
		final ExpandBar bar2 = new ExpandBar(expandBarManager, top, SWT.NONE, _("Data"), "/icons/16/data_16.png" ,
				_("Data like documents, products ... "));

		bar2.addAction(new OpenDocumentsAction());
		bar2.addAction(new OpenProductsAction());
		bar2.addAction(new OpenContactsAction());
		bar2.addAction(new OpenPaymentsAction());
		bar2.addAction(new OpenShippingsAction());
		bar2.addAction(new OpenVatsAction());
		bar2.addAction(new OpenTextsAction());
		bar2.addAction(new OpenListsAction());
		bar2.addAction(new OpenExpenditureVouchersAction());
		bar2.addAction(new OpenReceiptVouchersAction());

		// Create the 3rd expand bar "Create new"
		//T: Title of an expand bar in the navigations view
		final ExpandBar bar3 = new ExpandBar(expandBarManager, top, SWT.NONE, _("New"), "/icons/16/plus_16.png" ,
				_("Create new documents, products, contacts .. "));

		bar3.addAction(new NewProductAction());
		bar3.addAction(new NewContactAction(null));

		/*
		// Create the 4th expand bar "export"
		//T: Title of an expand bar in the navigations view
		final ExpandBar bar4 = new ExpandBar(expandBarManager, top, SWT.NONE, _("Export"), "/icons/16/export_16.png" ,
				_("Export documents, contacts .. to tables and files"));

		bar4.addAction(new ExportSalesAction());
*/
		// Create the 5th expand bar "Miscellaneous"
		//T: Title of an expand bar in the navigations view
		final ExpandBar bar5 = new ExpandBar(expandBarManager, top, SWT.NONE, _("Miscellaneous"), "/icons/16/misc_16.png" ,
				_("Miscellaneous"));

		bar5.addAction(new OpenParcelServiceAction());
		bar5.addAction(new OpenBrowserEditorAction(true));
		bar5.addAction(new OpenCalculatorAction());
//		bar5.addAction(new ReorganizeDocumentsAction());
	}

	/**
	 * Set the focus to the top composite.
	 * 
	 * @see com.sebulli.fakturama.editors.Editor#setFocus()
	 */
	@Override
	public void setFocus() {
		if(top != null) 
			top.setFocus();
	}
}
