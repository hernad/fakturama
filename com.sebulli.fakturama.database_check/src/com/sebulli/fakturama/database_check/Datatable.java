/* 
 * Fakturama - database checker - http://fakturama.sebulli.com
 * 
 * Copyright (C) 2014 Gerd Bartelt
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Bartelt - initial API and implementation
 */
package com.sebulli.fakturama.database_check;

import java.util.ArrayList;
import java.util.List;

/**
 * Data table that contains a list with all datasets 
 * 
 * @author Gerd Bartelt
 *
 */
public class Datatable {
	
	// List with all datasets
	private List<Dataset> datatable;
	
	// Table name
	private String name = "";
	
	/**
	 * Constructor
	 * Create a new list and set the table name
	 * 
	 * @param name The table name
	 */
	public Datatable (String name) {
		this.name = name;
		datatable = new ArrayList<Dataset>();
	}

	/**
	 * Getter with the list of all datasets
	 * 
	 * @return list of all datasets
	 */
	public List<Dataset> getDatatable() {
		return datatable;
	}

	/**
	 * Get the table name
	 * 
	 * @return The table name
	 */
	public String getName () {
		return name;
	}
}
