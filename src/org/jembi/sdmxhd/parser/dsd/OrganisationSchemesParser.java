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

import org.jembi.sdmxhd.dsd.Organisation;
import org.jembi.sdmxhd.dsd.OrganisationScheme;
import org.jembi.sdmxhd.header.Contact;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.util.Constants;

public class OrganisationSchemesParser {

	private ZipFile zipFile = null;

	public List<OrganisationScheme> parse(XMLEventReader eventReader,
			StartElement startElement, ZipFile zipFile)
			throws XMLStreamException, ExternalRefrenceNotFoundException,
			IOException {
		this.zipFile = zipFile;

		List<OrganisationScheme> organisationSchemes = new ArrayList<OrganisationScheme>();
		Boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"OrganisationScheme")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					OrganisationScheme os = processOrganisationScheme(se,
							eventReader);
					organisationSchemes.add(os);
				}
				break;

			// stop looping when concepts end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"OrganisationSchemes")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return organisationSchemes;
	}

	private OrganisationScheme processOrganisationScheme(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException,
			ExternalRefrenceNotFoundException {
		OrganisationScheme organisationScheme = new OrganisationScheme();
		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				organisationScheme.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				organisationScheme.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				organisationScheme.setId(id);

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
								"External Reference to OrganisationScheme "
										+ id + " not found in this file");
					}

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								"OrganisationScheme")
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
		organisationScheme.setId(se.getAttributeByName(new QName("id"))
				.getValue());
		organisationScheme.setAgencyID(se.getAttributeByName(
				new QName("agencyID")).getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			organisationScheme.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			organisationScheme.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			organisationScheme.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			organisationScheme.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			organisationScheme.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			organisationScheme.setValidTo(a.getValue());
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
						organisationScheme.getName().addValue(
								langCode.getValue(), tmp);
					} else {
						organisationScheme.getName().setDefaultStr(tmp);
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
						organisationScheme.getDescription().addValue(
								langCode.getValue(), tmp);
					} else {
						organisationScheme.getDescription().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Agencies")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read Agency
					List<Organisation> agencies = processAgencies(se,
							eventReader);
					organisationScheme.setAgencies(agencies);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DataProviders")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read DataProviders
					List<Organisation> dataProviders = processDataProviders(se,
							eventReader);
					organisationScheme.setDataProviders(dataProviders);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DataConsumersType")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read DataConsumersType
					List<Organisation> dataConsumersType = processDataConsumers(
							se, eventReader);
					organisationScheme.setDataConsumers(dataConsumersType);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"OrganisationScheme")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return organisationScheme;
	}

	private List<Organisation> processAgencies(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException,
			ExternalRefrenceNotFoundException {
		List<Organisation> agencies = new ArrayList<Organisation>();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Agency")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read org
					Organisation o = processOrganisation(se, eventReader,
							"Agency");
					agencies.add(o);
				}

			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Agencies")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return agencies;
	}

	private List<Organisation> processDataProviders(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException,
			ExternalRefrenceNotFoundException {
		List<Organisation> dateProviders = new ArrayList<Organisation>();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart()
						.equalsIgnoreCase("DataProvider")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read org
					Organisation o = processOrganisation(se, eventReader,
							"DataProvider");
					dateProviders.add(o);
				}

			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"DataProviders")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return dateProviders;
	}

	private List<Organisation> processDataConsumers(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException,
			ExternalRefrenceNotFoundException {
		List<Organisation> dataConsumers = new ArrayList<Organisation>();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart()
						.equalsIgnoreCase("DataConsumer")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read org
					Organisation o = processOrganisation(se, eventReader,
							"DataConsumer");
					dataConsumers.add(o);
				}

			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"DataConsumers")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return dataConsumers;
	}

	private Organisation processOrganisation(StartElement se,
			XMLEventReader eventReader, String xmlElementName)
			throws XMLStreamException, ExternalRefrenceNotFoundException {
		Organisation o = new Organisation();

		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				o.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				o.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				o.setId(id);

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
								"External Reference to Organisation " + id
										+ " not found in this file");
					}

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								xmlElementName)
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
		o.setId(se.getAttributeByName(new QName("id")).getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			o.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			o.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			o.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("parentOrganisation"));
		if (a != null) {
			o.setParentOrganisation(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			o.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			o.setValidTo(a.getValue());
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
						o.getName().addValue(langCode.getValue(), tmp);
					} else {
						o.getName().setDefaultStr(tmp);
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
						o.getDescription().addValue(langCode.getValue(), tmp);
					} else {
						o.getDescription().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"MaintenanceContact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read MaintenanceContact
					Contact c = processContact(se, eventReader);
					o.setMaintenanceContact(c);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CollectorContact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read MaintenanceContact
					Contact c = processContact(se, eventReader);
					o.setCollectorContact(c);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DisseminatorContact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read conDisseminatorContactcept
					Contact c = processContact(se, eventReader);
					o.setDisseminatorContact(c);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"ReporterContact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read ReporterContact
					Contact c = processContact(se, eventReader);
					o.setReporterContact(c);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"OtherContact")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read OtherContact
					Contact c = processContact(se, eventReader);
					o.setOtherContact(c);
				}

			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart()
						.equalsIgnoreCase(xmlElementName)
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return o;
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
