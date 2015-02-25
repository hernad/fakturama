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

package com.sebulli.fakturama.export.csv.products;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.export.EmptyWizardPage;

/**
 * Export wizard to export sales
 * 
 * @author Gerd Bartelt
 */
public class ExportWizard extends Wizard implements IExportWizard {

	// The first (and only) page of this wizard
	EmptyWizardPage page1;

	/**
	 * Constructor Adds the first page to the wizard
	 */
	public ExportWizard() {
		//T: Title of the export wizard
		setWindowTitle(_("Export"));
		//T: Title of the export wizard
		page1 = new EmptyWizardPage(_("Export all products"),
				//T: Text of the export wizard
				_("Export the products in an comma separated value (*.csv) table."),
				  Activator.getImageDescriptor("/icons/preview/products_csv.png")
						);
		addPage(page1);
	}


	/**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		

		// Create a "SAVE AS" file dialog
		FileDialog fileDialog = new FileDialog(page1.getShell(), SWT.SAVE);
		
		fileDialog.setFilterExtensions(new String[] { "*.csv" });
		//T: Text in a file name dialog
		fileDialog.setFilterNames(new String[] { _("Comma separated value file")+" (*.csv)" });
		//T: Text in a file name dialog
		fileDialog.setText(_("Enter file name to save"));
		String selectedFile = fileDialog.open();
		if (selectedFile != null) {
			Exporter exporter = new Exporter();
			return exporter.export(selectedFile);
		}
		else 
			return false;
	}

	/**
	 * Initializes this creation wizard using the passed workbench and object
	 * selection.
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
