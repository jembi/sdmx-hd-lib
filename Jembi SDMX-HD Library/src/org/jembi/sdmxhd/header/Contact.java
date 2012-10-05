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
import org.jembi.sdmxhd.util.annotations.XMLElement;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a Contact in a SDMX-HD Header
 */
public class Contact implements XMLWritable {
	/*
	 * note: general convention of field starting with a lower case name has
	 * been broken this is due to the fact that the name of primitive fields is
	 * used to to write the element to XML
	 */
	@XMLElement(name = "Name", namespace = Constants.DEFAULT_NAMESPACE)
	private String name;

	@XMLElement(name = "Department", namespace = Constants.DEFAULT_NAMESPACE)
	private String department;

	@XMLElement(name = "Role", namespace = Constants.DEFAULT_NAMESPACE)
	private String role;

	@XMLElement(name = "Telephone", namespace = Constants.DEFAULT_NAMESPACE)
	private String telephone;

	@XMLElement(name = "Fax", namespace = Constants.DEFAULT_NAMESPACE)
	private String fax;

	@XMLElement(name = "X400", namespace = Constants.DEFAULT_NAMESPACE)
	private String x400;

	@XMLElement(name = "URI", namespace = Constants.DEFAULT_NAMESPACE)
	private String uri;

	@XMLElement(name = "Email", namespace = Constants.DEFAULT_NAMESPACE)
	private String email;

	/**
	 * Converts this Contact to its SDMX-HD XML form and write the XML to the
	 * supplied xmlWriter
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getX400() {
		return x400;
	}

	public void setX400(String x400) {
		this.x400 = x400;
	}

	public String getURI() {
		return uri;
	}

	public void setURI(String uri) {
		this.uri = uri;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
