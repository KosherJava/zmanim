/*
 * Zmanim Java API
 * Copyright (C) 2004-2011 Eliyahu Hershfeld
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
package net.sourceforge.zmanim.util;

import java.util.Calendar;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times based on the <a
 * href=""http://noaa.gov">NOAA</a> algorithm. This calculator uses the Java algorithm based on the implementation by <a
 * href=""http://noaa.gov">NOAA - National Oceanic and Atmospheric Administration</a>'s <a href =
 * "http://www.srrb.noaa.gov/highlights/sunrise/sunrise.html">Surface Radiation Research Branch</a>. NOAA's <a
 * href="http://www.srrb.noaa.gov/highlights/sunrise/solareqns.PDF">implementation</a> is based on equations from <a
 * href="http://www.willbell.com/math/mc1.htm">Astronomical Algorithms</a> by <a
 * href="http://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a>. Added to the algorithm is an adjustment of the zenith
 * to account for elevation. The algorithm can be found in the <a
 * href="http://en.wikipedia.org/wiki/Sunrise_equation">Wikipedia Sunrise Equation</a> article.
 * 
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.1
 */
public class NOAACalculator extends AstronomicalCalculator {
	/**
	 * The <a href="http://en.wikipedia.org/wiki/Julian_day">Julian day</a> of January 1, 2000
	 */
	private static final double JULIAN_DAY_JAN_1_2000 = 2451545.0;

	/**
	 * Julian days per century
	 */
	private static final double JULIAN_DAYS_PER_CENTURY = 36525.0;

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getCalculatorName()
	 */
	public String getCalculatorName() {
		return "US National Oceanic and Atmospheric Administration Algorithm";
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunrise(Calendar, GeoLocation, double, boolean)
	 */
	public double getUTCSunrise(Calendar calendar, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation);

		double sunrise = getSunriseUTC(getJulianDay(calendar), geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith);
		sunrise = sunrise / 60;

		// ensure that the time is >= 0 and < 24
		while (sunrise < 0.0) {
			sunrise += 24.0;
		}
		while (sunrise >= 24.0) {
			sunrise -= 24.0;
		}
		return sunrise;
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunset(Calendar, GeoLocation, double, boolean)
	 */
	public double getUTCSunset(Calendar calendar, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation);

		double sunset = getSunsetUTC(getJulianDay(calendar), geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith);
		sunset = sunset / 60;

		// ensure that the time is >= 0 and < 24
		while (sunset < 0.0) {
			sunset += 24.0;
		}
		while (sunset >= 24.0) {
			sunset -= 24.0;
		}
		return sunset;
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Julian_day">Julian day</a> from a Java Calendar
	 * 
	 * @param calendar
	 *            The Java Calendar
	 * @return the Julian day corresponding to the date Note: Number is returned for start of day. Fractional days
	 *         should be added later.
	 */
	private static double getJulianDay(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (month <= 2) {
			year -= 1;
			month += 12;
		}
		int a = year / 100;
		int b = 2 - a + a / 4;

		return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
	}

	/**
	 * Convert <a href="http://en.wikipedia.org/wiki/Julian_day">Julian day</a> to centuries since J2000.0.
	 * 
	 * @param julianDay
	 *            the Julian Day to convert
	 * @return the centuries since 2000 Julian corresponding to the Julian Day
	 */
	private static double getJulianCenturiesFromJulianDay(double julianDay) {
		return (julianDay - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
	}

	/**
	 * Convert centuries since J2000.0 to <a href="http://en.wikipedia.org/wiki/Julian_day">Julian day</a>.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Julian Day corresponding to the Julian centuries passed in
	 */
	private static double getJulianDayFromJulianCenturies(double julianCenturies) {
		return julianCenturies * JULIAN_DAYS_PER_CENTURY + JULIAN_DAY_JAN_1_2000;
	}

	/**
	 * Returns the Geometric <a href="http://en.wikipedia.org/wiki/Mean_longitude">Mean Longitude</a> of the Sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Geometric Mean Longitude of the Sun in degrees
	 */
	private static double getSunGeometricMeanLongitude(double julianCenturies) {
		double longitude = 280.46646 + julianCenturies * (36000.76983 + 0.0003032 * julianCenturies);
		while (longitude > 360.0) {
			longitude -= 360.0;
		}
		while (longitude < 0.0) {
			longitude += 360.0;
		}

		return longitude; // in degrees
	}

	/**
	 * Returns the Geometric <a href="http://en.wikipedia.org/wiki/Mean_anomaly">Mean Anomaly</a> of the Sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the Geometric Mean Anomaly of the Sun in degrees
	 */
	private static double getSunGeometricMeanAnomaly(double julianCenturies) {
		return 357.52911 + julianCenturies * (35999.05029 - 0.0001537 * julianCenturies); // in degrees
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Eccentricity_%28orbit%29">eccentricity of earth's orbit</a>.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the unitless eccentricity
	 */
	private static double getEarthOrbitEccentricity(double julianCenturies) {
		return 0.016708634 - julianCenturies * (0.000042037 + 0.0000001267 * julianCenturies); // unitless
	}

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Equation_of_the_center">equation of center</a> for the sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the equation of center for the sun in degrees
	 */
	private static double getSunEquationOfCenter(double julianCenturies) {
		double m = getSunGeometricMeanAnomaly(julianCenturies);

		double mrad = Math.toRadians(m);
		double sinm = Math.sin(mrad);
		double sin2m = Math.sin(mrad + mrad);
		double sin3m = Math.sin(mrad + mrad + mrad);

		return sinm * (1.914602 - julianCenturies * (0.004817 + 0.000014 * julianCenturies)) + sin2m
				* (0.019993 - 0.000101 * julianCenturies) + sin3m * 0.000289;// in degrees
	}

	/**
	 * Return the true longitude of the sun
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the sun's true longitude in degrees
	 */
	private static double getSunTrueLongitude(double julianCenturies) {
		double sunLongitude = getSunGeometricMeanLongitude(julianCenturies);
		double center = getSunEquationOfCenter(julianCenturies);

		return sunLongitude + center; // in degrees
	}

	// /**
	// * Returns the <a href="http://en.wikipedia.org/wiki/True_anomaly">true anamoly</a> of the sun.
	// *
	// * @param julianCenturies
	// * the number of Julian centuries since J2000.0
	// * @return the sun's true anamoly in degrees
	// */
	// private static double getSunTrueAnomaly(double julianCenturies) {
	// double meanAnomaly = getSunGeometricMeanAnomaly(julianCenturies);
	// double equationOfCenter = getSunEquationOfCenter(julianCenturies);
	//
	// return meanAnomaly + equationOfCenter; // in degrees
	// }

	/**
	 * Return the apparent longitude of the sun
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return sun's apparent longitude in degrees
	 */
	private static double getSunApparentLongitude(double julianCenturies) {
		double sunTrueLongitude = getSunTrueLongitude(julianCenturies);

		double omega = 125.04 - 1934.136 * julianCenturies;
		double lambda = sunTrueLongitude - 0.00569 - 0.00478 * Math.sin(Math.toRadians(omega));
		return lambda; // in degrees
	}

	/**
	 * Returns the mean <a href="http://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial tilt).
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the mean obliquity in degrees
	 */
	private static double getMeanObliquityOfEcliptic(double julianCenturies) {
		double seconds = 21.448 - julianCenturies
				* (46.8150 + julianCenturies * (0.00059 - julianCenturies * (0.001813)));
		return 23.0 + (26.0 + (seconds / 60.0)) / 60.0; // in degrees
	}

	/**
	 * Returns the corrected <a href="http://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial
	 * tilt).
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return the corrected obliquity in degrees
	 */
	private static double getObliquityCorrection(double julianCenturies) {
		double obliquityOfEcliptic = getMeanObliquityOfEcliptic(julianCenturies);

		double omega = 125.04 - 1934.136 * julianCenturies;
		return obliquityOfEcliptic + 0.00256 * Math.cos(Math.toRadians(omega)); // in degrees
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Declination">declination</a> of the sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @param sun
	 *            's declination in degrees
	 */
	private static double getSunDeclination(double julianCenturies) {
		double obliquityCorrection = getObliquityCorrection(julianCenturies);
		double lambda = getSunApparentLongitude(julianCenturies);

		double sint = Math.sin(Math.toRadians(obliquityCorrection)) * Math.sin(Math.toRadians(lambda));
		double theta = Math.toDegrees(Math.asin(sint));
		return theta; // in degrees
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Equation_of_time">Equation of Time</a> - the difference between
	 * true solar time and mean solar time
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @return equation of time in minutes of time
	 */
	private static double getEquationOfTime(double julianCenturies) {
		double epsilon = getObliquityCorrection(julianCenturies);
		double geomMeanLongSun = getSunGeometricMeanLongitude(julianCenturies);
		double eccentricityEarthOrbit = getEarthOrbitEccentricity(julianCenturies);
		double geomMeanAnomalySun = getSunGeometricMeanAnomaly(julianCenturies);

		double y = Math.tan(Math.toRadians(epsilon) / 2.0);
		y *= y;

		double sin2l0 = Math.sin(2.0 * Math.toRadians(geomMeanLongSun));
		double sinm = Math.sin(Math.toRadians(geomMeanAnomalySun));
		double cos2l0 = Math.cos(2.0 * Math.toRadians(geomMeanLongSun));
		double sin4l0 = Math.sin(4.0 * Math.toRadians(geomMeanLongSun));
		double sin2m = Math.sin(2.0 * Math.toRadians(geomMeanAnomalySun));

		double equationOfTime = y * sin2l0 - 2.0 * eccentricityEarthOrbit * sinm + 4.0 * eccentricityEarthOrbit * y
				* sinm * cos2l0 - 0.5 * y * y * sin4l0 - 1.25 * eccentricityEarthOrbit * eccentricityEarthOrbit * sin2m;
		return Math.toDegrees(equationOfTime) * 4.0; // in minutes of time
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Hour_angle">hour angle</a> of the sun at sunrise for the
	 * latitude.
	 * 
	 * @param lat
	 *            , the latitude of observer in degrees
	 * @param solarDec
	 *            the declination angle of sun in degrees
	 * @return hour angle of sunrise in radians
	 */
	private static double getSunHourAngleAtSunrise(double lat, double solarDec, double zenith) {
		double latRad = Math.toRadians(lat);
		double sdRad = Math.toRadians(solarDec);

		return (Math.acos(Math.cos(Math.toRadians(zenith)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad)
				* Math.tan(sdRad))); // in radians
	}

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Hour_angle">hour angle</a> of the sun at sunset for the
	 * latitude. TODO: use - {@link #getSunHourAngleAtSunrise(double, double, double)} implementation to avoid
	 * duplication of code.
	 * 
	 * @param lat
	 *            the latitude of observer in degrees
	 * @param solarDec
	 *            the declination angle of sun in degrees
	 * @return the hour angle of sunset in radians
	 */
	private static double getSunHourAngleAtSunset(double lat, double solarDec, double zenith) {
		double latRad = Math.toRadians(lat);
		double sdRad = Math.toRadians(solarDec);

		double hourAngle = (Math.acos(Math.cos(Math.toRadians(zenith)) / (Math.cos(latRad) * Math.cos(sdRad))
				- Math.tan(latRad) * Math.tan(sdRad)));
		return -hourAngle; // in radians
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of sunrise for the given day at the given location on earth
	 * 
	 * @param julianDay
	 *            the Julian day
	 * @param latitude
	 *            the latitude of observer in degrees
	 * @param longitude
	 *            the longitude of observer in degrees
	 * @return the time in minutes from zero UTC
	 */
	private static double getSunriseUTC(double julianDay, double latitude, double longitude, double zenith) {
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);

		// Find the time of solar noon at the location, and use that declination. This is better than start of the
		// Julian day

		double noonmin = getSolarNoonUTC(julianCenturies, longitude);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First pass to approximate sunrise (using solar noon)

		double eqTime = getEquationOfTime(tnoon);
		double solarDec = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngleAtSunrise(latitude, solarDec, zenith);

		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta; // in minutes of time
		double timeUTC = 720 + timeDiff - eqTime; // in minutes

		// Second pass includes fractional Julian Day in gamma calc

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) + timeUTC
				/ 1440.0);
		eqTime = getEquationOfTime(newt);
		solarDec = getSunDeclination(newt);
		hourAngle = getSunHourAngleAtSunrise(latitude, solarDec, zenith);
		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - eqTime; // in minutes
		return timeUTC;
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of <a href="http://en.wikipedia.org/wiki/Noon#Solar_noon">solar noon</a> for the given day at the given location
	 * on earth.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since J2000.0
	 * @param longitude
	 *            the longitude of observer in degrees
	 * @return the time in minutes from zero UTC
	 */
	private static double getSolarNoonUTC(double julianCenturies, double longitude) {
		// First pass uses approximate solar noon to calculate eqtime
		double tnoon = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) + longitude
				/ 360.0);
		double eqTime = getEquationOfTime(tnoon);
		double solNoonUTC = 720 + (longitude * 4) - eqTime; // min

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) - 0.5
				+ solNoonUTC / 1440.0);

		eqTime = getEquationOfTime(newt);
		return 720 + (longitude * 4) - eqTime; // min
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of sunset for the given day at the given location on earth
	 * 
	 * @param julianDay
	 *            the Julian day
	 * @param latitude
	 *            the latitude of observer in degrees
	 * @param longitude
	 *            : longitude of observer in degrees
	 * @param zenith
	 * @return the time in minutes from zero Universal Coordinated Time (UTC)
	 */
	private static double getSunsetUTC(double julianDay, double latitude, double longitude, double zenith) {
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);

		// Find the time of solar noon at the location, and use that declination. This is better than start of the
		// Julian day

		double noonmin = getSolarNoonUTC(julianCenturies, longitude);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First calculates sunrise and approx length of day

		double eqTime = getEquationOfTime(tnoon);
		double solarDec = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngleAtSunset(latitude, solarDec, zenith);

		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - eqTime;

		// Second pass includes fractional Julian Day in gamma calc

		double newt = getJulianCenturiesFromJulianDay(getJulianDayFromJulianCenturies(julianCenturies) + timeUTC
				/ 1440.0);
		eqTime = getEquationOfTime(newt);
		solarDec = getSunDeclination(newt);
		hourAngle = getSunHourAngleAtSunset(latitude, solarDec, zenith);

		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - eqTime; // in minutes
		return timeUTC;
	}
}