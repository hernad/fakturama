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

package com.sebulli.fakturama.export.productbuyers;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.logger.Logger;

/**
 * Create the first (and only) page of the sales export wizard. This page is
 * used to select the start and end date.
 * 
 * @author Gerd Bartelt
 */
public class ExportOptionPage extends WizardPage {

	//Control elements
	private Button buttonQ;
	private Button buttonV;
	
	/**
	 * Constructor Create the page and set title and message.
	 */
	public ExportOptionPage(String title, String label) {
		super("ExportOptionPage");
		//T: Title of the Sales Export Wizard Page 1
		setTitle(title);
		setMessage(label );
	}

	/**
	 * Creates the top level control for this dialog page under the given parent
	 * composite.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		// Create the top composite
		Composite top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(top);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(top);
		setControl(top);

		// Preview image
		ImageDescriptor previewImagePath = Activator.getImageDescriptor("/icons/preview/export_product_buyers.png");
		Label preview = new Label(top, SWT.BORDER);
		preview.setText(_("preview"));
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(preview);
		try {
			preview.setImage(previewImagePath.createImage());
		}
		catch (Exception e) {
			Logger.logError(e, "Icon not found");
		}

		
		
		// Create the label with the help text
		Label labelDescription = new Label(top, SWT.NONE);
		
		//T: Export Sales Wizard Page 1 - Long description.
		labelDescription.setText(_("Set sort order:"));
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).indent(0, 10).applyTo(labelDescription);

		// Radio buttons for sort order
		buttonQ = new Button (top, SWT.RADIO);
		buttonQ.setText (_("Sort by quantity"));
		buttonV = new Button (top, SWT.RADIO);
		buttonV.setText (_("Sort by volume"));

		// Default: Sort by volume
		buttonV.setSelection (true);


		
	}

	/**
	 * Return whether the data should be sorted by quantity or by volume
	 * 
	 * @return 
	 * 		True, if the data should be sorted by quantity
	 */
	public boolean getSortByQuantity() {
		return buttonQ.getSelection();
	}

}
