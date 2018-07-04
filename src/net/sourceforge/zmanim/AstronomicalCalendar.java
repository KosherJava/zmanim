/*
 * Zmanim Java API
 * Copyright (C) 2004-2018 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package net.sourceforge.zmanim;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.sourceforge.zmanim.util.AstronomicalCalculator;
import net.sourceforge.zmanim.util.GeoLocation;
import net.sourceforge.zmanim.util.ZmanimFormatter;

/**
 * <p>This class extends ZmanimCalendar and provides many more zmanim than available in the ZmanimCalendar. The basis for
 * most zmanim in this class are from the <em>sefer</em> <b><a href="http://hebrewbooks.org/9765">Yisroel Vehazmanim</a></b>
 * by <b><a href="https://en.wikipedia.org/wiki/Yisroel_Dovid_Harfenes">Rabbi Yisroel Dovid Harfenes</a></b>.
 * As an example of the number of different <em>zmanim</em> made available by this class, there are methods to return 14
 * different calculations for <em>alos</em> (dawn) and 25 for <em>tzais</em> available in this API. The real power of this
 * API is the ease in calculating <em>zmanim</em> that are not part of the library. The methods for <em>zmanim</em>
 * calculations not present in this class or it's superclass  {@link ZmanimCalendar} are contained in the
 * {@link AstronomicalCalendar}, the base class of the calendars in our API since they are generic methods for calculating
 * time based on degrees or time before or after {@link #getSunrise sunrise} and {@link #getSunset sunset} and are of interest
 * for calculation beyond <em>zmanim</em> calculations. Here are some examples.
 * <p>First create the Calendar for the location you would like to calculate:
 * 
 * <pre style="background: #FEF0C9; display: inline-block;">
 * String locationName = &quot;Lakewood, NJ&quot;;
 * double latitude = 40.0828; // Lakewood, NJ
 * double longitude = -74.2094; // Lakewood, NJ
 * double elevation = 20; // optional elevation correction in Meters
 * // the String parameter in getTimeZone() has to be a valid timezone listed in
 * // {@link java.util.TimeZone#getAvailableIDs()}
 * TimeZone timeZone = TimeZone.getTimeZone(&quot;America/New_York&quot;);
 * GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
 * ComplexZmanimCalendar czc = new ComplexZmanimCalendar(location);
 * // Optionally set the date or it will default to today's date
 * czc.getCalendar().set(Calendar.MONTH, Calendar.FEBRUARY);
 * czc.getCalendar().set(Calendar.DAY_OF_MONTH, 8);</pre>
 * <p>
 * <b>Note:</b> For locations such as Israel where the beginning and end of daylight savings time can fluctuate from
 * year to year, if your version of Java does not have an <a href=
 * "http://www.oracle.com/technetwork/java/javase/tzdata-versions-138805.html">up to date timezone database</a>, create a
 * {@link java.util.SimpleTimeZone} with the known start and end of DST.
 * To get <em>alos</em> calculated as 14&deg; below the horizon (as calculated in the calendars published in Montreal),
 * add {@link AstronomicalCalendar#GEOMETRIC_ZENITH} (90) to the 14&deg; offset to get the desired time:
 * <br><br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 *  Date alos14 = czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 14);</pre>
 * <p>
 * To get <em>mincha gedola</em> calculated based on the MGA using a <em>shaah zmanis</em> based on the day starting
 * 16.1&deg; below the horizon (and ending 16.1&deg; after sunset) the following calculation can be used:
 * 
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date minchaGedola = czc.getTimeOffset(czc.getAlos16point1Degrees(), czc.getShaahZmanis16Point1Degrees() * 6.5);</pre>
 * <p>
 * or even simpler using the included convenience methods
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date minchaGedola = czc.getMinchaGedola(czc.getAlos16point1Degrees(), czc.getShaahZmanis16Point1Degrees());</pre>
 * <p>
 * A little more complex example would be calculating zmanim that rely on a <em>shaah zmanis</em> that is
 * not present in this library. While a drop more complex, it is still rather easy. An example would be to calculate
 * the <em><a href="https://en.wikipedia.org/wiki/Israel_Isserlein">Trumas Hadeshen</a>'s</em> <em>alos</em> to
 * <em>tzais</em> based <em>plag hamincha</em> as calculated in the Machzikei Hadass calendar in Manchester, England.
 * A number of this calendar's zmanim are calculated based on a day starting at <em>alos</em> of 12&deg; before sunrise
 * and ending at <em>tzais</em> of 7.083&deg; after sunset. Be aware that since the <em>alos</em> and <em>tzais</em>
 * do not use identical degree based offsets, this leads to <em>chatzos</em> being at a time other than the
 * {@link #getSunTransit() solar transit} (solar midday). To calculate this zman, use the following steps. Note that
 * <em>plag hamincha</em> is 10.75 hours after the start of the day, and the following steps are all that it takes.
 * <br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date plag = czc.getPlagHamincha(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12),
 * 				czc.getSunsetOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + ZENITH_7_POINT_083));</pre>
 * <p>
 * Something a drop more challenging, but still simple, would be calculating a zman using the same "complex" offset day
 * used in the above mentioned Manchester calendar, but for a <em>shaos zmaniyos</em> based <em>zman</em> not not
 * supported by this library, such as calculating
 * <em><a href="http://www.hebrewbooks.org/pdfpager.aspx?req=37941&amp;pgnum=434">zman shchitas Korban Pesach</a></em>
 * that is 8.5 <em>shaos zmaniyos</em> into the day.
 * <ol>
 * 	<li>Calculate the <em>shaah zmanis</em> in milliseconds for this day</li>
 * 	<li>Add 8.5 of these <em>shaos zmaniyos</em> to alos starting at 12&deg;</li>
 * </ol>
 * <br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 * long shaahZmanis = czc.getTemporalHour(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12),
 * 						czc.getSunsetOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + ZENITH_7_POINT_083));
 * Date zmanShchita = getTimeOffset(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12), 
 * 					shaahZmanis * 8.5);</pre>
 * <p>
 * Calculating <em>zman shchitas Korban Pesach</em> according to the <em>GRA</em> is simplicity itself.
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date zmanShchita = czc.getTimeOffset(czc.getSunrise(), czc.getShaahZmanisGra() * 8.5);</pre>
 * 
 * <h2>Documentation from the {@link ZmanimCalendar} parent class</h2>
 * {@inheritDoc}
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2018
 */
public class AstronomicalCalendar implements Cloneable {

	/**
	 * 90&deg; below the vertical. Used as a basis for most calculations since the location of the sun is 90&deg; below
	 * the horizon at sunrise and sunset.
	 * <b>Note </b>: it is important to note that for sunrise and sunset the {@link AstronomicalCalculator#adjustZenith
	 * adjusted zenith} is required to account for the radius of the sun and refraction. The adjusted zenith should not
	 * be used for calculations above or below 90&deg; since they are usuallyes are calculated as an offset to 90&deg;.
	 */
	public static final double GEOMETRIC_ZENITH = 90;

	/**
	 * Default value for Sun's zenith and true rise/set Zenith (used in this class and subclasses) is the angle that the
	 * center of the Sun makes to a line perpendicular to the Earth's surface. If the Sun were a point and the Earth
	 * were without an atmosphere, true sunset and sunrise would correspond to a 90&deg; zenith. Because the Sun is not
	 * a point, and because the atmosphere refracts light, this 90&deg; zenith does not, in fact, correspond to true
	 * sunset or sunrise, instead the center of the Sun's disk must lie just below the horizon for the upper edge to be
	 * obscured. This means that a zenith of just above 90&deg; must be used. The Sun subtends an angle of 16 minutes of
	 * arc (this can be changed via the {@link #setSunRadius(double)} method , and atmospheric refraction accounts for
	 * 34 minutes or so (this can be changed via the {@link #setRefraction(double)} method), giving a total of 50
	 * arcminutes. The total value for ZENITH is 90+(5/6) or 90.8333333&deg; for true sunrise/sunset.
	 */
	// public static double ZENITH = GEOMETRIC_ZENITH + 5.0 / 6.0;
	/** Sun's zenith at civil twilight (96&deg;). */
	public static final double CIVIL_ZENITH = 96;

	/** Sun's zenith at nautical twilight (102&deg;). */
	public static final double NAUTICAL_ZENITH = 102;

	/** Sun's zenith at astronomical twilight (108&deg;). */
	public static final double ASTRONOMICAL_ZENITH = 108;

	/** constant for milliseconds in a minute (60,000) */
	static final long MINUTE_MILLIS = 60 * 1000;

	/** constant for milliseconds in an hour (3,600,000) */
	static final long HOUR_MILLIS = MINUTE_MILLIS * 60;

	/**
	 * The Java Calendar encapsulated by this class to track the current date used by the class
	 */
	private Calendar calendar;

	private GeoLocation geoLocation;

	private AstronomicalCalculator astronomicalCalculator;

	/**
	 * The getSunrise method Returns a <code>Date</code> representing the
	 * {@link AstronomicalCalculator#getElevationAdjustment(double) elevation adjusted} sunrise time. The zenith used
	 * for the calculation uses {@link #GEOMETRIC_ZENITH geometric zenith} of 90&deg; plus
	 * {@link AstronomicalCalculator#getElevationAdjustment(double)}. This is adjusted by the
	 * {@link AstronomicalCalculator} to add approximately 50/60 of a degree to account for 34 archminutes of refraction
	 * and 16 archminutes for the sun's radius for a total of {@link AstronomicalCalculator#adjustZenith 90.83333&deg;}.
	 * See documentation for the specific implementation of the {@link AstronomicalCalculator} that you are using.
	 * 
	 * @return the <code>Date</code> representing the exact sunrise time. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalculator#adjustZenith
	 * @see #getSeaLevelSunrise()
	 * @see AstronomicalCalendar#getUTCSunrise
	 */
	public Date getSunrise() {
		double sunrise = getUTCSunrise(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunrise)) {
			return null;
		} else {
			return getDateFromTime(sunrise, true);
		}
	}

	/**
	 * A method that returns the sunrise without {@link AstronomicalCalculator#getElevationAdjustment(double) elevation
	 * adjustment}. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible light,
	 * something that is not affected by elevation. This method returns sunrise calculated at sea level. This forms the
	 * base for dawn calculations that are calculated as a dip below the horizon before sunrise.
	 * 
	 * @return the <code>Date</code> representing the exact sea-level sunrise time. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a null will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalendar#getSunrise
	 * @see AstronomicalCalendar#getUTCSeaLevelSunrise
	 * @see #getSeaLevelSunset()
	 */
	public Date getSeaLevelSunrise() {
		double sunrise = getUTCSeaLevelSunrise(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunrise)) {
			return null;
		} else {
			return getDateFromTime(sunrise, true);
		}
	}

	/**
	 * A method that returns the beginning of civil twilight (dawn) using a zenith of {@link #CIVIL_ZENITH 96&deg;}.
	 * 
	 * @return The <code>Date</code> of the beginning of civil twilight using a zenith of 96&deg;. If the calculation
	 *         can't be computed, null will be returned. See detailed explanation on top of the page.
	 * @see #CIVIL_ZENITH
	 */
	public Date getBeginCivilTwilight() {
		return getSunriseOffsetByDegrees(CIVIL_ZENITH);
	}

	/**
	 * A method that returns the beginning of nautical twilight using a zenith of {@link #NAUTICAL_ZENITH 102&deg;}.
	 * 
	 * @return The <code>Date</code> of the beginning of nautical twilight using a zenith of 102&deg;. If the
	 *         calculation can't be computed null will be returned. See detailed explanation on top of the page.
	 * @see #NAUTICAL_ZENITH
	 */
	public Date getBeginNauticalTwilight() {
		return getSunriseOffsetByDegrees(NAUTICAL_ZENITH);
	}

	/**
	 * A method that returns the beginning of astronomical twilight using a zenith of {@link #ASTRONOMICAL_ZENITH
	 * 108&deg;}.
	 * 
	 * @return The <code>Date</code> of the beginning of astronomical twilight using a zenith of 108&deg;. If the
	 *         calculation can't be computed, null will be returned. See detailed explanation on top of the page.
	 * @see #ASTRONOMICAL_ZENITH
	 */
	public Date getBeginAstronomicalTwilight() {
		return getSunriseOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}

	/**
	 * The getSunset method Returns a <code>Date</code> representing the
	 * {@link AstronomicalCalculator#getElevationAdjustment(double) elevation adjusted} sunset time. The zenith used for
	 * the calculation uses {@link #GEOMETRIC_ZENITH geometric zenith} of 90&deg; plus
	 * {@link AstronomicalCalculator#getElevationAdjustment(double)}. This is adjusted by the
	 * {@link AstronomicalCalculator} to add approximately 50/60 of a degree to account for 34 archminutes of refraction
	 * and 16 archminutes for the sun's radius for a total of {@link AstronomicalCalculator#adjustZenith 90.83333&deg;}.
	 * See documentation for the specific implementation of the {@link AstronomicalCalculator} that you are using. Note:
	 * In certain cases the calculates sunset will occur before sunrise. This will typically happen when a timezone
	 * other than the local timezone is used (calculating Los Angeles sunset using a GMT timezone for example). In this
	 * case the sunset date will be incremented to the following date.
	 * 
	 * @return the <code>Date</code> representing the exact sunset time. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalculator#adjustZenith
	 * @see #getSeaLevelSunset()
	 * @see AstronomicalCalendar#getUTCSunset
	 */
	public Date getSunset() {
		double sunset = getUTCSunset(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunset)) {
			return null;
		} else {
			return getDateFromTime(sunset, false);
		}
	}

	/**
	 * A method that returns the sunset without {@link AstronomicalCalculator#getElevationAdjustment(double) elevation
	 * adjustment}. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible light,
	 * something that is not affected by elevation. This method returns sunset calculated at sea level. This forms the
	 * base for dusk calculations that are calculated as a dip below the horizon after sunset.
	 * 
	 * @return the <code>Date</code> representing the exact sea-level sunset time. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a null will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalendar#getSunset
	 * @see AstronomicalCalendar#getUTCSeaLevelSunset 2see {@link #getSunset()}
	 */
	public Date getSeaLevelSunset() {
		double sunset = getUTCSeaLevelSunset(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunset)) {
			return null;
		} else {
			return getDateFromTime(sunset, false);
		}
	}

	/**
	 * A method that returns the end of civil twilight using a zenith of {@link #CIVIL_ZENITH 96&deg;}.
	 * 
	 * @return The <code>Date</code> of the end of civil twilight using a zenith of {@link #CIVIL_ZENITH 96&deg;}. If
	 *         the calculation can't be computed, null will be returned. See detailed explanation on top of the page.
	 * @see #CIVIL_ZENITH
	 */
	public Date getEndCivilTwilight() {
		return getSunsetOffsetByDegrees(CIVIL_ZENITH);
	}

	/**
	 * A method that returns the end of nautical twilight using a zenith of {@link #NAUTICAL_ZENITH 102&deg;}.
	 * 
	 * @return The <code>Date</code> of the end of nautical twilight using a zenith of {@link #NAUTICAL_ZENITH 102&deg;}
	 *         . If the calculation can't be computed, null will be returned. See detailed explanation on top of the
	 *         page.
	 * @see #NAUTICAL_ZENITH
	 */
	public Date getEndNauticalTwilight() {
		return getSunsetOffsetByDegrees(NAUTICAL_ZENITH);
	}

	/**
	 * A method that returns the end of astronomical twilight using a zenith of {@link #ASTRONOMICAL_ZENITH 108&deg;}.
	 * 
	 * @return the <code>Date</code> of the end of astronomical twilight using a zenith of {@link #ASTRONOMICAL_ZENITH
	 *         108&deg;}. If the calculation can't be computed, null will be returned. See detailed explanation on top
	 *         of the page.
	 * @see #ASTRONOMICAL_ZENITH
	 */
	public Date getEndAstronomicalTwilight() {
		return getSunsetOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}

	/**
	 * A utility method that returns a date offset by the offset time passed in as a parameter. This method casts the
	 * offset as a <code>long</code> and calls {@link #getTimeOffset(Date, long)}.
	 * 
	 * @param time
	 *            the start time
	 * @param offset
	 *            the offset in milliseconds to add to the time
	 * @return the {@link java.util.Date}with the offset added to it
	 */
	public Date getTimeOffset(Date time, double offset) {
		return getTimeOffset(time, (long) offset);
	}

	/**
	 * A utility method that returns a date offset by the offset time passed in. Please note that the level of light
	 * during twilight is not affected by elevation, so if this is being used to calculate an offset before sunrise or
	 * after sunset with the intent of getting a rough "level of light" calculation, the sunrise or sunset time passed
	 * to this method should be sea level sunrise and sunset.
	 * 
	 * @param time
	 *            the start time
	 * @param offset
	 *            the offset in milliseconds to add to the time.
	 * @return the {@link java.util.Date} with the offset in milliseconds added to it
	 */
	public Date getTimeOffset(Date time, long offset) {
		if (time == null || offset == Long.MIN_VALUE) {
			return null;
		}
		return new Date(time.getTime() + offset);
	}

	/**
	 * A utility method that returns the time of an offset by degrees below or above the horizon of
	 * {@link #getSunrise() sunrise}. Note that the degree offset is from the vertical, so for a calculation of 14&deg;
	 * before sunrise, an offset of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * 
	 * @param offsetZenith
	 *            the degrees before {@link #getSunrise()} to use in the calculation. For time after sunrise use
	 *            negative numbers. Note that the degree offset is from the vertical, so for a calculation of 14&deg;
	 *            before sunrise, an offset of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a
	 *            parameter.
	 * @return The {@link java.util.Date} of the offset after (or before) {@link #getSunrise()}. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does
	 *         not rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         page.
	 */
	public Date getSunriseOffsetByDegrees(double offsetZenith) {
		double dawn = getUTCSunrise(offsetZenith);
		if (Double.isNaN(dawn)) {
			return null;
		} else {
			return getDateFromTime(dawn, true);
		}
	}

	/**
	 * A utility method that returns the time of an offset by degrees below or above the horizon of {@link #getSunset()
	 * sunset}. Note that the degree offset is from the vertical, so for a calculation of 14&deg; after sunset, an
	 * offset of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * 
	 * @param offsetZenith
	 *            the degrees after {@link #getSunset()} to use in the calculation. For time before sunset use negative
	 *            numbers. Note that the degree offset is from the vertical, so for a calculation of 14&deg; after
	 *            sunset, an offset of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * @return The {@link java.util.Date}of the offset after (or before) {@link #getSunset()}. If the calculation can't
	 *         be computed such as in the Arctic Circle where there is at least one day a year where the sun does not
	 *         rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         page.
	 */
	public Date getSunsetOffsetByDegrees(double offsetZenith) {
		double sunset = getUTCSunset(offsetZenith);
		if (Double.isNaN(sunset)) {
			return null;
		} else {
			return getDateFromTime(sunset, false);
		}
	}

	/**
	 * Default constructor will set a default {@link GeoLocation#GeoLocation()}, a default
	 * {@link AstronomicalCalculator#getDefault() AstronomicalCalculator} and default the calendar to the current date.
	 */
	public AstronomicalCalendar() {
		this(new GeoLocation());
	}

	/**
	 * A constructor that takes in <a href="http://en.wikipedia.org/wiki/Geolocation">geolocation</a> information as a
	 * parameter.
	 * 
	 * @param geoLocation
	 *            The location information used for calculating astronomical sun times.
	 */
	public AstronomicalCalendar(GeoLocation geoLocation) {
		setCalendar(Calendar.getInstance(geoLocation.getTimeZone()));
		setGeoLocation(geoLocation);// duplicate call
		setAstronomicalCalculator(AstronomicalCalculator.getDefault());
	}

	/**
	 * A method that returns the sunrise in UTC time without correction for time zone offset from GMT and without using
	 * daylight savings time.
	 * 
	 * @param zenith
	 *            the degrees below the horizon. For time after sunrise use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 */
	public double getUTCSunrise(double zenith) {
		return getAstronomicalCalculator().getUTCSunrise(getAdjustedCalendar(), getGeoLocation(), zenith, true);
	}

	/**
	 * A method that returns the sunrise in UTC time without correction for time zone offset from GMT and without using
	 * daylight savings time. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible
	 * light, something that is not affected by elevation. This method returns UTC sunrise calculated at sea level. This
	 * forms the base for dawn calculations that are calculated as a dip below the horizon before sunrise.
	 * 
	 * @param zenith
	 *            the degrees below the horizon. For time after sunrise use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalendar#getUTCSunrise
	 * @see AstronomicalCalendar#getUTCSeaLevelSunset
	 */
	public double getUTCSeaLevelSunrise(double zenith) {
		return getAstronomicalCalculator().getUTCSunrise(getAdjustedCalendar(), getGeoLocation(), zenith, false);
	}

	/**
	 * A method that returns the sunset in UTC time without correction for time zone offset from GMT and without using
	 * daylight savings time.
	 * 
	 * @param zenith
	 *            the degrees below the horizon. For time after sunset use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalendar#getUTCSeaLevelSunset
	 */
	public double getUTCSunset(double zenith) {
		return getAstronomicalCalculator().getUTCSunset(getAdjustedCalendar(), getGeoLocation(), zenith, true);
	}

	/**
	 * A method that returns the sunset in UTC time without correction for elevation, time zone offset from GMT and
	 * without using daylight savings time. Non-sunrise and sunset calculations such as dawn and dusk, depend on the
	 * amount of visible light, something that is not affected by elevation. This method returns UTC sunset calculated
	 * at sea level. This forms the base for dusk calculations that are calculated as a dip below the horizon after
	 * sunset.
	 * 
	 * @param zenith
	 *            the degrees below the horizon. For time before sunset use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalendar#getUTCSunset
	 * @see AstronomicalCalendar#getUTCSeaLevelSunrise
	 */
	public double getUTCSeaLevelSunset(double zenith) {
		return getAstronomicalCalculator().getUTCSunset(getAdjustedCalendar(), getGeoLocation(), zenith, false);
	}

	/**
	 * A method that returns an {@link AstronomicalCalculator#getElevationAdjustment(double) elevation adjusted}
	 * temporal (solar) hour. The day from {@link #getSunrise() sunrise} to {@link #getSunset() sunset} is split into 12
	 * equal parts with each one being a temporal hour.
	 * 
	 * @see #getSunrise()
	 * @see #getSunset()
	 * @see #getTemporalHour(Date, Date)
	 * 
	 * @return the <code>long</code> millisecond length of a temporal hour. If the calculation can't be computed,
	 *         {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the page.
	 * 
	 * @see #getTemporalHour(Date, Date)
	 */
	public long getTemporalHour() {
		return getTemporalHour(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * A utility method that will allow the calculation of a temporal (solar) hour based on the sunrise and sunset
	 * passed as parameters to this method. An example of the use of this method would be the calculation of a
	 * non-elevation adjusted temporal hour by passing in {@link #getSeaLevelSunrise() sea level sunrise} and
	 * {@link #getSeaLevelSunset() sea level sunset} as parameters.
	 * 
	 * @param startOfday
	 *            The start of the day.
	 * @param endOfDay
	 *            The end of the day.
	 * 
	 * @return the <code>long</code> millisecond length of the temporal hour. If the calculation can't be computed a
	 *         {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the page.
	 * 
	 * @see #getTemporalHour()
	 */
	public long getTemporalHour(Date startOfday, Date endOfDay) {
		if (startOfday == null || endOfDay == null) {
			return Long.MIN_VALUE;
		}
		return (endOfDay.getTime() - startOfday.getTime()) / 12;
	}

	/**
	 * A method that returns sundial or solar noon. It occurs when the Sun is <a href
	 * ="http://en.wikipedia.org/wiki/Transit_%28astronomy%29">transitting</a> the <a
	 * href="http://en.wikipedia.org/wiki/Meridian_%28astronomy%29">celestial meridian</a>. In this class it is
	 * calculated as halfway between sea level sunrise and sea level sunset, which can be slightly off the real transit
	 * time due to changes in declination (the lengthening or shortening day).
	 * 
	 * @return the <code>Date</code> representing Sun's transit. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, null will be returned. See detailed explanation on top of the page.
	 * @see #getSunTransit(Date, Date)
	 * @see #getTemporalHour()
	 */
	public Date getSunTransit() {
		return getSunTransit(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * A method that returns sundial or solar noon. It occurs when the Sun is <a href
	 * ="http://en.wikipedia.org/wiki/Transit_%28astronomy%29">transitting</a> the <a
	 * href="http://en.wikipedia.org/wiki/Meridian_%28astronomy%29">celestial meridian</a>. In this class it is
	 * calculated as halfway between the sunrise and sunset passed to this method. This time can be slightly off the
	 * real transit time due to changes in declination (the lengthening or shortening day).
	 * 
	 * @param startOfDay
	 *            the start of day for calculating the sun's transit. This can be sea level sunrise, visual sunrise (or
	 *            any arbitrary start of day) passed to this method.
	 * @param endOfDay
	 *            the end of day for calculating the sun's transit. This can be sea level sunset, visual sunset (or any
	 *            arbitrary end of day) passed to this method.
	 * 
	 * @return the <code>Date</code> representing Sun's transit. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, null will be returned. See detailed explanation on top of the page.
	 */
	public Date getSunTransit(Date startOfDay, Date endOfDay) {
		long temporalHour = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, temporalHour * 6);
	}

	/**
	 * A method that returns a <code>Date</code> from the time passed in as a parameter.
	 * 
	 * @param time
	 *            The time to be set as the time for the <code>Date</code>. The time expected is in the format: 18.75
	 *            for 6:45:00 PM.time is sunrise and false if it is sunset
	 * @param isSunrise true if the 
	 * @return The Date.
	 */
	protected Date getDateFromTime(double time, boolean isSunrise) {
		if (Double.isNaN(time)) {
			return null;
		}
		double calculatedTime = time;
		
		Calendar adjustedCalendar = getAdjustedCalendar();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.clear();// clear all fields
		cal.set(Calendar.YEAR, adjustedCalendar.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, adjustedCalendar.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, adjustedCalendar.get(Calendar.DAY_OF_MONTH));

		int hours = (int) calculatedTime; // retain only the hours
		calculatedTime -= hours;
		int minutes = (int) (calculatedTime *= 60); // retain only the minutes
		calculatedTime -= minutes;
		int seconds = (int) (calculatedTime *= 60); // retain only the seconds
		calculatedTime -= seconds; // remaining milliseconds
		
		// Check if a date transition has occurred, or is about to occur - this indicates the date of the event is
		// actually not the target date, but the day prior or after
		int localTimeHours = (int)getGeoLocation().getLongitude() / 15;
		if (isSunrise && localTimeHours + hours > 18) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		} else if (!isSunrise && localTimeHours + hours < 6) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.MILLISECOND, (int) (calculatedTime * 1000));
		return cal.getTime();
	}

	/**
	 * Returns the dip below the horizon before sunrise that matches the offset minutes on passed in as a parameter. For
	 * example passing in 72 minutes for a calendar set to the equinox in Jerusalem returns a value close to 16.1&deg;
	 * Please note that this method is very slow and inefficient and should NEVER be used in a loop. TODO: Improve
	 * efficiency.
	 * 
	 * @param minutes
	 *            offset
	 * @return the degrees below the horizon before sunrise that match the offset in minutes passed it as a parameter.
	 * @see #getSunsetSolarDipFromOffset(double)
	 */
	public double getSunriseSolarDipFromOffset(double minutes) {
		Date offsetByDegrees = getSeaLevelSunrise();
		Date offsetByTime = getTimeOffset(getSeaLevelSunrise(), -(minutes * MINUTE_MILLIS));

		BigDecimal degrees = new BigDecimal(0);
		BigDecimal incrementor = new BigDecimal("0.0001");
		while (offsetByDegrees == null || offsetByDegrees.getTime() > offsetByTime.getTime()) {
			degrees = degrees.add(incrementor);
			offsetByDegrees = getSunriseOffsetByDegrees(GEOMETRIC_ZENITH + degrees.doubleValue());
		}
		return degrees.doubleValue();
	}

	/**
	 * Returns the dip below the horizon after sunset that matches the offset minutes on passed in as a parameter. For
	 * example passing in 72 minutes for a calendar set to the equinox in Jerusalem returns a value close to 16.1&deg;
	 * Please note that this method is very slow and inefficient and should NEVER be used in a loop. TODO: Improve
	 * efficiency.
	 * 
	 * @param minutes
	 *            offset
	 * @return the degrees below the horizon after sunset that match the offset in minutes passed it as a parameter.
	 * @see #getSunriseSolarDipFromOffset(double)
	 */
	public double getSunsetSolarDipFromOffset(double minutes) {
		Date offsetByDegrees = getSeaLevelSunset();
		Date offsetByTime = getTimeOffset(getSeaLevelSunset(), minutes * MINUTE_MILLIS);

		BigDecimal degrees = new BigDecimal(0);
		BigDecimal incrementor = new BigDecimal("0.001");
		while (offsetByDegrees == null || offsetByDegrees.getTime() < offsetByTime.getTime()) {
			degrees = degrees.add(incrementor);
			offsetByDegrees = getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + degrees.doubleValue());
		}
		return degrees.doubleValue();
	}
	
	/**
	 * Adjusts the <code>Calendar</code> to deal with edge cases where the location crosses the antimeridian.
	 * 
	 * @see GeoLocation#getAntimeridianAdjustment()
	 * @return the adjusted Calendar
	 */
	private Calendar getAdjustedCalendar(){
		int offset = getGeoLocation().getAntimeridianAdjustment();
		if (offset == 0) {
			return getCalendar();
		}
		Calendar adjustedCalendar = (Calendar) getCalendar().clone();
		adjustedCalendar.add(Calendar.DAY_OF_MONTH, offset);
		return adjustedCalendar;
	}

	/**
	 * @return an XML formatted representation of the class. It returns the default output of the
	 *         {@link net.sourceforge.zmanim.util.ZmanimFormatter#toXML(AstronomicalCalendar) toXML} method.
	 * @see net.sourceforge.zmanim.util.ZmanimFormatter#toXML(AstronomicalCalendar)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ZmanimFormatter.toXML(this);
	}
	
	/**
	 * @return a JSON formatted representation of the class. It returns the default output of the
	 *         {@link net.sourceforge.zmanim.util.ZmanimFormatter#toJSON(AstronomicalCalendar) toJSON} method.
	 * @see net.sourceforge.zmanim.util.ZmanimFormatter#toJSON(AstronomicalCalendar)
	 * @see java.lang.Object#toString()
	 */
	public String toJSON() {
		return ZmanimFormatter.toJSON(this);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof AstronomicalCalendar)) {
			return false;
		}
		AstronomicalCalendar aCal = (AstronomicalCalendar) object;
		return getCalendar().equals(aCal.getCalendar()) && getGeoLocation().equals(aCal.getGeoLocation())
				&& getAstronomicalCalculator().equals(aCal.getAstronomicalCalculator());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getClass().hashCode(); // needed or this and subclasses will return identical hash
		result += 37 * result + getCalendar().hashCode();
		result += 37 * result + getGeoLocation().hashCode();
		result += 37 * result + getAstronomicalCalculator().hashCode();
		return result;
	}

	/**
	 * A method that returns the currently set {@link GeoLocation} which contains location information used for the
	 * astronomical calculations.
	 * 
	 * @return Returns the geoLocation.
	 */
	public GeoLocation getGeoLocation() {
		return this.geoLocation;
	}

	/**
	 * Sets the {@link GeoLocation} <code>Object</code> to be used for astronomical calculations.
	 * 
	 * @param geoLocation
	 *            The geoLocation to set.
	 */
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
		getCalendar().setTimeZone(geoLocation.getTimeZone());
	}

	/**
	 * A method that returns the currently set AstronomicalCalculator.
	 * 
	 * @return Returns the astronomicalCalculator.
	 * @see #setAstronomicalCalculator(AstronomicalCalculator)
	 */
	public AstronomicalCalculator getAstronomicalCalculator() {
		return this.astronomicalCalculator;
	}

	/**
	 * A method to set the {@link AstronomicalCalculator} used for astronomical calculations. The Zmanim package ships
	 * with a number of different implementations of the <code>abstract</code> {@link AstronomicalCalculator} based on
	 * different algorithms, including {@link net.sourceforge.zmanim.util.SunTimesCalculator one implementation} based
	 * on the <a href = "http://aa.usno.navy.mil/">US Naval Observatory's</a> algorithm, and
	 * {@link net.sourceforge.zmanim.util.NOAACalculator another} based on <a href="http://noaa.gov">NOAA's</a>
	 * algorithm. This allows easy runtime switching and comparison of different algorithms.
	 * 
	 * @param astronomicalCalculator
	 *            The astronomicalCalculator to set.
	 */
	public void setAstronomicalCalculator(AstronomicalCalculator astronomicalCalculator) {
		this.astronomicalCalculator = astronomicalCalculator;
	}

	/**
	 * returns the Calendar object encapsulated in this class.
	 * 
	 * @return Returns the calendar.
	 */
	public Calendar getCalendar() {
		return this.calendar;
	}

	/**
	 * @param calendar
	 *            The calendar to set.
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if (getGeoLocation() != null) {// if available set the Calendar's timezone to the GeoLocation TimeZone
			getCalendar().setTimeZone(getGeoLocation().getTimeZone());
		}
	}

	/**
	 * A method that creates a <a href="http://en.wikipedia.org/wiki/Object_copy#Deep_copy">deep copy</a> of the object.
	 * <b>Note:</b> If the {@link java.util.TimeZone} in the cloned {@link net.sourceforge.zmanim.util.GeoLocation} will
	 * be changed from the original, it is critical that
	 * {@link net.sourceforge.zmanim.AstronomicalCalendar#getCalendar()}.
	 * {@link java.util.Calendar#setTimeZone(TimeZone) setTimeZone(TimeZone)} be called in order for the
	 * AstronomicalCalendar to output times in the expected offset after being cloned.
	 * 
	 * @see java.lang.Object#clone()
	 * @since 1.1
	 */
	public Object clone() {
		AstronomicalCalendar clone = null;
		try {
			clone = (AstronomicalCalendar) super.clone();
		} catch (CloneNotSupportedException cnse) {
			// Required by the compiler. Should never be reached since we implement clone()
		}
		clone.setGeoLocation((GeoLocation) getGeoLocation().clone());
		clone.setCalendar((Calendar) getCalendar().clone());
		clone.setAstronomicalCalculator((AstronomicalCalculator) getAstronomicalCalculator().clone());
		return clone;
	}
}
