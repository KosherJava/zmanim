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
 * Calculates Mishna Yomi, the two-mishnayos-a-day cycle that began on May 20, 1947 / 1 Sivan 5707.
 */
public class MishnaYomiCalculator {
  private static final LocalDate START = LocalDate.of(1947, 5, 20);
  private static final MishnaYomi[] CYCLE = buildCycle();

  public MishnaYomiCalculator() {}

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
