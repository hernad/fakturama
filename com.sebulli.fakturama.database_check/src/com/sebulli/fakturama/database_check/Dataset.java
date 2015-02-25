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
 * Dataset that represents one row in a table
 * 
 * @author Gerd Bartelt
 *
 */
public class Dataset {
	
	// Line number in the Database.script file
	private int lineNr = 0;
	// Name if the table
	private String tablename = "";
	// A list with all cells as String
	private List<String> data;

	/**
	 * Constructor
	 * Create a new row (list of strings)
	 */
	public Dataset() {
		data = new ArrayList<String>();
	}

	/**
	 * Getter for the line number
	 * 
	 * @return The line number of the Database.script file
	 */
	public int getLineNr() {
		return lineNr;
	}

	/**
	 * Setter for the line number
	 * @param lineNr The line number of the Database.script file
	 */
	public void setLineNr(int lineNr) {
		this.lineNr = lineNr;
	}
	
	/**
	 * Add a new cell 
	 * @param s Cell content as string
	 */
	public void addString (String s) {
		data.add(s);
	}

	/**
	 * Get the table name
	 * 
	 * @return Table name as string
	 */
	public String getTableName() {
		return tablename;
	}

	/**
	 * Set the name of the table 
	 * 
	 * @param tablename Table name as string
	 */
	public void setTableName(String tablename) {
		this.tablename = tablename;
	}
	
	/**
	 * Getter for the cell list
	 * 
	 * @return the cell list
	 */
	public List<String> getData() {
		return data;
	}

}
