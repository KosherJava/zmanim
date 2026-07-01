/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates the one and three chapter daily Rambam Mishneh Torah cycles that began on April 29,
 * 1984 / 27 Nisan 5744.
 */
public class RambamYomiCalculator {
  private static final LocalDate START = LocalDate.of(1984, 4, 29);
  private static final int ONE_CHAPTER_CYCLE = 1017;

  public RambamYomiCalculator() {}

  public static RambamYomi getRambamYomi(JewishCalendar calendar) {
    return getRambamYomi1Chapter(calendar);
  }

  public static RambamYomi getRambamYomi1Chapter(JewishCalendar calendar) {
    int d = day(calendar, ONE_CHAPTER_CYCLE);
    return chapterAt(d, LimudYomiData.RAMBAM_CHAPTERS);
  }

  public static RambamYomi getRambamYomi3Chapters(JewishCalendar calendar) {
    int d = day(calendar, ONE_CHAPTER_CYCLE / 3) * 3;
    RambamYomi first = chapterAt(d, LimudYomiData.RAMBAM_CHAPTERS);
    RambamYomi third = chapterAt(d + 2, LimudYomiData.RAMBAM_CHAPTERS);
    return new RambamYomi(
        first.getBookNumber(),
        first.getStartChapter(),
        third.getBookNumber(),
        third.getEndChapter());
  }

  private static int day(JewishCalendar calendar, int cycle) {
    LocalDate date = calendar.getLocalDate();
    if (date.isBefore(START)) {
      throw new IllegalArgumentException(
          date + " is prior to the Daily Rambam cycle that started on " + START);
    }
    return (int) (ChronoUnit.DAYS.between(START, date) % cycle);
  }

  private static RambamYomi chapterAt(int index, int[] chapters) {
    int total = index;
    for (int i = 0; i < chapters.length; i++) {
      if (total < chapters[i]) {
        return new RambamYomi(i, total + 1, total + 1);
      }
      total -= chapters[i];
    }
    throw new IllegalStateException("Unable to calculate Rambam Yomi");
  }
}
