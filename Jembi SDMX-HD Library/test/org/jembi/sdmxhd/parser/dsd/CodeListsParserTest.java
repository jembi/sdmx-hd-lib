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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import junit.framework.Assert;

import org.jembi.sdmxhd.primitives.CodeList;
import org.junit.Test;

public class CodeListsParserTest {

	@Test
	public void parse_shouldReturnValidCodeListsGivenAnAppropriateEventReader()
			throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");
		List<CodeList> codeLists = null;

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));
		boolean isCodeListsTagRead = false;
		while (!isCodeListsTagRead) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("CodeLists")) {
					CodeListsParser clp = new CodeListsParser();
					codeLists = clp.parse(eventReader, se, null);
					isCodeListsTagRead = true;
				}
				break;
			}
		}

		Assert.assertNotNull(codeLists);
		Assert.assertEquals(13, codeLists.size());
		Assert.assertEquals("CL_SPECIAL_VALUE", codeLists.get(0).getId());
		Assert.assertEquals("Special Value", codeLists.get(0).getName()
				.getDefaultStr());
		Assert.assertEquals(null, codeLists.get(0).getDescription().getValue(
				"en"));
	}

	@Test
	public void parse_shouldWorkWithExternalReferences() throws Exception {
		ZipFile zf = new ZipFile("test/sdmxhd/include/SDMX-HD.v1.0 sample1.zip");
		ZipEntry entry = zf.getEntry("DSD.xml");
		InputStream is = zf.getInputStream(entry);

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(is);

		List<CodeList> codeLists = null;

		boolean reachedEndTag = false;
		while (!reachedEndTag) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("CodeLists")) {
					CodeListsParser cp = new CodeListsParser();
					codeLists = cp.parse(eventReader, se, zf);
					reachedEndTag = true;
				}
				break;
			}
		}

		Assert.assertNotNull(codeLists);
		Assert.assertEquals(17, codeLists.size());
		Assert.assertEquals("CL_LOGICAL", codeLists.get(0).getId());
		Assert.assertEquals(5, codeLists.get(0).getCodes().size());
	}
}
