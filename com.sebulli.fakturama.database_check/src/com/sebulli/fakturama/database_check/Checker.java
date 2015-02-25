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

import java.util.List;

/**
 * Analyses the database and checks for errors
 * 
 * @author Gerd Bartelt
 *
 */
public class Checker {

	// Reference to the data base
	Database database;
	// Number of entries with "NULL"
	int nullEntries = 0;
	
	/**
	 * Constructor.
	 * Set a reference to the data base
	 * 
	 * @param database reference to the data base
	 */
	public Checker(Database database) {
		this.database = database;
	}

	/**
	 * Checks whether all columns exist, whether the data type is correct and 
	 * count the entries with "NULL"
	 * 
	 */
	private void checkColumns() {
		
		// Info message
		Logger.getInstance().logText("Checking data types ...");
		
		// Scan all tables
		for (String key : database.tableDataset.keySet()) {
			
			// Get the table and a list with all datasets in this table
			Datatable table = database.tableDataset.get(key);
			List<Dataset> datasets = table.getDatatable();
			
			// Scan all datasets (rows)
			for (Dataset dataset : datasets) {
				
				// take the line number
				Logger.getInstance().setLineNr(dataset.getLineNr());
				
				// Check, if table header exists
				if (database.tableHeaders.containsKey(key)) {
					// Check count of columns
					if (database.tableHeaders.get(key).columns.size() != dataset.getData().size()) {
						
						// Wrong number of columns
						String name = database.getData(dataset, "NAME");
						Logger.getInstance().logError("Dataset " + key + "(" + name + ") has " + dataset.getData().size() 
								+ " columns, expected: " + database.tableHeaders.get(key).columns.size());
					}
				} else {
					Logger.getInstance().logError("There is no table defined:" + key);

				}
				
				// Scan all columns of the dataset
				for (int column=0; column < dataset.getData().size(); column++) {
					String data = dataset.getData().get(column);
					
					// Convert it to UTF-8
					data = UnicodeEscape2UTF8.convertUnicodeEscape(data);
					
					// Is this a valid column ?
					if (database.tableHeaders.containsKey(key)) {
						if (column < database.tableHeaders.get(key).columns.size()) {
							
							// Get the column information with column data type
							TableColumn tablecolumn = database.tableHeaders.get(key).columns.get(column);
							Columntype type = tablecolumn.getType();
							
							// Check whether the data is from the correct type
							if (!data.equals("NULL")) {
								switch (type) {
								
								case NONE:
									Logger.getInstance().logError("NONE is not a valid type.");
								break;
								
								case BOOLEAN:
									if (!data.equals("TRUE") && !data.equals("FALSE"))
										Logger.getInstance().logError("BOOLEAN must be TRUE or FALSE but is:" + data + " Column name:" + tablecolumn.getName());
								break;

								case INTEGER:
									try {
										Integer.parseInt(data);
									} catch (Exception e) {
										Logger.getInstance().logError("Not an INTEGER value:" + data + " Column name:" + tablecolumn.getName());
									}
								break;
								
								case DOUBLE:
									try {
										Double.parseDouble(data);
									} catch (Exception e) {
										Logger.getInstance().logError("Not an DOUBLE value:" + data + " Column name:" + tablecolumn.getName());
									}
								break;
								
								case VARCHAR_256:
									if (!data.startsWith("'"))
										Logger.getInstance().logError("STRING value must start with a ':" + data + " Column name:" + tablecolumn.getName());
									else if (!data.endsWith("'"))
										Logger.getInstance().logError("STRING value must end with a ':" + data + " Column name:" + tablecolumn.getName());
									else if (data.length() > (256+2))
										Logger.getInstance().logError("STRING value must nut be longer than 256 characters:" + data + " Column name:" + tablecolumn.getName());
								break;

								case VARCHAR_32768:
								case VARCHAR_60000:
									if (!data.startsWith("'"))
										Logger.getInstance().logError("STRING value must start with a ':" + data + " Column name:" + tablecolumn.getName());
									else if (!data.endsWith("'"))
										Logger.getInstance().logError("STRING value must end with a ':" + data + " Column name:" + tablecolumn.getName());
									else if (data.length() > (32768+2))
										Logger.getInstance().logError("STRING value must nut be longer than 32768 characters:" + data + " Column name:" + tablecolumn.getName());
								break;
								
								}
								
							} else {
								nullEntries++;
							}
						}
						
					}
				}
			}
		}
		Logger.getInstance().logText("Entries with NULL:" + nullEntries);
	}

	/**
	 * Check the IDs of the datasets
	 */
	private void checkIDs() {
		// Info message
		Logger.getInstance().logText("Checking IDs ...");
		
		// Scan all tables
		for (String key : database.tableDataset.keySet()) {
			
			// Get the table and a list with all datasets in this table
			Datatable table = database.tableDataset.get(key);
			List<Dataset> datasets = table.getDatatable();
			int datasetNr = 0;
			
			// Scan all datasets (rows)
			for (Dataset dataset : datasets) {
				
				// set the line number
				Logger.getInstance().setLineNr(dataset.getLineNr());
				
				// Check, whether the id of the dataset is the same
				// as it's index
				// Column 0 is the columns with the ID
				int id = Integer.parseInt(dataset.getData().get(0));
				if (id != datasetNr) {
					Logger.getInstance().logError("Missing ID in dataset: " + dataset.getTableName());
					datasetNr = id;
				}
				datasetNr++;
			}
		}
	}

	/**
	 * Check whether the relationship between 2 datasets is correct
	 * 
	 * @param mainTable Table of the main dataset that refers to another
	 * @param columnname Column name of the main dataset that refers to another
	 * @param otherTable Table of the other table
	 * @param ignoreMinus1 True, if a reference to "-1" is acceptable.
	 * @param ignoreDeleted True, if a reference to a deleted dataset is acceptable
	 */
	private void checkRelationship(String mainTable, String columnname, String otherTable, boolean ignoreMinus1, boolean ignoreDeleted) {
		
		// Log message
		Logger.getInstance().logText("Checking relationship " + mainTable + "(" + columnname + ") -> " + otherTable);
		
		// set, of a dataset is marked as "deleted"
		boolean mainDeleted = false;
		boolean otherDeleted = false;
		String mainName = "";
		String otherName = "";
		String mainID = "";
		String otherID = "";
		
		// Get the main table and the datasets
		if (!database.tableDataset.containsKey(mainTable)) {
			return;
		}
		
		Datatable table = database.tableDataset.get(mainTable);
		List<Dataset> datasets = table.getDatatable();
		
		// Scan all datasets
		for (Dataset dataset : datasets) {
			
			// Set line number
			Logger.getInstance().setLineNr(dataset.getLineNr());
			
			// Get content of the cell
			String data = database.getData(dataset, columnname);
			mainName = database.getData(dataset, "NAME");
			mainID = database.getData(dataset, "ID");
			mainDeleted = database.getData(dataset, "DELETED").equals("TRUE");

			// If not an empty cell
			if (!data.equals("''")) {
				
				// Remove trailing and leading quotation marks
				data = data.replace('\'', ' ');
				data = data.trim();
				
				// Split the string if there are more than one ID
				String[] ids_s = data.split(",");
				
				// Scan all IDs
				for (String id_s:ids_s) {
					
					// Check whether the referencing dataset is marked as "deleted"
					data = database.getData(dataset, columnname);

					// Get the ID as integer
					int id = Integer.parseInt(id_s);
					
					// Check whether the other database exists
					if (database.existsData(otherTable, id, ignoreMinus1)) {
						
						// other exists, but is marked as "deleted"
						otherDeleted = database.getData(otherTable, id, "DELETED", ignoreMinus1).equals("TRUE");
						otherName = database.getData(otherTable, id, "NAME", true);
						otherID = database.getData(otherTable, id, "ID", true);
						
						// Log the relationship between both cells in an extra log file
						if (!mainDeleted && !ignoreDeleted) {
							Logger.getInstance().logRelationship(
									mainTable, mainName, mainID, 
									otherTable, otherName ,otherID, otherDeleted);
						}
						
						if (otherDeleted && !mainDeleted && !ignoreDeleted)
							Logger.getInstance().logWarning("Dataset " + otherTable + "(" + database.getData(otherTable, id, "NAME", ignoreMinus1) + ") is deleted, but "+ dataset.getTableName() + "(" + mainName + ") refers to it.");
					} else {
						Logger.getInstance().logError("ID " + id + " not found in table " + otherTable + ", but there is a reference from " + mainTable + "("+ mainName +")");
					}
				}
			}
		}
	}
	
	/**
	 * Check the relationship between different datasets
	 */
	private void checkRelationships() {
		Logger.getInstance().logText("Checking relationships ...");
		checkRelationship("PRODUCTS","VATID","VATS",false, false);
		checkRelationship("CONTACTS","PAYMENT","PAYMENTS",false, false);
		checkRelationship("DOCUMENTS","ADDRESSID","CONTACTS",true, false);
		checkRelationship("DOCUMENTS","PAYMENTID","PAYMENTS",true, false);
		checkRelationship("DOCUMENTS","SHIPPINGID","SHIPPINGS",false, false);
		checkRelationship("DOCUMENTS","INVOICEID","DOCUMENTS",true, false);
		checkRelationship("ITEMS","PRODUCTID","PRODUCTS",true, false);
		checkRelationship("ITEMS","OWNER","DOCUMENTS",false, true);
		checkRelationship("ITEMS","VATID","VATS",false, false);		
		checkRelationship("SHIPPINGS","VATID","VATS",false, false);	
		checkRelationship("EXPENDITUREITEMS","VATID","VATS",false, false);			
		checkRelationship("RECEIPTVOUCHERITEMS","VATID","VATS",false, false);	
		checkRelationship("DOCUMENTS","ITEMS","ITEMS",false, false);	
		
		
	}
	
	/**
	 * Start all test
	 */
	public void checkAll() {
		checkColumns();
		checkIDs();
		checkRelationships();
	}
}
