/* 
 * Fakturama - database checker - http://fakturama.sebulli.com
 * 
 * Copyright (C) 2014 Gerd Bartelt
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Bartelt - initial API and implementation
 */
package com.sebulli.fakturama.database_check;

/**
 * One columns of the table header
 * 
 * @author Gerd Bartelt
 *
 */
public class TableColumn {
	
	// Name of the columns
	private String name;
	// Type of the data in the column
	private Columntype type;
	
	/**
	 * Constructor
	 * Create a column with name and type
	 * 
	 * @param name
	 * @param type
	 */
	public TableColumn (String name, String type) {
		this.name = name;
		
		switch (type) {
		case "INTEGER": this.type = Columntype.INTEGER; break;
		case "BOOLEAN": this.type = Columntype.BOOLEAN; break;
		case "DOUBLE": this.type = Columntype.DOUBLE; break;
		case "VARCHAR(256)": this.type = Columntype.VARCHAR_256; break;
		case "VARCHAR(32768)": this.type = Columntype.VARCHAR_32768; break;
		case "VARCHAR(60000)": this.type = Columntype.VARCHAR_60000; 
//		Logger.getInstance().logWarning("Old data type VARCHAR(60000) is used");
		break;
		default: 
			this.type = Columntype.NONE;
			Logger.getInstance().logError("Wrong columntype. Name:" + name +" type: " + type );
		}

	}
	
	/**
	 * Getter of the colum name
	 * 
	 * @return the colum name
	 */
	public String getName () {
		return name;
	}
	
	/**
	 * Getter of the column type
	 * 
	 * @return the column type
	 */
	public Columntype getType () {
		return type;
	}
	
	
}
