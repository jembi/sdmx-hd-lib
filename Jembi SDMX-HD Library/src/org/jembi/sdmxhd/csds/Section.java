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
package org.jembi.sdmxhd.csds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;

/**
 * @author Ryan Crichton
 * 
 *         This Object represents a Section in a CDS
 */
public class Section implements XMLWritable {

	public String namespace = "";

	// The logical attributes for this Section, these will be written as xml
	// attributes when the Section is written to it SDMX-HD XML form
	@XMLAttribute
	private Map<String, String> attributes = new HashMap<String, String>();

	@XMLElement
	private List<Obs> obs = new ArrayList<Obs>();

	/**
	 * Adds a logical attribute to this Section element.
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void addAttribute(String attributeName, String value) {
		attributes.put(attributeName, value);
	}

	/**
	 * Gets the specified attribute
	 * 
	 * @param attribute
	 * @return specified attribute
	 */
	public String getAttributeValue(String attribute) {
		return attributes.get(attribute);
	}

	/**
	 * Converts this Section object to it SDMX-HD XML form.
	 * 
	 * @param xmlWriter
	 * @throws XMLStreamException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void toXML(XMLStreamWriter xmlWriter) throws XMLStreamException,
			IllegalArgumentException, IllegalAccessException {
		XMLBuilder.writeBeanToXML(this, xmlWriter, namespace);
	}

	/* getters and setters */

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<Obs> getObs() {
		return obs;
	}

	public void setObs(List<Obs> obs) {
		this.obs = obs;
	}

}
