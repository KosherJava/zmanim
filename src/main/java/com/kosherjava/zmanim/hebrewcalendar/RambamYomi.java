/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/**
 * An object representing a daily Rambam Mishneh Torah reading. The one chapter cycle generally
 * contains one reading, while the three chapter cycle may contain multiple collapsed readings when
 * the daily chapters cross from one section of the Mishneh Torah to another.
 */
public class RambamYomi {
  private final int[] bookNumbers;
  private final String[] startChapters;
  private final String[] endChapters;

  public RambamYomi(int bookNumber, int startChapter, int endChapter) {
    this(bookNumber, String.valueOf(startChapter), String.valueOf(endChapter));
  }

  public RambamYomi(int bookNumber, String startChapter, String endChapter) {
    this(new int[] {bookNumber}, new String[] {startChapter}, new String[] {endChapter});
  }

  public RambamYomi(int[] bookNumbers, String[] startChapters, String[] endChapters) {
    this.bookNumbers = bookNumbers;
    this.startChapters = startChapters;
    this.endChapters = endChapters;
  }

  public int getReadingCount() {
    return bookNumbers.length;
  }

  public int getBookNumber(int index) {
    return bookNumbers[index];
  }

  public String getStartChapter(int index) {
    return startChapters[index];
  }

  public String getEndChapter(int index) {
    return endChapters[index];
  }

  public int getBookNumber() {
    return getBookNumber(0);
  }

  public int getEndBookNumber() {
    return getBookNumber(bookNumbers.length - 1);
  }

  public int getStartChapter() {
    return parseChapter(getStartChapter(0));
  }

  public int getEndChapter() {
    return parseChapter(getEndChapter(endChapters.length - 1));
  }

  public String getStartChapterString() {
    return getStartChapter(0);
  }

  public String getEndChapterString() {
    return getEndChapter(endChapters.length - 1);
  }

  public String getName() {
    return getName(0);
  }

  public String getHebrewName() {
    return getHebrewName(0);
  }

  public String getEndName() {
    return getName(bookNumbers.length - 1);
  }

  public String getEndHebrewName() {
    return getHebrewName(bookNumbers.length - 1);
  }

  public String getName(int index) {
    return LimudYomiData.RAMBAM_NAMES[bookNumbers[index]];
  }

  public String getHebrewName(int index) {
    return LimudYomiData.RAMBAM_NAMES_HEBREW[bookNumbers[index]];
  }

  private int parseChapter(String chapter) {
    int dash = chapter.indexOf('-');
    int colon = chapter.indexOf(':');
    int end = chapter.length();
    if (dash >= 0) {
      end = dash;
    }
    if (colon >= 0 && colon < end) {
      end = colon;
    }
    return Integer.parseInt(chapter.substring(0, end));
  }
}
