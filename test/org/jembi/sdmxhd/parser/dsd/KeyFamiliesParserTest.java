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
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import junit.framework.Assert;

import org.jembi.sdmxhd.dsd.KeyFamily;
import org.junit.Test;

public class KeyFamiliesParserTest {

	@Test
	public void parse_shouldReturnAValidkeyFamiliesObject() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		List<KeyFamily> keyFamily = null;

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));
		boolean isCodeListsTagRead = false;
		while (!isCodeListsTagRead) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("KeyFamilies")) {
					KeyFamiliesParser kfp = new KeyFamiliesParser();
					keyFamily = kfp.parse(eventReader, se, null);
					isCodeListsTagRead = true;
				}
				break;
			}
		}

		Assert.assertNotNull(keyFamily);
		Assert.assertEquals(1, keyFamily.size());
		Assert.assertEquals("SDMX-HD", keyFamily.get(0).getName()
				.getDefaultStr());

		Assert.assertEquals(7, keyFamily.get(0).getComponents().getDimensions()
				.size());
		Assert.assertEquals("DISEASE", keyFamily.get(0).getComponents()
				.getDimensions().get(0).getConceptRef());

		Assert.assertEquals(8, keyFamily.get(0).getComponents().getAttributes()
				.size());
		Assert.assertEquals("DATE_COLLECT", keyFamily.get(0).getComponents()
				.getAttributes().get(0).getConceptRef());

		Assert.assertEquals(1, keyFamily.get(0).getComponents().getGroups()
				.size());
		Assert.assertTrue(keyFamily.get(0).getComponents().getGroups().get(0)
				.getDimensionRefs().contains("PROG"));

		Assert.assertNotNull(keyFamily.get(0).getComponents()
				.getTimeDimension());
		Assert.assertEquals("TIME_PERIOD", keyFamily.get(0).getComponents()
				.getTimeDimension().getConceptRef());
	}

}
