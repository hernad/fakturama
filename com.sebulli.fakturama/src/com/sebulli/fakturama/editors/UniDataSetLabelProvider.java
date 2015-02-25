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

package com.sebulli.fakturama.editors;

import org.eclipse.jface.viewers.LabelProvider;

import com.sebulli.fakturama.data.UniDataSet;

/**
 * Label provider for unidataset lists
 * 
 * A label provider implementation which, by default, uses an element's toString
 * value for its text and null for its image
 * 
 * @author Gerd Bartelt
 */
public class UniDataSetLabelProvider extends LabelProvider {

	// This entries of the UniDataSet will be displayed
	private String key = "name";

	/**
	 * Default constructor
	 */
	public UniDataSetLabelProvider() {
		key = "name";
	}

	/**
	 * Constructor with parameter to set the key
	 * 
	 * @param key
	 *            The key to the UniDataSet entry
	 * 
	 */
	public UniDataSetLabelProvider(String key) {
		this.key = key;
	}

	/**
	 * Returns the text string used to label the element, or null if there is no
	 * text label for the given object
	 * 
	 * Returns the name, and maybe the first name and the company
	 */
	@Override
	public String getText(Object element) {

		// The element is always an UniDataSet
		UniDataSet uds = (UniDataSet) element;

		String s = "";

		// Add the first name, if it exists
		if (uds.containsKey("firstname"))
			s += uds.getStringValueByKey("firstname") + " ";

		// Add always the key entry
		s += uds.getStringValueByKey(key);

		// Add the company, if it exists
		if (uds.containsKey("company"))
			s += " " + uds.getStringValueByKey("company");

		// Return the complete string
		return s;
	}

}
