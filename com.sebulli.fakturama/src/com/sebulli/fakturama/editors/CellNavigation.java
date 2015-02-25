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

import java.util.List;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import com.sebulli.fakturama.data.DataSetArray;
import com.sebulli.fakturama.views.datasettable.UniDataSetTableColumn;

/**
 * This class navigates between table viewer cells with the
 * TAB and CURSOR keys
 * 
 * @author Gerd Bartelt
 *
 */
public class CellNavigation {
	
	// Reference to all table columns
	private List<UniDataSetTableColumn> tableColumns;

	/**
	 * Constructor.
	 * 
	 * @param tableColumns
	 * 	List with all columns
	 * 
	 */
	public CellNavigation(List<UniDataSetTableColumn> tableColumns) {
		this.tableColumns = tableColumns;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> void selectNextCell(int keyCode, Object element, EditingSupport editingSupport,
			DataSetArray<T> items,TableViewer tableViewerItems ) {

		 int myColumn = -1; 
		 
		// Try to find the column number of the current cell
		// that is active
		for (int i=0; i< tableColumns.size(); i++){
			UniDataSetTableColumn col = tableColumns.get(i);
			EditingSupport es = (EditingSupport)col.getEditingSupport();
			
			// es must not be NULL
			if (es != null) {
				if (es.equals(editingSupport)) {
				myColumn = i;
				}
			}
		}
		
		// Exit, if my column was not found
		if (myColumn == -1)
			return;
		
		// Jump right
		if (keyCode == 9) {

			myColumn = myColumn + 1;

			// Select the next cell that supports TAB jumps
			for (int i=myColumn; i< tableColumns.size(); i++){
				UniDataSetTableColumn col = tableColumns.get(i);

				if (((ItemEditingSupport)col.getEditingSupport()).canJumpWithTabs()) {
					tableViewerItems.editElement(element, i);
					return;
				}

			}
			
			myColumn = 0;
			
		}

		Object newElement = null;
		
		// TAB can select the next row
		if (keyCode == SWT.TAB ) {
			newElement = items.getNextDataSet((T)element);
		}

		
		// Key up and down inactive, in multi-line controls 
		if (!((ItemEditingSupport)editingSupport).getMultiLineEditing()) {
			// Key down can select the next row
			if (keyCode == 16777218 /* KeyDown */) {
				newElement = items.getNextDataSet((T)element);
			}

			// Key up can select the previous row
			if (keyCode == 16777217/* KeyUp */) {
				newElement = items.getPreviousDataSet((T)element);
			}
			
		}


		
		if (newElement == null)
			return;

		// Start with the left column an the next row
		for (int i=myColumn; i< tableColumns.size(); i++){
			UniDataSetTableColumn col = tableColumns.get(i);
			if (col.getEditingSupport() != null && ((ItemEditingSupport)col.getEditingSupport()).canJumpWithTabs()) {
				tableViewerItems.editElement(newElement, i);
				return;
			}
		}
		
	}
	
}
