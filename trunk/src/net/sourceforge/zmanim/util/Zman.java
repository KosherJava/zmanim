/*
 * Zmanim Java API
 * Copyright (C) 2004-2011 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package net.sourceforge.zmanim.util;

import java.util.Comparator;
import java.util.Date;

/**
 * Wrapper class for an astronomical time, mostly used to sort collections of
 * astronomical times.
 * 
 * @author &copy; Eliyahu Hershfeld 2007-2011
 * @version 1.0
 */
public class Zman {
	private String zmanLabel;
	private Date zman;
	private long duration;
	private Date zmanDescription;

	public Zman(Date date, String label) {
		this.zmanLabel = label;
		this.zman = date;
	}

	public Zman(long duration, String label) {
		this.zmanLabel = label;
		this.duration = duration;
	}

	public Date getZman() {
		return this.zman;
	}

	public void setZman(Date date) {
		this.zman = date;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getZmanLabel() {
		return this.zmanLabel;
	}

	public void setZmanLabel(String label) {
		this.zmanLabel = label;
	}

	public static final Comparator<Zman> DATE_ORDER = new Comparator<Zman>() {
		public int compare(Zman z1, Zman z2) {
			return z1.getZman().compareTo(z2.getZman());
		}
	};

	public static final Comparator<Zman> NAME_ORDER = new Comparator<Zman>() {
		public int compare(Zman z1, Zman z2) {
			return z1.getZmanLabel().compareTo(z2.getZmanLabel());
		}
	};

	public static final Comparator<Zman> DURATION_ORDER = new Comparator<Zman>() {
		public int compare(Zman z1, Zman z2) {
			return z1.getDuration() == z2.getDuration() ? 0
					: z1.getDuration() > z2.getDuration() ? 1 : -1;
		}
	};

	/**
	 * @return the zmanDescription
	 */
	public Date getZmanDescription() {
		return this.zmanDescription;
	}

	/**
	 * @param zmanDescription
	 *            the zmanDescription to set
	 */
	public void setZmanDescription(Date zmanDescription) {
		this.zmanDescription = zmanDescription;
	}
}
