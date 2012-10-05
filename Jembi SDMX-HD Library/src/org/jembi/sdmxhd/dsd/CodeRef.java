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
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a CodeRef element in a SDMX-HD DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class CodeRef implements XMLWritable {

	@XMLElement(name = "URN", namespace = Constants.STRUCTURE_NAMESPACE)
	private String urn;

	@XMLElement(name = "CodelistAliasRef", namespace = Constants.STRUCTURE_NAMESPACE)
	private String codelistAliasRef;

	@XMLElement(name = "CodeID", namespace = Constants.STRUCTURE_NAMESPACE)
	private String codeID;

	@XMLElement
	private List<CodeRef> children = new ArrayList<CodeRef>();

	// TODO LevelRef

	@XMLElement(name = "NodeAliasID", namespace = Constants.STRUCTURE_NAMESPACE)
	private String nodeAliasID;

	@XMLElement(name = "Version", namespace = Constants.STRUCTURE_NAMESPACE)
	private String version;

	@XMLElement(name = "ValidFrom", namespace = Constants.STRUCTURE_NAMESPACE)
	private String validFrom;

	@XMLElement(name = "ValidTo", namespace = Constants.STRUCTURE_NAMESPACE)
	private String validTo;

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/**
	 * Recursively finds the CodeRef in this CodeRefs children that has the
	 * specified codelistAliasRef and codeValue
	 * 
	 * @param codelistAliasRef
	 * @param codeValue
	 * @return the found CodeRef
	 */
	public CodeRef findCodeRef(String codelistAliasRef, String codeValue) {
		if (this.codelistAliasRef.equalsIgnoreCase(codelistAliasRef)
				&& this.codeID.equalsIgnoreCase(codeValue)) {
			return this;
		} else {
			if (children != null) {
				for (CodeRef childCodeRef : children) {
					CodeRef foundCodeRef = childCodeRef.findCodeRef(
							codelistAliasRef, codeValue);
					if (foundCodeRef != null) {
						return foundCodeRef;
					}
				}
			}
		}
		return null;
	}

	/* getters and setters */

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getCodelistAliasRef() {
		return codelistAliasRef;
	}

	public void setCodelistAliasRef(String codelistAliasRef) {
		this.codelistAliasRef = codelistAliasRef;
	}

	public String getCodeID() {
		return codeID;
	}

	public void setCodeID(String codeID) {
		this.codeID = codeID;
	}

	public List<CodeRef> getChildren() {
		return children;
	}

	public void setChildren(List<CodeRef> children) {
		this.children = children;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNodeAliasID() {
		return nodeAliasID;
	}

	public void setNodeAliasID(String nodeAliasID) {
		this.nodeAliasID = nodeAliasID;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

}
