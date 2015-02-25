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

package com.sebulli.fakturama.export.csv.contacts;

import static com.sebulli.fakturama.export.Exporter.inQuotes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sebulli.fakturama.OSDependent;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetContact;
import com.sebulli.fakturama.misc.DataUtils;


/**
 * This class generates a list with all contacts
 * 
 * @author Gerd Bartelt
 */
public class Exporter {
	
	/**
	 * Constructor
	 * 
	 */
	public Exporter() {
		super();
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

		String NEW_LINE = OSDependent.getNewLine();
		
		// Create a File object
		File csvFile = new File(filename);
		BufferedWriter bos  = null;
		// Create a new file
		try {
			csvFile.createNewFile();
			bos = new BufferedWriter(new FileWriter(csvFile, false));

			bos.write(
					//T: Used as heading of a table. Keep the word short.
					"\""+ "id" + "\";"+ 
					//T: Used as heading of a table. Keep the word short.
					"\""+ "category" + "\";"+

					//T: Used as heading of a table. Keep the word short.
					"\""+ "gender" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "title" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "firstname" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "name" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "company" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "street" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "zip" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "city" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "country" + "\";"+
					
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_gender"  + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_title"  + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_firstname"  + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_name"  + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_company" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_street" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_zip" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_city" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "delivery_country" + "\";"+
					
					//T: Used as heading of a table. Keep the word short.
					"\""+ "account_holder" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "account" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "bank_code" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "bank_name" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "iban" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "bic" + "\";"+
					
					//T: Used as heading of a table. Keep the word short.
					"\""+ "nr" + "\";"+

					//T: Used as heading of a table. Keep the word short.
					"\""+ "note" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "date_added" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "payment" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "reliability" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "phone" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "fax" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "mobile" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "email" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "website" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "vatnr" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "vatnrvalid" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "discount" + "\""+
					NEW_LINE);
		
			// Get all undeleted contacts
			ArrayList<DataSetContact> contacts = Data.INSTANCE.getContacts().getActiveDatasets();
			
			// Export the product data
			for (DataSetContact contact : contacts) {
				
				
				// Place the products information into the table
				bos.write(
						contact.getStringValueByKey("id")+ ";" +
						inQuotes(contact.getStringValueByKey("category")) + ";" +
						
						inQuotes(DataSetContact.getGenderString(contact.getIntValueByKey("gender")))+ ";" +
						inQuotes(contact.getStringValueByKey("title"))+ ";" +
						inQuotes(contact.getStringValueByKey("firstname"))+ ";" +
						inQuotes(contact.getStringValueByKey("name"))+ ";" +
						inQuotes(contact.getStringValueByKey("company"))+ ";" +
						inQuotes(contact.getStringValueByKey("street"))+ ";" +
						inQuotes(contact.getStringValueByKey("zip"))+ ";" +
						inQuotes(contact.getStringValueByKey("city"))+ ";" +
						inQuotes(contact.getStringValueByKey("country"))+ ";" +

						inQuotes(DataSetContact.getGenderString(contact.getIntValueByKey("delivery_gender")))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_title"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_firstname"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_name"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_company"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_street"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_zip"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_city"))+ ";" +
						inQuotes(contact.getStringValueByKey("delivery_country"))+ ";" +

						
						inQuotes(contact.getStringValueByKey("account_holder"))+ ";" +
						inQuotes(contact.getStringValueByKey("account"))+ ";" +
						inQuotes(contact.getStringValueByKey("bank_code"))+ ";" +
						inQuotes(contact.getStringValueByKey("bank_name"))+ ";" +
						inQuotes( contact.getStringValueByKey("iban"))+ ";" +
						inQuotes(contact.getStringValueByKey("bic"))+ ";" +
						inQuotes(contact.getStringValueByKey("nr"))+ ";" +
						inQuotes(contact.getStringValueByKey("note"))+ ";" +
						inQuotes(contact.getStringValueByKey("date_added"))+ ";" +
						inQuotes(contact.getFormatedStringValueByKeyFromOtherTable("payment.PAYMENTS:description"))+ ";" +
						inQuotes(DataSetContact.getReliabilityString(contact.getIntValueByKey("reliability")))+ ";" +
						inQuotes(contact.getStringValueByKey("phone"))+ ";" +
						inQuotes(contact.getStringValueByKey("fax"))+ ";" +
						inQuotes(contact.getStringValueByKey("mobile"))+ ";" +
						inQuotes(contact.getStringValueByKey("email"))+ ";" +
						inQuotes(contact.getStringValueByKey("website"))+ ";" +
						inQuotes(contact.getStringValueByKey("vatnr"))+ ";" +
						contact.getStringValueByKey("vatnrvalid")+ ";" +
						inQuotes(DataUtils.DoubleToDecimalFormatedValue(contact.getDoubleValueByKey("discount"),"0.00")) +
						NEW_LINE);
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
