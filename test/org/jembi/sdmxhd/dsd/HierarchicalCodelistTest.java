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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import junit.framework.Assert;

import org.jembi.sdmxhd.parser.dsd.DSDParser;
import org.junit.Test;

public class HierarchicalCodelistTest {

	@Test
	public void testGetHierarchy() throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);
		HierarchicalCodelist hierarchicalCodelist = dsd
				.getHierarchicalCodelists().get(0);

		Hierarchy hierarchy = hierarchicalCodelist
				.getHierarchy("INDICATOR_HIERARCHY");

		Assert.assertNotNull(hierarchy);
		Assert.assertEquals("INDICATOR_HIERARCHY", hierarchy.getId());
		Assert.assertEquals(1, hierarchy.getCodeRefs().size());
	}

	@Test
	public void testGetCodeListRef() throws Exception {
		File f = new File("test/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);
		HierarchicalCodelist hierarchicalCodelist = dsd
				.getHierarchicalCodelists().get(0);

		CodelistRef codeListRef1 = hierarchicalCodelist
				.getCodeListRef("CL_PROGRAM");

		CodelistRef codeListRef2 = hierarchicalCodelist
				.getCodeListRef("AL_PROGRAM");

		Assert.assertNotNull(codeListRef1);
		Assert.assertNotNull(codeListRef2);

		Assert.assertEquals("CL_PROGRAM", codeListRef1.getCodelistID());
		Assert.assertEquals("CL_PROGRAM", codeListRef2.getCodelistID());

		Assert.assertEquals("AL_PROGRAM", codeListRef1.getAlias());
		Assert.assertEquals("AL_PROGRAM", codeListRef2.getAlias());

		Assert.assertEquals(codeListRef1, codeListRef2);
	}

}
