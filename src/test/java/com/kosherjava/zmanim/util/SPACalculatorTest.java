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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.kosherjava.zmanim.TestLocations;

/**
 * Coverage for {@link SPACalculator}, the NREL Solar Position Algorithm engine. Most cases are regression against the
 * library's own current output (8-decimal delta), but this suite also includes a <b>Tier-A independently verifiable</b>
 * anchor: the published NREL reference case (Reda &amp; Andreas, NREL/TP-560-34302), plus coverage of the
 * pressure/temperature/&Delta;T configuration knobs.
 *
 * @author Test coverage
 */
public class SPACalculatorTest {

	private static final double DELTA = 1e-8;
	private static final double ZENITH = 90.0;
	private static final double NO_EVENT = Double.NaN;

	private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private SPACalculator calculator() {
		return new SPACalculator();
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
			{"NJ Standard Date", "2017-10-17", "NJ", 11.13593689},
			{"LA Standard Date", "2017-10-17", "LA", 14.00756754},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 4.11873932},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 19.68293143},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 9.38998685},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 18.60534965},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 12.95741199},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 9.44912615},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunset() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 22.24099415},
			{"LA Standard Date", "2017-10-17", "LA", 1.31843857},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 15.64512300},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", NO_EVENT},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 8.56367402},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 21.53102552},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 5.65791427},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 20.21191830},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 0.58028674},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunset(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCNoon() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 16.69350885},
			{"LA Standard Date", "2017-10-17", "LA", 19.66696009},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 9.87817106},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 11.42056214},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 2.12799525},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 15.46051831},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 0.13165740},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 16.58459266},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 17.01689317},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCNoon(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCMidnight() {
		Object[][] cases = {
			{"NJ Standard Date", "2017-10-17", "NJ", 4.69189152},
			{"LA Standard Date", "2017-10-17", "LA", 7.66535335},
			{"Jerusalem Hist. Date", "1955-02-26", "JERUSALEM", 21.87680310},
			{"Norway Polar Solstice", "2017-06-21", "NORWAY", 23.42238534},
			{"Sydney Leap Day", "2020-02-29", "SYDNEY", 14.12642351},
			{"Macapa Equator New Year", "2000-01-01", "MACAPA", 3.46446119},
			{"Suva Fiji Solstice", "2023-06-21", "SUVA", 12.13348552},
			{"Ushuaia Southern Winter", "2017-06-21", "USHUAIA", 4.58641480},
			{"NJ Future DeltaT", "2100-07-04", "NJ", 5.01835716},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCMidnight(date((String) c[1]), geo((String) c[2]));
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getTimeAtAzimuth() {
		Object[][] cases = {
			{"NJ East", "2017-10-17", "NJ", 90.0, 9.96630382},
			{"NJ West", "2017-10-17", "NJ", 270.0, 23.43696229},
			{"LA East", "2017-10-17", "LA", 90.0, 12.71802376},
			{"LA West", "2017-10-17", "LA", 270.0, 2.63792595},
			{"Sydney East", "2020-02-29", "SYDNEY", 90.0, 20.92601636},
			{"Sydney West", "2020-02-29", "SYDNEY", 270.0, 7.34685680},
			{"Macapa East No Cross", "2000-01-01", "MACAPA", 90.0, NO_EVENT},
			{"Polar East", "2017-06-21", "NORWAY", 90.0, 6.01986373},
		};
		for (Object[] c : cases) {
			double actual = calculator().getTimeAtAzimuth(date((String) c[1]), geo((String) c[2]), (double) c[3]);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getSolarElevation() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 8.24142367},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 33.57165658},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 46.19391692},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 3.98549481},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 2.89087872},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 65.99118480},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 40.24649656},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarElevation(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getSolarAzimuth() {
		Object[][] cases = {
			{"NJ Noon", "2017-10-17 12:00:00", "NJ", 110.13757333},
			{"NJ Evening", "2017-10-17 18:30:00", "NJ", 212.62157928},
			{"LA Evening", "2017-10-17 20:00:00", "LA", 187.12533185},
			{"Polar Midnight", "2017-06-21 00:00:00", "NORWAY", 8.01127958},
			{"Sydney Morning", "2020-02-29 20:00:00", "SYDNEY", 97.32308776},
			{"Macapa Local Noon", "2000-01-01 15:00:00", "MACAPA", 164.22300003},
			{"Suva Afternoon", "2023-06-21 02:00:00", "SUVA", 325.62786587},
		};
		for (Object[] c : cases) {
			double actual = calculator().getSolarAzimuth(instant((String) c[1]), geo((String) c[2]));
			assertEquals((String) c[0], (double) c[3], actual, DELTA);
		}
	}

	@Test
	public void getUTCSunriseAtZenith() {
		Object[][] cases = {
			{"NJ Civil", "2017-10-17", "NJ", 96.0, 10.70943974},
			{"NJ Nautical", "2017-10-17", "NJ", 102.0, 10.17613961},
			{"NJ Astronomical", "2017-10-17", "NJ", 108.0, 9.64457480},
			{"Sydney Civil", "2020-02-29", "SYDNEY", 96.0, 19.27874463},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), (double) c[3], true);
			assertUtcHour((String) c[0], (double) c[4], actual);
		}
	}

	@Test
	public void getUTCSunriseWithoutElevationAdjustment() {
		Object[][] cases = {
			{"NJ No Elev Adj", "2017-10-17", "NJ", 11.17344334},
			{"Jerusalem No Elev Adj", "1955-02-26", "JERUSALEM", 4.18885558},
		};
		for (Object[] c : cases) {
			double actual = calculator().getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, false);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test
	public void getUTCSunriseWithDeltaTDisabled() {
		Object[][] cases = {
			{"NJ dT off", "2017-10-17", "NJ", 11.13586818},
			{"Jerusalem dT off", "1955-02-26", "JERUSALEM", 4.11871824},
		};
		for (Object[] c : cases) {
			SPACalculator calc = calculator();
			calc.setApplyDeltaT(false);
			double actual = calc.getUTCSunrise(date((String) c[1]), geo((String) c[2]), ZENITH, true);
			assertUtcHour((String) c[0], (double) c[3], actual);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getTimeAtAzimuthRejectsUnsupportedAzimuth() {
		calculator().getTimeAtAzimuth(date("2017-10-17"), geo("NJ"), 123.0);
	}

	/*
	 * Tier-A independently verifiable reference: the published NREL Solar Position Algorithm example
	 * (Reda & Andreas, NREL/TP-560-34302). 2003-10-17 12:30:30 local (tz -7 => 19:30:30 UTC),
	 * longitude -105.1786, latitude 39.742476, elevation 1830.14 m, pressure 820 mb, temperature 11 C,
	 * delta-T 67 s. The paper reports topocentric elevation 39.888378 and azimuth 194.340241.
	 */
	private SPACalculator nrelCalculator() {
		SPACalculator calc = new SPACalculator();
		calc.setDeltaTOverride(67.0);
		calc.setPressure(820);
		calc.setTemperature(11);
		return calc;
	}

	private Instant nrelInstant() {
		return instant("2003-10-17 19:30:30");
	}

	private GeoLocation nrelLocation() {
		return new GeoLocation("NREL Reference", 39.742476, -105.1786, 1830.14, TestLocations.UTC);
	}

	@Test
	public void getSolarElevationNrelReference() {
		double elevation = nrelCalculator().getSolarElevation(nrelInstant(), nrelLocation());
		assertEquals("NREL published topocentric elevation", 39.888378, elevation, 0.001);
	}

	@Test
	public void getSolarAzimuthNrelReference() {
		double azimuth = nrelCalculator().getSolarAzimuth(nrelInstant(), nrelLocation());
		assertEquals("NREL published topocentric azimuth", 194.340241, azimuth, 0.001);
	}

	@Test
	public void defaultsAndSetters() {
		SPACalculator calc = new SPACalculator();
		assertTrue(calc.isApplyDeltaT());
		assertNull(calc.getDeltaTOverride());
		assertEquals(1013.25, calc.getPressure(), 0);
		assertEquals(10.0, calc.getTemperature(), 0);

		calc.setDeltaTOverride(67.0);
		calc.setPressure(820);
		calc.setTemperature(11);
		calc.setApplyDeltaT(false);

		assertEquals(Double.valueOf(67.0), calc.getDeltaTOverride());
		assertEquals(820.0, calc.getPressure(), 0);
		assertEquals(11.0, calc.getTemperature(), 0);
		assertTrue(!calc.isApplyDeltaT());
	}

	@Test
	public void getCalculatorName() {
		assertEquals("NREL Solar Position Algorithm", calculator().getCalculatorName());
	}
}
