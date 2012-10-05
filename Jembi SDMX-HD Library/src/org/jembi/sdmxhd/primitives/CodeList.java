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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * This object represents a codelist structure, it stores localized name and
 * description of the codelist along with additional information that makes up
 * the codelist
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class CodeList implements XMLWritable {

	@XMLAttribute("id")
	private String id;
	@XMLAttribute("agencyID")
	private String agencyID;
	@XMLAttribute("version")
	private String version;
	@XMLAttribute("uri")
	private String uri;
	@XMLAttribute("urn")
	private String urn;
	@XMLAttribute("isExternalReference")
	private String isExternalReference;
	@XMLAttribute("isFinal")
	private String isFinal;
	@XMLAttribute("validFrom")
	private String validFrom;
	@XMLAttribute("validTo")
	private String validTo;

	@XMLElement(name = "Name", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString name = new LocalizedString();

	@XMLElement(name = "Description", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString description = new LocalizedString();

	@XMLElement(name = "Code", namespace = Constants.STRUCTURE_NAMESPACE)
	private Set<Code> codes = new HashSet<Code>();

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/**
	 * Find a code with a description that matches the supplied description.
	 * 
	 * @param description
	 *            the description for the required code
	 * @return the found code or null if not found
	 */
	public Code getCodeByDescription(String description) {
		for (Code code : codes) {
			Map<String, String> localizedStrings = code.getDescription()
					.getLocalizedStrings();
			for (String key : localizedStrings.keySet()) {
				if (localizedStrings.get(key).equalsIgnoreCase(description)) {
					return code;
				}
			}
		}
		return null;
	}

	/**
	 * Gets a code by it id
	 * 
	 * @param id
	 *            the code id
	 * @return the code with the specified id
	 */
	public Code getCodeByID(String id) {
		for (Code code : codes) {
			if (code.getValue().equalsIgnoreCase(id)) {
				return code;
			}
		}
		return null;
	}

	/* getters and setters */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Code> getCodes() {
		return codes;
	}

	public void setCodes(Set<Code> codes) {
		this.codes = codes;
	}

	public String getAgencyID() {
		return agencyID;
	}

	public void setAgencyID(String agencyID) {
		this.agencyID = agencyID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public LocalizedString getName() {
		return name;
	}

	public void setName(LocalizedString name) {
		this.name = name;
	}

	public LocalizedString getDescription() {
		return description;
	}

	public void setDescription(LocalizedString description) {
		this.description = description;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getIsExternalReference() {
		return isExternalReference;
	}

	public void setIsExternalReference(String isExternalReference) {
		this.isExternalReference = isExternalReference;
	}

	public String getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(String isFinal) {
		this.isFinal = isFinal;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

}
