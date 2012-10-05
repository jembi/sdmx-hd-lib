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

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.primitives.Concept;
import org.jembi.sdmxhd.primitives.ConceptScheme;
import org.junit.Assert;
import org.junit.Test;

public class ConceptsParserTest {

	@Test
	public void parse_shouldReturnTheCorrectListOfConceptsForTheGivenEventReader()
			throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");
		List<ConceptScheme> conceptSchemes = null;

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));
		boolean readConceptsTag = false;
		while (!readConceptsTag) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Concepts")) {
					ConceptsParser cp = new ConceptsParser();
					conceptSchemes = cp.parse(eventReader, se, null);
					readConceptsTag = true;
				}
				break;
			}
		}

		Assert.assertNotNull(conceptSchemes);
		Assert.assertEquals(2, conceptSchemes.size());

		Iterator<ConceptScheme> iterator = conceptSchemes.iterator();
		ConceptScheme cs_common = null;
		while (iterator.hasNext()) {
			ConceptScheme conceptScheme = (ConceptScheme) iterator.next();
			if (conceptScheme.getId().equalsIgnoreCase("CS_COMMON")) {
				cs_common = conceptScheme;
				break;
			}
		}

		List<Concept> concepts = cs_common.getConcepts();
		Assert.assertEquals(16, concepts.size());
		Assert.assertEquals("DISEASE", concepts.get(0).getId());
		Assert
				.assertEquals("Disease", concepts.get(0).getName().getValue(
						"en"));
		Assert.assertEquals("Disease name.", concepts.get(0).getDescription()
				.getDefaultStr());
	}

	@Test
	public void parse_shouldWorkWithExternalRefrences() throws Exception {
		ZipFile zf = new ZipFile("test/sdmxhd/include/SDMX-HD.v1.0 sample1.zip");
		ZipEntry entry = zf.getEntry("DSD.xml");
		InputStream is = zf.getInputStream(entry);

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(is);

		List<ConceptScheme> conceptSchemes = null;

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("Concepts")) {
					ConceptsParser cp = new ConceptsParser();
					conceptSchemes = cp.parse(eventReader, se, zf);
					endTagReached = true;
				}
				break;
			}
		}

		Assert.assertNotNull(conceptSchemes);
		Assert.assertEquals(2, conceptSchemes.size());
		Assert.assertEquals(39, conceptSchemes.get(0).getConcepts().size());
		Assert.assertEquals("Observation Comment", conceptSchemes.get(0)
				.getConcepts().get(0).getName().getValue("en"));
	}
}
