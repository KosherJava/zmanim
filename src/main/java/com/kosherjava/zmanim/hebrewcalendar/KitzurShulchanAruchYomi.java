/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** An object representing a Kitzur Shulchan Aruch Yomi reading. */
public class KitzurShulchanAruchYomi {
  private final String start;
  private final String end;

  public KitzurShulchanAruchYomi(String start, String end) {
    this.start = start;
    this.end = end;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }
}
