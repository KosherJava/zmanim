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
 * Regression coverage for {@link NOAACalculator}, the library's default sunrise/sunset engine. Expected values are the
 * library's own current output asserted to 8 decimals. Cases span both hemispheres, the equator, the dateline, a polar
 * no-event, a leap day, a year boundary and a future &Delta;T date. Each test iterates a labeled case table.
 *
 * @author Test coverage
 */
public class NOAACalculatorTest {

	private static final double DELTA = 1e-8;
	private static final double ZENITH = 90.0; // AstronomicalCalculator.GEOMETRIC_ZENITH
	private static final double NO_EVENT = Double.NaN;

	private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private NOAACalculator calculator() {
		return new NOAACalculator();
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
			{"NJ Standard Date", "2017-10-17", "NJ", 11.13540831},
			{"LA Standard Date", "2017-10-17", "LA", 14.00704301},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 4.11872483},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 19.68246214},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 9.39008078},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 18.60488873},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 12.95743787},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 9.44903860},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunset() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 22.24081501},
			{"LA Standard Date", "2017-10-17", "LA", 1.31823827},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 15.64544001},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 8.56373515},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 21.53146217},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 5.65781259},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 20.21256620},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 0.58065990},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunset(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCNoon() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 16.69315478},
			{"LA Standard Date", "2017-10-17", "LA", 19.66659743},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 9.87832210},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 11.42090460},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 2.12779094},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 15.46078241},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 0.13137581},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 16.58492880},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 17.01703523},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCNoon(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCMidnight() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 4.69150247},
			{"LA Standard Date", "2017-10-17", "LA", 7.66495567},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 21.87692914},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 23.42271259},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 14.12619634},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 3.46470789},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 12.13318970},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 4.58673539},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 5.01848806},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCMidnight(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getTimeAtAzimuth() {
		Object[][] cases = {
			{"NJ East", "2017-10-17", "NJ", 90.0, 9.96599151},
			{"NJ West", "2017-10-17", "NJ", 270.0, 23.43667951},
			{"LA East", "2017-10-17", "LA", 90.0, 12.71769344},
			{"LA West", "2017-10-17", "LA", 270.0, 2.63764230},
			{"Sydney East", "2020-02-29", "SYDNEY", 90.0, 20.92605878},
			{"Sydney West", "2020-02-29", "SYDNEY", 270.0, 7.34651923},
			{"Macapa East No Cross", "2000-01-01", "MACAPA", 90.0, NO_EVENT},
			{"Polar East", "2017-06-21", "NORWAY", 90.0, 6.02028371},
		};
		for (Object[] c : cases) {
			double actual = calculator().getTimeAtAzimuth(date((String) c[1]), geo((String) c[2]), (double) c[3]);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getSolarElevation() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 8.24496867},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 33.56992906},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 46.19384884},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 3.98724677},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 2.89748529},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 65.99072692},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 40.24513182},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarElevation(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getSolarAzimuth() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 110.14145579},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 212.62717267},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 187.13298966},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 8.00635444},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 97.32262775},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 164.21440268},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 325.62368833},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarAzimuth(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getUTCSunriseAtZenith() {
		Object[][] cases = {
			{"NJ Civil", "2017-10-17", "NJ", 96.0, 10.70891556},
			{"NJ Nautical", "2017-10-17", "NJ", 102.0, 10.17562194},
			{"NJ Astronomical", "2017-10-17", "NJ", 108.0, 9.64406486},
			{"Sydney Civil", "2020-02-29", "SYDNEY", 96.0, 19.27827196},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), (double) c[3], true);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getUTCSunriseWithoutElevationAdjustment() {
		Object[][] cases = {
			{"NJ No Elev Adj", "2017-10-17", "NJ", 11.17291440},
			{"Jerusalem No Elev Adj", "1955-02-26", "JERUSALEM", 4.18884065},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, false);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getTimeAtAzimuthRejectsUnsupportedAzimuth() {
		calculator().getTimeAtAzimuth(date("2017-10-17"), geo("NJ"), 123.0);
	}

	@Test
	public void getCalculatorName() {
		assertEquals("US National Oceanic and Atmospheric Administration Algorithm", calculator().getCalculatorName());
	}
}
