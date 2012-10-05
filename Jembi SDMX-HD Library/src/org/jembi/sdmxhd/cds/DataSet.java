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
package org.jembi.sdmxhd.cds;

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
 *         This object represents a DataSet element in a CDS
 */
public class DataSet implements XMLWritable {

	public String namespace = "";

	@XMLAttribute("keyFamilyURI")
	private String keyFamilyURI;
	@XMLAttribute("datasetID")
	private String datasetID;
	@XMLAttribute("dataProviderSchemeAgencyId")
	private String dataProviderSchemeAgencyId;
	@XMLAttribute("dataProviderSchemeId")
	private String dataProviderSchemeId;
	@XMLAttribute("dataProviderID")
	private String dataProviderID;
	@XMLAttribute("dataflowAgencyID")
	private String dataflowAgencyID;
	@XMLAttribute("dataflowID")
	private String dataflowID;
	@XMLAttribute("action")
	private String action;
	@XMLAttribute("reportingBeginDate")
	private String reportingBeginDate;
	@XMLAttribute("reportingEndDate")
	private String reportingEndDate;
	@XMLAttribute("validFromDate")
	private String validFromDate;
	@XMLAttribute("validToDate")
	private String validToDate;
	@XMLAttribute("publicationYear")
	private String publicationYear;
	@XMLAttribute("publicationPeriod")
	private String publicationPeriod;
	// The logical attributes for this DataSet, these will be written as xml
	// attributes when the DataSet is written to it SDMX-HD XML form
	@XMLAttribute
	private Map<String, String> attributes = new HashMap<String, String>();

	@XMLElement
	private List<Series> series = new ArrayList<Series>();

	/**
	 * Converts this object to it SDMX-HD XML form.
	 * 
	 * @param xmlWriter
	 *            the xmlWriter to write the XML to.
	 * @throws XMLStreamException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void toXML(XMLStreamWriter xmlWriter) throws XMLStreamException,
			IllegalArgumentException, IllegalAccessException {
		XMLBuilder.writeBeanToXML(this, xmlWriter, namespace);
	}

	public void addAttribute(String attributeName, String value) {
		attributes.put(attributeName, value);
	}

	/* getters and setters */

	public String getKeyFamilyURI() {
		return keyFamilyURI;
	}

	public void setKeyFamilyURI(String keyFamilyURI) {
		this.keyFamilyURI = keyFamilyURI;
	}

	public String getDatasetID() {
		return datasetID;
	}

	public void setDatasetID(String datasetID) {
		this.datasetID = datasetID;
	}

	public String getDataProviderSchemeAgencyId() {
		return dataProviderSchemeAgencyId;
	}

	public void setDataProviderSchemeAgencyId(String dataProviderSchemeAgencyId) {
		this.dataProviderSchemeAgencyId = dataProviderSchemeAgencyId;
	}

	public String getDataProviderSchemeId() {
		return dataProviderSchemeId;
	}

	public void setDataProviderSchemeId(String dataProviderSchemeId) {
		this.dataProviderSchemeId = dataProviderSchemeId;
	}

	public String getDataProviderID() {
		return dataProviderID;
	}

	public void setDataProviderID(String dataProviderID) {
		this.dataProviderID = dataProviderID;
	}

	public String getDataflowAgencyID() {
		return dataflowAgencyID;
	}

	public void setDataflowAgencyID(String dataflowAgencyID) {
		this.dataflowAgencyID = dataflowAgencyID;
	}

	public String getDataflowID() {
		return dataflowID;
	}

	public void setDataflowID(String dataflowID) {
		this.dataflowID = dataflowID;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReportingBeginDate() {
		return reportingBeginDate;
	}

	public void setReportingBeginDate(String reportingBeginDate) {
		this.reportingBeginDate = reportingBeginDate;
	}

	public String getReportingEndDate() {
		return reportingEndDate;
	}

	public void setReportingEndDate(String reportingEndDate) {
		this.reportingEndDate = reportingEndDate;
	}

	public String getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(String validFromDate) {
		this.validFromDate = validFromDate;
	}

	public String getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(String validToDate) {
		this.validToDate = validToDate;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getPublicationPeriod() {
		return publicationPeriod;
	}

	public void setPublicationPeriod(String publicationPeriod) {
		this.publicationPeriod = publicationPeriod;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
