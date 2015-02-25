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

package com.sebulli.fakturama.views.datasettable;

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.actions.CreateCollectiveInvoiceAction;
import com.sebulli.fakturama.actions.DeleteDataSetAction;
import com.sebulli.fakturama.actions.MarkDocumentAsPaidAction;
import com.sebulli.fakturama.actions.MarkOrderAsAction;
import com.sebulli.fakturama.actions.NewDocumentAction;
import com.sebulli.fakturama.actions.RemoveInvoiceReferenceAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * View with the table of all documents
 * 
 * @author Gerd Bartelt
 * 
 */
public class ViewDocumentTable extends ViewDataSetTable {

	// ID of this view
	public static final String ID = "com.sebulli.fakturama.views.datasettable.viewDocumentTable";

	// The document type that corresponds with the selected category
	private DocumentType documentType = DocumentType.NONE;

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Add the action to create a new entry
		addNewAction = new NewDocumentAction();

		// Mark the columns that are used by the search function.
		searchColumns = new String[4];
		searchColumns[0] = "name";
		searchColumns[1] = "date";
		searchColumns[2] = "addressfirstline";
		searchColumns[3] = "total";

		super.createPartControl(parent,DataSetDocument.class, true, false, ContextHelpConstants.DOCUMENT_TABLE_VIEW);

		// Name of this view
		this.setPartName(_("Documents"));

		// Create the context menu
		createContextMenu();

		// Name of the editor
		editor = "Document";

		// Get the column width from the preferences
		int cw_icon = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_ICON");
		int cw_document = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_DOCUMENT");
		int cw_date = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_DATE");
		int cw_name = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_NAME");
		int cw_state = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_STATE");
		int cw_total = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_TOTAL");
		int cw_printed = Activator.getDefault().getPreferenceStore().getInt("COLUMNWIDTH_DOCUMENTS_PRINTED");
		
		// Create the table columns
		// new TableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, "ID", 30, 0, true, "id");
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, "", cw_icon,  true, "$documenttype");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Document"), cw_document, true, "name");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Date"), cw_date, true, "date");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Name"), cw_name, false, "addressfirstline");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("State"), cw_state, true, "$status");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.RIGHT, _("Total"), cw_total, true, "total");
		//T: Used as heading of a table. Keep the word short.
		new UniDataSetTableColumn(tableColumnLayout, tableViewer, SWT.LEFT, _("Printed"), cw_printed, true, "$printed");

		
		
		// Add a selection listener
		hookSelect();

		// Set the input of the table viewer and the tree viewer
		tableViewer.setInput(Data.INSTANCE.getDocuments());
		topicTreeViewer.setInput(Data.INSTANCE.getDocuments());

		// On creating, set the unpaid invoices
		topicTreeViewer.selectItemByName(DocumentType.getPluralString(DocumentType.INVOICE) + "/" + DataSetDocument.getStringNOTPAID());

	}

	/**
	 * Create a context menu
	 */
	private void createContextMenu() {
		super.createMenuManager();

		// Add the entries for orders
		if (documentType.equals(DocumentType.ORDER)) {
			menuManager.add(new MarkOrderAsAction(MarkOrderAsAction.PENDING));
			menuManager.add(new MarkOrderAsAction(MarkOrderAsAction.PROCESSING));
			menuManager.add(new MarkOrderAsAction(MarkOrderAsAction.SHIPPED));
		}
		else if (documentType.equals(DocumentType.DELIVERY)) {
			menuManager.add(new RemoveInvoiceReferenceAction());
		}
		// Add the entries to mark a document as paid
		if (documentType.hasPaid()) {
			menuManager.add(new MarkDocumentAsPaidAction(false));
			menuManager.add(new MarkDocumentAsPaidAction(true));
		}

		menuManager.add(new Separator());

		// Add an entry to create a new document of each document type
		menuManager.add(new NewDocumentAction(DocumentType.LETTER));
		menuManager.add(new NewDocumentAction(DocumentType.OFFER));
		menuManager.add(new NewDocumentAction(DocumentType.ORDER));
		menuManager.add(new NewDocumentAction(DocumentType.CONFIRMATION));
		menuManager.add(new NewDocumentAction(DocumentType.INVOICE));
		menuManager.add(new NewDocumentAction(DocumentType.DELIVERY));
		menuManager.add(new NewDocumentAction(DocumentType.CREDIT));
		menuManager.add(new NewDocumentAction(DocumentType.DUNNING));
		menuManager.add(new NewDocumentAction(DocumentType.PROFORMA));
		
//		if(documentType.equals(DocumentType.DELIVERY)) {
//			menuManager.add(new CreateCollectiveInvoiceAction());
//		}

		menuManager.add(new Separator());

		menuManager.add(new DeleteDataSetAction());
	}

	/**
	 * Add a selection listener. The document view is the only view that
	 * contains a entry in the tree viewer for "THIS TRANSACTION" and
	 * "THIS CONTACT". If an entry is selected, it's possible to filter all
	 * documents from the same contact or with the same transaction ID.
	 */
	private void hookSelect() {

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			/**
			 * Notifies that the selection has changed
			 * 
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
			 */
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				// Get the selection
				ISelection selection = event.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					DataSetDocument uds = (DataSetDocument) ((IStructuredSelection) selection).getFirstElement();

					// Set the transaction and the contact filter
					if ((uds != null)) {
						topicTreeViewer.setTransaction(uds.getStringValueByKey("name"), uds.getIntValueByKey("transaction"));
						topicTreeViewer.setContact(uds.getStringValueByKey("addressfirstline"), uds.getIntValueByKey("addressid"));
					}
				}
			}
		});
	}

	/**
	 * Recreate the context menu
	 * 
	 * @see com.sebulli.fakturama.views.datasettable.ViewDataSetTable#setCategoryFilter(java.lang.String)
	 */
	@Override
	public void setCategoryFilter(String filter) {
		super.setCategoryFilter(filter);

		// Get the document of the filter string
		for (int i = 0; i < DocumentType.MAXID; i++) {
			if (filter.startsWith(DocumentType.getPluralString(i)))
				documentType = DocumentType.getType(i);
		}

		// Recreate the context menu
		menuManager.removeAll();
		createContextMenu();
	}

}
