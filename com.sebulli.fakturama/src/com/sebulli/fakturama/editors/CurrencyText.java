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

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.sebulli.fakturama.data.UniData;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * This class contains a SWT text widget and some methods to format the text as
 * a currency value.
 * 
 * @author Gerd Bartelt
 */
public class CurrencyText {

	// UniData object, that is modified, if the text changes.
	private UniData value;
	// The SWT text widget
	private Text text;

	/**
	 * Constructor Create a new text widget and add listeners
	 * 
	 * @param editor
	 *            The editor (is used to call the checkDirty method)
	 * @param parent
	 *            The parent composite in which the widget is placed.
	 * @param style
	 *            The text's style
	 * @param parvalue
	 *            The corresponding UniData object
	 */
	public CurrencyText(final Editor editor, Composite parent, int style, UniData parvalue) {

		// Set the local reference to the UniData object
		value = parvalue;

		// Create the SWT text widget
		this.text = new Text(parent, style);
		this.text.setText(DataUtils.DoubleToFormatedPrice(this.value.getValueAsDouble()));

		//Update the value on 'ENTER'
		text.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.keyCode == 13) {
						value.setValue(DataUtils.StringToDouble(text.getText()));
						update();
						if (editor != null)
							editor.checkDirty();
					}
				}
			});

		
		// Add a selection listener
		text.addSelectionListener(new SelectionAdapter() {

			/**
			 * Sent when default selection occurs in the control The content of
			 * the text widget is formated, if the used has pressed the enter
			 * key.
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				update();
				if (editor != null)
					editor.checkDirty();
			}

		});

		// Add a focus listener
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			/**
			 * Sent when a control loses focus
			 * 
			 * The content of the text widget is formated, if the focus is lost.
			 * 
			 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			@Override
			public void focusLost(FocusEvent e) {
				value.setValue(DataUtils.StringToDouble(text.getText()));
				update();
				if (editor != null)
					editor.checkDirty();
			}

		});
	}

	/**
	 * Get the text control
	 * 
	 * @return The SWT text control
	 */
	public Text getText() {
		return text;
	}
	
	/**
	 * Sets the tool tip text
	 * 
	 * @param toolTip
	 * 			The tool tip text
	 */
	public void setToolTipText(String toolTip) {
		text.setToolTipText(toolTip);
	}
	
	/**
	 * Update the text widget with the data
	 */
	public void update () {
		String s = DataUtils.DoubleToFormatedPrice(value.getValueAsDouble());
		if (!s.equals(text.getText()))
			text.setText(s);
	}

}
