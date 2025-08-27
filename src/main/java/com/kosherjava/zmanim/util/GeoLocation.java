/*
 * Zmanim Java API
 * Copyright (C) 2004-2025 Eliyahu Hershfeld
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

import java.util.Objects;
import java.util.TimeZone;

/**
 * A class that contains location information such as latitude and longitude required for astronomical calculations. The
 * elevation field may not be used by some calculation engines and would be ignored if set. Check the documentation for
 * specific implementations of the {@link AstronomicalCalculator} to see if elevation is calculated as part of the
 * algorithm.
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2025
 */
public class GeoLocation implements Cloneable {
	/**
	 * The latitude, for example 40.096 for Lakewood, NJ.
	 * @see #getLatitude()
	 * @see #setLatitude(double)
	 * @see #setLatitude(int, int, double, String)
	 */
	private double latitude;
	
	/**
	 * The longitude, for example -74.222 for Lakewood, NJ.
	 * @see #getLongitude()
	 * @see #setLongitude(double)
	 * @see #setLongitude(int, int, double, String)
	 */
	private double longitude;
	
	/**
	 * The location name used for display, for example "Lakewood, NJ".
	 * @see #getLocationName()
	 * @see #setLocationName(String)
	 */
	private String locationName;
	
	/**
	 * The location's time zone.
	 * @see #getTimeZone()
	 * @see #setTimeZone(TimeZone)
	 */
	private TimeZone timeZone;
	
	/**
	 * The elevation in Meters <b>above</b> sea level.
	 * @see #getElevation()
	 * @see #setElevation(double)
	 */
	private double elevation;
	
	/**
	 * Constant for a distance type calculation.
	 * @see #getGeodesicDistance(GeoLocation)
	 */
	private static final int DISTANCE = 0;
	
	/**
	 * Constant for an initial bearing type calculation.
	 * @see #getGeodesicInitialBearing(GeoLocation)
	 */
	private static final int INITIAL_BEARING = 1;
	
	/**
	 * Constant for a final bearing type calculation.
	 * @see #getGeodesicFinalBearing(GeoLocation)
	 */
	private static final int FINAL_BEARING = 2;

	/** constant for milliseconds in a minute (60,000) */
	private static final long MINUTE_MILLIS = 60 * 1000;

	/** constant for milliseconds in an hour (3,600,000) */
	private static final long HOUR_MILLIS = MINUTE_MILLIS * 60;

	private static final double PI2 = Math.PI * 2;
	private static final double PI_4 = Math.PI / 4;

	/**
	 * Method to return the elevation in Meters <b>above</b> sea level.
	 * 
	 * @return Returns the elevation in Meters.
	 * @see #setElevation(double)
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * Method to set the elevation in Meters <b>above</b> sea level.
	 * 
	 * @param elevation
	 *            The elevation to set in Meters. An IllegalArgumentException will be thrown if the value is a negative,
	 *            {@link java.lang.Double#isNaN(double) NaN}  or {@link java.lang.Double#isInfinite(double) infinite}.
	 */
	public void setElevation(double elevation) {
		if (elevation < 0) {
			throw new IllegalArgumentException("Elevation cannot be negative");
		}
		if (Double.isNaN(elevation) || Double.isInfinite(elevation)) {
			throw new IllegalArgumentException("Elevation cannot be NaN or infinite");
		}
		this.elevation = elevation;
	}

	/**
	 * GeoLocation constructor with parameters for all required fields.
	 * 
	 * @param name
	 *            The location name for display, for example "Lakewood, NJ".
	 * @param latitude
	 *            the latitude as a <code>double</code>, for example 40.096 for Lakewood, NJ.
	 *            <b>Note:</b> For latitudes south of the equator, a negative value should be used.
	 * @param longitude
	 *            the longitude as a <code>double</code>, for example -74.222 for Lakewood, NJ. <b>Note:</b> For longitudes
	 *            east of the <a href="https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a> (Greenwich),
	 *            a negative value should be used.
	 * @param timeZone
	 *            the <code>TimeZone</code> for the location.
	 */
	public GeoLocation(String name, double latitude, double longitude, TimeZone timeZone) {
		this(name, latitude, longitude, 0, timeZone);
	}

	/**
	 * GeoLocation constructor with parameters for all required fields.
	 * 
	 * @param name
	 *            The location name for display, for example "Lakewood, NJ".
	 * @param latitude
	 *            the latitude as a <code>double</code>, for example 40.096 for Lakewood, NJ.
	 *            <b>Note:</b> For latitudes south of the equator, a negative value should be used.
	 * @param longitude
	 *            double the longitude as a <code>double</code>, for example -74.222 for Lakewood, NJ.
	 *            <b>Note:</b> For longitudes east of the <a href="https://en.wikipedia.org/wiki/Prime_Meridian">Prime
	 *            Meridian</a> (Greenwich), a negative value should be used.
	 * @param elevation
	 *            the elevation above sea level in Meters.
	 * @param timeZone
	 *            the <code>TimeZone</code> for the location.
	 */
	public GeoLocation(String name, double latitude, double longitude, double elevation, TimeZone timeZone) {
		setLocationName(name);
		setLatitude(latitude);
		setLongitude(longitude);
		setElevation(elevation);
		setTimeZone(timeZone);
	}

	/**
	 * Default GeoLocation constructor will set location to the Prime Meridian at Greenwich, England and a TimeZone of
	 * GMT. The longitude will be set to 0 and the latitude will be 51.4772 to match the location of the <a
	 * href="https://www.rmg.co.uk/royal-observatory">Royal Observatory, Greenwich</a>. No daylight savings time will be used.
	 */
	public GeoLocation() {
		setLocationName("Greenwich, England");
		setLongitude(0); // added for clarity
		setLatitude(51.4772);
		setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Method to set the latitude as a <code>double</code>, for example 40.096 for Lakewood, NJ.
	 * 
	 * @param latitude
	 *            The degrees of latitude to set. The values should be between -90&deg; and 90&deg;. For example 40.096
	 *            would be used for Lakewood, NJ. <b>Note:</b> For latitudes south of the equator, a negative value 
	 *            should be used. An IllegalArgumentException will be thrown if the value exceeds the limits. 
	 */
	public void setLatitude(double latitude) {
		if (latitude > 90 || latitude < -90 || Double.isNaN(latitude)) {
			throw new IllegalArgumentException("Latitude must be between -90 and  90");
		}
		this.latitude = latitude;
	}

	/**
	 * Method to set the latitude in degrees, minutes and seconds. For example, set the degrees to 40, minutes to 5,
	 * seconds to 45.6 and direction to "N" for Lakewood, NJ.
	 * 
	 * @param degrees
	 *            The degrees of latitude to set between 0&deg; and 90&deg;, for example 40 would be used for Lakewood, NJ.
	 *            An IllegalArgumentException will be thrown if the value exceeds the limit.
	 * @param minutes
	 *            <a href="https://en.wikipedia.org/wiki/Minute_of_arc#Cartography">minutes of arc</a>, for example 5
	 *            would be used for Lakewood, NJ.
	 * @param seconds
	 *            <a href="https://en.wikipedia.org/wiki/Minute_of_arc#Cartography">seconds of arc</a>, for example 45.6
	 *             would be used for Lakewood, NJ.
	 * @param direction
	 *            "N" for north and "S" for south,  for example "N" would be used for Lakewood, NJ. An
	 *            IllegalArgumentException will be thrown if the value is not "S" or "N".
	 */
	public void setLatitude(int degrees, int minutes, double seconds, String direction) {
		double tempLat = degrees + ((minutes + (seconds / 60.0)) / 60.0);
		if (tempLat > 90 || tempLat < 0 || Double.isNaN(tempLat)) { //FIXME An exception should be thrown if degrees, minutes or seconds are negative
			throw new IllegalArgumentException(
					"Latitude must be between 0 and  90. Use direction of S instead of negative.");
		}
		if (direction.equals("S")) {
			tempLat *= -1;
		} else if (!direction.equals("N")) {
			throw new IllegalArgumentException("Latitude direction must be N or S");
		}
		this.latitude = tempLat;
	}

	/**
	 * Method to return the latitude as a <code>double</code>, for example 40.096 for Lakewood, NJ.
	 * @return Returns the latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Method to set the longitude as a <code>double</code>, for example -74.222 for Lakewood, NJ
	 * 
	 * @param longitude
	 *            The degrees of longitude to set as a <code>double</code> between -180.0&deg; and 180.0&deg;. For example
	 *            -74.222 would be used for Lakewood, NJ. Note: for longitudes east of the <a href=
	 *            "https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a> (Greenwich) a negative value
	 *            should be used.An IllegalArgumentException will be thrown if the value exceeds the limit. 
	 */
	public void setLongitude(double longitude) {
		if (longitude > 180 || longitude < -180 || Double.isNaN(longitude)) {
			throw new IllegalArgumentException("Longitude must be between -180 and  180");
		}
		this.longitude = longitude;
	}

	/**
	 * Method to set the longitude in degrees, minutes and seconds. For example, set the degrees to 74, minutes to 13,
	 * seconds to 19.2 and direction to "W" for Lakewood, NJ.
	 * 
	 * @param degrees
	 *            The degrees of longitude to set between 0&deg; and 180&deg;. For example 74 would be set for Lakewood, NJ.
	 *            An IllegalArgumentException will be thrown if the value exceeds the limits.
	 * @param minutes
	 *            <a href="https://en.wikipedia.org/wiki/Minute_of_arc#Cartography">minutes of arc</a>. For example 13
	 *            would be set for Lakewood, NJ.
	 * @param seconds
	 *            <a href="https://en.wikipedia.org/wiki/Minute_of_arc#Cartography">seconds of arc</a>. For example 19.2
	 *            would be set for Lakewood, NJ.
	 * @param direction
	 *            "E" for east of the <a href="https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a>
	 *            or "W"for west of it. For example, "W" would be set for Lakewood, NJ.
	 *            An IllegalArgumentException will be thrown if the value is not E or W.
	 */
	public void setLongitude(int degrees, int minutes, double seconds, String direction) {
		double longTemp = degrees + ((minutes + (seconds / 60.0)) / 60.0);
		if (longTemp > 180 || this.longitude < 0  || Double.isNaN(longTemp)) { //FIXME An exception should be thrown if degrees, minutes or seconds are negative
			throw new IllegalArgumentException("Longitude must be between 0 and  180.  Use a direction of W instead of negative.");
		}
		if (direction.equals("W")) {
			longTemp *= -1;
		} else if (!direction.equals("E")) {
			throw new IllegalArgumentException("Longitude direction must be E or W");
		}
		this.longitude = longTemp;
	}

	/**
	 * Method to return the longitude as a <code>double</code>, for example -74.222 for Lakewood, NJ.
	 * @return Returns the longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Method to return the location name (for display), for example "Lakewood, NJ".
	 * @return Returns the location name.
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * Setter for the location name (for display), for example "Lakewood, NJ".
	 * @param name
	 *            The setter method for the display name.
	 */
	public void setLocationName(String name) {
		this.locationName = name;
	}

	/**
	 * Method to return the time zone.
	 * @return Returns the timeZone.
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * Method to set the TimeZone. If this is ever set after the GeoLocation is set in the
	 * {@link com.kosherjava.zmanim.AstronomicalCalendar}, it is critical that
	 * {@link com.kosherjava.zmanim.AstronomicalCalendar#getCalendar()}.
	 * {@link java.util.Calendar#setTimeZone(TimeZone) setTimeZone(TimeZone)} be called in order for the
	 * AstronomicalCalendar to output times in the expected offset. This situation will arise if the
	 * AstronomicalCalendar is ever {@link com.kosherjava.zmanim.AstronomicalCalendar#clone() cloned}.
	 * 
	 * @param timeZone
	 *            The timeZone to set.
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * A method that will return the location's local mean time offset in milliseconds from local <a
	 * href="https://en.wikipedia.org/wiki/Standard_time">standard time</a>. The globe is split into 360&deg;, with
	 * 15&deg; per hour of the day. For a local that is at a longitude that is evenly divisible by 15 (longitude % 15 ==
	 * 0), at solar {@link com.kosherjava.zmanim.AstronomicalCalendar#getSunTransit() noon} (with adjustment for the <a
	 * href="https://en.wikipedia.org/wiki/Equation_of_time">equation of time</a>) the sun should be directly overhead,
	 * so a user who is 1&deg; west of this will have noon at 4 minutes after standard time noon, and conversely, a user
	 * who is 1&deg; east of the 15&deg; longitude will have noon at 11:56 AM. Lakewood, N.J., whose longitude is
	 * -74.222, is 0.778 away from the closest multiple of 15 at -75&deg;. This is multiplied by 4 to yield 3 minutes
	 * and 10 seconds earlier than standard time. The offset returned does not account for the <a
	 * href="https://en.wikipedia.org/wiki/Daylight_saving_time">Daylight saving time</a> offset since this class is
	 * unaware of dates.
	 * 
	 * @return the offset in milliseconds not accounting for Daylight saving time. A positive value will be returned
	 *         East of the 15&deg; timezone line, and a negative value West of it.
	 */
	public long getLocalMeanTimeOffset() {
		return (long) (getLongitude() * 4 * MINUTE_MILLIS - getTimeZone().getRawOffset());
	}
	
	/**
	 * Adjust the date for <a href="https://en.wikipedia.org/wiki/180th_meridian">antimeridian</a> crossover. This is
	 * needed to deal with edge cases such as Samoa that use a different calendar date than expected based on their
	 * geographic location.
	 *
	 * The actual Time Zone offset may deviate from the expected offset based on the longitude. Since the 'absolute time'
	 * calculations are always based on longitudinal offset from UTC for a given date, the date is presumed to only
	 * increase East of the Prime Meridian, and to only decrease West of it. For Time Zones that cross the antimeridian,
	 * the date will be artificially adjusted before calculation to conform with this presumption.
	 *
	 * For example, Apia, Samoa with a longitude of -171.75 uses a local offset of +14:00.  When calculating sunrise for
	 * 2018-02-03, the calculator should operate using 2018-02-02 since the expected zone is -11.  After determining the
	 * UTC time, the local DST offset of <a href="https://en.wikipedia.org/wiki/UTC%2B14:00">UTC+14:00</a> should be applied
	 * to bring the date back to 2018-02-03.
	 * 
	 * @return the number of days to adjust the date This will typically be 0 unless the date crosses the antimeridian
	 */
	public int getAntimeridianAdjustment() {
		double localHoursOffset = getLocalMeanTimeOffset() / (double)HOUR_MILLIS;
		
		if (localHoursOffset >= 20){// if the offset is 20 hours or more in the future (never expected anywhere other
									// than a location using a timezone across the antimeridian to the east such as Samoa)
			return 1; // roll the date forward a day
		} else if (localHoursOffset <= -20) {	// if the offset is 20 hours or more in the past (no current location is known
												//that crosses the antimeridian to the west, but better safe than sorry)
			return -1; // roll the date back a day
		}
		return 0; //99.999% of the world will have no adjustment
	}

	/**
	 * Calculate the initial <a href="https://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing between this
	 * Object and a second Object passed to this method using <a
	 * href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a> inverse formula See T Vincenty, "<a
	 * href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse Solutions of Geodesics on the Ellipsoid
	 * with application of nested equations</a>", Survey Review, vol XXII no 176, 1975
	 * 
	 * @param location
	 *            the destination location
	 * @return the initial bearing
	 */
	public double getGeodesicInitialBearing(GeoLocation location) {
		return vincentyInverseFormula(location, INITIAL_BEARING);
	}

	/**
	 * Calculate the final <a href="https://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing between this Object
	 * and a second Object passed to this method using <a href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus
	 * Vincenty's</a> inverse formula See T Vincenty, "<a href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and
	 * Inverse Solutions of Geodesics on the Ellipsoid with application of nested equations</a>", Survey Review, vol
	 * XXII no 176, 1975
	 * 
	 * @param location
	 *            the destination location
	 * @return the final bearing
	 */
	public double getGeodesicFinalBearing(GeoLocation location) {
		return vincentyInverseFormula(location, FINAL_BEARING);
	}

	/**
	 * Calculate <a href="https://en.wikipedia.org/wiki/Great-circle_distance">geodesic distance</a> in Meters between
	 * this Object and a second Object passed to this method using <a
	 * href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a> inverse formula See T Vincenty, "<a
	 * href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse Solutions of Geodesics on the Ellipsoid
	 * with application of nested equations</a>", Survey Review, vol XXII no 176, 1975
	 * 
	 * @see #vincentyInverseFormula(GeoLocation, int)
	 * @param location
	 *            the destination location
	 * @return the geodesic distance in Meters
	 */
	public double getGeodesicDistance(GeoLocation location) {
		return vincentyInverseFormula(location, DISTANCE);
	}

	/**
	 * Calculate <a href="https://en.wikipedia.org/wiki/Great-circle_distance">geodesic distance</a> in Meters between
	 * this Object and a second Object passed to this method using <a
	 * href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a> inverse formula See T Vincenty, "<a
	 * href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse Solutions of Geodesics on the Ellipsoid
	 * with application of nested equations</a>", Survey Review, vol XXII no 176, 1975
	 * 
	 * @param location
	 *            the destination location
	 * @param formula
	 *            This formula calculates initial bearing ({@link #INITIAL_BEARING}), final bearing (
	 *            {@link #FINAL_BEARING}) and distance ({@link #DISTANCE}).
	 * @return geodesic distance in Meters
	 */
	private double vincentyInverseFormula(GeoLocation location, int formula) {
		double majorSemiAxis = 6378137;
		double minorSemiAxis = 6356752.3142;
		double f = 1 / 298.257223563; // WGS-84 ellipsoid
		double L = Math.toRadians(location.getLongitude() - getLongitude());
		double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(getLatitude())));
		double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(location.getLatitude())));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

		double lambda = L;
		double lambdaP = PI2;
		double iterLimit = 20;
		double sinLambda = 0;
		double cosLambda = 0;
		double sinSigma = 0;
		double cosSigma = 0;
		double sigma = 0;
		double sinAlpha = 0;
		double cosSqAlpha = 0;
		double cos2SigmaM = 0;
		double C;
		while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0) {
			sinLambda = Math.sin(lambda);
			cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
			if (sinSigma == 0)
				return 0; // co-incident points
			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
			if (Double.isNaN(cos2SigmaM))
				cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
			C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L + (1 - C) * f * sinAlpha
					* (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
		}
		if (iterLimit == 0)
			return Double.NaN; // formula failed to converge

		double uSq = cosSqAlpha * (majorSemiAxis * majorSemiAxis - minorSemiAxis * minorSemiAxis) / (minorSemiAxis * minorSemiAxis);
		double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma = B
				* sinSigma
				* (cos2SigmaM + B
						/ 4
						* (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
								* (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
		double distance = minorSemiAxis * A * (sigma - deltaSigma);

		// initial bearing
		double fwdAz = Math.toDegrees(Math.atan2(cosU2 * sinLambda, cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
		// final bearing
		double revAz = Math.toDegrees(Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda));
		if (formula == DISTANCE) {
			return distance;
		} else if (formula == INITIAL_BEARING) {
			return fwdAz;
		} else if (formula == FINAL_BEARING) {
			return revAz;
		} else { // should never happen
			return Double.NaN;
		}
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Rhumb_line">rhumb line</a> bearing from the current location to
	 * the GeoLocation passed in.
	 * 
	 * @param location
	 *            destination location
	 * @return the bearing in degrees
	 */
	public double getRhumbLineBearing(GeoLocation location) {
		double dLon = Math.toRadians(location.getLongitude() - getLongitude());
		double dPhi = Math.log(Math.tan(Math.toRadians(location.getLatitude()) / 2 + PI_4)
				/ Math.tan(Math.toRadians(getLatitude()) / 2 + PI_4));
		if (Math.abs(dLon) > Math.PI)
			dLon = dLon > 0 ? -(PI2 - dLon) : (PI2 + dLon);
		return Math.toDegrees(Math.atan2(dLon, dPhi));
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Rhumb_line">rhumb line</a> distance from the current location
	 * to the GeoLocation passed in.
	 * 
	 * @param location
	 *            the destination location
	 * @return the distance in Meters
	 */
	public double getRhumbLineDistance(GeoLocation location) {
		double earthRadius = 6378137; // Earth's radius in meters (WGS-84)
		double dLat = Math.toRadians(location.getLatitude()) - Math.toRadians(getLatitude());
		double dLon = Math.abs(Math.toRadians(location.getLongitude()) - Math.toRadians(getLongitude()));
		double dPhi = Math.log(Math.tan(Math.toRadians(location.getLatitude()) / 2 + PI_4)
				/ Math.tan(Math.toRadians(getLatitude()) / 2 + PI_4));
		double q = dLat / dPhi;

		if (!(Math.abs(q) <= Double.MAX_VALUE)) {
			q = Math.cos(Math.toRadians(getLatitude()));
		}
		// if dLon over 180° take shorter rhumb across 180° meridian:
		if (dLon > Math.PI) {
			dLon = PI2 - dLon;
		}
		double d = Math.sqrt(dLat * dLat + q * q * dLon * dLon);
		return d * earthRadius;
	}
	
	/**
	 * A method that returns an XML formatted <code>String</code> representing the serialized <code>Object</code>. Very
	 * similar to the toString method but the return value is in an xml format. The format currently used (subject to
	 * change) is:
	 * 
	 * <pre>
	 *   &lt;GeoLocation&gt;
	 *   	 &lt;LocationName&gt;Lakewood, NJ&lt;/LocationName&gt;
	 *   	 &lt;Latitude&gt;40.096&amp;deg&lt;/Latitude&gt;
	 *   	 &lt;Longitude&gt;-74.222amp;deg&lt;/Longitude&gt;
	 *   	 &lt;Elevation&gt;0 Meters&lt;/Elevation&gt;
	 *   	 &lt;TimezoneName&gt;America/New_York&lt;/TimezoneName&gt;
	 *   	 &lt;TimeZoneDisplayName&gt;Eastern Standard Time&lt;/TimeZoneDisplayName&gt;
	 *   	 &lt;TimezoneGMTOffset&gt;-5&lt;/TimezoneGMTOffset&gt;
	 *   	 &lt;TimezoneDSTOffset&gt;1&lt;/TimezoneDSTOffset&gt;
	 *   &lt;/GeoLocation&gt;
	 * </pre>
	 * 
	 * @return The XML formatted <code>String</code>.
	 */
	public String toXML() {
		return "<GeoLocation>\n" +
				"\t<LocationName>" + getLocationName() + "</LocationName>\n" +
				"\t<Latitude>" + getLatitude() + "</Latitude>\n" +
				"\t<Longitude>" + getLongitude() + "</Longitude>\n" +
				"\t<Elevation>" + getElevation() + " Meters" + "</Elevation>\n" +
				"\t<TimezoneName>" + getTimeZone().getID() + "</TimezoneName>\n" +
				"\t<TimeZoneDisplayName>" + getTimeZone().getDisplayName() + "</TimeZoneDisplayName>\n" +
				"\t<TimezoneGMTOffset>" + getTimeZone().getRawOffset() / HOUR_MILLIS +
				"</TimezoneGMTOffset>\n" +
				"\t<TimezoneDSTOffset>" + getTimeZone().getDSTSavings() / HOUR_MILLIS +
				"</TimezoneDSTOffset>\n" +
				"</GeoLocation>";
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof GeoLocation))
			return false;
		GeoLocation geo = (GeoLocation) object;
		return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(geo.latitude)
				&& Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(geo.longitude)
				&& this.elevation == geo.elevation
				&& (Objects.equals(this.locationName, geo.locationName))
				&& (Objects.equals(this.timeZone, geo.timeZone));
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		int result = 17;
		long latLong = Double.doubleToLongBits(this.latitude);
		long lonLong = Double.doubleToLongBits(this.longitude);
		long elevLong = Double.doubleToLongBits(this.elevation);
		int latInt = (int) (latLong ^ (latLong >>> 32));
		int lonInt = (int) (lonLong ^ (lonLong >>> 32));
		int elevInt = (int) (elevLong ^ (elevLong >>> 32));
		result = 37 * result + getClass().hashCode();
		result += 37 * result + latInt;
		result += 37 * result + lonInt;
		result += 37 * result + elevInt;
		result += 37 * result + (this.locationName == null ? 0 : this.locationName.hashCode());
		result += 37 * result + (this.timeZone == null ? 0 : this.timeZone.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "\nLocation Name:\t\t\t" + getLocationName() +
			"\nLatitude:\t\t\t" + getLatitude() + "\u00B0" +
			"\nLongitude:\t\t\t" + getLongitude() + "\u00B0" +
			"\nElevation:\t\t\t" + getElevation() + " Meters" +
			"\nTimezone ID:\t\t\t" + getTimeZone().getID() +
			"\nTimezone Display Name:\t\t" + getTimeZone().getDisplayName() +
			" (" + getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")" +
			"\nTimezone GMT Offset:\t\t" + getTimeZone().getRawOffset() / HOUR_MILLIS +
			"\nTimezone DST Offset:\t\t" + getTimeZone().getDSTSavings() / HOUR_MILLIS;
	}

	/**
	 * An implementation of the {@link java.lang.Object#clone()} method that creates a <a
	 * href="https://en.wikipedia.org/wiki/Object_copy#Deep_copy">deep copy</a> of the object.
	 * <b>Note:</b> If the {@link java.util.TimeZone} in the clone will be changed from the original, it is critical
	 * that {@link com.kosherjava.zmanim.AstronomicalCalendar#getCalendar()}.
	 * {@link java.util.Calendar#setTimeZone(TimeZone) setTimeZone(TimeZone)} is called after cloning in order for the
	 * AstronomicalCalendar to output times in the expected offset.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		GeoLocation clone = null;
		try {
			clone = (GeoLocation) super.clone();
		} catch (CloneNotSupportedException cnse) {
			//Required by the compiler. Should never be reached since we implement clone()
		}
		if (clone != null) {
			clone.timeZone = (TimeZone) getTimeZone().clone();
			clone.locationName = getLocationName();
		}
		return clone;
	}
}
