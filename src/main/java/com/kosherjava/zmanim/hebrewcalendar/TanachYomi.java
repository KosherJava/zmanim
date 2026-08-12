/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
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
