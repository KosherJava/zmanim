/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** An object representing a Tanach Yomi seder. */
public class TanachYomi {
  private final String book;
  private final String hebrewBook;
  private final String seder;

  public TanachYomi(String book, String hebrewBook, String seder) {
    this.book = book;
    this.hebrewBook = hebrewBook;
    this.seder = seder;
  }

  public String getBook() {
    return book;
  }

  public String getHebrewBook() {
    return hebrewBook;
  }

  public String getSeder() {
    return seder;
  }
}
