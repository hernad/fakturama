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

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 * 
 * @author Gerd Bartelt
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	/**
	 * Create workbench window advisor
	 * 
	 * @param configurer
	 *            configurer
	 * @return new workbench window advisor
	 */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/**
	 * Returns the initial perspective
	 * 
	 * @return ID of the initial parespective
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	/**
	 * Initializes the workbench configurer to save and restore the workbench
	 * settings like position and size of the views.
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}
	
	/**
	 * Check for updates
	 */
	/*
	@Override
    public void preStartup() {
        P2Util.checkForUpdates();
    }
	*/
	
	/**
	 * Refresh a view by the view ID
	 * 
	 * @prama ID of the view to refresh
	 */
	static public void refreshView(String viewId) {

		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null)
			return;

		// Find the view
		ViewDataSetTable view = (ViewDataSetTable) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);

		// Refresh it
		if (view != null)
			view.refresh();

	}


}
