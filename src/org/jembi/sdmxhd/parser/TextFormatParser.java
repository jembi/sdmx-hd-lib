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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.jembi.sdmxhd.primitives.TextFormat;

public class TextFormatParser {

	public TextFormat parse(XMLEventReader eventReader, StartElement se,
			ZipFile zipFile) {
		TextFormat textFormat = new TextFormat();

		// optional attributes
		Attribute a = se.getAttributeByName(new QName("textType"));
		if (a != null) {
			textFormat.setTextType(a.getValue());
		}
		a = se.getAttributeByName(new QName("isSequence"));
		if (a != null) {
			textFormat.setSequence(Boolean.parseBoolean(a.getValue()));
		}
		a = se.getAttributeByName(new QName("minLength"));
		if (a != null) {
			textFormat.setMinLength(Integer.parseInt(a.getValue()));
		}
		a = se.getAttributeByName(new QName("maxLength"));
		if (a != null) {
			textFormat.setMaxLength(Integer.parseInt(a.getValue()));
		}
		a = se.getAttributeByName(new QName("startValue"));
		if (a != null) {
			textFormat.setStartValue(Double.parseDouble(a.getValue()));
		}
		a = se.getAttributeByName(new QName("endValue"));
		if (a != null) {
			textFormat.setEndValue(Double.parseDouble(a.getValue()));
		}
		a = se.getAttributeByName(new QName("interval"));
		if (a != null) {
			textFormat.setInterval(Double.parseDouble(a.getValue()));
		}
		a = se.getAttributeByName(new QName("timeInterval"));
		if (a != null) {
			textFormat.setTimeInterval(Double.parseDouble(a.getValue()));
		}
		a = se.getAttributeByName(new QName("decimals"));
		if (a != null) {
			textFormat.setDecimals(Integer.parseInt(a.getValue()));
		}
		a = se.getAttributeByName(new QName("pattern"));
		if (a != null) {
			textFormat.setPattern(a.getValue());
		}

		return textFormat;
	}

}
