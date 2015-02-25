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

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.sebulli.fakturama.backup.BackupManager;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.editors.ParcelServiceBrowserEditor;
import com.sebulli.fakturama.office.OfficeManager;
import com.sebulli.fakturama.preferences.PreferencesInDatabase;

/**
 * Applications workbench window advisor. Here are some methods that are called
 * after a window is opened or before it is closed.
 * 
 * @author Gerd Bartelt
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	
	private ArrayList<IEditorReference> openEditors = new ArrayList<IEditorReference>();
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Returns the active workbench window, 
	 * or, if no window is active, use at least the first existing one
	 * 
	 * @return the active Workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow () {
		
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (workbenchWindow == null) {
			if (PlatformUI.getWorkbench().getWorkbenchWindowCount()>0)
				workbenchWindow = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
			 
		}

		return workbenchWindow;
	}
	
	
	/**
	 * Creates a new action bar advisor.
	 * 
	 * @param configurer
	 *            configurer
	 * @return the new action bar advisor
	 */
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/**
	 * Called before the window is opened.
	 * 
	 * The initial size of the window is set and the cool bar and status bar is
	 * created.
	 */
	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1200, 800));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		
		IPartService service = (IPartService) configurer.getWindow().getService(IPartService.class);
		service.addPartListener(new IPartListener() {
		 
		    public void partActivated(IWorkbenchPart part) {}
		    public void partBroughtToTop(IWorkbenchPart part) {}
		    public void partClosed(IWorkbenchPart part) {}
		    public void partDeactivated(IWorkbenchPart part) {}
		 
		    /**
		     * A new editor was opened
		     */
		    @SuppressWarnings("restriction")
			public void partOpened(IWorkbenchPart part) {
		    	  {
		    		 IWorkbenchPartSite site  = part.getSite();
		    		 
		    		 // Close other editors only, if this part was an editor
		    		 if (!(site instanceof org.eclipse.ui.internal.EditorSite))
		    			 return;

		    		 IWorkbenchPage page = site.getPage();
		             IEditorReference[] editorReferences = page.getEditorReferences();
		             
		             // Get all editors
		             for (IEditorReference editorRef : editorReferences) {
		            	 IEditorPart editor = editorRef.getEditor(false);
		            	 
		            	 // Add new editors to the list of all open editors
		            	 boolean thisIsMe = false;
		            	 if (!openEditors.contains(editorRef)) {
		            		 openEditors.add(editorRef);
		            		 thisIsMe = true;
		            	 }
		            	 
		            	 if (editor!= null) {
			            	 // Close the other editors, if they are not dirty
			            	 if (!editor.isDirty() && !thisIsMe && 
						    		 // Do not close parcel service editors
			            			 !(editor instanceof ParcelServiceBrowserEditor)) {
			            			if (Activator.getDefault().getPreferenceStore().getBoolean("GENERAL_CLOSE_OTHER_EDITORS")) {
			            				page.closeEditor(editor, false);
			            				if (openEditors.size() > 1)
			            					openEditors.remove(editorRef);
			            			}
			            	 }
		            	 }
		             }
		         }
		    }
		});

	}

	/**
	 * Called after the window is opened.
	 * 
	 * The logger gets the information, that the workbench is now opened.
	 */
	@Override
	public void postWindowOpen() {
		Workspace.INSTANCE.showWorkingDirInTitleBar();
	}

	/**
	 * Called before the window shell is closed. The open views are closed
	 */
	@Override
	public boolean preWindowShellClose() {
		return true;
	}

	/**
	 * Called after the window shell is closed. All OpenOffice documents are
	 * closed Some (not all) of the preferences are stored in the data base.
	 * Then the data base is closed.
	 */
	@Override
	public void postWindowClose() {

		//Closes all OpenOffice documents 
		OfficeManager.INSTANCE.closeAll();

		if (Data.INSTANCE != null) {
			PreferencesInDatabase.savePreferencesInDatabase();
			Data.INSTANCE.close();

			// Create a database backup 
			BackupManager.createBackup();
		}
	}

}
