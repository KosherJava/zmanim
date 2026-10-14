/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates <em>Mishna Yomi</em>, the cycle of learning two mishnayos a day. The cycle began on
 * May 20, 1947 / 1 Sivan 5707 and repeats every 2,096 days. The schedule is calculated offline from
 * the number of mishnayos in each chapter of Shisha Sidrei Mishna.
 */
public class MishnaYomiCalculator {
  private static final LocalDate START = LocalDate.of(1947, 5, 20);
  private static final MishnaYomi[] CYCLE = buildCycle();

  public MishnaYomiCalculator() {}

  /**
   * Returns the Mishna Yomi for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the Mishna Yomi reading
   * @throws IllegalArgumentException if the date is before May 20, 1947, the start of the cycle
   */
  public static MishnaYomi getMishnaYomi(JewishCalendar calendar) {
    LocalDate date = calendar.getLocalDate();
    if (date.isBefore(START)) {
      throw new IllegalArgumentException(
          date + " is prior to the Mishna Yomi cycle that started on " + START);
    }
    int day = (int) (ChronoUnit.DAYS.between(START, date) % CYCLE.length);
    return CYCLE[day];
  }

  private static MishnaYomi[] buildCycle() {
    MishnaYomi[] cycle = new MishnaYomi[2096];
    int day = 0;
    int firstMasechta = 0;
    int firstChapter = 1;
    int firstMishna = 1;
    boolean haveFirst = false;
    for (int m = 0; m < LimudYomiData.MISHNA_CHAPTERS.length; m++) {
      int[] chapters = LimudYomiData.MISHNA_CHAPTERS[m];
      for (int c = 0; c < chapters.length; c++) {
        for (int mishnah = 1; mishnah <= chapters[c]; mishnah++) {
          if (!haveFirst) {
            firstMasechta = m;
            firstChapter = c + 1;
            firstMishna = mishnah;
            haveFirst = true;
          } else {
            cycle[day++] =
                new MishnaYomi(firstMasechta, firstChapter, firstMishna, m, c + 1, mishnah);
            haveFirst = false;
          }
        }
      }
    }
    return cycle;
  }
}
