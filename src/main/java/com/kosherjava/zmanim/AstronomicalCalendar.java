/*
 * Zmanim Java API
 * Copyright (C) 2004-2026 Eliyahu Hershfeld
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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.util.ZmanimFormatter;

/**
 * A Java calendar that calculates astronomical times such as {@link #getSunrise() sunrise}, {@link #getSunset() sunset} and twilight
 * times. This class contains a {@link #getLocalDate() LocalDate} and can therefore use the standard calendar functionality to change
 * dates etc. The calculation engine used to calculate the astronomical times can be changed to a different implementation by
 * implementing the abstract {@link AstronomicalCalculator} which is configured via
 * {@link #setAstronomicalCalculator(AstronomicalCalculator)}. A number of different calculation engine implementations are included
 * in the util package.
 * <b>Note:</b> There are times when the algorithms can't calculate proper values for sunrise, sunset and twilight. This is usually
 * caused by trying to calculate times for areas either very far North or South, where sunrise / sunset never happen on that date.
 * This is common when calculating deep twilight angles at high northern latitudes, such as London. The sun never reaches this dip at
 * certain times of the year. When the calculations encounter this condition a {@code null} will be returned when a {@link
 * java.time.Instant} or {@link java.time.Duration} is expected. The reason that {@code Exception}s are not thrown in these cases is
 * because the lack of a rise/set or twilight is not an exception, but an expected condition in many parts of the world.
 * <p>
 * Here is a simple example of how to use the API to calculate sunrise.
 * First create the AstronomicalCalendar for the location you would like to calculate sunrise or sunset times for:
 * 
 * {@snippet lang='java' :
 * String locationName = &quot;Lakewood, NJ&quot;;
 * double latitude = 40.0828; // Lakewood, NJ
 * double longitude = -74.2094; // Lakewood, NJ
 * double elevation = 20; // optional elevation correction in Meters
 * // @link region="target_zone_link" substring="getAvailableZoneIds()" target="java.time.ZoneId#getAvailableZoneIds()"
 * ZoneId zoneId = ZoneId.of("America/New_York"); // set the zoneId to a valid ZoneId listed in getAvailableZoneIds()
 * // @end
 * GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, zoneId);
 * AstronomicalCalendar ac = new AstronomicalCalendar(location);
 * }
 * 
 * To get the time of sunrise, first set the date you want (if not set, the date will default to today):
 * 
 * {@snippet lang='java' :
 * LocalDate localDate = LocalDate.of(1969, Month.FEBRUARY, 8);
 * ac.setLocalDate(localDate);
 * Instant sunrise = ac.getSunrise();
 * }
 * 
 * @author © Eliyahu Hershfeld 2004 - 2026
 */
public class AstronomicalCalendar implements Cloneable {

	/**
	 * 90° below the vertical. Used as a basis for most calculations since the location of the sun is 90° below the horizon
	 * at sunrise and sunset.
	 * <b>Note </b>: it is important to note that for sunrise and sunset the {@link AstronomicalCalculator#adjustZenith(double,
	 * double) adjusted zenith} is required to account for the radius of the sun and refraction. The adjusted zenith should not be
	 * used for calculations above or below 90° since they are usually calculated as an offset to 90°.
	 */
	public static final double GEOMETRIC_ZENITH = 90;

	/** Sun's zenith at civil twilight (96°). */
	public static final double CIVIL_ZENITH = 96;

	/** Sun's zenith at nautical twilight (102°). */
	public static final double NAUTICAL_ZENITH = 102;

	/** Sun's zenith at astronomical twilight (108°). */
	public static final double ASTRONOMICAL_ZENITH = 108;

	/** constant for nanoseconds in a minute (60 billion / 60,000,000,000) */
	public static final long MINUTE_NANOS = 60_000_000_000L;

	/** constant for nanoseconds in an hour (3.6 trillion / 3,600,000,000,000) */
	public static final long HOUR_NANOS = 3_600_000_000_000L;
	
	/**
	 * The {@code LocalDate} encapsulated by this class to track the current date used by the class
	 */
	private LocalDate localDate;

	/**
	 * the {@link GeoLocation} used for calculations.
	 */
	private GeoLocation geoLocation;

	/**
	 * the internal {@link AstronomicalCalculator} used for calculating solar based times.
	 */
	private AstronomicalCalculator astronomicalCalculator;

	/**
	 * The getSunrise method returns a {@code Instant} representing the {@link AstronomicalCalculator
	 * #getElevationAdjustment(double) elevation adjusted} sunrise time. The zenith used for the calculation uses {@link
	 * #GEOMETRIC_ZENITH geometric zenith} of 90° plus {@link AstronomicalCalculator#getElevationAdjustment(double)}. This is
	 * adjusted by the {@link AstronomicalCalculator} to add approximately 50/60 of a degree to account for 34 arcminutes of
	 * refraction and 16 arcminutes for the sun's radius for a total of {@link AstronomicalCalculator#adjustZenith 90.83333°}.
	 * See documentation for the specific implementation of the {@link AstronomicalCalculator} that you are using.
	 * 
	 * @return the {@code Instant} representing the exact sunrise time. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does not set, a
	 *         {@code null} will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalculator#adjustZenith(double, double)
	 * @see #getSeaLevelSunrise()
	 * @see #getUTCSunrise(double)
	 */
	public Instant getSunrise() {
		double sunrise = getUTCSunrise(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunrise)) {
			return null;
		} else {
			return getInstantFromTime(sunrise, SolarEvent.SUNRISE);
		}
	}

	/**
	 * A method that returns the sunrise without {@link AstronomicalCalculator#getElevationAdjustment(double) elevation
	 * adjustment}. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible light,
	 * something that is not affected by elevation. This method returns sunrise calculated at sea level. This forms the
	 * base for dawn calculations that are calculated as a dip below the horizon before sunrise.
	 * 
	 * @return the {@code Instant} representing the exact sea-level sunrise time. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@code null} will be returned. See detailed explanation on top of the page.
	 * @see #getSunrise()
	 * @see #getUTCSeaLevelSunrise(double)
	 * @see #getSeaLevelSunset()
	 */
	public Instant getSeaLevelSunrise() {
		double sunrise = getUTCSeaLevelSunrise(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunrise)) {
			return null;
		} else {
			return getInstantFromTime(sunrise, SolarEvent.SUNRISE);
		}
	}

	/**
	 * A method that returns the beginning of <a href="https://en.wikipedia.org/wiki/Twilight#Civil_twilight">civil twilight</a>
	 * (dawn) using a zenith of {@link #CIVIL_ZENITH 96°}.
	 * 
	 * @return The {@code Instant} of the beginning of civil twilight using a zenith of 96°. If the calculation
	 *         can't be computed, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getBeginCivilTwilight() {
		return getSunriseOffsetByDegrees(CIVIL_ZENITH);
	}

	/**
	 * A method that returns the beginning of <a href="https://en.wikipedia.org/wiki/Twilight#Nautical_twilight">nautical twilight</a>
	 * using a zenith of {@link #NAUTICAL_ZENITH 102°}.
	 * 
	 * @return The {@code Instant} of the beginning of nautical twilight using a zenith of 102°. If the calculation
	 *         can't be computed {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getBeginNauticalTwilight() {
		return getSunriseOffsetByDegrees(NAUTICAL_ZENITH);
	}

	/**
	 * A method that returns the beginning of <a href="https://en.wikipedia.org/wiki/Twilight#Astronomical_twilight">astronomical
	 * twilight</a> using a zenith of {@link #ASTRONOMICAL_ZENITH 108°}.
	 * 
	 * @return The {@code Instant} of the beginning of astronomical twilight using a zenith of 108°. If the calculation
	 *         can't be computed, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getBeginAstronomicalTwilight() {
		return getSunriseOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}

	/**
	 * The getSunset method returns an {@code Instant} representing the
	 * {@link AstronomicalCalculator#getElevationAdjustment(double) elevation adjusted} sunset time. The zenith used for the
	 * calculation uses {@link #GEOMETRIC_ZENITH geometric zenith} of 90° plus {@link AstronomicalCalculator
	 * #getElevationAdjustment(double)}. This is adjusted by the {@link AstronomicalCalculator} to add approximately 50/60 of a
	 * degree to account for 34 arcminutes of refraction and 16 arcminutes for the sun's radius for a total of {@link
	 * AstronomicalCalculator#adjustZenith(double, double) 90.83333°}. See documentation for the specific implementation of the
	 * {@link AstronomicalCalculator} that you are using.
	 * Note: In certain cases the calculates sunset will occur before sunrise. This will typically happen when a time zone other than
	 * the local timezone is used (calculating Los Angeles sunset using a GMT time zone for example). In this case the sunset date
	 * will be incremented to the following date.
	 *
	 * @return the {@code Instant} representing the exact sunset time. If the calculation can't be computed such as in the Arctic
	 *         Circle where there is at least one day a year where the sun does not rise, and one where it does not set, a
	 *         {@code null} will be returned. See detailed explanation on top of the page.
	 * @see AstronomicalCalculator#adjustZenith(double, double)
	 * @see #getSeaLevelSunset()
	 * @see #getUTCSunset(double)
	 */
	public Instant getSunset() {
		double sunset = getUTCSunset(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunset)) {
			return null;
		} else {
			return getInstantFromTime(sunset, SolarEvent.SUNSET);
		}
	}
	
	/**
	 * A method that returns the sunset without {@link AstronomicalCalculator#getElevationAdjustment(double) elevation adjustment}.
	 * Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible light, something that is not
	 * affected by elevation. This method returns sunset calculated at sea level. This forms the base for dusk calculations that are
	 * calculated as a dip below the horizon after sunset.
	 * 
	 * @return the {@code Instant} representing the exact sea-level sunset time. If the calculation can't be computed
	 *         such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and one
	 *         where it does not set, a {@code null} will be returned. See detailed explanation on top of the page.
	 * @see #getSunset()
	 * @see #getUTCSeaLevelSunset(double)
	 */
	public Instant getSeaLevelSunset() {
		double sunset = getUTCSeaLevelSunset(GEOMETRIC_ZENITH);
		if (Double.isNaN(sunset)) {
			return null;
		} else {
			return getInstantFromTime(sunset, SolarEvent.SUNSET);
		}
	}

	/**
	 * A method that returns the end of <a href="https://en.wikipedia.org/wiki/Twilight#Civil_twilight">civil twilight</a>
	 * using a zenith of {@link #CIVIL_ZENITH 96°}.
	 * 
	 * @return The {@code Instant} of the end of civil twilight using a zenith of {@link #CIVIL_ZENITH 96°}. If the
	 *         calculation can't be computed, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getEndCivilTwilight() {
		return getSunsetOffsetByDegrees(CIVIL_ZENITH);
	}

	/**
	 * A method that returns the end of nautical twilight using a zenith of {@link #NAUTICAL_ZENITH 102°}.
	 * 
	 * @return The {@code Instant} of the end of nautical twilight using a zenith of {@link #NAUTICAL_ZENITH 102°}. If the
	 *         calculation can't be computed, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getEndNauticalTwilight() {
		return getSunsetOffsetByDegrees(NAUTICAL_ZENITH);
	}

	/**
	 * A method that returns the end of astronomical twilight using a zenith of {@link #ASTRONOMICAL_ZENITH 108°}.
	 * 
	 * @return the {@code Instant} of the end of astronomical twilight using a zenith of {@link #ASTRONOMICAL_ZENITH 108°}. If
	 *         the calculation can't be computed, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getEndAstronomicalTwilight() {
		return getSunsetOffsetByDegrees(ASTRONOMICAL_ZENITH);
	}
	
	/**
	 * A utility method that returns an {@code Instant} offset by the offset time passed in. Please note that the level of light
	 * during twilight is not affected by elevation, so if this is being used to calculate an offset before sunrise or after sunset
	 * with the intent of getting a rough "level of light" calculation, the sunrise or sunset time passed to this method should be
	 * sea level sunrise and sunset.
	 * 
	 * @param time the start time
	 * @param offset the {@code Duration} of the offset to add to the time.
	 * @return the {@link java.time.Instant} with the offset of the {@code Duration} added to it
	 */
	public static Instant getTimeOffset(Instant time, Duration offset) {
		if (time == null || offset == null) {
			return null;
		}
		return time.plus(offset);
	}
	
	/**
	 * A utility method that returns the time of an offset by degrees below or above the horizon of {@link #getSunrise() sunrise}.
	 * Note that the degree offset is from the vertical, so for a calculation of 14° before sunrise, an offset of 14
	 * + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * 
	 * @param offsetZenith the degrees before {@link #getSunrise()} to use in the calculation. For time after sunrise use negative
	 *         numbers. Note that the degree offset is from the vertical, so for a calculation of 14° before sunrise, an offset
	 *         of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * @return The {@link java.time.Instant} of the offset after (or before) {@link #getSunrise()}. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does
	 *         not rise, and one where it does not set, a {@code null} will be returned. See detailed explanation
	 *         on top of the page.
	 */
	public Instant getSunriseOffsetByDegrees(double offsetZenith) {
		double dawn = getUTCSunrise(offsetZenith);
		return Double.isNaN(dawn) ? null : getInstantFromTime(dawn, SolarEvent.SUNRISE);
	}

	/**
	 * A utility method that returns the time of an offset by degrees below or above the horizon of {@link #getSunset()
	 * sunset}. Note that the degree offset is from the vertical, so for a calculation of 14° after sunset, an offset of 14 +
	 * {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * 
	 * @param offsetZenith the degrees after {@link #getSunset()} to use in the calculation. For time before sunset use negative
	 *         numbers. Note that the degree offset is from the vertical, so for a calculation of 14° after sunset, an offset
	 *         of 14 + {@link #GEOMETRIC_ZENITH} = 104 would have to be passed as a parameter.
	 * @return The {@link java.time.Instant} of the offset after (or before) {@link #getSunset()}. If the calculation
	 *         can't be computed such as in the Arctic Circle where there is at least one day a year where the sun does not rise, and
	 *         and one where it does not set, a {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getSunsetOffsetByDegrees(double offsetZenith) {
		double sunset = getUTCSunset(offsetZenith);
		return Double.isNaN(sunset) ? null : getInstantFromTime(sunset, SolarEvent.SUNSET);
	}

	/**
	 * Default constructor will set a default {@link GeoLocation#GeoLocation()}, a default {@link AstronomicalCalculator#getDefault()
	 * AstronomicalCalculator} and default the {@code LocalDate} to the current date.
	 */
	public AstronomicalCalendar() {
		this(new GeoLocation());
	}

	/**
	 * A constructor that takes in <a href="https://en.wikipedia.org/wiki/Geolocation">geolocation</a> information as a parameter.
	 * The default {@link AstronomicalCalculator#getDefault() AstronomicalCalculator} used for solar calculations is the more
	 * accurate {@link com.kosherjava.zmanim.util.NOAACalculator}.
	 *
	 * @param geoLocation The location information used for calculating astronomical solar times.
	 * @see setAstronomicalCalculator(AstronomicalCalculator) for changing the calculator class.
	 */
	public AstronomicalCalendar(GeoLocation geoLocation) {
		setLocalDate(LocalDate.now(geoLocation.getZoneId()));
		setGeoLocation(geoLocation);
		setAstronomicalCalculator(AstronomicalCalculator.getDefault());
	}

	/**
	 * A method that returns the sunrise in UTC time without correction for time zone offset from GMT and without using daylight
	 * savings time.
	 * 
	 * @param zenith the degrees below the horizon. For time after sunrise use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 */
	public double getUTCSunrise(double zenith) {
		return getAstronomicalCalculator().getUTCSunrise(getAdjustedLocalDate(), getGeoLocation(), zenith, true);
	}

	/**
	 * A method that returns the sunrise in UTC time without correction for time zone offset from GMT and without using
	 * daylight savings time. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible
	 * light, something that is not affected by elevation. This method returns UTC sunrise calculated at sea level. This
	 * forms the base for dawn calculations that are calculated as a dip below the horizon before sunrise.
	 * 
	 * @param zenith the degrees below the horizon. For time after sunrise use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see #getUTCSunrise(double)
	 * @see #getUTCSeaLevelSunset(double)
	 */
	public double getUTCSeaLevelSunrise(double zenith) {
		return getAstronomicalCalculator().getUTCSunrise(getAdjustedLocalDate(), getGeoLocation(), zenith, false);
	}

	/**
	 * A method that returns the sunset in UTC time without correction for time zone offset from GMT and without using
	 * daylight savings time.
	 * 
	 * @param zenith the degrees below the horizon. For time after sunset use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see #getUTCSeaLevelSunset(double)
	 */
	public double getUTCSunset(double zenith) {
		return getAstronomicalCalculator().getUTCSunset(getAdjustedLocalDate(), getGeoLocation(), zenith, true);
	}

	/**
	 * A method that returns the sunset in UTC time without correction for elevation, time zone offset from GMT and without using
	 * daylight savings time. Non-sunrise and sunset calculations such as dawn and dusk, depend on the amount of visible light,
	 * something that is not affected by elevation. This method returns UTC sunset calculated at sea level. This forms the base for
	 * dusk calculations that are calculated as a dip below the horizon after sunset.
	 * 
	 * @param zenith the degrees below the horizon. For time before sunset use negative numbers.
	 * @return The time in the format: 18.75 for 18:45:00 UTC/GMT. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@link Double#NaN} will be returned. See detailed explanation on top of the page.
	 * @see #getUTCSunset(double)
	 * @see #getUTCSeaLevelSunrise(double)
	 */
	public double getUTCSeaLevelSunset(double zenith) {
		return getAstronomicalCalculator().getUTCSunset(getAdjustedLocalDate(), getGeoLocation(), zenith, false);
	}

	/**
	 * A method that returns a sea-level based temporal (solar) hour. The day from {@link #getSeaLevelSunrise() sea-level sunrise} to
	 * {@link #getSeaLevelSunset() sea-level sunset} is split into 12 equal parts with each one being a temporal hour.
	 * 
	 * @see #getSeaLevelSunrise()
	 * @see #getSeaLevelSunset()
	 * @see #getTemporalHour(Instant, Instant)
	 * @return the {@code Duration} of the temporal hour. If the calculation can't be computed a {@code null} will be
	 *         returned. See detailed explanation on top of the page.
	 */
	public Duration getTemporalHour() {
		return getTemporalHour(getSeaLevelSunrise(), getSeaLevelSunset());
	}
	
	/**
	 * A utility method that will allow the calculation of a temporal (solar) hour based on the sunrise and sunset passed as
	 * parameters to this method. An example of the use of this method would be the calculation of a elevation adjusted temporal
	 * hour by passing in {@link #getSunrise() sunrise} and {@link #getSunset() sunset} as parameters.
	 * 
	 * @param startOfDay The start of the day.
	 * @param endOfDay The end of the day.
	 * @return the {@code Duration} of the temporal hour. If the calculation can't be computed a {@code null} will be
	 *         returned. See detailed explanation on top of the page.
	 * @see #getTemporalHour()
	 */
	public Duration getTemporalHour(Instant startOfDay, Instant endOfDay) {
		if (startOfDay == null || endOfDay == null) {
			return null;
		}
		
		return Duration.between(startOfDay, endOfDay).dividedBy(12);
	}

	/**
	 * A method that returns sundial or solar noon. It occurs when the Sun is <a href=
	 * "https://en.wikipedia.org/wiki/Transit_%28astronomy%29">transiting</a> the <a
	 * href="https://en.wikipedia.org/wiki/Meridian_%28astronomy%29">celestial meridian</a>. The calculations used by this class
	 * depend on the {@link AstronomicalCalculator} used. If this calendar instance is {@link #setAstronomicalCalculator(
	 * AstronomicalCalculator)} is set to use the {@link com.kosherjava.zmanim.util.NOAACalculator} (the default) it will calculate
	 * astronomical noon. If the calendar instance is  to use the {@link com.kosherjava.zmanim.util.SunTimesCalculator}, that does
	 * not have code to calculate astronomical noon, the sun transit is calculated as halfway between sea level sunrise and sea level
	 * sunset, which can be slightly off the real transit time due to changes in declination (the lengthening or shortening day). See
	 * <a href="https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of Chatzos</a> for details on the proper
	 * definition of solar noon / midday.
	 * 
	 * @return the {@code Instant} representing Sun's transit. If the calculation can't be computed such as when using the {@link
	 *         com.kosherjava.zmanim.util.SunTimesCalculator USNO calculator} that does not support getting solar noon for the Arctic
	 *         Circle (where there is at least one day a year where the sun does not rise, and one where it does not set), a
	 *         {@code null} will be returned. See detailed explanation on top of the page.
	 * @see #getSunTransit(Instant, Instant)
	 * @see #getTemporalHour()
	 * @see com.kosherjava.zmanim.util.NOAACalculator#getUTCNoon(LocalDate, GeoLocation)
	 * @see com.kosherjava.zmanim.util.SunTimesCalculator#getUTCNoon(LocalDate, GeoLocation)
	 */
	public Instant getSunTransit() {
		double noon = getAstronomicalCalculator().getUTCNoon(getAdjustedLocalDate(), getGeoLocation());
		return getInstantFromTime(noon, SolarEvent.NOON);
	}
	
	/**
	 * A method that returns solar midnight as the <b>end of the day</b> (that may actually be after midnight of the  day it is
	 * being calculated for). For example calculating solar midnight for February 8, will calculate it for midnight between February
	 * 8 and February 9. It occurs when the Sun is <a href="https://en.wikipedia.org/wiki/Transit_%28astronomy%29">transiting</a> the
	 * lower <a href="https://en.wikipedia.org/wiki/Meridian_%28astronomy%29">celestial meridian</a>, or when the sun is at it's
	 * <a href="https://en.wikipedia.org/wiki/Nadir">nadir</a>. The calculations used by this class depend on the {@link
	 * AstronomicalCalculator} used. If this calendar instance is {@link #setAstronomicalCalculator(AstronomicalCalculator) set} to
	 * use the {@link com.kosherjava.zmanim.util.NOAACalculator} (the default) it will calculate astronomical midnight. If the
	 * calendar instance is to use the {@link com.kosherjava.zmanim.util.SunTimesCalculator USNO Calculator}, that does not have code
	 * to calculate astronomical noon, midnight is calculated as 12 hours after halfway between sea level sunrise and sea level sunset
	 * of that day. This can be slightly off the real transit time due to changes in declination (the lengthening or shortening day).
	 * See <a href="https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of Chatzos</a> for details on the proper
	 * definition of solar noon / midday.
	 * 
	 * @return the {@code Instant} representing Sun's lower transit at the <b>end of the current day</b>. If the calculation
	 *         can't be computed such as when using the {@link com.kosherjava.zmanim.util.SunTimesCalculator USNO calculator} that does
	 *         not support getting solar noon or midnight for the Arctic Circle (where there is at least one day a year where the sun
	 *         does not rise, and one where it does not set), a {@code null} will be returned. This is not relevant when using the
	 *         {@link com.kosherjava.zmanim.util.NOAACalculator NOAA Calculator} that is never expected to return {@code null}.
	 *         See the detailed explanation on top of the page.
	 * @see #getSunTransit()
	 * @see com.kosherjava.zmanim.util.NOAACalculator#getUTCNoon(LocalDate, GeoLocation)
	 * @see com.kosherjava.zmanim.util.SunTimesCalculator#getUTCNoon(LocalDate, GeoLocation)
	 */
	public Instant getSolarMidnight() {
		double noon = getAstronomicalCalculator().getUTCMidnight(getAdjustedLocalDate(), getGeoLocation());
		return getInstantFromTime(noon, SolarEvent.MIDNIGHT);
	}

	/**
	 * A method that returns sundial or solar noon (or midnight) calculated as halfway between the times passed in. It is close to,
	 * but not exactly  occurs when the Sun is <a href="https://en.wikipedia.org/wiki/Transit_%28astronomy%29">transiting</a> the
	 * <a href="https://en.wikipedia.org/wiki/Meridian_%28astronomy%29">celestial meridian</a>. It will not exactly match the
	 * astronomical transit, due to changes in declination (the lengthening or shortening day).
	 * 
	 * @param startOfDay the start of day for calculating the sun's transit. This can be sea level sunrise, visual sunrise (or any
	 *         arbitrary start of day) passed to this method.
	 * @param endOfDay the end of day for calculating the sun's transit. This can be sea level sunset, visual sunset (or any arbitrary
	 *         end of day) passed to this method.
	 * @return the {@code Instant} representing Sun's transit. If the calculation can't be computed such as in the
	 *         Arctic Circle where there is at least one day a year where the sun does not rise, and one where it does
	 *         not set, {@code null} will be returned. See detailed explanation on top of the page.
	 */
	public Instant getSunTransit(Instant startOfDay, Instant endOfDay) {
		Duration temporalHour = getTemporalHour(startOfDay, endOfDay);
		if (temporalHour == null) {
			return null;
		}
		return getTimeOffset(startOfDay, temporalHour.multipliedBy(6));
	}

	/**
	 * An enum to indicate what type of solar event is being calculated.
	 */
	protected enum SolarEvent {
		/**SUNRISE A solar event related to sunrise*/SUNRISE, /**SUNSET A solar event related to sunset*/SUNSET,
		/**NOON A solar event related to noon*/NOON, /**MIDNIGHT A solar event related to midnight*/MIDNIGHT,
		/**NONE solar event representing azimuth or elevation calculations that can be any time of the day*/ NONE;
	}
	
	/**
	 * Return the time at either an azimuth 90° (directly east) or 270° (directly west).
	 * 
	 * @param azimuth the azimuth that you want to get the time of day for.
	 * @return the time that the azimuth will be reached. There are cases where this azimuth will never be reached for the date and
	 *         location, and a null will be returned in that case.
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getTimeAtAzimuth(LocalDate, GeoLocation, double)
	 * @see com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getSunriseOrEasternmostSolarAzimuth()
	 * @see com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getSunsetOrWesternmostSolarAzimuth()
	 * @throws IllegalArgumentException if the azimuth is not 90° or 270°.
	 * @todo Once a reliable implementation to get any azimuth at any date for any latitude is implemented, make this method more
	 *         generic.
	 */
	public Instant getTimeAtAzimuth90Or270(double azimuth) {
		double rawAzimuth = getAstronomicalCalculator().getTimeAtAzimuth(getAdjustedLocalDate(), getGeoLocation(), azimuth);
		return getInstantFromTime(rawAzimuth, SolarEvent.NONE);
	}
	
	/**
	 * A method that returns an {@code Instant} from the {@code double} passed in as a parameter.
	 * 
	 * @param time The time to be set as the time for the {@code Instant}. The time expected is in the format: 18.75 for 6:45:00 PM. 
	 * @param solarEvent the type of {@link SolarEvent}
	 * @return The Instant object representation of the time double
	 */
	protected Instant getInstantFromTime(double time, SolarEvent solarEvent) {
		if (Double.isNaN(time)) {
			return null;
		}
		
		LocalDate date = getAdjustedLocalDate();
		double localTimeHours = (getGeoLocation().getLongitude() / 15) + time;
		
		if (solarEvent == SolarEvent.SUNRISE && localTimeHours > 18) {
			date = date.minusDays(1);
		} else if (solarEvent == SolarEvent.SUNSET && localTimeHours < 6) {
			date = date.plusDays(1);
		} else if (solarEvent == SolarEvent.MIDNIGHT && localTimeHours < 12) {
			date = date.plusDays(1);
		} else if (solarEvent == SolarEvent.NOON) {
			if (localTimeHours < 0) {
				date = date.plusDays(1);
			} else if (localTimeHours > 24) {
				date = date.minusDays(1);
			}
		} else if (solarEvent == SolarEvent.NONE) {
			// Azimuth events can occur at any time of day; apply the same date-boundary adjustments as SUNRISE/SUNSET to handle
			// far-east and far-west locations where local date differs from UTC date. For example, Suva, Fiji (UTC+12): the first
			// half of any local day (midnight–noon local) corresponds to the previous UTC calendar date (12:00–24:00 UTC the day
			// before).  A morning azimuth event like 90° (due East, ~6–7 AM local) therefore has a UTC time of ~18:xx on the day
			// before the requested Fiji local date. Without a date adjustment, anchoring that UTC time to the wrong calendar date
			// produces an Instant one full day too late.
			if (localTimeHours > 18) {
				date = date.minusDays(1);
			} else if (localTimeHours < 6) {
				date = date.plusDays(1);
			}
		}
		
		LocalDateTime dateTime = date.atStartOfDay().plusNanos(Math.round(time * HOUR_NANOS));
		
		// The computed time is in UTC fractional hours; anchor in UTC before converting.
		return ZonedDateTime.of(dateTime, ZoneOffset.UTC).toInstant();
	}

	/**
	 * Returns the sun's elevation (number of degrees) below the horizon before sunrise that matches the offset minutes
	 * on passed in as a parameter. For example passing in 72 minutes for a calendar set to the equinox in Jerusalem
	 * returns a value close to 16.1°.
	 * 
	 * @param minutes minutes before sunrise
	 * @return the degrees below the horizon before {@link #getSeaLevelSunrise()} that match the offset in minutes passed in as a
	 *         parameter. If the calculation can't be computed (no sunrise occurs on this day) a {@link Double#NaN} will be returned.
	 * @deprecated This method is slow and inefficient and should NEVER be used in a loop. This method should be replaced by calls to
	 *         {@link AstronomicalCalculator#getSolarElevation(Instant, GeoLocation)}. That method will efficiently return the solar
	 *         elevation (the sun's position in degrees below (or above) the horizon) at the given time even in the arctic when there
	 *         is no sunrise.
	 * @see AstronomicalCalculator#getSolarElevation(Instant, GeoLocation)
	 * @see #getSunsetSolarDipFromOffset(double)
	 */
	@Deprecated(forRemoval=false)
	public double getSunriseSolarDipFromOffset(double minutes) {
		if (minutes == 0.0) {
			return 0.0;
		}
		if (Double.isNaN(minutes)) {
			return Double.NaN;
		}
		
		Instant seaLevelSunrise = getSeaLevelSunrise();
		if (seaLevelSunrise == null) {
			return Double.NaN;
		}

		Duration offsetDuration = Duration.ofNanos((long) (-minutes * MINUTE_NANOS));
		Instant offsetByTime = getTimeOffset(seaLevelSunrise, offsetDuration);
		long offsetByTimeMilli = offsetByTime.toEpochMilli();
		double degrees = 0.0;
		double incrementor = 0.0001;
		Instant offsetByDegrees;

		do {
			if (minutes > 0.0) {
				degrees += incrementor;
			} else {
				degrees -= incrementor;
			}

			offsetByDegrees = getSunriseOffsetByDegrees(GEOMETRIC_ZENITH + degrees);

			if (offsetByDegrees == null || Math.abs(degrees) > 30.0) {
				return Double.NaN;
			}
			
		} while ((minutes > 0.0 && offsetByDegrees.toEpochMilli() > offsetByTimeMilli) ||
				(minutes < 0.0 && offsetByDegrees.toEpochMilli() < offsetByTimeMilli));

		return degrees;
	}

	/**
	 * Returns the sun's elevation (number of degrees) below the horizon after sunset that matches the offset minutes
	 * passed in as a parameter. For example passing in 72 minutes for a date set to the equinox in Jerusalem
	 * returns a value close to 16.1°.
	 * 
	 * @param minutes minutes after sunset
	 * @return the degrees below the horizon after sunset that match the offset in minutes passed it as a parameter. If the
	 *         calculation can't be computed (no sunset occurs on this day) a {@link Double#NaN} will be returned.
	 * @deprecated This method is slow and inefficient and should NEVER be used in a loop. This method should be replaced by calls to
	 *         {@link AstronomicalCalculator#getSolarElevation(Instant, GeoLocation)}. That method will efficiently return the solar
	 *         elevation (the sun's position in degrees below (or above) the horizon) at the given time even in the arctic when there
	 *         is no sunrise.
	 * @see AstronomicalCalculator#getSolarElevation(Instant, GeoLocation)
	 * @see #getSunriseSolarDipFromOffset(double)
	 */
	@Deprecated(forRemoval=false)
	public double getSunsetSolarDipFromOffset(double minutes) {
		if (minutes == 0.0) {
			return 0.0;
		}
		if (Double.isNaN(minutes)) {
			return Double.NaN;
		}
		
		Instant seaLevelSunset = getSeaLevelSunset();
		if (seaLevelSunset == null) {
			return Double.NaN;
		}

		Duration offsetDuration = Duration.ofNanos((long) (minutes * MINUTE_NANOS));
		Instant offsetByTime = getTimeOffset(seaLevelSunset, offsetDuration);
		long offsetByTimeMilli = offsetByTime.toEpochMilli();
		double degrees = 0.0;
		double incrementor = 0.0001;
		Instant offsetByDegrees;

		do {
			if (minutes > 0.0) {
				degrees += incrementor;
			} else {
				degrees -= incrementor;
			}

			offsetByDegrees = getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + degrees);

			if (offsetByDegrees == null || Math.abs(degrees) > 30.0) {
				return Double.NaN;
			}

		} while ((minutes > 0.0 && offsetByDegrees.toEpochMilli() < offsetByTimeMilli) ||
				(minutes < 0.0 && offsetByDegrees.toEpochMilli() > offsetByTimeMilli));

		return degrees;
	}

	/**
	 * A method that returns <a href="https://en.wikipedia.org/wiki/Local_mean_time">local mean time (LMT)</a> for a {@code LocalTime}
	 * passed to this method. This time is adjusted from standard time to account for the local latitude. The 360° of the globe
	 * divided by 24 calculates to 15° per hour with 4 minutes per degree, so at a longitude of 0 , 15, 30 etc... noon is at exactly
	 * 12:00 PM. Lakewood, NJ, with a longitude of -74.222, is 0.7906 away from the closest multiple of 15 at -75°. This is
	 * multiplied by 4 clock minutes (per degree) to yield 3 minutes and 7 seconds for a noon time of 11:56:53 AM. This method is not
	 * tied to the theoretical 15° time zones, but will adjust to the actual time zone and <a href=
	 * "https://en.wikipedia.org/wiki/Daylight_saving_time">Daylight saving time</a> to return LMT.
	 * 
	 * @param localTime the local wall-clock time (such as 12:00 for noon and 00:00 for midnight) to calculate as LMT.
	 * @return the {@code Instant} representing the local mean time (LMT) for the time passed in. In Lakewood, NJ, passing noon
	 *         will return 11:56:50 AM.
	 * @see GeoLocation#getLocalMeanTimeOffset(Instant)
	 */
	public Instant getLocalMeanTime(LocalTime localTime) {
		Instant localMeanTime = LocalDateTime.of(getAdjustedLocalDate(), localTime).toInstant(ZoneOffset.UTC);
		long totalNanos = (long) (getGeoLocation().getLongitude() * 4 * MINUTE_NANOS);
		return getTimeOffset(localMeanTime, Duration.ofNanos(-totalNanos));
	}

	/**
	 * Adjusts the {@code LocalDate} to deal with edge cases where the location crosses the antimeridian.
	 * 
	 * @see GeoLocation#getAntimeridianAdjustment(Instant)
	 * @return the adjusted {@code LocalDate}
	 */
	protected LocalDate getAdjustedLocalDate(){
		int offset = getGeoLocation().getAntimeridianAdjustment(getMidnightLastNight().toInstant());
		return offset == 0 ? getLocalDate() : getLocalDate().plusDays(offset);
	}

	/**
	 * Used by Molad based <em>zmanim</em> to determine if <em>zmanim</em> occur during the current day. This is also used as the
	 * anchor for current timezone-offset calculations.
	 * @return midnight at the start of the current local date in the configured {@link GeoLocation#getZoneId()}.
	 */
	protected ZonedDateTime getMidnightLastNight() {
		return ZonedDateTime.of(getLocalDate(),LocalTime.MIDNIGHT,getGeoLocation().getZoneId());
	}

	/**
	 * Used by Molad based <em>zmanim</em> to determine if <em>zmanim</em> occur during the current day.
	 * @return following midnight
	 */
	protected ZonedDateTime getMidnightTonight() {
		return ZonedDateTime.of(getLocalDate().plusDays(1),LocalTime.MIDNIGHT,getGeoLocation().getZoneId());
	}

	/**
	 * Returns an XML formatted representation of the class using the default output of the
	 *         {@link com.kosherjava.zmanim.util.ZmanimFormatter#toXML(AstronomicalCalendar) toXML} method.
	 * @return an XML formatted representation of the class. It returns the default output of the
	 *         {@link com.kosherjava.zmanim.util.ZmanimFormatter#toXML(AstronomicalCalendar) toXML} method.
	 * @see com.kosherjava.zmanim.util.ZmanimFormatter#toXML(AstronomicalCalendar)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ZmanimFormatter.toXML(this);
	}
	
	/**
	 * Returns a JSON formatted representation of the class using the default output of the
	 *         {@link com.kosherjava.zmanim.util.ZmanimFormatter#toJSON(AstronomicalCalendar) toJSON} method.
	 * @return a JSON formatted representation of the class. It returns the default output of the
	 *         {@link com.kosherjava.zmanim.util.ZmanimFormatter#toJSON(AstronomicalCalendar) toJSON} method.
	 * @see com.kosherjava.zmanim.util.ZmanimFormatter#toJSON(AstronomicalCalendar)
	 * @see java.lang.Object#toString()
	 */
	public String toJSON() {
		return ZmanimFormatter.toJSON(this);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Two {@code AstronomicalCalculator} instances are considered equal if their {@link #getLocalDate()}, {@link #getGeoLocation()}
	 * and {@link #getAstronomicalCalculator()} values are identical.
	 * 
	 * @param object the reference object with which to compare
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		AstronomicalCalendar aCal = (AstronomicalCalendar) object;
		return Objects.equals(getLocalDate(), aCal.getLocalDate())
				&& Objects.equals(getGeoLocation(), aCal.getGeoLocation())
				&& Objects.equals(getAstronomicalCalculator(), aCal.getAstronomicalCalculator());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation hashes the {@code Class}, {@linkplain #getLocalDate()}, {@link #getGeoLocation()} and
	 * {@link #getAstronomicalCalculator()} properties to maintain the contract with {@link #equals(Object)}.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getClass(), getLocalDate(), getGeoLocation(), getAstronomicalCalculator());
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
	 * Sets the {@link GeoLocation} {@code Object} to be used for astronomical calculations.
	 * 
	 * @param geoLocation The geoLocation to set.
	 * @todo Possibly adjust for horizon elevation. It may be smart to just have the calculator check the GeoLocation
	 *       though it doesn't really belong there.
	 */
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
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
	 * with a number of different implementations of the {@code abstract} {@link AstronomicalCalculator} based on
	 * different algorithms, including the default {@link com.kosherjava.zmanim.util.NOAACalculator} based on <a href=
	 * "https://noaa.gov">NOAA's</a> implementation of Jean Meeus's algorithms as well as {@link
	 * com.kosherjava.zmanim.util.SunTimesCalculator} based on the <a href = "https://www.cnmoc.usff.navy.mil/usno/">US
	 * Naval Observatory's</a> algorithm. This allows easy runtime switching and comparison of different algorithms.
	 * 
	 * @param astronomicalCalculator The {@code AstronomicalCalculator} to set.
	 */
	public void setAstronomicalCalculator(AstronomicalCalculator astronomicalCalculator) {
		this.astronomicalCalculator = astronomicalCalculator;
	}
	
	/**
	 * returns the {@code LocalDate} object encapsulated in this class.
	 * 
	 * @return Returns the {@code LocalDate}.
	 */
	public LocalDate getLocalDate() {
		return this.localDate;
	}
	
	/**
	 * Sets the {@code LocalDate}  object for use in this class.
	 * @param localDate The {@code LocalDate} to set.
	 */
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Object clone() {
		AstronomicalCalendar clone = null;
		try {
			clone = (AstronomicalCalendar) super.clone();
		} catch (CloneNotSupportedException cnse) {
			throw new AssertionError("Clone not supported on a Cloneable object", cnse);
		}

		clone.setGeoLocation((GeoLocation) getGeoLocation().clone()); // consider converting the GeoLocation class to be immutable to avoid the deep copy
		clone.setAstronomicalCalculator((AstronomicalCalculator) getAstronomicalCalculator().clone()); // likely not needed

		return clone;
	}
}
