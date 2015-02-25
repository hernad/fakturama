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

package com.sebulli.fakturama.misc;

import static com.sebulli.fakturama.Translate._;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Version;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ApplicationWorkbenchAdvisor;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetArray;
import com.sebulli.fakturama.data.DataSetList;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.views.datasettable.ViewListTable;

/**
 * This class load the country codes
 * 
 * @author Gerd Bartelt
 *
 */
public class CountryCodes {

	
	static int i;
	
	/**
	 * Updates the country codes list
	 * 
	 * @param version
	 * 	The new Version
	 */
	public static void update(final Version newVersion, final Version oldVersion, final StatusLineManager slm) {
		if ((newVersion.compareTo(new Version (1,5,0)) >= 0 ) && 
		    (oldVersion.compareTo(new Version (1,5,0)) < 0  )){
		
			// Rename countrycodes to countrycodes_2
			for (DataSetList list: Data.INSTANCE.getListEntries().getActiveDatasets()) {
				if (list.getStringValueByKey("category").equals("countrycodes"))
					list.setStringValueByKey("category", "countrycodes_2");
			}
			
			// Load all country codes
			loadFromRecouces(Data.INSTANCE.getListEntries(), slm);

		}
	}
	
	/**
	 * Loads all country codes files
	 */
	public static void loadFromRecouces(final DataSetArray<DataSetList> list, final StatusLineManager slm) {

		// Loads all country codes files
		loadListFromRecouces( "countrycodes_2" , Activator.getDefault().getBundle().getResource("ISO3166_alpha2.txt"), list, slm);
		loadListFromRecouces( "countrycodes_3" , Activator.getDefault().getBundle().getResource("ISO3166_alpha3.txt"), list, slm);
		
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
				// Refresh the list view 
		    	try {
					ApplicationWorkbenchAdvisor.refreshView(ViewListTable.ID);
		    	}
		    	catch (Exception e) {
		    	}
		    }
		});

	}
	
	
	/**
	 * Loads the country codes from resources
	 */
	private static void loadListFromRecouces(String category, URL url, DataSetArray<DataSetList> list, final StatusLineManager slm) {

		
		i = 0;

		try {
			// Open the resource message po file.
			if (url == null)
				return;
			
			InputStream	in = url.openStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF8"));
	        String strLine;
	        
	        //Read file line by line
	        while ((strLine = br.readLine()) != null)  {
	        	strLine = strLine.trim();
	        	
				i++;
				if (slm != null) {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
							//T: Message in the status bar
					    	slm.setMessage(_("Importing country code " + i));
					    }
					});
				}

	        	// Ignore empty and comments
	        	if (!strLine.isEmpty() && !strLine.startsWith("#")) {
	        		
	        		// Split entrys by a "="
	        		String parts[] = strLine.split("=", 2);
	        		if (parts.length == 2) {
	        			String key = parts[0].trim();
	        			String value = parts[1].trim();

	        			// Use only uppercase keys without special characters
	        			key = DataUtils.replaceAllAccentedChars(key).toUpperCase();
        				DataSetList newListEntry = new DataSetList(category, key, value);
        				list.addNewDataSetIfNew(newListEntry);
	        		}
	        	}
	        }
	        //Close the input stream
	        in.close();

		}
		catch (IOException e) {
			Logger.logError(e, "Error loading " + url.getFile());
		}
	}
	
	/**
	 * Gets the country code by the country name
	 * 
	 * @param country
	 * 	Name of the country
	 * @param codeNr
	 * 		"2" or "3"
	 * @return
	 * The 2 or 3 digits country code
	 */
	public static String getCountryCodeByCountry (String country, String codeNr) {
		DataSetList test = new DataSetList("countrycodes_"+ codeNr, DataUtils.replaceAllAccentedChars(country), "");
		DataSetList existing = Data.INSTANCE.getListEntries().getExistingDataSet( test );
		if (existing!= null)
			return existing.getStringValueByKey("value");
		else 
			return "";

	}
}
