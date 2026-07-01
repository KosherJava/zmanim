/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;

/**
 * Calculates Tanach Yomi according to the annual Masoretic seder cycle that began on October 26,
 * 1948 / 23 Tishrei 5709.
 */
public class TanachYomiCalculator {
  private static final LocalDate START = LocalDate.of(1948, 10, 26);
  private static final String JOSHUA = "Joshua";
  private static final String JEREMIAH = "Jeremiah";
  private static final String RUTH = "Ruth";
  private static final String SHIR_HASHIRIM = "Song of Songs";
  private static final String[] BOOKS = {
    JOSHUA,
    "Judges",
    "Samuel",
    "Kings",
    "Isaiah",
    JEREMIAH,
    "Ezekiel",
    "Minor Prophets",
    "Psalms",
    "Proverbs",
    "Job",
    SHIR_HASHIRIM,
    RUTH,
    "Lamentations",
    "Ecclesiastes",
    "Esther",
    "Daniel",
    "Ezra and Nehemiah",
    "Chronicles",
    "Chronicles"
  };
  private static final String[] HEBREW_BOOKS = {
    "יהושע",
    "שופטים",
    "שמואל",
    "מלכים",
    "ישעיהו",
    "ירמיהו",
    "יחזקאל",
    "תרי עשר",
    "תהלים",
    "משלי",
    "איוב",
    "שיר השירים",
    "רות",
    "איכה",
    "קהלת",
    "אסתר",
    "דניאל",
    "עזרא ונחמיה",
    "דברי הימים",
    "דברי הימים"
  };
  private static final int[] BLATT = {
    14, 14, 34, 35, 26, 31, 29, 21, 19, 8, 8, 1, 1, 1, 4, 5, 7, 10, 25, 25
  };

  public TanachYomiCalculator() {}

  public static TanachYomi getTanachYomi(JewishCalendar calendar) {
    if (calendar.getLocalDate().isBefore(START)) {
      throw new IllegalArgumentException(
          calendar.getLocalDate() + " is prior to the Tanach Yomi cycle that started on " + START);
    }
    if (skipDay(calendar)) {
      return null;
    }
    int year = calendar.getJewishYear();
    JewishCalendar start = new JewishCalendar(year, JewishCalendar.TISHREI, 23);
    int total = 0;
    if (calendar.getLocalDate().isBefore(start.getLocalDate())) {
      JewishCalendar rh = new JewishCalendar(year, JewishCalendar.TISHREI, 1);
      int rhDow = rh.getDayOfWeek();
      int blatt = rhDow == 5 ? 11 : rhDow == 7 ? 10 : 12;
      JewishCalendar loop = new JewishCalendar(year, JewishCalendar.TISHREI, 3);
      while (loop.getLocalDate().isBefore(calendar.getLocalDate())) {
        if (!skipDay(loop)) {
          blatt++;
        }
        loop.plusDays(1);
      }
      return new TanachYomi("Chronicles", "דברי הימים", String.valueOf(blatt));
    }
    JewishCalendar loop = new JewishCalendar(year, JewishCalendar.TISHREI, 23);
    while (loop.getLocalDate().isBefore(calendar.getLocalDate())) {
      if (!skipDay(loop)) {
        total++;
      }
      loop.plusDays(1);
    }
    ReadingTable readingTable = makeReadingTable(year);
    int remaining = total;
    for (int i = 0; i < readingTable.blatt.length; i++) {
      if (remaining < readingTable.blatt[i]) {
        int blatt = remaining + 1;
        String seder = String.valueOf(blatt);
        if ((readingTable.longShirHaShirim && BOOKS[i].equals(SHIR_HASHIRIM))
            || (readingTable.longRuth && BOOKS[i].equals(RUTH))) {
          seder = "1." + blatt;
        } else if (readingTable.longJoshua && BOOKS[i].equals(JOSHUA) && blatt >= 4) {
          seder = blatt == 4 ? "4.1" : blatt == 5 ? "4.2" : String.valueOf(blatt - 1);
        } else if (readingTable.longJeremiah && BOOKS[i].equals(JEREMIAH) && blatt >= 9) {
          seder = blatt == 9 ? "9.1" : blatt == 10 ? "9.2" : String.valueOf(blatt - 1);
        }
        return new TanachYomi(BOOKS[i], HEBREW_BOOKS[i], seder);
      }
      remaining -= readingTable.blatt[i];
    }
    throw new IllegalStateException("Unable to calculate Tanach Yomi");
  }

  private static boolean skipDay(JewishCalendar calendar) {
    if (calendar.getDayOfWeek() == 7) {
      return true;
    }
    JewishCalendar c = new JewishCalendar(calendar.getLocalDate());
    c.setInIsrael(true);
    c.setUseModernHolidays(true);
    int yomTov = c.getYomTovIndex();
    if (yomTov == JewishCalendar.PURIM
        || yomTov == JewishCalendar.YOM_HAATZMAUT
        || yomTov == JewishCalendar.TISHA_BEAV
        || yomTov == JewishCalendar.YOM_KIPPUR
        || yomTov == JewishCalendar.SHAVUOS
        || yomTov == JewishCalendar.ROSH_HASHANA
        || yomTov == JewishCalendar.SHEMINI_ATZERES) {
      return true;
    }
    return (yomTov == JewishCalendar.PESACH
            && (c.getJewishDayOfMonth() == 15 || c.getJewishDayOfMonth() == 21))
        || (yomTov == JewishCalendar.SUCCOS && c.getJewishDayOfMonth() == 15);
  }

  private static int calculateNumDaysToRead(int year) {
    JewishCalendar loop = new JewishCalendar(year, JewishCalendar.TISHREI, 23);
    JewishCalendar end = new JewishCalendar(year + 1, JewishCalendar.TISHREI, 22);
    int count = 0;
    while (!loop.getLocalDate().isAfter(end.getLocalDate())) {
      if (!skipDay(loop)) {
        count++;
      }
      loop.plusDays(1);
    }
    return count;
  }

  private static ReadingTable makeReadingTable(int year) {
    int numDays = calculateNumDaysToRead(year);
    int count = JewishDate.isJewishLeapYear(year) ? numDays - 25 : numDays;
    int extra = count - 293;
    int[] table = BLATT.clone();
    ReadingTable result = new ReadingTable(table);
    switch (extra) {
      case 0:
        return result;
      case 4:
        table[0] = 15;
        result.longJoshua = true;
      case 3:
        table[5] = 32;
        result.longJeremiah = true;
      case 2:
        table[11] = 2;
        result.longShirHaShirim = true;
      case 1:
        table[12] = 2;
        result.longRuth = true;
        return result;
      default:
        throw new IllegalStateException(
            "Unexpected Tanach Yomi year length " + year + " => " + extra);
    }
  }

  private static class ReadingTable {
    private final int[] blatt;
    private boolean longRuth;
    private boolean longShirHaShirim;
    private boolean longJeremiah;
    private boolean longJoshua;

    private ReadingTable(int[] blatt) {
      this.blatt = blatt;
    }
  }
}
