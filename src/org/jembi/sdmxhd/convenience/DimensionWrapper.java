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
package org.jembi.sdmxhd.convenience;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jembi.sdmxhd.dsd.Dimension;
import org.jembi.sdmxhd.primitives.Code;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a Dimension with a corresponding codes to
 *         simplify working with Dimensions and Codes. It can also define a
 *         Dimension hierarchy.
 * 
 *         This is used for hierarchical codelists is SDMX-HD that define a
 *         hierarchy of dimensons.
 */
public class DimensionWrapper {

	private Dimension dimension;
	private Code code;

	private List<DimensionWrapper> children;

	public boolean hasChildren() {
		if (children == null || children.size() < 1) {
			return false;
		}
		return true;
	}

	/**
	 * This finds all the combinations of this dimension hierarchy and their
	 * codes. ie. It find all the path through the hierarchy and return the
	 * collection of paths. Each path is a list of DimensionWrappers.
	 * 
	 * @return a list of the possible paths through the hierarchy. Each path is
	 *         a list of DimensionWrappers in the correct order.
	 */
	public List<List<DimensionWrapper>> getAllCombinationsOfDimensions() {
		List<List<DimensionWrapper>> collOfPaths = new LinkedList<List<DimensionWrapper>>();
		if (children == null || children.size() < 1) {
			List<DimensionWrapper> path = new ArrayList<DimensionWrapper>();
			path.add(this);
			collOfPaths.add(path);
			return collOfPaths;
		}
		for (DimensionWrapper child : children) {
			List<List<DimensionWrapper>> allCombinationsOfDimensions = child
					.getAllCombinationsOfDimensions();
			for (List<DimensionWrapper> path : allCombinationsOfDimensions) {
				path.add(0, this);
				collOfPaths.add(path);
			}
		}
		return collOfPaths;
	}

	/**
	 * Gets all dimensions in this DimensionWrapper, even those that are
	 * contained in the children of this DimensionWrapper.
	 * 
	 * @return a list of all the dimensions
	 */
	public List<Dimension> getAllDimensions() {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		dimensions.add(dimension);

		for (DimensionWrapper dw : children) {
			List<Dimension> childDimensions = dw.getAllDimensions();
			dimensions.addAll(childDimensions);
		}

		return dimensions;
	}

	/* getters and setter */

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public List<DimensionWrapper> getChildren() {
		return children;
	}

	public void setChildren(List<DimensionWrapper> children) {
		this.children = children;
	}

}
