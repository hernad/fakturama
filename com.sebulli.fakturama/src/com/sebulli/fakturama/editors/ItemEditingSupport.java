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

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * Abstract class for item editing support.
 * This class implements the canJumpWithTabs-method that is
 * common in both classes "DocumentItemEditingSuport" and "ExpenditureItemEditingSupport"
 * 
 * @author Gerd Bartelt
 *
 */
public abstract class ItemEditingSupport extends EditingSupport{

	public ItemEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	protected boolean canJumpWithTabs() {
		return false;
	}

	protected boolean getMultiLineEditing() {
		return false;
	}

}
