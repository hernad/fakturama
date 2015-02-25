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
 * UniDataSet for all vouchers.
 * 
 * @author Gerd Bartelt
 */
public class DataSetAccountEntry extends UniDataSet {
	
	
	/**
	 * Constructor Creates a account entry
	 * 
	 */
	public DataSetAccountEntry() {
		this("", "", "" , 0.0);
	}

	/**
	 * Constructor Creates a account entry from a voucher
	 * 
	 */
	public DataSetAccountEntry(DataSetVoucher voucher, Double sign) {
		this(voucher.getStringValueByKey("date"), voucher.getStringValueByKey("name"),
				voucher.getStringValueByKey("nr") +  "  " + voucher.getStringValueByKey("documentnr"),
				voucher.getDoubleValueByKey("paid") * sign);
	}

	
	/**
	 * Constructor Creates a account entry from a document
	 * 
	 */
	public DataSetAccountEntry(DataSetDocument document) {
		this(document.getStringValueByKey("paydate"), document.getStringValueByKey("addressfirstline"),
				document.getStringValueByKey("name") ,
				document.getDoubleValueByKey("payvalue"));
	}

	/**
	 * Constructor Creates a account entry
	 * @param date
	 * @param name
	 * 		Customer or supplier
	 * @param text
	 * @param value
	 */
	public DataSetAccountEntry(String date, String name, String text, double value){
		
		this.hashMap.put("date", new UniData(UniDataType.DATE, date));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("text", new UniData(UniDataType.TEXT, text));
		this.hashMap.put("value", new UniData(UniDataType.PRICE, value));
	}
}
