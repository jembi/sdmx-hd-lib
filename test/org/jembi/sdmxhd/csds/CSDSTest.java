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
package org.jembi.sdmxhd.csds;

import java.io.File;
import java.io.FileWriter;

import org.jembi.sdmxhd.header.Header;
import org.jembi.sdmxhd.header.Sender;
import org.junit.Test;

public class CSDSTest {

	@Test
	public void testToXML() throws Exception {
		Sender p = new Sender();
		p.setId("HSID");
		p.setName("Health Statistics and Informatics Department ");

		Header h = new Header();
		h.setId("SDMX-HD-CDS");
		h.setTest(false);
		h.setTruncated(false);
		h.getName().addValue("en", "SDMX Health Domain");
		h.setPrepared("2009-03-20");
		h.setDataSetID("MyDataSet");
		h.getSenders().add(p);

		Obs o = new Obs();
		o.getAttributes().put("OBS_VALUE", "23");
		o.getAttributes().put("DATE_COLLECT", "2009-03-20");

		Section s = new Section();
		s.getAttributes().put("GENDER", "1");
		s.getAttributes().put("INDICATOR", "2");
		s.getAttributes().put("AGROUP", "1");
		s.getObs().add(o);

		Group g = new Group();
		g.getSections().add(s);

		DataSet d = new DataSet();
		d.setDatasetID("MyDataSet");
		d.getGroups().add(g);

		CSDS csds = new CSDS();
		csds.getDatasets().add(d);
		csds.setHeader(h);

		String xml = csds
				.toXML("urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:SL-HIV:1.0:cross");

		FileWriter fw = new FileWriter(new File(
				"test/org/jembi/sdmxhd/include/generatedCSDS.xml"));
		fw.write(xml);
		fw.close();

		System.out.println("CSDS:");
		System.out.println(xml);

		// Assert.assertEquals("<?xml version=\"1.0\" ?><CompactData xmlns=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message\" xmlns:ns=\"urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:compact\" xmlns:compact=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/compact\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message SDMXMessage.xsd urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:compact CDS.xsd http://www.SDMX.org/resources/SDMXML/schemas/v2_0/compact SDMXCompactData.xsd\"><Header><ID>SDMX-HD-CDS</ID><Test>false</Test><Truncated>false</Truncated><Name xml:lang=\"en\">SDMX Health Domain</Name><Prepared>2009-03-20</Prepared><Sender id=\"HSID\"><Name>Health Statistics and Informatics Department </Name></Sender><DataSetID>MyDataSet</DataSetID></Header><ns:DataSet datasetID=\"MyDataSet\"><ns:Series AGROUP=\"1\" GENDER=\"1\" INDICATOR=\"2\"><ns:Obs DATE_COLLECT=\"2009-03-20\" OBS_VALUE=\"23\"></ns:Obs></ns:Series></ns:DataSet></CompactData>",
		// xml);
	}

}
