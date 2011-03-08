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

import java.util.Calendar;
import java.util.Date;

/**
 * The JewishCalendar extends the JewishDate class and adds calendar methods.
 * 
 * This open source Java code was originally ported by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a> from his C++
 * code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements and some bug
 * fixing.
 * 
 * The methods used to obtain the parsha were derived from the source code of <a
 * href="http://www.sadinoff.com/hebcal/">HebCal</a> by Danny Sadinoff and JCal for the Mac by Frank Yellin. Both based
 * their code on routines by Nachum Dershowitz and Edward M. Reingold. The class allows setting whether the parsha and
 * holiday scheme follows the Israel scheme or outside Israel scheme.
 * 
 * <b>TODO:</b> Some do not belong in this class, but here is a partial list of what should still be implemented in
 * some form:
 * <ol>
 * <li>Add special parshiyos (shekalim, parah, zachor and hachodesh</li>
 * <li>Molad / Shabbos Mevarchim)</li>
 * <li>Haftorah (various minhagim)</li>
 * <li>Daf Yomi (Bavli, Yerushalmi, Mishna yomis etc)</li>
 * <li>Support showing the upcoming parsha for the middle of the week</li>
 * </ol>
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.0.1
 */
public class JewishCalendar extends JewishDate {
	private boolean inIsrael = false;

	/**
	 * Default constructor will set a default date to the current system date.
	 */
	public JewishCalendar() {
		super();
	}

	/**
	 * A constructor that initializes the date to the {@link java.util.Date Date} parameter.
	 * 
	 * @param date
	 *            the <code>Date</code> to set the calendar to
	 */
	public JewishCalendar(Date date) {
		super(date);
	}

	/**
	 * A constructor that initializes the date to the {@link java.util.Calendar Calendar} parameter.
	 * 
	 * @param calendar
	 *            the <code>Calendar</code> to set the calendar to
	 */
	public JewishCalendar(Calendar calendar) {
		super(calendar);
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
	public JewishCalendar(int gregorianYear, int gregorianMonth, int gregorianDayOfMonth) {
		super(gregorianYear, gregorianMonth, gregorianDayOfMonth);
	}

	/**
	 * Creates a Jewish date based on Gregorian date and whether in Israel
	 * 
	 * @param gregorianYear
	 *            the Gregorian year
	 * @param gregorianMonth
	 *            the Gregorian month. Unlike the Java Calendar where January has the value of 0,This expects a 1 for
	 *            January
	 * @param gregorianDayOfMonth
	 *            the Gregorian day of month. If this is > the number of days in the month/year, the last valid date of
	 *            the month will be set
	 * @param inIsrael
	 *            whether in Israel. This affects Yom Tov and Parsha calculations
	 */
	public JewishCalendar(int gregorianYear, int gregorianMonth, int gregorianDayOfMonth, boolean inIsrael) {
		super(gregorianYear, gregorianMonth, gregorianDayOfMonth);
		setInIsrael(inIsrael);
	}

	/**
	 * Sets whether to use Israel parsha and holiday scheme or not. Default is false.
	 * 
	 * @param inIsrael
	 *            set to true for calculations for Israel
	 */
	public void setInIsrael(boolean inIsrael) {
		this.inIsrael = inIsrael;
	}

	/**
	 * Gets whether Israel parsha and holiday scheme is used or not. The default (if not set) is false.
	 * 
	 * @return if the if the calendar is set to Israel
	 */
	public boolean getInIsrael() {
		return inIsrael;
	}

	/**
	 * Returns a String of the Jewish holiday or fast day for the current day, or a null if there is no holiday for this
	 * day. Has no "modern" holidays.
	 * 
	 * @return A String containing the holiday name or an empty string if it is not a holiday.
	 */
	public String getHoliday() {
		// check by month (starts from Nissan)
		switch (getJewishMonth()) {
		case 1:
			if (getJewishDayOfMonth() == 14) {
				return "Erev Pesach";
			} else if (getJewishDayOfMonth() == 15 || getJewishDayOfMonth() == 21
					|| (!inIsrael && (getJewishDayOfMonth() == 16 || getJewishDayOfMonth() == 22))) {
				return "Pesach";
			} else if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20
					|| (getJewishDayOfMonth() == 16 && inIsrael)) {
				return "Chol Hamoed Pesach";
			}
			break;
		case 2:
			if (getJewishDayOfMonth() == 14) {
				return "Pesach Sheni";
			}
			break;
		case 3:
			if (getJewishDayOfMonth() == 5) {
				return "Erev Shavuos";
				// if (ashkenaz) {
				// return "Erev Shavuos";
				// } else {
				// return "Erev Shavuot";
				// }
			} else if (getJewishDayOfMonth() == 6 || (getJewishDayOfMonth() == 7 && !inIsrael)) {
				return "Shavuos";
				// if (ashkenaz) {
				// return "Shavuos";
				// } else {
				// return "Shavuot";
				// }
			}
			break;
		case 4:
			// push off the fast day if it falls on Shabbos
			if ((getJewishDayOfMonth() == 17 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 18 && getDayOfWeek() == 1)) {
				return "Tzom Tammuz";
			}
			break;
		case 5:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((getDayOfWeek() == 1 && getJewishDayOfMonth() == 10)
					|| (getDayOfWeek() != 7 && getJewishDayOfMonth() == 9)) {
				return "Tisha B'av";
			} else if (getJewishDayOfMonth() == 15) {
				return "Tu B'Av";
			}
			break;
		case 6:
			if (getJewishDayOfMonth() == 29) {
				return "Erev Rosh Hashanah";
			}
			break;
		case 7:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2) {
				return "Rosh Hashanah";
			} else if ((getJewishDayOfMonth() == 3 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 4 && getDayOfWeek() == 1)) { // push
																				// off
																				// Tzom
				// Gedalia
				// if it falls on
				// Shabbos
				return "Tzom Gedalia";
			} else if (getJewishDayOfMonth() == 9) {
				return "Erev Yom Kippur";
			} else if (getJewishDayOfMonth() == 10) {
				return "Yom Kippur";
			} else if (getJewishDayOfMonth() == 14) {
				return "Erev Sukkos";
				// if (ashkenaz) {
				// return "Erev Sukkos";
				// } else {
				// return "Erev Sukkot";
				// }
			}
			if (getJewishDayOfMonth() == 15 || (getJewishDayOfMonth() == 16 && !inIsrael)) {
				return "Sukkos";
				// if (ashkenaz) {
				// return "Sukkos";
				// } else {
				// return "Sukkot";
				// }
			}
			if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20 || (getJewishDayOfMonth() == 16 && inIsrael)) {
				return "Chol Hamoed Sukkos";
				// if (ashkenaz) {
				// return "Chol Hamoed Sukkos";
				// } else {
				// return "Chol Hamoed Sukkot";
				// }
			}
			if (getJewishDayOfMonth() == 21) {
				return "Hoshana Rabah";
			}
			if (getJewishDayOfMonth() == 22) {
				return "Shmini Atzeres";
				// if (ashkenaz) {
				// return "Shmini Atzeres";
				// } else {
				// return "Shmini Atzeret";
				// }
			}
			if (getJewishDayOfMonth() == 23 && !inIsrael) {
				return "Simchas Torah";
				// if (ashkenaz) {
				// return "Simchas Torah";
				// } else {
				// return "Simchat Torah";
				// }
			}
			break;
		case 9:
			if (getJewishDayOfMonth() == 24) {
				return "Erev Chanukah";
			} else if (getJewishDayOfMonth() >= 25) {
				return "Chanukah";
			}
			break;
		case 10:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2
					|| (getJewishDayOfMonth() == 3 && isKislevShort(getJewishYear()))) {
				return "Chanukah";
			} else if (getJewishDayOfMonth() == 10) {
				return "Asarah BeTeves";
				// if (ashkenaz) {
				// return "Tzom Teves";
				// } else {
				// return "Tzom Tevet";
				// }
			}
			break;
		case 11:
			if (getJewishDayOfMonth() == 15) {
				return "Tu B'Shvat";
			}
			break;
		case 12:
			if (!isJewishLeapYear(getJewishYear())) {
				// if 13th Adar falls on Friday or Shabbos, push back to
				// Thursday
				if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
						|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
					return "Ta'anis Esther";
					// if (ashkenaz) {
					// return "Ta'anis Esther";
					// } else {
					// return "Ta'anit Esther";
					// }
				}
				if (getJewishDayOfMonth() == 14) {
					return "Purim";
				} else if (getJewishDayOfMonth() == 15) {
					return "Shushan Purim";
				}
			}
			// else if a leap year //
			else {
				if (getJewishDayOfMonth() == 14) {
					return "Purim Katan";
				}
			}
			break;
		case 13:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
					|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
				return "Ta'anis Esther";
				// if (ashkenaz) {
				// return "Ta'anis Esther";
				// } else {
				// return "Ta'anit Esther";
				// }
			}
			if (getJewishDayOfMonth() == 14) {
				return "Purim";
			} else if (getJewishDayOfMonth() == 15) {
				return "Shushan Purim";
			}
			break;
		}
		// if we get to this stage, then there are no holidays for the given
		// date
		// return "";
		return null;
	}

	public String getHoliday2() {
		// check by month (starts from Nissan)
		switch (getJewishMonth()) {
		case 1:
			if (getJewishDayOfMonth() == 14) {
				return "Erev Pesach";
			} else if (getJewishDayOfMonth() == 15 || getJewishDayOfMonth() == 21
					|| (!inIsrael && (getJewishDayOfMonth() == 16 || getJewishDayOfMonth() == 22))) {
				return "Pesach";
			} else if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20
					|| (getJewishDayOfMonth() == 16 && inIsrael)) {
				return "Chol Hamoed Pesach";
			}
			break;
		case 2:
			if (getJewishDayOfMonth() == 14) {
				return "Pesach Sheni";
			}
			break;
		case 3:
			if (getJewishDayOfMonth() == 5) {
				return "Erev Shavuos";
				// if (ashkenaz) {
				// return "Erev Shavuos";
				// } else {
				// return "Erev Shavuot";
				// }
			} else if (getJewishDayOfMonth() == 6 || (getJewishDayOfMonth() == 7 && !inIsrael)) {
				return "Shavuos";
				// if (ashkenaz) {
				// return "Shavuos";
				// } else {
				// return "Shavuot";
				// }
			}
			break;
		case 4:
			// push off the fast day if it falls on Shabbos
			if ((getJewishDayOfMonth() == 17 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 18 && getDayOfWeek() == 1)) {
				return "Tzom Tammuz";
			}
			break;
		case 5:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((getDayOfWeek() == 1 && getJewishDayOfMonth() == 10)
					|| (getDayOfWeek() != 7 && getJewishDayOfMonth() == 9)) {
				return "Tisha B'av";
			} else if (getJewishDayOfMonth() == 15) {
				return "Tu B'Av";
			}
			break;
		case 6:
			if (getJewishDayOfMonth() == 29) {
				return "Erev Rosh Hashanah";
			}
			break;
		case 7:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2) {
				return "Rosh Hashanah";
			} else if ((getJewishDayOfMonth() == 3 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 4 && getDayOfWeek() == 1)) { // push
																				// off
																				// Tzom
				// Gedalia
				// if it falls on
				// Shabbos
				return "Tzom Gedalia";
			} else if (getJewishDayOfMonth() == 9) {
				return "Erev Yom Kippur";
			} else if (getJewishDayOfMonth() == 10) {
				return "Yom Kippur";
			} else if (getJewishDayOfMonth() == 14) {
				return "Erev Sukkos";
				// if (ashkenaz) {
				// return "Erev Sukkos";
				// } else {
				// return "Erev Sukkot";
				// }
			}
			if (getJewishDayOfMonth() == 15 || (getJewishDayOfMonth() == 16 && !inIsrael)) {
				return "Sukkos";
				// if (ashkenaz) {
				// return "Sukkos";
				// } else {
				// return "Sukkot";
				// }
			}
			if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20 || (getJewishDayOfMonth() == 16 && inIsrael)) {
				return "Chol Hamoed Sukkos";
				// if (ashkenaz) {
				// return "Chol Hamoed Sukkos";
				// } else {
				// return "Chol Hamoed Sukkot";
				// }
			}
			if (getJewishDayOfMonth() == 21) {
				return "Hoshana Rabah";
			}
			if (getJewishDayOfMonth() == 22) {
				return "Shmini Atzeres";
				// if (ashkenaz) {
				// return "Shmini Atzeres";
				// } else {
				// return "Shmini Atzeret";
				// }
			}
			if (getJewishDayOfMonth() == 23 && !inIsrael) {
				return "Simchas Torah";
				// if (ashkenaz) {
				// return "Simchas Torah";
				// } else {
				// return "Simchat Torah";
				// }
			}
			break;
		case 9:
			if (getJewishDayOfMonth() == 24) {
				return "Erev Chanukah";
			} else if (getJewishDayOfMonth() >= 25) {
				return "Chanukah";
			}
			break;
		case 10:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2
					|| (getJewishDayOfMonth() == 3 && isKislevShort(getJewishYear()))) {
				return "Chanukah";
			} else if (getJewishDayOfMonth() == 10) {
				return "Asarah BeTeves";
				// if (ashkenaz) {
				// return "Tzom Teves";
				// } else {
				// return "Tzom Tevet";
				// }
			}
			break;
		case 11:
			if (getJewishDayOfMonth() == 15) {
				return "Tu B'Shvat";
			}
			break;
		case 12:
			if (!isJewishLeapYear(getJewishYear())) {
				// if 13th Adar falls on Friday or Shabbos, push back to
				// Thursday
				if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
						|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
					return "Ta'anis Esther";
					// if (ashkenaz) {
					// return "Ta'anis Esther";
					// } else {
					// return "Ta'anit Esther";
					// }
				}
				if (getJewishDayOfMonth() == 14) {
					return "Purim";
				} else if (getJewishDayOfMonth() == 15) {
					return "Shushan Purim";
				}
			}
			// else if a leap year //
			else {
				if (getJewishDayOfMonth() == 14) {
					return "Purim Katan";
				}
			}
			break;
		case 13:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
					|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
				return "Ta'anis Esther";
				// if (ashkenaz) {
				// return "Ta'anis Esther";
				// } else {
				// return "Ta'anit Esther";
				// }
			}
			if (getJewishDayOfMonth() == 14) {
				return "Purim";
			} else if (getJewishDayOfMonth() == 15) {
				return "Shushan Purim";
			}
			break;
		}
		// if we get to this stage, then there are no holidays for the given
		// date
		// return "";
		return null;
	}

	/**
	 * Use the formatting class for formatting Ashkenazi VS Sephardi English transliteration. Using the default will use
	 * the Ashkenazi pronounciation.
	 */
	private static final String[] parshios = { "Bereshis", "Noach", "Lech Lecha", "Vayera", "Chayei Sara", "Toldos",
			"Vayetzei", "Vayishlach", "Vayeshev", "Miketz", "Vayigash", "Vayechi", "Shemos", "Vaera", "Bo",
			"Beshalach", "Yisro", "Mishpatim", "Terumah", "Tetzaveh", "Ki Sisa", "Vayakhel", "Pekudei", "Vayikra",
			"Tzav", "Shmini", "Tazria", "Metzora", "Achrei Mos", "Kedoshim", "Emor", "Behar", "Bechukosai", "Bamidbar",
			"Nasso", "Beha'aloscha", "Sh'lach", "Korach", "Chukas", "Balak", "Pinchas", "Matos", "Masei", "Devarim",
			"Vaeschanan", "Eikev", "Re'eh", "Shoftim", "Ki Seitzei", "Ki Savo", "Nitzavim", "Vayeilech", "Ha'Azinu",
			"Vayakhel Pekudei", "Tazria Metzora", "Achrei Mos Kedoshim", "Behar Bechukosai", "Chukas Balak",
			"Matos Masei", "Nitzavim Vayeilech" };
	// These indices were originally included in the emacs 19 distribution.
	// These arrays determine the correct indices into the parsha names
	// -1 means no parsha that week, values > 52 means it is a double parsha
	private static final int[] Sat_short = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 53, 23, 24, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45, 46, 47,
			48, 49, 50 };

	private static final int[] Sat_long = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 53, 23, 24, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45, 46, 47,
			48, 49, 59 };

	private static final int[] Mon_short = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 53, 23, 24, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45, 46, 47, 48,
			49, 59 };

	private static final int[] Mon_long = // split
	{ 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23, 24, -1, 25, 54, 55,
			30, 56, 33, -1, 34, 35, 36, 37, 57, 40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Thu_normal = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 53, 23, 24, -1, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45, 46, 47,
			48, 49, 50 };
	private static final int[] Thu_normal_Israel = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 53, 23, 24, -1, 25, 54, 55, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45,
			46, 47, 48, 49, 50 };

	private static final int[] Thu_long = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 23, 24, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44, 45, 46, 47,
			48, 49, 50 };

	private static final int[] Sat_short_leap = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 58,
			43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Sat_long_leap = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, -1, 34, 35, 36, 37, 57, 40, 58,
			43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_short_leap = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, -1, 34, 35, 36, 37, 57, 40, 58, 43,
			44, 45, 46, 47, 48, 49, 59 };
	private static final int[] Mon_short_leap_Israel = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_long_leap = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 58,
			43, 44, 45, 46, 47, 48, 49, 50 };
	private static final int[] Mon_long_leap_Israel = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50 };

	private static final int[] Thu_short_leap = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, -1, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50 };

	private static final int[] Thu_long_leap = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, -1, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 59 };

	/**
	 * returns a string of today's parsha(ios) or an empty string if there are none. FIXME: consider possibly return the
	 * parsha of the week for any day during the week instead of empty. To do this the simple way, create a new instance
	 * of the class in the mothod, roll it to the next shabbos. If the shabbos has no parsha, keep rolling by a week
	 * till a parsha is encountered. Possibly turn into static method that takes in a year, month, day, roll to next
	 * shabbos (not that simple with the API for date passed in) and if it is not a shabbos roll forwarde one week at a
	 * time to get the parsha. I do not think it is possible to have more than 2 shabbosim in a row without a parsha,
	 * but I may be wrong.
	 * 
	 * @return the string of the parsha. Will currently return blank for weekdays and a shabbos on a yom tov.
	 */
	public String getParsha() {

		// if today is not Shabbos, then there is no normal parsha reading. If
		// commented our will return LAST week's parsha for a non shabbos
		if (getDayOfWeek() != 7) {
			return "";
		}

		// kvia = whether a Jewish year is short/regular/long (0/1/2)
		// roshHashana = Rosh Hashana of this Jewish year
		// roshHashanaDay= day of week Rosh Hashana was on this year
		// week= current week in Jewish calendar from Rosh Hashana
		// array= the correct index array for this Jewish year
		// index= the index number of the parsha name
		int kvia;
		int roshHashanaDay;
		int week;
		int[] array = null;
		int index;

		JewishDate roshHashana = new JewishDate();
		// set it to Rosh Hashana of this year
		roshHashana.setJewishDate(getJewishYear(), 7, 1);

		// get day Rosh Hashana was on
		roshHashanaDay = roshHashana.getDayOfWeek();

		// week is the week since the first Shabbos on or after Rosh Hashana
		week = (((getAbsDate() - roshHashana.getAbsDate()) - (7 - roshHashanaDay)) / 7);

		// get kvia
		if (isCheshvanLong(getJewishYear()) && !isKislevShort(getJewishYear())) {
			kvia = 2;
		} else if (!isCheshvanLong(getJewishYear()) && isKislevShort(getJewishYear())) {
			kvia = 0;
		} else {
			kvia = 1;
		}
		// determine appropriate array
		if (!isJewishLeapYear(getJewishYear())) {
			switch (roshHashanaDay) {
			case 7: // RH was on a Saturday
				if (kvia == 0) {
					array = Sat_short;
				} else if (kvia == 2) {
					array = Sat_long;
				}
				break;
			case 2: // RH was on a Monday
				if (kvia == 0) {
					array = Mon_short;
				} else if (kvia == 2) {
					array = inIsrael ? Mon_short : Mon_long;
				}
				break;
			case 3: // RH was on a Tuesday
				if (kvia == 1) {
					array = inIsrael ? Mon_short : Mon_long;
				}
				break;
			case 5: // RH was on a Thursday
				if (kvia == 1) {
					array = inIsrael ? Thu_normal_Israel : Thu_normal;
				} else if (kvia == 2) {
					array = Thu_long;
				}
				break;
			}
		}

		// if leap year //
		else {
			switch (roshHashanaDay) {
			case 7: // RH was on a Sat
				if (kvia == 0) {
					array = Sat_short_leap;
				} else if (kvia == 2) {
					array = inIsrael ? Sat_short_leap : Sat_long_leap;
				}
				break;
			case 2: // RH was on a Mon
				if (kvia == 0) {
					array = inIsrael ? Mon_short_leap_Israel : Mon_short_leap;
				} else if (kvia == 2) {
					array = inIsrael ? Mon_long_leap_Israel : Mon_long_leap;
				}
				break;
			case 3: // RH was on a Tue
				if (kvia == 1) {
					array = inIsrael ? Mon_long_leap_Israel : Mon_long_leap;
				}
				break;
			case 5: // RH was on a Thu
				if (kvia == 0) {
					array = Thu_short_leap;
				} else if (kvia == 2) {
					array = Thu_long_leap;
				}
				break;
			}
		}
		// if something goes wrong
		if (array == null) {
			throw new RuntimeException(
					"Unable to claculate the parsha. No index array matched any of the known types for the date: "
							+ toString());
		}
		// get index from array
		index = array[week];

		// If no Parsha this week
		if (index == -1) {
			return "";
		}

		// if parsha this week
		// else {
		// if (getDayOfWeek() != 7){//in weekday return next shabbos's parsha
		// System.out.print(" index=" + index + " ");
		// return parshios[index + 1];
		// this code returns odd data for yom tov. See parshas kedoshim display
		// for 2011 for example. It will also break for Sept 25, 2011 where it
		// goes one beyong the index of Nitzavim-Vayelech
		// }
		return parshios[index];
		// }
	}

	/**
	 * Returns if the day is Rosh Chodesh.
	 * 
	 * @return true if it is Rosh Chodesh. Rosh Hashana will return false
	 */
	public boolean isRoshChodesh() {
		// Rosh Hashana is not rosh chodesh. Elul never has 30 days
		return (getJewishDayOfMonth() == 1 && getJewishMonth() != 7) || getJewishDayOfMonth() == 30;
	}

	/**
	 * Returns the int value of the Omer day or {@link Integer#MIN_VALUE} if the day is not in the omer
	 * 
	 * @return The Omer count as an int or {@link Integer#MIN_VALUE} if it is not a day of the Omer.
	 */
	public int getDayOfOmer() {
		// int omer = 0;
		// better to use MIN_VALUE that is a better equivalent to null
		int omer = Integer.MIN_VALUE;

		// if Nissan and second day of Pesach and on
		if (getJewishMonth() == 1 && getJewishDayOfMonth() >= 16) {
			omer = getJewishDayOfMonth() - 15;
			// if Iyar
		} else if (getJewishMonth() == 2) {
			omer = getJewishDayOfMonth() + 15;
			// if Sivan and before Shavuos
		} else if (getJewishMonth() == 3 && getJewishDayOfMonth() < 6) {
			omer = getJewishDayOfMonth() + 44;
		}
		return omer;
	}
}