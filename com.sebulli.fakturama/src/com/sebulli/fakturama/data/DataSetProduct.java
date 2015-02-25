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

import com.sebulli.fakturama.Activator;

/**
 * UniDataSet for all products
 * 
 * @author Gerd Bartelt
 */
public class DataSetProduct extends UniDataSet {

	/**
	 * Constructor Creates an new product
	 */
	public DataSetProduct() {
		this("");
	}

	/**
	 * Constructor Creates an new product
	 * 
	 * @param category
	 *            Category of the new product
	 */
	public DataSetProduct(String category) {
		this("", "", category, "", 0.0, -1, "", "",1.0, -1, "");
	}

	/**
	 * Constructor Creates an new product
	 * 
	 * @param name
	 * @param itemnr
	 * @param category
	 * @param description
	 * @param price
	 * @param vatId
	 * @param options
	 * @param picturename
	 * @param quantity
	 */
	public DataSetProduct(String name, String itemnr, String category, String description, Double price, int vatId, String options, String picturename,
					Double quantity, int webshopid, String qunit) {
		this(-1, name, itemnr, false, category, description, price, price, price, price, price, 1, 10, 100, 1000, 10000, vatId, options, 0.0, -1, "",
				picturename, quantity , webshopid, qunit);
	}

	/**
	 * Constructor Creates an new product
	 * 
	 * @param id
	 * @param name
	 * @param itemnr
	 * @param deleted
	 * @param category
	 * @param description
	 * @param price1
	 * @param price2
	 * @param price3
	 * @param price4
	 * @param price5
	 * @param block1
	 * @param block2
	 * @param block3
	 * @param block4
	 * @param block5
	 * @param vatId
	 * @param options
	 * @param weight
	 * @param unit
	 * @param date_added
	 * @param picturename
	 * @param quantity
	 */
	public DataSetProduct(int id, String name, String itemnr, boolean deleted, String category, String description, Double price1, Double price2,
			Double price3, Double price4, Double price5, int block1, int block2, int block3, int block4, int block5, int vatId, String options, Double weight,
			int unit, String date_added, String picturename, Double quantity, int webshopid, String qunit) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("itemnr", new UniData(UniDataType.STRING, itemnr));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("description", new UniData(UniDataType.TEXT, description));
		this.hashMap.put("price1", new UniData(UniDataType.PRICE, price1));
		this.hashMap.put("price2", new UniData(UniDataType.PRICE, price2));
		this.hashMap.put("price3", new UniData(UniDataType.PRICE, price3));
		this.hashMap.put("price4", new UniData(UniDataType.PRICE, price4));
		this.hashMap.put("price5", new UniData(UniDataType.PRICE, price5));
		this.hashMap.put("block1", new UniData(UniDataType.INT, block1));
		this.hashMap.put("block2", new UniData(UniDataType.INT, block2));
		this.hashMap.put("block3", new UniData(UniDataType.INT, block3));
		this.hashMap.put("block4", new UniData(UniDataType.INT, block4));
		this.hashMap.put("block5", new UniData(UniDataType.INT, block5));
		this.hashMap.put("vatid", new UniData(UniDataType.INT, vatId));
		this.hashMap.put("options", new UniData(UniDataType.TEXT, options));
		this.hashMap.put("weight", new UniData(UniDataType.DOUBLE, weight));
		this.hashMap.put("unit", new UniData(UniDataType.INT, unit));
		this.hashMap.put("date_added", new UniData(UniDataType.DATE, date_added));
		this.hashMap.put("picturename", new UniData(UniDataType.STRING, picturename));
		this.hashMap.put("quantity", new UniData(UniDataType.QUANTITY, quantity));
		this.hashMap.put("webshopid", new UniData(UniDataType.ID, webshopid));
		this.hashMap.put("qunit", new UniData(UniDataType.STRING, qunit));

		// Name of the table in the data base
		sqlTabeName = "Products";
	}

	/**
	 * Get the products price. Because the products price can be a graduated
	 * price, it is necessary to compare all blocks.
	 * 
	 * @param quantity
	 *            Quantity to search for
	 * @return The price for this quantity
	 */
	public double getPriceByQuantity(Double quantity) {

		// Start with first block
		Double price = this.getDoubleValueByKey("price1");
		int blockQuantity = 0;
		int newQuantity;
		int scaledPrices;
		
		scaledPrices = Activator.getDefault().getPreferenceStore().getInt("PRODUCT_SCALED_PRICES");

		// search all used blocks
		for (int i = 1; i <= scaledPrices; i++) {
			newQuantity = this.getIntValueByKey("block" + Integer.toString(i));
			if ((newQuantity > blockQuantity) && (quantity >= (newQuantity - 0.0001))) {
				blockQuantity = newQuantity;
				price = this.getDoubleValueByKey("price" + Integer.toString(i));
			}
		}
		return price;
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
		int webshopid = uds.getIntValueByKey("webshopid");
		if (webshopid > 0 ) {
			if (webshopid == this.getIntValueByKey("webshopid"))
				return true;
		}
		if (!uds.getStringValueByKey("itemnr").equalsIgnoreCase(this.getStringValueByKey("itemnr")))
			return false;
		if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
			return false;

		return true;
	}

}
