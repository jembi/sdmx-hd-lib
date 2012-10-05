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
package org.jembi.sdmxhd.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Bob Jolliffe
 * 
 *         Utility class for resolving URLs
 */
public class URLHandler {

	private String baseURL;

	public URLHandler(String baseURL) {
		this.baseURL = baseURL;
	}

	public InputStream getInputStream(String urlString)
			throws MalformedURLException, java.io.IOException {

		// check if relative path URL ie. no protocol
		if (!urlString.contains(":")) {

			// remove the dot if it's there - all references in zipfile are
			// relative to "root"
			if (urlString.charAt(0) == '.') {
				urlString = urlString.substring(1);
			}
			urlString = baseURL + urlString;
		}

		URL url = new URL(urlString);
		return url.openStream();

	}
}
