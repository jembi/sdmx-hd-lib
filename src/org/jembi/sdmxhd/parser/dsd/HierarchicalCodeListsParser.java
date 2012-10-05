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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.dsd.CodeRef;
import org.jembi.sdmxhd.dsd.CodelistRef;
import org.jembi.sdmxhd.dsd.HierarchicalCodelist;
import org.jembi.sdmxhd.dsd.Hierarchy;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows HierarchicalCodeLists element in a SDMX-HD DSD to be parsed
 *         into HierarchicalCodeList objects
 */
public class HierarchicalCodeListsParser {

	private ZipFile zipFile = null;

	/**
	 * Parses HierarchicalCodeLists element in a SDMX-HD DSD into
	 * HierarchicalCodeList objects
	 * 
	 * @param eventReader
	 * @param zipFile
	 * @param startElement
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 */
	public List<HierarchicalCodelist> parse(XMLEventReader eventReader,
			StartElement startElement, ZipFile zipFile)
			throws XMLStreamException, ExternalRefrenceNotFoundException,
			IOException {
		this.zipFile = zipFile;

		List<HierarchicalCodelist> hierarchicalCodelists = new ArrayList<HierarchicalCodelist>();
		Boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelist")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					HierarchicalCodelist hcl = processHierarchicalCodelist(se,
							eventReader);
					hierarchicalCodelists.add(hcl);
				}
				break;

			// stop looping when concepts end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelists")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return hierarchicalCodelists;
	}

	private HierarchicalCodelist processHierarchicalCodelist(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException,
			ExternalRefrenceNotFoundException, IOException {
		HierarchicalCodelist hierarchicalCodelist = new HierarchicalCodelist();
		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				hierarchicalCodelist.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				hierarchicalCodelist.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				hierarchicalCodelist.setId(id);

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
								"External Reference to HierarchicalCodeList "
										+ id + " not found in this file");
					}

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								"HierarchicalCodelist")
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
		hierarchicalCodelist.setId(se.getAttributeByName(new QName("id"))
				.getValue());
		hierarchicalCodelist.setAgencyID(se.getAttributeByName(
				new QName("agencyID")).getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			hierarchicalCodelist.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			hierarchicalCodelist.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			hierarchicalCodelist.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			hierarchicalCodelist.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			hierarchicalCodelist.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			hierarchicalCodelist.setValidTo(a.getValue());
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
					Characters c = eventReader.nextEvent().asCharacters();
					if (langCode != null) {
						hierarchicalCodelist.getName().addValue(
								langCode.getValue(), c.getData());
					} else {
						hierarchicalCodelist.getName().setDefaultStr(
								c.getData());
					}
					eventReader.nextTag();
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
					Characters c = eventReader.nextEvent().asCharacters();
					if (langCode != null) {
						hierarchicalCodelist.getDescription().addValue(
								langCode.getValue(), c.getData());
					} else {
						hierarchicalCodelist.getDescription().setDefaultStr(
								c.getData());
					}
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodelistRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read concept
					CodelistRef clr = processCodeListRef(se, eventReader);
					hierarchicalCodelist.getCodeListRefs().add(clr);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Hierarchy")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read hierarchy
					Hierarchy h = processHierarchy(se, eventReader);
					hierarchicalCodelist.getHierarchys().add(h);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelist")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return hierarchicalCodelist;
	}

	private CodelistRef processCodeListRef(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		CodelistRef codeListRef = new CodelistRef();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("URN")) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeListRef.setUrn(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"AgencyID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeListRef.setAgencyID(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodelistID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeListRef.setCodelistID(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Version")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeListRef.setVersion(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart()
						.equalsIgnoreCase("Alias")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeListRef.setAlias(c.getData());
					eventReader.nextTag();
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("CodelistRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return codeListRef;
	}

	private Hierarchy processHierarchy(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		Hierarchy hierarchy = new Hierarchy();

		// read concept scheme properties
		// mandatory
		hierarchy.setId(se.getAttributeByName(new QName("id")).getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			hierarchy.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			hierarchy.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			hierarchy.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			hierarchy.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			hierarchy.setValidTo(a.getValue());
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
					Characters c = eventReader.nextEvent().asCharacters();
					if (langCode != null) {
						hierarchy.getName().addValue(langCode.getValue(),
								c.getData());
					} else {
						hierarchy.getName().setDefaultStr(c.getData());
					}
					eventReader.nextTag();
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
					Characters c = eventReader.nextEvent().asCharacters();
					if (langCode != null) {
						hierarchy.getDescription().addValue(
								langCode.getValue(), c.getData());
					} else {
						hierarchy.getDescription().setDefaultStr(c.getData());
					}
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodeRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read codeRef
					CodeRef cr = processCodeRef(se, eventReader);
					hierarchy.getCodeRefs().add(cr);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Hierarchy")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return hierarchy;
	}

	private CodeRef processCodeRef(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		CodeRef codeRef = new CodeRef();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("URN")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setUrn(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodelistAliasRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setCodelistAliasRef(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodeID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setCodeID(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CodeRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					codeRef.getChildren().add(processCodeRef(se, eventReader));
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"NodeAliasID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setNodeAliasID(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Version")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setVersion(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"ValidFrom")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setValidFrom(c.getData());
					eventReader.nextTag();
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"ValidTo")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Characters c = eventReader.nextEvent().asCharacters();
					codeRef.setValidTo(c.getData());
					eventReader.nextTag();
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("CodeRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return codeRef;
	}

}
