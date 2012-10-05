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

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLElement;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a SDMX-HD Header
 */
public class Header implements XMLWritable {

	@XMLElement(name = "ID", namespace = Constants.DEFAULT_NAMESPACE)
	private String id;

	@XMLElement(name = "Test", namespace = Constants.DEFAULT_NAMESPACE)
	private boolean test;

	@XMLElement(name = "Truncated", namespace = Constants.DEFAULT_NAMESPACE)
	private boolean truncated;

	@XMLElement(name = "Name", namespace = Constants.DEFAULT_NAMESPACE)
	private LocalizedString name = new LocalizedString();

	@XMLElement(name = "Prepared", namespace = Constants.DEFAULT_NAMESPACE)
	private String prepared;

	@XMLElement(name = "Senders", namespace = Constants.DEFAULT_NAMESPACE)
	private List<Sender> senders = new ArrayList<Sender>();

	@XMLElement(name = "Receivers", namespace = Constants.DEFAULT_NAMESPACE)
	private List<Receiver> receivers = new ArrayList<Receiver>();

	@XMLElement(name = "KeyFamilyRef", namespace = Constants.DEFAULT_NAMESPACE)
	private String keyFamilyRef;

	@XMLElement(name = "KeyFamilyAgency", namespace = Constants.DEFAULT_NAMESPACE)
	private String keyFamilyAgency;

	@XMLElement(name = "DataSetAgency", namespace = Constants.DEFAULT_NAMESPACE)
	private String dataSetAgency;

	@XMLElement(name = "DataSetID", namespace = Constants.DEFAULT_NAMESPACE)
	private String dataSetID;

	@XMLElement(name = "DataSetAction", namespace = Constants.DEFAULT_NAMESPACE)
	private String dataSetAction;

	@XMLElement(name = "Extracted", namespace = Constants.DEFAULT_NAMESPACE)
	private String extracted;

	@XMLElement(name = "ReportingBegin", namespace = Constants.DEFAULT_NAMESPACE)
	private String reportingBegin;

	@XMLElement(name = "ReportingEnd", namespace = Constants.DEFAULT_NAMESPACE)
	private String reportingEnd;

	@XMLElement(name = "Source", namespace = Constants.DEFAULT_NAMESPACE)
	private String source;

	/**
	 * Converts this Header to its SDMX-HD XML form and writes it to the
	 * specified xmlWriter.
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getPrepared() {
		return prepared;
	}

	public void setPrepared(String prepared) {
		this.prepared = prepared;
	}

	public String getKeyFamilyRef() {
		return keyFamilyRef;
	}

	public void setKeyFamilyRef(String keyFamilyRef) {
		this.keyFamilyRef = keyFamilyRef;
	}

	public String getKeyFamilyAgency() {
		return keyFamilyAgency;
	}

	public void setKeyFamilyAgency(String keyFamilyAgency) {
		this.keyFamilyAgency = keyFamilyAgency;
	}

	public String getDataSetAgency() {
		return dataSetAgency;
	}

	public void setDataSetAgency(String dataSetAgency) {
		this.dataSetAgency = dataSetAgency;
	}

	public String getDataSetID() {
		return dataSetID;
	}

	public void setDataSetID(String dataSetID) {
		this.dataSetID = dataSetID;
	}

	public String getDataSetAction() {
		return dataSetAction;
	}

	public void setDataSetAction(String dataSetAction) {
		this.dataSetAction = dataSetAction;
	}

	public String getExtracted() {
		return extracted;
	}

	public void setExtracted(String extracted) {
		this.extracted = extracted;
	}

	public String getReportingBegin() {
		return reportingBegin;
	}

	public void setReportingBegin(String reportingBegin) {
		this.reportingBegin = reportingBegin;
	}

	public String getReportingEnd() {
		return reportingEnd;
	}

	public void setReportingEnd(String reportingEnd) {
		this.reportingEnd = reportingEnd;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Sender> getSenders() {
		return senders;
	}

	public void setSenders(List<Sender> senders) {
		this.senders = senders;
	}

	public List<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}

	public LocalizedString getName() {
		return name;
	}

	public void setName(LocalizedString name) {
		this.name = name;
	}

}
