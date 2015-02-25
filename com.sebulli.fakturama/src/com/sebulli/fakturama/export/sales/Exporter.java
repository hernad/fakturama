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

package com.sebulli.fakturama.export.sales;

import static com.sebulli.fakturama.Translate._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.calculate.PriceValue;
import com.sebulli.fakturama.calculate.VatSummaryItem;
import com.sebulli.fakturama.calculate.VatSummarySetManager;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.data.DataSetExpenditureVoucher;
import com.sebulli.fakturama.data.UniDataSetSorter;
import com.sebulli.fakturama.export.CellFormatter;
import com.sebulli.fakturama.export.OOCalcExporter;
import com.sebulli.fakturama.misc.DataUtils;


/**
 * This class exports all invoices in an OpenOffice.org 
 * Calc table. 
 * 
 * @author Gerd Bartelt
 */
public class Exporter extends OOCalcExporter{


	
	// Settings from the preference page
	private boolean showZeroVatColumn;

	
	/**
	 * Constructor Sets the begin and end date
	 * 
	 * @param startDate
	 *            Begin date
	 * @param endDate
	 *            Begin date
	 */
	public Exporter(GregorianCalendar startDate, GregorianCalendar endDate,
			 boolean doNotUseTimePeriod, boolean showZeroVatColumn, boolean exportPaid) {
		super(startDate, endDate,doNotUseTimePeriod);
		this.showZeroVatColumn = showZeroVatColumn;
		this.exportPaid = exportPaid;
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
		// Get all undeleted expenditures
		ArrayList<DataSetExpenditureVoucher> expenditures = Data.INSTANCE.getExpenditureVouchers().getActiveDatasets();

		// Sort the documents by the pay date
		Collections.sort(documents, new UniDataSetSorter(documentDateKey));

		// Sort the expenditures by category and date
		Collections.sort(expenditures, new UniDataSetSorter("category", "date"));


		// Count the columns that contain a VAT and net value 
		int columnsWithVatHeading = 0;
		int columnsWithNetHeading = 0;

		// Count the columns that contain a VAT value of 0% 
		int zeroVatColumns = 0;

		// Fill the first 4 rows with the company information
		fillCompanyInformation(0);
		fillTimeIntervall(5);
		
		// Counter for the current row and columns in the Calc document
		int row = 9;
		int col = 0;

		// Table heading
		if (this.exportPaid)
			//T: Sales Exporter - Text in the Calc document for the Earnings
			setCellTextInBold(row++, 0, _("Earnings"));
		else
			//T: Sales Exporter - Text in the Calc document for the Earnings
			setCellTextInBold(row++, 0, _("Unpaid invoices"));
		
		
		row++;

		// Create a VAT summary set manager that collects all VAT
		// values of all documents
		VatSummarySetManager vatSummarySetAllDocuments = new VatSummarySetManager();

		// Table column headings
		int headLine = row;
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Pay Date"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Invoice Nr."));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Invoice Date"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("First Name"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Last Name"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Company"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("VAT ID."));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Country"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Invoice Value"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Pay Value"));
		//T: Used as heading of a table. Keep the word short.
		setCellTextInBold(row, col++, _("Net Value"));
		row++;

		// The documents are exported in 2 runs.
		// First, only the VAT summary of all documents is calculated and
		// the columns are created.
		// Later all the documents are analyzed a second time and then they
		// are exported document by document into the table.
		for (DataSetDocument document : documents) {

			if (documentShouldBeExported(document)) {
				document.calculate();
				vatSummarySetAllDocuments.add(document, 1.0);
			}
		}

		col = 11;
		columnsWithVatHeading = 0;
		columnsWithNetHeading = 0;
		boolean vatIsNotZero = false;

		// A column for each Vat value is created 
		// The VAT summary items are sorted. So first ignore the VAT entries
		// with 0%. 
		// If the VAT value is >0%, create a column with heading.
		for (Iterator<VatSummaryItem> iterator = vatSummarySetAllDocuments.getVatSummaryItems().iterator(); iterator.hasNext();) {
			VatSummaryItem item = iterator.next();

			// Create a column, if the value is not 0%
			if ((item.getVat().doubleValue() > 0.001) || vatIsNotZero || showZeroVatColumn) {

				// If the first non-zero VAT column is created,
				// do not check the value any more.
				vatIsNotZero = true;

				// Count the columns
				columnsWithVatHeading++;

				// Create a column heading in bold
				int column = vatSummarySetAllDocuments.getIndex(item) - zeroVatColumns;
				setCellTextInBold(headLine, column + col, item.getVatName());

			}
			else
				// Count the columns with 0% VAT
				zeroVatColumns++;
		}

		// A column for each Net value is created 
		// The Net summary items are sorted. 
		for (Iterator<VatSummaryItem> iterator = vatSummarySetAllDocuments.getVatSummaryItems().iterator(); iterator.hasNext();) {
			VatSummaryItem item = iterator.next();

			// Count the columns
			columnsWithNetHeading++;

			// Create a column heading in bold
			int column = vatSummarySetAllDocuments.getIndex(item);
			setCellTextInBold(headLine, columnsWithVatHeading + column + col, "Netto \n" + item.getVatName());
		}

		// Second run.
		// Export the document data
		for (DataSetDocument document : documents) {

			if (documentShouldBeExported(document)) {

				// Now analyze document by document
				VatSummarySetManager vatSummarySetOneDocument = new VatSummarySetManager();
				document.calculate();

				// Calculate the relation between paid value and the value
				// of the invoice. This is used to calculate the VAT.
				// Example.
				// The net sum of the invoice is 100€.
				// Plus 20% VAT: +20€ = Total: 120€.
				//
				// The customer pays only 115€.
				// 
				// Then the paidFactor is 115/120 = 0.9583333..
				// The VAT value in the invoice is also scaled by this 0.958333...
				// to 19.17€
				Double paidFactor = document.getDoubleValueByKey("payvalue") / document.getDoubleValueByKey("total");

				// Use the paid value
				vatSummarySetOneDocument.add(document, paidFactor);

				// Fill the row with the document data
				col = 0;
				setCellText(row, col++, DataUtils.DateAsLocalString(document.getStringValueByKey("paydate")));
				setCellText(row, col++, document.getStringValueByKey("name"));
				setCellText(row, col++, DataUtils.DateAsLocalString(document.getStringValueByKey("date")));
				int addressid = document.getIntValueByKey("addressid");

				// Fill the address columns with the contact that corresponds to the addressid
				if (addressid >= 0) {
					setCellText(row, col++, document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:firstname"));
					setCellText(row, col++, document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:name"));
					setCellText(row, col++, document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:company"));
					setCellText(row, col++, document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:vatnr"));
					setCellText(row, col++, document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:country"));
				}
				// ... or use the documents first line
				else {
					setCellText(row, col++, document.getStringValueByKey("addressfirstline"));
					col += 4;
				}

				setCellValueAsLocalCurrency(row, col++, document.getDoubleValueByKey("total"));
				setCellValueAsLocalCurrency(row, col++, document.getDoubleValueByKey("payvalue"));

				// Calculate the total VAT of the document
				PriceValue totalVat = new PriceValue(0.0);

				// Get all VAT entries of this document and place them into the
				// corresponding column.
				for (Iterator<VatSummaryItem> iterator = vatSummarySetOneDocument.getVatSummaryItems().iterator(); iterator.hasNext();) {
					VatSummaryItem item = iterator.next();

					// Get the column
					int column = vatSummarySetAllDocuments.getIndex(item) - zeroVatColumns;

					// If column is <0, it was a VAT entry with 0%
					if (column >= 0) {

						// Round the VAT and add fill the table cell
						PriceValue vat = new PriceValue(item.getVat());
						totalVat.add(vat.asRoundedDouble());
						setCellValueAsLocalCurrency(row, column + (col + 1), vat.asRoundedDouble());
					}
				}

				// Get all net entries of this document and place them into the
				// corresponding column.
				for (Iterator<VatSummaryItem> iterator = vatSummarySetOneDocument.getVatSummaryItems().iterator(); iterator.hasNext();) {
					VatSummaryItem item = iterator.next();

					// Get the column
					int column = vatSummarySetAllDocuments.getIndex(item);

					// If column is <0, it was a VAT entry with 0%
					if (column >= 0) {

						// Round the net and add fill the table cell
						PriceValue net = new PriceValue(item.getNet());
						//totalVat.add(net.asRoundedDouble());
						setCellValueAsLocalCurrency(row, columnsWithVatHeading + column + (col + 1), net.asRoundedDouble());
					}
				}

				// Calculate the documents net total (incl. shipping) 
				// by the documents total value and the sum of all VAT values.
				Double net = document.getDoubleValueByKey("payvalue") - totalVat.asRoundedDouble();
				setCellValueAsLocalCurrency(row, col++, net);

				// Calculate the documents net total (incl. shipping)
				// a second time, but now use the documents net value,
				// and scale it by the scale factor.
				Double totalNet = document.getSummary().getTotalNet().asDouble();
				//totalNet += document.getSummary().getShipping().getUnitNet().asDouble();

				Double roundingError = totalNet * paidFactor - net;

				// Normally both results must be equal.
				// If the difference is grater than 1 Cent, display a warning.
				// It could be a rounding error.
				if (Math.abs(roundingError) > 0.01)
					setCellTextInRedBold(row, col + columnsWithVatHeading + columnsWithNetHeading, "Runden prüfen");

				// Set the background of the table rows. Use an light and
				// alternating blue color.
				if ((row % 2) == 0)
					setBackgroundColor( 0, row, col + columnsWithVatHeading + columnsWithNetHeading - 1, row, 0x00e8ebed);

				row++;
			}
		}

		// Insert a formula to calculate the sum of a column.
		// "sumrow" is the row under the table.
		int sumrow = row;


		int startColumn;

		// If paid documents are exported,
		if (this.exportPaid)
			// show also sum of columns with net value
			startColumn = -1;
		else
			// show also sum of columns with net value, paid value and value on invoice
			startColumn = -3;
		
		
		// Show the sum only, if there are values in the table
		if (sumrow > (headLine + 1)) {
			for (int i = startColumn; i < (columnsWithVatHeading + columnsWithNetHeading); i++) {
				col = 11 + i;
				try {
					// Create formula for the sum. 
					String cellNameBegin = CellFormatter.getCellName(headLine + 1, col);
					String cellNameEnd = CellFormatter.getCellName(row - 1, col);
					setFormula(col, sumrow, "=SUM(" + cellNameBegin + ":" + cellNameEnd + ")");
					setBold(sumrow, col);
				}
				catch (IndexOutOfBoundsException e) {
				}
			}
		}

		// Draw a horizontal line (set the border of the top and the bottom
		// of the table).
		for (col = 0; col < (columnsWithVatHeading + columnsWithNetHeading) + 11; col++) {
			setBorder(headLine, col, 0x000000, false, false, true, false);
			setBorder(sumrow, col, 0x000000, true, false, false, false);
		}

		// True = Export was successful
		return true;
	}


}
