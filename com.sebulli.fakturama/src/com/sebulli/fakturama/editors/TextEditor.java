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

import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetText;
import com.sebulli.fakturama.views.datasettable.ViewTextTable;

/**
 * The text editor
 * 
 * @author Gerd Bartelt
 */
public class TextEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.textEditor";

	// This UniDataSet represents the editor's input 
	private DataSetText text;

	// SWT widgets of the editor
	private Composite top;
	private Text textName;
	private Text textText;
	private Combo comboCategory;

	// defines, if the text is new created
	private boolean newText;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public TextEditor() {
		tableViewID = ViewTextTable.ID;
		editorID = "text";
	}

	/**
	 * Saves the contents of this part
	 * 
	 * @param monitor
	 *            Progress monitor
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		/*
		 * the following parameters are not saved:
		 * - id (constant)
		 */

		// Always set the editor's data set to "undeleted"
		text.setBooleanValueByKey("deleted", false);

		// Set the text data
		text.setStringValueByKey("name", textName.getText());
		text.setStringValueByKey("text", textText.getText());
		text.setStringValueByKey("category", comboCategory.getText());

		// If it is a new text, add it to the text list and
		// to the data base
		if (newText) {
			text = Data.INSTANCE.getTexts().addNewDataSet(text);
			newText = false;
		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getTexts().updateDataSet(text);
		}

		// Set the Editor's name to the shipping name.
		setPartName(text.getStringValueByKey("name"));

		// Refresh the table view of all texts
		refreshView();
		checkDirty();
	}

	/**
	 * There is no saveAs function
	 */
	@Override
	public void doSaveAs() {
	}

	/**
	 * Initializes the editor. If an existing data set is opened, the local
	 * variable "text" is set to This data set. If the editor is opened to
	 * create a new one, a new data set is created and the local variable "text"
	 * is set to this one.
	 * 
	 * @param input
	 *            The editor's input
	 * @param site
	 *            The editor's site
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// Set the site and the input
		setSite(site);
		setInput(input);

		// Set the editor's data set to the editor's input
		text = (DataSetText) ((UniDataSetEditorInput) input).getUniDataSet();

		// test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newText = (text == null);

		// If new ..
		if (newText) {

			// Create a new data set
			text = new DataSetText(((UniDataSetEditorInput) input).getCategory());

			//T: Text Editor: Part Name of a new text entry
			setPartName(_("New Text Entry"));
		}
		else {

			// Set the Editor's name to the shipping name.
			setPartName(text.getStringValueByKey("name"));
		}
	}

	/**
	 * Returns whether the contents of this part have changed since the last
	 * save operation
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		/*
		 * the following parameters are not checked:
		 * - id (constant)
		 */

		if (text.getBooleanValueByKey("deleted")) { return true; }
		if (newText) { return true; }

		if (!text.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!text.getStringValueByKey("text").equals(textText.getText())) { return true; }
		if (!text.getStringValueByKey("category").equals(comboCategory.getText())) { return true; }

		return false;
	}

	/**
	 * Returns whether the "Save As" operation is supported by this part.
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 * @return False, SaveAs is not allowed
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Creates the SWT controls for this workbench part
	 * 
	 * @param the
	 *            parent control
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create the top Composite
		top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(top);

		// Add context help reference 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.TEXT_EDITOR);

		// Create an invisible container for all hidden components
		// There is no invisible component, so no container has to be created
		//Composite invisible = new Composite(top, SWT.NONE);
		//invisible.setVisible(false);
		//GridDataFactory.fillDefaults().hint(0, 0).span(2, 1).applyTo(invisible);

		// Create the title
		Label labelTitle = new Label(top, SWT.NONE);
		//T: Text Editor: Title
		labelTitle.setText(_("Text Entry"));
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).span(2, 1).applyTo(labelTitle);
		makeLargeLabel(labelTitle);

		// The name
		Label labelName = new Label(top, SWT.NONE);
		labelName.setText(_("Name"));
		//T: Tool Tip Text
		labelName.setToolTipText(_("Name to identify the text"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(top, SWT.BORDER);
		textName.setText(text.getStringValueByKey("name"));
		textName.setToolTipText(labelName.getToolTipText());
		superviceControl(textName, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textName);

		// The category
		Label labelCategory = new Label(top, SWT.NONE);
		labelCategory.setText(_("Category"));
		//T: Tool Tip Text
		labelCategory.setToolTipText(_("You can set a category to classify the texts"));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);

		comboCategory = new Combo(top, SWT.BORDER);
		comboCategory.setText(text.getStringValueByKey("category"));
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		superviceControl(comboCategory);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboCategory);

		// Collect all category strings
		TreeSet<String> categories = new TreeSet<String>();
		categories.addAll(Data.INSTANCE.getTexts().getCategoryStrings());

		// Add all category strings to the combo
		for (Object category : categories) {
			comboCategory.add(category.toString());
		}


		
		// The text
		Label labelText = new Label(top, SWT.NONE);
		labelText.setText(_("Text"));
		//T: Tool Tip Text
		labelText.setToolTipText(_("Enter a text. It can be used as template. E.g. for the message field in the document editor."));

		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelText);
		textText = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		textText.setText(text.getStringValueByKey("text"));
		textText.setToolTipText(labelText.getToolTipText());
		superviceControl(textText, 10000);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(textText);
	}
	
	/**
	 * Set the focus to the top composite.
	 * 
	 * @see com.sebulli.fakturama.editors.Editor#setFocus()
	 */
	@Override
	public void setFocus() {
		if(top != null) 
			top.setFocus();
	}


}
