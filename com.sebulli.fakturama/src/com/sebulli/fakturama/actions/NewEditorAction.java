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

package com.sebulli.fakturama.actions;

import org.eclipse.jface.action.Action;

import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.editors.Editor;

/**
 * Parent class for all newXX actions.
 * 
 * Stores the information about the category or the parent editor.
 * 
 * @author Gerd Bartelt
 */
public class NewEditorAction extends Action {

	// category String
	protected String category = "";

	// Parent UniDataSet
	protected UniDataSet parent = null;

	// Parent Editor
	protected Editor parentEditor = null;

	/**
	 * Default constructor
	 * 
	 * @param text
	 *            Name of the action
	 */
	public NewEditorAction(String text) {
		super(text);
	}

	/**
	 * Constructor with additional parameter "category"
	 * 
	 * @param text
	 *            Name of the action
	 * @param category
	 *            Category of the new action
	 */
	public NewEditorAction(String text, String category) {
		super(text);
		if (category != null)
			this.category = category;
	}

	/**
	 * Constructor with a 2nd additional parameter "parentEditor"
	 * 
	 * @param text
	 *            Name of the action
	 * @param category
	 *            Category of the new action
	 * @param parentEditor
	 *            Parent editor, which is duplicated
	 */
	public NewEditorAction(String text, String category, Editor parentEditor) {
		super(text);
		if (category != null)
			this.category = category;
		this.parentEditor = parentEditor;

	}

	/**
	 * Setter for the property "category"
	 * 
	 * @param category
	 *            The category of the new action.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
