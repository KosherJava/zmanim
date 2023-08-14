/*
 * Zmanim Java API
 * Copyright (C) 2019 - 2023 Eliyahu Hershfeld
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
 * developing <em>siddur</em> type applications, but it is also valuable for <em>shul</em> calendars that set
 * <em>tefila</em> times based on if <a href="https://en.wikipedia.org/wiki/Tachanun"><em>tachanun</em></a> is
 * recited that day. There are many settings in this class to cover the vast majority of <em>minhagim</em>, but
 * there are likely some not covered here. The source for many of the <em>chasidishe minhagim</em> can be found
 * in the <a href="https://www.nli.org.il/he/books/NNL_ALEPH001141272/NLI">Minhag Yisrael Torah</a> on Orach
 * Chaim 131.
 * Dates used in specific communities such as specific <em>yahrzeits</em> or a holidays like Purim Mezhbizh
 * (Medzhybizh) celebrated on 11 {@link JewishDate#TEVES <em>Teves</em>} or <a href=
 * "https://en.wikipedia.org/wiki/Second_Purim#Purim_Saragossa_(18_Shevat)">Purim Saragossa</a> celebrated on
 * the (17th or) 18th of {@link JewishDate#SHEVAT <em>Shevat</em>} are not (and likely will not be) supported by
 * this class.
 * <p>Sample code:
 * <pre style="background: #FEF0C9; display: inline-block;">
 * TefilaRules tr = new TefilaRules();
 * JewishCalendar jewishCalendar = new JewishCalendar();
 * HebrewDateFormatter hdf = new HebrewDateFormatter();
 * jewishCalendar.setJewishDate(5783, JewishDate.TISHREI, 1); // Rosh Hashana
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jd));
 * jewishCalendar.setJewishDate(5783, JewishDate.ADAR, 17);
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
 * tr.setTachanunRecitedWeekOfPurim(false);
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));</pre>
 * 
 * @author &copy; Y. Paritcher 2019 - 2021
 * @author &copy; Eliyahu Hershfeld 2019 - 2023
 * 
 * @todo The following items may be added at a future date.
 * <ol>
 * <li><em>Lamnatzaiach</em></li>
 * <li><em>Selichos</em></li>
 * <li>...</li>
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
	 * @see #isTachanunRecitedMinchaAllYear()
	 * @see #setTachanunRecitedMinchaAllYear(boolean)
	 */
	private boolean tachanunRecitedMinchaAllYear = true;
	
	/**
	 * The default value is <code>false</code>.
	 * @see #isMizmorLesodaRecited(JewishCalendar)
	 * @see #setMizmorLesodaRecitedErevYomKippurAndPesach(boolean)
	 */
	private boolean mizmorLesodaRecitedErevYomKippurAndPesach = false;
	
	/**
	 * Returns if <em>tachanun</em> is recited during <em>shacharis</em> on the day in question. There are the many
	 * <em>minhagim</em> based settings that are available in this class that control what days are set for
	 * <em>tachanun</em> recital.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>tachanun</em> is recited during <em>shacharis</em>.
	 * @see #isTachanunRecitedMincha(JewishCalendar)
	 * @see #isTachanunRecitedSundays()
	 * @see #isTachanunRecitedFridays()
	 * @see #isTachanunRecitedEndOfTishrei()
	 * @see #isTachanunRecitedWeekAfterShavuos()
	 * @see #isTachanunRecited13SivanOutOfIsrael()
	 * @see #isTachanunRecitedPesachSheni()
	 * @see #isTachanunRecited15IyarOutOfIsrael()
	 * @see #isTachanunRecitedShivasYemeiHamiluim()
	 * @see #isTachanunRecitedWeekOfPurim()
	 * @see #isTachanunRecitedWeekOfHod()
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
	 * Returns if <em>tachanun</em> is recited during <em>mincha</em> on the day in question.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>tachanun</em> is recited during <em>mincha</em>.
	 * @see #isTachanunRecitedShacharis(JewishCalendar)
	 */
	public boolean isTachanunRecitedMincha(JewishCalendar jewishCalendar) {
		JewishCalendar tomorrow = new JewishCalendar();
		tomorrow = (JewishCalendar) jewishCalendar.clone();
		tomorrow.forward(Calendar.DATE, 1);
		
		if (!tachanunRecitedMinchaAllYear
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
	 * (<em>Sheailas Geshamim</em>). In Israel this is the 7th day of {@link JewishDate#CHESHVAN <em>Marcheshvan</em>}.
	 * Outside Israel recitation starts on the evening of December 4th (or 5th if it is the year before a civil leap year)
	 * in the 21st century and shifts a day forward every century not evenly divisible by 400. This method will return true
	 * if <em>vesein tal umatar</em> on the current Jewish date that starts on the previous night, so Dec 5/6 will be
	 * returned by this method in the 21st century. <em>vesein tal umatar</em> is not recited on <em>Shabbos</em> and the
	 * start date will be delayed a day when the start day is on a <em>Shabbos</em> (this can only occur out of Israel).
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
	 * <em>Sheailas Geshamim</em>). In Israel this is the 7th day of {@link JewishDate#CHESHVAN
	 * <em>Marcheshvan</em>} (so the 6th will return true). Outside Israel recitation starts on the evening
	 * of December 4th (or 5th if it is the year before a civil leap year) in the 21st century and shifts a
	 * day forward every century not evenly divisible by 400. <em>Vesein tal umatar</em> is not recited on
	 * <em>Shabbos</em> and the start date will be delayed a day when the start day is on a <em>Shabbos</em>
	 * (this can only occur out of Israel).
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar
	 *         Livracha</em> (<em>Sheailas Geshamim</em>).
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
	 * Returns if <em>Vesein Beracha</em> is recited. It is recited from 15 {@link JewishDate#NISSAN <em>Nissan</em>} to the
	 * point that {@link #isVeseinTalUmatarRecited(JewishCalendar) <em>vesein tal umatar</em> is recited}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return true if <em>Vesein Beracha</em> is recited.
	 * @see #isVeseinTalUmatarRecited(JewishCalendar)
	 */
	public boolean isVeseinBerachaRecited(JewishCalendar jewishCalendar) {
		return ! isVeseinTalUmatarRecited(jewishCalendar);
	}

	/**
	 * Returns if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 22
	 * {@link JewishDate#TISHREI <em>Tishrei</em>}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return true if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachStartDate(JewishCalendar jewishCalendar) {
		return jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 22;
	}

	/**
	 * Returns if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 15
	 * {@link JewishDate#NISSAN <em>Nissan</em>}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return true if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachRecited(JewishCalendar)
	 */
	public boolean isMashivHaruachEndDate(JewishCalendar jewishCalendar) {
		return jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 15;
	}

	/**
	 * Returns if <em>Mashiv Haruach Umorid Hageshem</em> is recited. This period starts on 22 {@link
	 * JewishDate#TISHREI <em>Tishrei</em>} and ends on the 15th day of {@link JewishDate#NISSAN <em>Nissan</em>}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return true if <em>Mashiv Haruach Umorid Hageshem</em> is recited.
	 * @see #isMashivHaruachStartDate(JewishCalendar)
	 * @see #isMashivHaruachEndDate(JewishCalendar)
	 */
	public boolean isMashivHaruachRecited(JewishCalendar jewishCalendar) {
		JewishDate startDate = new JewishDate(jewishCalendar.getJewishYear(), JewishDate.TISHREI, 22);
		JewishDate endDate = new JewishDate(jewishCalendar.getJewishYear(), JewishDate.NISSAN, 15);
		return jewishCalendar.compareTo(startDate) > 0 && jewishCalendar.compareTo(endDate) < 0;
	}

	/**
	 * Returns if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is
	 * recited. This period starts on the 15th day of {@link JewishDate#NISSAN <em>Nissan</em>} and ends on 22 {@link
	 * JewishDate#TISHREI <em>Tishrei</em>}.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * 
	 * @return true if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 */
	public boolean isMoridHatalRecited(JewishCalendar jewishCalendar) {
		return !isMashivHaruachRecited(jewishCalendar) || isMashivHaruachStartDate(jewishCalendar) || isMashivHaruachEndDate(jewishCalendar);
	}
	
	/**
	 * Returns if <em>Hallel</em> is recited on the day in question. This will return true for both <em>Hallel shalem</em>
	 * and <em>Chatzi Hallel</em>. See {@link #isHallelShalemRecited(JewishCalendar)} to know if the complete <em>Hallel</em>
	 * is recited.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>Hallel</em> is recited.
	 * @see #isHallelShalemRecited(JewishCalendar)
	 */
	public boolean isHallelRecited(JewishCalendar jewishCalendar) {
		int day = jewishCalendar.getJewishDayOfMonth();
		int month = jewishCalendar.getJewishMonth();
		int holidayIndex = jewishCalendar.getYomTovIndex();
		boolean inIsrael = jewishCalendar.getInIsrael();
		
		if(jewishCalendar.isRoshChodesh()) { //RH returns false for RC
			return true;
		}
		if(jewishCalendar.isChanukah()) {
			return true;
		}
		switch (month) {
			case JewishDate.NISSAN:
				if(day >= 15 && ((inIsrael && day <= 21) || (!inIsrael && day <= 22))){
					return true;
				}
				break;
			case JewishDate.IYAR: // modern holidays
				if(jewishCalendar.isUseModernHolidays()  && (holidayIndex == JewishCalendar.YOM_HAATZMAUT
						|| holidayIndex == JewishCalendar.YOM_YERUSHALAYIM)){
					return true;
				}
				break;
			case JewishDate.SIVAN:
				if (day == 6 || (!inIsrael && (day == 7))){
					return true;
				}
				break;
			case JewishDate.TISHREI:
				if (day >= 15 && (day <= 22 || (!inIsrael && (day <= 23)))){
					return true;
				}
		}
		return false;
	}

	/**
	 * Returns if <em>hallel shalem</em> is recited on the day in question. This will always return false if {@link
	 * #isHallelRecited(JewishCalendar)} returns false.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>hallel shalem</em> is recited.
	 * @see #isHallelRecited(JewishCalendar)
	 */
	public boolean isHallelShalemRecited(JewishCalendar jewishCalendar) {
		int day = jewishCalendar.getJewishDayOfMonth();
		int month = jewishCalendar.getJewishMonth();
		boolean inIsrael = jewishCalendar.getInIsrael();
		if(isHallelRecited(jewishCalendar)) {
			if((jewishCalendar.isRoshChodesh() && ! jewishCalendar.isChanukah())
					|| (month == JewishDate.NISSAN && ((inIsrael && day > 15) || (!inIsrael && day > 16)))) {
				return false;
			} else {
				return true;
			}
		} 
		return false;
	}
	
	/**
	 * Returns if <a href="https://en.wikipedia.org/wiki/Al_HaNissim"><em>Al HaNissim</em></a> is recited on the day in question.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>al hanissim</em> is recited.
	 * @see JewishCalendar#isChanukah()
	 * @see JewishCalendar#isPurim()
	 * @see JewishCalendar#getIsMukafChoma()
	 */
	public boolean isAlHanissimRecited(JewishCalendar jewishCalendar) {
	    return jewishCalendar.isPurim() || jewishCalendar.isChanukah();
	}
	
	/**
	 * Returns if <em>Yaaleh Veyavo</em> is recited on the day in question.
	 * 
	 * @param jewishCalendar the Jewish calendar day.
	 * @return if <em>Yaaleh Veyavo</em> is recited.
	 * @see JewishCalendar#isPesach()
	 * @see JewishCalendar#isShavuos()
	 * @see JewishCalendar#isRoshHashana()
	 * @see JewishCalendar#isYomKippur()
	 * @see JewishCalendar#isSuccos()
	 * @see JewishCalendar#isShminiAtzeres()
	 * @see JewishCalendar#isSimchasTorah()
	 * @see JewishCalendar#isRoshChodesh()
	 */
	public boolean isYaalehVeyavoRecited(JewishCalendar jewishCalendar) {
	    return jewishCalendar.isPesach() || jewishCalendar.isShavuos() ||jewishCalendar.isRoshHashana() || jewishCalendar.isYomKippur()
	    		|| jewishCalendar.isSuccos() || jewishCalendar.isShminiAtzeres() || jewishCalendar.isSimchasTorah()
	    		|| jewishCalendar.isRoshChodesh();
	}
	
	/**
	 * Returns if Is <em>Mizmor Lesoda</em> is recited on the day in question. 
	 * @param jewishCalendar  the Jewish calendar day.
	 * @return if <em>Mizmor Lesoda</em> is recited.
	 * 
	 * @see #isMizmorLesodaRecitedErevYomKippurAndPesach()
	 * 
	 */
	public boolean isMizmorLesodaRecited(JewishCalendar jewishCalendar) {
		if(jewishCalendar.isAssurBemelacha()) {
			return false;
		}
		
		int holidayIndex = jewishCalendar.getYomTovIndex();
		if(! isMizmorLesodaRecitedErevYomKippurAndPesach()
				&& (holidayIndex == JewishCalendar.EREV_YOM_KIPPUR
						|| holidayIndex == JewishCalendar.EREV_PESACH
						|| jewishCalendar.isCholHamoedPesach())) {
			return false;
		}
	    return true;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited during the week of Purim, from the 11th through the 17th of {@link
	 * JewishDate#ADAR <em>Adar</em>} (on a non-leap year, or {@link JewishDate#ADAR_II <em>Adar II</em>} on a leap year). Some
	 * <em>chasidishe</em> communities do not recite <em>tachanun</em> during this period. See the <a href=
	 * "https://www.nli.org.il/he/books/NNL_ALEPH001141272/NLI">Minhag Yisrael Torah</a> 131 and <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=4692&st=&pgnum=70">Darkei Chaim Veshalom 191</a>who discuss the
	 * <em>minhag</em> not to recite <em>tachanun</em>. Also see the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=8944&st=&pgnum=160">Mishmeres Shalom (Hadras Shalom)</a> who discusses the
	 * <em>minhag</em> of not reciting it on the 16th and 17th.
	 * @return If <em>tachanun</em> is set to be recited during the week of Purim from the 11th through the 17th of {@link
	 *         JewishDate#ADAR <em>Adar</em>} (on a non-leap year, or {@link JewishDate#ADAR_II <em>Adar II</em>} on a leap year).
	 * @see #setTachanunRecitedWeekOfPurim(boolean)
	 */
	public boolean isTachanunRecitedWeekOfPurim() {
		return tachanunRecitedWeekOfPurim;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited during the week of Purim from the 11th through the 17th of {@link
	 * JewishDate#ADAR <em>Adar</em>} (on a non-leap year), or {@link JewishDate#ADAR_II <em>Adar II</em>} (on a leap year).
	 * @param tachanunRecitedWeekOfPurim Sets if <em>tachanun</em> is to recited during the week of Purim from the 11th
	 *         through the 17th of {@link JewishDate#ADAR <em>Adar</em>} (on a non-leap year), or {@link JewishDate#ADAR_II
	 *         <em>Adar II</em>} (on a leap year). Some <em>chasidishe</em> communities do not recite <em>tachanun</em>
	 *         during this period.
	 * @see #isTachanunRecitedWeekOfPurim()
	 */
	public void setTachanunRecitedWeekOfPurim(boolean tachanunRecitedWeekOfPurim) {
		this.tachanunRecitedWeekOfPurim = tachanunRecitedWeekOfPurim;
	}

	/**
	 * Is <em>tachanun</em> set to be recited during the <em>sefira</em> week of <em>Hod</em> (14 - 20 {@link
	 * JewishDate#IYAR <em>Iyar</em>}, or the 29th - 35th of the {@link JewishCalendar#getDayOfOmer() <em>Omer</em>}). Some
	 * <em>chasidishe</em> communities do not recite <em>tachanun</em> during this week. See Minhag Yisrael Torah 131:Iyar.
	 * @return If <em>tachanun</em> is set to be recited during the <em>sefira</em> week of <em>Hod</em> (14 - 20 {@link
	 *         JewishDate#IYAR <em>Iyar</em>}, or the 29th - 35th of the {@link JewishCalendar#getDayOfOmer() <em>Omer</em>}).
	 * @see #setTachanunRecitedWeekOfHod(boolean)
	 */
	public boolean isTachanunRecitedWeekOfHod() {
		return tachanunRecitedWeekOfHod;
	}

	/**
	 * Sets if <em>tachanun</em> should should be recited during the <em>sefira</em> week of <em>Hod</em> (14 - 20
	 * {@link JewishDate#IYAR <em>Iyar</em>}, or the 29th - 35th of the {@link JewishCalendar#getDayOfOmer() <em>Omer</em>}).
	 * @param tachanunRecitedWeekOfHod Sets if <em>tachanun</em> should be recited during the <em>sefira</em> week of
	 * <em>Hod</em>.
	 * @see #isTachanunRecitedWeekOfHod()
	 */
	public void setTachanunRecitedWeekOfHod(boolean tachanunRecitedWeekOfHod) {
		this.tachanunRecitedWeekOfHod = tachanunRecitedWeekOfHod;
	}

	/**
	 * Is <em>tachanun</em> set to be recited at the end Of {@link JewishDate#TISHREI <em>Tishrei</em>}.The Magen Avraham 669:1 and
	 * the Pri Chadash 131:7 state that some places to not recite <em>tachanun</em> during this period. The Sh"UT Chasam Sofer on
	 * Choshen Mishpat 77 writes that this is the <em>minhag</em> in Ashkenaz. The Shaarei Teshuva 131:19 quotes the Sheyarie Kneses
	 * Hagdola who also states that it should not be recited. The Aderes wanted to institute saying <em>tachanun</em> during this
	 * period, but was dissuaded from this by Rav Shmuel Salant who did not want to change the <em>minhag</em> in Yerushalayim.
	 * The Aruch Hashulchan is of the opinion that that this <em>minhag</em> is incorrect, and it should be recited, and The Chazon
	 * Ish also recited <em>tachanun</em> during this period. See the Dirshu edition of the Mishna Berurah for details.
	 * @return If <em>tachanun</em> is set to be recited at the end of {@link JewishDate#TISHREI <em>Tishrei</em>}.
	 * @see #setTachanunRecitedEndOfTishrei(tachanunRecitedEndOfTishrei)
	 */
	public boolean isTachanunRecitedEndOfTishrei() {
		return tachanunRecitedEndOfTishrei;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited at the end of {@link JewishDate#TISHREI <em>Tishrei</em>}.
	 * @param tachanunRecitedEndOfTishrei is <em>tachanun</em> recited at the end of {@link JewishDate#TISHREI <em>Tishrei</em>}.
	 * @see #isTachanunRecitedEndOfTishrei()
	 */
	public void setTachanunRecitedEndOfTishrei(boolean tachanunRecitedEndOfTishrei) {
		this.tachanunRecitedEndOfTishrei = tachanunRecitedEndOfTishrei;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited during the week after <em>Shavuos</em>. This is the opinion of the Pri
	 * Megadim quoted by the Mishna Berurah. This is since <em>karbanos</em> of <em>Shavuos</em> have <em>tashlumim</em>
	 * for 7 days, it is still considered like a <em>Yom Tov</em>. The Chazon Ish quoted in the Orchos Rabainu vol. 1
	 * page 68 recited <em>tachanun</em> during this week.
	 * 
	 * @return If <em>tachanun</em> is set to be recited during the week after Shavuos.
	 * @see #setTachanunRecitedWeekAfterShavuos(boolean)
	 */
	public boolean isTachanunRecitedWeekAfterShavuos() {
		return tachanunRecitedWeekAfterShavuos;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited during the week after <em>Shavuos</em>.
	 * @param tachanunRecitedWeekAfterShavuos is <em>tachanun</em> recited during the week after Shavuos.
	 * @see #isTachanunRecitedWeekAfterShavuos()
	 */
	public void setTachanunRecitedWeekAfterShavuos(boolean tachanunRecitedWeekAfterShavuos) {
		this.tachanunRecitedWeekAfterShavuos = tachanunRecitedWeekAfterShavuos;
	}
	
	/**
	 * Is <em>tachanun</em> is set to be recited on the 13th of {@link JewishDate#SIVAN <em>Sivan</em>} (<a href=
	 * "https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot"><em>Yom Tov Sheni shel Galuyos</em></a> of the 7th
	 * day) outside Israel. This is brought down by the Shaarie Teshuva 131:19 quoting the <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=41295&st=&pgnum=39">Sheyarei Kneses Hagedola 131:12</a>that
	 * <em>tachanun</em> should not be recited on this day. Rav Shlomo Zalman Orbach in Halichos Shlomo on
	 * Shavuos 12:16:25 is of the opinion that even in <em>chutz laaretz</em> it should be recited since the <em>yemei
	 * Tashlumin</em> are counted based on Israel since that is where the <em>karbanos</em> are brought. Both
	 * {@link #isTachanunRecitedShacharis(JewishCalendar)} and {@link #isTachanunRecitedMincha(JewishCalendar)}
	 * only return false if the location is not set to {@link JewishCalendar#getInIsrael() Israel} and both
	 * {@link #tachanunRecitedWeekAfterShavuos} and {@link #setTachanunRecited13SivanOutOfIsrael} are set to false.
	 * 
	 * @return If <em>tachanun</em> is set to be recited on the 13th of {@link JewishDate#SIVAN <em>Sivan</em>} out of Israel.
	 * @see #setTachanunRecited13SivanOutOfIsrael(isTachanunRecitedThirteenSivanOutOfIsrael)
	 * @see #isTachanunRecitedWeekAfterShavuos()
	 */
	public boolean isTachanunRecited13SivanOutOfIsrael() {
		return tachanunRecited13SivanOutOfIsrael;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on the 13th of {@link JewishDate#SIVAN <em>Sivan</em>} (<a href=
	 * "https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot"><em>Yom Tov Sheni shel Galuyos</em></a> of the 7th
	 * day) outside Israel. 
	 * @param tachanunRecitedThirteenSivanOutOfIsrael sets if <em>tachanun</em> should be recited on the 13th of {@link
	 *          JewishDate#SIVAN <em>Sivan</em>} out of Israel. Both {@link #isTachanunRecitedShacharis(JewishCalendar)} and
	 *          {@link #isTachanunRecitedMincha(JewishCalendar)} only return false if the location is not set to {@link
	 *          JewishCalendar#getInIsrael() Israel} and both {@link #tachanunRecitedWeekAfterShavuos} and
	 *          {@link #setTachanunRecited13SivanOutOfIsrael} are set to false.
	 * @see #isTachanunRecited13SivanOutOfIsrael()
	 */
	public void setTachanunRecited13SivanOutOfIsrael(boolean tachanunRecitedThirteenSivanOutOfIsrael) {
		this.tachanunRecited13SivanOutOfIsrael = tachanunRecitedThirteenSivanOutOfIsrael;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited on {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}. The Pri Chadash 131:7
	 * states that <em>tachanun</em> should not be recited. The Aruch Hashulchan states that this is the minhag of the
	 * <em>sephardim</em>. The Shaarei Efraim 10:27 also mentions that it is not recited, as does the Siddur Yaavetz (Shaar Hayesod,
	 * Chodesh Iyar). The Pri Megadim (Mishbetzes Hazahav 131:15) and the Chazon Ish (Erev Pesahc Shchal Beshabos, page 203 in
	 * <a href="https://he.wikipedia.org/wiki/%D7%A9%D7%A8%D7%99%D7%94_%D7%93%D7%91%D7%9C%D7%99%D7%A6%D7%A7%D7%99">Rav Sheraya
	 * Devlitzky's</a> comments).
	 * 
	 * @return If <em>tachanun</em> is set to be recited on {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}.
	 * @see #setTachanunRecitedPesachSheni(boolean)
	 */
	public boolean isTachanunRecitedPesachSheni() {
		return tachanunRecitedPesachSheni;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}.
	 * @param tachanunRecitedPesachSheni sets if <em>tachanun</em> should be recited on <em>Pesach Sheni</em>.
	 * @see #isTachanunRecitedPesachSheni()
	 */
	public void setTachanunRecitedPesachSheni(boolean tachanunRecitedPesachSheni) {
		this.tachanunRecitedPesachSheni = tachanunRecitedPesachSheni;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited on 15 {@link JewishDate#IYAR <em>Iyar</em>} (<em>sfaika deyoma</em> of {@link
	 * JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}) out of Israel. If {@link #isTachanunRecitedPesachSheni()} is
	 * <code>true</code> this will be ignored even if <code>false</code>.
	 * 
	 * @return if <em>tachanun</em> is set to be recited on 15 {@link JewishDate#IYAR <em>Iyar</em>}  (<em>sfaika deyoma</em>
	 *          of {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>} out of Israel. If
	 *          {@link #isTachanunRecitedPesachSheni()} is <code>true</code> this will be ignored even if <code>false</code>.
	 * @see #setTachanunRecited15IyarOutOfIsrael(boolean)
	 * @see #setTachanunRecitedPesachSheni(boolean)
	 * @see #isTachanunRecitedPesachSheni()
	 */
	public boolean isTachanunRecited15IyarOutOfIsrael() {
		return tachanunRecited15IyarOutOfIsrael;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on the 15th of {@link JewishDate#IYAR <em>Iyar</em>}  (<a href=
	 * "https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot"><em>Yom Tov Sheni shel Galuyos</em></a> of
	 * {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}) out of Israel. Ignored if {@link
	 * #isTachanunRecitedPesachSheni()} is <code>true</code>.
	 * 
	 * @param tachanunRecited15IyarOutOfIsrael if <em>tachanun</em> should be recited on the 15th of {@link JewishDate#IYAR
	 *          <em>Iyar</em>} (<em>sfaika deyoma</em> of {@link JewishCalendar#PESACH_SHENI <em>Pesach Sheni</em>}) out of Israel.
	 * @see #isTachanunRecited15IyarOutOfIsrael()
	 */
	public void setTachanunRecited15IyarOutOfIsrael(boolean tachanunRecited15IyarOutOfIsrael) {
		this.tachanunRecited15IyarOutOfIsrael = tachanunRecited15IyarOutOfIsrael;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited on <em>mincha</em> on <em>erev {@link JewishCalendar#LAG_BAOMER Lag
	 * Baomer}</em>.
	 * @return if <em>tachanun</em> is set to be recited in <em>mincha</em> on <em>erev</em>
	 *          {@link JewishCalendar#LAG_BAOMER <em>Lag Baomer</em>}.
	 * @see #setTachanunRecitedMinchaErevLagBaomer(boolean)
	 */
	public boolean isTachanunRecitedMinchaErevLagBaomer() {
		return tachanunRecitedMinchaErevLagBaomer;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on <em>erev {@link JewishCalendar#LAG_BAOMER Lag Baomer}</em>.
	 * @param tachanunRecitedMinchaErevLagBaomer sets if <em>tachanun</em> should be recited on <em>mincha</em>
	 *          of <em>erev {@link JewishCalendar#LAG_BAOMER Lag Baomer}</em>.
	 * @see #isTachanunRecitedMinchaErevLagBaomer()
	 */
	public void setTachanunRecitedMinchaErevLagBaomer(boolean tachanunRecitedMinchaErevLagBaomer) {
		this.tachanunRecitedMinchaErevLagBaomer = tachanunRecitedMinchaErevLagBaomer;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited during the <em>Shivas Yemei Hamiluim</em>, from the 23 of {@link
	 * JewishDate#ADAR <em>Adar</em>} on a non-leap-year or {@link JewishDate#ADAR_II <em>Adar II</em>} on a
	 * leap year to the end of the month. Some <em>chasidishe</em> communities do not say <em>tachanun</em>
	 * during this week. See <a href="https://hebrewbooks.org/pdfpager.aspx?req=4692&st=&pgnum=70">Darkei
	 * Chaim Veshalom 191</a>.
	 * @return if <em>tachanun</em> is set to be recited during the <em>Shivas Yemei Hamiluim</em>, from the 23
	 *           of {@link JewishDate#ADAR <em>Adar</em>} on a non-leap-year or {@link JewishDate#ADAR_II
	 *           <em>Adar II</em>} on a leap year to the end of the month.
	 * @see #setTachanunRecitedShivasYemeiHamiluim(boolean)
	 */
	public boolean isTachanunRecitedShivasYemeiHamiluim() {
		return tachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited during the <em>Shivas Yemei Hamiluim</em>, from the 23 of
	 * {@link JewishDate#ADAR <em>Adar</em>} on a non-leap-year or {@link JewishDate#ADAR_II <em>Adar II</em>}
	 * on a leap year to the end of the month.
	 * @param tachanunRecitedShivasYemeiHamiluim sets if <em>tachanun</em> should be recited during the
	 *          <em>Shivas Yemei Hamiluim</em>.
	 * @see #isTachanunRecitedShivasYemeiHamiluim()
	 */
	public void setTachanunRecitedShivasYemeiHamiluim(boolean tachanunRecitedShivasYemeiHamiluim) {
		this.tachanunRecitedShivasYemeiHamiluim = tachanunRecitedShivasYemeiHamiluim;
	}

	/**
	 * Is <em>tachanun</em> set to be recited on Fridays. Some <em>chasidishe</em> communities do not recite
	 * <em>tachanun</em> on Fridays. See <a href="https://hebrewbooks.org/pdfpager.aspx?req=41190&st=&pgnum=10">Likutei
	 * Maharich Vol 2 Seder Hanhagos Erev Shabbos</a>. This is also the <em>minhag</em> in Satmar.
	 * @return if <em>tachanun</em> is set to be recited on Fridays.
	 * @see #setTachanunRecitedFridays(boolean)
	 */
	public boolean isTachanunRecitedFridays() {
		return tachanunRecitedFridays;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on Fridays. 
	 * @param tachanunRecitedFridays sets if <em>tachanun</em> should be recited on Fridays. Some <em>chasidishe</em>
	 *          communities do not recite <em>tachanun</em> on Fridays.
	 * @see #isTachanunRecitedFridays()
	 */
	public void setTachanunRecitedFridays(boolean tachanunRecitedFridays) {
		this.tachanunRecitedFridays = tachanunRecitedFridays;
	}

	/**
	 * Is <em>tachanun</em> set to be recited on Sundays. Some <em>chasidishe</em> communities do not recite
	 * <em>tachanun</em> on Sundays. See <a href="https://hebrewbooks.org/pdfpager.aspx?req=41190&st=&pgnum=10">Likutei
	 * Maharich Vol 2 Seder Hanhagos Erev Shabbos</a>. 
	 * @return if <em>tachanun</em> is set to be recited on Sundays.
	 * @see #setTachanunRecitedSundays(boolean)
	 */
	public boolean isTachanunRecitedSundays() {
		return tachanunRecitedSundays;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited on Sundays. 
	 * @param tachanunRecitedSundays sets if <em>tachanun</em> should be recited on Sundays. Some <em>chasidishe</em>
	 *          communities do not recite <em>tachanun</em> on Sundays.
	 * @see #isTachanunRecitedSundays()
	 */
	public void setTachanunRecitedSundays(boolean tachanunRecitedSundays) {
		this.tachanunRecitedSundays = tachanunRecitedSundays;
	}
	
	/**
	 * Is <em>tachanun</em> set to be recited in <em>Mincha</em> the entire year. Some <em>chasidishe</em> communities do
	 * not recite <em>tachanun</em> by <em>Mincha</em> all year round. See <a href=
	 * "https://hebrewbooks.org/pdfpager.aspx?req=4751&st=&pgnum=105">Nemukei Orach Chaim 131:3</a>.
	 * @return if <em>tachanun</em> is set to be recited in <em>Mincha</em> the entire year.
	 * @see #setTachanunRecitedMinchaAllYear(boolean)
	 */
	public boolean isTachanunRecitedMinchaAllYear() {
		return tachanunRecitedMinchaAllYear;
	}

	/**
	 * Sets if <em>tachanun</em> should be recited in <em>Mincha</em> the entire year.
	 * @param tachanunRecitedMinchaAllYear sets if <em>tachanun</em> should be recited by <em>mincha</em> all year. If set
	 *          to false, {@link #isTachanunRecitedMincha(JewishCalendar)} will always return false. If set to true (the
	 *          default), it will use the regular rules.
	 * @see #isTachanunRecitedMinchaAllYear()
	 */
	public void setTachanunRecitedMinchaAllYear(boolean tachanunRecitedMinchaAllYear) {
		this.tachanunRecitedMinchaAllYear = tachanunRecitedMinchaAllYear;
	}

	/**
	 * Sets if <em>Mizmor Lesoda</em> should be recited on <em>Erev Yom Kippur</em>, <em>Erev Pesach</em> and <em>Chol
	 * Hamoed Pesach</em>. Ashkenazi congregations do not recite it on these days, while Sephardi congregations do. The
	 * default value is <code>false</code>.
	 * @param mizmorLesodaRecitedErevYomKippurAndPesach Sets if <em>Mizmor Lesoda</em> should be recited on <em>Erev Yom
	 *          Kippur</em>, <em>Erev Pesach</em> and <em>Chol Hamoed Pesach</em>. If set to true (the default value is
	 *          <code>false</code>).
	 * @see #isTachanunRecitedMinchaAllYear()
	 */
	public void setMizmorLesodaRecitedErevYomKippurAndPesach(boolean mizmorLesodaRecitedErevYomKippurAndPesach) {
		this.mizmorLesodaRecitedErevYomKippurAndPesach = mizmorLesodaRecitedErevYomKippurAndPesach;
	}
	
	/**
	 * Is <em>Mizmor Lesoda</em> set to be recited on <em>Erev Yom Kippur</em>, <em>Erev Pesach</em> and <em>Chol
	 * Hamoed Pesach</em>. Ashkenazi congregations do not recite it on these days, while Sephardi congregations do.
	 * The default value is <code>false</code>.
	 * @return if <em>Mizmor Lesoda</em> is set to be recited on <em>Erev Yom Kippur</em>, <em>Erev Pesach</em> and
	 *          <em>Chol Hamoed Pesach</em>. If set to true (the default value is <code>false</code>).
	 * @see #isMizmorLesodaRecited(JewishCalendar)
	 */
	public boolean isMizmorLesodaRecitedErevYomKippurAndPesach() {
		return mizmorLesodaRecitedErevYomKippurAndPesach;
	}

}
