/*
 * Zmanim Java API
 * Copyright (C) 2019 - 2022 Eliyahu Hershfeld
 * Copyright (C) 2019 - 2021 Y Paritcher
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

/**
 * Tefila Rules is a utility class that covers the various <em>halachos</em> and <em>minhagim</em> regarding
 * changes to daily <em>tefila</em> / prayers, based on the Jewish calendar. This is mostly useful for use in
 * developing <em></em>, but it is also valuble for <em>shul</em> calendars that set <em>tefila</em> time based
 * on if <em>tachanun</em> is recited that day.
 * 
 * @author &copy; Y. Paritcher 2019 - 2021
 * @author &copy; Eliyahu Hershfeld 2019 - 2022
 * 
 * @todo The following items may be added.
 * <ol>
 * <li>Lamnatzaiach</li>
 * </ol>
 */
public class TefilaRules {
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedEndOfTishrei()
	 * @see #setTachanunRecitedEndOfTishrei(boolean)
	 */
	private boolean tachanunRecitedEndOfTishrei = true;
	
	/**
	 * The default value is <code>false</code>.
	 * @see #isTachanunRecitedWeekAfterShavuos()
	 * @see #setTachanunRecitedWeekAfterShavuos(boolean)
	 */
	private boolean tachanunRecitedWeekAfterShavuos = false;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecited13SivanOutOfIsrael()
	 * @see #setTachanunRecited13SivanOutOfIsrael(boolean)
	 */
	private boolean tachanunRecited13SivanOutOfIsrael = true;
	
	/**
	 * The default value is <code>false</code>.
	 * @see #isTachanunRecitedPesachSheni()
	 * @see #setTachanunRecitedPesachSheni(boolean)
	 */
	private boolean tachanunRecitedPesachSheni = false;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecited15IyarOutOfIsrael()
	 * @see #setTachanunRecited15IyarOutOfIsrael(boolean)
	 */
	private boolean tachanunRecited15IyarOutOfIsrael = true;
	
	/**
	 * The default value is <code>false</code>.
	 * @see #isTachanunRecitedMinchaErevLagBaomer()
	 * @see #setTachanunRecitedMinchaErevLagBaomer(boolean)
	 */
	private boolean tachanunRecitedMinchaErevLagBaomer = false;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedShivasYemeiHamiluim()
	 * @see #setTachanunRecitedShivasYemeiHamiluim(boolean)
	 */
	private boolean tachanunRecitedShivasYemeiHamiluim = true;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedWeekOfHod()
	 * @see #setTachanunRecitedWeekOfHod(boolean)
	 */
	private boolean tachanunRecitedWeekOfHod = true;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedWeekOfPurim()
	 * @see #setTachanunRecitedWeekOfPurim(boolean)
	 */
	private boolean tachanunRecitedWeekOfPurim = true;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedFridays()
	 * @see #setTachanunRecitedFridays(boolean)
	 */
	private boolean tachanunRecitedFridays = true;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedSundays()
	 * @see #setTachanunRecitedSundays(boolean)
	 */
	private boolean tachanunRecitedSundays = true;
	
	/**
	 * The default value is <code>true</code>.
	 * @see #isTachanunRecitedMincha()
	 * @see #setTachanunRecitedMincha(boolean)
	 */
	private boolean tachatnunRecitedMincha = true;

	/**
	 * Returns if <em>tachanun</em> is recited during <em>shacharis</em> on the day in question. See the many
	 * <em>minhag</em> based settings that are available in this class.
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>tachanun</em> is recited during <em>shacharis</em>.
	 */
	public boolean isTachanunRecitedShacharis(JewishCalendar jewishCalendar) {
		int holidayIndex = jewishCalendar.getYomTovIndex();
		int day = jewishCalendar.getJewishDayOfMonth();
		int month = jewishCalendar.getJewishMonth();

		if (jewishCalendar.getDayOfWeek() == Calendar.SATURDAY
				|| (!tachanunRecitedSundays && jewishCalendar.getDayOfWeek() == Calendar.SUNDAY)
				|| (!tachanunRecitedFridays && jewishCalendar.getDayOfWeek() == Calendar.FRIDAY)
				|| month == JewishDate.NISSAN
				|| (month == JewishDate.TISHREI && ((!tachanunRecitedEndOfTishrei && day > 8)
				|| (tachanunRecitedEndOfTishrei && (day > 8 && day < 22))))
				|| (month == JewishDate.SIVAN && (tachanunRecitedWeekAfterShavuos && day < 7
						|| !tachanunRecitedWeekAfterShavuos && day < (!jewishCalendar.getInIsrael()
								&& !tachanunRecited13SivanOutOfIsrael ? 14: 13)))
				|| (jewishCalendar.isYomTov() && (! jewishCalendar.isTaanis()
						|| (!tachanunRecitedPesachSheni && holidayIndex == JewishCalendar.PESACH_SHENI))) // Erev YT is included in isYomTov()
				|| (!jewishCalendar.getInIsrael() && !tachanunRecitedPesachSheni && !tachanunRecited15IyarOutOfIsrael
						&& jewishCalendar.getJewishMonth() == JewishDate.IYAR && day == 15)
				|| holidayIndex == JewishCalendar.TISHA_BEAV || jewishCalendar.isIsruChag()
				|| jewishCalendar.isRoshChodesh()
				|| (!tachanunRecitedShivasYemeiHamiluim &&
						((!jewishCalendar.isJewishLeapYear() && month == JewishDate.ADAR)
								|| (jewishCalendar.isJewishLeapYear() && month == JewishDate.ADAR_II)) && day > 22)
				|| (!tachanunRecitedWeekOfPurim &&
						((!jewishCalendar.isJewishLeapYear() && month == JewishDate.ADAR)
								|| (jewishCalendar.isJewishLeapYear() && month == JewishDate.ADAR_II)) && day > 10 && day < 18)
				|| (jewishCalendar.isUseModernHolidays()
						&& (holidayIndex == JewishCalendar.YOM_HAATZMAUT || holidayIndex == JewishCalendar.YOM_YERUSHALAYIM))
				|| (!tachanunRecitedWeekOfHod && month == JewishDate.IYAR && day > 13 && day < 21)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns if <em>tachanun</em> is recited during <em>mincha</em>.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return if <em>tachanun</em> is recited during <em>mincha</em>.
	 */
	public boolean isTachanunRecitedMincha(JewishCalendar jewishCalendar) {
		JewishCalendar tomorrow = new JewishCalendar();
		tomorrow = (JewishCalendar) jewishCalendar.clone();
		tomorrow.forward(Calendar.DATE, 1);
		
		if (!tachatnunRecitedMincha
					|| jewishCalendar.getDayOfWeek() == Calendar.FRIDAY
					|| ! isTachanunRecitedShacharis(jewishCalendar) 
					|| (! isTachanunRecitedShacharis(tomorrow) && 
							!(tomorrow.getYomTovIndex() == JewishCalendar.EREV_ROSH_HASHANA) &&
							!(tomorrow.getYomTovIndex() == JewishCalendar.EREV_YOM_KIPPUR) &&
							!(tomorrow.getYomTovIndex() == JewishCalendar.PESACH_SHENI))
					|| ! tachanunRecitedMinchaErevLagBaomer && tomorrow.getYomTovIndex() == JewishCalendar.LAG_BAOMER) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns if it is the Jewish day (starting the evening before) to start reciting <em>Vesein Tal Umatar Livracha</em>
	 * (<em>Sheailas Geshamim</em>). In Israel this is the 7th day of <em>Marcheshvan</em>. Outside Israel recitation
	 * starts on the evening of December 4th (or 5th if it is the year before a civil leap year) in the 21st century and
	 * shifts a day forward every century not evenly divisible by 400. This method will return true if <em>vesein tal
	 * umatar</em> on the current Jewish date that starts on the previous night, so Dec 5/6 will be returned by this method
	 * in the 21st century. <em>vesein tal umatar</em> is not recited on <em>Shabbos</em> and the start date will be
	 * delayed a day when the start day is on a <em>Shabbos</em> (this can only occur out of Israel).
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar Livracha</em>
	 *         (<em>Sheailas Geshamim</em>).
	 * 
	 * @see #isVeseinTalUmatarStartingTonight(JewishCalendar)
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarStartDate(JewishCalendar jewishCalendar) {
		if (jewishCalendar.getInIsrael()) {
			 // The 7th Cheshvan can't occur on Shabbos, so always return true for 7 Cheshvan
			if (jewishCalendar.getJewishMonth() == JewishDate.CHESHVAN && jewishCalendar.getJewishDayOfMonth() == 7) {
				return true;
			}
		} else {
			if (jewishCalendar.getDayOfWeek() == Calendar.SATURDAY) { //Not recited on Friday night
				return false;
			}
			if(jewishCalendar.getDayOfWeek() == Calendar.SUNDAY) { // When starting on Sunday, it can be the start date or delayed from Shabbos
				return jewishCalendar.getTekufasTishreiElapsedDays() == 48 || jewishCalendar.getTekufasTishreiElapsedDays() == 47;
			} else {
				return jewishCalendar.getTekufasTishreiElapsedDays() == 47;
			}
		}
		return false; // keep the compiler happy
	}
	
	/**
	 * Returns if true if tonight is the first night to start reciting <em>Vesein Tal Umatar Livracha</em> (
	 * <em>Sheailas Geshamim</em>). In Israel this is the 7th day of <em>Marcheshvan</em> (so the 6th will return
	 * true). Outside Israel recitation starts on the evening of December 4th (or 5th if it is the year before a
	 * civil leap year) in the 21st century and shifts a day forward every century not evenly divisible by 400.
	 * <em>Vesein tal umatar</em> is not recited on <em>Shabbos</em> and the start date will be delayed a day when
	 * the start day is on a <em>Shabbos</em> (this can only occur out of Israel).
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar
	 * Livracha</em> (<em>Sheailas Geshamim</em>).
	 * 
	 * @see #isVeseinTalUmatarStartDate(JewishCalendar)
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarStartingTonight(JewishCalendar jewishCalendar) {
		if (jewishCalendar.getInIsrael()) {
			// The 7th Cheshvan can't occur on Shabbos, so always return true for 6 Cheshvan
			if (jewishCalendar.getJewishMonth() == JewishDate.CHESHVAN && jewishCalendar.getJewishDayOfMonth() == 6) {
					return true;
			}
		} else {
			if (jewishCalendar.getDayOfWeek() == Calendar.FRIDAY) { //Not recited on Friday night
				return false;
			}
			if(jewishCalendar.getDayOfWeek() == Calendar.SATURDAY) { // When starting on motzai Shabbos, it can be the start date or delayed from Friday night
				return jewishCalendar.getTekufasTishreiElapsedDays() == 47 || jewishCalendar.getTekufasTishreiElapsedDays() == 46;
			} else {
				return jewishCalendar.getTekufasTishreiElapsedDays() == 46;
			}
		}
		return false;
	}

	/**
	 * Returns if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited. This will return
	 * true for the entire season, even on <em>Shabbos</em> when it is not recited.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited.
	 * 
	 * @see #isVeseinTalUmatarStartDate(JewishCalendar)
	 * @see #isVeseinTalUmatarStartingTonight(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarRecited(JewishCalendar jewishCalendar) {
		if (jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() < 15) {
			return true;
		}
		if (jewishCalendar.getJewishMonth() < JewishDate.CHESHVAN) {
			return false;
		}
		if (jewishCalendar.getInIsrael()) {
			return jewishCalendar.getJewishMonth() != JewishDate.CHESHVAN || jewishCalendar.getJewishDayOfMonth() >= 7;
		} else {
			return jewishCalendar.getTekufasTishreiElapsedDays() >= 47;
		}
	}
	
	/**
	 * Returns if <em>Vesein Beracha</em> is recited. It is recited from 15 <em>Nissan</em> to the point that {@link
	 * #isVeseinTalUmatarRecited(JewishCalendar) <em>vesein tal umatar</em> is recited}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if <em>Vesein Beracha</em> is recited.
	 * 
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinBerachaRecited(JewishCalendar jewishCalendar) {
		return ! isVeseinTalUmatarRecited(jewishCalendar);
	}

	/**
	 * Returns if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 22 <em>Tishrei</em>.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachStartDate(JewishCalendar jewishCalendar) {
		return jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 22;
	}

	/**
	 * Returns if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 15 <em>Nissan</em>.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachEndDate(JewishCalendar jewishCalendar) {
		return jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 15;
	}

	/**
	 * Returns if <em>Mashiv Haruach Umorid Hageshem</em> is recited. This period starts on 22 <em>Tishrei</em> and ends
	 * on the 15th day of <em>Nissan</em>.
	 * <em>Marcheshvan</em>. Outside of Israel recitation starts on December 4/5.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if <em>Mashiv Haruach Umorid Hageshem</em> is recited.
	 * 
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 */
	public boolean isMashivHaruachRecited(JewishCalendar jewishCalendar) {
		JewishDate startDate = new JewishDate(jewishCalendar.getJewishYear(), JewishDate.TISHREI, 22);
		JewishDate endDate = new JewishDate(jewishCalendar.getJewishYear(), JewishDate.NISSAN, 15);
		return jewishCalendar.compareTo(startDate) > 0 && jewishCalendar.compareTo(endDate) < 0;
	}

	/**
	 * Returns if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 * This period starts on 22 <em>Tishrei</em> and ends on the 15th day of
	 * <em>Nissan</em>.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 */
	public boolean isMoridHatalRecited(JewishCalendar jewishCalendar) {
		return !isMashivHaruachRecited(jewishCalendar) || isMashivHaruachStartDate(jewishCalendar) || isMashivHaruachEndDate(jewishCalendar);
	}
	
	/**
	 * @return If <em>tachanun</em> is set to be recited during the week of Purim from the 11th through the 17th of Adar
	 *         (on a non-leap year, or Adar II on a leap year). Some <em>Chasidish</em> communities do not recite
	 *         <em>tachanun</em> during this period.
	 */
	public boolean isTachanunRecitedWeekOfPurim() {
		return tachanunRecitedWeekOfPurim;
	}

	/**
	 * @param isTachanunRecitedWeekOfPurim Sets if tachanun is to recited during the week of Purim from the 11th through
	 *         the 17th of Adar (on a non-leap year), or Adar II (on a leap year). Some <em>Chasidish</em> communities do
	 *         not recite<em>tachanun</em> during this period.
	 */
	public void setTachanunRecitedWeekOfPurim(boolean isTachanunRecitedWeekOfPurim) {
		this.tachanunRecitedWeekOfPurim = isTachanunRecitedWeekOfPurim;
	}

	/**
	 * @return If <em>tachanun</em> is set to be recited during the <em>sefira</em> week of <em>Hod</em> (14 - 20 Iyar,
	 *         or the 29th - 35th of the <em>Omer</em>).
	 * @see #setTachanunRecitedWeekOfHod(boolean)
	 */
	public boolean isTachanunRecitedWeekOfHod() {
		return tachanunRecitedWeekOfHod;
	}

	/**
	 * @param isTachanunRecitedWeekOfHod Sets if <em>tachanun</em> should be recited during the <em>sefira</em> week of <em>Hod</em>.
	 * @see #isTachanunRecitedWeekOfHod()
	 */
	public void setTachanunRecitedWeekOfHod(boolean isTachanunRecitedWeekOfHod) {
		this.tachanunRecitedWeekOfHod = isTachanunRecitedWeekOfHod;
	}

	/**
	 * @return If <em>tachanun</em> is set to be recited at the end Of Tishrei.
	 * @see #setTachanunRecitedEndOfTishrei(isTachanunRecitedEndOfTishrei)
	 */
	public boolean isTachanunRecitedEndOfTishrei() {
		return tachanunRecitedEndOfTishrei;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited at the end of Tishrei.
	 * @param isTachanunRecitedEndOfTishrei is <em>tachanun</em> recited at the end of Tishrei.
	 * @see #isTachanunRecitedEndOfTishrei()
	 */
	public void setTachanunRecitedEndOfTishrei(boolean isTachanunRecitedEndOfTishrei) {
		this.tachanunRecitedEndOfTishrei = isTachanunRecitedEndOfTishrei;
	}
	
	/**
	 * @return If <em>tachanun</em> is set to be recited during the week after Shavuos.
	 * @see #setTachanunRecitedWeekAfterShavuos(isTachanunRecitedWeekAfterShavuos)
	 */
	public boolean isTachanunRecitedWeekAfterShavuos() {
		return tachanunRecitedWeekAfterShavuos;
	}

	/**
	 * Sets if <em>tachanun</em> is set to be recited during the week after <em>Shavuos</em>.
	 * @param isTachanunRecitedWeekAfterShavuos is <em>tachanun</em> recited during the week after Shavuos.
	 * @see #isTachanunRecitedEndOfTishrei()
	 */
	public void setTachanunRecitedWeekAfterShavuos(boolean isTachanunRecitedWeekAfterShavuos) {
		this.tachanunRecitedWeekAfterShavuos = isTachanunRecitedWeekAfterShavuos;
	}
	
	/**
	 * Returns If <em>tachanun</em> is set to be recited on the 13th of <em>Sivan</em> outside Israel. Both
	 * {@link #isTachanunRecitedShacharis(JewishCalendar)} and {@link #isTachanunRecitedMincha(JewishCalendar)}
	 * only return false if the location is not set to {@link JewishCalendar#getInIsrael() Israel} and both
	 * {@link #tachanunRecitedWeekAfterShavuos} and {@link #setTachanunRecited13SivanOutOfIsrael} are set to false.
	 * 
	 * @return If <em>tachanun</em> is set to be recited on the 13th of Sivan out of Israel.
	 * @see #setTachanunRecited13SivanOutOfIsrael(isTachanunRecitedThirteenSivanOutOfIsrael)
	 */
	public boolean isTachanunRecited13SivanOutOfIsrael() {
		return tachanunRecited13SivanOutOfIsrael;
	}

	/**
	 * @param isTachanunRecitedThirteenSivanOutOfIsrael sets if <em>tachanun</em> should be recited on the 13th
	 *          of <em>Sivan</em> out of Israel.
	 * @see #tachanunRecited13SivanOutOfIsrael
	 */
	public void setTachanunRecited13SivanOutOfIsrael(boolean isTachanunRecitedThirteenSivanOutOfIsrael) {
		this.tachanunRecited13SivanOutOfIsrael = isTachanunRecitedThirteenSivanOutOfIsrael;
	}
	
	/**
	 * @return If <em>tachanun</em> is recited on {@link JewishCalendar#PESACH_SHENI Pesach Sheni}.
	 */
	public boolean isTachanunRecitedPesachSheni() {
		return tachanunRecitedPesachSheni;
	}

	/**
	 * @param isTachanunRecitedPesachSheni sets if <em>tachanun</em> should be recited on <em>Pesach Sheni</em>
	 */
	public void setTachanunRecitedPesachSheni(boolean isTachanunRecitedPesachSheni) {
		this.tachanunRecitedPesachSheni = isTachanunRecitedPesachSheni;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on 15 Iyar (<em>sfaika deyoma</em> of {@link
	 *          JewishCalendar#PESACH_SHENI Pesach Sheni}) out of Israel.
	 */
	public boolean isTachanunRecited15IyarOutOfIsrael() {
		return tachanunRecited15IyarOutOfIsrael;
	}

	/**
	 * @param isTachanunRecited15IyarOutOfIsrael if <em>tachanun</em> should be recited on the 15th of <em>Iyar</em>
	 *          (<em>sfaika deyoma</em> of {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}) out of Israel.
	 */
	public void setTachanunRecited15IyarOutOfIsrael(boolean isTachanunRecited15IyarOutOfIsrael) {
		this.tachanunRecited15IyarOutOfIsrael = isTachanunRecited15IyarOutOfIsrael;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on {@link JewishCalendar#LAG_BAOMER <em>Lag Baomer</em>}.
	 */
	public boolean isTachanunRecitedMinchaErevLagBaomer() {
		return tachanunRecitedMinchaErevLagBaomer;
	}

	/**
	 * @param isTachanunRecitedMinchaErevLagBaomer sets if <em>tachanun</em> should be recited on <em>mincha</em>
	 *          of <em>erev {@link JewishCalendar#LAG_BAOMER Lag Baomer}</em>.
	 */
	public void setTachanunRecitedMinchaErevLagBaomer(boolean isTachanunRecitedMinchaErevLagBaomer) {
		this.tachanunRecitedMinchaErevLagBaomer = isTachanunRecitedMinchaErevLagBaomer;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on <em>Shivas Yemei Hamiluim</em>.
	 */
	public boolean isTachanunRecitedShivasYemeiHamiluim() {
		return tachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * @param isTachanunRecitedShivasYemeiHamiluim sets if <em>tachanun</em> should be recited during the
	 *          <em>Shivas Yemei Hamiluim</em>.
	 */
	public void setTachanunRecitedShivasYemeiHamiluim(boolean isTachanunRecitedShivasYemeiHamiluim) {
		this.tachanunRecitedShivasYemeiHamiluim = isTachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * @return if <em>tachanun</em> is recited on Fridays Some <em>chasidish</em> communities do not
	 *          recite <em>tachanun</em> on Fridays.
	 */
	public boolean isTachanunRecitedFridays() {
		return tachanunRecitedFridays;
	}

	/**
	 * @param isTachanunRecitedFridays sets if <em>tachanun</em> should be recited on Fridays. Some <em>chasidish</em>
	 *          communities do not recite <em>tachanun</em> on Fridays.
	 */
	public void setTachanunRecitedFridays(boolean isTachanunRecitedFridays) {
		this.tachanunRecitedFridays = isTachanunRecitedFridays;
	}

	/**
	 * @return if <em>tachanun</em> is recited on Sundays.  Some <em>chasidish</em> communities do not recite
	 *          <em>tachanun</em> on Fridays.
	 */
	public boolean isTachanunRecitedSundays() {
		return tachanunRecitedSundays;
	}

	/**
	 * @param isTachanunRecitedSundays sets if <em>tachanun</em> should be recited on Sundays. Some <em>chasidish</em>
	 *          communities do not recite <em>tachanun</em> on Sundays.
	 */
	public void setTachanunRecitedSundays(boolean isTachanunRecitedSundays) {
		this.tachanunRecitedSundays = isTachanunRecitedSundays;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on <em>Mincha</em> the entire year.  Some <em>chasidish</em>
	 *          communities do not recite <em>tachanun</em> all year round by <em>Mincha</em>.
	 */
	public boolean isTachanunRecitedMincha() {
		return tachatnunRecitedMincha;
	}

	/**
	 * @param isTachanunRecitedMincha sets if <em>tachanun</em> should be recited by <em>mincha</em>. If set to false, 
	 *          {@link #isTachanunRecitedMincha(JewishCalendar)} will always return false. If set to true (the default), it
	 *          will use the regular rules.
	 */
	public void setTachanunRecitedMincha(boolean isTachanunRecitedMincha) {
		this.tachatnunRecitedMincha = isTachanunRecitedMincha;
	}
}
