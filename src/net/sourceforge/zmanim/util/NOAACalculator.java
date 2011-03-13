/*
 * Zmanim Java API
 * Copyright (C) 2004-2011 Eliyahu Hershfeld
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc. 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA or connect to: http://www.fsf.org/copyleft/gpl.html
 */
package net.sourceforge.zmanim.util;

import net.sourceforge.zmanim.AstronomicalCalendar;
import java.util.Calendar;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times based on the <a href=""http://noaa.gov">NOAA</a> algorithm.
 * This calculator uses the Java algorithm based on the implementation by <a
 * href=""http://noaa.gov">NOAA - National Oceanic and Atmospheric
 * Administration</a>'s <a href =
 * "http://www.srrb.noaa.gov/highlights/sunrise/sunrisehtml">Surface Radiation
 * Research Branch</a>. NOAA's <a
 * href="http://www.srrb.noaa.gov/highlights/sunrise/solareqns.PDF">implementation</a>
 * is based on equations from <a
 * href="http://www.willbell.com/math/mc1.htm">Astronomical Algorithms</a> by
 * <a href="http://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a>. Added to
 * the algorithm is an adjustment of the zenith to account for elevation.
 *
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.1
 */
public class NOAACalculator extends AstronomicalCalculator {
	/**
	 * The Julian day of January 1, 2000
	 */
	private static final double JULIAN_DAY_JAN_1_2000 = 2451545.0;
	
	/**
	 * Julian days per century
	 */
	private static final double JULIAN_DAYS_PER_CENTURY = 36525.0;
	
	private String calculatorName = "US National Oceanic and Atmospheric Administration Algorithm";
	public String getCalculatorName() {
		return this.calculatorName;
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunrise(AstronomicalCalendar,
	 *      double, boolean)
	 */
	public double getUTCSunrise(AstronomicalCalendar astronomicalCalendar,
			double zenith, boolean adjustForElevation) {
		double adjustedZenith = zenith;

		if (adjustForElevation) {
			adjustedZenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation()
					.getElevation());
		} else {
			adjustedZenith = adjustZenith(zenith, 0);
		}

		double sunRise = calcSunriseUTC(calcJD(astronomicalCalendar
				.getCalendar()), astronomicalCalendar.getGeoLocation()
				.getLatitude(), -astronomicalCalendar.getGeoLocation()
				.getLongitude(), adjustedZenith);
		return sunRise / 60;
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunset(AstronomicalCalendar,
	 *      double, boolean)
	 */
	public double getUTCSunset(AstronomicalCalendar astronomicalCalendar,
			double zenith, boolean adjustForElevation) {
		double adjustedZenith = zenith;
		if (adjustForElevation) {
			adjustedZenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation()
					.getElevation());
		} else {
			adjustedZenith = adjustZenith(zenith, 0);
		}

		double sunSet = calcSunsetUTC(
				calcJD(astronomicalCalendar.getCalendar()),
				astronomicalCalendar.getGeoLocation().getLatitude(),
				-astronomicalCalendar.getGeoLocation().getLongitude(), adjustedZenith);
		return sunSet / 60;
	}

	/**
	 * Generate a Julian day from Java Calendar
	 *
	 * @param date
	 *            Java Calendar
	 * @return the Julian day corresponding to the date Note: Number is returned
	 *         for start of day. Fractional days should be added later.
	 */
	private static double calcJD(Calendar date) {
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		if (month <= 2) {
			year -= 1;
			month += 12;
		}
		double A = Math.floor(year / 100);
		double B = 2 - A + Math.floor(A / 4);

		return Math.floor(365.25 * (year + 4716))
				+ Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;
	}

	/**
	 * Convert Julian Day to centuries since J2000.0.
	 *
	 * @param julianDay
	 *            the Julian Day to convert
	 * @return the centuries since 2000 Julian corresponding to the Julian Day
	 */
	private static double getJulianCenturiesFromJulianDay(double julianDay) {
		return (julianDay - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
	}

	/**
	 * Convert centuries since J2000.0 to Julian Day.
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Julian Day corresponding to the Julian centuries passed in
	 */
	private static double getJulianDayFromJulianCenturies(double julianCenturies) {
		return julianCenturies * JULIAN_DAYS_PER_CENTURY + JULIAN_DAY_JAN_1_2000;
	}

	/**
	 * calculates the Geometric Mean Longitude of the Sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Geometric Mean Longitude of the Sun in degrees
	 */
	private static double calcGeomMeanLongSun(double julianCenturies) {
		double L0 = 280.46646 + julianCenturies * (36000.76983 + 0.0003032 * julianCenturies);
		while (L0 > 360.0) {
			L0 -= 360.0;
		}
		while (L0 < 0.0) {
			L0 += 360.0;
		}

		return L0; // in degrees
	}

	/**
	 * Calculate the Geometric Mean Anomaly of the Sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Geometric Mean Anomaly of the Sun in degrees
	 */
	private static double calcGeomMeanAnomalySun(double julianCenturies) {
		return 357.52911 + julianCenturies * (35999.05029 - 0.0001537 * julianCenturies); // in degrees
	}

	/**
	 * calculate the eccentricity of earth's orbit
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the unitless eccentricity
	 */
	private static double calcEccentricityEarthOrbit(double julianCenturies) {
		return 0.016708634 - julianCenturies * (0.000042037 + 0.0000001267 * julianCenturies); // unitless
	}

	/**
	 * Calculate the equation of center for the sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the equation of center for the sun in degrees
	 */
	private static double calcSunEqOfCenter(double julianCenturies) {
		double m = calcGeomMeanAnomalySun(julianCenturies);

		double mrad = Math.toRadians(m);
		double sinm = Math.sin(mrad);
		double sin2m = Math.sin(mrad + mrad);
		double sin3m = Math.sin(mrad + mrad + mrad);

		return sinm * (1.914602 - julianCenturies * (0.004817 + 0.000014 * julianCenturies)) + sin2m
				* (0.019993 - 0.000101 * julianCenturies) + sin3m * 0.000289;// in degrees
	}

	/**
	 * Calculate the true longitude of the sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the sun's true longitude in degrees
	 */
	private static double calcSunTrueLong(double julianCenturies) {
		double sunsLong = calcGeomMeanLongSun(julianCenturies);
		double c = calcSunEqOfCenter(julianCenturies);

		return sunsLong + c; // in degrees
	}

//	/**
//	 * Calculate the true anamoly of the sun
//	 *
//	 * @param t
//	 *            the number of Julian centuries since J2000.0
//	 * @return the sun's true anamoly in degrees
//	 */
//	private static double calcSunTrueAnomaly(double t) {
//		double m = calcGeomMeanAnomalySun(t);
//		double c = calcSunEqOfCenter(t);
//
//		double v = m + c;
//		return v; // in degrees
//	}

	/**
	 * calculate the apparent longitude of the sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return sun's apparent longitude in degrees
	 */
	private static double calcSunApparentLong(double julianCenturies) {
		double o = calcSunTrueLong(julianCenturies);

		double omega = 125.04 - 1934.136 * julianCenturies;
		double lambda = o - 0.00569 - 0.00478 * Math.sin(Math.toRadians(omega));
		return lambda; // in degrees
	}

	/**
	 * Calculate the mean obliquity of the ecliptic
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the mean obliquity in degrees
	 */
	private static double calcMeanObliquityOfEcliptic(double julianCenturies) {
		double seconds = 21.448 - julianCenturies
				* (46.8150 + julianCenturies * (0.00059 - julianCenturies * (0.001813)));
		return 23.0 + (26.0 + (seconds / 60.0)) / 60.0; // in degrees
	}

	/**
	 * calculate the corrected obliquity of the ecliptic
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the corrected obliquity in degrees
	 */
	private static double calcObliquityCorrection(double julianCenturies) {
		double obliquityOfEcliptic = calcMeanObliquityOfEcliptic(julianCenturies);

		double omega = 125.04 - 1934.136 * julianCenturies;
		return obliquityOfEcliptic + 0.00256 * Math.cos(Math.toRadians(omega)); // in degrees
	}

	/**
	 * Calculate the declination of the sun
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @param sun's
	 *            declination in degrees
	 */
	private static double calcSunDeclination(double julianCenturies) {
		double obliquityCorrection = calcObliquityCorrection(julianCenturies);
		double lambda = calcSunApparentLong(julianCenturies);

		double sint = Math.sin(Math.toRadians(obliquityCorrection))
				* Math.sin(Math.toRadians(lambda));
		double theta = Math.toDegrees(Math.asin(sint));
		return theta; // in degrees
	}

	/**
	 * calculate the difference between true solar time and mean solar time
	 *
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return equation of time in minutes of time
	 */
	private static double calcEquationOfTime(double julianCenturies) {
		double epsilon = calcObliquityCorrection(julianCenturies);
		double geomMeanLongSun = calcGeomMeanLongSun(julianCenturies);
		double eccentricityEarthOrbit = calcEccentricityEarthOrbit(julianCenturies);
		double geomMeanAnomalySun = calcGeomMeanAnomalySun(julianCenturies);

		double y = Math.tan(Math.toRadians(epsilon) / 2.0);
		y *= y;

		double sin2l0 = Math.sin(2.0 * Math.toRadians(geomMeanLongSun));
		double sinm = Math.sin(Math.toRadians(geomMeanAnomalySun));
		double cos2l0 = Math.cos(2.0 * Math.toRadians(geomMeanLongSun));
		double sin4l0 = Math.sin(4.0 * Math.toRadians(geomMeanLongSun));
		double sin2m = Math.sin(2.0 * Math.toRadians(geomMeanAnomalySun));

		double equationOfTime = y * sin2l0 - 2.0 * eccentricityEarthOrbit * sinm + 4.0 * eccentricityEarthOrbit * y * sinm
				* cos2l0 - 0.5 * y * y * sin4l0 - 1.25 * eccentricityEarthOrbit * eccentricityEarthOrbit * sin2m;
		return Math.toDegrees(equationOfTime) * 4.0; // in minutes of time
	}

	/**
	 * Calculate the hour angle of the sun at sunrise for the latitude
	 *
	 * @param lat,
	 *            the latitude of observer in degrees
	 * @param solarDec
	 *            the declination angle of sun in degrees
	 * @return hour angle of sunrise in radians
	 */
	private static double calcHourAngleSunrise(double lat, double solarDec,
			double zenith) {
		double latRad = Math.toRadians(lat);
		double sdRad = Math.toRadians(solarDec);

		return (Math.acos(Math.cos(Math.toRadians(zenith))
				/ (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad)
				* Math.tan(sdRad))); // in radians
	}

	/**
	 * Calculate the hour angle of the sun at sunset for the latitude
	 *
	 * @param lat
	 *            the latitude of observer in degrees
	 * @param solarDec
	 *            the declination angle of sun in degrees
	 * @return the hour angle of sunset in radians
	 * TODO: use - calcHourAngleSunrise implementation
	 */
	private static double calcHourAngleSunset(double lat, double solarDec,
			double zenith) {
		double latRad = Math.toRadians(lat);
		double sdRad = Math.toRadians(solarDec);

		double HA = (Math.acos(Math.cos(Math.toRadians(zenith))
				/ (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad)
				* Math.tan(sdRad)));
		return -HA; // in radians
	}

	/**
	 * Calculate the Universal Coordinated Time (UTC) of sunrise for the given
	 * day at the given location on earth
	 *
	 * @param julianDay
	 *            the Julian day
	 * @param latitude
	 *            the latitude of observer in degrees
	 * @param longitude
	 *            the longitude of observer in degrees
	 * @return the time in minutes from zero Z
	 */
	private static double calcSunriseUTC(double julianDay, double latitude,
			double longitude, double zenith) {
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);

		// Find the time of solar noon at the location, and use
		// that declination. This is better than start of the
		// Julian day

		double noonmin = calcSolNoonUTC(julianCenturies, longitude);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First pass to approximate sunrise (using solar noon)

		double eqTime = calcEquationOfTime(tnoon);
		double solarDec = calcSunDeclination(tnoon);
		double hourAngle = calcHourAngleSunrise(latitude, solarDec, zenith);

		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta; // in minutes of time
		double timeUTC = 720 + timeDiff - eqTime; // in minutes

		// Second pass includes fractional Julian Day in gamma calc

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) + timeUTC
				/ 1440.0);
		eqTime = calcEquationOfTime(newt);
		solarDec = calcSunDeclination(newt);
		hourAngle = calcHourAngleSunrise(latitude, solarDec, zenith);
		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - eqTime; // in minutes
		return timeUTC;
	}

	/**
	 * calculate the Universal Coordinated Time (UTC) of solar noon for the
	 * given day at the given location on earth
	 *
	 * @param t
	 *            the number of Julian centuries since J2000.0
	 * @param longitude
	 *            the longitude of observer in degrees
	 * @return the time in minutes from zero Z
	 */
	private static double calcSolNoonUTC(double t, double longitude) {
		// First pass uses approximate solar noon to calculate eqtime
		double tnoon = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(t) + longitude
				/ 360.0);
		double eqTime = calcEquationOfTime(tnoon);
		double solNoonUTC = 720 + (longitude * 4) - eqTime; // min

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(t) - 0.5
				+ solNoonUTC / 1440.0);

		eqTime = calcEquationOfTime(newt);
		return 720 + (longitude * 4) - eqTime; // min
	}

	/**
	 * calculate the Universal Coordinated Time (UTC) of sunset for the given
	 * day at the given location on earth
	 *
	 * @param julianDay
	 *            the Julian day
	 * @param latitude
	 *            the latitude of observer in degrees
	 * @param longitude :
	 *            longitude of observer in degrees
	 * @param zenith
	 * @return the time in minutes from zero Universal Coordinated Time (UTC)
	 */
	private static double calcSunsetUTC(double julianDay, double latitude,
			double longitude, double zenith) {
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);

		// Find the time of solar noon at the location, and use
		// that declination. This is better than start of the
		// Julian day

		double noonmin = calcSolNoonUTC(julianCenturies, longitude);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First calculates sunrise and approx length of day

		double eqTime = calcEquationOfTime(tnoon);
		double solarDec = calcSunDeclination(tnoon);
		double hourAngle = calcHourAngleSunset(latitude, solarDec, zenith);

		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - eqTime;

		// Second pass includes fractional Julian Day in gamma calc

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) + timeUTC
				/ 1440.0);
		eqTime = calcEquationOfTime(newt);
		solarDec = calcSunDeclination(newt);
		hourAngle = calcHourAngleSunset(latitude, solarDec, zenith);

		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - eqTime; // in minutes
		return timeUTC;
	}
}