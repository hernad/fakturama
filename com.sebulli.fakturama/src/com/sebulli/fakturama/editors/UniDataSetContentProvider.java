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

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sebulli.fakturama.data.UniDataSet;

/**
 * Content provider for all lists with unidatasets.
 * 
 * @author Gerd Bartelt
 */
public class UniDataSetContentProvider implements IStructuredContentProvider {

	/**
	 * Returns the elements to display in the viewer when its input is set to
	 * the given element.
	 * 
	 * Only those elements are returned, that are not marked as "deleted"
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {

		// Create 2 lists. One with all entries and one with only
		// those entries which are not marked as "deleted".
		ArrayList<UniDataSet> contentFiltered = new ArrayList<UniDataSet>();
		ArrayList<UniDataSet> content = (ArrayList<UniDataSet>) inputElement;

		// Copy only the "undeleted" entries to the final list.
		for (UniDataSet uds : content) {
			if (!uds.getBooleanValueByKey("deleted")) {
				contentFiltered.add(uds);
			}

		}

		// Return an array with only undeleted entries
		return contentFiltered.toArray();
	}

	/**
	 * Disposes of this content provider.
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * Notifies this content provider that the given viewer's input has been
	 * switched to a different element.
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged
	 *      (org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
	}

}
