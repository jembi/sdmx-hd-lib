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
package org.jembi.sdmxhd.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.ValidationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SDMXHDValidator {

	private static final String sdmxMessageSchema = "resources/SDMXMessage.xsd";
	private static final String sdmxCompactDataSchema = "resources/SDMXCompactData.xsd";

	private static DefaultHandler handler = new Handler();

	protected static class Handler extends DefaultHandler {

		public void error(SAXParseException sAXParseException)
				throws SAXException {
			System.out.println("Error: " + sAXParseException);
		}

		public void fatalError(SAXParseException sAXParseException)
				throws SAXException {
			System.out.println("FATAL: " + sAXParseException);
		}

		public void warning(org.xml.sax.SAXParseException sAXParseException)
				throws org.xml.sax.SAXException {
			System.out.println("Warning: " + sAXParseException);
		}

	}

	protected static class Resolver implements LSResourceResolver {

		public org.w3c.dom.ls.LSInput resolveResource(String str, String str1,
				String str2, String str3, String str4) {
			return null;

		}

	}

	public static void validateDSDbySchema(ZipFile zf, ZipEntry ze)
			throws IOException, ValidationException {
		InputStream dsdStream = zf.getInputStream(ze);
		try {
			validateSchema(dsdStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate DSD", e);
		}
	}

	public static void validateDSDbySchema(String dsdXML) throws IOException,
			ValidationException {
		InputStream dsdStream = new ByteArrayInputStream(dsdXML.getBytes());
		try {
			validateSchema(dsdStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate DSD", e);
		}
	}

	public static void validateCDSbySchema(ZipFile zf, ZipEntry ze)
			throws IOException, ValidationException {
		try {
			// TODO validate using CDS.xsd as well

			InputStream cdsStream = zf.getInputStream(ze);
			validateSchema(cdsStream, sdmxMessageSchema);

			cdsStream = zf.getInputStream(ze);
			validateSchema(cdsStream, sdmxCompactDataSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate CDS", e);
		}
	}

	public static void validateCDSbySchema(String cdsXML) throws IOException,
			ValidationException {
		try {
			// TODO validate using CDS.xsd as well

			InputStream cdsStream = new ByteArrayInputStream(cdsXML.getBytes());
			validateSchema(cdsStream, sdmxMessageSchema);

			cdsStream = new ByteArrayInputStream(cdsXML.getBytes());
			validateSchema(cdsStream, sdmxCompactDataSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate CDS", e);
		}
	}

	public static void validateMSDbySchema(ZipFile zf, ZipEntry ze)
			throws IOException, ValidationException {
		InputStream msdStream = zf.getInputStream(ze);
		try {
			validateSchema(msdStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate MSD", e);
		}
	}

	public static void validateMSDbySchema(String msdXML) throws IOException,
			ValidationException {
		InputStream msdStream = new ByteArrayInputStream(msdXML.getBytes());
		try {
			validateSchema(msdStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate MSD", e);
		}
	}

	public static void validateGMSbySchema(ZipFile zf, ZipEntry ze)
			throws IOException, ValidationException {
		InputStream gmsStream = zf.getInputStream(ze);
		try {
			validateSchema(gmsStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate GMS", e);
		}
	}

	public static void validateGMSbySchema(String gmsXML) throws IOException,
			ValidationException {
		InputStream gmsStream = new ByteArrayInputStream(gmsXML.getBytes());
		try {
			validateSchema(gmsStream, sdmxMessageSchema);
		} catch (SAXException e) {
			throw new ValidationException("Failed to validate GMS", e);
		}
	}

	private static void validateSchema(InputStream inputStream,
			String schemaPath) throws SAXException, IOException {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		schemaFactory.setErrorHandler(handler);
		// create a grammar object.
		Schema schemaGrammar = schemaFactory.newSchema(new File(schemaPath));

		Resolver resolver = new Resolver();
		// create a validator to validate against grammar sch.
		Validator schemaValidator = schemaGrammar.newValidator();
		schemaValidator.setResourceResolver(resolver);
		schemaValidator.setErrorHandler(handler);

		// validate xml instance against the grammar.
		schemaValidator.validate(new StreamSource(inputStream));
	}

	public static void validateCSDSbySchema(String string) {
		// TODO Auto-generated method stub

	}

	public static DefaultHandler getHandler() {
		return handler;
	}

	public static void setHandler(DefaultHandler handler) {
		SDMXHDValidator.handler = handler;
	}

}
