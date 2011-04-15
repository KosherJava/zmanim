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
 * This open source Java code was originally ported by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a>
 * from his C++ code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements
 * and some bug fixing.
 * 
 * The methods used to obtain the parsha were derived from the source code of <a
 * href="http://www.sadinoff.com/hebcal/">HebCal</a> by Danny Sadinoff and JCal for the Mac by Frank Yellin. Both based
 * their code on routines by Nachum Dershowitz and Edward M. Reingold. The class allows setting whether the parsha and
 * holiday scheme follows the Israel scheme or outside Israel scheme. The default is the outside Israel scheme.
 * 
 * <b>TODO:</b> Some do not belong in this class, but here is a partial list of what should still be implemented in some
 * form:
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
	public static final int EREV_PESACH = 0;
	public static final int PESACH = 1;
	public static final int CHOL_HAMOED_PESACH = 2;
	public static final int PESACH_SHENI = 3;
	public static final int EREV_SHAVUOS = 4;
	public static final int SHAVUOS = 5;
	public static final int SEVENTEEN_OF_TAMMUZ = 6;
	public static final int TISHA_BEAV = 7;
	public static final int TU_BEAV = 8;
	public static final int EREV_ROSH_HASHANA = 9;
	public static final int ROSH_HASHANA = 10;
	public static final int FAST_OF_GEDALYAH = 11;
	public static final int EREV_YOM_KIPPUR = 12;
	public static final int YOM_KIPPUR = 13;
	public static final int EREV_SUCCOS = 14;
	public static final int SUCCOS = 15;
	public static final int CHOL_HAMOED_SUCCOS = 16;
	public static final int HOSHANA_RABBA = 17;
	public static final int SHEMINI_ATZERES = 18;
	public static final int SIMCHAS_TORAH = 19;
	public static final int EREV_CHANUKAH = 20;// probably remove this
	public static final int CHANUKAH = 21;
	public static final int TENTH_OF_TEVES = 22;
	public static final int TU_BESHVAT = 23;
	public static final int FAST_OF_ESTHER = 24;
	public static final int PURIM = 25;
	public static final int SHUSHAN_PURIM = 26;
	public static final int PURIM_KATAN = 27;
	public static final int ROSH_CHODESH = 28;
	public static final int YOM_HASHOAH = 29;
	public static final int YOM_HAZIKARON = 30;
	public static final int YOM_HAATZMAUT = 31;
	public static final int YOM_YERUSHALAYIM = 32;

	private boolean inIsrael = false;
	private boolean useModernHolidays = false;

	/**
	 * Is this calendar set to return modern Israeli national holidays. By default this value is false. The holidays
	 * are: "Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut" and "Yom Yerushalayim"
	 * 
	 * @return the useModernHolidays true if set to return modern Israeli national holidays
	 */
	public boolean isUseModernHolidays() {
		return useModernHolidays;
	}

	/**
	 * Seth the calendar to return modern Israeli national holidays. By default this value is false. The holidays are:
	 * "Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut" and "Yom Yerushalayim"
	 * 
	 * @param useModernHolidays
	 *            the useModernHolidays to set
	 */
	public void setUseModernHolidays(boolean useModernHolidays) {
		this.useModernHolidays = useModernHolidays;
	}

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
	 *             if the day of month is < 1 or > 30, or a year of < 0 is passed in.
	 */
	public JewishCalendar(int jewishYear, int jewishMonth, int jewishDayOfMonth) {
		super(jewishYear, jewishMonth, jewishDayOfMonth);
	}

	/**
	 * Creates a Jewish date based on a Jewish date and whether in Israel
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
	 * @param inIsrael
	 *            whether in Israel. This affects Yom Tov and Parsha calculations
	 */
	public JewishCalendar(int jewishYear, int jewishMonth, int jewishDayOfMonth, boolean inIsrael) {
		super(jewishYear, jewishMonth, jewishDayOfMonth);
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
	 * Returns an index of the Jewish holiday or fast day for the current day, or a null if there is no holiday for this
	 * day.
	 * 
	 * @return A String containing the holiday name or an empty string if it is not a holiday.
	 */
	public int getYomTovIndex() {
		// check by month (starts from Nissan)
		switch (getJewishMonth()) {
		case NISSAN:
			if (getJewishDayOfMonth() == 14) {
				return EREV_PESACH;
			} else if (getJewishDayOfMonth() == 15 || getJewishDayOfMonth() == 21
					|| (!inIsrael && (getJewishDayOfMonth() == 16 || getJewishDayOfMonth() == 22))) {
				return PESACH;
			} else if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20
					|| (getJewishDayOfMonth() == 16 && inIsrael)) {
				return CHOL_HAMOED_PESACH;
			}
			if (isUseModernHolidays()
					&& ((getJewishDayOfMonth() == 26 && getDayOfWeek() == 5)
							|| (getJewishDayOfMonth() == 28 && getDayOfWeek() == 1)
							|| (getJewishDayOfMonth() == 27 && getDayOfWeek() == 3) || (getJewishDayOfMonth() == 27 && getDayOfWeek() == 5))) {
				return YOM_HASHOAH;
			}
			break;
		case IYAR:
			if (isUseModernHolidays()
					&& ((getJewishDayOfMonth() == 4 && getDayOfWeek() == 3) || ((getJewishDayOfMonth() == 3 || getJewishDayOfMonth() == 2) && getDayOfWeek() == 4))
					|| (getJewishDayOfMonth() == 5 && getDayOfWeek() == 2)) {
				return YOM_HAZIKARON;
			}
			// if 5 Iyar falls on Wed Yom Haatzmaut is that day. If it fal1s on Friday or Shabbos it is moved back to
			// Thursday. If it falls on Monday it is moved to Tuesday
			if (isUseModernHolidays()
					&& ((getJewishDayOfMonth() == 5 && getDayOfWeek() == 4) || ((getJewishDayOfMonth() == 4 || getJewishDayOfMonth() == 3) && getDayOfWeek() == 5))
					|| (getJewishDayOfMonth() == 6 && getDayOfWeek() == 3)) {
				return YOM_HAATZMAUT;
			}
			if (getJewishDayOfMonth() == 14) {
				return PESACH_SHENI;
			}
			if (isUseModernHolidays() && getJewishDayOfMonth() == 28) {
				return YOM_YERUSHALAYIM;
			}
			break;
		case SIVAN:
			if (getJewishDayOfMonth() == 5) {
				return EREV_SHAVUOS;
			} else if (getJewishDayOfMonth() == 6 || (getJewishDayOfMonth() == 7 && !inIsrael)) {
				return SHAVUOS;
			}
			break;
		case TAMMUZ:
			// push off the fast day if it falls on Shabbos
			if ((getJewishDayOfMonth() == 17 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 18 && getDayOfWeek() == 1)) {
				return SEVENTEEN_OF_TAMMUZ;
			}
			break;
		case AV:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((getDayOfWeek() == 1 && getJewishDayOfMonth() == 10)
					|| (getDayOfWeek() != 7 && getJewishDayOfMonth() == 9)) {
				return TISHA_BEAV;
			} else if (getJewishDayOfMonth() == 15) {
				return TU_BEAV;
			}
			break;
		case ELUL:
			if (getJewishDayOfMonth() == 29) {
				return EREV_ROSH_HASHANA;
			}
			break;
		case TISHREI:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2) {
				return ROSH_HASHANA;
			} else if ((getJewishDayOfMonth() == 3 && getDayOfWeek() != 7)
					|| (getJewishDayOfMonth() == 4 && getDayOfWeek() == 1)) {
				// push off Tzom Gedalia if it falls on Shabbos
				return FAST_OF_GEDALYAH;
			} else if (getJewishDayOfMonth() == 9) {
				return EREV_YOM_KIPPUR;
			} else if (getJewishDayOfMonth() == 10) {
				return YOM_KIPPUR;
			} else if (getJewishDayOfMonth() == 14) {
				return EREV_SUCCOS;
			}
			if (getJewishDayOfMonth() == 15 || (getJewishDayOfMonth() == 16 && !inIsrael)) {
				return SUCCOS;
			}
			if (getJewishDayOfMonth() >= 17 && getJewishDayOfMonth() <= 20 || (getJewishDayOfMonth() == 16 && inIsrael)) {
				return CHOL_HAMOED_SUCCOS;
			}
			if (getJewishDayOfMonth() == 21) {
				return HOSHANA_RABBA;
			}
			if (getJewishDayOfMonth() == 22) {
				return SHEMINI_ATZERES;
			}
			if (getJewishDayOfMonth() == 23 && !inIsrael) {
				return SIMCHAS_TORAH;
			}
			break;
		case KISLEV: // no yomtov in CHESHVAN
			if (getJewishDayOfMonth() == 24) {
				return EREV_CHANUKAH;
			} else if (getJewishDayOfMonth() >= 25) {
				return CHANUKAH;
			}
			break;
		case TEVES:
			if (getJewishDayOfMonth() == 1 || getJewishDayOfMonth() == 2
					|| (getJewishDayOfMonth() == 3 && isKislevShort())) {
				return CHANUKAH;
			} else if (getJewishDayOfMonth() == 10) {
				return TENTH_OF_TEVES;
			}
			break;
		case SHEVAT:
			if (getJewishDayOfMonth() == 15) {
				return TU_BESHVAT;
			}
			break;
		case ADAR:
			if (!isJewishLeapYear()) {
				// if 13th Adar falls on Friday or Shabbos, push back to Thursday
				if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
						|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
					return FAST_OF_ESTHER;
				}
				if (getJewishDayOfMonth() == 14) {
					return PURIM;
				} else if (getJewishDayOfMonth() == 15) {
					return SHUSHAN_PURIM;
				}
			} else { // else if a leap year
				if (getJewishDayOfMonth() == 14) {
					return PURIM_KATAN;
				}
			}
			break;
		case ADAR_II:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 12) && getDayOfWeek() == 5)
					|| (getJewishDayOfMonth() == 13 && !(getDayOfWeek() == 6 || getDayOfWeek() == 7))) {
				return FAST_OF_ESTHER;
			}
			if (getJewishDayOfMonth() == 14) {
				return PURIM;
			} else if (getJewishDayOfMonth() == 15) {
				return SHUSHAN_PURIM;
			}
			break;
		}
		// if we get to this stage, then there are no holidays for the given date return -1
		return -1;
	}

	/**
	 * Returns a String of the Jewish holiday or fast day for the current day, or a null if there is no holiday for this
	 * day. Has no "modern" holidays.
	 * 
	 * @return A String containing the holiday name or an empty string if it is not a holiday.
	 */
	public String getYomTov() {
		return new HebrewDateFormatter().formatYomTov(this);
	}

	/**
	 * Returns true if the current day is Yom Tov. The method returns false for Chanukah, Erev Yom tov and fast days.
	 * 
	 * @return true if the current day is a Yom Tov
	 * @see #isErevYomTov()
	 * @see #isTaanis()
	 */
	public boolean isYomTov() {
		int holidayIndex = getYomTovIndex();
		if (isErevYomTov() || holidayIndex == CHANUKAH || (isTaanis() && holidayIndex != YOM_KIPPUR)) {
			return false;
		}
		return getYomTovIndex() != -1;
	}

	/**
	 * Returns true if the current day is erev Yom Tov. The method returns true for Erev - Pesach, Shavuos, Rosh
	 * Hashana, Yom Kippur and Succos.
	 * 
	 * @return true if the current day is Erev - Pesach, Shavuos, Rosh Hashana, Yom Kippur and Succos
	 * @see #isYomTov()
	 */
	public boolean isErevYomTov() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == EREV_PESACH || holidayIndex == EREV_SHAVUOS || holidayIndex == EREV_ROSH_HASHANA
				|| holidayIndex == EREV_YOM_KIPPUR || holidayIndex == EREV_SUCCOS;
	}

	/**
	 * Returns true if the current day is Erev Rosh Chodesh. Returns false for Erev Rosh Hashana
	 * 
	 * @return true if the current day is Erev Rosh Chodesh. Returns false for Erev Rosh Hashana
	 * @see #isRoshChodesh()
	 */
	public boolean isErevRoshChodesh() {
		// Erev Rosh Hashana is not Erev Rosh Chodesh.
		return (getJewishDayOfMonth() == 29 && getJewishMonth() != ELUL);
	}

	/**
	 * Return true if the day is a Taanis (fast day). Return true for 17 of Tammuz, Tisha B'Av, Yom Kippur, Fast of
	 * Gedalyah, 10 of Teves and the Fast of Esther
	 * 
	 * @return true if today is a fast day
	 */
	public boolean isTaanis() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == SEVENTEEN_OF_TAMMUZ || holidayIndex == TISHA_BEAV || holidayIndex == YOM_KIPPUR
				|| holidayIndex == FAST_OF_GEDALYAH || holidayIndex == TENTH_OF_TEVES || holidayIndex == FAST_OF_ESTHER;
	}

	/**
	 * Returns the day of Chanukah or -1 if it is not Chanukah. To find out of the day is Erev Chanukah check if
	 * {@link #getYomTovIndex()} == {@link #EREV_CHANUKAH}.
	 * 
	 * @return the day of Chanukah or -1 if it is not Chanukah.
	 */
	public int getDayOfChanukah() {
		if (getYomTovIndex() == CHANUKAH) {
			if (getJewishMonth() == KISLEV) {
				return getJewishDayOfMonth() - 24;
			} else { // teves
				return isKislevShort() ? getJewishDayOfMonth() + 5 : getJewishDayOfMonth() + 4;
			}
		} else {
			return -1;
		}
	}

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
	 * Returns a string of the current parsha(ios) transliterated into Latin chars using Ashkenazi pronunciation. The
	 * {@link HebrewDateFormatter#formatParsha(JewishCalendar)} offers various formatting options for the Parsha.
	 * 
	 * @return the current parsha(ios) transliterated into Latin chars
	 * @see HebrewDateFormatter#formatParsha(JewishCalendar)
	 */
	public String getParsha() {
		return new HebrewDateFormatter().formatParsha(this);
	}

	/**
	 * Returns a the index of today's parsha(ios) or a -1 if there is none. To get the name of the Parsha, use the
	 * {@link HebrewDateFormatter#formatParsha(JewishCalendar)}.
	 * 
	 * TODO: consider possibly return the parsha of the week for any day during the week instead of empty. To do this
	 * the simple way, create a new instance of the class in the mothod, roll it to the next shabbos. If the shabbos has
	 * no parsha, keep rolling by a week till a parsha is encountered. Possibly turn into static method that takes in a
	 * year, month, day, roll to next shabbos (not that simple with the API for date passed in) and if it is not a
	 * shabbos roll forwarde one week at a time to get the parsha. I do not think it is possible to have more than 2
	 * shabbosim in a row without a parsha, but I may be wrong.
	 * 
	 * @return the string of the parsha. Will currently return blank for weekdays and a shabbos on a yom tov.
	 */
	public int getParshaIndex() {
		// if today is not Shabbos, then there is no normal parsha reading. If
		// commented our will return LAST week's parsha for a non shabbos
		if (getDayOfWeek() != 7) {
			// return "";
			return -1;
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
		if (isCheshvanLong() && !isKislevShort()) {
			kvia = 2;
		} else if (!isCheshvanLong() && isKislevShort()) {
			kvia = 0;
		} else {
			kvia = 1;
		}
		// determine appropriate array
		if (!isJewishLeapYear()) {
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
		} else { // if leap year
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
		// if (index == -1) {
		// return -1;
		// }

		// if parsha this week
		// else {
		// if (getDayOfWeek() != 7){//in weekday return next shabbos's parsha
		// System.out.print(" index=" + index + " ");
		// return parshios[index + 1];
		// this code returns odd data for yom tov. See parshas kedoshim display
		// for 2011 for example. It will also break for Sept 25, 2011 where it
		// goes one beyong the index of Nitzavim-Vayelech
		// }
		// return parshios[index];
		return index;
		// }
	}

	/**
	 * Returns if the day is Rosh Chodesh.
	 * 
	 * @return true if it is Rosh Chodesh. Rosh Hashana will return false
	 */
	public boolean isRoshChodesh() {
		// Rosh Hashana is not rosh chodesh. Elul never has 30 days
		return (getJewishDayOfMonth() == 1 && getJewishMonth() != TISHREI) || getJewishDayOfMonth() == 30;
	}

	/**
	 * Returns the int value of the Omer day or -1 if the day is not in the omer
	 * 
	 * @return The Omer count as an int or -1 if it is not a day of the Omer.
	 */
	public int getDayOfOmer() {
		int omer = -1; // not a day of the Omer

		// if Nissan and second day of Pesach and on
		if (getJewishMonth() == NISSAN && getJewishDayOfMonth() >= 16) {
			omer = getJewishDayOfMonth() - 15;
			// if Iyar
		} else if (getJewishMonth() == IYAR) {
			omer = getJewishDayOfMonth() + 15;
			// if Sivan and before Shavuos
		} else if (getJewishMonth() == SIVAN && getJewishDayOfMonth() < 6) {
			omer = getJewishDayOfMonth() + 44;
		}
		return omer;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JewishCalendar)) {
			return false;
		}
		JewishCalendar jewishCalendar = (JewishCalendar) object;
		return getAbsDate() == jewishCalendar.getAbsDate() && getInIsrael() == jewishCalendar.getInIsrael();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getClass().hashCode(); // needed or this and
														// subclasses will
														// return identical hash
		result += 37 * result + getAbsDate() + (getInIsrael() ? 1 : 3);
		return result;
	}
}