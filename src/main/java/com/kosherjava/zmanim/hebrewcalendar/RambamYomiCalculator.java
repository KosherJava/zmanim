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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates the one and three chapter daily Rambam Mishneh Torah cycles that began on April 29,
 * 1984 / 27 Nisan 5744. The one chapter cycle repeats every 1,017 days and the three chapter cycle
 * repeats every 339 days. The opening introduction sections and the published special groupings in
 * <em>Seder Tefilos</em> and <em>Hilchos Chametz U'Matzah</em> are handled offline.
 */
public class RambamYomiCalculator {
  private static final LocalDate START = LocalDate.of(1984, 4, 29);
  private static final int ONE_CHAPTER_CYCLE = 1017;
  private static final String[][] FIRST_FOUR_SECTIONS = {
    {"1-21", "22-33", "34-45"},
    {"1-83", "84-166", "167-248"},
    {"1-122", "123-245", "246-365"},
    {"1:1-4:8", "5:1-9:9", "10:1-14:10"}
  };

  public RambamYomiCalculator() {}

  /**
   * Returns the one chapter daily Rambam reading for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the one chapter Rambam Yomi reading
   * @throws IllegalArgumentException if the date is before April 29, 1984, the start of the cycle
   */
  public static RambamYomi getRambamYomi(JewishCalendar calendar) {
    return getRambamYomi1Chapter(calendar);
  }

  /**
   * Returns the one chapter daily Rambam reading for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the one chapter Rambam Yomi reading
   * @throws IllegalArgumentException if the date is before April 29, 1984, the start of the cycle
   */
  public static RambamYomi getRambamYomi1Chapter(JewishCalendar calendar) {
    int d = day(calendar, ONE_CHAPTER_CYCLE);
    RambamYomi reading = chapterAt(d, LimudYomiData.RAMBAM_CHAPTERS);
    if ("The Order of Prayer".equals(reading.getName())
        && "4".equals(reading.getStartChapterString())) {
      return new RambamYomi(reading.getBookNumber(), "4", "5");
    }
    return reading;
  }

  /**
   * Returns the three chapter daily Rambam reading for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the three chapter Rambam Yomi reading, collapsed by adjacent Mishneh Torah section
   * @throws IllegalArgumentException if the date is before April 29, 1984, the start of the cycle
   */
  public static RambamYomi getRambamYomi3Chapters(JewishCalendar calendar) {
    int d = day(calendar, ONE_CHAPTER_CYCLE / 3) * 3;
    int[] chapters = LimudYomiData.RAMBAM_CHAPTERS.clone();
    chapters[15] = 5; // The Order of Prayer has an extra day in the 3-chapter cycle.
    chapters[20] = 8; // The Haggadah text is combined with chapter 8 of Chametz U'Matzah.
    RambamYomi[] readings =
        new RambamYomi[] {
          chapterAt(d, chapters), chapterAt(d + 1, chapters), chapterAt(d + 2, chapters)
        };
    if ("Leavened and Unleavened Bread".equals(readings[0].getName())
        && "8".equals(readings[0].getStartChapterString())) {
      readings[0] = new RambamYomi(readings[0].getBookNumber(), "8", "9");
    }
    return collapseAdjacent(readings);
  }

  private static int day(JewishCalendar calendar, int cycle) {
    LocalDate date = calendar.getLocalDate();
    if (date.isBefore(START)) {
      throw new IllegalArgumentException(
          date + " is prior to the Daily Rambam cycle that started on " + START);
    }
    return (int) (ChronoUnit.DAYS.between(START, date) % cycle);
  }

  private static RambamYomi chapterAt(int index, int[] chapters) {
    int total = index;
    for (int i = 0; i < chapters.length; i++) {
      if (total < chapters[i]) {
        String chapter =
            i < FIRST_FOUR_SECTIONS.length
                ? FIRST_FOUR_SECTIONS[i][total]
                : String.valueOf(total + 1);
        return new RambamYomi(i, chapter, chapter);
      }
      total -= chapters[i];
    }
    throw new IllegalStateException("Unable to calculate Rambam Yomi");
  }

  private static RambamYomi collapseAdjacent(RambamYomi[] readings) {
    int count = 0;
    int[] books = new int[3];
    String[] starts = new String[3];
    String[] ends = new String[3];
    for (RambamYomi reading : readings) {
      if (count > 0 && books[count - 1] == reading.getBookNumber()) {
        ends[count - 1] = lastChapterInRange(reading.getEndChapterString());
      } else {
        books[count] = reading.getBookNumber();
        starts[count] = firstChapterInRange(reading.getStartChapterString());
        ends[count] = lastChapterInRange(reading.getEndChapterString());
        count++;
      }
    }
    int[] collapsedBooks = new int[count];
    String[] collapsedStarts = new String[count];
    String[] collapsedEnds = new String[count];
    System.arraycopy(books, 0, collapsedBooks, 0, count);
    System.arraycopy(starts, 0, collapsedStarts, 0, count);
    System.arraycopy(ends, 0, collapsedEnds, 0, count);
    return new RambamYomi(collapsedBooks, collapsedStarts, collapsedEnds);
  }

  private static String firstChapterInRange(String chapter) {
    int dash = chapter.indexOf('-');
    return dash < 0 ? chapter : chapter.substring(0, dash);
  }

  private static String lastChapterInRange(String chapter) {
    int dash = chapter.lastIndexOf('-');
    return dash < 0 ? chapter : chapter.substring(dash + 1);
  }
}
