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

package org.jembi.sdmxhd.dsd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a Hierarchy element in a SDMX-HD DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class Hierarchy implements XMLWritable {

	@XMLAttribute("id")
	private String id;
	@XMLAttribute("urn")
	private String urn;
	@XMLAttribute("version")
	private String version;
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

	@XMLElement
	private List<CodeRef> codeRefs = new ArrayList<CodeRef>();

	// TODO LevelType

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	public CodeRef findCodeRef(String codelistAliasRef, String codeValue) {
		for (CodeRef codeRef : codeRefs) {
			CodeRef foundCodeRef = codeRef.findCodeRef(codelistAliasRef,
					codeValue);
			if (foundCodeRef != null) {
				return foundCodeRef;
			}
		}
		return null;
	}

	/* getters and setter */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CodeRef> getCodeRefs() {
		return codeRefs;
	}

	public void setCodeRefs(List<CodeRef> codeRefs) {
		this.codeRefs = codeRefs;
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

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
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
