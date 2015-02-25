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

package com.sebulli.fakturama.editors;

import java.util.ArrayList;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Gerd Bartelt
 *
 */
public class Suggestion implements VerifyListener{
	
	private CCombo combo = null;
	private Text text = null;
	private boolean textCorrected = false;

	// The list to search for
	String[] suggestions = null; 

	/**
 	 * Constructor for combo boxes
 	 * 
	 * @param combo
	 * 			The combo box
	 * @param suggestions
	 * 			List with all the possible strings
	 */
	public Suggestion (CCombo combo, String[] suggestions) {
		this.combo = combo;
		this.suggestions = suggestions;
	}

	/**
 	 * Constructor for text fields
 	 * 
	 * @param text
	 * 			The text field
	 * @param suggestions
	 * 			List with all the possible strings
	 */
	public Suggestion (Text text, String[] suggestions) {
		this.text = text;
		this.suggestions = suggestions;
	}

	
	/**
	 * Search for the "base" string in the list and get those part of the string
	 * that was found in the list. If there are more than one entry that starts
	 * with the same sequence, return the sequence, that is equal in all strings
	 * of the list.
	 * 
	 * @param base
	 *            String to search for
	 * @return Result string
	 */
	public String getSuggestion(String base) {

		// Do not work with empty strings
		if (base.isEmpty())
			return "";

		// Temporary list with all strings that start with the base string
		ArrayList<String> resultStrings = new ArrayList<String>();

		// Get all strings that start with the base string
		// and copy them to the temporary list
		for (int i = 0; i < suggestions.length; i++) {
			if (suggestions[i].toLowerCase().startsWith(base.toLowerCase()))
				resultStrings.add(suggestions[i]);
		}

		// No string matches: return with an empty string
		if (resultStrings.isEmpty())
			return "";

		// There was at least one string found in the list.
		// Start with this entry.
		String tempResult = resultStrings.get(0);
		String result = "";

		// Get that part of the all the strings, that is equal
		for (String resultString : resultStrings) {

			// To compare two strings character by character, the minimum
			// length of both must be used for the loop
			int length = tempResult.length();
			if (resultString.length() < length)
				length = resultString.length();

			// Compare both strings, and get the part, that is equal
			for (int i = 0; i < length; i++) {
				if (tempResult.substring(0, i + 1).equalsIgnoreCase(resultString.substring(0, i + 1)))
					result = tempResult.substring(0, i + 1);

			}

			// Use the result to compare it with the next entry
			tempResult = result;
		}

		// Return the result
		return result;
	}

	@Override
	public void verifyText(VerifyEvent e) {

		// Delete or backslash will end the suggestion mode
		if ((e.keyCode == 8) || (e.keyCode == 127))
			textCorrected = true;

		// Do it only, if the new text is not empty.
		// This must be done to prevent an event loop.
		if (!e.text.isEmpty() && !textCorrected) {

			String s = "";
			
			// The complete text is the old one of the combo and
			// the new sequence from the event.
			if (combo != null)
				s = combo.getText() + e.text;

			// The complete text is the old one of the text field and
			// the new sequence from the event.
			if (text != null)
				s = text.getText() + e.text;

			// Get the suggestion ..
			String suggestion = getSuggestion(s);
			if (!suggestion.isEmpty()) {

				// .. and use it.
				if (combo != null)
					combo.setText("");
				if (text != null)
					text.setText("");
				
				e.text = suggestion;
			}
		}
		
	}

}
