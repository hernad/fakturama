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

import static com.sebulli.fakturama.Translate._;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DocumentType;
import com.sebulli.fakturama.parcelService.ParcelServiceManager;

/**
 * Manages the workspace
 * 
 * @author Gerd Bartelt
 */
public enum Workspace {
	INSTANCE;

	public String templateFolderName;
	public static final String productPictureFolderName = "/Pics/Products/";

	// Workspace path
	String workspace = "";

	// The plugin's preference store
	IPreferenceStore preferences;

	Boolean isInitialized;

	Workspace() {

		//T: Templates folder name
		templateFolderName = _("Templates");
		
		isInitialized = false;

		// Get the workspace from the preferences
		preferences = Activator.getDefault().getPreferenceStore();
		workspace = preferences.getString("GENERAL_WORKSPACE");
		
		// Get the program parameters
		String[] args = Platform.getCommandLineArgs();

		// Read the parameter "-workspace"
		String workspaceFromParameters = "";
		
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-dbHost")) {
				i++;
				preferences.setValue("DATABASE_HOST", args[i]);
			}
			if (args[i].equals("-workspace"))
			{
				i++;
				workspaceFromParameters = args[i];
				
				// Checks, whether the workspace from the parameters exists
				File workspacePath = new File(workspaceFromParameters);
				if (workspacePath.exists()) {
					// Use it, if it is an existing folder.
					workspace = workspaceFromParameters;
				}
			}
			i++;
		}
		

		// Checks, whether the workspace request is set.
		// If yes, the workspace is set to this value and the request value is cleared.
		// This mechanism is used, because the workspace can only be changed by restarting the application.
		String requestedWorkspace = preferences.getString("GENERAL_WORKSPACE_REQUEST");
		if (!requestedWorkspace.isEmpty()) {
			preferences.setValue("GENERAL_WORKSPACE_REQUEST", "");
			setWorkspace(requestedWorkspace);
		}

		// Checks, whether the workspace is set.
		// If not, the SelectWorkspaceAction is started to select it.
		if (workspace.isEmpty()) {
			selectWorkspace();
		}
		else {
			// Checks, whether the workspace exists
			// Exit, if the workspace path is not valid
			File workspacePath = new File(workspace);
			if (!workspacePath.exists()) {
				setWorkspace("");
				selectWorkspace();
			}
		}

		if (!workspace.isEmpty()) {
			showWorkingDirInTitleBar();
			setWorkspace(workspace);
		}

	}

	/**
	 * Initialize the workspace. e.g. Creates a new template folder
	 * 
	 */
	public void initWorkspace() {

		// Do not initialize twice
		if (isInitialized)
			return;

		// Exit, if the workspace path is not set
		if (workspace.isEmpty())
			return;

		// Exit, if the workspace path is not valid
		File workspacePath = new File(workspace);
		if (!workspacePath.exists())
			return;

		// Create and fill the template folder, if it does not exist.
		File directory = new File(workspace + "/" + templateFolderName);
		if (!directory.exists()) {

			// Copy the templates from the resources to the file system
			for (int i = 1; i <= DocumentType.MAXID; i++) {
				if (DocumentType.getType(i) == DocumentType.DELIVERY) {
					resourceCopy("Templates/Delivery/Document.ott", templateFolderName + "/" + DocumentType.getString(i), "Document.ott");
				}
				else {
					resourceCopy("Templates/Invoice/Document.ott", templateFolderName + "/" + DocumentType.getString(i), "Document.ott");
				}
			}
		}

		// Create the start page, if it does not exist.
		File startPage = new File(workspace + "/" + templateFolderName + "/Start" + "/" + "start.html");
		if (!startPage.exists()) {
			resourceCopy("Templates/Start/start.html", templateFolderName + "/Start" , "start.html");
			resourceCopy("Templates/Start/logo.png", templateFolderName + "/Start" , "logo.png");
		}
		
		// Copy the parcel service templates
		String parcelServiceTemplatePath = ParcelServiceManager.getRelativeTemplatePath();
		File parcelServiceFolder = new File(ParcelServiceManager.getTemplatePath());
		if (!parcelServiceFolder.exists()) {
			resourceCopy("Templates/ParcelService/DHL_de.txt", parcelServiceTemplatePath , "DHL_de.txt");
			resourceCopy("Templates/ParcelService/eFILIALE_de.txt", parcelServiceTemplatePath , "eFILIALE_de.txt");
			resourceCopy("Templates/ParcelService/myHermes_de.txt", parcelServiceTemplatePath , "myHermes_de.txt");
		}
		
		// copy the ZUGFeRD properties file if it doesn't exists 
		File zugferdProps = new File(workspace + "/measure-units.properties");
		if(!zugferdProps.exists()) {
			resourceCopy("/resources/measure-units.properties", "", "measure-units.properties");
		}
		
		isInitialized = true;

	}

	/**
	 * Copies a resource file from the resource to the file system
	 * 
	 * @param resource
	 *            The resource file
	 * @param filePath
	 *            The destination on the file system
	 * @param fileName
	 *            The destination file name
	 */
	public void resourceCopy(String resource, String filePath, String fileName) {

		// Remove the last "/"
		if (filePath.endsWith("/"))
			filePath = filePath.substring(0, filePath.length()-1);
			
		// Relative path
		filePath = workspace + "/" + filePath;

		// Create the destination folder
		File directory = new File(filePath);
		if (!directory.exists())
			directory.mkdirs();

		// Copy the file
		try {
			// Create the input stream from the resource file
			InputStream in = Activator.getDefault().getBundle().getResource(resource).openStream();

			// Create the output stream from the output file name
			File fout = new File(filePath + "/" + fileName);
			OutputStream out;
			out = new FileOutputStream(fout);

			// Copy the content
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Close both streams
			in.close();
			out.close();

		}
		catch (FileNotFoundException e) {
			Logger.logError(e, "Resource file not found");
		}
		catch (IOException e) {
			Logger.logError(e, "Error copying the resource file to the file system.");
		}

	}

	/**
	 * Set the workspace
	 * 
	 * @param workspace
	 *            Path to the workspace
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
		preferences.setValue("GENERAL_WORKSPACE", workspace);
	}

	/**
	 * Returns the path of the workspace
	 * 
	 * @return The workspace path as string
	 */
	public String getWorkspace() {
		return this.workspace;

	}

	/**
	 * Opens a dialog to select the workspace
	 */
	public void selectWorkspace() {
		// Open a directory dialog 
		DirectoryDialog directoryDialog = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		directoryDialog.setFilterPath(System.getProperty("user.home"));

		//T: Title of the dialog to select the working directory
		directoryDialog.setText(_("Select your working directory"));
		//T: Text of the dialog to select the working directory
		directoryDialog.setMessage(_("Please select your working directory, where all the data is stored."));
		String selectedDirectory = directoryDialog.open();

		if (selectedDirectory != null) {

			// test if it is valid
			if (selectedDirectory.equals("/") || selectedDirectory.equals("\\"))
				selectedDirectory = "";
			if (!selectedDirectory.isEmpty()) {

				// If there is a connection to the database,
				// use the new working directory after a restart.
				if (DataBaseConnectionState.INSTANCE.isConnected()) {

					// Store the requested directory in a preference value
					Activator.getDefault().getPreferenceStore().setValue("GENERAL_WORKSPACE_REQUEST", selectedDirectory);
					MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_INFORMATION);
					
					//T: Title of a message box
					messageBox.setText(_("Information"));
					//T: Text of the dialog that the workspace will be switched and that you should restart Fakturama.
					messageBox.setMessage(_("To switch the workspace,\nFakturama will be restarted!"));
					messageBox.open();

					// Close the workbench
					//					ViewManager.INSTANCE.closeAll();
					PlatformUI.getWorkbench().restart();
				}
				// if there is no connection, use it immediately
				else {
					setWorkspace(selectedDirectory);
					showWorkingDirInTitleBar();
				}
			}
		}

		// Close the workbench if no workspace is set.
		if (workspace.isEmpty())
			PlatformUI.getWorkbench().close();

	}

	/**
	 * Displays the current workspace in the title bar
	 */
	public void showWorkingDirInTitleBar() {
		Bundle b = Activator.getDefault().getBundle();
		
		try {
	        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getWorkbenchWindow().getShell().setText(b.getHeaders().get(Constants.BUNDLE_NAME) + " - " + workspace);
        }
        catch (Exception e) {
        	// silently ignore any Exception...
        }
	}
	
	/**
	 * Getter for the templateFolderName
	 * 
	 * @return
	 * 			templateFolderName
	 */
	public String getTemplateFolderName () {
		return templateFolderName;
	}

	/**
	 * Displays a message dialog
	 * 
	 * @param style
	 * 	Style of the dialog
	 * @param title
	 * 	Title of the dialog
	 * @param text
	 *  Dialog text
	 *  
	 * @return 
	 * 		The result of the dialog
	 */
	static public int showMessageBox(final int style, final String title, final String text) {
		try {
			// Show an information dialog
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						style );
			messageBox.setText(title);
			messageBox.setMessage(text);
			return messageBox.open();
		} catch (Exception e) {
			
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
					// Show an information dialog
					MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							SWT.OK );
					messageBox.setText(title);
					messageBox.setMessage(text);
					messageBox.open();
			    }
			});
			return SWT.OK;
		}
	}


}
