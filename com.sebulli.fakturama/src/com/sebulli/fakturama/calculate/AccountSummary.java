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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetAccountEntry;
import com.sebulli.fakturama.data.DataSetArray;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.data.DataSetPayment;
import com.sebulli.fakturama.data.DataSetVoucher;
import com.sebulli.fakturama.misc.DocumentType;

public class AccountSummary {

	HashMap<String,Integer> paymentIds = new HashMap<String,Integer>();  
	
	// Set with all accounts
	private TreeSet<String> accounts; 
	
	// Array with all entries of one account
	private ArrayList<DataSetAccountEntry> accountEntries;
	
	/**
	 * Constructor
	 */
	public AccountSummary() {
		accounts = new TreeSet<String>();
		accountEntries = new ArrayList<DataSetAccountEntry>() ;

	}
	
	/**
	 * Return a set of all accounts
	 * Collect them from the payments and the vouchers
	 */
	public void collectAccounts() {
		accounts = new TreeSet<String>();
		
		// Collect all category strings
		accounts.addAll(Data.INSTANCE.getPayments().getCategoryStrings());
		accounts.addAll(Data.INSTANCE.getReceiptVouchers().getCategoryStrings());
		accounts.addAll(Data.INSTANCE.getExpenditureVouchers().getCategoryStrings());

	}
	
	/**
	 * Getter for the collected accounts
	 * 
	 * @return
	 * 		Tree set with all accounts
	 */
	public TreeSet<String> getAccounts() {
		
		return accounts;
	}
	
	/**
	 * Collects account entries
	 * 
	 * @param account
	 * @param vouchers
	 * @param sign
	 * 	The sign (+1 for receipts or for expenditures -1)
	 */
	private void collectVouchers (String account, DataSetArray<?> vouchers , double sign) {

		ArrayList<?> entries = vouchers.getActiveDatasets();
		
		for (Object entry : entries) {
			DataSetVoucher voucher = (DataSetVoucher)entry;
			// Add only vouchers with this account
			if (voucher.getStringValueByKey("category").equalsIgnoreCase(account)) {
				DataSetAccountEntry accountEntry = new DataSetAccountEntry(voucher, sign);
				accountEntries.add(accountEntry);
			}
			
		}
	}

	private void collectDocuments (String account, DataSetArray<DataSetDocument> documents,
			 DataSetArray<DataSetPayment> payments) {

		paymentIds = new HashMap<String,Integer>();
		
		for (DataSetPayment payment : payments.getActiveDatasets()) {
			paymentIds.put(payment.getStringValueByKey("description"), payment.getIntValueByKey("id"));
		}
		

		
		ArrayList<DataSetDocument> entries = documents.getActiveDatasets();
		
		for (DataSetDocument document : entries) {
			
			// Only invoiced and credits in the interval
			// will be exported.
			boolean isInvoiceOrCredit = ((document.getIntValueByKey("category") == DocumentType.INVOICE.getInt()) || (document.getIntValueByKey("category") == DocumentType.CREDIT
					.getInt())) ;

			if (isInvoiceOrCredit) {
				int paymentid = document.getIntValueByKey("paymentid");
				String paymentdescription = document.getStringValueByKey("paymentdescription");

				if (paymentid < 0) {
					if (paymentIds.containsKey(paymentdescription))
						paymentid = paymentIds.get(paymentdescription);
				}
				
				if (paymentid >= 0) {
					String paymentAccount = payments.getDatasetById(paymentid).getStringValueByKey("category");
					if (paymentAccount.equalsIgnoreCase(account)) {
						DataSetAccountEntry accountEntry = new DataSetAccountEntry(document);
						accountEntries.add(accountEntry);
					}
				}
			}
		}
	}
	
	/**
	 * Getter for account entries
	 * 
	 * @return
	 */
	public ArrayList<DataSetAccountEntry> getAccountEntries() {
		return accountEntries;
	}
	
	/**
	 * Collects all entries from all vouchers
	 * 
	 * @param account
	 * 		The account name
	 */
	public void collectEntries(String account) {
		
		
		accountEntries = new ArrayList<DataSetAccountEntry>() ;

		collectDocuments( account, Data.INSTANCE.getDocuments(), Data.INSTANCE.getPayments());
		collectVouchers( account, Data.INSTANCE.getReceiptVouchers(), 1.0);
		collectVouchers( account, Data.INSTANCE.getExpenditureVouchers(), -1.0);
	}
	
	
}
