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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * Universal Handler to open an UniDataSet editor
 * 
 * @author Gerd Bartelt
 */
public class CallEditor extends AbstractHandler implements IHandler {

	/**
	 * Execute the command
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the parameter of the action that calls this handler
		String param = event.getParameter("com.sebulli.fakturama.editors.callEditorParameter");
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		// Get the corresponding table view
		IWorkbenchPage page = window.getActivePage();
		String viewId = "com.sebulli.fakturama.views.datasettable.view" + param + "Table";
		ViewDataSetTable view = (ViewDataSetTable) page.findView(viewId);

		// Get the selection in the table view
		ISelection selection = view.getSite().getSelectionProvider().getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();

			// If we had a selection lets open the editor
			if (obj != null) {

				// Define  the editor
				String editor = "com.sebulli.fakturama.editors." + param.toLowerCase() + "Editor";
				UniDataSet uds = (UniDataSet) obj;
				UniDataSetEditorInput input = new UniDataSetEditorInput(uds);

				// And try to open it
				try {
					page.openEditor(input, editor);
				}
				catch (PartInitException e) {
					Logger.logError(e, "Error opening Editor: " + editor);
				}
			}
		}
		return null;
	}

}
