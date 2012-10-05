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
package org.jembi.sdmxhd;

import org.jembi.sdmxhd.cds.CDS;
import org.jembi.sdmxhd.dsd.DSD;
import org.jembi.sdmxhd.gms.GMS;
import org.jembi.sdmxhd.msd.MSD;

/**
 * @author Ryan Crichton
 * 
 *         This object represents a SDMX-HD Message, it contains basic methods
 *         to get and set the various components of SDMX-HD Message
 */
public class SDMXHDMessage {

	private DSD dsd;
	private MSD msd;
	private CDS cds;
	private GMS gms;

	public DSD getDsd() {
		return dsd;
	}

	public void setDsd(DSD dsd) {
		this.dsd = dsd;
	}

	public MSD getMsd() {
		return msd;
	}

	public void setMsd(MSD msd) {
		this.msd = msd;
	}

	public CDS getCds() {
		return cds;
	}

	public void setCds(CDS cds) {
		this.cds = cds;
	}

	public GMS getGms() {
		return gms;
	}

	public void setGms(GMS gms) {
		this.gms = gms;
	}

}
