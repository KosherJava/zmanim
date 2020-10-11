/*
 * Zmanim Java API
 * Copyright (C) 2011 - 2020 Eliyahu Hershfeld
 * Copyright (C) September 2002 Avrom Finkelstien
 * Copyright (C) 2019 Y Paritcher
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
 * and some bug fixing. The class allows setting whether the holiday and parsha scheme follows the Israel scheme or outside Israel
 * scheme. The default is the outside Israel scheme.
 * The parsha code was ported by Y Paritcher from his <a href="https://github.com/yparitcher/libzmanim">libzmanim</a> code.
 * 
 * @todo Some do not belong in this class, but here is a partial list of what should still be implemented in some form:
 * <ol>
 * <li>Add Isru Chag</li>
 * <li>Mishna yomis etc</li>
 * </ol>
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Y Paritcher 2019
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011 - 2020
 */
public class JewishCalendar extends com.kosherjava.zmanim.hebrewcalendar.JewishDate {
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
	 * The {@link #VZOS_HABERACHA} enum exists for consistency, but is not currently used.
	 */
	public static enum Parsha {
		NONE, BERESHIS, NOACH, LECH_LECHA, VAYERA, CHAYEI_SARA, TOLDOS, VAYETZEI, VAYISHLACH, VAYESHEV, MIKETZ, VAYIGASH, VAYECHI, SHEMOS, VAERA, BO, BESHALACH, YISRO, MISHPATIM, TERUMAH, TETZAVEH, KI_SISA, VAYAKHEL, PEKUDEI, VAYIKRA, TZAV, SHMINI, TAZRIA, METZORA, ACHREI_MOS, KEDOSHIM, EMOR, BEHAR, BECHUKOSAI, BAMIDBAR, NASSO, BEHAALOSCHA, SHLACH, KORACH, CHUKAS, BALAK, PINCHAS, MATOS, MASEI, DEVARIM, VAESCHANAN, EIKEV, REEH, SHOFTIM, KI_SEITZEI, KI_SAVO, NITZAVIM, VAYEILECH, HAAZINU, VZOS_HABERACHA, VAYAKHEL_PEKUDEI, TAZRIA_METZORA, ACHREI_MOS_KEDOSHIM, BEHAR_BECHUKOSAI, CHUKAS_BALAK, MATOS_MASEI, NITZAVIM_VAYEILECH, SHKALIM, ZACHOR, PARA, HACHODESH
	};
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
	 * <a href="https://en.wikipedia.org/wiki/Birkat_Hachama">Birkas Hachamah</a> is recited every 28 years based on
	 * Tekufas Shmulel (Julian years) that a year is 365.25 days. The <a href="https://en.wikipedia.org/wiki/Maimonides">Rambam</a>
	 * in <a href="http://hebrewbooks.org/pdfpager.aspx?req=14278&amp;st=&amp;pgnum=323">Hilchos Kiddush Hachodesh 9:3</a> states that
	 * tekufas Nisan of year 1 was 7 days + 9 hours before molad Nisan. This is calculated as every 10,227 days (28 * 365.25).  
	 * @return true for a day that Birkas Hachamah is recited.
	 */
	public boolean isBirkasHachamah() {
		int elapsedDays = getJewishCalendarElapsedDays(getJewishYear()); //elapsed days since molad ToHu
		elapsedDays = elapsedDays + getDaysSinceStartOfJewishYear(); //elapsed days to the current calendar date
		
		/* Molad Nisan year 1 was 177 days after molad tohu of Tishrei. We multiply 29.5 day months * 6 months from Tishrei
		 * to Nisan = 177. Subtract 7 days since tekufas Nisan was 7 days and 9 hours before the molad as stated in the Rambam
		 * and we are now at 170 days. Because getJewishCalendarElapsedDays and getDaysSinceStartOfJewishYear use the value for
		 * Rosh Hashana as 1, we have to add 1 day days for a total of 171. To this add a day since the tekufah is on a Tuesday
		 * night and we push off the bracha to Wednesday AM resulting in the 172 used in the calculation.
		 */
		if (elapsedDays % (28 * 365.25) == 172) { // 28 years of 365.25 days + the offset from molad tohu mentioned above
			return true;
		}
		return false;
	}

	/**
	 * Return the type of year for parsha calculations. The algorithm follows the
	 * <a href="http://hebrewbooks.org/pdfpager.aspx?req=14268&amp;st=&amp;pgnum=222">Luach Arba'ah Shearim</a> in the Tur Ohr Hachaim.
	 * @return the type of year for parsha calculations.
	 */
	private int getParshaYearType() {
		int roshHashanaDayOfWeek = (getJewishCalendarElapsedDays(getJewishYear()) + 1) % 7; // plus one to the original Rosh Hashana of year 1 to get a week starting on Sunday
		if(roshHashanaDayOfWeek == 0) {
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
	 * Returns this week's {@link Parsha} if it is Shabbos.
	 * returns Parsha.NONE if a weekday or if there is no parshah that week (for example Yomtov is on Shabbos)
	 * @return the current parsha
	 */
	public Parsha getParshah() {
		if (getDayOfWeek() != Calendar.SATURDAY) {
			return Parsha.NONE;
		}
		
		int yearType = getParshaYearType();
		int roshHashanaDayOfWeek = getJewishCalendarElapsedDays(getJewishYear()) % 7;
		int day = roshHashanaDayOfWeek + getDaysSinceStartOfJewishYear();
		
		if (yearType >= 0) { // negative year should be impossible, but lets cover all bases
			return parshalist[yearType][day/7];
		}
		return Parsha.NONE; //keep the compiler happy
	}

	/**
	 * Returns a parsha enum if the Shabbos is one of the four parshiyos of Parsha.SHKALIM, Parsha.ZACHOR, Parsha.PARA,
	 * Parsha.HACHODESH or Parsha.NONE for a regular Shabbos (or any weekday).
	 * @return one of the four parshiyos of Parsha.SHKALIM, Parsha.ZACHOR, Parsha.PARA, Parsha.HACHODESH or Parsha.NONE.
	 */
	public Parsha getSpecialShabbos() {
		if(getDayOfWeek() == Calendar.SATURDAY)	{
			if((getJewishMonth() == SHEVAT && !isJewishLeapYear()) || (getJewishMonth() == ADAR && isJewishLeapYear())) {
				if(getJewishDayOfMonth() == 25 || getJewishDayOfMonth() == 27 || getJewishDayOfMonth() == 29) {
					return Parsha.SHKALIM;
				}
			}
			if((getJewishMonth() == ADAR && !isJewishLeapYear()) || getJewishMonth() == ADAR_II) {
				if(getJewishDayOfMonth() == 1) {
					return Parsha.SHKALIM;
				}
				if(getJewishDayOfMonth() == 8 || getJewishDayOfMonth() == 9 || getJewishDayOfMonth() == 11 || getJewishDayOfMonth() == 13) {
					return Parsha.ZACHOR;
				}
				if(getJewishDayOfMonth() == 18 || getJewishDayOfMonth() == 20 || getJewishDayOfMonth() == 22 || getJewishDayOfMonth() == 23) {
					return Parsha.PARA;
				}
				if(getJewishDayOfMonth() == 25 || getJewishDayOfMonth() == 27 || getJewishDayOfMonth() == 29) {
					return Parsha.HACHODESH;
				}
			}
			if(getJewishMonth() == NISSAN && getJewishDayOfMonth() == 1) {
				return Parsha.HACHODESH;
			}
		}
		return Parsha.NONE;
	}

	/**
	 * Returns an index of the Jewish holiday or fast day for the current day, or a null if there is no holiday for this
	 * day.
	 * 
	 * @return A String containing the holiday name or an empty string if it is not a holiday.
	 */
	public int getYomTovIndex() {
		final int day = getJewishDayOfMonth();
		final int dayOfWeek = getDayOfWeek();

		// check by month (starts from Nissan)
		switch (getJewishMonth()) {
		case NISSAN:
			if (day == 14) {
				return EREV_PESACH;
			}
			if (day == 15 || day == 21
					|| (!inIsrael && (day == 16 || day == 22))) {
				return PESACH;
			}
			if (day >= 17 && day <= 20
					|| (day == 16 && inIsrael)) {
				return CHOL_HAMOED_PESACH;
			}
			if (isUseModernHolidays()
					&& ((day == 26 && dayOfWeek == 5)
							|| (day == 28 && dayOfWeek == 2)
							|| (day == 27 && dayOfWeek != 1 && dayOfWeek != 6))) {
				return YOM_HASHOAH;
			}
			break;
		case IYAR:
			if (isUseModernHolidays()
					&& ((day == 4 && dayOfWeek == 3)
							|| ((day == 3 || day == 2) && dayOfWeek == 4) || (day == 5 && dayOfWeek == 2))) {
				return YOM_HAZIKARON;
			}
			// if 5 Iyar falls on Wed Yom Haatzmaut is that day. If it fal1s on Friday or Shabbos it is moved back to
			// Thursday. If it falls on Monday it is moved to Tuesday
			if (isUseModernHolidays()
					&& ((day == 5 && dayOfWeek == 4)
							|| ((day == 4 || day == 3) && dayOfWeek == 5) || (day == 6 && dayOfWeek == 3))) {
				return YOM_HAATZMAUT;
			}
			if (day == 14) {
				return PESACH_SHENI;
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
			break;
		case TAMMUZ:
			// push off the fast day if it falls on Shabbos
			if ((day == 17 && dayOfWeek != 7)
					|| (day == 18 && dayOfWeek == 1)) {
				return SEVENTEEN_OF_TAMMUZ;
			}
			break;
		case AV:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((dayOfWeek == 1 && day == 10)
					|| (dayOfWeek != 7 && day == 9)) {
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
			if ((day == 3 && dayOfWeek != 7)
					|| (day == 4 && dayOfWeek == 1)) {
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
			if (day >= 17 && day <= 20 || (day == 16 && inIsrael)) {
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
				if (((day == 11 || day == 12) && dayOfWeek == 5)
						|| (day == 13 && !(dayOfWeek == 6 || dayOfWeek == 7))) {
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
			}
			break;
		case ADAR_II:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((day == 11 || day == 12) && dayOfWeek == 5)
					|| (day == 13 && !(dayOfWeek == 6 || dayOfWeek == 7))) {
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
	 * Returns true if the current day is Yom Tov. The method returns false for Chanukah, Erev Yom Tov (with the
	 * exception of Hoshana Rabba and Erev the second days of Pesach) and fast days.
	 * 
	 * @return true if the current day is a Yom Tov
	 * @see #isErevYomTov()
	 * @see #isErevYomTovSheni()
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
	 * This method will return false for a.
	 * @return if the day is a <em>Yom Tov</em> that is <em>assur bemlacha</em> or <em>Shabbos</em>
	 */
	public boolean isAssurBemelacha() {
		return getDayOfWeek() == Calendar.SATURDAY || isYomTovAssurBemelacha();
	}
	
	/**
	 * Returns true if the day has candle lighting. This will return true on erev <em>Shabbos</em>, erev <em>Yom Tov</em>, the
	 * first day of <em>Rosh Hashana</em> and the first days of <em>Yom Tov</em> out of Israel. It is identical
	 * to calling {@link #isTomorrowShabbosOrYomTov()}.
	 * 
	 * @return if the day has candle lighting
	 */
	public boolean hasCandleLighting() {
		return isTomorrowShabbosOrYomTov();
	}
	
	/**
	 * Returns true if tomorrow is <em>Shabbos</em> or <em>Yom Tov</em>. This will return true on erev <em>Shabbos</em>, erev
	 * <em>Yom Tov</em>, the first day of <em>Rosh Hashana</em> and <em>erev</em> the first days of <em>Yom Tov</em> out of
	 * Israel. It is identical to calling {@link #hasCandleLighting()}.
	 * @return will return if the next day is <em>Shabbos</em> or <em>Yom Tov</em>
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
	 * Returns true if the current day is <em>Aseret Yemei Teshuva</em>.
	 * 
	 * @return if the current day is <em>Aseret Yemei Teshuvah</em>
	 */
	public boolean isAseresYemeiTeshuva(){
		return getJewishMonth() == TISHREI && getJewishDayOfMonth() <= 10;
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
	 * Returns true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em>.
	 *
	 * @return true if the current day is <em>Chol Hamoed</em> of <em>Pesach</em>
	 * @see #isYomTov()
	 * @see #CHOL_HAMOED_PESACH
	 */
	public boolean isCholHamoedPesach() {
		int holidayIndex = getYomTovIndex();
		return holidayIndex == CHOL_HAMOED_PESACH;
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
		return holidayIndex == CHOL_HAMOED_SUCCOS;
	}

	/**
	 * Returns true if the current day is erev Yom Tov. The method returns true for Erev - Pesach (first and last days),
	 * Shavuos, Rosh Hashana, Yom Kippur and Succos and Hoshana Rabba.
	 * 
	 * @return true if the current day is Erev - Pesach, Shavuos, Rosh Hashana, Yom Kippur and Succos
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
	 * Returns if the day is Shabbos and sunday is Rosh Chodesh.
	 *
	 * @return true if it is Shabbos and sunday is Rosh Chodesh.
	 */
	public boolean isMacharChodesh() {
		return (getDayOfWeek() == Calendar.SATURDAY && (getJewishDayOfMonth() == 30 || getJewishDayOfMonth() == 29));
	}

	/**
	 * Returns if the day is Shabbos Mevorchim.
	 *
	 * @return true if it is Shabbos Mevorchim.
	 */
	public boolean isShabbosMevorchim() {
		return (getDayOfWeek() == Calendar.SATURDAY && getJewishDayOfMonth() >= 23 && getJewishDayOfMonth() <= 29 && getJewishMonth() != ELUL);
	}

	/**
	 * Returns the int value of the Omer day or -1 if the day is not in the omer
	 * 
	 * @return The Omer count as an int or -1 if it is not a day of the Omer.
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
	 * Returns the molad in Standard Time in Yerushalayim as a Date. The traditional calculation uses local time. This
	 * method subtracts 20.94 minutes (20 minutes and 56.496 seconds) from the local time (Har Habayis with a longitude
	 * of 35.2354&deg; is 5.2354&deg; away from the %15 timezone longitude) to get to standard time. This method
	 * intentionally uses standard time and not dailight savings time. Java will implicitly format the time to the
	 * default (or set) Timezone.
	 * 
	 * @return the Date representing the moment of the molad in Yerushalayim standard time (GMT + 2)
	 */
	public Date getMoladAsDate() {
		com.kosherjava.zmanim.hebrewcalendar.JewishDate molad = getMolad();
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
	 * displaying the next <em>tzais</em> if the zman is between <em>alos</em> and <em>tzais</em>.
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
	 * Returns the earliest time of Kiddush Levana calculated as 7 days after the molad as mentioned by the <a
	 * href="http://en.wikipedia.org/wiki/Yosef_Karo">Mechaber</a>. See the <a
	 * href="http://en.wikipedia.org/wiki/Yoel_Sirkis">Bach's</a> opinion on this time. This method returns the time
	 * even if it is during the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider
	 * displaying the next <em>tzais</em> if the zman is between <em>alos</em> and <em>tzais</em>.
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
	 * halfway between molad and molad. This adds half the 29 days, 12 hours and 793 chalakim time between molad and
	 * molad (14 days, 18 hours, 22 minutes and 666 milliseconds) to the month's molad. This method returns the time
	 * even if it is during the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider
	 * displaying <em>alos</em> before this time if the zman is between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment halfway between molad and molad.
	 * @see #getSofZmanKidushLevana15Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevanaBetweenMoldos()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevanaBetweenMoldos(Date, Date)
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
	 * href="http://www.worldcat.org/oclc/461326125">Shaarei Zmanim</a>. This method returns the time even if it is during
	 * the day when <em>Kiddush Levana</em> can't be said. Callers of this method should consider displaying <em>alos</em>
	 * before this time if the zman is between <em>alos</em> and <em>tzais</em>.
	 * 
	 * @return the Date representing the moment 15 days after the molad.
	 * @see #getSofZmanKidushLevanaBetweenMoldos()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevana15Days()
	 * @see com.kosherjava.zmanim.ComplexZmanimCalendar#getSofZmanKidushLevana15Days(Date, Date)
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
