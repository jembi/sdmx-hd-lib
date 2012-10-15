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

import org.jembi.sdmxhd.dsd.Organisation;
import org.jembi.sdmxhd.dsd.OrganisationScheme;
import org.junit.Test;

public class OrganisationSchemesParserTest {

	@Test
	public void parseTest() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/orgDSD.xml");
		List<OrganisationScheme> orgScheme = null;

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
						"OrganisationSchemes")) {
					OrganisationSchemesParser osp = new OrganisationSchemesParser();
					orgScheme = osp.parse(eventReader, se, null);
					isCodeListsTagRead = true;
				}
				break;
			}
		}

		Assert.assertNotNull(orgScheme);
		OrganisationScheme orgScheme0 = orgScheme.get(0);
		OrganisationScheme orgScheme1 = orgScheme.get(1);
		Assert.assertNotNull(orgScheme0);
		Assert.assertNotNull(orgScheme1);

		Assert.assertEquals(orgScheme0.getId(), "ORGS_HISP");
		Organisation agency = orgScheme0.getAgencies().get(0);
		Assert.assertNotNull(agency);
		Assert.assertEquals(agency.getName().getDefaultStr(),
				"Sierra Leone Ministry of Health and Sanitation");

		Assert.assertEquals(orgScheme1.getId(), "ORGS_SL");
		Organisation dataProvider = orgScheme1.getDataProviders().get(0);
		Assert.assertNotNull(dataProvider);
		Assert.assertEquals(dataProvider.getName().getDefaultStr(),
				"Test Clinic");
	}

}
