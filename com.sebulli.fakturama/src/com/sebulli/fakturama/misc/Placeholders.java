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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetContact;
import com.sebulli.fakturama.data.DataSetDocument;


public class Placeholders {
	
	
	// all placeholders
	private static String placeholders[] = {
			"YOURCOMPANY.COMPANY",
			"YOURCOMPANY.OWNER",
			"YOURCOMPANY.OWNER.FIRSTNAME",
			"YOURCOMPANY.OWNER.LASTNAME",
			"YOURCOMPANY.STREET",
			"YOURCOMPANY.STREETNAME",
			"YOURCOMPANY.STREETNO",
			"YOURCOMPANY.ZIP",
			"YOURCOMPANY.CITY",
			"YOURCOMPANY.COUNTRY",
			"YOURCOMPANY.EMAIL",
			"YOURCOMPANY.PHONE",
			"YOURCOMPANY.PHONE.PRE",
			"YOURCOMPANY.PHONE.POST",
			"YOURCOMPANY.FAX",
			"YOURCOMPANY.FAX.PRE",
			"YOURCOMPANY.FAX.POST",
			"YOURCOMPANY.WEBSITE",
			"YOURCOMPANY.VATNR",
			"YOURCOMPANY.TAXOFFICE",
			"YOURCOMPANY.BANKACCOUNTNR",
			"YOURCOMPANY.BANK",
			"YOURCOMPANY.BANKCODE",
			"YOURCOMPANY.IBAN",
			"YOURCOMPANY.BIC",
			"YOURCOMPANY.CREDITORID",
			"DOCUMENT.DATE",
			"DOCUMENT.ADDRESSES.EQUAL",
			"DOCUMENT.ADDRESS",
			"DOCUMENT.DELIVERYADDRESS",
			"DOCUMENT.DIFFERENT.ADDRESS",
			"DOCUMENT.DIFFERENT.DELIVERYADDRESS",
			"DOCUMENT.TYPE",
			"DOCUMENT.NAME",
			"DOCUMENT.CUSTOMERREF",
			"DOCUMENT.CONSULTANT",
			"DOCUMENT.SERVICEDATE",
			"DOCUMENT.MESSAGE",
			"DOCUMENT.MESSAGE1",
			"DOCUMENT.MESSAGE2",
			"DOCUMENT.MESSAGE3",
			"DOCUMENT.TRANSACTION",
			"DOCUMENT.INVOICE",
			"DOCUMENT.WEBSHOP.ID",
			"DOCUMENT.WEBSHOP.DATE",
			"DOCUMENT.ORDER.DATE",
			"DOCUMENT.ITEMS.GROSS",
			"DOCUMENT.ITEMS.NET",
			"DOCUMENT.ITEMS.COUNT",
			"DOCUMENT.TOTAL.NET",
			"DOCUMENT.TOTAL.VAT",
			"DOCUMENT.TOTAL.GROSS",
			"DOCUMENT.DEPOSIT.DEPOSIT",
			"DOCUMENT.DEPOSIT.FINALPAYMENT",
			"DOCUMENT.DEPOSIT.DEP_TEXT",
			"DOCUMENT.DEPOSIT.FINALPMT_TEXT",
			"DOCUMENT.REFERENCE.OFFER",
			"DOCUMENT.REFERENCE.ORDER",
			"DOCUMENT.REFERENCE.CONFIRMATION",
			"DOCUMENT.REFERENCE.INVOICE",
			"DOCUMENT.REFERENCE.INVOICE.DATE",
			"DOCUMENT.REFERENCE.DELIVERY",
			"DOCUMENT.REFERENCE.CREDIT",
			"DOCUMENT.REFERENCE.DUNNING",
			"DOCUMENT.REFERENCE.PROFORMA",
			"ITEMS.DISCOUNT.PERCENT",
			"ITEMS.DISCOUNT.NET",
			"ITEMS.DISCOUNT.GROSS",

			"ITEMS.DISCOUNT.VALUE",
			"ITEMS.DISCOUNT.NETVALUE",
			"ITEMS.DISCOUNT.TARAVALUE",
			"ITEMS.DISCOUNT.DISCOUNTPERCENT",
			"ITEMS.DISCOUNT.DAYS",
			"ITEMS.DISCOUNT.DUEDATE",

			"SHIPPING.NET",
			"SHIPPING.VAT",
			"SHIPPING.GROSS",
			"SHIPPING.DESCRIPTION",
			"SHIPPING.VAT.DESCRIPTION",
			"DOCUMENT.DUNNING.LEVEL",
			"PAYMENT.TEXT",
			"PAYMENT.DESCRIPTION",
			"PAYMENT.PAID.VALUE",
			"PAYMENT.PAID.DATE",
			"PAYMENT.DUE.DAYS",
			"PAYMENT.DUE.DATE",
			"PAYMENT.PAID",
			"ADDRESS.FIRSTLINE",
			"ADDRESS",
			"ADDRESS.GENDER",
			"ADDRESS.GREETING",
			"ADDRESS.TITLE",
			"ADDRESS.NAME",
			"ADDRESS.BIRTHDAY",
			"ADDRESS.NAMEWITHCOMPANY",
			"ADDRESS.FIRSTNAME",
			"ADDRESS.LASTNAME",
			"ADDRESS.COMPANY",
			"ADDRESS.STREET",
			"ADDRESS.STREETNAME",
			"ADDRESS.STREETNO",
			"ADDRESS.ZIP",
			"ADDRESS.CITY",
			"ADDRESS.COUNTRY",
			"ADDRESS.COUNTRY.CODE2",
			"ADDRESS.COUNTRY.CODE3",
			"DELIVERY.ADDRESS.FIRSTLINE",
			"DELIVERY.ADDRESS",
			"DELIVERY.ADDRESS.GENDER",
			"DELIVERY.ADDRESS.GREETING",
			"DELIVERY.ADDRESS.TITLE",
			"DELIVERY.ADDRESS.NAME",
			"DELIVERY.ADDRESS.BIRTHDAY",
			"DELIVERY.ADDRESS.NAMEWITHCOMPANY",
			"DELIVERY.ADDRESS.FIRSTNAME",
			"DELIVERY.ADDRESS.LASTNAME",
			"DELIVERY.ADDRESS.COMPANY",
			"DELIVERY.ADDRESS.STREET",
			"DELIVERY.ADDRESS.STREETNAME",
			"DELIVERY.ADDRESS.STREETNO",
			"DELIVERY.ADDRESS.ZIP",
			"DELIVERY.ADDRESS.CITY",
			"DELIVERY.ADDRESS.COUNTRY",
			"DELIVERY.ADDRESS.COUNTRY.CODE2",
			"DELIVERY.ADDRESS.COUNTRY.CODE3",
			"ADDRESS.BANK.ACCOUNT.HOLDER",
			"ADDRESS.BANK.ACCOUNT",
			"ADDRESS.BANK.CODE",
			"ADDRESS.BANK.NAME",
			"ADDRESS.BANK.IBAN",
			"ADDRESS.BANK.BIC",
			"DEBITOR.MANDATREF",
			"ADDRESS.NR",
			"ADDRESS.PHONE",
			"ADDRESS.PHONE.PRE",
			"ADDRESS.PHONE.POST",
			"ADDRESS.FAX",
			"ADDRESS.FAX.PRE",
			"ADDRESS.FAX.POST",
			"ADDRESS.MOBILE",
			"ADDRESS.MOBILE.PRE",
			"ADDRESS.MOBILE.POST",
			"ADDRESS.SUPPLIER.NUMBER",
			"ADDRESS.EMAIL",
			"ADDRESS.WEBSITE",
			"ADDRESS.VATNR",
			"ADDRESS.NOTE",
			"ADDRESS.DISCOUNT"			
	};
	
	private static NumberFormat localizedNumberFormat = NumberFormat.getInstance(Locale.getDefault());;

	
	/**
	 * Returns the first name of a complete name
	 * 
	 * @param name
	 * 		First name and last name
	 * @return
	 * 		Only the first name
	 */
	public static String getFirstName (String name) {
		String s = name.trim();
		int lastSpace = s.lastIndexOf(" ");
		if (lastSpace > 0)
			return s.substring(0, lastSpace).trim();
		else
			return "";
	}
	
	/**
	 * Returns the last name of a complete name
	 * 
	 * @param name
	 * 		First name and last name
	 * @return
	 * 		Only the last name
	 */
	public static String getLastName (String name) {
		String s = name.trim();
		int lastSpace = s.lastIndexOf(" ");
		if (lastSpace > 0)
			return s.substring(lastSpace + 1).trim();
		else
			return "";
	}
	
	/**
	 * Returns the street name without the number
	 * 
	 * @param streetWithNo
	 * 		
	 * @return
	 * 		Only the street name
	 */
	public static String getStreetName (String streetWithNo) {
		String s = streetWithNo.trim();
		int indexNo = 0;
		
		// Search for the number
		Matcher matcher = Pattern.compile( "\\d+" ).matcher( s );
		if ( matcher.find() ) {
			indexNo = matcher.start();
		}
		
		// Extract the street
		if (indexNo > 0)
			return s.substring(0, indexNo).trim();
		else
			return s;
	}

	/**
	 * Returns the street number without the name
	 * 
	 * @param streetWithNo
	 * 		
	 * @return
	 * 		Only the street No
	 */
	public static String getStreetNo (String streetWithNo) {
		String s = streetWithNo.trim();
		int indexNo = 0;
		
		// Search for the number
		Matcher matcher = Pattern.compile( "\\d+" ).matcher( s );
		if ( matcher.find() ) {
			indexNo = matcher.start();
		}
		
		// Extract the Number
		if (indexNo > 0)
			return s.substring(indexNo).trim();
		else
			return "";
	}
	
	/**
	 * Get a part of the telephone number
	 * 
	 * @param pre
	 * 		TRUE, if the area code should be returned
	 * @return
	 * 		Part of the telephone number
	 */
	private static String getTelPrePost(String no, boolean pre){
		// if no contains "/" ord " " (space) then split there
		String parts[] = no.trim().split("[ /]", 2);
		
		// Split the number
		if (parts.length < 2) {
			String tel = parts[0];
			// devide the number at the 4th position
			if (tel.length() > 4) {
				if (pre)
					return tel.substring(0, 4);
				else
					return tel.substring(4);
			}
			// The number is very short
			if (pre)
				return "";
			else
				return tel;
		}
		// return the first or the second part
		else {
			if (pre)
				return parts[0];
			else
				return parts[1];
		}
	}
	
	static public String getDataFromAddressField(String address, String key) {
		
		String addressName = "";
		String addressFirstName = "";
		String addressLastName = "";
		String addressLine = "";
		String addressStreet = "";
		String addressZIP = "";
		String addressCity = "";
		String addressCountry = "";

		
		String[] addressLines = address.split("\\n");
		
		Boolean countryFound = false;
		Boolean cityFound = false;
		Boolean streetFound = false;
		String line = "";
		addressLine = "";
		
		// The first line is the name
		addressName = addressLines[0];
		addressFirstName = getFirstName(addressName);
		addressLastName = getLastName(addressName);
		
		// Analyze all the other lines. Start with the last
		for (int lineNr = addressLines.length -1; lineNr >= 1;lineNr--) {
			
			// Get one line
			line = addressLines[lineNr].trim();
			
			// Use only non-empty lines
			if (!line.isEmpty()) {
				
				if (!countryFound || !cityFound) {
					Matcher matcher = Pattern.compile( "\\d+" ).matcher( line );
					
					// A Number was found. So this line was not the country, it must be the ZIP code
					if ( matcher.find() ) {
						if (matcher.start() < 4)  {
							int codelen = matcher.end() - matcher.start();
							
							// Extract the ZIP code
							if (codelen >= 4 && codelen <=5 ) {
								addressZIP = matcher.group();

								// and the city
								addressCity = line.substring(matcher.end()+1).trim();
								
							}
							cityFound = true;
							countryFound = true;
						}
					}
					else {
						// It must be the country
						addressCountry =  line;
						countryFound = true;
					}
				}
				// City and maybe country were found. Search now for the street.
				else if (!streetFound){
					Matcher matcher = Pattern.compile( "\\d+" ).matcher( line );
					
					// A Number was found. This must be the street number
					if ( matcher.find() ) {
						if (matcher.start() > 3)  {
							// Extract the street number
							addressStreet  = line;
							streetFound = true;
						}
					}
				}
				// Street, city and maybe country were found. 
				// Search now for additional address information
				else {
					if (!addressLine.isEmpty())
						addressLine +=" ";
					addressLine = line;
				}
				
			}
		}

		if (key.equals("name")) return addressName;
		if (key.equals("firstname")) return addressFirstName;
		if (key.equals("lastname")) return addressLastName;
		if (key.equals("addressfirstline")) return addressLine;
		if (key.equals("street")) return addressStreet;
		if (key.equals("streetname")) return getStreetName(addressStreet);
		if (key.equals("streetno")) return getStreetNo(addressStreet);
		if (key.equals("zip")) return addressZIP;
		if (key.equals("city")) return addressCity;
		if (key.equals("county")) return addressCountry;
		return "";
	}
	

	/**
	 * Replaces all line breaks by a "-"
	 * 
	 * @param s
	 * 	The string in multiple lines
	 * @param replacement
	 * 	The replacement
	 * @return
	 * 	The string in one line, seperated by a "-"
	 */
	private static String StringInOneLine(String s, String replacement) {
		// Convert CRLF to LF 
		s = DataUtils.convertCRLF2LF(s).trim();
		// Replace line feeds by a " - "
		s = s.replaceAll("\\n", replacement);
		return s;
	}

	/**
	 * Removes the quotation marks of a String
	 * @param s
	 * 	The string with quotation marks
	 * @return
	 *  The string without them
	 */
	private static String removeQuotationMarks(String s) {
		
		// remove leading and trailing spaces
		s = s.trim();
		
		// Remove the leading
		if (s.startsWith("\""))
			s = s.substring(1);

		// Remove the trailing
		if (s.endsWith("\""))
			s = s.substring(0, s.length() - 1);
		
		return s;
	}
	
	/**
	 * Replace the placeholder values by a value in a list
	 * 
	 * @param replacements
	 * 		A list of replacements, separates by a ";"
	 * 		eg: {"Belgien","BEL";"Dänemark","DNK"}
	 * @param value
	 * 		The input value
	 * @return
	 * 		The modified value
	 */
	private static String replaceValues(String replacements, String value) {
		
		// Remove spaces
		replacements = replacements.trim();
		
		// Remove the leading {
		if (replacements.startsWith("{"))
			replacements = replacements.substring(1);

		// Remove the trailing }
		if (replacements.endsWith("}"))
			replacements = replacements.substring(0, replacements.length() - 1);

		String parts[] = replacements.split(";");

		// Nothing to do
		if (parts.length < 1)
			return value;
		
		// get all parts
		for (String part : parts) {
			String twoStrings[] = part.split(",");
			if (twoStrings.length == 2) {
				
				//Escape sequence "%COMMA" for ","
				twoStrings[0] = twoStrings[0].replace("%COMMA", ",");
			    
				// Replace the value, if it is equal to the entry
				if (DataUtils.replaceAllAccentedChars(value).equalsIgnoreCase(
						DataUtils.replaceAllAccentedChars(removeQuotationMarks(twoStrings[0])))) {
					value = removeQuotationMarks(twoStrings[1]);
					return value;
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Interprets the placeholder parameters
	 * 
	 * @param placeholder
	 * 		Name of the placeholder
	 * @param value
	 * 		The value
	 * @return
	 * 		The value mofified by the parameters
	 */
	public static String interpretParameters(String placeholder, String value) {
		String par;
		
		if (value == null)
			return value;
		
		// The parameters "PRE" and "POST" are only used, if the
		// placeholder value is not empty
		if (!value.isEmpty()) {
			
			// Parameter "PRE"
			par = Placeholders.extractParam(placeholder,"PRE");
			if (!par.isEmpty())
					value =  removeQuotationMarks(par) + value;

			// Parameter "POST"
			par = Placeholders.extractParam(placeholder,"POST");
			if (!par.isEmpty())
					value += removeQuotationMarks(par);

			// Parameter "INONELINE"
			par = Placeholders.extractParam(placeholder,"INONELINE");
			if (!par.isEmpty())
				value = StringInOneLine(value, removeQuotationMarks(par));

			// Parameter "REPLACE"
			par = Placeholders.extractParam(placeholder,"REPLACE");
			if (!par.isEmpty())
				value = replaceValues(removeQuotationMarks(par) , value);

			// Parameter "FORMAT"
			par = Placeholders.extractParam(placeholder,"FORMAT");
			if (!par.isEmpty()) {
				try {
					Double parsedDouble = localizedNumberFormat.parse(value).doubleValue();
					value = DataUtils.DoubleToDecimalFormatedValue(parsedDouble, par);
				}
				catch (Exception e) {
					// TODO implement!
				}
			}
		}
		else {
			// Parameter "EMPTY"
			par = Placeholders.extractParam(placeholder,"EMPTY");
			if (!par.isEmpty())
					value = removeQuotationMarks(par);
		}
		
		// Encode some special characters
		value = Placeholders.encodeEntinities(value);
		return value;
	}
	
	/**
	 * Extract the placeholder values from a given document
	 * 
	 * @param document
	 * 		The document with all the values
	 * @param placeholder
	 * 		The placeholder to extract
	 * @return
	 * 		The extracted value
	 */
	public static String getDocumentInfo(DataSetDocument document, String placeholder) {
		String value = getDocumentInfoByPlaceholder(document, extractPlaceholderName(placeholder));
		return interpretParameters(placeholder, value);
	}
	
	
	/**
	 * Extract the value of the parameter of a placeholder
	 * 
	 * @param placeholder
	 * 	The placeholder name
	 * 
	 * @param param
	 * 	Name of the parameter to extract
	 * 
	 * @return
	 *  The extracted value
	 */
	public static  String extractParam(String placeholder, String param) {
		String s;
		
		// A parameter starts with "$" and ends with ":"
		param = "$" + param + ":";
		
		// Return, if parameter was not in placeholder's name
		if (!placeholder.contains(param))
			return "";

		// Extract the string after the parameter name
		s = placeholder.substring(placeholder.indexOf(param)+param.length());

		// Extract the string until the next parameter, or the end
		int i;
		i = s.indexOf("$");
		if ( i>0 )
			s= s.substring(0, i);
		else if (i == 0)
			s = "";
		
		i = s.indexOf(">");
		if ( i>0 )
			s= s.substring(0, i);
		else if (i == 0)
			s = "";

		// Return the value
		return s;
	}
	
	/**
	 * Extracts the placeholder name, separated by a $
	 * 
	 * @param s
	 * 		The placeholder with parameters
	 * @return
	 * 		The placeholder name without paramater
	 */
	private static String extractPlaceholderName(String s) {
		return s.split("\\$" , 2)[0];
	}
	
	/**
	 * Decode the special characters
	 * 
	 * @param s
	 * 	String to convert
	 * @return
	 *  Converted
	 */
	public static  String encodeEntinities(String s) {
	
		s = s.replaceAll("%LT", "<");
		s = s.replaceAll("%GT", ">");
		s = s.replaceAll("%NL", "\n");
		s = s.replaceAll("%TAB", "\t");
		s = s.replaceAll("%DOLLAR", Matcher.quoteReplacement("$"));
		s = s.replaceAll("%A_GRAVE", Matcher.quoteReplacement("À"));
		s = s.replaceAll("%A_ACUTE", Matcher.quoteReplacement("Á"));
		s = s.replaceAll("%A_CIRC", Matcher.quoteReplacement("Â"));
		s = s.replaceAll("%A_TILDE", Matcher.quoteReplacement("Ã"));
		s = s.replaceAll("%A_RING", Matcher.quoteReplacement("Å"));
		s = s.replaceAll("%C_CED", Matcher.quoteReplacement("Ç"));
		s = s.replaceAll("%E_GRAVE", Matcher.quoteReplacement("È"));
		s = s.replaceAll("%E_ACUTE", Matcher.quoteReplacement("É"));
		s = s.replaceAll("%E_CIRC", Matcher.quoteReplacement("Ê"));
		s = s.replaceAll("%I_GRAVE", Matcher.quoteReplacement("Ì"));
		s = s.replaceAll("%I_ACUTE", Matcher.quoteReplacement("Í"));
		s = s.replaceAll("%I_CIRC", Matcher.quoteReplacement("Î"));
		s = s.replaceAll("%O_GRAVE", Matcher.quoteReplacement("Ò"));
		s = s.replaceAll("%O_ACUTE", Matcher.quoteReplacement("Ó"));
		s = s.replaceAll("%O_CIRC", Matcher.quoteReplacement("Ô"));
		s = s.replaceAll("%O_TILDE", Matcher.quoteReplacement("Õ"));
		s = s.replaceAll("%O_STROKE", Matcher.quoteReplacement("Ø"));
		s = s.replaceAll("%U_GRAVE", Matcher.quoteReplacement("Ù"));
		s = s.replaceAll("%U_ACUTE", Matcher.quoteReplacement("Ú"));
		s = s.replaceAll("%U_CIRC", Matcher.quoteReplacement("Û"));
		s = s.replaceAll("%a_GRAVE", Matcher.quoteReplacement("à"));
		s = s.replaceAll("%a_ACUTE", Matcher.quoteReplacement("á"));
		s = s.replaceAll("%a_CIRC", Matcher.quoteReplacement("â"));
		s = s.replaceAll("%a_TILDE", Matcher.quoteReplacement("ã"));
		s = s.replaceAll("%a_RING", Matcher.quoteReplacement("å"));
		s = s.replaceAll("%c_CED", Matcher.quoteReplacement("ç"));
		s = s.replaceAll("%e_GRAVE", Matcher.quoteReplacement("è"));
		s = s.replaceAll("%e_ACUTE", Matcher.quoteReplacement("é"));
		s = s.replaceAll("%e_CIRC", Matcher.quoteReplacement("ê"));
		s = s.replaceAll("%i_GRAVE", Matcher.quoteReplacement("ì"));
		s = s.replaceAll("%i_ACUTE", Matcher.quoteReplacement("í"));
		s = s.replaceAll("%i_CIRC", Matcher.quoteReplacement("î"));
		s = s.replaceAll("%n_TILDE", Matcher.quoteReplacement("ñ"));
		s = s.replaceAll("%o_GRAVE", Matcher.quoteReplacement("ò"));
		s = s.replaceAll("%o_ACUTE", Matcher.quoteReplacement("ó"));
		s = s.replaceAll("%o_CIRC", Matcher.quoteReplacement("ô"));
		s = s.replaceAll("%o_TILDE", Matcher.quoteReplacement("õ"));
		s = s.replaceAll("%u_GRAVE", Matcher.quoteReplacement("ù"));
		s = s.replaceAll("%u_ACUTE", Matcher.quoteReplacement("ú"));
		s = s.replaceAll("%u_CIRC", Matcher.quoteReplacement("û"));
		
		return s;
	}
	

	
	/**
	 * Get Information from document.
	 * If there is no reference to a customer, use the address field to
	 * Extract the address
	 * 
	 * @param document
	 * 	The document
	 * @param key
	 * 	The key to extract
	 * @return
	 *  The extracted result
	 */
	private static String getDocumentInfoByPlaceholder(DataSetDocument document, String key) {
		
		// Get the company information from the preferences
		if (key.startsWith("YOURCOMPANY")) {
			if (key.equals("YOURCOMPANY.COMPANY")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_NAME");

			String owner = Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_OWNER");
			if (key.equals("YOURCOMPANY.OWNER")) return  owner;
			if (key.equals("YOURCOMPANY.OWNER.FIRSTNAME")) return  Placeholders.getFirstName(owner);
			if (key.equals("YOURCOMPANY.OWNER.LASTNAME")) return  Placeholders.getLastName(owner);

			String streetWithNo = Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_STREET");
			if (key.equals("YOURCOMPANY.STREET")) return  streetWithNo;
			if (key.equals("YOURCOMPANY.STREETNAME")) return  Placeholders.getStreetName(streetWithNo);
			if (key.equals("YOURCOMPANY.STREETNO")) return  Placeholders.getStreetNo(streetWithNo);

			if (key.equals("YOURCOMPANY.ZIP")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_ZIP");
			if (key.equals("YOURCOMPANY.CITY")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_CITY");
			if (key.equals("YOURCOMPANY.COUNTRY")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_COUNTRY");
			if (key.equals("YOURCOMPANY.EMAIL")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_EMAIL");
			if (key.equals("YOURCOMPANY.PHONE")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_TEL");
			if (key.equals("YOURCOMPANY.PHONE.PRE")) return  getTelPrePost(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_TEL"), true);
			if (key.equals("YOURCOMPANY.PHONE.POST")) return  getTelPrePost(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_TEL"), false);
			if (key.equals("YOURCOMPANY.FAX")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_FAX");
			if (key.equals("YOURCOMPANY.FAX.PRE")) return  getTelPrePost(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_FAX"), true);
			if (key.equals("YOURCOMPANY.FAX.POST")) return  getTelPrePost(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_FAX"), false);
			if (key.equals("YOURCOMPANY.WEBSITE")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_WEBSITE");
			if (key.equals("YOURCOMPANY.VATNR")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_VATNR");
			if (key.equals("YOURCOMPANY.TAXOFFICE")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_TAXOFFICE");
			if (key.equals("YOURCOMPANY.BANKACCOUNTNR")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANKACCOUNTNR");
			if (key.equals("YOURCOMPANY.BANKCODE")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANKCODE");
			if (key.equals("YOURCOMPANY.BANK")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANK");
			if (key.equals("YOURCOMPANY.IBAN")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_IBAN");
			if (key.equals("YOURCOMPANY.BIC")) return  Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BIC");
		}

		
		
		if (document == null)
			return null;
		
		DataSetContact contact;
		
		// Get the contact of the UniDataSet document
		int addressId = document.getIntValueByKey("addressid");

		// Is there a reference to a contact ?
		contact = null;
		if (addressId >= 0) {
			try {
				contact = Data.INSTANCE.getContacts().getDatasetById(addressId);
			}
			catch (Exception e) {
			}
		}

		if (key.equals("DOCUMENT.DATE")) return document.getFormatedStringValueByKey("date");
		if (key.equals("DOCUMENT.ADDRESSES.EQUAL")) return ((Boolean)document.deliveryAddressEqualsBillingAddress()).toString();

		// Get address and delivery address
		// with option "DIFFERENT" and without
		String deliverystring;
		String differentstring;
		// address and delivery address
		for (int i = 0;i<2 ; i++) {
			if (i==1)
				deliverystring = "delivery";
			else
				deliverystring = "";
			
			String s = document.getStringValueByKey(deliverystring + "address");
			
			//  with option "DIFFERENT" and without
			for (int ii = 0 ; ii<2; ii++) {
				if (ii==1)
					differentstring = ".DIFFERENT";
				else
					differentstring = "";
				if (ii==1) {
					if (document.deliveryAddressEqualsBillingAddress())
						s="";
				}
				if (key.equals("DOCUMENT" + differentstring +"."+ deliverystring.toUpperCase()+ "ADDRESS")) return s;
				
			}
		}
		
		// Get information from the document
		if (key.equals("DOCUMENT.TYPE")) return DocumentType.getString(document.getIntValueByKey("category"));
		if (key.equals("DOCUMENT.NAME")) return document.getStringValueByKey("name");
		if (key.equals("DOCUMENT.CUSTOMERREF")) return document.getStringValueByKey("customerref");
		if (key.equals("DOCUMENT.CONSULTANT")) return document.getStringValueByKey("consultant");
		if (key.equals("DOCUMENT.SERVICEDATE")) return document.getFormatedStringValueByKey("servicedate");
		if (key.equals("DOCUMENT.MESSAGE")) return document.getStringValueByKey("message");
		if (key.equals("DOCUMENT.MESSAGE1")) return document.getStringValueByKey("message");
		if (key.equals("DOCUMENT.MESSAGE2")) return document.getStringValueByKey("message2");
		if (key.equals("DOCUMENT.MESSAGE3")) return document.getStringValueByKey("message3");
		if (key.equals("DOCUMENT.TRANSACTION")) return document.getStringValueByKey("transaction");
		if (key.equals("DOCUMENT.INVOICE")) return document.getStringValueByKeyFromOtherTable("invoiceid.DOCUMENTS:name");
		if (key.equals("DOCUMENT.WEBSHOP.ID")) return document.getStringValueByKey("webshopid");
		if (key.equals("DOCUMENT.WEBSHOP.DATE")) return document.getFormatedStringValueByKey("webshopdate");
		if (key.equals("DOCUMENT.ORDER.DATE")) return document.getFormatedStringValueByKey("orderdate");
		if (key.equals("DOCUMENT.ITEMS.GROSS")) return document.getSummary().getItemsGross().asFormatedRoundedString();
		if (key.equals("DOCUMENT.ITEMS.NET")) return document.getSummary().getItemsNet().asFormatedRoundedString();
		if (key.equals("DOCUMENT.TOTAL.NET")) return document.getSummary().getTotalNet().asFormatedRoundedString();
		if (key.equals("DOCUMENT.TOTAL.VAT")) return document.getSummary().getTotalVat().asFormatedRoundedString();
		if (key.equals("DOCUMENT.TOTAL.GROSS")) return document.getSummary().getTotalGross().asFormatedString();
		if (key.equals("DOCUMENT.ITEMS.COUNT")) return String.format("%d", document.getItems().getActiveDatasets().size());

		if (key.equals("DOCUMENT.DEPOSIT.DEPOSIT")) return document.getSummary().getDeposit().asFormatedString();
		if (key.equals("DOCUMENT.DEPOSIT.FINALPAYMENT")) return document.getSummary().getFinalPayment().asFormatedString();
		if (key.equals("DOCUMENT.DEPOSIT.DEP_TEXT")) return  Activator.getDefault().getPreferenceStore().getString("DEPOSIT_TEXT");
		if (key.equals("DOCUMENT.DEPOSIT.FINALPMT_TEXT")) return  Activator.getDefault().getPreferenceStore().getString("FINALPAYMENT_TEXT");
		if (key.equals("DOCUMENT.DEPOSIT.DEP_TEXT")) return  Activator.getDefault().getPreferenceStore().getString("DEPOSIT_TEXT");
		if (key.equals("DOCUMENT.DEPOSIT.FINALPMT_TEXT")) return  Activator.getDefault().getPreferenceStore().getString("FINALPAYMENT_TEXT");

		if (key.equals("ITEMS.DISCOUNT.PERCENT")) return document.getFormatedStringValueByKey("itemsdiscount");
		if (key.equals("ITEMS.DISCOUNT.NET")) return document.getSummary().getDiscountNet().asFormatedRoundedString();
		if (key.equals("ITEMS.DISCOUNT.GROSS")) return document.getSummary().getDiscountGross().asFormatedRoundedString();

		if (key.equals("ITEMS.DISCOUNT.DAYS")) return document.getStringValueByKeyFromOtherTable("paymentid.PAYMENTS:discountdays");
		if (key.equals("ITEMS.DISCOUNT.DUEDATE")) {
			return getDiscountDueDate(document);
		}
		if (key.equals("ITEMS.DISCOUNT.DISCOUNTPERCENT")) return DataUtils.DoubleToFormatedPercent(document.getDoubleValueByKeyFromOtherTable("paymentid.PAYMENTS:discountvalue"));
		double percent = document.getDoubleValueByKeyFromOtherTable("paymentid.PAYMENTS:discountvalue");
		if (key.equals("ITEMS.DISCOUNT.VALUE")) {
			return DataUtils.DoubleToFormatedPriceRound(document.getSummary().getTotalGross().asDouble() * (1 - percent));
		}
		if (key.equals("ITEMS.DISCOUNT.NETVALUE")) {
			return DataUtils.DoubleToFormatedPriceRound(document.getSummary().getTotalNet().asDouble() * (1 - percent));
		}
		if (key.equals("ITEMS.DISCOUNT.TARAVALUE")) {
			return DataUtils.DoubleToFormatedPriceRound(document.getSummary().getTotalVat().asDouble() * (1 - percent));
		}

		if (key.equals("SHIPPING.NET")) return document.getSummary().getShippingNet().asFormatedString();
		if (key.equals("SHIPPING.VAT")) return document.getSummary().getShippingVat().asFormatedString();
		if (key.equals("SHIPPING.GROSS")) return document.getSummary().getShippingGross().asFormatedString();
//		if (key.equals("SHIPPING.NAME")) return document.getStringValueByKey("shippingname");
		if (key.equals("SHIPPING.DESCRIPTION")) return document.getStringValueByKey("shippingdescription");
		if (key.equals("SHIPPING.VAT.DESCRIPTION")) return document.getStringValueByKey("shippingvatdescription");
		if (key.equals("DOCUMENT.DUNNING.LEVEL")) return document.getStringValueByKey("dunninglevel");


		// Get the reference string to other documents
		if (key.startsWith("DOCUMENT.REFERENCE.")) {
			Transaction transaction = new Transaction(document);
			if (key.equals("DOCUMENT.REFERENCE.OFFER")) return transaction.getReference(DocumentType.OFFER);
			if (key.equals("DOCUMENT.REFERENCE.ORDER")) return transaction.getReference(DocumentType.ORDER);
			if (key.equals("DOCUMENT.REFERENCE.CONFIRMATION")) return transaction.getReference(DocumentType.CONFIRMATION);
			if (key.equals("DOCUMENT.REFERENCE.INVOICE")) return transaction.getReference(DocumentType.INVOICE);
			if (key.equals("DOCUMENT.REFERENCE.INVOICE.DATE")) return transaction.getFirstReferencedDocumentDate(DocumentType.INVOICE);
			if (key.equals("DOCUMENT.REFERENCE.DELIVERY")) return transaction.getReference(DocumentType.DELIVERY);
			if (key.equals("DOCUMENT.REFERENCE.CREDIT")) return transaction.getReference(DocumentType.CREDIT);
			if (key.equals("DOCUMENT.REFERENCE.DUNNING")) return transaction.getReference(DocumentType.DUNNING);
			if (key.equals("DOCUMENT.REFERENCE.PROFORMA")) return transaction.getReference(DocumentType.PROFORMA);

		}
		
		if (key.equals("PAYMENT.TEXT")) {
			// Replace the placeholders in the payment text
			String paymenttext = createPaymentText(document, percent);
			return paymenttext;
		}
		
		if (key.equals("DOCUMENT.DUNNING.LEVEL")) return document.getStringValueByKey("dunninglevel");

		//setProperty("PAYMENT.NAME", document.getStringValueByKey("paymentname"));
		if (key.equals("PAYMENT.DESCRIPTION")) return document.getStringValueByKey("paymentdescription");
		if (key.equals("PAYMENT.PAID.VALUE")) return DataUtils.DoubleToFormatedPriceRound(document.getDoubleValueByKey("payvalue"));
		if (key.equals("PAYMENT.PAID.DATE")) return document.getFormatedStringValueByKey("paydate");
		if (key.equals("PAYMENT.DUE.DAYS")) return Integer.toString(document.getIntValueByKey("duedays"));
		if (key.equals("PAYMENT.DUE.DATE")) return
				DataUtils.DateAsLocalString(DataUtils.AddToDate(document.getStringValueByKey("date"), document.getIntValueByKey("duedays")));
		if (key.equals("PAYMENT.PAID")) return document.getStringValueByKey("paid");

		
		String key2;
		String addressField;
		
		if (key.startsWith("DELIVERY.")) {
			key2 = key.substring(9);
			addressField = document.getStringValueByKey("deliveryaddress");
		}
		else {
			key2 = key;
			addressField = document.getStringValueByKey("address");
		}

		if (key2.equals("ADDRESS.FIRSTLINE")) return getDataFromAddressField(addressField,"addressfirstline");
		
		// There is a reference to a contact. Use this
		if (contact != null) {
			if (key.equals("ADDRESS")) return contact.getAddress(false);
			if (key.equals("ADDRESS.GENDER")) return contact.getGenderString(false);
			if (key.equals("ADDRESS.GREETING")) return contact.getGreeting(false);
			if (key.equals("ADDRESS.TITLE")) return contact.getStringValueByKey("title");
			if (key.equals("ADDRESS.NAME")) return contact.getName(false);
			if (key.equals("ADDRESS.BIRTHDAY")) {
				if ("".equals(contact.getStringValueByKey("birthday")))
					return "";
				else {
					return contact.getFormatedStringValueByKey("birthday");
				}
			}
			if (key.equals("ADDRESS.NAMEWITHCOMPANY")) return contact.getNameWithCompany(false);
			if (key.equals("ADDRESS.FIRSTANDLASTNAME")) return contact.getFirstAndLastName(false);
			if (key.equals("ADDRESS.FIRSTNAME")) return contact.getStringValueByKey("firstname");
			if (key.equals("ADDRESS.LASTNAME")) return contact.getStringValueByKey("name");
			if (key.equals("ADDRESS.COMPANY")) return contact.getStringValueByKey("company");
			if (key.equals("ADDRESS.STREET")) return contact.getStringValueByKey("street");
			if (key.equals("ADDRESS.STREETNAME")) return getStreetName(contact.getStringValueByKey("street"));
			if (key.equals("ADDRESS.STREETNO")) return getStreetNo(contact.getStringValueByKey("street"));
			if (key.equals("ADDRESS.ZIP")) return contact.getStringValueByKey("zip");
			if (key.equals("ADDRESS.CITY")) return contact.getStringValueByKey("city");
			if (key.equals("ADDRESS.COUNTRY")) return contact.getStringValueByKey("country");
			if (key.equals("ADDRESS.COUNTRY.CODE2")) return CountryCodes.getCountryCodeByCountry(contact.getStringValueByKey("country"),"2");
			if (key.equals("ADDRESS.COUNTRY.CODE3")) return CountryCodes.getCountryCodeByCountry(contact.getStringValueByKey("country"),"3");
			if (key.equals("DELIVERY.ADDRESS")) return contact.getAddress(true);
			if (key.equals("DELIVERY.ADDRESS.GENDER")) return contact.getGenderString(true);
			if (key.equals("DELIVERY.ADDRESS.GREETING")) return contact.getGreeting(true);
			if (key.equals("DELIVERY.ADDRESS.TITLE")) return contact.getStringValueByKey("delivery_title");
			if (key.equals("DELIVERY.ADDRESS.NAME")) return contact.getName(true);
			if (key.equals("DELIVERY.ADDRESS.BIRTHDAY")) {
				if ("".equals(contact.getStringValueByKey("delivery_birthday")))
					return "";
				else
					return contact.getFormatedStringValueByKey("delivery_birthday");
			}
			if (key.equals("DELIVERY.ADDRESS.NAMEWITHCOMPANY")) return contact.getNameWithCompany(true);
			if (key.equals("DELIVERY.ADDRESS.FIRSTNAME")) return contact.getStringValueByKey("delivery_firstname");
			if (key.equals("DELIVERY.ADDRESS.LASTNAME")) return contact.getStringValueByKey("delivery_name");
			if (key.equals("DELIVERY.ADDRESS.COMPANY")) return contact.getStringValueByKey("delivery_company");
			if (key.equals("DELIVERY.ADDRESS.STREET")) return contact.getStringValueByKey("delivery_street");
			if (key.equals("DELIVERY.ADDRESS.STREETNAME")) return getStreetName(contact.getStringValueByKey("delivery_street"));
			if (key.equals("DELIVERY.ADDRESS.STREETNO")) return getStreetNo(contact.getStringValueByKey("delivery_street"));
			if (key.equals("DELIVERY.ADDRESS.ZIP")) return contact.getStringValueByKey("delivery_zip");
			if (key.equals("DELIVERY.ADDRESS.CITY")) return contact.getStringValueByKey("delivery_city");
			if (key.equals("DELIVERY.ADDRESS.COUNTRY")) return contact.getStringValueByKey("delivery_country");
			if (key.equals("DELIVERY.ADDRESS.COUNTRY.CODE2")) return CountryCodes.getCountryCodeByCountry(contact.getStringValueByKey("delivery_country"),"2");
			if (key.equals("DELIVERY.ADDRESS.COUNTRY.CODE3")) return CountryCodes.getCountryCodeByCountry(contact.getStringValueByKey("delivery_country"),"3");
			if (key.equals("ADDRESS.BANK.ACCOUNT.HOLDER")) return contact.getStringValueByKey("account_holder");
			if (key.equals("ADDRESS.BANK.ACCOUNT")) return contact.getStringValueByKey("account");
			if (key.equals("ADDRESS.BANK.CODE")) return contact.getStringValueByKey("bank_code");
			if (key.equals("ADDRESS.BANK.NAME")) return contact.getStringValueByKey("bank_name");
			if (key.equals("ADDRESS.BANK.IBAN")) return contact.getStringValueByKey("iban");
			if (key.equals("ADDRESS.BANK.BIC")) return contact.getStringValueByKey("bic");
			if (key.equals("ADDRESS.NR")) return contact.getStringValueByKey("nr");
			if (key.equals("ADDRESS.PHONE")) return contact.getStringValueByKey("phone");
			if (key.equals("ADDRESS.PHONE.PRE")) return getTelPrePost(contact.getStringValueByKey("phone"), true);
			if (key.equals("ADDRESS.PHONE.POST")) return getTelPrePost(contact.getStringValueByKey("phone"), false);
			if (key.equals("ADDRESS.FAX")) return contact.getStringValueByKey("fax");
			if (key.equals("ADDRESS.FAX.PRE")) return getTelPrePost(contact.getStringValueByKey("fax"), true);
			if (key.equals("ADDRESS.FAX.POST")) return getTelPrePost(contact.getStringValueByKey("fax"), false);
			if (key.equals("ADDRESS.MOBILE")) return contact.getStringValueByKey("mobile");
			if (key.equals("ADDRESS.MOBILE.PRE")) return getTelPrePost(contact.getStringValueByKey("mobile"), true);
			if (key.equals("ADDRESS.MOBILE.POST")) return getTelPrePost(contact.getStringValueByKey("mobile"), false);
			if (key.equals("ADDRESS.SUPPLIER.NUMBER")) return contact.getStringValueByKey("suppliernumber");
			if (key.equals("ADDRESS.EMAIL")) return contact.getStringValueByKey("email");
			if (key.equals("ADDRESS.WEBSITE")) return contact.getStringValueByKey("website");
			if (key.equals("ADDRESS.VATNR")) return contact.getStringValueByKey("vatnr");
			if (key.equals("ADDRESS.NOTE")) return contact.getStringValueByKey("note");
			if (key.equals("ADDRESS.DISCOUNT")) return contact.getFormatedStringValueByKey("discount");
		}
		// There is no reference - Try to get the information from the address field
		else {
			if (key2.equals("ADDRESS.GENDER")) return "";
			if (key2.equals("ADDRESS.TITLE")) return "";
			if (key2.equals("ADDRESS.NAME")) return getDataFromAddressField(addressField,"name");
			if (key2.equals("ADDRESS.FIRSTNAME")) return getDataFromAddressField(addressField,"firstname");
			if (key2.equals("ADDRESS.LASTNAME")) return getDataFromAddressField(addressField,"lastname");
			if (key2.equals("ADDRESS.COMPANY")) return getDataFromAddressField(addressField,"company");
			if (key2.equals("ADDRESS.STREET")) return getDataFromAddressField(addressField,"street");
			if (key2.equals("ADDRESS.STREETNAME")) return getDataFromAddressField(addressField,"streetname");
			if (key2.equals("ADDRESS.STREETNO")) return getDataFromAddressField(addressField,"streetno");
			if (key2.equals("ADDRESS.ZIP")) return getDataFromAddressField(addressField,"zip");
			if (key2.equals("ADDRESS.CITY")) return getDataFromAddressField(addressField,"city");
			if (key2.equals("ADDRESS.COUNTRY")) return getDataFromAddressField(addressField,"country");
			if (key2.equals("ADDRESS.COUNTRY.CODE2")) {
				return CountryCodes.getCountryCodeByCountry(getDataFromAddressField(addressField,"country"), "2");
			}
			if (key2.equals("ADDRESS.COUNTRY.CODE3")) {
				return CountryCodes.getCountryCodeByCountry(getDataFromAddressField(addressField,"country"), "3");
			}

			if (key2.equals("ADDRESS.GREETING")) return DataSetContact.getCommonGreeting();

			if (key.equals("ADDRESS.BANK.ACCOUNT.HOLDER")) return "";
			if (key.equals("ADDRESS.BANK.ACCOUNT")) return "";
			if (key.equals("ADDRESS.BANK.CODE")) return "";
			if (key.equals("ADDRESS.BANK.NAME")) return "";
			if (key.equals("ADDRESS.BANK.IBAN")) return "";
			if (key.equals("ADDRESS.BANK.BIC")) return "";
			if (key.equals("ADDRESS.NR")) return "";
			if (key.equals("ADDRESS.PHONE")) return "";
			if (key.equals("ADDRESS.PHONE.PRE")) return "";
			if (key.equals("ADDRESS.PHONE.POST")) return "";
			if (key.equals("ADDRESS.FAX")) return "";
			if (key.equals("ADDRESS.FAX.PRE")) return "";
			if (key.equals("ADDRESS.FAX.POST")) return "";
			if (key.equals("ADDRESS.MOBILE")) return "";
			if (key.equals("ADDRESS.MOBILE.PRE")) return "";
			if (key.equals("ADDRESS.MOBILE.POST")) return "";
			if (key.equals("ADDRESS.EMAIL")) return "";
			if (key.equals("ADDRESS.WEBSITE")) return "";
			if (key.equals("ADDRESS.VATNR")) return "";
			if (key.equals("ADDRESS.NOTE")) return "";
			if (key.equals("ADDRESS.DISCOUNT")) return "";
		}

		return null;
	}

	/**
     * @param document
     * @param percent
     * @return
     */
    public static String createPaymentText(DataSetDocument document, double percent) {
	    String paymenttext = document.getStringValueByKey("paymenttext");
	    paymenttext = paymenttext.replace("<PAID.VALUE>", DataUtils.DoubleToFormatedPriceRound(document.getDoubleValueByKey("payvalue")));
	    paymenttext = paymenttext.replace("<PAID.DATE>", document.getFormatedStringValueByKey("paydate"));
	    paymenttext = paymenttext.replace("<DUE.DAYS>", Integer.toString(document.getIntValueByKey("duedays")));
	    paymenttext = paymenttext.replace("<DUE.DATE>", DataUtils.DateAsLocalString(DataUtils.AddToDate(document.getStringValueByKey("date"), document.getIntValueByKey("duedays"))));
	    
	    paymenttext = paymenttext.replace("<DUE.DISCOUNT.PERCENT>", DataUtils.DoubleToFormatedPercent(document.getDoubleValueByKeyFromOtherTable("paymentid.PAYMENTS:discountvalue")));
	    paymenttext = paymenttext.replace("<DUE.DISCOUNT.DAYS>", document.getStringValueByKeyFromOtherTable("paymentid.PAYMENTS:discountdays"));
	    paymenttext = paymenttext.replace("<DUE.DISCOUNT.VALUE>", DataUtils.DoubleToFormatedPriceRound(document.getSummary().getTotalGross().asDouble() * (1 - percent)));
	    paymenttext = paymenttext.replace("<DUE.DISCOUNT.DATE>", getDiscountDueDate(document));
	    
	    // 2011-06-24 sbauer@eumedio.de
	    // New placeholder for bank
	    paymenttext = paymenttext.replace("<BANK.ACCOUNT.HOLDER>", 
	    		Activator.getDefault().getPreferenceStore().getString("BANK_ACCOUNT_HOLDER"));
	    paymenttext = paymenttext.replace("<BANK.ACCOUNT>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANKACCOUNTNR"));
	    paymenttext = paymenttext.replace("<BANK.IBAN>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_IBAN"));
	    paymenttext = paymenttext.replace("<BANK.BIC>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BIC"));
	    paymenttext = paymenttext.replace("<BANK.NAME>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANK"));
	    paymenttext = paymenttext.replace("<BANK.CODE>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANKCODE"));
	    paymenttext = paymenttext.replace("<YOURCOMPANY.CREDITORID>", 
	    		Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_CREDITORID"));
	    
	    // 2011-06-24 sbauer@eumedio.de
	    // Additional placeholder for censored bank account
	    String censoredAccount = censorAccountNumber(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_BANKACCOUNTNR"));
	    paymenttext = paymenttext.replace("<BANK.ACCOUNT.CENSORED>", censoredAccount);
	    censoredAccount = censorAccountNumber(Activator.getDefault().getPreferenceStore().getString("YOURCOMPANY_COMPANY_IBAN"));
	    paymenttext = paymenttext.replace("<BANK.IBAN.CENSORED>", censoredAccount);
	    
	    // debitor's bank account
	    paymenttext = paymenttext.replace("<DEBITOR.BANK.ACCOUNT.HOLDER>", 
	    		document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:account_holder"));
	    paymenttext = paymenttext.replace("<DEBITOR.BANK.IBAN>", 
	    		document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:iban"));
	    paymenttext = paymenttext.replace("<DEBITOR.BANK.BIC>", 
	    		document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:bic"));
	    paymenttext = paymenttext.replace("<DEBITOR.BANK.NAME>", 
	    		document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:bank_name"));
	    paymenttext = paymenttext.replace("<DEBITOR.MANDATREF>", 
	    		document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:mandat_ref"));
	    // Additional placeholder for censored bank account
	    censoredAccount = censorAccountNumber(document.getStringValueByKeyFromOtherTable("addressid.CONTACTS:iban"));
	    paymenttext = paymenttext.replace("<DEBITOR.BANK.IBAN.CENSORED>", censoredAccount);
	    
	    // 2011-06-24 sbauer@eumedio.de
	    // New placeholder for total sum
	    paymenttext = paymenttext.replace("<DOCUMENT.TOTAL>", document.getSummary().getTotalGross().asFormatedString());
	    return paymenttext;
    }

	/**
	 * @param paymenttext
	 * @param bankAccountLength
	 * @return
	 */
	private static String censorAccountNumber(String accountNumber) {
		String retval = "";
		Integer bankAccountLength = accountNumber.length();			
		// Only set placeholder if bank account exists
		if( bankAccountLength > 0 ) {				
			// Show only the last 3 digits
			Integer bankAccountCensoredLength = bankAccountLength - 3;
			String censoredDigits = "";				
			for( int i = 1; i <= bankAccountCensoredLength; i++ ) {
				censoredDigits += "*";
			}				
			retval = censoredDigits + accountNumber.substring( bankAccountCensoredLength );
		}
		return retval;
	}

	/**
	 * @param document
	 * @return
	 */
	private static String getDiscountDueDate(DataSetDocument document) {
		Calendar calendar = DataUtils.getCalendarFromDateString(document.getStringValueByKey("date"));
		calendar.add(Calendar.DATE, document.getDoubleValueByKeyFromOtherTable("paymentid.PAYMENTS:discountdays").intValue());
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * Getter for all placeholders
	 * @return
	 * 	String array with all placeholders
	 */
	public static String[] getPlaceholders() {
		return placeholders;
	}
	
	/**
	 * Test, if the name is in the list of all placeholders
	 * 
	 * @param testPlaceholder
	 * 		The placeholder to test
	 * @return
	 * 		TRUE, if the placeholder is in the list
	 */
	public static boolean isPlaceholder (String testPlaceholder) {
		
		String placeholderName = extractPlaceholderName(testPlaceholder);
		
		// Test all placeholders
		for (String placeholder : placeholders) {
			if (placeholderName.equals(placeholder))
				return true;
		}
		return false;
	}
}

