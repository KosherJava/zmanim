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
 * Implementation of solar times using the <a href="https://midcdmz.nrel.gov/spa/">Solar Position Algorithm (SPA)</a>. This was the
 * work of Ibrahim Reda and Afshin Andreas (NREL/TP-560-34302, <a href="https://doi.org/10.1016/j.solener.2003.12.003">Solar Energy
 * 76 (2004) 577-589</a>). SPA computes the Earth's heliocentric position from the <a href=
 * "https://en.wikipedia.org/wiki/VSOP_(planets)">VSOP87</a> series, applies nutation, aberration and the obliquity of the ecliptic
 * to obtain the Sun's apparent geocentric right ascension and declination, then performs the full <b>topocentric</b> reduction -
 * observer parallax (using latitude, longitude and elevation) and atmospheric refraction - to yield the observed topocentric zenith
 * and azimuth. Its stated uncertainty is ±0.0003° (about one arc-second) for the years −2000 to +6000.
 *
 * <p><b>Refraction is fully configurable through the parent {@link AstronomicalCalculator}.</b> Many SPA ports hardcode their
 * refraction model (and even their solar radius); this one does not:
 * <ul>
 * <li><b>Sunrise / sunset (the geometric 90° zenith only).</b> The horizon geometry is taken entirely from the parent's {@link
 *     #adjustZenith(double, double, double, LocalDate)}: {@link #getRefraction()} / {@link #setRefraction(double)} (default 0.5667° / 34′);
 *     the solar radius - the date-based {@link #getApparentSolarRadius(LocalDate)} by default (it varies slightly day to day), or the
 *     fixed {@link #getSolarRadius()} (default 0.26667° / 16′) when {@link #isUseApparentSolarRadius()} is {@code false};
 *     and the {@link #getElevationAdjustment(double, double) elevation dip} (WGS84 latitude-dependent geocentric radius with atmospheric
 *     refraction correction). SPA's own atmospheric refraction is <em>not</em> applied on this
 *     path - the calculator solves for the geometric (parallax-included, un-refracted) center of the Sun reaching the parent-supplied
 *     zenith, exactly as {@link NOAACalculator} and {@link MeeusCalculator} do. The parent only adds these corrections when the
 *     zenith is exactly 90°, so {@link #setRefraction(double)} and {@link #setSolarRadius(double)} change sunrise / sunset (and any
 *     90°-based time) and nothing else.</li>
 * <li><b>Twilight and depression-angle <em>zmanim</em> (e.g. <em>alos</em> / <em>tzais</em> at 16.1°, 19.8°).</b> These pass a zenith
 *     other than 90°, so {@link #adjustZenith(double, double, double, LocalDate)} adds nothing: no refraction, no solar radius. The time is
 *     solved for the Sun's <em>true geometric</em> depression, as such <em>zmanim</em> are defined. None of {@link
 *     #setRefraction(double)}, {@link #setSolarRadius(double)}, {@link #setPressure(double)} or {@link #setTemperature(double)}
 *     affect them.</li>
 * <li><b>Instantaneous {@link #getSolarElevation(Instant, GeoLocation) elevation} / {@link #getSolarAzimuth(Instant, GeoLocation)
 *     azimuth} only.</b> Here the full SPA (Reda-Andreas / Sæmundsson) atmospheric-refraction model is used, with its horizon
 *     parameter taken from the parent's {@link #getRefraction()} and the {@link #setPressure(double) pressure} and {@link
 *     #setTemperature(double) temperature} settable. <b>These two settings affect this path and only this path</b> - they have no
 *     effect on sunrise, sunset, twilight or any depression-angle <em>zman</em>. (Note that within the KosherJava calendar nothing
 *     calls  {@link #getSolarElevation(Instant, GeoLocation)} / {@code #getSolarAzimuth(Instant, GeoLocation)}; they are direct-query
 *     methods, so in normal calendar use the pressure and temperature settings are inert.)</li>
 * </ul>
 *
 * <p><b>ΔT</b> (TT − UT) is integral to SPA and is applied here. It defaults to an Espenak-Meeus estimate but can be overridden
 * exactly via {@link #setDeltaTOverride(Double)} (the published SPA test case fixes ΔT = 67 seconds), or disabled via {@link
 * #setApplyDeltaT(boolean)}.
 *
 * <p><b>Topocentric note.</b> Unlike {@link NOAACalculator#getSolarElevation(Instant, GeoLocation)} (sea-level, altitude-independent),
 * this calculator's elevation / azimuth are genuinely topocentric: the observer's {@link GeoLocation#getElevation() elevation} feeds
 * the parallax term. The {@code adjustForElevation} flag on sunrise / sunset continues to control only the geometric horizon
 * <em>dip</em> via the parent.
 *
 * <p><b>Coefficient coverage.</b> The VSOP87 Earth tables are the abridged Appendix III set (validated to about 0.1′ against Meeus's
 * worked example), and the nutation uses the principal terms. These give a few arc-seconds of accuracy - vastly beyond any
 * <em>zmanim</em> requirements. For the full published SPA ±0.0003° envelope, the VSOP87 tables and the IAU 1980 nutation series can
 * be extended without changing the algorithm.
 *
 * @author © Eliyahu Hershfeld 2026; NREL SPA (Reda and Andreas) port
 */
public class SPACalculator extends AstronomicalCalculator {

	/** The <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> of J2000.0. */
	private static final double JULIAN_DAY_JAN_1_2000 = 2451545.0;

	/** Julian days per century. */
	private static final double JULIAN_DAYS_PER_CENTURY = 36525.0;

	/** Whether to apply the ΔT (TT−UT) correction. Defaults to {@code true}. */
	private boolean applyDeltaT = true;

	/** An explicit ΔT (seconds) override; {@code null} means estimate it from the date. */
	private Double deltaTOverride = null;

	/** Annual average local pressure in millibars, used by the SPA refraction model. Default 1013.25. */
	private double pressure = 1013.25;

	/** Annual average local temperature in °C, used by the SPA refraction model. Default 10. */
	private double temperature = 10.0;

	/**
	 * An {@code enum} to indicate what type of solar event ({@link #SUNRISE SUNRISE}, {@link #SUNSET SUNSET},
	 * {@link #NOON NOON} or {@link #MIDNIGHT MIDNIGHT}) is being calculated.
	 */
	protected enum SolarEvent {
		/**SUNRISE A solar event related to sunrise*/SUNRISE, /**SUNSET A solar event related to sunset*/SUNSET,
		/**NOON A solar event related to noon*/NOON, /**MIDNIGHT A solar event related to midnight*/MIDNIGHT
	}

	/** Default constructor of the SPACalculator. */
	public SPACalculator() {
		super();
	}

	@Override
	public String getCalculatorName() {
		return "NREL Solar Position Algorithm";
	}

	/**
	 * Sets to apply ΔT {@code true} (default), or {@code false} to omit it (UT = TT).
	 * @param applyDeltaT {@code true} (default) to apply ΔT, {@code false} to omit it (UT = TT).
	 * @see #isApplyDeltaT()
	 */
	public void setApplyDeltaT(boolean applyDeltaT) {
		this.applyDeltaT = applyDeltaT;
	}

	/**
	 * Returns whether the ΔT correction is being applied. @see #setApplyDeltaT(boolean).
	 * @return whether the ΔT correction is being applied. @see #setApplyDeltaT(boolean)
	 * */
	public boolean isApplyDeltaT() {
		return this.applyDeltaT;
	}
	
	/**
	 * Override the estimated ΔT with an explicit value in seconds (for example 67 seconds for the published SPA test
	 * case), or {@code null} to revert to the date-based estimate.
	 * @param deltaTSeconds ΔT in seconds, or {@code null} to estimate.
	 */
	public void setDeltaTOverride(Double deltaTSeconds) {
		this.deltaTOverride = deltaTSeconds;
	}

	/**
	 * Return the explicit ΔT (TT - UT) override in seconds, or {@code null} if ΔT is being estimated from the
	 * date (the default). Note that this returns the override setting, not the estimated value in use.
	 * @return the explicit ΔT override in seconds, or {@code null} if it is being estimated from the date.
	 * @see #setDeltaTOverride(Double)
	 */
	public Double getDeltaTOverride() {
		return this.deltaTOverride;
	}

	/**
	 * Set the annual average local pressure used by the SPA atmospheric-refraction model. <b>This affects only
	 * {@link #getSolarElevation(Instant, GeoLocation)} and {@link #getSolarAzimuth(Instant, GeoLocation)}</b>; it has no
	 * effect on sunrise, sunset, twilight or any depression-angle <em>zman</em>, whose refraction (if any) comes from the
	 * parent's fixed {@link #getRefraction()}. See the class documentation.
	 * @param pressureMillibars the pressure in millibars (hPa).
	 */
	public void setPressure(double pressureMillibars) {
		this.pressure = pressureMillibars;
	}

	/**
	 * Return the pressure in millibars used by the refraction model.
	 * @return the pressure in millibars used by the refraction model.
	 * */
	public double getPressure() {
		return this.pressure;
	}

	/**
	 * Set the annual average local temperature used by the SPA atmospheric-refraction model. <b>This affects only
	 * {@link #getSolarElevation(Instant, GeoLocation)} and {@link #getSolarAzimuth(Instant, GeoLocation)}</b>; it has no
	 * effect on sunrise, sunset, twilight or any depression-angle <em>zman</em>, whose refraction (if any) comes from the
	 * parent's fixed {@link #getRefraction()}. See the class documentation.
	 * @param temperatureCelsius the temperature in degrees Celsius.
	 */
	public void setTemperature(double temperatureCelsius) {
		this.temperature = temperatureCelsius;
	}

	/**
	 * Returns the temperature in °C used by the refraction model.
	 * @return the temperature in °C used by the refraction model.
	 * */
	public double getTemperature() {
		return this.temperature;
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
	 * Calculate UTC sunrise or sunset (or any twilight angle) in 24-hour format. The horizon geometry (refraction, the sun's radius
	 * and the elevation dip) is supplied entirely by the parent {@link #adjustZenith(double, double, double, LocalDate)}, so the SPA
	 * topocentric solution is solved for the un-refracted, parallax-corrected center of the Sun reaching that zenith.
	 * @param localDate the date.
	 * @param geoLocation the observer location.
	 * @param zenith the (un-adjusted) zenith; {@link #adjustZenith adjusted} by the parent before use.
	 * @param adjustForElevation whether to include the geometric elevation dip.
	 * @param solarEvent {@link SolarEvent#SUNRISE} or {@link SolarEvent#SUNSET}.
	 * @return the UTC time in hours, or {@link Double#NaN} if the event does not occur (polar day / night).
	 */
	private double getUTCSunRiseSet(LocalDate localDate, GeoLocation geoLocation, double zenith,
			boolean adjustForElevation, SolarEvent solarEvent) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation, geoLocation.getLatitude(), localDate);
		double riseSet = solveRiseSet(localDate, geoLocation, adjustedZenith, solarEvent);
		if (Double.isNaN(riseSet)) {
			return Double.NaN;
		}
		riseSet = riseSet / 60;
		return (riseSet % 24 + 24) % 24;
	}

	/**
	 * Solve for the UTC time, in minutes after 0:00, at which the topocentric (parallax-corrected, un-refracted) center of
	 * the Sun reaches {@code adjustedZenith}. A first guess is taken from the closed-form hour-angle equation (geocentric
	 * declination at local noon), then refined by a secant iteration on the full SPA topocentric zenith.
	 * @param localDate the date.
	 * @param geoLocation the observer location.
	 * @param adjustedZenith the target zenith (already including refraction / radius / dip from the parent).
	 * @param solarEvent {@link SolarEvent#SUNRISE} or {@link SolarEvent#SUNSET}.
	 * @return UTC minutes after 0:00, or {@link Double#NaN} if the event does not occur.
	 */
	private double solveRiseSet(LocalDate localDate, GeoLocation geoLocation, double adjustedZenith, SolarEvent solarEvent) {
		double jdDay = getJulianDay(localDate);
		double lonWest = -geoLocation.getLongitude();
		double lat = geoLocation.getLatitude();
		double elevation = geoLocation.getElevation();

		// Closed-form first guess using the geocentric declination at local apparent noon.
		double noonMin = solveNoonMidnight(jdDay, lonWest, SolarEvent.NOON);
		double[] noonCoords = solarCoords(jdDay + noonMin / 1440.0);
		double declNoon = noonCoords[1];
		double eot = equationOfTime(jdDay + noonMin / 1440.0);
		double cosH0 = (cosDegrees(adjustedZenith) - sinDegrees(lat) * sinDegrees(declNoon))
				/ (cosDegrees(lat) * cosDegrees(declNoon));
		if (cosH0 < -1.0 || cosH0 > 1.0) {
			return Double.NaN; // sun never reaches this zenith (polar day / night)
		}
		double h0 = acosDegrees(cosH0); // degrees, positive
		double signed = (solarEvent == SolarEvent.SUNRISE) ? h0 : -h0;
		double guess = 720 + 4 * (lonWest - signed) - eot; // minutes

		// Secant refinement on f(t) = topocentricTrueZenith(t) - adjustedZenith.
		double t0 = guess;
		double t1 = guess + 0.5;
		double f0 = topocentricTrueZenith(jdDay + t0 / 1440.0, lat, geoLocation.getLongitude(), elevation) - adjustedZenith;
		double f1 = topocentricTrueZenith(jdDay + t1 / 1440.0, lat, geoLocation.getLongitude(), elevation) - adjustedZenith;
		for (int i = 0; i < 12 && Math.abs(f1) > 1e-9; i++) {
			double denom = (f1 - f0);
			if (denom == 0) {
				break;
			}
			double t2 = t1 - f1 * (t1 - t0) / denom;
			t0 = t1; f0 = f1;
			t1 = t2;
			f1 = topocentricTrueZenith(jdDay + t1 / 1440.0, lat, geoLocation.getLongitude(), elevation) - adjustedZenith;
		}
		return t1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getUTCNoon(LocalDate localDate, GeoLocation geoLocation) {
		double noon = solveNoonMidnight(getJulianDay(localDate), -geoLocation.getLongitude(), SolarEvent.NOON) / 60;
		return (noon % 24 + 24) % 24;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getUTCMidnight(LocalDate localDate, GeoLocation geoLocation) {
		double midnight = solveNoonMidnight(getJulianDay(localDate), -geoLocation.getLongitude(), SolarEvent.MIDNIGHT) / 60;
		return (midnight % 24 + 24) % 24;
	}

	/**
	 * Solar transit (noon) or anti-transit (midnight) in UTC minutes after 0:00, using the high-accuracy SPA equation of
	 * time. Parallax has a negligible (sub-second) effect on transit time, so the geocentric equation of time is used.
	 * @param julianDay the (UT) Julian day for 0:00.
	 * @param lonWest the observer longitude in degrees, positive west.
	 * @param solarEvent {@link SolarEvent#NOON} or {@link SolarEvent#MIDNIGHT}.
	 * @return UTC minutes after 0:00.
	 */
	private double solveNoonMidnight(double julianDay, double lonWest, SolarEvent solarEvent) {
		// Rigorous meridian solve: find the UT at which the Sun's apparent hour angle equals 0 (transit / noon)
		// or 180 (anti-transit / midnight). This matches the definition used by reference ephemerides (and the
		// NREL SPA Appendix A.2 transit procedure); the previous equation-of-time formulation differed from the
		// true meridian crossing by a near-constant ~0.2 s.
		double lonEast = -lonWest;
		double targetHourAngle = (solarEvent == SolarEvent.NOON) ? 0.0 : 180.0;
		double dayFraction = ((solarEvent == SolarEvent.NOON) ? 0.5 : 1.0) - lonEast / 360.0;
		for (int i = 0; i < 3; i++) {
			double[] sc = solarCoords(julianDay + dayFraction);
			double alpha = sc[0]; // apparent right ascension (degrees)
			double nu = sc[3];    // apparent sidereal time at Greenwich (degrees)
			double hourAngle = ((nu + lonEast - alpha - targetHourAngle) % 360 + 540) % 360 - 180; // [-180, 180)
			dayFraction -= hourAngle / 360.0; // the Sun's hour angle advances 360 deg per solar day
		}
		return dayFraction * 1440.0; // UTC minutes after 0:00
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
		for (int i = 0; i < 4; i++) {
			double jd = julianDay + dateTime;
			double decl = solarCoords(jd)[1];
			double ratio = tanDegrees(decl) / tanDegrees(geoLocation.getLatitude());
			if (Double.isNaN(ratio) || ratio > 1.0 || ratio < -1.0) {
				return Double.NaN;
			}
			double offset = ((targetAzimuth == 90.0) ? -1.0 : 1.0) * (acosDegrees(ratio) / 360.0);
			dateTime = solarNoonBase + offset - (equationOfTime(jd) / 1440.0);
		}
		double timeUTC = dateTime * 24.0;
		return (timeUTC % 24 + 24) % 24;
	}

	@Override
	public double getSolarElevation(Instant instant, GeoLocation geoLocation) {
		double jd = julianDayFromInstant(instant);
		double[] topo = topocentric(jd, geoLocation.getLatitude(), geoLocation.getLongitude(), geoLocation.getElevation());
		return topo[1]; // observed (refraction-corrected) topocentric elevation
	}

	@Override
	public double getSolarAzimuth(Instant instant, GeoLocation geoLocation) {
		double jd = julianDayFromInstant(instant);
		double[] topo = topocentric(jd, geoLocation.getLatitude(), geoLocation.getLongitude(), geoLocation.getElevation());
		return topo[2]; // topocentric azimuth, eastward from north
	}

	/**
	 * Compute the Sun's <b>geocentric</b> apparent coordinates and the apparent sidereal time for the given UT Julian day.
	 * @param julianDayUT the UT Julian day (including fractional day).
	 * @return {alpha (geocentric apparent RA, deg 0-360), delta (geocentric apparent declination, deg), epsilon (true
	 *         obliquity, deg), nu (apparent Greenwich sidereal time, deg 0-360), radius (AU), lambda (apparent longitude, deg)}.
	 */
	private double[] solarCoords(double julianDayUT) {
		double deltaTSeconds = applyDeltaT ? (deltaTOverride != null ? deltaTOverride : estimateDeltaT(julianDayUT)) : 0.0;
		double jde = julianDayUT + deltaTSeconds / 86400.0;
		double jce = (jde - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
		double jc = (julianDayUT - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
		double jme = jce / 10.0;

		double earthL = Math.toDegrees(sumSeries(EARTH_L, jme));
		double earthB = Math.toDegrees(sumSeries(EARTH_B, jme));
		double radius = sumSeries(EARTH_R, jme); // AU

		double theta = (earthL + 180.0) % 360.0;   // geocentric longitude
		double beta = -earthB;                      // geocentric latitude

		double[] nut = nutation(jce);               // {Δψ, Δε} in degrees
		double epsilon = meanObliquity(jme) + nut[1];
		double aberration = -20.4898 / (3600.0 * radius); // degrees
		double lambda = theta + nut[0] + aberration;      // apparent longitude

		// apparent sidereal time at Greenwich
		double nu0 = 280.46061837 + 360.98564736629 * (julianDayUT - JULIAN_DAY_JAN_1_2000)
				+ 0.000387933 * jc * jc - jc * jc * jc / 38710000.0;
		nu0 = (nu0 % 360 + 360) % 360;
		double nu = nu0 + nut[0] * cosDegrees(epsilon);

		double alpha = Math.toDegrees(Math.atan2(
				sinDegrees(lambda) * cosDegrees(epsilon) - tanDegrees(beta) * sinDegrees(epsilon),
				cosDegrees(lambda)));
		alpha = (alpha % 360 + 360) % 360;
		double delta = asinDegrees(sinDegrees(beta) * cosDegrees(epsilon)
				+ cosDegrees(beta) * sinDegrees(epsilon) * sinDegrees(lambda));

		return new double[] { alpha, delta, epsilon, nu, radius, lambda };
	}

	/**
	 * Full SPA topocentric reduction (observer parallax + atmospheric refraction).
	 * @param julianDayUT the UT Julian day.
	 * @param latitude observer latitude in degrees.
	 * @param longitude observer longitude in degrees, positive east.
	 * @param elevationMeters observer elevation in meters.
	 * @return {trueElevation (topocentric, no refraction, deg), observedElevation (refraction-corrected, deg),
	 *         azimuth (eastward from north, deg 0-360)}.
	 */
	private double[] topocentric(double julianDayUT, double latitude, double longitude, double elevationMeters) {
		double[] sc = solarCoords(julianDayUT);
		double alpha = sc[0], delta = sc[1], nu = sc[3], radius = sc[4];

		double h = (nu + longitude - alpha) % 360.0; // observer local hour angle (deg), positive west
		h = (h + 360) % 360;

		// equatorial horizontal parallax of the sun
		double xi = 8.794 / (3600.0 * radius); // degrees
		double u = Math.atan(0.99664719 * tanDegrees(latitude));
		double x = Math.cos(u) + (elevationMeters / 6378140.0) * cosDegrees(latitude);
		double y = 0.99664719 * Math.sin(u) + (elevationMeters / 6378140.0) * sinDegrees(latitude);

		double deltaAlpha = Math.toDegrees(Math.atan2(-x * sinDegrees(xi) * sinDegrees(h),
				cosDegrees(delta) - x * sinDegrees(xi) * cosDegrees(h)));
		double deltaPrime = Math.toDegrees(Math.atan2(
				(sinDegrees(delta) - y * sinDegrees(xi)) * cosDegrees(deltaAlpha),
				cosDegrees(delta) - x * sinDegrees(xi) * cosDegrees(h))); // topocentric declination
		double hPrime = h - deltaAlpha; // topocentric local hour angle

		double e0 = asinDegrees(sinDegrees(latitude) * sinDegrees(deltaPrime)
				+ cosDegrees(latitude) * cosDegrees(deltaPrime) * cosDegrees(hPrime)); // true (no refraction)

		double deltaE = refractionCorrection(e0); // degrees
		double e = e0 + deltaE;                    // observed elevation

		double gamma = Math.toDegrees(Math.atan2(sinDegrees(hPrime),
				cosDegrees(hPrime) * sinDegrees(latitude) - tanDegrees(deltaPrime) * cosDegrees(latitude)));
		double azimuth = (gamma + 180.0) % 360.0; // eastward from north
		azimuth = (azimuth + 360) % 360;

		return new double[] { e0, e, azimuth };
	}

	/**
	 * Topocentric <em>true</em> (un-refracted) zenith of the Sun's center, used by the rise/set solver so that refraction
	 * is supplied solely by the parent {@link #adjustZenith adjustZenith}.
	 * @param julianDayUT the UT Julian day.
	 * @param latitude observer latitude in degrees.
	 * @param longitude observer longitude in degrees, positive east.
	 * @param elevationMeters observer elevation in meters.
	 * @return the topocentric true zenith in degrees.
	 */
	private double topocentricTrueZenith(double julianDayUT, double latitude, double longitude, double elevationMeters) {
		return 90.0 - topocentric(julianDayUT, latitude, longitude, elevationMeters)[0];
	}

	/**
	 * The SPA atmospheric-refraction correction (Reda and Andreas) in degrees. The horizon refraction parameter is the parent's
	 * {@link #getRefraction()}, and the {@link #pressure} / {@link #temperature} are settable, so the model is fully configurable
	 * rather than hardcoded.
	 * @param trueElevation the true (un-refracted) topocentric elevation in degrees.
	 * @return the refraction correction in degrees to add to the true elevation.
	 */
	private double refractionCorrection(double trueElevation) {
		double atmosRefract = getRefraction(); // parent's configurable horizon refraction (degrees), default 34'
		// Fixed solar-radius constant (~16'), used only as a coarse below-horizon cutoff for refraction —
		// deliberately NOT the date-based apparent radius, since the seasonal ~16" variation is irrelevant to this gate.
		double sunRadius = getSolarRadius();
		if (trueElevation < -1.0 * (sunRadius + atmosRefract)) {
			return 0.0; // sun fully below the refracted horizon
		}
		return (pressure / 1010.0) * (283.0 / (273.0 + temperature))
				* 1.02 / (60.0 * tanDegrees(trueElevation + 10.3 / (trueElevation + 5.11)));
	}

	/**
	 * The <a href="https://en.wikipedia.org/wiki/Equation_of_time">equation of time</a> (apparent minus mean solar time)
	 * in minutes of time (Meeus chapter 28), built from the SPA geocentric apparent right ascension. The sign matches
	 * {@link NOAACalculator}.
	 * @param julianDayUT the UT Julian day.
	 * @return the equation of time in minutes.
	 */
	private double equationOfTime(double julianDayUT) {
		double[] sc = solarCoords(julianDayUT);
		double alpha = sc[0], epsilon = sc[2];
		double deltaTSeconds = applyDeltaT ? (deltaTOverride != null ? deltaTOverride : estimateDeltaT(julianDayUT)) : 0.0;
		double jme = ((julianDayUT + deltaTSeconds / 86400.0) - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY / 10.0;
		double l0 = 280.4664567 + jme * (360007.6982779 + jme * (0.03032028
				+ jme * (1.0 / 49931.0 - jme * (1.0 / 15300.0 + jme / 2000000.0))));
		l0 = (l0 % 360 + 360) % 360;
		double deltaPsi = nutation(((julianDayUT + deltaTSeconds / 86400.0) - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY)[0];
		double e = l0 - 0.0057183 - alpha + deltaPsi * cosDegrees(epsilon); // degrees
		e = ((e + 180) % 360 + 360) % 360 - 180;
		return e * 4.0;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> from a Java {@code LocalDate}.
	 * Julian day for 0h UT of the date (Meeus chapter 7).
	 * 
	 * @param localDate the {@code LocalDate} to get the Julian date for.
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
	 * Julian day (including fractional day) for an {@code Instant} in UTC.
	 * @param instant the day to calculate the Julian day for.
	 * @return the Julian day as a {@code double}
	 */
	private static double julianDayFromInstant(Instant instant) {
		ZonedDateTime utc = instant.atZone(ZoneOffset.UTC);
		double fractionalDay = (utc.getHour() + (utc.getMinute()
				+ (utc.getSecond() + utc.getNano() / 1_000_000_000.0) / 60.0) / 60.0) / 24.0;
		return getJulianDay(utc.toLocalDate()) + fractionalDay;
	}

	/**
	 * The mean obliquity of the ecliptic in degrees (SPA / Meeus 22.2 polynomial in U = JME/10).
	 * @param jme Julian millennia of ephemeris time since J2000.0.
	 * @return mean obliquity in degrees.
	 */
	private static double meanObliquity(double jme) {
		double u = jme / 10.0;
		double seconds = 84381.448 - 4680.93 * u - 1.55 * u * u + 1999.25 * Math.pow(u, 3) - 51.38 * Math.pow(u, 4)
				- 249.67 * Math.pow(u, 5) - 39.05 * Math.pow(u, 6) + 7.12 * Math.pow(u, 7) + 27.87 * Math.pow(u, 8)
				+ 5.79 * Math.pow(u, 9) + 2.45 * Math.pow(u, 10);
		return seconds / 3600.0;
	}

	/**
	 * Principal nutation terms in longitude (Δψ) and obliquity (Δε), returned in <b>degrees</b>
	 * (Meeus chapter 22, abridged). These dominant terms give sub-arc-second nutation; the full IAU 1980 series can be
	 * substituted for the complete SPA precision.
	 * @param jce Julian centuries of ephemeris time since J2000.0.
	 * @return {Δψ, Δε} in degrees.
	 */
	private static double[] nutation(double jce) {
		double t = jce;
		double omega = 125.04452 - 1934.136261 * t + 0.0020708 * t * t + t * t * t / 450000.0;
		double lSun = 280.4665 + 36000.7698 * t;
		double lMoon = 218.3165 + 481267.8813 * t;
		double deltaPsiArcsec = -17.20 * sinDegrees(omega) - 1.32 * sinDegrees(2 * lSun)
				- 0.23 * sinDegrees(2 * lMoon) + 0.21 * sinDegrees(2 * omega);
		double deltaEpsArcsec = 9.20 * cosDegrees(omega) + 0.57 * cosDegrees(2 * lSun)
				+ 0.10 * cosDegrees(2 * lMoon) - 0.09 * cosDegrees(2 * omega);
		return new double[] { deltaPsiArcsec / 3600.0, deltaEpsArcsec / 3600.0 };
	}

	/**
	 * Estimate ΔT = TT−UT in seconds (Espenak and Meeus polynomials). Approximate but good to a few seconds for
	 * the modern era.
	 * @param julianDay the (UT) Julian day.
	 * @return ΔT in seconds.
	 */
	private static double estimateDeltaT(double julianDay) {
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
		} else {
			double u = (year - 1820) / 100.0;
			return -20 + 32 * u * u;
		}
	}

	/**
	 * Evaluate a VSOP87 series Σ<sub>n</sub> (Σ<sub>i</sub> A<sub>i</sub>·cos(B<sub>i</sub> +
	 * C<sub>i</sub>·τ)) · τ<sup>n</sup> with coefficients in 1e-8 of the native unit (radians or AU).
	 * @param series indexed {@code [n][term][{A,B,C}]}.
	 * @param tau time in Julian millennia from J2000.0 (JME).
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

	// ---------------------------------------------------------------------------------------------------------------
	// Abridged VSOP87 Earth series (Meeus Appendix III). {A, B, C}; value Σ A·cos(B + C·τ), τ in Julian millennia,
	// result in 1e-8 rad (L, B) or 1e-8 AU (R). Validated to ~0.1" against Meeus's worked example.
	// ---------------------------------------------------------------------------------------------------------------

	/** Earth heliocentric longitude series L0..L5. */
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
	 * Two {@code SPACalculator} instances are considered equal if their {@link #isApplyDeltaT()}, {@link #getDeltaTOverride()},
	 * {@link #getPressure()} and {@link #getTemperature()} values are identical.
	 * 
	 * @param object the reference object with which to compare
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (!super.equals(object)) {
			return false;
		}
		SPACalculator other = (SPACalculator) object; // safe: super.equals() verifies getClass()
		return this.applyDeltaT == other.applyDeltaT
				&& Objects.equals(this.deltaTOverride, other.deltaTOverride)
				&& Double.compare(this.pressure, other.pressure) == 0
				&& Double.compare(this.temperature, other.temperature) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation hashes the {@code applyDeltaT}, {@code deltaTOverride}, {@code pressure}, and {@code temperature}
	 * properties to maintain the contract with {@link #equals(Object)}.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), applyDeltaT, deltaTOverride, pressure, temperature);
	}
}
