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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.sebulli.fakturama.calculate.Price;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetVoucherItem;
import com.sebulli.fakturama.data.DataSetList;
import com.sebulli.fakturama.data.DataSetVAT;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Item editing support for the item table of the document editor
 * 
 * @author Gerd Bartelt
 */
public class VoucherItemEditingSupport extends ItemEditingSupport {

	// Reference to this class
	private VoucherItemEditingSupport me;

	// The cell editor
	private CellEditor editor;
	private String[] categoryListEntries;

	// The current columns
	private int column;

	// The VAT combo
	private CCombo combo = null;
	private String carryString = "";

	// Text field "name"
	private Text text = null;
	
	private DataSetVoucherItem item = null;

	private Object activeObject;

	// The parent voucher editor that contains the item table
	private VoucherEditor voucherEditor;

	// Suggestion manager
	private Suggestion suggestion = null;
	
	/**
	 * Constructor Create support to edit the table entries.
	 * 
	 * @param documentEditor
	 *            The parent document editor that contains the item table
	 * @param viewer
	 *            The column viewer
	 * @param column
	 *            The column
	 */
	public VoucherItemEditingSupport(final VoucherEditor voucherEditor, ColumnViewer viewer, int column) {
		super(viewer);

		// Set the local variables
		this.voucherEditor = voucherEditor;
		this.column = column;
		me = this;


		// Create the correct editor based on the column index
		// Column no 2 and no 3 use a combo box cell editor.
		// The other columns a text cell editor.
		switch (column) {
		case 1:
			editor = new TextCellEditor(((TableViewer) viewer).getTable());
			text = (Text) editor.getControl();
			break;
		case 2:
			categoryListEntries = Data.INSTANCE.getListEntries().getStringsInCategory("name", "billing_accounts");
			editor = new ComboBoxCellEditor(((TableViewer) viewer).getTable(), categoryListEntries);
			combo = (CCombo) editor.getControl();

			combo.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {

					// Us the carryString, if it's not empty
					if (!carryString.isEmpty()) {

						// Use a 2nd string to prevent an event loop
						String carryString2 = carryString;
						carryString = "";
						combo.setText(carryString2);
					}

					// Get the content of the combo box
					String s = combo.getText();

					// Get list of all billing accounts
					ArrayList<DataSetList> billing_accounts = Data.INSTANCE.getListEntries().getActiveDatasetsByCategory("billing_accounts");

					// Search for the billing account with the same name as in the cell
					for (DataSetList billing_account : billing_accounts) {
						if (billing_account.getStringValueByKey("name").equalsIgnoreCase(s) && !s.isEmpty()) {

							// Get the VAT value from the billing account list
							String vatName = billing_account.getStringValueByKey("value");

							// Get the VAT entry with the same name
							DataSetVAT vat = Data.INSTANCE.getVATs().getDataSetByStringValue("name", vatName, DataSetVAT.getPurchaseTaxString());

							// Search also for the description
							if (vat == null)
								vat = Data.INSTANCE.getVATs().getDataSetByStringValue("description", vatName, DataSetVAT.getPurchaseTaxString());

							// Update the VAT cell in the table
							if (vat != null) {
								item.setIntValueByKey("vatid", vat.getIntValueByKey("id"));
								voucherEditor.getTableViewerItems().update(item, null);
							}

						}
					}

				}
			});
			break;
		case 3:
			editor = new ComboBoxCellEditor(((TableViewer) viewer).getTable(), Data.INSTANCE.getVATs().getStrings("name", DataSetVAT.getPurchaseTaxString()));
			break;
		default:
			editor = new TextCellEditor(((TableViewer) viewer).getTable());
		}
		
		// Add a Traverse Listener to the editor to navigate with
		// the tab and cursor keys
		Control c = editor.getControl();
		if (c != null) {
			c.addTraverseListener(new TraverseListener(){
	            public void keyTraversed(TraverseEvent e) {
	            	//e.doit = false;
	            	cancelAndSave();
	        		//select the next cell
	            	if (!(editor instanceof ComboBoxCellEditor) || e.keyCode == SWT.TAB)
	            		voucherEditor.selectNextCell(e.keyCode, activeObject, me);
	            };
			});
		}


	}

	
	
	/**
	 * Set suggestion handler
	 * 
	 */
	private void setSuggestionHandler () {

		switch (column) {
		case 1:
			suggestion = new Suggestion(text, voucherEditor.getVoucherItems().getStrings("name"));
			text.addVerifyListener(suggestion);
			break;
		case 2:
			suggestion = new Suggestion(combo, Data.INSTANCE.getListEntries().getStringsInCategory("name", "billing_accounts"));
			combo.addVerifyListener(suggestion);
			break;
		}
		
	}
	
	/**
	 * Specifies the columns with cells that are editable.
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {

		switch (this.column) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			return true;
		}
		return false;
	}

	/**
	 * Specifies the columns with cells that can be jumped by tab key.
	 * 
	 */
	@Override
	public boolean canJumpWithTabs() {

		switch (this.column) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			return true;
		}
		return false;
	}


	
	/**
	 * The editor to be shown
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	/**
	 * Get the value to set to the editor
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {

		setSuggestionHandler();
		Integer ix;
		String s;
		activeObject = element;
		voucherEditor.setItemEditing(this);

		item = (DataSetVoucherItem) element;
		switch (this.column) {
		case 1:
			return item.getFormatedStringValueByKey("name");
		case 2:

			// Get the index of that entry, that is equal to the category
			for (int i = 0; i < categoryListEntries.length; i++) {
				if (categoryListEntries[i].equals(item.getStringValueByKey("category")))
					return i;
			}

			// No entry found
			carryString = item.getStringValueByKey("category");
			return -1;

		case 3:
			// Get the name of the selected VAT and search the ComboBox for the VAT with the same name	
			ix = item.getIntValueByKey("vatid");
			s = Data.INSTANCE.getVATs().getDatasetById(ix).getStringValueByKey("description");
			String[] item_names = ((ComboBoxCellEditor) this.editor).getItems();
			for(ix = 0; ix < item_names.length; ix++) {
				if (item_names[ix].equals(s))
					return ix;
			}
			return 0;
		case 4:
			return item.getFormatedStringValueByKey("price");
		case 5:
			return DataUtils.CalculateGrossFromNet(item.getDoubleValueByKey("price"), item.getDoubleValueByKeyFromOtherTable("vatid.VATS:value"));
//			return new Price(item).getUnitGross().asFormatedString();
		}
		return "";
	}

	/**
	 * Sets the new value on the given element.
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		DataSetVoucherItem item = (DataSetVoucherItem) element;
		
		voucherEditor.setItemEditing(null);

		switch (this.column) {
		case 1:
			// Set the name
			item.setStringValueByKey("name", String.valueOf(value));
			break;

		case 2:
			// Get the selected item from the combo box
			Integer i = (Integer) value;

			// If there is an entry of the combo list selected
			if (i >= 0 && i < categoryListEntries.length)
				item.setStringValueByKey("category", categoryListEntries[i]);

			// If there is an entry with the same name as one of the combo list
			else {
				// get the text of the combo box
				String s = ((CCombo) editor.getControl()).getText();

				boolean found = false;

				// Search for the entry with the same value of the category
				for (int ii = 0; ii < categoryListEntries.length && !found; ii++) {
					String listEntry = categoryListEntries[ii];
					if (listEntry.equals(s)) {
						item.setStringValueByKey("category", listEntry);
						found = true;
					}
				}

				// No entry found
				if (!found)
					item.setStringValueByKey("category", s);

			}
			break;
		case 3:
			// Set the VAT

			// Get the selected item from the combo box
			i = (Integer) value;
			String s;

			// Get the VAT by the selected name
			if (i >= 0) {
				s = ((ComboBoxCellEditor) this.editor).getItems()[i];
				i = Data.INSTANCE.getVATs().getDataSetIDByStringValue("name", s, DataSetVAT.getPurchaseTaxString());
			}
			// Get the VAT by the Value in percent
			else {
				s = ((CCombo) ((ComboBoxCellEditor) this.editor).getControl()).getText();
				i = Data.INSTANCE.getVATs().getDataSetByDoubleValue("value", DataUtils.StringToDouble(s + "%"), DataSetVAT.getPurchaseTaxString());
			}

			// If no VAT is found, use the standard VAT
			if (i < 0)
				i = Integer.parseInt(Data.INSTANCE.getProperty("standardvat"));
			item.setIntValueByKey("vatid", i);
			break;
		case 4:
			// Net price
			item.setStringValueByKey("price", String.valueOf(value));
			break;
		case 5:
			// Gross price
			item.setDoubleValueByKey("price", new Price(DataUtils.StringToDouble((String) value), item.getDoubleValueByKeyFromOtherTable("vatid.VATS:value"),
					false, true).getUnitNet().asDouble());
			break;
		default:
			break;
		}

		this.voucherEditor.checkDirty();

		// Update the data
		getViewer().update(element, null);
	}


	/**
	 * Cancel editing of this cell
	 */
	public void cancelAndSave() {
		this.setValue(activeObject, this.editor.getValue());

	}

}
