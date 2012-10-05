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

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jembi.sdmxhd.convenience.DimensionWrapper;
import org.jembi.sdmxhd.header.Header;
import org.jembi.sdmxhd.primitives.Code;
import org.jembi.sdmxhd.primitives.CodeList;
import org.jembi.sdmxhd.primitives.ConceptScheme;
import org.jembi.sdmxhd.primitives.LocalizedString;
import org.jembi.sdmxhd.util.Constants;
import org.jembi.sdmxhd.validation.SDMXHDValidator;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a SDMX-HD DataSet Definition or DSD
 */
public class DSD {

	private Header header = null;
	private List<ConceptScheme> conceptSchemes = null;
	private List<CodeList> codeLists = null;
	private List<HierarchicalCodelist> hierarchicalCodelists = null;
	private List<KeyFamily> keyFamilies = null;
	private List<OrganisationScheme> organisationSchemes = null;

	public String toXML() throws Exception {
		Writer w = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		// factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,Boolean.TRUE);

		XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(w);

		xmlWriter.writeStartDocument();

		xmlWriter.setDefaultNamespace(Constants.DEFAULT_NAMESPACE);
		xmlWriter.setPrefix("structure", Constants.STRUCTURE_NAMESPACE);

		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE,
				Constants.STRUCTURE);
		xmlWriter.writeAttribute("xmlns:xsi",
				Constants.XML_SCHEMA_INSTANCE_NAMESPACE);
		xmlWriter.writeAttribute("xsi:schemaLocation",
				Constants.DEFAULT_NAMESPACE + " SDMXMessage.xsd");
		xmlWriter.writeAttribute("xmlns", Constants.DEFAULT_NAMESPACE);
		xmlWriter.writeAttribute("xmlns:structure",
				Constants.STRUCTURE_NAMESPACE);

		// write header to XML
		header.toXML(xmlWriter);

		// write conceptSchemes to XML
		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE,
				Constants.CONCEPTS);
		for (ConceptScheme cs : conceptSchemes) {
			cs.toXML(xmlWriter);
		}
		xmlWriter.writeEndElement();

		// write codeLists to XML
		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE,
				Constants.CODELISTS);
		for (CodeList cl : codeLists) {
			cl.toXML(xmlWriter);
		}
		xmlWriter.writeEndElement();

		// write hierarchicalCodelists to XML
		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE,
				Constants.HIERARCHICAL_CODELISTS);
		for (HierarchicalCodelist hcl : hierarchicalCodelists) {
			hcl.toXML(xmlWriter);
		}
		xmlWriter.writeEndElement();

		// write keyFamilies to XML
		xmlWriter.writeStartElement(Constants.DEFAULT_NAMESPACE,
				Constants.KEY_FAMILIES);
		for (KeyFamily kf : keyFamilies) {
			kf.toXML(xmlWriter);
		}
		xmlWriter.writeEndElement();

		xmlWriter.writeEndElement();

		// validate DSD
		SDMXHDValidator.validateDSDbySchema(w.toString());

		return w.toString();
	}

	/**
	 * Convenience method to return a list of all the names of the indicators in
	 * this DSD for the given keyFamily.
	 * 
	 * @return the list of the Indicators names
	 */
	public Set<LocalizedString> getIndicatorNames(String keyFamilyId) {
		Set<LocalizedString> indicatorNames = new HashSet<LocalizedString>();

		Dimension dimension = getDimension(Constants.INDICATOR_DIMENSION,
				keyFamilyId);
		if (dimension == null) {
			dimension = getDimension(Constants.DATAELEMENT_DIMENSION,
					keyFamilyId);
		}

		if (dimension != null) {
			CodeList codeList = getCodeList(dimension.getCodelistRef());
			for (Code code : codeList.getCodes()) {
				indicatorNames.add(code.getDescription());
			}
		}

		return indicatorNames;
	}

	/**
	 * Convenience method to return a list of all the dimensions in this DSD.
	 * 
	 * @return the list of all dimensions contained in this DSD
	 */
	public List<Dimension> getAllDimensions() {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		for (KeyFamily keyFamily : keyFamilies) {
			dimensions.addAll(keyFamily.getComponents().getDimensions());
		}
		return dimensions;
	}

	/**
	 * Convenience method to return a list of all the dimensions in this DSD for
	 * a particular keyFamily that are related to indicators. This means that
	 * there are referenced in the INDICATOR_DISAGGREGATION_HIERARCHY by an
	 * indicator.
	 * 
	 * @return the list of all dimensions contained in this DSD that are related
	 *         to indicators
	 */
	public List<Dimension> getAllIndicatorDimensions(String keyFamilyId) {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		List<String> addedDimensions = new ArrayList<String>();

		for (LocalizedString indicatorName : getIndicatorNames(keyFamilyId)) {
			List<DimensionWrapper> dimensionHierarchy = getDimensionHierarchy(
					indicatorName.getDefaultStr(), keyFamilyId);
			if (dimensionHierarchy != null) {
				for (DimensionWrapper dw : dimensionHierarchy) {
					List<Dimension> hierarchyDimensions = dw.getAllDimensions();
					for (Dimension d : hierarchyDimensions) {
						if (!addedDimensions.contains(d.getConceptRef())) {
							dimensions.add(d);
							addedDimensions.add(d.getConceptRef());
						}
					}
				}
			}
		}

		return dimensions;
	}

	/**
	 * Convenience method to return a list of all the dimensions in this DSD
	 * that are user specified.
	 * 
	 * @return the list of all dimensions contained in this DSD that are
	 *         non-standard.
	 */
	public List<Dimension> getAllDimensionsForIndicator(
			String sdmxhdIndicatorName, String keyFamilyId) {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		List<String> addedDimensions = new ArrayList<String>();

		List<DimensionWrapper> dimensionHierarchy = getDimensionHierarchy(
				sdmxhdIndicatorName, keyFamilyId);
		if (dimensionHierarchy != null) {
			for (DimensionWrapper dw : dimensionHierarchy) {
				List<Dimension> hierarchyDimensions = dw.getAllDimensions();
				for (Dimension d : hierarchyDimensions) {
					if (!addedDimensions.contains(d.getConceptRef())) {
						dimensions.add(d);
						addedDimensions.add(d.getConceptRef());
					}
				}
			}
		}

		return dimensions;
	}

	/**
	 * Convenience method to return a list of all the dimensions in this DSD
	 * that are user specified.
	 * 
	 * @return the list of all dimensions contained in this DSD that are
	 *         non-standard.
	 */
	public List<Dimension> getAllNonStanadrdDimensions() {
		// get all dimensions
		List<Dimension> allDimensions = getAllDimensions();
		// find dimensions that shouldn't be included
		List<Dimension> excludes = new ArrayList<Dimension>();
		for (Dimension d : allDimensions) {
			if (d.isFrequencyDimension()) {
				excludes.add(d);
			}
		}

		Dimension indDim = getDimension(Constants.INDICATOR_DIMENSION);
		excludes.add(indDim);
		Dimension dataElDim = getDimension(Constants.DATAELEMENT_DIMENSION);
		excludes.add(dataElDim);

		// remove dimensions that shouldn't be included
		allDimensions.removeAll(excludes);

		return allDimensions;
	}

	/**
	 * Convenience method to return a list of all the dimensions in this DSD
	 * that are user specified.
	 * 
	 * @return the list of all dimensions contained in this DSD that are
	 *         non-standard.
	 */
	public List<Dimension> getAllNonStanadrdDimensions(String keyFamilyId) {
		KeyFamily keyFamily = getKeyFamily(keyFamilyId);
		// get all dimensions for keyfamily
		List<Dimension> allDimensions = keyFamily.getComponents()
				.getDimensions();
		// find dimensions that shouldn't be included
		List<Dimension> excludes = new ArrayList<Dimension>();
		for (Dimension d : allDimensions) {
			if (d.isFrequencyDimension()) {
				excludes.add(d);
			}
		}

		Dimension indDim = getDimension(Constants.INDICATOR_DIMENSION,
				keyFamilyId);
		excludes.add(indDim);
		Dimension dataElDim = getDimension(Constants.DATAELEMENT_DIMENSION,
				keyFamilyId);
		excludes.add(dataElDim);

		// remove dimensions that shouldn't be included
		allDimensions.removeAll(excludes);

		return allDimensions;
	}

	/**
	 * Convenience method to return a list of all the attributes in this DSD.
	 * 
	 * @return the list all attributes contained in this DSD
	 */
	public List<Attribute> getAllAttributes() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (KeyFamily keyFamily : keyFamilies) {
			attributes.addAll(keyFamily.getComponents().getAttributes());
		}
		return attributes;
	}

	/**
	 * Convenience method to return a list of all the attributes in this DSD
	 * with the supplied attachementLevel and assignmentStatus. Both parameters
	 * are nullable.
	 * 
	 * @param attachementLevel
	 *            one of "DataSet", "Group", "Series" or "Observation"
	 * @param assignmentStatus
	 *            one of "Mandatory" or "Conditional"
	 * @return list of attributes
	 */
	public List<Attribute> getAttributes(String attachementLevel,
			String assignmentStatus) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (KeyFamily keyFamily : keyFamilies) {
			List<Attribute> list = keyFamily.getComponents().getAttributes();
			for (Attribute a : list) {
				if (a.getAttachmentLevel().equalsIgnoreCase(attachementLevel)
						&& a.getAssignmentStatus().equalsIgnoreCase(
								assignmentStatus)) {
					attributes.add(a);
				}
				if (attachementLevel == null
						&& a.getAssignmentStatus().equalsIgnoreCase(
								assignmentStatus)) {
					attributes.add(a);
				}
				if (assignmentStatus == null
						&& a.getAttachmentLevel().equalsIgnoreCase(
								attachementLevel)) {
					attributes.add(a);
				}
			}
		}
		return attributes;
	}

	/**
	 * Convenience method to return a list of all the attributes in this DSD in
	 * the specified keyFamily and with the supplied attachementLevel and
	 * assignmentStatus. Both parameters are nullable.
	 * 
	 * @param attachementLevel
	 *            one of "DataSet", "Group", "Series" or "Observation"
	 * @param assignmentStatus
	 *            one of "Mandatory" or "Conditional"
	 * @return list of attributes
	 */
	public List<Attribute> getAttributes(String keyFamilyId,
			String attachementLevel, String assignmentStatus) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Attribute> list = getKeyFamily(keyFamilyId).getComponents()
				.getAttributes();
		for (Attribute a : list) {
			if (a.getAttachmentLevel().equalsIgnoreCase(attachementLevel)
					&& a.getAssignmentStatus().equalsIgnoreCase(
							assignmentStatus)) {
				attributes.add(a);
			}
			if (attachementLevel == null
					&& a.getAssignmentStatus().equalsIgnoreCase(
							assignmentStatus)) {
				attributes.add(a);
			}
			if (assignmentStatus == null
					&& a.getAttachmentLevel()
							.equalsIgnoreCase(attachementLevel)) {
				attributes.add(a);
			}
		}
		return attributes;
	}

	/**
	 * Convenience method to return a list of disaggregation dimensions for a
	 * particular keyFamily using the given indicator name.
	 * 
	 * @param indicatorName
	 * @return the list of dimensions for the indicator or an empty list if
	 *         there is no disaggregation
	 */
	public List<DimensionWrapper> getDimensionHierarchy(String indicatorName,
			String keyFamilyId) {
		if (hierarchicalCodelists == null || hierarchicalCodelists.size() <= 0) {
			return null;
		}

		Dimension indDim = getIndicatorOrDataElementDimension(keyFamilyId);
		CodeList codelist = getCodeList(indDim.getCodelistRef());

		Code code = codelist.getCodeByDescription(indicatorName);

		if (code == null) {
			return null;
		}

		HierarchicalCodelist hierarchicalCodelist = getHierarchicalCodeList(Constants.HCL_CONFIGURATION_HIERARCHIES);

		Hierarchy hierarchy = hierarchicalCodelist
				.getHierarchy(Constants.INDICATOR_DISAGGREGATION_HIERARCHY);

		if (hierarchy == null) {
			return null;
		}

		CodeRef codeRef = hierarchy.findCodeRef(hierarchicalCodelist
				.getCodeListAlias(indDim.getCodelistRef()), code.getValue());

		if (codeRef == null) {
			return null;
		}

		// convert the codeRef to a DimensionWrapper list for ease of use
		List<DimensionWrapper> dimensionHierarchy;
		if (codeRef != null) {
			dimensionHierarchy = constructDimensionHierarchy(codeRef);
		} else {
			dimensionHierarchy = new ArrayList<DimensionWrapper>();
		}

		return dimensionHierarchy;
	}

	public HierarchicalCodelist getHierarchicalCodeList(
			String hierarchicalCodelistId) {
		for (Iterator<HierarchicalCodelist> iterator = hierarchicalCodelists
				.iterator(); iterator.hasNext();) {
			HierarchicalCodelist hcl = iterator.next();
			if (hcl.getId().equals(hierarchicalCodelistId)) {
				return hcl;
			}
		}
		return null;
	}

	public Dimension getIndicatorOrDataElementDimension(String keyFamilyId) {
		Dimension dimension = getDimension(Constants.INDICATOR_DIMENSION,
				keyFamilyId);
		if (dimension == null) {
			dimension = getDimension(Constants.DATAELEMENT_DIMENSION,
					keyFamilyId);
		}
		return dimension;
	}

	private List<DimensionWrapper> constructDimensionHierarchy(CodeRef codeRef) {
		if (codeRef.getChildren() != null) {
			List<DimensionWrapper> dimensionHierarchy = new ArrayList<DimensionWrapper>();
			for (CodeRef childCodeRef : codeRef.getChildren()) {
				DimensionWrapper dw = new DimensionWrapper();

				CodeList codeList = getCodeListByAlias(childCodeRef
						.getCodelistAliasRef());

				dw.setDimension(getDimension(codeList));
				dw.setCode(codeList.getCodeByID(childCodeRef.getCodeID()));
				dw.setChildren(constructDimensionHierarchy(childCodeRef));

				dimensionHierarchy.add(dw);
			}
			return dimensionHierarchy;
		} else {
			return null;
		}
	}

	/**
	 * This finds all the combinations of this dimension hierarchy and their
	 * codes for a specific Indicator. ie. It find all the path through the
	 * hierarchy and return the collection of paths. Each path is a list of
	 * DimensionWrappers.
	 * 
	 * @param indicatorName
	 * @return a list of the possible paths through the hierarchy. Each path is
	 *         a list of DimensionWrappers in the correct order.
	 */
	public List<List<DimensionWrapper>> getAllCombinationofDimensionsForIndicator(
			String indicatorName, String keyFamilyId) {
		List<DimensionWrapper> dh = getDimensionHierarchy(indicatorName,
				keyFamilyId);
		if (dh == null) {
			return null;
		}
		List<List<DimensionWrapper>> collOfPaths = new LinkedList<List<DimensionWrapper>>();
		for (DimensionWrapper dw : dh) {
			List<List<DimensionWrapper>> allCombinationsOfDimensions = dw
					.getAllCombinationsOfDimensions();
			collOfPaths.addAll(allCombinationsOfDimensions);
		}
		return collOfPaths;
	}

	public List<List<DimensionWrapper>> getAllCombinationOfDimensions(
			String keyFamilyId) {
		// get all (non-standard) dimensions
		List<Dimension> allDimensions = getAllNonStanadrdDimensions(keyFamilyId);

		List<List<DimensionWrapper>> combinationOfDimensions = new ArrayList<List<DimensionWrapper>>();
		return constructDimensonCombinations(0, allDimensions,
				new LinkedList<DimensionWrapper>(), combinationOfDimensions);
	}

	public List<List<DimensionWrapper>> getAllCombinationOfDimensions(
			String keyFamilyId, List<Dimension> restrictToTheseDimensions) {
		List<List<DimensionWrapper>> combinationOfDimensions = new ArrayList<List<DimensionWrapper>>();
		return constructDimensonCombinations(0, restrictToTheseDimensions,
				new LinkedList<DimensionWrapper>(), combinationOfDimensions);
	}

	private List<List<DimensionWrapper>> constructDimensonCombinations(
			int listIndex, List<Dimension> allDimensions,
			List<DimensionWrapper> currentCombinationOfDimension,
			List<List<DimensionWrapper>> combinationOfDimensions) {
		if (listIndex > allDimensions.size() - 1) {
			combinationOfDimensions.add(currentCombinationOfDimension);
			return combinationOfDimensions;
		}

		Dimension dim = allDimensions.get(listIndex);

		CodeList dimOpts = getCodeList(dim.getCodelistRef());

		for (Code code : dimOpts.getCodes()) {
			List<DimensionWrapper> combinationOfDimension = new ArrayList<DimensionWrapper>();
			combinationOfDimension.addAll(currentCombinationOfDimension);

			DimensionWrapper dimWrap = new DimensionWrapper();
			dimWrap.setDimension(dim);
			dimWrap.setCode(code);

			combinationOfDimension.add(dimWrap);

			combinationOfDimensions = constructDimensonCombinations(
					listIndex + 1, allDimensions, combinationOfDimension,
					combinationOfDimensions);
		}

		return combinationOfDimensions;
	}

	/**
	 * Gets the dimension that relates to the given codelist
	 * 
	 * @param codeList
	 * @return the dimension that relates to the given codelist
	 */
	public Dimension getDimension(CodeList codeList) {
		Iterator<KeyFamily> iterator = keyFamilies.iterator();
		while (iterator.hasNext()) {
			KeyFamily keyFamily = iterator.next();
			Iterator<Dimension> iterator2 = keyFamily.getComponents()
					.getDimensions().iterator();
			while (iterator2.hasNext()) {
				Dimension dimension = iterator2.next();
				if (dimension.getCodelistRef().equalsIgnoreCase(
						codeList.getId())) {
					return dimension;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the dimension that with the given conceptRef
	 * 
	 * @param conceptRef
	 * @return the dimension with the given conceptRef
	 */
	public Dimension getDimension(String conceptRef) {
		for (KeyFamily keyFamily : keyFamilies) {
			List<Dimension> dimensions = keyFamily.getComponents()
					.getDimensions();
			for (Dimension dimension : dimensions) {
				if (dimension.getConceptRef().equalsIgnoreCase(conceptRef)) {
					return dimension;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the dimension that with the given conceptRef for a paticular
	 * keyFamily
	 * 
	 * @param conceptRef
	 * @return the dimension with the given conceptRef
	 */
	public Dimension getDimension(String conceptRef, String keyFamilyId) {
		KeyFamily keyFamily = getKeyFamily(keyFamilyId);
		List<Dimension> dimensions = keyFamily.getComponents().getDimensions();
		for (Dimension dimension : dimensions) {
			if (dimension.getConceptRef().equalsIgnoreCase(conceptRef)) {
				return dimension;
			}
		}
		return null;
	}

	/**
	 * Gets a codelist this the specified id.
	 * 
	 * @param id
	 * @return the codelist with the specified id.
	 */
	public CodeList getCodeList(String id) {
		for (CodeList c : codeLists) {
			if (c.getId().equalsIgnoreCase(id)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Gets the codelist that has the specified alias defined in a SDMX-HD
	 * hierarchical codelist.
	 * 
	 * @param alias
	 * @return the codelist with the specified alias
	 */
	public CodeList getCodeListByAlias(String alias) {
		CodelistRef codeListRef = null;
		for (HierarchicalCodelist hc : hierarchicalCodelists) {
			CodelistRef codeListRefSearch = hc.getCodeListRef(alias);
			if (codeListRefSearch != null) {
				codeListRef = codeListRefSearch;
				break;
			}
		}

		return getCodeList(codeListRef.getCodelistID());
	}

	/**
	 * Gets the KeyFamily in this DSD that has the specified ID.
	 * 
	 * @param keyFamilyID
	 * @return the keyfamily with the specified ID
	 */
	public KeyFamily getKeyFamily(String keyFamilyID) {
		Iterator<KeyFamily> iter = keyFamilies.iterator();
		while (iter.hasNext()) {
			KeyFamily keyFamily = (KeyFamily) iter.next();
			if (keyFamily.getId().equals(keyFamilyID)) {
				return keyFamily;
			}
		}
		return null;
	}

	/* Getters and Setter */

	public List<ConceptScheme> getConceptSchemes() {
		return conceptSchemes;
	}

	public void setConceptSchemes(List<ConceptScheme> conceptSchemes) {
		this.conceptSchemes = conceptSchemes;
	}

	public List<CodeList> getCodeLists() {
		return codeLists;
	}

	public void setCodeLists(List<CodeList> codeLists) {
		this.codeLists = codeLists;
	}

	public List<KeyFamily> getKeyFamilies() {
		return keyFamilies;
	}

	public void setKeyFamilies(List<KeyFamily> keyFamilies) {
		this.keyFamilies = keyFamilies;
	}

	public List<HierarchicalCodelist> getHierarchicalCodelists() {
		return hierarchicalCodelists;
	}

	public void setHierarchicalCodelists(
			List<HierarchicalCodelist> hierarchicalCodelists) {
		this.hierarchicalCodelists = hierarchicalCodelists;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<OrganisationScheme> getOrganisationSchemes() {
		return organisationSchemes;
	}

	public void setOrganisationSchemes(
			List<OrganisationScheme> organisationSchemes) {
		this.organisationSchemes = organisationSchemes;
	}

}
