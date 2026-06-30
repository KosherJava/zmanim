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

import java.util.Locale;
import java.util.Objects;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;

/**
 * A class that contains location information such as latitude and longitude required for astronomical calculations. The
 * elevation field may not be used by some calculation engines and would be ignored if set. Check the documentation for
 * specific implementations of the {@link AstronomicalCalculator} to see if elevation is calculated as part of the
 * algorithm.
 * 
 * @author © Eliyahu Hershfeld 2004 - 2026
 */
public class GeoLocation implements Cloneable {
	/**
	 * The latitude, for example 40.096 for Lakewood, NJ.
	 * @see #getLatitude()
	 * @see #setLatitude(double)
	 */
	private double latitude;
	
	/**
	 * The longitude, for example -74.222 for Lakewood, NJ.
	 * @see #getLongitude()
	 * @see #setLongitude(double)
	 */
	private double longitude;
	
	/**
	 * The location name used for display, for example "Lakewood, NJ".
	 * @see #getLocationName()
	 * @see #setLocationName(String)
	 */
	private String locationName;
	
	/**
	 * The location's zoneId
	 * @see #getZoneId()
	 * @see #setZoneId(ZoneId)
	 */
	private ZoneId zoneId;
	
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
	 * @param elevation The elevation to set in Meters. An IllegalArgumentException will be thrown if the value is a negative,
	 *         {@link java.lang.Double#isNaN(double)}  or {@link java.lang.Double#isInfinite(double)}.
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
	 * @param name The location name for display, for example "Lakewood, NJ".
	 * @param latitude the latitude as a {@code double}, for example 40.096 for Lakewood, NJ. <b>Note:</b> For latitudes south of
	 *         the equator, a negative value should be used.
	 * @param longitude the longitude as a {@code double}, for example -74.222 for Lakewood, NJ. <b>Note:</b> For longitudes west
	 *         of the <a href="https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a> (Greenwich), a negative value should
	 *         be used.
	 * @param zoneId the {@code ZoneId} for the location.
	 */
	public GeoLocation(String name, double latitude, double longitude, ZoneId zoneId) {
		this(name, latitude, longitude, 0, zoneId);
	}

	/**
	 * GeoLocation constructor with parameters for all required fields.
	 * 
	 * @param name The location name for display, for example "Lakewood, NJ".
	 * @param latitude the latitude as a {@code double}, for example 40.096 for Lakewood, NJ. <b>Note:</b> For latitudes south of
	 *         the equator, a negative value should be used.
	 * @param longitude the longitude as a {@code double}, for example -74.222 for Lakewood, NJ. <b>Note:</b> For longitudes west
	 *         of the <a href="https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a> (Greenwich), a negative value should
	 *         be used.
	 * @param elevation the elevation above sea level in Meters.
	 * @param zoneId the {@code ZoneId} for the location.
	 */
	public GeoLocation(String name, double latitude, double longitude, double elevation, ZoneId zoneId) {
		setLocationName(name);
		setLatitude(latitude);
		setLongitude(longitude);
		setElevation(elevation);
		setZoneId(zoneId);
	}

	/**
	 * Default GeoLocation constructor will set location to the Prime Meridian at Greenwich, England and a {@code ZoneId}
	 * of GMT. The longitude will be set to 0 and the latitude will be 51.4772 to match the location of the <a
	 * href="https://www.rmg.co.uk/royal-observatory">Royal Observatory, Greenwich</a>. No daylight savings time will be used.
	 */
	public GeoLocation() {
		setLocationName("Greenwich, England");
		setLongitude(0); // added for clarity
		setLatitude(51.4772);
		setZoneId(ZoneId.of("GMT"));
	}

	/**
	 * Method to set the latitude as a {@code double}, for example 40.096 for Lakewood, NJ.
	 * 
	 * @param latitude The degrees of latitude to set. The values should be between -90° and 90°. For example 40.096 would be
	 *         used for Lakewood, NJ. <b>Note:</b> For latitudes south of the equator, a negative value should be used. An
	 *         IllegalArgumentException will be thrown if the value exceeds the limits.
	 * @throws IllegalArgumentException if the latitude is not between -90 and 90.
	 */
	public void setLatitude(double latitude) {
		if (latitude > 90 || latitude < -90 || Double.isNaN(latitude)) {
			throw new IllegalArgumentException("Latitude must be between -90 and  90");
		}
		this.latitude = latitude;
	}

	/**
	 * Method to return the latitude as a {@code double}, for example 40.096 for Lakewood, NJ.
	 * @return Returns the latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Method to set the longitude as a {@code double}, for example -74.222 for Lakewood, NJ
	 * 
	 * @param longitude The degrees of longitude to set as a {@code double} between -180.0° and 180.0°. For example
	 *         -74.222 would be used for Lakewood, NJ. Note: for longitudes west of the <a href=
	 *         "https://en.wikipedia.org/wiki/Prime_Meridian">Prime Meridian</a> (Greenwich) a negative value should be used. An
	 *         {@code IllegalArgumentException} will be thrown if the value exceeds the limit.
	 * @throws IllegalArgumentException if the longitude is not between -180 and 180.
	 */
	public void setLongitude(double longitude) {
		if (longitude > 180 || longitude < -180 || Double.isNaN(longitude)) {
			throw new IllegalArgumentException("Longitude must be between -180 and  180");
		}
		this.longitude = longitude;
	}

	/**
	 * Method to return the longitude as a {@code double}, for example -74.222 for Lakewood, NJ.
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
	 * @param name the display name to be set.
	 */
	public void setLocationName(String name) {
		this.locationName = name;
	}
	
	/**
	 * Method to return the {@code ZoneId}.
	 * @return Returns the zoneId.
	 */
	public ZoneId getZoneId() {
		return zoneId;
	}
	
	/**
	 * Method to set the zoneId. If this is ever set after the GeoLocation is set in the
	 * {@link com.kosherjava.zmanim.AstronomicalCalendar}, it is critical that
	 * {@link java.time.ZonedDateTime #setZoneId(ZoneId) setZoneId(ZoneId)} be called in order for the
	 * AstronomicalCalendar to output times in the expected offset. This situation will arise if the
	 * AstronomicalCalendar is ever {@link com.kosherjava.zmanim.AstronomicalCalendar#clone() cloned}.
	 * 
	 * @param zoneId The zoneId to set.
	 */
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * A method that will return the location's local mean time offset in milliseconds from the local clock time defined by the time
	 * zone offset in effect for the supplied {@code Instant}. The globe is split into 360°, with 15° per hour of the day. For
	 * a local that is at a longitude that is evenly divisible by 15 (longitude % 15 == 0), at solar {@link
	 * com.kosherjava.zmanim.AstronomicalCalendar#getSunTransit() noon} (with adjustment for the <a
	 * href="https://en.wikipedia.org/wiki/Equation_of_time">equation of time</a>) the sun should be directly overhead, so a user who
	 * is 1° west of this will have noon at 4 minutes after local clock noon, and conversely, a user who is 1° east of the 15°
	 * longitude will have noon at 11:56 AM local clock time. Lakewood, N.J., whose longitude is -74.222, is 0.778 away from the
	 * closest multiple of 15 at -75°. This is multiplied by 4 to yield 3 minutes and 10 seconds earlier than the local clock tim
	 *  derived from the zone offset in effect for the supplied instant, including any applicable <a href=
	 *  "https://en.wikipedia.org/wiki/Daylight_saving_time">Daylight saving time</a> adjustment.
	 * 
	 * @param instant the {@code Instant} used to calculate the local mean offset for the date in question.
	 * @return the offset in milliseconds relative to the time zone offset in effect at the supplied instant. A positive value will
	 *         be returned East of the 15° timezone line, and a negative value West of it.
	 */
	public long getLocalMeanTimeOffset(Instant instant) {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		long timezoneOffsetMillis = zonedDateTime.getOffset().getTotalSeconds() * 1000L;
		return (long) (getLongitude() * 4 * MINUTE_MILLIS - timezoneOffsetMillis);
	}
	
	/**
	 * Adjust the date for <a href="https://en.wikipedia.org/wiki/180th_meridian">antimeridian</a> crossover. This is
	 * needed to deal with edge cases such as Samoa that use a different calendar date than expected based on their
	 * geographic location. The actual Time Zone offset may deviate from the expected offset based on the longitude. Since the
	 * "absolute time" calculations are always based on longitudinal offset from UTC for a given date, the date is presumed to only
	 * increase East of the Prime Meridian, and to only decrease West of it. For Time Zones that cross the antimeridian, the date
	 * will be artificially adjusted before calculation to conform with this presumption. For example, Apia, Samoa with a longitude
	 * of -171.75 uses a local offset of +14:00.  When calculating sunrise for 2018-02-03, the calculator should operate using
	 * 2018-02-02 since the expected zone is -11.  After determining the UTC time, the local DST offset of <a href=
	 * "https://en.wikipedia.org/wiki/UTC%2B14:00">UTC+14:00</a> should be applied to bring the date back to 2018-02-03.
	 * 
	 * @param instant the {@code Instant} required for the local mean time offset calculation
	 * @return the number of days to adjust the date This will typically be 0 unless the date crosses the antimeridian
	 */
	public int getAntimeridianAdjustment(Instant instant) {
		double localHoursOffset = getLocalMeanTimeOffset(instant) / (double)HOUR_MILLIS;
		
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
	 * Calculate the initial <a href="https://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing between this and another
	 * {@code GeoLocation} passed to this method using <a href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus
	 * Vincenty's</a> inverse formula See T Vincenty, <a href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">"Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested equations"</a>, Survey Review, vol. XXII no 176, 1975
	 * 
	 * @param location the destination location
	 * @return the initial bearing
	 */
	public double getGeodesicInitialBearing(GeoLocation location) {
		return vincentyInverseFormula(location, INITIAL_BEARING);
	}

	/**
	 * Calculate the final <a href="https://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing between this and another
	 * {@code GeoLocation} passed to this method using <a href="https://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus
	 * Vincenty's</a> inverse formula See T Vincenty, <a href="https://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">"Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested equations"</a>, Survey Review, vol. XXII no 176, 1975
	 * 
	 * @param location the destination location
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
	 * @param location the destination location
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
	 * @param location the destination location
	 * @param formula This formula calculates initial bearing ({@link #INITIAL_BEARING}), final bearing ({@link #FINAL_BEARING}) and
	 *         distance ({@link #DISTANCE}).
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
		double lambdaP = 2 * Math.PI;
		double iterLimit = 20;
		double sinLambda = 0;
		double cosLambda = 0;
		double sinSigma = 0;
		double cosSigma = 0;
		double sigma = 0;
		double sinAlpha;
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
	 * @param location destination location
	 * @return the bearing in degrees
	 */
	public double getRhumbLineBearing(GeoLocation location) {
		double dLon = Math.toRadians(location.getLongitude() - getLongitude());
		double dPhi = Math.log(Math.tan(Math.toRadians(location.getLatitude()) / 2 + Math.PI / 4)
				/ Math.tan(Math.toRadians(getLatitude()) / 2 + Math.PI / 4));
		if (Math.abs(dLon) > Math.PI)
			dLon = dLon > 0 ? -(2 * Math.PI - dLon) : (2 * Math.PI + dLon);
		return Math.toDegrees(Math.atan2(dLon, dPhi));
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Rhumb_line">rhumb line</a> distance from the current location
	 * to the GeoLocation passed in.
	 * 
	 * @param location the destination location
	 * @return the distance in Meters
	 */
	public double getRhumbLineDistance(GeoLocation location) {
		double earthRadius = 6378137; // Earth's radius in meters (WGS-84)
		double dLat = Math.toRadians(location.getLatitude()) - Math.toRadians(getLatitude());
		double dLon = Math.abs(Math.toRadians(location.getLongitude()) - Math.toRadians(getLongitude()));
		double dPhi = Math.log(Math.tan(Math.toRadians(location.getLatitude()) / 2 + Math.PI / 4)
				/ Math.tan(Math.toRadians(getLatitude()) / 2 + Math.PI / 4));
		double q = dLat / dPhi;

		if (!(Math.abs(q) <= Double.MAX_VALUE)) {
			q = Math.cos(Math.toRadians(getLatitude()));
		}
		// if dLon over 180° take shorter rhumb across 180° meridian:
		if (dLon > Math.PI) {
			dLon = 2 * Math.PI - dLon;
		}
		double d = Math.sqrt(dLat * dLat + q * q * dLon * dLon);
		return d * earthRadius;
	}
	
	/**
	 * A method that returns an XML formatted {@code String} representing the serialized {@code Object}. Very
	 * similar to the toString method but the return value is in an xml format. The format currently used (subject to
	 * change) is:
	 * 
	 * {@snippet lang="xml" :
	 * <GeoLocation>
	 *     <LocationName>Lakewood, NJ</LocationName>
	 * 	   <Latitude>40.096°</Latitude>
	 *     <Longitude>-74.222°</Longitude>
	 *     <Elevation>0 Meters</Elevation>
	 * 	   <TimeZoneName>America/New_York</TimeZoneName>
	 * 	   <TimeZoneDisplayName>Eastern Standard Time</TimeZoneDisplayName>
	 * </GeoLocation>
	 * }
	 * 
	 * @return The XML formatted {@code String}.
	 */
	public String toXML() {
		return "<GeoLocation>\n" +
				"\t<LocationName>" + getLocationName() + "</LocationName>\n" +
				"\t<Latitude>" + getLatitude() + "</Latitude>\n" +
				"\t<Longitude>" + getLongitude() + "</Longitude>\n" +
				"\t<Elevation>" + getElevation() + " Meters" + "</Elevation>\n" +
				"\t<TimezoneName>" + getZoneId().getId() + "</TimezoneName>\n" +
				"\t<TimeZoneDisplayName>" + getZoneId().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "</TimeZoneDisplayName>\n" +
				"</GeoLocation>";
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		GeoLocation geo = (GeoLocation) object;

		return Double.compare(this.latitude, geo.latitude) == 0
			&& Double.compare(this.longitude, geo.longitude) == 0
			&& Double.compare(this.elevation, geo.elevation) == 0
			&& Objects.equals(this.locationName, geo.locationName)
			&& Objects.equals(this.zoneId, geo.zoneId);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getClass(), this.latitude, this.longitude, this.elevation, this.locationName, this.zoneId);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\nLocation Name:\t\t\t" + getLocationName() +
			"\nLatitude:\t\t\t" + getLatitude() + "\u00B0" +
			"\nLongitude:\t\t\t" + getLongitude() + "\u00B0" +
			"\nElevation:\t\t\t" + getElevation() + " Meters" +
			"\nTimezone ID:\t\t\t" + getZoneId().getId() +
			"\nTimezone Display Name:\t\t" + getZoneId().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	}

	/**
	 * An implementation of the {@link java.lang.Object#clone()} method that creates a <a
	 * href="https://en.wikipedia.org/wiki/Object_copy#Deep_copy">deep copy</a> of the object.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		GeoLocation clone = null;
		try {
			clone = (GeoLocation) super.clone();
		} catch (CloneNotSupportedException cnse) {
			throw new AssertionError("Clone not supported on a Cloneable object", cnse);
		}
		if (clone != null) {
			clone.zoneId = getZoneId();
			clone.locationName = getLocationName();
		}
		return clone;
	}
}
