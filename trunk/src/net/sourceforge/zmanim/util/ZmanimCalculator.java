/*
 * Zmanim Java API
 * Copyright (C) 2004-2010 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc. 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA or connect to: http://www.fsf.org/copyleft/gpl.html
 */
package net.sourceforge.zmanim.util;

import java.util.Calendar;

import net.sourceforge.zmanim.AstronomicalCalendar;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times.
 * This implementation is a port of the C++ algorithm written by Ken Bloom for
 * the sourceforge.net <a href="http://sourceforge.net/projects/zmanim/">Zmanim</a>
 * project. Ken's algorithm is based on the US Naval Almanac algorithm. Added to
 * Ken's code is adjustment of the zenith to account for elevation. Originally released
 * under the GPL, it has been released under the LGPL as of April 8, 2010.
 *
 * @author &copy; Chanoch (Ken) Bloom 2003 - 2004
 * @author &copy; Eliyahu Hershfeld 2004 - 2010
 * @version 1.1
 */
public class ZmanimCalculator extends AstronomicalCalculator {
	private String calculatorName = "US Naval Almanac Algorithm";
	public String getCalculatorName(){
		return calculatorName; //"US Naval Almanac Algorithm";
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunrise(AstronomicalCalendar,
	 *      double, boolean)
	 */
	public double getUTCSunrise(AstronomicalCalendar astronomicalCalendar,
			/*GeoLocation geoLocation,*/ double zenith, boolean adjustForElevation) {
		// zenith = adjustZenithForElevation(astronomicalCalendar, zenith,
		// geoLocation.getElevation());
		// double elevationAdjustment = this.getElevationAdjustment(zenith,
		// geoLocation.getElevation());
		// double refractionAdjustment = this.getRefraction(zenith);
		// zenith = zenith + elevationAdjustment + refractionAdjustment;
		if(adjustForElevation){
			zenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation().getElevation());
		} else {
			zenith = adjustZenith(zenith, 0);
		}

		// step 1: First calculate the day of the year
		// NOT NEEDED in this implementation

		// step 2: convert the longitude to hour value and calculate an
		// approximate time
		double lngHour = astronomicalCalendar.getGeoLocation().getLongitude() / 15;

		double t = astronomicalCalendar.getCalendar().get(Calendar.DAY_OF_YEAR)
				+ ((6 - lngHour) / 24); // use 18 for
		// sunset instead
		// of 6

		// step 3: calculate the sun's mean anomaly
		double m = (0.9856 * t) - 3.289;

		// step 4: calculate the sun's true longitude
		double l = m + (1.916 * Math.sin(Math.toRadians(m)))
				+ (0.020 * Math.sin(Math.toRadians(2 * m))) + 282.634;
		while (l < 0) {
			double Lx = l + 360;
			l = Lx;
		}
		while (l >= 360) {
			double Lx = l - 360;
			l = Lx;
		}

		// step 5a: calculate the sun's right ascension
		double RA = Math.toDegrees(Math.atan(0.91764 * Math.tan(Math
				.toRadians(l))));

		while (RA < 0) {
			double RAx = RA + 360;
			RA = RAx;
		}
		while (RA >= 360) {
			double RAx = RA - 360;
			RA = RAx;
		}

		// step 5b: right ascension value needs to be in the same quadrant as L
		double lQuadrant = Math.floor(l / 90) * 90;
		double raQuadrant = Math.floor(RA / 90) * 90;
		RA = RA + (lQuadrant - raQuadrant);

		// step 5c: right ascension value needs to be converted into hours
		RA /= 15;

		// step 6: calculate the sun's declination
		double sinDec = 0.39782 * Math.sin(Math.toRadians(l));
		double cosDec = Math.cos(Math.asin(sinDec));

		// step 7a: calculate the sun's local hour angle
		double cosH = (Math.cos(Math.toRadians(zenith)) - (sinDec * Math
				.sin(Math.toRadians(astronomicalCalendar.getGeoLocation().getLatitude()))))
				/ (cosDec * Math.cos(Math.toRadians(astronomicalCalendar.getGeoLocation().getLatitude())));

		// the following line would throw an Exception if the sun never rose.
		// this is not needed since the calculation will return a Double.NaN
		// if (cosH > 1) throw new Exception("doesnthappen");

		// FOR SUNSET use the following instead of the above if statement.
		// if (cosH < -1)

		// step 7b: finish calculating H and convert into hours
		double H = 360 - Math.toDegrees(Math.acos(cosH));

		// FOR SUNSET remove "360 - " from the above

		H = H / 15;

		// step 8: calculate local mean time

		double T = H + RA - (0.06571 * t) - 6.622;

		// step 9: convert to UTC
		double UT = T - lngHour;
		while (UT < 0) {
			double UTx = UT + 24;
			UT = UTx;
		}
		while (UT >= 24) {
			double UTx = UT - 24;
			UT = UTx;
		}
		return UT;
	}

	/**
	 * @see net.sourceforge.zmanim.util.AstronomicalCalculator#getUTCSunset(AstronomicalCalendar,
	 *      double, boolean)
	 */
	public double getUTCSunset(AstronomicalCalendar astronomicalCalendar,
			/*GeoLocation geoLocation,*/ double zenith, boolean adjustForElevation) {
		// zenith = adjustZenithForElevation(astronomicalCalendar, zenith,
		// geoLocation.getElevation());
		// double elevationAdjustment = this.getElevationAdjustment(zenith,
		// geoLocation.getElevation());
		// double refractionAdjustment = this.getRefraction(zenith);
		// zenith = zenith + elevationAdjustment + refractionAdjustment;

		if(adjustForElevation){
			zenith = adjustZenith(zenith, astronomicalCalendar.getGeoLocation().getElevation());
		} else {
			zenith = adjustZenith(zenith, 0);
		}

		// step 1: First calculate the day of the year
		// int calendarDayOfYear = calelendar.DAY_OF_YEAR;

		// int N=theday - date(1,1,theday.year()) + 1;
		int N = astronomicalCalendar.getCalendar().get(Calendar.DAY_OF_YEAR);

		// step 2: convert the longitude to hour value and calculate an
		// approximate time
		double lngHour = astronomicalCalendar.getGeoLocation().getLongitude() / 15;

		double t = N + ((18 - lngHour) / 24);

		// step 3: calculate the sun's mean anomaly
		double M = (0.9856 * t) - 3.289;

		// step 4: calculate the sun's true longitude
		double L = M + (1.916 * Math.sin(Math.toRadians(M)))
				+ (0.020 * Math.sin(Math.toRadians(2 * M))) + 282.634;
		while (L < 0) {
			double Lx = L + 360;
			L = Lx;
		}
		while (L >= 360) {
			double Lx = L - 360;
			L = Lx;
		}

		// step 5a: calculate the sun's right ascension
		double RA = Math.toDegrees(Math.atan(0.91764 * Math.tan(Math
				.toRadians(L))));
		while (RA < 0) {
			double RAx = RA + 360;
			RA = RAx;
		}
		while (RA >= 360) {
			double RAx = RA - 360;
			RA = RAx;
		}

		// step 5b: right ascension value needs to be in the same quadrant as L
		double Lquadrant = Math.floor(L / 90) * 90;
		double RAquadrant = Math.floor(RA / 90) * 90;
		RA = RA + (Lquadrant - RAquadrant);

		// step 5c: right ascension value needs to be converted into hours
		RA /= 15;

		// step 6: calculate the sun's declination
		double sinDec = 0.39782 * Math.sin(Math.toRadians(L));
		double cosDec = Math.cos(Math.asin(sinDec));

		// step 7a: calculate the sun's local hour angle
		double cosH = (Math.cos(Math.toRadians(zenith)) - (sinDec * Math
				.sin(Math.toRadians(astronomicalCalendar.getGeoLocation().getLatitude()))))
				/ (cosDec * Math.cos(Math.toRadians(astronomicalCalendar.getGeoLocation().getLatitude())));

		// the following line would throw an Exception if the sun never set.
		// this is not needed since the calculation will return a Double.NaN
		// if (cosH < -1) throw new ZmanimException("doesnthappen");

		// step 7b: finish calculating H and convert into hours
		double H = Math.toDegrees(Math.acos(cosH));
		H = H / 15;

		// step 8: calculate local mean time

		double T = H + RA - (0.06571 * t) - 6.622;

		// step 9: convert to UTC
		double UT = T - lngHour;
		while (UT < 0) {
			double UTx = UT + 24;
			UT = UTx;
		}
		while (UT >= 24) {
			double UTx = UT - 24;
			UT = UTx;
		}
		return UT;
	}
}