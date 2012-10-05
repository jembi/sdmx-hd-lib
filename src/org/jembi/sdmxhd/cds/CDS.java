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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.header.Header;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.validation.SDMXHDValidator;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a SDMX-HD Compact DataSet or CDS.
 */
public class CDS {

	private Header header;
	private List<DataSet> datasets = new ArrayList<DataSet>();

	/**
	 * Converts this CDS object into its SDMX-HD XML representation
	 * 
	 * @return XML for this CDS
	 * @throws XMLStreamException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ValidationException
	 */
	public String toXML(String derivedNamespace) throws XMLStreamException,
			IllegalArgumentException, IllegalAccessException,
			ValidationException, IOException {
		Writer w = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		// factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,Boolean.TRUE);

		XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(w);

		xmlWriter.writeStartDocument();

		xmlWriter.setDefaultNamespace(Constants.DEFAULT_NAMESPACE);
		xmlWriter.setPrefix("ns", derivedNamespace);
		xmlWriter.setPrefix("compact", Constants.COMPACT_NAMESPACE);

		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE, "CompactData");
		xmlWriter.writeAttribute("xmlns", Constants.DEFAULT_NAMESPACE);
		xmlWriter.writeAttribute("xmlns:ns", derivedNamespace);
		xmlWriter.writeAttribute("xmlns:compact", Constants.COMPACT_NAMESPACE);
		xmlWriter.writeAttribute("xmlns:xsi",
				Constants.XML_SCHEMA_INSTANCE_NAMESPACE);
		xmlWriter.writeAttribute("xsi:schemaLocation",
				Constants.STRUCTURE_NAMESPACE + " SDMXMessage.xsd "
						+ derivedNamespace + " CDS.xsd "
						+ Constants.COMPACT_NAMESPACE + " SDMXCompactData.xsd");

		// set derived namespace in sub objects
		for (DataSet d : datasets) {
			d.namespace = derivedNamespace;
			List<Series> seriesList = d.getSeries();
			for (Series s : seriesList) {
				s.namespace = derivedNamespace;
				List<Obs> obsList = s.getObs();
				for (Obs o : obsList) {
					o.namespace = derivedNamespace;
				}
			}
		}

		header.toXML(xmlWriter);
		for (DataSet d : datasets) {
			d.toXML(xmlWriter);
		}

		xmlWriter.writeEndElement();

		// validate CDS
		SDMXHDValidator.validateCDSbySchema(w.toString());

		return w.toString();
	}

	/* getters and setters */

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<DataSet> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet> datasets) {
		this.datasets = datasets;
	}

}
