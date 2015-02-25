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

package com.sebulli.fakturama.views.datasettable;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.sebulli.fakturama.data.UniDataSet;

/**
 * Sorter to reorder the elements of the UniDataSet table
 * 
 * @author Gerd Bartelt
 * 
 */
public class TableSorter extends ViewerSorter {

	private boolean descending = false;
	private String dataKey;
	private boolean isNumeric = false;
	private boolean isDate = false;

	/**
	 * Constructor Set the default order to "descending"
	 */
	public TableSorter() {
		this.descending = false;
		this.dataKey = "";
	}

	// Set the data key to use for the sorter
	public void setDataKey(UniDataSet uds, String dataKey) {

		// If it is the same data key (the same table column) like last
		// time, then toggle the sort order.
		if (this.dataKey.equals(dataKey)) {
			descending = !descending;
		}
		// Else start with a descending order and use the new data key
		else {
			this.dataKey = dataKey;
			this.descending = false;
		}

		// Detect, if there are date or a numeric values in the table column.
		isDate = UniDataSetTableColumn.isDate(uds, dataKey);
		isNumeric = UniDataSetTableColumn.isNumeric(uds, dataKey);
	}

	/**
	 * Returns a negative, zero, or positive number depending on whether the
	 * first element is less than, equal to, or greater than the second element.
	 * 
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		// There is not data key set, so do not sort
		if (dataKey.isEmpty())
			return 0;

		// Compare two elements
		UniDataSet uds1 = (UniDataSet) e1;
		UniDataSet uds2 = (UniDataSet) e2;
		int retval = 0;

		if (isDate) {

			// If it a date, compare the strings
			retval = uds1.getStringValueByKey(dataKey).compareTo(uds2.getStringValueByKey(dataKey));
		}
		else if (isNumeric)

			// If it's a numeric value, compare the formated double value
			retval = UniDataSetTableColumn.getDoubleValue(uds1, dataKey).compareTo(UniDataSetTableColumn.getDoubleValue(uds2, dataKey));
		else

			// Else compare the formated strings
			retval = UniDataSetTableColumn.getText(uds1, dataKey).compareTo(UniDataSetTableColumn.getText(uds2, dataKey));

		// Change the order
		if (!this.descending) {
			retval = -retval;
		}

		// The compare result
		return retval;
	}

}
