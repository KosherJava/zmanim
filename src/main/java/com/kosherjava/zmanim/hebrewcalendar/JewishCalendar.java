/*
 * Zmanim Java API
 * Copyright (C) 2011 - 2024 Eliyahu Hershfeld
 * Copyright (C) September 2002 Avrom Finkelstien
 * Copyright (C) 2019 - 2022 Y Paritcher
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

import com.kosherjava.zmanim.util.GeoLocation;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The JewishCalendar extends the JewishDate class and adds calendar methods.
 * 
 * This open source Java code was originally ported by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a>
 * from his C++ code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements
 * and some bug fixing. The class allows setting whether the holiday and <em>parsha</em> scheme follows the Israel scheme
 * or outside Israel scheme. The default is the outside Israel scheme.
 * The parsha code was ported by Y. Paritcher from his <a href="https://github.com/yparitcher/libzmanim">libzmanim</a> code.
 * 
 * @todo Some do not belong in this class, but here is a partial list of what should still be implemented in some form:
 * <ol>
 * <li>Mishna yomis etc</li>
 * </ol>
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Y. Paritcher 2019 - 2022
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011 - 2024
 */
public class JewishCalendar extends JewishDate {
	/** The 14th day of Nissan, the day before Pesach (Passover).*/
	public static final int EREV_PESACH = 0;
	/** The holiday of Pesach (Passover) on the 15th (and 16th out of Israel) day of Nissan.*/
	public static final int PESACH = 1;
	/** Chol Hamoed (interim days) of Pesach (Passover)*/
	public static final int CHOL_HAMOED_PESACH = 2;
	/** Pesach Sheni, the 14th day of Iyar, a minor holiday.*/
	public static final int PESACH_SHENI = 3;
	/** Erev Shavuos (the day before Shavuos), the 5th of Sivan*/
	public static final int EREV_SHAVUOS = 4;
	/** Shavuos (Pentecost), the 6th of Sivan*/
	public static final int SHAVUOS = 5;
	/** The fast of the 17th day of Tammuz*/
	public static final int SEVENTEEN_OF_TAMMUZ = 6;
	/** The fast of the 9th of Av*/
	public static final int TISHA_BEAV = 7;
	/** The 15th day of Av, a minor holiday*/
	public static final int TU_BEAV = 8;
	/** Erev Rosh Hashana (the day before Rosh Hashana), the 29th of Elul*/
	public static final int EREV_ROSH_HASHANA = 9;
	/** Rosh Hashana, the first and second days of Tishrei.*/
	public static final int ROSH_HASHANA = 10;
	/** The fast of Gedalyah, the 3rd of Tishrei.*/
	public static final int FAST_OF_GEDALYAH = 11;
	/** The 9th day of Tishrei, the day before of Yom Kippur.*/
	public static final int EREV_YOM_KIPPUR = 12;
	/** The holiday of Yom Kippur, the 10th day of Tishrei*/
	public static final int YOM_KIPPUR = 13;
	/** The 14th day of Tishrei, the day before of Succos/Sukkos (Tabernacles).*/
	public static final int EREV_SUCCOS = 14;
	/** The holiday of Succos/Sukkos (Tabernacles), the 15th (and 16th out of Israel) day of Tishrei */
	public static final int SUCCOS = 15;
	/** Chol Hamoed (interim days) of Succos/Sukkos (Tabernacles)*/
	public static final int CHOL_HAMOED_SUCCOS = 16;
	/** Hoshana Rabba, the 7th day of Succos/Sukkos that occurs on the 21st of Tishrei. */
	public static final int HOSHANA_RABBA = 17;
	/** Shmini Atzeres, the 8th day of Succos/Sukkos is an independent holiday that occurs on the 22nd of Tishrei. */
	public static final int SHEMINI_ATZERES = 18;
	/** Simchas Torah, the 9th day of Succos/Sukkos, or the second day of Shmini Atzeres that is celebrated
	 * {@link #getInIsrael() out of Israel} on the 23rd of Tishrei.
	 */
	public static final int SIMCHAS_TORAH = 19;
	// public static final int EREV_CHANUKAH = 20;// probably remove this
	/** The holiday of Chanukah. 8 days starting on the 25th day Kislev.*/
	public static final int CHANUKAH = 21;
	/** The fast of the 10th day of Teves.*/
	public static final int TENTH_OF_TEVES = 22;
	/** Tu Bishvat on the 15th day of Shevat, a minor holiday.*/
	public static final int TU_BESHVAT = 23;
	/** The fast of Esther, usually on the 13th day of Adar (or Adar II on leap years). It is earlier on some years.*/
	public static final int FAST_OF_ESTHER = 24;
	/** The holiday of Purim on the 14th day of Adar (or Adar II on leap years).*/
	public static final int PURIM = 25;
	/** The holiday of Shushan Purim on the 15th day of Adar (or Adar II on leap years).*/
	public static final int SHUSHAN_PURIM = 26;
	/** The holiday of Purim Katan on the 14th day of Adar I on a leap year when Purim is on Adar II, a minor holiday.*/
	public static final int PURIM_KATAN = 27;
	/**
	 * Rosh Chodesh, the new moon on the first day of the Jewish month, and the 30th day of the previous month in the
	 * case of a month with 30 days.
	 */
	public static final int ROSH_CHODESH = 28;
	/** Yom HaShoah, Holocaust Remembrance Day, usually held on the 27th of Nissan. If it falls on a Friday, it is moved
	 * to the 26th, and if it falls on a Sunday it is moved to the 28th. A {@link #isUseModernHolidays() modern holiday}.
	 */
	public static final int YOM_HASHOAH = 29;
	/**
	 * Yom HaZikaron, Israeli Memorial Day, held a day before Yom Ha'atzmaut.  A {@link #isUseModernHolidays() modern holiday}.
	 */
	public static final int YOM_HAZIKARON = 30;
	
	/** Yom Ha'atzmaut, Israel Independence Day, the 5th of Iyar, but if it occurs on a Friday or Saturday, the holiday is
	 * moved back to Thursday, the 3rd of 4th of Iyar, and if it falls on a Monday, it is moved forward to Tuesday the
	 * 6th of Iyar.  A {@link #isUseModernHolidays() modern holiday}.*/
	public static final int YOM_HAATZMAUT = 31;
	/**
	 * Yom Yerushalayim or Jerusalem Day, on 28 Iyar. A {@link #isUseModernHolidays() modern holiday}.
	 */
	public static final int YOM_YERUSHALAYIM = 32;
	
	/** The 33rd day of the Omer, the 18th of Iyar, a minor holiday.*/
	public static final int LAG_BAOMER = 33;
	
	/** The holiday of Purim Katan on the 15th day of Adar I on a leap year when Purim is on Adar II, a minor holiday.*/
	public static final int SHUSHAN_PURIM_KATAN = 34;
	
	/** The day following the last day of Pesach, Shavuos and Sukkos.*/
	public static final int ISRU_CHAG = 35;
	
	/**
	 * The day before <em>Rosh Chodesh</em> (moved to Thursday if <em>Rosh Chodesh</em> is on a Friday or <em>Shabbos</em>) in most months.
	 * This constant is not actively in use.
	 * @see #isYomKippurKatan()
	 */
	public static final int YOM_KIPPUR_KATAN = 36;
	
	/**
	 * The Monday, Thursday and Monday after the first <em>Shabbos</em> after <em>Rosh Chodesh Cheshvan</em> and <em>Iyar</em>) are BeHab
	 * days. This constant is not actively in use.
	 * @see #isBeHaB()
	 */
	public static final int BEHAB = 37;

	/**
	 * Is the calendar set to Israel, where some holidays have different rules.
	 * @see #getInIsrael()
	 * @see #setInIsrael(boolean)
	 */
	private boolean inIsrael = false;
	
	/**
	 * Is the calendar set to have Purim <em>demukafim</em>, where Purim is celebrated on Shushan Purim.
	 * @see #getIsMukafChoma()
	 * @see #setIsMukafChoma(boolean)
	 */
	private boolean isMukafChoma = false;

	/**
	 * Is the calendar set to use modern Israeli holidays such as Yom Haatzmaut.
	 * @see #isUseModernHolidays()
	 * @see #setUseModernHolidays(boolean)
	 */
	private boolean useModernHolidays = false;

	/**
	 * List of <em>parshiyos</em> or special <em>Shabasos</em>. {@link #NONE} indicates a week without a <em>parsha</em>, while the enum for
	 * the <em>parsha</em> of {@link #VZOS_HABERACHA} exists for consistency, but is not currently used. The special <em>Shabasos</em> of
	 * Shekalim, Zachor, Para, Hachodesh, as well as Shabbos Shuva, Shira, Hagadol, Chazon and Nachamu are also represented in this collection
	 * of <em>parshiyos</em>.
	 * @see #getSpecialShabbos()
	 * @see #getParshah()
	 */
	public enum Parsha {
		/**NONE A week without any <em>parsha</em> such as <em>Shabbos Chol Hamoed</em> */NONE,
		/**BERESHIS*/BERESHIS, /**NOACH*/NOACH, /**LECH_LECHA*/LECH_LECHA, /**VAYERA*/VAYERA, /**CHAYEI_SARA*/CHAYEI_SARA, /**TOLDOS*/TOLDOS, 
		/**VAYETZEI*/VAYETZEI, /**VAYISHLACH*/VAYISHLACH, /**VAYESHEV*/VAYESHEV, /**MIKETZ*/MIKETZ, /**VAYIGASH*/VAYIGASH, /**VAYECHI*/VAYECHI,
		/**SHEMOS*/SHEMOS, /**VAERA*/VAERA, /**BO*/BO, /**BESHALACH*/BESHALACH, /**YISRO*/YISRO, /**MISHPATIM*/MISHPATIM, /**TERUMAH*/TERUMAH,
		/**TETZAVEH*/TETZAVEH, /***KI_SISA*/KI_SISA, /**VAYAKHEL*/VAYAKHEL, /**PEKUDEI*/PEKUDEI, /**VAYIKRA*/VAYIKRA, /**TZAV*/TZAV, /**SHMINI*/SHMINI, 
		/**TAZRIA*/TAZRIA, /**METZORA*/METZORA, /**ACHREI_MOS*/ACHREI_MOS, /**KEDOSHIM*/KEDOSHIM, /**EMOR*/EMOR, /**BEHAR*/BEHAR, /**BECHUKOSAI*/BECHUKOSAI,
		/**BAMIDBAR*/BAMIDBAR, /**NASSO*/NASSO, /**BEHAALOSCHA*/BEHAALOSCHA, /**SHLACH*/SHLACH, /**KORACH*/KORACH, /**CHUKAS*/CHUKAS, /**BALAK*/BALAK,
		/**PINCHAS*/PINCHAS, /**MATOS*/MATOS, /**MASEI*/MASEI, /**DEVARIM*/DEVARIM, /**VAESCHANAN*/VAESCHANAN, /**EIKEV*/EIKEV, /**REEH*/REEH,
		/**SHOFTIM*/SHOFTIM, /**KI_SEITZEI*/KI_SEITZEI, /**KI_SAVO*/KI_SAVO, /**NITZAVIM*/NITZAVIM, /**VAYEILECH*/VAYEILECH, /**HAAZINU*/HAAZINU,
		/**VZOS_HABERACHA*/VZOS_HABERACHA, /**The double parsha of Vayakhel &amp; Peudei*/VAYAKHEL_PEKUDEI, /**The double <em>parsha</em> of Tazria
		 * &amp; Metzora*/TAZRIA_METZORA,/**The double <em>parsha</em> of Achrei Mos &amp; Kedoshim*/ACHREI_MOS_KEDOSHIM,/**The double <em>parsha</em>
		 * of Behar &amp; Bechukosai*/BEHAR_BECHUKOSAI,/**The double <em>parsha</em> of Chukas &amp; Balak*/CHUKAS_BALAK, /**The double
		 * <em>parsha</em> of Matos &amp; Masei*/MATOS_MASEI,/**The double <em>parsha</em> of Nitzavim &amp; Vayelech*/NITZAVIM_VAYEILECH,
		 /**The special <em>parsha</em> of Shekalim*/SHKALIM, /** The special <em>parsha</em> of Zachor*/ZACHOR, /**The special <em>parsha</em> of
		  * Para*/PARA, /** The special <em>parsha</em> of Hachodesh*/HACHODESH, /**<em>Shabbos</em> Shuva*/SHUVA, /**<em>Shabbos</em> Shira*/SHIRA, 
		  /**<em>Shabbos</em> Hagadol*/HAGADOL, /**<em>Shabbos</em> Chazon*/CHAZON, /**<em>Shabbos</em> Nachamu*/NACHAMU
	};
	
	/**
	 * An array of <em>parshiyos</em> in the 17 possible combinations.
	 */
	public static final Parsha[][] parshalist = {
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NONE, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS_BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NONE, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS_BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.ACHREI_MOS, Parsha.NONE, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS, Parsha.MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.ACHREI_MOS, Parsha.NONE, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS, Parsha.MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NONE, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS_BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR_BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL_PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.NONE, Parsha.SHMINI, Parsha.TAZRIA_METZORA, Parsha.ACHREI_MOS_KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH},
		{Parsha.NONE, Parsha.VAYEILECH, Parsha.HAAZINU, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS, Parsha.MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM},
		{Parsha.NONE, Parsha.NONE, Parsha.HAAZINU, Parsha.NONE, Parsha.NONE, Parsha.BERESHIS, Parsha.NOACH, Parsha.LECH_LECHA, Parsha.VAYERA, Parsha.CHAYEI_SARA, Parsha.TOLDOS, Parsha.VAYETZEI, Parsha.VAYISHLACH, Parsha.VAYESHEV, Parsha.MIKETZ, Parsha.VAYIGASH, Parsha.VAYECHI, Parsha.SHEMOS, Parsha.VAERA, Parsha.BO, Parsha.BESHALACH, Parsha.YISRO, Parsha.MISHPATIM, Parsha.TERUMAH, Parsha.TETZAVEH, Parsha.KI_SISA, Parsha.VAYAKHEL, Parsha.PEKUDEI, Parsha.VAYIKRA, Parsha.TZAV, Parsha.SHMINI, Parsha.TAZRIA, Parsha.METZORA, Parsha.NONE, Parsha.ACHREI_MOS, Parsha.KEDOSHIM, Parsha.EMOR, Parsha.BEHAR, Parsha.BECHUKOSAI, Parsha.BAMIDBAR, Parsha.NASSO, Parsha.BEHAALOSCHA, Parsha.SHLACH, Parsha.KORACH, Parsha.CHUKAS, Parsha.BALAK, Parsha.PINCHAS, Parsha.MATOS_MASEI, Parsha.DEVARIM, Parsha.VAESCHANAN, Parsha.EIKEV, Parsha.REEH, Parsha.SHOFTIM, Parsha.KI_SEITZEI, Parsha.KI_SAVO, Parsha.NITZAVIM_VAYEILECH}
	};

	/**
	 * Is this calendar set to return modern Israeli national holidays. By default, this value is false. The holidays
	 * are {@link #YOM_HASHOAH <em>Yom HaShoah</em>}, {@link #YOM_HAZIKARON <em>Yom Hazikaron</em>}, {@link
	 * #YOM_HAATZMAUT <em>Yom Ha'atzmaut</em>} and {@link #YOM_YERUSHALAYIM <em>Yom Yerushalayim</em>}.
	 * 
	 * @return the useModernHolidays true if set to return modern Israeli national holidays
	 * 
	 * @see #setUseModernHolidays(boolean)
	 */
	public boolean isUseModernHolidays() {
		return useModernHolidays;
	}

	/**
	 * Sets the calendar to return modern Israeli national holidays. By default, this value is false. The holidays are:
	 * {@link #YOM_HASHOAH <em>Yom HaShoah</em>}, {@link #YOM_HAZIKARON <em>Yom Hazikaron</em>}, {@link
	 * #YOM_HAATZMAUT <em>Yom Ha'atzmaut</em>} and {@link #YOM_YERUSHALAYIM <em>Yom Yerushalayim</em>}.
	 * 
	 * @param useModernHolidays
	 *            the useModernHolidays to set
	 * 
	 * @see #isUseModernHolidays()
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
	 * A constructor that initializes the date to the {@link java.time.LocalDate LocalDate} parameter.
	 * 
	 * @param localDate
	 *            the <code>LocalDate</code> to set the calendar to
	 */
	public JewishCalendar(LocalDate localDate) {
		super(localDate);
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
	 *            the Jewish month. The method expects a 1 for <em>Nissan</em> ... 12 for <em>Adar</em> and 13 for
	 *            <em>Adar II</em>. Use the constants {@link #NISSAN} ... {@link #ADAR} (or {@link #ADAR_II} for a
	 *            leap year Adar II) to avoid any confusion.
	 * @param jewishDayOfMonth
	 *            the Jewish day of month. If 30 is passed in for a month with only 29 days (for example {@link #IYAR},
	 *            or {@link #KISLEV} in a year that {@link #isKislevShort()}), the 29th (last valid date of the month)
	 *            will be set.
	 * @param inIsrael
	 *            whether in Israel. This affects <em>Yom Tov</em> calculations
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
	 * 
	 * @see #getInIsrael()
	 */
	public void setInIsrael(boolean inIsrael) {
		this.inIsrael = inIsrael;
	}

	/**
	 * Gets whether Israel holiday scheme is used or not. The default (if not set) is false.
	 * 
	 * @return if the calendar is set to Israel
	 * 
	 * @see #setInIsrael(boolean)
	 */
	public boolean getInIsrael() {
		return inIsrael;
	}
	
	/**
	 * Returns if the city is set as a city surrounded by a wall from the time of Yehoshua, and Shushan Purim
	 * should be celebrated as opposed to regular Purim.
	 * @return if the city is set as a city surrounded by a wall from the time of Yehoshua, and Shushan Purim
	 *         should be celebrated as opposed to regular Purim.
	 * @see #setIsMukafChoma(boolean)
	 */
	public boolean getIsMukafChoma() {
		return isMukafChoma;
	}

	/**
	 * Sets if the location is surrounded by a wall from the time of Yehoshua, and Shushan Purim should be
	 * celebrated as opposed to regular Purim. This should be set for Yerushalayim, Shushan and other cities.
	 * @param isMukafChoma is the city surrounded by a wall from the time of Yehoshua.
	 * 
	 * @see #getIsMukafChoma()
	 */
	public void setIsMukafChoma(boolean isMukafChoma) {
		this.isMukafChoma = isMukafChoma;
	}
	
	/**
	 * <a href="https://en.wikipedia.org/wiki/Birkat_Hachama">Birkas Hachamah</a> is recited every 28 years based on
	 * <em>Tekufas Shmuel</em> (Julian years) that a year is 365.25 days. The <a href="https://en.wikipedia.org/wiki/Maimonides"
	 * >Rambam</a> in <a href="http://hebrewbooks.org/pdfpager.aspx?req=14278&amp;st=&amp;pgnum=323">Hilchos Kiddush Hachodesh 9:3</a>
	 * states that <em>tekufas Nissan</em> of year 1 was 7 days + 9 hours before <em>molad Nissan</em>. This is calculated as every
	 * 10,227 days (28 * 365.25).  
	 * @return true for a day that <em>Birkas Hachamah</em> is recited.
	 */
	public boolean isBirkasHachamah() {
		int elapsedDays = getJewishCalendarElapsedDays(getJewishYear()); //elapsed days since molad ToHu
		elapsedDays = elapsedDays + getDaysSinceStartOfJewishYear(); //elapsed days to the current calendar date
		
		/* Molad Nissan year 1 was 177 days after molad tohu of Tishrei. We multiply 29.5 days * 6 months from Tishrei
		 * to Nissan = 177. Subtract 7 days since tekufas Nissan was 7 days and 9 hours before the molad as stated in the Rambam
		 * and we are now at 170 days. Because getJewishCalendarElapsedDays and getDaysSinceStartOfJewishYear use the value for
		 * Rosh Hashana as 1, we have to add 1 day for a total of 171. To this add a day since the tekufah is on a Tuesday
		 * night, and we push off the bracha to Wednesday morning resulting in the 172 used in the calculation.
		 */
		return elapsedDays % (28 * 365.25) == 172; // 28 years of 365.25 days + the offset from molad tohu mentioned above
	}

	/**
	 * Return the type of year for <em>parsha</em> calculations. The algorithm follows the
	 * <a href="http://hebrewbooks.org/pdfpager.aspx?req=14268&amp;st=&amp;pgnum=222">Luach Arba'ah Shearim</a> in the Tur Ohr Hachaim.
	 * @return the type of year for <em>parsha</em> calculations.
	 */
	private int getParshaYearType() {
		int roshHashanaDayOfWeek = (getJewishCalendarElapsedDays(getJewishYear()) + 1) % 7; // plus one to the original Rosh Hashana of year 1 to get a week starting on Sunday
		if (roshHashanaDayOfWeek == 0) {
			roshHashanaDayOfWeek = 7; // convert 0 to 7 for Shabbos for readability
		}
		if (isJewishLeapYear()) {
			switch (roshHashanaDayOfWeek) {
			case Calendar.MONDAY:
				if (isKislevShort()) { //BaCh
					if (getInIsrael()) {
						return 14;
					}
					return 6;
				}
				if (isCheshvanLong()) { //BaSh
					if (getInIsrael()) {
						return 15;
					}
					return 7;
				}
				break;
			case Calendar.TUESDAY: //Gak
				if (getInIsrael()) {
					return 15;
				}
				return 7;
			case Calendar.THURSDAY:
				if (isKislevShort()) { //HaCh
					return 8;
				}
				if (isCheshvanLong()) { //HaSh
					return 9;
				}
				break;
			case Calendar.SATURDAY:
				if (isKislevShort()) { //ZaCh
					return 10;
				}
				if (isCheshvanLong()) { //ZaSh
					if (getInIsrael()) {
						return 16;
					}
					return 11;
				}
				break;
			}
		} else { //not a leap year
			switch (roshHashanaDayOfWeek) {
			case Calendar.MONDAY:
				if (isKislevShort()) { //BaCh
					return 0;
				}
				if (isCheshvanLong()) { //BaSh
					if (getInIsrael()) {
						return 12;
					}
					return 1;
				}
				break;
			case Calendar.TUESDAY: //GaK
				if (getInIsrael()) {
					return 12;
				}
				return 1;
			case Calendar.THURSDAY:
				if (isCheshvanLong()) { //HaSh
					return 3;
				}
				if (!isKislevShort()) { //Hak
					if (getInIsrael()) {
						return 13;
					}
					return 2;
				}
				break;
			case Calendar.SATURDAY:
				if (isKislevShort()) { //ZaCh
					return 4;
				}
				if (isCheshvanLong()) { //ZaSh
					return 5;
				}
				break;
			}
		}
		return -1; //keep the compiler happy
	}

	/**
	 * Returns this week's {@link Parsha <em>Parsha</em>} if it is <em>Shabbos</em>. It returns {@link Parsha#NONE} if the date
	 * is a weekday or if there is no <em>parsha</em> that week (for example <em>Yom Tov</em> that falls on a <em>Shabbos</em>).
	 * 
	 * @return the current <em>parsha</em>.
	 */
	public Parsha getParshah() {
		if (getDayOfWeek() != Calendar.SATURDAY) {
			return Parsha.NONE;
		}
		
		int yearType = getParshaYearType();
		int roshHashanaDayOfWeek = getJewishCalendarElapsedDays(getJewishYear()) % 7;
		int day = roshHashanaDayOfWeek + getDaysSinceStartOfJewishYear();
		
		if (yearType >= 0) { // negative year should be impossible, but let's cover all bases
			return parshalist[yearType][day/7];
		}
		return Parsha.NONE; //keep the compiler happy
	}

	/**
	 * Returns this week's {@link Parsha <em>Parsha</em>} regardless of if it is the weekday or <em>Shabbos</em> (where next
	 * Shabbos's <em>Parsha</em> will be returned. If the upcoming <em>Shabbos</em> is a <em>Yom Tov</em> and has no <em>Parsha</em>,
	 * {@link Parsha#NONE} will be returned.
	 *
	 * @return the upcoming <em>parsha</em>.
	 */
	public Parsha getThisWeeksParshah() {
		JewishCalendar clone = (JewishCalendar) clone();
		int daysToShabbos = (Calendar.SATURDAY - getDayOfWeek() + 7) % 7;
		if (getDayOfWeek() != Calendar.SATURDAY) {
			clone.forward(Calendar.DATE, daysToShabbos);
		}
		return clone.getParshah();
	}

	/**
	 * Returns this week's {@link #getSpecialShabbos() special shabbos} regardless of if it is the weekday or <em>Shabbos</em> (where next
	 * Shabbos's <em>Parsha</em> will be returned. If the upcoming <em>Shabbos</em> is a <em>Yom Tov</em> and has no <em>Parsha</em>
	 * or special shabbos, {@link Parsha#NONE} will be returned.
	 *
	 * @return the upcoming <em>parsha</em>.
	 */
	public Parsha getThisWeeksSpecialParshah() {
		JewishCalendar clone = (JewishCalendar) clone();
		int daysToShabbos = (Calendar.SATURDAY - getDayOfWeek() + 7) % 7;
		if (getDayOfWeek() != Calendar.SATURDAY) {
			clone.forward(Calendar.DATE, daysToShabbos);
		}
		return clone.getSpecialShabbos();
	}
	
	/**
	 * Returns the upcoming {@link Parsha <em>Parsha</em>} regardless of if it is the weekday or <em>Shabbos</em> (where next
	 * Shabbos's <em>Parsha</em> will be returned. This is unlike {@link #getParshah()} that returns {@link Parsha#NONE} if
	 * the date is not <em>Shabbos</em>. If the upcoming <em>Shabbos</em> is a <em>Yom Tov</em> and has no <em>Parsha</em>, the
	 * following week's <em>Parsha</em> will be returned.
	 * 
	 * @return the upcoming <em>parsha</em>.
	 */
	public Parsha getUpcomingParshah() {
		JewishCalendar clone = (JewishCalendar) clone();
		int daysToShabbos = (Calendar.SATURDAY - getDayOfWeek() + 7) % 7;
		if (getDayOfWeek() != Calendar.SATURDAY) {
			clone.forward(Calendar.DATE, daysToShabbos);
		} else {
			clone.forward(Calendar.DATE, 7);
		}
		while(clone.getParshah() == Parsha.NONE) { //Yom Kippur / Sukkos or Pesach with 2 potential non-parsha Shabbosim in a row
			clone.forward(Calendar.DATE, 7);
		}
		return clone.getParshah();
	}
	
	/**
	 * Returns a {@link Parsha <em>Parsha</em>} enum if the <em>Shabbos</em> is one of the four <em>parshiyos</em> of {@link
	 * Parsha#SHKALIM <em>Shkalim</em>}, {@link Parsha#ZACHOR <em>Zachor</em>}, {@link Parsha#PARA <em>Para</em>}, {@link
	 * Parsha#HACHODESH <em>Hachdesh</em>}, or five other special <em>Shabbasos</em> of {@link Parsha#HAGADOL <em>Hagadol</em>},
	 * {@link Parsha#CHAZON <em>Chazon</em>}, {@link Parsha#NACHAMU <em>Nachamu</em>}, {@link Parsha#SHUVA <em>Shuva</em>},
	 * {@link Parsha#SHIRA <em>Shira</em>}, or {@link Parsha#NONE Parsha.NONE} for a regular <em>Shabbos</em> (or any weekday).
	 * 
	 * @return one of the four <em>parshiyos</em> of {@link	Parsha#SHKALIM <em>Shkalim</em>}, {@link Parsha#ZACHOR <em>Zachor</em>},
	 * 		{@link Parsha#PARA <em>Para</em>}, {@link Parsha#HACHODESH <em>Hachodesh</em>}, or five other special <em>Shabbasos</em>
	 * 		of {@link Parsha#HAGADOL <em>Hagadol</em>}, {@link Parsha#CHAZON <em>Chazon</em>}, {@link Parsha#NACHAMU <em>Nachamu</em>},
	 * 		{@link Parsha#SHUVA <em>Shuva</em>}, {@link Parsha#SHIRA <em>Shira</em>}, or {@link Parsha#NONE Parsha.NONE} for a regular
	 * 		<em>Shabbos</em> (or any weekday).
	 */
	public Parsha getSpecialShabbos() {
		if (getDayOfWeek() == Calendar.SATURDAY) {
			if ((getJewishMonth() == SHEVAT && !isJewishLeapYear()) || (getJewishMonth() == ADAR && isJewishLeapYear())) {
				if (getJewishDayOfMonth() == 25 || getJewishDayOfMonth() == 27 || getJewishDayOfMonth() == 29) {
					return Parsha.SHKALIM;
				}
			}
			if ((getJewishMonth() == ADAR && !isJewishLeapYear()) || getJewishMonth() == ADAR_II) {
				if (getJewishDayOfMonth() == 1) {
					return Parsha.SHKALIM;
				}
				if (getJewishDayOfMonth() == 8 || getJewishDayOfMonth() == 9 || getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 13) {
					return Parsha.ZACHOR;
				}
				if (getJewishDayOfMonth() == 18 || getJewishDayOfMonth() == 20 || getJewishDayOfMonth() == 22 || getJewishDayOfMonth() == 23) {
					return Parsha.PARA;
				}
				if (getJewishDayOfMonth() == 25 || getJewishDayOfMonth() == 27 || getJewishDayOfMonth() == 29) {
					return Parsha.HACHODESH;
				}
			}
			if (getJewishMonth() == NISSAN) {
				if (getJewishDayOfMonth() == 1) {
					return Parsha.HACHODESH;
				}
				if (getJewishDayOfMonth() >= 8 && getJewishDayOfMonth() <= 14) {
					return Parsha.HAGADOL;
				}
			}
			if (getJewishMonth() == AV) {
				if (getJewishDayOfMonth() >= 4 && getJewishDayOfMonth() <= 9) {
					return Parsha.CHAZON;
				}
				if (getJewishDayOfMonth() >= 10 && getJewishDayOfMonth() <= 16) {
					return Parsha.NACHAMU;
				}
			}
			if (getJewishMonth() == TISHREI) {
				if (getJewishDayOfMonth() >= 3 && getJewishDayOfMonth() <= 8) {
					return Parsha.SHUVA;
				}
				
			}
			if (getParshah() == Parsha.BESHALACH) {
				return Parsha.SHIRA;
			}
		}
		return Parsha.NONE;
	}

	/**
	 * Returns an index of the Jewish holiday or fast day for the current day, or a -1 if there is no holiday for this day.
	 * There are constants in this class representing each <em>Yom Tov</em>. Formatting of the <em>Yomim tovim</em> is done
	 * in the {@link HebrewDateFormatter#formatYomTov(JewishCalendar)}.
	 * 
	 * @todo Consider using enums instead of the constant ints.
	 * 
	 * @return the index of the holiday such as the constant {@link #LAG_BAOMER} or {@link #YOM_KIPPUR} or a -1 if it is not a holiday.
	 * 
	 * @see HebrewDateFormatter#formatYomTov(JewishCalendar)
	 */
	public int getYomTovIndex() {
		final int day = getJewishDayOfMonth();
		final int dayOfWeek = getDayOfWeek();

		// check by month (starting from Nissan)
		switch (getJewishMonth()) {
		case NISSAN:
			if (day == 14) {
				return EREV_PESACH;
			}
			if (day == 15 || day == 21
					|| (!inIsrael && (day == 16 || day == 22))) {
				return PESACH;
			}
			if (day >= 17 && day <= 20 || day == 16) {
				return CHOL_HAMOED_PESACH;
			}
			if (day == 22 || day == 23 && !inIsrael) {
				return ISRU_CHAG;
			}
			if (isUseModernHolidays()
					&& ((day == 26 && dayOfWeek == Calendar.THURSDAY)
							|| (day == 28 && dayOfWeek == Calendar.MONDAY)
							|| (day == 27 && dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.FRIDAY))) {
				return YOM_HASHOAH;
			}
			break;
		case IYAR:
			if (isUseModernHolidays()
					&& ((day == 4 && dayOfWeek == Calendar.TUESDAY)
							|| ((day == 3 || day == 2) && dayOfWeek == Calendar.WEDNESDAY) || (day == 5 && dayOfWeek == Calendar.MONDAY))) {
				return YOM_HAZIKARON;
			}
			// if 5 Iyar falls on Wed, Yom Haatzmaut is that day. If it falls on Friday or Shabbos, it is moved back to
			// Thursday. If it falls on Monday it is moved to Tuesday
			if (isUseModernHolidays()
					&& ((day == 5 && dayOfWeek == Calendar.WEDNESDAY)
							|| ((day == 4 || day == 3) && dayOfWeek == Calendar.THURSDAY) || (day == 6 && dayOfWeek == Calendar.TUESDAY))) {
				return YOM_HAATZMAUT;
			}
			if (day == 14) {
				return PESACH_SHENI;
			}
			if (day == 18) {
				return LAG_BAOMER;
			}
			if (isUseModernHolidays() && day == 28) {
				return YOM_YERUSHALAYIM;
			}
			break;
		case SIVAN:
			if (day == 5) {
				return EREV_SHAVUOS;
			}
			if (day == 6 || (day == 7 && !inIsrael)) {
				return SHAVUOS;
			}
			if (day == 7 || day == 8 && !inIsrael) {
				return ISRU_CHAG;
			}
			break;
		case TAMMUZ:
			// push off the fast day if it falls on Shabbos
			if ((day == 17 && dayOfWeek != Calendar.SATURDAY)
					|| (day == 18 && dayOfWeek == Calendar.SUNDAY)) {
				return SEVENTEEN_OF_TAMMUZ;
			}
			break;
		case AV:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((dayOfWeek == Calendar.SUNDAY && day == 10)
					|| (dayOfWeek != Calendar.SATURDAY && day == 9)) {
				return TISHA_BEAV;
			}
			if (day == 15) {
				return TU_BEAV;
			}
			break;
		case ELUL:
			if (day == 29) {
				return EREV_ROSH_HASHANA;
			}
			break;
		case TISHREI:
			if (day == 1 || day == 2) {
				return ROSH_HASHANA;
			}
			if ((day == 3 && dayOfWeek != Calendar.SATURDAY) || (day == 4 && dayOfWeek == Calendar.SUNDAY)) {
				// push off Tzom Gedalia if it falls on Shabbos
				return FAST_OF_GEDALYAH;
			}
			if (day == 9) {
				return EREV_YOM_KIPPUR;
			}
			if (day == 10) {
				return YOM_KIPPUR;
			}
			if (day == 14) {
				return EREV_SUCCOS;
			}
			if (day == 15 || (day == 16 && !inIsrael)) {
				return SUCCOS;
			}
			if (day >= 16 && day <= 20) {
				return CHOL_HAMOED_SUCCOS;
			}
			if (day == 21) {
				return HOSHANA_RABBA;
			}
			if (day == 22) {
				return SHEMINI_ATZERES;
			}
			if (day == 23 && !inIsrael) {
				return SIMCHAS_TORAH;
			}
			if (day == 23 || day == 24 && !inIsrael) {
				return ISRU_CHAG;
			}
			break;
		case KISLEV: // no yomtov in CHESHVAN
			// if (day == 24) {
			// return EREV_CHANUKAH;
			// } else
			if (day >= 25) {
				return CHANUKAH;
			}
			break;
		case TEVES:
			if (day == 1 || day == 2
					|| (day == 3 && isKislevShort())) {
				return CHANUKAH;
			}
			if (day == 10) {
				return TENTH_OF_TEVES;
			}
			break;
		case SHEVAT:
			if (day == 15) {
				return TU_BESHVAT;
			}
			break;
		case ADAR:
			if (!isJewishLeapYear()) {
				// if 13th Adar falls on Friday or Shabbos, push back to Thursday
				if (((day == 11 || day == 12) && dayOfWeek == Calendar.THURSDAY)
						|| (day == 13 && !(dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY))) {
					return FAST_OF_ESTHER;
				}
				if (day == 14) {
					return PURIM;
				}
				if (day == 15) {
					return SHUSHAN_PURIM;
				}
			} else { // else if a leap year
				if (day == 14) {
					return PURIM_KATAN;
				}
				if (day == 15) {
					return SHUSHAN_PURIM_KATAN;
				}
			}
			break;
		case ADAR_II:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((day == 11 || day == 12) && dayOfWeek == Calendar.THURSDAY)
					|| (day == 13 && !(dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY))) {
				return FAST_OF_ESTHER;
			}
			if (day == 14) {
				return PURIM;
			}
			if (day == 15) {
				return SHUSHAN_PURIM;
			}
			break;
		}
		// if we get to this stage, then there are no holidays for the given date return -1
		return -1;
	}

	/**
	 * Returns true if the current day is <em>Yom Tov</em>. The method returns true even for holidays such as {@link #CHANUKAH}
	 * and minor ones such as {@link #TU_BEAV} and {@link #PESACH_SHENI}. <em>Erev Yom Tov</em> (with the exception of
	 * {@link #HOSHANA_RABBA} and <em>erev</em> the second days of {@link #PESACH}) returns false, as do {@link #isTaanis() fast
	 * days} besides {@link #YOM_KIPPUR}. Use {@link #isAssurBemelacha()} to find the days that have a prohibition of work. 
	 * 
	 * @return true if the current day is a Yom Tov
	 * 
	 * @see #getYomTovIndex()
	 * @see #isErevYomTov()
	 * @see #isErevYomTovSheni()
	 * @see #isTaanis()
	 * @see #isAssurBemelacha()
	 * @see #isCholHamoed()
	 */
	public boolean isYomTov() {
		int holidayIndex = getYomTovIndex();
		if ((isErevYomTov() && ! ( holidayIndex == HOSHANA_RABBA || holidayIndex == CHOL_HAMOED_PESACH))
				|| (isTaanis() && holidayIndex != YOM_KIPPUR) || holidayIndex == ISRU_CHAG) {
			return false;
		}
		return getYomTovIndex() != -1;
	}

	/**
	 * Returns true if the <em>Yom Tov</em> day has a <em>melacha</em> (work)  prohibition. This method will return false for a
	 * non-<em>Yom Tov</em> day, even if it is <em>Shabbos</em>.
	 *
	 * @return if the <em>Yom Tov</em> day has a <em>melacha</em> (work)  prohibition.
	 */
	public boolean isYomTovAssurBemelacha() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == PESACH || holidayIndex == SHAVUOS || holidayIndex == SUCCOS || holidayIndex == SHEMINI_ATZERES ||
				holidayIndex == SIMCHAS_TORAH || holidayIndex == ROSH_HASHANA  || holidayIndex == YOM_KIPPUR;
	}
	
	/**
	 * Returns true if it is <em>Shabbos</em> or if it is a <em>Yom Tov</em> day that has a <em>melacha</em> (work)  prohibition.
	 * 
	 * @return if the day is a <em>Yom Tov</em> that is <em>assur bemlacha</em> or <em>Shabbos</em>
	 */
	public boolean isAssurBemelacha() {
		return getDayOfWeek() == Calendar.SATURDAY || isYomTovAssurBemelacha();
	}
	
	/**
	 * Returns true if the day has candle lighting. This will return true on <em>Erev Shabbos</em>, <em>Erev Yom Tov</em>, the
	 * first day of <em>Rosh Hashana</em> and the first days of <em>Yom Tov</em> out of Israel. It is identical
	 * to calling {@link #isTomorrowShabbosOrYomTov()}.
	 * 
	 * @return if the day has candle lighting.
	 * 
	 * @see #isTomorrowShabbosOrYomTov()
	 */
	public boolean hasCandleLighting() {
		return isTomorrowShabbosOrYomTov();
	}
	
	/**
	 * Returns true if tomorrow is <em>Shabbos</em> or <em>Yom Tov</em>. This will return true on <em>Erev Shabbos</em>,
	 * <em>Erev Yom Tov</em>, the first day of <em>Rosh Hashana</em> and <em>erev</em> the first days of <em>Yom Tov</em>
	 * out of Israel. It is identical to calling {@link #hasCandleLighting()}.
	 * 
	 * @return will return if the next day is <em>Shabbos</em> or <em>Yom Tov</em>.
	 * 
	 * @see #hasCandleLighting()
	 */
	public boolean isTomorrowShabbosOrYomTov() {
		return getDayOfWeek() == Calendar.FRIDAY || isErevYomTov() || isErevYomTovSheni();
	}
	
	/**
	 * Returns true if the day is the second day of <em>Yom Tov</em>. This impacts the second day of <em>Rosh Hashana</em> everywhere and
	 * the second days of Yom Tov in <em>chutz laaretz</em> (out of Israel).
	 * 
	 * @return  if the day is the second day of <em>Yom Tov</em>.
	 */
	public boolean isErevYomTovSheni() {
		return (getJewishMonth() == TISHREI && (getJewishDayOfMonth() == 1))
		|| (! getInIsrael()
				&& ((getJewishMonth() == NISSAN && (getJewishDayOfMonth() == 15 || getJewishDayOfMonth() == 21))
				|| (getJewishMonth() == TISHREI && (getJewishDayOfMonth() == 15 || getJewishDayOfMonth() == 22))
				|| (getJewishMonth() == SIVAN && getJewishDayOfMonth() == 6 )));
	}

	/**
	 * Returns true if the current day is <em>Aseres Yemei Teshuva</em>.
	 * 
	 * @return if the current day is <em>Aseres Yemei Teshuva</em>
	 */
	public boolean isAseresYemeiTeshuva() {
		return getJewishMonth() == TISHREI && getJewishDayOfMonth() <= 10;
	}
	
	/**
	 * Returns true if the current day is <em>Pesach</em> (either  the <em>Yom Tov</em> of <em>Pesach</em> or<em>Chol Hamoed Pesach</em>).
	 * 
	 * @return true if the current day is <em>Pesach</em> (either  the <em>Yom Tov</em> of <em>Pesach</em> or<em>Chol Hamoed Pesach</em>).
	 * @see #isYomTov()
	 * @see #isCholHamoedPesach()
	 * @see #PESACH
	 * @see #CHOL_HAMOED_PESACH
	 */
	public boolean isPesach() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == PESACH || holidayIndex == CHOL_HAMOED_PESACH;
	}
	
	/**
	 * Returns true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em>.
	 *
	 * @return true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em>
	 * @see #isYomTov()
	 * @see #isPesach()
	 * @see #CHOL_HAMOED_PESACH
	 */
	public boolean isCholHamoedPesach() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == CHOL_HAMOED_PESACH;
	}
	
	/**
	 * Returns true if the current day is <em>Shavuos</em>.
	 *
	 * @return true if the current day is <em>Shavuos</em>.
	 * @see #isYomTov()
	 * @see #SHAVUOS
	 */
	public boolean isShavuos() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == SHAVUOS;
	}
	
	/**
	 * Returns true if the current day is <em>Rosh Hashana</em>.
	 *
	 * @return true if the current day is <em>Rosh Hashana</em>.
	 * @see #isYomTov()
	 * @see #ROSH_HASHANA
	 */
	public boolean isRoshHashana() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == ROSH_HASHANA;
	}
	
	/**
	 * Returns true if the current day is <em>Yom Kippur</em>.
	 *
	 * @return true if the current day is <em>Yom Kippur</em>.
	 * @see #isYomTov()
	 * @see #YOM_KIPPUR
	 */
	public boolean isYomKippur() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == YOM_KIPPUR;
	}
	
	/**
	 * Returns true if the current day is <em>Succos</em> (either  the <em>Yom Tov</em> of <em>Succos</em> or<em>Chol Hamoed Succos</em>).
	 * It will return false for {@link #isShminiAtzeres() Shmini Atzeres} and {@link #isSimchasTorah() Simchas Torah}.
	 * 
	 * @return true if the current day is <em>Succos</em> (either  the <em>Yom Tov</em> of <em>Succos</em> or<em>Chol Hamoed Succos</em>.
	 * @see #isYomTov()
	 * @see #isCholHamoedSuccos()
	 * @see #isHoshanaRabba()
	 * @see #SUCCOS
	 * @see #CHOL_HAMOED_SUCCOS
	 * @see #HOSHANA_RABBA
	 */
	public boolean isSuccos() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == SUCCOS || holidayIndex == CHOL_HAMOED_SUCCOS || holidayIndex == HOSHANA_RABBA;
	}
	
	/**
	 * Returns true if the current day is <em>Hoshana Rabba</em>.
	 *
	 * @return true if the current day is <em>Hoshana Rabba</em>.
	 * @see #isYomTov()
	 * @see #HOSHANA_RABBA
	 */
	public boolean isHoshanaRabba() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == HOSHANA_RABBA;
	}
	
	/**
	 * Returns true if the current day is <em>Shmini Atzeres</em>.
	 *
	 * @return true if the current day is <em>Shmini Atzeres</em>.
	 * @see #isYomTov()
	 * @see #SHEMINI_ATZERES
	 */
	public boolean isShminiAtzeres() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == SHEMINI_ATZERES;
	}
	
	/**
	 * Returns true if the current day is <em>Simchas Torah</em>. This will always return false if {@link #getInIsrael() in Israel}
	 *
	 * @return true if the current day is <em>Shmini Atzeres</em>.
	 * @see #isYomTov()
	 * @see #SIMCHAS_TORAH
	 */
	public boolean isSimchasTorah() {
		int holidayIndex = getYomTovIndex();
		//if in Israel, Holiday index of SIMCHAS_TORAH will not be returned by getYomTovIndex()
		return holidayIndex == SIMCHAS_TORAH;
	}
	
	/**
	 * Returns true if the current day is <em>Chol Hamoed</em> of <em>Succos</em>.
	 *
	 * @return true if the current day is <em>Chol Hamoed</em> of <em>Succos</em>
	 * @see #isYomTov()
	 * @see #CHOL_HAMOED_SUCCOS
	 */
	public boolean isCholHamoedSuccos() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == CHOL_HAMOED_SUCCOS || holidayIndex == HOSHANA_RABBA;
	}
	
	/**
	 * Returns true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em> or <em>Succos</em>.
	 * 
	 * @return true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em> or <em>Succos</em>
	 * @see #isYomTov()
	 * @see #CHOL_HAMOED_PESACH
	 * @see #CHOL_HAMOED_SUCCOS
	 */
	public boolean isCholHamoed() {
		return isCholHamoedPesach() || isCholHamoedSuccos();
	}

	/**
	 * Returns true if the current day is <em>Erev Yom Tov</em>. The method returns true for <em>Erev</em> - <em>Pesach</em>
	 * (first and last days), <em>Shavuos</em>, <em>Rosh Hashana</em>, <em>Yom Kippur</em>, <em>Succos</em> and <em>Hoshana
	 * Rabba</em>.
	 * 
	 * @return true if the current day is <em>Erev</em> - <em>Pesach</em>, <em>Shavuos</em>, <em>Rosh Hashana</em>, <em>Yom
	 * Kippur</em>, <em>Succos</em> and <em>Hoshana Rabba</em>.
	 * @see #isYomTov()
	 * @see #isErevYomTovSheni()
	 */
	public boolean isErevYomTov() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == EREV_PESACH || holidayIndex == EREV_SHAVUOS || holidayIndex == EREV_ROSH_HASHANA
				|| holidayIndex == EREV_YOM_KIPPUR || holidayIndex == EREV_SUCCOS || holidayIndex == HOSHANA_RABBA
				|| (holidayIndex == CHOL_HAMOED_PESACH && getJewishDayOfMonth() == 20);
	}

	/**
	 * Returns true if the current day is <em>Erev Rosh Chodesh</em>. Returns false for <em>Erev Rosh Hashana</em>.
	 * 
	 * @return true if the current day is <em>Erev Rosh Chodesh</em>. Returns false for <em>Erev Rosh Hashana</em>.
	 * @see #isRoshChodesh()
	 */
	public boolean isErevRoshChodesh() {
		// Erev Rosh Hashana is not Erev Rosh Chodesh.
		return (getJewishDayOfMonth() == 29 && getJewishMonth() != ELUL);
	}
	
	
	/**
	 * Returns true if the current day is <em>Yom Kippur Katan</em>. Returns false for <em>Erev Rosh Hashana</em>,
	 * <em>Erev Rosh Chodesh Cheshvan</em>, <em>Teves</em> and <em>Iyyar</em>. If <em>Erev Rosh Chodesh</em> occurs
	 * on a Friday or <em>Shabbos</em>, <em>Yom Kippur Katan</em> is moved back to Thursday.
	 * 
	 * @return true if the current day is <em>Erev Rosh Chodesh</em>. Returns false for <em>Erev Rosh Hashana</em>.
	 * @see #isRoshChodesh()
	 */
	public boolean isYomKippurKatan() {
		int dayOfWeek = getDayOfWeek();
		int month = getJewishMonth();
		int day = getJewishDayOfMonth();
		if (month == JewishDate.ELUL || month == JewishDate.TISHREI || month == JewishDate.KISLEV || month == JewishDate.NISSAN) {
			return false;
		}

		if (day == 29 && dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY) {
			return true;
		}
		return (day == 27 || day == 28) && dayOfWeek == Calendar.THURSDAY;
	}
	
	/**
	 * The Monday, Thursday and Monday after the first <em>Shabbos</em> after {@link #isRoshChodesh() <em>Rosh Chodesh</em>}
	 * {@link JewishDate#CHESHVAN <em>Cheshvan</em>} and {@link JewishDate#IYAR <em>Iyar</em>} are <a href=
	 * "https://outorah.org/p/41334/"> <em>BeHaB</em></a> days. If the last Monday of Iyar's BeHaB coincides with {@link
	 * #PESACH_SHENI <em>Pesach Sheni</em>}, the method currently considers it both <em>Pesach Sheni</em> and <em>BeHaB</em>.
	 * As seen in an Ohr Sameach  article on the subject <a href="https://ohr.edu/this_week/insights_into_halacha/9340">The
	 * unknown Days: BeHaB Vs. Pesach Sheini?</a> there are some customs that delay the day to various points in the future.
	 * @return true if the day is <em>BeHaB</em>.
	 */
	public boolean isBeHaB() {
		int dayOfWeek = getDayOfWeek();
		int month = getJewishMonth();
		int day = getJewishDayOfMonth();
		
		if (month == JewishDate.CHESHVAN || month == JewishDate.IYAR) {
			return (dayOfWeek == Calendar.MONDAY && day > 4 && day < 18)
					|| (dayOfWeek == Calendar.THURSDAY && day > 7 && day < 14);
		}
		return false;
	}

	/**
	 * Return true if the day is a Taanis (fast day). Return true for <em>17 of Tammuz</em>, <em>Tisha B'Av</em>,
	 * <em>Yom Kippur</em>, <em>Fast of Gedalyah</em>, <em>10 of Teves</em> and the <em>Fast of Esther</em>.
	 * 
	 * @return true if today is a fast day
	 */
	public boolean isTaanis() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == SEVENTEEN_OF_TAMMUZ || holidayIndex == TISHA_BEAV || holidayIndex == YOM_KIPPUR
				|| holidayIndex == FAST_OF_GEDALYAH || holidayIndex == TENTH_OF_TEVES || holidayIndex == FAST_OF_ESTHER;
	}
	
	/**
	 * Return true if the day is <em>Taanis Bechoros</em> (on <em>Erev Pesach</em>). It will return true for the 14th
	 * of <em>Nissan</em> if it is not on <em>Shabbos</em>, or if the 12th of <em>Nissan</em> occurs on a Thursday.
	 * 
	 * @return true if today is <em>Taanis Bechoros</em>.
	 */
	public boolean isTaanisBechoros() {
	    final int day = getJewishDayOfMonth();
	    final int dayOfWeek = getDayOfWeek();
	    // on 14 Nissan unless that is Shabbos where the fast is moved back to Thursday
	    return getJewishMonth() == NISSAN && ((day == 14 && dayOfWeek != Calendar.SATURDAY) ||
	    		(day == 12 && dayOfWeek == Calendar.THURSDAY ));
	}

	/**
	 * Returns the day of <em>Chanukah</em> or -1 if it is not <em>Chanukah</em>.
	 * 
	 * @return the day of <em>Chanukah</em> or -1 if it is not <em>Chanukah</em>.
	 * @see #isChanukah()
	 */
	public int getDayOfChanukah() {
		final int day = getJewishDayOfMonth();
		if (isChanukah()) {
			if (getJewishMonth() == KISLEV) {
				return day - 24;
			} else { // teves
				return isKislevShort() ? day + 5 : day + 6;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Returns true if the current day is one of the 8 days of <em>Chanukah</em>.
	 * 
	 * @return if the current day is one of the 8 days of <em>Chanukah</em>.
	 * 
	 * @see #getDayOfChanukah()
	 */
	public boolean isChanukah() {
		return getYomTovIndex() == CHANUKAH;
	}
	
	/**
	 * Returns if the day is Purim (<a href="https://en.wikipedia.org/wiki/Purim#Shushan_Purim">Shushan Purim</a>
	 * in a mukaf choma and regular Purim in a non-mukaf choma). 
	 * @return if the day is Purim (Shushan Purim in a mukaf choma and regular Purim in a non-mukaf choma)
	 * 
	 * @see #getIsMukafChoma()
	 * @see #setIsMukafChoma(boolean)
	 */
	public boolean isPurim() {
		if (isMukafChoma) {
			return getYomTovIndex() == SHUSHAN_PURIM;
		} else {
			return getYomTovIndex() == PURIM;
		}
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
	 * Returns if the day is <em>Shabbos</em> and Sunday is <em>Rosh Chodesh</em>.
	 *
	 * @return true if it is <em>Shabbos</em> and Sunday is <em>Rosh Chodesh</em>.
	 * @todo There is more to tweak in this method (it does not cover all cases and opinions), and it may be removed.
	 */
	public boolean isMacharChodesh() {
		return (getDayOfWeek() == Calendar.SATURDAY && (getJewishDayOfMonth() == 30 || getJewishDayOfMonth() == 29));
	}

	/**
	 * Returns if the day is <em>Shabbos Mevorchim</em>.
	 *
	 * @return true if it is <em>Shabbos Mevorchim</em>.
	 */
	public boolean isShabbosMevorchim() {
		return (getDayOfWeek() == Calendar.SATURDAY && getJewishDayOfMonth() >= 23 && getJewishDayOfMonth() <= 29 && getJewishMonth() != ELUL);
	}

	/**
	 * Returns the int value of the <em>Omer</em> day or -1 if the day is not in the <em>Omer</em>.
	 * 
	 * @return The <em>Omer</em> count as an int or -1 if it is not a day of the <em>Omer</em>.
	 */
	public int getDayOfOmer() {
		int omer = -1; // not a day of the Omer
		int month = getJewishMonth();
		int day = getJewishDayOfMonth();

		// if Nissan and second day of Pesach and on
		if (month == NISSAN && day >= 16) {
			omer = day - 15;
			// if Iyar
		} else if (month == IYAR) {
			omer = day + 15;
			// if Sivan and before Shavuos
		} else if (month == SIVAN && day < 6) {
			omer = day + 44;
		}
		return omer;
	}
	
	/**
	 * Returns if the day is Tisha Be'Av (the 9th of Av).
	 * @return if the day is Tisha Be'Av (the 9th of Av).
	 */
	public boolean isTishaBav() {
	    int holidayIndex = getYomTovIndex();
	    return holidayIndex == TISHA_BEAV;
	}

	/**
	 * Returns the <em>molad</em> in Standard Time in Yerushalayim as a Date. The traditional calculation uses local time.
	 * This method subtracts 20.94 minutes (20 minutes and 56.496 seconds) from the local time (of <em>Har Habayis</em>
	 * with a longitude of 35.2354&deg; is 5.2354&deg; away from the %15 timezone longitude) to get to standard time. This
	 * method intentionally uses standard time and not daylight savings time. Java will implicitly format the time to the
	 * default (or set) Timezone.
	 * 
	 * @return the Date representing the moment of the <em>molad</em> in Yerushalayim standard time (GMT + 2)
	 */
	public Date getMoladAsDate() {
		JewishDate molad = getMolad();
		String locationName = "Jerusalem, Israel";

		double latitude = 31.778; // Har Habayis latitude
		double longitude = 35.2354; // Har Habayis longitude

		// The raw molad Date (point in time) must be generated using standard time. Using "Asia/Jerusalem" timezone will result in the time
		// being incorrectly off by an hour in the summer due to DST. Proper adjustment for the actual time in DST will be done by the date
		// formatter class used to display the Date.
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
	 * Returns the earliest time of <em>Kiddush Levana</em> calculated as 3 days after the molad. This method returns the time
	 * even if it is during the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider
	 * displaying the next <em>tzais</em> if the <em>zman</em> is between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment 3 days after the molad.
	 * 
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getTchilasZmanKidushLevana3Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getTchilasZmanKidushLevana3Days(Date, Date)
	 */
	public Date getTchilasZmanKidushLevana3Days() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		cal.add(Calendar.HOUR, 72); // 3 days after the molad
		return cal.getTime();
	}

	/**
	 * Returns the earliest time of <em>Kiddush Levana</em> calculated as 7 days after the <em>molad</em> as mentioned
	 * by the <a href="http://en.wikipedia.org/wiki/Yosef_Karo">Mechaber</a>. See the <a
	 * href="http://en.wikipedia.org/wiki/Yoel_Sirkis">Bach's</a> opinion on this time. This method returns the time
	 * even if it is during the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider
	 * displaying the next <em>tzais</em> if the <em>zman</em> is between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment 7 days after the molad.
	 * 
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getTchilasZmanKidushLevana7Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getTchilasZmanKidushLevana7Days(Date, Date)
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
	 * halfway between <em>molad</em> and <em>molad</em>. This adds half the 29 days, 12 hours and 793 <em>chalakim</em>
	 * time between <em>molad</em> and <em>molad</em> (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's
	 * <em>molad</em>. This method returns the time even if it is during the day when <em>Kiddush Levana</em> can't be
	 * recited. Callers of this method should consider displaying <em>alos</em> before this time if the <em>zman</em> is
	 * between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment halfway between <em>molad</em> and <em>molad</em>.
	 * 
	 * @see #getSofZmanKidushLevana15Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevanaBetweenMoldos()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevanaBetweenMoldos(Date, Date)
	 */
	public Date getSofZmanKidushLevanaBetweenMoldos() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		// add half the time between molad and molad (half of 29 days, 12 hours and 793 chalakim (44 minutes, 3.3
		// seconds), or 14 days, 18 hours, 22 minutes and 666 milliseconds). Add it as hours, not days, to avoid
		// DST/ST crossover issues.
		cal.add(Calendar.HOUR, (24 * 14) + 18);
		cal.add(Calendar.MINUTE, 22);
		cal.add(Calendar.SECOND, 1);
		cal.add(Calendar.MILLISECOND, 666);
		return cal.getTime();
	}

	/**
	 * Returns the latest time of <em>Kiddush Levana</em> calculated as 15 days after the <em>molad.</em> This is the
	 * opinion brought down in the Shulchan Aruch (Orach Chaim 426). It should be noted that some opinions hold that
	 * the <a href="http://en.wikipedia.org/wiki/Moses_Isserles">Rema</a> who brings down the <a
	 * href="http://en.wikipedia.org/wiki/Yaakov_ben_Moshe_Levi_Moelin">Maharil's</a> opinion of calculating it as
	 * {@link #getSofZmanKidushLevanaBetweenMoldos() half way between <em>molad</em> and <em>molad</em>} is of the
	 * opinion of the Mechaber as well. Also see the Aruch Hashulchan. For additional details on the subject, See Rabbi
	 * Dovid Heber's very detailed writeup in Siman Daled (chapter 4) of <a
	 * href="http://www.worldcat.org/oclc/461326125">Shaarei Zmanim</a>. This method returns the time even if it is during
	 * the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider displaying <em>alos</em>
	 * before this time if the <em>zman</em> is between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment 15 days after the <em>molad</em>.
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevana15Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevana15Days(Date, Date)
	 */
	public Date getSofZmanKidushLevana15Days() {
		Date molad = getMoladAsDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(molad);
		cal.add(Calendar.HOUR, 24 * 15); //15 days after the molad. Add it as hours, not days, to avoid DST/ST crossover issues.
		return cal.getTime();
	}

	/**
	 * Returns the <em>Daf Yomi (Bavli)</em> for the date that the calendar is set to. See the
	 * {@link HebrewDateFormatter#formatDafYomiBavli(Daf)} for the ability to format the <em>daf</em> in
	 * Hebrew or transliterated <em>masechta</em> names.
	 * 
	 * @return the daf as a {@link Daf}
	 */
	public Daf getDafYomiBavli() {
		return YomiCalculator.getDafYomiBavli(this);
	}
	/**
	 * Returns the <em>Daf Yomi (Yerushalmi)</em> for the date that the calendar is set to. See the
	 * {@link HebrewDateFormatter#formatDafYomiYerushalmi(Daf)} for the ability to format the <em>daf</em>
	 * in Hebrew or transliterated <em>masechta</em> names.
	 *
	 * @return the daf as a {@link Daf}
	 */
	public Daf getDafYomiYerushalmi() {
		return YerushalmiYomiCalculator.getDafYomiYerushalmi(this);
	}
	
	/**
	 * Returns the elapsed days since <em>Tekufas Tishrei</em>. This uses <em>Tekufas Shmuel</em> (identical to the <a href=
	 * "https://en.wikipedia.org/wiki/Julian_year_(astronomy)">Julian Year</a> with a solar year length of 365.25 days).
	 * The notation used below is D = days, H = hours and C = chalakim. <em><a href="https://en.wikipedia.org/wiki/Molad"
	 * >Molad</a> BaHaRad</em> was 2D,5H,204C or 5H,204C from the start of <em>Rosh Hashana</em> year 1. For <em>molad
	 * Nissan</em> add 177D, 4H and 438C (6 * 29D, 12H and 793C), or 177D,9H,642C after <em>Rosh Hashana</em> year 1.
	 * <em>Tekufas Nissan</em> was 7D, 9H and 642C before <em>molad Nissan</em> according to the Rambam, or 170D, 0H and
	 * 0C after <em>Rosh Hashana</em> year 1. <em>Tekufas Tishrei</em> was 182D and 3H (365.25 / 2) before <em>tekufas
	 * Nissan</em>, or 12D and 15H before <em>Rosh Hashana</em> of year 1. Outside of Israel we start reciting <em>Tal
	 * Umatar</em> in <em>Birkas Hashanim</em> from 60 days after <em>tekufas Tishrei</em>. The 60 days include the day of
	 * the <em>tekufah</em> and the day we start reciting <em>Tal Umatar</em>. 60 days from the tekufah == 47D and 9H
	 * from <em>Rosh Hashana</em> year 1.
	 * 
	 * @return the number of elapsed days since <em>tekufas Tishrei</em>.
	 * 
	 * @see #isVeseinTalUmatarStartDate()
	 * @see #isVeseinTalUmatarStartingTonight()
	 * @see #isVeseinTalUmatarRecited()
	 */
	public int getTekufasTishreiElapsedDays() {
		// Days since Rosh Hashana year 1. Add 1/2 day as the first tekufas tishrei was 9 hours into the day. This allows all
		// 4 years of the secular leap year cycle to share 47 days. Truncate 47D and 9H to 47D for simplicity.
		double days = getJewishCalendarElapsedDays(getJewishYear()) + (getDaysSinceStartOfJewishYear()-1) + 0.5;
		// days of completed solar years
		double solar = (getJewishYear() - 1) * 365.25;
		return (int) Math.floor(days - solar);
	}

	/**
	 * Returns if it is the Jewish day (starting the evening before) to start reciting <em>Vesein Tal Umatar
	 * Livracha</em> (<em>Sheailas Geshamim</em>). In Israel this is the 7th day of <em>Marcheshvan</em>. Outside
	 * Israel recitation starts on the evening of December 4th (or 5th if it is the year before a civil leap year)
	 * in the 21st century and shifts a day forward every century not evenly divisible by 400. This method will
	 * return true if <em>vesein tal umatar</em> on the current Jewish date that starts on the previous night, so
	 * Dec 5/6 will be returned by this method in the 21st century. <em>vesein tal umatar</em> is not recited on
	 * <em>Shabbos</em> and the start date will be delayed a day when the start day is on a <em>Shabbos</em> (this
	 * can only occur out of Israel).
	 * 
	 * @deprecated Use {@link TefilaRules#isVeseinTalUmatarStartDate(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar
	 * Livracha</em> (<em>Sheailas Geshamim</em>)).
	 * 
	 * @see #isVeseinTalUmatarStartingTonight()
	 * @see #isVeseinTalUmatarRecited()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isVeseinTalUmatarStartDate() {
		if (inIsrael) {
			 // The 7th Cheshvan can't occur on Shabbos, so always return true for 7 Cheshvan
			return getJewishMonth() == CHESHVAN && getJewishDayOfMonth() == 7;
		} else {
			if (getDayOfWeek() == Calendar.SATURDAY) { //Not recited on Friday night
				return false;
			}
			if (getDayOfWeek() == Calendar.SUNDAY) { // When starting on Sunday, it can be the start date or delayed from Shabbos
				return getTekufasTishreiElapsedDays() == 48 || getTekufasTishreiElapsedDays() == 47;
			} else {
				return getTekufasTishreiElapsedDays() == 47;
			}
		}
	}
	
	/**
	 * Returns true if tonight is the first night to start reciting <em>Vesein Tal Umatar Livracha</em> (
	 * <em>Sheailas Geshamim</em>). In Israel this is the 7th day of <em>Marcheshvan</em> (so the 6th will return
	 * true). Outside Israel recitation starts on the evening of December 4th (or 5th if it is the year before a
	 * civil leap year) in the 21st century and shifts a day forward every century not evenly divisible by 400.
	 * <em>Vesein tal umatar</em> is not recited on <em>Shabbos</em> and the start date will be delayed a day when
	 * the start day is on a <em>Shabbos</em> (this can only occur out of Israel).
	 * 
	 * @deprecated Use {@link TefilaRules#isVeseinTalUmatarStartingTonight(JewishCalendar)} instead. This method
	 *         will be removed in the v3.0 release.
	 * 
	 * @return true if it is the first Jewish day (starting the prior evening of reciting <em>Vesein Tal Umatar
	 * Livracha</em> (<em>Sheailas Geshamim</em>)).
	 * 
	 * @see #isVeseinTalUmatarStartDate()
	 * @see #isVeseinTalUmatarRecited()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isVeseinTalUmatarStartingTonight() {
		if (inIsrael) {
			// The 7th Cheshvan can't occur on Shabbos, so always return true for 6 Cheshvan
			return getJewishMonth() == CHESHVAN && getJewishDayOfMonth() == 6;
		} else {
			if (getDayOfWeek() == Calendar.FRIDAY) { //Not recited on Friday night
				return false;
			}
			if (getDayOfWeek() == Calendar.SATURDAY) { // When starting on motzai Shabbos, it can be the start date or delayed from Friday night
				return getTekufasTishreiElapsedDays() == 47 || getTekufasTishreiElapsedDays() == 46;
			} else {
				return getTekufasTishreiElapsedDays() == 46;
			}
		}
	}

	/**
	 * Returns if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited. This will return
	 * true for the entire season, even on <em>Shabbos</em> when it is not recited.
	 * 
	 * @deprecated Use {@link TefilaRules#isVeseinTalUmatarRecited(JewishCalendar)} instead. This method will
	 *         be removed in the v3.0 release.
	 * 
	 * @return true if <em>Vesein Tal Umatar Livracha</em> (<em>Sheailas Geshamim</em>) is recited.
	 * 
	 * @see #isVeseinTalUmatarStartDate()
	 * @see #isVeseinTalUmatarStartingTonight()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isVeseinTalUmatarRecited() {
		if (getJewishMonth() == NISSAN && getJewishDayOfMonth() < 15) {
			return true;
		}
		if (getJewishMonth() < CHESHVAN) {
			return false;
		}
		if (inIsrael) {
			return getJewishMonth() != CHESHVAN || getJewishDayOfMonth() >= 7;
		} else {
			return getTekufasTishreiElapsedDays() >= 47;
		}
	}
	
	/**
	 * Returns if <em>Vesein Beracha</em> is recited. It is recited from 15 <em>Nissan</em> to the point that {@link
	 * #isVeseinTalUmatarRecited() <em>vesein tal umatar</em> is recited}.
	 * 
	 * @deprecated Use {@link TefilaRules#isVeseinBerachaRecited(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if <em>Vesein Beracha</em> is recited.
	 * 
	 * @see #isVeseinTalUmatarRecited()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isVeseinBerachaRecited() {
		return !isVeseinTalUmatarRecited();
	}

	/**
	 * Returns if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 22 <em>Tishrei</em>.
	 * 
	 * @deprecated Use {@link TefilaRules#isMashivHaruachStartDate(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if the date is the start date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachEndDate()
	 * @see #isMashivHaruachRecited()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isMashivHaruachStartDate() {
		return getJewishMonth() == TISHREI && getJewishDayOfMonth() == 22;
	}

	/**
	 * Returns if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>. The date is 15 <em>Nissan</em>.
	 * 
	 * @deprecated Use {@link TefilaRules#isMashivHaruachEndDate(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if the date is the end date for reciting <em>Mashiv Haruach Umorid Hageshem</em>.
	 * 
	 * @see #isMashivHaruachStartDate()
	 * @see #isMashivHaruachRecited()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isMashivHaruachEndDate() {
		return getJewishMonth() == NISSAN && getJewishDayOfMonth() == 15;
	}

	/**
	 * Returns if <em>Mashiv Haruach Umorid Hageshem</em> is recited. This period starts on 22 <em>Tishrei</em> and ends
	 * on the 15th day of <em>Nissan</em>.
	 * 
	 * @deprecated Use {@link TefilaRules#isMashivHaruachRecited(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if <em>Mashiv Haruach Umorid Hageshem</em> is recited.
	 * 
	 * @see #isMashivHaruachStartDate()
	 * @see #isMashivHaruachEndDate()
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isMashivHaruachRecited() {
		JewishDate startDate = new JewishDate(getJewishYear(), TISHREI, 22);
		JewishDate endDate = new JewishDate(getJewishYear(), NISSAN, 15);
		return compareTo(startDate) > 0 && compareTo(endDate) < 0;
	}

	/**
	 * Returns if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 * This period starts on 22 <em>Tishrei</em> and ends on the 15th day of
	 * <em>Nissan</em>.
	 * 
	 * @deprecated Use {@link TefilaRules#isMoridHatalRecited(JewishCalendar)} instead. This method will be
	 *         removed in the v3.0 release.
	 * 
	 * @return true if <em>Morid Hatal</em> (or the lack of reciting <em>Mashiv Haruach</em> following <em>nussach Ashkenaz</em>) is recited.
	 */
	@Deprecated // (forRemoval=true) // add back once Java 9 is the minimum supported version
	public boolean isMoridHatalRecited() {
		return !isMashivHaruachRecited() || isMashivHaruachStartDate() || isMashivHaruachEndDate();
	}
	
	/**
	 * Returns true if the current day is <em>Isru Chag</em>. The method returns true for the day following <em>Pesach</em>
	 * <em>Shavuos</em> and <em>Succos</em>. It utilizes {@see #getInIsrael()} to return the proper date.
	 * 
	 * @return true if the current day is <em>Isru Chag</em>. The method returns true for the day following <em>Pesach</em>
	 * <em>Shavuos</em> and <em>Succos</em>. It utilizes {@see #getInIsrael()} to return the proper date.
	 */
	public boolean isIsruChag() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == ISRU_CHAG;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
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
	 * Overrides {@link Object#hashCode()}.
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getClass().hashCode(); // needed or this and subclasses will return identical hash
		result += 37 * result + getAbsDate() + (getInIsrael() ? 1 : 3);
		return result;
	}
}
