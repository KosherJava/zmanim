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
