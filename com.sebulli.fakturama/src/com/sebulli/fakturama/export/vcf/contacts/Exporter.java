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

package com.sebulli.fakturama.export.vcf.contacts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sebulli.fakturama.OSDependent;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetContact;


/**
 * This class generates a list with all contacts
 * 
 * @author Gerd Bartelt
 */
public class Exporter {
	
	// Buffered writer for the output stream
	private BufferedWriter bos  = null;
	
	// Constant for a OS dependent new line
	private String NEW_LINE;
	
	/**
	 * Constructor
	 * 
	 */
	public Exporter() {
		super();
		NEW_LINE = OSDependent.getNewLine();
	}

	/**
	 * Convert the special characters in a vcard string
	 * and add a backslash \
	 * 
	 * @param 
	 * 			s The String to convert
	 * @return
	 * 			The converted string
	 */
	private String encodeVCardString(String s) {
		s = s.replace("\n", "\\n");
		s = s.replace(":", "\\:");
		s = s.replace(",", "\\,");
		s = s.replace(";", "\\;");
		return s;
	}
	
	/**
	 * Write a property and one attribute
	 * 
	 * @param property
	 * 			The property to write
	 * @param s
	 * 			The 1st attribute
	 */
	private void writeVCard(String property, String s) {
		writeVCard(property, s, null);
	}

	/**
	 * Write a property and two attributes
	 * 
	 * @param property
	 * 			The property to write
	 * @param s1
	 * 			The 1st attribute
	 * @param s2
	 * 			The 2nd attribute
	 */
	private void writeVCard(String property, String s1, String s2) {
		writeVCard(property, s1, s2, null, null, null, null, null);
	}
	
	/**
	 * Write an attribute, if it is not empty and add 
	 * a semicolon between two attributes.
	 * 
	 * @param s
	 * 			The attribute to write
	 * @param first
	 * 			True, if it is the first attribute
	 * 
	 */
	private void writeAttribute(String s, boolean first) {

		// Exit, if the attribute is null
		if (s == null)
			return;
		
		// Write the attribute and add a semicolon before all
		// attributes, except the first one.
		try {
			if (!first)
				bos.write(";");
			bos.write(encodeVCardString(s));
		}
		catch (IOException e) {}

	}

	/**
	 * Write a property and 7 attributes
	 * 
	 * @param property
	 * 			The property to write
	 * @param s1
	 * 			The 1st attribute
	 * @param s2
	 * 			The 2nd attribute
	 * @param s3
	 * 			The 3rd attribute
	 * @param s4
	 * 			The 4th attribute
	 * @param s5
	 * 			The 5th attribute
	 * @param s6
	 * 			The 6th attribute
	 * @param s7
	 * 			The 7th attribute
	 */
	private void writeVCard(String property, String s1, String s2, 
			String s3, String s4, String s5, String s6, String s7) {
		
		// Set this flag, if at least one attribute is not empty
		boolean hasInformation = false;
		
		// Test all attributes and set the flag, if one is not empty
		if (s1 != null) 
			if (!s1.isEmpty())
				hasInformation = true;
		if (s2 != null) 
			if (!s2.isEmpty())
				hasInformation = true;
		if (s3 != null) 
			if (!s3.isEmpty())
				hasInformation = true;
		if (s4 != null) 
			if (!s4.isEmpty())
				hasInformation = true;
		if (s5 != null) 
			if (!s5.isEmpty())
				hasInformation = true;
		if (s6 != null) 
			if (!s6.isEmpty())
				hasInformation = true;
		if (s7 != null) 
			if (!s7.isEmpty())
				hasInformation = true;

		// Exit, if all attributes are empty 
		if (!hasInformation)
			return;
		
		// Write the property and all attributes
		try {
			bos.write(property);
			writeAttribute(s1, true);
			writeAttribute(s2, false);
			writeAttribute(s3, false);
			writeAttribute(s4, false);
			writeAttribute(s5, false);
			writeAttribute(s6, false);
			writeAttribute(s7, false);
			bos.write(NEW_LINE);
		}
		catch (IOException e) {
		}
	}
	
	/**
	 * 	Do the export job.
	 * 
	 * @param filename
	 * 			The name of the export file
	 * @return
	 * 			True, if the export was successful
	 */
	public boolean export(String filename) {

		
		// Create a File object
		File csvFile = new File(filename);
		
		// Create a new file
		try {
			csvFile.createNewFile();
			bos = new BufferedWriter(new FileWriter(csvFile, false));
			// Get all undeleted contacts
			ArrayList<DataSetContact> contacts = Data.INSTANCE.getContacts().getActiveDatasets();
			
			// Export the product data
			for (DataSetContact contact : contacts) {
				
				// Export one VCARD
				writeVCard("BEGIN:","VCARD");
				writeVCard("VERSION:","3.0");
				writeVCard("N:", contact.getStringValueByKey("name"),
						contact.getStringValueByKey("firstname"));
				writeVCard("FN:", contact.getNameWithCompany(false));
				writeVCard("ADR;TYPE=home:",
						"",
						contact.getStringValueByKey("company"),
						contact.getStringValueByKey("street"),
						contact.getStringValueByKey("city"),
						"",
						contact.getStringValueByKey("zip"),
						contact.getStringValueByKey("country")
						);
				writeVCard("ADR;TYPE=postal:",
						"",
						contact.getStringValueByKey("delivery_company"),
						contact.getStringValueByKey("delivery_street"),
						contact.getStringValueByKey("delivery_city"),
						"",
						contact.getStringValueByKey("delivery_zip"),
						contact.getStringValueByKey("delivery_country")
						);
				
				writeVCard("ADR;TYPE=other:",
						contact.getNameWithCompany(true),
						contact.getStringValueByKey("delivery_company"),
						contact.getStringValueByKey("delivery_street"),
						contact.getStringValueByKey("delivery_city"),
						"",
						contact.getStringValueByKey("delivery_zip"),
						contact.getStringValueByKey("delivery_country")
						);
				
				writeVCard("TEL;TYPE=HOME,WORK,VOICE:",contact.getStringValueByKey("phone"));
				writeVCard("TEL;TYPE=HOME,WORK,FAX:",contact.getStringValueByKey("fax"));
				writeVCard("TEL;TYPE=HOME,WORK,CELL:",contact.getStringValueByKey("mobile"));
				writeVCard("EMAIL;TYPE=internet:",contact.getStringValueByKey("email"));
				writeVCard("URL:",contact.getStringValueByKey("website"));

				writeVCard("NOTE:",contact.getStringValueByKey("note"));
				writeVCard("CATEGORIES:",contact.getStringValueByKey("category"));
				
				writeVCard("END:","VCARD");

			}
		
		}
		catch (IOException e) {
			return false;
		}

		try {
			if (bos!= null)
				bos.close();
		}
		catch (Exception e) {}

		// True = Export was successful
		return true;
	}

}
