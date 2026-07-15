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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Coverage for the {@link TefilaRules} daily-tefila predicates on two representative dates: an ordinary weekday during
 * the summer, and the first day of Sukkos.
 *
 * @author Test coverage
 */
public class TefilaRulesTest {

	private final TefilaRules rules = new TefilaRules();

	@Test
	public void ordinarySummerWeekday() {
		JewishCalendar date = new JewishCalendar(LocalDate.of(2023, 8, 21));

		assertTrue(rules.isTachanunRecitedShacharis(date));
		assertTrue(rules.isTachanunRecitedMincha(date));
		assertFalse(rules.isVeseinTalUmatarStartDate(date));
		assertFalse(rules.isVeseinTalUmatarStartingTonight(date));
		assertFalse(rules.isVeseinTalUmatarRecited(date));
		assertTrue(rules.isVeseinBerachaRecited(date));
		assertFalse(rules.isMashivHaruachStartDate(date));
		assertFalse(rules.isMashivHaruachEndDate(date));
		assertFalse(rules.isMashivHaruachRecited(date));
		assertTrue(rules.isMoridHatalRecited(date));
		assertFalse(rules.isHallelRecited(date));
		assertFalse(rules.isHallelShalemRecited(date));
		assertFalse(rules.isAlHanissimRecited(date));
		assertFalse(rules.isYaalehVeyavoRecited(date));
		assertTrue(rules.isMizmorLesodaRecited(date));
	}

	@Test
	public void firstDayOfSukkos() {
		JewishCalendar date = new JewishCalendar(LocalDate.of(2023, 10, 7));

		assertFalse(rules.isTachanunRecitedShacharis(date));
		assertFalse(rules.isTachanunRecitedMincha(date));
		assertFalse(rules.isVeseinTalUmatarStartDate(date));
		assertFalse(rules.isVeseinTalUmatarStartingTonight(date));
		assertFalse(rules.isVeseinTalUmatarRecited(date));
		assertTrue(rules.isVeseinBerachaRecited(date));
		assertTrue(rules.isMashivHaruachStartDate(date));
		assertFalse(rules.isMashivHaruachEndDate(date));
		assertFalse(rules.isMashivHaruachRecited(date));
		assertTrue(rules.isMoridHatalRecited(date));
		assertTrue(rules.isHallelRecited(date));
		assertTrue(rules.isHallelShalemRecited(date));
		assertFalse(rules.isAlHanissimRecited(date));
		assertTrue(rules.isYaalehVeyavoRecited(date));
		assertFalse(rules.isMizmorLesodaRecited(date));
	}
}
