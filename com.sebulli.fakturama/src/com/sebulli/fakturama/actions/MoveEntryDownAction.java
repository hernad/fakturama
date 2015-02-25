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

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.editors.DocumentEditor;

/**
 * This action moves the selected entry down
 * 
 * @author Gerd Bartelt
 */
public class MoveEntryDownAction extends Action {

	/**
	 * default constructor
	 */
	public MoveEntryDownAction() {
		//T: Text of the Action
		super(_("move down"));
		
		//T: Tool Tip Text
		setToolTipText(_("Move down the selected entry"));
		
		// The id is used to refer to the action in a menu or tool bar
		setId(ICommandIds.CMD_MOVE_DOWN);

		// Associate the action with a predefined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_MOVE_DOWN);
		setImageDescriptor(Activator.getImageDescriptor("/icons/16/down_16.png"));
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

		// Cast the part to DocumentEditor
		if (part instanceof DocumentEditor) {
			DocumentEditor documentEditor = (DocumentEditor) part;

			// Does the editor exist ?
			if (documentEditor != null) {

				// Get the selection
				selection = documentEditor.getSite().getSelectionProvider().getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection) selection).getFirstElement();

					// If we had a selection, move it up
					if (obj != null) {
						UniDataSet uds = (UniDataSet) obj;
						documentEditor.moveItem(uds, false);
					}
				}
			}
		}
	}
}
