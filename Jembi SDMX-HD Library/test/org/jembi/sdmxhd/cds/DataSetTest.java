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
package org.jembi.sdmxhd.cds;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.util.Constants;
import org.junit.Test;

public class DataSetTest {

	@Test
	public void testToXML() throws Exception {
		Writer w = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,
				Boolean.TRUE);

		XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(w);

		xmlWriter.setDefaultNamespace(Constants.DEFAULT_NAMESPACE);
		xmlWriter
				.setPrefix("ns",
						"urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=SDMX-HD:SL-HIV:1.0:compact");
		xmlWriter.setPrefix("compact", Constants.COMPACT_NAMESPACE);

		DataSet d = new DataSet();
		d.setAction("test");
		d.setDataProviderID("test");
		d.setDatasetID("test");
		d.setPublicationYear("test");

		Series s = new Series();
		HashMap<String, String> att = new HashMap<String, String>();
		att.put("test", "test");
		s.setAttributes(att);

		List<Series> seriesList = new ArrayList<Series>();
		seriesList.add(s);
		d.setSeries(seriesList);

		d.toXML(xmlWriter);

		System.out.println("DataSet:");
		System.out.println(w.toString());

		// Assert.assertEquals("<ns:DataSet datasetID=\"test\" dataProviderID=\"test\" action=\"test\" publicationYear=\"test\"><ns:Series test=\"test\"></ns:Series></ns:DataSet>",
		// w.toString());
	}

}
