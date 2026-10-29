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

/** An object representing the two mishnayos learned in the Mishna Yomi cycle. */
public class MishnaYomi {
  private final int startMasechtaNumber;
  private final int startChapter;
  private final int startMishna;
  private final int endMasechtaNumber;
  private final int endChapter;
  private final int endMishna;

  public MishnaYomi(
      int startMasechtaNumber,
      int startChapter,
      int startMishna,
      int endMasechtaNumber,
      int endChapter,
      int endMishna) {
    this.startMasechtaNumber = startMasechtaNumber;
    this.startChapter = startChapter;
    this.startMishna = startMishna;
    this.endMasechtaNumber = endMasechtaNumber;
    this.endChapter = endChapter;
    this.endMishna = endMishna;
  }

  public int getMasechtaNumber() {
    return startMasechtaNumber;
  }

  public int getStartMasechtaNumber() {
    return startMasechtaNumber;
  }

  public int getStartChapter() {
    return startChapter;
  }

  public int getChapter() {
    return startChapter;
  }

  public int getStartMishna() {
    return startMishna;
  }

  public int getEndMasechtaNumber() {
    return endMasechtaNumber;
  }

  public int getEndChapter() {
    return endChapter;
  }

  public int getEndMishna() {
    return endMishna;
  }

  public String getMasechtaTransliterated() {
    return LimudYomiData.MISHNA_NAMES[startMasechtaNumber];
  }

  public String getMasechta() {
    return LimudYomiData.MISHNA_NAMES_HEBREW[startMasechtaNumber];
  }

  public String getEndMasechtaTransliterated() {
    return LimudYomiData.MISHNA_NAMES[endMasechtaNumber];
  }

  public String getEndMasechta() {
    return LimudYomiData.MISHNA_NAMES_HEBREW[endMasechtaNumber];
  }
}
