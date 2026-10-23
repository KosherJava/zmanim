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
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Test;

import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.util.MeeusCalculator;
import com.kosherjava.zmanim.util.NOAACalculator;
import com.kosherjava.zmanim.util.SPACalculator;
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

	/**
	 * Near the date line a sunrise close to local noon has a UTC time just past 24:00. It must still fall on its own
	 * date: Cape Schmidt, Chukotka, on 10 January 2021, where the sun is up for about ten minutes around noon.
	 */
	@Test
	public void polarSunriseNearTheDateLineIsOnItsOwnDate() {
		ZoneId anadyr = ZoneId.of("Asia/Anadyr");
		LocalDate date = LocalDate.of(2021, 1, 10);
		AstronomicalCalculator[] calculators = { new NOAACalculator(), new MeeusCalculator(), new SPACalculator() };
		for (AstronomicalCalculator calculator : calculators) {
			AstronomicalCalendar calendar = new AstronomicalCalendar(new GeoLocation("Cape Schmidt", 68.88, -179.37, anadyr));
			calendar.setLocalDate(date);
			calendar.setAstronomicalCalculator(calculator);
			ZonedDateTime sunrise = calendar.getSeaLevelSunrise().atZone(anadyr);
			ZonedDateTime sunset = calendar.getSeaLevelSunset().atZone(anadyr);
			assertEquals(calculator.getCalculatorName(), date, sunrise.toLocalDate());
			assertTrue(calculator.getCalculatorName(), sunrise.isBefore(sunset));
		}
	}

	/**
	 * Asserts that {@code event} at the given place, in a zone a whole number of {@code zoneHours} from UTC, comes out
	 * within a second of {@code expectedMillis}. A result on the wrong day is 24 hours off.
	 */
	private void assertEventNear(String label, double latitude, double longitude, int zoneHours, LocalDate date,
			Function<AstronomicalCalendar, Instant> event, long expectedMillis) {
		AstronomicalCalendar calendar = new AstronomicalCalendar(
				new GeoLocation(label, latitude, longitude, ZoneOffset.ofHours(zoneHours)));
		calendar.setLocalDate(date);
		Instant actual = event.apply(calendar);
		Instant expected = Instant.ofEpochMilli(expectedMillis);
		assertTrue(label + ": " + actual + " is not near " + expected,
				Duration.between(expected, actual).abs().compareTo(Duration.ofSeconds(1)) < 0);
	}

	// One test for each date adjustment in getInstantFromTime. polarSunriseNearTheDateLineIsOnItsOwnDate covers a
	// sunrise at -6 hours local mean time or earlier.

	/** A sunrise just after local midnight, after 18:00 local mean time of the previous day, moves back a day. */
	@Test
	public void sunriseAfterSixPmLocalMeanTimeMovesBackADay() {
		assertEventNear("sunrise after 18:00", -70, 35.235, 2, LocalDate.of(2021, 1, 25),
				AstronomicalCalendar::getSeaLevelSunrise, 1611527037358L); // 2021-01-25T00:23:57+02:00
	}

	/** A sunset just before local midnight, before 06:00 local mean time of the date, moves forward a day. */
	@Test
	public void sunsetBeforeSixAmLocalMeanTimeMovesForwardADay() {
		assertEventNear("sunset before 06:00", -70, -179.37, -12, LocalDate.of(2021, 1, 23),
				AstronomicalCalendar::getSeaLevelSunset, 1611488915349L); // 2021-01-23T23:48:35-12:00
	}

	/** A sunset near local noon just east of the date line, at +30 hours local mean time or later, moves back a day. */
	@Test
	public void sunsetAfterThirtyHoursLocalMeanTimeMovesBackADay() {
		assertEventNear("sunset after 30:00", 68.88, 179, 12, LocalDate.of(2021, 12, 2),
				AstronomicalCalendar::getSeaLevelSunset, 1638403159563L); // 2021-12-02T11:59:19+12:00
	}

	/** Solar midnight before noon local mean time moves forward a day. */
	@Test
	public void solarMidnightBeforeNoonLocalMeanTimeMovesForwardADay() {
		assertEventNear("midnight before 12:00", -70, -179.37, -12, LocalDate.of(2021, 1, 1),
				AstronomicalCalendar::getSolarMidnight, 1609588897446L); // 2021-01-02T00:01:37-12:00
	}

	/** Solar noon before 0 hours local mean time moves forward a day. */
	@Test
	public void solarNoonBeforeMidnightLocalMeanTimeMovesForwardADay() {
		assertEventNear("noon before 00:00", -70, -179.37, -12, LocalDate.of(2021, 1, 1),
				AstronomicalCalendar::getSunTransit, 1609545683526L); // 2021-01-01T12:01:23-12:00
	}

	/** Solar noon after 24 hours local mean time moves back a day. */
	@Test
	public void solarNoonAfterTwentyFourHoursLocalMeanTimeMovesBackADay() {
		assertEventNear("noon after 24:00", -70, 179, 12, LocalDate.of(2021, 9, 14),
				AstronomicalCalendar::getSunTransit, 1631577581205L); // 2021-09-14T11:59:41+12:00
	}
}
