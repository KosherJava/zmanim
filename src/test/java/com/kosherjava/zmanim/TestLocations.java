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
package com.kosherjava.zmanim;

import java.time.ZoneId;

import com.kosherjava.zmanim.util.GeoLocation;

/**
 * Shared test fixture holding the reference locations used across the calculator and calendar test suites. The
 * locations span both hemispheres, the equator, the dateline and the Arctic Circle to exercise a range of edge cases.
 * Each location carries its real {@link ZoneId} for the calendar-level tests (the low-level calculator tests operate
 * purely in UTC hours and are unaffected by the zone).
 *
 * @author Test coverage
 */
public final class TestLocations {

	private TestLocations() {
		// utility holder; not instantiable
	}

	/** Woodcliff Lake area, New Jersey. */
	public static final double NJ_LAT = 41.1181036;
	public static final double NJ_LON = -74.0840691;
	public static final double NJ_ELEV = 167;

	/** Los Angeles area, California. */
	public static final double LA_LAT = 34.0201613;
	public static final double LA_LON = -118.6919095;
	public static final double LA_ELEV = 71;

	/** Jerusalem, Israel. */
	public static final double JERUSALEM_LAT = 31.7962994;
	public static final double JERUSALEM_LON = 35.1053185;
	public static final double JERUSALEM_ELEV = 754;

	/** Northern Norway (inside the Arctic Circle - polar day/night edge cases). */
	public static final double NORWAY_LAT = 70.1498248;
	public static final double NORWAY_LON = 9.1456867;
	public static final double NORWAY_ELEV = 0;

	/** Sydney, Australia (southern hemisphere). */
	public static final double SYDNEY_LAT = -33.8688;
	public static final double SYDNEY_LON = 151.2093;
	public static final double SYDNEY_ELEV = 58;

	/** Macapá, Brazil (on the equator). */
	public static final double MACAPA_LAT = 0.0349;
	public static final double MACAPA_LON = -51.0694;
	public static final double MACAPA_ELEV = 15;

	/** Suva, Fiji (near the dateline). */
	public static final double SUVA_LAT = -18.1416;
	public static final double SUVA_LON = 178.4419;
	public static final double SUVA_ELEV = 6;

	/** Ushuaia, Argentina (far southern latitude). */
	public static final double USHUAIA_LAT = -54.8019;
	public static final double USHUAIA_LON = -68.3030;
	public static final double USHUAIA_ELEV = 23;

	/** Lakewood, New Jersey - the library's canonical reference location. */
	public static final double LAKEWOOD_LAT = 40.0721087;
	public static final double LAKEWOOD_LON = -74.2400243;
	public static final double LAKEWOOD_ELEV = 15;

	/**
	 * A {@link ZoneId} of {@code UTC}, used for the low-level calculator tests where the calculation is done entirely in
	 * UTC hours and the zone is irrelevant.
	 */
	public static final ZoneId UTC = ZoneId.of("UTC");

	public static GeoLocation nj(ZoneId zoneId) {
		return new GeoLocation("Woodcliff Lake, NJ", NJ_LAT, NJ_LON, NJ_ELEV, zoneId);
	}

	public static GeoLocation la(ZoneId zoneId) {
		return new GeoLocation("Los Angeles, CA", LA_LAT, LA_LON, LA_ELEV, zoneId);
	}

	public static GeoLocation jerusalem(ZoneId zoneId) {
		return new GeoLocation("Jerusalem, Israel", JERUSALEM_LAT, JERUSALEM_LON, JERUSALEM_ELEV, zoneId);
	}

	public static GeoLocation norway(ZoneId zoneId) {
		return new GeoLocation("Northern Norway", NORWAY_LAT, NORWAY_LON, NORWAY_ELEV, zoneId);
	}

	public static GeoLocation sydney(ZoneId zoneId) {
		return new GeoLocation("Sydney, Australia", SYDNEY_LAT, SYDNEY_LON, SYDNEY_ELEV, zoneId);
	}

	public static GeoLocation macapa(ZoneId zoneId) {
		return new GeoLocation("Macapá, Brazil", MACAPA_LAT, MACAPA_LON, MACAPA_ELEV, zoneId);
	}

	public static GeoLocation suva(ZoneId zoneId) {
		return new GeoLocation("Suva, Fiji", SUVA_LAT, SUVA_LON, SUVA_ELEV, zoneId);
	}

	public static GeoLocation ushuaia(ZoneId zoneId) {
		return new GeoLocation("Ushuaia, Argentina", USHUAIA_LAT, USHUAIA_LON, USHUAIA_ELEV, zoneId);
	}

	public static GeoLocation lakewood() {
		return new GeoLocation("Lakewood, NJ", LAKEWOOD_LAT, LAKEWOOD_LON, LAKEWOOD_ELEV, ZoneId.of("America/New_York"));
	}
}
