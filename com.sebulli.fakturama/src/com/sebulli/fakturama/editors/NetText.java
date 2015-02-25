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

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.sebulli.fakturama.data.UniData;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Controls a text widget that contains the net value of a price. This control
 * interacts with a GrossText control, that contains the gross value. If the
 * value of this control is changes, also the corresponding gross control is
 * modified.
 * 
 * @author Gerd Bartelt
 */
public class NetText {

	// The  net value
	private UniData netValue;

	// VAT value as factor
	private Double vatValue;

	// The text control 
	private Text netText;

	// The corresponding text control that contains the gross value
	private Text grossText;

	/**
	 * Constructor that creates the text widget and connects it with the
	 * corresponding net widget.
	 * 
	 * @param editor
	 *            The editor that contains this widget.
	 * @param parent
	 *            The parent control.
	 * @param style
	 *            Style of the text widget
	 * @param net
	 *            The net value
	 * @param vat
	 *            The vat value ( factor )
	 */
	public NetText(final Editor editor, Composite parent, int style, UniData net, Double vat) {

		// Set the local variables
		this.netValue = net;
		this.vatValue = vat;

		// Create the text widget
		this.netText = new Text(parent, style);
		netText.setText(DataUtils.DoubleToFormatedPrice(netValue.getValueAsDouble()));

		// Set the text of the NetText, based on the GrossText's value.
		// Do this, if the text widget is selected (If "ENTER" is pressed).
		netText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				netText.setText(DataUtils.DoubleToFormatedPrice(netValue.getValueAsDouble()));
				editor.checkDirty();
			}
		});

		// Set the text of the GrossText, based on the NetText's value
		netText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (netText.isFocusControl()) {
					DataUtils.CalculateGrossFromNet(netText, grossText, vatValue, netValue);
					editor.checkDirty();
				}
			}
		});

	}

	/**
	 * Set the visibility of the text widget.
	 * 
	 * @param visible
	 *            True, if visible
	 */
	public void setVisible(boolean visible) {
		netText.setVisible(visible);
	}

	/**
	 * Get a reference of the gross text widget
	 * 
	 * @return The text widget.
	 */
	public Text getGrossText() {
		return this.grossText;
	}

	/**
	 * Set a reference to the gross text widget
	 * 
	 * @param grossT
	 *            The gtoss text widget
	 */
	public void setGrossText(Text grossT) {
		this.grossText = grossT;
	}

	/**
	 * Update the Vat factor.
	 * 
	 * @param vatValue
	 *            The Vat value as factor.
	 */
	public void setVatValue(Double vatValue) {
		this.vatValue = vatValue;
	}

	/**
	 * Get a reference of the text widget
	 * 
	 * @return The net text widget.
	 */
	public Text getNetText() {
		return netText;
	}

}
