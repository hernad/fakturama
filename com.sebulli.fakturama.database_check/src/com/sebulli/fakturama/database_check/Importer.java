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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Reads the Database.script and import the data into the Database object
 * 
 * @author Gerd Bartelt
 *
 */
public class Importer {

	// Reference to the database
	Database database;
	// The line number in the Database.script file
	int lineNr = 0;
	// Filename to import
	String fileName = "Database.script";
	
	// Output of the scrambled data
	PrintWriter outputWriter = null;

	/**
	 * Constructor
	 * Set a reference to the database
	 * 
	 * @param database A reference to the database
	 */
	public Importer(Database database, String fileName, PrintWriter outputWriter ) {
		this.database = database;
		if (!fileName.isEmpty())
			this.fileName = fileName;
		
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Import the table header with a definition of all columns
	 * 
	 * @param line the line of the Database.script without "CREATE MEMORY TABLE PUBLIC."
	 */
	private void importTableHeader(String line) {
		
		// New TableHeader object
		TableHeader tableheader = new TableHeader();
		
		String name;
		String values_line;
		String nextId;
		
		// Get the content between "(" and ")"
		int pos = line.indexOf("(");
		name = line.substring(0, pos);
		values_line = line.substring(pos+1,line.length()-1);
		
		// Get the content between "IDENTITY(START WITH" and ")"
		// it's the 
		pos = line.indexOf("IDENTITY(START WITH");
		nextId = line.substring(pos + 20);
		pos = nextId.indexOf(")");
		nextId = nextId.substring(0,pos);
		
		// Get the property string of all columns
		String[] values = values_line.split(",");
		for (String value : values) {
			
			// Get the name and the type
			String[] value_parts = value.split(" ");
			tableheader.addColumn(value_parts[0], value_parts[1]);
		}
		
		// Add the new table header to the database
		tableheader.setNextId(Integer.parseInt(nextId));
		tableheader.setName(name);
		database.addTableHeader(tableheader);
		
	}
	
	/**
	 * Import a line with data
	 * 
	 * @param line  the line of the Database.script without "INSERT INTO"
	 */
	private void importData(String line) {
		
		// Create a new dataset
		Dataset dataset = new Dataset();
		
		boolean inQMark = false;
		char c, c_next;
		String name;
		String data="";
		String values_line;

		// Get the content between "VALUES(" and ")"
		int pos = line.indexOf(" VALUES(");
		name = line.substring(0, pos);
		values_line = line.substring(pos+8,line.length()-1);
		
		// Wo which table belongs this row ?
		dataset.setTableName(name);
		
		// Do not import empty datasets
		if (values_line.isEmpty()) {
			Logger.getInstance().logError("no data");
		} else {
			
			// An additional "," helps to import the data
			values_line += ",";
			
			// Read the line character by character
			for (int i = 0; i<values_line.length(); i++) {
				
				// Get this and the next character
				c = values_line.charAt(i);
				if (i <(values_line.length()-1))
					c_next = values_line.charAt(i+1);
				else
					c_next = '\0';
				
				// Escape a double '' in quotation marks as a single '
				if (c == '\'') {
					if (inQMark && (c_next == '\'')) {
						i++;
					} else {
						inQMark = !inQMark;
					}
				}
				
				// Get the character
				data += c;

				// A data cell is terminated by a ","
				if ((c==',') && !inQMark) {
					
					// remove the terminating ","
					data = data.substring(0, data.length()-1);
					// Add the cell to the dataset
					dataset.addString(data);
					data = "";
				}
			}
			
			// Add the dataset to the database
			dataset.setLineNr(lineNr);
			database.addDataset(dataset);
		}
		
	}
	
	/**
	 * Run the import script
	 * 
	 * @return true, if Database.script was read
	 */
	public boolean run () {
		lineNr = 0;
		
		boolean publicSchema = false;
		
		// Read the Database.script line by line
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			Logger.getInstance().logText("Reading database ...");
		    for(String line; (line = br.readLine()) != null; ) {
		    	
		    	// Get the line number
		    	lineNr ++;
		    	Logger.getInstance().setLineNr(lineNr);
		    	// Collect all lines
		    	Logger.getInstance().lines.add(line);
		    	
		    	// switch between public schema
		    	if (line.startsWith("SET SCHEMA ")) {
		    		publicSchema = false;
		    	}
		    	if (line.startsWith("SET SCHEMA PUBLIC")) {
		    		publicSchema = true;
		    	}
		    	
		    	// Import the table header
		    	if (line.startsWith("CREATE MEMORY TABLE PUBLIC.")) {
		    		importTableHeader(line.substring(27));
		    	}
		    	
		    	// Import the data
		    	if (publicSchema && line.startsWith("INSERT INTO ")) {
		    		importData(line.substring(12));
		    	}
		    	// Write the other line of the data base to the scrambled output file
		    	else if (outputWriter != null) {
		    		outputWriter.println(line);
		    	}
		    }
		} catch (FileNotFoundException e) {
			Logger.getInstance().logText("File not found: \"" + fileName + "\"");
			return false;
		} catch (IOException e) {
			Logger.getInstance().logText("IO Exception");
			return false;
		} finally {
			
		}
		return true;
	}
	
}
