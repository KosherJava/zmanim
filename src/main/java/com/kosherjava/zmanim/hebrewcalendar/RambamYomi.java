/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** An object representing a daily Rambam Mishneh Torah reading. */
public class RambamYomi {
  private final int bookNumber;
  private final int endBookNumber;
  private final int startChapter;
  private final int endChapter;

  public RambamYomi(int bookNumber, int startChapter, int endChapter) {
    this(bookNumber, startChapter, bookNumber, endChapter);
  }

  public RambamYomi(int bookNumber, int startChapter, int endBookNumber, int endChapter) {
    this.bookNumber = bookNumber;
    this.startChapter = startChapter;
    this.endBookNumber = endBookNumber;
    this.endChapter = endChapter;
  }

  public int getBookNumber() {
    return bookNumber;
  }

  public int getEndBookNumber() {
    return endBookNumber;
  }

  public int getStartChapter() {
    return startChapter;
  }

  public int getEndChapter() {
    return endChapter;
  }

  public String getName() {
    return LimudYomiData.RAMBAM_NAMES[bookNumber];
  }

  public String getHebrewName() {
    return LimudYomiData.RAMBAM_NAMES_HEBREW[bookNumber];
  }

  public String getEndName() {
    return LimudYomiData.RAMBAM_NAMES[endBookNumber];
  }

  public String getEndHebrewName() {
    return LimudYomiData.RAMBAM_NAMES_HEBREW[endBookNumber];
  }
}
