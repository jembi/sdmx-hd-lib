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
 * This object represents a Attribute element in a SDMX-HD DSD
 */
@XMLNamespace(Constants.STRUCTURE_NAMESPACE)
public class Attribute implements XMLWritable {

	public static final String SERIES_ATTACHMENT_LEVEL = "Series";
	public static final String OBSERVATION_ATTACHMENT_LEVEL = "Observation";
	public static final String GROUP_ATTACHMENT_LEVEL = "Group";
	public static final String DATASET_ATTACHMENT_LEVEL = "Dataset";

	public static final String MANDATORY = "Mandatory";
	public static final String CONDITIONAL = "Conditional";

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
	@XMLAttribute("attachmentLevel")
	private String attachmentLevel;
	@XMLAttribute("assignmentStatus")
	private String assignmentStatus;
	@XMLAttribute("isTimeFormat")
	private boolean isTimeFormat = false;
	@XMLAttribute("crossSectionalAttachDataSet")
	private boolean crossSectionalAttachDataSet;
	@XMLAttribute("crossSectionalAttachGroup")
	private boolean crossSectionalAttachGroup;
	@XMLAttribute("crossSectionalAttachSection")
	private boolean crossSectionalAttachSection;
	@XMLAttribute("crossSectionalAttachObservation")
	private boolean crossSectionalAttachObservation;
	@XMLAttribute("isEntityAttribute")
	private boolean isEntityAttribute = false;
	@XMLAttribute("isNonObservationalTimeAttribute")
	private boolean isNonObservationalTimeAttribute = false;
	@XMLAttribute("isCountAttribute")
	private boolean isCountAttribute = false;
	@XMLAttribute("isFrequencyAttribute")
	private boolean isFrequencyAttribute = false;

	@XMLElement
	private TextFormat textFormat;

	@XMLElement(name = "AttachmentGroup", namespace = Constants.STRUCTURE_NAMESPACE)
	private String attachmentGroup;

	@XMLElement(name = "AttachmentMeasure", namespace = Constants.STRUCTURE_NAMESPACE)
	private String attachmentMeasure;

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

	public String getAttachmentGroup() {
		return attachmentGroup;
	}

	public void setAttachmentGroup(String attachmentGroup) {
		this.attachmentGroup = attachmentGroup;
	}

	public String getAttachmentMeasure() {
		return attachmentMeasure;
	}

	public void setAttachmentMeasure(String attachmentMeasure) {
		this.attachmentMeasure = attachmentMeasure;
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

	public String getAttachmentLevel() {
		return attachmentLevel;
	}

	public void setAttachmentLevel(String attachmentLevel) {
		this.attachmentLevel = attachmentLevel;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public boolean isTimeFormat() {
		return isTimeFormat;
	}

	public void setTimeFormat(boolean isTimeFormat) {
		this.isTimeFormat = isTimeFormat;
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

	public boolean isEntityAttribute() {
		return isEntityAttribute;
	}

	public void setEntityAttribute(boolean isEntityAttribute) {
		this.isEntityAttribute = isEntityAttribute;
	}

	public boolean isNonObservationalTimeAttribute() {
		return isNonObservationalTimeAttribute;
	}

	public void setNonObservationalTimeAttribute(
			boolean isNonObservationalTimeAttribute) {
		this.isNonObservationalTimeAttribute = isNonObservationalTimeAttribute;
	}

	public boolean isCountAttribute() {
		return isCountAttribute;
	}

	public void setCountAttribute(boolean isCountAttribute) {
		this.isCountAttribute = isCountAttribute;
	}

	public boolean isFrequencyAttribute() {
		return isFrequencyAttribute;
	}

	public void setFrequencyAttribute(boolean isFrequencyAttribute) {
		this.isFrequencyAttribute = isFrequencyAttribute;
	}

}
