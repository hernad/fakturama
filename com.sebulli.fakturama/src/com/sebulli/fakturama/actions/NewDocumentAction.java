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

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.data.DataSetDocument;
import com.sebulli.fakturama.editors.DocumentEditor;
import com.sebulli.fakturama.editors.Editor;
import com.sebulli.fakturama.editors.UniDataSetEditorInput;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DocumentType;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;
import com.sebulli.fakturama.views.datasettable.ViewDocumentTable;

/**
 * This action creates a new contact in an editor.
 * 
 * @author Gerd Bartelt
 */
public class NewDocumentAction extends NewEditorAction {
	private int iconSize = 16;

	/**
	 * Default Constructor with no parameters. If no parameters are set, an
	 * order document is created.
	 */
	public NewDocumentAction() {
		//T: Text of the action to create a new document
		super(_("New Document"));
		category = DocumentType.ORDER.getString();
		setText(DocumentType.ORDER.getString());
		setSettings(ICommandIds.CMD_NEW_DOCUMENT, "" , DocumentType.ORDER );
	}

	/**
	 * Constructor Creates an Action with default icon size of 16x16 pixel
	 * 
	 * @param documentType
	 *            Type of document to create
	 */
	public NewDocumentAction(DocumentType documentType) {
		super("");
		this.iconSize = 16;
		setDocumentType(documentType);
	}

	/**
	 * Constructor Creates an Action with default icon size of 16x16 pixel
	 * 
	 * @param documentType
	 *            Type of document to create
	 * @param editor
	 *            Parent editor. The Editors content is saved and duplicated.
	 * @param iconSize
	 *            Size of icon (16, 32 or 48)
	 */
	public NewDocumentAction(DocumentType documentType, Editor editor, int iconSize) {
		super("", null, editor);
		this.iconSize = iconSize;
		setDocumentType(documentType);
	}

	/**
	 * Sets Command ID and icon name of this action
	 * 
	 * @param cmd
	 *            Command ID
	 * @param image
	 *            Icon name
	 */
	private void setSettings(String cmd, String image, DocumentType documentType) {
		setId(cmd);
		setActionDefinitionId(cmd);
		setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor(image));
		
		//T: Tool Tip Text
		setToolTipText(_("Create:") + " " + documentType.getNewText());

	}

	/**
	 * Sets Document Type and generates icon name
	 * 
	 * @param documentType
	 */
	private void setDocumentType(DocumentType documentType) {

		category = documentType.getString();
		String iconSizeString = "_" + Integer.toString(iconSize);
		if (iconSize == 32) {
			iconSizeString = "_new" + iconSizeString;
			setText(documentType.getString());
		}
		else
			setText(documentType.getNewText());

		setSettings(ICommandIds.CMD_NEW_ + documentType.getTypeAsString(), "/icons/" + Integer.toString(iconSize) + "/"
				+ documentType.getTypeAsString().toLowerCase() + iconSizeString + ".png", documentType);
	}

	/**
	 * Run the action If a parent editor is set: Save the content and duplicate
	 * it.
	 * 
	 * Open a new document editor.
	 */
	@Override
	public void run() {

		// cancel, if the data base is not connected.
		if (!DataBaseConnectionState.INSTANCE.isConnected())
			return;

		// Does a parent editor exist ?
		if (parentEditor != null) {

			//if yes and if it was an Document Editor ...
			if (parentEditor instanceof DocumentEditor) {

				// Mark parent document, save it and use it as base
				// for a new document editor.
				((DocumentEditor) parentEditor).childDocumentGenerated();
				parentEditor.doSave(null);
				parent = ((DocumentEditor) parentEditor).getDocument();
			}
		}

		// Was the parent document an order with status pending ?
		if (parent != null) {

			// Parent document was an order
			if (DocumentType.getType(parent.getCategory()) == DocumentType.ORDER) {

				// State of order was pending
				if (parent.getIntValueByKey("progress") <=  MarkOrderAsAction.PENDING) {
					MarkOrderAsAction.markOrderAs((DataSetDocument) parent, MarkOrderAsAction.PROCESSING, "",
							Activator.getDefault().getPreferenceStore().getBoolean("WEBSHOP_NOTIFY_PROCESSING"));

					// Find the view
					ViewDataSetTable view = (ViewDataSetTable) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.findView(ViewDocumentTable.ID);

					// Refresh it
					if (view != null)
						view.refresh();

				}
			}
		}

		// Set the editors input
		UniDataSetEditorInput input = new UniDataSetEditorInput(category, parent);

		// Open the editor
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, DocumentEditor.ID);
		}
		catch (PartInitException e) {
			Logger.logError(e, "Error opening Editor: " + DocumentEditor.ID);
		}
	}
}
