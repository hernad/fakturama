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

package com.sebulli.fakturama.actions;

/**
 * Interface defining the application's command IDs. Key bindings can be defined
 * for specific commands. To associate an action with a command, use
 * IAction.setActionDefinitionId(commandId).
 * 
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_OPEN_CONTACTS = "com.sebulli.fakturama.actions.openContacts";
	public static final String CMD_OPEN_PRODUCTS = "com.sebulli.fakturama.actions.openProducts";
	public static final String CMD_OPEN_VATS = "com.sebulli.fakturama.actions.openVats";
	public static final String CMD_OPEN_DOCUMENTS = "com.sebulli.fakturama.actions.openDocuments";
	public static final String CMD_OPEN_PAYMENTS = "com.sebulli.fakturama.actions.openPayments";
	public static final String CMD_OPEN_SHIPPINGS = "com.sebulli.fakturama.actions.openShippings";
	public static final String CMD_OPEN_TEXTS = "com.sebulli.fakturama.actions.openTexts";
	public static final String CMD_OPEN_LISTS = "com.sebulli.fakturama.actions.openLists";
	public static final String CMD_OPEN_EXPENDITUREVOUCHERS = "com.sebulli.fakturama.actions.openExpenditureVouchers";
	public static final String CMD_OPEN_RECEIPTVOUCHERS = "com.sebulli.fakturama.actions.openReceiptVouchers";

	public static final String CMD_NEW_CONTACT = "com.sebulli.fakturama.actions.newContact";
	public static final String CMD_NEW_PRODUCT = "com.sebulli.fakturama.actions.newProduct";
	public static final String CMD_NEW_VAT = "com.sebulli.fakturama.actions.newVat";
	public static final String CMD_NEW_DOCUMENT = "com.sebulli.fakturama.actions.newDocument";
	public static final String CMD_NEW_PAYMENT = "com.sebulli.fakturama.actions.newPayment";
	public static final String CMD_NEW_SHIPPING = "com.sebulli.fakturama.actions.newShipping";
	public static final String CMD_NEW_TEXT = "com.sebulli.fakturama.actions.newText";
	public static final String CMD_NEW_LISTENTRY = "com.sebulli.fakturama.actions.newListEntry";
	public static final String CMD_NEW_EXPENDITUREVOUCHER = "com.sebulli.fakturama.actions.newExpenditureVoucher";
	public static final String CMD_NEW_RECEIPTVOUCHER = "com.sebulli.fakturama.actions.newReceiptVoucher";

	public static final String CMD_NEW_ = "com.sebulli.fakturama.actions.new";

	public static final String CMD_CREATE_OODOCUMENT = "com.sebulli.fakturama.actions.createOODocument";
	public static final String CMD_SAVE = "com.sebulli.fakturama.actions.save";

	public static final String CMD_DELETE_DATASET = "com.sebulli.fakturama.actions.deleteDataSet";

	public static final String CMD_SELECT_WORKSPACE = "com.sebulli.fakturama.actions.selectWorkspace";

	public static final String CMD_WEBSHOP_IMPORT = "com.sebulli.fakturama.actions.webShopImport";

	public static final String CMD_MARK_ORDER_AS = "com.sebulli.fakturama.actions.markOrderAs";
	public static final String CMD_MARK_DOCUMENT_AS_PAID = "com.sebulli.fakturama.actions.markDocumentAsPaid";
	
	public static final String CMD_CREATE_COLLECTIVE_INVOICE = "com.sebulli.fakturama.actions.createCollectiveInvoice";

	public static final String CMD_IMPORT_CSV = "com.sebulli.fakturama.actions.importCSV";

	public static final String CMD_EXPORT_SALES_SUMMARY = "com.sebulli.fakturama.actions.exportSalesSummary";

	public static final String CMD_OPEN_BROWSER_EDITOR = "com.sebulli.fakturama.actions.openBrowserEditor";
	public static final String CMD_OPEN_CALCULATOR = "com.sebulli.fakturama.actions.openCalculator";

	public static final String CMD_P2_UPDATE = "com.sebulli.fakturama.actions.update";
	public static final String CMD_P2_INSTALL = "com.sebulli.fakturama.actions.install";
	
	public static final String CMD_OPEN_PARCEL_SERVICE = "com.sebulli.fakturama.actions.openParcelService";

	public static final String CMD_MOVE_UP = "com.sebulli.fakturama.actions.moveEntryUp";
	public static final String CMD_MOVE_DOWN = "com.sebulli.fakturama.actions.moveEntryDown";
	
	public static final String CMD_REOGANIZE_DOCUMENTS = "com.sebulli.fakturama.actions.reorganizeDocuments";
	
	public static final String CMD_REMOVE_INVOICE_REF = "com.sebulli.fakturama.actions.removeInvoiceRef";
	
	
}
