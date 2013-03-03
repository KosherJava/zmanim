/*
 * Zmanim Java API
 * Copyright (C) 2004-2013 Eliyahu Hershfeld
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
package net.sourceforge.zmanim;

import java.util.Calendar;
import java.util.Date;
import net.sourceforge.zmanim.util.AstronomicalCalculator;
import net.sourceforge.zmanim.util.GeoLocation;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;

/**
 * This class extends ZmanimCalendar and provides many more zmanim than available in the ZmanimCalendar. The basis for
 * most zmanim in this class are from the <em>sefer</em> <b>Yisroel Vehazmanim</b> by <b>Rabbi Yisroel Dovid
 * Harfenes</b>. <br />
 * As an example of the number of different <em>zmanim</em> made available by this class, there are methods to return 12
 * different calculations for <em>alos</em> (dawn) available in this class. The real power of this API is the ease in
 * calculating <em>zmanim</em> that are not part of the API. The methods for doing <em>zmanim</em> calculations not
 * present in this class or it's superclass the {@link ZmanimCalendar} are contained in the {@link AstronomicalCalendar}
 * , the base class of the calendars in our API since they are generic methods for calculating time based on degrees or
 * time before or after {@link #getSunrise sunrise} and {@link #getSunset sunset} and are of interest for calculation
 * beyond <em>zmanim</em> calculations. Here are some examples: <br />
 * First create the Calendar for the location you would like to calculate:
 * 
 * <pre>
 * String locationName = &quot;Lakewood, NJ&quot;;
 * double latitude = 40.0828; // Lakewood, NJ
 * double longitude = -74.2094; // Lakewood, NJ
 * double elevation = 0;
 * // the String parameter in getTimeZone() has to be a valid timezone listed in
 * // {@link java.util.TimeZone#getAvailableIDs()}
 * TimeZone timeZone = TimeZone.getTimeZone(&quot;America/New_York&quot;);
 * GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
 * ComplexZmanimCalendar czc = new ComplexZmanimCalendar(location);
 * // Optionally set the date or it will default to today's date
 * czc.getCalendar().set(Calendar.MONTH, Calendar.FEBRUARY);
 * czc.getCalendar().set(Calendar.DAY_OF_MONTH, 8);
 * </pre>
 * 
 * <b>Note:</b> For locations such as Israel where the beginning and end of daylight savings time can fluctuate from
 * year to year create a {@link java.util.SimpleTimeZone} with the known start and end of DST. <br />
 * To get <em>alos</em> calculated as 14&deg; below the horizon (as calculated in the calendars published in Montreal),
 * add {@link AstronomicalCalendar#ASTRONOMICAL_ZENITH} (90) to the 14&deg; offset to get the desired time:
 * 
 * <pre>
 * Date alos14 = czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#ASTRONOMICAL_ZENITH} + 14);
 * </pre>
 * 
 * To get <em>mincha gedola</em> calculated based on the MGA using a <em>shaah
 * zmanis</em> based on the day starting 16.1&deg; below the horizon (and ending 16.1&deg; after sunset) the following
 * calculation can be used:
 * 
 * <pre>
 * Date minchaGedola = czc.getTimeOffset(czc.getAlos16point1Degrees(), czc.getShaahZmanis16Point1Degrees() * 6.5);
 * </pre>
 * 
 * A little more complex example would be calculating <em>plag hamincha</em> based on a <em>shaah zmanis</em> that was
 * not present in this class. While a drop more complex it is still rather easy. For example if you wanted to calculate
 * <em>plag</em> based on the day starting 12&deg; before sunrise and ending 12&deg; after sunset as calculated in the
 * calendars in Manchester, England (there is nothing that would prevent your calculating the day using sunrise and
 * sunset offsets that are not identical degrees, but this would lead to <em>chatzos</em> being a time other than the
 * {@link #getSunTransit() solar transit} (solar midday)). The steps involved would be to first calculate the
 * <em>shaah zmanis</em> and then use that time in milliseconds to calculate 10.75 hours after sunrise starting at
 * 12&deg; before sunset
 * 
 * <pre>
 * long shaahZmanis = czc.getTemporalHour(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#ASTRONOMICAL_ZENITH} + 12),
 * 						czc.getSunsetOffsetByDegrees({@link AstronomicalCalendar#ASTRONOMICAL_ZENITH} + 12));
 * Date plag = getTimeOffset(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#ASTRONOMICAL_ZENITH} + 12), 
 * 					shaahZmanis * 10.75);
 * </pre>
 * 
 * <h2>Disclaimer:</h2> While I did my best to get accurate results please do not rely on these zmanim for
 * <em>halacha lemaaseh</em>
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2013
 */
public class ComplexZmanimCalendar extends ZmanimCalendar {

	/**
	 * The zenith of 3.7&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> that <em>tzais</em> is the
	 * time it takes to walk 3/4 of a </em>Mil</em> at 18 minutes a <em>Mil</em>, or 13.5 minutes after sunset. The sun
	 * is 3.7&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} at this time in Jerusalem on March 16, about 4 days
	 * before the equinox, the day that a solar hour is 60 minutes.
	 * 
	 * TODO AT see #getTzaisGeonim3Point7Degrees()
	 */
	protected static final double ZENITH_3_POINT_7 = GEOMETRIC_ZENITH + 3.7;

	/**
	 * The zenith of 5.95&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun 24 minutes after sunset in Jerusalem on March 16, about 4 days before the equinox, the day that a solar
	 * hour is 60 minutes, which calculates to 5.95&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getTzaisGeonim5Point95Degrees()
	 */
	protected static final double ZENITH_5_POINT_95 = GEOMETRIC_ZENITH + 5.95;

	/**
	 * The zenith of 7.083&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This is often referred to as
	 * 7&deg;5' or 7&deg; and 5 minutes. This calculation is used for calculating <em>alos</em> (dawn) and
	 * <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of the sun 30
	 * minutes after sunset in Jerusalem on March 16, about 4 days before the equinox, the day that a solar hour is 60
	 * minutes, which calculates to 7.0833333&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. This is time some
	 * opinions consider dark enough for 3 stars to be visible. This is the opinion of the
	 * <em>Sh"Ut Melamed Leho'il</em>, <em>Sh"Ut Binyan Tziyon</em>, <em>Tenuvas Sadeh</em> and very close to the time
	 * of the <em>Mekor Chesed</em> on the <em>Sefer chasidim</em>.
	 * 
	 * @see #getTzaisGeonim7Point083Degrees()
	 * @see #getBainHasmashosRT13Point5MinutesBefore7Point083Degrees()
	 */
	protected static final double ZENITH_7_POINT_083 = GEOMETRIC_ZENITH + 7 + (5 / 60);

	/**
	 * The zenith of 10.2&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 45 minutes before {@link #getSunrise sunrise} in Jerusalem on March 16, about 4 days before the equinox, the day
	 * that a solar hour is 60 minutes which calculates to 10.2&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getMisheyakir10Point2Degrees()
	 */
	protected static final double ZENITH_10_POINT_2 = GEOMETRIC_ZENITH + 10.2;

	/**
	 * The zenith of 11&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 48 minutes before {@link #getSunrise sunrise} in Jerusalem on March 16, about 4 days before the equinox, the day
	 * that a solar hour is 60 minutes which calculates to 11&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getMisheyakir11Degrees()
	 */
	protected static final double ZENITH_11_DEGREES = GEOMETRIC_ZENITH + 11;

	/**
	 * The zenith of 11.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 52 minutes before {@link #getSunrise sunrise} in Jerusalem on March 16, about 4 days before the equinox, the day
	 * that a solar hour is 60 minutes which calculates to 11.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getMisheyakir11Point5Degrees()
	 */
	protected static final double ZENITH_11_POINT_5 = GEOMETRIC_ZENITH + 11.5;

	/**
	 * The zenith of 13.24&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>Rabainu Tam's bain hashmashos</em> according to some opinions. <br/>
	 * <br/>
	 * NOTE: See comments on {@link #getBainHasmashosRT13Point24Degrees} for additional details about the degrees.
	 * 
	 * @see #getBainHasmashosRT13Point24Degrees
	 * 
	 */
	protected static final double ZENITH_13_POINT_24 = GEOMETRIC_ZENITH + 13.24;

	/**
	 * The zenith of 19.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> (dawn) and <em>tzais</em> (nightfall) according to some opinions. This calculation is
	 * based on the position of the sun 90 minutes after sunset in Jerusalem on March 16, about 4 days before the
	 * equinox, the day that a solar hour is 60 minutes which calculates to 19.8&deg; below {@link #GEOMETRIC_ZENITH
	 * geometric zenith}
	 * 
	 * @see #getTzais19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 * @see #getAlos90()
	 * @see #getTzais90()
	 */
	protected static final double ZENITH_19_POINT_8 = GEOMETRIC_ZENITH + 19.8;

	/**
	 * The zenith of 26&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> (dawn) and <em>tzais</em> (nightfall) according to some opinions. This calculation is
	 * based on the position of the sun {@link #getAlos120() 120 minutes} after sunset in Jerusalem on March 16, about 4
	 * days before the equinox, the day that a solar hour is 60 minutes which calculates to 26&deg; below
	 * {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getAlos26Degrees()
	 * @see #getTzais26Degrees()
	 * @see #getAlos120()
	 * @see #getTzais120()
	 */
	protected static final double ZENITH_26_DEGREES = GEOMETRIC_ZENITH + 26.0;

	/**
	 * Experimental and may not make the final 1.3 cut
	 */

	/**
	 * The zenith of 4.37&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim4Point37Degrees() 16 7/8 minutes} after sunset (3/4 of a 22.5 minute Mil) in
	 * Jerusalem on March 16, about 4 days before the equinox, the day that a solar hour is 60 minutes which calculates
	 * to 4.37&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getTzaisGeonim4Point37Degrees()
	 */
	protected static final double ZENITH_4_POINT_37 = GEOMETRIC_ZENITH + 4.37;

	/**
	 * The zenith of 4.61&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim4Point37Degrees() 18 minutes} after sunset (3/4 of a 24 minute Mil) in Jerusalem on
	 * March 16, about 4 days before the equinox, the day that a solar hour is 60 minutes which calculates to 4.61&deg;
	 * below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getTzaisGeonim4Point61Degrees()
	 */
	protected static final double ZENITH_4_POINT_61 = GEOMETRIC_ZENITH + 4.61;

	protected static final double ZENITH_4_POINT_8 = GEOMETRIC_ZENITH + 4.8;

	/**
	 * The zenith of 3.65&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim3Point65Degrees() 13.5 minutes} after sunset (3/4 of an 18 minute Mil) in Jerusalem
	 * on March 16, about 4 days before the equinox, the day that a solar hour is 60 minutes which calculates to
	 * 3.65&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @see #getTzaisGeonim3Point65Degrees()
	 */
	protected static final double ZENITH_3_POINT_65 = GEOMETRIC_ZENITH + 3.65;

	protected static final double ZENITH_3_POINT_676 = GEOMETRIC_ZENITH + 3.676;

	protected static final double ZENITH_5_POINT_88 = GEOMETRIC_ZENITH + 5.88;

	private double ateretTorahSunsetOffset = 40;

	public ComplexZmanimCalendar(GeoLocation location) {
		super(location);
	}

	/**
	 * Default constructor will set a default {@link GeoLocation#GeoLocation()}, a default
	 * {@link AstronomicalCalculator#getDefault() AstronomicalCalculator} and default the calendar to the current date.
	 * 
	 * @see AstronomicalCalendar#AstronomicalCalendar()
	 */
	public ComplexZmanimCalendar() {
		super();
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a 19.8&deg; dip. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * when the sun is 19.8&deg; below the eastern geometric horizon before sunrise. Dusk for this is when the sun is
	 * 19.8&deg; below the western geometric horizon after sunset. This day is split into 12 equal parts with each part
	 * being a <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a {@link Long#MIN_VALUE}
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis19Point8Degrees() {
		return getTemporalHour(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a 18&deg; dip. This calculation divides
	 * the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is when
	 * the sun is 18&deg; below the eastern geometric horizon before sunrise. Dusk for this is when the sun is 18&deg;
	 * below the western geometric horizon after sunset. This day is split into 12 equal parts with each part being a
	 * <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a {@link Long#MIN_VALUE}
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis18Degrees() {
		return getTemporalHour(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a dip of 26&deg;. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * when the sun is {@link #getAlos26Degrees() 26&deg;} below the eastern geometric horizon before sunrise. Dusk for
	 * this is when the sun is {@link #getTzais26Degrees() 26&deg;} below the western geometric horizon after sunset.
	 * This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a {@link Long#MIN_VALUE}
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis26Degrees() {
		return getTemporalHour(getAlos26Degrees(), getTzais26Degrees());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a dip of 16.1&deg;. This calculation
	 * divides the day based on the opinion that the day runs from dawn to dusk. Dawn for this calculation is when the
	 * sun is 16.1&deg; below the eastern geometric horizon before sunrise and dusk is when the sun is 16.1&deg; below
	 * the western geometric horizon after sunset. This day is split into 12 equal parts with each part being a
	 * <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a {@link Long#MIN_VALUE}
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos16Point1Degrees()
	 * @see #getTzais16Point1Degrees()
	 * @see #getSofZmanShmaMGA16Point1Degrees()
	 * @see #getSofZmanTfilaMGA16Point1Degrees()
	 * @see #getMinchaGedola16Point1Degrees()
	 * @see #getMinchaKetana16Point1Degrees()
	 * @see #getPlagHamincha16Point1Degrees()
	 */

	public long getShaahZmanis16Point1Degrees() {
		return getTemporalHour(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (solar hour) according to the opinion of the MGA. This calculation
	 * divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk. Dawn for this
	 * calculation is 60 minutes before sunrise and dusk is 60 minutes after sunset. This day is split into 12 equal
	 * parts with each part being a <em>shaah zmanis</em>. Alternate mothods of calculating a <em>shaah zmanis</em> are
	 * available in the subclass {@link ComplexZmanimCalendar}
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis60Minutes() {
		return getTemporalHour(getAlos60(), getTzais60());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (solar hour) according to the opinion of the MGA. This calculation
	 * divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk. Dawn for this
	 * calculation is 72 minutes before sunrise and dusk is 72 minutes after sunset. This day is split into 12 equal
	 * parts with each part being a <em>shaah zmanis</em>. Alternate mothods of calculating a <em>shaah zmanis</em> are
	 * available in the subclass {@link ComplexZmanimCalendar}
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis72Minutes() {
		return getShaahZmanisMGA();
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the MGA based on
	 * <em>alos</em> being {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}.
	 * This calculation divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk.
	 * Dawn for this calculation is 72 minutes <em>zmaniyos</em> before sunrise and dusk is 72 minutes <em>zmaniyos</em>
	 * after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. This is
	 * identical to 1/10th of the day from {@link #getSunrise() sunrise} to {@link #getSunset() sunset}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzais72Zmanis()
	 */
	public long getShaahZmanis72MinutesZmanis() {
		return getTemporalHour(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a dip of 90 minutes. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * 90 minutes before sunrise and dusk is 90 minutes after sunset. This day is split into 12 equal parts with each
	 * part being a <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis90Minutes() {
		return getTemporalHour(getAlos90(), getTzais90());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the MGA based on
	 * <em>alos</em> being {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}.
	 * This calculation divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk.
	 * Dawn for this calculation is 90 minutes <em>zmaniyos</em> before sunrise and dusk is 90 minutes <em>zmaniyos</em>
	 * after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. This is
	 * identical to 1/8th of the day from {@link #getSunrise() sunrise} to {@link #getSunset() sunset}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos90Zmanis()
	 * @see #getTzais90Zmanis()
	 */
	public long getShaahZmanis90MinutesZmanis() {
		return getTemporalHour(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the MGA based on
	 * <em>alos</em> being {@link #getAlos96Zmanis() 96} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}.
	 * This calculation divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk.
	 * Dawn for this calculation is 96 minutes <em>zmaniyos</em> before sunrise and dusk is 96 minutes <em>zmaniyos</em>
	 * after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. This is
	 * identical to 1/7.5th of the day from {@link #getSunrise() sunrise} to {@link #getSunset() sunset}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos96Zmanis()
	 * @see #getTzais96Zmanis()
	 */
	public long getShaahZmanis96MinutesZmanis() {
		return getTemporalHour(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the
	 * <em>Chacham Yosef Harari-Raful</em> of <em>Yeshivat Ateret Torah</em> calculated with <em>alos</em> being 1/10th
	 * of sunrise to sunset day, or {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em> of such a day before
	 * {@link #getSunrise() sunrise}, and <em>tzais</em> is usually calculated as {@link #getTzaisAteretTorah() 40
	 * minutes} (configurable to any offset via {@link #setAteretTorahSunsetOffset(double)}) after {@link #getSunset()
	 * sunset}. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. Note that with this
	 * system, <em>chatzos</em> (mid-day) will not be the point that the sun is {@link #getSunTransit() halfway across
	 * the sky}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public long getShaahZmanisAteretTorah() {
		return getTemporalHour(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a dip of 96 minutes. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * 96 minutes before sunrise and dusk is 96 minutes after sunset. This day is split into 12 equal parts with each
	 * part being a <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis96Minutes() {
		return getTemporalHour(getAlos96(), getTzais96());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) calculated using a dip of 120 minutes. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * 120 minutes before sunrise and dusk is 120 minutes after sunset. This day is split into 12 equal parts with each
	 * part being a <em>shaah zmanis</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanis120Minutes() {
		return getTemporalHour(getAlos120(), getTzais120());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the MGA based on
	 * <em>alos</em> being {@link #getAlos120Zmanis() 120} minutes <em>zmaniyos</em> before {@link #getSunrise()
	 * sunrise}. This calculation divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn
	 * to dusk. Dawn for this calculation is 120 minutes <em>zmaniyos</em> before sunrise and dusk is 120 minutes
	 * <em>zmaniyos</em> after sunset. This day is split into 12 equal parts with each part being a
	 * <em>shaah zmanis</em>. This is identical to 1/6th of the day from {@link #getSunrise() sunrise} to
	 * {@link #getSunset() sunset}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos120Zmanis()
	 * @see #getTzais120Zmanis()
	 */
	public long getShaahZmanis120MinutesZmanis() {
		return getTemporalHour(getAlos120Zmanis(), getTzais120Zmanis());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on sunrise being 120 minutes <em>zmaniyos</em>(
	 * <em>GRA</em> and the <em>Baal Hatanya</em>) or 1/6th of the day before sea level sunrise. This is calculated as
	 * 10.75 hours after {@link #getAlos120Zmanis() dawn}. The formula used is:<br/>
	 * 10.75 * {@link #getShaahZmanis120MinutesZmanis()} after {@link #getAlos120Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis120MinutesZmanis()
	 */
	public Date getPlagHamincha120MinutesZmanis() {
		return getPlagHamincha(getAlos120Zmanis(), getTzais120Zmanis());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the <em>Magen Avraham</em> with the day
	 * starting 120 minutes before sunrise and ending 120 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos120() dawn 120 minutes}. The formula used is:<br/>
	 * 10.75 {@link #getShaahZmanis120Minutes()} after {@link #getAlos120()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis120Minutes()
	 */
	public Date getPlagHamincha120Minutes() {
		return getPlagHamincha(getAlos120(), getTzais120());
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated as 60 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise}. This is the time to walk the distance of 4 <em>Mil</em> at 15 minutes a <em>Mil</em> (the opinion of
	 * the Chavas Yair. See the Divray Malkiel). Time based offset calculations for <em>alos</em> are based on the
	 * opinion of most <em>Rishonim</em> who stated that the time of the <em>Neshef</em> (time between dawn and sunrise)
	 * does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4
	 * <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos60() {
		return getTimeOffset(getSeaLevelSunrise(), -60 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 72 minutes <em>zmaniyos</em>( <em>GRA</em> and the
	 * <em>Baal Hatanya</em>) or 1/10th of the day before sea level sunrise. This is based on an 18 minute <em>Mil</em>
	 * so the time for 4 <em>Mil</em> is 72 minutes which is 1/10th of a day (12 * 60 = 720) based on the day starting
	 * at {@link #getSeaLevelSunrise() sea level sunrise} and ending at {@link #getSeaLevelSunset() sea level sunset}.
	 * The actual alculation is {@link #getSeaLevelSunrise()}- ( {@link #getShaahZmanisGra()} * 1.2). This calculation
	 * is used in the calendars published by <em>Hisachdus Harabanim D'Artzos Habris Ve'Canada</em>
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Date getAlos72Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunrise(), (long) (shaahZmanis * -1.2));
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 96 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise} based on the time to walk the distance of 4 <em>Mil</em> at 24 minutes a <em>Mil</em>. Time based offset
	 * calculations for <em>alos</em> are based on the opinion of most <em>Rishonim</em> who stated that the time of the
	 * <em>Neshef</em> (time between dawn and sunrise) does not vary by the time of year or location but purely depends
	 * on the time it takes to walk the distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos96() {
		return getTimeOffset(getSeaLevelSunrise(), -96 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 90 minutes <em>zmaniyos</em>( <em>GRA</em> and the
	 * <em>Baal Hatanya</em>) or 1/8th of the day before sea level sunrise. This is based on a 22.5 minute <em>Mil</em>
	 * so the time for 4 <em>Mil</em> is 90 minutes which is 1/8th of a day (12 * 60) / 8 = 90 based on the day starting
	 * at {@link #getSunrise() sunrise} and ending at {@link #getSunset() sunset}. The actual calculation is
	 * {@link #getSunrise()} - ( {@link #getShaahZmanisGra()} * 1.5).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Date getAlos90Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunrise(), (long) (shaahZmanis * -1.5));
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 96 minutes <em>zmaniyos</em>( <em>GRA</em> and the
	 * <em>Baal Hatanya</em>) or 1/8th of the day before sea level sunrise. This is based on a 24 minute <em>Mil</em> so
	 * the time for 4 <em>Mil</em> is 96 minutes which is 1/7.5th of a day (12 * 60) / 7.5 = 96 based on the day
	 * starting at {@link #getSunrise() sunrise} and ending at {@link #getSunset() sunset}. The actual calculation is
	 * {@link #getSunrise()} - ( {@link #getShaahZmanisGra()} * 1.6).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Date getAlos96Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunrise(), (long) (shaahZmanis * -1.6));
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 90 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise} based on the time to walk the distance of 4 <em>Mil</em> at 22.5 minutes a <em>Mil</em>. Time based
	 * offset calculations for <em>alos</em> are based on the opinion of most <em>Rishonim</em> who stated that the time
	 * of the <em>Neshef</em> (time between dawn and sunrise) does not vary by the time of year or location but purely
	 * depends on the time it takes to walk the distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos90() {
		return getTimeOffset(getSeaLevelSunrise(), -90 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 120 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise} (no adjustment for elevation is made) based on the time to walk the distance of 5 <em>Mil</em>(
	 * <em>Ula</em>) at 24 minutes a <em>Mil</em>. Time based offset calculations for <em>alos</em> are based on the
	 * opinion of most <em>Rishonim</em> who stated that the time of the <em>Neshef</em> (time between dawn and sunrise)
	 * does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 5
	 * <em>Mil</em>(<em>Ula</em>).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos120() {
		return getTimeOffset(getSeaLevelSunrise(), -120 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 120 minutes <em>zmaniyos</em>( <em>GRA</em> and the
	 * <em>Baal Hatanya</em>) or 1/6th of the day before sea level sunrise. This is based on a 24 minute <em>Mil</em> so
	 * the time for 5 <em>Mil</em> is 120 minutes which is 1/6th of a day (12 * 60) / 6 = 120 based on the day starting
	 * at {@link #getSunrise() sunrise} and ending at {@link #getSunset() sunset}. The actual calculation is
	 * {@link #getSunrise()} - ( {@link #getShaahZmanisGra()} * 2).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Date getAlos120Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunrise(), shaahZmanis * -2);
	}

	/**
	 * A method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_26_DEGREES 26&deg;} below the
	 * eastern geometric horizon before sunrise. This calculation is based on the same calculation of
	 * {@link #getAlos120() 120 minutes} but uses a degree based calculation instead of 120 exact minutes. This
	 * calculation is based on the position of the sun 120 minutes before sunrise in Jerusalem during the equinox which
	 * calculates to 26&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_26_DEGREES
	 * @see #getAlos120()
	 * @see #getTzais120()
	 */
	public Date getAlos26Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_26_DEGREES);
	}

	/**
	 * A method to return <em>alos</em> (dawn) calculated when the sun is {@link #ASTRONOMICAL_ZENITH 18&deg;} below the
	 * eastern geometric horizon before sunrise.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ASTRONOMICAL_ZENITH
	 */
	public Date getAlos18Degrees() {
		return getSunriseOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_19_POINT_8 19.8&deg;} below the
	 * eastern geometric horizon before sunrise. This calculation is based on the same calculation of
	 * {@link #getAlos90() 90 minutes} but uses a degree based calculation instead of 90 exact minutes. This calculation
	 * is based on the position of the sun 90 minutes before sunrise in Jerusalem during the equinox which calculates to
	 * 19.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_19_POINT_8
	 * @see #getAlos90()
	 */
	public Date getAlos19Point8Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_19_POINT_8);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_16_POINT_1 16.1&deg;} below the
	 * eastern geometric horizon before sunrise. This calculation is based on the same calculation of
	 * {@link #getAlos72() 72 minutes} but uses a degree based calculation instead of 72 exact minutes. This calculation
	 * is based on the position of the sun 72 minutes before sunrise in Jerusalem during the equinox which calculates to
	 * 16.1&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_16_POINT_1
	 * @see #getAlos72()
	 */
	public Date getAlos16Point1Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_11_DEGREES
	 * 11.5&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 52 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem during the equinox which calculates to 11.5&deg; below
	 * {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_11_POINT_5
	 */
	public Date getMisheyakir11Point5Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_11_POINT_5);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_11_DEGREES
	 * 11&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 48 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem during the equinox which calculates to 11&deg; below
	 * {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @return If the calculation can't be computed such as northern and southern locations even south of the Arctic
	 *         Circle and north of the Antarctic Circle where the sun may not reach low enough below the horizon for
	 *         this calculation, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_11_DEGREES
	 */
	public Date getMisheyakir11Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_11_DEGREES);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_10_POINT_2
	 * 10.2&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 45 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem during the equinox which calculates to 10.2&deg; below
	 * {@link #GEOMETRIC_ZENITH geometric zenith}
	 * 
	 * @return the <code>Date</code> of the latest <em>misheyakir</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_10_POINT_2
	 */
	public Date getMisheyakir10Point2Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_10_POINT_2);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos19Point8Degrees() 19.8&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis19Point8Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos19Point8Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 19.8&deg; below sunrise or sunset. This returns the time of
	 * 3 * {@link #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 */
	public Date getSofZmanShmaMGA19Point8Degrees() {
		return getSofZmanShma(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis16Point1Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos16Point1Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of
	 * 3 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Date getSofZmanShmaMGA16Point1Degrees() {
		return getSofZmanShma(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos18Degrees() 18&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis18Degrees() shaos zmaniyos}</em> (solar
	 * hours) after {@link #getAlos18Degrees() dawn} based on the opinion of the <em>MGA</em> that the day is calculated
	 * from dawn to nightfall with both being 18&deg; below sunrise or sunset. This returns the time of 3 *
	 * {@link #getShaahZmanis18Degrees()} after {@link #getAlos18Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis18Degrees()
	 * @see #getAlos18Degrees()
	 */
	public Date getSofZmanShmaMGA18Degrees() {
		return getSofZmanShma(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos72() 72} minutes before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis72Minutes() shaos zmaniyos}</em> (solar
	 * hours) after {@link #getAlos72() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos72() dawn} of 72 minutes before sunrise to {@link #getTzais72() nightfall} of 72 minutes after
	 * sunset. This returns the time of 3 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() dawn}. This
	 * class returns an identical time to {@link #getSofZmanShmaMGA()} and is repeated here for clarity.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72Minutes()
	 * @see #getAlos72()
	 * @see #getSofZmanShmaMGA()
	 */
	public Date getSofZmanShmaMGA72Minutes() {
		return getSofZmanShmaMGA();
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em>
	 * , or 1/10th of the day before {@link #getSunrise() sunrise}. This time is 3
	 * <em>{@link #getShaahZmanis90MinutesZmanis() shaos zmaniyos}</em> (solar hours) after {@link #getAlos72Zmanis()
	 * dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos72Zmanis() dawn}
	 * of 72 minutes <em>zmaniyos</em>, or 1/10th of the day before {@link #getSeaLevelSunrise() sea level sunrise} to
	 * {@link #getTzais72Zmanis() nightfall} of 72 minutes <em>zmaniyos</em> after {@link #getSeaLevelSunset() sea level
	 * sunset}. This returns the time of 3 * {@link #getShaahZmanis72MinutesZmanis()} after {@link #getAlos72Zmanis()
	 * dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72MinutesZmanis()
	 * @see #getAlos72Zmanis()
	 */
	public Date getSofZmanShmaMGA72MinutesZmanis() {
		return getSofZmanShma(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos90() 90} minutes before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis90Minutes() shaos zmaniyos}</em> (solar
	 * hours) after {@link #getAlos90() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos90() dawn} of 90 minutes before sunrise to {@link #getTzais90() nightfall} of 90 minutes after
	 * sunset. This returns the time of 3 * {@link #getShaahZmanis90Minutes()} after {@link #getAlos90() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90Minutes()
	 * @see #getAlos90()
	 */
	public Date getSofZmanShmaMGA90Minutes() {
		return getSofZmanShma(getAlos90(), getTzais90());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em>
	 * before {@link #getSunrise() sunrise}. This time is 3
	 * <em>{@link #getShaahZmanis90MinutesZmanis() shaos zmaniyos}</em> (solar hours) after {@link #getAlos90Zmanis()
	 * dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos90Zmanis() dawn}
	 * of 90 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais90Zmanis() nightfall} of 90 minutes
	 * <em>zmaniyos</em> after sunset. This returns the time of 3 * {@link #getShaahZmanis90MinutesZmanis()} after
	 * {@link #getAlos90Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Date getSofZmanShmaMGA90MinutesZmanis() {
		return getSofZmanShma(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos96() 96} minutes before
	 * {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis96Minutes() shaos zmaniyos}</em> (solar
	 * hours) after {@link #getAlos96() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos96() dawn} of 96 minutes before sunrise to {@link #getTzais96() nightfall} of 96 minutes after
	 * sunset. This returns the time of 3 * {@link #getShaahZmanis96Minutes()} after {@link #getAlos96() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 * @see #getAlos96()
	 */
	public Date getSofZmanShmaMGA96Minutes() {
		return getSofZmanShma(getAlos96(), getTzais96());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos90Zmanis() 96} minutes <em>zmaniyos</em>
	 * before {@link #getSunrise() sunrise}. This time is 3
	 * <em>{@link #getShaahZmanis96MinutesZmanis() shaos zmaniyos}</em> (solar hours) after {@link #getAlos96Zmanis()
	 * dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos96Zmanis() dawn}
	 * of 96 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais90Zmanis() nightfall} of 96 minutes
	 * <em>zmaniyos</em> after sunset. This returns the time of 3 * {@link #getShaahZmanis96MinutesZmanis()} after
	 * {@link #getAlos96Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96MinutesZmanis()
	 * @see #getAlos96Zmanis()
	 */
	public Date getSofZmanShmaMGA96MinutesZmanis() {
		return getSofZmanShma(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) calculated as 3
	 * hours (regular and not zmaniyos) before {@link ZmanimCalendar#getChatzos()}. This is the opinion of the
	 * <em>Shach</em> in the <em>Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer</em> and others. This
	 * returns the time of 3 hours before {@link ZmanimCalendar#getChatzos()}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see ZmanimCalendar#getChatzos()
	 * @see #getSofZmanTfila2HoursBeforeChatzos()
	 */
	public Date getSofZmanShma3HoursBeforeChatzos() {
		return getTimeOffset(getChatzos(), -180 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos120() 120} minutes or 1/6th of the day
	 * before {@link #getSunrise() sunrise}. This time is 3 <em>{@link #getShaahZmanis120Minutes() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos120() dawn} based on the opinion of the <em>MGA</em> that the day is
	 * calculated from a {@link #getAlos120() dawn} of 120 minutes before sunrise to {@link #getTzais120() nightfall} of
	 * 120 minutes after sunset. This returns the time of 3 * {@link #getShaahZmanis120Minutes()} after
	 * {@link #getAlos120() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis120Minutes()
	 * @see #getAlos120()
	 */
	public Date getSofZmanShmaMGA120Minutes() {
		return getSofZmanShma(getAlos120(), getTzais120());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) based on the
	 * opinion that the day starts at <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at
	 * {@link #getSeaLevelSunset() sea level sunset}. 3 shaos zmaniyos are calculated based on this day and added to
	 * {@link #getAlos16Point1Degrees() alos}to reach this time. This time is 3 <em>shaos zmaniyos</em> (solar hours)
	 * after {@link #getAlos16Point1Degrees() dawn} based on the opinion that the day is calculated from a
	 * <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> to {@link #getSeaLevelSunset() sea level sunset}.<br />
	 * <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at midday.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this day. If the calculation can't
	 *         be computed such as northern and southern locations even south of the Arctic Circle and north of the
	 *         Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos16Point1Degrees()
	 * @see #getSeaLevelSunset()
	 */
	public Date getSofZmanShmaAlos16Point1ToSunset() {
		return getSofZmanShma(getAlos16Point1Degrees(), getSeaLevelSunset());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) based on the
	 * opinion that the day starts at <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at
	 * <em> {@link #getTzaisGeonim7Point083Degrees() tzais 7.083&deg;}</em>. 3 <em>shaos zmaniyos</em> are calculated
	 * based on this day and added to <em>{@link #getAlos16Point1Degrees() alos}</em> to reach this time. This time is 3
	 * <em>shaos zmaniyos</em> (temporal hours) after <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> based on
	 * the opinion that the day is calculated from a <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> to
	 * <em>{@link #getTzaisGeonim7Point083Degrees() tzais 7.083&deg;}</em>.<br />
	 * <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at midday.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this calculation. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos16Point1Degrees()
	 * @see #getTzaisGeonim7Point083Degrees()
	 */
	public Date getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees() {
		return getSofZmanShma(getAlos16Point1Degrees(), getTzaisGeonim7Point083Degrees());
	}

	/**
	 * From the GRA in Kol Eliyahu on Berachos #173 that states that <em>zman krias shema</em> is calculated as half the
	 * time from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getFixedLocalChatzos() fixed local chatzos}.
	 * The GRA himself seems to contradict this when he stated that <em>zman krias shema</em> is 1/4 of the day from
	 * sunrise to sunset. See <em>Sarah Lamoed</em> #25 in Yisroel Vehazmanim Vol III page 1016.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this calculation. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, a null will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getFixedLocalChatzos()
	 * @deprecated Pending confirmation from Rabbi Harfenes, this method is deprecated and should not be used.
	 */
	public Date getSofZmanShmaKolEliyahu() {
		Date chatzos = getFixedLocalChatzos();
		if (chatzos == null || getSunrise() == null) {
			return null;
		}
		long diff = (chatzos.getTime() - getSeaLevelSunrise().getTime()) / 2;
		return getTimeOffset(chatzos, -diff);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos19Point8Degrees() 19.8&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis19Point8Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos19Point8Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 19.8&deg; below sunrise or sunset. This returns the time of
	 * 4 * {@link #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 */
	public Date getSofZmanTfilaMGA19Point8Degrees() {
		return getSofZmanTfila(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis16Point1Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos16Point1Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of
	 * 4 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Date getSofZmanTfilaMGA16Point1Degrees() {
		return getSofZmanTfila(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos18Degrees() 18&deg;} before {@link #getSunrise()
	 * sunrise}. This time is 4 <em>{@link #getShaahZmanis18Degrees() shaos zmaniyos}</em> (solar hours) after
	 * {@link #getAlos18Degrees() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from dawn to
	 * nightfall with both being 18&deg; below sunrise or sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis18Degrees()} after {@link #getAlos18Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis18Degrees()
	 * @see #getAlos18Degrees()
	 */
	public Date getSofZmanTfilaMGA18Degrees() {
		return getSofZmanTfila(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise()
	 * sunrise}. This time is 4 <em>{@link #getShaahZmanis72Minutes() shaos zmaniyos}</em> (solar hours) after
	 * {@link #getAlos72() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos72() dawn} of 72 minutes before sunrise to {@link #getTzais72() nightfall} of 72 minutes after
	 * sunset. This returns the time of 4 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() dawn}. This
	 * class returns an identical time to {@link #getSofZmanTfilaMGA()} and is repeated here for clarity.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72Minutes()
	 * @see #getAlos72()
	 * @see #getSofZmanShmaMGA()
	 */
	public Date getSofZmanTfilaMGA72Minutes() {
		return getSofZmanTfilaMGA();
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em> before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis72MinutesZmanis() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos72Zmanis() dawn} based on the opinion of the <em>MGA</em> that the day is
	 * calculated from a {@link #getAlos72Zmanis() dawn} of 72 minutes <em>zmaniyos</em> before sunrise to
	 * {@link #getTzais72Zmanis() nightfall} of 72 minutes <em>zmaniyos</em> after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis72MinutesZmanis()} after {@link #getAlos72Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72MinutesZmanis()
	 * @see #getAlos72Zmanis()
	 */
	public Date getSofZmanTfilaMGA72MinutesZmanis() {
		return getSofZmanTfila(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos90() 90} minutes before {@link #getSunrise()
	 * sunrise}. This time is 4 <em>{@link #getShaahZmanis90Minutes() shaos zmaniyos}</em> (solar hours) after
	 * {@link #getAlos90() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos90() dawn} of 90 minutes before sunrise to {@link #getTzais90() nightfall} of 90 minutes after
	 * sunset. This returns the time of 4 * {@link #getShaahZmanis90Minutes()} after {@link #getAlos90() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90Minutes()
	 * @see #getAlos90()
	 */
	public Date getSofZmanTfilaMGA90Minutes() {
		return getSofZmanTfila(getAlos90(), getTzais90());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em> before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis90MinutesZmanis() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos90Zmanis() dawn} based on the opinion of the <em>MGA</em> that the day is
	 * calculated from a {@link #getAlos90Zmanis() dawn} of 90 minutes <em>zmaniyos</em> before sunrise to
	 * {@link #getTzais90Zmanis() nightfall} of 90 minutes <em>zmaniyos</em> after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis90MinutesZmanis()} after {@link #getAlos90Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Date getSofZmanTfilaMGA90MinutesZmanis() {
		return getSofZmanTfila(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos96() 96} minutes before {@link #getSunrise()
	 * sunrise}. This time is 4 <em>{@link #getShaahZmanis96Minutes() shaos zmaniyos}</em> (solar hours) after
	 * {@link #getAlos96() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos96() dawn} of 96 minutes before sunrise to {@link #getTzais96() nightfall} of 96 minutes after
	 * sunset. This returns the time of 4 * {@link #getShaahZmanis96Minutes()} after {@link #getAlos96() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 * @see #getAlos96()
	 */
	public Date getSofZmanTfilaMGA96Minutes() {
		return getSofZmanTfila(getAlos96(), getTzais96());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos96Zmanis() 96} minutes <em>zmaniyos</em> before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis96MinutesZmanis() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos96Zmanis() dawn} based on the opinion of the <em>MGA</em> that the day is
	 * calculated from a {@link #getAlos96Zmanis() dawn} of 96 minutes <em>zmaniyos</em> before sunrise to
	 * {@link #getTzais96Zmanis() nightfall} of 96 minutes <em>zmaniyos</em> after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis96MinutesZmanis()} after {@link #getAlos96Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Date getSofZmanTfilaMGA96MinutesZmanis() {
		return getSofZmanTfila(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos120() 120} minutes before {@link #getSunrise()
	 * sunrise} . This time is 4 <em>{@link #getShaahZmanis120Minutes() shaos zmaniyos}</em> (solar hours) after
	 * {@link #getAlos120() dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a
	 * {@link #getAlos120() dawn} of 120 minutes before sunrise to {@link #getTzais120() nightfall} of 120 minutes after
	 * sunset. This returns the time of 4 * {@link #getShaahZmanis120Minutes()} after {@link #getAlos120() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis120Minutes()
	 * @see #getAlos120()
	 */
	public Date getSofZmanTfilaMGA120Minutes() {
		return getSofZmanTfila(getAlos120(), getTzais120());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) calculated as 2 hours
	 * before {@link ZmanimCalendar#getChatzos()}. This is based on the opinions that calculate
	 * <em>sof zman krias shema</em> as {@link #getSofZmanShma3HoursBeforeChatzos()}. This returns the time of 2 hours
	 * before {@link ZmanimCalendar#getChatzos()}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see ZmanimCalendar#getChatzos()
	 * @see #getSofZmanShma3HoursBeforeChatzos()
	 */
	public Date getSofZmanTfila2HoursBeforeChatzos() {
		return getTimeOffset(getChatzos(), -120 * MINUTE_MILLIS);
	}

	/**
	 * This method returns mincha gedola calculated as 30 minutes after <em>{@link #getChatzos() chatzos}</em> and not
	 * 1/2 of a <em>{@link #getShaahZmanisGra() shaah zmanis}</em> after <em>{@link #getChatzos() chatzos}</em> as
	 * calculated by {@link #getMinchaGedola}. Some use this time to delay the start of mincha in the winter when 1/2 of
	 * a <em>{@link #getShaahZmanisGra() shaah zmanis}</em> is less than 30 minutes. See
	 * {@link #getMinchaGedolaGreaterThan30()}for a conveniance method that returns the later of the 2 calculations. One
	 * should not use this time to start <em>mincha</em> before the standard
	 * <em>{@link #getMinchaGedola() mincha gedola}</em>. See <em>Shulchan Aruch
	 * Orach Chayim Siman Raish Lamed Gimel seif alef</em> and the <em>Shaar Hatziyon seif katan ches</em>.
	 * 
	 * @return the <code>Date</code> of 30 mintes after <em>chatzos</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getMinchaGedola()
	 * @see #getMinchaGedolaGreaterThan30()
	 */
	public Date getMinchaGedola30Minutes() {
		return getTimeOffset(getChatzos(), MINUTE_MILLIS * 30);
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> according to the Magen Avraham with the day starting 72
	 * minutes before sunrise and ending 72 minutes after sunset. This is the earliest time to pray <em>mincha</em>. For
	 * more information on this see the documentation on <em>{@link #getMinchaGedola() mincha gedola}</em>. This is
	 * calculated as 6.5 {@link #getTemporalHour() solar hours} after alos. The calculation used is 6.5 *
	 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() alos}.
	 * 
	 * @see #getAlos72()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @see ZmanimCalendar#getMinchaGedola()
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaGedola72Minutes() {
		return getMinchaGedola(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> according to the Magen Avraham with the day starting and
	 * ending 16.1&deg; below the horizon. This is the earliest time to pray <em>mincha</em>. For more information on
	 * this see the documentation on <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 6.5
	 * {@link #getTemporalHour() solar hours} after alos. The calculation used is 6.5 *
	 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() alos}.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaGedola16Point1Degrees() {
		return getMinchaGedola(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This is a conveniance methd that returns the later of {@link #getMinchaGedola()} and
	 * {@link #getMinchaGedola30Minutes()}. In the winter when a <em>{@link #getShaahZmanisGra() shaah zmanis}</em> is
	 * less than 30 minutes {@link #getMinchaGedola30Minutes()} will be returned, otherwise {@link #getMinchaGedola()}
	 * will be returned.
	 * 
	 * @return the <code>Date</code> of the later of {@link #getMinchaGedola()} and {@link #getMinchaGedola30Minutes()}.
	 *         If the calculation can't be computed such as in the Arctic Circle where there is at least one day a year
	 *         where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaGedolaGreaterThan30() {
		if (getMinchaGedola30Minutes() == null || getMinchaGedola() == null) {
			return null;
		} else {
			return getMinchaGedola30Minutes().compareTo(getMinchaGedola()) > 0 ? getMinchaGedola30Minutes()
					: getMinchaGedola();
		}
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> according to the <em>Magen Avraham</em> with the day
	 * starting and ending 16.1&deg; below the horizon. This is the perfered earliest time to pray <em>mincha</em>
	 * according to the opinion of the <em>Rambam</em> and others. For more information on this see the documentation on
	 * <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 9.5 {@link #getTemporalHour() solar
	 * hours} after alos. The calculation used is 9.5 * {@link #getShaahZmanis16Point1Degrees()} after
	 * {@link #getAlos16Point1Degrees() alos}.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaKetana16Point1Degrees() {
		return getMinchaKetana(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> according to the <em>Magen Avraham</em> with the day
	 * starting 72 minutes before sunrise and ending 72 minutes after sunset. This is the perfered earliest time to pray
	 * <em>mincha</em> according to the opinion of the <em>Rambam</em> and others. For more information on this see the
	 * documentation on <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 9.5
	 * {@link #getShaahZmanis72Minutes()} after <em>alos</em>. The calculation used is 9.5 *
	 * {@link #getShaahZmanis72Minutes()} after <em>{@link #getAlos72() alos}</em>.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaKetana72Minutes() {
		return getMinchaKetana(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the <em>Magen Avraham</em> with the day
	 * starting 60 minutes before sunrise and ending 60 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos60() dawn}. The formula used is:<br/>
	 * 10.75 {@link #getShaahZmanis60Minutes()} after {@link #getAlos60()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis60Minutes()
	 */
	public Date getPlagHamincha60Minutes() {
		return getPlagHamincha(getAlos60(), getTzais60());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the <em>Magen Avraham</em> with the day
	 * starting 72 minutes before sunrise and ending 72 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos72() dawn}. The formula used is:<br/>
	 * 10.75 {@link #getShaahZmanis72Minutes()} after {@link #getAlos72()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis72Minutes()
	 */
	public Date getPlagHamincha72Minutes() {
		return getPlagHamincha(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the <em>Magen Avraham</em> with the day
	 * starting 90 minutes before sunrise and ending 90 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos90() dawn}. The formula used is:<br/>
	 * 10.75 {@link #getShaahZmanis90Minutes()} after {@link #getAlos90()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis90Minutes()
	 */
	public Date getPlagHamincha90Minutes() {
		return getPlagHamincha(getAlos90(), getTzais90());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the <em>Magen Avraham</em> with the day
	 * starting 96 minutes before sunrise and ending 96 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos96() dawn}. The formula used is:<br/>
	 * 10.75 {@link #getShaahZmanis96Minutes()} after {@link #getAlos96()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 */
	public Date getPlagHamincha96Minutes() {
		return getPlagHamincha(getAlos96(), getTzais96());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em>. This is calculated as 10.75 hours after
	 * {@link #getAlos96Zmanis() dawn}. The formula used is:<br/>
	 * 10.75 * {@link #getShaahZmanis96MinutesZmanis()} after {@link #getAlos96Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha96MinutesZmanis() {
		return getPlagHamincha(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em>. This is calculated as 10.75 hours after
	 * {@link #getAlos90Zmanis() dawn}. The formula used is:<br/>
	 * 10.75 * {@link #getShaahZmanis90MinutesZmanis()} after {@link #getAlos90Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha90MinutesZmanis() {
		return getPlagHamincha(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em>. This is calculated as 10.75 hours after
	 * {@link #getAlos72Zmanis() dawn}. The formula used is:<br/>
	 * 10.75 * {@link #getShaahZmanis72MinutesZmanis()} after {@link #getAlos72Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha72MinutesZmanis() {
		return getPlagHamincha(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at
	 * <em>{@link #getTzais16Point1Degrees() tzais 16.1&deg;}</em>. This is calculated as 10.75 hours <em>zmaniyos</em>
	 * after {@link #getAlos16Point1Degrees() dawn}. The formula is<br/>
	 * 10.75 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 */
	public Date getPlagHamincha16Point1Degrees() {
		return getPlagHamincha(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos19Point8Degrees() alos 19.8&deg;}</em> and ends at
	 * <em>{@link #getTzais19Point8Degrees() tzais 19.8&deg;}</em>. This is calculated as 10.75 hours <em>zmaniyos</em>
	 * after {@link #getAlos19Point8Degrees() dawn}. The formula is<br/>
	 * 10.75 * {@link #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis19Point8Degrees()
	 */
	public Date getPlagHamincha19Point8Degrees() {
		return getPlagHamincha(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos26Degrees() alos 26&deg;}</em> and ends at <em>{@link #getTzais26Degrees() tzais 26&deg;}</em>
	 * . This is calculated as 10.75 hours <em>zmaniyos</em> after {@link #getAlos26Degrees() dawn}. The formula is<br/>
	 * 10.75 * {@link #getShaahZmanis26Degrees()} after {@link #getAlos26Degrees()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis26Degrees()
	 */
	public Date getPlagHamincha26Degrees() {
		return getPlagHamincha(getAlos26Degrees(), getTzais26Degrees());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos18Degrees() alos 18&deg;}</em> and ends at <em>{@link #getTzais18Degrees() tzais 18&deg;}</em>
	 * . This is calculated as 10.75 hours <em>zmaniyos</em> after {@link #getAlos18Degrees() dawn}. The formula is<br/>
	 * 10.75 * {@link #getShaahZmanis18Degrees()} after {@link #getAlos18Degrees()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis18Degrees()
	 */
	public Date getPlagHamincha18Degrees() {
		return getPlagHamincha(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at {@link #getSunset() sunset}. 10.75 shaos
	 * zmaniyos are calculated based on this day and added to {@link #getAlos16Point1Degrees() alos} to reach this time.
	 * This time is 10.75 <em>shaos zmaniyos</em> (temporal hours) after {@link #getAlos16Point1Degrees() dawn} based on
	 * the opinion that the day is calculated from a {@link #getAlos16Point1Degrees() dawn} of 16.1 degrees before
	 * sunrise to {@link #getSeaLevelSunset() sea level sunset}. This returns the time of 10.75 * the calculated
	 * <em>shaah zmanis</em> after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the plag. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos16Point1Degrees()
	 * @see #getSeaLevelSunset()
	 */
	public Date getPlagAlosToSunset() {
		return getPlagHamincha(getAlos16Point1Degrees(), getSeaLevelSunset());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at {@link #getTzaisGeonim7Point083Degrees()
	 * tzais}. 10.75 shaos zmaniyos are calculated based on this day and added to {@link #getAlos16Point1Degrees() alos}
	 * to reach this time. This time is 10.75 <em>shaos zmaniyos</em> (temporal hours) after
	 * {@link #getAlos16Point1Degrees() dawn} based on the opinion that the day is calculated from a
	 * {@link #getAlos16Point1Degrees() dawn} of 16.1 degrees before sunrise to
	 * {@link #getTzaisGeonim7Point083Degrees() tzais} . This returns the time of 10.75 * the calculated
	 * <em>shaah zmanis</em> after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the plag. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos16Point1Degrees()
	 * @see #getTzaisGeonim7Point083Degrees()
	 */
	public Date getPlagAlos16Point1ToTzaisGeonim7Point083Degrees() {
		return getPlagHamincha(getAlos16Point1Degrees(), getTzaisGeonim7Point083Degrees());
	}

	/**
	 * Method to return <em>Bain Hashmasho</em> of <em>Rabainu Tam</em> calculated when the sun is
	 * {@link #ZENITH_13_POINT_24 13.24&deg;} below the western {@link #GEOMETRIC_ZENITH geometric horizon} (90&deg;)
	 * after sunset. This calculation is based on the same calculation of {@link #getBainHasmashosRT58Point5Minutes()
	 * Bain Hasmashos Rabainu Tam 58.5 minutes} but uses a degree based calculation instead of 58.5 exact minutes. This
	 * calculation is based on the position of the sun 58.5 minutes after sunset in Jerusalem during the equinox which
	 * calculates to 13.24&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.<br/>
	 * <br/>
	 * NOTE: As per Yisroel Vehazmanim Vol III page 1028 No 50, a dip of slightly less than 13&deg; should be used.
	 * Calculations show that the proper dip to be 13.2456&deg; (truncated to 13.24 that provides about 1.5 second
	 * earlier (<em>lechumra</em>) time) below the horizon at that time. This makes a difference of 1 minute and 10
	 * seconds in Jerusalem during the Equinox, and 1 minute 29 seconds during the solstice as compared to the proper
	 * 13.24&deg;. For NY during the solstice, the difference is 1 minute 56 seconds.
	 * 
	 * @return the <code>Date</code> of the sun being 13.24&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}
	 *         (90&deg;). If the calculation can't be computed such as northern and southern locations even south of the
	 *         Arctic Circle and north of the Antarctic Circle where the sun may not reach low enough below the horizon
	 *         for this calculation, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_13_POINT_24
	 * @see #getBainHasmashosRT58Point5Minutes()
	 */
	public Date getBainHasmashosRT13Point24Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_13_POINT_24);
	}

	/**
	 * This method returns <em>Bain Hashmashos<em> of <em>Rabainu Tam</em> calculated as a 58.5 minute offset after
	 * sunset. <em>Bain hashmashos</em> is 3/4 of a <em>Mil</em> before <em>tzais</em> or 3 1/4 <em>Mil</em> after
	 * sunset. With a <em>Mil<em> calculated as 18 minutes, 3.25 * 18
	 * = 58.5 minutes.
	 * 
	 * @return the <code>Date</code> of 58.5 minutes after sunset. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * 
	 */
	public Date getBainHasmashosRT58Point5Minutes() {
		return getTimeOffset(getSeaLevelSunset(), 58.5 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the time of <em>bain hashmashos</em> based on the calculation of 13.5 minutes (3/4 of an 18
	 * minute <em>Mil</em> before shkiah calculated as {@link #getTzaisGeonim7Point083Degrees() 7.083&deg;}.
	 * 
	 * @return the <code>Date</code> of the <em>bain hashmashos</em> of <em>Rabainu Tam</em> in this calculation. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getTzaisGeonim7Point083Degrees()
	 */
	public Date getBainHasmashosRT13Point5MinutesBefore7Point083Degrees() {
		return getTimeOffset(getSunsetOffsetByDegrees(ZENITH_7_POINT_083), -13.5 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>bain hashmashos</em> of <em>Rabainu Tam</em> calculated according to the opinion of the
	 * <em>Divray Yosef</em> (see Yisrael Vehazmanim) calculated 5/18th (27.77%) of the time between <em>alos</em>
	 * (calculated as 19.8&deg; before sunrise) and sunrise. This is added to sunset to arrive at the time for
	 * <em>bain hashmashos</em> of <em>Rabainu Tam<em>).
	 * 
	 * @return the <code>Date</code> of <em>bain hashmashos</em> of <em>Rabainu Tam</em> for this calculation. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getBainHasmashosRT2Stars() {
		Date alos19Point8 = getAlos19Point8Degrees();
		Date sunrise = getSeaLevelSunrise();
		if (alos19Point8 == null || sunrise == null) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunset(), (sunrise.getTime() - alos19Point8.getTime()) * (5 / 18d));
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated at the
	 * sun's position at {@link #ZENITH_3_POINT_7 3.7&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.7&deg; below sea level.
	 * @see #ZENITH_3_POINT_7
	 */
	// public Date getTzaisGeonim3Point7Degrees() {
	// return getSunsetOffsetByDegrees(ZENITH_3_POINT_7);
	// }

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated at the
	 * sun's position at {@link #ZENITH_5_POINT_95 5.95&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 5.95&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_5_POINT_95
	 */
	public Date getTzaisGeonim5Point95Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_5_POINT_95);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on an 18
	 * minute Mil, or 13.5 minutes. It is the sun's position at {@link #ZENITH_3_POINT_65 3.65&deg;} below the western
	 * horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.65&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_3_POINT_65
	 */
	public Date getTzaisGeonim3Point65Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_65);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on an 18
	 * minute Mil, or 13.5 minutes. It is the sun's position at {@link #ZENITH_3_POINT_676 3.676&deg;} below the western
	 * horizon based on the calculations of Stanley Fishkind. This is a very early <em>zman</em> and should not be
	 * relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.676&deg; below sea level. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #ZENITH_3_POINT_676
	 */
	public Date getTzaisGeonim3Point676Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_676);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on a 24
	 * minute Mil, or 18 minutes. It is the sun's position at {@link #ZENITH_4_POINT_61 4.61&deg;} below the western
	 * horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.61&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_61
	 */
	public Date getTzaisGeonim4Point61Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_61);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a>, based on a 22.5
	 * minute Mil, or 16 7/8 minutes. It is the sun's position at {@link #ZENITH_4_POINT_37 4.37&deg;} below the western
	 * horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.37&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_37
	 */
	public Date getTzaisGeonim4Point37Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_37);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a 24 minute
	 * <em><a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a></em>, (
	 * <em>Baal Hatanya</em>) based on a <em>Mil</em> being 24 minutes, and is calculated as 18 + 2 + 4 for a total of
	 * 24 minutes (FIXME: additional documentation details needed). It is the sun's position at
	 * {@link #ZENITH_5_POINT_88 5.88&deg;} below the western horizon. This is a very early <em>zman</em> and should not
	 * be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 5.88&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_5_POINT_88
	 */
	public Date getTzaisGeonim5Point88Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_5_POINT_88);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "http://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on the
	 * sun's position at {@link #ZENITH_4_POINT_8 4.8&deg;} below the western horizon. This is based on Rabbi Leo Levi's
	 * calculations. FIXME: additional documentation needed. This is the This is a very early <em>zman</em> and should
	 * not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.8&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_8
	 */
	public Date getTzaisGeonim4Point8Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_8);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 30
	 * minutes after sunset during the equinox in Yerushalayim. The sun's position at this time computes to
	 * {@link #ZENITH_7_POINT_083 7.083&deg; (or 7&deg 5\u2032} below the western horizon. Note that this is a common
	 * and rounded number. Computation shows the accurate number is 7.2&deg;
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 7.083&deg; below sea level. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #ZENITH_7_POINT_083
	 */
	public Date getTzaisGeonim7Point083Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_7_POINT_083);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated at the
	 * sun's position at {@link #ZENITH_8_POINT_5 8.5&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 8.5&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_8_POINT_5
	 */
	public Date getTzaisGeonim8Point5Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_8_POINT_5);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Chavas Yair</em> and
	 * </em>Divray Malkiel</em> that the time to walk the distance of a <em>Mil</em> is 15 minutes for a total of 60
	 * minutes for 4 <em>Mil</em> after {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing 60 minutes after sea level sunset. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getAlos60()
	 */
	public Date getTzais60() {
		return getTimeOffset(getSeaLevelSunset(), 60 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>tzais</em> usually calculated as 40 minutes (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}) after sunset. Please note that <em>Chacham Yosef Harari-Raful</em>
	 * of <em>Yeshivat Ateret Torah</em> who uses this time, does so only for calculating various other
	 * <em>zmanai hayom</em> such as <em>Sof Zman Krias Shema</em> and <em>Plag Hamincha</em>. His calendars do not
	 * publish a <em>zman</em> for <em>Tzais</em>. It should also be noted that <em>Chacham Harari-Raful</em> provided a
	 * 25 minute <em>zman</em> for Israel. This API uses 40 minutes year round in any place on the globe by default.
	 * This offset can be changed by calling {@link #setAteretTorahSunsetOffset(double)}.
	 * 
	 * @return the <code>Date</code> representing 40 minutes (configurable via {@link #setAteretTorahSunsetOffset})
	 *         after sea level sunset. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public Date getTzaisAteretTorah() {
		return getTimeOffset(getSeaLevelSunset(), getAteretTorahSunsetOffset() * MINUTE_MILLIS);
	}

	/**
	 * Returns the offset in minutes after sunset used to calculate sunset for the Ateret Torah zmanim. The defaullt
	 * value is 40 minutes. This affects most zmanim, since almost all zmanim use sunbset as part of their calculation.
	 * 
	 * @return the number of minutes after sunset for <em>Tzait</em>.
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public double getAteretTorahSunsetOffset() {
		return this.ateretTorahSunsetOffset;
	}

	/**
	 * Allows setting the offset in minutes after sunset for the Ateret Torah zmanim. The default if unset is 40
	 * minutes. Chacham Yosef Harari-Raful of Yeshivat Ateret Torah uses 40 minutes globally with the exception of
	 * Israel where a 25 minute offset is used. This 40 minute (or any other) offset can be overridden by this methd.
	 * This offset impacts all Ateret Torah zmanim.
	 * 
	 * @param ateretTorahSunsetOffset
	 *            the number of minutes after sunset to use as an offset for the Ateret Torah <em>tzais</em>
	 * @see #getAteretTorahSunsetOffset()
	 */
	public void setAteretTorahSunsetOffset(double ateretTorahSunsetOffset) {
		this.ateretTorahSunsetOffset = ateretTorahSunsetOffset;
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) based on the
	 * calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts
	 * {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is usually calculated as ending
	 * {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em> are calculated based on this day and added
	 * to {@link #getAlos72Zmanis() alos} to reach this time. This time is 3
	 * <em> {@link #getShaahZmanisAteretTorah() shaos zmaniyos}</em> (temporal hours) after
	 * <em>{@link #getAlos72Zmanis()
	 * alos 72 zmaniyos}</em>.<br />
	 * <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at midday.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this calculation. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, a null will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 * @see #getShaahZmanisAteretTorah()
	 */
	public Date getSofZmanShmaAteretTorah() {
		return getSofZmanShma(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) based on the calculation
	 * of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts {@link #getAlos72Zmanis() 1/10th of
	 * the day} before sunrise and is usually calculated as ending {@link #getTzaisAteretTorah() 40 minutes after
	 * sunset} (configurable to any offset via {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em> are
	 * calculated based on this day and added to {@link #getAlos72Zmanis() alos} to reach this time. This time is 4 *
	 * <em>{@link #getShaahZmanisAteretTorah() shaos zmaniyos}</em> (temporal hours) after
	 * <em>{@link #getAlos72Zmanis() alos 72 zmaniyos}</em>.<br />
	 * <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at midday.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this calculation. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, a null will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public Date getSofZmanTfilahAteretTorah() {
		return getSofZmanTfila(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> based on the calculation of
	 * <em>Chacham Yosef Harari-Raful</em> of <em>Yeshivat Ateret Torah</em>, that the day starts
	 * {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is usually calculated as ending
	 * {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}). This is the perfered earliest time to pray <em>mincha</em>
	 * according to the opinion of the <em>Rambam</em> and others. For more information on this see the documentation on
	 * <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 6.5 {@link #getShaahZmanisAteretTorah()
	 * solar hours} after alos. The calculation used is 6.5 * {@link #getShaahZmanisAteretTorah()} after
	 * <em>{@link #getAlos72Zmanis() alos}</em>.
	 * 
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetanaAteretTorah()
	 * @see ZmanimCalendar#getMinchaGedola()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 * 
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaGedolaAteretTorah() {
		return getMinchaGedola(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> based on the calculation of
	 * <em>Chacham Yosef Harari-Raful</em> of <em>Yeshivat Ateret Torah</em>, that the day starts
	 * {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is usually calculated as ending
	 * {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}). This is the perfered earliest time to pray <em>mincha</em>
	 * according to the opinion of the <em>Rambam</em> and others. For more information on this see the documentation on
	 * <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 9.5 {@link #getShaahZmanisAteretTorah()
	 * solar hours} after {@link #getAlos72Zmanis() alos}. The calculation used is 9.5 *
	 * {@link #getShaahZmanisAteretTorah()} after {@link #getAlos72Zmanis() alos}.
	 * 
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaKetanaAteretTorah() {
		return getMinchaKetana(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the calculation of Chacham Yosef Harari-Raful of
	 * Yeshivat Ateret Torah, that the day starts {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is
	 * usually calculated as ending {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset
	 * via {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em> are calculated based on this day and
	 * added to {@link #getAlos72Zmanis() alos} to reach this time. This time is 10.75
	 * <em>{@link #getShaahZmanisAteretTorah() shaos zmaniyos}</em> (temporal hours) after {@link #getAlos72Zmanis()
	 * dawn}.
	 * 
	 * @return the <code>Date</code> of the plag. If the calculation can't be computed such as in the Arctic Circle
	 *         where there is at least one day a year where the sun does not rise, and one where it does not set, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #setAteretTorahSunsetOffset(double)
	 * @see #getAteretTorahSunsetOffset()
	 */
	public Date getPlagHaminchaAteretTorah() {
		return getPlagHamincha(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>misheyakir</em> based on the common calculation of the Syrian community in NY
	 * that the <em>alos</em> is a fixed minute offset from day starting {@link #getAlos72Zmanis() 1/10th of the day}
	 * before sunrise. The common offsets are 6 minutes (based on th <em>Pri Megadim</em>, but not linked to the
	 * calculation of <em>Alos</em> as 1/10th of the day), 8 and 18 minutes (possibly attributed to
	 * <em>Chacham Baruch Ben Haim</em>). Since there is no universal accepted offset, the user of this API will have to
	 * specify one. <em>Chacham Yosef Harari-Raful</em> of <em>Yeshivat Ateret Torah</em> does not supply any
	 * <em>zman</em> for <em>misheyakir</em> and does not endorse any specific calculation for <em>misheyakir</em>. For
	 * that reason, this method is not a public method.
	 * 
	 * @param minutes
	 *            the number of minutes after <em>alos</em> calculated as {@link #getAlos72Zmanis() 1/10th of the day}
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos72Zmanis()
	 */
	// private Date getMesheyakirAteretTorah(double minutes) {
	// return getTimeOffset(getAlos72Zmanis(), minutes * MINUTE_MILLIS);
	// }

	/**
	 * Method to return <em>tzais</em> (dusk) calculated as 72 minutes zmaniyos, or 1/10th of the day after
	 * {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos72Zmanis()
	 */
	public Date getTzais72Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunset(), shaahZmanis * 1.2);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated using 90 minutes zmaniyos (<em>GRA</em> and the
	 * <em>Baal Hatanya</em>) after {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos90Zmanis()
	 */
	public Date getTzais90Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunset(), shaahZmanis * 1.5);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated using 96 minutes zmaniyos (<em>GRA</em> and the
	 * <em>Baal Hatanya</em>) after {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos96Zmanis()
	 */
	public Date getTzais96Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunset(), shaahZmanis * 1.6);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated as 90 minutes after sea level sunset. This method returns
	 * <em>tzais</em> (nightfall) based on the opinion of the Magen Avraham that the time to walk the distance of a
	 * <em>Mil</em> according to the <em>Rambam</em>'s opinion is 18 minutes for a total of 90 minutes based on the
	 * opinion of <em>Ula</em> who calculated <em>tzais</em> as 5 <em>Mil</em> after sea level shkiah (sunset). A
	 * similar calculation {@link #getTzais19Point8Degrees()}uses solar position calculations based on this time.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getTzais19Point8Degrees()
	 * @see #getAlos90()
	 */
	public Date getTzais90() {
		return getTimeOffset(getSeaLevelSunset(), 90 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>tzais</em> (nightfall) based on the opinion of the <em>Magen Avraham</em> that the time
	 * to walk the distance of a <em>Mil</em> according to the <em>Rambam</em>'s opinion is 2/5 of an hour (24 minutes)
	 * for a total of 120 minutes based on the opinion of <em>Ula</em> who calculated <em>tzais</em> as 5 <em>Mil</em>
	 * after sea level <em>shkiah</em> (sunset). A similar calculation {@link #getTzais26Degrees()} uses temporal
	 * calculations based on this time.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getTzais26Degrees()
	 * @see #getAlos120()
	 */
	public Date getTzais120() {
		return getTimeOffset(getSeaLevelSunset(), 120 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated using 120 minutes zmaniyos (<em>GRA</em> and the
	 * <em>Baal Hatanya</em>) after {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos120Zmanis()
	 */
	public Date getTzais120Zmanis() {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == Long.MIN_VALUE) {
			return null;
		}
		return getTimeOffset(getSeaLevelSunset(), shaahZmanis * 2.0);
	}

	/**
	 * For information on how this is calculated see the comments on {@link #getAlos16Point1Degrees()}
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTzais72()
	 * @see #getAlos16Point1Degrees() for more information on this calculation.
	 */
	public Date getTzais16Point1Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * For information on how this is calculated see the comments on {@link #getAlos26Degrees()}
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTzais120()
	 * @see #getAlos26Degrees()
	 */
	public Date getTzais26Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_26_DEGREES);
	}

	/**
	 * For information on how this is calculated see the comments on {@link #getAlos18Degrees()}
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos18Degrees()
	 */
	public Date getTzais18Degrees() {
		return getSunsetOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}

	/**
	 * For information on how this is calculated see the comments on {@link #getAlos19Point8Degrees()}
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTzais90()
	 * @see #getAlos19Point8Degrees()
	 */
	public Date getTzais19Point8Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_19_POINT_8);
	}

	/**
	 * A method to return <em>tzais</em> (dusk) calculated as 96 minutes after sea level sunset. For information on how
	 * this is calculated see the comments on {@link #getAlos96()}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos96()
	 */
	public Date getTzais96() {
		return getTimeOffset(getSeaLevelSunset(), 96 * MINUTE_MILLIS);
	}

	/**
	 * A method that returns the local time for fixed <em>chatzos</em>. This time is noon and midnight adjusted from
	 * standard time to account for the local latitude. The 360&deg; of the globe divided by 24 calculates to 15&deg;
	 * per hour with 4 minutes per degree, so at a longitude of 0 , 15, 30 etc... <em>Chatzos</em> in 12:00 noon.
	 * Lakewood, N.J., whose longitude is -74.2094, is 0.7906 away from the closest multiple of 15 at -75&deg;. This is
	 * multiplied by 4 to yield 3 minutes and 10 seconds for a <em>chatzos</em> of 11:56:50. This method is not tied to
	 * the theoretical 15&deg; timezones, but will adjust to the actual timezone and <a
	 * href="http://en.wikipedia.org/wiki/Daylight_saving_time">Daylight saving time</a>.
	 * 
	 * @return the Date representing the local <em>chatzos</em>
	 * @see GeoLocation#getLocalMeanTimeOffset()
	 */
	public Date getFixedLocalChatzos() {
		return getTimeOffset(getDateFromTime(12.0 - getGeoLocation().getTimeZone().getRawOffset()
				/ (double) HOUR_MILLIS), -getGeoLocation().getLocalMeanTimeOffset());
	}

	/**
	 * A method that returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) calculated as 3
	 * hours before {@link #getFixedLocalChatzos()}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> calculated as 3 hours before
	 *         {@link #getFixedLocalChatzos()}..
	 * @see #getFixedLocalChatzos()
	 * @see #getSofZmanTfilaFixedLocal()
	 */
	public Date getSofZmanShmaFixedLocal() {
		return getTimeOffset(getFixedLocalChatzos(), -180 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) calculated as 2 hours
	 * before {@link #getFixedLocalChatzos()}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>.
	 * @see #getFixedLocalChatzos()
	 * @see #getSofZmanShmaFixedLocal()
	 */
	public Date getSofZmanTfilaFixedLocal() {
		return getTimeOffset(getFixedLocalChatzos(), -120 * MINUTE_MILLIS);
	}

	/**
	 * Returns the latest time of Kiddush Levana according to the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion that it is calculated as
	 * halfway between molad and molad. This adds half the 29 days, 12 hours and 793 chalakim time between molad and
	 * molad (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's molad. If the time of
	 * <em>sof zman Kiddush Levana</em> occurs during the day (between the <em>alos</em> and <em>tzais</em> passed in as
	 * parameters), it returns the <em>alos</em> passed in. This method is available in the 1.3 release of the API but
	 * may change or be removed in the future since it depends on the still changing {@link JewishCalendar} and related
	 * classes.
	 * 
	 * @param alos
	 *            the begining of the Jewish day
	 * @param tzais
	 *            the end of the Jewish day
	 * @return the Date representing the moment halfway between molad and molad. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 * @see #getSofZmanKidushLevana15Days(Date, Date)
	 */
	public Date getSofZmanKidushLevanaBetweenMoldos(Date alos, Date tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		Calendar sofZmanKidushLevanaCalendar = (Calendar) getCalendar().clone();
		Date sofZmanKidushLevana = jewishCalendar.getSofZmanKidushLevanaBetweenMoldos();
		sofZmanKidushLevanaCalendar.setTime(sofZmanKidushLevana);
		if (alos != null && tzais != null
				&& sofZmanKidushLevanaCalendar.get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)
				&& sofZmanKidushLevanaCalendar.get(Calendar.MONTH) == getCalendar().get(Calendar.MONTH)
				&& sofZmanKidushLevanaCalendar.get(Calendar.DAY_OF_MONTH) == getCalendar().get(Calendar.DAY_OF_MONTH)) {
			if (sofZmanKidushLevana.after(alos) && sofZmanKidushLevana.before(tzais)) {
				return alos;
			} else {
				return sofZmanKidushLevana;
			}
		}
		return null;
	}

	/**
	 * Returns the latest time of Kiddush Levana according to the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion that it is calculated as
	 * halfway between molad and molad. This adds half the 29 days, 12 hours and 793 chalakim time between
	 * <em>molad</em> and <em>molad</em> (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's molad. If
	 * the time of <em>sof zman Kiddush Levana</em> occurs during the day (between
	 * <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it
	 * return the <em>alos</em> prior to the calculated <em>sof zman Kiddush Levana</em>. This method is available in
	 * the 1.3 release of the API but may change or be removed in the future since it depends on the still changing
	 * {@link JewishCalendar} and related classes.
	 * 
	 * @return the Date representing the moment halfway between molad and molad. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * @see #getSofZmanKidushLevanaBetweenMoldos(Date, Date)
	 * @see #getSofZmanKidushLevana15Days()
	 */
	public Date getSofZmanKidushLevanaBetweenMoldos() {
		return getSofZmanKidushLevanaBetweenMoldos(getAlos72(), getTzais72());
	}

	/**
	 * Returns the latest time of <em>Kiddush Levana<em> calculated as 15 days after the <em>molad</em>. This is the
	 * opinion brought down in the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that the
	 * <http://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the opinion of the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> of calculating
	 * {@link #getSofZmanKidushLevanaBetweenMoldos(Date, Date) half way between molad and mold} is of the opinion that
	 * Mechaber agrees to his opinion. Also see the Aruch Hashulchan. For additional details on the subject, See Rabbi
	 * Dovid Heber's very detailed writeup in Siman Daled (chapter 4) of <a
	 * href="http://www.worldcat.org/oclc/461326125">Shaarei Zmanim</a>. If the time of <em>sof zman Kiddush Levana</em>
	 * occurs during the day (between the <em>alos</em> and <em>tzais</em> passed in as parameters), it returns the
	 * <em>alos</em> passed in. This method is available in the 1.3 release of the API but may change or be removed in
	 * the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @param alos
	 *            the begining of the Jewish day
	 * @param tzais
	 *            the end of the Jewish day
	 * @return the Date representing the moment 15 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>alos</em> will be returned
	 * 
	 * @see #getSofZmanKidushLevanaBetweenMoldos(Date, Date)
	 */
	public Date getSofZmanKidushLevana15Days(Date alos, Date tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		Calendar sofZmanKidushLevanaCalendar = (Calendar) getCalendar().clone();
		Date sofZmanKidushLevana = jewishCalendar.getSofZmanKidushLevana15Days();
		sofZmanKidushLevanaCalendar.setTime(sofZmanKidushLevana);
		if (alos != null && tzais != null
				&& sofZmanKidushLevanaCalendar.get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)
				&& sofZmanKidushLevanaCalendar.get(Calendar.MONTH) == getCalendar().get(Calendar.MONTH)
				&& sofZmanKidushLevanaCalendar.get(Calendar.DAY_OF_MONTH) == getCalendar().get(Calendar.DAY_OF_MONTH)) {
			if (sofZmanKidushLevana.after(alos) && sofZmanKidushLevana.before(tzais)) {
				return alos;
			} else {
				return sofZmanKidushLevana;
			}
		}
		return null;
	}

	/**
	 * Returns the latest time of Kiddush Levana calculated as 15 days after the molad. This is the opinion brought down
	 * in the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that the
	 * <http://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the opinion of the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> of calculating
	 * {@link #getSofZmanKidushLevanaBetweenMoldos(Date, Date) half way between molad and mold} is of the opinion that
	 * Mechaber agrees to his opinion. Also see the Aruch Hashulchan. For additional details on the subject, See Rabbi
	 * Dovid Heber's very detailed writeup in Siman Daled (chapter 4) of <a
	 * href="http://www.worldcat.org/oclc/461326125">Shaarei Zmanim</a>. If the time of <em>sof zman Kiddush Levana</em>
	 * occurs during the day (between <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and
	 * <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it return the <em>alos</em> prior to the calculated
	 * <em>sof zman Kiddush Levana</em>. This method is available in the 1.3 release of the API but may change or be
	 * removed in the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @return the Date representing the moment 15 days after the <em>molad</em>. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * 
	 * @see #getSofZmanKidushLevana15Days(Date, Date)
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 */
	public Date getSofZmanKidushLevana15Days() {
		return getSofZmanKidushLevana15Days(getAlos72(), getTzais72());
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to <em>Rabbainu Yonah</em>'s opinion that it can
	 * be said 3 days after the molad.If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between
	 * <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it
	 * return the next <em>tzais</em>. This method is available in the 1.3 release of the API but may change or be
	 * removed in the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @param alos
	 *            the begining of the Jewish day
	 * @param tzais
	 *            the end of the Jewish day
	 * @return the Date representing the moment 3 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana3Days()
	 * @see #getTchilasZmanKidushLevana7Days(Date, Date)
	 */
	public Date getTchilasZmanKidushLevana3Days(Date alos, Date tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		Date tchilasZmanKidushLevana = jewishCalendar.getTchilasZmanKidushLevana3Days();
		Calendar tchilasZmanKidushLevanaCalendar = (Calendar) getCalendar().clone();
		tchilasZmanKidushLevanaCalendar.setTime(tchilasZmanKidushLevana);
		if (alos != null
				&& tzais != null
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.MONTH) == getCalendar().get(Calendar.MONTH)
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.DAY_OF_MONTH) == getCalendar().get(
						Calendar.DAY_OF_MONTH)) {
			if (tchilasZmanKidushLevana.after(alos) && tchilasZmanKidushLevana.before(tzais)) {
				return tzais;
			} else {
				return tchilasZmanKidushLevana;
			}
		}
		return null;
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to <em>Rabbainu Yonah</em>'s opinion that it can
	 * be said 3 days after the molad.If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between
	 * <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it
	 * return the next <em>tzais</em>. This method is available in the 1.3 release of the API but may change or be
	 * removed in the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @return the Date representing the moment 3 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana3Days(Date, Date)
	 * @see #getTchilasZmanKidushLevana7Days()
	 */
	public Date getTchilasZmanKidushLevana3Days() {
		return getTchilasZmanKidushLevana3Days(getAlos72(), getTzais72());
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to the opinions that it should not be said until 7
	 * days after the molad. If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between
	 * <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it
	 * return the next <em>tzais</em>. This method is available in the 1.3 release of the API but may change or be
	 * removed in the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @param alos
	 *            the begining of the Jewish day
	 * @param tzais
	 *            the end of the Jewish day
	 * @return the Date representing the moment 7 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana3Days(Date, Date)
	 * @see #getTchilasZmanKidushLevana7Days()
	 */
	public Date getTchilasZmanKidushLevana7Days(Date alos, Date tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));

		Calendar tchilasZmanKidushLevanaCalendar = (Calendar) getCalendar().clone();
		Date tchilasZmanKidushLevana = jewishCalendar.getTchilasZmanKidushLevana7Days();
		tchilasZmanKidushLevanaCalendar.setTime(tchilasZmanKidushLevana);
		if (alos != null
				&& tzais != null
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.MONTH) == getCalendar().get(Calendar.MONTH)
				&& tchilasZmanKidushLevanaCalendar.get(Calendar.DAY_OF_MONTH) == getCalendar().get(
						Calendar.DAY_OF_MONTH)) {
			if (tchilasZmanKidushLevana.after(alos) && tchilasZmanKidushLevana.before(tzais)) {
				return tzais;
			} else {
				return tchilasZmanKidushLevana;
			}
		}
		return null;
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to the opinions that it should not be said until 7
	 * days after the molad. If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between
	 * <em>{@link ZmanimCalendar#getAlos72() Alos}</em> and <em>{@link ZmanimCalendar#getTzais72() tzais}</em>) it
	 * return the next <em>tzais</em>. This method is available in the 1.3 release of the API but may change or be
	 * removed in the future since it depends on the still changing {@link JewishCalendar} and related classes.
	 * 
	 * @return the Date representing the moment 7 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana7Days(Date, Date)
	 * @see #getTchilasZmanKidushLevana3Days()
	 */
	public Date getTchilasZmanKidushLevana7Days() {
		return getTchilasZmanKidushLevana7Days(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the latest time one is allowed eating chametz on Erev Pesach according to the opinion of the
	 * <em>GRA</em> and the </em>Baal Hatanya</em>. This time is identical to the {@link #getSofZmanTfilaGRA() Sof zman
	 * tefilah GRA}. This time is 4 hours into the day based on the opinion of the <em>GRA</em> and the </em>Baal
	 * Hatanya</em> that the day is calculated from sunrise to sunset. This returns the time 4 *
	 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see ZmanimCalendar#getShaahZmanisGra()
	 * @see ZmanimCalendar#getSofZmanTfilaGRA()
	 * @return the <code>Date</code> one is allowed eating chametz on Erev Pesach. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanAchilasChametzGRA() {
		return getSofZmanTfilaGRA();
	}

	/**
	 * This method returns the latest time one is allowed eating chametz on Erev Pesach according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}.
	 * This time is identical to the {@link #getSofZmanTfilaMGA72Minutes() Sof zman tefilah MGA 72 minutes}. This time
	 * is 4 <em>{@link #getShaahZmanisMGA() shaos zmaniyos}</em> (temporal hours) after {@link #getAlos72() dawn} based
	 * on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos72() dawn} of 72 minutes
	 * before sunrise to {@link #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanisMGA()} after {@link #getAlos72() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time of eating chametz. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set), a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 * @see #getSofZmanTfilaMGA72Minutes()
	 */
	public Date getSofZmanAchilasChametzMGA72Minutes() {
		return getSofZmanTfilaMGA72Minutes();
	}

	/**
	 * This method returns the latest time one is allowed eating chametz on Erev Pesach according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 4 <em>{@link #getShaahZmanis16Point1Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos16Point1Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of
	 * 4 {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time of eating chametz. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 * @see #getSofZmanTfilaMGA16Point1Degrees()
	 */
	public Date getSofZmanAchilasChametzMGA16Point1Degrees() {
		return getSofZmanTfilaMGA16Point1Degrees();
	}

	/**
	 * This method returns the latest time for burning chametz on Erev Pesach according to the opinion of the
	 * <em>GRA</em> and the </em>Baal Hatanya</em>. This time is 5 hours into the day based on the opinion of the
	 * <em>GRA</em> and the </em>Baal Hatanya</em> that the day is calculated from sunrise to sunset. This returns the
	 * time 5 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see ZmanimCalendar#getShaahZmanisGra()
	 * @return the <code>Date</code> of the latest time for burning chametz on Erev Pesach. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanBiurChametzGRA() {
		return getTimeOffset(getSeaLevelSunrise(), getShaahZmanisGra() * 5);
	}

	/**
	 * This method returns the latest time for burning chametz on Erev Pesach according to the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}.
	 * This time is 5 <em>{@link #getShaahZmanisMGA() shaos zmaniyos}</em> (temporal hours) after {@link #getAlos72()
	 * dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos72() dawn} of 72
	 * minutes before sunrise to {@link #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 5
	 * * {@link #getShaahZmanisMGA()} after {@link #getAlos72() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time for burning chametz on Erev Pesach. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set), a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 */
	public Date getSofZmanBiurChametzMGA72Minutes() {
		return getTimeOffset(getAlos72(), getShaahZmanisMGA() * 5);
	}

	/**
	 * This method returns the latest time for burning <em>chametz</em> on <em>Erev Pesach</em> according to the opinion
	 * of the <em>MGA</em> based on <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before
	 * {@link #getSunrise() sunrise}. This time is 5 <em>{@link #getShaahZmanis16Point1Degrees() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getAlos16Point1Degrees() dawn} based on the opinion of the <em>MGA</em> that the day
	 * is calculated from dawn to nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of
	 * 5 {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time for burning chametz on Erev Pesach. If the calculation can't be
	 *         computed such as northern and southern locations even south of the Arctic Circle and north of the
	 *         Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Date getSofZmanBiurChametzMGA16Point1Degrees() {
		return getTimeOffset(getAlos16Point1Degrees(), getShaahZmanis16Point1Degrees() * 5);
	}

	/**
	 * A method that returns "solar" midnight, or the time when the sun is at its <a
	 * href="http://en.wikipedia.org/wiki/Nadir">nadir</a>. <br/>
	 * <br/>
	 * <b>Note:</b> this method is experimental and might be removed.
	 * 
	 * @return the <code>Date</code> of Solar Midnight (chatzos layla). If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getSolarMidnight() {
		ZmanimCalendar clonedCal = (ZmanimCalendar) clone();
		clonedCal.getCalendar().add(Calendar.DAY_OF_MONTH, 1);
		Date sunset = getSeaLevelSunset();
		Date sunrise = clonedCal.getSeaLevelSunrise();
		return getTimeOffset(sunset, getTemporalHour(sunset, sunrise) * 6);
	}
}