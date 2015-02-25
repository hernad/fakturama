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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Browser editor input
 * 
 * @author Gerd Bartelt
 */
public class BrowserEditorInput implements IEditorInput {

	String url;
	String name = "";
	boolean isFakturamaProjectUrl;

	/**
	 * Constructor
	 * 
	 * @param url
	 *            URL of the web browser
	 */
	public BrowserEditorInput(String url, String name, boolean isFakturamaProjectUrl) {
		this.url = url;
		this.name = name;
		this.isFakturamaProjectUrl = isFakturamaProjectUrl;
	}

	/**
	 * Returns whether the editor input exists
	 * 
	 * @return null
	 */
	@Override
	public boolean exists() {
		return false;
	}

	/**
	 * The editors image descriptor
	 * 
	 * @return null: there is no image
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/**
	 * Returns the name of this editor input for display purposes
	 * 
	 * @return the URL "fakturama.sebulli.com"
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns an object that can be used to save the state of this editor
	 * input.
	 * 
	 * @return null
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * Returns the tool tip text for this editor input
	 * 
	 * @return The editors name
	 */
	@Override
	public String getToolTipText() {
		return this.getName();
	}

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object.
	 * 
	 * @return null: there is no such object
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *            the object to compare
	 * @return True, if it it equal to this object
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) { return true; }
		if (url == null)
			return false;

		// Compare the URLs
		if (obj instanceof BrowserEditorInput) { return url.equals(((BrowserEditorInput) obj).getUrl()); }
		return false;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return Hash code of the URL
	 */
	@Override
	public int hashCode() {
		return url.hashCode();
	}

	
	/**
	 * Returns the URL
	 * 
	 * @return URL as string
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Returns whether it is the project URL
	 * 
	 * @return TRUE, if it is the Fakturama project URL
	 */
	public boolean getIsFakturamaProjectUrl() {
		return this.isFakturamaProjectUrl;
	}
}
