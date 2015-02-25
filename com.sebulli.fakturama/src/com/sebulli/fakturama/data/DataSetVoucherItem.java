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

/**
 * UniDataSet for all expenditures.
 * 
 * @author Gerd Bartelt
 */
public abstract class DataSetVoucherItem extends UniDataSet {

	/**
	 * Constructor Creates a new expenditure
	 * 
	 */
	protected DataSetVoucherItem() {
		this("");
	}

	/**
	 * Constructor Creates a new expenditure item
	 * 
	 * @param category
	 *            Category of the new expenditure
	 */
	protected DataSetVoucherItem(String category) {
		this("", category, 0.0, -1);
	}

	/**
	 * Constructor Creates a new expenditure item from a parent item
	 * 
	 * @param parent
	 *            Parent expenditure item
	 */
	protected DataSetVoucherItem(DataSetVoucherItem parent) {
		this(parent.getIntValueByKey("id"), parent.getStringValueByKey("name"), parent.getBooleanValueByKey("deleted"), parent.getStringValueByKey("category"),
				parent.getDoubleValueByKey("price"), parent.getIntValueByKey("vatid"));
	}

	/**
	 * Constructor Creates a new expenditure item
	 * 
	 * @param name
	 * @param category
	 * @param date
	 * @param documentnr
	 * @param items
	 */
	protected DataSetVoucherItem(String name, String category, Double price, int vatId) {
		this(-1, name, false, category, price, vatId);
	}

	/**
	 * Constructor Creates a new expenditure
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param date
	 * @param documentnr
	 * @param items
	 */
	protected DataSetVoucherItem(int id, String name, boolean deleted, String category, Double price, int vatId) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("price", new UniData(UniDataType.PRICE, price));
		this.hashMap.put("vatid", new UniData(UniDataType.ID, vatId));


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
	protected boolean isTheSameAs(UniDataSet uds) {

		return false;
	}

}
