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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.jembi.sdmxhd.cds.CDS;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.junit.Test;

public class CDSParserTest {

	@Test
	public void testParse() throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		File f = new File("test/org/jembi/sdmxhd/include/CDS.xml");

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		CDSParser parser = new CDSParser();
		CDS cds = parser
				.parse(eventReader, null,
						"urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:compact");

		Assert.assertNotNull(cds);
		Assert.assertNotNull(cds.getHeader());
		Assert.assertEquals(1, cds.getDatasets().size());
	}

}
