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

package com.sebulli.fakturama.calculate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.misc.DataUtils;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * This class can generate a customer statistic
 *  
 * @author Gerd Bartelt
 */
public class CustomerStatistics {

	// The customer has already ordered something
	private boolean isRegularCustomer = false;
	
	// How many orders
	private Integer ordersCount = 0;
	
	// The last date
	private GregorianCalendar lastOrderDate = null;

	// Some of the invoices
	private String invoices = "";
	
	// The total volume
	private Double total = 0.0;
	
	// Customer to test
	private int contactID = -1;
	private String address = "";
	
	/**
	 * Constructor
	 * 		Generates a statistic
	 * @param 
	 * 		contactID of the customer
	 */
	public CustomerStatistics (int contactID) {
		
		// Exit, if no customer is set
		if (contactID < 0)
			return;
		
		this.contactID = contactID;
		makeStatistics(true);

	}
	
	/**
	 * Constructor
	 * 		Generates a statistic
	 * @param 
	 * 		contactID of the customer
	 * @param 
	 * 		firstAddressLine of the customer
	 */
	public CustomerStatistics (int contactID, String address) {
		
		this.contactID = contactID;
		this.address = address;
		makeStatistics(false);

	}
	

	/**
	 * Make the Statistics. Search for other documents from this customer
	 * 
	 * @param 
	 * 		byID TRUE:  Compare contact ID
	 * 		     FLASE: Compare also first line of address
	 */
	private void makeStatistics(boolean byID) {
		// Get all undeleted documents
		ArrayList<DataSetDocument> documents = Data.INSTANCE.getDocuments().getActiveDatasets();

		// Export the document data
		for (DataSetDocument document : documents) {

			// Only paid invoiced from this customer will be used for the statistics
			if ( (document.getIntValueByKey("category") == DocumentType.INVOICE.getInt())
					 && document.getBooleanValueByKey("paid")  ) {

				boolean customerFound = false;

				// Compare the customer ID
				if ((document.getIntValueByKey("addressid") == contactID) && ( contactID >= 0 ))
					customerFound = true;

				// Compare the the address
				if (!byID && (address.length() > 10) && 
					DataUtils.similarity(document.getStringValueByKey("address"), address) > 0.7)
					customerFound = true;
				
				if (customerFound) {
					// It's a regular customer
					isRegularCustomer = true;

					// Add the invoice number to the list of invoices
					// Add maximum 4 invoices
					if (ordersCount < 4) {
						if (!invoices.isEmpty())
							invoices += ", ";
						invoices += document.getStringValueByKey("name");
					}
					else if (ordersCount == 4) {
						invoices += ", ...";
					}
					
					
					// Increment the count of orders
					ordersCount ++;
					
					// Increase the total
					total += document.getDoubleValueByKey("payvalue");
					
					// Get the date of the document and convert it to a
					// GregorianCalendar object.
					GregorianCalendar documentDate = new GregorianCalendar();
					try {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

						String expenditureDateString = "";

						// Use date 
						expenditureDateString = document.getStringValueByKey("orderdate");

						// Do only parse non empty strings
						if (!expenditureDateString.isEmpty()) {
							documentDate.setTime(formatter.parse(expenditureDateString));

							// Set the last order date
							if (lastOrderDate == null) {
								lastOrderDate = documentDate;
							} else {
								documentDate.after(lastOrderDate);
								lastOrderDate = documentDate;
							}
						}

					}
					catch (ParseException e) {
					}
					
				}

				
			}
		}

		
	}
	
	/**
	 * Returns whether the customer has already ordered something
	 * 
	 * @return
	 * 		True, if there are some paid invoices
	 */
	public boolean isRegularCustomer() {
		return isRegularCustomer;
	}
	
	/**
	 * Returns how often the customer has paid an invoice
	 * 
	 * @return
	 * 		The number of the paid invoices
	 */
	public Integer getOrdersCount () {
		return ordersCount;
	}
	
	/**
	 * Returns the total value
	 * 
	 * @return
	 * 		The total value
	 */
	public Double getTotal () {
		return total;
	}
	
	/**
	 * Returns the last date
	 * 
	 * @return
	 * 		The date of the last order
	 */
	public String getLastOrderDate() {
		if (lastOrderDate != null)
			return  DataUtils.getDateTimeAsLocalString(lastOrderDate);
		else
			return "-";
	}
	
	/**
	 * Returns the string with some of the invoices
	 * 
	 * @return
	 * 	String with invoice numbers
	 */
	public String getInvoices () {
		return invoices;
	}
}
