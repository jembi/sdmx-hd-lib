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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.bind.ValidationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.jembi.sdmxhd.SDMXHDMessage;
import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.dsd.KeyFamily;
import org.jembi.sdmxhd.parser.cds.CDSParser;
import org.jembi.sdmxhd.parser.dsd.DSDParser;
import org.jembi.sdmxhd.parser.exceptions.ExternalRefrenceNotFoundException;
import org.jembi.sdmxhd.parser.exceptions.SchemaValidationException;
import org.jembi.sdmxhd.parser.msd.MSDParser;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.validation.SDMXHDValidator;

/**
 * This class is responsible for Parsing an entire SDMX-HD message.
 * 
 * It splits the message up and delegates each part to parsers that can handle
 * that part.
 */
public class SDMXHDParser {

	/**
	 * Parses the SMDX-HD message contained in the ZipFile and return a SDMX-HD
	 * message object that represents the SDMX-HD message supplied.
	 * 
	 * @param zf
	 * @return a representation of the SDMX-HD message supplied.
	 * @throws XMLStreamException
	 * @throws ZipException
	 * @throws IOException
	 * @throws ExternalRefrenceNotFoundException
	 * @throws SchemaValidationException
	 * @throws ValidationException
	 */
	public SDMXHDMessage parse(ZipFile zf) throws XMLStreamException,
			ZipException, IOException, ExternalRefrenceNotFoundException,
			SchemaValidationException, ValidationException {
		// construct empty SDMXMessage
		SDMXHDMessage msg = new SDMXHDMessage();

		// setup StAX to steam XML
		XMLInputFactory factory = XMLInputFactory.newInstance();

		// read DSD
		ZipEntry ze = zf.getEntry(Constants.DSD_PATH);
		if (ze != null) {

			// validate DSD
			SDMXHDValidator.validateDSDbySchema(zf, ze);

			// parse DSD
			InputStream dsdStream = zf.getInputStream(ze);
			dsdStream = zf.getInputStream(ze);
			XMLEventReader eventReader = factory
					.createXMLEventReader(dsdStream);

			DSDParser dsdParser = new DSDParser();
			msg.setDsd(dsdParser.parse(eventReader, zf));
			eventReader.close();
		}

		// read MSD
		ze = zf.getEntry(Constants.MSD_PATH);
		if (ze != null) {
			// validate MSD
			SDMXHDValidator.validateMSDbySchema(zf, ze);

			// parse MSD
			InputStream msdStream = zf.getInputStream(ze);
			msdStream = zf.getInputStream(ze);
			XMLEventReader eventReader = factory
					.createXMLEventReader(msdStream);
			MSDParser msdParser = new MSDParser();
			msg.setMsd(msdParser.parse(eventReader, zf));

			eventReader.close();
		}

		// read CDS
		ze = zf.getEntry(Constants.CDS_PATH);
		if (ze != null) {
			// validate CDS
			SDMXHDValidator.validateCDSbySchema(zf, ze);

			// generate schema uri
			DSD dsd = msg.getDsd();
			if (dsd != null) {
				KeyFamily keyFamily = dsd.getKeyFamilies().get(0);

				String derivedNamespace = Constants.DERIVED_NAMESPACE_PREFIX
						+ keyFamily.getAgencyID() + ":" + keyFamily.getId()
						+ ":" + keyFamily.getVersion() + ":compact";

				// parse CDS
				InputStream cdsStream = zf.getInputStream(ze);
				cdsStream = zf.getInputStream(ze);
				XMLEventReader eventReader = factory
						.createXMLEventReader(cdsStream);

				CDSParser cdsParser = new CDSParser();
				msg.setCds(cdsParser.parse(eventReader, zf, derivedNamespace));
				eventReader.close();
			}
		}

		// TODO complete the following:
		/*
		 * //read CSDS ze = zf.getEntry(Constants.CSDS_PATH); if (ze != null) {
		 * // validate CDS SDMXHDValidator.validateCSDSbySchema(zf, ze);
		 * 
		 * // generate schema uri DSD dsd = msg.getDsd(); if (dsd != null) {
		 * KeyFamily keyFamily = dsd.getKeyFamilies().get(0);
		 * 
		 * String derivedNamespace = Constants.DERIVED_NAMESPACE_PREFIX +
		 * keyFamily.getAgencyID() + ":" + keyFamily.getId() + ":" +
		 * keyFamily.getVersion() + ":cross";
		 * 
		 * // parse CSDS InputStream csdsStream = zf.getInputStream(ze);
		 * csdsStream = zf.getInputStream(ze); XMLEventReader eventReader =
		 * factory.createXMLEventReader(csdsStream);
		 * 
		 * CSDSParser csdsParser = new CSDSParser();
		 * msg.setCds(csdsParser.parse(eventReader, zf, derivedNamespace));
		 * eventReader.close(); } }
		 */

		zf.close();
		return msg;
	}

}
