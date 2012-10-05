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

import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.XMLWritable;
import org.jembi.sdmxhd.primitives.TextFormat;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.util.XMLBuilder;
import org.jembi.sdmxhd.util.annotations.XMLAttribute;
import org.jembi.sdmxhd.util.annotations.XMLElement;
import org.jembi.sdmxhd.util.annotations.XMLNamespace;

/**
 * @author Ryan Crichton
 * 
 *         This object represents the TimeDimension element in a SDMX-HD DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class TimeDimension implements XMLWritable {

	@XMLAttribute("conceptRef")
	private String conceptRef;
	@XMLAttribute("conceptVersion")
	private String conceptVersion;
	@XMLAttribute("conceptAgency")
	private String conceptAgency;
	@XMLAttribute("conceptSchemeRef")
	private String conceptSchemeRef;
	@XMLAttribute("conceptSchemeAgency")
	private String conceptSchemeAgency;
	@XMLAttribute("codelist")
	private String codelist;
	@XMLAttribute("codelistVersion")
	private String codelistVersion;
	@XMLAttribute("codelistAgency")
	private String codelistAgency;
	@XMLAttribute("crossSectionalAttachDataSet")
	private boolean crossSectionalAttachDataSet;
	@XMLAttribute("crossSectionalAttachGroup")
	private boolean crossSectionalAttachGroup;
	@XMLAttribute("crossSectionalAttachSection")
	private boolean crossSectionalAttachSection;
	@XMLAttribute("crossSectionalAttachObservation")
	private boolean crossSectionalAttachObservation;

	@XMLElement
	private TextFormat textFormat;

	public void toXML(XMLStreamWriter xmlWriter) throws Exception {
		XMLBuilder.writeBeanToXML(this, xmlWriter);
	}

	/* getters and setters */

	public TextFormat getTextFormat() {
		return textFormat;
	}

	public void setTextFormat(TextFormat textFormat) {
		this.textFormat = textFormat;
	}

	public String getConceptRef() {
		return conceptRef;
	}

	public void setConceptRef(String conceptRef) {
		this.conceptRef = conceptRef;
	}

	public String getConceptVersion() {
		return conceptVersion;
	}

	public void setConceptVersion(String conceptVersion) {
		this.conceptVersion = conceptVersion;
	}

	public String getConceptAgency() {
		return conceptAgency;
	}

	public void setConceptAgency(String conceptAgency) {
		this.conceptAgency = conceptAgency;
	}

	public String getConceptSchemeRef() {
		return conceptSchemeRef;
	}

	public void setConceptSchemeRef(String conceptSchemeRef) {
		this.conceptSchemeRef = conceptSchemeRef;
	}

	public String getConceptSchemeAgency() {
		return conceptSchemeAgency;
	}

	public void setConceptSchemeAgency(String conceptSchemeAgency) {
		this.conceptSchemeAgency = conceptSchemeAgency;
	}

	public String getCodelist() {
		return codelist;
	}

	public void setCodelist(String codelist) {
		this.codelist = codelist;
	}

	public String getCodelistVersion() {
		return codelistVersion;
	}

	public void setCodelistVersion(String codelistVersion) {
		this.codelistVersion = codelistVersion;
	}

	public String getCodelistAgency() {
		return codelistAgency;
	}

	public void setCodelistAgency(String codelistAgency) {
		this.codelistAgency = codelistAgency;
	}

	public boolean isCrossSectionalAttachDataSet() {
		return crossSectionalAttachDataSet;
	}

	public void setCrossSectionalAttachDataSet(
			boolean crossSectionalAttachDataSet) {
		this.crossSectionalAttachDataSet = crossSectionalAttachDataSet;
	}

	public boolean isCrossSectionalAttachGroup() {
		return crossSectionalAttachGroup;
	}

	public void setCrossSectionalAttachGroup(boolean crossSectionalAttachGroup) {
		this.crossSectionalAttachGroup = crossSectionalAttachGroup;
	}

	public boolean isCrossSectionalAttachSection() {
		return crossSectionalAttachSection;
	}

	public void setCrossSectionalAttachSection(
			boolean crossSectionalAttachSection) {
		this.crossSectionalAttachSection = crossSectionalAttachSection;
	}

	public boolean isCrossSectionalAttachObservation() {
		return crossSectionalAttachObservation;
	}

	public void setCrossSectionalAttachObservation(
			boolean crossSectionalAttachObservation) {
		this.crossSectionalAttachObservation = crossSectionalAttachObservation;
	}

}
