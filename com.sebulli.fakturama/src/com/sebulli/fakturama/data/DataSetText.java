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
 * UniDataSet for all texts
 * 
 * @author Gerd Bartelt
 */
public class DataSetText extends UniDataSet {

	/**
	 * Constructor Creates a new text
	 */
	public DataSetText() {
		this("", "", "");
	}

	/**
	 * Constructor Creates a new text
	 * 
	 * @param category
	 *            Category of the new text
	 */
	public DataSetText(String category) {
		this("", category, "");
	}

	/**
	 * Constructor Creates a new text
	 * 
	 * @param name
	 * @param category
	 * @param text
	 */
	public DataSetText(String name, String category, String text) {
		this(-1, name, false, category, text);
	}

	/**
	 * Constructor Creates a new text
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param category
	 * @param text
	 */
	public DataSetText(int id, String name, boolean deleted, String category, String text) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.STRING, category));
		this.hashMap.put("text", new UniData(UniDataType.TEXT, text));

		// Name of the table in the data base
		sqlTabeName = "Texts";
	}

	/**
	 * Test, if this is equal to an other UniDataSet Only the names are compared
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	@Override
	public boolean isTheSameAs(UniDataSet uds) {
		if (!uds.getStringValueByKey("name").equalsIgnoreCase(this.getStringValueByKey("name")))
			return false;
		return true;
	}

}
