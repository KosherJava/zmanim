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
package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Coverage for {@link JewishCalendar} holiday / special-day predicates and {@link HebrewDateFormatter#formatParsha}.
 * These are deterministic halachic classifications asserted against known dates.
 *
 * @author Test coverage
 */
public class JewishCalendarHolidayTest {

	@Test
	public void roshHashanaAndTzomGedaliah() {
		JewishCalendar date = new JewishCalendar(LocalDate.of(2023, 9, 16));
		assertTrue(date.isRoshHashana());

		date.plusDays(1);
		assertTrue(date.isRoshHashana());

		date.plusDays(1); // Tzom Gedaliah (3 Tishrei)
		assertFalse(date.isRoshHashana());
		assertTrue(date.isTaanis());
	}

	@Test
	public void parsha() {
		HebrewDateFormatter formatter = new HebrewDateFormatter();
		assertEquals("Bereshis", formatter.formatParsha(new JewishCalendar(LocalDate.of(2023, 10, 14))));

		HebrewDateFormatter hebrew = new HebrewDateFormatter();
		hebrew.setHebrewFormat(true);
		assertEquals("בראשית", hebrew.formatParsha(new JewishCalendar(LocalDate.of(2023, 10, 14))));

		assertEquals("Terumah", formatter.formatParsha(new JewishCalendar(LocalDate.of(2024, 2, 17))));
	}

	@Test
	public void purimAndShushanPurim() {
		JewishCalendar date = new JewishCalendar();

		// 14 Adar in a non-leap year: Purim in unwalled cities, but Shushan Purim (15th) in walled cities.
		date.setJewishDate(5783, JewishDate.ADAR, 14);
		assertTrue(date.isPurim());
		date.setIsMukafChoma(true);
		assertFalse(date.isPurim());

		// 15 Adar: Shushan Purim, observed as Purim in walled cities.
		date.plusDays(1);
		assertTrue(date.isPurim());
		date.setIsMukafChoma(false);
		assertFalse(date.isPurim());

		// In a leap year Purim falls in Adar II, so Adar (I) 14 is not Purim.
		date.setJewishDate(5784, JewishDate.ADAR, 14);
		assertFalse(date.isPurim());
	}

	@Test
	public void assurBemelacha() {
		// Yom Kippur (10 Tishrei) is assur bemelacha.
		JewishCalendar yomKippur = new JewishCalendar(5784, JewishDate.TISHREI, 10);
		assertTrue(yomKippur.isAssurBemelacha());

		// An ordinary weekday is not.
		JewishCalendar weekday = new JewishCalendar(5784, JewishDate.CHESHVAN, 5);
		assertFalse(weekday.isAssurBemelacha());
	}
}
