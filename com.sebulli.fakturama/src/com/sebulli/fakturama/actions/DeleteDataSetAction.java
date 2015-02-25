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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.editors.DocumentEditor;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * This action deletes an selected data set. An dialog appears to confirm the
 * deletion.
 * 
 * @author Gerd Bartelt
 */
public class DeleteDataSetAction extends Action {

	/**
	 * default constructor
	 */
	public DeleteDataSetAction() {
		//T: Text of the DeleteDataSetAction
		super(_("delete"));
		
		//T: Tool Tip Text
		setToolTipText(_("Delete the marked entry"));
		
		// The id is used to refer to the action in a menu or tool bar
		setId(ICommandIds.CMD_DELETE_DATASET);

		// Associate the action with a predefined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_DELETE_DATASET);
		setImageDescriptor(Activator.getImageDescriptor("/icons/16/delete_16.png"));
	}

	/**
	 * Run the action Search all views to get the selected element. If a view
	 * with an selection is found, display the dialog before deleting the
	 * element
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

			// Does the part exist ?
			if (view != null) {

				// Get the selection
				selection = view.getSite().getSelectionProvider().getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection) selection).getFirstElement();

					// If we had a selection let us delete the element
					if (obj != null) {
						UniDataSet uds = (UniDataSet) obj;

						// before deleting: ask !
						MessageBox messageBox = new MessageBox(workbenchWindow.getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						//T: Title of the dialog to confirm deleting a data set.
						messageBox.setText(_("Confirm deleting"));
						//T: Text of the dialog to confirm deleting a data set.
						messageBox.setMessage(_("Entry")+ " " + uds.getStringValueByKey("name") + " " + 
								//T: Text of the dialog to confirm deleting a data set.
								_("will be deleted !"));

						// We can delete now.
						if (messageBox.open() == SWT.OK) {

							// Instead of deleting is completely from the database, the element is just marked
							// as deleted. So a document which still refers to this element would not cause an error.
							uds.setBooleanValueByKey("deleted", true);
							Data.INSTANCE.updateDataSet(uds);
						}

						// Refresh the table
						view.refresh();
					}
				}
			}
		}

		
		
		// Cast the part to DocumentEditor
		if (part instanceof DocumentEditor) {
			DocumentEditor documentEditor = (DocumentEditor) part;

			// Does the editor exist ?
			if (documentEditor != null) {

				// Get the selection
				selection = documentEditor.getSite().getSelectionProvider().getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection iselection = ((IStructuredSelection) selection);
					for (Iterator iterator = iselection.iterator(); iterator.hasNext();) {
						Object obj = (Object) iterator.next();
						// If we had a selection, delete it
						if (obj != null) {
							UniDataSet uds = (UniDataSet) obj;
							documentEditor.deleteItem(uds);
						}
					}
				}
			}
		}		
	}
}
