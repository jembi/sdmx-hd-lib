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

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * @author Ryan Crichton This object represents a SDMX-HD Concept
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class Concept implements XMLWritable {

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
	@XMLAttribute("coreRepresentation")
	private String coreRepresentation;
	@XMLAttribute("coreRepresentationAgency")
	private String coreRepresentationAgency;
	@XMLAttribute("parent")
	private String parent;
	@XMLAttribute("parentAgency")
	private String parentAgency;

	@XMLElement(name = "Name", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString name = new LocalizedString();

	@XMLElement(name = "Description", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString description = new LocalizedString();

	@XMLElement(name = "TextFormat", namespace = Constants.STRUCTURE_NAMESPACE)
	private TextFormat textFormat;

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/* getter and setter */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCoreRepresentation() {
		return coreRepresentation;
	}

	public void setCoreRepresentation(String coreRepresentation) {
		this.coreRepresentation = coreRepresentation;
	}

	public String getCoreRepresentationAgency() {
		return coreRepresentationAgency;
	}

	public void setCoreRepresentationAgency(String coreRepresentationAgency) {
		this.coreRepresentationAgency = coreRepresentationAgency;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentAgency() {
		return parentAgency;
	}

	public void setParentAgency(String parentAgency) {
		this.parentAgency = parentAgency;
	}

	public TextFormat getTextFormat() {
		return textFormat;
	}

	public void setTextFormat(TextFormat textFormat) {
		this.textFormat = textFormat;
	}

}
