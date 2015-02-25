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
import com.sebulli.fakturama.data.DataSetPayment;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.views.datasettable.ViewPaymentTable;

/**
 * The payment editor
 * 
 * @author Gerd Bartelt
 */
public class PaymentEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.paymentEditor";

	// This UniDataSet represents the editor's input 
	private DataSetPayment payment;

	// SWT widgets of the editor
	private Composite top;
	private Text textName;
	private Text textDescription;
	private Text textDiscountValue;
	private Text textDiscountDays;
	private Text textNetDays;
	private Text textPaid;
	private Text textDepositPaid;
	private Text textUnpaid;
	private Combo comboCategory;

	// defines, if the payment is new created
	private boolean newPayment;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public PaymentEditor() {
		tableViewID = ViewPaymentTable.ID;
		editorID = "payment";
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
		payment.setBooleanValueByKey("deleted", false);

		// Set the payment data
		payment.setStringValueByKey("name", textName.getText());
		payment.setStringValueByKey("description", textDescription.getText());
		payment.setStringValueByKey("category", comboCategory.getText());
		payment.setDoubleValueByKey("discountvalue", DataUtils.StringToDouble(textDiscountValue.getText()));
		payment.setStringValueByKey("discountdays", textDiscountDays.getText());
		payment.setStringValueByKey("netdays", textNetDays.getText());
		payment.setStringValueByKey("paidtext", DataUtils.removeCR(textPaid.getText()));
		payment.setStringValueByKey("deposittext", DataUtils.removeCR(textDepositPaid.getText()));
		payment.setStringValueByKey("unpaidtext", DataUtils.removeCR(textUnpaid.getText()));

		// If it is a new payment, add it to the payment list and
		// to the data base
		if (newPayment) {
			payment = Data.INSTANCE.getPayments().addNewDataSet(payment);
			newPayment = false;
			stdComposite.stdButton.setEnabled(true);
		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getPayments().updateDataSet(payment);
		}

		// Set the Editor's name to the payment name.
		setPartName(payment.getStringValueByKey("name"));

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
	 * variable "payment" is set to This data set. If the editor is opened to
	 * create a new one, a new data set is created and the local variable
	 * "payment" is set to this one.
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
		payment = (DataSetPayment) ((UniDataSetEditorInput) input).getUniDataSet();

		// test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newPayment = (payment == null);

		// If new ..
		if (newPayment) {

			// Create a new data set
			payment = new DataSetPayment(((UniDataSetEditorInput) input).getCategory());
			
			//T: Payment Editor: Part Name of a new payment
			setPartName(_("New Payment"));

		}
		else {

			// Set the Editor's name to the payment name.
			setPartName(payment.getStringValueByKey("name"));
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

		if (payment.getBooleanValueByKey("deleted")) { return true; }
		if (newPayment) { return true; }

		if (!payment.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!payment.getStringValueByKey("description").equals(textDescription.getText())) { return true; }
		if (!DataUtils.DoublesAreEqual(payment.getDoubleValueByKey("discountvalue"), DataUtils.StringToDouble(textDiscountValue.getText()))) { return true; }
		if (!payment.getStringValueByKey("discountdays").equals(textDiscountDays.getText())) { return true; }
		if (!payment.getStringValueByKey("netdays").equals(textNetDays.getText())) { return true; }
		if (!payment.getStringValueByKey("category").equals(comboCategory.getText())) { return true; }
		if (!DataUtils.MultiLineStringsAreEqual(payment.getStringValueByKey("paidtext"), textPaid.getText())) { return true; }
		if (!DataUtils.MultiLineStringsAreEqual(payment.getStringValueByKey("deposittext"), textDepositPaid.getText())) { return true; }
		if (!DataUtils.MultiLineStringsAreEqual(payment.getStringValueByKey("unpaidtext"), textUnpaid.getText())) { return true; }

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
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.PAYMENT_EDITOR);

		// Large payment label
		Label labelTitle = new Label(top, SWT.NONE);
		//T: Payment Editor: Title
		labelTitle.setText(_("Payment"));
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).span(4, 1).applyTo(labelTitle);
		makeLargeLabel(labelTitle);

		// Payment name
		Label labelName = new Label(top, SWT.NONE);
		labelName.setText(_("Name"));
		//T: Tool Tip Text
		labelName.setToolTipText(_("Name of the payment. This is also the identifier used by the shop system."));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(top, SWT.BORDER);
		textName.setText(payment.getStringValueByKey("name"));
		textName.setToolTipText(labelName.getToolTipText());
		superviceControl(textName, 32);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(textName);

		// Payment category
		Label labelCategory = new Label(top, SWT.NONE);
		//T: Payment Editor - category
		labelCategory.setText(_("Account"));
		//T: Payment Editor - category Tool Tip Text
		labelCategory.setToolTipText(_("Set an (bank) account. All invoices with this payment will be booked to that account. E.g. 'Bank', 'Cash', 'Credit Card'."));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);
		
		comboCategory = new Combo(top, SWT.BORDER);
		comboCategory.setText(payment.getStringValueByKey("category"));
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		superviceControl(comboCategory);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(comboCategory);

		// Collect all category strings
		TreeSet<String> categories = new TreeSet<String>();
		categories.addAll(Data.INSTANCE.getPayments().getCategoryStrings());
		categories.addAll(Data.INSTANCE.getReceiptVouchers().getCategoryStrings());
		categories.addAll(Data.INSTANCE.getExpenditureVouchers().getCategoryStrings());

		// Add all category strings to the combo
		for (Object category : categories) {
			comboCategory.add(category.toString());
		}
		
		// Payment description
		Label labelDescription = new Label(top, SWT.NONE);
		labelDescription.setText(_("Description"));
		//T: Tool Tip Text
		labelDescription.setToolTipText(_("The description is the text used in the documents"));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDescription);
		textDescription = new Text(top, SWT.BORDER);
		textDescription.setText(payment.getStringValueByKey("description"));
		textDescription.setToolTipText(labelDescription.getToolTipText());
		superviceControl(textDescription, 64);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(textDescription);

		// Payment discount value
		Label labelDiscountValue = new Label(top, SWT.NONE);
		labelDiscountValue.setText(_("Cash discount"));
		//T: Tool Tip Text
		labelDiscountValue.setToolTipText(_("Cash discount value in percent"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDiscountValue);
		textDiscountValue = new Text(top, SWT.BORDER);
		textDiscountValue.setText(DataUtils.DoubleToFormatedPercent(payment.getDoubleValueByKey("discountvalue")));
		textDiscountValue.setToolTipText(labelDiscountValue.getToolTipText());
		superviceControl(textDiscountValue, 12);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(textDiscountValue);

		// Payment days to pay the discount
		Label labelDiscountDays = new Label(top, SWT.NONE);
		//T: Label in the payment editor
		labelDiscountDays.setText(_("Discount Days"));
		//T: Tool Tip Text
		labelDiscountDays.setToolTipText(_("Within these days the reduced price can be paid"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDiscountDays);
		textDiscountDays = new Text(top, SWT.BORDER);
		textDiscountDays.setText(payment.getStringValueByKey("discountdays"));
		textDiscountDays.setToolTipText(labelDiscountDays.getToolTipText());
		superviceControl(textDiscountDays, 8);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(textDiscountDays);

		// Payment days to pay the net value
		Label labelNetDays = new Label(top, SWT.NONE);
		//T: Label in the payment editor
		labelNetDays.setText(_("Net Days"));
		//T: Tool Tip Text
		labelNetDays.setToolTipText(_("Within these days the unreduced price has to be paid"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelNetDays);
		textNetDays = new Text(top, SWT.BORDER);
		textNetDays.setText(payment.getStringValueByKey("netdays"));
		textNetDays.setToolTipText(labelNetDays.getToolTipText());
		superviceControl(textNetDays, 8);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(textNetDays);

		// Label for the "paid" text message
		Label labelPaid = new Label(top, SWT.NONE);
		//T: Payment Editor: Label for the text paid
		labelPaid.setText(_("Text 'paid'"));
		//T: Tool Tip Text
		labelPaid.setToolTipText(_("Enter the text that appears in the document, if the invoice is paid. Placeholders can be used."));		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelPaid);

		// Create text field for the "paid" text message
		textPaid = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		textPaid.setText(DataUtils.makeOSLineFeeds(payment.getStringValueByKey("paidtext")));
		textPaid.setToolTipText(labelPaid.getToolTipText());
		superviceControl(textPaid, 500);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 200).grab(true, true).applyTo(textPaid);

		// Label for the "depositpaid" text message
		Label labelDepositPaid = new Label(top, SWT.NONE);
		//T: Payment Editor: Label for the text paid
		labelDepositPaid.setText(_("Text 'deposit'"));
		//T: Tool Tip Text
		labelDepositPaid.setToolTipText(_("Enter the text that appears in the document, if the invoice has a deposit. Placeholders can be used."));
			
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(labelDepositPaid);

		// Create text field for the "depositpaid" text message
		textDepositPaid = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		textDepositPaid.setText(DataUtils.makeOSLineFeeds(payment.getStringValueByKey("deposittext")));
		textDepositPaid.setToolTipText(labelDepositPaid.getToolTipText());
		superviceControl(textDepositPaid, 500);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 200).grab(true, true).applyTo(textDepositPaid);
		
		// Label for the "unpaid" text message
		Label labelUnpaid = new Label(top, SWT.NONE);
		//T: Payment Editor: Label for the text unpaid
		labelUnpaid.setText(_("Text 'unpaid'"));
		//T: Tool Tip Text
		labelUnpaid.setToolTipText(_("Enter the text that appears in the document, if the invoice is not paid. Placeholders can be used."));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(labelUnpaid);

		// Create text field for "unpaid" text message
		textUnpaid = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		textUnpaid.setText(DataUtils.makeOSLineFeeds(payment.getStringValueByKey("unpaidtext")));
		textUnpaid.setToolTipText(labelUnpaid.getToolTipText());
		superviceControl(textUnpaid, 500);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 200).grab(true, true).applyTo(textUnpaid);

		// Empty label
		new Label(top, SWT.NONE);

		// Info label with the possible placeholders
		Label labelPlaceholderInfo1 = new Label(top, SWT.WRAP);
		//T: Label in the payment editor
		labelPlaceholderInfo1.setText(_("Placeholder") + ": <PAID.VALUE>, <PAID.DATE>");
		makeSmallLabel(labelPlaceholderInfo1);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(labelPlaceholderInfo1);

		// Empty label
		new Label(top, SWT.NONE);

		// Info label with the possible placeholders
		Label labelPlaceholderInfo2 = new Label(top, SWT.WRAP);
		//T: Label in the payment editor
		labelPlaceholderInfo2.setText(_("Placeholder") + ": <DUE.DAYS>, <DUE.DATE>, <DUE.DISCOUNT.PERCENT>, <DUE.DISCOUNT.DAYS>, <DUE.DISCOUNT.VALUE>, <DUE.DISCOUNT.DATE>,\n" +
				"<BANK.ACCOUNT.HOLDER>, <BANK.ACCOUNT>, <BANK.ACCOUNT.CENSORED>, <BANK.IBAN>, <BANK.IBAN.CENSORED>, <BANK.BIC>, <BANK.CODE>,\n" +
				"<BANK.NAME>, <DEBITOR.BANK.ACCOUNT.HOLDER>, <DEBITOR.BANK.IBAN>, <DEBITOR.BANK.IBAN.CENSORED>, <DEBITOR.BANK.BIC>,\n" +
				"<DEBITOR.BANK.NAME>, <DEBITOR.MANDATREF>, <DOCUMENT.TOTAL>, <YOURCOMPANY.CREDITORID>");
		makeSmallLabel(labelPlaceholderInfo2);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).span(3, 1).applyTo(labelPlaceholderInfo2);

		// Create the composite to make this payment to the standard payment. 
		Label labelStd = new Label(top, SWT.NONE);
		//T: Label in the payment editor
		labelStd.setText(_("Standard"));
		//T: Tool Tip Text
		labelStd.setToolTipText(_("Name of the payment method that is the standard"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelStd);
		//T: Payment Editor: Button description to make this as standard payment.
		stdComposite = new StdComposite(top, payment, Data.INSTANCE.getPayments(), "standardpayment", _("This Payment"), 3);
		
		//T: Tool Tip Text
		stdComposite.setToolTipText(_("Make this payment to the standard payment"));
		
		// disable the Standard Button, if this is a new payment
		if (!newPayment)
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
