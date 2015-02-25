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

package com.sebulli.fakturama.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.misc.DataUtils;

public class BackupManager {

	public static void createBackup() {

		// Get the path to the workspace
		String workspacePath = Workspace.INSTANCE.getWorkspace();
		if (workspacePath.length() == 0)
			return;

		workspacePath += "/";
		String backupPath = workspacePath + "Backup";

		// Create the backup folder, if it dosn't exist.
		File directory = new File(backupPath);
		if (!directory.exists())
			directory.mkdirs();

		// Filename of the zip file
		String dateString = DataUtils.DateAndTimeOfNowAsLocalString();
		dateString = dateString.replace(" ", "_");
		dateString = dateString.replace(":", "");

		backupPath += "/Backup_" + dateString + ".zip";

		// The file to add to the ZIP archive
		ArrayList<String> backupedFiles = new ArrayList<String>();
		backupedFiles.add("Database/Database.properties");
		backupedFiles.add("Database/Database.script");

		FileInputStream in;
		byte[] data = new byte[1024];
		int read = 0;

		try {
			// Connect ZIP archive with stream
			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(backupPath));

			// Set mode
			zip.setMethod(ZipOutputStream.DEFLATED);

			// Zip all files
			for (int i = 0; i < backupedFiles.size(); i++) {

				String backupedFile = backupedFiles.get(i);

				try {

					File inFile = new File(workspacePath + backupedFile);
					if (inFile.exists()) {
						in = new FileInputStream(workspacePath + backupedFile);

						if (in != null) {

							// Create a new entry
							ZipEntry entry = new ZipEntry(backupedFile);

							// Add a new entry to the archive
							zip.putNextEntry(entry);

							// Add the data
							while ((read = in.read(data, 0, 1024)) != -1)
								zip.write(data, 0, read);

							zip.closeEntry(); // Close the entry
							in.close();
						}
					}
				}
				catch (Exception e) {
					Logger.logError(e, "Error during file backup:" + backupedFile);
				}
			}
			zip.close();
		}
		catch (IOException ex) {
			Logger.logError(ex, "Error during backup");
		}
	}
}
