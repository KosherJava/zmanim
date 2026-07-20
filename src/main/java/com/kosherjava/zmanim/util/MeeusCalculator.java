/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
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
import java.util.Objects;

/**
 * Implementation of sunrise and sunset methods using the <b>higher-accuracy</b> solar position algorithm of
 * <a href="https://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a> from
 * <a href="https://www.willbell.com/math/mc1.htm">Astronomical Algorithms</a> (2nd ed.). Unlike the {@link NOAACalculator} (which
 * uses the low-accuracy method from the same work, with a three-term equation of center and W. M. Smart's truncated equation of time,
 * good to roughly 0.01° / about 1 minute of time), this calculator computes the Earth's heliocentric position from the abridged <a
 * href="https://en.wikipedia.org/wiki/VSOP_(planets)">VSOP87</a> series printed in Meeus's Appendix III, then derives the Sun's
 * apparent geocentric longitude (applying the FK5 frame correction, nutation in longitude and the aberration of light), apparent
 * right ascension and declination, and the equation of time per Meeus chapter 28. The resulting solar position is good to roughly an
 * arc-second, an order of magnitude better than the low-accuracy method.
 *
 * <p><b>Practical note on accuracy.</b> For horizon events (sunrise / sunset) the extra precision is largely masked by atmospheric
 * refraction (uncertain by arc-minutes with temperature and pressure) and is therefore typically worth only a few seconds over
 * {@link NOAACalculator}. The improvement is most visible for solar noon, the equation of time, and the Sun's elevation / azimuth
 * when it is well above the horizon.
 *
 * <p><b>Refraction and the Sun's radius are applied at sunrise and sunset only.</b> Like {@link NOAACalculator}, this class does not
 * model atmospheric refraction itself; the horizon corrections come entirely from the parent {@link #adjustZenith(double, double,
 * double, LocalDate)}, which adds {@link #getRefraction()} (default 0.5667° / 34′), the solar radius - the date-based {@link
 * #getApparentSolarRadius(LocalDate)} by default (it varies slightly day to day), or the fixed {@link #getSolarRadius()} (default
 * 0.26667° / 16′) when {@link #isUseApparentSolarRadius()} is {@code false} - and the {@link
 * #getElevationAdjustment(double, double) elevation dip} (WGS84 latitude-dependent geocentric radius with atmospheric refraction
 * correction), but <em>only</em> when the requested zenith is exactly the geometric 90°, that is,
 * only for true sunrise and sunset. For every other zenith - twilight angles and depression-angle <em>zmanim</em> such as
 * <em>alos</em> and <em>tzais</em> (e.g. 16.1°, 19.8°) - no refraction or solar-radius term is added and the time is solved for the
 * Sun's true geometric depression, as those <em>zmanim</em> are defined geometrically. Consequently {@link #setRefraction(double)}
 * and {@link #setSolarRadius(double)} affect sunrise / sunset (and any 90°-based time) but have no effect on depression-angle
 * <em>zmanim</em>. This calculator has no temperature or pressure inputs of any kind, since the only place they would matter is a
 * refraction model it does not use.
 *
 * <p><b>ΔT (terrestrial vs. universal time).</b> Meeus computes positions in Terrestrial Time (TT), whereas the civil times returned
 * here are Universal Time (UT ≈ UTC). This calculator therefore applies an estimate of <a href=
 * "https://en.wikipedia.org/wiki/%CE%94T_(timekeeping)">ΔT</a> (currently about a minute) when evaluating the Sun's position. This is
 * more correct than {@link NOAACalculator}, which omits ΔT. The correction can be disabled via {@link #setApplyDeltaT(boolean)} to
 * reproduce NOAA-style UT = TT behavior, which is useful for direct comparison.
 *
 * <p><b>Coefficient coverage.</b> The VSOP87 tables below are the abridged Earth series from Meeus Appendix III and the principal
 * (Meeus chapter 22 abridged) nutation terms. They yield sub-arc-second to a few arc-seconds of accuracy - far beyond what any
 * <em>zman</em> requires. For full textbook precision the term tables can simply be extended with the remaining Appendix III /
 * IAU 1980 entries; the algorithm itself does not change.
 *
 * @author © Eliyahu Hershfeld 2026
 */
public class MeeusCalculator extends AstronomicalCalculator {

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
	 * Whether to apply the ΔT (TT - UT) correction when evaluating the Sun's position. Defaults to
	 * {@code true} (the astronomically correct behavior). Set to {@code false} to reproduce NOAA-style UT = TT
	 * results for direct comparison with {@link NOAACalculator}.
	 * @see #setApplyDeltaT(boolean)
	 */
	private boolean applyDeltaT = true;

	/**
	 * An {@code enum} to indicate what type of solar event ({@link #SUNRISE SUNRISE}, {@link #SUNSET SUNSET},
	 * {@link #NOON NOON} or {@link #MIDNIGHT MIDNIGHT}) is being calculated.
	 */
	protected enum SolarEvent {
		/**SUNRISE A solar event related to sunrise*/SUNRISE, /**SUNSET A solar event related to sunset*/SUNSET,
		/**NOON A solar event related to noon*/NOON, /**MIDNIGHT A solar event related to midnight*/MIDNIGHT
	}

	/**
	 * Default constructor of the MeeusCalculator.
	 */
	public MeeusCalculator() {
		super();
	}

	@Override
	public String getCalculatorName() {
		return "Jean Meeus Higher-Accuracy (VSOP87) Algorithm";
	}

	/**
	 * A method to enable or disable the {@link #applyDeltaT ΔT} (TT - UT) correction.
	 * @param applyDeltaT {@code true} to apply ΔT (default, astronomically correct), {@code false} to omit it and
	 *         reproduce NOAA-style UT = TT behavior.
	 * @see #isApplyDeltaT()
	 */
	public void setApplyDeltaT(boolean applyDeltaT) {
		this.applyDeltaT = applyDeltaT;
	}

	/**
	 * is the {@link #applyDeltaT ΔT} correction being applied. 
	 * @return whether the {@link #applyDeltaT ΔT} correction is being applied.
	 * @see #setApplyDeltaT(boolean)
	 */
	public boolean isApplyDeltaT() {
		return this.applyDeltaT;
	}

	@Override
	public double getUTCSunrise(LocalDate dt, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		return getUTCSunRiseSet(dt, geoLocation, zenith, adjustForElevation, SolarEvent.SUNRISE);
	}

	@Override
	public double getUTCSunset(LocalDate dt, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		return getUTCSunRiseSet(dt, geoLocation, zenith, adjustForElevation, SolarEvent.SUNSET);
	}

	/**
	 * A method that calculates UTC sunrise or sunset as well as any time based on an angle above or below sunset and
	 * returns it as a {@code double} in 24-hour format. 5:45:00 AM will return 5.75.
	 *
	 * @param localDate Used to calculate day of year.
	 * @param geoLocation The location information used for astronomical calculation of solar times.
	 * @param zenith the azimuth below the vertical zenith of 90°, {@link #adjustZenith adjusted} for refraction, the
	 *         sun's radius and (optionally) elevation.
	 * @param adjustForElevation Should the time be adjusted for elevation.
	 * @param solarEvent if the calculation is for {@link SolarEvent#SUNRISE} or {@link SolarEvent#SUNSET}.
	 * @return The UTC time of sunrise or sunset in 24-hour format. 5:45:00 AM will return 5.75, while 5:45 PM will return
	 *         17.75. If an error was encountered in the calculation (expected behavior for some locations such as near the
	 *         poles, {@link Double#NaN} will be returned.
	 * @see #getElevationAdjustment(double, double)
	 */
	private double getUTCSunRiseSet(LocalDate localDate, GeoLocation geoLocation, double zenith,
			boolean adjustForElevation, SolarEvent solarEvent) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation, geoLocation.getLatitude(), localDate);
		double riseSet = getSunRiseSetUTC(localDate, geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith, solarEvent);
		riseSet = riseSet / 60;
		return (riseSet % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
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
	public double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation) {
		double midnight = getSolarNoonMidnightUTC(getJulianDay(localDate), -geoLocation.getLongitude(), SolarEvent.MIDNIGHT);
		midnight = midnight / 60;
		return (midnight % 24 + 24) % 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of solar noon, or the upcoming midnight (about 12 hours after solar noon) of the given day at the given location, in
	 * minutes after 0:00 UTC. Uses the high-accuracy equation of time.
	 *
	 * @param julianDay The Julian day (UT, for the start of the day).
	 * @param longitude The longitude of the observer in degrees (positive west, matching the value passed by the callers).
	 * @param solarEvent If the calculation is for {@link SolarEvent#NOON NOON} or {@link SolarEvent#MIDNIGHT MIDNIGHT}.
	 * @return the time in minutes after 0:00 UTC.
	 */
	private double getSolarNoonMidnightUTC(double julianDay, double longitude, SolarEvent solarEvent) {
		// no day-half shift: the loop epoch julianDay + solNoonUTC/1440 already lands on the event
		// First pass: seed includes the 720/1440-minute transit base so the equation of time is sampled near the true
		// transit (the NOAACalculator omits this base in its seed; here it is included for consistency with the loop).
		double base = (solarEvent == SolarEvent.NOON) ? 720 : 1440;
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + longitude / 360.0);
		double equationOfTime = getEquationOfTime(tnoon);
		double solNoonUTC = base + (longitude * 4) - equationOfTime; // minutes

		// Refine the equation of time at the calculated transit time.
		for (int i = 0; i < 3; i++) {
			double newt = getJulianCenturiesFromJulianDay(julianDay + solNoonUTC / 1440.0);
			equationOfTime = getEquationOfTime(newt);
			solNoonUTC = base + (longitude * 4) - equationOfTime;
		}
		return solNoonUTC;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of sunrise or sunset in minutes for the given day at the given location on earth, using the high-accuracy solar
	 * declination and equation of time.
	 *
	 * @param localDate The {@code LocalDate}.
	 * @param latitude The latitude of observer in degrees.
	 * @param longitude Longitude of observer in degrees (positive west).
	 * @param zenith Zenith (already adjusted for refraction / radius / elevation by the caller).
	 * @param solarEvent If the calculation is for {@link SolarEvent#SUNRISE SUNRISE} or {@link SolarEvent#SUNSET SUNSET}.
	 * @return The UTC time of sunrise or sunset in minutes. {@link Double#NaN} if the event does not occur (e.g. polar day
	 *         / night).
	 */
	private double getSunRiseSetUTC(LocalDate localDate, double latitude, double longitude, double zenith,
			SolarEvent solarEvent) {
		double julianDay = getJulianDay(localDate);

		// Use the declination at local solar noon as the starting point, then iterate.
		double noonmin = getSolarNoonMidnightUTC(julianDay, longitude, SolarEvent.NOON);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);
		double equationOfTime = getEquationOfTime(tnoon);
		double solarDeclination = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		double delta = longitude - Math.toDegrees(hourAngle);
		double timeUTC = 720 + (4 * delta) - equationOfTime;

		// Additional passes including the fractional Julian Day for improved accuracy (a third pass over the NOAA
		// implementation's two helps convergence at high latitudes).
		for (int i = 0; i < 2; i++) {
			double newt = getJulianCenturiesFromJulianDay(julianDay + timeUTC / 1440.0);
			equationOfTime = getEquationOfTime(newt);
			solarDeclination = getSunDeclination(newt);
			hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
			if (Double.isNaN(hourAngle)) {
				return Double.NaN;
			}
			delta = longitude - Math.toDegrees(hourAngle);
			timeUTC = 720 + (4 * delta) - equationOfTime;
		}
		return timeUTC;
	}

	/**
	 * {@inheritDoc}
	 * The current implementation in this class only supports azimuth values of 90° (directly east) or 270° (directly west) that are
	 * directly needed in this library for the {@link com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getPolarSunsetBenIshChai()}
	 * and {@link com.kosherjava.zmanim.ComprehensiveZmanimCalendar#getPolarSunriseBenIshChai()}.
	 * @throws IllegalArgumentException if the azimuth is not 90° or 270°.
	 * @todo complete the implementation for other azimuths. While not needed by this library, they may be of value to some projects.
	 *         There will be edge cases where the azimuth will occur more than once a day when based on the equation of time, the day
	 *         is shorter than 24 hours. In that case, the time for the first one will be returned.
	 */
	@Override
	public double getTimeAtAzimuth(LocalDate localDate, GeoLocation geoLocation, double targetAzimuth) {
		if (targetAzimuth != 90.0 && targetAzimuth != 270.0) {
			throw new IllegalArgumentException("The targetAzimuth must be 90 or 270. Other azimuth values are not supported");
		}

		double julianDay = getJulianDay(localDate);
		double solarNoonBase = 0.5 - (geoLocation.getLongitude() / 360.0);
		double dateTime = solarNoonBase + ((targetAzimuth == 90.0) ? 0.25 : 0.75);

		for (int i = 0; i < 3; i++) {
			double julianCenturies = getJulianCenturiesFromJulianDay(julianDay + dateTime);
			double ratio = tanDegrees(getSunDeclination(julianCenturies)) / tanDegrees(geoLocation.getLatitude());

			if (Double.isNaN(ratio) || ratio > 1.0 || ratio < -1.0) { // Handle Tropics, Polar Regions, and Equator line divisions
				return Double.NaN;
			}

			double offset = ((targetAzimuth == 90.0) ? -1.0 : 1.0) * (acosDegrees(ratio) / 360.0);
			dateTime = solarNoonBase + offset - (getEquationOfTime(julianCenturies) / 1440.0);
		}

		double timeUTC = dateTime * 24.0;
		return (timeUTC % 24 + 24) % 24; // normalize to [0,24) like the sibling UTC methods
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
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">solar elevation</a> or
	 * <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">solar azimuth</a> at the given location and
	 * time, using the high-accuracy solar declination and equation of time. Elevation is sea-level based and corrected for
	 * atmospheric refraction; it is not adjusted for the observer's altitude.
	 *
	 * @param instant date-time of calculation.
	 * @param geoLocation the location for calculating the elevation or azimuth.
	 * @param isAzimuth {@code true} for azimuth, {@code false} for elevation.
	 * @return solar elevation or azimuth in degrees.
	 */
	private double getSolarElevationAzimuth(Instant instant, GeoLocation geoLocation, boolean isAzimuth) {
		double lat = geoLocation.getLatitude();
		double lon = geoLocation.getLongitude();
		ZonedDateTime utc = instant.atZone(ZoneOffset.UTC);
		double fractionalDay = (utc.getHour() + (utc.getMinute()
				+ (utc.getSecond() + utc.getNano() / 1_000_000_000.0) / 60.0) / 60.0) / 24.0;
		double jc = getJulianCenturiesFromJulianDay(getJulianDay(utc.toLocalDate()) + fractionalDay);
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
	 * Apply an atmospheric refraction adjustment to the solar elevation (same model used by {@link NOAACalculator}).
	 * @param elevation the elevation to adjust.
	 * @return the refraction adjustment in degrees.
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
	 * Return the <a href="https://en.wikipedia.org/wiki/Hour_angle">hour angle</a> of the sun in radians for the given
	 * latitude, declination and zenith.
	 * @param latitude the latitude of observer in degrees.
	 * @param solarDeclination the declination of the sun in degrees.
	 * @param zenith the (adjusted) zenith in degrees.
	 * @param solarEvent {@link SolarEvent#SUNRISE} or {@link SolarEvent#SUNSET}.
	 * @return the hour angle in radians, or {@link Double#NaN} if the sun does not reach the zenith on that day.
	 */
	private static double getSunHourAngle(double latitude, double solarDeclination, double zenith, SolarEvent solarEvent) {
		double ratio = cosDegrees(zenith) / (cosDegrees(latitude) * cosDegrees(solarDeclination))
				- tanDegrees(latitude) * tanDegrees(solarDeclination);
		if (ratio < -1.0 || ratio > 1.0) {
			return Double.NaN; // the sun never reaches this zenith (polar day / night)
		}
		double hourAngle = Math.acos(ratio);
		if (solarEvent == SolarEvent.SUNSET) {
			hourAngle = -hourAngle;
		}
		return hourAngle;
	}

	// ---------------------------------------------------------------------------------------------------------------
	// Time scales
	// ---------------------------------------------------------------------------------------------------------------

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> for the start (0h UT) of the given
	 * date. Fractional days / time of day should be added later. (Meeus, chapter 7.)
	 * @param localDate the {@code LocalDate}.
	 * @return the Julian day for 0h UT of the date.
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
	 * Convert a Universal-Time <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> to Julian centuries
	 * since <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a> in <b>Terrestrial Time</b>. When
	 * {@link #applyDeltaT} is {@code true} the {@link #getDeltaT(double) ΔT} (TT - UT) offset is
	 * added so that the solar-position series, which are defined in TT, are evaluated correctly.
	 * @param julianDayUT the UT Julian day to convert.
	 * @return the Terrestrial-Time centuries since J2000.0.
	 */
	private double getJulianCenturiesFromJulianDay(double julianDayUT) {
		double jde = julianDayUT;
		if (applyDeltaT) {
			jde += getDeltaT(julianDayUT) / 86400.0; // ΔT is in seconds; convert to days
		}
		return (jde - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
	}

	/**
	 * Estimate <a href="https://en.wikipedia.org/wiki/%CE%94T_(timekeeping)">ΔT</a> = TT - UT in
	 * seconds for the given Julian day, using the polynomial expressions of Espenak and Meeus. The value is
	 * approximate (especially in the far past / future) but accurate to a few seconds for the modern era, which is well
	 * within the precision relevant here.
	 * @param julianDay the (UT) Julian day.
	 * @return ΔT in seconds.
	 */
	private static double getDeltaT(double julianDay) {
		double year = 2000.0 + (julianDay - JULIAN_DAY_JAN_1_2000) / 365.25;
		double t;
		if (year >= 2005 && year < 2050) {
			t = year - 2000;
			return 62.92 + 0.32217 * t + 0.005589 * t * t;
		} else if (year >= 1986 && year < 2005) {
			t = year - 2000;
			return 63.86 + 0.3345 * t - 0.060374 * t * t + 0.0017275 * t * t * t
					+ 0.000651814 * Math.pow(t, 4) + 0.00002373599 * Math.pow(t, 5);
		} else if (year >= 1961 && year < 1986) {
			t = year - 1975;
			return 45.45 + 1.067 * t - t * t / 260.0 - t * t * t / 718.0;
		} else if (year >= 2050 && year < 2150) {
			return -20 + 32 * Math.pow((year - 1820) / 100.0, 2) - 0.5628 * (2150 - year);
		} else { // generic parabola of Espenak and Meeus for dates well outside the calibrated range
			double u = (year - 1820) / 100.0;
			return -20 + 32 * u * u;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------
	// High-accuracy solar position (Meeus chapters 22, 25, 28 with abridged VSOP87, Appendix III)
	// ---------------------------------------------------------------------------------------------------------------

	/**
	 * Return the apparent <a href="https://en.wikipedia.org/wiki/Declination">declination</a> of the sun in degrees,
	 * computed from the apparent geocentric ecliptic coordinates derived from the VSOP87 series.
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return the sun's apparent declination in degrees.
	 */
	private static double getSunDeclination(double julianCenturies) {
		double[] sun = getSunApparentEclipticCoordinates(julianCenturies); // {lambda, beta} in degrees
		double lambda = sun[0];
		double beta = sun[1];
		double epsilon = getTrueObliquity(julianCenturies);
		double sinDec = sinDegrees(beta) * cosDegrees(epsilon) + cosDegrees(beta) * sinDegrees(epsilon) * sinDegrees(lambda);
		return asinDegrees(sinDec);
	}

	/**
	 * Return the apparent geocentric <a href="https://en.wikipedia.org/wiki/Right_ascension">right ascension</a> of the
	 * sun in degrees (0-360).
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return the sun's apparent right ascension in degrees.
	 */
	private static double getSunRightAscension(double julianCenturies) {
		double[] sun = getSunApparentEclipticCoordinates(julianCenturies);
		double lambda = sun[0];
		double beta = sun[1];
		double epsilon = getTrueObliquity(julianCenturies);
		double y = sinDegrees(lambda) * cosDegrees(epsilon) - tanDegrees(beta) * sinDegrees(epsilon);
		double x = cosDegrees(lambda);
		double ra = Math.toDegrees(Math.atan2(y, x));
		return (ra % 360 + 360) % 360;
	}

	/**
	 * Return the sun's <b>apparent</b> geocentric ecliptic longitude and latitude in degrees, built from the Earth's
	 * heliocentric VSOP87 position with the FK5 frame correction, nutation in longitude and the aberration of light
	 * applied (Meeus chapter 25).
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return a two-element array {apparent longitude λ, apparent latitude β} in degrees.
	 */
	private static double[] getSunApparentEclipticCoordinates(double julianCenturies) {
		double tau = julianCenturies / 10.0; // Julian millennia
		double earthL = Math.toDegrees(sumSeries(EARTH_L, tau));   // Earth heliocentric longitude (deg)
		double earthB = Math.toDegrees(sumSeries(EARTH_B, tau));   // Earth heliocentric latitude (deg)
		double earthR = sumSeries(EARTH_R, tau);                   // Earth radius vector (AU)

		// Geocentric position of the Sun is diametrically opposite the heliocentric Earth.
		double theta = earthL + 180.0;       // geocentric longitude (deg), dynamical (VSOP87) frame
		double beta = -earthB;               // geocentric latitude (deg)

		// FK5 frame correction (Meeus 25.9): convert from the VSOP87 dynamical frame to FK5.
		double lambdaPrime = theta - 1.397 * julianCenturies - 0.00031 * julianCenturies * julianCenturies;
		double deltaLongFK5 = -0.09033 + 0.03916 * (cosDegrees(lambdaPrime) + sinDegrees(lambdaPrime)) * tanDegrees(beta);
		double deltaLatFK5 = 0.03916 * (cosDegrees(lambdaPrime) - sinDegrees(lambdaPrime));
		theta += deltaLongFK5 / 3600.0;
		beta += deltaLatFK5 / 3600.0;

		// Nutation in longitude (apparent place) and aberration of light.
		double[] nut = getNutation(julianCenturies); // {Δψ, Δε} in arc-seconds
		double aberration = -20.4898 / earthR;        // arc-seconds (Meeus 25, abridged)
		double lambda = theta + nut[0] / 3600.0 + aberration / 3600.0;

		return new double[] { (lambda % 360 + 360) % 360, beta };
	}

	/**
	 * Return the true <a href="https://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> ε =
	 * ε<sub>0</sub> + Δε in degrees (mean obliquity, Meeus 22.2, plus nutation in
	 * obliquity).
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return the true obliquity in degrees.
	 */
	private static double getTrueObliquity(double julianCenturies) {
		double seconds = 21.448 - julianCenturies
				* (46.8150 + julianCenturies * (0.00059 - julianCenturies * 0.001813));
		double epsilon0 = 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
		double deltaEpsilon = getNutation(julianCenturies)[1] / 3600.0;
		return epsilon0 + deltaEpsilon;
	}

	/**
	 * Return the principal terms of the nutation in longitude (Δψ) and obliquity (Δε) in
	 * arc-seconds (Meeus chapter 22, abridged). These dominant terms give sub-arc-second accuracy in the nutation, which
	 * is far beyond what is required here; the full IAU 1980 series can be substituted for textbook precision.
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return a two-element array {Δψ, Δε} in arc-seconds.
	 */
	private static double[] getNutation(double julianCenturies) {
		double t = julianCenturies;
		double omega = 125.04452 - 1934.136261 * t + 0.0020708 * t * t + t * t * t / 450000.0; // moon's ascending node
		double lSun = 280.4665 + 36000.7698 * t;   // mean longitude of the sun
		double lMoon = 218.3165 + 481267.8813 * t; // mean longitude of the moon
		double deltaPsi = -17.20 * sinDegrees(omega) - 1.32 * sinDegrees(2 * lSun)
				- 0.23 * sinDegrees(2 * lMoon) + 0.21 * sinDegrees(2 * omega);
		double deltaEpsilon = 9.20 * cosDegrees(omega) + 0.57 * cosDegrees(2 * lSun)
				+ 0.10 * cosDegrees(2 * lMoon) - 0.09 * cosDegrees(2 * omega);
		return new double[] { deltaPsi, deltaEpsilon };
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Equation_of_time">equation of time</a> - apparent minus
	 * mean solar time - in minutes of time, following Meeus chapter 28 (E = L<sub>0</sub> - 0.0057183°
	 * - α + Δψ·cos ε). The sign convention matches {@link NOAACalculator} so the
	 * rise/set formulas are interchangeable.
	 * @param julianCenturies Terrestrial-Time centuries since J2000.0.
	 * @return the equation of time in minutes of time.
	 */
	private static double getEquationOfTime(double julianCenturies) {
		double tau = julianCenturies / 10.0;
		// Mean longitude of the sun (Meeus 28.1), in degrees.
		double l0 = 280.4664567 + tau * (360007.6982779 + tau * (0.03032028
				+ tau * (1.0 / 49931.0 - tau * (1.0 / 15300.0 + tau / 2000000.0))));
		l0 = (l0 % 360 + 360) % 360;
		double alpha = getSunRightAscension(julianCenturies);
		double[] nut = getNutation(julianCenturies);
		double epsilon = getTrueObliquity(julianCenturies);
		double e = l0 - 0.0057183 - alpha + (nut[0] / 3600.0) * cosDegrees(epsilon); // degrees
		// Reduce to (-180, 180] so the *4 conversion gives the customary +/- ~16 minute range.
		e = ((e + 180) % 360 + 360) % 360 - 180;
		return e * 4.0; // 1 degree = 4 minutes of time
	}

	/**
	 * Evaluate a VSOP87 series of the form Σ<sub>n</sub> (Σ<sub>i</sub> A<sub>i</sub>·cos(B<sub>i</sub>
	 * + C<sub>i</sub>·τ)) · τ<sup>n</sup>, where the coefficients are stored in 1e-8 of the series'
	 * native unit (radians for longitude/latitude, AU for the radius vector).
	 * @param series the series, indexed as {@code series[n][term][{A, B, C}]}.
	 * @param tau the time in Julian millennia from J2000.0.
	 * @return the evaluated quantity (radians or AU).
	 */
	private static double sumSeries(double[][][] series, double tau) {
		double total = 0.0;
		double tauPow = 1.0;
		for (double[][] terms : series) {
			double sum = 0.0;
			for (double[] term : terms) {
				sum += term[0] * Math.cos(term[1] + term[2] * tau);
			}
			total += sum * tauPow;
			tauPow *= tau;
		}
		return total / 1.0e8;
	}

	/**
	 * Earth heliocentric longitude series L0..L5. Abridged VSOP87 Earth series (Meeus, Astronomical Algorithms 2nd ed., Appendix
	 * III). Each term is {A, B, C}; the series value is Σ A·cos(B + C·τ), τ in Julian millennia, result in 1e-8 rad (L, B) or
	 * 1e-8 AU (R).
	 */
	private static final double[][][] EARTH_L = {
		{ // L0
			{175347046, 0, 0}, {3341656, 4.6692568, 6283.07585}, {34894, 4.6261, 12566.1517},
			{3497, 2.7441, 5753.3849}, {3418, 2.8289, 3.5231}, {3136, 3.6277, 77713.7715},
			{2676, 4.4181, 7860.4194}, {2343, 6.1352, 3930.2097}, {1324, 0.7425, 11506.7698},
			{1273, 2.0371, 529.6910}, {1199, 1.1096, 1577.3435}, {990, 5.233, 5884.927},
			{902, 2.045, 26.298}, {857, 3.508, 398.149}, {780, 1.179, 5223.694},
			{753, 2.533, 5507.553}, {505, 4.583, 18849.228}, {492, 4.205, 775.523},
			{357, 2.920, 0.067}, {317, 5.849, 11790.629}, {284, 1.899, 796.298},
			{271, 0.315, 10977.079}, {243, 0.345, 5486.778}, {206, 4.806, 2544.314},
			{205, 1.869, 5573.143}, {202, 2.458, 6069.777}, {156, 0.833, 213.299},
			{132, 3.411, 2942.463}, {126, 1.083, 20.775}, {115, 0.645, 0.980},
			{103, 0.636, 4694.003}, {102, 0.976, 15720.839}, {102, 4.267, 7.114},
			{99, 6.21, 2146.17}, {98, 0.68, 155.42}, {86, 5.98, 161000.69},
			{85, 1.30, 6275.96}, {85, 3.67, 71430.70}, {80, 1.81, 17260.15},
			{79, 3.04, 12036.46}, {75, 1.76, 5088.63}, {74, 3.50, 3154.69},
			{74, 4.68, 801.82}, {70, 0.83, 9437.76}, {62, 3.98, 8827.39},
			{61, 1.82, 7084.90}, {57, 2.78, 6286.60}, {56, 4.39, 14143.50},
			{56, 3.47, 6279.55}, {52, 0.19, 12139.55}, {52, 1.33, 1748.02},
			{51, 0.28, 5856.48}, {49, 0.49, 1194.45}, {41, 5.37, 8429.24},
			{41, 2.40, 19651.05}, {39, 6.17, 10447.39}, {37, 6.04, 10213.29},
			{37, 2.57, 1059.38}, {36, 1.71, 2352.87}, {36, 1.78, 6812.77},
			{33, 0.59, 17789.85}, {30, 0.44, 83996.85}, {30, 2.74, 1349.87},
			{25, 3.16, 4690.48}
		},
		{ // L1
			{628331966747.0, 0, 0}, {206059, 2.678235, 6283.07585}, {4303, 2.6351, 12566.1517},
			{425, 1.590, 3.523}, {119, 5.796, 26.298}, {109, 2.966, 1577.344},
			{93, 2.59, 18849.23}, {72, 1.14, 529.69}, {68, 1.87, 398.15},
			{67, 4.41, 5507.55}, {59, 2.89, 5223.69}, {56, 2.17, 155.42},
			{45, 0.40, 796.30}, {36, 0.47, 775.52}, {29, 2.65, 7.11},
			{21, 5.34, 0.98}, {19, 1.85, 5486.78}, {19, 4.97, 213.30},
			{17, 2.99, 6275.96}, {16, 0.03, 2544.31}, {16, 1.43, 2146.17},
			{15, 1.21, 10977.08}, {12, 2.83, 1748.02}, {12, 3.26, 5088.63},
			{12, 5.27, 1194.45}, {12, 2.08, 4694.00}, {11, 0.77, 553.57},
			{10, 1.30, 6286.60}, {10, 4.24, 1349.87}, {9, 2.70, 242.73},
			{9, 5.64, 951.72}, {8, 5.30, 2352.87}, {6, 2.65, 9437.76},
			{6, 4.67, 4690.48}
		},
		{ // L2
			{52919, 0, 0}, {8720, 1.0721, 6283.0758}, {309, 0.867, 12566.152},
			{27, 0.05, 3.52}, {16, 5.19, 26.30}, {16, 3.68, 155.42},
			{10, 0.76, 18849.23}, {9, 2.06, 77713.77}, {7, 0.83, 775.52},
			{5, 4.66, 1577.34}, {4, 1.03, 7.11}, {4, 3.44, 5573.14},
			{3, 5.14, 796.30}, {3, 6.05, 5507.55}, {3, 1.19, 242.73},
			{3, 6.12, 529.69}, {3, 0.31, 398.15}, {3, 2.28, 553.57},
			{2, 4.38, 5223.69}, {2, 3.75, 0.98}
		},
		{ // L3
			{289, 5.844, 6283.076}, {35, 0, 0}, {17, 5.49, 12566.15},
			{3, 5.20, 155.42}, {1, 4.72, 3.52}, {1, 5.30, 18849.23}, {1, 5.97, 242.73}
		},
		{ // L4
			{114, 3.142, 0}, {8, 4.13, 6283.08}, {1, 3.84, 12566.15}
		},
		{ // L5
			{1, 3.14, 0}
		}
	};

	/** Earth heliocentric latitude series B0..B1. */
	private static final double[][][] EARTH_B = {
		{ // B0
			{280, 3.199, 84334.662}, {102, 5.422, 5507.553}, {80, 3.88, 5223.69},
			{44, 3.70, 2352.87}, {32, 4.00, 1577.34}
		},
		{ // B1
			{9, 3.90, 5507.55}, {6, 1.73, 5223.69}
		}
	};

	/** Earth radius-vector series R0..R4 (AU). */
	private static final double[][][] EARTH_R = {
		{ // R0
			{100013989, 0, 0}, {1670700, 3.0984635, 6283.07585}, {13956, 3.05525, 12566.1517},
			{3084, 5.1985, 77713.7715}, {1628, 1.1739, 5753.3849}, {1576, 2.8469, 7860.4194},
			{925, 5.453, 11506.770}, {542, 4.564, 3930.210}, {472, 3.661, 5884.927},
			{346, 0.964, 5507.553}, {329, 5.900, 5223.694}, {307, 0.299, 5573.143},
			{243, 4.273, 11790.629}, {212, 5.847, 1577.344}, {186, 5.022, 10977.079},
			{175, 3.012, 18849.228}, {110, 5.055, 5486.778}, {98, 0.89, 6069.78},
			{86, 5.69, 15720.84}, {86, 1.27, 161000.69}, {65, 0.27, 17260.15},
			{63, 0.92, 529.69}, {57, 2.01, 83996.85}, {56, 5.24, 71430.70},
			{49, 3.25, 2544.31}, {47, 2.58, 775.52}, {45, 5.54, 9437.76},
			{43, 6.01, 6275.96}, {39, 5.36, 4694.00}, {38, 2.39, 8827.39},
			{37, 0.83, 19651.05}, {37, 4.90, 12139.55}, {36, 1.67, 12036.46},
			{35, 1.84, 2942.46}, {33, 0.24, 7084.90}, {32, 0.18, 5088.63},
			{32, 1.78, 398.15}, {28, 1.21, 6286.60}, {28, 1.90, 6279.55},
			{26, 4.59, 10447.39}
		},
		{ // R1
			{103019, 1.10749, 6283.07585}, {1721, 1.0644, 12566.1517}, {702, 3.142, 0},
			{32, 1.02, 18849.23}, {31, 2.84, 5507.55}, {25, 1.32, 5223.69},
			{18, 1.42, 1577.34}, {10, 5.91, 10977.08}, {9, 1.42, 6275.96}, {9, 0.27, 5486.78}
		},
		{ // R2
			{4359, 5.7846, 6283.0758}, {124, 5.579, 12566.152}, {12, 3.14, 0},
			{9, 3.63, 77713.77}, {6, 1.87, 5573.14}, {3, 5.47, 18849.23}
		},
		{ // R3
			{145, 4.273, 6283.076}, {7, 3.92, 12566.15}
		},
		{ // R4
			{4, 2.56, 6283.08}
		}
	};
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Two {@code MeeusCalculator} instances are considered equal if their {@link #isApplyDeltaT()} value is identical.
	 * 
	 * @param object the reference object with which to compare
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (!super.equals(object)) {
				return false;
		}
		return this.applyDeltaT == ((MeeusCalculator) object).applyDeltaT;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation hashes the {@code applyDeltaT} property to maintain the contract with {@link #equals(Object)}.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), applyDeltaT);
	}
}
