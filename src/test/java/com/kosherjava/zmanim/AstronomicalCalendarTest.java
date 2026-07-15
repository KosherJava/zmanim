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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.Test;

import com.kosherjava.zmanim.util.SunTimesCalculator;

/**
 * Regression and behavioral coverage for {@link AstronomicalCalendar}. The time values are the library's own current
 * output for the fixture (Lakewood, NJ on 2017-10-17); the behavioral tests exercise the polar no-event {@code null}
 * contract, calculator swapping, and the twilight/offset relationships. (Complements the existing
 * {@link AstronomicalCalendarRegressionTest}, which covers local-mean-time/DST edge cases.)
 *
 * @author Test coverage
 */
public class AstronomicalCalendarTest {

	private static final LocalDate FIXTURE_DATE = LocalDate.of(2017, 10, 17);

	private AstronomicalCalendar fixtureCalendar() {
		AstronomicalCalendar calendar = new AstronomicalCalendar(TestLocations.lakewood());
		calendar.setLocalDate(FIXTURE_DATE);
		return calendar;
	}

	private void assertInstant(String label, String expectedIso, Instant actual) {
		assertEquals(label, Instant.parse(expectedIso), actual);
	}

	@Test
	public void sunriseAndSunset() {
		AstronomicalCalendar calendar = fixtureCalendar();
		assertInstant("sunrise", "2017-10-17T11:09:11.571783718Z", calendar.getSunrise());
		assertInstant("sunset", "2017-10-17T22:14:38.994862349Z", calendar.getSunset());
		assertInstant("seaLevelSunrise", "2017-10-17T11:09:51.403184642Z", calendar.getSeaLevelSunrise());
		assertInstant("seaLevelSunset", "2017-10-17T22:13:59.201432122Z", calendar.getSeaLevelSunset());
	}

	@Test
	public void transitAndSolarMidnight() {
		AstronomicalCalendar calendar = fixtureCalendar();
		assertInstant("sunTransit", "2017-10-17T16:42:12.781249470Z", calendar.getSunTransit());
		assertInstant("solarMidnight", "2017-10-18T04:42:06.833038724Z", calendar.getSolarMidnight());
	}

	@Test
	public void twilights() {
		AstronomicalCalendar calendar = fixtureCalendar();
		assertInstant("beginCivil", "2017-10-17T10:42:27.439221886Z", calendar.getBeginCivilTwilight());
		assertInstant("beginNautical", "2017-10-17T10:10:57.242471901Z", calendar.getBeginNauticalTwilight());
		assertInstant("beginAstronomical", "2017-10-17T09:39:33.523030241Z", calendar.getBeginAstronomicalTwilight());
		assertInstant("endCivil", "2017-10-17T22:41:21.435143220Z", calendar.getEndCivilTwilight());
		assertInstant("endNautical", "2017-10-17T23:12:49.151356447Z", calendar.getEndNauticalTwilight());
		assertInstant("endAstronomical", "2017-10-17T23:44:09.707296295Z", calendar.getEndAstronomicalTwilight());
	}

	@Test
	public void temporalHour() {
		assertEquals(Duration.parse("PT55M20.649853956S"), fixtureCalendar().getTemporalHour());
	}

	/**
	 * The offset-by-degrees helpers underpin the named twilight methods, so passing the civil zenith (96°) must
	 * reproduce civil twilight exactly.
	 */
	@Test
	public void offsetByDegreesMatchesNamedTwilight() {
		AstronomicalCalendar calendar = fixtureCalendar();
		assertEquals(calendar.getBeginCivilTwilight(), calendar.getSunriseOffsetByDegrees(96.0));
		assertEquals(calendar.getEndCivilTwilight(), calendar.getSunsetOffsetByDegrees(96.0));
	}

	/**
	 * Inside the Arctic Circle the sun neither rises nor sets on the summer solstice, so the rise/set based times must
	 * be {@code null} rather than throw.
	 */
	@Test
	public void polarNoEventReturnsNull() {
		AstronomicalCalendar calendar = new AstronomicalCalendar(TestLocations.norway(TestLocations.UTC));
		calendar.setLocalDate(LocalDate.of(2017, 6, 21));
		assertNull("polar sunrise", calendar.getSunrise());
		assertNull("polar sunset", calendar.getSunset());
	}

	/**
	 * The calculation engine is pluggable; the NOAA and USNO algorithms differ slightly, so swapping the calculator
	 * must change the result.
	 */
	@Test
	public void swappingCalculatorChangesResult() {
		AstronomicalCalendar noaa = fixtureCalendar();
		Instant noaaSunset = noaa.getSunset();

		AstronomicalCalendar sunTimes = fixtureCalendar();
		sunTimes.setAstronomicalCalculator(new SunTimesCalculator());
		Instant sunTimesSunset = sunTimes.getSunset();

		assertNotEquals(noaaSunset, sunTimesSunset);
	}
}
