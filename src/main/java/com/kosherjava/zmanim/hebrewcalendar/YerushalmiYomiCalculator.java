/*
 * Zmanim Java API
 * Copyright (C) 2017 - 2021 Eliyahu Hershfeld
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
 * This class calculates the <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud">Talmud Yerusalmi</a> <a href=
 * "https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> page ({@link Daf}) for the a given date.
 * 
 * @author &copy; elihaidv
 * @author &copy; Eliyahu Hershfeld 2017 - 2021
 */
public class YerushalmiYomiCalculator {

	/**
	 * The start date of the first Daf Yomi Yerushalmi cycle of February 2, 1980 / 15 Shevat, 5740.
	 */
	private final static Calendar DAF_YOMI_START_DAY = new GregorianCalendar(1980, Calendar.FEBRUARY, 2);
	/** The number of milliseconds in a day. */
	private final static int DAY_MILIS = 1000 * 60 * 60 * 24;
	/** The number of pages in the Talmud Yerushalmi.*/
	private final static int WHOLE_SHAS_DAFS = 1554;
	/** The number of pages per <em>masechta</em> (tractate).*/
	private final static int[] BLATT_PER_MASSECTA = {
			68, 37, 34, 44, 31, 59, 26, 33, 28, 20, 13, 92, 65, 71, 22, 22, 42, 26, 26, 33, 34, 22,
			19, 85, 72, 47, 40, 47, 54, 48, 44, 37, 34, 44, 9, 57, 37, 19, 13};




	private final static Calendar New_DAF_YOMI_START_DAY = new GregorianCalendar(1980, Calendar.FEBRUARY, 2);

	private final static int New_WHOLE_SHAS_DAFS() {
		int sum = 0;
		for (int j = 0; j < New_BLATT_PER_MASSECTA.length; j++) {
			sum += BLATT_PER_MASSECTA[j];

		}
		return sum;
	}

	/*

		private static String[] masechtosYerushalmiTransliterated = {
		"Berachos",
		 "Pe'ah",
		  "Demai",
		  "Kilayim",
		   "Shevi'is",
			"Terumos",
			"Ma'asros",
			 "Ma'aser Sheni",
			 "Chalah",
			  "Orlah",
			   "Bikurim",

			    "Shabbos",
			     "Eruvin",
			      "Pesachim",

			"Beitzah",

			 "Rosh Hashanah",
			  "Yoma",
			   "Sukah",
			    "Ta'anis",

			     "Shekalim",
			      "Megilah",
			       "Chagigah",
			       "Moed Katan",

			"Yevamos",
			"Kesuvos",
			"Sotah",
			"Nedarim",
			 "Nazir",
			  "Gitin",
			  "Kidushin",

			   "Bava Kama",
			    "Bava Metzia",

			"Bava Basra",
			 "Sanhedrin",
			  "Makos",
			   "Shevuos",
			    "Avodah Zarah",
			     "Horayos",
			      "Nidah", "No Daf Today" };

// artscroll
'2023', 'W', 'טבש', 'דכ', 'Feb', '15', 'Berachos', '94'
'2023', 'SH', 'רייא', 'ח', 'Apr', '29', 'Peah', '73'
'2023', 'SH', 'זומת', 'וכ', 'July', '15', 'Demai', '77'
'2023', 'SH', 'ירשת', 'בכ', 'Oct', '7', 'Kilayim', '84'
'2024', 'T', 'תבט', 'אכ', 'Jan', '2', 'Shevi’is', '87'
'2024', 'TH', 'ןסינ', 'י', 'Apr', '18', 'Terumos', '107'
'2024', 'M', 'רייא', 'וכ', 'June', '3', 'Maasros', '46'
'2024', 'TH', 'זומת', 'וכ', 'Aug', '1', 'Maaser', 'Sheni', '59'
'2024', 'TH', 'לולא', 'זט', 'Sep', '19', 'Challah', '49'
'2024', 'TH', 'ירשת', 'טכ', 'Oct', '31', 'Orlah', '42'
'2024', 'T', 'ןושח', 'הכ', 'Nov', '26', 'Bikkurim', '26'

'2025', 'W', 'רדא', 'טי', 'Mar', '19', 'Shabbos113'
'2025', 'TH', 'ןויס', 'ב', 'May', '29', 'Eruvin', '71'
'2025', 'SH', 'בא', 'טכ', 'Aug', '23', 'Pesachim', '86'

'2025', 'TH', 'ןושח', 'א', 'Oct', '23', 'Shekalim', '61'
'2025', 'F', 'ולסכ', 'טכ', 'Dec', '19', 'Yoma', '57'
'2026', 'W', 'טבש', 'ג', 'Jan', '21', 'Succah', '33'
'2026', 'W', 'רדא', 'בכ', 'Mar', '11', 'Beitzah49'
'2026', 'T', 'ןסינ', 'כ', 'Apr', '7', 'Rosh', 'Hashanah', '27'
'2026', 'F', 'רייא', 'אכ', 'May', '8', 'Taanis', '31'


'2026', 'TH', 'זומת', 'ג', 'June', '18', 'Megillah', '41'
'2026', 'TH', 'בא', 'ב', 'July', '16', 'Chagigah', '28'
'2026', 'SH', 'בא', 'הכ', 'Aug', '8', 'Moed', 'Katan', '23'

'2026', 'W', 'ןושח', 'דכ', 'Nov', '4', 'Yevamos', '88'
'2027', 'W', 'טבש', 'בי', 'Jan', '20', 'Kesubos', '77'
'2027', 'W', 'א', 'רדא', 'דכ', 'Mar', '3', 'Nedarim', '42'
'2027', 'S', 'ןסינ', 'חי', 'Apr', '25', 'Nazir', '53'
'2027', 'W', 'ןויס', 'אי', 'June', '16', 'Sotah52'
'2027', 'S', 'בא', 'ה', 'Aug', '8', 'Gittin', '53'
'2027', 'TH', 'לולא', 'חכ', 'Sep', '30', 'Kiddushin', '53'
'2027', 'T', 'ןושח', 'ט', 'Nov', '9', 'Bava', 'Kamma', '40'
'2027', 'T', 'ולסכ', 'די', 'Dec', '14', 'Bava', 'Metzia', '35'
'2028', 'SH', 'תבט', 'גכ', 'Jan', '22', 'Bava', 'Basra', '39'
'2028', 'TH', 'ןסינ', 'י', 'Apr', '6', 'Sanhedrin', '75'
'2028', 'TH', 'רייא', 'טכ', 'May', '25', 'Shevuos', '49'
'2028', 'W', 'זומת', 'ד', 'June', '28', 'Avodah', 'Zarah', '34'
'2028', 'S', 'זומת', 'וט', 'July', '9', 'Makkos', '11'
'2028', 'TH', 'בא', 'ד', 'July', '27', 'Horayos', '18'
'2028', 'M', 'בא', 'וט', 'Aug', '7', 'Niddah', '11'



// orig

01/20/23	Fri	27 Teves 5783	Berachos 68
02/26/23	Sun	5 Adar 5783	Pe'ah 37
04/01/23	Shab	10 Nisan 5783	Demai 34
05/15/23	Mon	24 Iyar 5783	Kil'ayim 44
06/15/23	Thu	26 Sivan 5783	Shevi'is 31
08/14/23	Mon	27 Av 5783	Terumos 59
09/09/23	Shab	23 Elul 5783	Ma'asros 26
10/13/23	Fri	28 Tishrei 5784	Ma'aser Sheni 33
11/10/23	Fri	26 Cheshvan 5784	Chalah 28
11/30/23	Thu	17 Kislev 5784	Orlah 20
12/13/23	Wed	1 Teves 5784	Bikurim 13
03/14/24	Thu	4 AdarII 5784	Shabbos 92
05/18/24	Shab	10 Iyar 5784	Eruvin 65
07/28/24	Sun	22 Tamuz 5784	Pesachim 71
08/20/24	Tue	16 Av 5784	Beitzah 22
09/11/24	Wed	8 Elul 5784	Rosh Hashanah 22
10/24/24	Thu	22 Tishrei 5785	Yoma 42
11/19/24	Tue	18 Cheshvan 5785	Sukah 26
12/15/24	Sun	14 Kislev 5785	Ta'anis 26
01/17/25	Fri	17 Teves 5785	Shekalim 33
02/20/25	Thu	22 Shevat 5785	Megilah 34
03/14/25	Fri	14 Adar 5785	Chagigah 22
04/02/25	Wed	4 Nisan 5785	Moed Katan 19
06/26/25	Thu	30 Sivan 5785	Yevamos 85
09/07/25	Sun	14 Elul 5785	Kesuvos 72
10/25/25	Shab	3 Cheshvan 5786	Sotah 47
12/04/25	Thu	14 Kislev 5786	Nedarim 40
01/20/26	Tue	2 Shevat 5786	Nazir 47
03/15/26	Sun	26 Adar 5786	Gitin 54
05/02/26	Shab	15 Iyar 5786	Kidushin 48
06/15/26	Mon	30 Sivan 5786	Bava Kama 44
07/22/26	Wed	8 Av 5786	Bava Metzia 37
08/26/26	Wed	13 Elul 5786	Bava Basra 34
10/10/26	Shab	29 Tishrei 5787	Shevuos 44
10/19/26	Mon	8 Cheshvan 5787	Makos 9
12/15/26	Tue	5 Teves 5787	Sanhedrin 57
01/21/27	Thu	13 Shevat 5787	Avodah Zarah 37
02/09/27	Tue	2 AdarI 5787	Horayos 19
02/22/27	Mon	15 AdarI 5787	Nidah 13

	 */
	private final static int[] New_BLATT_PER_MASSECTA = {
			94, 73, //  "Berachos", "Pe'ah",
			77, 84, // "Demai", "Kilayim",
			87, 107, // "Shevi'is", "Terumos",
			46, 59, //  "Ma'asros", "Ma'aser Sheni",
			49, 42, // "Chalah", "Orlah",
			26,  //  "Bikurim",

			113, 71, // "Shabbos", "Eruvin",
			86, 61, // "Pesachim", "Shekalim",
			57, 33, // "Yoma","Sukah",
			49, 27, //beitzah "Rosh Hashanah"
			31, 41, // tanis megillah
			28, 23, // chagiga moded kattan

			88, 77, // yevamos kesubos
			42, 53, // nedarim nazir
			52, 53, // sota gittin
			53,     // kedushin

			40, // baba kama
			35, 39, // babametzia bababasra
			75, 49, // sanhedrin shevous
			34, 11, // avodah zarah makkos
			18, 11 // horoyos nidda
			};
			//, // horoyos  nidda
			//13};
	/**
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
		
		Calendar nextCycle = new GregorianCalendar();
		Calendar prevCycle = new GregorianCalendar();
		Calendar requested = calendar.getGregorianCalendar();
		int masechta = 0;
		Daf dafYomi = null;

		// There isn't Daf Yomi on Yom Kippur or Tisha B'Av.
		if ( calendar.getYomTovIndex() == JewishCalendar.YOM_KIPPUR ||
				calendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV ) {
			return null;
		}
		
		
		if (requested.before(DAF_YOMI_START_DAY)) {
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


	public static Daf getDafYomiYerushalmiNew(JewishCalendar calendar) {

		Calendar nextCycle = new GregorianCalendar();
		Calendar prevCycle = new GregorianCalendar();
		Calendar requested = calendar.getGregorianCalendar();
		int masechta = 0;
		Daf dafYomi = null;


		if (requested.before(New_DAF_YOMI_START_DAY)) {
			return getDafYomiYerushalmi(calendar);
		}

		// Start to calculate current cycle. init the start day
		nextCycle.setTime(New_DAF_YOMI_START_DAY.getTime());

		// Go cycle by cycle, until we get the next cycle
		while (requested.after(nextCycle)) {
			prevCycle.setTime(nextCycle.getTime());

			// Adds the number of whole shas dafs. and the number of days that not have daf.
			nextCycle.add(Calendar.DAY_OF_MONTH, New_WHOLE_SHAS_DAFS());
		}

		// Get the number of days from cycle start until request.
		int dafNo = (int)(getDiffBetweenDays(prevCycle, requested));

		// Get the number of special day to subtract
		int specialDays = getNumOfSpecialDays(prevCycle, requested);
		int total = dafNo - specialDays;

		// Finally find the daf.
		for (int j = 0; j < New_BLATT_PER_MASSECTA.length; j++) {

			if (total <= New_BLATT_PER_MASSECTA[j]) {
				dafYomi = new Daf(masechta, total + 1);
				break;
			}
			total -= New_BLATT_PER_MASSECTA[j];
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
