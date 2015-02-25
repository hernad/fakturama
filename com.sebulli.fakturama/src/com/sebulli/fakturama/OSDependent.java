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

import org.eclipse.core.runtime.Platform;

/**
 * These are the OS-dependent settings.
 * 
 * @author Gerd Bartelt
 */
public class OSDependent {

	/**
	 * Test, if it is a Mac OSX
	 * 
	 * @return TRUE, if it one
	 */
	public static boolean isMacOSX() {
		return Platform.getOS().equalsIgnoreCase("macosx");
	}

	/**
	 * Test, if it is a Linux system
	 * 
	 * @return TRUE, if it one
	 */
	public static boolean isLinux() {
		return Platform.getOS().equalsIgnoreCase("linux");
	}

	/**
	 * Test, if it is a Windows System
	 * 
	 * @return TRUE, if it one
	 */
	public static boolean isWin() {
		return Platform.getOS().toLowerCase().startsWith("win");
	}

	/**
	 * Returns the OS dependent program folder
	 * 
	 * @return Program folder as string
	 */
	public static String getProgramFolder() {

		if (isMacOSX())
			return "/Applications/";

		if (isLinux())
			return "/usr/lib/";

		if (isWin())
			return "C:\\Program Files\\";

		return "";

	}

	/**
	 * Returns the OS dependent default path of the OpenOffice installation
	 * 
	 * @return Default path as string
	 */
	public static String getOODefaultPath() {

		if (isMacOSX())
			return getProgramFolder() + "OpenOffice.org.app";

		if (isLinux())
			return getProgramFolder() + "openoffice";

		if (isWin())
			return getProgramFolder() + "OpenOffice.org 3";

		return "";

	}

	/**
	 * Returns the OpenOffice binary-
	 * 
	 * @param path
	 *            of the OpenOffice folder
	 * @return Full Path of the the binary.
	 */
	public static String getOOBinary(String path) {

		if (isMacOSX())
			return path + "/Contents/MacOS/soffice";

		if (isLinux())
			return path + "/program/soffice";

		if (isWin())
			return path + "\\program\\soffice.exe";

		return "";
	}

	/**
	 * Test, if it is allowed to add an about menu to the menu bar. In some OS
	 * the about menu is set to the menu bar by the OS. So, it is not necessary
	 * to add it twice.
	 * 
	 * @return TRUE, if it is necessary
	 */
	public static boolean canAddAboutMenuItem() {
		return !isMacOSX();
	}

	/**
	 * Test, if it is allowed to add an preference menu to the menu bar. In some
	 * OS the about menu is set to the menu bar by the OS. So, it is not
	 * necessary to add it twice.
	 * 
	 * @return TRUE, if it is necessary
	 */
	public static boolean canAddPreferenceAboutMenu() {
		return !isMacOSX();
	}

	/**
	 * Test, if OpenOffice is in an app archive instead a program folder.
	 * 
	 * @return TRUE, if it an app
	 */
	public static boolean isOOApp() {
		return isMacOSX();
	}

	/**
	 * Returns the OS dependent new line sequence
	 * 
	 * @return new line sequence
	 */
	public static String getNewLine() {

		if (isMacOSX())
			return "\n";

		if (isLinux())
			return "\n";

		if (isWin())
			return "\r\n";

		return "";

	}

}
