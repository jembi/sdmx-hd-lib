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

import org.jembi.sdmxhd.dsd.Components;
import org.jembi.sdmxhd.dsd.Dimension;
import org.jembi.sdmxhd.dsd.Group;
import org.jembi.sdmxhd.dsd.KeyFamily;
import org.jembi.sdmxhd.dsd.PrimaryMeasure;
import org.jembi.sdmxhd.dsd.TimeDimension;
import org.jembi.sdmxhd.parser.TextFormatParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.primitives.TextFormat;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows KeyFamilies element in a SDMX-HD DSD to be parsed into a
 *         KeyFamily object
 */
public class KeyFamiliesParser {

	private ZipFile zipFile = null;

	/**
	 * Parses KeyFamilies element in a SDMX-HD DSD into a KeyFamily object
	 * 
	 * @param eventReader
	 * @param startElement
	 * @param zipFile
	 * @return KeyFamily object
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 */
	public List<KeyFamily> parse(XMLEventReader eventReader,
			StartElement startElement, ZipFile zipFile)
			throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		this.zipFile = zipFile;

		List<KeyFamily> keyFamilies = new ArrayList<KeyFamily>();
		Boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("KeyFamily")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					KeyFamily kf = processKeyFamily(se, eventReader);
					keyFamilies.add(kf);
				}
				break;

			// stop looping when concepts end tag is reached
			case XMLEvent.END_ELEMENT:
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equalsIgnoreCase(
						"KeyFamilies")
						&& endElement.getName().getNamespaceURI()
								.equalsIgnoreCase(Constants.DEFAULT_NAMESPACE)) {
					endTagReached = true;
				}
				break;
			}
		}
		return keyFamilies;
	}

	private KeyFamily processKeyFamily(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		KeyFamily keyFamily = new KeyFamily();
		Attribute extern = se.getAttributeByName(new QName(
				"isExternalReference"));
		// if is external reference
		if (extern != null && extern.getValue().equalsIgnoreCase("TRUE")) {
			try {
				keyFamily.setIsExternalReference(extern.getValue());
				String uri = se.getAttributeByName(new QName("uri")).getValue();
				keyFamily.setUri(uri);
				if (uri.startsWith("./")) {
					uri = uri.substring(2);
				}
				String id = se.getAttributeByName(new QName("id")).getValue();
				keyFamily.setId(id);

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
								"External Reference to KeyFamily " + id
										+ " not found in this file");
					}

					switch (event.getEventType()) {
					case XMLEvent.START_ELEMENT:
						se = event.asStartElement();
						if (se.getName().getLocalPart().equalsIgnoreCase(
								"KeyFamily")
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
		keyFamily.setId(se.getAttributeByName(new QName("id")).getValue());
		keyFamily.setAgencyID(se.getAttributeByName(new QName("agencyID"))
				.getValue());

		// optional
		// optional attributes
		Attribute a = se.getAttributeByName(new QName("version"));
		if (a != null) {
			keyFamily.setVersion(a.getValue());
		}
		a = se.getAttributeByName(new QName("uri"));
		if (a != null) {
			keyFamily.setUri(a.getValue());
		}
		a = se.getAttributeByName(new QName("urn"));
		if (a != null) {
			keyFamily.setUrn(a.getValue());
		}
		a = se.getAttributeByName(new QName("isFinal"));
		if (a != null) {
			keyFamily.setIsFinal(a.getValue());
		}
		a = se.getAttributeByName(new QName("validFrom"));
		if (a != null) {
			keyFamily.setValidFrom(a.getValue());
		}
		a = se.getAttributeByName(new QName("validTo"));
		if (a != null) {
			keyFamily.setValidTo(a.getValue());
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
						keyFamily.getName().addValue(langCode.getValue(), tmp);
					} else {
						keyFamily.getName().setDefaultStr(tmp);
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
						keyFamily.getDescription().addValue(
								langCode.getValue(), tmp);
					} else {
						keyFamily.getDescription().setDefaultStr(tmp);
					}
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Components")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// read components
					Components com = processComponents(se, eventReader);
					keyFamily.setComponents(com);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("KeyFamily")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return keyFamily;
	}

	private Components processComponents(StartElement startElement,
			XMLEventReader eventReader) throws XMLStreamException {
		Components components = new Components();

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Dimension")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Dimension d = processDimension(se, eventReader);
					components.getDimensions().add(d);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"Attribute")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					org.jembi.sdmxhd.dsd.Attribute a = processAttribute(se,
							eventReader);
					components.getAttributes().add(a);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"TimeDimension")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TimeDimension td = processTimeDimension(se, eventReader);
					components.setTimeDimension(td);
				} else if (se.getName().getLocalPart()
						.equalsIgnoreCase("Group")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					Group g = processGroup(se, eventReader);
					components.getGroups().add(g);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"PrimaryMeasure")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					PrimaryMeasure p = processPrimaryMeasure(se, eventReader);
					components.setPrimaryMeasure(p);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"CrossSectionalMeasure")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					// TODO
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Components")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return components;
	}

	private TimeDimension processTimeDimension(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		TimeDimension td = new TimeDimension();

		// mandatory
		td.setConceptRef(se.getAttributeByName(new QName("conceptRef"))
				.getValue());

		// optional
		// optional attributes
		Attribute cVersion = se.getAttributeByName(new QName("conceptVersion"));
		if (cVersion != null) {
			td.setConceptVersion(cVersion.getValue());
		}
		Attribute cAgency = se.getAttributeByName(new QName("conceptAgency"));
		if (cAgency != null) {
			td.setConceptAgency(cAgency.getValue());
		}
		Attribute cSchemeRef = se.getAttributeByName(new QName(
				"conceptSchemeRef"));
		if (cSchemeRef != null) {
			td.setConceptSchemeRef(cSchemeRef.getValue());
		}
		Attribute cSchemeAgency = se.getAttributeByName(new QName(
				"conceptSchemeAgency"));
		if (cSchemeAgency != null) {
			td.setConceptSchemeAgency(cSchemeAgency.getValue());
		}
		Attribute codelist = se.getAttributeByName(new QName("codelist"));
		if (codelist != null) {
			td.setCodelist(codelist.getValue());
		}
		Attribute codelistVersion = se.getAttributeByName(new QName(
				"codelistVersion"));
		if (codelistVersion != null) {
			td.setCodelistVersion(codelistVersion.getValue());
		}
		Attribute codelistAgency = se.getAttributeByName(new QName(
				"codelistAgency"));
		if (codelistAgency != null) {
			td.setCodelistAgency(codelistAgency.getValue());
		}
		Attribute crossSectionalAttachDataSet = se
				.getAttributeByName(new QName("crossSectionalAttachDataSet"));
		if (crossSectionalAttachDataSet != null) {
			td.setCrossSectionalAttachDataSet(Boolean
					.parseBoolean(crossSectionalAttachDataSet.getValue()));
		}
		Attribute crossSectionalAttachGroup = se.getAttributeByName(new QName(
				"crossSectionalAttachGroup"));
		if (crossSectionalAttachGroup != null) {
			td.setCrossSectionalAttachGroup(Boolean
					.parseBoolean(crossSectionalAttachGroup.getValue()));
		}
		Attribute crossSectionalAttachSection = se
				.getAttributeByName(new QName("crossSectionalAttachSection"));
		if (crossSectionalAttachSection != null) {
			td.setCrossSectionalAttachSection(Boolean
					.parseBoolean(crossSectionalAttachSection.getValue()));
		}
		Attribute crossSectionalAttachObservation = se
				.getAttributeByName(new QName("crossSectionalAttachObservation"));
		if (crossSectionalAttachObservation != null) {
			td.setCrossSectionalAttachObservation(Boolean
					.parseBoolean(crossSectionalAttachObservation.getValue()));
		}

		boolean endTagReached = false;

		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("TextFormat")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TextFormatParser tfp = new TextFormatParser();
					TextFormat textFormat = tfp.parse(eventReader, se, zipFile);
					td.setTextFormat(textFormat);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"TimeDimension")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return td;
	}

	private Group processGroup(StartElement se, XMLEventReader eventReader)
			throws XMLStreamException {
		Group g = new Group();

		// mandatory
		g.setId(se.getAttributeByName(new QName("id")).getValue());

		boolean endTagReached = false;

		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Description")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					g.setDescription(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"DimensionRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					g.getDimensionRefs().add(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"AttachmentConstraintRef")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					g.setAttachmentConstraintRef(tmp);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Group")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return g;
	}

	private PrimaryMeasure processPrimaryMeasure(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		PrimaryMeasure pm = new PrimaryMeasure();

		// mandatory
		pm.setConceptRef(se.getAttributeByName(new QName("conceptRef"))
				.getValue());

		// optional
		// optional attributes
		Attribute cVersion = se.getAttributeByName(new QName("conceptVersion"));
		if (cVersion != null) {
			pm.setConceptVersion(cVersion.getValue());
		}
		Attribute cAgency = se.getAttributeByName(new QName("conceptAgency"));
		if (cAgency != null) {
			pm.setConceptAgency(cAgency.getValue());
		}
		Attribute cSchemeRef = se.getAttributeByName(new QName(
				"conceptSchemeRef"));
		if (cSchemeRef != null) {
			pm.setConceptSchemeRef(cSchemeRef.getValue());
		}
		Attribute cSchemeAgency = se.getAttributeByName(new QName(
				"conceptSchemeAgency"));
		if (cSchemeAgency != null) {
			pm.setConceptSchemeAgency(cSchemeAgency.getValue());
		}
		Attribute codelist = se.getAttributeByName(new QName("codelist"));
		if (codelist != null) {
			pm.setCodelist(codelist.getValue());
		}
		Attribute codelistVersion = se.getAttributeByName(new QName(
				"codelistVersion"));
		if (codelistVersion != null) {
			pm.setCodelistVersion(codelistVersion.getValue());
		}
		Attribute codelistAgency = se.getAttributeByName(new QName(
				"codelistAgency"));
		if (codelistAgency != null) {
			pm.setCodelistAgency(codelistAgency.getValue());
		}

		boolean endTagReached = false;

		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("TextFormat")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TextFormatParser tfp = new TextFormatParser();
					TextFormat textFormat = tfp.parse(eventReader, se, zipFile);
					pm.setTextFormat(textFormat);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase(
						"PrimaryMeasure")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}

		return pm;
	}

	private org.jembi.sdmxhd.dsd.Attribute processAttribute(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		org.jembi.sdmxhd.dsd.Attribute attribute = new org.jembi.sdmxhd.dsd.Attribute();

		// read concept scheme properties
		// mandatory
		attribute.setConceptRef(se.getAttributeByName(new QName("conceptRef"))
				.getValue());
		attribute.setAttachmentLevel(se.getAttributeByName(
				new QName("attachmentLevel")).getValue());
		attribute.setAssignmentStatus(se.getAttributeByName(
				new QName("assignmentStatus")).getValue());

		// optional
		// optional attributes
		Attribute cVersion = se.getAttributeByName(new QName("conceptVersion"));
		if (cVersion != null) {
			attribute.setConceptVersion(cVersion.getValue());
		}
		Attribute cAgency = se.getAttributeByName(new QName("conceptAgency"));
		if (cAgency != null) {
			attribute.setConceptAgency(cAgency.getValue());
		}
		Attribute cSchemeRef = se.getAttributeByName(new QName(
				"conceptSchemeRef"));
		if (cSchemeRef != null) {
			attribute.setConceptSchemeRef(cSchemeRef.getValue());
		}
		Attribute cSchemeAgency = se.getAttributeByName(new QName(
				"conceptSchemeAgency"));
		if (cSchemeAgency != null) {
			attribute.setConceptSchemeAgency(cSchemeAgency.getValue());
		}
		Attribute codelist = se.getAttributeByName(new QName("codelist"));
		if (codelist != null) {
			attribute.setCodelist(codelist.getValue());
		}
		Attribute codelistVersion = se.getAttributeByName(new QName(
				"codelistVersion"));
		if (codelistVersion != null) {
			attribute.setCodelistVersion(codelistVersion.getValue());
		}
		Attribute codelistAgency = se.getAttributeByName(new QName(
				"codelistAgency"));
		if (codelistAgency != null) {
			attribute.setCodelistAgency(codelistAgency.getValue());
		}
		Attribute isTimeFormat = se
				.getAttributeByName(new QName("isTimeFormat"));
		if (isTimeFormat != null) {
			attribute.setTimeFormat(Boolean.parseBoolean(isTimeFormat
					.getValue()));
		}
		Attribute crossSectionalAttachDataSet = se
				.getAttributeByName(new QName("crossSectionalAttachDataSet"));
		if (crossSectionalAttachDataSet != null) {
			attribute.setCrossSectionalAttachDataSet(Boolean
					.parseBoolean(crossSectionalAttachDataSet.getValue()));
		}
		Attribute crossSectionalAttachGroup = se.getAttributeByName(new QName(
				"crossSectionalAttachGroup"));
		if (crossSectionalAttachGroup != null) {
			attribute.setCrossSectionalAttachGroup(Boolean
					.parseBoolean(crossSectionalAttachGroup.getValue()));
		}
		Attribute crossSectionalAttachSection = se
				.getAttributeByName(new QName("crossSectionalAttachSection"));
		if (crossSectionalAttachSection != null) {
			attribute.setCrossSectionalAttachSection(Boolean
					.parseBoolean(crossSectionalAttachSection.getValue()));
		}
		Attribute crossSectionalAttachObservation = se
				.getAttributeByName(new QName("crossSectionalAttachObservation"));
		if (crossSectionalAttachObservation != null) {
			attribute.setCrossSectionalAttachObservation(Boolean
					.parseBoolean(crossSectionalAttachObservation.getValue()));
		}
		Attribute isEntityAttribute = se.getAttributeByName(new QName(
				"isEntityAttribute"));
		if (isEntityAttribute != null) {
			attribute.setEntityAttribute(Boolean.parseBoolean(isEntityAttribute
					.getValue()));
		}
		Attribute isNonObservationalTimeAttribute = se
				.getAttributeByName(new QName("isNonObservationalTimeAttribute"));
		if (isNonObservationalTimeAttribute != null) {
			attribute.setNonObservationalTimeAttribute(Boolean
					.parseBoolean(isNonObservationalTimeAttribute.getValue()));
		}
		Attribute isCountAttribute = se.getAttributeByName(new QName(
				"isCountAttribute"));
		if (isCountAttribute != null) {
			attribute.setCountAttribute(Boolean.parseBoolean(isCountAttribute
					.getValue()));
		}
		Attribute isFrequencyAttribute = se.getAttributeByName(new QName(
				"isFrequencyAttribute"));
		if (isFrequencyAttribute != null) {
			attribute.setFrequencyAttribute(Boolean
					.parseBoolean(isFrequencyAttribute.getValue()));
		}

		boolean endTagReached = false;

		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("TextFormat")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TextFormatParser tfp = new TextFormatParser();
					TextFormat textFormat = tfp.parse(eventReader, se, zipFile);
					attribute.setTextFormat(textFormat);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"AttachmentGroup")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					attribute.setAttachmentGroup(tmp);
				} else if (se.getName().getLocalPart().equalsIgnoreCase(
						"AttachmentMeasure")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					String tmp = "";
					while ((event = eventReader.nextEvent()).isCharacters()) {
						tmp += event.asCharacters().getData();
					}
					attribute.setAttachmentMeasure(tmp);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Attribute")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return attribute;
	}

	private Dimension processDimension(StartElement se,
			XMLEventReader eventReader) throws XMLStreamException {
		Dimension dimension = new Dimension();

		// read concept scheme properties
		// mandatory
		dimension.setConceptRef(se.getAttributeByName(new QName("conceptRef"))
				.getValue());

		// optional
		// optional attributes
		Attribute cVersion = se.getAttributeByName(new QName("conceptVersion"));
		if (cVersion != null) {
			dimension.setConceptVersion(cVersion.getValue());
		}
		Attribute cAgency = se.getAttributeByName(new QName("conceptAgency"));
		if (cAgency != null) {
			dimension.setConceptAgency(cAgency.getValue());
		}
		Attribute cSchemeRef = se.getAttributeByName(new QName(
				"conceptSchemeRef"));
		if (cSchemeRef != null) {
			dimension.setConceptSchemeRef(cSchemeRef.getValue());
		}
		Attribute cSchemeAgency = se.getAttributeByName(new QName(
				"conceptSchemeAgency"));
		if (cSchemeAgency != null) {
			dimension.setConceptSchemeAgency(cSchemeAgency.getValue());
		}
		Attribute codelist = se.getAttributeByName(new QName("codelist"));
		if (codelist != null) {
			dimension.setCodelistRef(codelist.getValue());
		}
		Attribute codelistVersion = se.getAttributeByName(new QName(
				"codelistVersion"));
		if (codelistVersion != null) {
			dimension.setCodelistVersion(codelistVersion.getValue());
		}
		Attribute codelistAgency = se.getAttributeByName(new QName(
				"codelistAgency"));
		if (codelistAgency != null) {
			dimension.setCodelistAgency(codelistAgency.getValue());
		}
		Attribute isMeasureDimension = se.getAttributeByName(new QName(
				"isMeasureDimension"));
		if (isMeasureDimension != null) {
			dimension.setMeasureDimension(Boolean
					.parseBoolean(isMeasureDimension.getValue()));
		}
		Attribute isFrequencyDimension = se.getAttributeByName(new QName(
				"isFrequencyDimension"));
		if (isFrequencyDimension != null) {
			dimension.setFrequencyDimension(Boolean
					.parseBoolean(isFrequencyDimension.getValue()));
		}
		Attribute isEntityDimension = se.getAttributeByName(new QName(
				"isEntityDimension"));
		if (isEntityDimension != null) {
			dimension.setEntityDimension(Boolean.parseBoolean(isEntityDimension
					.getValue()));
		}
		Attribute isCountDimension = se.getAttributeByName(new QName(
				"isCountDimension"));
		if (isCountDimension != null) {
			dimension.setCountDimension(Boolean.parseBoolean(isCountDimension
					.getValue()));
		}
		Attribute isNonObservationTimeDimension = se
				.getAttributeByName(new QName("isNonObservationTimeDimension"));
		if (isNonObservationTimeDimension != null) {
			dimension.setNonObservationTimeDimension(Boolean
					.parseBoolean(isNonObservationTimeDimension.getValue()));
		}
		Attribute isIdentityDimension = se.getAttributeByName(new QName(
				"isIdentityDimension"));
		if (isIdentityDimension != null) {
			dimension.setIdentityDimension(Boolean
					.parseBoolean(isIdentityDimension.getValue()));
		}
		Attribute crossSectionalAttachDataSet = se
				.getAttributeByName(new QName("crossSectionalAttachDataSet"));
		if (crossSectionalAttachDataSet != null) {
			dimension.setCrossSectionalAttachDataSet(Boolean
					.parseBoolean(crossSectionalAttachDataSet.getValue()));
		}
		Attribute crossSectionalAttachGroup = se.getAttributeByName(new QName(
				"crossSectionalAttachGroup"));
		if (crossSectionalAttachGroup != null) {
			dimension.setCrossSectionalAttachGroup(Boolean
					.parseBoolean(crossSectionalAttachGroup.getValue()));
		}
		Attribute crossSectionalAttachSection = se
				.getAttributeByName(new QName("crossSectionalAttachSection"));
		if (crossSectionalAttachSection != null) {
			dimension.setCrossSectionalAttachSection(Boolean
					.parseBoolean(crossSectionalAttachSection.getValue()));
		}
		Attribute crossSectionalAttachObservation = se
				.getAttributeByName(new QName("crossSectionalAttachObservation"));
		if (crossSectionalAttachObservation != null) {
			dimension.setCrossSectionalAttachObservation(Boolean
					.parseBoolean(crossSectionalAttachObservation.getValue()));
		}

		boolean endTagReached = false;

		while (!endTagReached) {
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("TextFormat")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					TextFormatParser tfp = new TextFormatParser();
					TextFormat textFormat = tfp.parse(eventReader, se, zipFile);
					dimension.setTextFormat(textFormat);
				}
			} else if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if (ee.getName().getLocalPart().equalsIgnoreCase("Dimension")
						&& ee.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.STRUCTURE_NAMESPACE)) {
					endTagReached = true;
				}
			}
		}
		return dimension;
	}

}
