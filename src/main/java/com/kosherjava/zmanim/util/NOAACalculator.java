/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
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
package com.kosherjava.zmanim.util;

import java.time.ZoneOffset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times based on the <a
 * href="https://noaa.gov">NOAA</a> algorithm. This calculator uses the Java algorithm based on the implementation by <a
 * href="https://noaa.gov">NOAA - National Oceanic and Atmospheric Administration</a>'s <a href =
 * "https://www.srrb.noaa.gov/highlights/sunrise/sunrise.html">Surface Radiation Research Branch</a>. NOAA's <a
 * href="https://www.srrb.noaa.gov/highlights/sunrise/solareqns.PDF">implementation</a> is based on equations from <a
 * href="https://www.amazon.com/Astronomical-Table-Sun-Moon-Planets/dp/1942675038/">Astronomical Algorithms</a> by <a
 * href="https://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a>. Added to the algorithm is an adjustment of the zenith
 * to account for elevation. The algorithm can be found in the <a
 * href="https://en.wikipedia.org/wiki/Sunrise_equation">Wikipedia Sunrise Equation</a> article.
 * 
 * @author © Eliyahu Hershfeld 2011 - 2026
 */
public class NOAACalculator extends AstronomicalCalculator {
	
	/**
	 * The <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> of January 1, 2000, known as
	 * <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 */
	private static final double JULIAN_DAY_JAN_1_2000 = 2451545.0;

	/**
	 * Julian days per century.
	 */
	private static final double JULIAN_DAYS_PER_CENTURY = 36525.0;
	
	/**
	 * An {@code enum} to indicate what type of solar event ({@link #SUNRISE SUNRISE}, {@link #SUNSET SUNSET},
	 * {@link #NOON NOON} or {@link #MIDNIGHT MIDNIGHT}) is being calculated.
	 * .
	 */
	protected enum SolarEvent {
		/**SUNRISE A solar event related to sunrise*/SUNRISE, /**SUNSET A solar event related to sunset*/SUNSET,
		/**NOON A solar event related to noon*/NOON, /**MIDNIGHT A solar event related to midnight*/MIDNIGHT
	}
	
	/**
	 * Default constructor of the NOAACalculator.
	 */
	public NOAACalculator() {
		super();
	}

	@Override
	public String getCalculatorName() {
		return "US National Oceanic and Atmospheric Administration Algorithm"; // Implementation of the Jean Meeus algorithm
	}

	@Override
	public double getUTCSunrise(LocalDate dt, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		return getUTCSunRiseSet(dt, geoLocation, zenith, adjustForElevation,SolarEvent.SUNRISE);
	}

	@Override
	public double getUTCSunset(LocalDate dt, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		return getUTCSunRiseSet(dt, geoLocation, zenith, adjustForElevation,SolarEvent.SUNSET);
	}
	
	/**
	 * A method that calculates UTC sunrise or sunset as well as any time based on an angle above or below sunset and
	 * returns it as a {@code double} in 24-hour format. 5:45:00 AM will return 5.75.
	 * 
	 * @param localDate Used to calculate day of year.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param zenith the azimuth below the vertical zenith of 90&deg;. For sunset typically the {@link #adjustZenith zenith} used for
	 *         the calculation uses geometric zenith of 90&deg; and {@link #adjustZenith adjusts} this slightly to account for solar
	 *         refraction and the sun's radius. Another example would be
	 *         {@link com.kosherjava.zmanim.AstronomicalCalendar#getEndNauticalTwilight()} that passes
	 *         {@link com.kosherjava.zmanim.AstronomicalCalendar#NAUTICAL_ZENITH} to this method.
	 * @param adjustForElevation Should the time be adjusted for elevation
	 * @param solarEvent if the calculation is for {@link SolarEvent#SUNRISE} or {@link SolarEvent#SUNSET}
	 * @return The UTC time of sunrise or sunset in 24-hour format. 5:45:00 AM will return 5.75, while 5:45 PM will return 17.75. If
	 *         an error was encountered in the calculation (expected behavior for some locations such as near the poles,
	 *         {@link Double#NaN} will be returned.
	 * @see #getElevationAdjustment(double, double)
	 */
	private double getUTCSunRiseSet(LocalDate localDate, GeoLocation geoLocation, double zenith, boolean adjustForElevation,
			SolarEvent solarEvent) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation, geoLocation.getLatitude(), localDate);
		double riseSet = getSunRiseSetUTC(localDate, geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith, solarEvent);
		riseSet = riseSet / 60;
		return (riseSet % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> from a Java {@code LocalDate}.
	 * 
	 * @param localDate The LocalDate
	 * @return the Julian day corresponding to the date Note: Number is returned for the start of the Julian day. Fractional days
	 *         / time should be added later.
	 */
	private static double getJulianDay(LocalDate localDate) {
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();

		if (month <= 2) {
			year -= 1;
			month += 12;
		}

		int a = year / 100;
		int b = 2 - a + a / 4;

		return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
	}

	/**
	 * Convert <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> to centuries since
	 * <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * 
	 * @param julianDay the Julian Day to convert
	 * @return the centuries since 2000 Julian corresponding to the Julian Day
	 */
	private static double getJulianCenturiesFromJulianDay(double julianDay) {
		return (julianDay - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
	}

	/**
	 * Returns the Geometric <a href="https://en.wikipedia.org/wiki/Mean_longitude">Mean Longitude</a> of the Sun.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the Geometric Mean Longitude of the Sun in degrees
	 */
	private static double getSunGeometricMeanLongitude(double julianCenturies) {
		double longitude = 280.46646 + julianCenturies * (36000.76983 + 0.0003032 * julianCenturies);
		return (longitude % 360 + 360) % 360; // return a longitude is in the range of 0 - 360
	}

	/**
	 * Returns the Geometric <a href="https://en.wikipedia.org/wiki/Mean_anomaly">Mean Anomaly</a> of the Sun in degrees.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the Geometric Mean Anomaly of the Sun in degrees
	 */
	private static double getSunGeometricMeanAnomaly(double julianCenturies) {
		return 357.52911 + julianCenturies * (35999.05029 - 0.0001537 * julianCenturies);
	}

	/**
	 * Return the unitless <a href="https://en.wikipedia.org/wiki/Eccentricity_%28orbit%29">eccentricity of earth's orbit</a>.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the unitless eccentricity
	 */
	private static double getEarthOrbitEccentricity(double julianCenturies) {
		return 0.016708634 - julianCenturies * (0.000042037 + 0.0000001267 * julianCenturies);
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Equation_of_the_center">equation of center</a> for the sun in degrees.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the equation of center for the sun in degrees
	 */
	private static double getSunEquationOfCenter(double julianCenturies) {
		double m = getSunGeometricMeanAnomaly(julianCenturies);
		double sinm = sinDegrees(m);
		double sin2m = sinDegrees(m + m);
		double sin3m = sinDegrees(m + m + m);
		return sinm * (1.914602 - julianCenturies * (0.004817 + 0.000014 * julianCenturies)) + sin2m
				* (0.019993 - 0.000101 * julianCenturies) + sin3m * 0.000289;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/True_longitude">true longitude</a> of the sun.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the sun's true longitude in degrees
	 */
	private static double getSunTrueLongitude(double julianCenturies) {
		double sunLongitude = getSunGeometricMeanLongitude(julianCenturies);
		double center = getSunEquationOfCenter(julianCenturies); 
		return sunLongitude + center;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Apparent_longitude">apparent longitude</a> of the sun.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return sun's apparent longitude in degrees
	 */
	private static double getSunApparentLongitude(double julianCenturies) {
		double sunTrueLongitude = getSunTrueLongitude(julianCenturies);
		double omega = 125.04 - 1934.136 * julianCenturies;
		return sunTrueLongitude - 0.00569 - 0.00478 * sinDegrees(omega);
	}

	/**
	 * Returns the mean <a href="https://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial tilt).
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the mean obliquity in degrees
	 */
	private static double getMeanObliquityOfEcliptic(double julianCenturies) {
		double seconds = 21.448 - julianCenturies
				* (46.8150 + julianCenturies * (0.00059 - julianCenturies * (0.001813)));
		return 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
	}

	/**
	 * Returns the corrected <a href="https://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial tilt).
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the corrected obliquity in degrees
	 */
	private static double getObliquityCorrection(double julianCenturies) {
		double obliquityOfEcliptic = getMeanObliquityOfEcliptic(julianCenturies);
		double omega = 125.04 - 1934.136 * julianCenturies;
		return obliquityOfEcliptic + 0.00256 * cosDegrees(omega);
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Declination">declination</a> of the sun.
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the sun's declination in degrees
	 */
	private static double getSunDeclination(double julianCenturies) {
		double obliquityCorrection = getObliquityCorrection(julianCenturies);
		double lambda = getSunApparentLongitude(julianCenturies);
		double sint = sinDegrees(obliquityCorrection) * sinDegrees(lambda);
		return asinDegrees(sint);
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Equation_of_time">Equation of Time</a> - the difference between
	 * true solar time and mean solar time
	 * 
	 * @param julianCenturies the number of Julian centuries since
	 *         <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return equation of time in minutes of time
	 */
	private static double getEquationOfTime(double julianCenturies) {
		double epsilon = getObliquityCorrection(julianCenturies);
		double geomMeanLongSun = getSunGeometricMeanLongitude(julianCenturies);
		double eccentricityEarthOrbit = getEarthOrbitEccentricity(julianCenturies);
		double geomMeanAnomalySun = getSunGeometricMeanAnomaly(julianCenturies);
		double y = tanDegrees(epsilon / 2.0);
		y *= y;
		double sin2l0 =sinDegrees(2.0 * geomMeanLongSun);
		double sinm = sinDegrees(geomMeanAnomalySun);
		double cos2l0 = cosDegrees(2.0 * geomMeanLongSun);
		double sin4l0 = sinDegrees(4.0 * geomMeanLongSun);
		double sin2m = sinDegrees(2.0 * geomMeanAnomalySun);
		double equationOfTime = y * sin2l0 - 2.0 * eccentricityEarthOrbit * sinm + 4.0 * eccentricityEarthOrbit * y
				* sinm * cos2l0 - 0.5 * y * y * sin4l0 - 1.25 * eccentricityEarthOrbit * eccentricityEarthOrbit * sin2m;
		return Math.toDegrees(equationOfTime) * 4.0;
	}
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Hour_angle">hour angle</a> of the sun in
	 * <a href="https://en.wikipedia.org/wiki/Radian">radians</a> at for the latitude.
	 * 
	 * @param latitude the latitude of observer in degrees
	 * @param solarDeclination the declination angle of sun in degrees
	 * @param zenith the zenith
	 * @param solarEvent If the hour angle is for {@link SolarEvent#SUNRISE SUNRISE} or {@link SolarEvent#SUNSET SUNSET}
	 * @return hour angle of sunrise in <a href="https://en.wikipedia.org/wiki/Radian">radians</a>
	 */
	private static double getSunHourAngle(double latitude, double solarDeclination, double zenith, SolarEvent solarEvent) {
		double ratio = cosDegrees(zenith) / (cosDegrees(latitude) * cosDegrees(solarDeclination)) - tanDegrees(latitude)
				* tanDegrees(solarDeclination);
		double hourAngle = Math.acos(ratio);
		
		if (solarEvent == SolarEvent.SUNSET) {
			hourAngle = -hourAngle;
		}
		return hourAngle;
	}



	@Override
	public double getSolarElevation(Instant instant, GeoLocation geoLocation) {
		return getSolarElevationAzimuth(instant, geoLocation, false);
	}

	@Override
	public double getSolarAzimuth(Instant instant, GeoLocation geoLocation) {
		return getSolarElevationAzimuth(instant, geoLocation, true);
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Elevation</a> or
	 * <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Azimuth</a> at the given location
	 * and time. Can be negative if the sun is below the horizon. Elevation is based on sea-level and is not
	 * adjusted for altitude.
	 * 
	 * @param instant date-time of calculation
	 * @param geoLocation The location for calculating the elevation or azimuth.
	 * @param isAzimuth true for azimuth, false for elevation
	 * @return solar elevation or azimuth in degrees.
	 * @see #getSolarElevation(Instant, GeoLocation)
	 * @see #getSolarAzimuth(Instant, GeoLocation)
	 */
	private double getSolarElevationAzimuth(Instant instant, GeoLocation geoLocation, boolean isAzimuth) {
		double lat = geoLocation.getLatitude();
		double lon = geoLocation.getLongitude();
		ZonedDateTime utc = instant.atZone(ZoneOffset.UTC);
		double fractionalDay = (utc.getHour() + (utc.getMinute()
				+ (utc.getSecond() + utc.getNano() / 1_000_000_000.0) / 60.0) / 60.0) / 24.0;
		double jd = getJulianDay(utc.toLocalDate()) + fractionalDay;
		double jc = getJulianCenturiesFromJulianDay(jd);
		double decl = getSunDeclination(jc);
		double eot = getEquationOfTime(jc);
		double trueSolarTime = ((fractionalDay + eot / 1440.0 + lon / 360.0) + 2) % 1;
		double hourAngle = trueSolarTime * 2 * Math.PI - Math.PI;
		double cosZenith = sinDegrees(lat) * sinDegrees(decl) + cosDegrees(lat) * cosDegrees(decl) * Math.cos(hourAngle);
		double zenithDeg = acosDegrees(Math.max(-1, Math.min(1, cosZenith)));
		double elevation = (90.0 - zenithDeg) + adjustElevationForRefraction(90.0 - zenithDeg);
		double azimuth;
		double azDenom = cosDegrees(lat) * sinDegrees(zenithDeg);

		if (Math.abs(azDenom) > 0.001) {
			double az = (sinDegrees(lat) * cosDegrees(zenithDeg) - sinDegrees(decl)) / azDenom;
			azimuth = 180 - acosDegrees(Math.max(-1, Math.min(1, az))) * (hourAngle > 0 ? -1 : 1);
		} else {
			azimuth = lat > 0 ? 180 : 0;
		}
		return isAzimuth ? (azimuth + 360) % 360 : elevation;
	}

	
	/**
	 * Apply refraction adjustment to solar elevation. 
	 * @param elevation the elevation to adjust.
	 * @return the adjusted elevation.
	 */
	private double adjustElevationForRefraction(double elevation) {
		if (elevation > 85.0) {
			return 0.0;
		}
		
		double te = tanDegrees(elevation);
		double correction;
		
		if (elevation > 5.0) {
			correction = 58.1 / te - 0.07 / Math.pow(te, 3) + 0.000086 / Math.pow(te, 5);
		} else if (elevation > -0.575) {
			correction = 1735.0 + elevation * (-518.2 + elevation * (103.4 + elevation * (-12.79 + 0.711 * elevation)));
		} else {
			correction = -20.774 / te;
		}
		return correction / 3600.0;
	}
	
	/**
	 * {@inheritDoc}
	 * @see #getSolarNoonMidnightUTC(double, double, SolarEvent)
	 */
	@Override
	public double getUTCNoon(LocalDate localDate, GeoLocation geoLocation) {
		double noon = getSolarNoonMidnightUTC(getJulianDay(localDate), -geoLocation.getLongitude(), SolarEvent.NOON);
		noon = noon / 60;
		return (noon % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}
	
	/**
	 * {@inheritDoc}
	 * @see #getSolarNoonMidnightUTC(double, double, SolarEvent)
	 */
	@Override
	public double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation) {
		double midnight = getSolarNoonMidnightUTC(getJulianDay(localDate), -geoLocation.getLongitude(), SolarEvent.MIDNIGHT);
		midnight = midnight / 60;
		return (midnight % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of the current day <a href="http://en.wikipedia.org/wiki/Noon#Solar_noon">solar noon</a> or the the upcoming
	 * midnight (about 12 hours after solar noon) of the given day at the given location on earth.
	 * 
	 * @param julianDay The Julian day since <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @param longitude The longitude of observer in degrees
	 * @param solarEvent If the calculation is for {@link SolarEvent#NOON NOON} or {@link SolarEvent#MIDNIGHT MIDNIGHT}
	 * @return The UTC time of sunset in 24-hour format. 12:45:00 AM will return 0.75 while 1:45:00 PM will return 13.75. If an error
	 *         was encountered in the calculation (expected behavior for some locations such as near the poles, {@link Double#NaN}
	 *         will be returned.
	 * @see #getUTCNoon(LocalDate, GeoLocation)
	 * @see #getUTCMidnight(LocalDate, GeoLocation)
	 */
	private static double getSolarNoonMidnightUTC(double julianDay, double longitude, SolarEvent solarEvent) {
		// no day-half shift: the loop epoch julianDay + solNoonUTC/1440 already lands on the event
		// First pass for approximate solar noon to calculate equation of time
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + longitude / 360.0);
		double equationOfTime = getEquationOfTime(tnoon);
		double solNoonUTC = (longitude * 4) - equationOfTime; // minutes

		// Refine the equation of time at the calculated transit time.
		double newt;
		for (int i = 0; i < 2; i++) {
			newt = getJulianCenturiesFromJulianDay(julianDay + solNoonUTC / 1440.0);
			equationOfTime = getEquationOfTime(newt);
			solNoonUTC = (solarEvent == SolarEvent.NOON ? 720 : 1440) + (longitude * 4) - equationOfTime;
		}
		return solNoonUTC;
	}
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of sunrise or sunset in minutes for the given day at the given location on earth.
	 * @todo Possibly increase the number of passes for improved accuracy, especially in the Arctic areas.
	 * 
	 * @param localDate The {@code LocalDate}.
	 * @param latitude The latitude of observer in degrees
	 * @param longitude Longitude of observer in degrees
	 * @param zenith Zenith
	 * @param solarEvent If the calculation is for {@link SolarEvent#SUNRISE SUNRISE} or {@link SolarEvent#SUNSET SUNSET}
	 * @return The UTC time of sunrise or sunset in 24-hour format. 5:45:00 AM will return 5.75, while 5:45 PM will return 17.75. If
	 *         an error was encountered in the calculation (expected behavior for some locations such as near the poles,
	 *         {@link Double#NaN} will be returned.
	 */
	private static double getSunRiseSetUTC(LocalDate localDate, double latitude, double longitude, double zenith,
			SolarEvent solarEvent) {
		double julianDay = getJulianDay(localDate);

		// Find the time of solar noon at the location, and use that declination.
		// This is better than start of the Julian day
		// TODO really not needed since the Julian day starts from local fixed noon. Changing this would be more
		// efficient but would likely cause a very minor discrepancy in the calculated times (likely not reducing
		// accuracy, just slightly different, thus potentially breaking test cases). Regardless, it would be within
		// milliseconds.
		double noonmin = getSolarNoonMidnightUTC(julianDay, longitude, SolarEvent.NOON);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);
		// First calculates sunrise and approximate length of day
		double equationOfTime = getEquationOfTime(tnoon);
		double solarDeclination = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - equationOfTime;
		// Second pass includes fractional Julian Day in gamma calc
		double newt = getJulianCenturiesFromJulianDay(julianDay + timeUTC / 1440.0);
		equationOfTime = getEquationOfTime(newt);
		solarDeclination = getSunDeclination(newt);
		hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - equationOfTime;
		return timeUTC;
	}
	
	/**
	 * {@inheritDoc}
	 * The current implementation in this class only supports azimuth values of 90° (directly east) or 270° (directly west) that are
	 * directly needed in this library for the
	 * {@link com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getPolarSunsetBenIshChai()} and
	 * {@link com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getPolarSunriseBenIshChai()}.
	 * @throws IllegalArgumentException if the azimuth is not 90° or 270°.
	 * @todo complete the implementation for other azimuths. While not needed by this library, they may be of value to some projects.
	 *         There will be edge cases where the azimuth will occur more than once a day when based on the equation of time, the day
	 *         is shorter than 24 hours. In that case, the time for the first one will be returned.
	 */
	public double getTimeAtAzimuth(LocalDate localDate, GeoLocation geoLocation, double targetAzimuth) {
		if (targetAzimuth != 90.0 && targetAzimuth != 270.0) {
			throw new IllegalArgumentException(	"The targetAzimuth must be 90 or 270. Other azimuth values are not supported");
		}

		double julianDay = getJulianDay(localDate); 
		double solarNoonBase = 0.5 - (geoLocation.getLongitude() / 360.0);
		double dateTime = solarNoonBase + ((targetAzimuth == 90.0) ? 0.25 : 0.75);
		
		for (int i = 0; i < 3; i++) {
			double julianCenturies = getJulianCenturiesFromJulianDay(julianDay + dateTime);
			double ratio = tanDegrees(getSunDeclination(julianCenturies)) / tanDegrees(geoLocation.getLatitude());

			if (Double.isNaN(ratio) || ratio > 1.0 || ratio < -1.0) { // Handle Tropics, the Poles, and Equator line divisions
				return Double.NaN;
			}

			double offset = ((targetAzimuth == 90.0) ? -1.0 : 1.0) * (acosDegrees(ratio) / 360.0);
			dateTime = solarNoonBase + offset - (getEquationOfTime(julianCenturies) / 1440.0);
		}
		
		return (dateTime * 24 % 24 + 24) % 24;
	}
}
