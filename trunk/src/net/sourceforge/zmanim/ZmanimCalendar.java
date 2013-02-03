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

import java.util.Date;

import net.sourceforge.zmanim.util.AstronomicalCalculator;
import net.sourceforge.zmanim.util.GeoLocation;

/**
 * The ZmanimCalendar is a specialized calendar that can calculate sunrise and sunset and Jewish <em>zmanim</em>
 * (religious times) for prayers and other Jewish religious duties. This class contains the main functionality of the
 * Zmanim library. For a much more extensive list of zmanim use the {@link ComplexZmanimCalendar} that extends this
 * class. See documentation for the {@link ComplexZmanimCalendar} and {@link AstronomicalCalendar} for simple examples
 * on using the API. <br/>
 * <b>Note:</b> It is important to read the technical notes on top of the {@link AstronomicalCalculator} documentation.
 * <h2>Disclaimer:</h2> I did my best to get accurate results but please do not rely on these zmanim for
 * <em>halacha lemaaseh</em>.
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2013
 */
public class ZmanimCalendar extends AstronomicalCalendar {

	/**
	 * The zenith of 16.1&deg; below geometric zenith (90&deg;). This calculation is used for determining <em>alos</em>
	 * (dawn) and <em>tzais</em> (nightfall) in some opinions. It is based on the calculation that the time between dawn
	 * and sunrise (and sunset to nightfall) is 72 minutes, the time that is takes to walk 4 <em>mil</em> at 18 minutes
	 * a mil (<em>Rambam</em> and others). The sun's position at 72 minutes before {@link #getSunrise sunrise} in
	 * Jerusalem on the equinox is 16.1&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}.
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
	 * solar hour is 60 minutes, which is 8.5&deg; below {@link #GEOMETRIC_ZENITH geometric zenith}. The Ohr Meir
	 * considers this the time that 3 small stars are visible, which is later than the required 3 medium stars.
	 * 
	 * @see #getTzais()
	 * @see ComplexZmanimCalendar#getTzaisGeonim8Point5Degrees()
	 */
	protected static final double ZENITH_8_POINT_5 = GEOMETRIC_ZENITH + 8.5;

	/**
	 * The default Shabbos candle lighting offset is 18 minutes. This can be changed via the
	 * {@link #setCandleLightingOffset(double)} and retrieved by the {@link #getCandleLightingOffset()}.
	 */
	private double candleLightingOffset = 18;

	/**
	 * A method that returns <em>tzais</em> (nightfall) when the sun is 8.5&deg; below the western geometric horizon
	 * (90&deg;) after {@link #getSunset sunset}. For information on the source of this calculation see
	 * {@link #ZENITH_8_POINT_5}.
	 * 
	 * @return The <code>Date</code> of nightfall. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 * @see #ZENITH_8_POINT_5
	 */
	public Date getTzais() {
		return this.getSunsetOffsetByDegrees(ZENITH_8_POINT_5);
	}

	/**
	 * Returns <em>alos</em> (dawn) based on the time when the sun is 16.1&deg; below the eastern
	 * {@link #GEOMETRIC_ZENITH geometric horizon} before {@link #getSunrise sunrise}. For more information the source
	 * of 16.1&deg; see {@link #ZENITH_16_POINT_1}.
	 * 
	 * @see #ZENITH_16_POINT_1
	 * @return The <code>Date</code> of dawn. If the calculation can't be computed such as northern and southern
	 *         locations even south of the Arctic Circle and north of the Antarctic Circle where the sun may not reach
	 *         low enough below the horizon for this calculation, a null will be returned. See detailed explanation on
	 *         top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getAlosHashachar() {
		return getSunriseOffsetByDegrees(ZENITH_16_POINT_1);
	}

	/**
	 * Method to return <em>alos</em> (dawn) calculated using 72 minutes before {@link #getSeaLevelSunrise() sea level
	 * sunrise} (no adjustment for elevation) based on the time to walk the distance of 4 <em>Mil</em> at 18 minutes a
	 * <em>Mil</em>. This is based on the opinion of most <em>Rishonim</em> who stated that the time of the
	 * <em>Neshef</em> (time between dawn and sunrise) does not vary by the time of year or location but purely depends
	 * on the time it takes to walk the distance of 4 <em>Mil</em>.
	 * 
	 * @return the <code>Date</code> representing the time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set,
	 *         a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getAlos72() {
		return getTimeOffset(getSeaLevelSunrise(), -72 * MINUTE_MILLIS);
	}

	/**
	 * This method returns <em>chatzos</em> (midday) following the opinion of the GRA that the day for Jewish halachic
	 * times start at {@link #getSeaLevelSunrise sea level sunrise} and ends at {@link #getSeaLevelSunset sea level
	 * sunset}. The returned value is identical to {@link #getSunTransit()}
	 * 
	 * @see AstronomicalCalendar#getSunTransit()
	 * @return the <code>Date</code> of chatzos. If the calculation can't be computed such as in the Arctic Circle where
	 *         there is at least one day where the sun does not rise, and one where it does not set, a null will be
	 *         returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getChatzos() {
		return getSunTransit();
	}

	/**
	 * This is a generic method for calculating the latest <em>zman krias shema</em> (time to recite Shema in the
	 * morning) based on the start and end of day passed to the method. The time from the start of day to the end of day
	 * are divided into 12 shaos zmaniyos (temporal hours), and <em>zman krias shema</em> is calculated as 3 shaos
	 * zmaniyos from the beginning of the day. As an example, passing {@link #getSeaLevelSunrise() sea level sunrise}
	 * and {@link #getSeaLevelSunset sea level sunset} to this method will return <em>zman krias shema</em> according to
	 * the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>zman krias shema</em>. This can be sunset or any tzais passed to
	 *            this method.
	 * @return the <code>Date</code> of the latest zman shema based on the start and end of day times passed to this
	 *         method. If the calculation can't be computed such as in the Arctic Circle where there is at least one day
	 *         a year where the sun does not rise, and one where it does not set, a null will be returned. See detailed
	 *         explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanShma(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 3);
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning). This time is 3
	 * <em>{@link #getShaahZmanisGra() shaos zmaniyos}</em> (solar hours) after {@link #getSeaLevelSunrise() sea level
	 * sunrise} based on the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em> that the day is calculated from
	 * sunrise to sunset. This returns the time 3 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea
	 * level sunrise}.
	 * 
	 * @see #getSofZmanShma(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @return the <code>Date</code> of the latest zman shema according to the GRA and Baal Hatanya. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does
	 *         not rise, and one where it does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanShmaGRA() {
		return getSofZmanShma(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * This method returns the latest <em>zman krias shema</em> (time to recite shema in the morning) in the opinion of
	 * the <em>MGA</em> based on <em>alos</em> being 72 minutes before {@link #getSunrise() sunrise}. This time is 3
	 * <em>shaos zmaniyos</em> (solar hours) after dawn based on the opinion of the <em>MGA</em> that the day is
	 * calculated from a dawn of 72 minutes before sunrise to nightfall of 72 minutes after sunset. This returns the
	 * time of 3 * <em>shaos zmaniyos</em> after dawn.
	 * 
	 * @return the <code>Date</code> of the latest <em>zman shema</em>. If the calculation can't be computed such as in
	 *         the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 * @see #getSofZmanShma(Date, Date)
	 * @see ComplexZmanimCalendar#getShaahZmanis72Minutes()
	 * @see ComplexZmanimCalendar#getAlos72()
	 * @see ComplexZmanimCalendar#getSofZmanShmaMGA72Minutes()
	 */
	public Date getSofZmanShmaMGA() {
		return getSofZmanShma(getAlos72(), getTzais72());
	}

	/**
	 * This method returns the <em>tzais</em> (nightfall) based on the opinion of the <em>Rambam</em> and
	 * <em>Rabainu Tam</em> that <em>tzais</em> is calculated as 72 minutes, the time it takes to walk 4 <em>Mil</em> at
	 * 18 minutes a <em>Mil</em>. Even for locations above sea level, this is calculated at sea level, since the
	 * darkness level is not affected by elevation.
	 * 
	 * @return the <code>Date</code> representing 72 minutes after sea level sunset. If the calculation can't be
	 *         computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise,
	 *         and one where it does not set, a null will be returned See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getTzais72() {
		return getTimeOffset(getSeaLevelSunset(), 72 * MINUTE_MILLIS);
	}

	/**
	 * A method to return candle lighting time. This is calculated as {@link #getCandleLightingOffset()} minutes before
	 * {@link #getSeaLevelSunset() sea level sunset}. This will return the time for any day of the week, since it can be
	 * used to calculate candle lighting time for <em>yom tov</em> (mid-week holidays) as well. To calculate the offset
	 * of non-sea level sunset, pass the elevation adjusted sunset to {@link #getTimeOffset(Date, long)}.
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
	 * This is a generic method for calculating the latest <em>zman tefilah<em> (time to recite the morning prayers)
	 * based on the start and end of day passed to the method. The time from the start of day to the end of day
	 * are divided into 12 shaos zmaniyos (temporal hours), and <em>zman krias shema</em> is calculated as 4 shaos
	 * zmaniyos from the beginning of the day. As an example, passing {@link #getSeaLevelSunrise() sea level sunrise}
	 * and {@link #getSeaLevelSunset sea level sunset} to this method will return <em>zman tefilah<em> according to
	 * the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>.
	 * 
	 * @param startOfDay
	 *            the start of day for calculating <em>zman tefilah<em>. This can be sunrise or any alos passed to
	 *            this method.
	 * @param endOfDay
	 *            the start of day for calculating <em>zman tefilah<em>. This can be sunset or any tzais passed to this
	 *            method.
	 * @return the <code>Date</code> of the latest <em>zman tefilah<em> based on the start and end of day times passed
	 *         to this method. If the calculation can't be computed such as in the Arctic Circle where there is at least
	 *         one day a year where the sun does not rise, and one where it does not set, a null will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 */
	public Date getSofZmanTfila(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 4);
	}

	/**
	 * This method returns the latest <em>zman tefilah<em> (time to recite the morning prayers). This time is 4
	 * hours into the day based on the opinion of the <em>GRA</em> and the </em>Baal Hatanya</em> that the day is
	 * calculated from sunrise to sunset. This returns the time 4 * {@link #getShaahZmanisGra()} after
	 * {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see #getSofZmanTfila(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @return the <code>Date</code> of the latest zman tefilah. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getSofZmanTfilaGRA() {
		return getSofZmanTfila(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * This method returns the latest <em>zman tfila</em> (time to recite the morning prayers) in the opinion of the
	 * <em>MGA</em> based on <em>alos</em> being {@link #getAlos72() 72} minutes before {@link #getSunrise() sunrise}.
	 * This time is 4 <em>{@link #getShaahZmanisMGA() shaos zmaniyos}</em> (temporal hours) after {@link #getAlos72()
	 * dawn} based on the opinion of the <em>MGA</em> that the day is calculated from a {@link #getAlos72() dawn} of 72
	 * minutes before sunrise to {@link #getTzais72() nightfall} of 72 minutes after sunset. This returns the time of 4
	 * * {@link #getShaahZmanisMGA()} after {@link #getAlos72() dawn}.
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
	 * This is a generic method for calulating <em>mincha gedola</em>. <em>Mincha gedola</em> is the earliest time one
	 * can pray mincha (6.5 hours from the begining of the day), based on the start and end of day passed to the method.
	 * The time from the start of day to the end of day are divided into 12 shaos zmaniyos, and <em>Mincha gedola</em>
	 * is calculated as 6.5 hours from the beginning of the day. As an example, passing {@link #getSeaLevelSunrise() sea
	 * level sunrise} and {@link #getSeaLevelSunset sea level sunset} to this method will return <em>Mincha gedola</em>
	 * according to the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>.
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
	 * This method returns the time of <em>mincha gedola</em>. <em>Mincha gedola</em> is the earliest time one can pray
	 * mincha. The Ramba"m is of the opinion that it is better to delay <em>mincha</em> until
	 * <em>{@link #getMinchaKetana() mincha ketana}</em> while the <em>Ra"sh,
	 * Tur, GRA</em> and others are of the opinion that <em>mincha</em> can be prayed <em>lechatchila</em> starting at
	 * <em>mincha gedola</em>. This is calculated as 6.5 {@link #getShaahZmanisGra() sea level solar hours} after
	 * {@link #getSeaLevelSunrise() sea level sunrise}. This calculation is based on the opinion of the <em>GRA</em> and
	 * the <em>Baal Hatanya</em> that the day is calculated from sunrise to sunset. This returns the time 6.5 *
	 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see #getMinchaGedola(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaKetana()
	 * @return the <code>Date</code> of the time of mincha gedola. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaGedola() {
		return getMinchaGedola(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * This is a generic method for calulating <em>mincha ketana</em>. <em>Mincha ketana</em> is the preferred time one
	 * can pray can pray <em>mincha</em> in the opinion of the Rambam and others (9.5 hours from the begining of the
	 * day), based on the start and end of day passed to the method. The time from the start of day to the end of day
	 * are divided into 12 shaos zmaniyos, and <em>mincha ketana</em> is calculated as 9.5 hours from the beginning of
	 * the day. As an example, passing {@link #getSeaLevelSunrise() sea level sunrise} and {@link #getSeaLevelSunset sea
	 * level sunset} to this method will return <em>Mincha ketana</em> according to the opinion of the <em>GRA</em> and
	 * the <em>Baal Hatanya</em>.
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
	 */
	public Date getMinchaKetana(Date startOfDay, Date endOfDay) {
		long shaahZmanis = getTemporalHour(startOfDay, endOfDay);
		return getTimeOffset(startOfDay, shaahZmanis * 9.5);
	}

	/**
	 * This method returns the time of <em>mincha ketana</em>. This is the preferred earliest time to pray
	 * <em>mincha</em> in the opinion of the Rambam and others. For more information on this see the documentation on
	 * <em>{@link #getMinchaGedola() mincha gedola}</em>. This is calculated as 9.5 {@link #getShaahZmanisGra() sea
	 * level solar hours} after {@link #getSeaLevelSunrise() sea level sunrise}. This calculation is calculated based on
	 * the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em> that the day is calculated from sunrise to sunset.
	 * This returns the time 9.5 * {@link #getShaahZmanisGra()} after {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see #getMinchaKetana(Date, Date)
	 * @see #getShaahZmanisGra()
	 * @see #getMinchaGedola()
	 * @return the <code>Date</code> of the time of mincha ketana. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, a null will be returned. See detailed explanation on top of the {@link AstronomicalCalendar}
	 *         documentation.
	 */
	public Date getMinchaKetana() {
		return getMinchaKetana(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * This is a generic method for calulating <em>plag hamincha</em> (1.25 hours before the end of the day) based on
	 * the start and end of day passed to the method. The time from the start of day to the end of day are divided into
	 * 12 shaos zmaniyos, and plag is calculated as 10.75 hours from the beginning of the day. As an example, passing
	 * {@link #getSeaLevelSunrise() sea level sunrise} and {@link #getSeaLevelSunset sea level sunset} to this method
	 * will return Plag Hamincha according to the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>.
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
	 * This method returns the time of <em>plag hamincha</em>. This is calculated as 10.75 hours after sunrise. This
	 * calculation is based on the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em> that the day is calculated
	 * from sunrise to sunset. This returns the time 10.75 * {@link #getShaahZmanisGra()} after
	 * {@link #getSeaLevelSunrise() sea level sunrise}.
	 * 
	 * @see #getPlagHamincha(Date, Date)
	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
	 *         in the Arctic Circle where there is at least one day a year where the sun does not rise, and one where it
	 *         does not set, a null will be returned. See detailed explanation on top of the
	 *         {@link AstronomicalCalendar} documentation.
	 */
	public Date getPlagHamincha() {
		return getPlagHamincha(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * A method that returns a <em>shaah zmanis</em> ( {@link #getTemporalHour(Date, Date) temporal hour}) according to
	 * the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>. This calculation divides the day based on the
	 * opinion of the <em>GRA</em> and the <em>Baal Hatanya</em> that the day runs from {@link #getSeaLevelSunrise()
	 * sunrise} to {@link #getSeaLevelSunrise sunset}. The calculations are based on a day from
	 * {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset() sea level sunset}. The day is
	 * split into 12 equal parts with each one being a <em>shaah zmanis</em>. This method is similar to
	 * {@link #getTemporalHour}, but all calculations are based on a sealevel sunrise and sunset. An explanation and
	 * detailed sources for not using elevation for anything besides sunrise and sunset can be found in <a
	 * href="http://www.worldcat.org/oclc/659793988">Zmanim Kehilchasam (second edition published in 2007)</a> by Rabbi
	 * Dovid Yehuda Bursztyn chapter 2 (pages 186-187).
	 * 
	 * @return the <code>long</code> millisecond length of a <em>shaah zmanis</em> calculated from
	 *         {@link #getSeaLevelSunrise() sea level sunrise} to {@link #getSeaLevelSunset sea level sunset}. If the
	 *         calculation can't be computed such as in the Arctic Circle where there is at least one day a year where
	 *         the sun does not rise, and one where it does not set, {@link Long#MIN_VALUE} will be returned. See
	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
	 * @see #getTemporalHour(Date, Date)
	 * @see #getSeaLevelSunrise()
	 * @see #getSeaLevelSunset()
	 */
	public long getShaahZmanisGra() {
		return getTemporalHour(getSeaLevelSunrise(), getSeaLevelSunset());
	}

	/**
	 * A method that returns a <em>shaah zmanis</em> (temporal hour) according to the opinion of the Magen Avraham. This
	 * calculation divides the day based on the opinion of the <em>MGA</em> that the day runs from dawn to dusk (for sof
	 * zman krias shema and tfila). Dawn for this calculation is 72 minutes before sunrise and dusk is 72 minutes after
	 * sunset. This day is split into 12 equal parts with each part being a <em>shaah zmanis</em>. Alternate mothods of
	 * calculating a <em>shaah zmanis</em> are available in the subclass {@link ComplexZmanimCalendar}.
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
	 * A method to get the offset in minutes before {@link AstronomicalCalendar#getSunset() sunset} which is used in
	 * calculating candle lighting time. The default time used is 18 minutes before sunset. Some calendars use 15
	 * minutes, while the custom in Jerusalem is to use a 40 minute offset. Please check the local custom for candle
	 * lighting time.
	 * 
	 * @return Returns the currently set candle lighting offset in minutes.
	 * @see #getCandleLighting()
	 * @see #setCandleLightingOffset(double)
	 */
	public double getCandleLightingOffset() {
		return this.candleLightingOffset;
	}

	/**
	 * A method to set the offset in minutes before {@link AstronomicalCalendar#getSunset() sunset} that is used in
	 * calculating candle lighting time. The default time used is 18 minutes before sunset. Some calendars use 15
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
}