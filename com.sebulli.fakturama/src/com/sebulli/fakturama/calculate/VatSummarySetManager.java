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

import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * Stores one VatSummarySet object and provides some methods e.g. to add an
 * UniDataSet document
 * 
 * @author Gerd Bartelt
 */
public class VatSummarySetManager {
	VatSummarySet vatSummarySet;

	/**
	 * Constructor Creates a new VatSummarySet
	 */
	public VatSummarySetManager() {
		vatSummarySet = new VatSummarySet();
	}

	/**
	 * Add an UniDataSet document to the VatSummarySet
	 * 
	 * @param document
	 *            Document to add
	 */
	public void add(DataSetDocument document, Double scaleFactor) {
		int parentSign = DocumentType.getType(document.getIntValueByKey("category")).sign();

		// Create a new summary object and start the calculation.
		// This will add all the entries to the VatSummarySet
		DocumentSummary summary = new DocumentSummary();
		summary.calculate(vatSummarySet, document.getItems(), document.getDoubleValueByKey("shipping") * parentSign,
				document.getDoubleValueByKey("shippingvat"), document.getStringValueByKey("shippingvatdescription"),
				document.getIntValueByKey("shippingautovat"), document.getDoubleValueByKey("itemsdiscount"), document.getBooleanValueByKey("novat"),
 			        document.getStringValueByKey("novatdescription"), scaleFactor, document.getIntValueByKey("netgross"), document.getDoubleValueByKey("deposit"));
	}
	
	
	
	/**
	 * Getter for the VatSummarySet
	 * 
	 * @return The VatSummarySet
	 */
	public VatSummarySet getVatSummaryItems() {
		return vatSummarySet;
	}

	/**
	 * Get the size of the
	 * 
	 * @return The size of the VatSummarySet
	 */
	public int size() {
		return vatSummarySet.size();
	}

	/**
	 * Get the index of a VatSummaryItem
	 * 
	 * @param vatSummaryItem
	 *            Item to search for
	 * @return Index of the item or -1, of none was found
	 */
	public int getIndex(VatSummaryItem vatSummaryItem) {
		return vatSummarySet.getIndex(vatSummaryItem);
	}
}
