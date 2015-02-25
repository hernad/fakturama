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

public class DataSetReceiptVoucherItem extends DataSetVoucherItem{

	
	/**
	 * Default constructor
	 */
	public DataSetReceiptVoucherItem() {
		super();
		// Name of the table in the data base
		sqlTabeName = "ReceiptVoucherItems";
	}
	
	/**
	 * Constructor Creates a new receipt item from a parent item
	 * 
	 * @param parent
	 *            Parent receipt item
	 */
	public DataSetReceiptVoucherItem(DataSetVoucherItem parent) {
		super(parent);
		sqlTabeName = "ReceiptVoucherItems";
	}
	
	/**
	 * Constructor Creates a new receipt item
	 * 
	 * @param name
	 * @param category
	 * @param date
	 * @param documentnr
	 * @param items
	 */
	public DataSetReceiptVoucherItem(String name, String category, Double price, int vatId) {
		super(name, category, price, vatId);
		sqlTabeName = "ReceiptVoucherItems";
	}



}
