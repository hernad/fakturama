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

public class DataSetExpenditureVoucherItem extends DataSetVoucherItem{

	
	/**
	 * Default constructor
	 */
	public DataSetExpenditureVoucherItem() {
		super();
		// Name of the table in the data base
		sqlTabeName = "ExpenditureItems";
	}
	
	/**
	 * Constructor Creates a new expenditure item from a parent item
	 * 
	 * @param parent
	 *            Parent expenditure item
	 */
	public DataSetExpenditureVoucherItem(DataSetVoucherItem parent) {
		super(parent);
		sqlTabeName = "ExpenditureItems";
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
	public DataSetExpenditureVoucherItem(String name, String category, Double price, int vatId) {
		super(name, category, price, vatId);
		sqlTabeName = "ExpenditureItems";
	}



}
