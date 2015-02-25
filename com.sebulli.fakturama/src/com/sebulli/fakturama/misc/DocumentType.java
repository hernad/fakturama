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

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sebulli.fakturama.Activator;

/**
 * Enumeration of all 8 data types, a document can be.
 * 
 * @author Gerd Bartelt
 */
public enum DocumentType {
	// all 8 data types
	NONE, LETTER, OFFER, ORDER, CONFIRMATION, INVOICE, DELIVERY, CREDIT, DUNNING, PROFORMA;

	// 9 types.
	public final static int MAXID = 9;

	/**
	 * Convert from a DocumentType to the corresponding integer
	 * 
	 * @param documentType
	 *            Document type to convert
	 * @return The integer that corresponds to the DocumentType
	 */
	public static int getInt(DocumentType documentType) {
		switch (documentType) {
		case LETTER:
			return 1;
		case OFFER:
			return 2;
		case ORDER:
			return 3;
		case CONFIRMATION:
			return 4;
		case INVOICE:
			return 5;
		case DELIVERY:
			return 6;
		case CREDIT:
			return 7;
		case DUNNING:
			return 8;
		case PROFORMA:
			return 9;
		}
		return 0;
	}

	/**
	 * Convert from a document type String to the corresponding integer
	 * 
	 * @param documentType
	 *            Document type as string to convert
	 * @return The integer that corresponds to the DocumentType
	 */
	public static int getInt(String documentType) {
		if (getString(LETTER).equals(documentType))
			return getInt(LETTER);
		if (getString(OFFER).equals(documentType))
			return getInt(OFFER);
		if (getString(ORDER).equals(documentType))
			return getInt(ORDER);
		if (getString(CONFIRMATION).equals(documentType))
			return getInt(CONFIRMATION);
		if (getString(INVOICE).equals(documentType))
			return getInt(INVOICE);
		if (getString(DELIVERY).equals(documentType))
			return getInt(DELIVERY);
		if (getString(CREDIT).equals(documentType))
			return getInt(CREDIT);
		if (getString(DUNNING).equals(documentType))
			return getInt(DUNNING);
		if (getString(PROFORMA).equals(documentType))
			return getInt(PROFORMA);

		return getInt(NONE);
	}

	/**
	 * Gets the corresponding integer of an DocumentType
	 * 
	 * @return The integer that corresponds to the DocumentType
	 */
	public int getInt() {
		return getInt(this);
	}

	/**
	 * Convert from a document type string to a DocumentType
	 * 
	 * @param documentType
	 *            String to convert
	 * @return The DocumentType that corresponds to the String
	 */
	public static DocumentType getType(String documentType) {
		if (isDocumentTypeString(LETTER, documentType))
			return LETTER;
		if (isDocumentTypeString(OFFER, documentType))
			return OFFER;
		if (isDocumentTypeString(ORDER, documentType))
			return ORDER;
		if (isDocumentTypeString(CONFIRMATION, documentType))
			return CONFIRMATION;
		if (isDocumentTypeString(INVOICE, documentType))
			return INVOICE;
		if (isDocumentTypeString(DELIVERY, documentType))
			return DELIVERY;
		if (isDocumentTypeString(CREDIT, documentType))
			return CREDIT;
		if (isDocumentTypeString(DUNNING, documentType))
			return DUNNING;
		if (isDocumentTypeString(PROFORMA, documentType))
			return PROFORMA;

		return NONE;
	}

	/**
	 * Convert from an integer to a document type localized string The singular
	 * style is used.
	 * 
	 * @param i
	 *            Integer to convert
	 * @return The DocumentType as localized string
	 */
	public static String getString(int i) {
		switch (i) {
		case 1:
			//T: Type of the document (singular)
			return _("Letter");
		case 2:
			//T: Type of the document (singular)
			return _("Offer");
		case 3:
			//T: Type of the document (singular)
			return _("Order");
		case 4:
			//T: Type of the document (singular)
			return _("Confirmation");
		case 5:
			//T: Type of the document (singular)
			return _("Invoice");
		case 6:
			//T: Type of the document (singular)
			return _("Delivery Note");
		case 7:
			//T: Type of the document (singular)
			return _("Credit");
		case 8:
			//T: Type of the document (singular)
			return _("Dunning");
		case 9:
			//T: Type of the document (singular)
			return _("Proforma");
		}
		return "";
	}

	/**
	 * Gets the document type as localized string The singular style is used.
	 * 
	 * @return The DocumentType as localized string
	 */
	public String getString() {
		return getString(this.getInt());
	}

	/**
	 * Convert from an integer to a document type localized string The plural
	 * style is used.
	 * 
	 * @param i
	 *            Integer to convert
	 * @return The DocumentType as localized string
	 */
	public static String getPluralString(int i) {
		switch (i) {
		case 1:
			//T: Type of 2 or more documents (plural)
			return _("Letters");
		case 2:
			//T: Type of 2 or more documents (plural)
			return _("Offers");
		case 3:
			//T: Type of 2 or more documents (plural)
			return _("Orders");
		case 4:
			//T: Type of 2 or more documents (plural)
			return _("Confirmations");
		case 5:
			//T: Type of 2 or more documents (plural)
			return _("Invoices");
		case 6:
			//T: Type of 2 or more documents (plural)
			return _("Delivery Notes");
		case 7:
			//T: Type of 2 or more documents (plural)
			return _("Credit Items");
		case 8:
			//T: Type of 2 or more documents (plural)
			return _("Dunning Letters");
		case 9:
			//T: Type of 2 or more documents (plural)
			return _("Proforma Invoices");
		}
		return "";
	}

	/**
	 * Convert from an integer to a DocumentType
	 * 
	 * @param i
	 *            Integer to convert
	 * @return The DocumentType
	 */
	public static DocumentType getType(int i) {
		switch (i) {
		case 1:
			return LETTER;
		case 2:
			return OFFER;
		case 3:
			return ORDER;
		case 4:
			return CONFIRMATION;
		case 5:
			return INVOICE;
		case 6:
			return DELIVERY;
		case 7:
			return CREDIT;
		case 8:
			return DUNNING;
		case 9:
			return PROFORMA;
		}
		return NONE;
	}

	/**
	 * Convert from an integer to a document type non-localized string The
	 * singular style is used.
	 * 
	 * @param i
	 *            Integer to convert
	 * @return The DocumentType as non-localized string
	 */
	public static String getTypeAsString(int i) {
		// do not translate !!
		switch (i) {
		case 1:
			return "Letter";
		case 2:
			return "Offer";
		case 3:
			return "Order";
		case 4:
			return "Confirmation";
		case 5:
			return "Invoice";
		case 6:
			return "Delivery";
		case 7:
			return "Credit";
		case 8:
			return "Dunning";
		case 9:
			return "Proforma";
		}
		return "NONE";
	}

	/**
	 * Convert from Document Type to a document type non-localized string The
	 * singular style is used.
	 * 
	 * @param documentType
	 *            DocumentType to convert
	 * @return The DocumentType as non-localized string
	 */
	public static String getTypeAsString(DocumentType documentType) {
		return getTypeAsString(getInt(documentType));
	}

	/**
	 * Get the type as non-localized string
	 * 
	 * @return The DocumentType as non-localized string
	 */
	public String getTypeAsString() {
		return DocumentType.getTypeAsString(this);
	}

	/**
	 * Convert from DocumentType to a document type localized string The
	 * singular style is used.
	 * 
	 * @param documentType
	 *            DocumentType to convert
	 * @return The DocumentType as localized string
	 */
	public static String getString(DocumentType documentType) {
		return getString(getInt(documentType));
	}

	/**
	 * Convert from DocumentType to a document type localized string The plural
	 * style is used.
	 * 
	 * @param documentType
	 *            DocumentType to convert
	 * @return The DocumentType as localized string
	 */
	public static String getPluralString(DocumentType documentType) {
		return getPluralString(getInt(documentType));
	}

	/**
	 * Get the DocumentType as plural localized string
	 * 
	 * @return DocumentType as localized string
	 */
	public String getPluralString() {
		return getPluralString(this);
	}

	/**
	 * Compares an DocumentType and a document type String. The string can
	 * describe the type as a singular or plural.
	 * 
	 * @param documentType
	 *            First compare parameter as DocumentType
	 * @param documentTypeString
	 *            Second compare parameter as String
	 * @return True, of both are equal
	 */
	private static boolean isDocumentTypeString(DocumentType documentType, String documentTypeString) {

		// Remove all trailed signs starting from "/" 
		if (documentTypeString.contains("/") && (documentTypeString.length() > 1))
			documentTypeString = documentTypeString.substring(0, documentTypeString.indexOf("/"));

		// Test, if it is as singular
		if (getString(documentType).equals(documentTypeString))
			return true;

		// Test, if it is as plural
		if (getPluralString(documentType).equals(documentTypeString))
			return true;

		return false;
	}

	/**
	 * JFace DocumentType content provider Provides all Document types as an
	 * String array
	 * 
	 * @author Gerd Bartelt
	 */
	public static class DocumentTypeContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {

			// Get all document types
			ArrayList<String> strings = new ArrayList<String>();
			for (int i = 1; i <= MAXID; i++)
				strings.add(getString(i));

			// Convert them to an Array
			return strings.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			viewer.refresh();
		}

	}

	/**
	 * Defines all Document Types that contains an item table
	 * 
	 * @return True for all types with item table
	 */
	public boolean hasItems() {
		switch (this) {
		case LETTER:
			return false;
		case OFFER:
			return true;
		case ORDER:
			return true;
		case CONFIRMATION:
			return true;
		case INVOICE:
			return true;
		case DELIVERY:
			return true;
		case CREDIT:
			return true;
		case DUNNING:
			return false;
		case PROFORMA:
			return true;
		}
		return false;
	}

	/**
	 * Defines all Document Types that contains a price
	 * 
	 * @return True for all types with a price
	 */
	public boolean hasPrice() {
		switch (this) {
		case LETTER:
			return false;
		case OFFER:
			return true;
		case ORDER:
			return true;
		case CONFIRMATION:
			return true;
		case INVOICE:
			return true;
		case DELIVERY:
			return false;
		case CREDIT:
			return true;
		case DUNNING:
			return false;
		case PROFORMA:
			return true;
		}
		return false;
	}

	/**
	 * Defines all Document Types that contains a price for items
	 * 
	 * @return True for all types with a price for items
	 */
	public boolean hasItemsPrice() {
		if (this == DELIVERY)
			return  Activator.getDefault().getPreferenceStore().getBoolean("DOCUMENT_DELIVERY_NOTE_ITEMS_WITH_PRICE");
		else 
			return this.hasPrice();
	}
	
	/**
	 * Defines all Document Types that can be marked as paid
	 * 
	 * @return True for all types with a price
	 */
	public boolean hasPaid() {
		switch (this) {
		case LETTER:
			return false;
		case OFFER:
			return false;
		case ORDER:
			return false;
		case CONFIRMATION:
			return false;
		case INVOICE:
			return true;
		case DELIVERY:
			return false;
		case CREDIT:
			return true;
		case DUNNING:
			return false;
		case PROFORMA:
			return false;
		}
		return false;
	}

	/**
	 * Defines all Document Types that contains a reference to an invoice
	 * document.
	 * 
	 * @return True for all types with a reference to an invoice document.
	 */
	public boolean hasInvoiceReference() {
		switch (this) {
		case LETTER:
			return false;
		case OFFER:
			return false;
		case ORDER:
			return false;
		case CONFIRMATION:
			return false;
		case INVOICE:
			return false;
		case DELIVERY:
			return true;
		case CREDIT:
			return true;
		case DUNNING:
			return true;
		case PROFORMA:
			return false;

		}
		return false;
	}

	/**
	 * Defines all Document Types that can add items from a delivery note.
	 * 
	 * @return True for all types that can add items from a delivery note.
	 */
	public boolean hasAddFromDeliveryNote() {
		switch (this) {
		case LETTER:
			return false;
		case OFFER:
			return false;
		case ORDER:
			return false;
		case CONFIRMATION:
			return false;
		case INVOICE:
			return true;
		case DELIVERY:
			return false;
		case CREDIT:
			return false;
		case DUNNING:
			return false;
		case PROFORMA:
			return true;

		}
		return false;
	}
	
	/**
	 * Defines the sign of a document
	 * 
	 * @return 1 for documents with positive sign, -1 for those with negative
	 *         sign.
	 */
	public int sign() {
		switch (this) {
		case LETTER:
			return 1;
		case OFFER:
			return 1;
		case ORDER:
			return 1;
		case CONFIRMATION:
			return 1;
		case INVOICE:
			return 1;
		case DELIVERY:
			return 1;
		case CREDIT:
			return -1;
		case DUNNING:
			return 1;
		case PROFORMA:
			return 1;
		}
		return 1;
	}

	
	
	/**
	 * Get the text to create a new instance of this document
	 * 
	 * @return Text as localized string.
	 */
	public String getNewText() {
		return (getNewText(this.getInt()));
	}
	
	/**
	 * Get the text to create a new instance of this document
	 * 
	 * @param i
	 * 		The document type index
	 * @return 
	 * Text as localized string.
	 */
	public static String getNewText(int i) {
		switch (DocumentType.getType(i)) {
		case LETTER:
			//T: Text of the action to create a new document
			return _("New Letter");
		case OFFER:
			//T: Text of the action to create a new document
			return _("New Offer");
		case ORDER:
			//T: Text of the action to create a new document
			return _("New Order");
		case CONFIRMATION:
			//T: Text of the action to create a new document
			return _("New Confirmation");
		case INVOICE:
			//T: Text of the action to create a new document
			return _("New Invoice");
		case DELIVERY:
			//T: Text of the action to create a new document
			return _("New Delivery Note");
		case CREDIT:
			//T: Text of the action to create a new document
			return _("New Credit");
		case DUNNING:
			//T: Text of the action to create a new document
			return _("New Dunning");
		case PROFORMA:
			//T: Text of the action to create a new document
			return _("New Proforma Invoice");
		}
		//T: Text of the action to create a new document
		return _("New Document");
		
	}

}
