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

package com.sebulli.fakturama.importer;

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
 * Create a page of the import wizard. This page is
 * used to select some options.
 * 
 * @author Gerd Bartelt
 */
public class ImportOptionPage extends WizardPage {

	//Control elements
	private Button buttonUpdateExisting;
	private Button buttonUpdateWithEmptyValues;
	String image = "";
	
	/**
	 * Constructor Create the page and set title and message.
	 */
	public ImportOptionPage(String title, String label, String image) {
		super("ImportOptionPage");
		//T: Title of the Import Wizard Page 1
		setTitle(title);
		setMessage(label );
		this.image = image;
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
		if (!image.isEmpty()) {
			ImageDescriptor previewImagePath = Activator.getImageDescriptor(image);
			Label preview = new Label(top, SWT.BORDER);
			preview.setText(_("preview"));
			GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(preview);
			try {
				preview.setImage(previewImagePath.createImage());
			}
			catch (Exception e) {
				Logger.logError(e, "Icon not found");
			}
		}

		
		
		// Create the label with the help text
		Label labelDescription = new Label(top, SWT.NONE);
		
		//T: Import Wizard Page 1 - Long description.
		labelDescription.setText(_("Set some import options."));
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).indent(0, 10).applyTo(labelDescription);

		// Checkbox
		buttonUpdateExisting = new Button (top, SWT.CHECK);
		//T: Import Wizard page
		buttonUpdateExisting.setText (_("Update existing entries"));

		// Checkbox
		buttonUpdateWithEmptyValues = new Button (top, SWT.CHECK);
		//T: Import Wizard page
		buttonUpdateWithEmptyValues.setText (_("Update with empty values"));
		
	}

	/**
	 * Return whether existing entries should be overwritten
	 * 
	 * @return 
	 * 		True, if they should be overwritten
	 */
	public boolean getUpdateExisting() {
		return buttonUpdateExisting.getSelection();
	}

	
	/**
	 * Return whether empty cells should be imported
	 * 
	 * @return 
	 * 		True, if they should be imported
	 */
	public boolean getUpdateWithEmptyValues() {
		return buttonUpdateWithEmptyValues.getSelection();
	}

}
