/*
 * Zmanim Java API
 * Copyright © 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 */
package com.kosherjava.zmanim.hebrewcalendar;

/** Calculates the annual Hebrew-date Kitzur Shulchan Aruch Yomi schedule. */
public class KitzurShulchanAruchYomiCalculator {
  public KitzurShulchanAruchYomiCalculator() {}

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
