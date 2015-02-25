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
 * List with all types of data
 * 
 * @author Gerd Bartelt
 *
 */
public enum Columntype {
		/**
		 * All types used by the data base
		 */
		NONE, INTEGER, BOOLEAN, DOUBLE, VARCHAR_256, VARCHAR_32768, VARCHAR_60000;
}
