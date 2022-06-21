/*
 * Zmanim Java API
 * Copyright (C) 2004-2022 Eliyahu Hershfeld
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
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim;

import java.util.Calendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;

/**
 * <p>This class extends ZmanimCalendar and provides many more <em>zmanim</em> than available in the ZmanimCalendar. The basis
 * for most <em>zmanim</em> in this class are from the <em>sefer</em> <b><a href="https://hebrewbooks.org/9765">Yisroel
 * Vehazmanim</a></b> by <b><a href="https://en.wikipedia.org/wiki/Yisroel_Dovid_Harfenes">Rabbi Yisrael Dovid Harfenes</a></b>.
 * As an example of the number of different <em>zmanim</em> made available by this class, there are methods to return 18
 * different calculations for <em>alos</em> (dawn), 18 for <em>plag hamincha</em> and 29 for <em>tzais</em> available in this
 * API. The real power of this API is the ease in calculating <em>zmanim</em> that are not part of the library. The methods for
 * <em>zmanim</em> calculations not present in this class or it's superclass  {@link ZmanimCalendar} are contained in the
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
 * "https://www.oracle.com/java/technologies/tzdata-versions.html">up to date timezone database</a>, create a
 * {@link java.util.SimpleTimeZone} with the known start and end of DST.
 * To get <em>alos</em> calculated as 14&deg; below the horizon (as calculated in the calendars published in Montreal),
 * add {@link AstronomicalCalendar#GEOMETRIC_ZENITH} (90) to the 14&deg; offset to get the desired time:
 * <br><br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 *  Date alos14 = czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 14);</pre>
 * <p>
 * To get <em>mincha gedola</em> calculated based on the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern"
 * >Magen Avraham (MGA)</a> using a <em>shaah zmanis</em> based on the day starting
 * 16.1&deg; below the horizon (and ending 16.1&deg; after sunset) the following calculation can be used:
 * 
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date minchaGedola = czc.getTimeOffset(czc.getAlos16point1Degrees(), czc.getShaahZmanis16Point1Degrees() * 6.5);</pre>
 * <p>
 * or even simpler using the included convenience methods
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date minchaGedola = czc.getMinchaGedola(czc.getAlos16point1Degrees(), czc.getShaahZmanis16Point1Degrees());</pre>
 * <p>
 * A little more complex example would be calculating <em>zmanim</em> that rely on a <em>shaah zmanis</em> that is
 * not present in this library. While a drop more complex, it is still rather easy. An example would be to calculate
 * the <a href="https://en.wikipedia.org/wiki/Israel_Isserlein">Trumas Hadeshen</a>'s <em>alos</em> to
 * <em>tzais</em> based <em>plag hamincha</em> as calculated in the Machzikei Hadass calendar in Manchester, England.
 * A number of this calendar's <em>zmanim</em> are calculated based on a day starting at <em>alos</em> of 12&deg; before
 * sunrise and ending at <em>tzais</em> of 7.083&deg; after sunset. Be aware that since the <em>alos</em> and <em>tzais</em>
 * do not use identical degree-based offsets, this leads to <em>chatzos</em> being at a time other than the
 * {@link #getSunTransit() solar transit} (solar midday). To calculate this <em>zman</em>, use the following steps. Note
 * that <em>plag hamincha</em> is 10.75 hours after the start of the day, and the following steps are all that it takes.
 * <br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date plag = czc.getPlagHamincha(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12),
 * 				czc.getSunsetOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + ZENITH_7_POINT_083));</pre>
 * <p>
 * Something a drop more challenging, but still simple, would be calculating a <em>zman</em> using the same "complex"
 * offset day used in the above mentioned Manchester calendar, but for a <em>shaos zmaniyos</em> based <em>zman</em> not
 * supported by this library, such as calculating the point that one should be <em>makpid</em>
 * not to eat on <em>erev Shabbos</em> or <em>erev Yom Tov</em>. This is 9 <em>shaos zmaniyos</em> into the day.
 * <ol>
 * 	<li>Calculate the <em>shaah zmanis</em> in milliseconds for this day</li>
 * 	<li>Add 9 of these <em>shaos zmaniyos</em> to <em>alos</em> starting at 12&deg;</li>
 * </ol>
 * <br>
 * <pre style="background: #FEF0C9; display: inline-block;">
 * long shaahZmanis = czc.getTemporalHour(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12),
 * 						czc.getSunsetOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + ZENITH_7_POINT_083));
 * Date sofZmanAchila = getTimeOffset(czc.getSunriseOffsetByDegrees({@link AstronomicalCalendar#GEOMETRIC_ZENITH} + 12),
 * 					shaahZmanis * 9);</pre>
 * <p>
 * Calculating this <em>sof zman achila</em> according to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>
 * is simplicity itself.
 * <pre style="background: #FEF0C9; display: inline-block;">
 * Date sofZamnAchila = czc.getTimeOffset(czc.getSunrise(), czc.getShaahZmanisGra() * 9);</pre>
 * 
 * <h2>Documentation from the {@link ZmanimCalendar} parent class</h2>
 * {@inheritDoc}
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2022
 */
public class ComplexZmanimCalendar extends ZmanimCalendar {

	/**
	 * The zenith of 3.7&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> that <em>tzais</em> is the
	 * time it takes to walk 3/4 of a <em>Mil</em> at 18 minutes a <em>Mil</em>, or 13.5 minutes after sunset. The sun
	 * is 3.7&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} at this time in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>.
	 * 
	 * @see #getTzaisGeonim3Point7Degrees()
	 */
	protected static final double ZENITH_3_POINT_7 = GEOMETRIC_ZENITH + 3.7;

	/**
	 * The zenith of 3.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> that <em>tzais</em> is the
	 * time it takes to walk 3/4 of a <em>Mil</em> at 18 minutes a <em>Mil</em>, or 13.5 minutes after sunset. The sun
	 * is 3.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} at this time in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>.
	 * 
	 * @see #getTzaisGeonim3Point8Degrees()
	 */
	protected static final double ZENITH_3_POINT_8 = GEOMETRIC_ZENITH + 3.8;

	/**
	 * The zenith of 5.95&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun 24 minutes after sunset in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * which calculates to 5.95&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getTzaisGeonim5Point95Degrees()
	 */
	protected static final double ZENITH_5_POINT_95 = GEOMETRIC_ZENITH + 5.95;

	/**
	 * The zenith of 7.083&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This is often referred to as
	 * 7&deg;5' or 7&deg; and 5 minutes. This calculation is used for calculating <em>alos</em> (dawn) and
	 * <em>tzais</em> (nightfall) according to some opinions. This calculation is based on observation of 3 medium sized
	 * stars by Dr. Baruch Cohen in his calendar published in in 1899 in Strasbourg, France. This calculates to
	 * 7.0833333&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. The <a href="https://hebrewbooks.org/1053">Sh"Ut
	 * Melamed Leho'il</a> in Orach Chaim 30 agreed to this <em>zman</em>, as did the Sh"Ut Bnei Tziyon, Tenuvas Sadeh and
	 * it is very close to the time of the <a href="https://hebrewbooks.org/22044">Mekor Chesed</a> of the Sefer chasidim.
	 * It is close to the position of the sun 30 minutes after sunset in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, but not
	 * Exactly. The actual position of the sun 30 minutes after sunset in Jerusalem at the equilux is 7.205&deg; and
	 * 7.199&deg; at the equinox. See Hazmanim Bahalacha vol 2, pages 520-521 for details.
	 * @todo Hyperlink the proper sources.
	 * 
	 * @see #getTzaisGeonim7Point083Degrees()
	 * @see #getBainHasmashosRT13Point5MinutesBefore7Point083Degrees()
	 */
	protected static final double ZENITH_7_POINT_083 = GEOMETRIC_ZENITH + 7 + (5.0 / 60);

	/**
	 * The zenith of 10.2&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 45 minutes before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> which
	 * calculates to 10.2&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getMisheyakir10Point2Degrees()
	 */
	protected static final double ZENITH_10_POINT_2 = GEOMETRIC_ZENITH + 10.2;

	/**
	 * The zenith of 11&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 48 minutes before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * calculates to 11&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getMisheyakir11Degrees()
	 */
	protected static final double ZENITH_11_DEGREES = GEOMETRIC_ZENITH + 11;

	/**
	 * The zenith of 11.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun
	 * 52 minutes before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * calculates to 11.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getMisheyakir11Point5Degrees()
	 */
	protected static final double ZENITH_11_POINT_5 = GEOMETRIC_ZENITH + 11.5;

	/**
	 * The zenith of 13.24&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating Rabbeinu Tam's <em>bain hashmashos</em> according to some opinions.
	 * NOTE: See comments on {@link #getBainHasmashosRT13Point24Degrees} for additional details about the degrees.
	 * 
	 * @see #getBainHasmashosRT13Point24Degrees
	 * 
	 */
	protected static final double ZENITH_13_POINT_24 = GEOMETRIC_ZENITH + 13.24;
	
	/**
	 * The zenith of 19&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> according to some opinions.
	 * 
	 * @see #getAlos19Degrees()
	 * @see #ZENITH_19_POINT_8
	 */
	protected static final double ZENITH_19_DEGREES = GEOMETRIC_ZENITH + 19;

	/**
	 * The zenith of 19.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> (dawn) and <em>tzais</em> (nightfall) according to some opinions. This calculation is
	 * based on the position of the sun 90 minutes after sunset in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> which
	 * calculates to 19.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getTzais19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 * @see #getAlos90()
	 * @see #getTzais90()
	 * @see #ZENITH_19_DEGREES
	 */
	protected static final double ZENITH_19_POINT_8 = GEOMETRIC_ZENITH + 19.8;

	/**
	 * The zenith of 26&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> (dawn) and <em>tzais</em> (nightfall) according to some opinions. This calculation is
	 * based on the position of the sun {@link #getAlos120() 120 minutes} after sunset in Jerusalem o<a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> which
	 * calculates to 26&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. Since the level of darkness when the sun is
	 * 26&deg; and at a point when the level of darkness is long past the 18&deg; point where the darkest point is reached,
	 * it should only be used <em>lechumra</em> such as delaying the start of nighttime <em>mitzvos</em> or avoiding eating
	 * this early on a fast day.
	 * 
	 * @see #getAlos26Degrees()
	 * @see #getTzais26Degrees()
	 * @see #getAlos120()
	 * @see #getTzais120()
	 */
	protected static final double ZENITH_26_DEGREES = GEOMETRIC_ZENITH + 26.0;

	/**
	 * The zenith of 4.37&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim4Point37Degrees() 16 7/8 minutes} after sunset (3/4 of a 22.5-minute <em>Mil</em>)
	 * in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * which calculates to 4.37&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getTzaisGeonim4Point37Degrees()
	 */
	protected static final double ZENITH_4_POINT_37 = GEOMETRIC_ZENITH + 4.37;

	/**
	 * The zenith of 4.61&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim4Point37Degrees() 18 minutes} after sunset (3/4 of a 24-minute <em>Mil</em>) in
	 * Jerusalem <a href="https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox
	 * / equilux</a>, which calculates to 4.61&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * @todo add documentation links
	 * 
	 * @see #getTzaisGeonim4Point61Degrees()
	 */
	protected static final double ZENITH_4_POINT_61 = GEOMETRIC_ZENITH + 4.61;

	/**
	 * The zenith of 4.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;).
	 * @todo Add more documentation.
	 * @see #getTzaisGeonim4Point8Degrees()
	 */
	protected static final double ZENITH_4_POINT_8 = GEOMETRIC_ZENITH + 4.8;

	/**
	 * The zenith of 3.65&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This calculation is based on the position of
	 * the sun {@link #getTzaisGeonim3Point65Degrees() 13.5 minutes} after sunset (3/4 of an 18-minute <em>Mil</em>)
	 * in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> which
	 * calculates to 3.65&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getTzaisGeonim3Point65Degrees()
	 */
	protected static final double ZENITH_3_POINT_65 = GEOMETRIC_ZENITH + 3.65;

	/**
	 * The zenith of 3.676&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;).
	 * @todo Add more documentation.
	 */
	protected static final double ZENITH_3_POINT_676 = GEOMETRIC_ZENITH + 3.676;

	/**
	 * The zenith of 5.88&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;).
	 * @todo Add more documentation.
	 */
	protected static final double ZENITH_5_POINT_88 = GEOMETRIC_ZENITH + 5.88;

	/**
	 * The zenith of 1.583&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>netz amiti</em> (sunrise) and <em>shkiah amiti</em> (sunset) based on the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>.
	 *
	 * @see #getSunriseBaalHatanya()
	 * @see #getSunsetBaalHatanya()
	 */
	protected static final double ZENITH_1_POINT_583 = GEOMETRIC_ZENITH + 1.583;

	/**
	 * The zenith of 16.9&deg; below geometric zenith (90&deg;). This calculation is used for determining <em>alos</em>
	 * (dawn) based on the opinion of the Baal Hatanya. It is based on the calculation that the time between dawn
	 * and <em>netz amiti</em> (sunrise) is 72 minutes, the time that is takes to walk 4 <em>mil</em> at 18 minutes
	 * a <em>mil</em> (<a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> and others). The sun's position at 72
	 * minutes before {@link #getSunriseBaalHatanya <em>netz amiti</em> (sunrise)} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> is
	 * 16.9&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 *
	 * @see #getAlosBaalHatanya()
	 */
	protected static final double ZENITH_16_POINT_9 = GEOMETRIC_ZENITH + 16.9;

	/**
	 * The zenith of 6&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> / nightfall based on the opinion of the Baal Hatanya. This calculation is based on the
	 * position of the sun 24 minutes after {@link #getSunset sunset} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * is 6&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 *
	 * @see #getTzaisBaalHatanya()
	 */
	protected static final double ZENITH_6_DEGREES = GEOMETRIC_ZENITH + 6;

	/**
	 * The zenith of 6.45&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions. This is based on the calculations of <a href=
	 * "https://en.wikipedia.org/wiki/Yechiel_Michel_Tucazinsky">Rabbi Yechiel Michel Tucazinsky</a> of the position of
	 * the sun no later than {@link #getTzaisGeonim6Point45Degrees() 31 minutes} after sunset in Jerusalem, and at the
	 * height of the summer solstice, this <em>zman</em> is 28 minutes after <em>shkiah</em>. This computes to 6.45&deg;
	 * below {@link #GEOMETRIC_ZENITH geometric zenith}. This calculation is found in the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=50536&st=&pgnum=51">Birur Halacha Yoreh Deah 262</a> it the commonly
	 * used <em>zman</em> in Israel. It should be noted that this differs from the 6.1&deg;/6.2&deg; calculation for
	 * Rabbi Tucazinsky's time as calculated by the Hazmanim Bahalacha Vol II chapter 50:7 (page 515).
	 * 
	 * @see #getTzaisGeonim6Point45Degrees()
	 */
	protected static final double ZENITH_6_POINT_45 = GEOMETRIC_ZENITH + 6.45;
	
	/**
	 * The zenith of 7.65&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions.
	 * 
	 * @see #getMisheyakir7Point65Degrees()
	 */
	protected static final double ZENITH_7_POINT_65 = GEOMETRIC_ZENITH + 7.65;
	
	/**
	 * The zenith of 7.67&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> according to some opinions.
	 * 
	 * @see #getTzaisGeonim7Point67Degrees()
	 */
	protected static final double ZENITH_7_POINT_67 = GEOMETRIC_ZENITH + 7.67;
	
	/**
	 * The zenith of 9.3&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>tzais</em> (nightfall) according to some opinions.
	 * 
	 * @see #getTzaisGeonim9Point3Degrees()
	 */
	protected static final double ZENITH_9_POINT_3 = GEOMETRIC_ZENITH + 9.3;
	
	/**
	 * The zenith of 9.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>misheyakir</em> according to some opinions.
	 * 
	 * @see #getMisheyakir9Point5Degrees()
	 */
	protected static final double ZENITH_9_POINT_5 = GEOMETRIC_ZENITH + 9.5;
	
	/**
	 * The zenith of 9.75&deg; below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating <em>alos</em> (dawn) and <em>tzais</em> (nightfall) according to some opinions.
	 * 
	 * @see #getTzaisGeonim9Point75Degrees()
	 */
	protected static final double ZENITH_9_POINT_75 = GEOMETRIC_ZENITH + 9.75;
	
	/**
	 * The zenith of 2.1&deg; above {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating the start of <em>bain hashmashos</em> (twilight) of 13.5 minutes before sunset converted to degrees
	 * according to the Yereim. As is traditional with degrees below the horizon, this is calculated without refraction
	 * and from the center of the sun. It would be 0.833&deg; less without this. 
	 * 
	 * @see #getBainHasmashosYereim2Point1Degrees()
	 */
	protected static final double ZENITH_MINUS_2_POINT_1 = GEOMETRIC_ZENITH - 2.1;
	
	/**
	 * The zenith of 2.8&deg; above {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating the start of <em>bain hashmashos</em> (twilight) of 16.875 minutes before sunset converted to degrees
	 * according to the Yereim. As is traditional with degrees below the horizon, this is calculated without refraction
	 * and from the center of the sun. It would be 0.833&deg; less without this.
	 * 
	 * @see #getBainHasmashosYereim2Point8Degrees()
	 */
	protected static final double ZENITH_MINUS_2_POINT_8 = GEOMETRIC_ZENITH - 2.8;
	
	/**
	 * The zenith of 3.05&deg; above {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for
	 * calculating the start of <em>bain hashmashos</em> (twilight) of 18 minutes before sunset converted to degrees
	 * according to the Yereim. As is traditional with degrees below the horizon, this is calculated without refraction
	 * and from the center of the sun. It would be 0.833&deg; less without this.
	 * 
	 * @see #getBainHasmashosYereim3Point05Degrees()
	 */
	protected static final double ZENITH_MINUS_3_POINT_05 = GEOMETRIC_ZENITH - 3.05;

	/**
	 * The offset in minutes (defaults to 40) after sunset used for <em>tzeit</em> based on calculations of
	 * <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah. 
	 * @see #getTzaisAteretTorah()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	private double ateretTorahSunsetOffset = 40;

	/**
	 * A constructor that takes a {@link GeoLocation} as a parameter.
	 * 
	 * @param location
	 *            the location
	 * 
	 * @see ZmanimCalendar#ZmanimCalendar(GeoLocation)
	 */
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
	 * divides the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen
	 * Avraham (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is when the sun is 19.8&deg;
	 * below the eastern geometric horizon before sunrise. Dusk for this is when the sun is 19.8&deg; below the western
	 * geometric horizon after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>.
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
	 * the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham
	 * (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is when the sun is 18&deg; below the
	 * eastern geometric horizon before sunrise. Dusk for this is when the sun is 18&deg; below the western geometric
	 * horizon after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>.
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
	 * divides the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen
	 * Avraham (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is when the sun is
	 * {@link #getAlos26Degrees() 26&deg;} below the eastern geometric horizon before sunrise. Dusk for this is when
	 * the sun is {@link #getTzais26Degrees() 26&deg;} below the western geometric horizon after sunset. This day is
	 * split into 12 equal parts with each part being a <em>shaah zmanis</em>. Since <em>zmanim</em> that use this
	 * method are extremely late or early and at a point when the sky is a long time past the 18&deg; point where the
	 * darkest point is reached, <em>zmanim</em> that use this should only be used <em>lechumra</em>, such as
	 * delaying the start of nighttime <em>mitzvos</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a {@link Long#MIN_VALUE}
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis120Minutes()
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
	 * Method to return a <em>shaah zmanis</em> (solar hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a>. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * 60 minutes before sunrise and dusk is 60 minutes after sunset. This day is split into 12 equal parts with each
	 * part being a <em>shaah zmanis</em>. Alternate methods of calculating a <em>shaah zmanis</em> are available in the
	 * subclass {@link ComplexZmanimCalendar}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos60()
	 * @see #getTzais60()
	 * @see #getPlagHamincha60Minutes()
	 */
	public long getShaahZmanis60Minutes() {
		return getTemporalHour(getAlos60(), getTzais60());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (solar hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a>. This calculation divides the day
	 * based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is 72 minutes
	 * before sunrise and dusk is 72 minutes after sunset. This day is split into 12 equal parts with each part
	 * being a <em>shaah zmanis</em>. Alternate methods of calculating a <em>shaah zmanis</em> are available in the
	 * subclass {@link ComplexZmanimCalendar}.
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
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em> being
	 * {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This calculation
	 * divides the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation
	 * is 72 minutes <em>zmaniyos</em> before sunrise and dusk is 72 minutes <em>zmaniyos</em> after sunset. This day
	 * is split into 12 equal parts with each part being a <em>shaah zmanis</em>. This is identical to 1/10th of the day
	 * from {@link #getSunrise() sunrise} to {@link #getSunset() sunset}.
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
	 * divides the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen
	 * Avraham (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is 90 minutes before sunrise
	 * and dusk is 90 minutes after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>.
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
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em> being
	 * {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This calculation divides
	 * the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is 90 minutes
	 * <em>zmaniyos</em> before sunrise and dusk is 90 minutes <em>zmaniyos</em> after sunset. This day is split into 12 equal
	 * parts with each part being a <em>shaah zmanis</em>. This is 1/8th of the day from {@link #getSunrise() sunrise} to
	 * {@link #getSunset() sunset}.
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
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em> being {@link
	 * #getAlos96Zmanis() 96} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This calculation divides the
	 * day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is 96 minutes
	 * <em>zmaniyos</em> before sunrise and dusk is 96 minutes <em>zmaniyos</em> after sunset. This day is split into 12
	 * equal parts with each part being a <em>shaah zmanis</em>. This is identical to 1/7.5th of the day from
	 * {@link #getSunrise() sunrise} to {@link #getSunset() sunset}.
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
	 * <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah calculated with <em>alos</em> being 1/10th
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
	 * divides the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen
	 * Avraham (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is 96 minutes before sunrise
	 * and dusk is 96 minutes after sunset. This day is split into 12 equal parts with each part being a <em>shaah
	 * zmanis</em>.
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
	 * divides the day based on the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen
	 * Avraham (MGA)</a> that the day runs from dawn to dusk. Dawn for this calculation is 120 minutes before sunrise and
	 * dusk is 120 minutes after sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>.
	 * Since <em>zmanim</em> that use this method are extremely late or early and at a point when the sky is a long time
	 * past the 18&deg; point where the darkest point is reached, <em>zmanim</em> that use this should only be used
	 * <em>lechumra</em> only, such as delaying the start of nighttime <em>mitzvos</em>.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis26Degrees()
	 */
	public long getShaahZmanis120Minutes() {
		return getTemporalHour(getAlos120(), getTzais120());
	}

	/**
	 * Method to return a <em>shaah zmanis</em> (temporal hour) according to the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em> being {@link
	 * #getAlos120Zmanis() 120} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This calculation divides
	 * the day based on the opinion of the MGA that the day runs from dawn to dusk. Dawn for this calculation is
	 * 120 minutes <em>zmaniyos</em> before sunrise and dusk is 120 minutes <em>zmaniyos</em> after sunset. This day is
	 * split into 12 equal parts with each part being a <em>shaah zmanis</em>. This is identical to 1/6th of the day from
	 * {@link #getSunrise() sunrise} to {@link #getSunset() sunset}. Since <em>zmanim</em> that use this method are
	 * extremely late or early and at a point when the sky is a long time past the 18&deg; point where the darkest point
	 * is reached, <em>zmanim</em> that use this should only be used <em>lechumra</em> such as delaying the start of
	 * nighttime <em>mitzvos</em>.
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
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on sunrise
	 * being 120 minutes <em>zmaniyos</em> or 1/6th of the day before sunrise. This is calculated as 10.75 hours after
	 * {@link #getAlos120Zmanis() dawn}. The formula used is 10.75 * {@link #getShaahZmanis120MinutesZmanis()} after
	 * {@link #getAlos120Zmanis() dawn}. Since the <em>zman</em> based on an extremely early <em>alos</em> and a very
	 * late <em>tzais</em>, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis120MinutesZmanis()
	 * @see #getAlos120()
	 * @see #getTzais120()
	 * @see #getPlagHamincha26Degrees()
	 * @see #getPlagHamincha120Minutes()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha120MinutesZmanis() {
		return getPlagHamincha(getAlos120Zmanis(), getTzais120Zmanis());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> according to the
	 * Magen Avraham with the day starting 120 minutes before sunrise and ending 120 minutes after sunset. This is
	 * calculated as 10.75 hours after {@link #getAlos120() dawn 120 minutes}. The formula used is 10.75 {@link
	 * #getShaahZmanis120Minutes()} after {@link #getAlos120()}. Since the <em>zman</em> based on an extremely early
	 * <em>alos</em> and a very late <em>tzais</em>, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis120Minutes()
	 * @see #getPlagHamincha26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha120Minutes() {
		return getPlagHamincha(getAlos120(), getTzais120());
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated as 60 minutes before sunrise. This is the time to walk the 
	 * distance of 4 <em>Mil</em> at 15 minutes a <em>Mil</em>. This seems to be the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Yair_Bacharach">Chavas Yair</a> in the Mekor Chaim, Orach Chaim Ch.
	 * 90, though  the Mekor Chaim in Ch. 58 and in the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=45193&pgnum=214">Chut Hashani Cha 97</a> states that
	 * a a person walks 3 and a 1/3 <em>mil</em> in an hour, or an 18-minute <em>mil</em>. Also see the <a href=
	 * "https://he.wikipedia.org/wiki/%D7%9E%D7%9C%D7%9B%D7%99%D7%90%D7%9C_%D7%A6%D7%91%D7%99_%D7%98%D7%A0%D7%A0%D7%91%D7%95%D7%99%D7%9D"
	 * >Divrei Malkiel</a> <a href="https://hebrewbooks.org/pdfpager.aspx?req=803&pgnum=33">Vol. 4, Ch. 20, page 34</a>) who
	 * mentions the 15 minute <em>mil</em> lechumra by baking matzos. Also see the <a href=
	 * "https://en.wikipedia.org/wiki/Joseph_Colon_Trabotto">Maharik</a> <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=1142&pgnum=216">Ch. 173</a> where the questioner quoting the
	 * <a href="https://en.wikipedia.org/wiki/Eliezer_ben_Nathan">Ra'avan</a> is of the opinion that the time to walk a
	 * <em>mil</em> is 15 minutes (5 <em>mil</em> in a little over an hour). There are many who believe that there is a
	 * <em>ta'us sofer</em> (scribe's error) in the Ra'avan, and it should 4 <em>mil</em> in a little over an hour, or an
	 * 18-minute <em>mil</em>. Time based offset calculations are based on the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Rishonim">Rishonim</a></em> who stated that the time of the <em>neshef</em>
	 * (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to
	 * walk the distance of 4* <em>mil</em>. {@link #getTzaisGeonim9Point75Degrees()} is a related <em>zman</em> that is a
	 * degree-based calculation based on 60 minutes.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}.
	 *         documentation.
	 *
	 * @see #getTzais60()
	 * @see #getPlagHamincha60Minutes()
	 * @see #getShaahZmanis60Minutes()
	 */
	public Long getAlos60() {
		return getTimeOffset(getSunrise(), -60 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 72 minutes <em>zmaniyos</em> or 1/10th of the day before
	 * sunrise. This is based on an 18-minute <em>Mil</em> so the time for 4 <em>Mil</em> is 72 minutes which is 1/10th
	 * of a day (12 * 60 = 720) based on the a day being from {@link #getSeaLevelSunrise() sea level sunrise} to
	 * {@link #getSeaLevelSunrise sea level sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset}
	 * (depending on the {@link #isUseElevation()} setting).
	 * The actual calculation is {@link #getSeaLevelSunrise()} - ({@link #getShaahZmanisGra()} * 1.2). This calculation
	 * is used in the calendars published by the <a href=
	 * "https://en.wikipedia.org/wiki/Central_Rabbinical_Congress">Hisachdus Harabanim D'Artzos Habris Ve'Canada</a>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Long getAlos72Zmanis() {
		return getZmanisBasedOffset(-1.2);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 96 minutes before before {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting) that is based
	 * on the time to walk the distance of 4 <em>Mil</em> at 24 minutes a <em>Mil</em>. Time based offset
	 * calculations for <em>alos</em> are based on the opinion of the <em><a href="https://en.wikipedia.org/wiki/Rishonim"
	 * >Rishonim</a></em> who stated that the time of the <em>Neshef</em> (time between dawn and sunrise) does not vary
	 * by the time of year or location but purely depends on the time it takes to walk the distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getAlos96() {
		return getTimeOffset(getElevationAdjustedSunrise(), -96 * MINUTE_MILLIS);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 90 minutes <em>zmaniyos</em> or 1/8th of the day before
	 * {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link
	 * #isUseElevation()} setting). This is based on a 22.5-minute <em>Mil</em> so the time for 4 <em>Mil</em> is 90
	 * minutes which is 1/8th of a day (12 * 60) / 8 = 90
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}.
	 * The actual calculation used is {@link #getSunrise()} - ({@link #getShaahZmanisGra()} * 1.5).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Long getAlos90Zmanis() {
		return getZmanisBasedOffset(-1.5);
	}

	/**
	 * This method returns <em>alos</em> (dawn) calculated using 96 minutes <em>zmaniyos</em> or 1/7.5th of the day before
	 * {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link
	 * #isUseElevation()} setting). This is based on a 24-minute <em>Mil</em> so the time for 4 <em>Mil</em> is 96
	 * minutes which is 1/7.5th of a day (12 * 60 / 7.5 = 96).
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}.
	 * The actual calculation used is {@link #getSunrise()} - ({@link #getShaahZmanisGra()} * 1.6).
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getShaahZmanisGra()
	 */
	public Long getAlos96Zmanis() {
		return getZmanisBasedOffset(-1.6);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 90 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise} based on the time to walk the distance of 4 <em>Mil</em> at 22.5 minutes a <em>Mil</em>. Time based
	 * offset calculations for <em>alos</em> are based on the opinion of the <em><a href=
	 * "https://en.wikipedia.org/wiki/Rishonim">Rishonim</a></em> who stated that the time of the <em>Neshef</em>
	 * (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it
	 * takes to walk the distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getAlos90() {
		return getTimeOffset(getElevationAdjustedSunrise(), -90 * MINUTE_MILLIS);
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns <em>alos</em> (dawn) calculated using 120 minutes
	 * before {@link #getSeaLevelSunrise() sea level sunrise} (no adjustment for elevation is made) based on the time
	 * to walk the distance of 5 <em>Mil</em>(<em>Ula</em>) at 24 minutes a <em>Mil</em>. Time based offset calculations
	 * for <em>alos</em> are based on the* opinion of the <em><a href="https://en.wikipedia.org/wiki/Rishonim">Rishonim</a>
	 * </em> who stated that the time of the <em>neshef</em> (time between dawn and sunrise) does not vary by the time of
	 * year or location but purely depends on the time it takes to walk the distance of 5 <em>Mil</em>(<em>Ula</em>). Since
	 * this time is extremely early, it should only be used <em>lechumra</em>, such as not eating after this time on a fast
	 * day, and not as the start time for <em>mitzvos</em> that can only be performed during the day.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only (such as stopping to eat at this time on a fast day),
	 *         since it returns a very early time, and if used <em>lekula</em> can result in doing <em>mitzvos hayom</em>
	 *         too early according to most opinions. There is no current plan to remove this method from the API, and this
	 *         deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * 
	 * @see #getTzais120()
	 * @see #getAlos26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getAlos120() {
		return getTimeOffset(getElevationAdjustedSunrise(), -120 * MINUTE_MILLIS);
	}

	/**
	 * This method should be used <em>lechumra</em> only and  method returns <em>alos</em> (dawn) calculated using
	 * 120 minutes <em>zmaniyos</em> or 1/6th of the day before {@link #getSunrise() sunrise} or {@link
	 * #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting). This is based
	 * on a 24-minute <em>Mil</em> so the time for 5 <em>Mil</em> is 120 minutes which is 1/6th of a day (12 * 60 /
	 * 6 = 120). The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link
	 * #getSeaLevelSunrise sea level sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending
	 * on the {@link #isUseElevation()}. The actual calculation used is {@link #getSunrise()} - ({@link
	 * #getShaahZmanisGra()} * 2). Since this time is extremely early, it should only be used <em>lechumra</em>, such
	 * as not eating after this time on a fast day, and not as the start time for <em>mitzvos</em> that can only be
	 * performed during the day.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only (such as stopping to eat at this time on a fast day),
	 *         since it returns a very early time, and if used <em>lekula</em> can result in doing <em>mitzvos hayom</em>
	 *         too early according to most opinions. There is no current plan to remove this method from the API, and this
	 *         deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos120()
	 * @see #getAlos26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getAlos120Zmanis() {
		return getZmanisBasedOffset(-2.0);
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns <em>alos</em> (dawn) calculated when the sun is {@link
	 * #ZENITH_26_DEGREES 26&deg;} below the eastern geometric horizon before sunrise. This calculation is based on the same
	 * calculation of {@link #getAlos120() 120 minutes} but uses a degree-based calculation instead of 120 exact minutes. This
	 * calculation is based on the position of the sun 120 minutes before sunrise in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * calculates to 26&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. Since this time is extremely early, it should
	 * only be used <em>lechumra</em> only, such as not eating after this time on a fast day, and not as the start time for
	 * <em>mitzvos</em> that can only be performed during the day.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only (such as stopping to eat at this time on a fast day),
	 *         since it returns a very early time, and if used <em>lekula</em> can result in doing <em>mitzvos hayom</em>
	 *         too early according to most opinions. There is no current plan to remove this  method from the API, and this
	 *         deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_26_DEGREES
	 * @see #getAlos120()
	 * @see #getTzais120()
	 * @see #getTzais26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getAlos26Degrees() {
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
	public Long getAlos18Degrees() {
		return getSunriseOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}
	
	/**
	 * A method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_19_DEGREES 19&deg;} below the
	 * eastern geometric horizon before sunrise. This is the <a href="https://en.wikipedia.org/wiki/Maimonides"
	 * >Rambam</a>'s <em>alos</em> according to Rabbi Moshe Kosower's <a href=
	 * "https://www.worldcat.org/oclc/145454098">Maaglei Tzedek</a>, page 88, <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=33464&pgnum=13">Ayeles Hashachar Vol. I, page 12</a>, <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=55960&pgnum=258">Yom Valayla Shel Torah, Ch. 34, p. 222</a> and 
	 * Rabbi Yaakov Shakow's <a href="https://www.worldcat.org/oclc/1043573513">Luach Ikvei Hayom</a>.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ASTRONOMICAL_ZENITH
	 */
	public Long getAlos19Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_19_DEGREES);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_19_POINT_8 19.8&deg;} below the
	 * eastern geometric horizon before sunrise. This calculation is based on the same calculation of
	 * {@link #getAlos90() 90 minutes} but uses a degree-based calculation instead of 90 exact minutes. This calculation
	 * is based on the position of the sun 90 minutes before sunrise in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * calculates to 19.8&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_19_POINT_8
	 * @see #getAlos90()
	 */
	public Long getAlos19Point8Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_19_POINT_8);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated when the sun is {@link #ZENITH_16_POINT_1 16.1&deg;} below the
	 * eastern geometric horizon before sunrise. This calculation is based on the same calculation of
	 * {@link #getAlos72() 72 minutes} but uses a degree-based calculation instead of 72 exact minutes. This calculation
	 * is based on the position of the sun 72 minutes before sunrise in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * calculates to 16.1&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return the <code>Date</code> representing <em>alos</em>. If the calculation can't be computed such as northern
	 *         and southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun
	 *         may not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_16_POINT_1
	 * @see #getAlos72()
	 */
	public Long getAlos16Point1Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_11_DEGREES
	 * 11.5&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 52 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * which calculates to 11.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * @todo recalculate.
	 * 
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_11_POINT_5
	 */
	public Long getMisheyakir11Point5Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_11_POINT_5);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_11_DEGREES
	 * 11&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 48 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem d<a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * which calculates to 11&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return If the calculation can't be computed such as northern and southern locations even south of the Arctic
	 *         Circle and north of the Antarctic Circle where the sun may not reach low enough below the horizon for
	 *         this calculation, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_11_DEGREES
	 */
	public Long getMisheyakir11Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_11_DEGREES);
	}

	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_10_POINT_2
	 * 10.2&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is used for calculating
	 * <em>misheyakir</em> according to some opinions. This calculation is based on the position of the sun 45 minutes
	 * before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox</a> which calculates
	 * to 10.2&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_10_POINT_2
	 */
	public Long getMisheyakir10Point2Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_10_POINT_2);
	}
	
	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_7_POINT_65
	 * 7.65&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). The degrees are based on a 35/36 minute
	 * <em>zman</em> <a href="https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the
	 * equinox / equilux</a>, when the <em>neshef</em> (twilight) is the shortest. This time is based on <a href=
	 * "https://en.wikipedia.org/wiki/Moshe_Feinstein">Rabbi Moshe Feinstein</a> who writes in <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=14677&pgnum=7">Ohr Hachaim Vol. 4, Ch. 6</a>)
	 * that <em>misheyakir</em> in New York is 35-40 minutes before sunset, something that is a drop less than 8&deg;.
	 * <a href="https://en.wikipedia.org/wiki/Yisroel_Taplin">Rabbi Yisroel Taplin</a> in <a href=
	 * "https://www.worldcat.org/oclc/889556744">Zmanei Yisrael</a> (page 117) notes that <a href=
	 * "https://en.wikipedia.org/wiki/Yaakov_Kamenetsky">Rabbi Yaakov Kamenetsky</a> stated that it is not less than 36
	 * minutes before sunrise (maybe it is 40 minutes). Sefer Yisrael Vehazmanim (p. 7) quotes the Tamar Yifrach
	 * in the name of the <a href="https://en.wikipedia.org/wiki/Joel_Teitelbaum">Satmar Rov</a> that one should be stringent
	 * not consider <em>misheyakir</em> before 36 minutes. This is also the accepted <a href=
	 * "https://en.wikipedia.org/wiki/Minhag">minhag</a> in <a href=
	 * "https://en.wikipedia.org/wiki/Lakewood_Township,_New_Jersey">Lakewood</a> that is used in the <a href=
	 * "https://en.wikipedia.org/wiki/Beth_Medrash_Govoha">Yeshiva</a>. This follows the opinion of <a href=
	 * "https://en.wikipedia.org/wiki/Shmuel_Kamenetsky">Rabbi Shmuel Kamenetsky</a> who provided the time of 35/36 minutes,
	 * but did not provide a degree-based time. Since this <em>zman</em> depends on the level of light, Rabbi Yaakov Shakow
	 * presented this degree-based calculations to Rabbi Kamenetsky who agreed to them.
	 * 
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_7_POINT_65
	 * @see #getMisheyakir9Point5Degrees()
	 */
	public Long getMisheyakir7Point65Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_7_POINT_65);
	}
	
	/**
	 * This method returns <em>misheyakir</em> based on the position of the sun when it is {@link #ZENITH_9_POINT_5
	 * 9.5&deg;} below {@link #GEOMETRIC_ZENITH geometric zenith} (90&deg;). This calculation is based on Rabbi Dovid Kronglass's
	 * Calculation of 45 minutes in Baltimore as mentioned in <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=20287&pgnum=29">Divrei Chachamim No. 24</a> brought down by the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=50535&pgnum=87">Birur Halacha, Tinyana, Ch. 18</a>. This calculates to
	 * 9.5&deg;. Also see <a href="https://en.wikipedia.org/wiki/Jacob_Isaac_Neiman">Rabbi Yaakov Yitzchok Neiman</a> in Kovetz
	 * Eitz Chaim Vol. 9, p. 202 that the Vya'an Yosef did not want to rely on times earlier than 45 minutes in New York. This
	 * <em>zman</em> is also used in the calendars published by Rabbi Hershel Edelstein. As mentioned in Yisroel Vehazmanim,
	 * Rabbi Edelstein who was given the 45 minute <em>zman</em> by Rabbi Bick. The calendars published by the <em><a href=
	 * "https://en.wikipedia.org/wiki/Mizrahi_Jews">Edot Hamizrach</a></em> communities also use this <em>zman</em>. This also
	 * follows the opinion of <a href="https://en.wikipedia.org/wiki/Shmuel_Kamenetsky">Rabbi Shmuel Kamenetsky</a> who provided
	 * the time of 36 and 45 minutes, but did not provide a degree-based time. Since this <em>zman</em> depends on the level of
	 * light, Rabbi Yaakov Shakow presented these degree-based times to Rabbi Shmuel Kamenetsky who agreed to them.
	 * 
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_9_POINT_5
	 * @see #getMisheyakir7Point65Degrees()
	 */
	public Long getMisheyakir9Point5Degrees() {
		return getSunriseOffsetByDegrees(ZENITH_9_POINT_5);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos19Point8Degrees() 19.8&deg;} before {@link #getSunrise() sunrise}. This
	 * time is 3 {@link #getShaahZmanis19Point8Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link
	 * #getAlos19Point8Degrees() dawn} based on the opinion of the MGA that the day is calculated from dawn to nightfall
	 * with both being 19.8&deg; below sunrise or sunset. This returns the time of 3 *
	 * {@link #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 */
	public Long getSofZmanShmaMGA19Point8Degrees() {
		return getSofZmanShma(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before {@link #getSunrise() sunrise}. This time
	 * is 3 {@link #getShaahZmanis16Point1Degrees() <em>shaos zmaniyos</em>} (solar hours) after
	 * {@link #getAlos16Point1Degrees() dawn} based on the opinion of the MGA that the day is calculated from
	 * dawn to nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of
	 * 3 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Long getSofZmanShmaMGA16Point1Degrees() {
		return getSofZmanShma(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos18Degrees() 18&deg;} before {@link #getSunrise() sunrise}. This time is 3
	 * {@link #getShaahZmanis18Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos18Degrees() dawn}
	 * based on the opinion of the MGA that the day is calculated from dawn to nightfall with both being 18&deg;
	 * below sunrise or sunset. This returns the time of 3 * {@link #getShaahZmanis18Degrees()} after
	 * {@link #getAlos18Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis18Degrees()
	 * @see #getAlos18Degrees()
	 */
	public Long getSofZmanShmaMGA18Degrees() {
		return getSofZmanShma(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}. This time is 3 {@link
	 * #getShaahZmanis72Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos72() dawn} based on the opinion
	 * of the MGA that the day is calculated from a {@link #getAlos72() dawn} of 72 minutes before sunrise to
	 * {@link #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 3 * {@link
	 * #getShaahZmanis72Minutes()} after {@link #getAlos72() dawn}. This class returns an identical time to {@link
	 * #getSofZmanShmaMGA()} and is repeated here for clarity.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72Minutes()
	 * @see #getAlos72()
	 * @see #getSofZmanShmaMGA()
	 */
	public Long getSofZmanShmaMGA72Minutes() {
		return getSofZmanShmaMGA();
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite <em>Shema</em> in the morning) according
	 * to the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em>, or 1/10th of the day before
	 * {@link #getSunrise() sunrise}. This time is 3 {@link #getShaahZmanis90MinutesZmanis() <em>shaos zmaniyos</em>}
	 * (solar hours) after {@link #getAlos72Zmanis() dawn} based on the opinion of the MGA that the day is calculated
	 * from a {@link #getAlos72Zmanis() dawn} of 72 minutes <em>zmaniyos</em>, or 1/10th of the day before
	 * {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getTzais72Zmanis() nightfall} of 72 minutes
	 * <em>zmaniyos</em> after {@link #getSeaLevelSunset() sea level sunset}. This returns the time of 3 *
	 * {@link #getShaahZmanis72MinutesZmanis()} after {@link #getAlos72Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72MinutesZmanis()
	 * @see #getAlos72Zmanis()
	 */
	public Long getSofZmanShmaMGA72MinutesZmanis() {
		return getSofZmanShma(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite <em>Shema</em> in the morning) according
	 * to the opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos90() 90} minutes before {@link #getSunrise() sunrise}. This time is 3
	 * {@link #getShaahZmanis90Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos90() dawn} based on
	 * the opinion of the MGA that the day is calculated from a {@link #getAlos90() dawn} of 90 minutes before sunrise to
	 * {@link #getTzais90() nightfall} of 90 minutes after sunset. This returns the time of 3 *
	 * {@link #getShaahZmanis90Minutes()} after {@link #getAlos90() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90Minutes()
	 * @see #getAlos90()
	 */
	public Long getSofZmanShmaMGA90Minutes() {
		return getSofZmanShma(getAlos90(), getTzais90());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em> before {@link #getSunrise()
	 * sunrise}. This time is 3 {@link #getShaahZmanis90MinutesZmanis() <em>shaos zmaniyos</em>} (solar hours) after
	 * {@link #getAlos90Zmanis() dawn} based on the opinion of the MGA that the day is calculated from a {@link
	 * #getAlos90Zmanis() dawn} of 90 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais90Zmanis() nightfall}
	 * of 90 minutes <em>zmaniyos</em> after sunset. This returns the time of 3 * {@link #getShaahZmanis90MinutesZmanis()}
	 * after {@link #getAlos90Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Long getSofZmanShmaMGA90MinutesZmanis() {
		return getSofZmanShma(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos96() 96} minutes before {@link #getSunrise() sunrise}. This time is 3
	 * {@link #getShaahZmanis96Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos96() dawn} based on
	 * the opinion of the MGA that the day is calculated from a {@link #getAlos96() dawn} of 96 minutes before
	 * sunrise to {@link #getTzais96() nightfall} of 96 minutes after sunset. This returns the time of 3 * {@link
	 * #getShaahZmanis96Minutes()} after {@link #getAlos96() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 * @see #getAlos96()
	 */
	public Long getSofZmanShmaMGA96Minutes() {
		return getSofZmanShma(getAlos96(), getTzais96());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos90Zmanis() 96} minutes <em>zmaniyos</em> before {@link #getSunrise()
	 * sunrise}. This time is 3 {@link #getShaahZmanis96MinutesZmanis() <em>shaos zmaniyos</em>} (solar hours) after
	 * {@link #getAlos96Zmanis() dawn} based on the opinion of the MGA that the day is calculated from a {@link
	 * #getAlos96Zmanis() dawn} of 96 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais90Zmanis() nightfall}
	 * of 96 minutes <em>zmaniyos</em> after sunset. This returns the time of 3 * {@link #getShaahZmanis96MinutesZmanis()}
	 * after {@link #getAlos96Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96MinutesZmanis()
	 * @see #getAlos96Zmanis()
	 */
	public Long getSofZmanShmaMGA96MinutesZmanis() {
		return getSofZmanShma(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite <em>Shema</em> in the morning) calculated
	 * as 3 hours (regular clock hours and not <em>sha'os zmaniyos</em>) before {@link ZmanimCalendar#getChatzos()}.
	 * Generally known as part of the "Komarno" <em>zmanim</em> after <a href=
	 * "https://en.wikipedia.org/wiki/Komarno_(Hasidic_dynasty)#Rabbi_Yitzchak_Eisik_Safrin">Rav Yitzchak Eizik of
	 * Komarno</a>, a proponent of this calculation, it actually predates him a lot. It is the opinion of the 
	 * <em>Shach</em> in the Nekudas Hakesef (Yoreh Deah 184), <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=21638&st=&pgnum=30">Rav Moshe Lifshitz</a> in his commentary
	 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=21638&st=&pgnum=50">Lechem Mishneh on Brachos 1:2</a>. It is
	 * next brought down about 100 years later by the <a href="https://en.wikipedia.org/wiki/Jacob_Emden">Yaavetz</a>
	 * (in his <em>siddur</em>, <a href="https://hebrewbooks.org/pdfpager.aspx?req=7920&st=&pgnum=6">Mor Uktziah Orach
	 * Chaim 1</a>, <a href="https://hebrewbooks.org/pdfpager.aspx?req=22309&st=&pgnum=30">Lechem Shamayim, Brachos 1:2</a>
	 * and <a href="https://hebrewbooks.org/pdfpager.aspx?req=1408&st=&pgnum=69">She'elos Yaavetz vol. 1 no. 40</a>),
	 * Rav Yitzchak Eizik of Komarno in the Ma'aseh Oreg on Mishnayos Brachos 11:2, Shevus Yaakov, Chasan Sofer and others.
	 * See Yisrael Vehazmanim <a href="https://hebrewbooks.org/pdfpager.aspx?req=9765&st=&pgnum=83">vol. 1 7:3, page 55 -
	 * 62</a>. A variant of this calculation {@link #getSofZmanShmaFixedLocal()} uses {@link #getFixedLocalChatzos() fixed
	 * local <em>chatzos</em>} for calculating this type of <em>zman</em>.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see ZmanimCalendar#getChatzos()
	 * @see #getSofZmanShmaFixedLocal()
	 * @see #getSofZmanTfila2HoursBeforeChatzos()
	 */
	public Long getSofZmanShma3HoursBeforeChatzos() {
		return getTimeOffset(getChatzos(), -180 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based
	 * on <em>alos</em> being {@link #getAlos120() 120} minutes or 1/6th of the day before {@link #getSunrise() sunrise}.
	 * This time is 3 {@link #getShaahZmanis120Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos120()
	 * dawn} based on the opinion of the MGA that the day is calculated from a {@link #getAlos120() dawn} of 120 minutes
	 * before sunrise to {@link #getTzais120() nightfall} of 120 minutes after sunset. This returns the time of 3
	 * {@link #getShaahZmanis120Minutes()} after {@link #getAlos120() dawn}. This is an extremely early <em>zman</em> that
	 * is very much a <em>chumra</em>.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis120Minutes()
	 * @see #getAlos120()
	 */
	public Long getSofZmanShmaMGA120Minutes() {
		return getSofZmanShma(getAlos120(), getTzais120());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite <em>Shema</em> in the morning) based
	 * on the opinion that the day starts at {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} and ends at
	 * {@link #getSeaLevelSunset() sea level sunset}. This is the opinion of the <a href=
	 * "https://hebrewbooks.org/40357">\u05D7\u05D9\u05D3\u05D5\u05E9\u05D9
	 * \u05D5\u05DB\u05DC\u05DC\u05D5\u05EA \u05D4\u05E8\u05D6\u05F4\u05D4</a> and the <a href=
	 * "https://hebrewbooks.org/14799">\u05DE\u05E0\u05D5\u05E8\u05D4 \u05D4\u05D8\u05D4\u05D5\u05E8\u05D4</a> as
	 * mentioned by Yisrael Vehazmanim <a href="https://hebrewbooks.org/pdfpager.aspx?req=9765&pgnum=81">vol 1, sec. 7,
	 * ch. 3 no. 16</a>. Three <em>shaos zmaniyos</em> are calculated based on this day and added to {@link
	 * #getAlos16Point1Degrees() <em>alos</em>} to reach this time. This time is 3 <em>shaos zmaniyos</em> (solar hours)
	 * after {@link #getAlos16Point1Degrees() dawn} based on the opinion that the day is calculated from a {@link
	 * #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} to {@link #getSeaLevelSunset() sea level sunset}.
	 * <b>Note:</b> Based on this calculation <em>chatzos</em> will not be at midday.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this day. If the calculation can't
	 *         be computed such as northern and southern locations even south of the Arctic Circle and north of the
	 *         Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos16Point1Degrees()
	 * @see #getSeaLevelSunset()
	 */
	public Long getSofZmanShmaAlos16Point1ToSunset() {
		return getSofZmanShma(getAlos16Point1Degrees(), getElevationAdjustedSunset());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) based on the
	 * opinion that the day starts at {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} and ends at
	 * {@link #getTzaisGeonim7Point083Degrees() <em>tzais</em> 7.083&deg;}. 3 <em>shaos zmaniyos</em> are calculated
	 * based on this day and added to {@link #getAlos16Point1Degrees() <em>alos</em>} to reach this time. This time is 3
	 * <em>shaos zmaniyos</em> (temporal hours) after {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} based on
	 * the opinion that the day is calculated from a {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} to
	 * {@link #getTzaisGeonim7Point083Degrees() <em>tzais</em> 7.083&deg;}.
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
	public Long getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees() {
		return getSofZmanShma(getAlos16Point1Degrees(), getTzaisGeonim7Point083Degrees());
	}

	/**
	 * From the GRA in Kol Eliyahu on Berachos #173 that states that <em>zman krias shema</em> is calculated as half the
	 * time from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getFixedLocalChatzos() fixed local chatzos}.
	 * The GRA himself seems to contradict this when he stated that <em>zman krias shema</em> is 1/4 of the day from
	 * sunrise to sunset. See <em>Sarah Lamoed</em> #25 in Yisroel Vehazmanim Vol. III page 1016.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this calculation. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, a null will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getFixedLocalChatzos()
	 * 
	 * @deprecated As per a conversation Rabbi Yisroel Twerski had with Rabbi Harfenes, this <em>zman</em> published in
	 *         the Yisrael Vehazmanim was based on a misunderstanding and should not be used. This deprecated method
	 *         will be removed (likely in v3.0) pending confirmation from Rabbi Harfenes.
	 */
	@Deprecated // (since="1.3", forRemoval=true) // add back once Java 9 is the minimum supported version
	public Long getSofZmanShmaKolEliyahu() {
		Long chatzos = getFixedLocalChatzos();
		if (chatzos == null || getSunrise() == null) {
			return null;
		}
		long diff = (chatzos - getElevationAdjustedSunrise()) / 2;
		return getTimeOffset(chatzos, -diff);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos19Point8Degrees() 19.8&deg;} before {@link #getSunrise() sunrise}. This time
	 * is 4 {@link #getShaahZmanis19Point8Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link
	 * #getAlos19Point8Degrees() dawn} based on the opinion of the MGA that the day is calculated from dawn to
	 * nightfall with both being 19.8&deg; below sunrise or sunset. This returns the time of 4 * {@link
	 * #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis19Point8Degrees()
	 * @see #getAlos19Point8Degrees()
	 */
	public Long getSofZmanTfilaMGA19Point8Degrees() {
		return getSofZmanTfila(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos16Point1Degrees() 16.1&deg;} before {@link #getSunrise() sunrise}. This time
	 * is 4 {@link #getShaahZmanis16Point1Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link
	 * #getAlos16Point1Degrees() dawn} based on the opinion of the MGA that the day is calculated from dawn to
	 * nightfall with both being 16.1&deg; below sunrise or sunset. This returns the time of 4 * {@link
	 * #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Long getSofZmanTfilaMGA16Point1Degrees() {
		return getSofZmanTfila(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos18Degrees() 18&deg;} before {@link #getSunrise() sunrise}. This time is 4
	 * {@link #getShaahZmanis18Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos18Degrees() dawn}
	 * based on the opinion of the MGA that the day is calculated from dawn to nightfall with both being 18&deg;
	 * below sunrise or sunset. This returns the time of 4 * {@link #getShaahZmanis18Degrees()} after
	 * {@link #getAlos18Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis18Degrees()
	 * @see #getAlos18Degrees()
	 */
	public Long getSofZmanTfilaMGA18Degrees() {
		return getSofZmanTfila(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}. This time is 4
	 * {@link #getShaahZmanis72Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos72() dawn} based on
	 * the opinion of the MGA that the day is calculated from a {@link #getAlos72() dawn} of 72 minutes before
	 * sunrise to {@link #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() dawn}. This class returns an identical time to
	 * {@link #getSofZmanTfilaMGA()} and is repeated here for clarity.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72Minutes()
	 * @see #getAlos72()
	 * @see #getSofZmanShmaMGA()
	 */
	public Long getSofZmanTfilaMGA72Minutes() {
		return getSofZmanTfilaMGA();
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos72Zmanis() 72} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This time is 4
	 * {@link #getShaahZmanis72MinutesZmanis() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos72Zmanis() dawn}
	 * based on the opinion of the MGA that the day is calculated from a {@link #getAlos72Zmanis() dawn} of 72
	 * minutes <em>zmaniyos</em> before sunrise to {@link #getTzais72Zmanis() nightfall} of 72 minutes <em>zmaniyos</em>
	 * after sunset. This returns the time of 4 * {@link #getShaahZmanis72MinutesZmanis()} after {@link #getAlos72Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis72MinutesZmanis()
	 * @see #getAlos72Zmanis()
	 */
	public Long getSofZmanTfilaMGA72MinutesZmanis() {
		return getSofZmanTfila(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos90() 90} minutes before {@link #getSunrise() sunrise}. This time is 4
	 * {@link #getShaahZmanis90Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos90() dawn} based on
	 * the opinion of the MGA that the day is calculated from a {@link #getAlos90() dawn} of 90 minutes before sunrise to
	 * {@link #getTzais90() nightfall} of 90 minutes after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis90Minutes()} after {@link #getAlos90() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90Minutes()
	 * @see #getAlos90()
	 */
	public Long getSofZmanTfilaMGA90Minutes() {
		return getSofZmanTfila(getAlos90(), getTzais90());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos90Zmanis() 90} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This time is
	 * 4 {@link #getShaahZmanis90MinutesZmanis() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos90Zmanis()
	 * dawn} based on the opinion of the MGA that the day is calculated from a {@link #getAlos90Zmanis() dawn}
	 * of 90 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais90Zmanis() nightfall} of 90 minutes
	 * <em>zmaniyos</em> after sunset. This returns the time of 4 * {@link #getShaahZmanis90MinutesZmanis()} after
	 * {@link #getAlos90Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Long getSofZmanTfilaMGA90MinutesZmanis() {
		return getSofZmanTfila(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos96() 96} minutes before {@link #getSunrise() sunrise}. This time is 4
	 * {@link #getShaahZmanis96Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos96() dawn} based on
	 * the opinion of the MGA that the day is calculated from a {@link #getAlos96() dawn} of 96 minutes before
	 * sunrise to {@link #getTzais96() nightfall} of 96 minutes after sunset. This returns the time of 4 *
	 * {@link #getShaahZmanis96Minutes()} after {@link #getAlos96() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 * @see #getAlos96()
	 */
	public Long getSofZmanTfilaMGA96Minutes() {
		return getSofZmanTfila(getAlos96(), getTzais96());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to the morning prayers) according to the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos96Zmanis() 96} minutes <em>zmaniyos</em> before {@link #getSunrise() sunrise}. This time is
	 * 4 {@link #getShaahZmanis96MinutesZmanis() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos96Zmanis()
	 * dawn} based on the opinion of the MGA that the day is calculated from a {@link #getAlos96Zmanis() dawn}
	 * of 96 minutes <em>zmaniyos</em> before sunrise to {@link #getTzais96Zmanis() nightfall} of 96 minutes
	 * <em>zmaniyos</em> after sunset. This returns the time of 4 * {@link #getShaahZmanis96MinutesZmanis()} after
	 * {@link #getAlos96Zmanis() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis90MinutesZmanis()
	 * @see #getAlos90Zmanis()
	 */
	public Long getSofZmanTfilaMGA96MinutesZmanis() {
		return getSofZmanTfila(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on
	 * <em>alos</em> being {@link #getAlos120() 120} minutes before {@link #getSunrise() sunrise} . This time is 4
	 * {@link #getShaahZmanis120Minutes() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos120() dawn}
	 * based on the opinion of the MGA that the day is calculated from a {@link #getAlos120() dawn} of 120
	 * minutes before sunrise to {@link #getTzais120() nightfall} of 120 minutes after sunset. This returns the time of
	 * 4 * {@link #getShaahZmanis120Minutes()} after {@link #getAlos120() dawn}. This is an extremely early <em>zman</em>
	 * that is very much a <em>chumra</em>.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis120Minutes()
	 * @see #getAlos120()
	 */
	public Long getSofZmanTfilaMGA120Minutes() {
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
	public Long getSofZmanTfila2HoursBeforeChatzos() {
		return getTimeOffset(getChatzos(), -120 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>mincha gedola</em> calculated as 30 minutes after {@link #getChatzos() <em>chatzos</em>}
	 * and not 1/2 of a {@link #getShaahZmanisGra() <em>shaah zmanis</em>} after {@link #getChatzos() <em>chatzos</em>} as
	 * calculated by {@link #getMinchaGedola}. Some use this time to delay the start of <em>mincha</em> in the winter when
	 * 1/2 of a {@link #getShaahZmanisGra() <em>shaah zmanis</em>} is less than 30 minutes. See
	 * {@link #getMinchaGedolaGreaterThan30()} for a convenience method that returns the later of the 2 calculations. One
	 * should not use this time to start <em>mincha</em> before the standard {@link #getMinchaGedola() <em>mincha gedola</em>}.
	 * See Shulchan Aruch <a href="https://hebrewbooks.org/pdfpager.aspx?req=49624&st=&pgnum=291">Orach Chayim 234:1</a> and
	 * the Shaar Hatziyon <em>seif katan ches</em>.
	 * 
	 * @return the <code>Date</code> of 30 minutes after <em>chatzos</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getMinchaGedola()
	 * @see #getMinchaGedolaGreaterThan30()
	 */
	public Long getMinchaGedola30Minutes() {
		return getTimeOffset(getChatzos(), MINUTE_MILLIS * 30);
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> according to the Magen Avraham with the day starting 72
	 * minutes before sunrise and ending 72 minutes after sunset. This is the earliest time to pray <em>mincha</em>. For
	 * more information on this see the documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}. This is
	 * calculated as 6.5 {@link #getTemporalHour() solar hours} after <em>alos</em>. The calculation used is 6.5 *
	 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() <em>alos</em>}.
	 * 
	 * @see #getAlos72()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @see ZmanimCalendar#getMinchaGedola()
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaGedola72Minutes() {
		return getMinchaGedola(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> according to the Magen Avraham with the day starting and
	 * ending 16.1&deg; below the horizon. This is the earliest time to pray <em>mincha</em>. For more information on
	 * this see the documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}. This is calculated as 6.5
	 * {@link #getTemporalHour() solar hours} after <em>alos</em>. The calculation used is 6.5 *
	 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() <em>alos</em>}.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun  may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getMinchaGedola16Point1Degrees() {
		return getMinchaGedola(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This is a convenience method that returns the later of {@link #getMinchaGedola()} and
	 * {@link #getMinchaGedola30Minutes()}. In the winter when 1/2 of a {@link #getShaahZmanisGra() <em>shaah zmanis</em>} is
	 * less than 30 minutes {@link #getMinchaGedola30Minutes()} will be returned, otherwise {@link #getMinchaGedola()}
	 * will be returned.
	 * 
	 * @return the <code>Date</code> of the later of {@link #getMinchaGedola()} and {@link #getMinchaGedola30Minutes()}.
	 *         If the calculation can't be computed such as in the Arctic Circle where there is at least one day a year
	 *         where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getMinchaGedolaGreaterThan30() {
		if (getMinchaGedola30Minutes() == null || getMinchaGedola() == null) {
			return null;
		} else {
			return getMinchaGedola30Minutes().compareTo(getMinchaGedola()) > 0 ? getMinchaGedola30Minutes()
					: getMinchaGedola();
		}
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> according to the Magen Avraham with the day starting and
	 * ending 16.1&deg; below the horizon. This is the preferred earliest time to pray <em>mincha</em> according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> and others. For more information on
	 * this see the documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}. This is calculated as 9.5
	 * {@link #getTemporalHour() solar hours} after <em>alos</em>. The calculation used is 9.5 *
	 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() <em>alos</em>}.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of <em>mincha ketana</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getMinchaKetana16Point1Degrees() {
		return getMinchaKetana(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> according to the Magen Avraham with the day
	 * starting 72 minutes before sunrise and ending 72 minutes after sunset. This is the preferred earliest time to pray
	 * <em>mincha</em> according to the opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a>
	 * and others. For more information on this see the documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}.
	 * This is calculated as 9.5 {@link #getShaahZmanis72Minutes()} after <em>alos</em>. The calculation used is 9.5 *
	 * {@link #getShaahZmanis72Minutes()} after {@link #getAlos72() <em>alos</em>}.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of <em>mincha ketana</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaKetana72Minutes() {
		return getMinchaKetana(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> according to the Magen Avraham with the day starting 60
	 * minutes before sunrise and ending 60 minutes after sunset. This is calculated as 10.75 hours after
	 * {@link #getAlos60() dawn}. The formula used is 10.75 {@link #getShaahZmanis60Minutes()} after {@link #getAlos60()}.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis60Minutes()
	 * @see #getAlos60()
	 * @see #getTzais60()
	 */
	public Long getPlagHamincha60Minutes() {
		return getPlagHamincha(getAlos60(), getTzais60());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> according to the
	 * Magen Avraham with the day starting 72 minutes before sunrise and ending 72 minutes after sunset. This is calculated
	 * as 10.75 hours after {@link #getAlos72() dawn}. The formula used is 10.75 {@link #getShaahZmanis72Minutes()} after
	 * {@link #getAlos72()}. Since <em>plag</em> by this calculation can occur after sunset, it should only be used
	 * <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis72Minutes()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha72Minutes() {
		return getPlagHamincha(getAlos72(), getTzais72());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> according to the
	 * Magen Avraham with the day starting 90 minutes before sunrise and ending 90 minutes after sunset. This is calculated
	 * as 10.75 hours after {@link #getAlos90() dawn}. The formula used is 10.75 {@link #getShaahZmanis90Minutes()} after
	 * {@link #getAlos90()}. Since <em>plag</em> by this calculation can occur after sunset, it should only be used
	 * <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis90Minutes()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha90Minutes() {
		return getPlagHamincha(getAlos90(), getTzais90());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> according to the Magen
	 * Avraham with the day starting 96 minutes before sunrise and ending 96 minutes after sunset. This is calculated as 10.75
	 * hours after {@link #getAlos96() dawn}. The formula used is 10.75 {@link #getShaahZmanis96Minutes()} after
	 * {@link #getAlos96()}. Since <em>plag</em> by this calculation can occur after sunset, it should only be used
	 * <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanis96Minutes()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha96Minutes() {
		return getPlagHamincha(getAlos96(), getTzais96());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em>. This is calculated
	 * as 10.75 hours after {@link #getAlos96Zmanis() dawn}. The formula used is 10.75 * {@link
	 * #getShaahZmanis96MinutesZmanis()} after {@link #getAlos96Zmanis() dawn}. Since <em>plag</em> by this calculation can
	 * occur after sunset, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha96MinutesZmanis() {
		return getPlagHamincha(getAlos96Zmanis(), getTzais96Zmanis());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em>. This is calculated
	 * as 10.75 hours after {@link #getAlos90Zmanis() dawn}. The formula used is 10.75 * {@link
	 * #getShaahZmanis90MinutesZmanis()} after {@link #getAlos90Zmanis() dawn}. Since <em>plag</em> by this calculation can
	 * occur after sunset, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha90MinutesZmanis() {
		return getPlagHamincha(getAlos90Zmanis(), getTzais90Zmanis());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em>. This is calculated as
	 * 10.75 hours after {@link #getAlos72Zmanis()}. The formula used is 10.75 * {@link #getShaahZmanis72MinutesZmanis()} after
	 * {@link #getAlos72Zmanis() dawn}. Since <em>plag</em> by this calculation can occur after sunset, it should only be used
	 * <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha72MinutesZmanis() {
		return getPlagHamincha(getAlos72Zmanis(), getTzais72Zmanis());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on the
	 * opinion that the day starts at {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} and ends at {@link
	 * #getTzais16Point1Degrees() <em>tzais</em> 16.1&deg;}. This is calculated as 10.75 hours <em>zmaniyos</em>
	 * after {@link #getAlos16Point1Degrees() dawn}. The formula used is 10.75 * {@link #getShaahZmanis16Point1Degrees()}
	 * after {@link #getAlos16Point1Degrees()}. Since <em>plag</em> by this calculation can occur after sunset, it
	 * should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha16Point1Degrees() {
		return getPlagHamincha(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on the
	 * opinion that the day starts at {@link #getAlos19Point8Degrees() <em>alos</em> 19.8&deg;} and ends at {@link
	 * #getTzais19Point8Degrees() <em>tzais</em> 19.8&deg;}. This is calculated as 10.75 hours <em>zmaniyos</em>
	 * after {@link #getAlos19Point8Degrees() dawn}. The formula used is 10.75 * {@link
	 * #getShaahZmanis19Point8Degrees()} after {@link #getAlos19Point8Degrees()}. Since <em>plag</em> by this
	 * calculation can occur after sunset, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis19Point8Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha19Point8Degrees() {
		return getPlagHamincha(getAlos19Point8Degrees(), getTzais19Point8Degrees());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on the
	 * opinion that the day starts at {@link #getAlos26Degrees() <em>alos</em> 26&deg;} and ends at {@link
	 * #getTzais26Degrees() <em>tzais</em> 26&deg;}. This is calculated as 10.75 hours <em>zmaniyos</em> after {@link
	 * #getAlos26Degrees() dawn}. The formula used is 10.75 * {@link #getShaahZmanis26Degrees()} after {@link
	 * #getAlos26Degrees()}. Since the <em>zman</em> based on an extremely early <em>alos</em> and a very late
	 * <em>tzais</em>, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis26Degrees()
	 * @see #getPlagHamincha120Minutes()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha26Degrees() {
		return getPlagHamincha(getAlos26Degrees(), getTzais26Degrees());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on the
	 * opinion that the day starts at {@link #getAlos18Degrees() <em>alos</em> 18&deg;} and ends at {@link
	 * #getTzais18Degrees() <em>tzais</em> 18&deg;}. This is calculated as 10.75 hours <em>zmaniyos</em> after {@link
	 * #getAlos18Degrees() dawn}. The formula used is 10.75 * {@link #getShaahZmanis18Degrees()} after {@link
	 * #getAlos18Degrees()}. Since <em>plag</em> by this calculation can occur after sunset, it should only be used
	 * <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis18Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagHamincha18Degrees() {
		return getPlagHamincha(getAlos18Degrees(), getTzais18Degrees());
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns the time of <em>plag hamincha</em> based on the opinion
	 * that the day starts at {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} and ends at {@link #getSunset() sunset}.
	 * 10.75 <em>shaos zmaniyos</em> are calculated based on this day and added to {@link #getAlos16Point1Degrees()
	 * <em>alos</em>} to reach this time. This time is 10.75 <em>shaos zmaniyos</em> (temporal hours) after {@link
	 * #getAlos16Point1Degrees() dawn} based on the opinion that the day is calculated from a {@link #getAlos16Point1Degrees()
	 * dawn} of 16.1 degrees before sunrise to {@link #getSeaLevelSunset() sea level sunset}. This returns the time of 10.75 *
	 * the calculated <em>shaah zmanis</em> after {@link #getAlos16Point1Degrees() dawn}. Since <em>plag</em> by this
	 * calculation can occur after sunset, it should only be used <em>lechumra</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> of the <em>plag</em>. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos16Point1Degrees()
	 * @see #getSeaLevelSunset()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getPlagAlosToSunset() {
		return getPlagHamincha(getAlos16Point1Degrees(), getElevationAdjustedSunset());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
	 * {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;} and ends at {@link #getTzaisGeonim7Point083Degrees()
	 * <em>tzais</em>}. 10.75 <em>shaos zmaniyos</em> are calculated based on this day and added to {@link
	 * #getAlos16Point1Degrees() <em>alos</em>} to reach this time. This time is 10.75 <em>shaos zmaniyos</em> (temporal
	 * hours) after {@link #getAlos16Point1Degrees() dawn} based on the opinion that the day is calculated from a
	 * {@link #getAlos16Point1Degrees() dawn} of 16.1 degrees before sunrise to
	 * {@link #getTzaisGeonim7Point083Degrees() <em>tzais</em>} . This returns the time of 10.75 * the calculated
	 * <em>shaah zmanis</em> after {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the <em>plag</em>. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not
	 *         reach low enough below the horizon for this calculation, a null will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos16Point1Degrees()
	 * @see #getTzaisGeonim7Point083Degrees()
	 */
	public Long getPlagAlos16Point1ToTzaisGeonim7Point083Degrees() {
		return getPlagHamincha(getAlos16Point1Degrees(), getTzaisGeonim7Point083Degrees());
	}

	/**
	 * Method to return the beginning of <em>bain hashmashos</em> of Rabbeinu Tam calculated when the sun is
	 * {@link #ZENITH_13_POINT_24 13.24&deg;} below the western {@link #GEOMETRIC_ZENITH geometric horizon} (90&deg;)
	 * after sunset. This calculation is based on the same calculation of {@link #getBainHasmashosRT58Point5Minutes()
	 * <em>bain hashmashos</em> Rabbeinu Tam 58.5 minutes} but uses a degree-based calculation instead of 58.5 exact
	 * minutes. This calculation is based on the position of the sun 58.5 minutes after sunset in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * which calculates to 13.24&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * NOTE: As per Yisrael Vehazmanim Vol. III page 1028, No. 50, a dip of slightly less than 13&deg; should be used.
	 * Calculations show that the proper dip to be 13.2456&deg; (truncated to 13.24 that provides about 1.5 second
	 * earlier (<em>lechumra</em>) time) below the horizon at that time. This makes a difference of 1 minute and 10
	 * seconds in Jerusalem during the Equinox, and 1 minute 29 seconds during the solstice as compared to the proper
	 * 13.24&deg; versus 13&deg;. For NY during the solstice, the difference is 1 minute 56 seconds.
	 * @todo recalculate the above based on equilux/equinox calculations.
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
	public Long getBainHasmashosRT13Point24Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_13_POINT_24);
	}

	/**
	 * This method returns the beginning of <em>Bain hashmashos</em> of Rabbeinu Tam calculated as a 58.5
	 * minute offset after sunset. <em>bain hashmashos</em> is 3/4 of a <em>Mil</em> before <em>tzais</em> or 3 1/4
	 * <em>Mil</em> after sunset. With a <em>Mil</em> calculated as 18 minutes, 3.25 * 18 = 58.5 minutes.
	 * 
	 * @return the <code>Date</code> of 58.5 minutes after sunset. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * 
	 */
	public Long getBainHasmashosRT58Point5Minutes() {
		return getTimeOffset(getElevationAdjustedSunset(), 58.5 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the beginning of <em>bain hashmashos</em> based on the calculation of 13.5 minutes (3/4 of an
	 * 18-minute <em>Mil</em>) before <em>shkiah</em> calculated as {@link #getTzaisGeonim7Point083Degrees() 7.083&deg;}.
	 * 
	 * @return the <code>Date</code> of the <em>bain hashmashos</em> of Rabbeinu Tam in this calculation. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getTzaisGeonim7Point083Degrees()
	 */
	public Long getBainHasmashosRT13Point5MinutesBefore7Point083Degrees() {
		return getTimeOffset(getSunsetOffsetByDegrees(ZENITH_7_POINT_083), -13.5 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the beginning of <em>bain hashmashos</em> of Rabbeinu Tam calculated according to the
	 * opinion of the <em>Divrei Yosef</em> (see Yisrael Vehazmanim) calculated 5/18th (27.77%) of the time between
	 * <em>alos</em> (calculated as 19.8&deg; before sunrise) and sunrise. This is added to sunset to arrive at the time
	 * for <em>bain hashmashos</em> of Rabbeinu Tam.
	 * 
	 * @return the <code>Date</code> of <em>bain hashmashos</em> of Rabbeinu Tam for this calculation. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getBainHasmashosRT2Stars() {
		Long alos19Point8 = getAlos19Point8Degrees();
		Long sunrise = getElevationAdjustedSunrise();
		if (alos19Point8 == null || sunrise == null) {
			return null;
		}

		return getTimeOffset(getElevationAdjustedSunset(), (sunrise - alos19Point8) * (5 / 18d));
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> (twilight) according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as 18 minutes
	 * or 3/4 of a 24-minute <em>Mil</em> before sunset. According to the Yereim, <em>bain hashmashos</em> starts 3/4
	 * of a <em>Mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset. 
	 * 
	 * @return the <code>Date</code> of 18 minutes before sunset. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getBainHasmashosYereim3Point05Degrees()
	 */
	public Long getBainHasmashosYereim18Minutes() {
		return getTimeOffset(getElevationAdjustedSunset(), -18 * MINUTE_MILLIS);
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> (twilight) according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as the sun's
	 * position 3.05&deg; above the horizon <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * its position 18 minutes or 3/4 of an 24-minute <em>mil</em> before sunset. According to the Yereim, <em>bain
	 * hashmashos</em> starts 3/4 of a <em>Mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset.
	 * Note that <em>lechumra</em> (of about 14 seconds) a refraction value of 0.5166&deg; as opposed to the traditional
	 * 0.566&deg; is used. This is more inline with the actual refraction in <em>Eretz Yisrael</em> and is brought down
	 * by <a href=
	 * "http://beinenu.com/rabbis/%D7%94%D7%A8%D7%91-%D7%99%D7%93%D7%99%D7%93%D7%99%D7%94-%D7%9E%D7%A0%D7%AA">Rabbi
	 * Yedidya Manet</a> in his <a href="https://www.nli.org.il/en/books/NNL_ALEPH002542826/NLI">Zmanei Halacha
	 * Lema’aseh</a> (p. 11). That is the first source that I am aware of that calculates degree-based Yereim
	 * <em>zmanim</em>. The 0.5166&deg; refraction is also used by the <a href="https://zmanim.online/">Luach Itim
	 * Lebinah</a>. Calculating the Yereim's <em>bain hashmashos</em> using 18-minute based degrees is also suggested
	 * in the upcoming 8th edition of the zmanim Kehilchasam. For more details, see the article <a href=
	 * "https://kosherjava.com/2020/12/07/the-yereims-bein-hashmashos/">The Yereim’s <em>Bein Hashmashos</em></a>.
	 * 
	 * @todo recalculate based on equinox/equilux
	 * @return the <code>Date</code> of the sun's position 3.05&deg; minutes before sunset. If the calculation can't
	 *         be computed such as in the Arctic Circle where there is at least one day a year where the sun does not
	 *         rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_MINUS_3_POINT_05
	 * @see #getBainHasmashosYereim18Minutes()
	 * @see #getBainHasmashosYereim2Point8Degrees()
	 * @see #getBainHasmashosYereim2Point1Degrees()
	 */
	public Long getBainHasmashosYereim3Point05Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_MINUS_3_POINT_05);
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> (twilight) according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as 16.875
	 * minutes or 3/4 of a 22.5-minute <em>Mil</em> before sunset. According to the Yereim, <em>bain hashmashos</em>
	 * starts 3/4 of a <em>Mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset. 
	 * 
	 * @return the <code>Date</code> of 16.875 minutes before sunset. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * 
	 * @see #getBainHasmashosYereim2Point8Degrees()
	 */
	public Long getBainHasmashosYereim16Point875Minutes() {
		return getTimeOffset(getElevationAdjustedSunset(), -16.875 * MINUTE_MILLIS);
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> (twilight) according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as the sun's
	 * position 2.8&deg; above the horizon <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>,
	 * its position 16.875 minutes or 3/4 of an 18-minute <em>Mil</em> before sunset. According to the Yereim, <em>bain
	 * hashmashos</em> starts 3/4 of a <em>Mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset.
	 * Details, including how the degrees were calculated can be seen in the documentation of
	 * {@link #getBainHasmashosYereim3Point05Degrees()}.
	 * 
	 * @return the <code>Date</code> of the sun's position 2.8&deg; minutes before sunset. If the calculation can't
	 *         be computed such as in the Arctic Circle where there is at least one day a year where the sun does not
	 *         rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_MINUS_2_POINT_8
	 * @see #getBainHasmashosYereim16Point875Minutes()
	 * @see #getBainHasmashosYereim3Point05Degrees()
	 * @see #getBainHasmashosYereim2Point1Degrees()
	 */
	public Long getBainHasmashosYereim2Point8Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_MINUS_2_POINT_8);
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> (twilight) according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as 13.5 minutes
	 * or 3/4 of an 18-minute <em>Mil</em> before sunset. According to the Yereim, <em>bain hashmashos</em> starts 3/4 of
	 * a <em>Mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset.
	 * 
	 * @return the <code>Date</code> of 13.5 minutes before sunset. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * 
	 * @see #getBainHasmashosYereim2Point1Degrees()
	 */
	public Long getBainHasmashosYereim13Point5Minutes() {
		return getTimeOffset(getElevationAdjustedSunset(), -13.5 * MINUTE_MILLIS);
	}
	
	/**
	 * This method returns the beginning of <em>bain hashmashos</em> according to the <a href=
	 * "https://en.wikipedia.org/wiki/Eliezer_ben_Samuel">Yereim (Rabbi Eliezer of Metz)</a> calculated as the sun's
	 * position 2.1&deg; above the horizon <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> in
	 * Yerushalayim, its position 13.5 minutes or 3/4 of an 18-minute <em>Mil</em> before sunset. According to the Yereim,
	 * <em>bain hashmashos</em> starts 3/4 of a <em>mil</em> before sunset and <em>tzais</em> or nightfall starts at sunset.
	 * Details, including how the degrees were calculated can be seen in the documentation of
	 * {@link #getBainHasmashosYereim3Point05Degrees()}.
	 * 
	 * @return the <code>Date</code> of the sun's position 2.1&deg; minutes before sunset. If the calculation can't
	 *         be computed such as in the Arctic Circle where there is at least one day a year where the sun does not
	 *         rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #ZENITH_MINUS_2_POINT_1
	 * @see #getBainHasmashosYereim13Point5Minutes()
	 * @see #getBainHasmashosYereim2Point8Degrees()
	 * @see #getBainHasmashosYereim3Point05Degrees()
	 */
	public Long getBainHasmashosYereim2Point1Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_MINUS_2_POINT_1);
	}
	
	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated at the
	 * sun's position at {@link #ZENITH_3_POINT_7 3.7&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.7&deg; below sea level.
	 * @see #ZENITH_3_POINT_7
	 */
	public Long getTzaisGeonim3Point7Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_7);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated at the
	 * sun's position at {@link #ZENITH_3_POINT_8 3.8&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.8&deg; below sea level.
	 * @see #ZENITH_3_POINT_8
	 */
	public Long getTzaisGeonim3Point8Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_8);
	}

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
	public Long getTzaisGeonim5Point95Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_5_POINT_95);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on an 18
	 * minute Mil, or 13.5 minutes. It is the sun's position at {@link #ZENITH_3_POINT_65 3.65&deg;} below the western
	 * horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 3.65&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_3_POINT_65
	 */
	public Long getTzaisGeonim3Point65Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_65);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on an 18
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
	public Long getTzaisGeonim3Point676Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_3_POINT_676);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <em><a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >mil</a></em> based
	 * on a 24-minute Mil, or 18 minutes. It is the sun's position at {@link #ZENITH_4_POINT_61 4.61&deg;} below the
	 * western horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.61&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_61
	 */
	public Long getTzaisGeonim4Point61Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_61);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a>, based on a 22.5
	 * minute Mil, or 16 7/8 minutes. It is the sun's position at {@link #ZENITH_4_POINT_37 4.37&deg;} below the western
	 * horizon. This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.37&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_37
	 */
	public Long getTzaisGeonim4Point37Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_37);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a 24-minute <em><a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a></em>,
	 * based on a <em>Mil</em> being 24 minutes, and is calculated as 18 + 2 + 4 for a total of 24 minutes. It is the
	 * sun's position at {@link #ZENITH_5_POINT_88 5.88&deg;} below the western horizon. This is a very early
	 * <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * 
	 * @todo Additional detailed documentation needed.
	 * @return the <code>Date</code> representing the time when the sun is 5.88&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_5_POINT_88
	 */
	public Long getTzaisGeonim5Point88Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_5_POINT_88);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 3/4
	 * of a <a href= "https://en.wikipedia.org/wiki/Biblical_and_Talmudic_units_of_measurement" >Mil</a> based on the
	 * sun's position at {@link #ZENITH_4_POINT_8 4.8&deg;} below the western horizon. This is based on Rabbi Leo Levi's
	 * calculations. This is the This is a very early <em>zman</em> and should not be relied on without Rabbinical guidance.
	 * @todo Additional documentation needed.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 4.8&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_4_POINT_8
	 */
	public Long getTzaisGeonim4Point8Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_4_POINT_8);
	}
	
	
	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> as calculated by
	 * <a href="https://en.wikipedia.org/wiki/Yechiel_Michel_Tucazinsky">Rabbi Yechiel Michel Tucazinsky</a>. It is
	 * based on of the position of the sun no later than {@link #getTzaisGeonim6Point45Degrees() 31 minutes} after sunset
	 * in Jerusalem the height of the summer solstice and is 28 minutes after <em>shkiah</em> <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>. This
	 * computes to 6.45&deg; below the western horizon.
	 * @todo Additional documentation details needed.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 6.45&deg; below sea level. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #ZENITH_6_POINT_45
	 */
	public Long getTzaisGeonim6Point45Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_6_POINT_45);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated when the
	 * sun's position {@link #ZENITH_7_POINT_083 7.083&deg; (or 7&deg; 5\u2032}) below the western horizon. This is often
	 * referred to as 7&deg;5' or 7&deg; and 5 minutes. This calculation is based on the observation of 3 medium sized
	 * stars by Dr. Baruch (Berthold) Cohn in his <em>luach</em> <a href=
	 * "https://sammlungen.ub.uni-frankfurt.de/freimann/content/titleinfo/983088">Tabellen enthaltend die Zeitangaben für
	 * den Beginn der Nacht und des Tages für die Breitengrade + 66 bis -38</a> published in Strasbourg, France in 1899.
	 * This calendar was very popular in Europe, and many other calendars based their time on it. <a href=
	 * "https://en.wikipedia.org/wiki/David_Zvi_Hoffmann">Rav Dovid Tzvi Hoffman</a> in his
	 * <a href="https://hebrewbooks.org/1053">Sh"Ut Melamed Leho'il</a> in an exchange of letters with Baruch Cohn in <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=1053&st=&pgnum=37">Orach Chaim 30</a> agreed to this <em>zman</em> (page 36),
	 * as did the Sh"Ut Bnei Tziyon and the Tenuvas Sadeh. It is very close to the time of the <a href=
	 * "https://hebrewbooks.org/22044">Mekor Chesed</a> of the Sefer chasidim. It is close to the position of the sun 30 minutes
	 * after sunset in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, but not
	 * Exactly. The actual position of the sun 30 minutes after sunset in Jerusalem at the equilux is 7.205&deg; and 7.199&deg;
	 * at the equinox. See Hazmanim Bahalacha vol 2, pages 520-521 for more details.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 7.083&deg; below sea level. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #ZENITH_7_POINT_083
	 */
	public Long getTzaisGeonim7Point083Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_7_POINT_083);
	}
	
	/**
	 * This method returns <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 45 minutes
	 * after sunset during the summer solstice in New York, when the <em>neshef</em> (twilight) is the longest. The sun's
	 * position at this time computes to {@link #ZENITH_7_POINT_67 7.75&deg;} below the western horizon. See <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=921&pgnum=149">Igros Moshe Even Haezer 4, Ch. 4</a> (regarding
	 * <em>tzais</em> for <em>krias Shema</em>). It is also mentioned in Rabbi Heber's <a href=
	 * "https://hebrewbooks.org/53000">Shaarei Zmanim</a> on in
	 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=53055&pgnum=101">chapter 10 (page 87)</a> and
	 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=53055&pgnum=122">chapter 12 (page 108)</a>. Also see the
	 * time of 45 minutes in <a href="https://en.wikipedia.org/wiki/Simcha_Bunim_Cohen">Rabbi Simcha Bunim Cohen's</a> <a
	 * href="https://www.worldcat.org/oclc/179728985">The radiance of Shabbos</a> as the earliest <em>zman</em> for New York.
	 * This <em>zman</em> is also listed in the <a href="https://hebrewbooks.org/pdfpager.aspx?req=1927&pgnum=90">Divrei
	 * Shalom Vol. III, chapter 75</a>, and <a href="https://hebrewbooks.org/pdfpager.aspx?req=892&pgnum=431">Bais Av"i
	 * Vol. III, chapter 117</a>. This <em>zman</em> is also listed in the Divrei Shalom etc. chapter 177 (FIXME - could not
	 * be located). Since this <em>zman</em> depends on the level of light, Rabbi Yaakov Shakow presented this degree-based
	 * calculation to Rabbi <a href="https://en.wikipedia.org/wiki/Shmuel_Kamenetsky">Rabbi Shmuel Kamenetsky</a> who agreed
	 * to it.
	 * @todo add hyperlinks to source of Divrei Shalom.
	 * @return the <code>Date</code> representing the time when the sun is 7.67&deg; below sea level. If the
	 *         calculation can't be computed such as northern and southern locations even south of the Arctic Circle and
	 *         north of the Antarctic Circle where the sun may not reach low enough below the horizon for this
	 *         calculation, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #ZENITH_7_POINT_67
	 */
	public Long getTzaisGeonim7Point67Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_7_POINT_67);
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
	public Long getTzaisGeonim8Point5Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_8_POINT_5);
	}
	
	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the calculations used in the <a href=
	 * "https://www.worldcat.org/oclc/243303103">Luach Itim Lebinah</a> as the stringent time for <em>tzais</em>.  It is
	 * calculated at the sun's position at {@link #ZENITH_9_POINT_3 9.3&deg;} below the western horizon.
	 * 
	 * @return the <code>Date</code> representing the time when the sun is 9.3&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getTzaisGeonim9Point3Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_9_POINT_3);
	}
	
	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Geonim</em> calculated as 60
	 * minutes after sunset <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, the
	 * day that a solar hour is 60 minutes in New York. The sun's position at this time computes to
	 * {@link #ZENITH_9_POINT_75 9.75&deg;} below the western horizon. This is the opinion of <a href=
	 * "https://en.wikipedia.org/wiki/Yosef_Eliyahu_Henkin">Rabbi Eliyahu Henkin</a>.  This also follows the opinion of
	 * <a href="https://en.wikipedia.org/wiki/Shmuel_Kamenetsky">Rabbi Shmuel Kamenetsky</a>. Rabbi Yaakov Shakow presented
	 * these degree-based times to Rabbi Shmuel Kamenetsky who agreed to them.
	 * 
	 * @todo recalculate based on equinox / equilux.
	 * @return the <code>Date</code> representing the time when the sun is 9.75&deg; below sea level. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of
	 *         the Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a
	 *         null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 *
	 * @see #getTzais60()
	 */
	public Long getTzaisGeonim9Point75Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_9_POINT_75);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <a href=
	 * "https://en.wikipedia.org/wiki/Yair_Bacharach">Chavas Yair</a> and <a href=
	 * "https://he.wikipedia.org/wiki/%D7%9E%D7%9C%D7%9B%D7%99%D7%90%D7%9C_%D7%A6%D7%91%D7%99_%D7%98%D7%A0%D7%A0%D7%91%D7%95%D7%99%D7%9D"
	 * >Divrei Malkiel</a> that the time to walk the distance of a <em>Mil</em> is 15 minutes for a total of 60 minutes
	 * for 4 <em>Mil</em> after {@link #getSeaLevelSunset() sea level sunset}. See detailed documentation explaining the
	 * 60 minute concept at {@link #getAlos60()}.
	 * 
	 * @return the <code>Date</code> representing 60 minutes after sea level sunset. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getAlos60()
	 * @see #getPlagHamincha60Minutes()
	 * @see #getShaahZmanis60Minutes()
	 */
	public Long getTzais60() {
		return getTimeOffset(getElevationAdjustedSunset(), 60 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>tzais</em> usually calculated as 40 minutes (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}) after sunset. Please note that <em>Chacham</em> Yosef Harari-Raful
	 * of Yeshivat Ateret Torah who uses this time, does so only for calculating various other <em>zmanai hayom</em>
	 * such as <em>Sof Zman Krias Shema</em> and <em>Plag Hamincha</em>. His calendars do not publish a <em>zman</em>
	 * for <em>Tzais</em>. It should also be noted that <em>Chacham</em> Harari-Raful provided a 25 minute <em>zman</em>
	 * for Israel. This API uses 40 minutes year round in any place on the globe by default. This offset can be change
	 *  by calling {@link #setAteretTorahSunsetOffset(double)}.
	 * 
	 * @return the <code>Date</code> representing 40 minutes (configurable via {@link #setAteretTorahSunsetOffset})
	 *         after sea level sunset. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public Long getTzaisAteretTorah() {
		return getTimeOffset(getElevationAdjustedSunset(), getAteretTorahSunsetOffset() * MINUTE_MILLIS);
	}

	/**
	 * Returns the offset in minutes after sunset used to calculate <em>tzais</em> based on the calculations of
	 * <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah calculations. The default value is 40 minutes.
	 * This affects most <em>zmanim</em>, since almost all zmanim use subset as part of their calculation.
	 * 
	 * @return the number of minutes after sunset for <em>Tzait</em>.
	 * @see #setAteretTorahSunsetOffset(double)
	 */
	public double getAteretTorahSunsetOffset() {
		return ateretTorahSunsetOffset;
	}

	/**
	 * Allows setting the offset in minutes after sunset for the Ateret Torah <em>zmanim</em>. The default if unset is
	 * 40 minutes. <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah uses 40 minutes globally with the exception
	 * of Israel where a 25 minute offset is used. This 40 minute (or any other) offset can be overridden by this method.
	 * This offset impacts all Ateret Torah <em>zmanim</em>.
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
	 * calculation of <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts
	 * {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is usually calculated as ending
	 * {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em> are calculated based on this day and added
	 * to {@link #getAlos72Zmanis() <em>alos</em>} to reach this time. This time is 3
	 * {@link #getShaahZmanisAteretTorah() <em>shaos zmaniyos</em>} (temporal hours) after
	 * {@link #getAlos72Zmanis() <em>alos</em> 72 <em>zmaniyos</em>}. <b>Note: </b> Based on this calculation <em>chatzos</em>
	 * will not be at midday.
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
	public Long getSofZmanShmaAteretTorah() {
		return getSofZmanShma(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) based on the calculation
	 * of <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts {@link #getAlos72Zmanis()
	 * 1/10th of the day} before sunrise and is usually calculated as ending {@link #getTzaisAteretTorah() 40 minutes
	 * after sunset} (configurable to any offset via {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em>
	 * are calculated based on this day and added to {@link #getAlos72Zmanis() <em>alos</em>} to reach this time. This time
	 * is 4 * {@link #getShaahZmanisAteretTorah() <em>shaos zmaniyos</em>} (temporal hours) after
	 * {@link #getAlos72Zmanis() <em>alos</em> 72 zmaniyos}.
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
	public Long getSofZmanTfilahAteretTorah() {
		return getSofZmanTfila(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>mincha gedola</em> based on the calculation of <em>Chacham</em> Yosef
	 * Harari-Raful of Yeshivat Ateret Torah, that the day starts {@link #getAlos72Zmanis() 1/10th of the day}
	 * before sunrise and is usually calculated as ending {@link #getTzaisAteretTorah() 40 minutes after sunset}
	 * (configurable to any offset via {@link #setAteretTorahSunsetOffset(double)}). This is the preferred earliest
	 * time to pray <em>mincha</em> according to the opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides"
	 * >Rambam</a> and others. For more information on this see the documentation on {@link #getMinchaGedola() <em>mincha
	 * gedola</em>}. This is calculated as 6.5 {@link #getShaahZmanisAteretTorah()  solar hours} after <em>alos</em>. The
	 * calculation used is 6.5 * {@link #getShaahZmanisAteretTorah()} after {@link #getAlos72Zmanis() <em>alos</em>}.
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
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaGedolaAteretTorah() {
		return getMinchaGedola(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>mincha ketana</em> based on the calculation of
	 * <em>Chacham</em> Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts
	 * {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is usually calculated as ending
	 * {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset via
	 * {@link #setAteretTorahSunsetOffset(double)}). This is the preferred earliest time to pray <em>mincha</em>
	 * according to the opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> and others.
	 * For more information on this see the documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}. This is
	 * calculated as 9.5 {@link #getShaahZmanisAteretTorah() solar hours} after {@link #getAlos72Zmanis() <em>alos</em>}.
	 * The calculation used is 9.5 * {@link #getShaahZmanisAteretTorah()} after {@link #getAlos72Zmanis() <em>alos</em>}.
	 * 
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #getAteretTorahSunsetOffset()
	 * @see #setAteretTorahSunsetOffset(double)
	 * @see #getMinchaGedola()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of <em>mincha ketana</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaKetanaAteretTorah() {
		return getMinchaKetana(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em> based on the calculation of <em>Chacham</em> Yosef Harari-Raful
	 * of Yeshivat Ateret Torah, that the day starts {@link #getAlos72Zmanis() 1/10th of the day} before sunrise and is
	 * usually calculated as ending {@link #getTzaisAteretTorah() 40 minutes after sunset} (configurable to any offset
	 * via {@link #setAteretTorahSunsetOffset(double)}). <em>shaos zmaniyos</em> are calculated based on this day and
	 * added to {@link #getAlos72Zmanis() <em>alos</em>} to reach this time. This time is 10.75
	 * {@link #getShaahZmanisAteretTorah() <em>shaos zmaniyos</em>} (temporal hours) after {@link #getAlos72Zmanis()
	 * dawn}.
	 * 
	 * @return the <code>Date</code> of the <em>plag</em>. If the calculation can't be computed such as in the Arctic Circle
	 *         where there is at least one day a year where the sun does not rise, and one where it does not set, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72Zmanis()
	 * @see #getTzaisAteretTorah()
	 * @see #getShaahZmanisAteretTorah()
	 * @see #setAteretTorahSunsetOffset(double)
	 * @see #getAteretTorahSunsetOffset()
	 */
	public Long getPlagHaminchaAteretTorah() {
		return getPlagHamincha(getAlos72Zmanis(), getTzaisAteretTorah());
	}

	/**
	 * This method returns the time of <em>misheyakir</em> based on the common calculation of the Syrian community in NY
	 * that the <em>alos</em> is a fixed minute offset from day starting {@link #getAlos72Zmanis() 1/10th of the day}
	 * before sunrise. The common offsets are 6 minutes (based on the <em>Pri Megadim</em>, but not linked to the
	 * calculation of <em>Alos</em> as 1/10th of the day), 8 and 18 minutes (possibly attributed to
	 * <a href="https://en.wikipedia.org/wiki/Baruch_Ben_Haim"><em>Chacham</em> Baruch Ben Haim</a>). Since there is no
	 * universal accepted offset, the user of this API will have to specify one. <em>Chacham</em> Yosef Harari-Raful of
	 * Yeshivat Ateret Torah does not supply any <em>zman</em> for <em>misheyakir</em> and does not endorse any specific
	 * calculation for <em>misheyakir</em>. For that reason, this method is not a public method.
	 * 
	 * @param minutes
	 *            the number of minutes after <em>alos</em> calculated as {@link #getAlos72Zmanis() 1/10th of the day}
	 * @return the <code>Date</code> of <em>misheyakir</em>. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos72Zmanis()
	 */
	// private Long getMesheyakirAteretTorah(double minutes) {
	// return getTimeOffset(getAlos72Zmanis(), minutes * MINUTE_MILLIS);
	// }

	/**
	 * Method to return <em>tzais</em> (dusk) calculated as 72 minutes zmaniyos, or 1/10th of the day after
	 * {@link #getSeaLevelSunset() sea level sunset}. This is the way that the <a href=
	 * "https://en.wikipedia.org/wiki/Abraham_Cohen_Pimentel">Minchas Cohen</a> in Ma'amar 2:4 calculates Rebbeinu Tam's
	 * time of <em>tzeis</em>. It should be noted that this calculation results in the shortest time from sunset to
	 * <em>tzais</em> being during the winter solstice, the longest at the summer solstice and 72 clock minutes at the
	 * equinox. This does not match reality, since there is no direct relationship between the length of the day and
	 * twilight. The shortest twilight is during the equinox, the longest is during the the summer solstice, and in the
	 * winter with the shortest daylight, the twilight period is longer than during the equinoxes.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos72Zmanis()
	 */
	public Long getTzais72Zmanis() {
		return getZmanisBasedOffset(1.2);
	}
	
	/**
	 * A utility method to return <em>alos</em> (dawn) or <em>tzais</em> (dusk) based on a fractional day offset. 
	 * @param hours the number of <em>shaaos zmaniyos</em> (temporal hours) before sunrise or after sunset that defines dawn
	 *        or dusk. If a negative number is passed in, it will return the time of <em>alos</em> (dawn) (subtracting the
	 *        time from sunrise) and if a positive number is passed in, it will return the time of <em>tzais</em> (dusk)
	 *        (adding the time to sunset). If 0 is passed in, a null will be returned (since we can't tell if it is sunrise
	 *        or sunset based).
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. A null will also be returned if 0 is passed in, since we can't tell if it is sunrise
	 *         or sunset based. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	private Long getZmanisBasedOffset(double hours) {
		long shaahZmanis = getShaahZmanisGra();
		if (shaahZmanis == NEVER || hours == 0) {
			return null;
		}

		if(hours > 0) {
			return getTimeOffset(getElevationAdjustedSunset(), (long) (shaahZmanis * hours));
		} else {
			return getTimeOffset(getElevationAdjustedSunrise(), (long) (shaahZmanis * hours));
		}
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated using 90 minutes zmaniyos or 1/8th of the day after {@link
	 * #getSeaLevelSunset() sea level sunset}. This time is known in Yiddish as the <em>achtel</em> (an eighth)
	 * <em>zman</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos90Zmanis()
	 */
	public Long getTzais90Zmanis() {
		return getZmanisBasedOffset(1.5);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated using 96 minutes <em>zmaniyos</em> or 1/7.5 of the day after
	 * {@link #getSeaLevelSunset() sea level sunset}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos96Zmanis()
	 */
	public Long getTzais96Zmanis() {
		return getZmanisBasedOffset(1.6);
	}

	/**
	 * Method to return <em>tzais</em> (dusk) calculated as 90 minutes after sea level sunset. This method returns
	 * <em>tzais</em> (nightfall) based on the opinion of the Magen Avraham that the time to walk the distance of a
	 * <em>Mil</em> according to the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a>'s opinion
	 * is 18 minutes for a total of 90 minutes based on the opinion of Ula who calculated <em>tzais</em> as 5
	 * <em>Mil</em> after sea level <em>shkiah</em> (sunset). A similar calculation {@link #getTzais19Point8Degrees()}
	 * uses solar position calculations based on this time.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getTzais19Point8Degrees()
	 * @see #getAlos90()
	 */
	public Long getTzais90() {
		return getTimeOffset(getElevationAdjustedSunset(), 90 * MINUTE_MILLIS);
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns <em>tzais</em> (nightfall) based on the calculations
	 * of <a href="https://en.wikipedia.org/wiki/Avraham_Chaim_Naeh">Rav Chaim Naeh</a> that the time to walk the
	 * distance of a <em>Mil</em> according to the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a>'s opinion
	 * is 2/5 of an hour (24 minutes) for a total of 120 minutes based on the opinion of <em>Ula</em> who calculated
	 * <em>tzais</em> as 5 <em>Mil</em> after sea level <em>shkiah</em> (sunset). A similar calculation {@link
	 * #getTzais26Degrees()} uses degree-based calculations based on this 120 minute calculation. Since the <em>zman</em>
	 * is extremely late and at a point that is long past the 18&deg; point where the darkest point is
	 * reached, it should only be used <em>lechumra</em>, such as delaying the start of nighttime <em>mitzvos</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}.
	 *         documentation.
	 * @see #getTzais26Degrees()
	 * @see #getAlos120()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getTzais120() {
		return getTimeOffset(getElevationAdjustedSunset(), 120 * MINUTE_MILLIS);
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns <em>tzais</em> (dusk) calculated using 120 minutes
	 * <em>zmaniyos</em> after {@link #getSeaLevelSunset() sea level sunset}. Since the <em>zman</em>
	 * is extremely late and at a point when the it is long past the 18&deg; point where the darkest point is
	 * reached, it should only be used <em>lechumra</em>, such as delaying the start of nighttime <em>mitzvos</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getAlos120Zmanis()
	 * @see #getTzais120()
	 * @see #getTzais26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getTzais120Zmanis() {
		return getZmanisBasedOffset(2.0);
	}

	/**
	 * This calculates the time of <em>tzais</em> at the point when the sun is 16.1&deg; below the horizon. This is
	 * the sun's dip below the horizon 72 minutes after sunset according Rabbeinu Tam's calculation of <em>tzais</em>
	 * <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> in
	 * Jerusalem. The question of equinox VS equilux is complex, with Rabbi Meir Posen in the <a href=
	 * "https://www.worldcat.org/oclc/956316270">Ohr Meir</a> of the opinion that the equilux should be used. See
	 * Yisrael Vehazmanim vol I, 34:1:4. Rabbi Yedidya Manet in his <a href=
	 * "https://www.nli.org.il/en/books/NNL_ALEPH002542826/NLI">Zmanei Halacha Lema'aseh</a> (4th edition part 2, pages
	 * and 22 and 24) and Rabbi Yonah Metzbuch (in a letter published by Rabbi Manet) are of the opinion that the
	 * astronomical equinox should be used. The difference adds up to about 9 seconds, too trivial to make much of a
	 * difference. For information on how this is calculated see the comments on {@link #getAlos16Point1Degrees()}.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTzais72()
	 * @see #getAlos16Point1Degrees() for more information on this calculation.
	 */
	public Long getTzais16Point1Degrees() {
		return getSunsetOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * This method should be used <em>lechumra</em> only and returns <em>tzais</em> based on when the sun is 26&deg;
	 * below the horizon.For information on how this is calculated see the comments on {@link #getAlos26Degrees()}.
	 * Since the <em>zman</em> is extremely late and at a point when it is long past the 18&deg; point where the
	 * darkest point is reached, it should only be used <em>lechumra</em> such as delaying the start of nighttime
	 * <em>mitzvos</em>.
	 * 
	 * @deprecated This method should be used <em>lechumra</em> only since it returns a very late time, and if used
	 *         <em>lekula</em> can result in <em>chillul Shabbos</em> etc. There is no current plan to remove this
	 *         method from the API, and this deprecation is intended to alert developers of the danger of using it.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as northern and
	 *         southern locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may
	 *         not reach low enough below the horizon for this calculation, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTzais120()
	 * @see #getAlos26Degrees()
	 */
	@Deprecated // (forRemoval=false) // add back once Java 9 is the minimum supported version
	public Long getTzais26Degrees() {
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
	public Long getTzais18Degrees() {
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
	public Long getTzais19Point8Degrees() {
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
	public Long getTzais96() {
		return getTimeOffset(getElevationAdjustedSunset(), 96 * MINUTE_MILLIS);
	}

	/**
	 * A method that returns the local time for fixed <em>chatzos</em>. This time is noon and midnight adjusted from
	 * standard time to account for the local latitude. The 360&deg; of the globe divided by 24 calculates to 15&deg;
	 * per hour with 4 minutes per degree, so at a longitude of 0 , 15, 30 etc... <em>Chatzos</em> is at exactly 12:00
	 * noon. This is the time of <em>chatzos</em> according to the <a href=
	 * "https://en.wikipedia.org/wiki/Aruch_HaShulchan">Aruch Hashulchan</a> in <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=7705&pgnum=426">Orach Chaim 233:14</a> and <a href=
	 * "https://en.wikipedia.org/wiki/Moshe_Feinstein">Rabbi Moshe Feinstein</a> in Igros Moshe <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=916&st=&pgnum=67">Orach Chaim 1:24</a> and <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=14675&pgnum=191">2:20</a>.
	 * Lakewood, N.J., with a longitude of -74.2094, is 0.7906 away from the closest multiple of 15 at -75&deg;. This
	 * is multiplied by 4 to yield 3 minutes and 10 seconds for a <em>chatzos</em> of 11:56:50. This method is not tied
	 * to the theoretical 15&deg; timezones, but will adjust to the actual timezone and <a
	 * href="https://en.wikipedia.org/wiki/Daylight_saving_time">Daylight saving time</a>.
	 * 
	 * @return the Date representing the local <em>chatzos</em>
	 * @see GeoLocation#getLocalMeanTimeOffset()
	 */
	public Long getFixedLocalChatzos() {
		return getTimeOffset(getDateFromTime(12.0 - getGeoLocation().getTimeZone().getRawOffset()
				/ (double) HOUR_MILLIS, true), -getGeoLocation().getLocalMeanTimeOffset());
	}

	/**
	 * A method that returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) calculated as 3
	 * clock hours before {@link #getFixedLocalChatzos()}. Note that there are opinions brought down in Yisrael Vehazmanim
	 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=9765&st=&pgnum=85">page 57</a> and Rav Yitzchak Silber's <a href=
	 * "https://www.worldcat.org/oclc/811253716">Sha'aos Shavos Balalacha</a> that this calculation is a mistake and regular
	 * <em>chatzos</em> shoud be used for clock-hour calculations as opposed to fixed local <em>chatzos</em>. According to
	 * these opinions it should be 3 clock hours before regular <em>chatzos</em> as calculated in {@link
	 * #getSofZmanShma3HoursBeforeChatzos()}.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> calculated as 3 clock hours before
	 *         {@link #getFixedLocalChatzos()}.
	 * @see #getFixedLocalChatzos()
	 * @see #getSofZmanShma3HoursBeforeChatzos()
	 * @see #getSofZmanTfilaFixedLocal()
	 *         
	 * @deprecated This method of calculating <em>sof zman Shma</em> is considered a mistaken understanding of the proper
	 *         calculation of this <em>zman</em> in the opinion of Rav Yitzchak Silber's <a href=
	 *         "https://www.worldcat.org/oclc/811253716">Sha'aos Shavos Balalacha</a>. On pages 316-318 he discusses Rav Yisrael
	 *         Harfenes's calculations and points to his seeming agreement that using fixed local <em>chatzos</em> as the focal
	 *         point is problematic. See Yisrael Vehazmanim <a href=
	 *         "https://hebrewbooks.org/pdfpager.aspx?req=9765&st=&pgnum=85">page 57</a>. While the Yisrael Vehazmanim mentions
	 *         this issue in vol. 1, it was not corrected in the calculations in vol. 3 and other parts of the <em>sefer</em>.
	 *         A competent rabbinical authority should be consulted before using this <em>zman</em>. Instead, the use of {@link
	 *         #getSofZmanShma3HoursBeforeChatzos()} should be used to calculate <em>sof zman Tfila</em> using 3 fixed clock hours.
	 *         This will likely be removed in v3.0.
	 */
	@Deprecated // (since="2.4.0", forRemoval=true) // add back once Java 9 is the minimum supported version
	public Long getSofZmanShmaFixedLocal() {
		return getTimeOffset(getFixedLocalChatzos(), -180 * MINUTE_MILLIS);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) calculated as 2 hours
	 * before {@link #getFixedLocalChatzos()}. See the documentation on {@link #getSofZmanShmaFixedLocal()} showing
	 * differing opinions on how the <em>zman</em> is calculated. According to many opinions {@link
	 * #getSofZmanTfila2HoursBeforeChatzos()} should be used as opposed to this <em>zman</em>.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>.
	 * @see #getFixedLocalChatzos()
	 * @see #getSofZmanShmaFixedLocal()
	 * @see #getSofZmanTfila2HoursBeforeChatzos()
	 *         
	 * @deprecated This method of calculating <em>sof zman Tfila</em> is considered a mistaken understanding of the proper
	 *         calculation of this <em>zman</em> in the opinion of Rav Yitzchak Silber's <a href=
	 *         "https://www.worldcat.org/oclc/811253716">Sha'aos Shavos Balalacha</a>. On pages 316-318 he discusses Rav Yisrael
	 *         Harfenes's calculations and points to his seeming agreement that using fixed local <em>chatzos</em> as the focal
	 *         point is problematic. See Yisrael Vehazmanim <a href=
	 *         "https://hebrewbooks.org/pdfpager.aspx?req=9765&st=&pgnum=85">page 57</a>. While the Yisrael Vehazmanim mentions
	 *         this issue in vol. 1, it was not corrected in the calculations in vol. 3 and other parts of the <em>sefer</em>.
	 *         A competent rabbinical authority should be consulted before using this <em>zman</em>. Instead, the use of {@link
	 *         #getSofZmanTfila2HoursBeforeChatzos()} should be used to calculate <em>sof zman Tfila</em> using 2 fixed
	 *         clock hours. This will likely be removed in v3.0.
	 */
	@Deprecated // (since="2.4.0", forRemoval=true) // add back once Java 9 is the minimum supported version
	public Long getSofZmanTfilaFixedLocal() {
		return getTimeOffset(getFixedLocalChatzos(), -120 * MINUTE_MILLIS);
	}

	/**
	 * Returns the latest time of <em>Kidush Levana</em> according to the <a
	 * href="https://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion that it is calculated as
	 * halfway between <em>molad</em> and <em>molad</em>. This adds half the 29 days, 12 hours and 793 chalakim time
	 * between <em>molad</em> and <em>molad</em> (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's <em>molad</em>.
	 * If the time of <em>sof zman Kiddush Levana</em> occurs during the day (between the <em>alos</em> and <em>tzais</em> passed in
	 * as parameters), it returns the <em>alos</em> passed in. If a null <em>alos</em> or <em>tzais</em> are passed to this method,
	 * the non-daytime adjusted time will be returned.
	 * 
	 * @param alos
	 *            the beginning of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em> and
	 *            ending at <em>tzais</em>), the time returned will be <em>alos</em>. If either the <em>alos</em> or <em>tzais</em>
	 *            parameters are null, no daytime adjustment will be made.
	 * @param tzais
	 *            the end of the Jewish day. If Kidush Levana occurs during the day (starting at <em>alos</em> and ending at
	 *            <em>tzais</em>), the time returned will be <em>alos</em>. If either the <em>alos</em> or <em>tzais</em> parameter
	 *            are null, no daytime adjustment will be made.
	 * @return the Date representing the moment halfway between molad and molad. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 * @see #getSofZmanKidushLevana15Days(Long, Long)
	 * @see JewishCalendar#getSofZmanKidushLevanaBetweenMoldos()
	 */
	public Long getSofZmanKidushLevanaBetweenMoldos(Long alos, Long tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));

		// Do not calculate for impossible dates, but account for extreme cases. In the extreme case of Rapa Iti in French
		// Polynesia on Dec 2027 when kiddush Levana 3 days can be said on <em>Rosh Chodesh</em>, the sof zman Kiddush Levana
		// will be on the 12th of the Teves. In the case of Anadyr, Russia on Jan, 2071, sof zman Kiddush Levana between the
		// moldos will occur is on the night of 17th of Shevat. See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (pages 28 and 32).
		if(jewishCalendar.getJewishDayOfMonth() < 11 || jewishCalendar.getJewishDayOfMonth() > 16) { 
			return null;
		}
		return getMoladBasedTime(jewishCalendar.getSofZmanKidushLevanaBetweenMoldos(), alos, tzais, false);
	}
	
	/**
	 * Returns the Date of the <em>molad</em> based time if it occurs on the current date. Since <em>Kiddush Levana</em>
	 * can only be said during the day, there are parameters to limit it to between <em>alos</em> and <em>tzais</em>. If
	 * the time occurs between <em>alos</em> and <em>tzais</em>, <em>tzais</em> will be returned.
	 * 
	 * @param moladBasedTime
	 *            the <em>molad</em> based time such as <em>molad</em>, <em>tchilas</em> and <em>sof zman Kiddush Levana</em>
	 * @param alos
	 *            optional start of day to limit <em>molad</em> times to the end of the night before or beginning of the next night.
	 *            Ignored if either <em>alos</em> or <em>tzais</em> are null.
	 * @param tzais
	 *            optional end of day to limit <em>molad</em> times to the end of the night before or beginning of the next night.
	 *            Ignored if either <em>tzais</em> or <em>alos</em> are null
	 * @param techila
	 *            is it the start of <em>Kiddush Levana</em> time or the end? If it is start roll it to the next <em>tzais</em>, and
	 *            and if it is the end, return the end of the previous night (<em>alos</em> passed in). Ignored if either
	 *            <em>alos</em> or <em>tzais</em> are null.
	 * @return the <em>molad</em> based time. If the <em>zman</em> does not occur during the current date, null will be returned. 
	 */
	private Long getMoladBasedTime(Long moladBasedTime, Long alos, Long tzais, boolean techila) {
		Long lastMidnight = getMidnightLastNight();
		Long midnightTonight = getMidnightTonight();
		if (((moladBasedTime >= lastMidnight) && (moladBasedTime <= midnightTonight))) {
			if(alos != null || tzais != null) {
				if(techila && ((moladBasedTime >= tzais) && (moladBasedTime <= alos))) {
					return tzais;
				} else {
					return alos;
				}
			}
			return moladBasedTime;
		}
		return null;
	}

	/**
	 * Returns the latest time of Kiddush Levana according to the <a
	 * href="https://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion that it is calculated as
	 * halfway between <em>molad</em> and <em>molad</em>. This adds half the 29 days, 12 hours and 793 chalakim time between
	 * <em>molad</em> and <em>molad</em> (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's <em>molad</em>.
	 * The <em>sof zman Kiddush Levana</em> will be returned even if it occurs during the day. To limit the time to between
	 * <em>tzais</em> and <em>alos</em>, see {@link #getSofZmanKidushLevanaBetweenMoldos(Long, Long)}.
	 * 
	 * @return the Date representing the moment halfway between molad and molad. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * @see #getSofZmanKidushLevanaBetweenMoldos(Long, Long)
	 * @see #getSofZmanKidushLevana15Days()
	 * @see JewishCalendar#getSofZmanKidushLevanaBetweenMoldos()
	 */
	public Long getSofZmanKidushLevanaBetweenMoldos() {
		return getSofZmanKidushLevanaBetweenMoldos(null, null); 
	}

	/**
	 * Returns the latest time of <em>Kiddush Levana</em> calculated as 15 days after the <em>molad</em>. This is the
	 * opinion brought down in the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that the
	 * <a href="https://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the opinion of the <a
	 * href="https://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> of calculating
	 * {@link #getSofZmanKidushLevanaBetweenMoldos(Long, Long) half way between <em>molad</em> and <em>molad</em>} is of
	 * the opinion that the Mechaber agrees to his opinion. Also see the Aruch Hashulchan. For additional details on the subject,
	 * see Rabbi Dovid Heber's very detailed write-up in <em>Siman Daled</em> (chapter 4) of <a href=
	 * "https://hebrewbooks.org/53000">Shaarei Zmanim</a>. If the time of <em>sof zman Kiddush Levana</em> occurs during
	 * the day (between the <em>alos</em> and <em>tzais</em> passed in as parameters), it returns the <em>alos</em> passed in. If a
	 * null <em>alos</em> or <em>tzais</em> are passed to this method, the non-daytime adjusted time will be returned.
	 * 
	 * @param alos
	 *            the beginning of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em> and
	 *            ending at <em>tzais</em>), the time returned will be <em>alos</em>. If either the <em>alos</em> or <em>tzais</em>
	 *            parameters are null, no daytime adjustment will be made.
	 * @param tzais
	 *            the end of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em> and ending at
	 *            <em>tzais</em>), the time returned will be <em>alos</em>. If either the <em>alos</em> or <em>tzais</em> parameters
	 *            are null, no daytime adjustment will be made.
	 *
	 * @return the Date representing the moment 15 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>alos</em> will be returned
	 * 
	 * @see #getSofZmanKidushLevanaBetweenMoldos(Long, Long)
	 * @see JewishCalendar#getSofZmanKidushLevana15Days()
	 */
	public Long getSofZmanKidushLevana15Days(Long alos, Long tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		// Do not calculate for impossible dates, but account for extreme cases. In the extreme case of Rapa Iti in
		// French Polynesia on Dec 2027 when kiddush Levana 3 days can be said on <em>Rosh Chodesh</em>, the sof zman Kiddush
		// Levana will be on the 12th of the Teves. in the case of Anadyr, Russia on Jan, 2071, sof zman kiddush levana will
		// occur after midnight on the 17th of Shevat. See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (pages 28 and 32).
		if(jewishCalendar.getJewishDayOfMonth() < 11 || jewishCalendar.getJewishDayOfMonth() > 17) {
			return null;
		}
		return getMoladBasedTime(jewishCalendar.getSofZmanKidushLevana15Days(), alos, tzais, false);
	}

	/**
	 * Returns the latest time of <em>Kiddush Levana</em> calculated as 15 days after the molad. This is the opinion of
	 * the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that the
	 * <a href="https://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the opinion of the <a
	 * href="https://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> of calculating
	 * {@link #getSofZmanKidushLevanaBetweenMoldos(Long, Long) half way between <em>molad</em> and <em>molad</em>} is of
	 * the opinion that the Mechaber agrees to his opinion. Also see the Aruch Hashulchan. For additional details on the subject,
	 * See Rabbi Dovid Heber's very detailed write-up in Siman Daled (chapter 4) of <a href="https://hebrewbooks.org/53000">Shaarei
	 * Zmanim</a>. The <em>sof zman Kiddush Levana</em> will be returned even if it occurs during the day. To limit the time to
	 * between <em>tzais</em> and <em>alos</em>, see {@link #getSofZmanKidushLevana15Days(Long, Long)}.
	 * 
	 * @return the Date representing the moment 15 days after the <em>molad</em>. If the time occurs between
	 *         <em>alos</em> and <em>tzais</em>, <em>alos</em> will be returned
	 * 
	 * @see #getSofZmanKidushLevana15Days(Long, Long)
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 * @see JewishCalendar#getSofZmanKidushLevana15Days()
	 * 
	 */
	public Long getSofZmanKidushLevana15Days() {
		return getSofZmanKidushLevana15Days(null, null);
	}
	
	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to <a href=
	 * "https://en.wikipedia.org/wiki/Yonah_Gerondi">Rabbeinu Yonah</a>'s opinion that it can be said 3 days after the
	 * <em>molad</em>. The time will be returned even if it occurs during the day when <em>Kiddush Levana</em> can't be said.
	 * Use {@link #getTchilasZmanKidushLevana3Days(Long, Long)} if you want to limit the time to night hours.
	 * 
	 * @return the Date representing the moment 3 days after the molad.
	 * @see #getTchilasZmanKidushLevana3Days(Long, Long)
	 * @see #getTchilasZmanKidushLevana7Days()
	 * @see JewishCalendar#getTchilasZmanKidushLevana3Days()
	 */
	public Long getTchilasZmanKidushLevana3Days() {
		return getTchilasZmanKidushLevana3Days(null, null);
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to <a href=
	 * "https://en.wikipedia.org/wiki/Yonah_Gerondi">Rabbeinu Yonah</a>'s opinion that it can be said 3 days after the <em>molad</em>.
	 * If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between <em>alos</em> and <em>tzais</em> passed to
	 * this method) it will return the following <em>tzais</em>. If null is passed for either <em>alos</em> or <em>tzais</em>, the actual
	 * <em>tchilas zman Kiddush Levana</em> will be returned, regardless of if it is during the day or not.
	 * 
	 * @param alos
	 *            the beginning of the Jewish day. If Kidush Levana occurs during the day (starting at <em>alos</em> and ending
	 *            at <em>tzais</em>), the time returned will be <em>tzais</em>. If either the <em>alos</em> or <em>tzais</em> parameters
	 *            are null, no daytime adjustment will be made.
	 * @param tzais
	 *            the end of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em> and ending at
	 *            <em>tzais</em>), the time returned will be <em>tzais</em>. If either the <em>alos</em> or <em>tzais</em> parameters
	 *            are null, no daytime adjustment will be made.
	 *
	 * @return the Date representing the moment 3 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana3Days()
	 * @see #getTchilasZmanKidushLevana7Days(Long, Long)
	 * @see JewishCalendar#getTchilasZmanKidushLevana3Days()
	 */
	public Long getTchilasZmanKidushLevana3Days(Long alos, Long tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		
		// Do not calculate for impossible dates, but account for extreme cases. Tchilas zman kiddush Levana 3 days for
		// the extreme case of Rapa Iti in French Polynesia on Dec 2027 when kiddush Levana 3 days can be said on the evening
		// of the 30th, the second night of Rosh Chodesh. The 3rd day after the <em>molad</em> will be on the 4th of the month.
		// In the case of Anadyr, Russia on Jan, 2071, when sof zman kiddush levana is on the 17th of the month, the 3rd day
		// from the molad will be on the 5th day of Shevat. See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (pages 28 and 32).
		if(jewishCalendar.getJewishDayOfMonth() > 5 && jewishCalendar.getJewishDayOfMonth() < 30) {
			return null;
		}
		
		Long zman = getMoladBasedTime(jewishCalendar.getTchilasZmanKidushLevana3Days(), alos, tzais, true);
		
		//Get the following month's zman kiddush Levana for the extreme case of Rapa Iti in French Polynesia on Dec 2027 when
		// kiddush Levana can be said on Rosh Chodesh (the evening of the 30th). See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (page 32)
		if(zman == null && jewishCalendar.getJewishDayOfMonth() == 30) {
			jewishCalendar.forward(Calendar.MONTH, 1);
			zman = getMoladBasedTime(jewishCalendar.getTchilasZmanKidushLevana3Days(), null, null, true);
		}
		
		return zman;
	}
	
	/**
	 * Returns the point in time of <em>Molad</em> as a <code>Date</code> Object. For the traditional day of week, hour,
	 * minute and chalakim, {@link JewishCalendar#getMoladAsDate()} and the not yet completed
	 * {@link com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter} that will have formatting for this.
	 * 
	 * @return the Date representing the moment of the molad. If the molad does not occur on this day, a null will be returned.
	 * 
	 * @see #getTchilasZmanKidushLevana3Days()
	 * @see #getTchilasZmanKidushLevana7Days(Long, Long)
	 * @see JewishCalendar#getMoladAsDate()
	 */
	public Long getZmanMolad() {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		
		// Optimize to not calculate for impossible dates, but account for extreme cases. The molad in the extreme case of Rapa
		// Iti in French Polynesia on Dec 2027 occurs on the night of the 27th of Kislev. In the case of Anadyr, Russia on
		// Jan 2071, the molad will be on the 2nd day of Shevat. See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (pages 28 and 32).
		if(jewishCalendar.getJewishDayOfMonth() > 2 && jewishCalendar.getJewishDayOfMonth() < 27) {
			return null;
		}
		Long molad = getMoladBasedTime(jewishCalendar.getMoladAsDate(), null, null, true);

		// deal with molad that happens on the end of the previous month
		if(molad == null && jewishCalendar.getJewishDayOfMonth() > 26) {
			jewishCalendar.forward(Calendar.MONTH, 1);
			molad = getMoladBasedTime(jewishCalendar.getMoladAsDate(), null, null, true);
		}
		return molad;
	}
	
	/**
	 * Used by Molad based <em>zmanim</em> to determine if <em>zmanim</em> occur during the current day.
	 * @see #getMoladBasedTime(Long, Long, Long, boolean)
	 * @return previous midnight
	 */
	private Long getMidnightLastNight() {
		Calendar midnight = (Calendar)getCalendar().clone();
		// reset hour, minutes, seconds and millis
		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.MINUTE, 0);
		midnight.set(Calendar.SECOND, 0);
		midnight.set(Calendar.MILLISECOND, 0);
		return midnight.getTimeInMillis();
	}
	
	/**
	 * Used by Molad based <em>zmanim</em> to determine if <em>zmanim</em> occur during the current day.
	 * @see #getMoladBasedTime(Long, Long, Long, boolean)
	 * @return following midnight
	 */
	private Long getMidnightTonight() {
		Calendar midnight = (Calendar)getCalendar().clone();
		midnight.add(Calendar.DAY_OF_YEAR, 1);//roll to tonight
		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.MINUTE, 0);
		midnight.set(Calendar.SECOND, 0);
		midnight.set(Calendar.MILLISECOND, 0);
		return midnight.getTimeInMillis();
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to the opinions that it should not be said until 7
	 * days after the <em>molad</em>. If the time of <em>tchilas zman Kiddush Levana</em> occurs during the day (between
	 * {@link ZmanimCalendar#getAlos72() <em>alos</em>} and {@link ZmanimCalendar#getTzais72() <em>tzais</em>}) it
	 * return the next <em>tzais</em>.
	 * 
	 * @param alos
	 *            the beginning of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em>
	 *            and ending at <em>tzais</em>), the time returned will be <em>tzais</em>. If either the <em>alos</em> or
	 *            <em>tzais</em> parameters are null, no daytime adjustment will be made.
	 * @param tzais
	 *            the end of the Jewish day. If <em>Kidush Levana</em> occurs during the day (starting at <em>alos</em> and
	 *            ending at <em>tzais</em>), the time returned will be <em>tzais</em>. If either the <em>alos</em> or
	 *            <em>tzais</em> parameters are null, no daytime adjustment will be made.
	 *
	 * @return the Date representing the moment 7 days after the molad. If the time occurs between <em>alos</em> and
	 *         <em>tzais</em>, <em>tzais</em> will be returned
	 * @see #getTchilasZmanKidushLevana3Days(Long, Long)
	 * @see #getTchilasZmanKidushLevana7Days()
	 * @see JewishCalendar#getTchilasZmanKidushLevana7Days()
	 */
	public Long getTchilasZmanKidushLevana7Days(Long alos, Long tzais) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		
		// Optimize to not calculate for impossible dates, but account for extreme cases. Tchilas zman kiddush Levana 7 days for
		// the extreme case of Rapa Iti in French Polynesia on Jan 2028 (when kiddush Levana 3 days can be said on the evening
		// of the 30th, the second night of Rosh Chodesh), the 7th day after the molad will be on the 4th of the month.
		// In the case of Anadyr, Russia on Jan, 2071, when sof zman kiddush levana is on the 17th of the month, the 7th day
		// from the molad will be on the 9th day of Shevat. See Rabbi Dovid Heber's Shaarei Zmanim chapter 4 (pages 28 and 32).
		if(jewishCalendar.getJewishDayOfMonth() < 4 || jewishCalendar.getJewishDayOfMonth() > 9) { 
			return null;
		}
		
		return getMoladBasedTime(jewishCalendar.getTchilasZmanKidushLevana7Days(), alos, tzais, true);
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> according to the opinions that it should not be said until 7
	 * days after the <em>molad</em>. The time will be returned even if it occurs during the day when <em>Kiddush Levana</em>
	 * can't be recited. Use {@link #getTchilasZmanKidushLevana7Days(Long, Long)} if you want to limit the time to night hours.
	 * 
	 * @return the Date representing the moment 7 days after the molad regardless of it is day or night.
	 * @see #getTchilasZmanKidushLevana7Days(Long, Long)
	 * @see JewishCalendar#getTchilasZmanKidushLevana7Days()
	 * @see #getTchilasZmanKidushLevana3Days()
	 */
	public Long getTchilasZmanKidushLevana7Days() {
		return getTchilasZmanKidushLevana7Days(null, null);
	}

	/**
	 * This method returns the latest time one is allowed eating <em>chametz</em> on <em>Erev Pesach</em> according to
	 * the opinion of the<a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. This time is identical to the {@link
	 * #getSofZmanTfilaGRA() <em>Sof zman tfilah</em> GRA} and is provided as a convenience method for those who are
	 * unaware how this <em>zman</em> is calculated. This time is 4 hours into the day based on the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that the day is calculated from sunrise to sunset.
	 * This returns the time 4 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see ZmanimCalendar#getShaahZmanisGra()
	 * @see ZmanimCalendar#getSofZmanTfilaGRA()
	 * @return the <code>Date</code> one is allowed eating <em>chametz</em> on <em>Erev Pesach</em>. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does
	 *         not rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getSofZmanAchilasChametzGRA() {
		return getSofZmanTfilaGRA();
	}

	/**
	 * This method returns the latest time one is allowed eating <em>chametz</em> on <em>Erev Pesach</em> according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}. This time is identical to the
	 * {@link #getSofZmanTfilaMGA72Minutes() <em>Sof zman tfilah</em> MGA 72 minutes}. This time is 4 {@link #getShaahZmanisMGA()
	 * <em>shaos zmaniyos</em>} (temporal hours) after {@link #getAlos72() dawn} based on the opinion of the MGA that the day is
	 * calculated from a {@link #getAlos72() dawn} of 72 minutes before sunrise to {@link #getTzais72() nightfall} of 72 minutes
	 * after sunset. This returns the time of 4 * {@link #getShaahZmanisMGA()} after {@link #getAlos72() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time of eating <em>chametz</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set), a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 * @see #getSofZmanTfilaMGA72Minutes()
	 */
	public Long getSofZmanAchilasChametzMGA72Minutes() {
		return getSofZmanTfilaMGA72Minutes();
	}

	/**
	 * This method returns the latest time one is allowed eating <em>chametz</em> on <em>Erev Pesach</em> according to the
	 * opinion of the<a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos16Point1Degrees() 16.1&deg;} before {@link #getSunrise() sunrise}. This time is 4 {@link
	 * #getShaahZmanis16Point1Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos16Point1Degrees() dawn}
	 * based on the opinion of the MGA that the day is calculated from dawn to nightfall with both being 16.1&deg;
	 * below sunrise or sunset. This returns the time of 4 {@link #getShaahZmanis16Point1Degrees()} after
	 * {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time of eating <em>chametz</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 * @see #getSofZmanTfilaMGA16Point1Degrees()
	 */
	public Long getSofZmanAchilasChametzMGA16Point1Degrees() {
		return getSofZmanTfilaMGA16Point1Degrees();
	}

	/**
	 * This method returns the latest time for burning <em>chametz</em> on <em>Erev Pesach</em> according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. This time is 5 hours into the day based on the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that the day is calculated from
	 * sunrise to sunset. This returns the time 5 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea
	 * level sunrise}.
	 * 
	 * @see ZmanimCalendar#getShaahZmanisGra()
	 * @return the <code>Date</code> of the latest time for burning <em>chametz</em> on <em>Erev Pesach</em>. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getSofZmanBiurChametzGRA() {
		return getTimeOffset(getElevationAdjustedSunrise(), getShaahZmanisGra() * 5);
	}

	/**
	 * This method returns the latest time for burning <em>chametz</em> on <em>Erev Pesach</em> according to the opinion of
	 * the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}. This time is 5 {@link
	 * #getShaahZmanisMGA() <em>shaos zmaniyos</em>} (temporal hours) after {@link #getAlos72() dawn} based on the opinion of
	 * the MGA that the day is calculated from a {@link #getAlos72() dawn} of 72 minutes before sunrise to {@link
	 * #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 5 * {@link #getShaahZmanisMGA()} after
	 * {@link #getAlos72() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time for burning <em>chametz</em> on <em>Erev Pesach</em>. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set), a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 */
	public Long getSofZmanBiurChametzMGA72Minutes() {
		return getTimeOffset(getAlos72(), getShaahZmanisMGA() * 5);
	}

	/**
	 * This method returns the latest time for burning <em>chametz</em> on <em>Erev Pesach</em> according to the opinion
	 * of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> based on <em>alos</em>
	 * being {@link #getAlos16Point1Degrees() 16.1&deg;} before {@link #getSunrise() sunrise}. This time is 5
	 * {@link #getShaahZmanis16Point1Degrees() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos16Point1Degrees()
	 * dawn} based on the opinion of the MGA that the day is calculated from dawn to nightfall with both being 16.1&deg;
	 * below sunrise or sunset. This returns the time of 5 {@link #getShaahZmanis16Point1Degrees()} after
	 * {@link #getAlos16Point1Degrees() dawn}.
	 * 
	 * @return the <code>Date</code> of the latest time for burning <em>chametz</em> on <em>Erev Pesach</em>. If the calculation
	 *         can't be computed such as northern and southern locations even south of the Arctic Circle and north of the
	 *         Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a null
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @see #getAlos16Point1Degrees()
	 */
	public Long getSofZmanBiurChametzMGA16Point1Degrees() {
		return getTimeOffset(getAlos16Point1Degrees(), getShaahZmanis16Point1Degrees() * 5);
	}

	/**
	 * A method that returns "solar" midnight, or the time when the sun is at its <a
	 * href="https://en.wikipedia.org/wiki/Nadir">nadir</a>.
	 * <b>Note:</b> this method is experimental and might be removed.
	 * 
	 * @return the <code>Date</code> of Solar Midnight (chatzos layla). If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getSolarMidnight() {
		ZmanimCalendar clonedCal = (ZmanimCalendar) clone();
		clonedCal.getCalendar().add(Calendar.DAY_OF_MONTH, 1);
		Long sunset = getSeaLevelSunset();
		Long sunrise = clonedCal.getSeaLevelSunrise();
		return getTimeOffset(sunset, getTemporalHour(sunset, sunrise) * 6);
	}
	
	/**
	 * A method that returns the <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>'s
	 * <em>netz amiti</em> (sunrise) without {@link AstronomicalCalculator#getElevationAdjustment(double)
	 * elevation adjustment}. This forms the base for the Baal Hatanya's dawn-based calculations that are
	 * calculated as a dip below the horizon before sunrise.
	 *
	 * According to the Baal Hatanya, <em>netz amiti</em>, or true (halachic) sunrise, is when the top of the sun's
	 * disk is visible at an elevation similar to the mountains of Eretz Yisrael. The time is calculated as the point at which
	 * the center of the sun's disk is 1.583&deg; below the horizon. This degree-based calculation can be found in Rabbi Shalom
	 * DovBer Levine's commentary on The <a href="https://www.chabadlibrary.org/books/pdf/Seder-Hachnosas-Shabbos.pdf">Baal
	 * Hatanya's Seder Hachnasas Shabbos</a>. From an elevation of 546 meters, the top of <a href=
	 * "https://en.wikipedia.org/wiki/Mount_Carmel">Har Hacarmel</a>, the sun disappears when it is 1&deg; 35' or 1.583&deg;
	 * below the sea level horizon. This in turn is based on the Gemara <a href=
	 * "https://hebrewbooks.org/shas.aspx?mesechta=2&daf=35">Shabbos 35a</a>. There are other opinions brought down by
	 * Rabbi Levine, including Rabbi Yosef Yitzchok Feigelstock who calculates it as the degrees below the horizon 4 minutes after
	 * sunset in Yerushalayim (on the equinox). That is brought down as 1.583&deg;. This is identical to the 1&deg; 35' <em>zman</em>
	 * and is probably a typo and should be 1.683&deg;. These calculations are used by most <a href=
	 * "https://en.wikipedia.org/wiki/Chabad">Chabad</a> calendars that use the Baal Hatanya's <em>zmanim</em>. See
	 * <a href="https://www.chabad.org/library/article_cdo/aid/3209349/jewish/About-Our-Zmanim-Calculations.htm">About Our
	 * <em>Zmanim</em> Calculations @ Chabad.org</a>.
	 *
	 * Note: <em>netz amiti</em> is used only for calculating certain <em>zmanim</em>, and is intentionally unpublished. For
	 * practical purposes, daytime <em>mitzvos</em> like <em>shofar</em> and <em>lulav</em> should not be done until after the
	 * published time for <em>netz</em> / sunrise.
	 * 
	 * @return the <code>Date</code> representing the exact sea-level <em>netz amiti</em> (sunrise) time. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a null will be returned. See detailed explanation on top of the page.
	 * 
	 * @see #getSunrise()
	 * @see #getSeaLevelSunrise()
	 * @see #getSunsetBaalHatanya()
	 * @see #ZENITH_1_POINT_583
	 */
	private Long getSunriseBaalHatanya() {
		return getSunriseOffsetByDegrees(ZENITH_1_POINT_583);
	}

	/**
	 * A method that returns the <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>'s
	 * <em>shkiah amiti</em> (sunset) without {@link AstronomicalCalculator#getElevationAdjustment(double)
	 * elevation adjustment}. This forms the base for the Baal Hatanya's dusk-based calculations that are calculated
	 * as a dip below the horizon after sunset.
	 * 
	 * According to the Baal Hatanya, <em>shkiah amiti</em>, true (<em>halachic</em>) sunset, is when the top of the 
	 * sun's disk disappears from view at an elevation similar to the mountains of <em>Eretz Yisrael</em>.
	 * This time is calculated as the point at which the center of the sun's disk is 1.583 degrees below the horizon.
	 *
	 * Note: <em>shkiah amiti</em> is used only for calculating certain <em>zmanim</em>, and is intentionally unpublished. For
	 * practical purposes, all daytime mitzvos should be completed before the published time for <em>shkiah</em> / sunset.
	 *
	 * For further explanation of the calculations used for the Baal Hatanya's <em>zmanim</em> in this library, see
	 * <a href="https://www.chabad.org/library/article_cdo/aid/3209349/jewish/About-Our-Zmanim-Calculations.htm">About Our
	 * <em>Zmanim</em> Calculations @ Chabad.org</a>.
	 * 
	 * @return the <code>Date</code> representing the exact sea-level <em>shkiah amiti</em> (sunset) time. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does not
	 *         rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getSunset()
	 * @see #getSeaLevelSunset()
	 * @see #getSunriseBaalHatanya()
	 * @see #ZENITH_1_POINT_583
	 */
	private Long getSunsetBaalHatanya() {
		return getSunsetOffsetByDegrees(ZENITH_1_POINT_583);
	}

	/**
	 * A method that returns the <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>'s
	 * a <em>shaah zmanis</em> ({@link #getTemporalHour(Long, Long) temporal hour}). This forms the base for the
	 * Baal Hatanya's  day  based calculations that are calculated as a 1.583&deg; dip below the horizon after sunset. 
	 * According to the Baal Hatanya, <em>shkiah amiti</em>, true (halachic) sunset, is when the top of the 
	 * sun's disk disappears from view at an elevation similar to the mountains of Eretz Yisrael.
	 * This time is calculated as the point at which the center of the sun's disk is 1.583 degrees below the horizon.
	 * A method that returns a <em>shaah zmanis</em> ({@link #getTemporalHour(Long, Long) temporal hour}) calculated 
	 * based on the <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>'s <em>netz
	 * amiti</em> and <em>shkiah amiti</em> using a dip of 1.583&deg; below the sea level horizon. This calculation divides
	 * the day based on the opinion of the Baal Hatanya that the day runs from {@link #getSunriseBaalHatanya() netz amiti}
	 * to {@link #getSunsetBaalHatanya() <em>shkiah amiti</em>}. The calculations are based on a day from {@link
	 * #getSunriseBaalHatanya() sea level <em>netz amiti</em>} to {@link #getSunsetBaalHatanya() sea level <em>shkiah amiti</em>}.
	 * The day is split into 12 equal parts with each one being a <em>shaah zmanis</em>. This method is similar to {@link
	 * #getTemporalHour}, but all calculations are based on a sea level sunrise and sunset.
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em> calculated from
	 *         {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)} to {@link #getSunsetBaalHatanya() <em>shkiah amiti</em>
	 *         ("real" sunset)}. If the calculation can't be computed such as in the Arctic Circle where there is at least one day a
	 *         year where the sun does not rise, and one where it does not set, {@link Long#MIN_VALUE} will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getTemporalHour(Long, Long)
	 * @see #getSunriseBaalHatanya()
	 * @see #getSunsetBaalHatanya()
	 * @see #ZENITH_1_POINT_583
	 */
	public long getShaahZmanisBaalHatanya() {
		return getTemporalHour(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Shneur_Zalman_of_Liadi">Baal Hatanya</a>'s <em>alos</em>
	 * (dawn) calculated as the time when the sun is 16.9&deg; below the eastern {@link #GEOMETRIC_ZENITH geometric horizon}
	 * before {@link #getSunrise sunrise}. For more information the source of 16.9&deg; see {@link #ZENITH_16_POINT_9}.
	 * 
	 * @see #ZENITH_16_POINT_9
	 * @return The <code>Date</code> of dawn. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getAlosBaalHatanya() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_9);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning). This time is 3
	 * {@link #getShaahZmanisBaalHatanya() <em>shaos zmaniyos</em>} (solar hours) after {@link #getSunriseBaalHatanya() 
	 * <em>netz amiti</em> (sunrise)} based on the opinion of the Baal Hatanya that the day is calculated from
	 * sunrise to sunset. This returns the time 3 * {@link #getShaahZmanisBaalHatanya()} after {@link #getSunriseBaalHatanya() 
	 * <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see ZmanimCalendar#getSofZmanShma(Long, Long)
	 * @see #getShaahZmanisBaalHatanya()
	 * @return the <code>Date</code> of the latest <em>zman shema</em> according to the Baal Hatanya. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does
	 *         not rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getSofZmanShmaBaalHatanya() {
		return getSofZmanShma(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * This method returns the latest <em>zman tfilah</em> (time to recite the morning prayers). This time is 4
	 * hours into the day based on the opinion of the Baal Hatanya that the day is
	 * calculated from sunrise to sunset. This returns the time 4 * {@link #getShaahZmanisBaalHatanya()} after
	 * {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see ZmanimCalendar#getSofZmanTfila(Long, Long)
	 * @see #getShaahZmanisBaalHatanya()
	 * @return the <code>Date</code> of the latest <em>zman tfilah</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getSofZmanTfilaBaalHatanya() {
		return getSofZmanTfila(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * This method returns the latest time one is allowed eating <em>chametz</em> on <em>Erev Pesach</em> according to the
	 * opinion of the Baal Hatanya. This time is identical to the {@link #getSofZmanTfilaBaalHatanya() <em>Sof zman tfilah</em>
	 * Baal Hatanya}. This time is 4 hours into the day based on the opinion of the Baal Hatanya that the day is calculated
	 * from sunrise to sunset. This returns the time 4 {@link #getShaahZmanisBaalHatanya()} after
	 * {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see #getShaahZmanisBaalHatanya()
	 * @see #getSofZmanTfilaBaalHatanya()
	 * @return the <code>Date</code> one is allowed eating <em>chametz</em> on <em>Erev Pesach</em>. If the calculation can't
	 *         be computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getSofZmanAchilasChametzBaalHatanya() {
		return getSofZmanTfilaBaalHatanya();
	}

	/**
	 * This method returns the latest time for burning <em>chametz</em> on <em>Erev Pesach</em> according to the opinion of
	 * the Baal Hatanya. This time is 5 hours into the day based on the opinion of the Baal Hatanya that the day is calculated
	 * from sunrise to sunset. This returns the time 5 * {@link #getShaahZmanisBaalHatanya()} after
	 * {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see #getShaahZmanisBaalHatanya()
	 * @return the <code>Date</code> of the latest time for burning <em>chametz</em> on <em>Erev Pesach</em>. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getSofZmanBiurChametzBaalHatanya() {
		return getTimeOffset(getSunriseBaalHatanya(), getShaahZmanisBaalHatanya() * 5);
	}

	/**
	 * This method returns the time of <em>mincha gedola</em>. <em>Mincha gedola</em> is the earliest time one can pray
	 * <em>mincha</em>. The <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> is of the opinion that it is
	 * better to delay <em>mincha</em> until {@link #getMinchaKetanaBaalHatanya() <em>mincha ketana</em>} while the
	 * <a href="https://en.wikipedia.org/wiki/Asher_ben_Jehiel">Ra"sh</a>,
	 * <a href="https://en.wikipedia.org/wiki/Jacob_ben_Asher">Tur</a>, <a href=
	 * "https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> and others are of the opinion that <em>mincha</em> can be prayed
	 * <em>lechatchila</em> starting at <em>mincha gedola</em>. This is calculated as 6.5 {@link #getShaahZmanisBaalHatanya()
	 * sea level solar hours} after {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}. This calculation is based
	 * on the opinion of the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 6.5
	 * * {@link #getShaahZmanisBaalHatanya()} after {@link #getSunriseBaalHatanya() <em>netz amiti</em> ("real" sunrise)}.
	 * 
	 * @see #getMinchaGedola(Long, Long)
	 * @see #getShaahZmanisBaalHatanya()
	 * @see #getMinchaKetanaBaalHatanya()
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaGedolaBaalHatanya() {
		return getMinchaGedola(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * This is a convenience method that returns the later of {@link #getMinchaGedolaBaalHatanya()} and
	 * {@link #getMinchaGedola30Minutes()}. In the winter when 1/2 of a {@link #getShaahZmanisBaalHatanya()
	 * <em>shaah zmanis</em>} is less than 30 minutes {@link #getMinchaGedola30Minutes()} will be returned, otherwise
	 * {@link #getMinchaGedolaBaalHatanya()} will be returned.
	 * 
	 * @return the <code>Date</code> of the later of {@link #getMinchaGedolaBaalHatanya()} and {@link #getMinchaGedola30Minutes()}.
	 *         If the calculation can't be computed such as in the Arctic Circle where there is at least one day a year
	 *         where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getMinchaGedolaBaalHatanyaGreaterThan30() {
		if (getMinchaGedola30Minutes() == null || getMinchaGedolaBaalHatanya() == null) {
			return null;
		} else {
			return getMinchaGedola30Minutes().compareTo(getMinchaGedolaBaalHatanya()) > 0 ? getMinchaGedola30Minutes()
					: getMinchaGedolaBaalHatanya();
		}
	}

	/**
	 * This method returns the time of <em>mincha ketana</em>. This is the preferred earliest time to pray
	 * <em>mincha</em> in the opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> and others.
	 * For more information on this see the documentation on {@link #getMinchaGedolaBaalHatanya() <em>mincha gedola</em>}.
	 * This is calculated as 9.5 {@link #getShaahZmanisBaalHatanya()  sea level solar hours} after {@link #getSunriseBaalHatanya()
	 * <em>netz amiti</em> (sunrise)}. This calculation is calculated based on the opinion of the Baal Hatanya that the
	 * day is calculated from sunrise to sunset. This returns the time 9.5 * {@link #getShaahZmanisBaalHatanya()} after {@link
	 * #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see #getMinchaKetana(Long, Long)
	 * @see #getShaahZmanisBaalHatanya()
	 * @see #getMinchaGedolaBaalHatanya()
	 * @return the <code>Date</code> of the time of <em>mincha ketana</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getMinchaKetanaBaalHatanya() {
		return getMinchaKetana(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * This method returns the time of <em>plag hamincha</em>. This is calculated as 10.75 hours after sunrise. This
	 * calculation is based on the opinion of the Baal Hatanya that the day is calculated
	 * from sunrise to sunset. This returns the time 10.75 * {@link #getShaahZmanisBaalHatanya()} after
	 * {@link #getSunriseBaalHatanya() <em>netz amiti</em> (sunrise)}.
	 * 
	 * @see #getPlagHamincha(Long, Long)
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Long getPlagHaminchaBaalHatanya() {
		return getPlagHamincha(getSunriseBaalHatanya(), getSunsetBaalHatanya());
	}

	/**
	 * A method that returns <em>tzais</em> (nightfall) when the sun is 6&deg; below the western geometric horizon
	 * (90&deg;) after {@link #getSunset sunset}. For information on the source of this calculation see
	 * {@link #ZENITH_6_DEGREES}.
	 * 
	 * @return The <code>Date</code> of nightfall. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_6_DEGREES
	 */
	public Long getTzaisBaalHatanya() {
		return this.getSunsetOffsetByDegrees(ZENITH_6_DEGREES);
	}
	
	/**
	 * A utility methos to calculate zmanim based on <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe
	 * Feinstein</a> as calculated in <a href="https://en.wikipedia.org/wiki/Mesivtha_Tifereth_Jerusalem">MTJ</a>, <a href=
	 * "https://en.wikipedia.org/wiki/Mesivtha_Tifereth_Jerusalem">Yeshiva of Staten Island</a>, and Camp Yeshiva
	 * of Staten Island. The day is split in two, from <em>alos</em> / sunrise to fixed local <em>chatzos</em>, and the
	 * second half of the day, from fixed local <em>chatzos</em> to sunset / <em>tzais</em>. Morning based times are calculated
	 * based on the first 6 hours, and afternoon times based on the second half of the day.
	 * 
	 * @param startOfHalfDay
	 *            The start of the half day. This would be <em>alos</em> or sunrise for morning based times and fixed
	 *            local <em>chatzos</em> for the second half of the day.
	 * @param endOfHalfDay
	 *            The end of the half day. This would be fixed local <em>chatzos</em> for morning based times and sunset
	 *            or <em>tzais</em> for afternoon based times.
	 * @param hours
	 *            the number of hours to offset the beginning of the first or second half of the day
	 * 
	 * @return the <code>Date</code> of the later of {@link #getMinchaGedolaBaalHatanya()} and {@link #getMinchaGedola30Minutes()}.
	 *         If the calculation can't be computed such as in the Arctic Circle where there is at least one day a year
	 *         where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 *
	 * @see ComplexZmanimCalendar#getFixedLocalChatzos()
	 */
	public Long getFixedLocalChatzosBasedZmanim(Long startOfHalfDay, Long endOfHalfDay, double hours) {
		if (startOfHalfDay == null || endOfHalfDay == null) {
			return null;
		}
		long shaahZmanis = (endOfHalfDay - startOfHalfDay) / 6;
		return (long)(startOfHalfDay + shaahZmanis * hours);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman krias shema</em> (latest time to recite <em>Shema</em> in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> that the
	 * day is calculated from dawn to nightfall, but calculated using the first half of the day only. The half a day starts
	 * at <em>alos</em> defined as {@link #getAlos18Degrees() 18&deg;} and ends at {@link #getFixedLocalChatzos() fixed local
	 * chatzos}. <em>Sof Zman Shema</em> is 3 <em>shaos zmaniyos</em> (solar hours) after <em>alos</em> or half of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos18Degrees()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanShmaMGA18DegreesToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getAlos18Degrees(), getFixedLocalChatzos(), 3);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman krias shema</em> (latest time to recite <em>Shema</em> in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> that the
	 * day is calculated from dawn to nightfall, but calculated using the first half of the day only. The half a day starts
	 * at <em>alos</em> defined as {@link #getAlos16Point1Degrees() 16.1&deg;} and ends at {@link #getFixedLocalChatzos() fixed local
	 * chatzos}. <em>Sof Zman Shema</em> is 3 <em>shaos zmaniyos</em> (solar hours) after this <em>alos</em> or half of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos16Point1Degrees()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getAlos16Point1Degrees(), getFixedLocalChatzos(), 3);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman krias shema</em> (latest time to recite <em>Shema</em> in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> that the
	 * day is calculated from dawn to nightfall, but calculated using the first half of the day only. The half a day starts
	 * at <em>alos</em> defined as {@link #getAlos90() 90 minutes before sunrise} and ends at {@link #getFixedLocalChatzos()
	 * fixed local chatzos}. <em>Sof Zman Shema</em> is 3 <em>shaos zmaniyos</em> (solar hours) after this <em>alos</em> or
	 * half of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos90()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanShmaMGA90MinutesToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getAlos90(), getFixedLocalChatzos(), 3);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman krias shema</em> (latest time to recite <em>Shema</em> in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a> that the
	 * day is calculated from dawn to nightfall, but calculated using the first half of the day only. The half a day starts
	 * at <em>alos</em> defined as {@link #getAlos72() 72 minutes before sunrise} and ends at {@link #getFixedLocalChatzos()
	 * fixed local chatzos}. <em>Sof Zman Shema</em> is 3 <em>shaos zmaniyos</em> (solar hours) after this <em>alos</em> or
	 * half of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getAlos72()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanShmaMGA72MinutesToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getAlos72(), getFixedLocalChatzos(), 3);
	}
		
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman krias shema</em> (latest time to recite <em>Shema</em> in the morning) according to the
	 * opinion of the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that the day is calculated from
	 * sunrise to sunset, but calculated using the first half of the day only. The half a day starts at {@link #getSunrise()
	 * sunrise} and ends at {@link #getFixedLocalChatzos() fixed local chatzos}. <em>Sof zman Shema</em> is 3 <em>shaos
	 * zmaniyos</em> (solar hours) after sunrise or half of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSunrise()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanShmaGRASunriseToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getSunrise(), getFixedLocalChatzos(), 3);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of the
	 * calculation of <em>sof zman tfila</em> (<em>zman tfilah</em> (the latest time to recite the morning prayers))
	 * according to the opinion of the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that the day is
	 * calculated from sunrise to sunset, but calculated using the first half of the day only. The half a day starts at
	 * {@link #getSunrise() sunrise} and ends at {@link #getFixedLocalChatzos() fixed local chatzos}. <em>Sof zman tefila</em>
	 * is 4 <em>shaos zmaniyos</em> (solar hours) after sunrise or 2/3 of this half-day.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman krias shema</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSunrise()
	 * @see #getFixedLocalChatzos()
	 * @see #getFixedLocalChatzosBasedZmanim(Long, Long, double)
	 */
	public Long getSofZmanTfilaGRASunriseToFixedLocalChatzos() {
		return getFixedLocalChatzosBasedZmanim(getSunrise(), getFixedLocalChatzos(), 4);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion of
	 * the calculation of <em>mincha gedola</em>, the earliest time one can pray <em>mincha</em> <a href=
	 * "https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>that is 30 minutes after {@link #getFixedLocalChatzos() fixed
	 * local chatzos}.
	 * 
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 *         
	 * @see #getMinchaGedola()
	 * @see #getFixedLocalChatzos()
	 * @see #getMinchaKetanaGRAFixedLocalChatzosToSunset
	 */
	public Long getMinchaGedolaGRAFixedLocalChatzos30Minutes() {
		return getTimeOffset(getFixedLocalChatzos(), MINUTE_MILLIS * 30);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion
	 * of the calculation of <em>mincha ketana</em> (the preferred time to recite the <em>mincha prayers</em> according to
	 * the opinion of the <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a> and others) calculated according
	 * to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that is 3.5 <em>shaos zmaniyos</em> (solar
	 * hours) after {@link #getFixedLocalChatzos() fixed local chatzos}.
	 * 
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 *         
	 * @see #getMinchaGedola()
	 * @see #getFixedLocalChatzos()
	 * @see #getMinchaGedolaGRAFixedLocalChatzos30Minutes
	 */
	public Long getMinchaKetanaGRAFixedLocalChatzosToSunset() {
		return getFixedLocalChatzosBasedZmanim(getFixedLocalChatzos(), getSunset(), 3.5);
	}
	
	/**
	 * This method returns <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe Feinstein's</a> opinion
	 * of the calculation of <em>plag hamincha</em>. This method returns <em>plag hamincha</em> calculated according to the
	 * <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> that the day ends at sunset and is 4.75 <em>shaos
	 * zmaniyos</em> (solar hours) after {@link #getFixedLocalChatzos() fixed local chatzos}.
	 * 
	 * @return the <code>Date</code> of the time of <em>mincha gedola</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 *         
	 * @see #getPlagHamincha()
	 * @see #getFixedLocalChatzos()
	 * @see #getMinchaKetanaGRAFixedLocalChatzosToSunset
	 * @see #getMinchaGedolaGRAFixedLocalChatzos30Minutes
	 */
	public Long getPlagHaminchaGRAFixedLocalChatzosToSunset() {
		return getFixedLocalChatzosBasedZmanim(getFixedLocalChatzos(), getSunset(), 4.75);
	}
	
	/**
	 * Method to return <em>tzais</em> (dusk) calculated as 50 minutes after sea level sunset. This method returns
	 * <em>tzais</em> (nightfall) based on the opinion of Rabbi Moshe Feinstein for the New York area. This time should
	 * not be used for latitudes different than the NY area.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Long getTzais50() {
		return getTimeOffset(getElevationAdjustedSunset(), 50 * MINUTE_MILLIS);
	}
	
	/**
	 * A method for calculating <em>samuch lemincha ketana</em>, / near <em>mincha ketana</em> time that is half an hour before
	 * {@link #getMinchaKetana()} or is 9 * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em>} (solar hours) after {@link
	 * #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()}
	 * setting), calculated according to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a> using a day starting at
	 * sunrise and ending at sunset. This is the time that eating or other activity can't begin prior to praying <em>mincha</em>.
	 * The calculation used is 9 * {@link #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() <em>alos</em>
	 * 16.1&deg;}. See the <a href="https://hebrewbooks.org/pdfpager.aspx?req=60387&st=&pgnum=294">Mechaber and Mishna Berurah
	 * 232</a> for details.
	 * 
	 * @see #getShaahZmanisGra()
	 * @see #getSamuchLeMinchaKetana16Point1Degrees()
	 * @return the <code>Date</code> of the time of <em>samuch lemincha ketana</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getSamuchLeMinchaKetanaGRA() {
		return getSamuchLeMinchaKetana(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}
	
	/**
	 * A method for calculating <em>samuch lemincha ketana</em>, / near <em>mincha ketana</em> time that is half an hour before
	 * {@link #getMinchaGedola16Point1Degrees()}  or 9 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day,
	 * calculated using a day starting and ending 16.1&deg; below the horizon. This is the time that eating or other activity
	 * can't begin prior to praying <em>mincha</em>. The calculation used is 9 * {@link #getShaahZmanis16Point1Degrees()} after
	 * {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;}. See the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=60387&st=&pgnum=294">Mechaber and Mishna Berurah 232</a>.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @return the <code>Date</code> of the time of <em>samuch lemincha ketana</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getSamuchLeMinchaKetana16Point1Degrees() {
		return getSamuchLeMinchaKetana(getAlos16Point1Degrees(), getTzais16Point1Degrees());
	}
	
	/**
	 * A method for calculating <em>samuch lemincha ketana</em>, / near <em>mincha ketana</em> time that is half an hour before
	 * {@link #getMinchaKetana72Minutes()}  or 9 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day,
	 * calculated using a day starting 72 minutes before sunrise and ending 72 minutes after sunset. This is the time that eating
	 * or other activity can't begin prior to praying <em>mincha</em>. The calculation used is 9 * {@link
	 * #getShaahZmanis16Point1Degrees()} after {@link #getAlos16Point1Degrees() <em>alos</em> 16.1&deg;}. See the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=60387&st=&pgnum=294">Mechaber and Mishna Berurah 232</a>.
	 * 
	 * @see #getShaahZmanis16Point1Degrees()
	 * @return the <code>Date</code> of the time of <em>samuch lemincha ketana</em>. If the calculation can't be computed such
	 *         as northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle
	 *         where the sun may not reach low enough below the horizon for this calculation, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Long getSamuchLeMinchaKetana72Minutes() {
		return getSamuchLeMinchaKetana(getAlos72(), getTzais72());
	}
}
