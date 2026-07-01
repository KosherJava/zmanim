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

/**
 * Calculates the annual Hebrew-date Kitzur Shulchan Aruch Yomi schedule. The schedule repeats every
 * Hebrew year. It returns {@code null} for dates that do not have a regular table entry, such as 30
 * Cheshvan and 30 Adar I.
 */
public class KitzurShulchanAruchYomiCalculator {
  public KitzurShulchanAruchYomiCalculator() {}

  /**
   * Returns the Kitzur Shulchan Aruch Yomi reading for the date that the calendar is set to.
   *
   * @param calendar the calendar set to the date to calculate
   * @return the Kitzur Shulchan Aruch Yomi reading, or {@code null} on days without a reading
   */
  public static KitzurShulchanAruchYomi getKitzurShulchanAruchYomi(JewishCalendar calendar) {
    int month = calendar.getJewishMonth();
    int day = calendar.getJewishDayOfMonth();
    String[] monthSchedule = getMonthSchedule(calendar, month);
    if (monthSchedule == null || day > monthSchedule.length) {
      return null;
    }
    String reading = monthSchedule[day - 1];
    if (reading == null || reading.length() == 0) {
      return null;
    }
    String[] parts = reading.split("-");
    return new KitzurShulchanAruchYomi(parts[0], parts.length > 1 ? parts[1] : parts[0]);
  }

  private static String[] getMonthSchedule(JewishCalendar calendar, int month) {
    switch (month) {
      case JewishCalendar.NISSAN:
        return LimudYomiData.KITZUR_NISAN;
      case JewishCalendar.IYAR:
        return LimudYomiData.KITZUR_IYYAR;
      case JewishCalendar.SIVAN:
        return LimudYomiData.KITZUR_SIVAN;
      case JewishCalendar.TAMMUZ:
        return LimudYomiData.KITZUR_TAMUZ;
      case JewishCalendar.AV:
        return LimudYomiData.KITZUR_AV;
      case JewishCalendar.ELUL:
        return LimudYomiData.KITZUR_ELUL;
      case JewishCalendar.TISHREI:
        return LimudYomiData.KITZUR_TISHREI;
      case JewishCalendar.CHESHVAN:
        return LimudYomiData.KITZUR_CHESHVAN;
      case JewishCalendar.KISLEV:
        return LimudYomiData.KITZUR_KISLEV;
      case JewishCalendar.TEVES:
        return LimudYomiData.KITZUR_TEVET;
      case JewishCalendar.SHEVAT:
        return LimudYomiData.KITZUR_SH_VAT;
      case JewishCalendar.ADAR:
        return LimudYomiData.KITZUR_ADAR;
      case JewishCalendar.ADAR_II:
        return calendar.isJewishLeapYear() ? LimudYomiData.KITZUR_LEAP_OPTION_A : null;
      default:
        return null;
    }
  }
}
