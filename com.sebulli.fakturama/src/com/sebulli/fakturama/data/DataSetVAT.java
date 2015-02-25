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

import com.sebulli.fakturama.misc.DataUtils;

/**
 * UniDataSet for all vats
 * 
 * @author Gerd Bartelt
 */
public class DataSetVAT extends UniDataSet {

	
	/**
	 * Constructor Creates a new vat
	 */
	public DataSetVAT() {
		this("");
	}

	/**
	 * Constructor Creates a new vat
	 * 
	 * @param category
	 *            of the new vat
	 */
	public DataSetVAT(String category) {
		this("", category, "", 0.0);
	}

	/**
	 * Constructor Creates a new vat
	 * 
	 * @param name
	 * @param category
	 * @param description
	 * @param value
	 */
	public DataSetVAT(String name, String category, String description, Double value) {
		this(-1, name, false, category, description, value);
	}

	/**
	 * Constructor Creates a new vat
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param description
	 * @param value
	 */
	public DataSetVAT(int id, String name, boolean deleted, String category, String description, Double value) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("description", new UniData(UniDataType.STRING, description));
		this.hashMap.put("value", new UniData(UniDataType.PERCENT, value));

		// Name of the table in the data base
		sqlTabeName = "Vats";
	}

	/**
	 * Test, if this is equal to an other UniDataSet Only the name and the
	 * values are compared If the name is not set, only the values are used.
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	@Override
	public boolean isTheSameAs(UniDataSet uds) {

		// If the name of the DataSet to test is empty, than search for an entry with at least the same VAT value
		if (!uds.getStringValueByKey("name").isEmpty())
			if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
				return false;

		if (!DataUtils.DoublesAreEqual(uds.getDoubleValueByKey("value"), this.getDoubleValueByKey("value")))
			return false;
		return true;
	}

	public static String getPurchaseTaxString() {
		//T: Name of the tax that is raised when goods are purchased
		return _("Purchase Tax");
	}

	public static String getSalesTaxString() {
		//T: Name of the tax that is raised when goods are sold
		return _("Sales Tax");
	}
	

	
}
