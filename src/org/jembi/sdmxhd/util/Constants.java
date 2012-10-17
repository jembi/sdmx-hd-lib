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

public class Constants {
	// Namespace constants
	public static final String STRUCTURE_NAMESPACE = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure";
	public static final String COMPACT_NAMESPACE = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/compact";
	public static final String DEFAULT_NAMESPACE = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message";
	public static final String CROSS_NAMESPACE = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/cross";
	public static final String XML_SCHEMA_INSTANCE_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

	public static final String DERIVED_NAMESPACE_PREFIX = "urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=";

	// Zip file paths
	public static final String DSD_PATH = "DSD.xml";
	public static final String CDS_PATH = "Data_CDS.xml";
	public static final String CSDS_PATH = "Data_CROSS.xml";
	public static final String MSD_PATH = "MSD.xml";

	// Element Names
	public static final String CONCEPTS = "Concepts";
	public static final String CODELISTS = "CodeLists";
	public static final String HIERARCHICAL_CODELISTS = "HierarchicalCodelists";
	public static final String KEY_FAMILIES = "KeyFamilies";
	public static final String STRUCTURE = "Structure";
	public static final String CROSS_SECTIONAL_DATA = "CrossSectionalData";

	public static final String INDICATOR_DISAGGREGATION_HIERARCHY = "INDICATOR_DISAGGREGATION_HIERARCHY";
	public static final String INDICATOR_DISAGGREGATION_HIERARCHY_BACKWARDS_COMPATIBLE = "HY_INDICATOR_DISAGGREGATION";
	
	public static final String HCL_CONFIGURATION_HIERARCHIES = "CODELIST_HIERARCHY";
	public static final String HCL_CONFIGURATION_HIERARCHIES_BACKWARDS_COMPATIBLE = "HCL_CONFIGURATION_HIERARCHIES";
	
	public static final String INDICATOR_CODELIST = "CL_INDICATOR";
	public static final String INDICATOR_DIMENSION = "INDICATOR";
	public static final String DATAELEMENT_DIMENSION = "DATAELEMENT";

}
