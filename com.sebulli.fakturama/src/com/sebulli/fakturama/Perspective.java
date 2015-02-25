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

package com.sebulli.fakturama;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import com.sebulli.fakturama.views.Calculator;
import com.sebulli.fakturama.views.ErrorView;
import com.sebulli.fakturama.views.NavigationView;
import com.sebulli.fakturama.views.datasettable.ViewContactTable;
import com.sebulli.fakturama.views.datasettable.ViewDocumentTable;
import com.sebulli.fakturama.views.datasettable.ViewExpenditureVoucherTable;
import com.sebulli.fakturama.views.datasettable.ViewListTable;
import com.sebulli.fakturama.views.datasettable.ViewPaymentTable;
import com.sebulli.fakturama.views.datasettable.ViewProductTable;
import com.sebulli.fakturama.views.datasettable.ViewShippingTable;
import com.sebulli.fakturama.views.datasettable.ViewTextTable;
import com.sebulli.fakturama.views.datasettable.ViewVatTable;

/**
 * This is the default (and the only) perspective in the Fakturama project.
 * 
 * @author Gerd Bartelt
 */
public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.sebulli.fakturama.perspective";

	/** bottom folder's id. */
	public static final String ID_BOTTOM = "com.sebulli.fakturama.perspective.bottomFolder";

	/**
	 * Creates the initial layout of the perspective. The Navigation view and
	 * the error view on the left side. The Table views under the editor area.
	 * The calculator on the right side of the editor.
	 * 
	 * @param layout
	 *            Page layout
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
		layout.addPlaceholder(ErrorView.ID, IPageLayout.BOTTOM, 0.7f, NavigationView.ID);

		IPlaceholderFolderLayout folder = layout.createPlaceholderFolder(ID_BOTTOM, IPageLayout.BOTTOM, 0.6f, editorArea);

		folder.addPlaceholder(ViewDocumentTable.ID);
		folder.addPlaceholder(ViewContactTable.ID);
		folder.addPlaceholder(ViewProductTable.ID);
		folder.addPlaceholder(ViewVatTable.ID);
		folder.addPlaceholder(ViewShippingTable.ID);
		folder.addPlaceholder(ViewPaymentTable.ID);
		folder.addPlaceholder(ViewTextTable.ID);
		folder.addPlaceholder(ViewExpenditureVoucherTable.ID);
		folder.addPlaceholder(ViewListTable.ID);

		layout.getViewLayout(ViewDocumentTable.ID).setMoveable(false);
		layout.getViewLayout(ViewContactTable.ID).setMoveable(false);
		layout.getViewLayout(ViewProductTable.ID).setMoveable(false);
		layout.getViewLayout(ViewVatTable.ID).setMoveable(false);
		layout.getViewLayout(ViewShippingTable.ID).setMoveable(false);
		layout.getViewLayout(ViewPaymentTable.ID).setMoveable(false);
		layout.getViewLayout(ViewTextTable.ID).setMoveable(false);
		layout.getViewLayout(ViewExpenditureVoucherTable.ID).setMoveable(false);
		layout.getViewLayout(ViewListTable.ID).setMoveable(false);

		layout.addPlaceholder(Calculator.ID, IPageLayout.RIGHT, 0.7f, editorArea);
	}
}
