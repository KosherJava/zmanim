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

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * An abstract class that all sun time calculating classes extend. This allows the algorithm used to be changed at
 * runtime, easily allowing comparison the results of using different algorithms.
 * @todo Consider methods that would allow atmospheric modeling. This can currently be adjusted by {@link
 * #setRefraction(double) setting the refraction}.
 * 
 * @author © Eliyahu Hershfeld 2004 - 2026
 */
public abstract class AstronomicalCalculator implements Cloneable {
	/**
	 * The commonly used average solar refraction. <a href="https://www.cs.tau.ac.il//~nachum/calendar-book/index.shtml"
	 * >Calendrical Calculations</a> lists a more accurate global average of 34.478885263888294
	 * 
	 * @see #getRefraction()
	 */
	private double refraction = 34 / 60d;

	/**
	 * The commonly used average solar radius in minutes of a degree.
	 * 
	 * @see #getSolarRadius()
	 */
	private double solarRadius = 16 / 60d;

	/**
	 * The commonly used average earth radius in KM. At this time, this only affects elevation adjustment and not the
	 * sunrise and sunset calculations. The value currently defaults to 6356.9 KM.
	 * 
	 * @see #getEarthRadius()
	 * @see #setEarthRadius(double)
	 */
	private double earthRadius = 6356.9; // in KM
	
	/**
	 * Default constructor using the default {@link #refraction refraction}, {@link #solarRadius solar radius} and
	 * {@link #earthRadius earth radius}.
	 */
	public AstronomicalCalculator() {
		// keep the defaults for now. 
	}

	/**
	 * A method that returns the earth radius in KM. The value currently defaults to 6356.9 KM if not set.
	 * 
	 * @return the earthRadius the earth radius in KM.
	 */
	public double getEarthRadius() {
		return earthRadius;
	}

	/**
	 * A method that allows setting the earth's radius.
	 * 
	 * @param earthRadius the earthRadius to set in KM
	 */
	public void setEarthRadius(double earthRadius) {
		this.earthRadius = earthRadius;
	}

	/**
	 * The zenith of astronomical sunrise and sunset. The sun is 90° from the vertical 0°
	 */
	private static final double GEOMETRIC_ZENITH = 90;

	/**
	 * Returns the default class for calculating sunrise and sunset. This is currently the more accurate
	 * {@link NOAACalculator}, but this may change in the future.
	 * 
	 * @return AstronomicalCalculator the default class for calculating sunrise and sunset. In the current
	 *         implementation the default calculator returned is the more accurate {@link NOAACalculator}.
	 */
	public static AstronomicalCalculator getDefault() {
		return new NOAACalculator();
	}

	/**
	 * Returns the name of the algorithm.
	 * 
	 * @return the descriptive name of the algorithm.
	 */
	public abstract String getCalculatorName();

	/**
	 * A method that calculates UTC sunrise as well as any time based on an angle above or below sunrise. This abstract
	 * method is implemented by the classes that extend this class.
	 * 
	 * @param localDate The <code>LocalDate</code> representing the date to calculate sunrise for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param zenith the azimuth below the vertical zenith of 90°. For sunrise typically the {@link #adjustZenith zenith} used for the
	 *         calculation uses geometric zenith of 90° and {@link #adjustZenith adjusts} this slightly to account for solar refraction
	 *         and the sun's radius. Another example would be
	 *         {@link com.kosherjava.zmanim.AstronomicalCalendar#getBeginNauticalTwilight()} that passes
	 *         {@link com.kosherjava.zmanim.AstronomicalCalendar#NAUTICAL_ZENITH} to this method.
	 * @param adjustForElevation Should the time be adjusted for elevation
	 * @return The UTC time of sunrise in 24-hour format. 5:45:00 AM will return 5.75. If an error was encountered in the
	 *         calculation (expected behavior for some locations such as near the poles, {@link Double#NaN} will be returned.
	 * @see #getElevationAdjustment(double)
	 */
	public abstract double getUTCSunrise(LocalDate localDate, GeoLocation geoLocation, double zenith,
			boolean adjustForElevation);

	/**
	 * A method that calculates UTC sunset as well as any time based on an angle above or below sunset. This abstract
	 * method is implemented by the classes that extend this class.
	 * 
	 * @param localDate The <code>LocalDate</code> representing the date to calculate sunset for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param zenith the azimuth below the vertical zenith of 90°. For sunset typically the zenith used for the calculation uses
	 *         geometric zenith of 90° and {@link #adjustZenith adjusts} this slightly to account for solar refraction and the sun's
	 *         radius. Another example would be {@link com.kosherjava.zmanim.AstronomicalCalendar#getEndNauticalTwilight()} that passes
	 *         {@link com.kosherjava.zmanim.AstronomicalCalendar#NAUTICAL_ZENITH} to this method.
	 * @param adjustForElevation Should the time be adjusted for elevation
	 * @return The UTC time of sunset in 24-hour format. 5:45:00 AM will return 5.75. If an error was encountered in the
	 *         calculation (expected behavior for some locations such as near the poles, {@link Double#NaN} will be returned.
	 * @see #getElevationAdjustment(double)
	 */
	public abstract double getUTCSunset(LocalDate localDate, GeoLocation geoLocation, double zenith,
			boolean adjustForElevation);
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC) of
	 * <a href="https://en.wikipedia.org/wiki/Noon#Solar_noon">solar noon</a> (UTC) for the given day at the given location. The
	 * {@link NOAACalculator}, the default calculator implementation calculates true solar noon, something that can be calculated
	 * even in the Arctic / Antarctic where there may be no sunrise or sunset, while the {@link SunTimesCalculator} approximates it,
	 * calculating the time as halfway between sunrise and sunset, something that can't be calculated in Polar regions where there is
	 * no sunrise or sunset for part of the year. See <a href="https://kosherjava.com/2020/07/02/definition-of-chatzos/">The
	 * Definition of <em>Chatzos</em></a> for details on solar noon /
	 * midnight calculations.
	 * 
	 * @param localDate The <code>LocalDate</code> representing the date to calculate noon for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * 
	 * @return The UTC time of solar noon in 24-hour format. 1:45:00 PM will return 13.75. If an error was encountered in the
	 *         the calculation (expected behavior for some locations such as near the poles, {@link Double#NaN} will be returned.
	 * @see #getUTCMidnight(LocalDate, GeoLocation)
	 */
	public abstract double getUTCNoon(LocalDate localDate, GeoLocation geoLocation);
	
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC) of
	 * <a href="https://en.wikipedia.org/wiki/Midnight">solar midnight</a> (UTC) for the given day at the given location. The
	 * {@link NOAACalculator}, the default calculator implementation calculates true solar midnight, something that can be calculated
	 * even in the Arctic / Antarctic where there may be no sunrise or sunset, while the {@link SunTimesCalculator} approximates it,
	 * calculating the time as 12 hours after halfway between sunrise and sunset, something that can't be calculated in Polar
	 * regions where there is no sunrise or sunset for part of the year. See <a href=
	 * "https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of <em>Chatzos</em></a> for details on solar noon /
	 * midnight calculations.
	 * 
	 * @param localDate The <code>LocalDate</code> representing the date to calculate midnight for. The calculation will be for
	 *         midnight at the end of the day passed in.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * 
	 * @return The UTC time of solar midnight in in a 24-hour <code>double</code> format. 1:45:00 AM will return 1.75. If an error
	 *         was encountered in the calculation (expected behavior for the {@link SunTimesCalculator} at times of the year in
	 *         Polar regions), {@link Double#NaN} will be returned.
	 * @see #getUTCNoon(LocalDate, GeoLocation)
	 */
	public abstract double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation);
	
	/**
	 * Returns the time that the azimuth will occur for the date and location passed to this method. As an example, to know when the
	 * sun will be directly west, pass in an azimuth of 270°, and for directly east, pass in an azimuth of 90°.
	 * 
	 * @param localDate The <code>LocalDate</code> representing the date to calculate the time of the azimuth for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param azimuth the azimuth to run the calculation for
	 * 
	 * @return The UTC time that the azimuth will be reached in a 24-hour <code>double</code> format. 5:45:00 PM will return 17.75.
	 *         If an error was encountered in the calculation (expected behavior for some dates at latitudes below 23.44°, a
	 *         {@link Double#NaN} will be returned.
	 */
	public abstract double getTimeAtAzimuth(LocalDate localDate, GeoLocation geoLocation, double azimuth);
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Elevation</a> for the
	 * horizontal coordinate system at the given location at the given time. Can be negative if the sun is below the
	 * horizon. Not corrected for altitude.
	 * 
	 * @param instant The <code>instant</code> to calculate the elevation for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @return solar elevation in degrees. The horizon (calculated in a vacuum using the solar radius as the point)is 90°, civil
	 *         twilight is 96° etc. This means that sunrise and sunset that do use refraction and are calculated from the upper
	 *         limb of the sun will return about 0.8333°.
	 */
	public abstract double getSolarElevation(Instant instant, GeoLocation geoLocation);
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Azimuth</a> for the horizontal coordinate
	 * system at the given location at the given time. Not corrected for altitude. True south is 180°.
	 * 
	 * @param instant The <code>instant</code> to calculate the azimuth for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @return the solar azimuth in degrees. Astronomical midday would be 180° in the norther hemosphere and 0° in the southern
	 *         hemosphere. Depending on the location and time of year, sunrise will have an azimuth of about 90° and sunset about 270°.
	 */
	public abstract double getSolarAzimuth(Instant instant, GeoLocation geoLocation);

	/**
	 * Method to return the adjustment to the zenith required to account for the elevation. Since a person at a higher elevation can
	 * see farther below the horizon, the calculation for sunrise / sunset is calculated below the horizon used at sea level. This is
	 * only used for sunrise and sunset and not times before or after it such as {@link
	 * com.kosherjava.zmanim.AstronomicalCalendar#getBeginNauticalTwilight() nautical twilight} since those calculations are based on
	 * the level of available light at the given dip below the horizon, something that is not affected by elevation, the adjustment
	 * should only be made if the zenith == 90° {@link #adjustZenith adjusted} for refraction and solar radius. The algorithm used is
	 *  
	 * <pre>
	 * elevationAdjustment = Math.toDegrees(Math.acos(earthRadiusInMeters / (earthRadiusInMeters + elevationMeters)));
	 * </pre>
	 * 
	 * The source of this algorithm is <a href="https://www.cs.tau.ac.il/~nachum/calendar-book/index.shtml">Calendrical
	 * Calculations</a> by Edward M. Reingold and Nachum Dershowitz. An alternate algorithm that produces similar (but not completely
	 * accurate) result found in Ma'aglay Tzedek by Moishe Kosower and other sources is:
	 * 
	 * <pre>
	 * elevationAdjustment = 0.0347 * Math.sqrt(elevationMeters);
	 * </pre>
	 * 
	 * @param elevation elevation in Meters.
	 * @return the adjusted zenith
	 */
	double getElevationAdjustment(double elevation) {
		return Math.toDegrees(Math.acos(earthRadius / (earthRadius + (elevation / 1000))));
	}

	/**
	 * Adjusts the zenith of astronomical sunrise and sunset to account for solar refraction, solar radius and elevation. The value
	 * for Sun's zenith and true rise/set Zenith (used in this class and subclasses) is the angle that the center of the Sun makes to
	 * a line perpendicular to the Earth's surface. If the Sun were a point and the Earth were without an atmosphere, true sunset and
	 * sunrise would correspond to a 90° zenith. Because the Sun is not a point, and because the atmosphere refracts light, this 90°
	 * zenith does not, in fact, correspond to true sunset or sunrise, instead the center of the Sun's disk must lie just below the
	 * horizon for the upper edge to be obscured. This means that a zenith of just above 90° must be used. The Sun subtends an angle
	 * of 16 minutes of arc (this can be changed via the {@link #setSolarRadius(double)} method , and atmospheric refraction accounts
	 * for 34 minutes or so (this can be changed via the {@link #setRefraction(double)} method), giving a total of 50 arcminutes. The
	 * total value for ZENITH is 90+(5/6) or 90.8333333° for true sunrise/sunset. Since a person at an elevation can see below the
	 * horizon of a person at sea level, this will also adjust the zenith to account for elevation if available. Note that this will
	 * only adjust the value if the zenith is exactly 90°. For values below and above this no correction is done. As an example,
	 * astronomical twilight is when the sun is 18° below the horizon or {@link
	 * com.kosherjava.zmanim.AstronomicalCalendar#ASTRONOMICAL_ZENITH 108° below the zenith}. This is traditionally calculated with
	 * none of the above mentioned adjustments. The same goes for various <em>tzais</em> and <em>alos</em> times such as the {@link
	 * com.kosherjava.zmanim.ZmanimCalendar#ZENITH_16_POINT_1 16.1°} dip used in {@link
	 * com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getAlos16Point1Degrees()}.
	 * 
	 * @param zenith the azimuth below the vertical zenith of 90°. For sunset typically the {@link #adjustZenith zenith} used for the
	 *         calculation uses geometric zenith of 90° and {@link #adjustZenith adjusts} this slightly to account for solar
	 *         refraction and the sun's radius. Another example would be {@link
	 *         com.kosherjava.zmanim.AstronomicalCalendar#getEndNauticalTwilight()} that passes {@link
	 *         com.kosherjava.zmanim.AstronomicalCalendar#NAUTICAL_ZENITH} to this method.
	 * @param elevation elevation in Meters.
	 * @return The zenith adjusted to include the {@link #getSolarRadius sun's radius}, {@link #getRefraction
	 *         refraction} and {@link #getElevationAdjustment elevation} adjustment. This will only be adjusted for
	 *         sunrise and sunset (if the zenith == 90°)
	 * @see #getElevationAdjustment(double)
	 */
	double adjustZenith(double zenith, double elevation) {
		double adjustedZenith = zenith;
		if (zenith == GEOMETRIC_ZENITH) { // only adjust if it is exactly sunrise or sunset
			adjustedZenith = zenith + (getSolarRadius() + getRefraction() + getElevationAdjustment(elevation));
		}
		return adjustedZenith;
	}

	/**
	 * Method to get the refraction value to be used when calculating sunrise and sunset. The default value is 34 arcminutes
	 * (returned in degrees). The <a href="https://www.cs.tau.ac.il/~nachum/calendar-book/second-edition/errata.pdf">Errata and
	 * Notes for Calendrical Calculations: The Millennium Edition</a> by Edward M. Reingold and Nachum Dershowitz lists the
	 * actual average refraction value as 34.478885263888294 or approximately 34' 29". The refraction value as well as the
	 * solar radius and elevation adjustment are added to the zenith used to calculate sunrise and sunset.
	 * 
	 * @return The refraction in degrees.
	 */
	public double getRefraction() {
		return this.refraction;
	}

	/**
	 * A method to allow overriding the default refraction of the calculator.
	 * @todo At some point in the future, an AtmosphericModel or Refraction object that models the atmosphere of different
	 * locations might be used for increased accuracy.
	 * 
	 * @param refraction The refraction in degrees.
	 * @see #getRefraction()
	 */
	public void setRefraction(double refraction) {
		this.refraction = refraction;
	}

	/**
	 * Method to get the sun's radius. The default value is 16 arcminutes. The sun's radius as it appears from earth is
	 * almost universally given as 16 arcminutes but in fact it differs by the time of the year. At the <a
	 * href="https://en.wikipedia.org/wiki/Perihelion">perihelion</a> it has an apparent radius of 16.293, while at the
	 * <a href="https://en.wikipedia.org/wiki/Aphelion">aphelion</a> it has an apparent radius of 15.755. There is little
	 * affect for most location, but at high and low latitudes the difference becomes more apparent. My Calculations for
	 * the difference at the location of the <a href="https://www.rmg.co.uk/royal-observatory">Royal Observatory, Greenwich</a>
	 * shows only a 4.494-second difference between the perihelion and aphelion radii, but moving into the arctic circle the
	 * difference becomes more noticeable. Tests for Tromso, Norway (latitude 69.672312, longitude 19.049787) show that
	 * on May 17, the rise of the midnight sun, a 2 minute and 23 second difference is observed between the perihelion and
	 * aphelion radii using the USNO algorithm, but only 1 minute and 6 seconds difference using the NOAA algorithm.
	 * Areas farther north show an even greater difference. Note that these test are not real valid test cases because
	 * they show the extreme difference on days that are not the perihelion or aphelion, but are shown for illustrative
	 * purposes only.
	 * 
	 * @return The sun's radius in degrees.
	 */
	public double getSolarRadius() {
		return this.solarRadius;
	}

	/**
	 * Method to set the sun's radius.
	 * 
	 * @param solarRadius The sun's radius in degrees.
	 * @see #getSolarRadius()
	 */
	public void setSolarRadius(double solarRadius) {
		this.solarRadius = solarRadius;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Two {@code AstronomicalCalculator} instances are considered equal if their
	 * Earth radius, refraction, and solar radius values are identical.
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
		AstronomicalCalculator calculator = (AstronomicalCalculator) object;

		return Double.compare(this.getEarthRadius(), calculator.getEarthRadius()) == 0
				&& Double.compare(this.getRefraction(), calculator.getRefraction()) == 0
				&& Double.compare(this.getSolarRadius(), calculator.getSolarRadius()) == 0;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation hashes the earth radius, refraction, and solar radius properties to maintain the contract with
	 * {@link #equals(Object)}.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getEarthRadius(), getRefraction(), getSolarRadius());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public AstronomicalCalculator clone() {
		try {
			return (AstronomicalCalculator) super.clone();
		} catch (CloneNotSupportedException cnse) {
			throw new AssertionError("Clone not supported on a Cloneable object", cnse);
		}
	}
	
	/**
	 * A helper method to retun the <a href="https://en.wikipedia.org/wiki/Law_of_tangents">tan / tangent</a> in degrees.
	 * @param angle the angle
	 * @return the tangent in degrees
	 */
	protected static double tanDegrees(double angle) {
	    return Math.tan(Math.toRadians(angle));
	}

	/**
	 * A helper method to retun the <a href="https://en.wikipedia.org/wiki/Sine_and_cosine">sine</a> in degrees.
	 * @param angle the angle
	 * @return the sine in degrees.
	 */
	protected static double sinDegrees(double angle) {
	    return Math.sin(Math.toRadians(angle));
	}

	/**
	 * A helper method to retun the <a href="https://en.wikipedia.org/wiki/Sine_and_cosine">cosine</a> in degrees.
	 * @param angle the angle
	 * @return the cosine in degrees.
	 */
	protected static double cosDegrees(double angle) {
	    return Math.cos(Math.toRadians(angle));
	}

	/**
	 * A helper method to retun the <a href="https://en.wikipedia.org/wiki/Inverse_trigonometric_functions">arc cosine</a> in degrees.
	 * @param angle the angle
	 * @return the arc cosine in degrees.
	 */
	protected static double acosDegrees(double angle) {
	    return Math.toDegrees(Math.acos(angle));
	}

	/**
	 * A helper method to retun the <a href="https://en.wikipedia.org/wiki/Inverse_trigonometric_functions">arc sine</a> in degrees.
	 * @param angle the angle
	 * @return the arc sine in degrees.
	 */
	protected static double asinDegrees(double angle) {
	    return Math.toDegrees(Math.asin(angle));
	}
}
