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

package com.sebulli.fakturama.export.products;

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetProduct;
import com.sebulli.fakturama.export.OOCalcExporter;


/**
 * This class generates a list with all products
 * 
 * @author Gerd Bartelt
 */
public class Exporter extends OOCalcExporter{
	
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
	 * @return
	 * 			True, if the export was successful
	 */
	public boolean export() {

		// Try to generate a spreadsheet
		if (!createSpreadSheet())
			return false;

		// Get all undeleted products
		ArrayList<DataSetProduct> products = Data.INSTANCE.getProducts().getActiveDatasets();

		// Counter for the current row and columns in the Calc document
		int row = 0;
		int col = 0;

		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, "ID");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Item Number"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Name"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Category"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Description"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Price")+ "(1)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Price")+ "(2)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Price")+ "(3)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Price")+ "(4)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Price")+ "(5)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity")+ "(1)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity")+ "(2)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity")+ "(3)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity")+ "(4)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity")+ "(5)");
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("VAT"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Options"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Weight (kg)"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Unit"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Date"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Product Picture"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Web Shop"));
		
		
		// Draw a horizontal line
		for (col = 0; col < 23; col++) {
			setBorder(row, col, 0x000000, false, false, true, false);
		}
		row++;
		
		// Export the product data
		for (DataSetProduct product : products) {
			
			col = 0;
			
			// Place the products information into the table
			setCellText(row, col++, product.getFormatedStringValueByKey("id"));
			setCellText(row, col++, product.getFormatedStringValueByKey("itemnr"));
			setCellText(row, col++, product.getFormatedStringValueByKey("name"));
			setCellText(row, col++, product.getFormatedStringValueByKey("category"));
			setCellText(row, col++, product.getFormatedStringValueByKey("description"));
			setCellValueAsLocalCurrency(row, col++, product.getDoubleValueByKey("price1"));
			setCellValueAsLocalCurrency(row, col++, product.getDoubleValueByKey("price2"));
			setCellValueAsLocalCurrency(row, col++, product.getDoubleValueByKey("price3"));
			setCellValueAsLocalCurrency(row, col++, product.getDoubleValueByKey("price4"));
			setCellValueAsLocalCurrency(row, col++, product.getDoubleValueByKey("price5"));
			setCellText(row, col++, product.getFormatedStringValueByKey("block1"));
			setCellText(row, col++, product.getFormatedStringValueByKey("block2"));
			setCellText(row, col++, product.getFormatedStringValueByKey("block3"));
			setCellText(row, col++, product.getFormatedStringValueByKey("block4"));
			setCellText(row, col++, product.getFormatedStringValueByKey("block5"));
			setCellText(row, col++, product.getFormatedStringValueByKeyFromOtherTable("vatid.VATS:value"));
			setCellText(row, col++, product.getFormatedStringValueByKey("options"));
			setCellText(row, col++, product.getFormatedStringValueByKey("weight"));
			setCellText(row, col++, product.getFormatedStringValueByKey("unit"));
			setCellText(row, col++, product.getFormatedStringValueByKey("date_added"));
			setCellText(row, col++, product.getFormatedStringValueByKey("picturename"));
			setCellText(row, col++, product.getFormatedStringValueByKey("quantity"));
			setCellText(row, col++, product.getFormatedStringValueByKey("webshopid"));
			
			// Alternate the background color
			//if ((row % 2) == 0)
			//	setBackgroundColor( 0, row, col-1, row, 0x00e8ebed);

			row++;
		}

		// True = Export was successful
		return true;
	}

}
