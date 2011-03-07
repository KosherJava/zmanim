/*
 * Zmanim Java API
 * Copyright (C) 2007-2008 Eliyahu Hershfeld
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc. 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA or connect to: http://www.fsf.org/copyleft/gpl.html
 */
package net.sourceforge.zmanim.util;

import java.util.Comparator;
import java.util.Date;

/**
 * Wrapper class for an astronomical time, mostly used to sort collections of
 * astronomical times.
 * 
 * @author &copy; Eliyahu Hershfeld 2007-2008
 * @version 1.0
 */
public class Zman {
	private String zmanLabel;
	private Date zman;
	private long duration;
	private Date zmanDescription;

	public Zman(Date date, String label) {
		zmanLabel = label;
		zman = date;
	}

	public Zman(long duration, String label) {
		zmanLabel = label;
		this.duration = duration;
	}

	public Date getZman() {
		return zman;
	}

	public void setZman(Date date) {
		zman = date;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getZmanLabel() {
		return zmanLabel;
	}

	public void setZmanLabel(String label) {
		zmanLabel = label;
	}

	public static final Comparator DATE_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			Zman z1 = (Zman) o1;
			Zman z2 = (Zman) o2;
			return z1.getZman().compareTo(z2.getZman());
		}
	};

	public static final Comparator NAME_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			Zman z1 = (Zman) o1;
			Zman z2 = (Zman) o2;
			return z1.getZmanLabel().compareTo(z2.getZmanLabel());
		}
	};

	public static final Comparator DURATION_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			Zman z1 = (Zman) o1;
			Zman z2 = (Zman) o2;
			return z1.getDuration() == z2.getDuration() ? 0
					: z1.getDuration() > z2.getDuration() ? 1 : -1;
		}
	};

	/**
	 * @return the zmanDescription
	 */
	public Date getZmanDescription() {
		return zmanDescription;
	}

	/**
	 * @param zmanDescription
	 *            the zmanDescription to set
	 */
	public void setZmanDescription(Date zmanDescription) {
		this.zmanDescription = zmanDescription;
	}
}
