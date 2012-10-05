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
package org.jembi.sdmxhd.parser;

import java.util.zip.ZipFile;

import org.jembi.sdmxhd.SDMXHDMessage;
import org.junit.Assert;
import org.junit.Test;

public class SDMXHDParserTest {

	@Test
	public void parse_shouldParseASDMXMessageAndReturnAValidSDMXHDMessageObject()
			throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		SDMXHDMessage msg = parser.parse(new ZipFile(
				"test/sdmxhd/include/SDMX-HD.v1.0 sample1.zip"));

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
		/*
		 * Assert.assertNotNull(msg.getCds());
		 * Assert.assertNotNull(msg.getGms());
		 * Assert.assertNotNull(msg.getMsd());
		 */
	}

	@Test
	public void parse_shouldParseASDMXMessageAndReturnAValidSDMXHDMessageObjectWithCDS()
			throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		SDMXHDMessage msg = parser.parse(new ZipFile(
				"test/sdmxhd/include/SDMX-HD.v1.0 sample2.zip"));

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
		/*
		 * Assert.assertNotNull(msg.getCds());
		 * Assert.assertNotNull(msg.getGms());
		 * Assert.assertNotNull(msg.getMsd());
		 */
	}

	@Test
	public void generalTest() throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		ZipFile zf = new ZipFile(
				"/home/ryan/Documents/Jembi Projects/TRACnet Integration/SDMX-HD Examples and Docs/SDMX-HD.v1.0 sample1.USG [complete].zip");
		SDMXHDMessage msg = parser.parse(zf);

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
	}

	@Test
	public void generalTest2() throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		ZipFile zf = new ZipFile(
				"test/sdmxhd/include/SDMX-HD-v1.0 TRACnet Demo Data.zip");
		SDMXHDMessage msg = parser.parse(zf);

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
	}

	@Test
	public void generalTest3() throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		ZipFile zf = new ZipFile(
				"/home/ryan/Documents/Jembi Projects/Sierra Leone Implmentation/SMDX-HD work in progress/DSD Latest3.zip");
		SDMXHDMessage msg = parser.parse(zf);

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
	}

	@Test
	public void finalDraftSpecTest() throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		ZipFile zf = new ZipFile(
				"test/org/jembi/sdmxhd/include/final-spec/final-spec.zip");
		SDMXHDMessage msg = parser.parse(zf);

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
	}

	@Test
	public void sierraLeoneMultiKeyfamilyTest() throws Exception {

		SDMXHDParser parser = new SDMXHDParser();
		ZipFile zf = new ZipFile(
				"/home/ryan/Documents/Jembi Projects/Sierra Leone Implmentation/SMDX-HD work in progress/version 2/SLv2.zip");
		SDMXHDMessage msg = parser.parse(zf);

		// List<List<DimensionWrapper>> allCombinationOfDimensions =
		// msg.getDsd().getAllCombinationOfDimensions("KF_358732");

		Assert.assertNotNull(msg);
		Assert.assertNotNull(msg.getDsd());
	}

}
