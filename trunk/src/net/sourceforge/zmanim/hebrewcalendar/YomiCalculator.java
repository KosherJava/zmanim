/*
 * Zmanim Java API
 * Copyright (C) 2011 Eliyahu Hershfeld
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
package net.sourceforge.zmanim.hebrewcalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class calculates the Daf Yomi page (daf) for a given date. The class currently only supports Daf Yomi Bavli, but
 * may cover Yerushalmi, Mishna Yomis etc in the future.
 * 
 * @author &copy; Bob Newell (original C code)
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.0.1
 */
public class YomiCalculator {

	private static Date dafYomiStartDate = new GregorianCalendar(1923, Calendar.SEPTEMBER, 11).getTime();
	private static int dafYomiJulianStartDay = getJulianDay(dafYomiStartDate);
	private static Date shekalimChangeDate = new GregorianCalendar(1975, Calendar.JUNE, 24).getTime();
	private static int shekalimJulianChangeDay = getJulianDay(shekalimChangeDate);

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Daf_yomi">Daf Yomi</a> <a
	 * href="http://en.wikipedia.org/wiki/Talmud">Bavli</a> {@link Daf} for a given date. The first Daf Yomi cycle
	 * started on Rosh Hashana 5684 (September 11, 1923) and calculations prior to this date will result in an
	 * IllegalArgumentException thrown. For historical calculations (supported by this method), it is important to note
	 * that a change in length of the cycle was instituted starting in the eighth Daf Yomi cycle beginning on June 24,
	 * 1975. The Daf Yomi Bavli cycle has a single masechta of the Talmud Yerushalmi - Shekalim as part of the cycle.
	 * Unlike the Bavli where the number of daf per masechta was standardized since the original <a
	 * href="http://en.wikipedia.org/wiki/Daniel_Bomberg">Bomberg Edition</a> published from 1520 - 1523, there is no
	 * uniform page length in the Yerushalmi. The early cycles had the Yerushalmi Shekalim length of 13 days following
	 * the <a href="http://en.wikipedia.org/wiki/Zhytomyr">Zhytomyr</a> Shas used by <a
	 * href="http://en.wikipedia.org/wiki/Meir_Shapiro">Rabbi Meir Shapiro</a>. With the start of the eighth Daf Yomi
	 * cycle beginning on June 24, 1975 the length of the Yerushalmi shekalim was changed from 13 to 22 daf to follow
	 * the Vilna Shas that is in common use today.
	 * 
	 * @param calendar
	 *            the calendar date for calculation
	 * @return the {@link Daf}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the date is prior to the September 11, 1923 start date of the first Daf Yomi cycle
	 */
	public static Daf getDafYomiBavli(JewishCalendar calendar) {
		/*
		 * The number of daf per masechta. Since the number of blatt in Shekalim changed on the 8th Daf Yomi cycle
		 * beginning on June 24, 1975 from 13 to 22, the actual calculation for blattPerMasechta[4] will later be
		 * adjusted based on the cycle.
		 */
		int[] blattPerMasechta = { 64, 157, 105, 121, 22, 88, 56, 40, 35, 31, 32, 29, 27, 122, 112, 91, 66, 49, 90, 82,
				119, 119, 176, 113, 24, 49, 76, 14, 120, 110, 142, 61, 34, 34, 28, 22, 4, 10, 4, 73 };
		Date date = calendar.getTime();

		Daf dafYomi = null;
		int julianDay = getJulianDay(date);
		int cycleNo = 0;
		int dafNo = 0;
		if (date.before(dafYomiStartDate)) {
			// TODO: should we return a null or throw an IllegalArgumentException?
			throw new IllegalArgumentException(date + " is prior to organized Daf Yomi Bavli cycles that started on "
					+ dafYomiStartDate);
		}
		if (date.equals(shekalimChangeDate) || date.after(shekalimChangeDate)) {
			cycleNo = 8 + ((julianDay - shekalimJulianChangeDay) / 2711);
			dafNo = ((julianDay - shekalimJulianChangeDay) % 2711);
		} else {
			cycleNo = 1 + ((julianDay - dafYomiJulianStartDay) / 2702);
			dafNo = ((julianDay - dafYomiJulianStartDay) % 2702);
		}

		int total = 0;
		int masechta = -1;
		int blatt = 0;

		/* Fix Shekalim for old cycles. */
		if (cycleNo <= 7) {
			blattPerMasechta[4] = 13;
		} else {
			blattPerMasechta[4] = 22; // correct any change that may have been changed from a prior calculation
		}
		/* Finally find the daf. */
		for (int j = 0; j < blattPerMasechta.length; j++) {
			masechta++;
			total = total + blattPerMasechta[j] - 1;
			if (dafNo < total) {
				blatt = 1 + blattPerMasechta[j] - (total - dafNo);
				/* Fiddle with the weird ones near the end. */
				if (masechta == 36) {
					blatt += 21;
				} else if (masechta == 37) {
					blatt += 24;
				} else if (masechta == 38) {
					blatt += 33;
				}
				dafYomi = new Daf(masechta, blatt);
				break;
			}
		}

		return dafYomi;
	}

	/**
	 * Return the <a href="http://en.wikipedia.org/wiki/Julian_day">Julian day</a> from a Java Date.
	 * 
	 * @param date
	 *            The Java Date
	 * @return the Julian day number corresponding to the date
	 */
	private static int getJulianDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (month <= 2) {
			year -= 1;
			month += 12;
		}
		int a = year / 100;
		int b = 2 - a + a / 4;
		return (int) (Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5);
	}
}