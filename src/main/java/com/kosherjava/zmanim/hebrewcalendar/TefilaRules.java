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
 * changes to daily <em>tefila</em> / prayers, based on the Jewish calendar.
 * 
 * @author &copy; Y. Paritcher 2019 - 2021
 * @author &copy; Eliyahu Hershfeld 2019 - 2022
 * 
 * @todo The following items have to be implemented
 * <ol>
 * <li>Consider allowing setting arbitrary Jewish Calendar days where <em>tachanun</em> is not recited. What format should
 * 	be used, what about ADAR issue? (yahrzeit on a non-leap year that occurs on a leap, or the other
 *  direction), or cheshvan/Kislev chaseyra or yesaira issues</li>
 * </ol>
 */
public class TefilaRules {
	/**
	 * @see #isTachanunRecitedEndOfTishrei()
	 * @see #setTachanunRecitedEndOfTishrei(boolean)
	 */
	private boolean isTachanunRecitedEndOfTishrei = true;
	
	/**
	 * @see #isTachanunRecitedWeekAfterShavuos()
	 * @see #setTachanunRecitedWeekAfterShavuos(boolean)
	 */
	private boolean isTachanunRecitedWeekAfterShavuos = false;
	
	/**
	 * @see #isTachanunRecited13SivanOutOfIsrael()
	 * @see #setTachanunRecited13SivanOutOfIsrael(boolean)
	 */
	private boolean isTachanunRecited13SivanOutOfIsrael = true;
	
	/**
	 * @see #isTachanunRecitedPesachSheni()
	 * @see #setTachanunRecitedPesachSheni(boolean)
	 */
	private boolean isTachanunRecitedPesachSheni = false;
	
	/**
	 * @see #isTachanunRecited15IyarOutOfIsrael()
	 * @see #setTachanunRecited15IyarOutOfIsrael(boolean)
	 */
	private boolean isTachanunRecited15IyarOutOfIsrael = true;
	
	/**
	 * @see #isTachanunRecitedMinchaErevLagBaomer()
	 * @see #setTachanunRecitedMinchaErevLagBaomer(boolean)
	 */
	private boolean isTachanunRecitedMinchaErevLagBaomer = false;
	
	/**
	 * @see #isTachanunRecitedShivasYemeiHamiluim()
	 * @see #setTachanunRecitedShivasYemeiHamiluim(boolean)
	 */
	private boolean isTachanunRecitedShivasYemeiHamiluim = true;
	
	/**
	 * @see #isTachanunRecitedWeekOfHod()
	 * @see #setTachanunRecitedWeekOfHod(boolean)
	 */
	private boolean isTachanunRecitedWeekOfHod = true;
	
	/**
	 * @see #isTachanunRecitedWeekOfPurim()
	 * @see #setTachanunRecitedWeekOfPurim(boolean)
	 */
	private boolean isTachanunRecitedWeekOfPurim = true;
	
	/**
	 * @see #isTachanunRecitedFridays()
	 * @see #setTachanunRecitedFridays(boolean)
	 */
	private boolean isTachanunRecitedFridays = true;
	
	/**
	 * @see #isTachanunRecitedSundays()
	 * @see #setTachanunRecitedSundays(boolean)
	 */
	private boolean isTachanunRecitedSundays = true;
	
	/**
	 * @see #isTachanunRecitedMincha()
	 * @see #setTachanunRecitedMincha(boolean)
	 */
	private boolean isTachanunRecitedMincha = true;

	/**
	 * Returns if <em>tachanun</em> is recited during <em>shacharis</em>.
	 * @return if <em>tachanun</em> is recited during <em>shacharis</em>.
	 */
	public boolean isTachanunShacharis(JewishCalendar jc) {
		int holidayIndex = jc.getYomTovIndex();
		int day = jc.getJewishDayOfMonth();
		int month = jc.getJewishMonth();

		if (jc.getDayOfWeek() == Calendar.SATURDAY
				|| (!isTachanunRecitedSundays && jc.getDayOfWeek() == Calendar.SUNDAY)
				|| (!isTachanunRecitedFridays && jc.getDayOfWeek() == Calendar.FRIDAY)
				|| month == JewishDate.NISSAN
				|| (month == JewishDate.TISHREI && ((!isTachanunRecitedEndOfTishrei && day > 8)
				|| (isTachanunRecitedEndOfTishrei && (day > 8 && day < 22))))
				|| (month == JewishDate.SIVAN && (isTachanunRecitedWeekAfterShavuos && day < 7
						|| !isTachanunRecitedWeekAfterShavuos && day < (!jc.getInIsrael() && !isTachanunRecited13SivanOutOfIsrael ? 14: 13)))
				|| (jc.isYomTov() && (! jc.isTaanis()
						|| (!isTachanunRecitedPesachSheni && holidayIndex == JewishCalendar.PESACH_SHENI))) // Erev YT is included in isYomTov()
				|| (!jc.getInIsrael() && !isTachanunRecitedPesachSheni && !isTachanunRecited15IyarOutOfIsrael && jc.getJewishMonth() == JewishDate.IYAR && day == 15)
				|| holidayIndex == JewishCalendar.TISHA_BEAV || jc.isIsruChag()
				|| jc.isRoshChodesh()
				|| (!isTachanunRecitedShivasYemeiHamiluim &&
						((!jc.isJewishLeapYear() && month == JewishDate.ADAR) || (jc.isJewishLeapYear() && month == JewishDate.ADAR_II)) && day > 22)
				|| (!isTachanunRecitedWeekOfPurim &&
						((!jc.isJewishLeapYear() && month == JewishDate.ADAR) || (jc.isJewishLeapYear() && month == JewishDate.ADAR_II)) && day > 10 && day < 18)
				|| (jc.isUseModernHolidays() && (holidayIndex == JewishCalendar.YOM_HAATZMAUT || holidayIndex == JewishCalendar.YOM_YERUSHALAYIM))
				|| (!isTachanunRecitedWeekOfHod && month == JewishDate.IYAR && day > 13 && day < 21)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns if <em>tachanun</em> is recited during <em>mincha</em>.
	 * @return if <em>tachanun</em> is recited during <em>mincha</em>.
	 */
	public boolean isTachanunMincha(JewishCalendar jc) {
		JewishCalendar tomorrow = new JewishCalendar();
		tomorrow = (JewishCalendar) jc.clone();
		tomorrow.forward(Calendar.DATE, 1);
		
		if (!isTachanunRecitedMincha
					|| jc.getDayOfWeek() == Calendar.FRIDAY
					|| ! isTachanunShacharis(jc) 
					|| (! isTachanunShacharis(tomorrow) && 
							!(tomorrow.getYomTovIndex() == JewishCalendar.EREV_ROSH_HASHANA) &&
							!(tomorrow.getYomTovIndex() == JewishCalendar.EREV_YOM_KIPPUR) &&
							!(tomorrow.getYomTovIndex() == JewishCalendar.PESACH_SHENI))
					|| ! isTachanunRecitedMinchaErevLagBaomer && tomorrow.getYomTovIndex() == JewishCalendar.LAG_BAOMER) {
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
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar Livracha</em>
	 *         (<em>Sheailas Geshamim</em>).
	 * 
	 * @see #isVeseinTalUmatarStartingTonight(JewishCalendar)
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarStartDate(JewishCalendar jc) {
		if (jc.getInIsrael()) {
			 // The 7th Cheshvan can't occur on Shabbos, so always return true for 7 Cheshvan
			if (jc.getJewishMonth() == JewishDate.CHESHVAN && jc.getJewishDayOfMonth() == 7) {
				return true;
			}
		} else {
			if (jc.getDayOfWeek() == Calendar.SATURDAY) { //Not recited on Friday night
				return false;
			}
			if(jc.getDayOfWeek() == Calendar.SUNDAY) { // When starting on Sunday, it can be the start date or delayed from Shabbos
				return jc.getTekufasTishreiElapsedDays() == 48 || jc.getTekufasTishreiElapsedDays() == 47;
			} else {
				return jc.getTekufasTishreiElapsedDays() == 47;
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
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar
	 * Livracha</em> (<em>Sheailas Geshamim</em>).
	 * 
	 * @see #isVeseinTalUmatarStartDate(JewishCalendar)
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarStartingTonight(JewishCalendar jc) {
		if (jc.getInIsrael()) {
			// The 7th Cheshvan can't occur on Shabbos, so always return true for 6 Cheshvan
			if (jc.getJewishMonth() == JewishDate.CHESHVAN && jc.getJewishDayOfMonth() == 6) {
					return true;
			}
		} else {
			if (jc.getDayOfWeek() == Calendar.FRIDAY) { //Not recited on Friday night
				return false;
			}
			if(jc.getDayOfWeek() == Calendar.SATURDAY) { // When starting on motzai Shabbos, it can be the start date or delayed from Friday night
				return jc.getTekufasTishreiElapsedDays() == 47 || jc.getTekufasTishreiElapsedDays() == 46;
			} else {
				return jc.getTekufasTishreiElapsedDays() == 46;
			}
		}
		return false;
	}

	/**
	 * Returns if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited. This will return
	 * true for the entire season, even on <em>Shabbos</em> when it is not recited.
	 * @return true if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited.
	 * 
	 * @see #isVeseinTalUmatarStartDate(JewishCalendar)
	 * @see #isVeseinTalUmatarStartingTonight(JewishCalendar)
	 */
	public boolean isVeseinTalUmatarRecited(JewishCalendar jc) {
		if (jc.getJewishMonth() == JewishDate.NISSAN && jc.getJewishDayOfMonth() < 15) {
			return true;
		}
		if (jc.getJewishMonth() < JewishDate.CHESHVAN) {
			return false;
		}
		if (jc.getInIsrael()) {
			return jc.getJewishMonth() != JewishDate.CHESHVAN || jc.getJewishDayOfMonth() >= 7;
		} else {
			return jc.getTekufasTishreiElapsedDays() >= 47;
		}
	}
	
	/**
	 * Returns if <em>Vesein Beracha</em> is recited. It is recited from 15 <em>Nissan</em> to the point that {@link
	 * #isVeseinTalUmatarRecited(JewishCalendar) <em>vesein tal umatar</em> is recited}.
	 * 
	 * @return true if <em>Vesein Beracha</em> is recited.
	 * 
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinBerachaRecited(JewishCalendar jc) {
		return ! isVeseinTalUmatarRecited(jc);
	}

	/**
	 * Returns if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 22 <em>Tishrei</em>.
	 * 
	 * @return true if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachStartDate(JewishCalendar jc) {
		return jc.getJewishMonth() == JewishDate.TISHREI && jc.getJewishDayOfMonth() == 22;
	}

	/**
	 * Returns if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 15 <em>Nissan</em>.
	 * 
	 * @return true if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachEndDate(JewishCalendar jc) {
		return jc.getJewishMonth() == JewishDate.NISSAN && jc.getJewishDayOfMonth() == 15;
	}

	/**
	 * Returns if <em>Mashiv Haruach Umorid Hageshem</em> is recited. This period starts on 22 <em>Tishrei</em> and ends
	 * on the 15th day of <em>Nissan</em>.
	 * <em>Marcheshvan</em>. Outside of Israel recitation starts on December 4/5.
	 * 
	 * @return true if <em>Mashiv Haruach Umorid Hageshem</em> is recited.
	 * 
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 */
	public boolean isMashivHaruachRecited(JewishCalendar jc) {
		JewishDate startDate = new JewishDate(jc.getJewishYear(), JewishDate.TISHREI, 22);
		JewishDate endDate = new JewishDate(jc.getJewishYear(), JewishDate.NISSAN, 15);
		return jc.compareTo(startDate) > 0 && jc.compareTo(endDate) < 0;
	}

	/**
	 * Returns if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 * This period starts on 22 <em>Tishrei</em> and ends on the 15th day of
	 * <em>Nissan</em>.
	 * 
	 * @return true if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 */
	public boolean isMoridHatalRecited(JewishCalendar jc) {
		return !isMashivHaruachRecited(jc) || isMashivHaruachStartDate(jc) || isMashivHaruachEndDate(jc);
	}
	
	/**
	 * @return If <em>tachanun</em> is set to be recited during the week of Purim from the 11th through the 17th of Adar
	 *         (on a non-leap year, or Adar II on a leap year).
	 */
	public boolean isTachanunRecitedWeekOfPurim() {
		return isTachanunRecitedWeekOfPurim;
	}

	/**
	 * @param isTachanunRecitedWeekOfPurim Sets if tachanun is to recited during the week of Purim from the 11th through
	 *         the 17th of Adar (on a non-leap year), or Adar II (on a leap year).
	 */
	public void setTachanunRecitedWeekOfPurim(boolean isTachanunRecitedWeekOfPurim) {
		this.isTachanunRecitedWeekOfPurim = isTachanunRecitedWeekOfPurim;
	}

	/**
	 * @return If <em>tachanun</em> is set to be recited during the <em>sefira</em> week of <em>Hod</em> (14 - 20 Iyar,
	 *         or the 29th - 35th of the <em>Omer</em>).
	 * @see #setTachanunRecitedWeekOfHod(boolean)
	 */
	public boolean isTachanunRecitedWeekOfHod() {
		return isTachanunRecitedWeekOfHod;
	}

	/**
	 * @param isTachanunRecitedWeekOfHod Sets if <em>tachanun</em> should be recited during the <em>sefira</em> week of <em>Hod</em>.
	 * @see #isTachanunRecitedWeekOfHod()
	 */
	public void setTachanunRecitedWeekOfHod(boolean isTachanunRecitedWeekOfHod) {
		this.isTachanunRecitedWeekOfHod = isTachanunRecitedWeekOfHod;
	}

	/**
	 * @return If <em>tachanun</em> is set to be recited at the end Of Tishrei.
	 * @see #setTachanunRecitedEndOfTishrei(isTachanunRecitedEndOfTishrei)
	 */
	public boolean isTachanunRecitedEndOfTishrei() {
		return isTachanunRecitedEndOfTishrei;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited at the end of Tishrei.
	 * @param isTachanunRecitedEndOfTishrei is <em>tachanun</em> recited at the end of Tishrei.
	 * @see #isTachanunRecitedEndOfTishrei()
	 */
	public void setTachanunRecitedEndOfTishrei(boolean isTachanunRecitedEndOfTishrei) {
		this.isTachanunRecitedEndOfTishrei = isTachanunRecitedEndOfTishrei;
	}
	
	/**
	 * @return If <em>tachanun</em> is set to be recited during the week after Shavuos.
	 * @see #setTachanunRecitedWeekAfterShavuos(isTachanunRecitedWeekAfterShavuos)
	 */
	public boolean isTachanunRecitedWeekAfterShavuos() {
		return isTachanunRecitedWeekAfterShavuos;
	}

	/**
	 * Sets if <em>tachanun</em> is set to be recited at the end of Tishrei.
	 * @param isTachanunRecitedWeekAfterShavuos is <em>tachanun</em> recited during the week after Shavuos.
	 * @see #isTachanunRecitedEndOfTishrei()
	 */
	public void setTachanunRecitedWeekAfterShavuos(boolean isTachanunRecitedWeekAfterShavuos) {
		this.isTachanunRecitedWeekAfterShavuos = isTachanunRecitedWeekAfterShavuos;
	}
	
	/**
	 * Returns If <em>tachanun</em> is set to be recited on the 13th of Sivan
	 * Outside Israel. {@link #isTachanunShacharis(JewishCalendar)}
	 * {@link #isTachanunMincha(JewishCalendar)} only return false if the location
	 * is not set to {@link JewishCalendar#getInIsrael() Israel} and
	 * {@link #isTachanunRecitedWeekAfterShavuos} and
	 * {@link #setTachanunRecited13SivanOutOfIsrael} are both set to false.
	 * 
	 * @return If <em>tachanun</em> is set to be recited on the 13th of Sivan out of Israel.
	 * @see #setTachanunRecited13SivanOutOfIsrael(isTachanunRecitedThirteenSivanOutOfIsrael)
	 */
	public boolean isTachanunRecited13SivanOutOfIsrael() {
		return isTachanunRecited13SivanOutOfIsrael;
	}

	/**
	 * @param isTachanunRecitedThirteenSivanOutOfIsrael sets if <em>tachanun</em> should be recited on the 13th of Sivan out of Israel.
	 * @see #isTachanunRecited13SivanOutOfIsrael
	 */
	public void setTachanunRecited13SivanOutOfIsrael(boolean isTachanunRecitedThirteenSivanOutOfIsrael) {
		this.isTachanunRecited13SivanOutOfIsrael = isTachanunRecitedThirteenSivanOutOfIsrael;
	}
	
	/**
	 * @return If <em>tachanun</em> is recited on {@link JewishCalendar#PESACH_SHENI Pesach Sheni}.
	 */
	public boolean isTachanunRecitedPesachSheni() {
		return isTachanunRecitedPesachSheni;
	}

	/**
	 * @param isTachanunRecitedPesachSheni sets if <em>tachanun</em> should be recited on <em>Pesach Sheni</em>
	 */
	public void setTachanunRecitedPesachSheni(boolean isTachanunRecitedPesachSheni) {
		this.isTachanunRecitedPesachSheni = isTachanunRecitedPesachSheni;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on 15 Iyar (sfaika deyoma of {@link JewishCalendar#PESACH_SHENI Pesach Sheni}) out of Israel.
	 */
	public boolean isTachanunRecited15IyarOutOfIsrael() {
		return isTachanunRecited15IyarOutOfIsrael;
	}

	/**
	 * @param isTachanunRecited15IyarOutOfIsrael if <em>tachanun</em> should be recited on the 15th of Iyar out of Israel
	 */
	public void setTachanunRecited15IyarOutOfIsrael(boolean isTachanunRecited15IyarOutOfIsrael) {
		this.isTachanunRecited15IyarOutOfIsrael = isTachanunRecited15IyarOutOfIsrael;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on <em>Lag Baomer</em>.
	 */
	public boolean isTachanunRecitedMinchaErevLagBaomer() {
		return isTachanunRecitedMinchaErevLagBaomer;
	}

	/**
	 * @param isTachanunRecitedMinchaErevLagBaomer sets if <em>tachanun</em> should be recited on <em>mincha</em> of <em>erev Lag Baomer</em>.
	 */
	public void setTachanunRecitedMinchaErevLagBaomer(boolean isTachanunRecitedMinchaErevLagBaomer) {
		this.isTachanunRecitedMinchaErevLagBaomer = isTachanunRecitedMinchaErevLagBaomer;
	}
	
	/**
	 * @return if <em>tachanun</em> is recited on <em>Shivas Yemei Hamiluim</em>.
	 */
	public boolean isTachanunRecitedShivasYemeiHamiluim() {
		return isTachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * @param isTachanunRecitedShivasYemeiHamiluim sets if <em>tachanun</em> should be recited on <em>mincha</em> of <em>Shivas Yemei Hamiluim</em>.
	 */
	public void setTachanunRecitedShivasYemeiHamiluim(boolean isTachanunRecitedShivasYemeiHamiluim) {
		this.isTachanunRecitedShivasYemeiHamiluim = isTachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * @return
	 */
	public boolean isTachanunRecitedFridays() {
		return isTachanunRecitedFridays;
	}

	/**
	 * @param isTachanunRecitedFridays
	 */
	public void setTachanunRecitedFridays(boolean isTachanunRecitedFridays) {
		this.isTachanunRecitedFridays = isTachanunRecitedFridays;
	}

	/**
	 * @return
	 */
	public boolean isTachanunRecitedSundays() {
		return isTachanunRecitedSundays;
	}

	/**
	 * @param isTachanunRecitedSundays
	 */
	public void setTachanunRecitedSundays(boolean isTachanunRecitedSundays) {
		this.isTachanunRecitedSundays = isTachanunRecitedSundays;
	}
	
	/**
	 * @return
	 */
	public boolean isTachanunRecitedMincha() {
		return isTachanunRecitedMincha;
	}

	/**
	 * @param isTachanunRecitedMincha sets if <em>tachanun</em> should be recited by <em>mincha</em>. If set to false, 
	 *          {@link #isTachanunMincha(JewishCalendar)} will always return false. If set to true (the default), it
	 *          will use the regular rules.
	 */
	public void setTachanunRecitedMincha(boolean isTachanunRecitedMincha) {
		this.isTachanunRecitedMincha = isTachanunRecitedMincha;
	}
}
