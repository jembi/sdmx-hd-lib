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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jembi.sdmxhd.cds.DataSet;
import org.junit.Assert;
import org.junit.Test;

public class DataSetParserTest {

	@Test
	public void testParse() throws FileNotFoundException, XMLStreamException {
		File f = new File("test/sdmxhd/include/CDS.xml");
		List<DataSet> dataSets = new ArrayList<DataSet>();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));
		boolean isDataSetTagRead = false;
		while (!isDataSetTagRead) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLEvent.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (se.getName().getLocalPart().equalsIgnoreCase("DataSet")) {
					DataSetParser dsp = new DataSetParser();
					dataSets
							.add(dsp
									.parse(eventReader, se, null,
											"urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:compact"));
					isDataSetTagRead = true;
				}
				break;
			}
		}
		Assert.assertEquals(1, dataSets.size());
		Assert.assertEquals("MyDataSet", dataSets.get(0).getDatasetID());

		Assert.assertEquals(11, dataSets.get(0).getSeries().size());
		Assert.assertEquals(2, dataSets.get(0).getSeries().get(0).getObs()
				.size());
		Assert.assertEquals("23.76", dataSets.get(0).getSeries().get(0)
				.getObs().get(1).getAttributeValue("OBS_VALUE"));
	}

}
