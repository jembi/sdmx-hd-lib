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

import junit.framework.Assert;

import org.jembi.sdmxhd.convenience.DimensionWrapper;
import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.dsd.Dimension;
import org.junit.Test;

public class DSDParserTest {

	@Test
	public void parse_shouldreturnAValidDSDObject() throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser parser = new DSDParser();
		DSD dsd = parser.parse(eventReader, null);

		Assert.assertNotNull(dsd);
		Assert.assertEquals(2, dsd.getConceptSchemes().size());
		Assert.assertEquals(13, dsd.getCodeLists().size());
		Assert.assertEquals(1, dsd.getHierarchicalCodelists().size());
		Assert.assertEquals(1, dsd.getKeyFamilies().size());
	}

	@Test
	public void sierraLeoneTest() throws Exception {
		File f = new File(
				"/home/ryan/Documents/Jembi Projects/Sierra Leone Implmentation/SMDX-HD work in progress/art_sl_structure v2 edited.xml");

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser parser = new DSDParser();
		DSD dsd = parser.parse(eventReader, null);

		Assert.assertNotNull(dsd);

		List<Dimension> allIndicatorDimensions = dsd
				.getAllIndicatorDimensions("SL-ART-SUMMARY");

		List<List<DimensionWrapper>> allCombinationofDimensionsForIndicator = dsd
				.getAllCombinationofDimensionsForIndicator(
						"ART enrollment stage 2", "SL-ART-SUMMARY");
		Assert.assertNotNull(allCombinationofDimensionsForIndicator.get(0).get(
				0).getCode());
	}

	@Test
	public void sierraLeoneTest2() throws Exception {
		File f = new File(
				"/home/ryan/Documents/Jembi Projects/Sierra Leone Implmentation/SMDX-HD work in progress/art_sl_structure_v5_altns_edited.xml");

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser parser = new DSDParser();
		DSD dsd = parser.parse(eventReader, null);

		Assert.assertNotNull(dsd);

		List<Dimension> allIndicatorDimensions = dsd
				.getAllIndicatorDimensions("SL-ART-SUMMARY");

		List<List<DimensionWrapper>> allCombinationofDimensionsForIndicator = dsd
				.getAllCombinationofDimensionsForIndicator(
						"ART enrollment stage 2", "SL-ART-SUMMARY");
		Assert.assertNotNull(allCombinationofDimensionsForIndicator.get(0).get(
				0).getCode());
	}

}
