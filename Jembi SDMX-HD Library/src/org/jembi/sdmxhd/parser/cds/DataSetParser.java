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
package org.jembi.sdmxhd.parser.cds;

import java.util.Iterator;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.cds.DataSet;
import org.jembi.sdmxhd.cds.Obs;
import org.jembi.sdmxhd.cds.Series;

public class DataSetParser {

	private ZipFile zipFile = null;

	public DataSet parse(XMLEventReader eventReader, StartElement se,
			ZipFile zipFile, String derivedNamespace) throws XMLStreamException {
		// set zip file
		this.zipFile = zipFile;

		DataSet d = new DataSet();

		// read all dynamic attributes
		Iterator<Attribute> attributes = se.getAttributes();
		while (attributes.hasNext()) {
			Attribute attr = attributes.next();
			d.addAttribute(attr.getName().getLocalPart(), attr.getValue());
		}

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("keyFamilyURI"));
		if (a != null) {
			d.setKeyFamilyURI(a.getValue());
		}
		a = se.getAttributeByName(new QName("datasetID"));
		if (a != null) {
			d.setDatasetID(a.getValue());
		}
		a = se.getAttributeByName(new QName("dataProviderSchemeAgencyId"));
		if (a != null) {
			d.setDataProviderSchemeAgencyId(a.getValue());
		}
		a = se.getAttributeByName(new QName("dataProviderSchemeId"));
		if (a != null) {
			d.setDataProviderSchemeId(a.getValue());
		}
		a = se.getAttributeByName(new QName("dataProviderID"));
		if (a != null) {
			d.setDataProviderID(a.getValue());
		}
		a = se.getAttributeByName(new QName("dataflowAgencyID"));
		if (a != null) {
			d.setDataflowAgencyID(a.getValue());
		}
		a = se.getAttributeByName(new QName("dataflowID"));
		if (a != null) {
			d.setDataflowID(a.getValue());
		}
		a = se.getAttributeByName(new QName("action"));
		if (a != null) {
			d.setAction(a.getValue());
		}
		a = se.getAttributeByName(new QName("reportingBeginDate"));
		if (a != null) {
			d.setReportingBeginDate(a.getValue());
		}
		a = se.getAttributeByName(new QName("reportingEndDate"));
		if (a != null) {
			d.setReportingEndDate(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFromDate"));
		if (a != null) {
			d.setValidFromDate(a.getValue());
		}
		a = se.getAttributeByName(new QName("validToDate"));
		if (a != null) {
			d.setValidToDate(a.getValue());
		}
		a = se.getAttributeByName(new QName("publicationYear"));
		if (a != null) {
			d.setPublicationYear(a.getValue());
		}
		a = se.getAttributeByName(new QName("publicationPeriod"));
		if (a != null) {
			d.setPublicationPeriod(a.getValue());
		}

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Series")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								derivedNamespace)) {
					// read Series
					Series s = processSeries(se, eventReader, derivedNamespace);
					d.getSeries().add(s);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("DataSet")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								derivedNamespace)) {
					endTagReached = true;
				}
			}
		}

		return d;
	}

	private Series processSeries(StartElement se, XMLEventReader eventReader,
			String derivedNamespace) throws XMLStreamException {
		Series s = new Series();

		// read all dynamic attributes
		Iterator<Attribute> attributes = se.getAttributes();
		while (attributes.hasNext()) {
			Attribute attr = attributes.next();
			s.addAttribute(attr.getName().getLocalPart(), attr.getValue());
		}

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Obs")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								derivedNamespace)) {
					// read Series
					Obs o = processObs(se, eventReader);
					s.getObs().add(o);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Series")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								derivedNamespace)) {
					endTagReached = true;
				}
			}
		}

		return s;
	}

	private Obs processObs(StartElement se, XMLEventReader eventReader) {
		Obs o = new Obs();

		// read all dynamic attributes
		Iterator<Attribute> attributes = se.getAttributes();
		while (attributes.hasNext()) {
			Attribute attr = attributes.next();
			o.addAttribute(attr.getName().getLocalPart(), attr.getValue());
		}

		return o;
	}

}
