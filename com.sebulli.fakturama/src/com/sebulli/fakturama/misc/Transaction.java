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

package com.sebulli.fakturama.misc;

import java.util.ArrayList;
import java.util.UUID;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;

/**
 * Organize all document of a specified transaction
 * 
 * @author Gerd Bartelt
 *
 */
public class Transaction {
		
	// The transcation no.
	int transaction = -1;
	// An list with all documents with the same transaction number
	ArrayList<DataSetDocument> documents = null;
	
	/**
	 * Constructor
	 * 
	 * Collects all documents with the same transaction number
	 * 
	 * @param document
	 * 	The document with the parent transaction number
	 */
	public Transaction (DataSetDocument document) {
		
		// Get the transaction number
		transaction = document.getIntValueByKey("transaction");

		// Exit, if there is no number
		if (transaction == -1)
			return;

		// Create a new list
		documents = new ArrayList<DataSetDocument>();
		
		// Get all documents
		ArrayList<DataSetDocument> allDocuments;
		allDocuments = Data.INSTANCE.getDocuments().getActiveDatasets();
		
		// Search for all documents with the same number
		for (DataSetDocument oneDocument: allDocuments) {
			if (oneDocument.getIntValueByKey("transaction") == transaction ) {
				// Add the documents to the list
				documents.add(oneDocument);
			}
		}
	}
	
	/**
	 * Generates a random transaction number
	 * 
	 * @return new random ID
	 */
	public static int getNewTransactionId () {
		return Math.abs(UUID.randomUUID().hashCode());
	}
	
	/**
	 * Returns a string with all documents with the same transaction
	 *  
	 * @param docType
	 * 		Only those documents will be returned
	 * @return
	 * 		String with the document names
	 */
	public String getReference (DocumentType docType) {
		
		// Start with an empty string
		String reference = "";
		
		// Get all documents
		for (DataSetDocument document: documents) {
			
			// Has this document the same type
			if (document.getIntValueByKey("category") == docType.getInt()) {

				// Separate multiple reference names by a comma
				if (!reference.isEmpty())
					reference += ", ";

				// Add the name to the reference string
				reference += document.getStringValueByKey("name");
			}
		}
		
		// Return the reference string
		return reference;
	}
	
	/**
	 * Gets the first referenced document for this transaction.
	 * 
	 * @return
	 */
	public String getFirstReferencedDocumentDate(DocumentType docType) {
		DataSetDocument reference = null;
		
		// Get all documents
		for (DataSetDocument document: documents) {
			
			// Has this document the same type
			if (document.getIntValueByKey("category") == docType.getInt()) {
				// Add the name to the reference string
				reference = document;
				break;
			}
		}
		
		// Return the reference date
		return reference != null ? reference.getFormatedStringValueByKey("date") : "";
		
	}
	
	/**
	 * Merge 2 transactions and to one single
	 * 
	 * @param mainDocument the main document
	 * @param otherDocument the document which gets the id of the main document
	 */
	public static void mergeTwoTransactions (DataSetDocument mainDocument, DataSetDocument otherDocument) {
		int idMainDocument = mainDocument.getIntValueByKey("transaction");
		int idOtherDocument = otherDocument.getIntValueByKey("transaction");
		
		// Nothing to do, if both are equal
		if (idMainDocument == idOtherDocument)
			return;
		
		// Get all documents
		ArrayList<DataSetDocument> allDocuments;
		allDocuments = Data.INSTANCE.getDocuments().getActiveDatasets();
		
		// Search for all documents with the same number
		for (DataSetDocument oneDocument: allDocuments) {
			if (oneDocument.getIntValueByKey("transaction") == idOtherDocument ) {

				// Change the transaction number to the new one
				oneDocument.setIntValueByKey("transaction", idMainDocument);
				Data.INSTANCE.updateDataSet(oneDocument);
			}
		}

	}

	/**
     * @return the documents
     */
    public ArrayList<DataSetDocument> getDocuments() {
    	return documents;
    }
}
