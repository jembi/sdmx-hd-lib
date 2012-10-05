/*******************************************************************************
 * Copyright (c) 2010 Ryan Crichton.
 * 
 * This file is part of Jembi SDMX-HD Library.
 * 
 * Jembi SDMX-HD Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Jembi SDMX-HD Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jembi SDMX-HD Library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.jembi.sdmxhd.primitives;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represent a localised string. It allows various localised version
 * of a string to be stored.
 * 
 * @author Ryan Crichton
 */
public class LocalizedString {

	private String defaultStr = null;
	private Map<String, String> localizedStrings = new HashMap<String, String>();

	/**
	 * Adds a localisation for this LocalisedString object.
	 * 
	 * @param langCode
	 *            The language code for this value
	 * @param name
	 *            The value to be stored
	 */
	public void addValue(String langCode, String name) {
		localizedStrings.put(langCode, name);
	}

	/**
	 * Gets the value stored in this localised string according to the provided
	 * language code
	 * 
	 * @param langCode
	 *            The language of the string to be returned
	 * @return The localised value
	 */
	public String getValue(String langCode) {
		return localizedStrings.get(langCode);
	}

	@Override
	public boolean equals(Object ls) {
		if (ls == this) {
			return true;
		}
		if (!(ls instanceof LocalizedString) || ls == null) {
			return false;
		}
		LocalizedString other = (LocalizedString) ls;
		for (String key : this.localizedStrings.keySet()) {
			if (!this.getValue(key).equals(other.getValue(key))) {
				return false;
			}
		}
		return true;
	}

	/* getters and setters */

	public Map<String, String> getLocalizedStrings() {
		return localizedStrings;
	}

	public void setLocalizedStrings(Map<String, String> localizedStrings) {
		this.localizedStrings = localizedStrings;
	}

	public String getDefaultStr() {
		if (defaultStr != null) {
			return defaultStr;
		} else {
			if (localizedStrings.size() < 1) {
				return null;
			} else if (localizedStrings.keySet().contains("en")) {
				return localizedStrings.get("en");
			} else {
				return localizedStrings.get(localizedStrings.keySet()
						.iterator().next());
			}
		}
	}

	public void setDefaultStr(String defaultStr) {
		this.defaultStr = defaultStr;
	}

}
