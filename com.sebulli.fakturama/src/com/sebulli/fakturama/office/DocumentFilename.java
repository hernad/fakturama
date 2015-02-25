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

package com.sebulli.fakturama.office;

import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.misc.DocumentType;

/**
 * This class provides functionality to get the path, the filename and the
 * filename extension of an OpenOffice document template file.
 * 
 * @author Gerd Bartelt
 */
public class DocumentFilename {

	// Path, name and extension of the filename
	private String path;
	private String name;
	private String extension;

	/**
	 * Constructor Create the file name and extract the extension
	 * 
	 * @param path
	 *            Path to the file
	 * @param filename
	 *            The file name with extension
	 */
	public DocumentFilename(String path, String filename) {
		this.path = path;
		int pPos = filename.lastIndexOf(".");
		if (pPos > 0) {

			// Extract name and extension
			this.name = filename.substring(0, pPos);
			this.extension = filename.substring(pPos, filename.length());

		}
		else {

			// There is no extension
			this.name = filename;
			this.extension = "";
		}
	}

	/**
	 * Returns the path
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the file name without the extension
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the extension
	 * 
	 * @return
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Returns the path and name with extension
	 * 
	 * @return
	 */
	public String getPathAndFilename() {
		return path + name + extension;
	}

	/**
	 * Get the relative path of the template
	 * 
	 * @param doctype
	 * 		The doctype defines the path
	 * @return
	 * 		The path as string
	 */
	public static String getRelativeFolder(DocumentType doctype) {
		return "/Templates/" + doctype.getTypeAsString() + "/";
	}
	
	/**
	 * Get the localized relative path of the template
	 * 
	 * @param doctype
	 * 		The doctype defines the path
	 * @return
	 * 		The path as string
	 */
	public static String getLocalizedRelativeFolder(DocumentType doctype) {
		return "/" + Workspace.INSTANCE.getTemplateFolderName() + "/" + doctype.getString() + "/";
		
	}
	
	/**
	 * Check, if 2 filenames are equal.
	 * Test only the relative path and use the parameter "folder" to
	 * separate the relative path from the absolute one.
	 *  
	 * @param fileName1
	 * @param fileName2
	 * @param folder
	 * 		The folder name to separate the relative path
	 * @return
	 * 		True, if both are equal
	 */
	public static boolean filesAreEqual(String fileName1, String fileName2, String folder) {
		
		int pos;
		pos = fileName1.indexOf(folder);
		if (pos >= 0)
			fileName1 = fileName1.substring(pos);

		pos = fileName2.indexOf(folder);
		if (pos >= 0)
			fileName2 = fileName2.substring(pos);

		
		return fileName1.equals(fileName2);
	}
	
	/**
	 * Test, if 2 template filenames are equal.
	 * The absolute path is ignored
	 * 
	 * @param fileName1
	 * @param fileName2
	 * 
	 * @return
	 * 		True, if both filenames are equal
	 */
	public static boolean filesAreEqual(String fileName1, String fileName2) {
		
		// Test, if also the absolute path is equal
		if (fileName1.equals(fileName2))
			return true;
		
		// If not, use the unlocalized folder names
		if (filesAreEqual(fileName1,fileName2, "/Templates/"))
			return true;

		// Use the localized folder names
		if (filesAreEqual(fileName1,fileName2, "/" + Workspace.INSTANCE.getTemplateFolderName() + "/"))
			return true;
		
		return false;
	}
	
	
}
