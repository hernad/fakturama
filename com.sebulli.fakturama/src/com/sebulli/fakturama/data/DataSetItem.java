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
 * UniDataSet for all items.
 * 
 * @author Gerd Bartelt
 */
public class DataSetItem extends UniDataSet {

	public int row = 0;
	
	/**
	 * Constructor Creates a new item
	 * 
	 */
	public DataSetItem() {
		this("", "", "", 1.0, "", 0.0, 0, "", "");
	}

	/**
	 * Constructor Creates a new item with positive signs from a parent item
	 * 
	 * @param parent
	 *            Parent item
	 */
	public DataSetItem(DataSetItem parent) {
		this(parent, 1);
	}

	/**
	 * Constructor Creates a new item from a parent item
	 * 
	 * @param parent
	 *            Parent item
	 * @param sign
	 *            Sign of the new item
	 */
	public DataSetItem(DataSetItem parent, int sign) {
		this(parent.getIntValueByKey("id"), parent.getStringValueByKey("name"), parent.getIntValueByKey("productid"), parent.getStringValueByKey("itemnr"),
				parent.getBooleanValueByKey("deleted"), parent.getStringValueByKey("category"), parent.getIntValueByKey("owner"), parent
						.getBooleanValueByKey("shared"), sign * parent.getDoubleValueByKey("quantity"), parent.getStringValueByKey("description"), parent
						.getDoubleValueByKey("price"), parent.getIntValueByKey("vatid"), parent.getDoubleValueByKey("discount"), parent
						.getDoubleValueByKey("vatvalue"), parent.getStringValueByKey("vatname"), parent.getStringValueByKey("vatdescription"), parent
						.getBooleanValueByKey("novat"), parent.getStringValueByKey("picturename"), parent.getBooleanValueByKey("optional")
						, parent.getStringValueByKey("qunit"));

	}

	/**
	 * Constructor Creates a new item
	 * 
	 * @param name
	 * @param itemnr
	 * @param category
	 * @param quantity
	 * @param description
	 * @param price
	 * @param vatId
	 */
	public DataSetItem(String name, String itemnr, String category, Double quantity, String description, Double price, int vatId ,String picturename, String qunit) {
		this(-1, name, -1, itemnr, false, category, -1, false, quantity, description, price, vatId, 0.0, 0.0, "", "", false , picturename, false, qunit);
	}

	/**
	 * Constructor Creates a new item from a product and the quantity
	 * 
	 * @param quantity
	 *            Quantity of the new item
	 * @param product
	 *            Product
	 */
	public DataSetItem(Double quantity, DataSetProduct product) {
		this(-1, product.getStringValueByKey("name"), product.getIntValueByKey("id"), product.getStringValueByKey("itemnr"), false, "", -1, false, quantity,
				product.getStringValueByKey("description"), product.getPriceByQuantity(quantity), product.getIntValueByKey("vatid"), 0.0, 0.0, "", "", false,
				product.getStringValueByKey("picturename"), false, product.getStringValueByKey("qunit"));
		this.setVat(product.getIntValueByKey("vatid"));
	}

	/**
	 * Constructor Creates a new item from a product and the quantity
	 * 
	 * @param quantity
	 *            Quantity of the new item
	 * @param product
	 *            Product
	 * @param discount
	 *            Discount of the new item
	 */
	public DataSetItem(Double quantity, DataSetProduct product, Double discount) {
		this(-1, product.getStringValueByKey("name"), product.getIntValueByKey("id"), product.getStringValueByKey("itemnr"), false, "", -1, false, quantity,
				product.getStringValueByKey("description"), product.getPriceByQuantity(quantity), product.getIntValueByKey("vatid"), discount, 0.0, "", "", false,
				product.getStringValueByKey("picturename"), false, product.getStringValueByKey("qunit"));
		this.setVat(product.getIntValueByKey("vatid"));
	}

	/**
	 * Constructor Creates a new item from a product and the quantity
	 * 
	 * @param id
	 * @param name
	 * @param productid
	 * @param itemnr
	 * @param deleted
	 * @param category
	 * @param owner
	 * @param shared
	 * @param quantity
	 * @param description
	 * @param price
	 * @param vatId
	 * @param discount
	 * @param vatvalue
	 * @param vatname
	 * @param vatdescription
	 * @param noVat
	 * @param picturename
	 */
	public DataSetItem(int id, String name, int productid, String itemnr, boolean deleted, String category, int owner, boolean shared, Double quantity,
			String description, Double price, int vatId, double discount, double vatvalue, String vatname, String vatdescription, boolean noVat ,
			String picturename, boolean optional, String qunit) {

		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("productid", new UniData(UniDataType.ID, productid));
		this.hashMap.put("itemnr", new UniData(UniDataType.STRING, itemnr));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("owner", new UniData(UniDataType.ID, owner));
		this.hashMap.put("shared", new UniData(UniDataType.BOOLEAN, shared));
		this.hashMap.put("quantity", new UniData(UniDataType.QUANTITY, quantity));
		this.hashMap.put("description", new UniData(UniDataType.TEXT, description));
		this.hashMap.put("price", new UniData(UniDataType.PRICE, price));

		this.hashMap.put("vatid", new UniData(UniDataType.ID, vatId));
		this.hashMap.put("vatvalue", new UniData(UniDataType.PERCENT, vatvalue));
		this.hashMap.put("vatname", new UniData(UniDataType.STRING, vatname));
		this.hashMap.put("vatdescription", new UniData(UniDataType.STRING, vatdescription));
		this.hashMap.put("novat", new UniData(UniDataType.BOOLEAN, noVat));
		this.hashMap.put("discount", new UniData(UniDataType.PERCENT, discount));
		this.hashMap.put("picturename", new UniData(UniDataType.STRING, picturename));
		this.hashMap.put("optional", new UniData(UniDataType.BOOLEAN, optional));
		this.hashMap.put("qunit", new UniData(UniDataType.STRING, qunit));

		// Name of the table in the data base
		sqlTabeName = "Items";

	}

	/**
	 * Set the VAT ID and all of the values that are in relation to the VAT ID
	 * 
	 * @param vatId
	 *            New VAT ID
	 */
	public void setVat(int vatId) {
		DataSetVAT dsVat = Data.INSTANCE.getVATs().getDatasetById(vatId);
		this.setIntValueByKey("vatid", vatId);
		this.setDoubleValueByKey("vatvalue", dsVat.getDoubleValueByKey("value"));
		this.setStringValueByKey("vatname", dsVat.getStringValueByKey("name"));
		this.setStringValueByKey("vatdescription", dsVat.getStringValueByKey("description"));
		this.setBooleanValueByKey("novat", false);
	}

}
