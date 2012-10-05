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

import java.io.IOException;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.cds.CDS;
import org.jembi.sdmxhd.parser.HeaderParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.util.Constants;

/**
 * @author Ryan Crichton
 * 
 *         Allows a SDMX-HD CSD to be parsed into a CSD object
 */
public class CDSParser {

	/**
	 * Parses SDMX-HD CSD into a CSD object
	 * 
	 * @param eventReader
	 * @param zipFile
	 * @param derivedNamespace
	 * @return CDS represented as an object
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 */
	public CDS parse(XMLEventReader eventReader, ZipFile zipFile,
			String derivedNamespace) throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		CDS cds = new CDS();
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Header")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								Constants.DEFAULT_NAMESPACE)) {
					HeaderParser hParser = new HeaderParser();
					cds.setHeader(hParser.parse(eventReader, se, zipFile));
				}
				if (se.getName().getLocalPart().equalsIgnoreCase("DataSet")
						&& se.getName().getNamespaceURI().equalsIgnoreCase(
								derivedNamespace)) {
					DataSetParser dsParser = new DataSetParser();
					cds.getDatasets().add(
							dsParser.parse(eventReader, se, zipFile,
									derivedNamespace));
				}
			}
		}
		return cds;
	}

}
