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

package com.sebulli.fakturama.myexporter;



/**
 * This class generates a list with all products
 * 
 * @author Gerd Bartelt
 */
public class Exporter{
	
	/**
	 * Constructor
	 * 
	 */
	public Exporter() {
		super();
	}


	// Do the export job.
	public boolean export() {

		System.out.println("Do the export job here.");
		
		// True = Export was successful
		return true;
	}

}
