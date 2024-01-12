/*
 * Zmanim Java API
 * Copyright (C) 2004-2023 Eliyahu Hershfeld
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
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim;

import java.util.Calendar;
import java.util.Date;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;

/**
 * The ZmanimCalendar is a specialized calendar that can calculate sunrise, sunset and Jewish <em>zmanim</em>
 * (religious times) for prayers and other Jewish religious duties. This class contains the main functionality of the
 * <em>Zmanim</em> library. For a much more extensive list of <em>zmanim</em>, use the {@link ComplexZmanimCalendar} that
 * extends this class. See documentation for the {@link ComplexZmanimCalendar} and {@link AstronomicalCalendar} for
 * simple examples on using the API. 
 * <strong>Elevation based <em>zmanim</em> (even sunrise and sunset) should not be used <em>lekula</em> without the guidance
 * of a <em>posek</em></strong>. According to Rabbi Dovid Yehudah Bursztyn in his
 * <a href="https://www.worldcat.org/oclc/1158574217">Zmanim Kehilchasam, 7th edition</a> chapter 2, section 7 (pages 181-182)
 * and section 9 (pages 186-187), no <em>zmanim</em> besides sunrise and sunset should use elevation. However, Rabbi Yechiel
 * Avrahom Zilber in the <a href="https://hebrewbooks.org/51654">Birur Halacha Vol. 6</a> Ch. 58 Pages
 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=42">34</a> and
 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=50">42</a> is of the opinion that elevation should be
 * accounted for in <em>zmanim</em> calculations. Related to this, Rabbi Yaakov Karp in <a href=
 * "https://www.worldcat.org/oclc/919472094">Shimush Zekeinim</a>, Ch. 1, page 17 states that obstructing horizons should
 * be factored into <em>zmanim</em> calculations. The setting defaults to false (elevation will not be used for
 * <em>zmanim</em> calculations besides sunrise and sunset), unless the setting is changed to true in {@link
 * #setUseElevation(boolean)}. This will impact sunrise and sunset-based <em>zmanim</em> such as {@link #getSunrise()},
 * {@link #getSunset()}, {@link #getSofZmanShmaGRA()}, <em>alos</em>-based <em>zmanim</em> such as {@link #getSofZmanShmaMGA()}
 * that are based on a fixed offset of sunrise or sunset and <em>zmanim</em> based on a percentage of the day such as
 * {@link ComplexZmanimCalendar#getSofZmanShmaMGA90MinutesZmanis()} that are based on sunrise and sunset. Even when set to
 * true it will not impact <em>zmanim</em> that are a degree-based offset of sunrise and sunset, such as {@link
 * ComplexZmanimCalendar#getSofZmanShmaMGA16Point1Degrees()} or {@link ComplexZmanimCalendar#getSofZmanShmaBaalHatanya()} since
 * these <em>zmanim</em> are not linked to sunrise or sunset times (the calculations are based on the astronomical definition of
 * sunrise and sunset calculated in a vacuum with the solar radius above the horizon), and are therefore not impacted by the use
 * of elevation.
 * For additional information on the <em>halachic</em> impact of elevation on <em>zmanim</em> see:
 * <ul>
 * <li><a href="https://www.nli.org.il/en/books/NNL_ALEPH002542826/NLI">Zmanei Halacha Lema'aseh</a> 4th edition by <a href=
 * "http://beinenu.com/rabbis/%D7%94%D7%A8%D7%91-%D7%99%D7%93%D7%99%D7%93%D7%99%D7%94-%D7%9E%D7%A0%D7%AA">Rabbi Yedidya Manat</a>.
 * See section 1, pages 11-12 for a very concise write-up, with details in section 2, pages 37 - 63 and 133 - 151.</li>
 * <li><a href="https://www.worldcat.org/oclc/1158574217">Zmanim Kehilchasam</a> 7th edition, by Rabbi Dovid Yehuda Burstein,  vol 1,
 * chapter 2, pages 95 - 188.</li>
 * <li><a href="https://www.worldcat.org/oclc/36089452">Hazmanim Bahalacha</a> by Rabbi Chaim Banish , perek 7, pages 53 - 63.</li>
 * </ul>
 * 
 * <p><b>Note:</b> It is important to read the technical notes on top of the {@link AstronomicalCalculator} documentation
 * before using this code.
 * <p>I would like to thank <a href="https://www.worldcat.org/search?q=au%3AShakow%2C+Yaakov">Rabbi Yaakov Shakow</a>, the
 * author of Luach Ikvei Hayom who spent a considerable amount of time reviewing, correcting and making suggestions on the
 * documentation in this library.
 * <h2>Disclaimer:</h2> I did my best to get accurate results, but please double-check before relying on these
 * <em>zmanim</em> for <em>halacha lema'aseh</em>.
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2023
 */
public class ZmanimCalendar extends AstronomicalCalendar {
	
	/**
	 * Is elevation factored in for some <em>zmanim</em> (see {@link #isUseElevation()} for additional information).
	 * @see #isUseElevation()
	 * @see #setUseElevation(boolean)
	 */
	private boolean useElevation;

	/**
	 * Is elevation above sea level calculated for times besides sunrise and sunset. According to Rabbi Dovid Yehuda
	 * Bursztyn in his <a href="https://www.worldcat.org/oclc/659793988">Zmanim Kehilchasam (second edition published
	 * in 2007)</a> chapter 2 (pages 186-187) no <em>zmanim</em> besides sunrise and sunset should use elevation. However,
	 * Rabbi Yechiel Avrahom Zilber in the <a href="https://hebrewbooks.org/51654">Birur Halacha Vol. 6</a> Ch. 58 Pages
	 * <a href="https://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=42">34</a> and <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=51654&amp;pgnum=50">42</a> is of the opinion that elevation should be
	 * accounted for in <em>zmanim</em> calculations. Related to this, Rabbi Yaakov Karp in <a href=
	 * "https://www.worldcat.org/oclc/919472094">Shimush Zekeinim</a>, Ch. 1, page 17 states that obstructing horizons
	 * should be factored into <em>zmanim</em> calculations.The setting defaults to false (elevation will not be used for
	 * <em>zmanim</em> calculations), unless the setting is changed to true in {@link #setUseElevation(boolean)}. This will
	 * impact sunrise and sunset based <em>zmanim</em> such as {@link #getSunrise()}, {@link #getSunset()},
	 * {@link #getSofZmanShmaGRA()}, alos based <em>zmanim</em> such as {@link #getSofZmanShmaMGA()} that are based on a
	 * fixed offset of sunrise or sunset and <em>zmanim</em> based on a percentage of the day such as {@link
	 * ComplexZmanimCalendar#getSofZmanShmaMGA90MinutesZmanis()} that are based on sunrise and sunset. It will not impact
	 * <em>zmanim</em> that are a degree based offset of sunrise and sunset, such as
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
	 * @param useElevation set to true to use elevation in <em>zmanim</em> calculations
	 */
	public void setUseElevation(boolean useElevation) {
		this.useElevation = useElevation;
	}
	
	/**
	 * Is astronomical <em>chatzos</em> used for <em>zmanim</em> calculations. The default value of <code>true</code> will
	 * keep the standard astronomical <em>chatzos</em> calculation, while setting it to <code>false</code> will use half of
	 * a solar day calculation for <em>chatzos</em>.
	 * @see #isUseAstronomicalChatzos()
	 * @see #setUseAstronomicalChatzos(boolean)
	 * @see #getChatzos()
	 * @see #getSunTransit()
	 * @see #getChatzosAsHalfDay()
	 * @see #useAstronomicalChatzosForOtherZmanim
	 */
	private boolean useAstronomicalChatzos = true;
	
	/**
	 * Is {@link #getSunTransit() astronomical <em>chatzos</em>} used for {@link #getChatzos()} for enhanced accuracy. For
	 * example as the day lengthens, the second half of the day is longer than the first and astronomical <em>chatzos</em>
	 * would be a drop earlier than half of the time between sunrise and sunset.
	 * 
	 * @todo In the future, if this is set to true, the following may change to enhance accuracy. {@link #getSofZmanShmaGRA()
	 * <em>Sof zman Shma</em> GRA} would be calculated as 3 <em>shaos zmaniyos</em> after sunrise, but the <em>shaos
	 * zmaniyos</em> would be calculated a a 6th of the time between sunrise and <em>chatzos</em>, as opposed to a 12th of the
	 * time between sunrise and sunset. {@link #getMinchaGedola() <em>mincha gedola</em>} will be calculated as half a
	 * <em>shaah zmanis</em> of afternoon hours (a 6th of the time between <em>chatzos</em> and sunset after astronomical
	 * <em>chatzos</em> as opposed to 6.5 <em>shaos zmaniyos</em> after sunrise. {@link #getPlagHamincha() <em>Plag
	 * hamincha</em>} would be calculated as 4.75 <em>shaos zmaniyos</em> after astronomical <em>chatzos</em> as opposed to 10.75
	 * <em>shaos zmaniyos</em> after sunrise. Etc.
	 * 
	 * @return if the use of astronomical <em>chatzos</em> is active.
	 * @see #useAstronomicalChatzos
	 * @see #setUseAstronomicalChatzos(boolean)
	 * @see #getChatzos()
	 * @see #getSunTransit()
	 * @see #getChatzosAsHalfDay()
	 * @see #isUseAstronomicalChatzosForOtherZmanim()
	 */
	public boolean isUseAstronomicalChatzos() {
		return useAstronomicalChatzos;
	}

	/**
	 * Sets if astronomical <em>chatzos</em> should be used in calculations of other <em>zmanim</em> for enhanced accuracy.
	 * @param useAstronomicalChatzos set to true to use astronomical in <em>chatzos</em> in <em>zmanim</em> calculations.
	 * @see #useAstronomicalChatzos
	 * @see #isUseAstronomicalChatzos()
	 * @see #getChatzos()
	 * @see #getSunTransit()
	 * @see #getChatzosAsHalfDay()
	 * @see #setUseAstronomicalChatzosForOtherZmanim(boolean)
	 */
	public void setUseAstronomicalChatzos(boolean useAstronomicalChatzos) {
		this.useAstronomicalChatzos = useAstronomicalChatzos;
	}
	
	/**
	 * Is astronomical <em>chatzos</em> used for <em>zmanim</em> calculations besides <em>chatzos</em> itself for enhanced
	 * accuracy. The default value of <code>false</code> will keep the standard start to end of day calculations, while setting
	 * it to <code>true</code> will use half of a solar day calculation for <em>zmanim</em>.
	 * @see #isUseAstronomicalChatzosForOtherZmanim()
	 * @see #setUseAstronomicalChatzosForOtherZmanim(boolean)
	 * @see #isUseAstronomicalChatzos()
	 * @see #setUseAstronomicalChatzos(boolean)
	 * @see #getChatzos()
	 */
	private boolean useAstronomicalChatzosForOtherZmanim = false;
	
	/**
	 * Is astronomical <em>chatzos</em> used for <em>zmanim</em> calculations besides <em>chatzos</em> itself for enhanced
	 * accuracy. For example as the day is lengthening (as we approach spring season), the second half of the day is longer than
	 * the first and astronomical <em>chatzos</em> would be a drop earlier than half of the time between sunrise and sunset.
	 * Conversely, the second half of the day would be shorter in the 'fall' season as the days start getting shorter.
	 * 
	 * @todo In the future, if this is set to true, the following may change to enhance accuracy. {@link #getSofZmanShmaGRA()
	 * <em>Sof zman Shma</em> GRA} would be calculated as 3 <em>shaos zmaniyos</em> after sunrise, but the <em>shaos
	 * zmaniyos</em> would be calculated a a 6th of the time between sunrise and <em>chatzos</em>, as opposed to a 12th of the
	 * time between sunrise and sunset. {@link #getMinchaGedola() <em>mincha gedola</em>} will be calculated as half a
	 * <em>shaah zmanis</em> of afternoon hours (a 6th of the time between <em>chatzos</em> and sunset after astronomical
	 * <em>chatzos</em> as opposed to 6.5 <em>shaos zmaniyos</em> after sunrise. {@link #getPlagHamincha() <em>Plag
	 * hamincha</em>} would be calculated as 4.75 <em>shaos zmaniyos</em> after astronomical <em>chatzos</em> as opposed to 10.75
	 * <em>shaos zmaniyos</em> after sunrise. Etc.
	 * 
	 * @return if the use of astronomical <em>chatzos</em> is active.
	 * @see #useAstronomicalChatzosForOtherZmanim
	 * @see #setUseAstronomicalChatzosForOtherZmanim(boolean)
	 * @see #useAstronomicalChatzos
	 * @see #setUseAstronomicalChatzos(boolean)
	 */
	public boolean isUseAstronomicalChatzosForOtherZmanim() {
		return useAstronomicalChatzosForOtherZmanim;
	}

	/**
	 * Sets if astronomical <em>chatzos</em> should be used in calculations of other <em>zmanim</em> for enhanced accuracy.
	 * @param useAstronomicalChatzosForOtherZmanim set to true to use astronomical in <em>chatzos</em> in <em>zmanim</em> calculations.
	 * @see #useAstronomicalChatzos
	 * @see #isUseAstronomicalChatzos()
	 */
	public void setUseAstronomicalChatzosForOtherZmanim(boolean useAstronomicalChatzosForOtherZmanim) {
		this.useAstronomicalChatzosForOtherZmanim = useAstronomicalChatzosForOtherZmanim;
	}

	/**
	 * The zenith of 16.1&deg; below geometric zenith (90&deg;). This calculation is used for determining <em>alos</em>
	 * (dawn) and <em>tzais</em> (nightfall) in some opinions. It is based on the calculation that the time between dawn
	 * and sunrise (and sunset to nightfall) is 72 minutes, the time that is takes to walk 4 <em>mil</em> at 18 minutes
	 * a mil (<em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others). The sun's position at
	 * 72 minutes before {@link #getSunrise sunrise} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a> is
	 * 16.1&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
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
	 * minutes after {@link #getSunset sunset} in Jerusalem <a href=
	 * "https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox / equilux</a>, which
	 * is 8.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. The <em><a href=
	 * "https://www.worldcat.org/oclc/29283612">Ohr Meir</a></em> considers this the time that 3 small stars are visible,
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
	 * This method will return {@link #getSeaLevelSunrise() sea level sunrise} if {@link #isUseElevation()} is false (the
	 * default), or elevation adjusted {@link AstronomicalCalendar#getSunrise()} if it is true. This allows relevant <em>zmanim</em>
	 * in this and extending classes (such as the {@link ComplexZmanimCalendar}) to automatically adjust to the elevation setting.
	 * 
	 * @return {@link #getSeaLevelSunrise()} if {@link #isUseElevation()} is false (the default), or elevation adjusted
	 *         {@link AstronomicalCalendar#getSunrise()} if it is true.
	 * @see com.kosherjava.zmanim.AstronomicalCalendar#getSunrise()
	 */
	protected Date getElevationAdjustedSunrise() {
		if(isUseElevation()) {
			return super.getSunrise();
		}
		return getSeaLevelSunrise();
	}
	
	/**
	 * This method will return {@link #getSeaLevelSunrise() sea level sunrise} if {@link #isUseElevation()} is false (the default),
	 * or elevation adjusted {@link AstronomicalCalendar#getSunrise()} if it is true. This allows relevant <em>zmanim</em>
	 * in this and extending classes (such as the {@link ComplexZmanimCalendar}) to automatically adjust to the elevation setting.
	 * 
	 * @return {@link #getSeaLevelSunset()} if {@link #isUseElevation()} is false (the default), or elevation adjusted
	 *         {@link AstronomicalCalendar#getSunset()} if it is true.
	 * @see com.kosherjava.zmanim.AstronomicalCalendar#getSunset()
	 */
	protected Date getElevationAdjustedSunset() {
		if(isUseElevation()) {
			return super.getSunset();
		}
		return getSeaLevelSunset();
	}

	/**
	 * A method that returns <em>tzais</em> (nightfall) when the sun is {@link #ZENITH_8_POINT_5 8.5&deg;} below the
	 * {@link #GEOMETRIC_ZENITH geometric horizon} (90&deg;) after {@link #getSunset sunset}, a time that Rabbi Meir
	 * Posen in his the <em><a href="https://www.worldcat.org/oclc/29283612">Ohr Meir</a></em> calculated that 3 small
	 * stars are visible, which is later than the required 3 medium stars. See the {@link #ZENITH_8_POINT_5} constant.
	 * 
	 * @see #ZENITH_8_POINT_5
	 * 
	 * @return The <code>Date</code> of nightfall. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a <code>null</code> will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
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
	 * on the <a href="https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">around the equinox /
	 * equilux</a> is 16.1&deg; below {@link #GEOMETRIC_ZENITH}.
	 * 
	 * @see #ZENITH_16_POINT_1
	 * @see ComplexZmanimCalendar#getAlos16Point1Degrees()
	 * 
	 * @return The <code>Date</code> of dawn. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a <code>null</code> will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getAlosHashachar() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 72 minutes before {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting). This time
	 * is based on the time to walk the distance of 4 <em>Mil</em> at 18 minutes a <em>Mil</em>. The 72-minute time (but
	 * not the concept of fixed minutes) is based on the opinion that the time of the <em>Neshef</em> (twilight between
	 * dawn and sunrise) does not vary by the time of year or location but depends on the time it takes to walk the
	 * distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a <code>null</code> will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos72() {
		return getTimeOffset(getElevationAdjustedSunrise(), -72 * MINUTE_MILLIS);
	}

	/**
	 * This method returns {@link #getSunTransit() Astronomical <em>chatzos</em>} if the
	 * {@link com.kosherjava.zmanim.util.AstronomicalCalculator calculator} class used supports it and
	 * {@link #isUseAstronomicalChatzos() isUseAstronomicalChatzos()} is set to <em>true</em> or the {@link #getChatzosAsHalfDay()
	 * halfway point between sunrise and sunset} if it does not support it, or it is not configured to use it. There are currently
	 * two {@link com.kosherjava.zmanim.util.AstronomicalCalculator calculators} available in the API, the default {@link
	 * com.kosherjava.zmanim.util.NOAACalculator NOAA calculator} and the {@link com.kosherjava.zmanim.util.SunTimesCalculator USNO
	 * calculator}. The USNO calculator calculates <em>chatzos</em> as halfway between sunrise and sunset (identical to six <em>shaos
	 * zmaniyos</em> after sunrise), while the NOAACalculator calculates it more accurately as {@link #getSunTransit() astronomical
	 * <em>chatzos</em>}. See <a href="https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of <em>Chatzos</em></a>
	 * for a detailed explanation of the ways to calculate <em>Chatzos</em>. Since half-day <em>chatzos</em> can be <code>null</code> in
	 * the Arctic on a day when either sunrise or sunset did not happen and astronomical <em>chatzos</em> can be calculated even in the
	 * Arctic, if half-day <em>chatzos</em> calculates as <code>null</code> and astronomical <em>chatzos</em> is supported by the
	 * calculator, astronomical <em>chatzos</em> will be returned to avoid returning a <code>null</code>.
	 * 
	 * @see AstronomicalCalendar#getSunTransit()
	 * @see #getChatzosAsHalfDay()
	 * @see #isUseAstronomicalChatzos()
	 * @see #setUseAstronomicalChatzos(boolean)
	 * @return the <code>Date</code> of <em>chatzos</em>. If the calculation can't be computed such as in the Arctic Circle
	 *         where there is at least one day where the sun does not rise, and one where it does not set, and the calculator does not
	 *         support astronomical calculations (that will never report a <code>null</code>) a <code>null</code> will be returned.
	 *         See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getChatzos() {
		if(useAstronomicalChatzos) {
			return getSunTransit(); // can be null of the calculator does not support astronomical chatzos
		} else {
			Date halfDayChatzos = getChatzosAsHalfDay();
			if(halfDayChatzos == null) {
				return getSunTransit(); // can be null if the calculator does not support astronomical chatzos
			} else {
				return halfDayChatzos;
			}
		}
	}
	
	/**
	 * Returns <em>chatzos</em> calculated as halfway between sunrise and sunset. Many are of the opinion that
	 * <em>chatzos</em> is calculated as the midpoint between {@link #getSeaLevelSunrise sea level sunrise} and
	 * {@link #getSeaLevelSunset sea level sunset}, despite it not being the most accurate way to calculate it. A day
	 * starting at <em>alos</em> and ending at <em>tzais</em> using the same time or degree offset will also return
	 * the same time. In reality due to lengthening or shortening of day, this is not necessarily the exact midpoint of
	 * the day, but it is very close. This method allows you to use the NOAACalculator and still calculate <em>chatzos
	 * </em> as six <em>shaos zmaniyos</em> after sunrise. There are currently two {@link
	 * com.kosherjava.zmanim.util.AstronomicalCalculator calculators} available in the API, the {@link
	 * com.kosherjava.zmanim.util.NOAACalculator} and the {@link com.kosherjava.zmanim.util.SunTimesCalculator}.
	 * The SunTimesCalculator calculates <em>chatzos</em> as halfway between sunrise and sunset (and of six <em>shaos
	 * zmaniyos</em>), while the NOAACalculator calculates it as astronomical <em>chatzos</em> that is slightly more
	 * accurate. This method allows you to use the NOAACalculator and still calculate <em>chatzos</em> as six <em>shaos
	 * zmaniyos</em> after sunrise. See <a href="https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition
	 * of <em>Chatzos</em></a> for a detailed explanation of the ways to calculate <em>Chatzos</em>.
	 *
	 * @see com.kosherjava.zmanim.util.NOAACalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see com.kosherjava.zmanim.util.SunTimesCalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see AstronomicalCalendar#getSunTransit(Date, Date)
	 * @see #getChatzos()
	 * @see #getSunTransit()
	 * @see #isUseAstronomicalChatzos()
	 * 
	 * @return the <code>Date</code> of the latest <em>chatzos</em>. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where
	 *         it does not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getChatzosAsHalfDay() {
		return getSunTransit(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * A generic method for calculating the latest <em>zman krias shema</em> (time to recite shema in the morning) that is 3 *
	 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and end of the day passed
	 * to this method. The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal
	 * hours), and the latest <em>zman krias shema</em> is calculated as 3 of those <em>shaos zmaniyos</em> after the beginning of
	 * the day. If {@link #isUseAstronomicalChatzosForOtherZmanim()} is <code>true</code>, the 3 <em>shaos zmaniyos</em> will be
	 * based on 1/6 of the time between sunrise and {@link #getSunTransit() astronomical <em>chatzos</em>}. As an example, passing
	 * {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea level sunrise} and {@link
	 * #getSeaLevelSunset() sea level sunset} to this method (or {@link #getElevationAdjustedSunrise()} and {@link
	 * #getElevationAdjustedSunset()} that is driven off the {@link #isUseElevation()} setting) will return <em>sof zman krias
	 * shema</em> according to the opinion of the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. In cases
	 * where the start and end dates are not synchronous such as in {@link ComplexZmanimCalendar
	 * #getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees()} <code>false</code> should be passed to the synchronous parameter
	 * to ensure that {@link #isUseAstronomicalChatzosForOtherZmanim()} will not be used.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>zman krias shema</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @see #isUseAstronomicalChatzosForOtherZmanim()
	 * @return the <code>Date</code> of the latest <em>zman shema</em> based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a <code>null</code> will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanShma(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(startOfDay, getChatzos(), 3);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 3);
		}
	}
	
	/**
	 * A generic method for calculating the latest <em>zman krias shema</em> that calls {@link #getSofZmanShma(Date, Date, boolean)}
	 * passing <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are 
	 * synchronous. Passing true when they are not synchronous is too much of a risk. See information on that method for more details.
	 * @param startOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>zman krias shema</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @return the <code>Date</code> of the latest <em>zman shema</em> based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a <code>null</code> will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanShma(Date, Date, boolean)
	 */
	public Date getSofZmanShma(Date startOfDay, Date endOfDay) {
		return getSofZmanShma(startOfDay, endOfDay, false);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite shema in the morning) that is 3 *
	 * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em>} (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. 
	 *  The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level
	 *  sunset} or from {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 *  setting).
	 * 
	 * @see #getSofZmanShma(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #isUseElevation()
	 * @see ComplexZmanimCalendar#getSofZmanShmaBaalHatanya()
	 * @return the <code>Date</code> of the latest <em>zman shema</em> according to the GRA. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a <code>null</code> will be returned. See the detailed explanation on top
	 *         of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanShmaGRA() {
		return getSofZmanShma(getElevationAdjustedSunrise(), getElevationAdjustedSunset(), true);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite shema in the morning) that is 3 *
	 * {@link #getShaahZmanisMGA() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos72()}, according to the
	 * <a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a>. The day is calculated
	 * from 72 minutes before {@link #getSeaLevelSunrise() sea level sunrise} to 72 minutes after {@link
	 * #getSeaLevelSunset() sea level sunset} or from 72 minutes before {@link #getSunrise() sunrise} to {@link #getSunset()
	 * sunset} (depending on the {@link #isUseElevation()} setting).
	 * 
	 * @return the <code>Date</code> of the latest <em>zman shema</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanShma(Date, Date)
	 * @see ComplexZmanimCalendar#getShaahZmanis72Minutes()
	 * @see ComplexZmanimCalendar#getAlos72()
	 * @see ComplexZmanimCalendar#getSofZmanShmaMGA72Minutes() that 
	 */
	public Date getSofZmanShmaMGA() {
		return getSofZmanShma(getAlos72(), getTzais72(), true);
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of <em>Rabbeinu Tam</em> that
	 * <em>tzais hakochavim</em> is calculated as 72 minutes, the time it takes to walk 4 <em>Mil</em> at 18 minutes
	 * a <em>Mil</em>. According to the <a href="https://en.wikipedia.org/wiki/Samuel_Loew">Machtzis Hashekel</a> in
	 * Orach Chaim 235:3, the <a href="https://en.wikipedia.org/wiki/Joseph_ben_Meir_Teomim">Pri Megadim</a> in Orach
	 * Chaim 261:2 (see the Biur Halacha) and others (see Hazmanim Bahalacha 17:3 and 17:5) the 72 minutes are standard
	 * clock minutes any time of the year in any location. Depending on the {@link #isUseElevation()} setting, a 
	 * 72-minute offset from  either {@link #getSunset() sunset} or {@link #getSeaLevelSunset() sea level sunset} is used.
	 * 
	 * @see ComplexZmanimCalendar#getTzais16Point1Degrees()
	 * @return the <code>Date</code> representing 72 minutes after sunset. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a <code>null</code> will be returned See detailed explanation on top of the
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
	 *         least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
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
	 * "https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. This method's synchronous parameter indicates if the start
	 * and end of day for the calculation are synchronous, having the same offset. This is typically the case, but some
	 * <em>zmanim</em> calculations are based on a start and end at different offsets from the real start and end of the day,
	 * such as starting the day at <em>alos</em> and an ending it at <em>tzais geonim</em> or some other variant. If the day
	 * is not synchronous a {@link #getHalfDayBasedZman(Date, Date, double) half-day based calculations} will be bypassed.
	 * It would be illogical to use a half-day based calculation that start/end at <em>chatzos</em> when the two "halves" of
	 * the day are not equal, and the halfway point between them is not at <em>chatzos</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman tfilah</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>zman tfilah</em>. This can be sunset or any <em>tzais</em> passed
	 *            to this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @return the <code>Date</code> of the latest <em>zman tfilah</em> based on the start and end of day times passed
	 *         to this method. If the calculation can't be computed such as in the Arctic Circle where there is at least
	 *         one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanTfila(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(startOfDay, getChatzos(), 4);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 4);
		}
	}
	
	/**
	 * A generic method for calculating the latest <em>zman tfila</em> that calls {@link #getSofZmanTfila(Date, Date, boolean)}
	 * passing <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are 
	 * synchronous. Passing true when they are not synchronous is too much of a risk. See information on that method for more details.
	 * @param startOfDay
	 *            the start of day for calculating <em>zman tfilah</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>zman tfilah</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @return the <code>Date</code> of the latest <em>zman tfilah</em> based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a <code>null</code> will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanShma(Date, Date, boolean)
	 */
	public Date getSofZmanTfila(Date startOfDay, Date endOfDay) {
		return getSofZmanTfila(startOfDay, endOfDay, false);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite shema in the morning) that is 4 *
	 * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em> }(solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. 
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level
	 * sunset} or from {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting).
	 * 
	 * @see #getSofZmanTfila(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see ComplexZmanimCalendar#getSofZmanTfilaBaalHatanya()
	 * @return the <code>Date</code> of the latest <em>zman tfilah</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanTfilaGRA() {
		return getSofZmanTfila(getElevationAdjustedSunrise(), getElevationAdjustedSunset(), true);
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite shema in the morning) that is 4 *
	 * {@link #getShaahZmanisMGA() <em>shaos zmaniyos</em>} (solar hours) after {@link #getAlos72()}, according to the
	 * <em><a href="https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a></em>. The day is calculated
	 * from 72 minutes before {@link #getSeaLevelSunrise() sea level sunrise} to 72 minutes after {@link
	 * #getSeaLevelSunset() sea level sunset} or from 72 minutes before {@link #getSunrise() sunrise} to {@link #getSunset()
	 * sunset} (depending on the {@link #isUseElevation()} setting).
	 * 
	 * @return the <code>Date</code> of the latest <em>zman tfila</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanTfila(Date, Date)
	 * @see #getShaahZmanisMGA()
	 * @see #getAlos72()
	 */
	public Date getSofZmanTfilaMGA() {
		return getSofZmanTfila(getAlos72(), getTzais72(), true);
	}

	/**
	 * A generic method for calculating <em>mincha gedola</em> (the earliest time to recite the <em>mincha</em> prayers) that
	 * is 6.5 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and end of the
	 * day passed to this method. The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em>
	 * (temporal hours), and <em>mincha gedola</em> is calculated as 6.5 of those <em>shaos zmaniyos</em> after the beginning
	 * of the day. As an example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link
	 * #getSeaLevelSunrise() sea level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link
	 * #isUseElevation()} elevation setting) to this method will return <em>mincha gedola</em> according to the opinion of the
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. Alternatively, this method uses {@link
	 * #isUseAstronomicalChatzosForOtherZmanim()} to control if the time is based on 6.5 <em>shaos zmaniyos</em> into the day
	 * mentioned above, or as half an hour <em>zmaniyos</em> based on the second half of the day after <em>chatzos</em> ({@link
	 * #getSunTransit() astronomical <em>chatzos</em>} if supported by the {@link AstronomicalCalculator calculator} and {@link
	 * #isUseAstronomicalChatzos() configured} or {@link #getChatzosAsHalfDay() <em>chatzos</em> as half a day} if not. This
	 * method's synchronous parameter indicates if the start and end of day for the calculation are synchronous, having the same
	 * offset. This is typically the case, but some <em>zmanim</em> calculations are based on a start and end at different offsets
	 * from the real start and end of the day, such as starting the day at <em>alos</em> and an ending it at <em>tzais geonim</em>
	 * or some other variant. If the day is not synchronous a {@link #getHalfDayBasedZman(Date, Date, double) half-day based
	 * calculations} will be bypassed. It would be illogical to use a half-day based calculation that start/end at <em>chatzos</em>
	 * when the two "halves" of the day are not equal, and the halfway point between them is not at <em>chatzos</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha gedola</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>Mincha gedola</em>. This can be sunset or any <em>tzais</em> passed
	 *            to this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @return the <code>Date</code> of the time of <em>Mincha gedola</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSunTransit()
	 * @see #getChatzosAsHalfDay()
	 * @see #getChatzos()
	 * @see #isUseAstronomicalChatzos()
	 * @see #isUseAstronomicalChatzosForOtherZmanim()
	 */
	public Date getMinchaGedola(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(getChatzos(), endOfDay, 0.5);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 6.5);
		}
	}
	
	/**
	 * A generic method for calculating <em>mincha gedola</em> that calls {@link #getMinchaGedola(Date, Date, boolean)} passing
	 * <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are
	 * synchronous. Passing true when they are not synchronous is too much of a risk. See information on that method for more
	 * details.
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha gedola</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>Mincha gedola</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @return the <code>Date</code> of the latest <em>Mincha gedola</em> based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a <code>null</code> will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getMinchaGedola(Date, Date, boolean)
	 */
	public Date getMinchaGedola(Date startOfDay, Date endOfDay) {
		return getMinchaGedola(startOfDay, endOfDay, false);
	}

	/**
	 * This method returns the latest <em>mincha gedola</em>,the earliest time one can pray <em>mincha</em> that is 6.5 *
	 * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em>} (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. <em>Mincha gedola</em> is the earliest
	 * time one can pray <em>mincha</em>. The Ramba"m is of the opinion that it is better to delay <em>mincha</em> until
	 * {@link #getMinchaKetana() <em>mincha ketana</em>} while the <em>Ra"sh, Tur, GRA</em> and others are of the
	 * opinion that <em>mincha</em> can be prayed <em>lechatchila</em> starting at <em>mincha gedola</em>.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting).
	 * @todo Consider adjusting this to calculate the time as half an hour <em>zmaniyos</em> after either {@link
	 *         #getSunTransit() astronomical <em>chatzos</em>} or {@link #getChatzosAsHalfDay() <em>chatzos</em> as half a day}
	 *         for {@link AstronomicalCalculator calculators} that support it, based on {@link #isUseAstronomicalChatzos()}.
	 * 
	 * @see #getMinchaGedola(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaKetana()
	 * @see ComplexZmanimCalendar#getMinchaGedolaBaalHatanya()
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaGedola() {
		return getMinchaGedola(getElevationAdjustedSunrise(), getElevationAdjustedSunset(), true);
	}
	
	/**
	 * A generic method for calculating <em>samuch lemincha ketana</em>, / near <em>mincha ketana</em> time that is half
	 * an hour before {@link #getMinchaKetana(Date, Date)}  or 9 * <em>shaos zmaniyos</em> (temporal hours) after the
	 * start of the day, calculated using the start and end of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours), and
	 * <em>samuch lemincha ketana</em> is calculated as 9 of those <em>shaos zmaniyos</em> after the beginning of the day.
	 * For example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea
	 * level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation
	 * setting) to this method will return <em>samuch lemincha ketana</em> according to the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>samuch lemincha ketana</em>. This can be sunrise or any <em>alos</em>
	 *            passed to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>samuch lemincha ketana</em>. This can be sunset or any <em>tzais</em>
	 *            passed to this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @return the <code>Date</code> of the time of <em>Mincha ketana</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 *
	 * @see ComplexZmanimCalendar#getSamuchLeMinchaKetanaGRA()
	 * @see ComplexZmanimCalendar#getSamuchLeMinchaKetana16Point1Degrees()
	 * @see ComplexZmanimCalendar#getSamuchLeMinchaKetana72Minutes()
	 */
	public Date getSamuchLeMinchaKetana(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(getChatzos(), endOfDay, 3);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 9);
		}
	}
	
	/**
	 * A generic method for calculating <em>samuch lemincha ketana</em> that calls {@link #getSamuchLeMinchaKetana(Date, Date, boolean)}
	 * passing <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are 
	 * synchronous. Passing true when they are not synchronous is too much of a risk. See information on that method for more details.
	 * @param startOfDay
	 *            the start of day for calculating <em>samuch lemincha ketana</em>. This can be sunrise or any <em>alos</em>
	 *            passed to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>samuch lemincha ketana</em>. This can be sunset or any <em>tzais</em>
	 *            passed to this method.
	 * @return the <code>Date</code> of the time of <em>samuch lemincha ketana</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getSamuchLeMinchaKetana(Date, Date, boolean)
	 */
	public Date getSamuchLeMinchaKetana(Date startOfDay, Date endOfDay) {
		return getSamuchLeMinchaKetana(startOfDay, endOfDay, false);
	}

	/**
	 * A generic method for calculating <em>mincha ketana</em>, (the preferred time to recite the mincha prayers in
	 * the opinion of the <em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others) that is
	 * 9.5 * <em>shaos zmaniyos</em> (temporal hours) after the start of the day, calculated using the start and end
	 * of the day passed to this method.
	 * The time from the start of day to the end of day are divided into 12 <em>shaos zmaniyos</em> (temporal hours), and
	 * <em>mincha ketana</em> is calculated as 9.5 of those <em>shaos zmaniyos</em> after the beginning of the day. As an
	 * example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link #getSeaLevelSunrise() sea
	 * level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()}
	 * elevation setting) to this method will return <em>mincha ketana</em> according to the opinion of the
	 * <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. This method's synchronous parameter indicates if the start
	 * and end of day for the calculation are synchronous, having the same offset. This is typically the case, but some
	 * <em>zmanim</em> calculations are based on a start and end at different offsets from the real start and end of the day,
	 * such as starting the day at <em>alos</em> and an ending it at <em>tzais geonim</em> or some other variant. If the day
	 * is not synchronous a {@link #getHalfDayBasedZman(Date, Date, double) half-day based calculations} will be bypassed.
	 * It would be illogical to use a half-day based calculation that start/end at <em>chatzos</em> when the two "halves" of
	 * the day are not equal, and the halfway point between them is not at <em>chatzos</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha ketana</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>Mincha ketana</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @return the <code>Date</code> of the time of <em>Mincha ketana</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaKetana(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(getChatzos(), endOfDay, 3.5);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 9.5);
		}
	}
	
	/**
	 * A generic method for calculating <em>mincha ketana</em> that calls {@link #getMinchaKetana(Date, Date, boolean)} passing
	 * <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are synchronous.
	 * Passing true when they are not synchronous is too much of a risk. See information on that method for more details.
	 * @param startOfDay
	 *            the start of day for calculating <em>Mincha ketana</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating <em>Mincha ketana</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @return the <code>Date</code> of the time of <em>Mincha ketana</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code> will
	 *         be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getMinchaKetana(Date, Date, boolean)
	 */
	public Date getMinchaKetana(Date startOfDay, Date endOfDay) {
		return getMinchaKetana(startOfDay, endOfDay, false);
	}

	/**
	 * This method returns <em>mincha ketana</em>,the preferred earliest time to pray <em>mincha</em> in the
	 * opinion of the <em><a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a></em> and others, that is 9.5
	 * * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em>} (solar hours) after {@link #getSunrise() sunrise} or
	 * {@link #getSeaLevelSunrise() sea level sunrise} (depending on the {@link #isUseElevation()} setting), according
	 * to the <a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a>. For more information on this see the
	 * documentation on {@link #getMinchaGedola() <em>mincha gedola</em>}.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level
	 * sunset} or from {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * setting.
	 * 
	 * @see #getMinchaKetana(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaGedola()
	 * @see ComplexZmanimCalendar#getMinchaKetanaBaalHatanya()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getMinchaKetana() {
		return getMinchaKetana(getElevationAdjustedSunrise(), getElevationAdjustedSunset(), true);
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
	 * <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. This method's synchronous parameter indicates if
	 * the start and end of day for the calculation are synchronous, having the same offset. This is typically the case, but
	 * some <em>zmanim</em> calculations are based on a start and end at different offsets from the real start and end of the
	 * day, such as starting the day at <em>alos</em> and an ending it at <em>tzais geonim</em> or some other variant. If the
	 * day is not synchronous a {@link #getHalfDayBasedZman(Date, Date, double) half-day based calculations} will be bypassed.
	 * It would be illogical to use a half-day based calculation that start/end at <em>chatzos</em> when the two "halves" of
	 * the day are not equal, and the halfway point between them is not at <em>chatzos</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating plag. This can be sunrise or any <em>alos</em> passed to this method.
	 * @param endOfDay
	 *            the end of day for calculating plag. This can be sunset or any <em>tzais</em> passed to this method.
	 * @param synchronous
	 *            If the <em>zman</em> has a synchronous start and end of the day. If this is <code>false</code>, using a {@link
	 *            #isUseAstronomicalChatzosForOtherZmanim()} makes no sense and will be ignored even if set to true, since by
	 *            definition <em>chatzos</em> will not be the middle of the day for the <em>zman</em>.
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code>
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha(Date startOfDay, Date endOfDay, boolean synchronous) {
		if(isUseAstronomicalChatzosForOtherZmanim() && synchronous) {
			return getHalfDayBasedZman(getChatzos(), endOfDay, 4.75);
		} else {
			return getShaahZmanisBasedZman(startOfDay, endOfDay, 10.75);
		}
	}
	
	/**
	 * A generic method for calculating <em>plag hamincha</em> that calls {@link #getPlagHamincha(Date, Date, boolean)} passing
	 * <code>false</code> to the synchronous parameter since there is no way to know if the start and end of the day are synchronous.
	 * Passing true when they are not synchronous is too much of a risk. See information on that method for more details.
	 * @param startOfDay
	 *            the start of day for calculating plag. This can be sunrise or any <em>alos</em> passed to this method.
	 * @param endOfDay
	 *            the end of day for calculating plag. This can be sunset or any <em>tzais</em> passed to this method.
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em> based on the start and end of day times
	 *         passed to this method. If the calculation can't be computed such as in the Arctic Circle where there is
	 *         at least one day a year where the sun does not rise, and one where it does not set, a <code>null</code>
	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getPlagHamincha(Date, Date, boolean)
	 */
	public Date getPlagHamincha(Date startOfDay, Date endOfDay) {
		return getPlagHamincha(startOfDay, endOfDay, false);
	}

	/**
	 * This method returns <em>plag hamincha</em>, that is 10.75 * {@link #getShaahZmanisGra() <em>shaos zmaniyos</em>}
	 * (solar hours) after {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise() sea level sunrise} (depending on
	 * the {@link #isUseElevation()} setting), according to the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon"
	 * >GRA</a></em>. Plag hamincha is the earliest time that <em>Shabbos</em> can be started.
	 * The day is calculated from {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level
	 * sunset} or {@link #getSunrise() sunrise} to {@link #getSunset() sunset} (depending on the {@link #isUseElevation()}
	 * 
	 * @see #getPlagHamincha(Date, Date, boolean)
	 * @see #getPlagHamincha(Date, Date)
	 * @see ComplexZmanimCalendar#getPlagHaminchaBaalHatanya()
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a <code>null</code> will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha() {
		return getPlagHamincha(getElevationAdjustedSunrise(), getElevationAdjustedSunset(), true);
	}

	/**
	 * A method that returns a <em>shaah zmanis</em> ({@link #getTemporalHour(Date, Date) temporal hour}) according to
	 * the opinion of the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>. This calculation divides
	 * the day based on the opinion of the <em>GRA</em> that the day runs from {@link #getSeaLevelSunrise() sea
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
	 * "https://en.wikipedia.org/wiki/Avraham_Gombinern">Magen Avraham (MGA)</a></em> based on 72 minutes <em>alos</em>
	 * and <em>tzais</em>. This calculation divides the day that runs from dawn to dusk (for <em>sof zman krias shema</em> and
	 * <em>tfila</em>). Dawn for this calculation is 72 minutes before {@link #getSunrise() sunrise} or {@link #getSeaLevelSunrise()
	 * sea level sunrise} (depending on the {@link #isUseElevation()} elevation setting) and dusk is 72 minutes after {@link
	 * #getSunset sunset} or {@link #getSeaLevelSunset() sea level sunset} (depending on the {@link #isUseElevation()} elevation
	 * setting). This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. Alternate methods of calculating
	 * a <em>shaah zmanis</em> according to the Magen Avraham (MGA) are available in the subclass {@link ComplexZmanimCalendar}.
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
	 * calendars use 15 minutes, while the custom in Jerusalem is to use a 40-minute offset. Please check the local custom
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
	 * minutes, while the custom in Jerusalem is to use a 40-minute offset.
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
	 * {@link #isUseElevation()} settings. The {@link JewishCalendar#getInIsrael()} will be set by the inIsrael parameter.
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

	/**
	 * A generic utility method for calculating any <em>shaah zmanis</em> (temporal hour) based <em>zman</em> with the
	 * day defined as the start and end of day (or night) and the number of <em>shaos zmaniyos</em> passed to the
	 * method. This simplifies the code in other methods such as {@link #getPlagHamincha(Date, Date)} and cuts down on
	 * code replication. As an example, passing {@link #getSunrise() sunrise} and {@link #getSunset sunset} or {@link
	 * #getSeaLevelSunrise() sea level sunrise} and {@link #getSeaLevelSunset() sea level sunset} (depending on the
	 * {@link #isUseElevation()} elevation setting) and 10.75 hours to this method will return <em>plag mincha</em>
	 * according to the opinion of the <em><a href="https://en.wikipedia.org/wiki/Vilna_Gaon">GRA</a></em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating the <em>zman</em>. This can be sunrise or any <em>alos</em> passed
	 *            to this method.
	 * @param endOfDay
	 *            the end of day for calculating the <em>zman</em>. This can be sunset or any <em>tzais</em> passed to
	 *            this method.
	 * @param hours
	 *            the number of <em>shaos zmaniyos</em> (temporal hours) to offset from the start of day
	 * @return the <code>Date</code> of the time of <em>zman</em> with the <em>shaos zmaniyos</em> (temporal hours)
	 *         in the day offset from the start of day passed to this method. If the calculation can't be computed such
	 *         as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a <code>null</code> will be  returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getShaahZmanisBasedZman(Date startOfDay, Date endOfDay, double hours) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * hours);
	}
	
	/**
	 * A utility method that returns the percentage of a <em>shaah zmanis</em> after sunset (or before sunrise) for a given degree
	 * offset. For the <a href="https://kosherjava.com/2022/01/12/equinox-vs-equilux-zmanim-calculations/">equilux</a> where there
	 * is a 720-minute day, passing 16.1&deg; for the location of Jerusalem will return about 1.2. This will work for any location
	 * or date, but will typically only be of interest at the equinox/equilux to calculate the percentage of a <em>shaah zmanis</em>
	 * for those who want to use the <a href="https://en.wikipedia.org/wiki/Abraham_Cohen_Pimentel">Minchas Cohen</a> in Ma'amar 2:4
	 * and the <a href="https://en.wikipedia.org/wiki/Hezekiah_da_Silva">Pri Chadash</a> who calculate <em>tzais</em> as a percentage
	 * of the day after sunset. While the Minchas Cohen only applies this to 72 minutes or a 1/10 of the day around the world (based
	 * on the equinox / equilux in Israel, this method allows calculations for any degrees level for any location.
	 * 
	 * @param degrees
	 *            the number of degrees below the horizon after sunset.
	 * @param sunset
	 *            if <code>true</code> the calculation should be degrees after sunset, or if <code>false</code>, degrees before sunrise.
	 * @return the <code>double</code> percentage of a <em>sha'ah zmanis</em> for a given set of degrees below the astronomical horizon
	 *         for the current calendar.  If the calculation can't be computed a {@link Double#MIN_VALUE} will be returned. See detailed
	 *         explanation on top of the page.
	 */
	public double getPercentOfShaahZmanisFromDegrees(double degrees, boolean sunset) {
		Date seaLevelSunrise = getSeaLevelSunrise();
		Date seaLevelSunset = getSeaLevelSunset();
		Date twilight = null;
		if(sunset) {
			twilight = getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + degrees);
		} else {
			twilight = getSunriseOffsetByDegrees(GEOMETRIC_ZENITH + degrees);
		}
		if(seaLevelSunrise == null || seaLevelSunset == null || twilight == null) {
			return Double.MIN_VALUE;
		}
		double shaahZmanis = (seaLevelSunset.getTime() - seaLevelSunrise.getTime()) / 12.0;
		long riseSetToTwilight;
		if(sunset) {
			riseSetToTwilight = twilight.getTime() - seaLevelSunset.getTime();
		} else {
			riseSetToTwilight = seaLevelSunrise.getTime() - twilight.getTime();
		}
		return riseSetToTwilight / shaahZmanis;
	}
	
	/**
	 * A utility method to calculate <em>zmanim</em> based on <a href="https://en.wikipedia.org/wiki/Moshe_Feinstein">Rav Moshe
	 * Feinstein</a> and others as calculated in <a href="https://en.wikipedia.org/wiki/Mesivtha_Tifereth_Jerusalem">MTJ</a>, <a href=
	 * "https://en.wikipedia.org/wiki/Mesivtha_Tifereth_Jerusalem">Yeshiva of Staten Island</a>, and Camp Yeshiva
	 * of Staten Island and other calendars. The day is split in two, from <em>alos</em> / sunrise to <em>chatzos</em>, and the
	 * second half of the day, from <em>chatzos</em> to sunset / <em>tzais</em>. Morning based times are calculated. based on the first
	 * 6 hours of the day, and afternoon times based on the second half of the day. As an example, passing 0.5, a start of
	 * <em>chatzos</em> and an end of day as sunset will return the time of <em>mincha gedola</em> GRA as half an hour <em>zmanis</em>
	 * based on the second half of the day. Some <em>zmanim</em> calculations can be based on subtracting <em>shaos zmaniyos</em>
	 * from the end of the day, and that is supported by passing a negative hour to this method.
	 * 
	 * @param startOfHalfDay
	 *            The start of the half day. This would be <em>alos</em> or sunrise for morning based times such as <em>sof zman krias
	 *            shema</em> and <em>chatzos</em> for afternoon based times such as <em>mincha gedola</em>.
	 * @param endOfHalfDay
	 *            The end of the half day. This would be <em>chatzos</em> for morning based times  such as <em>sof zman krias shema</em>
	 *            and sunset or <em>tzais</em> for afternoon based times such as <em>mincha gedola</em>.
	 * @param hours
	 *            The number of <em>shaos zmaniyos</em> (hours) to offset the beginning of the first or second half of the day. For example,
	 *            3 for <em>sof zman Shma</em>, 0.5 for <em>mincha gedola</em> (half an hour after <em>chatzos</em>) and 4.75 for <em>plag
	 *            hamincha</em>. If the number of hours is negative, it will subtract the number of <em>shaos zmaniyos</em> from the end
	 *            of the day.
	 * 
	 * @return the <code>Date</code> of <em>zman</em> based on calculation of the first or second half of the day. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where the
	 *         sun does not rise, and one where it does not set, a <code>null</code> will be returned. See detailed explanation
	 *         on top of the {@link AstronomicalCalendar} documentation.
	 *
	 * @see ComplexZmanimCalendar#getFixedLocalChatzos()
	 */
	public Date getHalfDayBasedZman(Date startOfHalfDay, Date endOfHalfDay, double hours) {
		if (startOfHalfDay == null || endOfHalfDay == null) {
			return null;
		}
		long shaahZmanis = getHalfDayBasedShaahZmanis(startOfHalfDay, endOfHalfDay);
		if(shaahZmanis == Long.MIN_VALUE) { //defensive, should not be needed
			return null;
		}
		if(hours >= 0) { // forward from start a day
			return getTimeOffset(startOfHalfDay, shaahZmanis * hours);
		} else { // subtract from end of day
			return getTimeOffset(endOfHalfDay, shaahZmanis * hours);
		}
	}
	
	/**
	 * A utility method to calculate the length of a <em>sha'ah zmanis</em> based on 1/6 of a 6-hour day.
	 * @param startOfHalfDay The start of the half-day. This would be <em>alos</em> or sunrise for the first half of the day,
	 *            or <em>chatzos</em> for the second half of the day.
	 * @param endOfHalfDay The end of the half-day. This would be <em>chatzos</em> for the first half of the day, or sunset or
	 *            <em>tzais</em> for the second half of the day.
	 * @return The <code>long</code> millisecond length of a <em>shaah zmanis</em> based on 1/6 of a half-day. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, {@link Long#MIN_VALUE} will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getHalfDayBasedZman(Date, Date, double)
	 * @see #isUseAstronomicalChatzosForOtherZmanim()
	 * @todo Consider adjusting various shaah zmanis times to use this.
	 */
	public long getHalfDayBasedShaahZmanis(Date startOfHalfDay, Date endOfHalfDay) {
		if (startOfHalfDay == null || endOfHalfDay == null) {
			return Long.MIN_VALUE;
		}
		return (endOfHalfDay.getTime() - startOfHalfDay.getTime()) / 6;
	}
}
