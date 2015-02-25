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
import java.util.HashMap;
import java.util.List;

/**
 * Model to store the database table headers and table content
 * 
 * @author Gerd Bartelt
 * 
 */
public class Database {

	// Table headers
	public HashMap<String, TableHeader> tableHeaders;
	
	// Content of the tables
	public HashMap<String, Datatable> tableDataset;
	// Sorted Keys
	public List<String> dataTableKeys;
	
	/**
	 * Constructor Create an empty hash map with table headers and tables
	 */
	public Database() {
		tableHeaders = new HashMap<String, TableHeader>();
		tableDataset = new HashMap<String, Datatable>();
		dataTableKeys = new ArrayList<String>();
		
	}

	/**
	 * Add a new table header to the hash map of all table headers
	 * 
	 * @param tableheader
	 *            The new table header to add
	 */
	public void addTableHeader(TableHeader tableheader) {
		tableHeaders.put(tableheader.getName(), tableheader);
	}

	/**
	 * Add a new data set (table row) to the hash map of all datasets
	 * 
	 * @param dataset
	 *            The new data set to add
	 */
	public void addDataset(Dataset dataset) {
		String tableName = dataset.getTableName();
		
		if (!tableDataset.containsKey(tableName)) {
			tableDataset.put(tableName,
					new Datatable(tableName));
			// Add the table name also to the sorted list
			dataTableKeys.add(tableName);
		}
		tableDataset.get(tableName).getDatatable().add(dataset);
	}

	/**
	 * Get the index of a column by table name and column name
	 * @param tablename 
	 * @param columnname
	 * @return The index or -1 of column was not found
	 */
	public int getColumnIndexByName (String tablename, String columnname) {
		int columnNr = -1;

		// Scan all columns of the table header for this column with the
		// specified name
		// and then get the column-number
		for (int column = 0; column < this.tableHeaders.get(tablename).columns
				.size(); column++) {
			// Get the name of the table header
			TableColumn tablecolumn = this.tableHeaders.get(tablename).columns
					.get(column);
			// Is this the column we're looking for ?
			if (tablecolumn.getName().equals(columnname))
				columnNr = column;
		}
		
		return columnNr;
	}
	
	/**
	 * Get a table cell as String by data set and columnname
	 * 
	 * @param dataset
	 *            The data set
	 * @param columnname
	 *            The name of the column
	 * 
	 * @return The cell content as String
	 */
	public String getDataRaw(Dataset dataset, String columnname) {

		int columnNr = getColumnIndexByName(dataset.getTableName(), columnname);
		
		// If columns was found
		if (columnNr >= 0) {
			// Get the data cell data as String
			return dataset.getData().get(columnNr);
		} else {
			// If not: generate an error message
			Logger.getInstance().logError(
					"Column " + columnname + " not found in table "
							+ dataset.getTableName());
		}

		return "";
	}
	
	/**
	 * Get a table cell as String by data set and columnname
	 * Convert it to UTF-8
	 * @param dataset
	 *            The data set
	 * @param columnname
	 *            The name of the column
	 * 
	 * @return The cell content as String
	 */
	public String getData(Dataset dataset, String columnname) {

		String data = getDataRaw(dataset, columnname);

		// Convert it to UTF-8
		data = UnicodeEscape2UTF8.convertUnicodeEscape(data);
		return data;
	}

	/**
	 * Check whether a data set with a specified ID exists or not
	 * 
	 * @param tablename
	 *            Name of the table in which we search for the data set
	 * @param datasetid
	 *            ID of the datset
	 * @param ignoreMinus1
	 *            If true, a reference of "-1" = "no reference would be ok
	 * 
	 * @return true, if the data set exists
	 */
	public boolean existsData(String tablename, int datasetid,
			boolean ignoreMinus1) {

		// Get the data set with the ID
		Datatable table = this.tableDataset.get(tablename);

		// Check whether the ID exceeds the number of entries
		if (datasetid >= table.getDatatable().size()) {
			return false;
		}
		
		// Ignore references to -1 or not
		if ((datasetid == -1) && (!ignoreMinus1)) {
			return false;
		}
		
		// return a "found"
		return true;
	}

	/**
	 * Get the content of a table cell as string by table name, data set id and column name
	 * 
	 * @param tablename The name of the table with the data
	 * @param datasetid ID of the data set 
	 * @param columnname Name of the columns
	 * @param ignoreMinus1 true, if a reference to -1 should be ignored
	 * 
	 * @return The cell content as string
	 */
	public String getDataRaw(String tablename, int datasetid, String columnname,
			boolean ignoreMinus1) {
		
		// Get the table
		Datatable table = this.tableDataset.get(tablename);
		
		// A reference to -1 = no reference
		if (datasetid == -1) {
			if (!ignoreMinus1)
				Logger.getInstance().logError(
						"ID " + datasetid + " not found in table " + tablename);
			return "";
		}
		
		// Check whether the ID exceeds the number of entries
		if (datasetid >= table.getDatatable().size()) {
			Logger.getInstance().logError(
					"ID " + datasetid + " not found in table " + tablename);
			return "";
		}
		
		// Get the data set by ID
		Dataset dataset = table.getDatatable().get(datasetid);

		// get the number of the column by column name
		int columnNr = getColumnIndexByName(dataset.getTableName(), columnname);

		// was cell found ?
		if (columnNr >= 0) {
			// Return the data
			return dataset.getData().get(columnNr);
		} else {
			Logger.getInstance().logError(
					"Column " + columnname + " not found in table "
							+ dataset.getTableName());
		}

		return "";
	}
	
	/**
	 * Get the content of a table cell as string by table name, data set id and column name
	 * Convert it also to UTF8
	 * 
	 * @param tablename The name of the table with the data
	 * @param datasetid ID of the data set 
	 * @param columnname Name of the columns
	 * @param ignoreMinus1 true, if a reference to -1 should be ignored
	 * 
	 * @return The cell content as string
	 */
	public String getData(String tablename, int datasetid, String columnname,
			boolean ignoreMinus1) {
		
		String data = getDataRaw(tablename, datasetid, columnname, ignoreMinus1);

		// Convert it to UTF-8
		data = UnicodeEscape2UTF8.convertUnicodeEscape(data);
		return data;
	}
	

}
