/*
 * Zmanim Java API
 * Copyright (C) 2011 - 2014 Eliyahu Hershfeld
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
package net.sourceforge.zmanim.hebrewcalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.sourceforge.zmanim.util.GeoLocation;

/**
 * The JewishCalendar extends the JewishDate class and adds calendar methods.
 * 
 * This open source Java code was originally ported by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a>
 * from his C++ code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements
 * and some bug fixing.
 * 
 * The class allows setting whether the holiday scheme follows the Israel scheme or outside Israel scheme. The default is the outside Israel scheme.
 * 
 * TODO: Some do not belong in this class, but here is a partial list of what should still be implemented in some form:
 * <ol>
 * <li>Add Isru Chag</li>
 * <li>Shabbos Mevarchim</li>
 * <li>Daf Yomi Yerushalmi, Mishna yomis etc)</li>
 * </ol>
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011 - 2016
 * @version 1.0.0
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
	// public static final int EREV_CHANUKAH = 20;// probably remove this
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
	 *             if the day of month is &lt; 1 or &gt; 30, or a year of &lt; 0 is passed in.
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
	 *            whether in Israel. This affects Yom Tov calculations
	 */
	public JewishCalendar(int jewishYear, int jewishMonth, int jewishDayOfMonth, boolean inIsrael) {
		super(jewishYear, jewishMonth, jewishDayOfMonth);
		setInIsrael(inIsrael);
	}

	/**
	 * Sets whether to use Israel holiday scheme or not. Default is false.
	 * 
	 * @param inIsrael
	 *            set to true for calculations for Israel
	 */
	public void setInIsrael(boolean inIsrael) {
		this.inIsrael = inIsrael;
	}

	/**
	 * Gets whether Israel holiday scheme is used or not. The default (if not set) is false.
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
							|| (getJewishDayOfMonth() == 28 && getDayOfWeek() == 2)
							|| (getJewishDayOfMonth() == 27 && getDayOfWeek() != 1 && getDayOfWeek() != 6))) {
				return YOM_HASHOAH;
			}
			break;
		case IYAR:
			if (isUseModernHolidays()
					&& ((getJewishDayOfMonth() == 4 && getDayOfWeek() == 3)
							|| ((getJewishDayOfMonth() == 3 || getJewishDayOfMonth() == 2) && getDayOfWeek() == 4) || (getJewishDayOfMonth() == 5 && getDayOfWeek() == 2))) {
				return YOM_HAZIKARON;
			}
			// if 5 Iyar falls on Wed Yom Haatzmaut is that day. If it fal1s on Friday or Shabbos it is moved back to
			// Thursday. If it falls on Monday it is moved to Tuesday
			if (isUseModernHolidays()
					&& ((getJewishDayOfMonth() == 5 && getDayOfWeek() == 4)
							|| ((getJewishDayOfMonth() == 4 || getJewishDayOfMonth() == 3) && getDayOfWeek() == 5) || (getJewishDayOfMonth() == 6 && getDayOfWeek() == 3))) {
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
			// if (getJewishDayOfMonth() == 24) {
			// return EREV_CHANUKAH;
			// } else
			if (getJewishDayOfMonth() >= 25) {
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
	 * Returns true if the current day is Yom Tov. The method returns false for Chanukah, Erev Yom Tov (with the
	 * exception of Hoshana Rabba and Erev the second days of Pesach) and fast days.
	 * 
	 * @return true if the current day is a Yom Tov
	 * @see #isErevYomTov()
	 * @see #isTaanis()
	 */
	public boolean isYomTov() {
		int holidayIndex = getYomTovIndex();
		if ((isErevYomTov() && (holidayIndex != HOSHANA_RABBA && (holidayIndex == CHOL_HAMOED_PESACH && getJewishDayOfMonth() != 20)))
				|| holidayIndex == CHANUKAH || (isTaanis() && holidayIndex != YOM_KIPPUR)) {
			return false;
		}
		return getYomTovIndex() != -1;
	}
	
	/**
	 * Returns true if the Yom Tov day has a melacha (work)  prohibition. This method will return false for a non Yom Tov day, even if it is Shabbos.
	 * 
	 * @return if the Yom Tov day has a melacha (work)  prohibition.
	 */
	public boolean isYomTovAssurBemelacha(){
		int holidayIndex = getYomTovIndex();
		return holidayIndex == PESACH || holidayIndex == SHAVUOS || holidayIndex == SUCCOS || holidayIndex == SHEMINI_ATZERES || 
				holidayIndex == SIMCHAS_TORAH || holidayIndex == ROSH_HASHANA  || holidayIndex == YOM_KIPPUR;
	}

	/**
	 * Returns true if the current day is Chol Hamoed of Pesach or Succos.
	 * 
	 * @return true if the current day is Chol Hamoed of Pesach or Succos
	 * @see #isYomTov()
	 * @see #CHOL_HAMOED_PESACH
	 * @see #CHOL_HAMOED_SUCCOS
	 */
	public boolean isCholHamoed() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == JewishCalendar.CHOL_HAMOED_PESACH || holidayIndex == JewishCalendar.CHOL_HAMOED_SUCCOS;
	}

	/**
	 * Returns true if the current day is erev Yom Tov. The method returns true for Erev - Pesach (first and last days),
	 * Shavuos, Rosh Hashana, Yom Kippur and Succos and Hoshana Rabba.
	 * 
	 * @return true if the current day is Erev - Pesach, Shavuos, Rosh Hashana, Yom Kippur and Succos
	 * @see #isYomTov()
	 */
	public boolean isErevYomTov() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == EREV_PESACH || holidayIndex == EREV_SHAVUOS || holidayIndex == EREV_ROSH_HASHANA
				|| holidayIndex == EREV_YOM_KIPPUR || holidayIndex == EREV_SUCCOS || holidayIndex == HOSHANA_RABBA
				|| (holidayIndex == CHOL_HAMOED_PESACH && getJewishDayOfMonth() == 20);
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
	 * Returns the day of Chanukah or -1 if it is not Chanukah.
	 * 
	 * @return the day of Chanukah or -1 if it is not Chanukah.
	 */
	public int getDayOfChanukah() {
		if (isChanukah()) {
			if (getJewishMonth() == KISLEV) {
				return getJewishDayOfMonth() - 24;
			} else { // teves
				return isKislevShort() ? getJewishDayOfMonth() + 5 : getJewishDayOfMonth() + 6;
			}
		} else {
			return -1;
		}
	}

	public boolean isChanukah() {
		return getYomTovIndex() == CHANUKAH;
	}

	/**
	 * Returns if the day is Rosh Chodesh. Rosh Hashana will return false
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
	 * Returns the molad in Standard Time in Yerushalayim as a Date. The traditional calculation uses local time. This
	 * method subtracts 20.94 minutes (20 minutes and 56.496 seconds) from the local time (Har Habayis with a longitude
	 * of 35.2354&deg; is 5.2354&deg; away from the %15 timezone longitude) to get to standard time. This method
	 * intentionally uses standard time and not dailight savings time. Java will implicitly format the time to the
	 * default (or set) Timezone.
	 * 
	 * @return the Date representing the moment of the molad in Yerushalayim standard time (GMT + 2)
	 */
	public Date getMoladAsDate() {
		JewishDate molad = getMolad();
		String locationName = "Jerusalem, Israel";

		double latitude = 31.778; // Har Habayis latitude
		double longitude = 35.2354; // Har Habayis longitude

		// The molad calculation always expects output in standard time. Using "Asia/Jerusalem" timezone will incorrect
		// adjust for DST.
		TimeZone yerushalayimStandardTZ = TimeZone.getTimeZone("GMT+2");
		GeoLocation geo = new GeoLocation(locationName, latitude, longitude, yerushalayimStandardTZ);
		Calendar cal = Calendar.getInstance(geo.getTimeZone());
		cal.clear();
		double moladSeconds = molad.getMoladChalakim() * 10 / (double) 3;
		cal.set(molad.getGregorianYear(), molad.getGregorianMonth(), molad.getGregorianDayOfMonth(),
				molad.getMoladHours(), molad.getMoladMinutes(), (int) moladSeconds);
		cal.set(Calendar.MILLISECOND, (int) (1000 * (moladSeconds - (int) moladSeconds)));
		// subtract local time difference of 20.94 minutes (20 minutes and 56.496 seconds) to get to Standard time
		cal.add(Calendar.MILLISECOND, -1 * (int) geo.getLocalMeanTimeOffset());
		return cal.getTime();
	}

	/**
	 * Returns the earliest time of Kiddush Levana calculated as 3 days after the molad. TODO: Currently returns the
	 * time even if it is during the day. It should return the
	 * {@link net.sourceforge.zmanim.ZmanimCalendar#getTzais72() Tzais} after to the time if the zman is between Alos
	 * and Tzais.
	 * 
	 * @return the Date representing the moment 3 days after the molad.
	 */
	public Date getTchilasZmanKidushLevana3Days() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		cal.add(Calendar.HOUR, 72); // 3 days after the molad
		return cal.getTime();
	}

	/**
	 * Returns the earliest time of Kiddush Levana calculated as 7 days after the molad as mentioned by the <a
	 * href="http://en.wikipedia.org/wiki/Yosef_Karo">Mechaber</a>. See the <a
	 * href="http://en.wikipedia.org/wiki/Yoel_Sirkis">Bach's</a> opinion on this time. TODO: Currently returns the time
	 * even if it is during the day. It should return the {@link net.sourceforge.zmanim.ZmanimCalendar#getTzais72()
	 * Tzais} after to the time if the zman is between Alos and Tzais.
	 * 
	 * @return the Date representing the moment 7 days after the molad.
	 */
	public Date getTchilasZmanKidushLevana7Days() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		cal.add(Calendar.HOUR, 168); // 7 days after the molad
		return cal.getTime();
	}

	/**
	 * Returns the latest time of Kiddush Levana according to the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion that it is calculated as
	 * halfway between molad and molad. This adds half the 29 days, 12 hours and 793 chalakim time between molad and
	 * molad (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's molad. TODO: Currently returns the time
	 * even if it is during the day. It should return the {@link net.sourceforge.zmanim.ZmanimCalendar#getAlos72() Alos}
	 * prior to the time if the zman is between Alos and Tzais.
	 * 
	 * @return the Date representing the moment halfway between molad and molad.
	 * @see #getSofZmanKidushLevana15Days()
	 */
	public Date getSofZmanKidushLevanaBetweenMoldos() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		// add half the time between molad and molad (half of 29 days, 12 hours and 793 chalakim (44 minutes, 3.3
		// seconds), or 14 days, 18 hours, 22 minutes and 666 milliseconds)
		cal.add(Calendar.DAY_OF_MONTH, 14);
		cal.add(Calendar.HOUR_OF_DAY, 18);
		cal.add(Calendar.MINUTE, 22);
		cal.add(Calendar.SECOND, 1);
		cal.add(Calendar.MILLISECOND, 666);
		return cal.getTime();
	}

	/**
	 * Returns the latest time of Kiddush Levana calculated as 15 days after the molad. This is the opinion brought down
	 * in the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that the
	 * <a href="http://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the opinion of the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> of calculating
	 * {@link #getSofZmanKidushLevanaBetweenMoldos() half way between molad and mold} is of the opinion that Mechaber
	 * agrees to his opinion. Also see the Aruch Hashulchan. For additional details on the subject, See Rabbi Dovid
	 * Heber's very detailed writeup in Siman Daled (chapter 4) of <a
	 * href="http://www.worldcat.org/oclc/461326125">Shaarei Zmanim</a>. TODO: Currently returns the time even if it is
	 * during the day. It should return the {@link net.sourceforge.zmanim.ZmanimCalendar#getAlos72() Alos} prior to the
	 * time if the zman is between Alos and Tzais.
	 * 
	 * @return the Date representing the moment 15 days after the molad.
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 */
	public Date getSofZmanKidushLevana15Days() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		cal.add(Calendar.DAY_OF_YEAR, 15); // 15 days after the molad
		return cal.getTime();
	}

	/**
	 * Returns the Daf Yomi (Bavli) for the date that the calendar is set to. See the
	 * {@link HebrewDateFormatter#formatDafYomiBavli(Daf)} for the ability to format the daf in Hebrew or transliterated
	 * masechta names.
	 * 
	 * @return the daf as a {@link Daf}
	 */
	public Daf getDafYomiBavli() {
		return YomiCalculator.getDafYomiBavli(this);
	}
	/**
	 * Returns the Daf Yomi (Yerushalmi) for the date that the calendar is set to. See the
	 * {@link HebrewDateFormatter#formatDafYomiYerushalmi(Daf)} for the ability to format the daf in Hebrew or transliterated
	 * masechta names.
	 * 
	 * @return the daf as a {@link Daf}
	 */
	public Daf getDafYomiYerushalmi() {
		return YerushalmiYomiCalculator.getDafYomiYerushalmi(this);
	}


	/**
	 * @see Object#equals(Object)
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
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getClass().hashCode(); // needed or this and subclasses will return identical hash
		result += 37 * result + getAbsDate() + (getInIsrael() ? 1 : 3);
		return result;
	}
}
