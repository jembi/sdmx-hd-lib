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
package org.jembi.sdmxhd.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * This class allow Java Bean object that conform to the following specification
 * to be written to XML. It uses reflection to look at the class of the object
 * to be written to XML and find what field are XML attributes and what are XML
 * elements. It uses this information to then write the java bean to XML.
 * Complex object contained within the java bean have their own toXML() methods
 * called to write them to XML.
 * <p>
 * The bean must be annotated as follow for then to be written to XML:
 * <ul>
 * <li>Each field that must be written as an XML attribute must be annotated
 * with the XMLAttribute annotation.
 * 
 * <li>Each field that must be written as an XML element must be annotated with
 * the XMLElement annotation.
 * 
 * <li>The class may be annotated by the XMLNamespace annotation if a specific
 * namespace must be used.
 * </ul>
 * 
 * @author Ryan Crichton
 */
public class XMLBuilder {

	/**
	 * Writes the specified object to XML
	 * 
	 * @param bean
	 *            the object to be written
	 * @param xmlWriter
	 *            the xmlWriter to write the XML to.
	 * @throws XMLStreamException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void writeBeanToXML(XMLWritable bean,
			XMLStreamWriter xmlWriter) throws XMLStreamException,
			IllegalArgumentException, IllegalAccessException {
		writeBeanToXML(bean, xmlWriter, null, null);
	}

	/**
	 * Writes the specified object to XML
	 * 
	 * @param bean
	 *            the object to be written
	 * @param xmlWriter
	 *            the xmlWriter to write the XML to.
	 * @throws XMLStreamException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void writeBeanToXML(XMLWritable bean,
			XMLStreamWriter xmlWriter, String namespace)
			throws XMLStreamException, IllegalArgumentException,
			IllegalAccessException {
		writeBeanToXML(bean, xmlWriter, namespace, null);
	}

	/**
	 * Writes the specified object to XML
	 * 
	 * @param bean
	 *            the object to be written
	 * @param xmlWriter
	 *            the xmlWriter to write the XML to.
	 * @throws XMLStreamException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void writeBeanToXML(XMLWritable bean,
			XMLStreamWriter xmlWriter, String namespace, String name)
			throws XMLStreamException, IllegalArgumentException,
			IllegalAccessException {
		if (namespace == null) {
			// get object namespace if available
			XMLNamespace xmlNamespaceAnno = bean.getClass().getAnnotation(
					XMLNamespace.class);
			if (xmlNamespaceAnno != null) {
				namespace = xmlNamespaceAnno.value();
			}
		}

		if (name == null) {
			name = bean.getClass().getSimpleName();
		}

		if (namespace != null) {
			xmlWriter.writeStartElement(namespace, name);
		} else {
			xmlWriter.writeStartElement(name);
		}

		// write all attributes first...
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(XMLAttribute.class)) {
				f.setAccessible(true);
				// if field is a Map
				if (Map.class.isAssignableFrom(f.getType())) {
					Map<?, ?> m = (Map<?, ?>) f.get(bean);
					for (Object key : m.keySet()) {
						xmlWriter.writeAttribute(key.toString(), m.get(key)
								.toString());
					}
				}
				// otherwise
				else {
					// get attribute name
					XMLAttribute annotation = f
							.getAnnotation(XMLAttribute.class);
					String attrName = annotation.value();
					if (attrName.equals("") || attrName == null) {
						attrName = f.getName();
					}
					// write attribute to xml element
					Object o = f.get(bean);
					if (o != null) {
						xmlWriter.writeAttribute(attrName, o.toString());
					}
				}
			}
		}

		// ...then write all elements
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(XMLElement.class)) {
				// get element name
				XMLElement annotation = f.getAnnotation(XMLElement.class);
				String elementName = annotation.name();
				String elementNamespace = annotation.namespace();

				// write element to xml inside the parent element
				f.setAccessible(true);
				Object o = f.get(bean);
				if (o != null) {
					boolean isCollection = false;
					if (o instanceof Collection) {
						isCollection = true;
					}

					// if field is a primitive
					if (f.getType().isPrimitive()
							|| f.getType() == String.class) {
						xmlWriter.writeStartElement(elementNamespace,
								elementName);
						xmlWriter.writeCharacters(o.toString());
						xmlWriter.writeEndElement();
					}
					// if field is a LocalizedString
					else if (f.getType() == LocalizedString.class) {
						LocalizedString l = (LocalizedString) o;
						writeLocalizedStringElement(xmlWriter, elementName,
								elementNamespace, l);
					}
					// if field is a Collection
					else if (isCollection) {
						Collection<?> c = (Collection<?>) o;
						for (Object element : c) {
							// object is primitive or string
							if (element.getClass().isPrimitive()
									|| element.getClass().equals(String.class)) {
								xmlWriter.writeStartElement(elementNamespace,
										elementName);
								xmlWriter.writeCharacters(element.toString());
								xmlWriter.writeEndElement();
							}
							// object is a LocalizedString
							else if (element.getClass().equals(
									LocalizedString.class)) {
								LocalizedString l = (LocalizedString) element;
								writeLocalizedStringElement(xmlWriter,
										elementName, elementNamespace, l);
							}
							// else, it must be a complex object
							else {
								Class<?> paramaterType = XMLStreamWriter.class;
								try {
									Method toXMLMethod = element.getClass()
											.getMethod("toXML", paramaterType);
									toXMLMethod.invoke(element, xmlWriter);
								} catch (Exception e) {
									throw new IllegalArgumentException(e);
								}
							}
						}
					}
					// if field is any other object
					else {
						try {
							Class<?> paramaterType = XMLStreamWriter.class;
							o.getClass().getMethod("toXML", paramaterType)
									.invoke(o, xmlWriter);
						} catch (Exception e) {
							throw new IllegalArgumentException(e);
						}
					}
				}
			}
		}
		xmlWriter.writeEndElement();
	}

	private static void writeLocalizedStringElement(XMLStreamWriter xmlWriter,
			String elementName, String elementNamespace,
			LocalizedString localizedString) throws XMLStreamException {
		Map<String, String> localizedStrings = localizedString
				.getLocalizedStrings();
		if (localizedStrings.size() < 1) {
			if (localizedString.getDefaultStr() != null) {
				xmlWriter.writeStartElement(elementNamespace, elementName);
				xmlWriter.writeCharacters(localizedString.getDefaultStr());
				xmlWriter.writeEndElement();
			}
		} else {
			for (String langCode : localizedStrings.keySet()) {
				xmlWriter.writeStartElement(elementNamespace, elementName);
				xmlWriter.writeAttribute("xml:lang", langCode);
				xmlWriter.writeCharacters(localizedStrings.get(langCode));
				xmlWriter.writeEndElement();
			}
		}
	}

}
