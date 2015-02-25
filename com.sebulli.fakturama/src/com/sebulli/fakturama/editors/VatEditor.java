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

package com.sebulli.fakturama.editors;

import static com.sebulli.fakturama.Translate._;

import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetVAT;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.views.datasettable.ViewVatTable;

/**
 * The VAT editor
 * 
 * @author Gerd Bartelt
 */
public class VatEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.vatEditor";

	// This UniDataSet represents the editor's input 
	private DataSetVAT vat;

	// SWT widgets of the editor
	private Composite top;
	private Text textName;
	private Text textDescription;
	private Text textValue;
	private Combo comboCategory;

	// defines, if the payment is new created
	private boolean newVat;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public VatEditor() {
		tableViewID = ViewVatTable.ID;
		editorID = "vat";
	}

	/**
	 * Saves the contents of this part
	 * 
	 * @param monitor
	 *            Progress monitor
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {

		/*
		 * the following parameters are not saved: 
		 * - id (constant)
		 */

		// Always set the editor's data set to "undeleted"
		vat.setBooleanValueByKey("deleted", false);

		// Set the payment data
		vat.setStringValueByKey("name", textName.getText());
		vat.setStringValueByKey("category", comboCategory.getText());
		vat.setStringValueByKey("description", textDescription.getText());
		vat.setDoubleValueByKey("value", DataUtils.StringToDouble(textValue.getText() + "%"));

		// If it is a new VAT, add it to the VAT list and
		// to the data base
		if (newVat) {
			vat = Data.INSTANCE.getVATs().addNewDataSet(vat);
			newVat = false;
			stdComposite.stdButton.setEnabled(true);
		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getVATs().updateDataSet(vat);
		}

		// Set the Editor's name to the payment name.
		setPartName(vat.getStringValueByKey("name"));

		// Refresh the table view of all payments
		refreshView();
		checkDirty();
	}

	/**
	 * There is no saveAs function
	 */
	@Override
	public void doSaveAs() {
	}

	/**
	 * Initializes the editor. If an existing data set is opened, the local
	 * variable "vat" is set to This data set. If the editor is opened to create
	 * a new one, a new data set is created and the local variable "vat" is set
	 * to this one.
	 * 
	 * @param input
	 *            The editor's input
	 * @param site
	 *            The editor's site
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// Set the site and the input
		setSite(site);
		setInput(input);

		// Set the editor's data set to the editor's input
		vat = (DataSetVAT) ((UniDataSetEditorInput) input).getUniDataSet();

		// test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newVat = (vat == null);

		// If new ..
		if (newVat) {

			// Create a new data set
			vat = new DataSetVAT(((UniDataSetEditorInput) input).getCategory());

			//T: VAT Editor: Part Name of a new VAT Entry
			setPartName(_("New TAX Rate"));

		}
		else {

			// Set the Editor's name to the payment name.
			setPartName(vat.getStringValueByKey("name"));
		}
	}

	/**
	 * Returns whether the contents of this part have changed since the last
	 * save operation
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		/*
		 * the following parameters are not checked:
		 *  - id (constant)
		 */

		if (vat.getBooleanValueByKey("deleted")) { return true; }
		if (newVat) { return true; }

		if (!vat.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!vat.getStringValueByKey("description").equals(textDescription.getText())) { return true; }
		if (!DataUtils.DoublesAreEqual(vat.getDoubleValueByKey("value"), DataUtils.StringToDouble(textValue.getText() + "%"))) { return true; }
		if (!vat.getStringValueByKey("category").equals(comboCategory.getText())) { return true; }

		return false;
	}

	/**
	 * Returns whether the "Save As" operation is supported by this part.
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 * @return False, SaveAs is not allowed
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Creates the SWT controls for this workbench part
	 * 
	 * @param the
	 *            parent control
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create the top Composite
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.VAT_EDITOR);

		// There is no invisible component, so no container has to be created
		//Composite invisible = new Composite(top, SWT.NONE);
		//invisible.setVisible(false);
		//GridDataFactory.fillDefaults().hint(0, 0).span(2, 1).applyTo(invisible);

		// Large VAT label
		Label labelTitle = new Label(top, SWT.NONE);
		//T: VAT Editor: Title VAT Entry
		labelTitle.setText(_("TAX Rate"));
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).span(2, 1).applyTo(labelTitle);
		makeLargeLabel(labelTitle);

		// Name of the VAT
		Label labelName = new Label(top, SWT.NONE);
		labelName.setText(_("Name"));
		//T: Tool Tip Text
		labelName.setToolTipText(_("Name of the tax rate. This is also the identifier used by the shop system."));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(top, SWT.BORDER);
		textName.setText(vat.getStringValueByKey("name"));
		textName.setToolTipText(labelName.getToolTipText());
		superviceControl(textName, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textName);

		// Category of the VAT
		Label labelCategory = new Label(top, SWT.NONE);
		labelCategory.setText(_("Category"));
		//T: Tool Tip Text
		labelCategory.setToolTipText(_("You can set a category to classify the tax rates"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);

		comboCategory = new Combo(top, SWT.BORDER);
		comboCategory.setText(vat.getStringValueByKey("category"));
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		superviceControl(comboCategory);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboCategory);

		// Collect all category strings
		TreeSet<String> categories = new TreeSet<String>();
		categories.addAll(Data.INSTANCE.getVATs().getCategoryStrings());

		// Add all category strings to the combo
		for (Object category : categories) {
			comboCategory.add(category.toString());
		}

		
		// The description
		Label labelDescription = new Label(top, SWT.NONE);
		labelDescription.setText(_("Description"));
		//T: Tool Tip Text
		labelDescription.setToolTipText(_("The description is the text used in the documents"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDescription);
		textDescription = new Text(top, SWT.BORDER);
		textDescription.setText(vat.getStringValueByKey("description"));
		textDescription.setToolTipText(labelDescription.getToolTipText());
		superviceControl(textDescription, 250);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textDescription);

		// The value
		Label labelValue = new Label(top, SWT.NONE);
		labelValue.setText(_("Value"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelValue);
		textValue = new Text(top, SWT.BORDER);
		textValue.setText(DataUtils.DoubleToFormatedPercent(vat.getDoubleValueByKey("value")));
		superviceControl(textValue, 16);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textValue);

		// Create the composite to make this payment to the standard payment. 
		Label labelStdVat = new Label(top, SWT.NONE);
		labelStdVat.setText(_("Standard"));
		//T: Tool Tip Text
		labelStdVat.setToolTipText(_("Name of the tax rate that is the standard"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelStdVat);
		//T: VAT Editor: Button description to make this as standard VAT.
		stdComposite = new StdComposite(top, vat, Data.INSTANCE.getVATs(), "standardvat",  _("This TAX Rate"), 1);
		//T: Tool Tip Text
		stdComposite.setToolTipText(_("Make this tax rate to the standard"));

		// Disable the Standard Button, if this is a new VAT
		if (!newVat)
			stdComposite.stdButton.setEnabled(true);


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
