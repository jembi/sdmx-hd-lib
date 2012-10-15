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
package org.jembi.sdmxhd.parser.csds;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.jembi.sdmxhd.csds.CSDS;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.junit.Test;

public class CSDSParserTest {

	@Test
	public void testParse() throws XMLStreamException, IOException,
			ExternalRefrenceNotFoundException {
		File f = new File("test/org/jembi/sdmxhd/include/generatedCSDS.xml");

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		CSDSParser parser = new CSDSParser();
		CSDS csds = parser
				.parse(eventReader, null,
						"urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:SL-HIV:1.0:cross");

		Assert.assertNotNull(csds);
		Assert.assertNotNull(csds.getHeader());
		Assert.assertEquals(1, csds.getDatasets().size());
	}

}
