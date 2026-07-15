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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.Test;

/**
 * Regression and behavioral coverage for {@link ZmanimCalendar}, the standard-zmanim entry point. Every zero-argument
 * zman getter is pinned against the library's own current output for the fixture (Lakewood, NJ on 2017-10-17), and the
 * elevation toggle is verified to actually move the sunrise-based zmanim. (The full opinion-heavy surface is covered by
 * {@link ComprehensiveZmanimCalendarTest}.)
 *
 * @author Test coverage
 */
public class ZmanimCalendarTest {

	private static final LocalDate FIXTURE_DATE = LocalDate.of(2017, 10, 17);

	private ZmanimCalendar fixtureCalendar() {
		ZmanimCalendar calendar = new ZmanimCalendar(TestLocations.lakewood());
		calendar.setLocalDate(FIXTURE_DATE);
		return calendar;
	}

	private void assertInstant(String label, String expectedIso, Instant actual) {
		assertEquals(label, Instant.parse(expectedIso), actual);
	}

	@Test
	public void alosAndMisheyakir() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertInstant("alos16.1", "2017-10-17T09:49:30.219906135Z", calendar.getAlos16Point1Degrees());
		assertInstant("alos72", "2017-10-17T09:57:51.403184642Z", calendar.getAlos72Minutes());
	}

	@Test
	public void sofZmanShmaAndTfila() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertInstant("sofZmanShmaGRA", "2017-10-17T13:55:53.352746510Z", calendar.getSofZmanShmaGRA());
		assertInstant("sofZmanShmaMGA72", "2017-10-17T13:19:53.352746510Z", calendar.getSofZmanShmaMGA72Minutes());
		assertInstant("sofZmanTfilaGRA", "2017-10-17T14:51:14.002600466Z", calendar.getSofZmanTfilaGRA());
		assertInstant("sofZmanTfilaMGA72", "2017-10-17T14:27:14.002600466Z", calendar.getSofZmanTfilaMGA72Minutes());
	}

	@Test
	public void chatzos() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertInstant("chatzosHayom", "2017-10-17T16:42:12.781249470Z", calendar.getChatzosHayom());
		assertInstant("chatzosHayomAsHalfDay", "2017-10-17T16:41:55.302308378Z", calendar.getChatzosHayomAsHalfDay());
		assertInstant("chatzosHalayla", "2017-10-18T04:42:06.833038724Z", calendar.getChatzosHalayla());
	}

	@Test
	public void minchaAndPlag() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertInstant("minchaGedolaGRA", "2017-10-17T17:09:35.627235356Z", calendar.getMinchaGedolaGRA());
		assertInstant("minchaKetanaGRA", "2017-10-17T19:55:37.576797224Z", calendar.getMinchaKetanaGRA());
		assertInstant("plagHaminchaGRA", "2017-10-17T21:04:48.389114669Z", calendar.getPlagHaminchaGRA());
	}

	@Test
	public void candleLightingAndTzais() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertInstant("candleLighting", "2017-10-17T21:55:59.201432122Z", calendar.getCandleLighting());
		assertInstant("tzais72", "2017-10-17T23:25:59.201432122Z", calendar.getTzais72Minutes());
		assertInstant("tzaisGeonim8.5", "2017-10-17T22:54:29.772724455Z", calendar.getTzaisGeonim8Point5Degrees());
	}

	@Test
	public void shaahZmanis() {
		ZmanimCalendar calendar = fixtureCalendar();
		assertEquals("shaahZmanisGRA", Duration.parse("PT55M20.649853956S"), calendar.getShaahZmanisGRA());
		assertEquals("shaahZmanis72", Duration.parse("PT1H7M20.649853956S"), calendar.getShaahZmanis72Minutes());
	}

	/**
	 * Toggling {@link ZmanimCalendar#setUseElevation(boolean)} must change the sunrise/sunset-based zmanim, since the
	 * elevation adjustment shifts the underlying sunrise. Written relative to the default so it does not depend on what
	 * that default currently is.
	 */
	@Test
	public void elevationToggleAffectsZmanim() {
		ZmanimCalendar calendar = fixtureCalendar();
		boolean defaultSetting = calendar.isUseElevation();
		Instant atDefault = calendar.getSofZmanShmaGRA();

		calendar.setUseElevation(!defaultSetting);
		Instant toggled = calendar.getSofZmanShmaGRA();

		assertNotEquals(atDefault, toggled);
	}
}
