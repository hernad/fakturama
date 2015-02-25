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


public class DataSetReceiptVoucher extends DataSetVoucher{
	
	//T: Text voucher name. Customer or supplier
	public final static String CUSTOMERSUPPLIER = _("Customer");
	
	/**
	 * Constructor
	 */
	public DataSetReceiptVoucher() {
		super();
		// Name of the table in the data base
		sqlTabeName = "ReceiptVouchers";

	}
	
	/**
	 * Constructor Creates a new receipt voucher
	 * 
	 * @param category
	 *            Category of the new receipt voucher
	 */
	public DataSetReceiptVoucher(String category) {
		super (category);
		// Name of the table in the data base
		sqlTabeName = "ReceiptVouchers";
	}


	
	/**
	 * Returns the voucher by its ID
	 * 
	 * @param id
	 * 	Id if the data set
	 * @return
	 * 	The data set from the data object
	 */
	protected DataSetVoucherItem getVoucherItemByID(int id) {
		return (Data.INSTANCE.getReceiptVoucherItems().getDatasetById(id));
	}

	
}
