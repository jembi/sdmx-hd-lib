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

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.header.Contact;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class Organisation implements XMLWritable {

	@XMLAttribute("id")
	private String id;
	@XMLAttribute("version")
	private String version;
	@XMLAttribute("uri")
	private String uri;
	@XMLAttribute("urn")
	private String urn;
	@XMLAttribute("isExternalReference")
	private String isExternalReference;
	@XMLAttribute("parentOrganisation")
	private String parentOrganisation;
	@XMLAttribute("validFrom")
	private String validFrom;
	@XMLAttribute("validTo")
	private String validTo;

	@XMLElement(name = "Name", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString name = new LocalizedString();

	@XMLElement(name = "Description", namespace = Constants.STRUCTURE_NAMESPACE)
	private LocalizedString description = new LocalizedString();

	@XMLElement(name = "MaintenanceContact", namespace = Constants.STRUCTURE_NAMESPACE)
	private Contact maintenanceContact;

	@XMLElement(name = "CollectorContact", namespace = Constants.STRUCTURE_NAMESPACE)
	private Contact collectorContact;

	@XMLElement(name = "DisseminatorContact", namespace = Constants.STRUCTURE_NAMESPACE)
	private Contact disseminatorContact;

	@XMLElement(name = "ReporterContact", namespace = Constants.STRUCTURE_NAMESPACE)
	private Contact reporterContact;

	@XMLElement(name = "OtherContact", namespace = Constants.STRUCTURE_NAMESPACE)
	private Contact otherContact;

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getParentOrganisation() {
		return parentOrganisation;
	}

	public void setParentOrganisation(String parentOrganisation) {
		this.parentOrganisation = parentOrganisation;
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

	public Contact getMaintenanceContact() {
		return maintenanceContact;
	}

	public void setMaintenanceContact(Contact maintenanceContact) {
		this.maintenanceContact = maintenanceContact;
	}

	public Contact getCollectorContact() {
		return collectorContact;
	}

	public void setCollectorContact(Contact collectorContact) {
		this.collectorContact = collectorContact;
	}

	public Contact getDisseminatorContact() {
		return disseminatorContact;
	}

	public void setDisseminatorContact(Contact disseminatorContact) {
		this.disseminatorContact = disseminatorContact;
	}

	public Contact getReporterContact() {
		return reporterContact;
	}

	public void setReporterContact(Contact reporterContact) {
		this.reporterContact = reporterContact;
	}

	public Contact getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(Contact otherContact) {
		this.otherContact = otherContact;
	}

}
