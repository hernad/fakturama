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

package com.sebulli.fakturama.export.buyers;

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.data.DataSetItem;
import com.sebulli.fakturama.data.UniDataSetSorter;
import com.sebulli.fakturama.export.BuyersAndTotal;
import com.sebulli.fakturama.export.OOCalcExporter;
import com.sebulli.fakturama.export.TotalSoldAndQuantity;
import com.sebulli.fakturama.logger.Logger;


/**
 * This class generates a list with all items and
 * all the buyers 
 * 
 * @author Gerd Bartelt
 */
public class Exporter extends OOCalcExporter{

	// List with all buyers
	private BuyersAndTotal buyersAndTotal = new BuyersAndTotal();
	//private Map<String, TotalSoldAndQuantity> buyers = new HashMap<String, TotalSoldAndQuantity>();

	
	/**
	 * Comparator to sort the List of buyers by volume or by quantity
	 * 
	 * @author Gerd Bartelt
	 */
	private class BuyersTotalSoldComparer implements Comparator<Object> {
		
		// List with all buyers
		private Map<String, TotalSoldAndQuantity>  data = null;

		/**
		 * Constructor
		 * 
		 * @param data 
		 * 			The data to sort
		 */
		public BuyersTotalSoldComparer (Map<String, TotalSoldAndQuantity> data){
			super();
			this.data = data;
		}

		/**
		 * Compare two objects by quantity or by volume
		 *
		 * @param o1
		 * 			The first object
		 * @param o2
		 * 			The second object
		 */
         public int compare(Object o1, Object o2) {
        	 int result;
        	 
           	 Double e1 = this.data.get(o1).getTotalSold();
           	 Double e2 = this.data.get(o2).getTotalSold();
           	 result = e2.compareTo(e1);
        	 
        	 // Two items must not be equal. If they were, they would be
        	 // replaces in the map
        	 if (result == 0)
        		 result = 1;

        	 return result;
         }
	}
	
	
	/**
	 * Constructor Sets the begin and end date
	 * 
	 * @param startDate
	 *            Begin date
	 * @param endDate
	 *            Begin date
	 */
	public Exporter(GregorianCalendar startDate, GregorianCalendar endDate, boolean doNotUseTimePeriod) {
		super(startDate, endDate,  doNotUseTimePeriod);
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
		usePaidDate = Activator.getDefault().getPreferenceStore().getBoolean("EXPORTSALES_PAIDDATE");
		
		
		// Use pay date or document date
		if (usePaidDate)
			documentDateKey = "paydate";
		else
			documentDateKey = "date";

		// Get all undeleted documents
		ArrayList<DataSetDocument> documents = Data.INSTANCE.getDocuments().getActiveDatasets();

		// Sort the documents by the pay date
		Collections.sort(documents, new UniDataSetSorter(documentDateKey));

		//T: Title of the exported table
		setCellTextInBold(0, 0, _("List of all buyers"));

		// Fill the first 4 rows with the company information
		fillCompanyInformation(2);
		fillTimeIntervall(7);

		// Counter for the current row and columns in the Calc document
		int row = 11;
		int col = 0;

		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Buyers"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Volume"));

		// Draw a horizontal line
		for (col = 0; col < 2; col++) {
			setBorder(row, col, 0x000000, false, false, true, false);
		}
		row++;
		
		// Export the document data
		for (DataSetDocument document : documents) {

			if (documentShouldBeExported(document)) {
				
				// Get all items by ID from the item string
				String itemsString = document.getStringValueByKey("items");
				String[] itemsStringParts = itemsString.split(",");

				// Get the name of the buyer
				String buyerName = document.getStringValueByKey("addressfirstline");
				
				// Parse the item string ..
				for (String itemsStringPart : itemsStringParts) {
					int id;
					if (itemsStringPart.length() > 0) {
						try {
							id = Integer.parseInt(itemsStringPart);
						}
						catch (NumberFormatException e) {
							Logger.logError(e, "Error parsing item string");
							id = 0;
						}

						// And copy the item to a new one
						DataSetItem item = Data.INSTANCE.getItems().getDatasetById(id);
						
						buyersAndTotal.add(buyerName, item);
					}
				}
			}
		}

		
		SortedMap<String, TotalSoldAndQuantity> sortedBuyers = new TreeMap<String, TotalSoldAndQuantity>(new BuyersTotalSoldComparer(buyersAndTotal.getBuyers()));
		sortedBuyers.putAll(buyersAndTotal.getBuyers());

		// Get through the list of all buyers
		for (Iterator<?> iteratorBuyer = sortedBuyers.entrySet().iterator(); iteratorBuyer.hasNext(); ) {

			// Get the next buyer
			@SuppressWarnings("unchecked")
			Entry<String, TotalSoldAndQuantity> buyer = (Entry<String, TotalSoldAndQuantity>)iteratorBuyer.next();
			col = 0;
			
			// Place the buyer's name, the quantity and the volume into
			// the next columns
			setCellText(row, col++, buyer.getKey());
			setCellValueAsLocalCurrency(row, col++, buyer.getValue().getTotalSold());
			
			// Alternate the background color
			if ((row % 2) == 0)
				setBackgroundColor( 0, row, col-1, row, 0x00e8ebed);

			row++;
		}

		// True = Export was successful
		return true;
	}

}
