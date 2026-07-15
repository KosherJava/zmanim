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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Test;

/**
 * Coverage for the location- and date-gated zmanim of {@link ComprehensiveZmanimCalendar} that only produce a value
 * under specific conditions: the <em>chametz</em> times (only on Erev Pesach, 14 Nissan), the <em>Kidush Levana</em> /
 * molad times (only within their lunar-month windows), and the <em>polar</em> zmanim (only inside the Arctic/Antarctic
 * circle during polar day/night, when there is no ordinary sunrise or sunset). The broader
 * {@link ComprehensiveZmanimCalendarTest} net pins these as {@code null} for its temperate ordinary-day fixture; this
 * test verifies they return the correct times under the conditions they actually apply.
 *
 * @author Test coverage
 */
public class SpecialDayZmanimTest {

	private ComprehensiveZmanimCalendar calendarFor(LocalDate date) {
		ComprehensiveZmanimCalendar calendar = new ComprehensiveZmanimCalendar(TestLocations.lakewood());
		calendar.setLocalDate(date);
		return calendar;
	}

	private void assertInstant(String label, String expectedIso, Instant actual) {
		assertEquals(label, Instant.parse(expectedIso), actual);
	}

	/** 14 Nissan 5777 (Erev Pesach) falls on 2017-04-10. */
	private static final LocalDate EREV_PESACH = LocalDate.of(2017, 4, 10);

	@Test
	public void chametzTimesOnErevPesach() {
		ComprehensiveZmanimCalendar calendar = calendarFor(EREV_PESACH);

		// All ten sof zman achilas/biur chametz opinions must produce a time on Erev Pesach.
		assertNotNull(calendar.getSofZmanAchilasChametzGRA());
		assertNotNull(calendar.getSofZmanAchilasChametzMGA72Minutes());
		assertNotNull(calendar.getSofZmanAchilasChametzMGA72MinutesZmanis());
		assertNotNull(calendar.getSofZmanAchilasChametzMGA16Point1Degrees());
		assertNotNull(calendar.getSofZmanAchilasChametzBaalHatanya());
		assertNotNull(calendar.getSofZmanBiurChametzGRA());
		assertNotNull(calendar.getSofZmanBiurChametzMGA72Minutes());
		assertNotNull(calendar.getSofZmanBiurChametzMGA72MinutesZmanis());
		assertNotNull(calendar.getSofZmanBiurChametzMGA16Point1Degrees());
		assertNotNull(calendar.getSofZmanBiurChametzBaalHatanya());

		// Regression on the GRA opinions; biur (end of the 5th hour) is one shaah zmanis after achilas (end of the 4th).
		assertInstant("achilasGRA", "2017-04-10T14:47:45.675920561Z", calendar.getSofZmanAchilasChametzGRA());
		assertInstant("biurGRA", "2017-04-10T15:53:07.762314340Z", calendar.getSofZmanBiurChametzGRA());
	}

	@Test
	public void chametzTimesAreNullOnAnOrdinaryDay() {
		ComprehensiveZmanimCalendar calendar = calendarFor(LocalDate.of(2017, 10, 17));
		assertNull(calendar.getSofZmanAchilasChametzGRA());
		assertNull(calendar.getSofZmanBiurChametzGRA());
	}

	@Test
	public void zmanMoladAndTchilasKidushLevana() {
		// The molad of Cheshvan 5778 (the moment itself falls on 2017-10-20).
		assertInstant("zmanMolad", "2017-10-20T09:52:00.170666666Z", calendarFor(LocalDate.of(2017, 10, 20)).getZmanMolad());

		// Earliest Kidush Levana: 3 days and 7 days after the molad.
		assertInstant("tchilas3Days", "2017-10-23T09:52:00.170666666Z",
				calendarFor(LocalDate.of(2017, 10, 23)).getTchilasZmanKidushLevana3Days());
		assertInstant("tchilas7Days", "2017-10-27T09:52:00.170666666Z",
				calendarFor(LocalDate.of(2017, 10, 27)).getTchilasZmanKidushLevana7Days());
	}

	@Test
	public void sofZmanKidushLevana() {
		// Latest Kidush Levana for the month whose molad was in late September 2017 (sayable through 2017-10-05).
		assertInstant("sof15Days", "2017-10-05T21:07:56.837333333Z",
				calendarFor(LocalDate.of(2017, 10, 5)).getSofZmanKidushLevana15Days());
		assertInstant("sofBetweenMoldos", "2017-10-05T15:29:58.503333333Z",
				calendarFor(LocalDate.of(2017, 10, 5)).getSofZmanKidushLevanaBetweenMoldos());
	}

	@Test
	public void kidushLevanaIsNullOutsideItsWindow() {
		// 2017-10-17 is past the previous month's sof zman and before the next month's tchilas zman.
		ComprehensiveZmanimCalendar calendar = calendarFor(LocalDate.of(2017, 10, 17));
		assertNull(calendar.getZmanMolad());
		assertNull(calendar.getTchilasZmanKidushLevana3Days());
		assertNull(calendar.getSofZmanKidushLevana15Days());
	}

	private ComprehensiveZmanimCalendar norwayCalendarFor(LocalDate date) {
		ComprehensiveZmanimCalendar calendar =
				new ComprehensiveZmanimCalendar(TestLocations.norway(ZoneId.of("Europe/Oslo")));
		calendar.setLocalDate(date);
		return calendar;
	}

	/** 2017-06-21 (summer solstice) is inside the polar day at the Norway fixture: no ordinary sunrise or sunset. */
	private static final LocalDate NORWAY_POLAR_DAY = LocalDate.of(2017, 6, 21);

	@Test
	public void polarZmanimDuringPolarDay() {
		ComprehensiveZmanimCalendar calendar = norwayCalendarFor(NORWAY_POLAR_DAY);

		// Precondition: there is genuinely no sunrise/sunset here, which is what activates the polar alternatives.
		assertNull(calendar.getSunrise());
		assertNull(calendar.getSunset());

		// Ben Ish Chai: use the sun's crossing of due east (90) / due west (270) as the "sunrise"/"sunset".
		assertInstant("polarSunriseBenIshChai", "2017-06-21T06:01:13.021342105Z", calendar.getPolarSunriseBenIshChai());
		assertInstant("polarSunsetBenIshChai", "2017-06-21T16:49:17.587975180Z", calendar.getPolarSunsetBenIshChai());
		assertInstant("polarPlagBenIshChai", "2017-06-21T15:41:47.112284232Z", calendar.getPolarPlagHaminchaBenIshChai());

		// Teshuvos Vehanhagos: during polar summer the start of day is chatzos halayla.
		assertInstant("polarStartOfDayTeshuvos", "2017-06-21T23:25:21.765329979Z",
				calendar.getPolarStartOfDayTeshuvosVehanhagos());
		assertInstant("polarPlagTeshuvos", "2017-06-21T20:55:21.765329979Z",
				calendar.getPolarPlagHaminchaTeshuvosVehanhagos());
	}

	@Test
	public void polarZmanimAreNullWhereSunRisesAndSets() {
		// At the temperate Lakewood fixture the sun rises and sets normally, so the polar alternatives do not apply.
		ComprehensiveZmanimCalendar calendar = calendarFor(LocalDate.of(2017, 10, 17));
		assertNull(calendar.getPolarSunriseBenIshChai());
		assertNull(calendar.getPolarSunsetBenIshChai());
		assertNull(calendar.getPolarPlagHaminchaBenIshChai());
		assertNull(calendar.getPolarStartOfDayTeshuvosVehanhagos());
		assertNull(calendar.getPolarPlagHaminchaTeshuvosVehanhagos());
	}
}
