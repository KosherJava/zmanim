/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** An object representing a Shemiras HaLashon Yomi reading. */
public class ShemirasHaLashonYomi {
  private final int book;
  private final String section;
  private final String start;
  private final String end;

  public ShemirasHaLashonYomi(int book, String section, String start, String end) {
    this.book = book;
    this.section = section;
    this.start = start;
    this.end = end;
  }

  public int getBook() {
    return book;
  }

  public String getSection() {
    return section;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }
}
