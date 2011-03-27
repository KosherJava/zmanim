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

/**
 * A class for various location calculations
 * Most of the code in this class is ported from <a href="http://www.movable-type.co.uk/">Chris Veness'</a>
 * <a href="http://www.fsf.org/licensing/licenses/lgpl.html">LGPL</a> Javascript Implementation
 *
 * @author &copy; Eliyahu Hershfeld 2009
 * @version 0.1
 */
public class GeoLocationUtils {
	private static int DISTANCE = 0;
	private static int INITIAL_BEARING = 1;
	private static int FINAL_BEARING = 2;

	/**
	 * Calculate the initial <a
	 * href="http://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing
	 * between this Object and a second Object passed to this method using <a
	 * href="http://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a>
	 * inverse formula See T Vincenty, "<a
	 * href="http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested
	 * equations</a>", Survey Review, vol XXII no 176, 1975.
	 *
	 * @param location
	 *            the destination location
	 */
	public static double getGeodesicInitialBearing(GeoLocation location, GeoLocation destination) {
		return vincentyFormula(location, destination, INITIAL_BEARING);
	}

	/**
	 * Calculate the final <a
	 * href="http://en.wikipedia.org/wiki/Great_circle">geodesic</a> bearing
	 * between this Object and a second Object passed to this method using <a
	 * href="http://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a>
	 * inverse formula See T Vincenty, "<a
	 * href="http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested
	 * equations</a>", Survey Review, vol XXII no 176, 1975.
	 *
	 * @param location
	 *            the destination location
	 */
	public static double getGeodesicFinalBearing(GeoLocation location, GeoLocation destination) {
		return vincentyFormula(location, destination, FINAL_BEARING);
	}

	/**
	 * Calculate <a
	 * href="http://en.wikipedia.org/wiki/Great-circle_distance">geodesic
	 * distance</a> in Meters between this Object and a second Object passed to
	 * this method using <a
	 * href="http://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a>
	 * inverse formula See T Vincenty, "<a
	 * href="http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested
	 * equations</a>", Survey Review, vol XXII no 176, 1975.
	 *
	 * @param location
	 *            the destination location
	 */
	public static double getGeodesicDistance(GeoLocation location, GeoLocation destination) {
		return vincentyFormula(location, destination, DISTANCE);
	}

	/**
	 * Calculate <a
	 * href="http://en.wikipedia.org/wiki/Great-circle_distance">geodesic
	 * distance</a> in Meters between this Object and a second Object passed to
	 * this method using <a
	 * href="http://en.wikipedia.org/wiki/Thaddeus_Vincenty">Thaddeus Vincenty's</a>
	 * inverse formula See T Vincenty, "<a
	 * href="http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf">Direct and Inverse
	 * Solutions of Geodesics on the Ellipsoid with application of nested
	 * equations</a>", Survey Review, vol XXII no 176, 1975.
	 *
	 * @param location
	 *            the destination location
	 * @param formula
	 *            This formula calculates initial bearing ({@link #INITIAL_BEARING}),
	 *            final bearing ({@link #FINAL_BEARING}) and distance ({@link #DISTANCE}).
	 */
	private static double vincentyFormula(GeoLocation location, GeoLocation destination, int formula) {
		double a = 6378137;
		double b = 6356752.3142;
		double f = 1 / 298.257223563; // WGS-84 ellipsiod
		double L = Math.toRadians(destination.getLongitude() - location.getLongitude());
		double U1 = Math
				.atan((1 - f) * Math.tan(Math.toRadians(location.getLatitude())));
		double U2 = Math.atan((1 - f)
				* Math.tan(Math.toRadians(destination.getLatitude())));
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
		double sinAlpha = 0;
		double cosSqAlpha = 0;
		double cos2SigmaM = 0;
		double C;
		while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0) {
			sinLambda = Math.sin(lambda);
			cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
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
			lambda = L
					+ (1 - C)
					* f
					* sinAlpha
					* (sigma + C
							* sinSigma
							* (cos2SigmaM + C * cosSigma
									* (-1 + 2 * cos2SigmaM * cos2SigmaM)));
		}
		if (iterLimit == 0)
			return Double.NaN; // formula failed to converge

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384
				* (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma = B
				* sinSigma
				* (cos2SigmaM + B
						/ 4
						* (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B
								/ 6 * cos2SigmaM
								* (-3 + 4 * sinSigma * sinSigma)
								* (-3 + 4 * cos2SigmaM * cos2SigmaM)));
		double distance = b * A * (sigma - deltaSigma);

		// initial bearing
		double fwdAz = Math.toDegrees(Math.atan2(cosU2 * sinLambda, cosU1
				* sinU2 - sinU1 * cosU2 * cosLambda));
		// final bearing
		double revAz = Math.toDegrees(Math.atan2(cosU1 * sinLambda, -sinU1
				* cosU2 + cosU1 * sinU2 * cosLambda));
		if (formula == DISTANCE) {
			return distance;
		} else if (formula == INITIAL_BEARING) {
			return fwdAz;
		} else if (formula == FINAL_BEARING) {
			return revAz;
		} else { // should never happpen
			return Double.NaN;
		}
	}

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Rhumb_line">rhumb line</a>
	 * bearing from the current location to the GeoLocation passed in.
	 *
	 * @param location
	 *            destination location
	 * @return the bearing in degrees
	 */
	public static double getRhumbLineBearing(GeoLocation location, GeoLocation destination) {
		double dLon = Math.toRadians(destination.getLongitude() - location.getLongitude());
		double dPhi = Math.log(Math.tan(Math.toRadians(destination.getLatitude())
				/ 2 + Math.PI / 4)
				/ Math.tan(Math.toRadians(location.getLatitude()) / 2 + Math.PI / 4));
		if (Math.abs(dLon) > Math.PI)
			dLon = dLon > 0 ? -(2 * Math.PI - dLon) : (2 * Math.PI + dLon);
		return Math.toDegrees(Math.atan2(dLon, dPhi));
	}

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Rhumb_line">rhumb line</a>
	 * distance from the current location to the GeoLocation passed in.
	 * Ported from <a href="http://www.movable-type.co.uk/">Chris Veness'</a> Javascript Implementation
	 *
	 * @param location
	 *            the destination location
	 * @return the distance in Meters
	 */
	public static double getRhumbLineDistance(GeoLocation location, GeoLocation destination) {
		double R = 6371; // earth's mean radius in km
		double dLat = Math.toRadians(destination.getLatitude() - location.getLatitude());
		double dLon = Math.toRadians(Math.abs(destination.getLongitude()
				- location.getLongitude()));
		double dPhi = Math.log(Math.tan(Math.toRadians(destination.getLongitude())
				/ 2 + Math.PI / 4)
				/ Math.tan(Math.toRadians(location.getLatitude()) / 2 + Math.PI / 4));
		double q = (Math.abs(dLat) > 1e-10) ? dLat / dPhi : Math.cos(Math
				.toRadians(location.getLatitude()));
		// if dLon over 180° take shorter rhumb across 180° meridian:
		if (dLon > Math.PI)
			dLon = 2 * Math.PI - dLon;
		double d = Math.sqrt(dLat * dLat + q * q * dLon * dLon);
		return d * R;
	}

}