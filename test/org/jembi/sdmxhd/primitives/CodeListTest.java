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
package org.jembi.sdmxhd.primitives;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import junit.framework.Assert;

import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.parser.dsd.DSDParser;
import org.junit.Test;

public class CodeListTest {

	@Test
	public void testGetCodeByDescription() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<CodeList> codeLists = dsd.getCodeLists();
		CodeList codeList = codeLists.get(0);
		Code code = codeList.getCodeByDescription("not applicable");

		Assert.assertNotNull(code);
		Assert.assertEquals("1", code.getValue());
		Assert.assertEquals("not applicable", code.getDescription().getValue(
				"en"));
	}

	@Test
	public void testGetCodeByID() throws Exception {
		File f = new File("test/org/jembi/sdmxhd/include/DSD.xml");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory
				.createXMLEventReader(new FileReader(f));

		DSDParser dsdParser = new DSDParser();
		DSD dsd = dsdParser.parse(eventReader, null);

		List<CodeList> codeLists = dsd.getCodeLists();
		CodeList codeList = codeLists.get(0);
		Code code = codeList.getCodeByID("1");

		Assert.assertNotNull(code);
		Assert.assertEquals("1", code.getValue());
		Assert.assertEquals("not applicable", code.getDescription().getValue(
				"en"));
	}

}
