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
package org.jembi.sdmxhd.dsd;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.bind.ValidationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.jembi.sdmxhd.SDMXHDMessage;
import org.jembi.sdmxhd.convenience.DimensionWrapper;
import org.jembi.sdmxhd.parser.SDMXHDParser;
import org.jembi.sdmxhd.parser.dsd.DSDParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.parser.exceptions.SchemaValidationException;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.junit.Test;

public class DSDTest {

	@Test
	public void testGetIndicatorNames() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		Set<LocalizedString> actual = dsd.getIndicatorNames("SDMX-HD");

		/*
		 * Set<LocalizedString> expected = new HashSet<LocalizedString>();
		 * LocalizedString ls = new LocalizedString(); ls.addValue("en",
		 * "Number of adults with advanced HIV infection who are currently receiving antiretroviral therapy in accordance with the nationally approved treatment protocol (or WHO/UNAIDS standards) and who were started on TB treatment (in accordance with national TB programme guidelines) within the reporting year"
		 * ); expected.add(ls); ls = new LocalizedString(); ls.addValue("en",
		 * "Percentage of estimated HIV-positive incident TB cases that received treatment for TB and HIV"
		 * ); expected.add(ls); ls = new LocalizedString(); ls.addValue("en",
		 * "Estimated number of incident TB cases in people living with HIV");
		 * expected.add(ls); ls = new LocalizedString(); ls.addValue("en",
		 * "Number of orphans infected with Malaria"); expected.add(ls);
		 * 
		 * Assert.assertEquals(expected, actual);
		 */

		/*
		 * LocalizedString ls = new LocalizedString(); ls.addValue("en",
		 * "Number of orphans infected with Malaria"); ls.addValue("fr",
		 * "Number of orphans infected with Malaria");
		 * 
		 * LocalizedString actualLS = actual.;
		 */

		Assert.assertNotNull(actual);
		Assert.assertEquals(4, actual.size());
		// Assert.assertEquals(ls, actualLS);
	}

	@Test
	public void testGetAllDimensions() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<Dimension> dimensions = dsd.getAllDimensions();

		Assert.assertEquals(7, dimensions.size());
		Assert.assertEquals("DISEASE", dimensions.get(0).getConceptRef());
	}

	/*
	 * @Test public void testGetAllAttributes() throws Exception { File f = new
	 * File("test/sdmxhd/include/DSD.xml"); XMLInputFactory factory =
	 * XMLInputFactory.newInstance(); XMLEventReader eventReader =
	 * factory.createXMLEventReader(new FileReader(f));
	 * 
	 * DSDParser dsdParser = new DSDParser(); DSD dsd =
	 * dsdParser.parse(eventReader, null);
	 * 
	 * List<Attribute> attributes = dsd.getAllAttributes();
	 * 
	 * Assert.assertEquals(8, attributes.size());
	 * Assert.assertEquals("DATE_COLLECT", attributes.get(0).getConceptRef()); }
	 */

	@Test
	public void testGetDimensions() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<DimensionWrapper> dimensionHierarchy = dsd
				.getDimensionHierarchy(
						"Estimated number of incident TB cases in people living with HIV",
						"SDMX-HD");

		Assert.assertEquals(3, dimensionHierarchy.size());
		Assert.assertEquals("VALUE_TYPE", dimensionHierarchy.get(0)
				.getDimension().getConceptRef());
	}

	@Test
	public void testToXML() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		String xml = dsd.toXML();

		System.out.println("DSD:");
		System.out.println(xml);
	}

	@Test
	public void testAllIndicatorDimensions() throws ZipException,
			ValidationException, XMLStreamException, IOException,
			ExternalRefrenceNotFoundException, SchemaValidationException {
		SDMXHDParser parser = new SDMXHDParser();
		SDMXHDMessage msg = parser.parse(new ZipFile(
				"test/org/jembi/sdmxhd/include/SDMX-HD.v1.0 sample1.USG [complete].zip"));

		DSD dsd = msg.getDsd();
		List<Dimension> allIndicatorDimensions = dsd
				.getAllIndicatorDimensions("SDMX-HD");

		Assert.assertNotNull(allIndicatorDimensions);
	}

	@Test
	public void testGetAllCombinationOfDimensions() throws Exception {
		File f = new File(
				"test/org/jembi/sdmxhd/include/art_sl_structure_v4.5_edited_no_disag_hy.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<List<DimensionWrapper>> allCombinationOfDimensions = dsd
				.getAllCombinationOfDimensions("SDMX-HD");
		List<Dimension> allIndicatorDimensions = dsd
				.getAllIndicatorDimensions("ART-SUMMARY");
		String s = "";
	}

}
