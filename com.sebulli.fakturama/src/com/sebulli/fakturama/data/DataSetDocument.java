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

package com.sebulli.fakturama.data;

import static com.sebulli.fakturama.Translate._;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.actions.MarkOrderAsAction;
import com.sebulli.fakturama.calculate.DocumentSummary;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DocumentType;
import com.sebulli.fakturama.misc.Transaction;

/**
 * UniDataSet for all documents
 * 
 * @author Gerd Bartelt
 */
public class DataSetDocument extends UniDataSet {
	DocumentSummary summary = new DocumentSummary();

	/**
	 * Constructor Creates an new letter
	 */
	public DataSetDocument() {
		this(DocumentType.LETTER);
	}

	/**
	 * Constructor Create a new document
	 * 
	 * @param documentType
	 *            Type of new document
	 */
	public DataSetDocument(DocumentType documentType) {
		this(documentType, "", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));

	}

	/**
	 * Constructor Create a new document, set the date to now and create a
	 * transaction ID
	 * 
	 * @param documentType
	 *            Type of the new document
	 * @param webshopid
	 *            Web shop ID (order number)
	 * @param webshopdate
	 *            Web shop date (date of order)
	 */
	public DataSetDocument(DocumentType documentType, String webshopid, String webshopdate) {
		this(-1, "000000", false, documentType, -1, "", "", "", 0, "", "", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()), (new SimpleDateFormat(
				"yyyy-MM-dd")).format(new Date()), -1, "", 0, false, "2000-01-01", 0.0, "", "", 0, "", 0.0, 0.0, "", 1, 0.0, false, 0.0, "", 0, webshopid, webshopdate,
				webshopdate, false, "", "", 
				Activator.getDefault().getPreferenceStore().getInt("DOCUMENT_USE_NET_GROSS") + 1,
				0.0, 0, -1, "", "","","");

		this.hashMap.put("transaction", new UniData(UniDataType.INT, Transaction.getNewTransactionId()));
	}

	/**
	 * Constructor Create a new document from an other document Also mark all of
	 * the items as "shared"
	 * 
	 * @param documentType
	 *            Type of the new document
	 * @param parent
	 *            Parent document
	 */
	public DataSetDocument(DocumentType documentType, DataSetDocument parent) {
		// create a copy
		this(-1, parent.getStringValueByKey("name"), false, documentType, parent.getIntValueByKey("addressid"), parent.getStringValueByKey("address"), parent
				.getStringValueByKey("deliveryaddress"), parent.getStringValueByKey("addressfirstline"), parent.getIntValueByKey("progress"), parent
				.getStringValueByKey("customerref"), parent.getStringValueByKey("consultant"), (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()), (new SimpleDateFormat("yyyy-MM-dd"))
				.format(new Date()), parent.getIntValueByKey("paymentid"), parent.getStringValueByKey("paymentname"), parent.getIntValueByKey("duedays"),
				parent.getBooleanValueByKey("paid"), parent.getStringValueByKey("paydate"), 0.0, parent.getStringValueByKey("paymenttext"), parent
				.getStringValueByKey("items"), parent.getIntValueByKey("shippingid"), parent.getStringValueByKey("shippingname"), parent
				.getDoubleValueByKey("shipping"), parent.getDoubleValueByKey("shippingvat"), parent.getStringValueByKey("shippingvatdescription"),
				parent.getIntValueByKey("shippingautovat"), parent.getDoubleValueByKey("total"), parent.getBooleanValueByKey("isdeposit"), parent.getDoubleValueByKey("deposit"), parent.getStringValueByKey("message"), parent
						.getIntValueByKey("transaction"), parent.getStringValueByKey("webshopid"), parent.getStringValueByKey("webshopdate"), parent
						.getStringValueByKey("orderdate"), parent.getBooleanValueByKey("novat"), parent.getStringValueByKey("novatname"), parent
						.getStringValueByKey("novatdescription"), parent.getIntValueByKey("netgross"),
						parent.getDoubleValueByKey("itemsdiscount"), parent.getIntValueByKey("dunninglevel"), parent
						.getIntValueByKey("invoiceid"), parent.getStringValueByKey("paymentdescription"), parent.getStringValueByKey("shippingdescription"),
						parent.getStringValueByKey("message2"),parent.getStringValueByKey("message3"));

		// Get the Items string, split it ..
		String itemsString = this.getStringValueByKey("items");
		String[] itemsStringParts = itemsString.split(",");

		// .. and get all the items.
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

				// Mark all items as "shared"
				DataSetItem item = Data.INSTANCE.getItems().getDatasetById(id);
				item.setBooleanValueByKey("shared", true);
				Data.INSTANCE.updateDataSet(item);
			}
		}
		
		if(parent.getSummary() != null) {
			this.summary = parent.getSummary();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 * @param deleted
	 * @param documentType
	 * @param addressid
	 * @param address
	 * @param deliveryaddress
	 * @param addressfirstline
	 * @param progress
	 * @param customerref
	 * @param consultant
	 * @param date
	 * @param servicedate
	 * @param paymentid
	 * @param paymentname
	 * @param duedays
	 * @param paid
	 * @param paydate
	 * @param payvalue
	 * @param paymenttext
	 * @param items
	 * @param shippingid
	 * @param shippingname
	 * @param shipping
	 * @param shippingvat
	 * @param shippingvatdescription
	 * @param shippingautovat
	 * @param total
	 * @param isdeposit
	 * @param deposit
	 * @param message
	 * @param transaction
	 * @param webshopid
	 * @param webshopdate
	 * @param orderdate
	 * @param noVat
	 * @param noVatName
	 * @param noVatDescription
	 * @param netgross
	 * @param itemsdiscount
	 * @param dunninglevel
	 * @param invoiceid
	 * @param paymentdescription
	 * @param shippingdescription
	 */
	public DataSetDocument(int id, String name, boolean deleted, DocumentType documentType, int addressid, String address, String deliveryaddress,
			String addressfirstline, int progress, String customerref, String consultant, String date, String servicedate, int paymentid, String paymentname, int duedays,
			boolean paid, String paydate, Double payvalue, String paymenttext, String items, int shippingid, String shippingname, Double shipping,
			Double shippingvat, String shippingvatdescription, int shippingautovat, Double total, boolean isdeposit, Double deposit, String message, int transaction, String webshopid,
			String webshopdate, String orderdate, boolean noVat, String noVatName, String noVatDescription, int netgross, double itemsdiscount, int dunninglevel,
			int invoiceid, String paymentdescription, String shippingdescription,
			String message2, String message3 ) {
		this.hashMap.put("id", new UniData(UniDataType.ID, id));
		this.hashMap.put("name", new UniData(UniDataType.STRING, name));
		this.hashMap.put("deleted", new UniData(UniDataType.BOOLEAN, deleted));
		this.hashMap.put("category", new UniData(UniDataType.INT, documentType.getInt()));
		this.hashMap.put("addressid", new UniData(UniDataType.ID, addressid));
		this.hashMap.put("address", new UniData(UniDataType.TEXT, address));
		this.hashMap.put("deliveryaddress", new UniData(UniDataType.TEXT, deliveryaddress));
		this.hashMap.put("addressfirstline", new UniData(UniDataType.STRING, addressfirstline));
		this.hashMap.put("progress", new UniData(UniDataType.INT, progress));
		this.hashMap.put("customerref", new UniData(UniDataType.STRING, customerref));
		this.hashMap.put("consultant", new UniData(UniDataType.STRING, consultant));
		this.hashMap.put("date", new UniData(UniDataType.DATE, date));
		this.hashMap.put("servicedate", new UniData(UniDataType.DATE, servicedate));
		this.hashMap.put("paymentid", new UniData(UniDataType.ID, paymentid));
		this.hashMap.put("paymentname", new UniData(UniDataType.STRING, paymentname));
		this.hashMap.put("paymentdescription", new UniData(UniDataType.TEXT, paymentdescription));
		this.hashMap.put("duedays", new UniData(UniDataType.INT, duedays));
		this.hashMap.put("paid", new UniData(UniDataType.BOOLEAN, paid));
		this.hashMap.put("paydate", new UniData(UniDataType.DATE, paydate));
		this.hashMap.put("payvalue", new UniData(UniDataType.PRICE, payvalue));
		this.hashMap.put("paymenttext", new UniData(UniDataType.TEXT, paymenttext));
		this.hashMap.put("items", new UniData(UniDataType.TEXT, items));
		this.hashMap.put("shippingid", new UniData(UniDataType.ID, shippingid));
		this.hashMap.put("shippingname", new UniData(UniDataType.STRING, shippingname));
		this.hashMap.put("shippingdescription", new UniData(UniDataType.STRING, shippingdescription));
		this.hashMap.put("shipping", new UniData(UniDataType.PRICE, shipping));
		this.hashMap.put("shippingvat", new UniData(UniDataType.PERCENT, shippingvat));
		this.hashMap.put("shippingvatdescription", new UniData(UniDataType.STRING, shippingvatdescription));
		this.hashMap.put("shippingautovat", new UniData(UniDataType.INT, shippingautovat));
		this.hashMap.put("total", new UniData(UniDataType.PRICE, total));
		this.hashMap.put("isdeposit", new UniData(UniDataType.BOOLEAN, isdeposit));
		this.hashMap.put("deposit", new UniData(UniDataType.PRICE, deposit));
		this.hashMap.put("message", new UniData(UniDataType.TEXT, message));
		this.hashMap.put("transaction", new UniData(UniDataType.INT, transaction));
		this.hashMap.put("webshopid", new UniData(UniDataType.STRING, webshopid));
		this.hashMap.put("webshopdate", new UniData(UniDataType.DATE, webshopdate));
		this.hashMap.put("orderdate", new UniData(UniDataType.DATE, orderdate));
		this.hashMap.put("novat", new UniData(UniDataType.BOOLEAN, noVat));
		this.hashMap.put("novatname", new UniData(UniDataType.STRING, noVatName));
		this.hashMap.put("novatdescription", new UniData(UniDataType.STRING, noVatDescription));
		this.hashMap.put("itemsdiscount", new UniData(UniDataType.PERCENT, itemsdiscount));
		this.hashMap.put("dunninglevel", new UniData(UniDataType.INT, dunninglevel));
		this.hashMap.put("invoiceid", new UniData(UniDataType.ID, invoiceid));
		this.hashMap.put("printed", new UniData(UniDataType.BOOLEAN, false));
		this.hashMap.put("printedtemplate", new UniData(UniDataType.STRING, ""));
		this.hashMap.put("message2", new UniData(UniDataType.TEXT, message2));
		this.hashMap.put("message3", new UniData(UniDataType.TEXT, message3));
		this.hashMap.put("odtpath", new UniData(UniDataType.STRING, ""));
		this.hashMap.put("pdfpath", new UniData(UniDataType.STRING, ""));
		this.hashMap.put("netgross", new UniData(UniDataType.INT, netgross));

		// Name of the table in the data base
		sqlTabeName = "Documents";
	}

	/**
	 * Get the payment state as localized string
	 * 
	 * @return String for "paid"
	 */
	public static String getStringPAID() {
		//T: Mark a paid document with this text.
		return _("paid");
	};

	/**
	 * Get the payment state as localized string
	 * 
	 * @return String for "unpaid"
	 */
	public static String getStringNOTPAID() {
		//T: Mark an unpaid document with this text.
		return _("unpaid");
	};
	
	/**
	 * Get the state whether the delivery note has an invoice or not as localized string
	 * 
	 * @return String for "has invoice"
	 */
	public static String getStringHASINVOICE() {
		//T: Mark an delivery note with this text.
		return _("has invoice");
	};
	/**
	 * Get the state whether the delivery note has an invoice or not as localized string
	 * 
	 * @return String for "has invoice"
	 */
	public static String getStringHASNOINVOICE() {
		//T: Mark an delivery note with this text.
		return _("has no invoice");
	};
	/**
	 * Get the shipping state as localized string
	 * 
	 * @return String for "shipped"
	 */
	public static String getStringSHIPPED() {
		//T: Mark an order with this text.
		return _("shipped");
	};

	/**
	 * Get the shipping state as localized string
	 * 
	 * @return String for "not shipped"
	 */
	public static String getStringNOTSHIPPED() {
		//T: Mark an order with this text.
		return _("not shipped");
	};

	/**
	 * Get the category as string
	 * 
	 * @return category as string
	 */
	public String getCategory() {
		try {
			String category = DocumentType.getPluralString(hashMap.get("category").getValueAsInteger());
			DocumentType documentType = DocumentType.getType(hashMap.get("category").getValueAsInteger());
			
			// use the document type to generate the category string ..
			switch (documentType) {
			case INVOICE:
			case CREDIT:
			case DUNNING:
				// .. the state of the payment ..
				if (this.hashMap.get("paid").getValueAsBoolean())
					category += "/" + DataSetDocument.getStringPAID();
				else
					category += "/" + DataSetDocument.getStringNOTPAID();
				break;
			case DELIVERY:
				// .. the state of the delivery document ..
				if (this.hashMap.get("invoiceid").getValueAsInteger() >=0 )
					category += "/" + DataSetDocument.getStringHASINVOICE();
				else
					category += "/" + DataSetDocument.getStringHASNOINVOICE();
				break;
			case ORDER:
				// .. and the state of the shipping
				switch (this.hashMap.get("progress").getValueAsInteger()) {
				case 0:
				case MarkOrderAsAction.PENDING:
				case MarkOrderAsAction.PROCESSING:
					category += "/" + DataSetDocument.getStringNOTSHIPPED();
					break;
				case MarkOrderAsAction.SHIPPED:
				case MarkOrderAsAction.COMPLETED:
					category += "/" + DataSetDocument.getStringSHIPPED();
					break;
				}
				break;
			}
			return category;
		}
		catch (Exception e) {
			Logger.logError(e, "Error getting key category.");
		}
		return "";
	}

	/**
	 * Get the category strings. Generate only categories of document types,
	 * that are existing. This is used to generate the tree in the documents
	 * view.
	 * 
	 * @param usedDocuments
	 *            Array with all document types, that are used
	 * @return Array with all category strings
	 */
	static public ArrayList<String> getCategoryStrings(boolean usedDocuments[]) {

		ArrayList<String> list = new ArrayList<String>();

		if (usedDocuments[DocumentType.LETTER.getInt()])
			list.add(DocumentType.LETTER.getPluralString());

		if (usedDocuments[DocumentType.OFFER.getInt()])
			list.add(DocumentType.OFFER.getPluralString());

		if (usedDocuments[DocumentType.ORDER.getInt()]) {
			// add shipping state
			list.add(DocumentType.ORDER.getPluralString() + "/" + getStringNOTSHIPPED());
			list.add(DocumentType.ORDER.getPluralString() + "/" + getStringSHIPPED());
		}

		if (usedDocuments[DocumentType.CONFIRMATION.getInt()])
			list.add(DocumentType.CONFIRMATION.getPluralString());

		if (usedDocuments[DocumentType.INVOICE.getInt()]) {
			// add payment state
			list.add(DocumentType.INVOICE.getPluralString() + "/" + getStringNOTPAID());
			list.add(DocumentType.INVOICE.getPluralString() + "/" + getStringPAID());
		}

		if (usedDocuments[DocumentType.DELIVERY.getInt()]) {
			// add state whether delivery not has a reference to an invoice or not
			list.add(DocumentType.DELIVERY.getPluralString()+ "/" + getStringHASINVOICE());
			list.add(DocumentType.DELIVERY.getPluralString()+ "/" + getStringHASNOINVOICE());
		}
		
		if (usedDocuments[DocumentType.CREDIT.getInt()]) {
			// add payment state
			list.add(DocumentType.CREDIT.getPluralString() + "/" + getStringNOTPAID());
			list.add(DocumentType.CREDIT.getPluralString() + "/" + getStringPAID());
		}

		if (usedDocuments[DocumentType.DUNNING.getInt()]) {
			// add payment state
			list.add(DocumentType.DUNNING.getPluralString() + "/" + getStringNOTPAID());
			list.add(DocumentType.DUNNING.getPluralString() + "/" + getStringPAID());
		}

		if (usedDocuments[DocumentType.PROFORMA.getInt()]) {
			list.add(DocumentType.PROFORMA.getPluralString());
		}

		return list;
	}

	/**
	 * Get all the document items. Generate the list by the items string
	 * 
	 * @return All items of this document
	 */
	public DataSetArray<DataSetItem> getItems() {
		DataSetArray<DataSetItem> items = new DataSetArray<DataSetItem>();

		// Split the items string
		String itemsString = this.getStringValueByKey("items");
		String[] itemsStringParts = itemsString.split(",");

		// Get all items
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
				items.getDatasets().add(Data.INSTANCE.getItems().getDatasetById(id));
			}
		}
		return items;
	}

	/**
	 * Recalculate the document total values
	 */
	public void calculate() {
		int sign = DocumentType.getType(this.getIntValueByKey("category")).sign();
		calculate(this.getItems(), this.getDoubleValueByKey("shipping") * sign, this.getDoubleValueByKey("shippingvat"),
				this.getStringValueByKey("shippingvatdescription"), this.getIntValueByKey("shippingautovat"), this.getDoubleValueByKey("itemsdiscount"),
				this.getBooleanValueByKey("novat"), this.getStringValueByKey("novatdescription"), 1.0, this.getIntValueByKey("netgross"), this.getDoubleValueByKey("deposit"));
	}

	/**
	 * Recalculate the document total values
	 * 
	 * @param items
	 *            Items as DataSetArray
	 * @param shippingNet
	 *            Net value
	 * @param shippingVat
	 *            Shipping vat
	 * @param shippingVatDescription
	 *            Shipping vat name
	 * @param shippingAutoVat
	 *            Way of calculating the shipping vat
	 * @param itemsDiscount
	 *            Discount
	 * @param noVat
	 *            True, if 0% vat is used
	 * @param noVatDescription
	 *            Name of the vat, if 0% is used
	 * @param netgross
	 *            should the values be rounded to optimal net or gross values
	 */
	public void calculate(DataSetArray<DataSetItem> items, double shippingNet, double shippingVat, String shippingVatDescription, int shippingAutoVat,
			Double itemsDiscount, boolean noVat, String noVatDescription, Double scaleFactor, int netgross, Double deposit) {
		summary.calculate(null, items, shippingNet, shippingVat, shippingVatDescription, shippingAutoVat, itemsDiscount, noVat, noVatDescription, scaleFactor, netgross,deposit);
	}

	/**
	 * Getter for the documents summary
	 * 
	 * @return Summary
	 */
	public DocumentSummary getSummary() {
		return this.summary;
	}

	/**
	 * Sets the state of the document to paid or unpaid Take the total value
	 * as paid value and the date of today.
	 * 
	 * @param paid
	 */
	public void setPaid(boolean paid) {
		this.setBooleanValueByKey("paid", paid);
		if (paid) {
			this.setStringValueByKey("paydate", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
			this.setDoubleValueByKey("payvalue", this.getDoubleValueByKey("total"));
		}
	}

	/**
	 * Test, if this is equal to an other UniDataSet Only web shop id and web
	 * shop date are compared
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
	public boolean isTheSameAs(UniDataSet uds) {
		if (!uds.getStringValueByKey("webshopid").equalsIgnoreCase(this.getStringValueByKey("webshopid")))
			return false;
		if (!uds.getStringValueByKey("webshopdate").equals(this.getStringValueByKey("webshopdate")))
			return false;
		return true;
	}


	/**
	 * Returns true, if billing and delivery address are equal
	 * 
	 * @return
	 * 	True, if both are equal
	 */
	public boolean deliveryAddressEqualsBillingAddress() {
		return this.getStringValueByKey("deliveryaddress").equalsIgnoreCase(this.getStringValueByKey("address"));
	}
}
