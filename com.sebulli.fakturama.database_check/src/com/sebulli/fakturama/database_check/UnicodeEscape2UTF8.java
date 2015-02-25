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
 * UTF8 Escape functionality. Based on this example:
 * http://www.xinotes.net/notes/note/813/
 * 
 * @author Gerd Bartelt
 *
 */
public class UnicodeEscape2UTF8 {
    
	static enum ParseState {
	NORMAL,
	ESCAPE,
	UNICODE_ESCAPE
    }

    // convert unicode escapes back to char
    public static String convertUnicodeEscape(String s) {
	char[] out = new char[s.length()];

	ParseState state = ParseState.NORMAL;
	int j = 0, k = 0, unicode = 0;
	char c = ' ';
	for (int i = 0; i < s.length(); i++) {
	    c = s.charAt(i);
	    if (state == ParseState.ESCAPE) {
		if (c == 'u') {
		    state = ParseState.UNICODE_ESCAPE;
		    unicode = 0;
		}
		else { // we don't care about other escapes
		    out[j++] = '\\';
		    out[j++] = c;
		    state = ParseState.NORMAL;
		}
	    }
	    else if (state == ParseState.UNICODE_ESCAPE) {
		if ((c >= '0') && (c <= '9')) {
		    unicode = (unicode << 4) + c - '0';
		}
		else if ((c >= 'a') && (c <= 'f')) {
		    unicode = (unicode << 4) + 10 + c - 'a';
		}
		else if ((c >= 'A') && (c <= 'F')) {
		    unicode = (unicode << 4) + 10 + c - 'A';
		}
		else {
			Logger.getInstance().logError("Malformed unicode escape");
		}
		k++;

		if (k == 4) {
		    out[j++] = (char) unicode;
		    k = 0;
		    state = ParseState.NORMAL;
		}
	    }
	    else if (c == '\\') {
		state = ParseState.ESCAPE;
	    }
	    else {
		out[j++] = c;
	    }
	}

	if (state == ParseState.ESCAPE) {
	    out[j++] = c;
	}

	return new String(out, 0, j);
    }
}
