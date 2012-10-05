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
package org.jembi.sdmxhd.dsd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * @author Ryan Crichton
 * 
 *         This object represents the Group element in a SDMX-HD DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class Group implements XMLWritable {

	@XMLAttribute("id")
	private String id;

	@XMLElement(name = "DimensionRef", namespace = Constants.STRUCTURE_NAMESPACE)
	private List<String> dimensionRefs = new ArrayList<String>();

	@XMLElement(name = "AttachmentConstraintRef", namespace = Constants.STRUCTURE_NAMESPACE)
	private String attachmentConstraintRef;

	@XMLElement(name = "Description", namespace = Constants.STRUCTURE_NAMESPACE)
	private String description;

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/* getters and setters */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttachmentConstraintRef() {
		return attachmentConstraintRef;
	}

	public void setAttachmentConstraintRef(String attachmentConstraintRef) {
		this.attachmentConstraintRef = attachmentConstraintRef;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getDimensionRefs() {
		return dimensionRefs;
	}

	public void setDimensionRefs(List<String> dimensionRefs) {
		this.dimensionRefs = dimensionRefs;
	}

}
