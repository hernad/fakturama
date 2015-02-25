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

package com.sebulli.fakturama.importer.csv.contacts;

import static com.sebulli.fakturama.Translate._;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;

import com.sebulli.fakturama.ApplicationWorkbenchAdvisor;
import com.sebulli.fakturama.OSDependent;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetContact;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.views.datasettable.ViewContactTable;

/**
 * CSV importer for contacts
 * 
 * @author Gerd Bartelt
 */
public class Importer {

	// Defines all columns that are used and imported
	private String[] requiredHeaders = { "category", "gender", "title", "firstname", "name", "company", "street", "zip", "city", "country",
			"delivery_gender", "delivery_title", "delivery_firstname", "delivery_name", "delivery_company",
			"delivery_street", "delivery_zip", "delivery_city", "delivery_country",
			"account_holder", "account", "bank_code", "bank_name", "iban", "bic",
			"nr", "note", "date_added", /* "payment",*/ "reliability",
			"phone", "fax", "mobile", "email", "website", "vatnr", "vatnrvalid", "discount" };

	// The result string
	String result = "";

	// NewLine
	String NL = "";

	/**
	 * Contstructor
	 */
	public Importer() {
		// String for a new line
		NL = OSDependent.getNewLine();
	}

	/**
	 * Returns, if a column is in the list of required columns
	 * 
	 * @param columnName
	 *            The name of the columns to test
	 * @return TRUE, if this column is in the list of required columns
	 */
	private boolean isRequiredColumn(String columnName) {

		// Test all columns
		for (int i = 0; i < requiredHeaders.length; i++) {
			if (columnName.equalsIgnoreCase(requiredHeaders[i]))
				return true;
		}
		return false;
	}


	/**
	 * The import procedure
	 * 
	 * @param fileName
	 *            Name of the file to import
	 * @param test
	 *            if true, the dataset are not imported (currently not used)
	 * @param updateExisting
	 *            if true, also existing entries will be updated
	 * @param importEmptyValues
	 *            if true, also empty values will be updated
	 */
	public void importCSV(final String fileName, boolean test,boolean updateExisting, boolean importEmptyValues) {

		// Result string
		//T: Importing + .. FILENAME
		result = _("Importing") + " " + fileName;

		// Count the imported contacts
		int importedContacts = 0;
		int updatedContacts = 0;

		// Count the line of the import file
		int lineNr = 0;


		// Open the existing file
		InputStreamReader isr;
		BufferedReader in = null;
		CSVReader csvr = null;

	
		try {
			isr = new InputStreamReader(new FileInputStream(fileName),"UTF-8");
			in = new BufferedReader(isr);
			csvr = new CSVReader(in, ';');
		}
		catch (UnsupportedEncodingException e) {
			Logger.logError(e, "Unsupported UTF-8 encoding");
			result += NL + "Unsupported UTF-8 encoding";
			return;
		}
		catch (FileNotFoundException e) {
			//T: Error message
			result += NL + _("File not found.");
			return;
		}

		String[] columns;

		// Read the first line
		try {
		
			// Read next CSV line
			columns = csvr.readNext();
			
			if (columns.length < 5) {
				//T: Error message
				result += NL + _("Error reading the first line");
				return;
			}
		}
		catch (IOException e1) {
			//T: Error message
			result += NL + _("Error reading the first line");
			return;
		}

		// Read the existing file and store it in a buffer
		// with a fix size. Only the newest lines are kept.
		try {

			// Read line by line
			String[] cells;
			while ((cells = csvr.readNext()) != null) {
				lineNr++;

				DataSetContact contact = new DataSetContact();
				Properties prop = new Properties();

				// Dispatch all the cells into a property
				for (int col = 0; col < cells.length; col++) {
					if (col < columns.length) {

						if (isRequiredColumn(columns[col])) {
							prop.setProperty(columns[col].toLowerCase(), cells[col]);
						}
					}
				}

				// Test, if all columns are used
				if ((prop.size() > 0) && (prop.size() != requiredHeaders.length)) {
					for (int i = 0; i < requiredHeaders.length; i++) {
						if (!prop.containsKey(requiredHeaders[i]))
							//T: Format: LINE: xx: NO DATA IN COLUMN yy FOUND.
							result += NL + _("Line") + ": " + Integer.toString(lineNr) + ": " + 
							//T: Format: LINE: xx: NO DATA IN COLUMN yy FOUND.
							_("No Data in Column") + " \"" + requiredHeaders[i] + "\" " + 
							//T: Format: LINE: xx: NO DATA IN COLUMN yy FOUND.
							_("found.");
					}
				}
				else {

					contact.setStringValueByKey("category", prop.getProperty("category"));
					contact.setIntValueByKey("gender", DataSetContact.getGenderID(prop.getProperty("gender")));

					contact.setStringValueByKey("title", prop.getProperty("title"));
					contact.setStringValueByKey("firstname", prop.getProperty("firstname"));
					contact.setStringValueByKey("name", prop.getProperty("name"));
					contact.setStringValueByKey("company", prop.getProperty("company"));
					contact.setStringValueByKey("street", prop.getProperty("street"));
					contact.setStringValueByKey("zip", prop.getProperty("zip"));
					contact.setStringValueByKey("city", prop.getProperty("city"));
					contact.setStringValueByKey("country", prop.getProperty("country"));

					contact.setIntValueByKey("delivery_gender", DataSetContact.getGenderID(prop.getProperty("delivery_gender")));
					contact.setStringValueByKey("delivery_title", prop.getProperty("delivery_title"));
					contact.setStringValueByKey("delivery_firstname", prop.getProperty("delivery_firstname"));
					contact.setStringValueByKey("delivery_name", prop.getProperty("delivery_name"));
					contact.setStringValueByKey("delivery_company", prop.getProperty("delivery_company"));
					contact.setStringValueByKey("delivery_street", prop.getProperty("delivery_street"));
					contact.setStringValueByKey("delivery_zip", prop.getProperty("delivery_zip"));
					contact.setStringValueByKey("delivery_city", prop.getProperty("delivery_city"));
					contact.setStringValueByKey("delivery_country", prop.getProperty("delivery_country"));

					contact.setStringValueByKey("account_holder", prop.getProperty("account_holder"));
					contact.setStringValueByKey("account", prop.getProperty("account"));
					contact.setStringValueByKey("bank_code", prop.getProperty("bank_code"));
					contact.setStringValueByKey("bank_name", prop.getProperty("bank_name"));
					contact.setStringValueByKey("iban", prop.getProperty("iban"));
					contact.setStringValueByKey("bic", prop.getProperty("bic"));
					
					contact.setStringValueByKey("nr", prop.getProperty("nr"));
					contact.setStringValueByKey("note", prop.getProperty("note"));
					
					if (prop.getProperty("date_added").isEmpty())
						contact.setStringValueByKey("date_added", DataUtils.DateAsISO8601String());
					else
						contact.setStringValueByKey("date_added", DataUtils.DateAsISO8601String(prop.getProperty("date_added")));
					
					//contact.setStringValueByKey("payment", prop.getProperty("payment"));
					contact.setIntValueByKey("reliability", DataSetContact.getReliabilityID(prop.getProperty("reliability")));

					contact.setStringValueByKey("phone", prop.getProperty("phone"));
					contact.setStringValueByKey("fax", prop.getProperty("fax"));
					contact.setStringValueByKey("mobile", prop.getProperty("mobile"));
					contact.setStringValueByKey("email", prop.getProperty("email"));
					contact.setStringValueByKey("website", prop.getProperty("website"));
					contact.setStringValueByKey("vatnr", prop.getProperty("vatnr"));
					contact.setStringValueByKey("vatnrvalid", prop.getProperty("vatnrvalid"));
					contact.setStringValueByKey("discount", prop.getProperty("discount"));
					
					
					// Add the product to the data base
					if (Data.INSTANCE.getContacts().isNew(contact)) {
						importedContacts++;
						Data.INSTANCE.getContacts().addNewDataSet(contact);
					}
					else if (updateExisting)
					{
						// Update data
						DataSetContact existingContact = Data.INSTANCE.getContacts().getExistingDataSet(contact);
						UniDataSet.copy(existingContact, contact, importEmptyValues);
						
						updatedContacts ++;
						
						// Update the modified product data
						Data.INSTANCE.getContacts().updateDataSet(existingContact);

					}
					
				}

			}
			
			// Refresh the views
			ApplicationWorkbenchAdvisor.refreshView(ViewContactTable.ID);
			
			// The result string
			//T: Message: xx Contacts have been imported 
			result += NL + Integer.toString(importedContacts) + " " + _("contacts have been imported.");
			if (updatedContacts > 0)
				result += NL + Integer.toString(updatedContacts) + " " + _("contacts have been updated.");

		}
		catch (IOException e) {
			//T: Error message
			result += NL + _("Error opening the file.");
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
				if (in != null) {
					in.close();
				}
				if (csvr != null) {
					csvr.close();
				}
			}
			catch (IOException e) {
				result += NL + _("Error closing a file.");
			}
		}
		

	}

	public String getResult() {
		return result;
	}

}
