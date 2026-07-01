/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** An object representing the daily Tehillim portion. */
public class TehillimYomi {
  private final int startChapter;
  private final int endChapter;

  public TehillimYomi(int startChapter, int endChapter) {
    this.startChapter = startChapter;
    this.endChapter = endChapter;
  }

  public int getStartChapter() {
    return startChapter;
  }

  public int getEndChapter() {
    return endChapter;
  }
}
