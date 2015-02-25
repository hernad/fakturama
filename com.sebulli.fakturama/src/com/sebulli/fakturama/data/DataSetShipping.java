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
 * UniDataSet for all shippings
 * 
 * @author Gerd Bartelt
 */
public class DataSetShipping extends UniDataSet {

	// calculate the shipping's vat with a fix vat value
	public static final int SHIPPINGVATFIX = 0;
	// calculate the shipping's vat with the same vat of the items. The shipping vat is a gross value.
	public static final int SHIPPINGVATGROSS = 1;
	// calculate the shipping's vat with the same vat of the items. The shipping vat is a net value.
	public static final int SHIPPINGVATNET = 2;

	/**
	 * Constructor Creates a new shipping
	 */
	public DataSetShipping() {
		this("");
	}

	/**
	 * Constructor Creates a new shipping
	 * 
	 * @param category
	 *            Category of the new shipping
	 */
	public DataSetShipping(String category) {
		this("", category, "", 0.0, 0, 1);
	}

	/**
	 * Constructor Creates a new shipping
	 * 
	 * @param name
	 * @param category
	 * @param description
	 * @param value
	 * @param vatId
	 * @param autovat
	 */
	public DataSetShipping(String name, String category, String description, Double value, int vatId, int autovat) {
		this(-1, name, false, category, description, value, vatId, autovat);
	}

	/**
	 * Constructor Creates a new shipping
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param description
	 * @param value
	 * @param vatId
	 * @param autovat
	 */
	public DataSetShipping(int id, String name, boolean deleted, String category, String description, Double value, int vatId, int autovat) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("description", new UniData(UniDataType.STRING, description));
		this.hashMap.put("vatid", new UniData(UniDataType.ID, vatId));
		this.hashMap.put("value", new UniData(UniDataType.PRICE, value));
		this.hashMap.put("autovat", new UniData(UniDataType.INT, autovat));

		// Name of the table in the data base
		sqlTabeName = "Shippings";
	}

	/**
	 * Test, if this is equal to an other UniDataSet Only the names and the
	 * values are compared
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	@Override
	public boolean isTheSameAs(UniDataSet uds) {
		if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
			return false;
		if (!uds.getStringValueByKey("value").equals(this.getStringValueByKey("value")))
			return false;
		return true;
	}

}
