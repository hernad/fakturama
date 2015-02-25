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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.hsqldb.lib.StringUtil;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.logger.Logger;

/**
 * HSQLDB data base manager
 * 
 * @author Gerd Bartelt
 */
public class DataBase {

	// The plugin's preference store
	private IPreferenceStore preferences;

	private final static Integer DBVersion = 2;
	
	/**
	 * @uml.property  name="checkSize"
	 */
	private boolean checkSize = true;
	
	/**
	 * @uml.property  name="con"
	 */
	private Connection con = null;

	/**
	 * Convert a UniDataType to the Types for the data base
	 * 
	 * @param udt
	 * @return
	 */
	private String getDataBaseTypeByUniDataType(UniDataType udt) {
		// Depending on the UniDataSet type, add an data base type
		switch (udt) {
		case ID:
		case INT:
			return "INTEGER";

		case BOOLEAN:
			return "BOOLEAN";

		case DOUBLE:
		case QUANTITY:
		case PRICE:
		case PERCENT:
			return "DOUBLE";

		case DATE:
		case STRING:
			return "VARCHAR(256)";

		case TEXT:
			return "VARCHAR(32768)";

		default:
			Logger.logError("DataBase.java: Unknown UniDataType");
		}
		return "VARCHAR(32768)";
	}

	/**
	 * Generate the SQL string to create a new table in the data base
	 * 
	 * @param uds
	 *            UniDataSet as template for the new table
	 * @return
	 */
	private String getCreateSqlTableString(UniDataSet uds) {
		String s = "";

		// Generate a list with all keys in the template UniDataSet
		// and sort it alphabetically
		List<String> list = new ArrayList<String>();
		list.addAll(uds.getHashMap().keySet());
		Collections.sort(list);

		try {
			s = "id INT IDENTITY PRIMARY KEY";

			// Add all keys to the SQL string.
			// They are the columns of the new table
			// But do not add a column for "id" because the ID is the 
			// id of the data base entry
			for (String key : list) {

				if (!key.equalsIgnoreCase("id")) {

					// Separate the columns by an ","	
					s += ", " + key + " ";
					s += getDataBaseTypeByUniDataType(uds.hashMap.get(key).getUniDataType());
				}
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Error creating SQL String from dataset.");
		}

		// return the SQL string in brackets. 
		return uds.sqlTabeName + "(" + s + ")";
	}

	/**
	 * Generate the SQL string with all columns to insert a new row in the data
	 * base
	 * 
	 * @param uds
	 * @return
	 */
	private String getInsertSqlColumnsString(UniDataSet uds) {
		String s = "";

		// Generate a list with all keys in the template UniDataSet
		// and sort it alphabetically
		List<String> list = new ArrayList<String>();
		list.addAll(uds.getHashMap().keySet());
		Collections.sort(list);

		try {
			s = "id";

			// Get all UniDataSet keys and use them as columns headers
			for (String key : list) {
				if (!key.equalsIgnoreCase("id")) {
					s += ", " + key;
				}
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Error creating SQL columns string from dataset.");
		}

		// return the SQL string in brackets. 
		return "(" + s + ")";
	}

	/**
	 * Generate the SQL string to insert a new column in the data base It is a
	 * SQL string with placeholders
	 * 
	 * @param uds
	 * @return
	 */
	private String getInsertSqlColumnsStringWithPlaceholders(UniDataSet uds) {
		String s = "";

		// Generate a list with all keys in the template UniDataSet
		// and sort it alphabetically
		List<String> list = new ArrayList<String>();
		list.addAll(uds.getHashMap().keySet());
		Collections.sort(list);

		try {
			s = "?";

			// Get all UniDataSet keys 
			for (String key : list) {
				if (!key.equalsIgnoreCase("id")) {
					s += ", " + "?";
				}
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Error creating SQL columns string from dataset.");
		}

		// return the SQL string in brackets. 
		return "(" + s + ")";
	}

	/**
	 * Generate the SQL string to update data in the data base It is a SQL
	 * string with placeholders
	 * 
	 * @param uds
	 * @return
	 */
	private String getUpdateSqlValuesStringWithPlaceholders(UniDataSet uds) {
		String s = "";

		// Generate a list with all keys in the template UniDataSet
		// and sort it alphabetically
		List<String> list = new ArrayList<String>();
		list.addAll(uds.getHashMap().keySet());
		Collections.sort(list);

		try {
			// Get all UniDataSet keys 
			for (String key : list) {
				if (!key.equalsIgnoreCase("id")) {
					s += ", " + key + "=?";
				}
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Error creating SQL values string from dataset.");
		}

		// remove first ", "
		return s.substring(2);
	}

	/**
	 * Set an SQL parameter
	 * 
	 * @param prepStmt
	 *            Prepared Statement
	 * @param uds
	 *            UniDataSet
	 * @param useId
	 *            True, if the id is used
	 */
	private void setSqlParameters(PreparedStatement prepStmt, UniDataSet uds, boolean useId) {
		int i;

		// Generate a list with all keys in the template UniDataSet
		// and sort it alphabetically
		List<String> list = new ArrayList<String>();
		list.addAll(uds.getHashMap().keySet());
		Collections.sort(list);

		try {
			i = 1;

			// Set also the ID
			if (useId) {
				prepStmt.setInt(i, uds.getIntValueByKey("id"));
				i++;
			}

			// Set all other columns, depending on the data type
			for (String key : list) {
				if (!key.equalsIgnoreCase("id")) {
					UniDataType udt = uds.getUniDataTypeByKey(key);
					switch (udt) {
					case ID:
					case INT:
						prepStmt.setInt(i, uds.getIntValueByKey(key));
						break;
					case BOOLEAN:
						prepStmt.setBoolean(i, uds.getBooleanValueByKey(key));
						break;
					case PRICE:
					case PERCENT:
					case QUANTITY:
					case DOUBLE:
						prepStmt.setDouble(i, uds.getDoubleValueByKey(key));
						break;

					case DATE:
					case STRING:
					case TEXT:
						prepStmt.setString(i, uds.getStringValueByKey(key));
						break;

					default:
						Logger.logError("Unspecified Data");
						break;
					}
					i++;
				}
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Error creating SQL values string from dataset.");
		}
	}

	/**
	 * Insert a UniDataSet object in the data base
	 * 
	 * @param uds
	 *            UniDataSet to insert
	 */
	public void insertUniDataSet(UniDataSet uds) {
		String s;
		ResultSet rs;
		Statement stmt;
		PreparedStatement prepStmt;
		try {
			stmt = con.createStatement();

			s = "SELECT * FROM " + uds.sqlTabeName + " WHERE ID=" + uds.getStringValueByKey("id");
			rs = stmt.executeQuery(s);

			// test, if there is not an existing object with the same ID
			if (rs.next()) {
				Logger.logError("Dataset with this id is already in database" + uds.getStringValueByKey("name"));
			}
			else {

				// Generate the statement to insert a value and execute it
				s = "INSERT INTO " + uds.sqlTabeName + " " + getInsertSqlColumnsString(uds) + " VALUES" + getInsertSqlColumnsStringWithPlaceholders(uds);
				prepStmt = con.prepareStatement(s);
				setSqlParameters(prepStmt, uds, true);
				prepStmt.executeUpdate();
				prepStmt.close();

			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e) {
			Logger.logError(e, "Error saving dataset " + uds.getStringValueByKey("name"));
		}

	}

	/**
	 * Update a UniDataSet object in the database
	 * 
	 * @param uds
	 *            UniDataSet object to update
	 */
	public void updateUniDataSet(UniDataSet uds) {
		String s;
		PreparedStatement prepStmt;

		try {
			// Create the SQL statement to update the data and execute it.
			s = "UPDATE " + uds.sqlTabeName + " SET " + getUpdateSqlValuesStringWithPlaceholders(uds) + " WHERE ID=" + uds.getStringValueByKey("id");
			prepStmt = con.prepareStatement(s);
			setSqlParameters(prepStmt, uds, false);
			prepStmt.executeUpdate();
			prepStmt.close();
		}
		catch (SQLException e) {
			Logger.logError(e, "Error saving dataset " + uds.getStringValueByKey("name"));
		}
	}

	/**
	 * Creates a new dataset based on a template
	 * 
	 * @param udsTemplate
	 * @return
	 */
	private UniDataSet createDatasetByTemplate(UniDataSet udsTemplate) {
		UniDataSet uds = null;
		// Create a new temporary UniDataSet to store the data
		if (udsTemplate instanceof DataSetProduct)
			uds = new DataSetProduct();
		if (udsTemplate instanceof DataSetContact)
			uds = new DataSetContact();
		if (udsTemplate instanceof DataSetItem)
			uds = new DataSetItem();
		if (udsTemplate instanceof DataSetVAT)
			uds = new DataSetVAT();
		if (udsTemplate instanceof DataSetProperty)
			uds = new DataSetProperty();
		if (udsTemplate instanceof DataSetShipping)
			uds = new DataSetShipping();
		if (udsTemplate instanceof DataSetPayment)
			uds = new DataSetPayment();
		if (udsTemplate instanceof DataSetDocument)
			uds = new DataSetDocument();
		if (udsTemplate instanceof DataSetText)
			uds = new DataSetText();
		if (udsTemplate instanceof DataSetList)
			uds = new DataSetList();
		if (udsTemplate instanceof DataSetExpenditureVoucher)
			uds = new DataSetExpenditureVoucher();
		if (udsTemplate instanceof DataSetExpenditureVoucherItem)
			uds = new DataSetExpenditureVoucherItem();
		if (udsTemplate instanceof DataSetReceiptVoucher)
			uds = new DataSetReceiptVoucher();
		if (udsTemplate instanceof DataSetReceiptVoucherItem)
			uds = new DataSetReceiptVoucherItem();
		return uds;
	}
	
	/**
	 * Copy the data base table into a UniDataSet ArrayList
	 * 
	 * @param uniDataList
	 *            Copy the table to this ArrayList
	 * @param udsTemplate
	 *            Use this as template
	 */
	@SuppressWarnings("unchecked")
	public void getTable(@SuppressWarnings("rawtypes") ArrayList uniDataList, UniDataSet udsTemplate) {
		String s;
		String columnName;
		ResultSet rs;
		Statement stmt;
		UniDataSet uds = null;

		try {

			// read the data base table
			stmt = con.createStatement();
			s = "SELECT * FROM " + udsTemplate.sqlTabeName;
			rs = stmt.executeQuery(s);
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {

				uds = createDatasetByTemplate(udsTemplate);

				if (uds == null)
					Logger.logError("DataBase.getTable() Error: unknown UniDataSet Type");

				int id = -1;
				
				// Copy the table to the new UniDataSet
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					columnName = meta.getColumnName(i).toLowerCase();
					s = rs.getString(i);
					String dbt = getDataBaseTypeByUniDataType(uds.getUniDataTypeByKey(columnName));

					if (columnName.equals("id")) {
						try {
							id = Integer.parseInt(s);
						} catch (Exception e) {
						}
					}
					
					// Test Data length
					if (s!= null && checkSize) {
						if ( (dbt.equals("VARCHAR(256)") && (s.length() >= 256)) || 
								(dbt.equals("VARCHAR(32768)") && (s.length() >= 32768)) ){
							if (Workspace.showMessageBox(SWT.OK | SWT.CANCEL, "Warning", 
									"Dataset \"" + columnName + "\" is to long for " + dbt + ":\n"+ s) == SWT.CANCEL)
								checkSize = false;
						}
					}
					
					uds.setStringValueByKey(columnName, s);
				}

				// Add the new UniDataSet to the Array List
				if (id >= 0) {
					if (id == uniDataList.size()) {
						// Add it to the end
						uniDataList.add(id,uds);
					} else 	if (id < uniDataList.size()) { 
						// Set it at the specified position
						uniDataList.set(id,uds);
					} else if (id > uniDataList.size()) { 
						// Add it to the specified position and fill the empty ids up to this position also
						// with dummy datasets
						int udlsize = uniDataList.size();
						for (int i = udlsize; i < id ; i++) {
							UniDataSet dummyUds = createDatasetByTemplate(udsTemplate);
							//mark the dummy datasets as "deleted"
							if (dummyUds.containsKey("deleted"))
								dummyUds.setBooleanValueByKey("deleted", true);
							uniDataList.add(i,uds);
						}
						uniDataList.add(id,uds);
					}
				}
				else
					uniDataList.add(uds);
			}

			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Logger.logError(e, "Error reading database table " + udsTemplate.sqlTabeName);
		}

	}

	/**
	 * Check the table in the data base, if there is a column for each
	 * UniDataSet property. If not, create a new column.
	 * 
	 * @param uds
	 *            The UniDataSet to check. Defines also the table.
	 */
	private void checkTableAndInsertNewColumns(UniDataSet uds) {
		ResultSet rs = null;
		Statement stmt;
		ResultSetMetaData rsmd;
		int columns = 0;

		try {

			// Get the columns of the table, specified by the UniDataSet uds.
			stmt = con.createStatement();
			try {
				rs = stmt.executeQuery("SELECT * FROM " + uds.sqlTabeName);
			}
			catch (SQLException e) {

				// Create the table, if it does not exist
				stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(uds));
				rs = stmt.executeQuery("SELECT * FROM " + uds.sqlTabeName);
			}

			rsmd = rs.getMetaData();
			columns = rsmd.getColumnCount();

			// Generate a list with all keys in the template UniDataSet
			// and sort it alphabetically
			List<String> list = new ArrayList<String>();
			list.addAll(uds.getHashMap().keySet());
			Collections.sort(list);

			// Get all UniDataSet keys and test, if all columns exist
			for (String key : list) {

				// Do not test the ID column.
				if (!key.equalsIgnoreCase("id")) {

					String columnname = key;
					Boolean columnExists = false;

					// Search all column for the key.
					for (int i = 1; i <= columns; i++) {
						if (rsmd.getColumnName(i).equalsIgnoreCase(columnname)) {
							columnExists = true;
							
							// Get the Type
							String typeName = rsmd.getColumnTypeName(i);
							int size = rsmd.getPrecision(i);
							
							// Get the Type name and size
							if (typeName.equalsIgnoreCase("VARCHAR"))
								typeName += "(" + size + ")";
							
							// Get the type of the UniData
							String udsDbType = getDataBaseTypeByUniDataType(uds.hashMap.get(key).getUniDataType());
							
							// Check whether the type is correct
							if (!typeName.equalsIgnoreCase(udsDbType)) {
								String info = "Column Type not correct - will be changed ..: " + uds.sqlTabeName + ":" + columnname + ":" + typeName + " - " + udsDbType;
								Logger.logInfo(info);

								// Change the type of the column
								try {
									stmt.executeUpdate("ALTER TABLE " + uds.sqlTabeName + " ALTER COLUMN " + columnname + " " + udsDbType);
								}
								catch (SQLException e) {
									Logger.logError(e, "Error changing type of table column:" + columnname + " " + udsDbType);
								}
								
							}
						}
					}

					// Create a new column, if it does not exist yet.
					if (!columnExists) {
						String dType = getDataBaseTypeByUniDataType(uds.hashMap.get(key).getUniDataType());
						stmt.executeUpdate("ALTER TABLE " + uds.sqlTabeName + " ADD " + columnname + " " + dType);
						Logger.logInfo("New column " + columnname + " added in table " + uds.sqlTabeName + " - Data type: " + dType);
					}
				}
			}

			rs.close();

		}
		catch (SQLException e) {
			Logger.logError(e, "Error inserting a new table column.");
		}

	}

	
	/**
	 * Rename a table column
	 * 
	 * @param table
	 * 				The table that contains the column
	 * @param oldName
	 * 				The old name of the column
	 * @param newName
	 * 				The new name of the column
	 */
	private void renameColumn (String table, String oldName, String newName) {
		ResultSet rs = null;
		Statement stmt;
		ResultSetMetaData rsmd;
		int columns = 0;

		try {

			// Get the columns of the table.
			stmt = con.createStatement();
			try {
				rs = stmt.executeQuery("SELECT * FROM " + table);
				rsmd = rs.getMetaData();
				columns = rsmd.getColumnCount();

				int oldColumnIndex = -1;

				// Search all column for those with the "oldName"
				for (int i = 1; i <= columns; i++) {
					if (rsmd.getColumnName(i).equalsIgnoreCase(oldName)) {
						oldColumnIndex = i;
					}
				}
				
				//One with the oldName was found
				if (oldColumnIndex >= 0 ) {
					
					// Rename the column
					stmt = con.createStatement();
					String s = "ALTER TABLE " + table + 
					" ALTER COLUMN " + oldName + " RENAME TO " + newName ;
					stmt.executeUpdate(s);
					stmt.close();
					
					Logger.logInfo("Column: " + oldName + " in Table " + table + " renamed to: " + newName);

				}
			}
			catch (SQLException e) {
				Logger.logError(e, "Error renaming table column.");
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException e) {
			Logger.logError(e, "Error inserting a new table column.");
		}

		
	}
	
	/**
	 * Perfrom updates
	 * 
	 * @param version
	 * 			The version of the database
	 */
	private void performUpdates (int version) {

		// Rename some columns
		if (version < 2) {
			renameColumn("Documents", "payed", "paid");
			renameColumn("Payments", "payedtext", "paidtext");
			renameColumn("Payments", "unpayedtext", "unpaidtext");
			renameColumn("Payments", "defaultpayed", "defaultpaid");
		}
	}
	
	
	/**
	 * Check for updates and perform them
	 */
	private void check4Updates () {
		String s;
		ResultSet rs;
		Statement stmt;
		int version = 0;

		try {

			// read the data base table
			stmt = con.createStatement();
			s = "SELECT * FROM Properties WHERE name = 'version'";
			rs = stmt.executeQuery(s);

			// Get the version of the database
			if (rs.next()) {
				if (rs.getString("name").equals("version")) {
					version = rs.getInt("value");
				}
			}
			rs.close();
			stmt.close();
			
			// perform the Updates
			if (version > 0)
				performUpdates(version);
			
			// Update the Database Version
			if (version != DBVersion ) {
				try {
					stmt = con.createStatement();
					stmt.executeUpdate("UPDATE Properties SET value='" + DBVersion + "' WHERE name='version'");
					stmt.close();
				}
				catch (SQLException e) {
					Logger.logError(e, "Error saving dataset Properties:version");
				}
			}

		}
		catch (Exception e) {
			Logger.logError(e, "Error reading database table Properties" );
		}


	}
	
	/**
	 * Connect to the data base
	 * 
	 * @param workingDirectory
	 *            Working directory
	 * @return True, if the data base is connected
	 */
	public boolean connect(String workingDirectory) {
		String dataBaseName;
		ResultSet rs;
		String bundleVersion = Platform.getBundle("com.sebulli.fakturama").getHeaders().get("Bundle-Version").toString();
		preferences = Activator.getDefault().getPreferenceStore();

		// Get the JDBC driver
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		}
		catch (ClassNotFoundException e) {
			Logger.logError("Class org.hsqldb.jdbc.JDBCDriver not found");
			return false;
		}

		// The data base is in the /Database/ directory
		// If this folder doesn't exist - create it !
		String path = workingDirectory + "/Database/";
		File directory = new File(path);
		if (!directory.exists())
			directory.mkdirs();

		dataBaseName = path + "Database";
		String dataBaseHost = preferences.getString("DATABASE_HOST");
		try {
			// connect to the database
			if(!StringUtil.isEmpty(dataBaseHost)) {
				con = DriverManager.getConnection("jdbc:hsqldb:hsql://" + dataBaseHost + "/Fakturama", "sa", "");
			} else {
				con = DriverManager.getConnection("jdbc:hsqldb:file:" + dataBaseName + ";shutdown=true", "sa", "");
			}
			Statement stmt = con.createStatement();

			// Read the "Properties" table, to see, if it exists.
			// If not - it is a new data base.
			try {
				rs = stmt.executeQuery("SELECT * FROM Properties");
				rs.close();

				check4Updates();
				
				// Check all tables, if there is a column for each
				// UniDataSet property.
				checkTableAndInsertNewColumns(new DataSetProduct());
				checkTableAndInsertNewColumns(new DataSetContact());
				checkTableAndInsertNewColumns(new DataSetItem());
				checkTableAndInsertNewColumns(new DataSetVAT());
				checkTableAndInsertNewColumns(new DataSetShipping());
				checkTableAndInsertNewColumns(new DataSetPayment());
				checkTableAndInsertNewColumns(new DataSetText());
				checkTableAndInsertNewColumns(new DataSetDocument());
				checkTableAndInsertNewColumns(new DataSetList());
				checkTableAndInsertNewColumns(new DataSetExpenditureVoucher());
				checkTableAndInsertNewColumns(new DataSetExpenditureVoucherItem());
				checkTableAndInsertNewColumns(new DataSetReceiptVoucher());
				checkTableAndInsertNewColumns(new DataSetReceiptVoucherItem());

			}
			catch (SQLException e) {
				// In a new data base: create all the tables
				try {
					stmt.executeUpdate("CREATE TABLE Properties(Id INT IDENTITY PRIMARY KEY, Name VARCHAR (256), Value VARCHAR (32768) )");
					stmt.executeUpdate("INSERT INTO Properties VALUES(0,'version','" + DBVersion + "')");
					stmt.executeUpdate("INSERT INTO Properties VALUES(1,'bundleversion','" + bundleVersion + "')");
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetProduct()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetContact()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetItem()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetVAT()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetShipping()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetPayment()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetText()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetDocument()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetList()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetExpenditureVoucher()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetExpenditureVoucherItem()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetReceiptVoucher()));
					stmt.executeUpdate("CREATE TABLE " + getCreateSqlTableString(new DataSetReceiptVoucherItem()));
					stmt.close();
					return true;

				}
				catch (SQLException e2) {
					Logger.logError(e2, "Error creating new tables in database.");
				}
			}

			stmt.close();
		}
		catch (SQLException e) {
			Logger.logError(e, "Error connecting the Database:" + dataBaseName);
			if(dataBaseHost != null) {
				Logger.logError("Selected Database Host: " + dataBaseHost);
				
			}
		}
		return false;
	}

	/**
	 * Test, if the data base is connected
	 * 
	 * @return True, if connected
	 */
	public boolean isConnected() {
		return (con != null);
	}

	/**
	 * Close the data base
	 */
	public void close() {
		if (con != null)
			try {
				con.close();
			}
			catch (SQLException e) {
				Logger.logError(e, "Error closing the Database");
			}
	}

}
