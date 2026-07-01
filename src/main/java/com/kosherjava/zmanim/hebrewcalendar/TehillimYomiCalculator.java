/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;


/** Calculates the daily Tehillim cycle divided by Hebrew day of month. */
public class TehillimYomiCalculator {
  private static final int[][] SCHEDULE = {
    {0, 0}, {1, 9}, {10, 17}, {18, 22}, {23, 28}, {29, 34}, {35, 38}, {39, 43}, {44, 48}, {49, 54},
    {55, 59}, {60, 65}, {66, 68}, {69, 71}, {72, 76}, {77, 78}, {79, 82}, {83, 87}, {88, 89},
    {90, 96}, {97, 103}, {104, 105}, {106, 107}, {108, 112}, {113, 118}, {119, 119}, {119, 119},
    {120, 134}, {135, 139}, {140, 144}, {145, 150}
  };

  public TehillimYomiCalculator() {}

  public static TehillimYomi getDailyTehillim(JewishCalendar calendar) {
    int day = calendar.getJewishDayOfMonth();
    if (day == 29 && calendar.getDaysInJewishMonth() == 29) {
      return new TehillimYomi(140, 150);
    }
    return new TehillimYomi(SCHEDULE[day][0], SCHEDULE[day][1]);
  }
}
