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

import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.primitives.Code;
import org.jembi.sdmxhd.primitives.CodeList;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows CodeLists element in a SDMX-HD DSD to be parsed into CodeList
 *         objects
 */
public class CodeListsParser {

	private ZipFile zipFile = null;

	/**
	 * Parses CodeLists element in a SDMX-HD DSD into CodeList objects.
	 * 
	 * @param eventReader
	 * @param startElement
	 * @param zipFile
	 * @return a list of CodeLists
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 */
	public List<CodeList> parse(XMLEventReader eventReader,
			StartElement startElement, ZipFile zipFile)
			throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		// set zip file
		this.zipFile = zipFile;

		List<CodeList> codeLists = new ArrayList<CodeList>();
		Boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("CodeList")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					CodeList cl = processCodeList(se, eventReader);
					codeLists.add(cl);
				}
				break;

			// stop looping when concepts end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"CodeLists")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return codeLists;
	}

	private CodeList processCodeList(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		CodeList codeList = new CodeList();

		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				codeList.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				codeList.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				codeList.setId(id);

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
								"CodeList")
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

		// read codeList properties
		// mandatory
		codeList.setId(se.getAttributeByName(new QName("id")).getValue());

		codeList.setAgencyID(se.getAttributeByName(new QName("agencyID"))
				.getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			codeList.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			codeList.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			codeList.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			codeList.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			codeList.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			codeList.setValidTo(a.getValue());
		}

		// read elements
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
						codeList.getName().addValue(langCode.getValue(), tmp);
					} else {
						codeList.getName().setDefaultStr(tmp);
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
						codeList.getDescription().addValue(langCode.getValue(),
								tmp);
					} else {
						codeList.getDescription().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase("Code")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read code
					Code c = processCode(se, eventReader);
					codeList.getCodes().add(c);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("CodeList")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return codeList;
	}

	private Code processCode(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		Code code = new Code();

		// read code properties
		// mandatory
		code.setValue(se.getAttributeByName(new QName("value")).getValue());

		// optional attributes
		Attribute a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			code.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("parentCode"));
		if (a != null) {
			code.setParentCode(a.getValue());
		}

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Description")
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
						code.getDescription()
								.addValue(langCode.getValue(), tmp);
					} else {
						code.getDescription().setDefaultStr(tmp);
					}
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Code")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return code;
	}

}
