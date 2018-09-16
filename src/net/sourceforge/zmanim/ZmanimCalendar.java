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
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package net.sourceforge.zmanim;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.util.AstronomicalCalculator;
import net.sourceforge.zmanim.util.GeoLocation;

/**
 * The ZmanimCalendar is a specialized calendar that can calculate sunrise and sunset and Jewish <em>zmanim</em>
 * (religious times) for prayers and other Jewish religious duties. This class contains the main functionality of the
 * Zmanim library. For a much more extensive list of zmanim use the {@link ComplexZmanimCalendar} that extends this
 * class. See documentation for the {@link ComplexZmanimCalendar} and {@link AstronomicalCalendar} for simple examples
 * on using the API. According to Rabbi Dovid Yehudah Bursztyn in his <a href="http://www.worldcat.org/oclc/659793988"
 * >Zmanim Kehilchasam (second edition published in 2007)</a> chapter 2 (pages 186-187) no zmanim besides sunrise and
 * sunset should use elevation. However Rabbi Yechiel Avrahom Zilber in the <a href="http://hebrewbooks.org/51654"
 * >Birur Halacha Vol. 6</a> Ch. 58 Pages <a href="http://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=42">34</a>
 * and <a href="http://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=50">42</a> is of the opinion that elevation
 * should be accounted for in zmanim calculations. Related to this, Rabbi Yaakov Karp in <a href=
 * "http://www.worldcat.org/oclc/919472094">Shimush Zekeinim</a>, Ch. 1, page 17 states that obstructing horizons should
 * be factored into zmanim calculations. The setting defaults to false (elevation will not be used for zmanim calculations),
 * unless the setting is changed to true in {@link #setUseElevation(boolean)}. This will impact sunrise and sunset based
 * zmanim such as {@link #getSunrise()}, {@link #getSunset()}, {@link #getSofZmanShmaGRA()}, alos based zmanim such as
 * {@link #getSofZmanShmaMGA()} that are based on a fixed offset of sunrise or sunset and zmanim based on a percentage of
 * the day such as {@link ComplexZmanimCalendar#getSofZmanShmaMGA90MinutesZmanis()} that are based on sunrise and sunset.
 * It will not impact zmanim that are a degree based offset of sunrise and sunset, such as
 * {@link ComplexZmanimCalendar#getSofZmanShmaMGA16Point1Degrees()} or {@link ComplexZmanimCalendar#getSofZmanShmaBaalHatanya()}.
 * 
 * <p><b>Note:</b> It is important to read the technical notes on top of the {@link AstronomicalCalculator} documentation
 * before using this code.
 * <p>I would like to thank Rabbi Yaakov Shakow, the author of Luach Ikvei Hayom who spent a considerable amount of time
 * reviewing, correcting and making suggestions on the documentation in this library.
 * <h2>Disclaimer:</h2> I did my best to get accurate results, but please double-check before relying on these zmanim for
 * <em>halacha lemaaseh</em>.
 * 
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2018
 */
public class ZmanimCalendar extends AstronomicalCalendar {
	
	/**
	 * Is elevation factored in for some zmanim (see {@link #isUseElevation()} for additional information).
	 * @see #isUseElevation()
	 * @see #setUseElevation(boolean)
	 */
	private boolean useElevation;

	/**
	 * Is elevation above sea level calculated for times besides sunrise and sunset. According to Rabbi Dovid Yehuda
	 * Bursztyn in his <a href="http://www.worldcat.org/oclc/659793988">Zmanim Kehilchasam (second edition published
	 * in 2007)</a> chapter 2 (pages 186-187) no zmanim besides sunrise and sunset should use elevation. However Rabbi
	 * Yechiel Avrahom Zilber in the <a href="http://hebrewbooks.org/51654">Birur Halacha Vol. 6</a> Ch. 58 Pages
	 * <a href="http://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=42">34</a> and <a href=
	 * "http://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=50">42</a> is of the opinion that elevation should be
	 * accounted for in zmanim calculations. Related to this, Rabbi Yaakov Karp in <a href=
	 * "http://www.worldcat.org/oclc/919472094">Shimush Zekeinim</a>, Ch. 1, page 17 states that obstructing horizons
	 * should be factored into zmanim calculations.The setting defaults to false (elevation will not be used for zmanim
	 * calculations), unless the setting is changed to true in {@link #setUseElevation(boolean)}. This will impact sunrise
	 * and sunset based zmanim such as {@link #getSunrise()}, {@link #getSunset()}, {@link #getSofZmanShmaGRA()}, alos based
	 * zmanim such as {@link #getSofZmanShmaMGA()} that are based on a fixed offset of sunrise or sunset and zmanim based on
	 * a percentage of the day such as {@link ComplexZmanimCalendar#getSofZmanShmaMGA90MinutesZmanis()} that are based on
	 * sunrise and sunset. It will not impact zmanim that are a degree based offset of sunrise and sunset, such as
	 * {@link ComplexZmanimCalendar#getSofZmanShmaMGA16Point1Degrees()} or {@link ComplexZmanimCalendar#getSofZmanShmaBaalHatanya()}.
	 * 
	 * @return if the use of elevation is active
	 * 
	 * @see #setUseElevation(boolean)
	 */
	public boolean isUseElevation() {
		return useElevation;
	}

	/**
	 * Sets whether elevation above sea level is factored into <em>zmanim</em> calculations for times besides sunrise and sunset.
	 * See {@link #isUseElevation()} for more details. 
	 * @see #isUseElevation()
	 * 
	 * @param useElevation set to true to use elevation in zmanim calculations
	 */
	public void setUseElevation(boolean useElevation) {
		this.useElevation = useElevation;
	}

	/**
	 * The zenith of 16.1&deg; below geometric zenith (90&deg;). This calculation is used for determining <em>alos</em>
	 * (dawn) and <em>tzais</em> (nightfall) in some opinions. It is based on the calculation that the time between dawn
	 * and sunrise (and sunset to nightfall) is 72 minutes, the time that is takes to walk 4 <em>mil</em> at 18 minutes
	 * a mil (<em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others). The sun's position at
	 * 72 minutes before {@link #getSunrise sunrise} in Jerusalem on the equinox is 16.1&deg; below
	 * {@link #GEOMETRIC_ZENITH geometric zenith}.
	 * 
	 * @see #getAlosHashachar()
	 * @see ComplexZmanimCalendar#getAlos16Point1Degrees()
	 * @see ComplexZmanimCalendar#getTzais16Point1Degrees()
	 * @see ComplexZmanimCalendar#getSofZmanShmaMGA16Point1Degrees()
	 * @see ComplexZmanimCalendar#getSofZmanTfilaMGA16Point1Degrees()
	 * @see ComplexZmanimCalendar#getMinchaGedola16Point1Degrees()
	 * @see ComplexZmanimCalendar#getMinchaKetana16Point1Degrees()
	 * @see ComplexZmanimCalendar#getPlagHamincha16Point1Degrees()
	 * @see ComplexZmanimCalendar#getPlagAlos16Point1ToTzaisGeonim7Point083Degrees()
	 * @see ComplexZmanimCalendar#getSofZmanShmaAlos16Point1ToSunset()
	 */
	protected static final double ZENITH_16_POINT_1 = GEOMETRIC_ZENITH + 16.1;

	/**
	 * The zenith of 8.5&deg; below geometric zenith (90&deg;). This calculation is used for calculating <em>alos</em>
	 * (dawn) and <em>tzais</em> (nightfall) in some opinions. This calculation is based on the position of the sun 36
	 * minutes after {@link #getSunset sunset} in Jerusalem on March 16, about 4 days before the equinox, the day that a
	 * solar hour is 60 minutes, which is 8.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. The <em><a href=
	 * "http://www.worldcat.org/oclc/29283612">Ohr Meir</a></em> considers this the time that 3 small stars are visible,
	 * which is later than the required 3 medium stars.
	 * 
	 * @see #getTzais()
	 * @see ComplexZmanimCalendar#getTzaisGeonim8Point5Degrees()
	 */
	protected static final double ZENITH_8_POINT_5 = GEOMETRIC_ZENITH + 8.5;

	/**
	 * The default <em>Shabbos</em> candle lighting offset is 18 minutes. This can be changed via the
	 * {@link #setCandleLightingOffset(double)} and retrieved by the {@link #getCandleLightingOffset()}.
	 */
	private double candleLightingOffset = 18;
	
	/**
	 * This method will return {@link #getSeaLevelSunrise() sea level sunrise} if {@link #isUseElevation()} is false
	 * (the default), or elevation adjusted {@link AstronomicalCalendar#getSunrise()} if it is true. This allows relevant zmanim
	 * in this and extending classes (such as the {@link ComplexZmanimCalendar}) to automatically adjust to the elevation setting.
	 * 
	 * @return {@link #getSeaLevelSunrise()} if {@link #isUseElevation()} is false (the default), or elevation adjusted
	 *          {@link AstronomicalCalendar#getSunrise()} if it is true.
	 * @see net.sourceforge.zmanim.AstronomicalCalendar#getSunrise()
	 */
	protected Date getElevationAdjustedSunrise() {
		if(isUseElevation()) {
			return super.getSunrise();
		}
		return getSeaLevelSunrise();
	}
	
	/**
	 * This method will return {@link #getSeaLevelSunrise() sea level sunrise} if {@link #isUseElevation()} is false
	 * (the default), or elevation adjusted {@link AstronomicalCalendar#getSunrise()} if it is true. This allows relevant zmanim
	 * in this and extending classes (such as the {@link ComplexZmanimCalendar}) to automatically adjust to the elevation setting.
	 * 
	 * @return {@link #getSeaLevelSunset()} if {@link #isUseElevation()} is false (the default), or elevation adjusted
	 *          {@link AstronomicalCalendar#getSunset()} if it is true.
	 * @see net.sourceforge.zmanim.AstronomicalCalendar#getSunset()
	 */
	protected Date getElevationAdjustedSunset() {
		if(isUseElevation()) {
			return super.getSunset();
		}
		return getSeaLevelSunset();
	}

	/**
	 * A method that returns <em>tzais</em> (nightfall) when the sun is {@link ZENITH_8_POINT_5 8.5&deg;} below the
	 * {@link #GEOMETRIC_ZENITH geometric horizon} (90&deg;) after {@link #getSunset sunset}, a time that Rabbi Meir
	 * Posen in his the <em><a href="http://www.worldcat.org/oclc/29283612">Ohr Meir</a></em> calculated that 3 small
	 * stars are visible, which is later than the required 3 medium stars. See the {@link #ZENITH_8_POINT_5} constant.
	 * 
	 * @see #ZENITH_8_POINT_5
	 * 
	 * @return The <code>Date</code> of nightfall. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_8_POINT_5
	 * ComplexZmanimCalendar#getTzaisGeonim8Point5Degrees() that returns an identical time to this generic <em>tzais</em>
	 */
	public Date getTzais() {
		return getSunsetOffsetByDegrees(ZENITH_8_POINT_5);
	}

	/**
	 * Returns <em>alos</em> (dawn) based on the time when the sun is {@link #ZENITH_16_POINT_1 16.1&deg;} below the
	 * eastern {@link #GEOMETRIC_ZENITH geometric horizon} before {@link #getSunrise sunrise}. This is based on the
	 * calculation that the time between dawn and sunrise (and sunset to nightfall) is 72 minutes, the time that is
	 * takes to walk 4 <em>mil</em> at 18 minutes a mil (<em><a href="https://en.wikipedia.org/wiki/Maimonides"
	 * >Rambam</a></em> and others). The sun's position at 72 minutes before {@link #getSunrise sunrise} in Jerusalem
	 * on the equinox is 16.1&deg; below. See the {@link #GEOMETRIC_ZENITH} constant.
	 * 
	 * @see #ZENITH_16_POINT_1
	 * @see ComplexZmanimCalendar#getAlos16Point1Degrees()
	 * 
	 * @return The <code>Date</code> of dawn. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getAlosHashachar() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 72 minutes before {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting). This time
	 * is based on the time to walk the distance of 4 <em>Mil</em> at 18 minutes a <em>Mil</em>. The 72 minute time (but
	 * not the concept of fixed minutes) is based on the opinion that the time of the <em>Neshef</em> (twilight between
	 * dawn and sunrise) does not vary by the time of year or location but depends on the time it takes to walk the
	 * distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos72() {
		return getTimeOffset(getElevationAdjustedSunrise(), -72 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>chatzos</em> (midday) following most opinions that <em>chatzos</em> is the midpoint
	 * between {@link #getSeaLevelSunrise sea level sunrise} and {@link #getSeaLevelSunset sea level sunset}. A day
	 * starting at <em>alos</em> and ending at <em>tzais</em> using the same time or degree offset will also return
	 * the same time. The returned value is identical to {@link #getSunTransit()}. In reality due to lengthening or
	 * shortening of day, this is not necessarily the exact midpoint of the day, but it is very close.
	 * 
	 * @see AstronomicalCalendar#getSunTransit()
	 * @return the <code>Date</code> of chatzos. If the calculation can't be computed such as in the Arctic Circle
	 *         where there is at least one day where the sun does not rise, and one where it does not set, a null will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getChatzos() {
		return getSunTransit();
	}

	/**
	 * A generic method for calculating the latest <em>zman krias shema</em> (time to recite shema in the morning)
	 * that is 3 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and
	 * end of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours),
	 * and the latest <em>zman krias shema</em> is calculated as 3 of those <em>shaos zmaniyos</em> after the beginning of
	 * the day. As an example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise()
	 * sea level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()}
	 * elevation setting) to this method will return <em>sof zman krias shema</em> according to the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunset or any tzais passed to
	 *            this method.
	 * @return the <code>Date</code> of the latest <em>zman shema</em> based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanShma(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 3);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite shema in the morning) that is 3 *
	 * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em> (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. 
	 *  The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 *  sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 *  setting).
	 * 
	 * @see #getSofZmanShma(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #isUseElevation()
	 * @see ComplexZmanimCalendar#getSofZmanShmaBaalHatanya()
	 * @return the <code>Date</code> of the latest zman shema according to the GRA. If the calculation can't be computed
	 * such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 * does not set, a null will be returned. See the detailed explanation on top of the {@link AstronomicalCalendar}
	 * documentation.
	 */
	public Date getSofZmanShmaGRA() {
		return getSofZmanShma(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite shema in the morning) that is 3 *
	 * <em>{@link #getShaahZmanisMGA() shaos zmaniyos}</em> (solar hours) after {@link #getAlos72()}, according to the
	 * <em><a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a></em>. The day is calculated
	 * from 72 minutes before {@link #getSeaLevelSunrise() sea level sunrise} to 72 minutes after {@link
	 * #getSeaLevelSunrise sea level sunset} or from 72 minutes before {@link #getSunrise() sunrise} to {@link #getSunset()
	 * sunset} (depending on the {@link #isUseElevation()} setting).
	 * 
	 * @return the <code>Date</code> of the latest <em>zman shema</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanShma(Date, Date)
	 * @see ComplexZmanimCalendar#getShaahZmanis72Minutes()
	 * @see ComplexZmanimCalendar#getAlos72()
	 * @see ComplexZmanimCalendar#getSofZmanShmaMGA72Minutes() that 
	 */
	public Date getSofZmanShmaMGA() {
		return getSofZmanShma(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em><a href=
	 * "https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and <em>Rabbeinu Tam</em> that <em>tzais</em> is
	 * calculated as 72 minutes, the time it takes to walk 4 <em>Mil</em> at 18 minutes a <em>Mil</em>. Based on
	 * the on the {@link #isUseElevation()} setting) a 72 minute offset from either {@link #getSunset() sunset} or
	 * {@link #getSeaLevelSunset() sea level sunset} is used.
	 * 
	 * @see ComplexZmanimCalendar#getTzais16Point1Degrees()
	 * @return the <code>Date</code> representing 72 minutes after sunset. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getTzais72() {
		return getTimeOffset(getElevationAdjustedSunset(), 72 * MINUTE_MILLIS);
	}

	/**
	 * A method to return candle lighting time, calculated as {@link #getCandleLightingOffset()} minutes before
	 * {@link #getSeaLevelSunset() sea level sunset}. This will return the time for any day of the week, since it can be
	 * used to calculate candle lighting time for <em>Yom Tov</em> (mid-week holidays) as well. Elevation adjustments
	 * are intentionally not performed by this method, but you can calculate it by passing the elevation adjusted sunset
	 * to {@link #getTimeOffset(Date, long)}.
	 * 
	 * @return candle lighting time. If the calculation can't be computed such as in the Arctic Circle where there is at
	 *         least one day a year where the sun does not rise, and one where it does not set, a null will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * 
	 * @see #getSeaLevelSunset()
	 * @see #getCandleLightingOffset()
	 * @see #setCandleLightingOffset(double)
	 */
	public Date getCandleLighting() {
		return getTimeOffset(getSeaLevelSunset(), -getCandleLightingOffset() * MINUTE_MILLIS);
	}

	/**
	 * A generic method for calculating the latest <em>zman tfilah</em> (time to recite the morning prayers)
	 * that is 4 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and
	 * end of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours),
	 * and <em>sof zman tfila</em> is calculated as 4 of those <em>shaos zmaniyos</em> after the beginning of the day.
	 * As an example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise()
	 * sea level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()}
	 * elevation setting) to this method will return <em>zman tfilah</em> according to the opinion of the <em><a href=
	 * "https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman tfilah</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>zman tfilah</em>. This can be sunset or any tzais passed to this
	 *            method.
	 * @return the <code>Date</code> of the latest <em>zman tfilah</em> based on the start and end of day times passed
	 *         to this method. If the calculation can't be computed such as in the Arctic Circle where there is at least
	 *         one day a year where the sun does not rise, and one where it does not set, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanTfila(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 4);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite shema in the morning) that is 4 *
	 * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em> (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. 
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting).
	 * 
	 * @see #getSofZmanTfila(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see ComplexZmanimCalendar#getSofZmanTfilaBaalHatanya()
	 * @return the <code>Date</code> of the latest zman tfilah. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getSofZmanTfilaGRA() {
		return getSofZmanTfila(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite shema in the morning) that is 4 *
	 * <em>{@link #getShaahZmanisMGA() shaos zmaniyos}</em> (solar hours) after {@link #getAlos72()}, according to the
	 * <em><a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a></em>. The day is calculated
	 * from 72 minutes before {@link #getSeaLevelSunrise() sea level sunrise} to 72 minutes after {@link
	 * #getSeaLevelSunrise sea level sunset} or from 72 minutes before {@link #getSunrise() sunrise} to {@link #getSunset()
	 * sunset} (depending on the {@link #isUseElevation()} setting).
	 * 
	 * @return the <code>Date</code> of the latest zman tfila. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set), a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 * @see #getSofZmanTfila(Date, Date)
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 */
	public Date getSofZmanTfilaMGA() {
		return getSofZmanTfila(getAlos72(), getTzais72());
	}

	/**
	 * A generic method for calculating the latest <em>mincha gedola</em> (the earliest time to recite the mincha  prayers)
	 * that is 6.5 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and end
	 * of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours), and
	 * <em>mincha gedola</em> is calculated as 6.5 of those <em>shaos zmaniyos</em> after the beginning of the day. As an
	 * example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea level
	 * sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation
	 * setting) to this method will return <em>mincha gedola</em> according to the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha gedola</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>Mincha gedola</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @return the <code>Date</code> of the time of <em>Mincha gedola</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaGedola(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 6.5);
	}

	/**
	 * This method returns the latest <em>mincha gedola</em>,the earliest time one can pray <em>mincha</em> that is 6.5 *
	 * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em> (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. <em>Mincha gedola</em> is the earliest
	 * time one can pray <em>mincha</em>. The Ramba"m is of the opinion that it is better to delay <em>mincha</em> until
	 * <em>{@link #getMinchaKetana() mincha ketana}</em> while the <em>Ra"sh, Tur, GRA</em> and others are of the
	 * opinion that <em>mincha</em> can be prayed <em>lechatchila</em> starting at <em>mincha gedola</em>.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting).
	 * 
	 * @see #getMinchaGedola(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaKetana()
	 * @see ComplexZmanimCalendar#getMinchaGedolaBaalHatanya()
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaGedola() {
		return getMinchaGedola(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * A generic method for calculating <em>mincha ketana</em>, (the preferred time to recite the mincha prayers in
	 * the opinion of the <em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others) that is
	 * 9.5 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and end
	 * of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours), and
	 * <em>mincha ketana</em> is calculated as 9.5 of those <em>shaos zmaniyos</em> after the beginning of the day. As an
	 * example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea level
	 * sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation
	 * setting) to this method will return <em>mincha ketana</em> according to the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha ketana</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>Mincha ketana</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @return the <code>Date</code> of the time of <em>Mincha ketana</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 *
	 */
	public Date getMinchaKetana(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 9.5);
	}

	/**
	 * This method returns <em>mincha ketana</em>,the preferred earliest time to pray <em>mincha</em> in the
	 * opinion of the <em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others, that is 9.5
	 * * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em> (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. For more information on this see the
	 * documentation on <em>{@link #getMinchaGedola() mincha gedola}</em>.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting.
	 * 
	 * @see #getMinchaKetana(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaGedola()
	 * @see ComplexZmanimCalendar#getMinchaKetanaBaalHatanya()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaKetana() {
		return getMinchaKetana(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * A generic method for calculating <em>plag hamincha</em> (the earliest time that Shabbos can be started) that is
	 * 10.75 hours after the start of the day, (or 1.25 hours before the end of the day) based on the start and end of
	 * the day passed to the method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours), and
	 * <em>plag hamincha</em> is calculated as 10.75 of those <em>shaos zmaniyos</em> after the beginning of the day. As an
	 * example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea level
	 * sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation
	 * setting) to this method will return <em>plag mincha</em> according to the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating plag. This can be sunrise or any alos passed to this method.
	 * @param endOfDay
	 *            the start of day for calculating plag. This can be sunrise or any alos passed to this method.
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 10.75);
	}

	/**
	 * This method returns <em>plag hamincha</em>, that is 10.75 * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em>
	 * (solar hours) after {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level sunrise} (depending on
	 * the {@link #isUseElevation()} setting), according to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon"
	 * >GRA</a></em>. Plag hamincha is the earliest time that <em>Shabbos</em> can be started.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunrise sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * 
	 * @see #getPlagHamincha(Date, Date)
	 * @see ComplexZmanimCalendar#getPlagHaminchaBaalHatanya()
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha() {
		return getPlagHamincha(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * A method that returns a <em>shaah zmanis</em> ({@link #getTemporalHour(Date, Date) temporal hour}) according to
	 * the opinion of the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. This calculation divides
	 * the day based on the opinion of the <em>GRA</em> that the day runs from from {@link #getSeaLevelSunrise() sea
	 * level sunrise} to {@link #getSeaLevelSunrise sea level sunset} or {@link #getSunrise() sunrise} to
	 * {@link #getSunset() sunset} (depending on the {@link #isUseElevation()} setting). The day is split into 12 equal
	 * parts with each one being a <em>shaah zmanis</em>. This method is similar to {@link #getTemporalHour}, but can
	 * account for elevation.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em> calculated from sunrise to sunset.
	 *         If the calculation can't be computed such as in the Arctic Circle where there is at least one day a year
	 *         where the sun does not rise, and one where it does not set, {@link Long#MIN_VALUE} will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTemporalHour(Date, Date)
	 * @see #getSeaLevelSunrise()
	 * @see #getSeaLevelSunset()
	 * @see ComplexZmanimCalendar#getShaahZmanisBaalHatanya()
	 */
	public long getShaahZmanisGra() {
		return getTemporalHour(getElevationAdjustedSunrise(), getElevationAdjustedSunset());
	}

	/**
	 * A method that returns a <em>shaah zmanis</em> (temporal hour) according to the opinion of the <em><a href=
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a></em> based on a 72 minutes <em>alos</em>
	 * and <em>tzais</em>. This calculation divides the day that runs from dawn to dusk (for sof zman krias shema and tfila).
	 * Dawn for this calculation is 72 minutes before {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level
	 * sunrise} (depending on the {@link #isUseElevation()} elevation setting) and dusk is 72 minutes after {@link #getSunset
	 * sunset} or {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation setting).
	 * This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. Alternate methods of calculating a
	 * <em>shaah zmanis</em> according to the Magen Avraham (MGA) are available in the subclass {@link ComplexZmanimCalendar}.
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em>. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public long getShaahZmanisMGA() {
		return getTemporalHour(getAlos72(), getTzais72());
	}

	/**
	 * Default constructor will set a default {@link GeoLocation#GeoLocation()}, a default
	 * {@link AstronomicalCalculator#getDefault() AstronomicalCalculator} and default the calendar to the current date.
	 * 
	 * @see AstronomicalCalendar#AstronomicalCalendar()
	 */
	public ZmanimCalendar() {
		super();
	}

	/**
	 * A constructor that takes a {@link GeoLocation} as a parameter.
	 * 
	 * @param location
	 *            the location
	 */
	public ZmanimCalendar(GeoLocation location) {
		super(location);
	}

	/**
	 * A method to get the offset in minutes before {@link AstronomicalCalendar#getSeaLevelSunset() sea level sunset} which
	 * is used in calculating candle lighting time. The default time used is 18 minutes before sea level sunset. Some
	 * calendars use 15 minutes, while the custom in Jerusalem is to use a 40 minute offset. Please check the local custom
	 * for candle lighting time.
	 * 
	 * @return Returns the currently set candle lighting offset in minutes.
	 * @see #getCandleLighting()
	 * @see #setCandleLightingOffset(double)
	 */
	public double getCandleLightingOffset() {
		return candleLightingOffset;
	}

	/**
	 * A method to set the offset in minutes before {@link AstronomicalCalendar#getSeaLevelSunset() sea level sunset} that is
	 * used in calculating candle lighting time. The default time used is 18 minutes before sunset. Some calendars use 15
	 * minutes, while the custom in Jerusalem is to use a 40 minute offset.
	 * 
	 * @param candleLightingOffset
	 *            The candle lighting offset to set in minutes.
	 * @see #getCandleLighting()
	 * @see #getCandleLightingOffset()
	 */
	public void setCandleLightingOffset(double candleLightingOffset) {
		this.candleLightingOffset = candleLightingOffset;
	}
	
	/**
	 * This is a utility method to determine if the current Date (date-time) passed in has a <em>melacha</em> (work) prohibition.
	 * Since there are many opinions on the time of <em>tzais</em>, the <em>tzais</em> for the current day has to be passed to this
	 * class. Sunset is the classes current day's {@link #getElevationAdjustedSunset() elevation adjusted sunset} that observes the
	 * {@link isUseElevation()} settings. The {@link JewishCalendar#getInIsrael()} will be set by the inIsrael parameter.
	 * 
	 * @param currentTime the current time
	 * @param tzais the time of tzais
	 * @param inIsrael whether to use Israel holiday scheme or not
	 * 
	 * @return true if <em>melacha</em> is prohibited or false if it is not.
	 * 
	 * @see JewishCalendar#isAssurBemelacha()
	 * @see JewishCalendar#hasCandleLighting()
	 * @see JewishCalendar#setInIsrael(boolean)
	 */
	public boolean isAssurBemlacha(Date currentTime, Date tzais, boolean inIsrael) {
		JewishCalendar jewishCalendar = new JewishCalendar();
		jewishCalendar.setGregorianDate(getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DAY_OF_MONTH));
		jewishCalendar.setInIsrael(inIsrael);
		
		if(jewishCalendar.hasCandleLighting() && currentTime.compareTo(getElevationAdjustedSunset()) >= 0) { //erev shabbos, YT or YT sheni and after shkiah
			return true;
		}
		
		if(jewishCalendar.isAssurBemelacha()  && currentTime.compareTo(tzais) <= 0) { //is shabbos or YT and it is before tzais
			return true;
		}
		
		return false;
	}
}
