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
 *         This object represents a HierarchicalCodelist element in a SDMS-HD
 *         DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class HierarchicalCodelist implements XMLWritable {

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

	@XMLElement
	private List<CodelistRef> codeListRefs = new ArrayList<CodelistRef>();
	@XMLElement
	private List<Hierarchy> hierarchys = new ArrayList<Hierarchy>();

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	public Hierarchy getHierarchy(String id) {
		for (Hierarchy h : hierarchys) {
			if (h.getId().equalsIgnoreCase(id)) {
				return h;
			}
		}
		return null;
	}

	public CodelistRef getCodeListRef(String aliasOrCodelistID) {
		for (CodelistRef codeListref : codeListRefs) {
			if (codeListref.getAlias().equalsIgnoreCase(aliasOrCodelistID)
					|| codeListref.getCodelistID().equalsIgnoreCase(
							aliasOrCodelistID)) {
				return codeListref;
			}
		}
		return null;
	}

	/* getter and setter */

	public List<Hierarchy> getHierarchys() {
		return hierarchys;
	}

	public void setHierarchys(List<Hierarchy> hierarchys) {
		this.hierarchys = hierarchys;
	}

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

	public List<CodelistRef> getCodeListRefs() {
		return codeListRefs;
	}

	public void setCodeListRefs(List<CodelistRef> codeListRefs) {
		this.codeListRefs = codeListRefs;
	}

	public String getCodeListAlias(String codelistID) {
		for (CodelistRef clr : getCodeListRefs()) {
			if (clr.getCodelistID().equalsIgnoreCase(codelistID)) {
				return clr.getAlias();
			}
		}
		return null;
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
