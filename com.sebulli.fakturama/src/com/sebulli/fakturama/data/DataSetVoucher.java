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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sebulli.fakturama.calculate.VoucherSummary;
import com.sebulli.fakturama.logger.Logger;

/**
 * UniDataSet for all vouchers.
 * 
 * @author Gerd Bartelt
 */
public abstract class DataSetVoucher extends UniDataSet {
	
	
	protected VoucherSummary summary = new VoucherSummary();
	
	protected String editorID = "";

	/**
	 * Constructor Creates a new voucher
	 * 
	 */
	protected DataSetVoucher() {
		this("");
	}

	/**
	 * Constructor Creates a new voucher
	 * 
	 * @param category
	 *            Category of the new voucher
	 */
	protected DataSetVoucher(String category) {
		this("", category, (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()), "", "", "", 0.0, 0.0, false, false);
	}

	/**
	 * Constructor Creates a new voucher
	 * 
	 * @param name
	 * @param category
	 * @param date
	 * @param nr
	 * @param documentnr
	 * @param items
	 * @param paid
	 * @param total
	 * @param discounted
	 */
	protected DataSetVoucher(String name, String category, String date, String nr, String documentnr, String items, Double paid,
					Double total, boolean discounted, boolean donotbook) {
		this(-1, name, false, category, date, nr, documentnr, items, paid, total, discounted, donotbook);
	}

	/**
	 * Constructor Creates a new voucher
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param date
	 * @param nr
	 * @param documentnr
	 * @param items
	 * @param paid
	 * @param total
	 * @param discounted
	 */
	protected DataSetVoucher(int id, String name, boolean deleted, String category, String date, String nr, String documentnr, String items,
				Double paid, Double total, boolean discounted, boolean donotbook) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("date", new UniData(UniDataType.DATE, date));
		this.hashMap.put("nr", new UniData(UniDataType.STRING, documentnr));
		this.hashMap.put("documentnr", new UniData(UniDataType.STRING, documentnr));
		this.hashMap.put("items", new UniData(UniDataType.STRING, items));
		this.hashMap.put("paid", new UniData(UniDataType.PRICE, paid));
		this.hashMap.put("total", new UniData(UniDataType.PRICE, total));
		this.hashMap.put("discounted", new UniData(UniDataType.BOOLEAN, discounted));
		this.hashMap.put("donotbook", new UniData(UniDataType.BOOLEAN, donotbook));
		
	}
	
	/**
	 * Get all the voucher items. Generate the list by the items string
	 * 
	 * @return All items of this voucher
	 */
	public DataSetArray<DataSetVoucherItem> getItems() {
		DataSetArray<DataSetVoucherItem> items = new DataSetArray<DataSetVoucherItem>();

		// Split the items string
		String itemsString = this.getStringValueByKey("items");
		String[] itemsStringParts = itemsString.split(",");

		// Get all items
		for (String itemsStringPart : itemsStringParts) {
			int id;
			if (itemsStringPart.length() > 0) {
				try {
					id = Integer.parseInt(itemsStringPart);
				}
				catch (NumberFormatException e) {
					Logger.logError(e, "Error parsing item string");
					id = 0;
				}
				items.getDatasets().add(getVoucherItemByID(id));
			}
		}
		return items;
	}

	/**
	 * Get one voucher item as array Generate the list with only one entry
	 * 
	 * @return One items of this voucher
	 */
	public DataSetArray<DataSetVoucherItem> getItems(int index) {
		DataSetArray<DataSetVoucherItem> items = new DataSetArray<DataSetVoucherItem>();

		// Get one item and add it to the list
		items.getDatasets().add(getItem(index));
		return items;
	}

	/**
	 * Returns the voucher item by its ID
	 * 
	 * @param id
	 * 	Id if the data set
	 * @return
	 * 	The data set from the data object
	 */
	protected DataSetVoucherItem getVoucherItemByID(int id) {
		return null;
	}
	
	/**
	 * Get one voucher item
	 * 
	 * @return One items of this voucher
	 */
	public DataSetVoucherItem getItem(int index) {

		// Split the items string
		String itemsString = this.getStringValueByKey("items");
		String[] itemsStringParts = itemsString.split(",");
		String itemsStringPart = itemsStringParts[index];
		int id;
		if (itemsStringPart.length() > 0) {
			try {
				id = Integer.parseInt(itemsStringPart);
			}
			catch (NumberFormatException e) {
				Logger.logError(e, "Error parsing item string");
				id = 0;
			}
			return (getVoucherItemByID(id));
		}

		Logger.logError("Voucher item not found:" + index);
		return null;

	}

	/**
	 * Recalculate the voucher total values
	 */
	public void calculate() {
		calculate(this.getItems(), false, this.getDoubleValueByKey("paid"),this.getDoubleValueByKey("total"), this.getBooleanValueByKey("discounted") );
	}

	/**
	 * Recalculate the voucher total values
	 * 
	 * @param items
	 *            voucher items as DataSetArray
	 * @param useCategory
	 *            If true, the category is also used for the vat summary as a
	 *            description
	 */
	public void calculate(DataSetArray<DataSetVoucherItem> items, boolean useCategory, Double paid, Double total, boolean discounted) {
		summary.calculate(null, items, useCategory, paid, total, discounted);
	}

	/**
	 * Getter for the voucher summary
	 * 
	 * @return Summary
	 */
	public VoucherSummary getSummary() {
		return this.summary;
	}

	/**
	 * Test, if this is equal to an other UniDataSet Only the names and the item
	 * numbers are compared
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	@Override
	public boolean isTheSameAs(UniDataSet uds) {
		if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
			return false;
		if (!uds.getStringValueByKey("category").equalsIgnoreCase(this.getStringValueByKey("category")))
			return false;
		if (!uds.getStringValueByKey("date").equals(this.getStringValueByKey("date")))
			return false;
		if (!uds.getStringValueByKey("nr").equalsIgnoreCase(this.getStringValueByKey("nr")))
			return false;
		if (!uds.getStringValueByKey("documentnr").equalsIgnoreCase(this.getStringValueByKey("documentnr")))
			return false;

		return true;
	}

}
