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
 * UniDataSet for all list entries
 * 
 * @author Gerd Bartelt
 */
public class DataSetList extends UniDataSet {

	/**
	 * Constructor Creates an new list entry
	 */
	public DataSetList() {
		this("");
	}

	/**
	 * Constructor Creates a new list entry
	 * 
	 * @param category
	 *            Category of the new list entry
	 */
	public DataSetList(String category) {
		this(category, "", "");
	}

	/**
	 * Constructor Creates a new list entry
	 * 
	 * @param name
	 * @param category
	 * @param value
	 */
	public DataSetList(String category, String name, String value) {
		this(-1, name, false, category, value);
	}

	/**
	 * Constructor Creates a list entry
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param value
	 */
	public DataSetList(int id, String name, boolean deleted, String category, String value) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("value", new UniData(UniDataType.STRING, value));

		// Name of the table in the data base
		sqlTabeName = "List";
	}

	/**
	 * Test, if this is equal to an other list entry
	 * Compare the category and the name. But ignore case and special characters
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	public boolean isTheSameAs(UniDataSet uds) {
		
		if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
			return false;

		if (!uds.getStringValueByKey("category").equalsIgnoreCase(this.getStringValueByKey("category")))
			return false;
		
		return true;
		
	}

	
}
