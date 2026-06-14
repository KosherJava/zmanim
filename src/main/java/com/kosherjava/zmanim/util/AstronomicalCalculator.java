/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
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
	 * The commonly used average solar refraction in degrees that defaults to 34′, or 0.5666°.
	 * <a href="https://www.cs.tau.ac.il//~nachum/calendar-book/index.shtml">Calendrical Calculations</a> lists a more accurate
	 * global average of 34.478885263888294′ or 0.574648087731472°,
	 * 
	 * @see #getRefraction()
	 */
	private double refraction = 34 / 60d;

	/**
	 * The commonly used average solar radius that is about 16′ or 0.2666°.
	 * 
	 * @see #getSolarRadius()
	 * @see #getApparentSolarRadius(LocalDate)
	 */
	private double solarRadius = 16 / 60d;
	
	/**
	 * Whether sunrise and sunset should use the date-based apparent solar radius ({@link #getApparentSolarRadius(LocalDate)}),
	 * which varies slightly through the year as the Earth-Sun distance changes, rather than the fixed {@link #getSolarRadius()
	 * solar radius}. Defaults to {@code true}. When {@code false}, the fixed {@link #getSolarRadius()} value (default 16&prime;)
	 * is used instead, which is appropriate for matching other implementations that assume a constant solar radius. Note that
	 * calling {@link #setSolarRadius(double)} automatically sets this to {@code false}.
	 *
	 * @see #isUseApparentSolarRadius()
	 * @see #setUseApparentSolarRadius(boolean)
	 * @see #getApparentSolarRadius(LocalDate)
	 * @see #getSolarRadius()
	 */
	private boolean useApparentSolarRadius = true;

	/**
	 * Returns if useApparentSolarRadius is true (the default) or false.
	 * @return if useApparentSolarRadius is true or false.
	 * @see #getApparentSolarRadius(LocalDate)
	 * @see #getSolarRadius()
	 */
	public boolean isUseApparentSolarRadius() {
		return useApparentSolarRadius;
	}

	/**
	 * Sets if useApparentSolarRadius should be true (the default) or false.
	 * @param useApparentSolarRadius should apparent solar radius be used (default is true).
	 * @see #getApparentSolarRadius(LocalDate)
	 * @see #getSolarRadius()
	 */
	public void setUseApparentSolarRadius(boolean useApparentSolarRadius) {
		this.useApparentSolarRadius = useApparentSolarRadius;
	}

	/**
	 * The commonly used average earth radius of 6371.0088 KM (the IUGG mean radius R1 = (2a + b) / 3 of the WGS-84
	 * ellipsoid). At this time, this only affects the elevation adjustment, not the underlying solar position
	 * calculations.
	 *
	 * @see #getEarthRadius()
	 * @see #setEarthRadius(double)
	 */
	private double earthRadius = 6371.0088; // in KM (IUGG mean radius R1 = (2a + b) / 3, WGS-84)
	
	/**
	 * Default constructor using the default {@link #refraction refraction}, {@link #solarRadius solar radius} and
	 * {@link #earthRadius earth radius}.
	 */
	public AstronomicalCalculator() {
		// keep the defaults for now. 
	}

	/**
	 * A method that returns the earth radius in KM. The value currently defaults to the commonly used average earth
	 * radius of 6371.0088 KM (the IUGG mean radius R1 = (2a + b) / 3 of the WGS-84 ellipsoid) if not set. At this time,
	 * this only affects the elevation adjustment, not the underlying solar position calculations.
	 *
	 * @return the earth radius in KM.
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
	 * @param localDate The {@code LocalDate} representing the date to calculate sunrise for.
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
	 * @param localDate The {@code LocalDate}> representing the date to calculate sunset for.
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
	 * @param localDate The {@code LocalDate} representing the date to calculate noon for.
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
	 * @param localDate The {@code LocalDate} representing the date to calculate midnight for. The calculation will be for
	 *         midnight at the end of the day passed in.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * 
	 * @return The UTC time of solar midnight in a 24-hour {@code double} format. 1:45:00 AM will return 1.75. If an error
	 *         was encountered in the calculation (expected behavior for the {@link SunTimesCalculator} at times of the year in
	 *         Polar regions), {@link Double#NaN} will be returned.
	 * @see #getUTCNoon(LocalDate, GeoLocation)
	 */
	public abstract double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation);
	
	/**
	 * Returns the time that the azimuth will occur for the date and location passed to this method. As an example, to know when the
	 * sun will be directly west, pass in an azimuth of 270°, and for directly east, pass in an azimuth of 90°.
	 * 
	 * @param localDate The {@code LocalDate} representing the date to calculate the time of the azimuth for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param azimuth the azimuth to run the calculation for
	 * 
	 * @return The UTC time that the azimuth will be reached in a 24-hour {@code double} format. 5:45:00 PM will return 17.75.
	 *         If an error was encountered in the calculation (expected behavior for some dates at latitudes below 23.44°, a
	 *         {@link Double#NaN} will be returned.
	 */
	public abstract double getTimeAtAzimuth(LocalDate localDate, GeoLocation geoLocation, double azimuth);
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Elevation</a> for the
	 * horizontal coordinate system at the given location at the given time. Can be negative if the sun is below the
	 * horizon. Not corrected for altitude.
	 * 
	 * @param instant The {@code instant} to calculate the elevation for.
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
	 * @param instant The {@code instant} to calculate the azimuth for.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @return the solar azimuth in degrees. Astronomical midday would be 180° in the northern hemisphere and 0° in the southern
	 *         hemisphere. Depending on the location and time of year, sunrise will have an azimuth of about 90° and sunset about 270°.
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
	 * @param localDate the date to use for the solar radius. See {@link #getApparentSolarRadius(LocalDate)}.
	 * @return The zenith adjusted to include the {@link #getSolarRadius sun's radius}, {@link #getRefraction
	 *         refraction} and {@link #getElevationAdjustment elevation} adjustment. This will only be adjusted for
	 *         sunrise and sunset (if the zenith == 90°)
	 * @see #getElevationAdjustment(double)
	 */
	double adjustZenith(double zenith, double elevation, LocalDate localDate) {
		double adjustedZenith = zenith;
		if (zenith == GEOMETRIC_ZENITH) { // only adjust if it is exactly sunrise or sunset
			if(isUseApparentSolarRadius() && localDate != null) {
				adjustedZenith = zenith + (getApparentSolarRadius(localDate) + getRefraction() + getElevationAdjustment(elevation));
			} else {
				adjustedZenith = zenith + (getSolarRadius() + getRefraction() + getElevationAdjustment(elevation));
			}
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
	* Method to get the fixed sun's radius. The default value is 16 arcminutes. The sun's radius as it appears from earth is almost
	* universally given as 16 arcminutes but in fact it differs by the time of the year. At the <a href=
	* "https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">perihelion</a> it has an apparent radius of 16.293′ (0.2710°),
	* while at the <a href="https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">aphelion</a> it has an apparent radius of
	* 15.755′ (0.2622°). There is little effect for most locations, but at high and low latitudes the difference becomes more
	* apparent. Calculations for the difference at the location of the <a href="https://www.rmg.co.uk/royal-observatory">Royal
	* Observatory, Greenwich</a> show only a 4.494-second difference between the perihelion and aphelion radii, but moving into the
	* arctic circle the difference becomes more noticeable. Tests for Tromso, Norway (latitude 69.67°, longitude 19.05°) show that on
	* May 17, the rise of the midnight sun, a 2 minute and 23 second difference is observed between the perihelion and aphelion radii
	* using the USNO algorithm, but only 1 minute and 6 seconds difference using the NOAA algorithm. Areas farther north show an even
	* greater difference. Note that these are not real-world tests. It simply compared the min and max solar radius at different
	* locations. Real world examples of comparing the actual apparent solar radius would yield less significant differences.
	* Regardless, this is exactly the error that the date-based apparent solar radius eliminates: by default ({@link
	* #isUseApparentSolarRadius()} is {@code true}) sunrise and sunset use {@link #getApparentSolarRadius(LocalDate)}
	* instead, and the fixed value returned here only applies when that setting is {@code false} (typically to match other
	* implementations that assume a constant 16′ radius; see {@link #setSolarRadius(double)}, which switches that setting off
	* automatically).
	*
	* @return The fixed sun's radius in degrees, used only when {@link #isUseApparentSolarRadius()} is {@code false}.
	* @see #getApparentSolarRadius(LocalDate)
	* @see #isUseApparentSolarRadius()
	*/
	public double getSolarRadius() {
		return this.solarRadius;
	}

	/**
	 * Sets the Sun's radius as a fixed, date-independent value in degrees, for example {@code 16.0 / 60.0} for the
	 * conventional 16′. This is typically used to match another implementation or reference that assumes a constant
	 * solar radius.
	 * <p>
	 * A fixed radius has no effect while {@link #isUseApparentSolarRadius()} is {@code true} (the default), since
	 * in that mode the date-based {@link #getApparentSolarRadius(LocalDate) apparent solar radius} is used instead. So that
	 * a value set here actually takes effect, this method also disables that mode (equivalent to calling
	 * {@link #setUseApparentSolarRadius(boolean)}). To return to the date-based radius afterward, pass {@code true} to
	 * {@link #setUseApparentSolarRadius(boolean)}.
	 *
	 * @param solarRadius the Sun's radius in degrees. Must be a non-negative number.
	 * @throws IllegalArgumentException if {@code solarRadius} is &lt; 0 or is {@link Double#NaN}.
	 * @see #getSolarRadius()
	 * @see #isUseApparentSolarRadius()
	 * @see #getApparentSolarRadius(LocalDate)
	 */
	public void setSolarRadius(double solarRadius) {
		if (solarRadius < 0 || Double.isNaN(solarRadius)) {
			throw new IllegalArgumentException("Solar radius must be a non-negative number");
		}
		this.solarRadius = solarRadius;
		this.useApparentSolarRadius = false;
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
				&& Double.compare(this.getSolarRadius(), calculator.getSolarRadius()) == 0
				&& this.isUseApparentSolarRadius()  == calculator.isUseApparentSolarRadius();
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
		return Objects.hash(getEarthRadius(), getRefraction(), getSolarRadius(), useApparentSolarRadius);
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
	
	/**
	 * The Sun's apparent angular semi-diameter (the "solar radius") in degrees for each day of the year, precomputed from
	 * the <a href="https://en.wikipedia.org/wiki/VSOP_(planets)">VSOP87</a> Earth-Sun distance for reference year 2050.
	 * The Sun's apparent size varies through the year as the Earth-Sun distance changes between the perihelion (~Jan 3,
	 * about 16.27′ or 0.2710°) and the aphelion (~Jul 5, about 15.73′ or 0.2622°), with a mean of the conventional 16′.
	 * The table has 365 entries indexed by day-of-year (1-365); 2050 is a common year, so February 29 reuses the
	 * February 28 value (see {@link #getApparentSolarRadius(LocalDate)}).
	 * <p>
	 * <b>Valid epoch.</b> Reference year 2050 was chosen as the midpoint of the years 2000-2100 so that the small drift in
	 * the value for a fixed calendar date stays symmetric and minimal across that span. The date of the perihelion advances
	 * through the calendar by roughly one day every 57 years, so the apparent radius for a given calendar date is not
	 * perfectly constant from year to year. Across 2000-2100 this table is within about 0.4″ of the exact value (at most
	 * roughly 50 ms of sunrise / sunset time at latitude 60°, and less elsewhere) - negligible for any practical purpose.
	 * Outside 2000-2100 the table is still returned but its accuracy degrades gradually as the perihelion drifts further;
	 * for use centuries away from 2050 the table should be regenerated against a closer reference year.
	 * <p>
	 * A fixed table is used in preference to evaluating the VSOP87 series at run time because, given the negligible drift
	 * above, the lookup is both simpler and faster while remaining far more accurate than any sunrise / sunset calculation
	 * requires.
	 *
	 * @see #getApparentSolarRadius(LocalDate)
	 */
	private static final double[] SOLAR_RADIUS_BY_DAY_OF_YEAR = {
		0.27108024, 0.27108486, 0.27108790, 0.27108930, 0.27108899, 0.27108695, 0.27108316, 0.27107762, 0.27107033,
		0.27106133, 0.27105062, 0.27103826, 0.27102427, 0.27100873, 0.27099168, 0.27097320, 0.27095337, 0.27093228, 
		0.27091002, 0.27088667, 0.27086231, 0.27083701, 0.27081079, 0.27078369, 0.27075569, 0.27072676, 0.27069684,
		0.27066588, 0.27063378, 0.27060048, 0.27056589, 0.27052995, 0.27049261, 0.27045383, 0.27041359, 0.27037186, 
		0.27032864, 0.27028396, 0.27023782, 0.27019025, 0.27014129, 0.27009098, 0.27003938, 0.26998658, 0.26993264,
		0.26987767, 0.26982177, 0.26976506, 0.26970763, 0.26964958, 0.26959099, 0.26953191, 0.26947239, 0.26941242, 
		0.26935200, 0.26929108, 0.26922962, 0.26916755, 0.26910482, 0.26904136, 0.26897712, 0.26891206, 0.26884614,
		0.26877935, 0.26871165, 0.26864306, 0.26857358, 0.26850321, 0.26843197, 0.26835989, 0.26828703, 0.26821343, 
		0.26813918, 0.26806437, 0.26798910, 0.26791348, 0.26783763, 0.26776167, 0.26768569, 0.26760979, 0.26753404,
		0.26745846, 0.26738308, 0.26730790, 0.26723289, 0.26715800, 0.26708320, 0.26700842, 0.26693363, 0.26685877, 
		0.26678380, 0.26670870, 0.26663342, 0.26655796, 0.26648229, 0.26640640, 0.26633030, 0.26625399, 0.26617748,
		0.26610082, 0.26602406, 0.26594728, 0.26587055, 0.26579398, 0.26571769, 0.26564180, 0.26556641, 0.26549164, 
		0.26541756, 0.26534425, 0.26527174, 0.26520006, 0.26512920, 0.26505915, 0.26498987, 0.26492132, 0.26485345,
		0.26478622, 0.26471959, 0.26465351, 0.26458794, 0.26452285, 0.26445820, 0.26439396, 0.26433010, 0.26426661, 
		0.26420348, 0.26414070, 0.26407832, 0.26401636, 0.26395489, 0.26389400, 0.26383378, 0.26377435, 0.26371580,
		0.26365825, 0.26360179, 0.26354651, 0.26349247, 0.26343971, 0.26338825, 0.26333807, 0.26328918, 0.26324153, 
		0.26319510, 0.26314983, 0.26310568, 0.26306261, 0.26302057, 0.26297951, 0.26293938, 0.26290014, 0.26286173,
		0.26282411, 0.26278725, 0.26275111, 0.26271570, 0.26268102, 0.26264710, 0.26261399, 0.26258177, 0.26255053, 
		0.26252037, 0.26249137, 0.26246366, 0.26243731, 0.26241239, 0.26238897, 0.26236707, 0.26234671, 0.26232790,
		0.26231061, 0.26229481, 0.26228048, 0.26226756, 0.26225602, 0.26224581, 0.26223687, 0.26222914, 0.26222257, 
		0.26221708, 0.26221262, 0.26220912, 0.26220653, 0.26220480, 0.26220392, 0.26220388, 0.26220470, 0.26220642,
		0.26220910, 0.26221282, 0.26221768, 0.26222375, 0.26223114, 0.26223991, 0.26225014, 0.26226187, 0.26227512, 
		0.26228992, 0.26230626, 0.26232413, 0.26234349, 0.26236433, 0.26238659, 0.26241024, 0.26243521, 0.26246145,
		0.26248890, 0.26251746, 0.26254708, 0.26257766, 0.26260913, 0.26264141, 0.26267446, 0.26270823, 0.26274270, 
		0.26277789, 0.26281382, 0.26285054, 0.26288813, 0.26292666, 0.26296622, 0.26300686, 0.26304868, 0.26309171,
		0.26313601, 0.26318159, 0.26322848, 0.26327666, 0.26332612, 0.26337685, 0.26342881, 0.26348197, 0.26353627, 
		0.26359167, 0.26364809, 0.26370545, 0.26376368, 0.26382267, 0.26388233, 0.26394256, 0.26400328, 0.26406442,
		0.26412593, 0.26418777, 0.26424995, 0.26431249, 0.26437542, 0.26443880, 0.26450270, 0.26456718, 0.26463231, 
		0.26469815, 0.26476474, 0.26483213, 0.26490034, 0.26496937, 0.26503922, 0.26510990, 0.26518138, 0.26525364,
		0.26532664, 0.26540034, 0.26547467, 0.26554957, 0.26562495, 0.26570072, 0.26577676, 0.26585296, 0.26592922, 
		0.26600544, 0.26608154, 0.26615745, 0.26623314, 0.26630859, 0.26638382, 0.26645884, 0.26653372, 0.26660850,
		0.26668323, 0.26675798, 0.26683280, 0.26690773, 0.26698280, 0.26705803, 0.26713345, 0.26720905, 0.26728485, 
		0.26736083, 0.26743698, 0.26751326, 0.26758964, 0.26766606, 0.26774245, 0.26781872, 0.26789476, 0.26797045,
		0.26804569, 0.26812035, 0.26819433, 0.26826755, 0.26833992, 0.26841141, 0.26848200, 0.26855170, 0.26862051,
		0.26868849, 0.26875567, 0.26882211, 0.26888786, 0.26895296, 0.26901746, 0.26908139, 0.26914478, 0.26920767,
		0.26927006, 0.26933199, 0.26939345, 0.26945445, 0.26951496, 0.26957497, 0.26963442, 0.26969325, 0.26975136,
		0.26980865, 0.26986502, 0.26992034, 0.26997450, 0.27002740, 0.27007895, 0.27012907, 0.27017773, 0.27022491,
		0.27027060, 0.27031483, 0.27035764, 0.27039906, 0.27043914, 0.27047794, 0.27051549, 0.27055186, 0.27058709,
		0.27062122, 0.27065430, 0.27068636, 0.27071746, 0.27074761, 0.27077684, 0.27080516, 0.27083256, 0.27085899,
		0.27088442, 0.27090875, 0.27093191, 0.27095379, 0.27097427, 0.27099326, 0.27101067, 0.27102640, 0.27104041, 
		0.27105266, 0.27106312, 0.27107182, 0.27107876, 0.27108399};

	/**
	 * Returns the Sun's apparent angular semi-diameter (the "solar radius") for the given date, in degrees. The Sun's
	 * apparent size changes over the year as the Earth-Sun distance varies between the <a href=
	 * "https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">perihelion</a> (~Jan 3, when the Sun is largest)
	 * and the <a href="https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">aphelion</a> (~Jul 5, when it is
	 * smallest). The value is read from a precomputed table ({@link #SOLAR_RADIUS_BY_DAY_OF_YEAR}) keyed by calendar
	 * day; the date's month and day are mapped onto the (common-year) reference year 2050 via {@code withYear(2050)},
	 * so a February 29 input is automatically resolved to February 28 and assigned that day's value.
	 * <p>
	 * This is the value the calculator applies <b>automatically</b> for sunrise and sunset while
	 * {@link #isUseApparentSolarRadius()} is {@code true} (the default). It is recomputed from the date on
	 * every call and is never stored, so it is always correct for the date being calculated. The method is exposed only
	 * for inspection and comparison.
	 * <p>
	 * <b>Do not pass the result to {@link #setSolarRadius(double)}.</b> {@link #setSolarRadius(double)} is meant for a
	 * fixed, date-independent radius (for example to match another implementation that uses a constant 16′). Storing
	 * a single day's apparent radius there would freeze that one day's value onto this instance and apply it incorrectly
	 * to every other date subsequently calculated (and would also switch off {@link #isUseApparentSolarRadius()
	 * apparent-radius mode}). To use the date-based radius, simply leave {@link #isUseApparentSolarRadius()}
	 * enabled and let the calculator call this method itself.
	 *
	 * @param localDate the date for which to return the Sun's apparent semi-diameter. If a {@code null} is passed, the
	 *         default solar radius of 16/60 will be returned.
	 * 
	 * @return the Sun's apparent semi-diameter in degrees: about 0.2711° near the <a href=
	 *         "https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">perihelion</a>, about 0.2622° near the
	 *         <a href="https://en.wikipedia.org/wiki/Apsis#Perihelion_and_aphelion">aphelion</a>, and about 0.2666°
	 *         - the conventional 16′ - near the <a href="https://en.wikipedia.org/wiki/Equinox">equinoxes</a>.
	 * @see #isUseApparentSolarRadius()
	 * @see #getSolarRadius()
	 */
	public double getApparentSolarRadius(LocalDate localDate) {
		return localDate == null ? 16 / 60d : SOLAR_RADIUS_BY_DAY_OF_YEAR[localDate.withYear(2050).getDayOfYear() - 1];
	}

}
