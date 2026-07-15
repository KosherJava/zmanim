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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.kosherjava.zmanim.TestLocations;

/**
 * Regression coverage for {@link MeeusCalculator}, the higher-accuracy Jean Meeus (VSOP87) engine with &Delta;T
 * support. Expected values are the library's own current output (defaults, &Delta;T on) asserted to 8 decimals, plus a
 * &Delta;T-disabled variant via {@link MeeusCalculator#setApplyDeltaT(boolean)}.
 *
 * @author Test coverage
 */
public class MeeusCalculatorTest {

	private static final double DELTA = 1e-8;
	private static final double ZENITH = 90.0;
	private static final double NO_EVENT = Double.NaN;

	private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private MeeusCalculator calculator() {
		return new MeeusCalculator();
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

	private Instant instant(String isoDateTime) {
		return LocalDateTime.parse(isoDateTime, DATE_TIME).toInstant(ZoneOffset.UTC);
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
			{"NJ Standard Date", "2017-10-17", "NJ", 11.13565507},
			{"LA Standard Date", "2017-10-17", "LA", 14.00730639},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 4.11849856},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 19.68266925},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 9.38974837},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 18.60510228},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 12.95697826},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 9.44873166},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunset() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 22.24115497},
			{"LA Standard Date", "2017-10-17", "LA", 1.31857880},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 15.64527430},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 8.56381339},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 21.53114700},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 5.65803778},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 20.21223110},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 0.58039250},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunset(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCNoon() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 16.69344845},
			{"LA Standard Date", "2017-10-17", "LA", 19.66689969},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 9.87812628},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 11.42050168},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 2.12793408},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 15.46045983},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 0.13159547},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 16.58453220},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 17.01674900},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCNoon(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCMidnight() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 4.69183111},
			{"LA Standard Date", "2017-10-17", "LA", 7.66529295},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 21.87675832},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 23.42232487},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 14.12636235},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 3.46440271},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 12.13342359},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 4.58635433},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 5.01821298},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCMidnight(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getTimeAtAzimuth() {
		Object[][] cases = {
			{"NJ East", "2017-10-17", "NJ", 90.0, 9.96630242},
			{"NJ West", "2017-10-17", "NJ", 270.0, 23.43696025},
			{"LA East", "2017-10-17", "LA", 90.0, 12.71802243},
			{"LA West", "2017-10-17", "LA", 270.0, 2.63792382},
			{"Sydney East", "2020-02-29", "SYDNEY", 90.0, 20.92601400},
			{"Sydney West", "2020-02-29", "SYDNEY", 270.0, 7.34685529},
			{"Macapa East No Cross", "2000-01-01", "MACAPA", 90.0, NO_EVENT},
			{"Polar East", "2017-06-21", "NORWAY", 90.0, 6.01986159},
		};
		for (Object[] c : cases) {
			double actual = calculator().getTimeAtAzimuth(date((String) c[1]), geo((String) c[2]), (double) c[3]);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getSolarElevation() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 8.24234881},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 33.57213757},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 46.19473579},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 3.98711013},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 2.89445709},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 65.99208585},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 40.24688524},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarElevation(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getSolarAzimuth() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 110.13820751},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 212.62255322},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 187.12661462},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 8.01211355},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 97.32257044},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 164.22488663},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 325.62695543},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarAzimuth(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getUTCSunriseAtZenith() {
		Object[][] cases = {
			{"NJ Civil", "2017-10-17", "NJ", 96.0, 10.70916170},
			{"NJ Nautical", "2017-10-17", "NJ", 102.0, 10.17586674},
			{"NJ Astronomical", "2017-10-17", "NJ", 108.0, 9.64430764},
			{"Sydney Civil", "2020-02-29", "SYDNEY", 96.0, 19.27848095},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), (double) c[3], true);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getUTCSunriseWithoutElevationAdjustment() {
		Object[][] cases = {
			{"NJ No Elev Adj", "2017-10-17", "NJ", 11.17316120},
			{"Jerusalem No Elev Adj", "1955-02-26", "JERUSALEM", 4.18861444},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, false);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunriseWithDeltaTDisabled() {
		Object[][] cases = {
			{"NJ dT off", "2017-10-17", "NJ", 11.13563996},
			{"Jerusalem dT off", "1955-02-26", "JERUSALEM", 4.11850671},
		};
		for (Object[] c : cases) {
			MeeusCalculator calc = calculator();
			calc.setApplyDeltaT(false);
			double actual = calc.getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getTimeAtAzimuthRejectsUnsupportedAzimuth() {
		calculator().getTimeAtAzimuth(date("2017-10-17"), geo("NJ"), 123.0);
	}

	@Test
	public void getCalculatorName() {
		assertEquals("Jean Meeus Higher-Accuracy (VSOP87) Algorithm", calculator().getCalculatorName());
	}
}
