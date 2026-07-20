/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times. This calculator uses the Java algorithm
 * written by <a href="https://web.archive.org/web/20090531215353/http://www.kevinboone.com/suntimes.html">Kevin
 * Boone</a> that is based on the <a href = "https://aa.usno.navy.mil/">US Naval Observatory's</a><a
 * href="https://aa.usno.navy.mil/publications/asa">Astronomical Almanac</a> and used with his permission. Added to Kevin's
 * code is adjustment of the zenith to account for elevation. This algorithm returns the same time every year and does not
 * account for leap years. It is not as accurate as the Jean Meeus based {@link NOAACalculator} that is the default calculator
 * use by the KosherJava <em>zmanim</em> library, and is certainly not as accurate as some newer calculators such as the {@link 
 * MeeusCalculator} ot the {@link SPACalculator}. It also does not have an implementation of some solar calculation methods as
 * detailed in the deprecation notes.
 * 
 * @deprecated This calculator implements the legacy <em><a href=
 *         "https://www.google.com/books/edition/Almanac_for_Computers/4f8TAQAAIAAJ">Almanac for Computers</a></em> procedure of the
 *         US Naval Observatory, a deliberately simplified solar model (a single-term equation of center, fixed mean obliquity, and
 *         no nutation or aberration) that was designed for roughly one-minute accuracy on early calculators and personal computers.
 *         It is the least accurate of the calculators in this library by about an order of magnitude: in a grid study over latitudes
 *         60°S to 60°N for all of 2024, its sunrise and sunset differed from an exact ephemeris by a median of approximately 8
 *         seconds, compared with roughly 1 second for {@link NOAACalculator} and under 1 second for {@link MeeusCalculator} and
 *         {@link SPACalculator}, with worst cases of tens of seconds and far larger errors at high latitudes and for deep (18°)
 *         twilight. In addition, this calculator does not compute a true solar transit: {@link #getUTCNoon(LocalDate, GeoLocation)}
 *         returns the midpoint of its own sunrise and sunset, {@link #getUTCMidnight(LocalDate, GeoLocation)} returns that value
 *         plus twelve hours, and {@link #getSolarElevation(Instant, GeoLocation)} is not implemented. It is retained only for
 *         backward compatibility and for cases where its simplicity is preferred over accuracy. For new code use
 *         {@link SPACalculator} (the NREL Solar Position Algorithm, the most accurate option) or {@link MeeusCalculator} (the
 *         high-accuracy method of Jean Meeus); where a lighter NOAA-family calculator is sufficient, {@link NOAACalculator} is the
 *         library default and is already substantially more accurate than this class. There are no current plans on removing this
 *         calculator. There is value on using it as a reference for various reasons, key among them is to compare and validate
 *         historically calculates astronomical times. 
 *
 * @author © Eliyahu Hershfeld 2004 - 2026
 * @author © Kevin Boone 2000
 */
@Deprecated  (since = "3.0.0", forRemoval=false)
public class SunTimesCalculator extends AstronomicalCalculator {
	
	/**
	 * Default constructor of the SunTimesCalculator.
	 */
	public SunTimesCalculator() {
		super();
	}

	@Override
	public String getCalculatorName() {
		return "US Naval Almanac Algorithm";
	}

	@Override
	public double getUTCSunrise(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation, geoLocation.getLatitude(), localDate);
		return getTimeUTC(localDate, geoLocation, adjustedZenith, true);
	}

	@Override
	public double getUTCSunset(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation, geoLocation.getLatitude(), localDate);
		return getTimeUTC(localDate, geoLocation, adjustedZenith, false);
	}

	/**
	 * The number of degrees of longitude that corresponds to one hour of time difference.
	 */
	private static final double DEG_PER_HOUR = 360.0 / 24.0;

	/**
	 * Get time difference between location's longitude and the Meridian, in hours.
	 * 
	 * @param longitude the longitude
	 * @return time difference between the location's longitude and the Meridian, in hours. West of Meridian has a negative
	 *         time difference
	 */
	private static double getHoursFromMeridian(double longitude) {
		return longitude / DEG_PER_HOUR;
	}
	
	/**
	 * Calculate the approximate time of sunset or sunrise in days since midnight Jan 1st, assuming 6am and 6pm events. We
	 * need this figure to derive the Sun's mean anomaly.
	 * 
	 * @param dayOfYear the day of year
	 * @param hoursFromMeridian hours from the meridian
	 * @param isSunrise true for sunrise and false for sunset
	 * @return the approximate time of sunset or sunrise in days since midnight Jan 1st, assuming 6am and 6pm events. We
	 *         need this figure to derive the Sun's mean anomaly.
	 */
	private static double getApproxTimeDays(int dayOfYear, double hoursFromMeridian, boolean isSunrise) {
		if (isSunrise) {
			return dayOfYear + ((6.0 - hoursFromMeridian) / 24);
		} else { // sunset
			return dayOfYear + ((18.0 - hoursFromMeridian) / 24);
		}
	}
	
	/**
	 * Calculate the Sun's mean anomaly in degrees, at sunrise or sunset, given the longitude in degrees
	 * 
	 * @param dayOfYear the day of the year
	 * @param longitude longitude
	 * @param isSunrise true for sunrise and false for sunset
	 * @return the Sun's mean anomaly in degrees
	 */
	private static double getMeanAnomaly(int dayOfYear, double longitude, boolean isSunrise) {
		return (0.9856 * getApproxTimeDays(dayOfYear, getHoursFromMeridian(longitude), isSunrise)) - 3.289;
	}

	/**
	 * Returns the Sun's true longitude in degrees. 
	 * @param sunMeanAnomaly the Sun's mean anomaly in degrees
	 * @return the Sun's true longitude in degrees. The result is an angle &gt;= 0 and &lt;= 360.
	 */
	private static double getSunTrueLongitude(double sunMeanAnomaly) {
		double l = sunMeanAnomaly + (1.916 * sinDegrees(sunMeanAnomaly)) + (0.020 * sinDegrees(2 * sunMeanAnomaly)) + 282.634;
		return (l % 360 + 360) % 360; // return longitude as a 0-360 degree range
		
	}

	/**
	 * Calculates the Sun's right ascension in hours.
	 * @param sunTrueLongitude the Sun's true longitude in degrees &gt; 0 and &lt; 360.
	 * @return the Sun's right ascension in hours in angles &gt; 0 and &lt; 360.
	 */
	private static double getSunRightAscensionHours(double sunTrueLongitude) {
		double a = 0.91764 * tanDegrees(sunTrueLongitude);
		double ra = 360.0 / (2.0 * Math.PI) * Math.atan(a);

		double lQuadrant = Math.floor(sunTrueLongitude / 90.0) * 90.0;
		double raQuadrant = Math.floor(ra / 90.0) * 90.0;
		ra = ra + (lQuadrant - raQuadrant);

		return ra / DEG_PER_HOUR; // convert to hours
	}

	/**
	 * Calculate the cosine of the Sun's local hour angle
	 * 
	 * @param sunTrueLongitude the sun's true longitude
	 * @param latitude the latitude
	 * @param zenith the zenith
	 * @return the cosine of the Sun's local hour angle
	 */
	private static double getCosLocalHourAngle(double sunTrueLongitude, double latitude, double zenith) {
		double sinDec = 0.39782 * sinDegrees(sunTrueLongitude);
		double cosDec = cosDegrees(asinDegrees(sinDec));
		return (cosDegrees(zenith) - (sinDec * sinDegrees(latitude))) / (cosDec * cosDegrees(latitude));
	}
	
	/**
	 * Calculate local mean time of rising or setting. By 'local' is meant the exact time at the location, assuming that
	 * there were no time zone. That is, the time difference between the location and the Meridian depended entirely on
	 * the longitude. We can't do anything with this time directly; we must convert it to UTC and then to a local time.
	 * 
	 * @param localHour the local hour
	 * @param sunRightAscensionHours the sun's right ascension in hours
	 * @param approxTimeDays approximate time days
	 * 
	 * @return the fractional number of hours since midnight as a double
	 */
	private static double getLocalMeanTime(double localHour, double sunRightAscensionHours, double approxTimeDays) {
		return localHour + sunRightAscensionHours - (0.06571 * approxTimeDays) - 6.622;
	}

	/**
	 * Get sunrise or sunset time in UTC, according to flag. This time is returned as a double and is not adjusted for time-zone.
	 * 
	 * @param localDate the {@code LocalDate} object to extract the day of year for calculation
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param zenith Sun's zenith in degrees
	 * @param isSunrise {@code true} for sunrise and false for sunset.
	 * @return the time as a double. If an error was encountered in the calculation (expected behavior for some locations such as
	 *         near the poles, {@link Double#NaN} will be returned.
	 */
	private static double getTimeUTC(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean isSunrise) {
		int dayOfYear = localDate.getDayOfYear();
		double sunMeanAnomaly = getMeanAnomaly(dayOfYear, geoLocation.getLongitude(), isSunrise);
		double sunTrueLong = getSunTrueLongitude(sunMeanAnomaly);
		double sunRightAscensionHours = getSunRightAscensionHours(sunTrueLong);
		double cosLocalHourAngle = getCosLocalHourAngle(sunTrueLong, geoLocation.getLatitude(), zenith);

		double localHourAngle;
		if (isSunrise) {
			localHourAngle = 360.0 - acosDegrees(cosLocalHourAngle);
		} else { // sunset
			localHourAngle = acosDegrees(cosLocalHourAngle);
		}
		double localHour = localHourAngle / DEG_PER_HOUR;

		double localMeanTime = getLocalMeanTime(localHour, sunRightAscensionHours,
				getApproxTimeDays(dayOfYear, getHoursFromMeridian(geoLocation.getLongitude()), isSunrise));
		double pocessedTime = localMeanTime - getHoursFromMeridian(geoLocation.getLongitude());
		return (pocessedTime % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}
	
	@Override
	public double getUTCNoon(LocalDate localDate, GeoLocation geoLocation) {
		double sunrise = getUTCSunrise(localDate, geoLocation, 90, false);
		double sunset = getUTCSunset(localDate, geoLocation, 90, false);
		double noon = sunrise + ((sunset - sunrise) / 2);
		if (noon < sunrise) {
			noon -= 12;
		}
		return (noon % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}
	
	@Override
	public double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation) {
		return (getUTCNoon(localDate, geoLocation) + 12) % 24;
	}
	
	/**
	 * <b>This calculator class does not implement the getSolarAzimuth method, and throws a {@link UnsupportedOperationException}.
	 * Use the {@link NOAACalculator}if this method is required</b>.
	 * <br>{@inheritDoc}
	 * @throws UnsupportedOperationException This calculator class does not implement the getSolarAzimuth method. Use the
	 *         {@link NOAACalculator} instead.
	 */
	@Override
	public double getSolarAzimuth(Instant instant, GeoLocation geoLocation) {
		throw new UnsupportedOperationException(
				"The SunTimesCalculator class does not implement the getSolarAzimuth method. Use the {@link NOAACalculator} instead.");
	}
	
	/**
	 * <b>This calculator class does not implement the getSolarElevation method, and throws a {@link UnsupportedOperationException}.
	 * Use the {@link NOAACalculator}if this method is required</b>.
	 * <br>{@inheritDoc}
	 * @throws UnsupportedOperationException This calculator class does not implement the getSolarElevation method. Use the
	 *         {@link NOAACalculator} instead.
	 */
	@Override
	public double getSolarElevation(Instant instant, GeoLocation geoLocation) {
		throw new UnsupportedOperationException(
				"The SunTimesCalculator class does not implement the getSolarElevation method. Use the NOAACalculator instead.");
	}
	
	/**
	 * <b>This calculator class does not implement the getTimeAtAzimuth method, and throws a {@link UnsupportedOperationException}.
	 * Use the {@link NOAACalculator}if this method is required</b>.
	 * <br>{@inheritDoc}
	 * @throws UnsupportedOperationException This calculator class does not implement the getTimeAtAzimuth method. Use the
	 *         {@link NOAACalculator} instead.
	 */
	@Override
	public double getTimeAtAzimuth(LocalDate localDate, GeoLocation geoLocation, double azimuth) {
		throw new UnsupportedOperationException(
				"The SunTimesCalculator class does not implement the getTimeAtAzimuth method. Use the {@link NOAACalculator} instead.");
	}
}
