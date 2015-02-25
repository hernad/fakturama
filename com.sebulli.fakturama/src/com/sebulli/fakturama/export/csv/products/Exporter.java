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

package com.sebulli.fakturama.export.csv.products;

import static com.sebulli.fakturama.export.Exporter.inQuotes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sebulli.fakturama.OSDependent;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetProduct;
import com.sebulli.fakturama.misc.DataUtils;


/**
 * This class generates a list with all products
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
		BufferedWriter bos = null;
		// Create a new file
		try {
			csvFile.createNewFile();
			bos = new BufferedWriter(new FileWriter(csvFile, false));
			bos.write(
					//T: Used as heading of a table. Keep the word short.
					"\""+ "id" + "\";"+ 
					//T: Used as heading of a table. Keep the word short.
					"\""+ "itemnr" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "name" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "category" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "description" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "price1" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "price2" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "price3" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "price4" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "price5" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "block1" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "block2" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "block3" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "block4" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "block5" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "vat" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "options" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "weight" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "unit" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "date_added" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "picturename" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "quantity" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "webshopid" + "\";"+
					//T: Used as heading of a table. Keep the word short.
					"\""+ "qunit" + "\""+
					NEW_LINE);

		
		
			// Get all undeleted products
			ArrayList<DataSetProduct> products = Data.INSTANCE.getProducts().getActiveDatasets();
			
			// Export the product data
			for (DataSetProduct product : products) {
				
				
				// Place the products information into the table
				bos.write(
						product.getStringValueByKey("id") + ";" +
						inQuotes(product.getStringValueByKey("itemnr")) + ";" +
						inQuotes(product.getStringValueByKey("name")) + ";" +
						inQuotes(product.getStringValueByKey("category")) + ";" +
						inQuotes(product.getStringValueByKey("description")) + ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("price1"),"0.000000")+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("price2"),"0.000000")+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("price3"),"0.000000")+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("price4"),"0.000000")+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("price5"),"0.000000")+ ";" +
						inQuotes(product.getStringValueByKey("block1"))+ ";" +
						inQuotes(product.getStringValueByKey("block2"))+ ";" +
						inQuotes(product.getStringValueByKey("block3"))+ ";" +
						inQuotes(product.getStringValueByKey("block4"))+ ";" +
						inQuotes(product.getStringValueByKey("block5"))+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKeyFromOtherTable("vatid.VATS:value"),"0.00")+ ";" +
						inQuotes(product.getStringValueByKey("options"))+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("weight"),"0.00")+ ";" +
						product.getStringValueByKey("unit")+ ";" +
						inQuotes(product.getStringValueByKey("date_added"))+ ";" +
						inQuotes(product.getStringValueByKey("picturename"))+ ";" +
						DataUtils.DoubleToDecimalFormatedValue(product.getDoubleValueByKey("quantity"),"0.00")+ ";" +
						product.getStringValueByKey("webshopid")+ "" +
						inQuotes(product.getStringValueByKey("qunit"))+ ";" +
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
