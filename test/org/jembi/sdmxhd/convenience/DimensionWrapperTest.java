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
package org.jembi.sdmxhd.convenience;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import junit.framework.Assert;

import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.parser.dsd.DSDParser;
import org.junit.Test;

public class DimensionWrapperTest {

	@Test
	public void testGetAllCombinationsOfDimensions() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<DimensionWrapper> dhs = dsd
				.getDimensionHierarchy(
						"Estimated number of incident TB cases in people living with HIV",
						"SDMX-HD");
		List<List<DimensionWrapper>> allCombinationsOfDimensions = dhs.get(1)
				.getAllCombinationsOfDimensions();
		Assert.assertEquals("GENDER", allCombinationsOfDimensions.get(0).get(0)
				.getDimension().getConceptRef());
		Assert.assertEquals("Males", allCombinationsOfDimensions.get(0).get(0)
				.getCode().getDescription().getDefaultStr());
	}

}
