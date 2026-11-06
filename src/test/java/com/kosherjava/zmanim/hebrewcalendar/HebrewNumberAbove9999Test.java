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
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * {@link HebrewDateFormatter#formatHebrewNumber(int)} past 9999, needed to format Jewish years of Gregorian dates after 6239.
 */
public class HebrewNumberAbove9999Test {

	@Test
	public void numbersUpTo9999AreUnchanged() {
		HebrewDateFormatter formatter = new HebrewDateFormatter();
		assertEquals("ה׳ אלפים", formatter.formatHebrewNumber(5000));
		assertEquals("תשפ״ו", formatter.formatHebrewNumber(5786));
		formatter.setUseLongHebrewYears(true);
		assertEquals("ה׳ תשפ״ו", formatter.formatHebrewNumber(5786));
		assertEquals("ט׳ תתקצ״ט", formatter.formatHebrewNumber(9999));
	}

	@Test
	public void thousandsFromTenAreWrittenAsNumbers() {
		HebrewDateFormatter formatter = new HebrewDateFormatter();
		assertEquals("י׳ אלפים", formatter.formatHebrewNumber(10_000));
		assertEquals("שמ״ה", formatter.formatHebrewNumber(12_345)); // short years drop the thousands, as for 5786
		formatter.setUseLongHebrewYears(true);
		assertEquals("יב׳ שמ״ה", formatter.formatHebrewNumber(12_345));
		assertEquals("טו׳ א׳", formatter.formatHebrewNumber(15_001));
		assertEquals("תתקצט׳ תתקצ״ט", formatter.formatHebrewNumber(999_999));
		formatter.setUseGershGershayim(false);
		assertEquals("יב שמה", formatter.formatHebrewNumber(12_345));
	}

	@Test(expected = IllegalArgumentException.class)
	public void aMillionIsRejected() {
		new HebrewDateFormatter().formatHebrewNumber(1_000_000);
	}

	@Test
	public void formatsDatesWithFiveDigitJewishYears() {
		HebrewDateFormatter formatter = new HebrewDateFormatter();
		formatter.setHebrewFormat(true);
		JewishCalendar jewishCalendar = new JewishCalendar(LocalDate.of(6500, 1, 1)); // Jewish year 10,260
		assertEquals(10_260, jewishCalendar.getJewishYear());
		assertTrue(formatter.format(jewishCalendar).endsWith(" ר״ס"));
	}
}
