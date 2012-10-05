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
package org.jembi.sdmxhd.header;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a Reciever in a SDMX-HD Header
 */
public class Receiver implements XMLWritable {

	@XMLAttribute("id")
	private String id;

	/*
	 * note: general convention of field starting with a lower case name has
	 * been broken this is due to the fact that the name of primitive fields is
	 * used to to write the element to XML
	 */
	@XMLElement(name = "Name", namespace = Constants.DEFAULT_NAMESPACE)
	private String name;

	@XMLElement(name = "Contact", namespace = Constants.DEFAULT_NAMESPACE)
	private Contact contact;

	/**
	 * Converts this Receiver to its SDMX-HD XML form and writes it to the
	 * specified xmlWriter
	 * 
	 * @param xmlWriter
	 * @throws IllegalArgumentException
	 * @throws XMLStreamException
	 * @throws IllegalAccessException
	 */
	public void toXML(XMLStreamWriter xmlWriter)
			throws IllegalArgumentException, XMLStreamException,
			IllegalAccessException {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/* getters and setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
