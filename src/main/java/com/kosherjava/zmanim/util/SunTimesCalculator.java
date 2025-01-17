/*
 * Zmanim Java API
 * Copyright (C) 2004-2026 Eliyahu Hershfeld
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
package com.kosherjava.zmanim.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times. This calculator uses the Java algorithm
 * written by <a href="https://web.archive.org/web/20090531215353/http://www.kevinboone.com/suntimes.html">Kevin
 * Boone</a> that is based on the <a href = "https://aa.usno.navy.mil/">US Naval Observatory's</a><a
 * href="https://aa.usno.navy.mil/publications/asa">Astronomical Almanac</a> and used with his permission. Added to Kevin's
 * code is adjustment of the zenith to account for elevation. This algorithm returns the same time every year and does not
 * account for leap years. It is not as accurate as the Jean Meeus based {@link NOAACalculator} that is the default calculator
 * use by the KosherJava <em>zmanim</em> library. It also does not have an implementation of some solar calculation methods.
 *
 * @author &copy; Eliyahu Hershfeld 2004 - 2026
 * @author &copy; Kevin Boone 2000
 */
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
		double adjustedZenith = adjustZenith(zenith, elevation);
		return getTimeUTC(localDate, geoLocation, adjustedZenith, true);
	}

	@Override
	public double getUTCSunset(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation);
		return getTimeUTC(localDate, geoLocation, adjustedZenith, false);
	}

	/**
	 * The number of degrees of longitude that corresponds to one hour of time difference.
	 */
	private static final double DEG_PER_HOUR = 360.0 / 24.0;

	private static final double PI2 = Math.PI * 2;

	/**
	 * The sine in degrees.
	 * @param deg the degrees
	 * @return sin of the angle in degrees
	 */
	private static double sinDeg(double deg) {
		return Math.sin(deg * PI2 / 360.0);
	}

	/**
	 * Return the arc cosine in degrees.
	 * @param x angle
	 * @return acos of the angle in degrees
	 */
	private static double acosDeg(double x) {
		return Math.acos(x) * 360.0 / PI2;
	}

	/**
	 * Return the arc sine in degrees.
	 * @param x angle
	 * @return asin of the angle in degrees
	 */
	private static double asinDeg(double x) {
		return Math.asin(x) * 360.0 / PI2;
	}

	/**
	 * Return the tangent in degrees.
	 * @param deg degrees
	 * @return tan of the angle in degrees
	 */
	private static double tanDeg(double deg) {
		return Math.tan(deg * PI2 / 360.0);
	}
	
	/**
	 * Calculate cosine of the angle in degrees
	 * 
	 * @param deg degrees
	 * @return cosine of the angle in degrees
	 */
	private static double cosDeg(double deg) {
		return Math.cos(deg * PI2 / 360.0);
	}

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
	 * 
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
		double l = sunMeanAnomaly + (1.916 * sinDeg(sunMeanAnomaly)) + (0.020 * sinDeg(2 * sunMeanAnomaly)) + 282.634;

		// get longitude into 0-360 degree range
		if (l >= 360.0) {
			l = l - 360.0;
		}
		if (l < 0) {
			l = l + 360.0;
		}
		return l;
	}

	/**
	 * Calculates the Sun's right ascension in hours.
	 * @param sunTrueLongitude the Sun's true longitude in degrees &gt; 0 and &lt; 360.
	 * @return the Sun's right ascension in hours in angles &gt; 0 and &lt; 360.
	 */
	private static double getSunRightAscensionHours(double sunTrueLongitude) {
		double a = 0.91764 * tanDeg(sunTrueLongitude);
		double ra = 360.0 / PI2 * Math.atan(a);

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
		double sinDec = 0.39782 * sinDeg(sunTrueLongitude);
		double cosDec = cosDeg(asinDeg(sinDec));
		return (cosDeg(zenith) - (sinDec * sinDeg(latitude))) / (cosDec * cosDeg(latitude));
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
	 * @param localDate
	 *            the <code>LocalDate</code> object to extract the day of year for calculation
	 * @param geoLocation
	 *            The location information used for astronomical calculation of solar times.
	 * @param zenith
	 *            Sun's zenith in degrees
	 * @param isSunrise
	 *            True for sunrise and false for sunset.
	 * @return the time as a double. If an error was encountered in the calculation (expected behavior for some locations such as
	 *         near the poles, {@link Double#NaN} will be returned.
	 */
	private static double getTimeUTC(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean isSunrise) {
		return getTimeUTC(localDate, geoLocation, zenith, isSunrise, true);
	}

	private static double getTimeUTC(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean isSunrise, boolean interpolate) {
		int dayOfYear = localDate.getDayOfYear();
        double latitude = geoLocation.getLatitude();
        double longitude = geoLocation.getLongitude();

		double sunMeanAnomaly = getMeanAnomaly(dayOfYear, longitude, isSunrise);
		double sunTrueLong = getSunTrueLongitude(sunMeanAnomaly);
		double cosLocalHourAngle = getCosLocalHourAngle(sunTrueLong, latitude, zenith);
		double localHourAngle = acosDeg(cosLocalHourAngle);
		if (Double.isNaN(localHourAngle)) {
			if (interpolate) {
				return interpolateTimeUTC(localDate, geoLocation, zenith, isSunrise);
			}
			return Double.NaN;
		}
		if (isSunrise) {
			localHourAngle = 360.0 - localHourAngle;
		}
		double localHour = localHourAngle / DEG_PER_HOUR;

		double sunRightAscensionHours = getSunRightAscensionHours(sunTrueLong);
		double localMeanTime = getLocalMeanTime(localHour, sunRightAscensionHours,
				getApproxTimeDays(dayOfYear, getHoursFromMeridian(longitude), isSunrise));
		double processedTime = localMeanTime - getHoursFromMeridian(longitude);
		return processedTime > 0  ? processedTime % 24 : processedTime % 24 + 24; // ensure that the time is >= 0 and < 24
	}

	// Use linear interpolation to calculate a missing time.
	private static double interpolateTimeUTC(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean isSunrise) {
		final int dayOfYear = localDate.getDayOfYear();
		int x1 = 0;
		double y1 = 0;
		int x2 = 0;
		double y2 = 0;
		int dd1 = Integer.MAX_VALUE;
		int dd2 = Integer.MAX_VALUE;

		final int d1 = Math.max(dayOfYear - 180, 1);
		final int d2 = Math.min(dayOfYear + 180, 365);
		for (int d = d1; d < dayOfYear; d++) {
			double time = getTimeUTC(localDate.withDayOfYear(d), geoLocation, zenith, isSunrise, false);
			if (!Double.isNaN(time)) {
				int dd = Math.abs(dayOfYear - d);
				if (dd < dd1) {
					x2 = x1;
					y2 = y1;
					dd2 = dd1;

					x1 = d;
					y1 = time;
					dd1 = dd;
				}
			}
		}
		for (int d = dayOfYear + 1; d <= d2; d++) {
			double time = getTimeUTC(localDate.withDayOfYear(d), geoLocation, zenith, isSunrise, false);
			if (!Double.isNaN(time)) {
				if ((x2 > 0) && (x2 < dayOfYear)) {
					x2 = 0;
					y2 = 0;
					dd2 = Integer.MAX_VALUE;
				}
				int dd = Math.abs(dayOfYear - d);
				if (dd <= dd2) {
					x2 = d;
					y2 = time;
					dd2 = dd;
				}
			}
		}

		if ((x1 == 0) || (x2 == 0)) {
			return Double.NaN;
		}
		double dx = x2 - x1;
		if (dx == 0) {
			return Double.NaN;
		}
		double dy = y2 - y1;
		return y1 + (((dayOfYear - x1) * dy) / dx);
	}

	@Override
	public double getUTCNoon(LocalDate localDate, GeoLocation geoLocation) {
		double sunrise = getUTCSunrise(localDate, geoLocation, 90, false);
		double sunset = getUTCSunset(localDate, geoLocation, 90, false);
		double noon = sunrise + ((sunset - sunrise) / 2);
		if (noon < 0) {
			noon += 12;
		}
		if (noon < sunrise) {
			noon -= 12;
		}
		return noon;
	}
	
	@Override
	public double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation) {
		return (getUTCNoon(localDate, geoLocation) + 12);
	}

	/**
	 * <b>This calculator class does not implement the getSolarAzimuth method, and throws a {@link UnsupportedOperationException}.
	 * Use the {@link NOAACalculator}if this method is required</b>.
	 * <br>{@inheritDoc}
	 * @throws UnsupportedOperationException This calculator class does not implement the getSolarAzimuth method. Use the
	 *         {@link NOAACalculator} instead.
	 */
	@Override
	public double getSolarAzimuth(ZonedDateTime zdt, GeoLocation geoLocation) {
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
	public double getSolarElevation(ZonedDateTime zdt, GeoLocation geoLocation) {
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
