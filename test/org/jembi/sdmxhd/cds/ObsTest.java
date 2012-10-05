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

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.util.Constants;
import org.junit.Test;

public class ObsTest {

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

		Obs obs = new Obs();
		obs.addAttribute("TEST1", "test1");
		obs.addAttribute("TEST2", "test2");
		obs.addAttribute("TEST3", "test3");

		obs.toXML(xmlWriter);

		System.out.println("Obs:");
		System.out.println(w.toString());

		// Assert.assertEquals("<ns:Obs TEST2=\"test2\" TEST3=\"test3\" TEST1=\"test1\"></ns:Obs>",
		// w.toString());
	}

}
