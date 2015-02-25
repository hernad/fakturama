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

import java.util.Iterator;

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
 * This action marks an entry in the invoice table as unpaid or paid.
 * 
 * @author Gerd Bartelt
 */
public class MarkDocumentAsPaidAction extends Action {

	// progress of the order. Value from 0 to 100 (percent)
	boolean paid;

	/**
	 * Constructor Instead of using a value for the states "unpaid" or "paid"
	 * a progress value from 0 to 100 (percent) is used.
	 * 
	 * So it's possible to insert states between these.
	 * 
	 * @param text
	 * @param progress
	 */
	public MarkDocumentAsPaidAction(boolean paid) {
		super();
		this.paid = paid;

		// Correlation between progress value and state.
		// Depending on the state, the icon and the command ID is selected.
		if (paid) {
			//T: Text of the action
			this.setText(_("mark as \"paid\""));
			setSettings(ICommandIds.CMD_MARK_DOCUMENT_AS_PAID, "/icons/16/checked_16.png");
		}
		else {
			//T: Text of the action
			this.setText(_("mark as \"unpaid\""));
			setSettings(ICommandIds.CMD_MARK_DOCUMENT_AS_PAID, "/icons/16/error_16.png");
		}

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

//					Object obj = ((IStructuredSelection) selection).getFirstElement();
					Iterator iterator = ((IStructuredSelection) selection).iterator();
					while(iterator.hasNext()) {
						Object obj = iterator.next();
						// If we had a selection let change the state
//						if (obj != null) {
							DataSetDocument uds = (DataSetDocument) obj;
							if (uds instanceof DataSetDocument) {
	
								// Do it only, if it is allowed to mark this kind of document as paid.
								if (DocumentType.getType(uds.getIntValueByKey("category")).hasPaid()) {
	
									// change the state
									uds.setPaid(paid);
	
									// also in the database
									Data.INSTANCE.updateDataSet(uds);
								}
							}
//						}
					}
	
					// Refresh the table with orders.
					view.refresh();
				}
			}
		}
	}
}
