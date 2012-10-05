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

package org.jembi.sdmxhd.parser.dsd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.parser.TextFormatParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.primitives.Concept;
import org.jembi.sdmxhd.primitives.ConceptScheme;
import org.jembi.sdmxhd.primitives.TextFormat;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows Concepts element in a SDMX-HD DSD to be parsed into
 *         ConceptScheme objects
 */
public class ConceptsParser {

	private ZipFile zipFile = null;

	/**
	 * Parses Concepts element in a SDMX-HD DSD into ConceptScheme objects
	 * 
	 * @param eventReader
	 * @param startElement
	 * @param zipFile
	 * @return a list of ConceptSchemes
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 */
	public List<ConceptScheme> parse(XMLEventReader eventReader,
			StartElement startElement, ZipFile zipFile)
			throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		// set zip file
		this.zipFile = zipFile;

		List<ConceptScheme> conceptSchemes = new ArrayList<ConceptScheme>();
		Boolean inConceptsElement = true;
		while (inConceptsElement) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"ConceptScheme")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					ConceptScheme cs = processConceptScheme(se, eventReader);
					conceptSchemes.add(cs);
				}
				break;

			// stop looping when concepts end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"Concepts")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					inConceptsElement = false;
				}
				break;
			}
		}
		return conceptSchemes;
	}

	private ConceptScheme processConceptScheme(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {

		ConceptScheme cScheme = new ConceptScheme();
		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				cScheme.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				cScheme.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				cScheme.setId(id);

				// setup StAX to steam XML
				XMLInputFactory factory = XMLInputFactory.newInstance();
				ZipEntry entry = zipFile.getEntry(uri);
				InputStream dsdStream = zipFile.getInputStream(entry);

				eventReader = factory.createXMLEventReader(dsdStream);

				boolean positionReached = false;
				while (!positionReached) {
					XMLEvent event = eventReader.nextEvent();

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								"ConceptScheme")
								&& se.getName().getNamespaceURI()
										.equalsIgnoreCase(
												Constants.STRUCTURE_NAMESPACE)
								&& se.getAttributeByName(new QName("id"))
										.getValue().equalsIgnoreCase(id)) {
							positionReached = true;
						}
						break;
					}
				}
			} catch (Exception e) {
				throw new ExternalRefrenceNotFoundException(e.getMessage());
			}
		}

		// read concept scheme properties
		// mandatory
		cScheme.setId(se.getAttributeByName(new QName("id")).getValue());
		cScheme.setAgencyID(se.getAttributeByName(new QName("agencyID"))
				.getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			cScheme.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			cScheme.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			cScheme.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			cScheme.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			cScheme.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			cScheme.setValidTo(a.getValue());
		}

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Attribute langCode = null;
					while (se.getAttributes().hasNext()) {
						Attribute att = (Attribute) se.getAttributes().next();
						if (att.getName().getLocalPart().equalsIgnoreCase(
								"lang")) {
							langCode = att;
							break;
						}
					}
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					if (langCode != null) {
						cScheme.getName().addValue(langCode.getValue(), tmp);
					} else {
						cScheme.getName().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Description")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Attribute langCode = null;
					while (se.getAttributes().hasNext()) {
						Attribute att = (Attribute) se.getAttributes().next();
						if (att.getName().getLocalPart().equalsIgnoreCase(
								"lang")) {
							langCode = att;
							break;
						}
					}
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					if (langCode != null) {
						cScheme.getDescription().addValue(langCode.getValue(),
								tmp);
					} else {
						cScheme.getName().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Concept")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read concept
					Concept c = processConcept(se, eventReader);
					cScheme.getConcepts().add(c);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"ConceptScheme")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return cScheme;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param se
	 * @param eventReader
	 * @return
	 * @throws XMLStreamException
	 * @throws ExternalRefrenceNotFoundException
	 * @throws IOException
	 */
	private Concept processConcept(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException, ExternalRefrenceNotFoundException,
			IOException {
		Concept c = new Concept();

		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				c.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				c.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				c.setId(id);

				// setup StAX to steam XML
				XMLInputFactory factory = XMLInputFactory.newInstance();
				ZipEntry entry = zipFile.getEntry(uri);
				InputStream dsdStream = zipFile.getInputStream(entry);

				eventReader = factory.createXMLEventReader(dsdStream);

				boolean positionReached = false;
				while (!positionReached) {
					XMLEvent event = null;
					if (eventReader.hasNext()) {
						event = eventReader.nextEvent();
					} else {
						throw new ExternalRefrenceNotFoundException(
								"External Reference to Concept " + id
										+ " not found in this file");
					}

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								"Concept")
								&& se.getName().getNamespaceURI()
										.equalsIgnoreCase(
												Constants.STRUCTURE_NAMESPACE)
								&& se.getAttributeByName(new QName("id"))
										.getValue().equalsIgnoreCase(id)) {
							positionReached = true;
						}
						break;
					}
				}
			} catch (Exception e) {
				throw new ExternalRefrenceNotFoundException(e.getMessage());
			}
		}

		// mandatory attributes
		c.setId(se.getAttributeByName(new QName("id")).getValue());

		// optional attributes
		Attribute a = se.getAttributeByName(new QName("agencyID"));
		if (a != null) {
			c.setAgencyID(a.getValue());
		}
		a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			c.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			c.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			c.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("coreRepresentation"));
		if (a != null) {
			c.setCoreRepresentation(a.getValue());
		}
		a = se.getAttributeByName(new QName("coreRepresentationAgency"));
		if (a != null) {
			c.setCoreRepresentationAgency(a.getValue());
		}
		a = se.getAttributeByName(new QName("parent"));
		if (a != null) {
			c.setParent(a.getValue());
		}
		a = se.getAttributeByName(new QName("parentAgency"));
		if (a != null) {
			c.setParentAgency(a.getValue());
		}

		// Elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Attribute langCode = null;
					while (se.getAttributes().hasNext()) {
						Attribute att = (Attribute) se.getAttributes().next();
						if (att.getName().getLocalPart().equalsIgnoreCase(
								"lang")) {
							langCode = att;
							break;
						}
					}
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					if (langCode != null) {
						c.getName().addValue(langCode.getValue(), tmp);
					} else {
						c.getName().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Description")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Attribute langCode = null;
					while (se.getAttributes().hasNext()) {
						Attribute att = (Attribute) se.getAttributes().next();
						if (att.getName().getLocalPart().equalsIgnoreCase(
								"lang")) {
							langCode = att;
							break;
						}
					}
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					if (langCode != null) {
						c.getDescription().addValue(langCode.getValue(), tmp);
					} else {
						c.getDescription().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"TextFormat")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TextFormatParser tfp = new TextFormatParser();
					TextFormat textFormat = tfp.parse(eventReader, se, zipFile);
					c.setTextFormat(textFormat);
				}
				break;
			case XMLEvent.END_ELEMENT:
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Concept")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return c;
	}

}
