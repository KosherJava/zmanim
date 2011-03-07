/*
 * Zmanim Java API
 * Copyright (C) 2004-2008 Eliyahu Hershfeld
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
 * Implementation of sunrise and sunset methods to calculate astronomical times.
 * This calculator uses the Java algorithm written by <a
 * href="http://www.jstot.me.uk/jsuntimes/">Jonathan Stott</a> that is based on
 * the implementation by <a href=""http://noaa.gov">NOAA - National Oceanic and
 * Atmospheric Administration</a>'s <a href =
 * "http://www.srrb.noaa.gov/highlights/sunrise/sunrisehtml">Surface Radiation
 * Research Branch</a>. NOAA's <a
 * href="http://www.srrb.noaa.gov/highlights/sunrise/solareqns.PDF">implementation</a>
 * is based on equations from <a
 * href="http://www.willbell.com/math/mc1.htm">Astronomical Algorithms</a> by
 * <a href="http://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a>. Jonathan's
 * implementation was released under the GPL. Added to the algorithm is an
 * adjustment of the zenith to account for elevation.
 *
 * @author Jonathan Stott 2000 - 2004
 * @author &copy; Eliyahu Hershfeld 2004 - 2008
 * @version 1.1
 * @deprecated This class is based on the NOAA algorithm but does not return calculations that match the NOAA algorithm JavaScript implementation. The calculations are about 2 minutes off. This call has been replaced by the NOAACalculator class.
 * @see net.sourceforge.zmanim.util.NOAACalculator
 */
public class JSuntimeCalculator extends AstronomicalCalculator {
	private String calculatorName =  "US National Oceanic and Atmospheric Administration Algorithm";
	/**
	 * @deprecated This class is based on the NOAA algorithm but does not return calculations that match the NOAA algorithm JavaScript implementation. The calculations are about 2 minutes off. This call has been replaced by the NOAACalculator class.
	 * @see net.sourceforge.zmanim.util.NOAACalculator#getCalculatorName()
	 */
	public String getCalculatorName() {
		return calculatorName;
	}

	/**
	 * @deprecated This class is based on the NOAA algorithm but does not return calculations that match the NOAA algorithm JavaScript implementation. The calculations are about 2 minutes off. This call has been replaced by the NOAACalculator class.
	 * @see net.sourceforge.zmanim.util.NOAACalculator#getUTCSunrise(AstronomicalCalendar, double, boolean)
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunrise(AstronomicalCalendar,
	 *      double, boolean)
	 * @throws ZmanimException
	 *             if the year entered == 2000. This calculator can't properly
	 *             deal with the year 2000. It can properly calculate times for
	 *             years <> 2000.
	 */
	public double getUTCSunrise(AstronomicalCalendar astronomicalCalendar, double zenith, boolean adjustForElevation) {
//		if (astronomicalCalendar.getCalendar().get(Calendar.YEAR) == 2000) {
//			throw new ZmanimException(
//					"JSuntimeCalculator can not calculate times for the year 2000. Please try a date with a different year.");
//		}

		if (adjustForElevation) {
			zenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation()
					.getElevation());
		} else {
			zenith = adjustZenith(zenith, 0);
		}
		double timeMins = morningPhenomenon(dateToJulian(astronomicalCalendar
				.getCalendar()), astronomicalCalendar.getGeoLocation()
				.getLatitude(), -astronomicalCalendar.getGeoLocation()
				.getLongitude(), zenith);
		return timeMins / 60;
	}

	/**
	 * @deprecated  This class is based on the NOAA algorithm but does not return calculations that match the NOAAA algorithm JavaScript implementation. The calculations are about 2 minutes off. This call has been replaced by the NOAACalculator class.
	 * @see net.sourceforge.zmanim.util.NOAACalculator#getUTCSunset(AstronomicalCalendar, double, boolean)
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunset(AstronomicalCalendar,
	 *      double, boolean)
	 * @throws ZmanimException
	 *             if the year entered == 2000. This calculator can't properly
	 *             deal with the year 2000. It can properly calculate times for
	 *             years <> 2000.
	 */
	public double getUTCSunset(AstronomicalCalendar astronomicalCalendar, double zenith, boolean adjustForElevation) {
//		if (astronomicalCalendar.getCalendar().get(Calendar.YEAR) == 2000) {
//			throw new ZmanimException(
//					"JSuntimeCalculator can not calculate times for the year 2000. Please try a date with a different year.");
//		}

		if (adjustForElevation) {
			zenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation()
					.getElevation());
		} else {
			zenith = adjustZenith(zenith, 0);
		}
		double timeMins = eveningPhenomenon(dateToJulian(astronomicalCalendar
				.getCalendar()), astronomicalCalendar.getGeoLocation()
				.getLatitude(), -astronomicalCalendar.getGeoLocation()
				.getLongitude(), zenith);
		return timeMins / 60;
	}

	/**
	 * Calculate the UTC of a morning phenomenon for the given day at the given
	 * latitude and longitude on Earth
	 *
	 * @param julian
	 *            Julian day
	 * @param latitude
	 *            latitude of observer in degrees
	 * @param longitude
	 *            longitude of observer in degrees
	 * @param zenithDistance
	 *            one of Sun.SUNRISE_SUNSET_ZENITH_DISTANCE,
	 *            Sun.CIVIL_TWILIGHT_ZENITH_DISTANCE,
	 *            Sun.NAUTICAL_TWILIGHT_ZENITH_DISTANCE,
	 *            Sun.ASTRONOMICAL_TWILIGHT_ZENITH_DISTANCE.
	 * @return time in minutes from zero Z
	 */
	private static double morningPhenomenon(double julian, double latitude,
			double longitude, double zenithDistance) {
		double t = julianDayToJulianCenturies(julian);
		double eqtime = equationOfTime(t);
		double solarDec = sunDeclination(t);
		double hourangle = hourAngleMorning(latitude, solarDec, zenithDistance);
		double delta = longitude - Math.toDegrees(hourangle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - eqtime;

		// Second pass includes fractional julian day in gamma calc
		double newt = julianDayToJulianCenturies(julianCenturiesToJulianDay(t)
				+ timeUTC / 1440);
		eqtime = equationOfTime(newt);
		solarDec = sunDeclination(newt);
		hourangle = hourAngleMorning(latitude, solarDec, zenithDistance);
		delta = longitude - Math.toDegrees(hourangle);
		timeDiff = 4 * delta;

		double morning = 720 + timeDiff - eqtime;
		return morning;
	}

	/**
	 * Calculate the UTC of an evening phenomenon for the given day at the given
	 * latitude and longitude on Earth
	 *
	 * @param julian
	 *            Julian day
	 * @param latitude
	 *            latitude of observer in degrees
	 * @param longitude
	 *            longitude of observer in degrees
	 * @param zenithDistance
	 *            one of Sun.SUNRISE_SUNSET_ZENITH_DISTANCE,
	 *            Sun.CIVIL_TWILIGHT_ZENITH_DISTANCE,
	 *            Sun.NAUTICAL_TWILIGHT_ZENITH_DISTANCE,
	 *            Sun.ASTRONOMICAL_TWILIGHT_ZENITH_DISTANCE.
	 * @return time in minutes from zero Z
	 */
	private static double eveningPhenomenon(double julian, double latitude,
			double longitude, double zenithDistance) {
		double t = julianDayToJulianCenturies(julian);

		// First calculates sunrise and approx length of day
		double eqtime = equationOfTime(t);
		double solarDec = sunDeclination(t);
		double hourangle = hourAngleEvening(latitude, solarDec, zenithDistance);

		double delta = longitude - Math.toDegrees(hourangle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - eqtime;

		// first pass used to include fractional day in gamma calc
		double newt = julianDayToJulianCenturies(julianCenturiesToJulianDay(t)
				+ timeUTC / 1440);
		eqtime = equationOfTime(newt);
		solarDec = sunDeclination(newt);
		hourangle = hourAngleEvening(latitude, solarDec, zenithDistance);

		delta = longitude - Math.toDegrees(hourangle);
		timeDiff = 4 * delta;

		double evening = 720 + timeDiff - eqtime;
		return evening;
	}

	private static double dateToJulian(Calendar date) {
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);

		double extra = (100.0 * year) + month - 190002.5;
		double JD = (367.0 * year)
				- (Math
						.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0))
				+ Math.floor((275.0 * month) / 9.0) + day
				+ ((hour + ((minute + (second / 60.0)) / 60.0)) / 24.0)
				+ 1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
		return JD;
	}

	/**
	 * Convert Julian Day to centuries since J2000.0
	 *
	 * @param julian
	 *            The Julian Day to convert
	 * @return the value corresponding to the Julian Day
	 */
	private static double julianDayToJulianCenturies(double julian) {
		return (julian - 2451545) / 36525;
	}

	/**
	 * Convert centuries since J2000.0 to Julian Day
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return The Julian Day corresponding to the value of t
	 */
	private static double julianCenturiesToJulianDay(double t) {
		return (t * 36525) + 2451545;
	}

	/**
	 * Calculate the difference between true solar time and mean solar time
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return
	 */
	private static double equationOfTime(double t) {
		double epsilon = obliquityCorrection(t);
		double l0 = geomMeanLongSun(t);
		double e = eccentricityOfEarthsOrbit(t);
		double m = geometricMeanAnomalyOfSun(t);
		double y = Math.pow((Math.tan(Math.toRadians(epsilon) / 2)), 2);

		double eTime = y * Math.sin(2 * Math.toRadians(l0)) - 2 * e
				* Math.sin(Math.toRadians(m)) + 4 * e * y
				* Math.sin(Math.toRadians(m))
				* Math.cos(2 * Math.toRadians(l0)) - 0.5 * y * y
				* Math.sin(4 * Math.toRadians(l0)) - 1.25 * e * e
				* Math.sin(2 * Math.toRadians(m));
		return Math.toDegrees(eTime) * 4;
	}

	/**
	 * Calculate the declination of the sun
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return The Sun's declination in degrees
	 */
	private static double sunDeclination(double t) {
		double e = obliquityCorrection(t);
		double lambda = sunsApparentLongitude(t);

		double sint = Math.sin(Math.toRadians(e))
				* Math.sin(Math.toRadians(lambda));
		return Math.toDegrees(Math.asin(sint));
	}

	/**
	 * calculate the hour angle of the sun for a morning phenomenon for the
	 * given latitude
	 *
	 * @param lat
	 *            Latitude of the observer in degrees
	 * @param solarDec
	 *            declination of the sun in degrees
	 * @param zenithDistance
	 *            zenith distance of the sun in degrees
	 * @return hour angle of sunrise in radians
	 */
	private static double hourAngleMorning(double lat, double solarDec,
			double zenithDistance) {
		return (Math.acos(Math.cos(Math.toRadians(zenithDistance))
				/ (Math.cos(Math.toRadians(lat)) * Math.cos(Math
						.toRadians(solarDec))) - Math.tan(Math.toRadians(lat))
				* Math.tan(Math.toRadians(solarDec))));
	}

	/**
	 * Calculate the hour angle of the sun for an evening phenomenon for the
	 * given latitude
	 *
	 * @param lat
	 *            Latitude of the observer in degrees
	 * @param solarDec
	 *            declination of the Sun in degrees
	 * @param zenithDistance
	 *            zenith distance of the sun in degrees
	 * @return hour angle of sunset in radians
	 */
	private static double hourAngleEvening(double lat, double solarDec,
			double zenithDistance) {
		return -hourAngleMorning(lat, solarDec, zenithDistance);
	}

	/**
	 * Calculate the corrected obliquity of the ecliptic
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return Corrected obliquity in degrees
	 */
	private static double obliquityCorrection(double t) {
		return meanObliquityOfEcliptic(t) + 0.00256
				* Math.cos(Math.toRadians(125.04 - 1934.136 * t));
	}

	/**
	 * Calculate the mean obliquity of the ecliptic
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return Mean obliquity in degrees
	 */
	private static double meanObliquityOfEcliptic(double t) {
		return 23 + (26 + (21.448 - t
				* (46.815 + t * (0.00059 - t * (0.001813))) / 60)) / 60;
	}

	/**
	 * Calculate the geometric mean longitude of the sun
	 *
	 * @param t
	 *            number of Julian centuries since J2000.0
	 * @return the geometric mean longitude of the sun in degrees
	 */
	private static double geomMeanLongSun(double t) {
		double l0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);

		while ((l0 >= 0) && (l0 <= 360)) {
			if (l0 > 360) {
				l0 = l0 - 360;
			}

			if (l0 < 0) {
				l0 = l0 + 360;
			}
		}
		return l0;
	}

	/**
	 * Calculate the eccentricity of Earth's orbit
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return the eccentricity
	 */
	private static double eccentricityOfEarthsOrbit(double t) {
		return 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
	}

	/**
	 * Calculate the geometric mean anomaly of the Sun
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return the geometric mean anomaly of the Sun in degrees
	 */
	private static double geometricMeanAnomalyOfSun(double t) {
		return 357.52911 + t * (35999.05029 - 0.0001537 * t);
	}

	/**
	 * Calculate the apparent longitude of the sun
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return The apparent longitude of the Sun in degrees
	 */
	private static double sunsApparentLongitude(double t) {
		return sunsTrueLongitude(t) - 0.00569 - 0.00478
				* Math.sin(Math.toRadians(125.04 - 1934.136 * t));
	}

	/**
	 * Calculate the true longitude of the sun
	 *
	 * @param t
	 *            Number of Julian centuries since J2000.0
	 * @return The Sun's true longitude in degrees
	 */
	private static double sunsTrueLongitude(double t) {
		return geomMeanLongSun(t) + equationOfCentreForSun(t);
	}

	/**
	 * Calculate the equation of centre for the Sun
	 *
	 * @param centuries
	 *            Number of Julian centuries since J2000.0
	 * @return The equation of centre for the Sun in degrees
	 */
	private static double equationOfCentreForSun(double t) {
		double m = geometricMeanAnomalyOfSun(t);

		return Math.sin(Math.toRadians(m))
				* (1.914602 - t * (0.004817 + 0.000014 * t))
				+ Math.sin(2 * Math.toRadians(m)) * (0.019993 - 0.000101 * t)
				+ Math.sin(3 * Math.toRadians(m)) * 0.000289;
	}
}