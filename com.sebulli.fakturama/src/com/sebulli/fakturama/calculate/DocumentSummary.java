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
import java.util.Iterator;

import com.sebulli.fakturama.data.DataSetArray;
import com.sebulli.fakturama.data.DataSetItem;
import com.sebulli.fakturama.data.DataSetShipping;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Calculates the tax, gross and sum of one document. This is the central
 * calculation used by the document editors and the export functions.
 * 
 * @author Gerd Bartelt
 */
public class DocumentSummary {

	// The prices are not rounded to net or gross
	public static final int NOTSPECIFIED = 0;
	// The prices are rounded, that the net values are full cent values.
	public static final int ROUND_NET_VALUES = 1;
	// The prices are rounded, that the gross values are full cent values.
	public static final int ROUND_GROSS_VALUES = 2;
	
	// sum off items
	private PriceValue itemsNet;
	private PriceValue itemsGross;

	// total sum
	private PriceValue totalNet;
	private PriceValue totalVat;
	private PriceValue totalGross;

	// discount values
	private PriceValue discountNet;
	private PriceValue discountGross;

	// shipping value
	private PriceValue shippingNet;
	private PriceValue shippingVat;
	private PriceValue shippingGross;

	// deposit value
	private PriceValue deposit;
	private PriceValue finalPayment; 

	/**
	 * Default constructor. Resets all value to 0.
	 */
	public DocumentSummary() {
		resetValues();
	}

	/**
	 * Reset all values to 0
	 */
	private void resetValues() {
		itemsNet = new PriceValue(0.0);
		itemsGross = new PriceValue(0.0);
		totalNet = new PriceValue(0.0);
		totalVat = new PriceValue(0.0);
		totalGross = new PriceValue(0.0);
		discountNet = new PriceValue(0.0);
		discountGross = new PriceValue(0.0);
		shippingNet = new PriceValue(0.0);
		shippingVat = new PriceValue(0.0);
		shippingGross = new PriceValue(0.0);
		deposit = new PriceValue(0.0);
		finalPayment = new PriceValue(0.0);
	}

	/**
	 * Calculates the tax, gross and sum of a document
	 * 
	 * @param globalVatSummarySet
	 *            The documents vat is added to this global VAT summary set.
	 * @param items
	 *            Document's items
	 * @param shippingValue
	 *            Document's shipping
	 * @param shippingVatPercent
	 *            Shipping's VAT - This is only used, if the shipping's VAT is
	 *            not calculated based on the items.
	 * @param shippingVatDescription
	 *            Shipping's VAT name
	 * @param shippingAutoVat
	 *            If TRUE, the shipping VAT is based on the item's VAT
	 * @param itemsDiscount
	 *            Discount value
	 * @param noVat
	 *            TRUE, if all VAT values are set to 0.
	 * @param noVatDescription
	 *            Name of the VAT, which is 0.
	 * @param scaleFactor
	 * 
	 * @param deposit
	 */
	public void calculate(VatSummarySet globalVatSummarySet, DataSetArray<DataSetItem> items, double shippingValue, double shippingVatPercent,
			String shippingVatDescription, int shippingAutoVat, Double itemsDiscount, boolean noVat, String noVatDescription, Double scaleFactor, int netgross, Double deposit) {
		Double vatPercent;
		String vatDescription;

		// This Vat summary contains only the VAT entries of this document,
		// whereas the the parameter vatSummaryItems is a global VAT summary
		// and contains entries from this document and from others.
		VatSummarySet documentVatSummaryItems = new VatSummarySet();

		// Set the values to 0.0
		resetValues();

		// Use all non-deleted items
		ArrayList<DataSetItem> itemDataset = items.getActiveDatasets();
		for (DataSetItem item : itemDataset) {

			// Get the data from each item
			vatDescription = item.getStringValueByKey("vatdescription");
			vatPercent = item.getDoubleValueByKey("vatvalue");
			Price price = new Price(item, scaleFactor);
			Double itemVat = price.getTotalVat().asDouble();

			// Add the total net value of this item to the sum of net items
			this.itemsNet.add(price.getTotalNet().asDouble());

			// If noVat is set, the VAT is 0%
			if (noVat) {
				vatDescription = noVatDescription;
				vatPercent = 0.0;
				itemVat = 0.0;
			}

			// Add the VAT to the sum of VATs
			this.totalVat.add(itemVat);

			// Add the VAT summary item to the ... 
			VatSummaryItem vatSummaryItem = new VatSummaryItem(vatDescription, vatPercent, price.getTotalNet().asDouble(), itemVat);

			// .. VAT summary of the document ..
			documentVatSummaryItems.add(vatSummaryItem);

		}

		// *** round sum of items
		
		// round to full net cents
		if (netgross == DocumentSummary.ROUND_NET_VALUES) {
			this.itemsNet.round();
		} 
		
		// Gross value is the sum of net and VAT value
		this.totalNet = new PriceValue(this.itemsNet);
		this.itemsGross = new PriceValue(this.itemsNet);
		this.itemsGross.add(this.totalVat.asDouble());
		
		// round to full gross cents
		if (netgross == DocumentSummary.ROUND_GROSS_VALUES) {
			this.itemsGross.round();
			this.itemsNet.set(this.itemsGross.asDouble() - this.totalVat.asDouble() );
			this.totalNet.set(this.itemsNet.asDouble());
		}
		this.totalGross.set(this.itemsGross.asDouble());
		
		
		Double itemsNet = this.itemsNet.asDouble();
		Double itemsGross = this.itemsGross.asDouble();

		
		// *** DISCOUNT ***
		
		// Calculate the absolute discount values
		this.discountNet.set(itemsDiscount * itemsNet);
		this.discountGross.set(itemsDiscount * itemsGross);

		// Calculate discount
		if (!DataUtils.DoublesAreEqual(itemsDiscount, 0.0)) {

			// Discount value = discount percent * Net value
			Double discountNet = itemsDiscount * itemsNet;

			// Calculate the vat value in percent from the gross value of all items
			// and the net value of all items. So the discount's vat is the average 
			// value of the item's vat
			Double discountVatPercent;
			if (itemsNet != 0.0)
				discountVatPercent = (itemsGross / itemsNet) - 1;
			else
				// do not divide by zero
				discountVatPercent = 0.0;

			// If noVat is set, the VAT is 0%
			if (noVat) {
				discountVatPercent = 0.0;
			}

			// Reduce all the VAT entries in the VAT Summary Set by the discount 
			Double discountVatValue = 0.0;
			String discountVatDescription = "";
			for (Iterator<VatSummaryItem> iterator = documentVatSummaryItems.iterator(); iterator.hasNext();) {

				// Get the data from each entry
				VatSummaryItem vatSummaryItem = iterator.next();
				discountVatDescription = vatSummaryItem.getVatName();
				discountVatPercent = vatSummaryItem.getVatPercent();

				// If noVat is set, the VAT is 0%
				if (noVat) {
					discountVatDescription = noVatDescription;
					discountVatPercent = 0.0;
				}

				// Calculate the ratio of this vat summary item and all items.
				// The discountNetPart is proportional to this ratio.
				Double discountNetPart = 0.0;
				if (itemsNet != 0.0)
					discountNetPart = discountNet * (vatSummaryItem.getNet() / itemsNet);

				// Add discountNetPart to the sum "discountVatValue"  
				Price discountPart = new Price(discountNetPart, discountVatPercent);
				discountVatValue += discountPart.getUnitVat().asDouble();

				VatSummaryItem discountVatSummaryItem = new VatSummaryItem(discountVatDescription, discountVatPercent, discountPart.getUnitNet().asDouble(),
						discountPart.getUnitVat().asDouble());

				// Adjust the vat summary item by the discount part
				documentVatSummaryItems.add(discountVatSummaryItem);

			}

			// adjust the documents sum by the discount
			this.totalVat.add(discountVatValue);
			this.totalNet.add(discountNet);
			
			// round to full net cents
			if (netgross == DocumentSummary.ROUND_NET_VALUES) {
				this.discountNet.round();
				this.totalNet.round();
			} 
			
			if (netgross != DocumentSummary.ROUND_GROSS_VALUES) {
				this.totalGross.set(this.totalNet.asDouble() + this.totalVat.asDouble());
			}
			
			// round to full gross cents
			if (netgross == DocumentSummary.ROUND_GROSS_VALUES) {
				this.discountGross.round();
				this.totalGross.add(this.discountGross.asDouble());
				this.totalGross.round();
				this.discountNet.set(this.discountGross.asDouble() - discountVatValue);
				this.totalNet.set(this.totalGross.asDouble() - this.totalVat.asDouble());
			}
		}

		// calculate shipping

		// Scale the shipping
		shippingValue = shippingValue * scaleFactor;

		// If shippingAutoVat is not fix, the shipping vat is 
		// an average value of the vats of the items.
		if (shippingAutoVat != DataSetShipping.SHIPPINGVATFIX) {

			// If the shipping is set as gross value, calculate the net value.
			// Use the average vat of all the items.
			if (shippingAutoVat == DataSetShipping.SHIPPINGVATGROSS) {
				if (itemsGross != 0.0)
					shippingNet.set(shippingValue * itemsNet / itemsGross);
				else
					shippingNet.set(shippingValue);

			}

			// If the shipping is set as net value, use the net value.
			if (shippingAutoVat == DataSetShipping.SHIPPINGVATNET)
				shippingNet.set(shippingValue);

			// Use the average vat of all the items.
			if (itemsNet != 0.0)
				shippingVatPercent = (itemsGross / itemsNet) - 1;
			else
				shippingVatPercent = 0;

			// Increase the vat summary entries by the shipping ratio

			// Calculate the sum of all VatSummary entries
			Double netSumOfAllVatSummaryItems = 0.0;
			for (VatSummaryItem vatSummaryItem : documentVatSummaryItems) {
				netSumOfAllVatSummaryItems += vatSummaryItem.getNet();
			}

			for (Iterator<VatSummaryItem> iterator = documentVatSummaryItems.iterator(); iterator.hasNext();) {

				// Get the data from each entry
				VatSummaryItem vatSummaryItem = iterator.next();
				shippingVatDescription = vatSummaryItem.getVatName();
				shippingVatPercent = vatSummaryItem.getVatPercent();

				// If noVat is set, the VAT is 0%
				if (noVat) {
					shippingVatDescription = noVatDescription;
					shippingVatPercent = 0.0;
				}

				// Calculate the ratio of this vat summary item and all items.
				// The shippingNetPart is proportional to this ratio.
				Double shippingNetPart = 0.0;
				if (netSumOfAllVatSummaryItems != 0.0)
					shippingNetPart = shippingNet.asDouble() * (vatSummaryItem.getNet() / netSumOfAllVatSummaryItems);

				// Add shippingNetPart to the sum "shippingVatValue"  
				Price shippingPart = new Price(shippingNetPart, shippingVatPercent);
				shippingVat.add(shippingPart.getUnitVat().asDouble());

				VatSummaryItem shippingVatSummaryItem = new VatSummaryItem(shippingVatDescription, shippingVatPercent, shippingPart.getUnitNet().asDouble(),
						shippingPart.getUnitVat().asDouble());

				// Adjust the vat summary item by the shipping part
				documentVatSummaryItems.add(shippingVatSummaryItem);

			}

		}

		// If shippingAutoVat is fix set, the shipping vat is 
		// a constant percent value.
		else {

			shippingNet.set(shippingValue);

			// If noVat is set, the VAT is 0%
			if (noVat) {
				shippingVatDescription = noVatDescription;
				shippingVatPercent = 0.0;
			}

			// use shippingVatPercent as fix percent value for the shipping
			shippingVat.set(shippingNet.asDouble() * shippingVatPercent);

			VatSummaryItem shippingVatSummaryItem = new VatSummaryItem(shippingVatDescription, shippingVatPercent, shippingNet.asDouble(),
					shippingVat.asDouble());

			// Adjust the vat summary item by the shipping part
			documentVatSummaryItems.add(shippingVatSummaryItem);

		}

		// round to full net cents
		if (netgross == DocumentSummary.ROUND_NET_VALUES) {
			this.shippingNet.round();
			this.totalNet.round();
		} 
		
		this.shippingGross.set(this.shippingNet.asDouble() + this.shippingVat.asDouble());
		
		// round to full gross cents
		if (netgross == DocumentSummary.ROUND_GROSS_VALUES) {
			this.shippingGross.round();
			this.totalGross.round();
			this.totalNet.set(this.totalGross.asDouble() - this.totalVat.asDouble());
			this.shippingNet.set(this.shippingGross.asDouble() - this.shippingVat.asDouble());
		}

		
		// Add the shipping to the documents sum.
		this.totalVat.add(shippingVat.asDouble());
		this.totalNet.add(shippingNet.asDouble());
		this.totalGross.set(this.totalNet.asDouble() + this.totalVat.asDouble());

		// Finally, round the values
		if (netgross == DocumentSummary.ROUND_NET_VALUES) {
			this.totalNet.round();
			this.totalVat.round();
			this.totalGross.set(this.totalNet.asDouble() + this.totalVat.asDouble());
		} else if (netgross == DocumentSummary.ROUND_GROSS_VALUES) {
			this.totalGross.round();
			this.totalVat.round();
			this.totalNet.set(this.totalGross.asDouble() - this.totalVat.asDouble());
		} else {
			this.totalNet.round();
			this.totalGross.round();
			this.totalVat.set(this.totalGross.asDouble() - this.totalNet.asDouble());
		}

		this.discountNet.round();
		this.discountGross.round();

		this.itemsNet.round();
		this.itemsGross.round();

		// Finally, round the values
		if (netgross == DocumentSummary.ROUND_NET_VALUES) {
			this.shippingNet.round();
			this.shippingVat.round();
			this.shippingGross.set(this.shippingNet.asDouble() + this.shippingVat.asDouble());
		} else if (netgross == DocumentSummary.ROUND_GROSS_VALUES) {
			this.shippingGross.round();
			this.shippingVat.round();
			this.shippingNet.set(this.shippingGross.asDouble() - this.shippingVat.asDouble());
		} else {
			this.shippingNet.round();
			this.shippingGross.round();
			this.shippingVat.set(this.shippingGross.asDouble() - this.shippingVat.asDouble());
		}

		//calculate the final payment
		this.deposit.set(deposit);
		this.finalPayment.set(this.totalGross.asDouble() - deposit);

		// Round also the Vat summaries
		documentVatSummaryItems.roundAllEntries();

		// Add the entries of the document summary set also to the global one
		if (globalVatSummarySet != null)
			globalVatSummarySet.addVatSummarySet(documentVatSummaryItems);

	}

	/**
	 * Getter for shipping value (net)
	 * 
	 * @return shipping net as PriceValue
	 */
	public PriceValue getShippingNet() {
		return this.shippingNet;
	}

	/**
	 * Getter for shipping Vat value (Vat)
	 * 
	 * @return shipping Vat as PriceValue
	 */
	public PriceValue getShippingVat() {
		return this.shippingVat;
	}

	/**
	 * Getter for shipping value (gross)
	 * 
	 * @return shipping gross as PriceValue
	 */
	public PriceValue getShippingGross() {
		return this.shippingGross;
	}

	/**
	 * Getter for sum of items (net)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getItemsNet() {
		return this.itemsNet;
	}

	/**
	 * Getter for sum of items (gross)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getItemsGross() {
		return this.itemsGross;
	}

	/**
	 * Getter for total document sum (net)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getTotalNet() {
		return this.totalNet;
	}

	/**
	 * Getter for total document sum (vat)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getTotalVat() {
		return this.totalVat;
	}

	/**
	 * Getter for total document sum (gross)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getTotalGross() {
		return this.totalGross;
	}

	/**
	 * Getter for discount (net)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getDiscountNet() {
		return this.discountNet;
	}

	/**
	 * Getter for discount (gross)
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getDiscountGross() {
		return this.discountGross;
	}

	/**
	 * Getter for the deposit
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getDeposit() {
		return this.deposit;
	}
	
	/**
	 * Getter for the final payment
	 * 
	 * @return Sum as PriceValue
	 */
	public PriceValue getFinalPayment() {
		return this.finalPayment;
	} 
}
