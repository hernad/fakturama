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

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;

import com.sebulli.fakturama.calculate.Price;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetItem;
import com.sebulli.fakturama.data.DataSetProduct;
import com.sebulli.fakturama.data.DataSetVAT;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Item editing support for the item table of the document editor
 * 
 * @author Gerd Bartelt
 */
public class DocumentItemEditingSupport extends ItemEditingSupport {

	private boolean multiLineEditing = false;
	
	// Public column enum
	public static enum Column {
		OPTIONAL, QUANTITY, QUNIT, ITEMNR, PICTURE, NAME, DESCRIPTION,
		VAT, PRICE, DISCOUNT, TOTAL
	}
	
	// Reference to this class
	private DocumentItemEditingSupport me;

	// The cell editor
	private CellEditor editor;

	// The current columns
	private Column column;

	// The active Object
	private Object activeObject;

	// The parent document editor that contains the item table
	private DocumentEditor documentEditor;

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
	public DocumentItemEditingSupport(final DocumentEditor documentEditor, ColumnViewer viewer, final Column column) {
		super(viewer);

		// Set the local variables
		this.documentEditor = documentEditor;
		this.column = column;
		me = this;
		multiLineEditing = false;
		
		// Create the correct editor based on the column index
		// Column nr.6 uses a combo box cell editor.
		// The other columns a text cell editor.
		switch (column) {
		
		case OPTIONAL:
			// No Editor
			editor = new EmptyCellEditor(((TableViewer) viewer).getTable());
			break;
		case PICTURE:
			// Editor for the preview picture
			editor = new PictureViewEditor(((TableViewer) viewer).getTable());
			break;
		case VAT:
			editor = new ComboBoxCellEditor(((TableViewer) viewer).getTable(), Data.INSTANCE.getVATs().getStrings("name", DataSetVAT.getSalesTaxString()));
			break;
		case DESCRIPTION:
			// Multi line editor
			editor = new TextCellEditor(((TableViewer) viewer).getTable(),SWT.MULTI);
			multiLineEditing = true;
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
	            		documentEditor.selectNextCell(e.keyCode, activeObject, me);
	            };
			});
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
		case OPTIONAL:
		case QUANTITY:
		case QUNIT:
		case ITEMNR:
		case PICTURE:
		case NAME:
		case DESCRIPTION:
		case VAT:
		case PRICE:
		case DISCOUNT:
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
		case OPTIONAL:
		case QUANTITY:
		case QUNIT:
		case ITEMNR:
		case NAME:
		case DESCRIPTION:
		case VAT:
		case PRICE:
		case DISCOUNT:
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

		activeObject = element;
		documentEditor.setItemEditing(this);

		DataSetItem item = (DataSetItem) element;
		switch (this.column) {
		case OPTIONAL:
			//Toggle the value
			boolean optional = !item.getBooleanValueByKey("optional");
			this.setValue(activeObject, optional);
			return optional;
		case QUANTITY:
			return item.getFormatedStringValueByKey("quantity");
		case QUNIT:
			return item.getFormatedStringValueByKey("qunit");
		case ITEMNR:
			return item.getStringValueByKey("itemnr");
		case PICTURE:
			// Open a preview dialog
			((PictureViewEditor)editor).openPreview(item.getStringValueByKey("picturename"));
			return "";
		case NAME:
			return item.getStringValueByKey("name");
		case DESCRIPTION:
			return DataUtils.makeOSLineFeeds(item.getStringValueByKey("description"));
		case VAT:
			return item.getIntValueByKey("vatid");
		case PRICE:
			if (documentEditor.getUseGross())
				return new Price(item).getUnitGross().asFormatedString();
			else
				return new Price(item).getUnitNet().asFormatedString();
		case DISCOUNT:
			return item.getFormatedStringValueByKey("discount");
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
		DataSetItem item = (DataSetItem) element;

		documentEditor.setItemEditing(null);

		switch (this.column) {
		case OPTIONAL:
			// Set the optional flag
			item.setBooleanValueByKey("optional", (Boolean)value);
			break;
		case QUANTITY:
			
			Double oldQuanity = item.getDoubleValueByKey("quantity");
			
			// Set the quantity
			item.setStringValueByKey("quantity", String.valueOf(value));
			int productId = item.getIntValueByKey("productid");

			// If the item is coupled with a product, get the graduated price
			if (productId >= 0) {

				DataSetProduct product = Data.INSTANCE.getProducts().getDatasetById(productId);
				
				// Compare the price. Is it equal to the price of the product,
				// then use the product price.
				// If the price is not equal, it was modified. In this case, do not
				// modify the price value.
				Double oldPrice = item.getDoubleValueByKey("price");
				Double oldPriceByQuantity = product.getPriceByQuantity(oldQuanity);
				Double newPrice = product.getPriceByQuantity(DataUtils.StringToDouble(String.valueOf(value)));

				if (DataUtils.DoublesAreEqual(oldPrice, oldPriceByQuantity))
					// Do not use 0.00€
					//if (!DataUtils.DoublesAreEqual(newPrice, 0.0))
					item.setDoubleValueByKey("price", newPrice);
			}
			
			break;
		case QUNIT:
			// Set the quanity unit
			item.setStringValueByKey("qunit", String.valueOf(value));
			break;
		case ITEMNR:
			// Set the item number
			item.setStringValueByKey("itemnr", String.valueOf(value));
			break;
		case NAME:
			// Set the name
			item.setStringValueByKey("name", String.valueOf(value));
			break;
		case DESCRIPTION:
			// Set the description
			item.setStringValueByKey("description", DataUtils.removeCR(String.valueOf(value)));
			break;
		case VAT:
			// Set the VAT

			// Get the selected item from the combo box
			Integer i = (Integer) value;
			String s;

			// Get the VAT by the selected name
			if (i >= 0) {
				s = ((ComboBoxCellEditor) this.editor).getItems()[i];
				i = Data.INSTANCE.getVATs().getDataSetIDByStringValue("name", s, DataSetVAT.getSalesTaxString());
			}
			// Get the VAT by the Value in percent
			else {
				s = ((CCombo) ((ComboBoxCellEditor) this.editor).getControl()).getText();
				i = Data.INSTANCE.getVATs().getDataSetByDoubleValue("value", DataUtils.StringToDouble(s + "%"), DataSetVAT.getSalesTaxString());
			}

			// If no VAT is found, use the standard VAT
			if (i < 0)
				i = Integer.parseInt(Data.INSTANCE.getProperty("standardvat"));

			// Set the vat and store the vat value before and after the modification.
			Double oldVat = 1.0 + item.getDoubleValueByKeyFromOtherTable("vatid.VATS:value");
			item.setVat(i);
			Double newVat = 1.0 + item.getDoubleValueByKeyFromOtherTable("vatid.VATS:value");

			// Modify the net value that the gross value stays constant.
			if (documentEditor.getUseGross())
				item.setDoubleValueByKey("price", oldVat / newVat * item.getDoubleValueByKey("price"));

			break;
		case PRICE:
			boolean useGross = documentEditor.getUseGross();
			String priceString = (String) value;
			
			// If the price is taged with an "Net" or "Gross", force this
			// value to a net or gross value
			//T: Tag to mark a price as net or gross
			if (priceString.toLowerCase().contains((_("Net")).toLowerCase()))
				useGross = false;
			//T: Tag to mark a price as net or gross
			if (priceString.toLowerCase().contains((_("Gross")).toLowerCase()))
				useGross = true;
			
			// Set the price as gross or net value.
			// If the editor displays gross values, calculate the net value,
			// because only net values are stored.
			if (useGross)
				item.setDoubleValueByKey("price",
						new Price(DataUtils.StringToDouble(priceString), item.getDoubleValueByKey("vatvalue"), item.getBooleanValueByKey("novat"), true)
								.getUnitNet().asDouble());
			else
				item.setStringValueByKey("price", String.valueOf(priceString));
			break;
		case DISCOUNT:
			// Set the discount value
			Double d = DataUtils.StringToDoubleDiscount(String.valueOf(value));
			item.setDoubleValueByKey("discount", d);
			break;
		default:
			break;
		}

		// Recalculate the total sum of the document
		documentEditor.calculate();

		// Update the data
		getViewer().update(element, null);
	}

	/**
	 * Cancel editing of this cell
	 */
	public void cancelAndSave() {
		this.setValue(activeObject, this.editor.getValue());
	}

	/**
	 * Getter for multi line editing
	 * 
	 * @return
	 *  True, if it is a control that suports multi line editing
	 */
	public boolean getMultiLineEditing () {
		return this.multiLineEditing;
		
	}

	
}
