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

/**
 * Calculates the daily Tehillim cycle divided by the Hebrew day of month. In a 29-day Hebrew month,
 * the 30th day's portion is combined with the 29th day so that Sefer Tehillim is completed every
 * month.
 */
public class TehillimYomiCalculator {
  private static final int[][] SCHEDULE = {
    {0, 0},
    {1, 9},
    {10, 17},
    {18, 22},
    {23, 28},
    {29, 34},
    {35, 38},
    {39, 43},
    {44, 48},
    {49, 54},
    {55, 59},
    {60, 65},
    {66, 68},
    {69, 71},
    {72, 76},
    {77, 78},
    {79, 82},
    {83, 87},
    {88, 89},
    {90, 96},
    {97, 103},
    {104, 105},
    {106, 107},
    {108, 112},
    {113, 118},
    {119, 119},
    {119, 119},
    {120, 134},
    {135, 139},
    {140, 144},
    {145, 150}
  };

  public TehillimYomiCalculator() {}

  /**
   * Returns the daily Tehillim for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the daily Tehillim reading
   */
  public static TehillimYomi getDailyTehillim(JewishCalendar calendar) {
    int day = calendar.getJewishDayOfMonth();
    if (day == 29 && calendar.getDaysInJewishMonth() == 29) {
      return new TehillimYomi(140, 150);
    }
    return new TehillimYomi(SCHEDULE[day][0], SCHEDULE[day][1]);
  }
}
