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

import org.jembi.sdmxhd.dsd.HierarchicalCodelist;
import org.junit.Assert;
import org.junit.Test;

public class HierarchicalCodeListParserTest {

	@Test
	public void parse_shouldReturnAValidHierarchicalCodeListObject()
			throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");
		List<HierarchicalCodelist> hierarchicalCodelist = null;

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));
		boolean isCodeListsTagRead = false;
		while (!isCodeListsTagRead) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelists")) {
					HierarchicalCodeListsParser hcp = new HierarchicalCodeListsParser();
					hierarchicalCodelist = hcp.parse(eventReader, se, null);
					isCodeListsTagRead = true;
				}
				break;
			}
		}
		Assert.assertEquals(3, hierarchicalCodelist.get(0).getHierarchys()
				.size());
		Assert.assertEquals("TIME_PERIOD_HIERARCHY", hierarchicalCodelist
				.get(0).getHierarchys().get(0).getId());
		Assert.assertEquals(4, hierarchicalCodelist.get(0).getHierarchys().get(
				0).getCodeRefs().get(0).getChildren().size());
	}

	@Test
	public void parse_shouldWorkWithExternalReferences() throws Exception {
		ZipFile zf = new ZipFile(
				"test/sdmxhd/include/HierarchicalCodelistExternTestMsg.zip");
		ZipEntry entry = zf.getEntry("DSD.xml");
		InputStream is = zf.getInputStream(entry);

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(is);

		List<HierarchicalCodelist> hierarchicalCodeLists = null;

		boolean endTagReached = false;
		while (!endTagReached) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase(
						"HierarchicalCodelists")) {
					HierarchicalCodeListsParser hcp = new HierarchicalCodeListsParser();
					hierarchicalCodeLists = hcp.parse(eventReader, se, zf);
					endTagReached = true;
				}
				break;
			}
		}

		Assert.assertNotNull(hierarchicalCodeLists);
		Assert.assertEquals(1, hierarchicalCodeLists.size());
		Assert.assertEquals(6, hierarchicalCodeLists.get(0).getCodeListRefs()
				.size());
		Assert.assertEquals("AL_OPERAND", hierarchicalCodeLists.get(0)
				.getCodeListRefs().get(0).getAlias());
		Assert.assertEquals(2, hierarchicalCodeLists.get(0).getHierarchys()
				.size());
		Assert.assertEquals("INDICATOR_HIERARCHY", hierarchicalCodeLists.get(0)
				.getHierarchys().get(0).getId());
	}

}
