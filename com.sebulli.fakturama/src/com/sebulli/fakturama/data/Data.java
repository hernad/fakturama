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

package com.sebulli.fakturama.data;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.CountryCodes;

/**
 * Contains the data model
 * 
 * @author Gerd Bartelt
 */
public enum Data {
	INSTANCE;

	// Data Model
	/**
	 * @uml.property  name="properties"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetProperty> properties;
	/**
	 * @uml.property  name="products"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetProduct> products;
	/**
	 * @uml.property  name="contacts"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetContact> contacts;
	/**
	 * @uml.property  name="vats"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetVAT> vats;
	/**
	 * @uml.property  name="items"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetItem> items;
	/**
	 * @uml.property  name="documents"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetDocument> documents;
	/**
	 * @uml.property  name="shippings"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetShipping> shippings;
	/**
	 * @uml.property  name="payments"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetPayment> payments;
	/**
	 * @uml.property  name="texts"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetText> texts;
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetList> list;
	/**
	 * @uml.property  name="expenditurevouchers"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetExpenditureVoucher> expenditurevouchers;
	/**
	 * @uml.property  name="expenditurevoucheritems"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetExpenditureVoucherItem> expenditurevoucheritems;
	/**
	 * @uml.property  name="receiptvouchers"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetReceiptVoucher> receiptvouchers;
	/**
	 * @uml.property  name="receiptvoucheritems"
	 * @uml.associationEnd  
	 */
	private DataSetArray<DataSetReceiptVoucherItem> receiptvoucheritems;

	// Reference to data base
	/**
	 * @uml.property  name="db"
	 * @uml.associationEnd  
	 */
	DataBase db = null;

	// True, if a new data base was created
	/**
	 * @uml.property  name="newDBcreated"
	 */
	boolean newDBcreated = false;

	/**
	 * Constructor Connect to the data base and copy the data from the data base
	 */
	Data() {
		// connect to the data base
		this.db = new DataBase();

		// Reference to database
		DataBase dbref = null;

		// Get the workspace
		Workspace.INSTANCE.initWorkspace();
		String workspace = Workspace.INSTANCE.getWorkspace();

		// do not try to create a data base, if the workspace is not set.
		if (!workspace.isEmpty())
			newDBcreated = this.db.connect(workspace);

		// Set the reference to the database, if there is a connection
		if (this.db.isConnected())
			dbref = db;
		else {
			throw new RuntimeException("couldn't connect to database. see error log.");
		}

		// Read the data from the database
		properties = new DataSetArray<DataSetProperty>(dbref, new DataSetProperty());
		products = new DataSetArray<DataSetProduct>(dbref, new DataSetProduct());
		contacts = new DataSetArray<DataSetContact>(dbref, new DataSetContact());
		vats = new DataSetArray<DataSetVAT>(dbref, new DataSetVAT());
		documents = new DataSetArray<DataSetDocument>(dbref, new DataSetDocument());
		items = new DataSetArray<DataSetItem>(dbref, new DataSetItem());
		shippings = new DataSetArray<DataSetShipping>(dbref, new DataSetShipping());
		payments = new DataSetArray<DataSetPayment>(dbref, new DataSetPayment());
		texts = new DataSetArray<DataSetText>(dbref, new DataSetText());
		list = new DataSetArray<DataSetList>(dbref, new DataSetList());
		expenditurevouchers = new DataSetArray<DataSetExpenditureVoucher>(dbref, new DataSetExpenditureVoucher());
		expenditurevoucheritems = new DataSetArray<DataSetExpenditureVoucherItem>(dbref, new DataSetExpenditureVoucherItem());
		receiptvouchers = new DataSetArray<DataSetReceiptVoucher>(dbref, new DataSetReceiptVoucher());
		receiptvoucheritems = new DataSetArray<DataSetReceiptVoucherItem>(dbref, new DataSetReceiptVoucherItem());

		// If there is a connection to the data base
		// read all the tables
		if (this.db.isConnected()) {

			// If the data base is new, create some default entries
			if (newDBcreated)
				fillWithInitialData();

			// Set the data base as connected
			DataBaseConnectionState.INSTANCE.setConnected();

		}
		// No connection, so create empty data sets
		else {
			// Display a warning
			if (!workspace.isEmpty()) {
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_INFORMATION);
				
				//T: Title of a message box
				messageBox.setText(_("Information"));
				//T: Text of the message box, if there is no connection to the database
				messageBox.setMessage(_("No connection to the database.\n\nIs the database opened by an other process ?"));
				messageBox.open();
			}
		}
	}

	/**
	 * If a new data base was created, fill some data with initial values
	 */
	public void fillWithInitialData() {

		// Fill some UniDataSets
		//T: Name of default VAT entry
		vats.addNewDataSet(new DataSetVAT(_("Tax-free"), "", 
				//T: Description of default VAT entry
				_("Free of Tax"), 0.0));

		//T: Name of default shipping entry
		shippings.addNewDataSet(new DataSetShipping(_("Free"), "", 
				//T: Description of default shipping entry
				_("Free of shipping costs"), 0.0, 0, 1));
		
		//T: Name of default payment entry
		payments.addNewDataSet(new DataSetPayment(_("Cash"), "", 
				//T: Description of default payment entry
				_("Pay Cash"), 0.0, 0, 0, 
				//T: Paid text of default payment entry
				_("Thank you for the payment."), 
				//T: Text of default deposit payment entry
				_("Thank you for the payment."),
				//T: Unpaid text of default payment entry
				_("Payment due upon receipt of invoice."), false));

		// Set the default value to this entries
		setProperty("standardvat", "0");
		setProperty("standardshipping", "0");
		setProperty("standardpayment", "0");
		
		// Load the default country codes
		CountryCodes.loadFromRecouces(list, null);
		
	}

	/**
	 * Close the data base
	 */
	public void close() {
		if (db != null)
			db.close();
	}

	/**
	 * Test if a new data base was created
	 * 
	 * @return True, if a new data base was created
	 */
	public boolean getNewDBCreated() {
		return newDBcreated;
	}

	/**
	 * Returns, if the property exists.
	 * 
	 * @param key
	 *            Property key
	 * @return Value as String
	 */
	public boolean isExistingProperty(String key) {
		for (DataSetProperty property : properties.getDatasets()) {
			if (property.getStringValueByKey("name").equalsIgnoreCase(key))
				return true;
		}

		return false;
	}

	/**
	 * Get a property value
	 * 
	 * @param key
	 *            Property key
	 * @return Value as String
	 */
	public String getProperty(String key) {
		for (DataSetProperty property : properties.getDatasets()) {
			if (property.getStringValueByKey("name").equalsIgnoreCase(key))
				return property.getStringValueByKey("value");
		}
		Logger.logInfo("Key " + key + " not in property list");
		return "";
	}

	/**
	 * Get a property value with a default value
	 * 
	 * @param key
	 *            Property key
	 * @param def
	 *            default value, if the key is not existing
	 * @return Value as String
	 */
	public String getProperty(String key, String def) {
		for (DataSetProperty property : properties.getDatasets()) {
			if (property.getStringValueByKey("name").equalsIgnoreCase(key))
				return property.getStringValueByKey("value");
		}
		return def;
	}
	
	/**
	 * Get a property value as integer
	 * 
	 * @param key
	 *            Property key
	 * @return Value as integer
	 */
	public int getPropertyAsInt(String key) {
		try {
			return Integer.parseInt(getProperty(key));
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Set a property value
	 * 
	 * @param key
	 *            Property key
	 * @param value
	 *            Property value
	 */
	public void setProperty(String key, String value) {

		// Set an existing property entry
		for (DataSetProperty property : properties.getDatasets()) {
			if (property.getStringValueByKey("name").equalsIgnoreCase(key)) {
				property.setStringValueByKey("value", value);
				properties.updateDataSet(property);
				return;
			}
		}

		// Add a new one, if it is not yet existing
		properties.addNewDataSet(new DataSetProperty(key, value));
		//Logger.logInfo("New property " + key + " added");
	}

	/**
	 * Set a property value
	 * 
	 * @param key
	 *            Property key
	 * @param value
	 *            Property value as integer
	 */
	public void setProperty(String key, Integer value) {

		// Set an existing property entry
		for (DataSetProperty property : properties.getDatasets()) {
			if (property.getStringValueByKey("name").equalsIgnoreCase(key)) {
				property.setIntValueByKey("value", value);
				properties.updateDataSet(property);
				return;
			}
		}

		// Add a new one, if it is not yet existing
		properties.addNewDataSet(new DataSetProperty(key, value.toString()));
		//Logger.logInfo("New property " + key + " added");
	}

	/**
	 * Getter for the DataSetArray products
	 * 
	 * @return All products
	 */
	public DataSetArray<DataSetProduct> getProducts() {
		return products;
	}

	/**
	 * Getter for the DataSetArray contacts
	 * 
	 * @return All contacts
	 */
	public DataSetArray<DataSetContact> getContacts() {
		return contacts;
	}

	/**
	 * Getter for the DataSetArray vats
	 * 
	 * @return All vats
	 */
	public DataSetArray<DataSetVAT> getVATs() {
		return vats;
	}

	/**
	 * Getter for the DataSetArray documents
	 * 
	 * @return All documents
	 */
	public DataSetArray<DataSetDocument> getDocuments() {
		return documents;
	}

	/**
	 * Getter for the DataSetArray items
	 * 
	 * @return All items
	 */
	public DataSetArray<DataSetItem> getItems() {
		return items;
	}

	/**
	 * Getter for the DataSetArray shippings
	 * 
	 * @return All shippings
	 */
	public DataSetArray<DataSetShipping> getShippings() {
		return shippings;
	}

	/**
	 * Getter for the DataSetArray payments
	 * 
	 * @return All payments
	 */
	public DataSetArray<DataSetPayment> getPayments() {
		return payments;
	}

	/**
	 * Getter for the DataSetArray texts
	 * 
	 * @return All texts
	 */
	public DataSetArray<DataSetText> getTexts() {
		return texts;
	}

	/**
	 * Getter for the DataSetArray list
	 * 
	 * @return All list entries
	 */
	public DataSetArray<DataSetList> getListEntries() {
		return list;
	}

	/**
	 * Getter for the DataSetArray expenditure vouchers
	 * 
	 * @return All items
	 */
	public DataSetArray<DataSetExpenditureVoucher> getExpenditureVouchers() {
		return expenditurevouchers;
	}

	/**
	 * Getter for the DataSetArray expenditure voucher items
	 * 
	 * @return All items
	 */
	public DataSetArray<DataSetExpenditureVoucherItem> getExpenditureVoucherItems() {
		return expenditurevoucheritems;
	}

	/**
	 * Getter for the DataSetArray receipt vouchers
	 * 
	 * @return All items
	 */
	public DataSetArray<DataSetReceiptVoucher> getReceiptVouchers() {
		return receiptvouchers;
	}

	/**
	 * Getter for the DataSetArray receipt voucher items
	 * 
	 * @return All items
	 */
	public DataSetArray<DataSetReceiptVoucherItem> getReceiptVoucherItems() {
		return receiptvoucheritems;
	}

	/**
	 * Get a UniDataSet value by table Name and ID.
	 * 
	 * @param tableName
	 *            Table name
	 * @param id
	 *            ID of the table entry
	 * @return The UniDataSet
	 */
	public UniDataSet getUniDataSetByTableNameAndId(String tableName, int id) {
		try {
			if (tableName.equalsIgnoreCase("products")) { return getProducts().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("vats")) { return getVATs().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("contacts")) { return getContacts().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("documents")) { return getDocuments().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("items")) { return getItems().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("shippings")) { return getShippings().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("payments")) { return getPayments().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("texts")) { return getTexts().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("list")) { return getListEntries().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("expenditures")) { return getExpenditureVouchers().getDatasetById(id); }
			if (tableName.equalsIgnoreCase("expenditureitems")) { return getExpenditureVoucherItems().getDatasetById(id); }
		}
		catch (IndexOutOfBoundsException e) {
			Logger.logError(e, "Index out of bounds: " + "TableName: " + tableName + " ID:" + Integer.toString(id));
		}

		// not found
		return null;
	}

	/**
	 * Update the data base with the new value
	 * 
	 * @param uds
	 *            UniDataSet to update
	 */
	public void updateDataSet(UniDataSet uds) {
		db.updateUniDataSet(uds);
	}

}
