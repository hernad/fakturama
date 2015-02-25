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

package com.sebulli.fakturama.export.productbuyers;

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

	// export Options
	private boolean sortByQuantity = false;
	
	/**
	 * Comparator to sort the List of items by volume or by quantity
	 * 
	 * @author Gerd Bartelt
	 */
	private class TotalSoldComparer implements Comparator<Object> {
		
		// List with all items
		private Map<String, BuyersAndTotal>  data = null;
		// Sort by volume or by quantity
		private boolean sortByQuantity;
		
		/**
		 * Constructor
		 * 
		 * @param data 
		 * 			The data to sort
		 * @param sortByQuantity 
		 * 			How to sort the data
		 */
		public TotalSoldComparer (Map<String, BuyersAndTotal> data, boolean sortByQuantity){
			super();
			this.data = data;
			this.sortByQuantity = sortByQuantity;
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
        	 if (sortByQuantity) {
        		 Integer e1 = (Integer) this.data.get(o1).getTotalQuantity();
        		 Integer e2 = (Integer) this.data.get(o2).getTotalQuantity();
                 result = e2.compareTo(e1);
        	 }
        	 else {
            	 Double e1 = (Double) this.data.get(o1).getTotalSold();
            	 Double e2 = (Double) this.data.get(o2).getTotalSold();
                 result = e2.compareTo(e1);
        	 }
        	 
        	 // Two items must not be equal. If they were, they would be
        	 // replaces in the map
        	 if (result == 0)
        		 result = 1;
        	 
        	 return result;
         }
	}
	
	/**
	 * Comparator to sort the List of buyers by volume or by quantity
	 * 
	 * @author Gerd Bartelt
	 */
	private class BuyersTotalSoldComparer implements Comparator<Object> {
		
		// List with all buyers
		private Map<String, TotalSoldAndQuantity>  data = null;
		// Sort by volume or by quantity
		private boolean sortByQuantity;

		/**
		 * Constructor
		 * 
		 * @param data 
		 * 			The data to sort
		 * @param sortByQuantity 
		 * 			How to sort the data
		 */
		public BuyersTotalSoldComparer (Map<String, TotalSoldAndQuantity> data, boolean sortByQuantity){
			super();
			this.data = data;
			this.sortByQuantity = sortByQuantity;
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
        	 if (sortByQuantity) {
        		 Integer e1 = this.data.get(o1).getTotalQuantity();
        		 Integer e2 = this.data.get(o2).getTotalQuantity();
        		 result =  e2.compareTo(e1);
        	 }
        	 else {
            	 Double e1 = this.data.get(o1).getTotalSold();
            	 Double e2 = this.data.get(o2).getTotalSold();
            	 result = e2.compareTo(e1);
        	 }
        	 
        	 // Two items must not be equal. If they were, they would be
        	 // replaces in the map
        	 if (result == 0)
        		 result = 1;

        	 return result;
         }
	}
	
	
	// (unsorted) List with all items
	private Map<String, BuyersAndTotal> itemMap = new HashMap<String, BuyersAndTotal>();

	
	/**
	 * Constructor Sets the begin and end date
	 * 
	 * @param startDate
	 *            Begin date
	 * @param endDate
	 *            Begin date
	 */
	public Exporter(GregorianCalendar startDate, GregorianCalendar endDate,
			 boolean doNotUseTimePeriod,
			 boolean sortByQuantity) {
		super(startDate, endDate, doNotUseTimePeriod);
		this.sortByQuantity = sortByQuantity;
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
		setCellTextInBold(0, 0, _("List of products and buyers"));

		// Fill the first 4 rows with the company information
		fillCompanyInformation(2);
		fillTimeIntervall(7);

		// Counter for the current row and columns in the Calc document
		int row = 11;
		int col = 0;

		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Product"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Volume"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Buyers"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Quantity"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Volume"));

		// Draw a horizontal line
		for (col = 0; col < 6; col++) {
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
						
						if (itemMap.containsKey(item.getStringValueByKey("name"))) {
							itemMap.get(item.getStringValueByKey("name")).add(buyerName, item);
						}
						else {
							itemMap.put(item.getStringValueByKey("name"), new BuyersAndTotal (buyerName, item));
						}
					}
				}
			}
		}
		
		// Sort the list of all items by quantity or by volume
		SortedMap<String, BuyersAndTotal> sortedItemMap = new TreeMap<String, BuyersAndTotal>(new TotalSoldComparer(itemMap, sortByQuantity));
		sortedItemMap.putAll(itemMap);
		
		// Alternate the background color every new item 
		int altrow =0;
		
		// Get all items of the list
		for ( Iterator<Entry<String, BuyersAndTotal>> iterator = sortedItemMap.entrySet().iterator(); iterator.hasNext(); ) {
			
			// Get the item
			Entry<String, BuyersAndTotal> entry = iterator.next();
			
			// Place the item, the total quantity and volume in the first 3 columns
			col = 0;
			setCellText(row, col++, entry.getKey());
			setCellText(row, col++, Integer.toString(entry.getValue().getTotalQuantity()));
			setCellValueAsLocalCurrency(row, col++, entry.getValue().getTotalSold());

			// Get the buyers and sort them
			Map<String, TotalSoldAndQuantity> buyers = entry.getValue().getBuyers();
			SortedMap<String, TotalSoldAndQuantity> sortedBuyers = new TreeMap<String, TotalSoldAndQuantity>(new BuyersTotalSoldComparer(buyers, sortByQuantity));
			sortedBuyers.putAll(buyers);

			// Get through the list of all buyers
			for (Iterator<?> iteratorBuyer = sortedBuyers.entrySet().iterator(); iteratorBuyer.hasNext(); ) {

				// Get the next buyer
				@SuppressWarnings("unchecked")
				Entry<String, TotalSoldAndQuantity> buyer = (Entry<String, TotalSoldAndQuantity>)iteratorBuyer.next();
				col = 3;
				
				// Place the buyer's name, the quantity and the volume into
				// the next columns
				setCellText(row, col++, buyer.getKey());
				setCellText(row, col++, Integer.toString(buyer.getValue().getTotalQuantity()));
				setCellValueAsLocalCurrency(row, col++, buyer.getValue().getTotalSold());
				
				// Alternate the background color
				if ((altrow % 2) == 0)
					setBackgroundColor( 0, row, col-1, row, 0x00e8ebed);

				row++;
			}
			altrow ++;
		}
		
		// True = Export was successful
		return true;
	}

}
