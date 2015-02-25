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
 * UniDataSet for all properties
 * 
 * @author Gerd Bartelt
 */
public class DataSetProperty extends UniDataSet {

	/**
	 * Constructor Creates an new property
	 */
	public DataSetProperty() {
		this(-1, "", "");
	}

	/**
	 * Constructor Creates an new property
	 * 
	 * @param name
	 * @param value
	 */
	public DataSetProperty(String name, String value) {
		this(-1, name, value);
	}

	/**
	 * Constructor Creates an new property
	 * 
	 * @param id
	 * @param name
	 * @param value
	 */
	public DataSetProperty(int id, String name, String value) {
		this.hashMap.put("id", new UniData(UniDataType.INT, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("value", new UniData(UniDataType.TEXT, value));

		// Name of the table in the data base
		sqlTabeName = "Properties";
	}

}
