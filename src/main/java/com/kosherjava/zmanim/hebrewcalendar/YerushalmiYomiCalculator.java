/*
 * Zmanim Java API
 * Copyright (C) 2017 - 2025 Eliyahu Hershfeld
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.kosherjava.zmanim.util.TimeZoneUtils;

/**
 * This class calculates the <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Talmud Yerusalmi</a> <a href=
 * "https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> page ({@link Daf}) for the a given date.
 * 
 * @author &copy; elihaidv
 * @author &copy; Eliyahu Hershfeld 2017 - 2025
 */
public class YerushalmiYomiCalculator {
	
	/**
	 * The start date of the first Daf Yomi Yerushalmi cycle of February 2, 1980 / 15 Shevat, 5740.
	 */
	private final static Calendar DAF_YOMI_START_DAY;
	static {
		DAF_YOMI_START_DAY = new GregorianCalendar(1980, Calendar.FEBRUARY, 2);
		DAF_YOMI_START_DAY.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	/** The number of milliseconds in a day. */
	private final static int DAY_MILIS = 1000 * 60 * 60 * 24;
	/** The number of pages in the Talmud Yerushalmi.*/
	private final static int WHOLE_SHAS_DAFS = 1554;
	/** The number of pages per <em>masechta</em> (tractate).*/
	private final static int[] BLATT_PER_MASECHTA = { 
			68, 37, 34, 44, 31, 59, 26, 33, 28, 20, 13, 92, 65, 71, 22, 22, 42, 26, 26, 33, 34, 22,
			19, 85, 72, 47, 40, 47, 54, 48, 44, 37, 34, 44, 9, 57, 37, 19, 13};

	/**
	 * Default constructor.
	 */
	public YerushalmiYomiCalculator() {
		// nothing here
	}
	
	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a>
	 * <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Yerusalmi</a> page ({@link Daf}) for a given date.
	 * The first Daf Yomi cycle started on 15 Shevat (Tu Bishvat), 5740 (February, 2, 1980) and calculations
	 * prior to this date will result in an IllegalArgumentException thrown. A null will be returned on Tisha B'Av or
	 * Yom Kippur.
	 *
	 * @param calendar
	 *            the calendar date for calculation
	 * @return the {@link Daf} or null if the date is on Tisha B'Av or Yom Kippur.
	 *
	 * @throws IllegalArgumentException
	 *             if the date is prior to the February 2, 1980, the start of the first Daf Yomi Yerushalmi cycle
	 */
	public static Daf getDafYomiYerushalmi(JewishCalendar calendar) {
		
		Calendar nextCycle = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		Calendar prevCycle = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		Calendar requested = calendar.getGregorianCalendar();
		int masechta = 0;
		Daf dafYomi = null;

		// There isn't Daf Yomi on Yom Kippur or Tisha B'Av.
		if ( calendar.getYomTovIndex() == JewishCalendar.YOM_KIPPUR ||
				calendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV ) {
			return null;
		}
		
		
		if (requested.before(DAF_YOMI_START_DAY)) {
			throw new IllegalArgumentException(requested + " is prior to organized Daf Yomi Yerushalmi cycles that started on "
					+ DAF_YOMI_START_DAY);
		}
		
		// Start to calculate current cycle. init the start day
		nextCycle.setTime(DAF_YOMI_START_DAY.getTime());

		// Go cycle by cycle, until we get the next cycle
		while (requested.after(nextCycle)) {
			prevCycle.setTime(nextCycle.getTime());
			
			// Adds the number of whole shas dafs. and the number of days that not have daf.
			nextCycle = TimeZoneUtils.addDay(nextCycle, WHOLE_SHAS_DAFS);
			nextCycle = TimeZoneUtils.addDay(nextCycle, getNumOfSpecialDays(prevCycle, nextCycle));
		}

		// Get the number of days from cycle start until request.
		int dafNo = (int)(getDiffBetweenDays(prevCycle, requested));
		
		// Get the number of special day to subtract
		int specialDays = getNumOfSpecialDays(prevCycle, requested);
		int total = dafNo - specialDays;
				
		// Finally find the daf.
		for (int i : BLATT_PER_MASECHTA) {
			if (total < i) {
				dafYomi = new Daf(masechta, total + 1);
				break;
			}
			total -= i;
			masechta++;
		}

		return dafYomi;
	}
	
	/**
	 * Return the number of special days (Yom Kippur and Tisha Beav, where there are no dafim), between the start date
	 * (as a <code>Calendar</code>) and end date (also as a <code>Calendar</code>).
	 * 
	 * @param start date to start calculating from
	 * @param end date to finish calculating at
	 * @return the number of special days between the start and end dates
	 */
	private static int getNumOfSpecialDays(Calendar start, Calendar end) {
		
		// Find the start and end Jewish years
		int startYear = new JewishCalendar(start).getJewishYear();
		int endYear = new JewishCalendar(end).getJewishYear();
		
		// Value to return
		int specialDays = 0;
		
		//Instant of special Dates
		JewishCalendar yom_kippur = new JewishCalendar(5770, 7, 10);
		JewishCalendar tisha_beav = new JewishCalendar(5770, 5, 9);

		// Go over the years and find special dates
		for (int i = startYear; i <= endYear; i++) {
			yom_kippur.setJewishYear(i);
			tisha_beav.setJewishYear(i);
			
			if (isBetween(start, yom_kippur.getGregorianCalendar(), end)) {
				specialDays++;
			}
			if (isBetween(start, tisha_beav.getGregorianCalendar(), end)) {
				specialDays++;
			}
		}
		
		return specialDays;
	}

	/**
	 * Return if the date is between two dates
	 * 
	 * @param start the start date
	 * @param date the date being compared
	 * @param end the end date
	 * @return if the date is between the start and end dates
	 */
	private static boolean isBetween(Calendar start, Calendar date, Calendar end ) {
		return start.before(date) && end.after(date);
	}
	
	/**
	 * Return the number of days between the dates passed in
	 * @param start the start date
	 * @param end the end date
	 * @return the number of days between the start and end dates
	 */
	private static long getDiffBetweenDays(Calendar start, Calendar end) {
		return  (end.getTimeInMillis() - start.getTimeInMillis()) / DAY_MILIS;
	}
}
