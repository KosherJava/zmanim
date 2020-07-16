/*
 * Zmanim Java API
 * Copyright (C) 2017 - 2018 Eliyahu Hershfeld
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

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class calculates the <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Yerusalmi</a> <a href=
 * "https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> page ({@link Daf}) for the a given date.
 * 
 * @author &copy; elihaidv
 * @author &copy; Eliyahu Hershfeld 2017 - 2018
 */
public class YerushalmiYomiCalculator {
	
	private final static Calendar DAF_YOMI_START_DAY = new GregorianCalendar(1980, Calendar.FEBRUARY, 2);
	private final static int DAY_MILIS = 1000 * 60 * 60 * 24;
	private final static int WHOLE_SHAS_DAFS = 1554;
	private final static int[] BLATT_PER_MASSECTA = { 
			68, 37, 34, 44, 31, 59, 26, 33, 28, 20, 13, 92, 65, 71, 22, 22, 42, 26, 26, 33, 34, 22,
			19, 85, 72, 47, 40, 47, 54, 48, 44, 37, 34, 44, 9, 57, 37, 19, 13};

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a>
	 * <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Yerusalmi</a> page ({@link Daf}) for a given date.
	 * The first Daf Yomi cycle started on To Bishvat 5740 (February, 2, 1980) and calculations prior to this
	 * date will result in an IllegalArgumentException thrown.
	 * 
	 * @param calendar
	 *            the calendar date for calculation
	 * @return the {@link Daf}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the date is prior to the September 11, 1923 start date of the first Daf Yomi cycle
	 */
	public static Daf getDafYomiYerushalmi(JewishCalendar calendar) {
		
		Calendar nextCycle = new GregorianCalendar();
		Calendar prevCycle = new GregorianCalendar();
		Calendar requested = calendar.getGregorianCalendar();
		int masechta = 0;
		Daf dafYomi = null;
		
		// There isn't Daf Yomi in Yom Kippur and Tisha Beav.
		if ( calendar.getYomTovIndex() == JewishCalendar.YOM_KIPPUR ||
			 calendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV ) {
			return new Daf(39,0);
		}
		
		
		if (requested.before(DAF_YOMI_START_DAY)) {
			// TODO: should we return a null or throw an IllegalArgumentException?
			throw new IllegalArgumentException(requested + " is prior to organized Daf Yomi Yerushlmi cycles that started on "
					+ DAF_YOMI_START_DAY);
		}
		
		// Start to calculate current cycle. init the start day
		nextCycle.setTime(DAF_YOMI_START_DAY.getTime());
		
		// Go cycle by cycle, until we get the next cycle
		while (requested.after(nextCycle)) {
			prevCycle.setTime(nextCycle.getTime());
			
			// Adds the number of whole shas dafs. and the number of days that not have daf.
			nextCycle.add(Calendar.DAY_OF_MONTH, WHOLE_SHAS_DAFS);
			nextCycle.add(Calendar.DAY_OF_MONTH, getNumOfSpecialDays(prevCycle, nextCycle));		
		}
		
		// Get the number of days from cycle start until request.
		int dafNo = (int)(getDiffBetweenDays(prevCycle, requested));
		
		// Get the number of special day to subtract
		int specialDays = getNumOfSpecialDays(prevCycle, requested);
		int total = dafNo - specialDays;
				
		// Finally find the daf.
		for (int j = 0; j < BLATT_PER_MASSECTA.length; j++) {
			
			if (total <= BLATT_PER_MASSECTA[j]) {
				dafYomi = new Daf(masechta, total + 1);
				break;
			}
			total -= BLATT_PER_MASSECTA[j];
			masechta++;
		}

		return dafYomi;
	}
	
	/**
	 * Return the number of special days (Yom Kippur and Tisha Beav) That there is no Daf in this days.
	 * From the last given number of days until given date
	 * 
	 * @param start start date to calculate
	 * @param end end date to calculate
	 * @return the number of special days
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
	private static boolean isBetween( Calendar start, Calendar date, Calendar end ) {
		return start.before(date) && end.after(date);
	}
	
	/**
	 * Return the number of days between the dates passed in
	 * @param start the start date
	 * @param end the end date
	 * @return the number of days between the start and end dates
	 */
	private static long getDiffBetweenDays(Calendar start, Calendar end) {
		return  ( end.getTimeInMillis() - start.getTimeInMillis()) / DAY_MILIS;
	}
}
