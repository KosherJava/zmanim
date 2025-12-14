/*
 * Zmanim Java API
 * Copyright (C) 2004-2025 Eliyahu Hershfeld
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
package com.kosherjava.zmanim.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * A wrapper class for astronomical times / <em>zmanim</em> that is mostly intended to allow sorting collections of astronomical times.
 * It has fields for both date/time and duration based <em>zmanim</em>, name / labels as well as a longer description or explanation of a
 * <em>zman</em>.
 * <p>
 * Here is an example of various ways of sorting <em>zmanim</em>.
 * <p>First create the Calendar for the location you would like to calculate:
 * 
 * <pre style="background: #FEF0C9; display: inline-block;">
 * String locationName = &quot;Lakewood, NJ&quot;;
 * double latitude = 40.0828; // Lakewood, NJ
 * double longitude = -74.2094; // Lakewood, NJ
 * double elevation = 20; // optional elevation correction in Meters
 * // the String parameter in getTimeZone() has to be a valid timezone listed in {@link com.ibm.icu.util.TimeZone#getAvailableIDs()}
 * TimeZone timeZone = TimeZone.getTimeZone(&quot;America/New_York&quot;);
 * GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
 * ComplexZmanimCalendar czc = new ComplexZmanimCalendar(location);
  * Zman sunset = new Zman(czc.getSunset(), "Sunset");
 * Zman shaah16 = new Zman(czc.getShaahZmanis16Point1Degrees(), "Shaah zmanis 16.1");
 * Zman sunrise = new Zman(czc.getSunrise(), "Sunrise");
 * Zman shaah = new Zman(czc.getShaahZmanisGra(), "Shaah zmanis GRA");
 * ArrayList&lt;Zman&gt; zl = new ArrayList&lt;Zman&gt;();
 * zl.add(sunset);
 * zl.add(shaah16);
 * zl.add(sunrise);
 * zl.add(shaah);
 * //will sort sunset, shaah 1.6, sunrise, shaah GRA
 * System.out.println(zl);
 * Collections.sort(zl, Zman.DATE_ORDER);
 * // will sort sunrise, sunset, shaah, shaah 1.6 (the last 2 are not in any specific order)
 * Collections.sort(zl, Zman.DURATION_ORDER);
 * // will sort sunrise, sunset (the first 2 are not in any specific order), shaah GRA, shaah 1.6
 * Collections.sort(zl, Zman.NAME_ORDER);
 * // will sort shaah 1.6, shaah GRA, sunrise, sunset
 * </pre>
 * 
 * @author &copy; Eliyahu Hershfeld 2007-2025
 * @todo Add secondary sorting. As of now the {@code Comparator}s in this class do not sort by secondary order. This means that when sorting a
 * {@link java.util.Collection} of <em>zmanim</em> and using the {@link #DATE_ORDER} {@code Comparator} will have the duration based <em>zmanim</em>
 * at the end, but they will not be sorted by duration. This should be N/A for label based sorting.
 */
public class Zman {
	/**
	 * The name / label of the <em>zman</em>.
	 */
	private String label;
	
	/**
	 * The {@link Date} of the <em>zman</em>
	 */
	private Date zman;
	
	/**
	 * The duration if the <em>zman</em> is  a {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour} (or the various
	 * <em>shaah zmanis</em> base times such as {@link com.kosherjava.zmanim.ZmanimCalendar#getShaahZmanisGra()  <em>shaah Zmanis GRA</em>} or
	 * {@link com.kosherjava.zmanim.ComplexZmanimCalendar#getShaahZmanis16Point1Degrees() <em>shaah Zmanis 16.1&deg;</em>}).
	 */
	private long duration;
	
	/**
	 * A longer description or explanation of a <em>zman</em>.
	 */
	private String description;
	
	/**
	 * The location information of the <em>zman</em>.
	 */
	private GeoLocation geoLocation;

	/**
	 * The constructor setting a {@link Date} based <em>zman</em> and a label. In most cases you will likely want to call
	 * {@link #Zman(Date, GeoLocation, String)} that also sets the location.
	 * @param date the Date of the <em>zman</em>.
	 * @param label the label of the  <em>zman</em> such as "<em>Sof Zman Krias Shema GRA</em>".
	 * @see #Zman(Date, GeoLocation, String)
	 */
	public Zman(Date date, String label) {
		this(date, null, label);
	}
	
	/**
	 * The constructor setting a {@link Date} based <em>zman</em> and a label. In most cases you will likely want to call
	 * {@link #Zman(Date, GeoLocation, String)} that also sets the geo location.
	 * @param date the Date of the <em>zman</em>.
	 * @param geoLocation the {@link GeoLocation} of the <em>zman</em>.
	 * @param label the label of the  <em>zman</em> such as "<em>Sof Zman Krias Shema GRA</em>".
	 */
	public Zman(Date date, GeoLocation geoLocation, String label) {
		this.zman = date;
		this.geoLocation = geoLocation;
		this.label = label;
	}

	/**
	 * The constructor setting a duration based <em>zman</em> such as
	 * {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour} (or the various <em>shaah zmanis</em> times such as
	 * {@link com.kosherjava.zmanim.ZmanimCalendar#getShaahZmanisGra() <em>shaah zmanis GRA</em>} or
	 * {@link com.kosherjava.zmanim.ComplexZmanimCalendar#getShaahZmanis16Point1Degrees() <em>shaah Zmanis 16.1&deg;</em>}) and label.
	 * @param duration a duration based <em>zman</em> such as ({@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour()}
	 * @param label the label of the  <em>zman</em> such as "<em>Shaah Zmanis GRA</em>".
	 * @see #Zman(Date, String)
	 */
	public Zman(long duration, String label) {
		this.label = label;
		this.duration = duration;
	}

	/**
	 * Returns the {@code Date} based <em>zman</em>.
	 * @return the <em>zman</em>.
	 * @see #setZman(Date)
	 */
	public Date getZman() {
		return this.zman;
	}

	/**
	 * Sets a {@code Date} based <em>zman</em>.
	 * @param date a {@code Date} based <em>zman</em>
	 * @see #getZman()
	 */
	public void setZman(Date date) {
		this.zman = date;
	}
	
	/**
	 * Returns the {link TimeZone} of the <em>zman</em>.
	 * @return the time zone
	 */
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	/**
	 * Sets the {@code GeoLocation} of the <em>zman</em>.
	 * @param geoLocation the {@code GeoLocation}  of the <em>zman</em>.
	 */
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	/**
	 * Returns a duration based <em>zman</em> such as {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour}
	 * (or the various <em>shaah zmanis</em> times such as {@link com.kosherjava.zmanim.ZmanimCalendar#getShaahZmanisGra() <em>shaah zmanis GRA</em>}
	 * or {@link com.kosherjava.zmanim.ComplexZmanimCalendar#getShaahZmanis16Point1Degrees() <em>shaah zmanis 16.1&deg;</em>}).
	 * @return the duration based <em>zman</em>.
	 * @see #setDuration(long)
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 *  Sets a duration based <em>zman</em> such as {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour}
	 * (or the various <em>shaah zmanis</em> times as {@link com.kosherjava.zmanim.ZmanimCalendar#getShaahZmanisGra() <em>shaah zmanis GRA</em>} or
	 * {@link com.kosherjava.zmanim.ComplexZmanimCalendar#getShaahZmanis16Point1Degrees() <em>shaah zmanis 16.1&deg;</em>}).
	 * @param duration duration based <em>zman</em> such as {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour()}.
	 * @see #getDuration()
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Returns the name / label of the <em>zman</em> such as "<em>Sof Zman Krias Shema GRA</em>". There are no automatically set labels
	 * and you must set them using {@link #setLabel(String)}.
	 * @return the name/label of the <em>zman</em>.
	 * @see #setLabel(String)
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the name / label of the <em>zman</em> such as "<em>Sof Zman Krias Shema GRA</em>".
	 * @param label the name / label to set for the <em>zman</em>.
	 * @see #getLabel()
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the longer description or explanation of a <em>zman</em>. There is no default value for this and it must be set using
	 * {@link #setDescription(String)}
	 * @return the description or explanation of a <em>zman</em>.
	 * @see #setDescription(String)
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the longer description or explanation of a <em>zman</em>.
	 * @param description
	 *            the <em>zman</em> description to set.
	 * @see #getDescription()
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * A {@link Comparator} that will compare and sort <em>zmanim</em> by date/time order. Compares its two arguments by the zman's date/time
	 * order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
	 * than the second.
	 * Please note that this class will handle cases where either the {@code Zman} is a null or {@link #getZman()} returns a null.
	 */
	public static final Comparator<Zman> DATE_ORDER = new Comparator<Zman>() {
		public int compare(Zman zman1, Zman zman2) {
			long firstTime = (zman1 == null || zman1.getZman() == null) ? Long.MAX_VALUE : zman1.getZman().getTime();
			long secondTime = (zman2 == null || zman2.getZman() == null) ? Long.MAX_VALUE : zman2.getZman().getTime();
			return Long.valueOf(firstTime).compareTo(Long.valueOf(secondTime));
		}
	};

	/**
	 * A {@link Comparator} that will compare and sort zmanim by zmanim label order. Compares its two arguments by the zmanim label
	 * name order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
	 * than the second.
	 * Please note that this class will sort cases where either the {@code Zman} is a null or {@link #label} returns a null
	 * as empty {@code String}s.
	 */
	public static final Comparator<Zman> NAME_ORDER = new Comparator<Zman>() {
		public int compare(Zman zman1, Zman zman2) {
			String firstLabel = (zman1 == null || zman1.getLabel() == null) ? "" : zman1.getLabel();
			String secondLabel = (zman2 == null || zman2.getLabel() == null) ? "" : zman2.getLabel();
			return firstLabel.compareTo(secondLabel);
		}
	};

	/**
	 * A {@link Comparator} that will compare and sort duration based <em>zmanim</em>  such as
	 * {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour} (or the various <em>shaah zmanis</em> times
	 * such as <em>{@link com.kosherjava.zmanim.ZmanimCalendar#getShaahZmanisGra() shaah zmanis GRA}</em> or
	 * {@link com.kosherjava.zmanim.ComplexZmanimCalendar#getShaahZmanis16Point1Degrees() <em>shaah zmanis 16.1&deg;</em>}). Returns a negative
	 * integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 * Please note that this class will sort cases where {@code Zman} is a null.
	 */
	public static final Comparator<Zman> DURATION_ORDER = new Comparator<Zman>() {
		public int compare(Zman zman1, Zman zman2) {
			long firstDuration  = zman1 == null ? Long.MAX_VALUE : zman1.getDuration();
			long secondDuration  = zman2 == null ? Long.MAX_VALUE : zman2.getDuration();
			return firstDuration == secondDuration ? 0 	: firstDuration > secondDuration ? 1 : -1;
		}
	};

	/**
	 * A method that returns an XML formatted <code>String</code> representing the serialized <code>Object</code>. Very
	 * similar to the toString method but the return value is in an xml format. The format currently used (subject to
	 * change) is:
	 * 
	 * <pre>
	 * &lt;Zman&gt;
	 * 	&lt;Label&gt;Sof Zman Krias Shema GRA&lt;/Label&gt;
	 * 	&lt;Zman&gt;1969-02-08T09:37:56.820&lt;/Zman&gt;
	 * 	&lt;TimeZone&gt;
	 * 		&lt;TimezoneName&gt;America/Montreal&lt;/TimezoneName&gt;
	 * 		&lt;TimeZoneDisplayName&gt;Eastern Standard Time&lt;/TimeZoneDisplayName&gt;
	 * 		&lt;TimezoneGMTOffset&gt;-5&lt;/TimezoneGMTOffset&gt;
	 * 		&lt;TimezoneDSTOffset&gt;1&lt;/TimezoneDSTOffset&gt;
	 * 	&lt;/TimeZone&gt;
	 * 	&lt;Duration&gt;0&lt;/Duration&gt;
	 * 	&lt;Description&gt;Sof Zman Krias Shema GRA is 3 sha'os zmaniyos calculated from sunrise to sunset.&lt;/Description&gt;
	 * &lt;/Zman&gt;
	 * </pre>
	 * @return The XML formatted <code>String</code>.
	 */
	public String toXML() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		StringBuilder sb = new StringBuilder();
		sb.append("<Zman>\n");
		sb.append("\t<Label>").append(getLabel()).append("</Label>\n");
		sb.append("\t<Zman>").append(getZman() == null ? "": formatter.format(getZman())).append("</Zman>\n");
		sb.append("\t" + getGeoLocation().toXML().replaceAll("\n", "\n\t"));
		sb.append("\n\t<Duration>").append(getDuration()).append("</Duration>\n");
		sb.append("\t<Description>").append(getDescription()).append("</Description>\n");
		sb.append("</Zman>");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nLabel:\t").append(this.getLabel());
		sb.append("\nZman:\t").append(getZman());
		sb.append("\nGeoLocation:\t").append(getGeoLocation().toString().replaceAll("\n", "\n\t"));
		sb.append("\nDuration:\t").append(getDuration());
		sb.append("\nDescription:\t").append(getDescription());
		return sb.toString();
	}
}
