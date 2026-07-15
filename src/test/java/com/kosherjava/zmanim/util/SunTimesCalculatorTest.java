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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;

import org.junit.Test;

import com.kosherjava.zmanim.TestLocations;

/**
 * Regression coverage for {@link SunTimesCalculator} (the older USNO / "Almanac for Computers" algorithm). Expected
 * values are the library's own current output asserted to 8 decimals. Unlike {@link NOAACalculator}, this calculator
 * returns {@link Double#NaN} for solar noon/midnight at the polar location and does not implement solar
 * elevation/azimuth or time-at-azimuth (those throw {@link UnsupportedOperationException}).
 *
 * @author Test coverage
 */
public class SunTimesCalculatorTest {

	private static final double DELTA = 1e-8;
	private static final double ZENITH = 90.0;
	private static final double NO_EVENT = Double.NaN;

	private SunTimesCalculator calculator() {
		return new SunTimesCalculator();
	}

	private GeoLocation geo(String location) {
		switch (location) {
			case "NJ": return TestLocations.nj(TestLocations.UTC);
			case "LA": return TestLocations.la(TestLocations.UTC);
			case "JERUSALEM": return TestLocations.jerusalem(TestLocations.UTC);
			case "NORWAY": return TestLocations.norway(TestLocations.UTC);
			case "SYDNEY": return TestLocations.sydney(TestLocations.UTC);
			case "MACAPA": return TestLocations.macapa(TestLocations.UTC);
			case "SUVA": return TestLocations.suva(TestLocations.UTC);
			case "USHUAIA": return TestLocations.ushuaia(TestLocations.UTC);
			default: throw new IllegalArgumentException("Unknown location: " + location);
		}
	}

	private LocalDate date(String iso) {
		return LocalDate.parse(iso);
	}

	private void assertUtcHour(String label, double expected, double actual) {
		if (Double.isNaN(expected)) {
			assertTrue(label + " should have no event (NaN)", Double.isNaN(actual));
		} else {
			assertEquals(label, expected, actual, DELTA);
		}
	}

	@Test
	public void getUTCSunrise() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 11.12643745},
			{"LA Standard Date", "2017-10-17", "LA", 14.00076197},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 4.11037446},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 19.68724563},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 9.39464043},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 18.60351329},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 12.95527929},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 9.43972880},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunset() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 22.25383390},
			{"LA Standard Date", "2017-10-17", "LA", 1.32889810},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 15.65101844},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 8.56114598},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 21.53593703},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 5.65641338},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 20.20896383},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 0.57971462},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunset(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCNoon() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 16.69011947},
			{"LA Standard Date", "2017-10-17", "LA", 19.66482200},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 9.88071908},
			{"Norway Polar No Noon", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 2.12420286},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 15.46529012},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 0.12996327},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 16.58212152},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 17.00973685},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCNoon(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCMidnight() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 4.69011947},
			{"LA Standard Date", "2017-10-17", "LA", 7.66482200},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 21.88071908},
			{"Norway Polar No Midnight", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 14.12420286},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 3.46529012},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 12.12996327},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 4.58212152},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 5.00973685},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCMidnight(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunriseAtZenith() {
		Object[][] cases = {
			{"NJ Civil", "2017-10-17", "NJ", 96.0, 10.70056986},
			{"NJ Nautical", "2017-10-17", "NJ", 102.0, 10.16784691},
			{"NJ Astronomical", "2017-10-17", "NJ", 108.0, 9.63665090},
			{"Sydney Civil", "2020-02-29", "SYDNEY", 96.0, 19.28346936},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), (double) c[3], true);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getUTCSunriseWithoutElevationAdjustment() {
		Object[][] cases = {
			{"NJ No Elev Adj", "2017-10-17", "NJ", 11.16388056},
			{"Jerusalem No Elev Adj", "1955-02-26", "JERUSALEM", 4.18050398},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, false);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getTimeAtAzimuthUnsupported() {
		calculator().getTimeAtAzimuth(date("2017-10-17"), geo("NJ"), 90.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getSolarElevationUnsupported() {
		calculator().getSolarElevation(Instant.parse("2017-10-17T12:00:00Z"), geo("NJ"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getSolarAzimuthUnsupported() {
		calculator().getSolarAzimuth(Instant.parse("2017-10-17T12:00:00Z"), geo("NJ"));
	}

	@Test
	public void getCalculatorName() {
		assertEquals("US Naval Almanac Algorithm", calculator().getCalculatorName());
	}
}
