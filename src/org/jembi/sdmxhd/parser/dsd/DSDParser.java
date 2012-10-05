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
import java.util.zip.ZipFile;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.parser.HeaderParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows a SDMX-HD DSD to be parsed into a DSD object
 */
public class DSDParser {

	/**
	 * Parses SDMX-HD DSD into a DSD object
	 * 
	 * @param eventReader
	 * @param zipFile
	 * @throws XMLStreamException
	 * @throws ExternalRefrenceNotFoundException
	 * @throws IOException
	 */
	public DSD parse(XMLEventReader eventReader, ZipFile zipFile)
			throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		DSD dsd = new DSD();
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Header")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					HeaderParser hParser = new HeaderParser();
					dsd.setHeader(hParser.parse(eventReader, se, zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"OrganisationScheme")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					OrganisationSchemesParser osParser = new OrganisationSchemesParser();
					dsd.setOrganisationSchemes(osParser.parse(eventReader, se,
							zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase("CodeLists")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					CodeListsParser clParser = new CodeListsParser();
					dsd.setCodeLists(clParser.parse(eventReader, se, zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelists")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					HierarchicalCodeListsParser hclParser = new HierarchicalCodeListsParser();
					dsd.setHierarchicalCodelists(hclParser.parse(eventReader,
							se, zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase("Concepts")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					ConceptsParser cParser = new ConceptsParser();
					dsd.setConceptSchemes(cParser.parse(eventReader, se,
							zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase("KeyFamilies")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					KeyFamiliesParser kfParser = new KeyFamiliesParser();
					dsd
							.setKeyFamilies(kfParser.parse(eventReader, se,
									zipFile));
				}
			}
		}
		return dsd;
	}

}
