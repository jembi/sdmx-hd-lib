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
package org.jembi.sdmxhd.parser;

import java.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.header.Contact;
import org.jembi.sdmxhd.header.Header;
import org.jembi.sdmxhd.header.Receiver;
import org.jembi.sdmxhd.header.Sender;
import org.jembi.sdmxhd.util.Constants;

public class HeaderParser {

	private ZipFile zipFile = null;

	public Header parse(XMLEventReader eventReader, StartElement startElement,
			ZipFile zipFile) throws XMLStreamException {
		// set zip file
		this.zipFile = zipFile;

		Header h = new Header();
		Boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("ID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setId(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase("Test")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setTest(Boolean.parseBoolean(tmp));
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Truncated")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setTruncated(Boolean.parseBoolean(tmp));
				} else if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
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
						h.getName().addValue(langCode.getValue(), tmp);
					} else {
						h.getName().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Prepared")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setPrepared(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Sender")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					Sender s = processSender(se, eventReader);
					h.getSenders().add(s);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Receiver")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					Receiver r = processReceiver(se, eventReader);
					h.getReceivers().add(r);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setKeyFamilyRef(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"KeyFamilyAgency")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setKeyFamilyAgency(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DataSetAgency")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setDataSetAgency(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DataSetID")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setDataSetID(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DataSetAction")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setDataSetAction(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Extracted")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setExtracted(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"ReportingBegin")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setReportingBegin(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"ReportingEnd")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setReportingEnd(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Source")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					h.setSource(tmp);
				}
				break;

			// stop looping when header end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"Header")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return h;
	}

	private Receiver processReceiver(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		Receiver receiver = new Receiver();

		// read receiver properties
		// mandatory
		receiver.setId(se.getAttributeByName(new QName("id")).getValue());

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					receiver.setName(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Contact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					// read Contact
					Contact c = processContact(se, eventReader);
					receiver.setContact(c);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Receiver")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return receiver;
	}

	private Sender processSender(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		Sender sender = new Sender();

		// read receiver properties
		// mandatory
		sender.setId(se.getAttributeByName(new QName("id")).getValue());

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					sender.setName(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Contact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					// read Contact
					Contact c = processContact(se, eventReader);
					sender.setContact(c);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Sender")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return sender;
	}

	private Contact processContact(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		Contact contact = new Contact();

		// read elements
		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Name")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setName(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Department")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setDepartment(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase("Role")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setRole(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Telephone")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setTelephone(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase("Fax")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setFax(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase("X400")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setX400(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase("URI")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setURI(tmp);
				} else if (se.getName().getLocalPart()
						.equalsIgnoreCase("Email")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					contact.setEmail(tmp);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Contact")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return contact;
	}

}
