/*
 * Zmanim Java API
 * Copyright (C) 2017 - 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
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
 * This class calculates the <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Talmud Yerusalmi</a> <a href=
 * "https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> page ({@link Daf}) for the a given date.
 * 
 * @author &copy; elihaidv
 * @author &copy; Eliyahu Hershfeld 2017 - 2026
 */
public class YerushalmiYomiCalculator {

    /** Start date of first Daf Yomi Yerushalmi cycle: 1980-02-02 */
    private static final LocalDate DAF_YOMI_START_DAY = LocalDate.of(1980, 2, 2);

    /** number of pages (<em>blatt/dafim</em>) in <em>Shas</em>*/
    private static final int WHOLE_SHAS_DAFS = 1554;
    
    /** number of pages (<em>blatt/dafim</em>) per <em>masechta</em>*/
    private static final int[] BLATT_PER_MASECHTA = {
            68, 37, 34, 44, 31, 59, 26, 33, 28, 20, 13, 92, 65, 71, 22, 22, 42, 26, 26, 33, 34, 22,
            19, 85, 72, 47, 40, 47, 54, 48, 44, 37, 34, 44, 9, 57, 37, 19, 13
    };

    /**
     * Default constructor.
     */
    public YerushalmiYomiCalculator() {
        // No-op
    }

    /**
     * Returns the Daf Yomi Yerushalmi page for a given Jewish date.
     * Returns null on Yom Kippur or Tisha B’Av.
     * @param jewishCalendar the <code>JewishCalendar</code> to set.
     * @return the daf.
     */
    public static Daf getDafYomiYerushalmi(JewishCalendar jewishCalendar) {
        LocalDate requested = jewishCalendar.getLocalDate();
        int masechta = 0;
        Daf dafYomi = null;

        // No Daf Yomi on Yom Kippur or Tisha B'Av
        if (jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_KIPPUR ||
                jewishCalendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV) {
            return null;
        }

        if (requested.isBefore(DAF_YOMI_START_DAY)) {
            throw new IllegalArgumentException(requested + " is prior to the first Daf Yomi cycle starting on "
                    + DAF_YOMI_START_DAY);
        }

        // Initialize cycle dates
        LocalDate prevCycle = DAF_YOMI_START_DAY;
        LocalDate nextCycle = getNextCycleStart(prevCycle);

        // Loop through cycles until we reach the requested date
        while (!requested.isBefore(nextCycle)) {
            prevCycle = nextCycle;
            nextCycle = getNextCycleStart(prevCycle);
        }

        // Days between start of cycle and requested date
        int dafNo = (int) getDiffBetweenDays(prevCycle, requested);

        int specialDays = getNumOfSpecialDays(prevCycle, requested);
        int total = dafNo - specialDays;

        // Find the daf
        for (int blatt : BLATT_PER_MASECHTA) {
            if (total < blatt) {
                dafYomi = new Daf(masechta, total + 1);
                break;
            }
            total -= blatt;
            masechta++;
        }

        return dafYomi;
    }

    /**
     * Returns the start date of the cycle after the one beginning on {@code cycleStart}.
     * The cycle end is inclusive, so skipped days on the tentative end date extend the cycle.
     * @param cycleStart the first day of the current cycle
     * @return the first day of the following cycle
     */
    private static LocalDate getNextCycleStart(LocalDate cycleStart) {
        LocalDate endDate = cycleStart.plusDays(WHOLE_SHAS_DAFS - 1);
        int specialDays = getNumOfSpecialDays(cycleStart, endDate);

        while (specialDays > 0) {
            LocalDate newStart = endDate.plusDays(1);
            endDate = endDate.plusDays(specialDays);
            specialDays = getNumOfSpecialDays(newStart, endDate);
        }

        return endDate.plusDays(1);
    }

    /**
     * Counts the number of "special days" (Yom Kippur, Tisha B’Av) between two dates.
     * @param start the start date for the calculation
     * @param end the end date for the calculation
     * @return the number of special days
     */
    private static int getNumOfSpecialDays(LocalDate start, LocalDate end) {

        int startYear = new JewishCalendar(start).getJewishYear();
        int endYear = new JewishCalendar(end).getJewishYear();

        int specialDays = 0;

        // Loop over each Jewish year in range
        for (int year = startYear; year <= endYear; year++) {
            // Create Yom Kippur and Tisha B’Av for that Jewish year
            JewishCalendar yomKippur = new JewishCalendar(year, JewishCalendar.TISHREI, 10);
            JewishCalendar tishaBeav = new JewishCalendar(year, JewishCalendar.AV, 9);
            // Check if Tisha B'Av is a Nidche.
            if (tishaBeav.getDayOfWeek() == 7) {
                tishaBeav.plusDays(1);
            }
            LocalDate ykDate = yomKippur.getLocalDate();
            LocalDate tbDate = tishaBeav.getLocalDate();

            if (isBetween(start, ykDate, end)) specialDays++;
            if (isBetween(start, tbDate, end)) specialDays++;
        }

        return specialDays;
    }


    /**
     * Checks if a date is after start and on or before end.
     * @param start the start <code>ZonedDateTime</code>
     * @param date the <code>ZonedDateTime</code> to check
     * @param end the end <code>ZonedDateTime</code>
     * @return if the date is between the two dates
     */
    private static boolean isBetween(LocalDate start, LocalDate date, LocalDate end) {
        return start.isBefore(date) && !end.isBefore(date);
    }

    /**
     * Returns the number of full days between two <code>ZonedDateTime</code>s.
     * @param start the start <code>ZonedDateTime</code>
     * @param end the end <code>ZonedDateTime</code>
     * @return the number of days between the dates.
     */
    private static long getDiffBetweenDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
