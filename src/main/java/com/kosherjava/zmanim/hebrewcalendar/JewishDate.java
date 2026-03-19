/*
 * Zmanim Java API
 * Copyright (C) 2011 - 2026 Eliyahu Hershfeld
 * Copyright (C) September 2002 Avrom Finkelstien
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;

/**
 * The JewishDate is the base calendar class, that supports maintenance of a {@link java.util.GregorianCalendar}
 * instance along with the corresponding Jewish date. This class can use the standard Java Date and Calendar
 * classes for setting and maintaining the dates, but it does not subclass these classes or use them internally
 * in any calculations. This class also does not have a concept of a time (which the Date class does). Please
 * note that the calendar does not currently support dates prior to 1/1/1 Gregorian. Also keep in mind that the
 * Gregorian calendar started on October 15, 1582, so any calculations prior to that are suspect (at least from
 * a Gregorian perspective). While 1/1/1 Gregorian and forward are technically supported, any calculations prior to <a
 * href="http://en.wikipedia.org/wiki/Hillel_II">Hillel II's (Hakatan's</a>) calendar (4119 in the Jewish Calendar / 359
 * CE Julian as recorded by <a href="http://en.wikipedia.org/wiki/Hai_Gaon">Rav Hai Gaon</a>) would be just an
 * approximation.
 * 
 * This open source Java code was written by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a> from his C++
 * code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements and some bug
 * fixing.
 * 
 * Some of Avrom's original C++ code was translated from
 * <a href="https://web.archive.org/web/20120124134148/http://emr.cs.uiuc.edu/~reingold/calendar.C">C/C++ code</a> in
 * <a href="http://www.calendarists.com">Calendrical Calculations</a> by Nachum Dershowitz and Edward M.
 * Reingold, Software-- Practice &amp; Experience, vol. 20, no. 9 (September, 1990), pp. 899- 928. Any method with the mark
 * "ND+ER" indicates that the method was taken from this source with minor modifications.
 * 
 * If you are looking for a class that implements a Jewish calendar version of the Calendar class, one is available from
 * the <a href="http://site.icu-project.org/" >ICU (International Components for Unicode)</a> project, formerly part of
 * IBM's DeveloperWorks.
 * 
 * @see JewishCalendar
 * @see HebrewDateFormatter
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011 - 2026
 */
public class JewishDate implements Comparable<JewishDate>, Cloneable {
	/**
	 * Value of the month field indicating Nissan, the first numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 7th (or 8th in a {@link #isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int NISSAN = 1;

	/**
	 * Value of the month field indicating Iyar, the second numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 8th (or 9th in a {@link #isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int IYAR = 2;

	/**
	 * Value of the month field indicating Sivan, the third numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 9th (or 10th in a {@link #isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int SIVAN = 3;

	/**
	 * Value of the month field indicating Tammuz, the fourth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 10th (or 11th in a {@link #isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int TAMMUZ = 4;

	/**
	 * Value of the month field indicating Av, the fifth numeric month of the year in the Jewish calendar. With the year
	 * starting at {@link #TISHREI}, it would actually be the 11th (or 12th in a {@link #isJewishLeapYear() leap year})
	 * month of the year.
	 */
	public static final int AV = 5;

	/**
	 * Value of the month field indicating Elul, the sixth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 12th (or 13th in a {@link #isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int ELUL = 6;

	/**
	 * Value of the month field indicating Tishrei, the seventh numeric month of the year in the Jewish calendar. With
	 * the year starting at this month, it would actually be the 1st month of the year.
	 */
	public static final int TISHREI = 7;

	/**
	 * Value of the month field indicating Cheshvan/marcheshvan, the eighth numeric month of the year in the Jewish
	 * calendar. With the year starting at {@link #TISHREI}, it would actually be the 2nd month of the year.
	 */
	public static final int CHESHVAN = 8;

	/**
	 * Value of the month field indicating Kislev, the ninth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 3rd month of the year.
	 */
	public static final int KISLEV = 9;

	/**
	 * Value of the month field indicating Teves, the tenth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 4th month of the year.
	 */
	public static final int TEVES = 10;

	/**
	 * Value of the month field indicating Shevat, the eleventh numeric month of the year in the Jewish calendar. With
	 * the year starting at {@link #TISHREI}, it would actually be the 5th month of the year.
	 */
	public static final int SHEVAT = 11;

	/**
	 * Value of the month field indicating Adar (or Adar I in a {@link #isJewishLeapYear() leap year}), the twelfth
	 * numeric month of the year in the Jewish calendar. With the year starting at {@link #TISHREI}, it would actually
	 * be the 6th month of the year.
	 */
	public static final int ADAR = 12;

	/**
	 * Value of the month field indicating Adar II, the leap (intercalary or embolismic) thirteenth (Undecimber) numeric
	 * month of the year added in Jewish {@link #isJewishLeapYear() leap year}). The leap years are years 3, 6, 8, 11,
	 * 14, 17 and 19 of a 19-year cycle. With the year starting at {@link #TISHREI}, it would actually be the 7th month
	 * of the year.
	 */
	public static final int ADAR_II = 13;

	/**
	 * the Jewish epoch using the RD (Rata Die/Fixed Date or Reingold Dershowitz) day used in Calendrical Calculations.
	 * Day 1 is January 1, 0001 of the Gregorian calendar
	 */
	private static final int JEWISH_EPOCH = -1373429;

	/** The number  of <em>chalakim</em> (18) in a minute.*/
	private static final int CHALAKIM_PER_MINUTE = 18;
	/** The number  of <em>chalakim</em> (1080) in an hour.*/
	private static final int CHALAKIM_PER_HOUR = 1080;
	/** The number of <em>chalakim</em> (25,920) in a 24-hour day .*/
	private static final int CHALAKIM_PER_DAY = 25920; // 24 * 1080
	/** The number  of <em>chalakim</em> in an average Jewish month. A month has 29 days, 12 hours and 793
	 * <em>chalakim</em> (44 minutes and 3.3 seconds) for a total of 765,433 <em>chalakim</em>*/
	private static final long CHALAKIM_PER_MONTH = 765433; // (29 * 24 + 12) * 1080 + 793
	/**
	 * Days from the beginning of Sunday till <em>molad BaHaRaD</em>. Calculated as 1 day, 5 hours and 204 <em>chalakim</em> =
	 * (24 + 5) * 1080 + 204 = 31524
	 */
	private static final int CHALAKIM_MOLAD_TOHU = 31524;

	/**
	 * A short year where both {@link #CHESHVAN} and {@link #KISLEV} are 29 days.
	 * 
	 * @see #getCheshvanKislevKviah()
	 * @see HebrewDateFormatter#getFormattedKviah(int)
	 */
	public static final int CHASERIM = 0;

	/**
	 * An ordered year where {@link #CHESHVAN} is 29 days and {@link #KISLEV} is 30 days.
	 * 
	 * @see #getCheshvanKislevKviah()
	 * @see HebrewDateFormatter#getFormattedKviah(int)
	 */
	public static final int KESIDRAN = 1;

	/**
	 * A long year where both {@link #CHESHVAN} and {@link #KISLEV} are 30 days.
	 * 
	 * @see #getCheshvanKislevKviah()
	 * @see HebrewDateFormatter#getFormattedKviah(int)
	 */
	public static final int SHELAIMIM = 2;

	/** the internal Jewish month.*/
	private int jewishMonth;
	/** the internal Jewish day.*/
	private int jewishDay;
	/** the internal Jewish year.*/
	private int jewishYear;
	/** the internal count of <em>molad</em> hours.*/
	private int moladHours;
	/** the internal count of <em>molad</em> minutes.*/
	private int moladMinutes;
	/** the internal count of <em>molad chalakim</em>.*/
	private int moladChalakim;

	/**
	 * Returns the <em>molad</em> hours. Only a JewishDate object populated with {@link #getMolad()},
	 * {@link #setJewishDate(int, int, int, int, int, int)} or {@link #setMoladHours(int)} will have this field
	 * populated. A regular JewishDate object will have this field set to 0.
	 * 
	 * @return the <em>molad</em> hours
	 * @see #setMoladHours(int)
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 */
	public int getMoladHours() {
		return moladHours;
	}

	/**
	 * Sets the <em>molad</em> hours.
	 * 
	 * @param moladHours
	 *            the <em>molad</em> hours to set
	 * @see #getMoladHours()
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 * 
	 */
	public void setMoladHours(int moladHours) {
		this.moladHours = moladHours;
	}

	/**
	 * Returns the <em>molad</em> minutes. Only an object populated with {@link #getMolad()},
	 * {@link #setJewishDate(int, int, int, int, int, int)} or or {@link #setMoladMinutes(int)} will have these fields
	 * populated. A regular JewishDate object will have this field set to 0.
	 * 
	 * @return the <em>molad</em> minutes
	 * @see #setMoladMinutes(int)
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 */
	public int getMoladMinutes() {
		return moladMinutes;
	}

	/**
	 * Sets the <em>molad</em> minutes. The expectation is that the traditional minute-less <em>chalakim</em> will be broken out to
	 * minutes and {@link #setMoladChalakim(int) <em>chalakim</em> / parts} , so 793 (TaShTZaG) parts would have the minutes set to
	 * 44 and <em>chalakim</em> to 1.
	 * 
	 * @param moladMinutes
	 *            the molad minutes to set
	 * @see #getMoladMinutes()
	 * @see #setMoladChalakim(int)
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 * 
	 */
	public void setMoladMinutes(int moladMinutes) {
		this.moladMinutes = moladMinutes;
	}

	/**
	 * Sets the <em>molad</em> chalakim/parts. The expectation is that the traditional minute-less <em>chalakim</em> will be broken
	 * out to {@link #setMoladMinutes(int) minutes} and chalakim, so 793 (TaShTZaG) parts would have the minutes set to 44 and
	 * <em>chalakim</em> to 1.
	 * 
	 * @param moladChalakim
	 *            the <em>molad chalakim</em> / parts to set
	 * @see #getMoladChalakim()
	 * @see #setMoladMinutes(int)
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 * 
	 */
	public void setMoladChalakim(int moladChalakim) {
		this.moladChalakim = moladChalakim;
	}

	/**
	 * Returns the <em>molad chalakim</em> / parts. Only an object populated with {@link #getMolad()},
	 * {@link #setJewishDate(int, int, int, int, int, int)} or or {@link #setMoladChalakim(int)} will have these fields
	 * populated. A regular JewishDate object will have this field set to 0.
	 * 
	 * @return the <em>molad chalakim</em> / parts
	 * @see #setMoladChalakim(int)
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 */
	public int getMoladChalakim() {
		return moladChalakim;
	}

	/** 1 == Sunday, 2 == Monday, etc... */
	private int dayOfWeek;

	/** Returns the absolute date (days since January 1, 0001 of the Gregorian calendar).
	 * @see #getAbsDate()
	 * @see #setJewishDateFromAbsDate()
	 */
	private int gregorianAbsDate;

	/**
	 * Returns the number of days in a given month in a given month and year.
	 *
	 * @param month
	 *            the month. As with other cases in this class, this is 1-based, not zero-based.
	 * @param year
	 *            the year (only impacts February)
	 * @return the number of days in the month in the given year
	 */
	private static int getLastDayOfGregorianMonth(int month, int year) {
        return YearMonth.of(year, month).lengthOfMonth();
	}

	/**
	 * Computes the Gregorian date from the absolute date. ND+ER
	 * @param absDate the absolute date
	 */
	private static LocalDate absDateToDate(int absDate) {
		int year = absDate / 366; // Search forward year by year from approximate year
		while (absDate >= gregorianDateToAbsDate(year + 1, 1, 1)) {
			year++;
		}

		int month = 1; // Search forward month by month from January
		while (absDate > gregorianDateToAbsDate(year, month, getLastDayOfGregorianMonth(month, year))) {
			month++;
		}

		int dayOfMonth = absDate - gregorianDateToAbsDate(year, month, 1) + 1;
        return LocalDate.of(year, month, dayOfMonth);
	}

	/**
	 * Returns the absolute date (days since January 1, 0001 of the Gregorian calendar).
	 * 
	 * @return the number of days since January 1, 1
	 */
	public int getAbsDate() {
		return gregorianAbsDate;
	}

	/**
	 * Computes the absolute date from a Gregorian date. ND+ER
	 * 
	 * @param year
	 *            the Gregorian year
	 * @param month
	 *            the Gregorian month. Unlike the Java Calendar where January has the value of 0,This expects a 1 for
	 *            January
	 * @param dayOfMonth
	 *            the day of the month (1st, 2nd, etc...)
	 * @return the absolute Gregorian day
	 */
	private static int gregorianDateToAbsDate(int year, int month, int dayOfMonth) {
		int absDate = dayOfMonth;
		for (int m = month - 1; m > 0; m--) {
			absDate += getLastDayOfGregorianMonth(m, year); // days in prior months of the year
		}
		return (absDate // days this year
				+ 365 * (year - 1) // days in previous years ignoring leap days
				+ (year - 1) / 4 // Julian leap days before this year
				- (year - 1) / 100 // minus prior century years
		+ (year - 1) / 400); // plus prior years divisible by 400
	}

	/**
	 * Returns if the year is a Jewish leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19-year cycle are leap years.
	 * 
	 * @param year
	 *            the Jewish year.
	 * @return true if it is a leap year
	 * @see #isJewishLeapYear()
	 */
	private static boolean isJewishLeapYear(int year) {
		return ((7 * year) + 1) % 19 < 7;
	}

	/**
	 * Returns if the year the calendar is set to is a Jewish leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19-year
	 * cycle are leap years.
	 * 
	 * @return true if it is a leap year
	 * @see #isJewishLeapYear(int)
	 */
	public boolean isJewishLeapYear() {
		return isJewishLeapYear(getJewishYear());
	}

	/**
	 * Returns the last month of a given Jewish year. This will be 12 on a non {@link #isJewishLeapYear(int) leap year}
	 * or 13 on a leap year.
	 * 
	 * @param year
	 *            the Jewish year.
	 * @return 12 on a non leap year or 13 on a leap year
	 * @see #isJewishLeapYear(int)
	 */
	private static int getLastMonthOfJewishYear(int year) {
		return isJewishLeapYear(year) ? ADAR_II : ADAR;
	}

	/**
	 * Returns the number of days elapsed from the Sunday prior to the start of the Jewish calendar to the mean
	 * conjunction of Tishri of the Jewish year.
	 * 
	 * @param year
	 *            the Jewish year
	 * @return the number of days elapsed from prior to the <em>molad Tohu BaHaRaD</em> (Be = Monday, <em>Ha</em> = 5
	 *         hours and <em>RaD</em> = 204 <em>chalakim</em> / parts) prior to the start of the Jewish calendar, to
	 *         the mean conjunction of Tishri of the Jewish year. BeHaRaD is 23:11:20 on Sunday night(5 hours 204/1080
	 *         <em>chalakim</em> after sunset on Sunday evening).
	 */
	public static int getJewishCalendarElapsedDays(int year) {
		long chalakimSince = getChalakimSinceMoladTohu(year, TISHREI);
		int moladDay = (int) (chalakimSince / (long) CHALAKIM_PER_DAY);
		int moladParts = (int) (chalakimSince - moladDay * (long) CHALAKIM_PER_DAY);
		// delay Rosh Hashana for the 4 dechiyos
		return addDechiyos(year, moladDay, moladParts);
	}

	/**
	 * Adds the 4 dechiyos for <em>molad</em> Tishrei. These are:
	 * <ol>
	 * <li><em>Lo ADU Rosh</em> - Rosh Hashana can't fall on a Sunday, Wednesday or Friday. If the <em>molad</em> fell on one
	 * of these days, Rosh Hashana is delayed to the following day.</li>
	 * <li><em>Molad Zaken</em> - If the <em>molad</em> of Tishrei falls after 12 noon, Rosh Hashana is delayed to the following
	 * day. If the following day is <em>ADU</em>, it will be delayed an additional day.</li>
	 * <li><em>GaTRaD</em> - If on a non leap year the <em>molad</em> of Tishrei falls on a Tuesday (Ga) on or after 9 hours
	 * (<em>T</em>) and (<em>RaD</em> 204 <em>chalakim</em> it is delayed till Thursday (one day delay, plus one day for
	 * <em>Lo ADU Rosh</em>)</li>
	 * <li><em>BeTuTaKPaT</em> - if the year following a leap year falls on a Monday (<em>Be</em>) on or after 15 hours
	 * (<em>Tu</em>) and 589 <em>chalakim</em> (<em>TaKPaT</em>) it is delayed till Tuesday</li>
	 * </ol>
	 * 
	 * @param year the year
	 * @param moladDay the <em>molad</em> day
	 * @param moladParts the <em>molad</em> parts
	 * @return the number of elapsed days in the JewishCalendar adjusted for the 4 dechiyos.
	 */
	private static int addDechiyos(int year, int moladDay, int moladParts) {
		int roshHashanaDay = moladDay; // if no dechiyos
		// delay Rosh Hashana for the dechiyos of the Molad - new moon 1 - Molad Zaken, 2- GaTRaD 3- BeTuTaKPaT
		if ((moladParts >= 19440) // Dechiya of Molad Zaken - molad is >= midday (18 hours * 1080 chalakim)
				|| (((moladDay % 7) == 2) // start Dechiya of GaTRaD - Ga = is a Tuesday
						&& (moladParts >= 9924) // TRaD = 9 hours, 204 parts or later (9 * 1080 + 204)
				&& !isJewishLeapYear(year)) // of a non-leap year - end Dechiya of GaTRaD
				|| (((moladDay % 7) == 1) // start Dechiya of BeTuTaKPaT - Be = is on a Monday
						&& (moladParts >= 16789) // TUTaKPaT part of BeTuTaKPaT = 15 hours, 589 parts or later (15 * 1080 + 589)
				&& (isJewishLeapYear(year - 1)))) { // in a year following a leap year - end Dechiya of BeTuTaKPaT
			roshHashanaDay += 1; // Then postpone Rosh HaShanah one day
		}
		// start 4th Dechiya - Lo ADU Rosh - Rosh Hashana can't occur on A- sunday, D- Wednesday, U - Friday
		if (((roshHashanaDay % 7) == 0)// If Rosh HaShanah would occur on Sunday,
				|| ((roshHashanaDay % 7) == 3) // or Wednesday,
				|| ((roshHashanaDay % 7) == 5)) { // or Friday - end 4th Dechiya - Lo ADU Rosh
			roshHashanaDay = roshHashanaDay + 1; // Then postpone it one (more) day
		}
		return roshHashanaDay;
	}

	/**
	 * Returns the number of <em>chalakim</em> (parts - 1080 to the hour) from the original hypothetical <em>Molad Tohu</em>
	 * to the year and month passed in.
	 * 
	 * @param year
	 *            the Jewish year
	 * @param month
	 *            the Jewish month the Jewish month, with the month numbers starting from Nissan. Use the JewishDate
	 *            constants such as {@link JewishDate#TISHREI}.
	 * @return the number of <em>chalakim</em> (parts - 1080 to the hour) from the original hypothetical <em>Molad Tohu</em>
	 */
	private static long getChalakimSinceMoladTohu(int year, int month) {
		// Jewish lunar month = 29 days, 12 hours and 793 chalakim
		// chalakim since Molad Tohu BeHaRaD - 1 day, 5 hours and 204 chalakim
		int monthOfYear = getJewishMonthOfYear(year, month);
		int monthsElapsed = (235 * ((year - 1) / 19)) // Months in complete 19-year lunar (Metonic) cycles so far
				+ (12 * ((year - 1) % 19)) // Regular months in this cycle
				+ ((7 * ((year - 1) % 19) + 1) / 19) // Leap months this cycle
				+ (monthOfYear - 1); // add elapsed months till the start of the molad of the month
		// return chalakim prior to BeHaRaD + number of chalakim since
		return CHALAKIM_MOLAD_TOHU + (CHALAKIM_PER_MONTH * monthsElapsed);
	}

	/**
	 * Returns the number of <em>chalakim</em> (parts - 1080 to the hour) from the original hypothetical <em>Molad Tohu</em>
	 * to the Jewish year and month that this Object is set to.
	 * 
	 * @return the number of <em>chalakim</em> (parts - 1080 to the hour) from the original hypothetical <em>Molad Tohu</em> 
	 */
	public long getChalakimSinceMoladTohu() {
		return getChalakimSinceMoladTohu(jewishYear, jewishMonth);
	}

	/**
	 * Converts the {@link JewishDate#NISSAN} based constants used by this class to numeric month starting from
	 * {@link JewishDate#TISHREI}. This is required for <em>molad</em> calculations.
	 * 
	 * @param year
	 *            The Jewish year
	 * @param month
	 *            The Jewish Month
	 * @return the Jewish month of the year starting with Tishrei
	 */
	private static int getJewishMonthOfYear(int year, int month) {
		boolean isLeapYear = isJewishLeapYear(year);
		return (month + (isLeapYear ? 6 : 5)) % (isLeapYear ? 13 : 12) + 1;
	}

	/**
	 * Validates the components of a Jewish date for validity. It will throw an {@link IllegalArgumentException} if the Jewish
	 * date is earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month &lt; 1 or &gt; 12 (or 13 on a {@link #isJewishLeapYear(int)
	 * leap year}), the day of month is &lt; 1 or &gt; 30, an hour &lt; 0 or &gt; 23, a minute &lt; 0 or &gt; 59 or
	 * <em>chalakim</em> &lt; 0 or &gt; 17. For larger a larger number of <em>chalakim</em> such as 793 (TaShTzaG) break the
	 * <em>chalakim</em> into minutes (18 <em>chalakim</em> per minutes, so it would be 44 minutes and 1 <em>chelek</em> in the
	 * case of 793 / <em>TaShTzaG</em>).
	 * 
	 * @param year
	 *            the Jewish year to validate. It will reject any year &lt;= 3761 (lower than the year 1 Gregorian).
	 * @param month
	 *            the Jewish month to validate. It will reject a month &lt; 1 or &gt; 12 (or 13 on a leap year) .
	 * @param dayOfMonth
	 *            the day of the Jewish month to validate. It will reject any value &lt; 1 or &gt; 30 TODO: check calling
	 *            methods to see if there is any reason that the class can validate that 30 is invalid for some months.
	 * @param hours
	 *            the hours (for <em>molad</em> calculations). It will reject an hour &lt; 0 or &gt; 23
	 * @param minutes
	 *            the minutes (for <em>molad</em> calculations). It will reject a minute &lt; 0 or &gt; 59
	 * @param chalakim
	 *            the <em>chalakim</em> / parts (for <em>molad</em> calculations). It will reject a <em>chalakim</em> &lt; 0 or &gt;
	 *            17. For larger numbers such as 793 (<em>TaShTzaG</em>) break the <em>chalakim</em> into minutes (18 <em>chalakim</em>
	 *            per minutes, so it would be 44  minutes and 1 <em>chelek</em> in the case of 793 / <em>TaShTzaG</em>)
	 * 
	 * @throws IllegalArgumentException
	 *             if a Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month &lt; 1 or &gt; 12 (or 13 on a leap year),
	 *             the day of month is &lt; 1 or &gt; 30, an hour &lt; 0 or &gt; 23, a minute &lt; 0 or &gt; 59 or <em>chalakim</em>
	 *             &lt; 0 or &gt; 17. For larger a larger number of <em>chalakim</em> such as 793 (<em>TaShTzaG</em>) break the
	 *             <em>chalakim</em> into minutes (18 <em>chalakim</em> per minutes, so it would be 44 minutes and 1 <em>chelek</em>
	 *             in the case of 793 (<em>TaShTzaG</em>).
	 */
	private static void validateJewishDate(int year, int month, int dayOfMonth, int hours, int minutes, int chalakim) {
		if (month < NISSAN || month > getLastMonthOfJewishYear(year)) {
			throw new IllegalArgumentException("The Jewish month has to be between 1 and 12 (or 13 on a leap year). "
					+ month + " is invalid for the year " + year + ".");
		}
        int monthLength = getDaysInJewishMonth(month, year);
		if (dayOfMonth < 1 || dayOfMonth > monthLength) {
			throw new IllegalArgumentException(
                    "The Jewish day of month can't be < 1 or > " + monthLength +
                            " for the month index " + monthLength + ".  " + dayOfMonth + " is invalid.");
		}
		// reject dates prior to 18 Teves, 3761 (1/1/1 AD). This restriction can be relaxed if the date coding is
		// changed/corrected
		if ((year < 3761) || (year == 3761 && (month >= TISHREI && month < TEVES))
				|| (year == 3761 && month == TEVES && dayOfMonth < 18)) {
			throw new IllegalArgumentException(
					"A Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian) can't be set. " + year + ", " + month
							+ ", " + dayOfMonth + " is invalid.");
		}
		if (hours < 0 || hours > 23) {
			throw new IllegalArgumentException("Hours < 0 or > 23 can't be set. " + hours + " is invalid.");
		}

		if (minutes < 0 || minutes > 59) {
			throw new IllegalArgumentException("Minutes < 0 or > 59 can't be set. " + minutes + " is invalid.");
		}

		if (chalakim < 0 || chalakim > 17) {
			throw new IllegalArgumentException(
					"Chalakim/parts < 0 or > 17 can't be set. "
							+ chalakim
							+ " is invalid. For larger numbers such as 793 (TaShTzaG) break the <em>chalakim</em> into minutes (18 chalakim per minutes, so it would be 44 minutes and 1 chelek in the case of 793 (TaShTzaG)");
		}
	}

	/**
	 * Returns the number of days for a given Jewish year. ND+ER
	 * 
	 * @param year
	 *            the Jewish year
	 * @return the number of days for a given Jewish year.
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 */
	public static int getDaysInJewishYear(int year) {
		return getJewishCalendarElapsedDays(year + 1) - getJewishCalendarElapsedDays(year);
	}

	/**
	 * Returns the number of days for the current year that the calendar is set to.
	 * 
	 * @return the number of days for the Object's current Jewish year.
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 * @see #isJewishLeapYear()
	 */
	public int getDaysInJewishYear() {
		return getDaysInJewishYear(getJewishYear());
	}

	/**
	 * Returns if Cheshvan is long in a given Jewish year. The method name isLong is done since in a Kesidran (ordered)
	 * year Cheshvan is short. ND+ER
	 * 
	 * @param year
	 *            the year
	 * @return true if Cheshvan is long in Jewish year.
	 * @see #isCheshvanLong()
	 * @see #getCheshvanKislevKviah()
	 */
	private static boolean isCheshvanLong(int year) {
		return getDaysInJewishYear(year) % 10 == 5;
	}

	/**
	 * Returns if Cheshvan is long (30 days VS 29 days) for the current year that the calendar is set to. The method
	 * name isLong is done since in a Kesidran (ordered) year Cheshvan is short.
	 * 
	 * @return true if Cheshvan is long for the current year that the calendar is set to
	 * @see #isCheshvanLong()
	 */
	public boolean isCheshvanLong() {
		return isCheshvanLong(getJewishYear());
	}

	/**
	 * Returns if Kislev is short (29 days VS 30 days) in a given Jewish year. The method name isShort is done since in
	 * a Kesidran (ordered) year Kislev is long. ND+ER
	 * 
	 * @param year
	 *            the Jewish year
	 * @return true if Kislev is short for the given Jewish year.
	 * @see #isKislevShort()
	 * @see #getCheshvanKislevKviah()
	 */
	private static boolean isKislevShort(int year) {
		return getDaysInJewishYear(year) % 10 == 3;
	}

	/**
	 * Returns if the Kislev is short for the year that this class is set to. The method name isShort is done since in a
	 * Kesidran (ordered) year Kislev is long.
	 * 
	 * @return true if Kislev is short for the year that this class is set to
	 */
	public boolean isKislevShort() {
		return isKislevShort(getJewishYear());
	}

	/**
	 * Returns the Cheshvan and Kislev kviah (whether a Jewish year is short, regular or long). It will return
	 * {@link #SHELAIMIM} if both cheshvan and kislev are 30 days, {@link #KESIDRAN} if Cheshvan is 29 days and Kislev
	 * is 30 days and {@link #CHASERIM} if both are 29 days.
	 * 
	 * @return {@link #SHELAIMIM} if both cheshvan and kislev are 30 days, {@link #KESIDRAN} if Cheshvan is 29 days and
	 *         Kislev is 30 days and {@link #CHASERIM} if both are 29 days.
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 */
	public int getCheshvanKislevKviah() {
		if (isCheshvanLong() && !isKislevShort()) {
			return SHELAIMIM;
		} else if (!isCheshvanLong() && isKislevShort()) {
			return CHASERIM;
		} else {
			return KESIDRAN;
		}
	}

	/**
	 * Returns the number of days of a Jewish month for a given month and year.
	 * 
	 * @param month
	 *            the Jewish month
	 * @param year
	 *            the Jewish Year
	 * @return the number of days for a given Jewish month
	 */
	private static int getDaysInJewishMonth(int month, int year) {
		if ((month == IYAR) || (month == TAMMUZ) || (month == ELUL) || ((month == CHESHVAN) && !(isCheshvanLong(year)))
				|| ((month == KISLEV) && isKislevShort(year)) || (month == TEVES)
				|| ((month == ADAR) && !(isJewishLeapYear(year))) || (month == ADAR_II)) {
			return 29;
		} else {
			return 30;
		}
	}

	/**
	 * Returns the number of days of the Jewish month that the calendar is currently set to.
	 * 
	 * @return the number of days for the Jewish month that the calendar is currently set to.
	 */
	public int getDaysInJewishMonth() {
		return getDaysInJewishMonth(getJewishMonth(), getJewishYear());
	}

	/**
     * Computes and sets the Jewish date fields based on the provided absolute (Gregorian) date.
	 */
	private void setAbsDate(int gregorianAbsDate) {
        if (gregorianAbsDate <= 0) {
            throw new IllegalArgumentException("Dates in the BC era are not supported");
        }
        this.gregorianAbsDate = gregorianAbsDate;
		// Approximation from below
		jewishYear = (gregorianAbsDate - JEWISH_EPOCH) / 366;
		// Search forward for year from the approximation
		while (gregorianAbsDate >= jewishDateToAbsDate(jewishYear + 1, TISHREI, 1)) {
			jewishYear++;
		}
		// Search forward for month from either Tishri or Nissan.
		if (gregorianAbsDate < jewishDateToAbsDate(jewishYear, NISSAN, 1)) {
			jewishMonth = TISHREI;// Start at Tishri
		} else {
			jewishMonth = NISSAN;// Start at Nissan
		}
		while (gregorianAbsDate > jewishDateToAbsDate(jewishYear, jewishMonth, getDaysInJewishMonth())) {
			jewishMonth++;
		}
		// Calculate the day by subtraction
		jewishDay = gregorianAbsDate - jewishDateToAbsDate(jewishYear, jewishMonth, 1) + 1;

        // day of week (same calculation as original)
        dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;

        // Set the molad fields to 0
        moladHours = 0;
        moladMinutes = 0;
        moladChalakim = 0;
	}

	/**
	 * Returns the absolute date of Jewish date. ND+ER
	 * 
	 * @param year
	 *            the Jewish year. The year can't be negative
	 * @param month
	 *            the Jewish month starting with Nissan. Nissan expects a value of 1 etc. until Adar with a value of 12.
	 *            For a leap year, 13 will be the expected value for Adar II. Use the constants {@link JewishDate#NISSAN}
	 *            etc.
	 * @param dayOfMonth
	 *            the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that only
	 *            has 29 days, the day will be set as 29.
	 * @return the absolute date of the Jewish date.
	 */
	private static int jewishDateToAbsDate(int year, int month, int dayOfMonth) {
		int elapsed = getDaysSinceStartOfJewishYear(year, month, dayOfMonth);
		// add elapsed days this year + Days in prior years + Days elapsed before absolute year 1
		return elapsed + getJewishCalendarElapsedDays(year) + JEWISH_EPOCH;
	}

	/**
	 * Returns the <em>molad</em> for a given year and month. Returns a JewishDate {@link Object} set to the date of the <em>molad</em>
	 * with the {@link #getMoladHours() hours}, {@link #getMoladMinutes() minutes} and {@link #getMoladChalakim()
	 * chalakim} set. In the current implementation, it sets the <em>molad</em> time based on a midnight date rollover. This
	 * means that Rosh Chodesh Adar II, 5771 with a <em>molad</em> of 7 <em>chalakim</em> past midnight on Shabbos 29 Adar I / March 5,
	 * 2011 12:00 AM and 7 chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
	 * 
	 * @return a JewishDate {@link Object} set to the date of the <em>molad</em> with the {@link #getMoladHours() hours},
	 *         {@link #getMoladMinutes() minutes} and {@link #getMoladChalakim() <em>chalakim</em>} set.
	 */
	public JewishDate getMolad() {
		JewishDate moladDate = new JewishDate(getChalakimSinceMoladTohu());
		if (moladDate.getMoladHours() >= 6) {
            moladDate.addDays(1);
		}
		moladDate.setMoladHours((moladDate.getMoladHours() + 18) % 24);
		return moladDate;
	}

	/**
	 * Returns the number of days from the Jewish epoch from the number of <em>chalakim</em> from the epoch passed in.
	 * 
	 * @param chalakim
	 *            the number of <em>chalakim</em> since the beginning of Sunday prior to BaHaRaD
	 * @return the number of days from the Jewish epoch
	 */
	private static int moladToAbsDate(long chalakim) {
		return (int) (chalakim / CHALAKIM_PER_DAY) + JEWISH_EPOCH;
	}

	/**
	 * Constructor that creates a JewishDate based on a <em>molad</em> passed in. The <em>molad</em> would be the number of
	 * <em>chalakim</em> / parts starting at the beginning of Sunday prior to the <em>Molad Tohu BeHaRaD</em> (<em>Be</em> =
	 * Monday, <em>Ha</em> = 5 hours and <em>RaD</em> = 204 <em>chalakim</em> / parts) - prior to the start of the Jewish
	 * calendar. <em>BeHaRaD</em> is 23:11:20 on Sunday night(5 hours 204/1080 <em>chalakim</em> after sunset on Sunday evening).
	 * 
	 * @param molad the number of <em>chalakim</em> since the beginning of Sunday prior to BaHaRaD
	 */
	public JewishDate(long molad) {
        setAbsDate(moladToAbsDate(molad));
		int conjunctionDay = (int) (molad / (long) CHALAKIM_PER_DAY);
		int chalakim = (int) (molad - conjunctionDay * (long) CHALAKIM_PER_DAY);
        setMoladHours(chalakim / CHALAKIM_PER_HOUR);
        chalakim = chalakim - (getMoladHours() * CHALAKIM_PER_HOUR);
        setMoladMinutes(chalakim / CHALAKIM_PER_MINUTE);
        setMoladChalakim(chalakim - moladMinutes * CHALAKIM_PER_MINUTE);
	}

	/**
	 * returns the number of days from Rosh Hashana of the date passed in, to the full date passed in.
	 * 
	 * @param year
	 *            the Jewish year
	 * @param month
	 *            the Jewish month
	 * @param dayOfMonth
	 *            the day in the Jewish month
	 * @return the number of days
	 */
	private static int getDaysSinceStartOfJewishYear(int year, int month, int dayOfMonth) {
		int elapsedDays = dayOfMonth;
		// Before Tishrei (from Nissan to Tishrei), add days in prior months
		if (month < TISHREI) {
			// this year before and after Nissan.
			for (int m = TISHREI; m <= getLastMonthOfJewishYear(year); m++) {
				elapsedDays += getDaysInJewishMonth(m, year);
			}
			for (int m = NISSAN; m < month; m++) {
				elapsedDays += getDaysInJewishMonth(m, year);
			}
		} else { // Add days in prior months this year
			for (int m = TISHREI; m < month; m++) {
				elapsedDays += getDaysInJewishMonth(m, year);
			}
		}
		return elapsedDays;
	}

	/**
	 * returns the number of days from Rosh Hashana of the date passed in, to the full date passed in.
	 * 
	 * @return the number of days
	 */
	public int getDaysSinceStartOfJewishYear() {
		return getDaysSinceStartOfJewishYear(getJewishYear(), getJewishMonth(), getJewishDayOfMonth());
	}

	/**
	 * Creates a Jewish date based on a Jewish year, month and day of month.
	 * 
	 * @param jewishYear
	 *            the Jewish year
	 * @param jewishMonth
	 *            the Jewish month. The method expects a 1 for Nissan ... 12 for Adar and 13 for Adar II. Use the
	 *            constants {@link #NISSAN} ... {@link #ADAR} (or {@link #ADAR_II} for a leap year Adar II) to avoid any
	 *            confusion.
	 * @param jewishDayOfMonth
	 *            the Jewish day of month. If 30 is passed in for a month with only 29 days (for example {@link #IYAR},
	 *            or {@link #KISLEV} in a year that {@link #isKislevShort()}), the 29th (last valid date of the month)
	 *            will be set
	 * @throws IllegalArgumentException
	 *             if the day of month is &lt; 1 or &gt; 30, or a year of &lt; 0 is passed in.
	 */
	public JewishDate(int jewishYear, int jewishMonth, int jewishDayOfMonth) {
		setJewishDate(jewishYear, jewishMonth, jewishDayOfMonth);
	}

	/**
	 * Default constructor will set a default date to the current system date.
	 */
	public JewishDate() {
		resetDate();
	}

	/**
	 * A constructor that initializes the date to the {@link java.util.Calendar Calendar} parameter.
	 * 
	 * @param zonedDateTime
	 *            the <code>ZonedDateTime</code> to set the calendar to
	 * @throws IllegalArgumentException
	 *             if the {@link Calendar#ERA} is {@link GregorianCalendar#BC}
	 */
	public JewishDate(ZonedDateTime zonedDateTime) {
		setGregorianDate(zonedDateTime);
	}

	/**
	 * A constructor that initializes the date to the {@link java.time.LocalDate LocalDate} parameter.
	 *
	 * @param localDate
	 *            the <code>LocalDate</code> to set the calendar to
	 * @throws IllegalArgumentException
	 *            if the {@link Calendar#ERA} is {@link GregorianCalendar#BC}
	 */
	public JewishDate(LocalDate localDate) {
		setGregorianDate(localDate);
	}

	/**
	 * Sets the date based on a {@link java.util.Calendar Calendar} object. Modifies the Jewish date as well.
	 * 
	 * @param zonedDateTime
	 *            the <code>ZonedDateTime</code> to set the calendar to
	 * @throws IllegalArgumentException
	 *             if the {@link Calendar#ERA} is {@link GregorianCalendar#BC}
	 */
	public void setGregorianDate(ZonedDateTime zonedDateTime) {
		setGregorianDate(zonedDateTime.toLocalDate());
	}

	/**
	 * Sets the date based on a {@link java.time.LocalDate LocalDate} object. Modifies the Jewish date as well.
	 *
	 * @param localDate
	 *            the <code>LocalDate</code> to set the calendar to
	 * @throws IllegalArgumentException
	 *             if the date would fall prior to the year 1 AD
	 */
	public void setGregorianDate(LocalDate localDate) {
        int absDate = gregorianDateToAbsDate(localDate.getYear(),  localDate.getMonth().getValue(), localDate.getDayOfMonth()); // FIXME + 1;// 1 = January

	    // convert to Jewish date
	    setAbsDate(absDate);
	}

	/**
	 * Sets the Jewish Date and updates the Gregorian date accordingly.
	 * 
	 * @param year
	 *            the Jewish year. The year can't be negative
	 * @param month
	 *            the Jewish month starting with Nissan. A value of 1 is expected for Nissan ... 12 for Adar and 13 for
	 *            Adar II. Use the constants {@link #NISSAN} ... {@link #ADAR} (or {@link #ADAR_II} for a leap year Adar
	 *            II) to avoid any confusion.
	 * @param dayOfMonth
	 *            the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that only
	 *            has 29 days, the day will be set as 29.
	 * @throws IllegalArgumentException
	 *             if a Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month &lt; 1 or &gt; 12 (or 13 on a
	 *             leap year) or the day of month is &lt; 1 or &gt; 30 is passed in
	 */
	public void setJewishDate(int year, int month, int dayOfMonth) {
		setJewishDate(year, month, dayOfMonth, 0, 0, 0);
	}

	/**
	 * Sets the Jewish Date and updates the Gregorian date accordingly.
	 * 
	 * @param year
	 *            the Jewish year. The year can't be negative
	 * @param month
	 *            the Jewish month starting with Nissan. A value of 1 is expected for Nissan ... 12 for Adar and 13 for
	 *            Adar II. Use the constants {@link #NISSAN} ... {@link #ADAR} (or {@link #ADAR_II} for a leap year Adar
	 *            II) to avoid any confusion.
	 * @param dayOfMonth
	 *            the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that only
	 *            has 29 days, the day will be set as 29.
	 * 
	 * @param hours
	 *            the hour of the day. Used for <em>molad</em> calculations
	 * @param minutes
	 *            the minutes. Used for <em>molad</em> calculations
	 * @param chalakim
	 *            the <em>chalakim</em> / parts. Used for <em>molad</em> calculations. The <em>chalakim</em> should not
	 *            exceed 17. Minutes should be used for larger numbers.
	 * 
	 * @throws IllegalArgumentException
	 *             if a Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month &lt; 1 or &gt; 12 (or 13 on a leap year), the day
	 *             of month is &lt; 1 or &gt; 30, an hour &lt; 0 or &gt; 23, a minute &lt; 0 &gt; 59 or <em>chalakim</em> &lt; 0 &gt; 17. For
	 *             larger a larger number of <em>chalakim</em> such as 793 (<em>TaShTzaG</em>) break the <em>chalakim</em> into minutes (18
	 *             <em>chalakim</em> per minutes, so it would be 44 minutes and 1 <em>chelek</em> in the case of 793 (<em>TaShTzaG</em>).
	 */
	public void setJewishDate(int year, int month, int dayOfMonth, int hours, int minutes, int chalakim) {

        // if 30 is passed for a month that only has 29 days (for example by rolling the month from a month that had 30
        // days to a month that only has 29) set the date to 29th
        if (dayOfMonth > getDaysInJewishMonth(month, year)) {
            dayOfMonth = getDaysInJewishMonth(month, year);
        }

		validateJewishDate(year, month, dayOfMonth, hours, minutes, chalakim);

		jewishMonth = month;
		jewishDay = dayOfMonth;
		jewishYear = year;
		moladHours = hours;
		moladMinutes = minutes;
		moladChalakim = chalakim;

		gregorianAbsDate = jewishDateToAbsDate(jewishYear, jewishMonth, jewishDay); // reset Gregorian date

		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1; // reset day of week
	}

    public void setJewishMonth(int month){
        int year = getJewishYear();
        if (month < NISSAN || month > getLastMonthOfJewishYear(year)) {
            throw new IllegalArgumentException("The Jewish month has to be between 1 and 12 (or 13 on a leap year). "
                    + month + " is invalid for the year " + year + ".");
        }
        int day = Math.min(getDaysInJewishMonth(month,year),getJewishDayOfMonth());
        setJewishDate(year, month, day);
    }

    public void setJewishYear(int year){
        int month = Math.min(getJewishMonth(),getLastMonthOfJewishYear(year));
        int day = Math.min(getJewishDayOfMonth(), getDaysInJewishMonth(month,year));
        setJewishDate(year, month, day);
    }


	/**
	 * Returns this object's date as a {@link java.time.LocalDate} object.
	 *
	 * @return The {@link java.time.LocalDate}
	 */
	public LocalDate getLocalDate() {
        return absDateToDate(getAbsDate());
	}

	/**
	 * Resets this date to the current system date.
	 */
	public void resetDate() {
		LocalDate localDate = LocalDate.now();
		setGregorianDate(localDate);
	}

    public void minusDays(int days){
        if (days < 1) {
            throw new IllegalArgumentException("the amount of days to subtract has to be greater than zero.");
        }
        setAbsDate(getAbsDate() - days);

    }
    public void addDays(int days){
        if (days < 1) {
            throw new IllegalArgumentException("the amount of days to add has to be greater than zero.");
        }
        setAbsDate(getAbsDate() + days);
    }
    public void addMonths(int months){
        if (months < 1) {
            throw new IllegalArgumentException("the amount of months to add has to be greater than zero.");
        }
        int year = getJewishYear();
        int month = getJewishMonth();
        for (int i = 0; i < months; i++) {
            if (month == ELUL) {
                month = TISHREI;
                year++;
            } else if ((! isJewishLeapYear(year) && month == ADAR)
                    || (isJewishLeapYear(year) && month == ADAR_II)){
                month = NISSAN;
            } else {
                month++;
            }
        }
        int day = Math.min(getJewishDayOfMonth(), getDaysInJewishMonth(month,year));
        setJewishDate(year, month, day);
    }
    public void addYears(int years, boolean useAdarAlephInLeapYear){
        if (years < 1) {
            throw new IllegalArgumentException("the amount of years to add has to be greater than zero.");
        }
        int addedYear = getJewishYear() + years;
        // If we are in the month of Adar in a non-leap year and we are skipping
        // to a year that is a leap year, we will use useAdarAlephInYear do
        // decide which month to skip to.
        int month;
        if (getJewishMonth() == JewishDate.ADAR && !isJewishLeapYear(getJewishYear()) && isJewishLeapYear(addedYear)){
            if (useAdarAlephInLeapYear){
                month = JewishDate.ADAR;
            }else{
                month = JewishDate.ADAR_II;
            }
        } else{
            // If we are in JewishDate.ADAR_II, this will clamp the month to 12 (JewishDate.ADAR)
            month = Math.min(getJewishMonth(),getLastMonthOfJewishYear(addedYear));
        }
        // Clamp to final day of the month
        int day = Math.min(getJewishDayOfMonth(), getDaysInJewishMonth(month,addedYear));
        setJewishDate(addedYear, month, day);
    }



	/**
	 * Returns a string containing the Jewish date in the form, "day Month, year" e.g. "21 Shevat, 5729". For more
	 * complex formatting, use the formatter classes.
	 * 
	 * @return the Jewish date in the form "day Month, year" e.g. "21 Shevat, 5729"
	 * @see HebrewDateFormatter#format(JewishDate)
	 */
	public String toString() {
		return new HebrewDateFormatter().format(this);
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
		JewishDate jewishDate = (JewishDate) object;
		return gregorianAbsDate == jewishDate.getAbsDate();
	}

	/**
	 * Compares two dates as per the compareTo() method in the Comparable interface. Returns a value less than 0 if this
	 * date is "less than" (before) the date, greater than 0 if this date is "greater than" (after) the date, or 0 if
	 * they are equal.
	 */
	public int compareTo(JewishDate jewishDate) {
		return Integer.compare(gregorianAbsDate, jewishDate.getAbsDate());
	}

	/**
	 * Returns the Jewish month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nissan and goes to
	 * 13 for Adar II
	 * 
	 * @return the Jewish month from 1 to 12 (or 13 years in a leap year). The month count starts with 1 for Nissan and
	 *         goes to 13 for Adar II
	 */
	public int getJewishMonth() {
		return jewishMonth;
	}

	/**
	 * Returns the Jewish day of month.
	 * 
	 * @return the Jewish day of the month
	 */
	public int getJewishDayOfMonth() {
		return jewishDay;
	}

	/**
	 * Returns the Jewish year.
	 * 
	 * @return the Jewish year
	 */
	public int getJewishYear() {
		return jewishYear;
	}

	/**
	 * Returns the day of the week as a number between 1-7.
	 * 
	 * @return the day of the week as a number between 1-7.
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * A method that creates a <a href="http://en.wikipedia.org/wiki/Object_copy#Deep_copy">deep copy</a> of the object.
	 * 
	 * @see Object#clone()
	 */
	public Object clone() {
		JewishDate clone = null;
		try {
			clone = (JewishDate) super.clone();
		} catch (CloneNotSupportedException cnse) {
			// Required by the compiler. Should never be reached since we implement clone()
		}
		if (clone != null) {
            clone.setAbsDate(getAbsDate());
		}
		return clone;
	}

	/**
	 * Overrides {@link Object#hashCode()}.
	 * @see Object#hashCode()
	 */
	public int hashCode() {
        return Integer.hashCode(gregorianAbsDate);
	}
}
