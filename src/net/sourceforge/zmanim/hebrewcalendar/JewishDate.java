/*
 * Zmanim Java API
 * Copyright (C) 2011 Eliyahu Hershfeld
 * Copyright (C) September 2002 Avrom Finkelstien
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc. 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA or connect to: http://www.fsf.org/copyleft/gpl.html
 */
package net.sourceforge.zmanim.hebrewcalendar;

import java.util.*;

/**
 * The JewishDate class allows one to maintain an instance of a Gregorian date along with the corresponding Jewish date.
 * This class can use the standard Java Date and Calendar classes for setting it, but does not subclass these classes or
 * use them internally to any extensive use. This class also does not have a concept of a time (which the Date class
 * does). If you are looking for a class that implements a Jewish calendar version of the Calendar class, one is
 * available from <a href="http://oss.software.ibm.com/developerworks/opensource/icu4j/" >developerWorks</a> by IBM.
 * 
 * This open source Java code was written by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a> from his C++
 * code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements and some bug
 * fixing.
 * 
 * Some of Avrom's original C++ code was translated from <a href="http://emr.cs.uiuc.edu/~reingold/calendar.C">C/C++
 * code</a> in <a href="http://www.calendarists.com">Calendrical Calculations</a> by Nachum Dershowitz and Edward M.
 * Reingold, Software-- Practice & Experience, vol. 20, no. 9 (September, 1990), pp. 899- 928. Any method with the mark
 * "ND+ER" indicates that the method was taken from this source with minor modifications.
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.2.6
 */
public class JewishDate implements Comparable, Cloneable {
	private static final int JEWISH_EPOCH = -1373429;

	private int jewishMonth;
	private int jewishDay;
	private int jewishYear;

	/**
	 * The month, where 1 == January, 2 == February, etc...
	 * <p>
	 * Note how this is explicitly different than Java's Calendar class!
	 */
	private int gregorianMonth;

	/** The day of the Gregorian month. E.g., 1st, 2nd, 3rd, etc... */
	private int gregorianDayOfMonth;

	/** The Gregorian year, e.g., 2011, 2012, etc... */
	private int gregorianYear;

	/**
	 * 1 == Sunday, 2 == Monday, etc...
	 */
	private int dayOfWeek;

	private int gregorianAbsDate;

	/**
	 * Returns the number of days in a given month in a given year.
	 * 
	 * @param month
	 *            the month. As with other cases in this class, this is 1-based, not zero-based.
	 * @param year
	 *            the year (only impacts February)
	 * @return the number of days in the month in the given year
	 */
	private static int getLastDayOfGregorianMonth(int month, int year) {
		switch (month) {
		case 2:
			if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0)) {
				return 29;
			} else {
				return 28;
			}
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default:
			return 31;
		}
	}

	/**
	 * Returns the number of days in a given month for the current year. Will likely turn into a private method in the
	 * future.
	 * 
	 * @param month
	 *            the month, where 1==January, 2==February, etc...
	 * @return the number of days in the month
	 */
	int getLastDayOfGregorianMonth(int month) {
		return getLastDayOfGregorianMonth(month, gregorianYear);
	}

	/**
	 * Computes the Gregorian date from the absolute date. ND+ER
	 */
	private void absDateToDate() {
		// Search forward year by year from approximate year
		gregorianYear = gregorianAbsDate / 366;
		while (gregorianAbsDate >= gregorianDateToAbsDate(gregorianYear + 1, 1, 1)) {
			gregorianYear++;
		}
		// Search forward month by month from January
		gregorianMonth = 1;
		while (gregorianAbsDate > gregorianDateToAbsDate(gregorianYear, gregorianMonth,
				getLastDayOfGregorianMonth(gregorianMonth))) {
			gregorianMonth++;
		}
		gregorianDayOfMonth = gregorianAbsDate - gregorianDateToAbsDate(gregorianYear, gregorianMonth, 1) + 1;
	}

	/**
	 * Returns the absolute date (days since January 1, 0001 on the Gregorian calendar).
	 * 
	 * @return the number of days since January 1, 1
	 */
	protected int getAbsDate() {
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
			// days in prior months of the year
			absDate += getLastDayOfGregorianMonth(m, year);
		}
		return (absDate // days this year
				+ 365 * (year - 1) // days in previous years ignoring leap days
				+ (year - 1) / 4 // Julian leap days before this year...
				- (year - 1) / 100 // ...minus prior century years...
		+ (year - 1) / 400); // ...plus prior years divisible by 400
	}

	/**
	 * Returns if the year is a Jewish leap year.
	 * 
	 * @param year
	 *            the Jewish year.
	 * @return true if it is a leap year
	 */
	public static boolean isJewishLeapYear(int year) {
		return ((7 * year) + 1) % 19 < 7;
	}

	/**
	 * Returns the last month of a given Jewish year.
	 * 
	 * @param year
	 *            the Jewish year.
	 * @return 12 on a non leap-year or 13 on a leap-year
	 */
	private static int getLastMonthOfJewishYear(int year) {
		return isJewishLeapYear(year) ? 13 : 12;
	}

	/**
	 * Returns the number of days elapsed from the Sunday prior to the start of the Jewish calendar to the mean
	 * conjunction of Tishri of the Jewish year.ND+ER
	 * 
	 * @param year
	 *            the Jewish year
	 * @return the number of days elapsed from the Sunday prior to the start of the Jewish calendar to the mean
	 *         conjunction of Tishri of Jewish year.
	 */
	private static int getJewishCalendarElapsedDays(int year) {
		int monthsElapsed = (235 * ((year - 1) / 19)) // Months in complete cycles so far
				+ (12 * ((year - 1) % 19)) // Regular months in this cycle
				+ ((7 * ((year - 1) % 19) + 1) / 19); // Leap months this cycle

		int partsElapsed = 204 + 793 * (monthsElapsed % 1080);
		int hoursElapsed = 5 + 12 * monthsElapsed + 793 * (monthsElapsed / 1080) + partsElapsed / 1080;
		int conjunctionDay = 1 + 29 * monthsElapsed + hoursElapsed / 24;
		int conjunctionParts = 1080 * (hoursElapsed % 24) + partsElapsed % 1080;
		int alternativeDay;
		if ((conjunctionParts >= 19440) // If new moon is at or after midday,
				|| (((conjunctionDay % 7) == 2) // ...or is on a Tuesday...
						&& (conjunctionParts >= 9924) // at 9 hours, 204 parts
														// or later...
				&& !isJewishLeapYear(year)) // ...of a common year,
				|| (((conjunctionDay % 7) == 1) // ...or is on a Monday at...
						&& (conjunctionParts >= 16789) // 15 hours, 589 parts or
														// later...
				&& (isJewishLeapYear(year - 1)))) { // at the end of a leap year
			// Then postpone Rosh HaShanah one day
			alternativeDay = conjunctionDay + 1;
		} else {
			alternativeDay = conjunctionDay;
		}
		if (((alternativeDay % 7) == 0)// If Rosh HaShanah would occur on
										// Sunday,
				|| ((alternativeDay % 7) == 3) // or Wednesday,
				|| ((alternativeDay % 7) == 5)) { // or Friday
			// Then postpone it one (more) day
			return (1 + alternativeDay);
		} else {
			return alternativeDay;
		}
	}

	/**
	 * Returns the number of days for a given Jewish year. ND+ER
	 * 
	 * @param year
	 *            the Jewish year
	 * @return the number of days for a given Jewish year.
	 */
	private static int getDaysInJewishYear(int year) {
		return getJewishCalendarElapsedDays(year + 1) - getJewishCalendarElapsedDays(year);
	}

	/**
	 * Returns if Cheshvan is long in a given Jewish year. ND+ER
	 * 
	 * @param year
	 *            the year
	 * @return true if Cheshvan is long in Jewish year.
	 */
	public static boolean isCheshvanLong(int year) {
		return getDaysInJewishYear(year) % 10 == 5;
	}

	/**
	 * Returns if Kislev is short in a given Jewish year. ND+ER
	 * 
	 * @param year
	 *            the Jewish year
	 * @return true if Kislev is short for the given Jewish year.
	 */
	public static boolean isKislevShort(int year) {
		return getDaysInJewishYear(year) % 10 == 3;
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
	public static int getDaysInJewishMonth(int month, int year) {
		if ((month == 2) || (month == 4) || (month == 6) || ((month == 8) && !(isCheshvanLong(year)))
				|| ((month == 9) && isKislevShort(year)) || (month == 10)
				|| ((month == 12) && !(isJewishLeapYear(year))) || (month == 13)) {
			return 29;
		} else {
			return 30;
		}
	}

	/**
	 * Computes the Jewish date from the absolute date. ND+ER
	 */
	private void absDateToJewishDate() {
		// Approximation from below
		jewishYear = (gregorianAbsDate + JEWISH_EPOCH) / 366;
		// Search forward for year from the approximation
		while (gregorianAbsDate >= jewishDateToAbsDate(jewishYear + 1, 7, 1)) {
			jewishYear++;
		}
		// Search forward for month from either Tishri or Nisan.
		if (gregorianAbsDate < jewishDateToAbsDate(jewishYear, 1, 1)) {
			jewishMonth = 7;// Start at Tishri
		} else {
			jewishMonth = 1;// Start at Nisan
		}
		while (gregorianAbsDate > jewishDateToAbsDate(jewishYear, jewishMonth,
				getDaysInJewishMonth(jewishMonth, jewishYear))) {
			jewishMonth++;
		}
		// Calculate the day by subtraction
		jewishDay = gregorianAbsDate - jewishDateToAbsDate(jewishYear, jewishMonth, 1) + 1;
	}

	/**
	 * Returns the absolute date of Jewish date. ND+ER
	 * 
	 * @param year
	 *            the Jewish year. The year can't be negative
	 * @param month
	 *            the Jewish month starting with Nisan. Nisan expects a value of 1 etc till Adar with a value of 12. For
	 *            a leap year, 13 will be the expected value for Adar II.
	 * @param dayOfMonth
	 *            the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that only
	 *            has 29 days, the day will be set as 29.
	 * @return the absolute date of the Jewish date.
	 */
	private static int jewishDateToAbsDate(int year, int month, int dayOfMonth) {

		int absDate = dayOfMonth;

		// Before Tishri, so add days in prior months
		if (month < 7) {
			// this year before and after Nisan.
			for (int m = 7; m <= getLastMonthOfJewishYear(year); m++) {
				absDate += getDaysInJewishMonth(m, year);
			}
			for (int m = 1; m < month; m++) {
				absDate += getDaysInJewishMonth(m, year);
			}
		} else { // Add days in prior months this year
			for (int m = 7; m < month; m++) {
				absDate += getDaysInJewishMonth(m, year);
			}
		}

		// Days in prior years + Days elapsed before absolute date 1
		return (absDate + getJewishCalendarElapsedDays(year) + JEWISH_EPOCH);
	}

	/**
	 * Creates a Jewish date based on a Gregorian date
	 * 
	 * @param gregorianYear
	 *            the Gregorian year
	 * @param gregorianMonth
	 *            the Gregorian month. Unlike the Java Calendar where January has the value of 0,This expects a 1 for
	 *            January
	 * @param gregorianDayOfMonth
	 *            the Gregorian day of month. If this is > the number of days in the month/year, the last valid date of
	 *            the month will be set
	 * 
	 */
	public JewishDate(int gregorianYear, int gregorianMonth, int gregorianDayOfMonth) {
		setGregorianDate(gregorianYear, gregorianMonth, gregorianDayOfMonth);
	}

	/**
	 * Default constructor will set a default date to the current system date.
	 */
	public JewishDate() {
		resetDate();
	}

	/**
	 * A constructor that initializes the date to the {@link java.util.Date Date} paremeter.
	 * 
	 * @param date
	 *            the <code>Date</code> to set the calendar to
	 */
	public JewishDate(Date date) {
		setDate(date);
	}

	/**
	 * A constructor that initializes the date to the {@link java.util.Calendar Calendar} paremeter.
	 * 
	 * @param calendar
	 *            the <code>Calendar</code> to set the calendar to
	 */
	public JewishDate(Calendar calendar) {
		setDate(calendar);
	}

	/**
	 * Sets the date based on a {@link java.util.Calendar Calendar} object. Modifies the Jewish date as well.
	 * 
	 * @param calendar
	 *            the <code>Calendar</code> to set the calendar to
	 */
	public void setDate(Calendar calendar) {
		gregorianMonth = calendar.get(Calendar.MONTH) + 1;
		gregorianDayOfMonth = calendar.get(Calendar.DATE);
		gregorianYear = calendar.get(Calendar.YEAR);

		// init the date
		gregorianAbsDate = gregorianDateToAbsDate(gregorianYear, gregorianMonth, gregorianDayOfMonth);
		absDateToJewishDate();

		// set day of week
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Sets the date based on a {@link java.util.Date Date} object. Modifies the Jewish date as well.
	 * 
	 * @param date
	 *            the <code>Date</code> to set the calendar to
	 */
	public void setDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
	}

	/**
	 * Sets the Gregorian Date, and updates the Jewish date accordingly. Confusingly unlike the Java Calendar where
	 * January has the value of 0,This expects a 1 for January this uses a
	 * 
	 * @param year
	 *            the Gregorian year
	 * @param month
	 *            the Gregorian month. Unlike the Java Calendar where January has the value of 0,This expects a 1 for
	 *            January
	 * @param dayOfMonth
	 *            the Gregorian day of month. If this is > the number of days in the month/year, the last valid date of
	 *            the month will be set
	 */
	public void setGregorianDate(int year, int month, int dayOfMonth) {
		// precondition should be 1->12 anyways, but just in case... //
		if (month > 12 || month < 1) {
			throw new IllegalArgumentException("The Gregorian month has to be between 1 - 12. " + month
					+ " is invalid.");
		}
		if (dayOfMonth <= 0) {
			throw new IllegalArgumentException("The day of month can't be less than 1. " + dayOfMonth + " is invalid.");
		}

		// make sure date is a valid date for the given month, if not, set to
		// last day of month
		if (dayOfMonth > getLastDayOfGregorianMonth(month, year)) {
			dayOfMonth = getLastDayOfGregorianMonth(month, year);
		}
		if (year < 0) {
			throw new IllegalArgumentException("Years < 0 can't be claculated. " + year + " is invalid.");
		}
		// init month, date, year
		gregorianMonth = month;
		gregorianDayOfMonth = dayOfMonth;
		gregorianYear = year;

		// init date
		gregorianAbsDate = gregorianDateToAbsDate(gregorianYear, gregorianMonth, gregorianDayOfMonth);
		absDateToJewishDate();

		// set day of week
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Sets the Jewish Date and updates the Gregorian date accordingly.
	 * 
	 * @param year
	 *            the Jewish year. The year can't be negative
	 * @param month
	 *            the Jewish month starting with Nisan. Nisan expects a value of 1 etc till Adar with a value of 12. For
	 *            a leap year, 13 will be the expected value for Adar II.
	 * @param dayOfMonth
	 *            the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that only
	 *            has 29 days, the day will be set as 29.
	 */
	public void setJewishDate(int year, int month, int dayOfMonth) {
		if (month < 1 || month > getLastMonthOfJewishYear(year)) {
			throw new IllegalArgumentException("The Jewish month has to be between 1 and 12 (or 13 on a leap year). "
					+ month + " is invalid for the year " + year + ".");
		}
		if (dayOfMonth < 1) {
			throw new IllegalArgumentException("The Jewish day of month can't be < 1.  " + dayOfMonth + " is invalid.");
		}

		if (dayOfMonth > 30) {
			throw new IllegalArgumentException("The Jewish day of month can't be > 30.  " + dayOfMonth + " is invalid.");
		}

		// if 30 is passed for a month that only has 29 days (for example by
		// rolling the month from a month that had 30 days to a month that only
		// has 29) set the date to 29th
		if (dayOfMonth > getDaysInJewishMonth(month, year)) {
			dayOfMonth = getDaysInJewishMonth(month, year);
		}

		if (year < 0) {
			throw new IllegalArgumentException("A Jewish years < 0 can't be set. " + year + " is invalid.");
		}

		jewishMonth = month;
		jewishDay = dayOfMonth;
		jewishYear = year;

		// reset Gregorian date
		gregorianAbsDate = jewishDateToAbsDate(jewishYear, jewishMonth, jewishDay);
		absDateToDate();

		// reset day of week
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Returns this object's date as a java.util.Date object. <b>Note</b>: This class does not have a concept of time.
	 * 
	 * @return The <code>Date</code>
	 */
	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(gregorianYear, gregorianMonth - 1, gregorianDayOfMonth);
		return cal.getTime();
	}

	/**
	 * Resets this date to the current system date.
	 */
	public void resetDate() {
		Calendar calendar = Calendar.getInstance();
		setDate(calendar);
	}

	/**
	 * Returns a string containing the Jewish date in the form, "day Month, year" e.g. "21 Shevat, 5729". For more
	 * complex formatting, use the formatter classes.
	 * 
	 * @return the Jewish date in the form "day Month, year" e.g. "21 Shevat, 5729"
	 */
	public String toString() {
		return HebrewDateFormatter.getHebrewDateAsString(this);
	}

	/**
	 * Rolls the date forward by 1. It modifies both the Gregorian and Jewish dates accordingly.
	 */
	public void forward() {
		// Change Gregorian date
		if (gregorianDayOfMonth == getLastDayOfGregorianMonth(gregorianMonth)) {
			// if last day of year
			if (gregorianMonth == 12) {
				gregorianYear++;
				gregorianMonth = 1;
				gregorianDayOfMonth = 1;
			} else {
				gregorianMonth++;
				gregorianDayOfMonth = 1;
			}
		} else { // if not last day of month
			gregorianDayOfMonth++;
		}

		// Change the Jewish Date
		if (jewishDay == getDaysInJewishMonth(jewishMonth, jewishYear)) {
			// if it last day of elul (i.e. last day of Jewish year)
			if (jewishMonth == 6) {
				jewishYear++;
				jewishMonth++;
				jewishDay = 1;
			} else if (jewishMonth == getLastMonthOfJewishYear(jewishYear)) {
				// if it is the last day of Adar, or Adar II as case may be
				jewishMonth = 1;
				jewishDay = 1;
			} else {
				jewishMonth++;
				jewishDay = 1;
			}
		} else { // if not last date of month
			jewishDay++;
		}
		// if last day of week, loop back to Sunday
		if (dayOfWeek == 7) {
			dayOfWeek = 1;
		} else {
			dayOfWeek++;
		}
		// increment the absolute date
		gregorianAbsDate++;
	}

	/**
	 * Rolls the date back by 1. It modifies both the Gregorian and Jewish dates accordingly
	 */
	public void back() {
		// Change Gregorian date
		// if first day of month
		if (gregorianDayOfMonth == 1) {
			// if first day of year
			if (gregorianMonth == 1) {
				gregorianMonth = 12;
				gregorianYear--;
			} else {
				gregorianMonth--;
			}
			// change to last day of previous month
			gregorianDayOfMonth = getLastDayOfGregorianMonth(gregorianMonth);
		} else {
			gregorianDayOfMonth--;
		}
		// change Jewish date
		// if first day of the Jewish month
		if (jewishDay == 1) {
			// if Nissan
			if (jewishMonth == 1) {
				jewishMonth = getLastMonthOfJewishYear(jewishYear);
			} else if (jewishMonth == 7) { // if Rosh Hashana
				jewishYear--;
				jewishMonth--;
			} else {
				jewishMonth--;
			}
			jewishDay = getDaysInJewishMonth(jewishMonth, jewishYear);
		} else {
			jewishDay--;
		}
		// if first day of week, loop back to Saturday
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			dayOfWeek--;
		}
		// change the absolute date
		gregorianAbsDate--;
	}

	/**
	 * Compares two dates to see if they are equal
	 */
	public boolean equals(Object object) {

		if (!(object instanceof JewishDate)) {
			return false;
		}

		JewishDate hebDate = (JewishDate) object;
		return gregorianAbsDate == hebDate.getAbsDate();
	}

	/**
	 * Compares two dates as per the compareTo() method in the Comparable interface. Returns a value less than 0 if this
	 * date is "less than" (before) the date, greater than 0 if this date is "greater than" (after) the date, or 0 if
	 * they are equal.
	 */
	public int compareTo(Object o) {
		JewishDate hebDate = (JewishDate) o;
		if (gregorianAbsDate < hebDate.getAbsDate()) {
			return -1;
		} else if (gregorianAbsDate > hebDate.getAbsDate()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Returns the Gregorian month (between 1-12).
	 * 
	 * @return the Gregorian month (between 1-12). Unlike the java.util.Calendar, this will be 1 based and not 0 based.
	 */
	public int getGregorianMonth() {
		return gregorianMonth;
	}

	/**
	 * Returns the Gregorian day of the month.
	 * 
	 * @return the Gregorian day of the mont
	 */
	public int getGregorianDayOfMonth() {
		return gregorianDayOfMonth;
	}

	/**
	 * Returns the Gregotian year.
	 * 
	 * @return the Gregorian year
	 */
	public int getGregorianYear() {
		return gregorianYear;
	}

	/**
	 * Returns the Jewish month (1-12 or 13).
	 * 
	 * @return the Jewish month from 1 to 12 (or 13 years in a leap year). The month count starts with 1 for Nisan and
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
	 * Sets the Gregorian month.
	 * 
	 * @param month
	 *            the Gregorian month
	 * 
	 */
	public void setGregorianMonth(int month) {
		setGregorianDate(gregorianYear, month, gregorianDayOfMonth);
	}

	/**
	 * sets the Gregorian year.
	 * 
	 * @param year
	 *            the Gregorian year.
	 */
	public void setGregorianYear(int year) {
		setGregorianDate(year, gregorianMonth, gregorianDayOfMonth);
	}

	/**
	 * sets the Gregorian Day of month.
	 * 
	 * @param dayOfMonth
	 *            the Gregorian Day of month.
	 */
	public void setGregorianDayOfMonth(int dayOfMonth) {
		setGregorianDate(gregorianYear, gregorianMonth, dayOfMonth);
	}

	/**
	 * sets the Jewish month.
	 * 
	 * @param month
	 *            the Jewish month from 1 to 12 (or 13 years in a leap year). The month count starts with 1 for Nisan
	 *            and goes to 13 for Adar II
	 */
	public void setJewishMonth(int month) {
		setJewishDate(jewishYear, month, jewishDay);
	}

	/**
	 * sets the Jewish year.
	 * 
	 * @param year
	 *            the Jewish year
	 */
	public void setJewishYear(int year) {
		setJewishDate(year, jewishMonth, jewishDay);
	}

	/**
	 * sets the Jewish day of month.
	 * 
	 * @param dayOfMonth
	 *            the Jewish day of month
	 */
	public void setJewishDayOfMonth(int dayOfMonth) {
		setJewishDate(jewishYear, jewishMonth, dayOfMonth);
	}

	/** Create a copy of this date. */
	// FIXME - create deep clone, or simplify by creating a clone constructor
	public JewishDate clone() {
		return new JewishDate(gregorianYear, gregorianMonth, gregorianDayOfMonth);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getClass().hashCode(); // needed or this and
														// subclasses will
														// return identical hash
		result += 37 * result + gregorianAbsDate;
		return result;
	}
}
