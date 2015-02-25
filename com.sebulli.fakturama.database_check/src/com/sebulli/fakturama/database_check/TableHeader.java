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
 * A table header with a description of all columns of a table
 * 
 * @author Gerd Bartelt
 *
 */
public class TableHeader {

	// Table name
	String name;
	// All columns
	List<TableColumn> columns;
	// Next ID
	int nextId = 0;
	
	/**
	 * Constructor
	 * Create a new list of table columns
	 */
	public TableHeader() {
		columns = new ArrayList<TableColumn>();
	}
	
	/**
	 * Setter for the table name
	 * 
	 * @param name The table name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Getter for the table name
	 * 
	 * @return The table name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Add a new column
	 * 
	 * @param name Name of the column
	 * @param type Type of the data in the column
	 */
	public void addColumn(String name, String type) {
		columns.add(new TableColumn(name, type));
	}
	
	/**
	 * Set the next ID
	 * 
	 * @param nextId
	 */
	public void setNextId (int nextId) {
		this.nextId = nextId;
	}
}
