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

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.sebulli.fakturama.data.DataSetList;
import com.sebulli.fakturama.data.DataSetListNames;
import com.sebulli.fakturama.views.datasettable.ViewListTable;

/**
 * The text editor
 * 
 * @author Gerd Bartelt
 */
public class ListEditor extends Editor {

	// Editor's ID
	public static final String ID = "com.sebulli.fakturama.editors.listEditor";

	// This UniDataSet represents the editor's input 
	private DataSetList listEntry;

	// SWT widgets of the editor
	private Composite top;
	private Combo comboCategory;
	private Text textName;
	private Text textValue;

	// defines, if the text is new created
	private boolean newList;

	/**
	 * Constructor
	 * 
	 * Associate the table view with the editor
	 */
	public ListEditor() {
		tableViewID = ViewListTable.ID;
		editorID = "list";
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

		// Get the selected category
		String category = "";
		int i = comboCategory.getSelectionIndex();
		if (i > -1)
			category = comboCategory.getItem(i);
		else
			category = comboCategory.getText();

		category = DataSetListNames.NAMES.getName(category);

		// Exit, if the selected entry is not a name of a valid list
		if (!DataSetListNames.NAMES.exists(category))
			return;

		// Always set the editor's data set to "undeleted"
		listEntry.setBooleanValueByKey("deleted", false);

		// Set the text data
		listEntry.setStringValueByKey("name", textName.getText());
		listEntry.setStringValueByKey("value", textValue.getText());
		listEntry.setStringValueByKey("category", category);

		// If it is a new text, add it to the text list and
		// to the data base
		if (newList) {
			listEntry = Data.INSTANCE.getListEntries().addNewDataSet(listEntry);
			newList = false;
		}
		// If it's not new, update at least the data base
		else {
			Data.INSTANCE.getListEntries().updateDataSet(listEntry);
		}

		// Set the Editor's name to the list name.
		setPartName(listEntry.getStringValueByKey("name"));

		// Refresh the table view of all list entries
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
		listEntry = (DataSetList) ((UniDataSetEditorInput) input).getUniDataSet();

		// test, if the editor is opened to create a new data set. This is,
		// if there is no input set.
		newList = (listEntry == null);

		// If new ..
		if (newList) {

			// Create a new data set
			listEntry = new DataSetList(((UniDataSetEditorInput) input).getCategory());

			//T: List Editor: Part Name of a new list entry
			setPartName(_("New List Entry"));
		}
		else {

			// Set the Editor's name to the list name.
			setPartName(listEntry.getStringValueByKey("name"));
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

		if (listEntry.getBooleanValueByKey("deleted")) { return true; }
		if (newList) { return true; }

		if (!listEntry.getStringValueByKey("name").equals(textName.getText())) { return true; }
		if (!listEntry.getStringValueByKey("value").equals(textValue.getText())) { return true; }

		// Get the selected entry
		String category = "";
		int i = comboCategory.getSelectionIndex();
		if (i > -1)
			category = comboCategory.getItem(i);
		else
			category = comboCategory.getText();

		category = DataSetListNames.NAMES.getName(category);

		if (!listEntry.getStringValueByKey("category").equals(category)) { return true; }

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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(top, ContextHelpConstants.LIST_EDITOR);

		// Create the title
		Label labelTitle = new Label(top, SWT.NONE);
		//T: List Editor - Title
		labelTitle.setText(_("List Entry"));
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).span(2, 1).applyTo(labelTitle);
		makeLargeLabel(labelTitle);

		// The category
		Label labelCategory = new Label(top, SWT.NONE);
		//T: List Editor - Category ( Name of the List to place this entry)
		labelCategory.setText(_("List"));
		//T: Tool Tip Text
		labelCategory.setToolTipText(_("Please select a list"));
		
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCategory);
		comboCategory = new Combo(top, SWT.BORDER);
		comboCategory.setToolTipText(labelCategory.getToolTipText());
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).hint(300, SWT.DEFAULT).applyTo(comboCategory);

		// Add each localizes list name to the combo
		for (Map.Entry<String, String> entry : DataSetListNames.NAMES.getLocalizedNames()) {
			comboCategory.add(entry.getValue());
		}

		// Check dirty, if the selection changes
		comboCategory.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkDirty();
			}
		});

		// Select the category
		comboCategory.setText(DataSetListNames.NAMES.getLocalizedName(listEntry.getStringValueByKey("category")));

		// The name
		Label labelName = new Label(top, SWT.NONE);
		labelName.setText(_("Name"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelName);
		textName = new Text(top, SWT.BORDER);
		textName.setText(listEntry.getStringValueByKey("name"));
		superviceControl(textName, 64);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textName);

		// The value
		Label labelCode = new Label(top, SWT.NONE);
		labelCode.setText(_("Value"));
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(labelCode);
		textValue = new Text(top, SWT.BORDER);
		textValue.setText(listEntry.getStringValueByKey("value"));
		superviceControl(textValue, 250);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textValue);
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
