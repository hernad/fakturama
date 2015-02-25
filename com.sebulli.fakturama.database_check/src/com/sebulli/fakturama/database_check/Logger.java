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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Log error or warnings to the console and to a log file
 * 
 * @author Gerd Bartelt
 */
public class Logger {
	
	// Singleton
	private static Logger instance = null;
	
	// The line number in Database.script
	private int lineNr = 0;
	// Count the errors
	private int errors;
	
	// Show also warnings
	private boolean showWarnings = false;
	
	// The log file for errros
	private PrintWriter out;
	// The log file for relationsships
	private PrintWriter rels;
	
	// All lines
	public List<String> lines;
	
	/**
	 * Constructor
	 * Open the log file
	 */
	private Logger() {
		errors = 0;
		try {
			out  = new PrintWriter("database_check.txt");
			rels = new PrintWriter("relationships.csv");
		} catch (FileNotFoundException e) {
		}
		
		lines = new ArrayList<String>();
	}
	
	/**
	 * Singleton mechanism
	 * @return
	 */
	public static Logger getInstance() {
		if (instance == null) {
			synchronized (Logger.class) {
				instance = new Logger();
			}
		}
		return instance;
	}

	/**
	 * Show a warning
	 * 
	 * @param showWarnings warning text
	 */
	public void config (boolean showWarnings)  {
		this.showWarnings = showWarnings;
	}
	
	/**
	 * Output a text to console and logfile
	 * @param line
	 */
	private void logLine (String line) {
		System.out.println(line);
		out.println(line);
	}
	
	/**
	 * Output an error text to error console and logfile
	 * @param line
	 */
	private void logErrorLine (String line) {
		System.out.println(line);
		out.println(line);
		
	}
	
	/**
	 * Output a text without line number
	 * @param text
	 */
	public void logText (String text) {
		logLine(text);
	}
	
	/**
	 * Log an error and count the error
	 * 
	 * @param error Text
	 */
	public void logError (String error) {
		logErrorLine("ERROR in " + lineNr +": " + error);
		int lineIndex = lineNr-1;
		if (lineIndex>=0 && lineIndex<lines.size() )
			logLine("Line:" + lines.get(lineIndex));
		errors ++;
	}
	
	/**
	 * Log a warning without counting it like an error
	 * 
	 * @param warning Text
	 */
	public void logWarning (String warning) {
		if (showWarnings) {
			logLine("WARNING in " + lineNr +": " + warning);
			int lineIndex = lineNr-1;
			if (lineIndex>=0 && lineIndex<lines.size() )
				logLine("Line:" + lines.get(lineIndex));
		}
		
	}
	
	/**
	 * removes semicolons and commas and limit string length
	 * 
	 * @param s
	 * @return
	 */
	private String cleanUpForCSV (String s) {
		s = s.replace(";", " ");
		s = s.replace(",", " ");
		if (s.length()>40)
			s = s.substring(0,40) + "...";
		
				
		return s;
	}
	/**
	 * Log a relationship between 2 table cells
	 * 
	 * @param warning Text
	 */
	public void logRelationship (String mainTableName, String mainName, String mainID,
			String otherTableName, String otherName, String otherID, boolean otherDeleted) {
		rels.print(cleanUpForCSV(mainTableName) + ";");
		rels.print(cleanUpForCSV(mainID) + ";");
		rels.print(cleanUpForCSV(mainName) + ";");
		rels.print(cleanUpForCSV(otherTableName) + ";");
		rels.print(cleanUpForCSV(otherID) + ";");
		rels.print(cleanUpForCSV(otherName) + ";");
		if (otherDeleted)
			rels.print("DELETED" + ";");
		else
			rels.print(";");
		rels.println();
	}
	
	/**
	 * Set the line number of the Database.script
	 * 
	 * @param lineNr
	 */
	public void setLineNr (int lineNr) {
		this.lineNr = lineNr;
	}
	
	/**
	 * Log the result and close the log file
	 */
	public void logFinal() {
		if (errors > 0) {
			logLine (errors + " errors found.");
		}
		else {
			logLine ("Database seems to be ok.");
		}
		
		out.close();
		rels.close();
	}
	

}
