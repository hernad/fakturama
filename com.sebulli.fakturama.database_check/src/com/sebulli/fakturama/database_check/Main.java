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

public class Main {

	/**
	 * Main program
	 * 
	 * @param args program arguments
	 */
	public static void main(String[] args) {
		
		String inputFilename = "";
		String outputFilename = "";
		
		// Show also warnings
		boolean showWarnings = false;
		
		// scramble data
		boolean crambleData = false;
		
		// Output of the scrambled data
		PrintWriter outputWriter = null;
		
		// analyze all program arguments
		for (String arg: args) {
			
			// Show also warnings with -w
			if (arg.equals("-w"))
				showWarnings = true;
			
			// Show also warnings with -w
			if (arg.equals("-x"))
				crambleData = true;
			
			// Set output file with "-oFilename"
			if (arg.startsWith("-o")) {
				outputFilename = arg.substring(2);
			}
			
			// Set input file with "-iFilename"
			if (arg.startsWith("-i")) {
				inputFilename = arg.substring(2);
			}
		}
		
		// Output program version
		Logger.getInstance().logText("Database checker version 1.1.5 - 2014-04-20");
		Logger.getInstance().logText("Gerd Bartelt - www.sebulli.com");
		
		// Configure logger
		Logger.getInstance().config(showWarnings);
		
		// Create the writer for the scrambled output file
		if (!outputFilename.isEmpty()) {
			try {
				outputWriter = new PrintWriter(outputFilename);
			} catch (FileNotFoundException e) {
			}
		}
		
		// Import the database
		Database database = new Database();
		Importer importer = new Importer(database, inputFilename, outputWriter);
		
		// Scramble and check the database for errors
		Checker checker = new Checker(database);
		if (importer.run()) {
			
			// Scramble the database
			if (crambleData) {
				Scrambler scrambler = new Scrambler(database, outputWriter);
				scrambler.run();
			}
			
			// Finally close the output file
			if (outputWriter != null) 
				outputWriter.close();
			
			checker.checkAll();
		}
		
		Logger.getInstance().logFinal();
	}

}
