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

package com.sebulli.fakturama.calculate;

import com.sebulli.fakturama.misc.DataUtils;

/**
 * Class PriceValue Stores a value as double and provides methods to get the
 * value formated or rounded
 * 
 * @author Gerd Bartelt
 */
public class PriceValue {
	private Double value;

	/**
	 * Constructor Creates a PriceValue from an other PriceValue
	 * 
	 * @param value
	 *            Other PriceValue
	 */
	public PriceValue(PriceValue value) {
		this.value = value.asDouble();
	}

	/**
	 * Creates a PriceValue from a double value
	 * 
	 * @param value
	 *            Value as double
	 */
	public PriceValue(Double value) {
		this.value = value;
	}

	/**
	 * Sets the PriceValue to an double
	 * 
	 * @param d
	 *            New double value
	 */
	public void set(Double d) {
		this.value = d;
	}

	/**
	 * Adds a double to the PriceValue
	 * 
	 * @param d
	 *            Double to add
	 */
	public void add(Double d) {
		this.value += d;
	}

	/**
	 * Get the PriceValue as Double
	 * 
	 * @return Value as Double
	 */
	public Double asDouble() {
		return value;
	}

	/**
	 * Get the PriceValue as rounded Double
	 * 
	 * @return Roundes value as Double
	 */
	public Double asRoundedDouble() {
		return DataUtils.round(value);
	}

	/**
	 * Get the PriceValue as formated String
	 * 
	 * @return PriceValue as formated currency string
	 */
	public String asFormatedString() {
		return DataUtils.DoubleToFormatedPrice(value);
	}

	/**
	 * Get the PriceValue as formated and rounded String
	 * 
	 * @return PriceValue as formated and rounded currency string
	 */
	public String asFormatedRoundedString() {
		return DataUtils.DoubleToFormatedPriceRound(value);
	}

	/**
	 * Round the value
	 */
	public void round() {
		value = DataUtils.round(value);
	}

}
