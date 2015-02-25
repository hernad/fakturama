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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetShipping;
import com.sebulli.fakturama.data.DataSetVAT;
import com.sebulli.fakturama.data.UniData;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.data.UniDataType;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.views.datasettable.ViewShippingTable;

/**
 * The payment editor
 * 
 * @author Gerd Bartelt
 */
public class ShippingEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.shippingEditor";

	// This UniDataSet represents the editor's input 
	private DataSetShipping shipping;

	// SWT widgets of the editor
	private Composite top;
	private Text textName;
	private Text textDescription;
	private Combo comboVat;
	private ComboViewer comboViewer;
	private Combo comboAutoVat;
	private NetText netText;
	private GrossText grossText;
	private Combo comboCategory;

	// These flags are set by the preference settings.
	// They define, if elements of the editor are displayed, or not.
	private boolean useNet;
	private boolean useGross;

	// These are (non visible) values of the document
	private UniData net;
	private Double vat = 0.0;
	private int vatId = 0;
	private int autoVat = 1;

	// defines, if the shipping is new created
	private boolean newShipping;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public ShippingEditor() {
		tableViewID = ViewShippingTable.ID;
		editorID = "shipping";
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
		shipping.setBooleanValueByKey("deleted", false);

		// Set the shipping data
		shipping.setStringValueByKey("name", textName.getText());
		shipping.setStringValueByKey("category", comboCategory.getText());
		shipping.setStringValueByKey("description", textDescription.getText());
		shipping.setDoubleValueByKey("value", net.getValueAsDouble());
		shipping.setIntValueByKey("vatid", vatId);
		shipping.setIntValueByKey("autovat", autoVat);

		// If it is a new shipping, add it to the shipping list and
		// to the data base
		if (newShipping) {
			shipping = Data.INSTANCE.getShippings().addNewDataSet(shipping);
			newShipping = false;
			stdComposite.stdButton.setEnabled(true);
		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getShippings().updateDataSet(shipping);
		}

		// Set the Editor's name to the shipping name.
		setPartName(shipping.getStringValueByKey("name"));

		// Refresh the table view of all contacts
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
	 * variable "shipping" is set to This data set. If the editor is opened to
	 * create a new one, a new data set is created and the local variable
	 * "shipping" is set to this one.
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
		shipping = (DataSetShipping) ((UniDataSetEditorInput) input).getUniDataSet();

		// test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newShipping = (shipping == null);

		// If new ..
		if (newShipping) {

			// Create a new data set
			shipping = new DataSetShipping(((UniDataSetEditorInput) input).getCategory());

			//T: Shipping Editor: Part Name of a new shipping
			setPartName(_("New Shipping"));
		}
		else {

			// Set the Editor's name to the shipping name.
			setPartName(shipping.getStringValueByKey("name"));
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
		 * - id (constant)
		 */

		if (shipping.getBooleanValueByKey("deleted")) { return true; }
		if (newShipping) { return true; }

		if (!shipping.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!shipping.getStringValueByKey("description").equals(textDescription.getText())) { return true; }
		if (!DataUtils.DoublesAreEqual(shipping.getDoubleValueByKey("value"), net.getValueAsDouble())) { return true; }
		if (!shipping.getStringValueByKey("category").equals(comboCategory.getText())) { return true; }
		if (shipping.getIntValueByKey("vatid") != vatId) { return true; }
		if (shipping.getIntValueByKey("autovat") != autoVat) { return true; }

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
	 * Show or hide the netText and grossText widget, depending on the setting
	 * "autoVat".
	 */
	private void autoVatChanged() {
		switch (autoVat) {

		// The gross value is based on the net value by using
		// a constant Vat factor
		case DataSetShipping.SHIPPINGVATFIX:
			comboVat.setVisible(true);
			if (netText != null) {
				netText.setVisible(true);
				netText.setVatValue(vat);
			}
			if (grossText != null) {
				grossText.setVisible(true);
				grossText.setVatValue(vat);
			}
			break;

		// The shipping net value is based on the gross value using the
		// same VAT factor as the items. The gross value is kept constant.
		case DataSetShipping.SHIPPINGVATGROSS:
			comboVat.setVisible(false);
			if (netText != null) {
				netText.setVisible(false);
				netText.setVatValue(0.0);
			}
			if (grossText != null) {
				grossText.setVisible(true);
				grossText.setVatValue(0.0);
			}
			break;

		// The shipping gross value is based on the net value using the
		// same VAT factor as the items. The net value is kept constant.
		case DataSetShipping.SHIPPINGVATNET:
			comboVat.setVisible(false);
			if (netText != null) {
				netText.setVisible(true);
				netText.setVatValue(0.0);
			}
			if (grossText != null) {
				grossText.setVisible(false);
				grossText.setVatValue(0.0);
			}
			break;
		}

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

		// Some of this editos's control elements can be hidden.
		// Get the these settings from the preference store
		useNet = (Activator.getDefault().getPreferenceStore().getInt("PRODUCT_USE_NET_GROSS") != 2);
		useGross = (Activator.getDefault().getPreferenceStore().getInt("PRODUCT_USE_NET_GROSS") != 1);

		// Get the auto VAT setting
		autoVat = shipping.getIntValueByKey("autovat");

		if (autoVat == DataSetShipping.SHIPPINGVATGROSS)
			useGross = true;
		if (autoVat == DataSetShipping.SHIPPINGVATNET)
			useNet = true;

		// Get the VAT ID
		vatId = shipping.getIntValueByKey("vatid");

		// Get the VAT by the VAT ID
		try {
			vat = Data.INSTANCE.getVATs().getDatasetById(vatId).getDoubleValueByKey("value");
		}
		catch (IndexOutOfBoundsException e) {
			vat = 0.0;
		}

		// Create the top Composite
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.SHIPPING_EDITOR);

		// Create an invisible container for all hidden components
		// There is no invisible component, so no container has to be created
		// Composite invisible = new Composite(top, SWT.NONE);
		// invisible.setVisible(false);
		// GridDataFactory.fillDefaults().hint(0, 0).span(2, 1).applyTo(invisible);

		// Create the title
		Label labelTitle = new Label(top, SWT.NONE);
		//T: Shipping Editor: Title
		labelTitle.setText(_("Shipping Costs"));
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).span(2, 1).applyTo(labelTitle);
		makeLargeLabel(labelTitle);

		// Shipping name
		Label labelName = new Label(top, SWT.NONE);
		labelName.setText(_("Name"));
		//T: Tool Tip Text
		labelName.setToolTipText(_("Name of the shipping. This is also the identifier used by the shop system."));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(top, SWT.BORDER);
		textName.setText(shipping.getStringValueByKey("name"));
		textName.setToolTipText(labelName.getToolTipText());

		superviceControl(textName, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textName);

		// Shipping category
		Label labelCategory = new Label(top, SWT.NONE);
		labelCategory.setText(_("Category"));
		//T: Tool Tip Text
		labelCategory.setToolTipText(_("You can set a category to classify the shippings"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);

		comboCategory = new Combo(top, SWT.BORDER);
		comboCategory.setText(shipping.getStringValueByKey("category"));
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		superviceControl(comboCategory);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboCategory);

		// Collect all category strings
		TreeSet<String> categories = new TreeSet<String>();
		categories.addAll(Data.INSTANCE.getShippings().getCategoryStrings());

		// Add all category strings to the combo
		for (Object category : categories) {
			comboCategory.add(category.toString());
		}


		
		// Shipping description
		Label labelDescription = new Label(top, SWT.NONE);
		labelDescription.setText(_("Description"));
		//T: Tool Tip Text
		labelDescription.setToolTipText(_("The description is the text used in the documents"));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDescription);
		textDescription = new Text(top, SWT.BORDER);
		textDescription.setText(shipping.getStringValueByKey("description"));
		textDescription.setToolTipText(labelDescription.getToolTipText());

		superviceControl(textDescription, 250);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textDescription);

		// Shipping value
		Label labelValue = new Label(top, SWT.NONE);
		labelValue.setText(_("Value"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelValue);

		// Variable to store the net value
		net = new UniData(UniDataType.STRING, shipping.getDoubleValueByKey("value"));

		// Create a composite that contains a widget for the net and gross value
		Composite netGrossComposite = new Composite(top, SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(0, 0).numColumns((useNet && useGross) ? 2 : 1).applyTo(netGrossComposite);

		// Create a net label
		if (useNet) {
			Label netValueLabel = new Label(netGrossComposite, SWT.NONE);
			netValueLabel.setText(_("Net"));
			GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(netValueLabel);
		}

		// Create a gross label
		if (useGross) {
			Label grossValueLabel = new Label(netGrossComposite, SWT.NONE);
			grossValueLabel.setText(_("Gross"));
			GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(grossValueLabel);
		}

		// Create a net text widget
		if (useNet) {
			netText = new NetText(this, netGrossComposite, SWT.BORDER | SWT.RIGHT, net, vat);
		}

		// Create a gross text widget
		if (useGross) {
			grossText = new GrossText(this, netGrossComposite, SWT.BORDER | SWT.RIGHT, net, vat);
		}

		// If net and gross were created, link both together
		// so, if one is modified, the other will be recalculated.
		if (useNet && useGross) {
			netText.setGrossText(grossText.getGrossText());
			grossText.setNetText(netText.getNetText());
		}



		// Apply the gross text widget
		if (useGross) {
			GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).applyTo(grossText.getGrossText());
		}
		// Apply the net text widget
		if (useNet) {
			GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).applyTo(netText.getNetText());
		}

		
		// VAT Label
		Label labelVat = new Label(top, SWT.NONE);
		labelVat.setText(_("VAT"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelVat);

		// VAT combo list
		comboVat = new Combo(top, SWT.BORDER);
		comboViewer = new ComboViewer(comboVat);
		comboViewer.setContentProvider(new UniDataSetContentProvider());
		comboViewer.setLabelProvider(new UniDataSetLabelProvider());

		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				// Handle selection changed event 
				ISelection selection = event.getSelection();
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;

				// If one element is selected
				if (!structuredSelection.isEmpty()) {

					// Get the first element ...
					Object firstElement = structuredSelection.getFirstElement();

					// Get the selected VAT
					UniDataSet uds = (UniDataSet) firstElement;

					// Store the old value
					Double oldVat = vat;

					// Get the new value
					vatId = uds.getIntValueByKey("id");
					vat = uds.getDoubleValueByKey("value");

					// Recalculate the price values if gross is selected,
					// So the gross value will stay constant.
					if (!useNet) {
						net.setValue(net.getValueAsDouble() * ((1 + oldVat) / (1 + vat)));
					}

					// Update net and gross text widget
					if (netText != null)
						netText.setVatValue(vat);
					if (grossText != null)
						grossText.setVatValue(vat);

					// Check, if the document has changed.
					checkDirty();
				}
			}
		});

		// Create a JFace combo viewer for the VAT list
		comboViewer.setInput(Data.INSTANCE.getVATs().getActiveDatasetsPrefereCategory(DataSetVAT.getSalesTaxString()));
		try {
			comboViewer.setSelection(new StructuredSelection(Data.INSTANCE.getVATs().getDatasetById(vatId)), true);
		}
		catch (IndexOutOfBoundsException e) {
			comboVat.setText("invalid");
			vatId = -1;
		}

		
		// Create a label for the automatic VAT calculation
		Label labelAutoVat = new Label(top, SWT.NONE);
		//T: Shipping Editor: Label VAT Calculation
		labelAutoVat.setText(_("VAT Calculation"));
		//T: Tool Tip Text
		labelAutoVat.setToolTipText(_("Define how the VAT for the shipping is calculated. Should a fix value be used, or should the VAT calculated based on the VAT of the items."));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelAutoVat);

		
		// Create a combo list box for the automatic VAT calculation
		comboAutoVat = new Combo(top, SWT.BORDER);
		//T: Shipping Editor: list entry for "constant VAT calculation"
		comboAutoVat.add(_("Constant VAT"));
		comboAutoVat.setToolTipText(labelAutoVat.getToolTipText());
		if (useGross)
			//T: Shipping Editor: list entry for "Calculate VAT from goods VAT - constant Gross"
			comboAutoVat.add(_("Calculate VAT from Value of Goods - Constant Gross Value"));
		if (useNet)
			//T: Shipping Editor: list entry for "Calculate VAT from goods VAT - constant Net"
			comboAutoVat.add(_("Calculate VAT from Value of Goods - Constant Net Value"));

		comboAutoVat.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// Get the selected list entry
				autoVat = comboAutoVat.getSelectionIndex();

				// If no gross values are used, do not allow to select
				// the entry "SHIPPINGVATGROSS"
				if (!useGross && (autoVat == DataSetShipping.SHIPPINGVATGROSS))
					autoVat = DataSetShipping.SHIPPINGVATNET;

				// Display or hide the net and gross widgets
				autoVatChanged();

				// Check, if the document has changed.
				checkDirty();
			}
		});

		// On creating this editor, select the entry of the autoVat list,
		// that is set by the shipping.
		try {
			comboAutoVat.select(autoVat);
			autoVatChanged();
		}
		catch (IndexOutOfBoundsException e) {
			comboAutoVat.setText("invalid");
			autoVat = DataSetShipping.SHIPPINGVATGROSS;
		}

		// Create the composite to make this payment to the standard payment. 
		Label labelStdShipping = new Label(top, SWT.NONE);
		labelStdShipping.setText(_("Standard"));
		//T: Tool Tip Text
		labelStdShipping.setToolTipText(_("Name of the shipping that is the standard"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelStdShipping);
		//T: Shipping Editor: Button description to make this as standard shipping.
		stdComposite = new StdComposite(top, shipping, Data.INSTANCE.getShippings(), "standardshipping", _("This Shipping"), 1);
		
		//T: Tool Tip Text
		stdComposite.setToolTipText(_("Make this shipping to the standard shipping"));

		// Disable the Standard Button, if this is a new shipping
		if (!newShipping)
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
