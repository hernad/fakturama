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

import static com.sebulli.fakturama.Translate._;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.misc.DocumentType;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * This action removes the invoice reference of delivery note
 * 
 * @author Gerd Bartelt
 */
public class RemoveInvoiceReferenceAction extends Action {

	/**
	 * Constructor 
	 */
	public RemoveInvoiceReferenceAction() {
		super();

		this.setText(_("remove reference to invoice"));
		setSettings(ICommandIds.CMD_REMOVE_INVOICE_REF, "/icons/16/remove_invoice_16.png");

	}

	/**
	 * Set command ID and icon for this action.
	 * 
	 * @param cmd
	 *            command ID
	 * @param image
	 *            Actions's icon
	 */
	private void setSettings(String cmd, String image) {
		setId(cmd);
		setActionDefinitionId(cmd);
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor(image));
	}


	/**
	 * Run the action Search all views to get the selected element. If a view
	 * with an selection is found, change the state, if it was an order.
	 */
	@Override
	public void run() {

		// cancel, if the data base is not connected.
		if (!DataBaseConnectionState.INSTANCE.isConnected())
			return;

		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();

		// Get the active part (view)
		IWorkbenchPart part = null;
		if (page != null)
			part = page.getActivePart();

		ISelection selection;

		// Cast the part to ViewDataSetTable
		if (part instanceof ViewDataSetTable) {

			ViewDataSetTable view = (ViewDataSetTable) part;

			// does the view exist ?
			if (view != null) {

				//get the selection
				selection = view.getSite().getSelectionProvider().getSelection();

				if (selection != null && selection instanceof IStructuredSelection) {

					Object obj = ((IStructuredSelection) selection).getFirstElement();

					// If there is a selection let change the state
					if (obj != null) {

						// Get the document
						DataSetDocument uds = (DataSetDocument) obj;
						// and the type of the document
						DocumentType documentType = DocumentType.getType(uds.getCategory());

						// Exit, if it was not a delivery note
						if (documentType != DocumentType.DELIVERY)
							return;

						// remove the reference
						uds.setIntValueByKey("invoiceid", -1);

						// also in the database
						Data.INSTANCE.updateDataSet(uds);

						// Refresh the table with delivery notes.
						view.refresh();

					}
				}
			}
		}
	}
}
