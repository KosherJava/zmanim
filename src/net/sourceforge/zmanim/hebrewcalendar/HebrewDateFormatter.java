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

import java.text.SimpleDateFormat;

/**
 * The HebrewDateFormatter class formats a {@link JewishDate}.
 * 
 * The class formats Jewish dates in Hebrew or Latin chars, and has various settings. Sample full date output includes
 * (using various options):
 * <ul>
 * <li>21 Shevat, 5729</li>
 * <li>&#x5DB;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5D8;</li>
 * <li>&#x5D4;&#x5F3; &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;</li>
 * <li>&#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DA;&#x5F3;</li>
 * </ul>
 * <b>TODO:</b> Support formatting of Yomim Tovim. <b>TODO:</b> Support alternate transliterations for parshas hashavua.
 * 
 * @see net.sourceforge.zmanim.hebrewcalendar.JewishDate
 * @see net.sourceforge.zmanim.hebrewcalendar.JewishCalendar
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.2
 */
public class HebrewDateFormatter {
	private boolean hebrewFormat = true;
	private boolean useLonghebrewYears = false;
	private boolean useGershGershayim = true;
	private boolean includeYom = true;
	private boolean longWeekFormat = true;
	
	private static final String GERESH = "\u05F3";
	private static final String GERSHAYIM = "\u05F4";
	private String[] transliteratedMonths = { "Nissan", "Iyar", "Sivan", "Tamuz", "Av", "Elul", "Tishrei", "Cheshvan",
			"Kislev", "Teves", "Shevat", "Adar", "Adar II", "Adar I" };
	private String hebrewOmerPrefix = "\u05D1";

	/**
	 * @return the hebrewFormat
	 */
	public boolean isHebrewFormat() {
		return hebrewFormat;
	}

	/**
	 * @param hebrewFormat
	 *            the hebrewFormat to set
	 */
	public void setHebrewFormat(boolean hebrewFormat) {
		this.hebrewFormat = hebrewFormat;
	}

	/**
	 * Returns the Hebrew Omer prefix. By default it is the letter &#x5D1;, but can be set to &#x5DC; (or any other
	 * prefix) using the {@link #setHebrewOmerPrefix(String)}.
	 * 
	 * @return the hebrewOmerPrefix
	 */
	public String getHebrewOmerPrefix() {
		return hebrewOmerPrefix;
	}

	/**
	 * Method to set the Hebrew Omer prefix. By default it is the letter &#x5D1;, but this allows setting it to a
	 * &#x5DC; (or any other prefix).
	 * 
	 * @param hebrewOmerPrefix
	 *            the hebrewOmerPrefix to set. You can use the Unicode &#92;u05DC to set it to &#x5DC;.
	 */
	public void setHebrewOmerPrefix(String hebrewOmerPrefix) {
		this.hebrewOmerPrefix = hebrewOmerPrefix;
	}

	/**
	 * Returns the list of months transliterated into Latin chars. The default list of months uses Ashkenazi
	 * pronunciation in typical American English spelling. This list has a length of 14 with 3 variations for Adar -
	 * "Adar", "Adar II", "Adar I"
	 * 
	 * @return the list of months beginning in Nissan and ending in in "Adar", "Adar II", "Adar I". The default list is
	 *         currently "Nissan", "Iyar", "Sivan", "Tamuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves",
	 *         "Shevat", "Adar", "Adar II", "Adar I"
	 */
	public String[] getTransliteratedMonthList() {
		return transliteratedMonths;
	}

	/**
	 * Setter method to allow overriding of the default list of months transliterated into into Latin chars. The default
	 * uses Ashkenazi American English transliteration.
	 * 
	 * @param transliteratedMonths
	 *            an array of 14 month names such as { "Nissan", "Iyar", "Sivan", "Tamuz", "Av", "Elul", "Tishrei",
	 *            "Heshvan", "Kislev", "Tevet", "Shevat", "Adar", "Adar II", "Adar I" }
	 */
	public void setTransliteratedMonthList(String[] transliteratedMonths) {
		this.transliteratedMonths = transliteratedMonths;
	}

	/**
	 * Unicode list of Hebrew months.
	 */
	private static final String[] hebrewMonths = { "\u05E0\u05D9\u05E1\u05DF", "\u05D0\u05D9\u05D9\u05E8",
			"\u05E1\u05D9\u05D5\u05D5\u05DF", "\u05EA\u05DE\u05D5\u05D6", "\u05D0\u05D1", "\u05D0\u05DC\u05D5\u05DC",
			"\u05EA\u05E9\u05E8\u05D9", "\u05D7\u05E9\u05D5\u05D5\u05DF", "\u05DB\u05E1\u05DC\u05D5",
			"\u05D8\u05D1\u05EA", "\u05E9\u05D1\u05D8", "\u05D0\u05D3\u05E8", "\u05D0\u05D3\u05E8 \u05D1",
			"\u05D0\u05D3\u05E8 \u05D0" };
	

	/**
	 * list of transliterated parshiyos. The formatParsha method formatting Ashkenazi VS Sephardi English
	 * transliteration. Using the default will use the Ashkenazi pronounciation. FIXME: correct docs.
	 */
	private String[] transliteratedParshios = { "Bereshis", "Noach", "Lech Lecha", "Vayera",
			"Chayei Sara", "Toldos", "Vayetzei", "Vayishlach", "Vayeshev", "Miketz", "Vayigash", "Vayechi", "Shemos",
			"Vaera", "Bo", "Beshalach", "Yisro", "Mishpatim", "Terumah", "Tetzaveh", "Ki Sisa", "Vayakhel", "Pekudei",
			"Vayikra", "Tzav", "Shmini", "Tazria", "Metzora", "Achrei Mos", "Kedoshim", "Emor", "Behar", "Bechukosai",
			"Bamidbar", "Nasso", "Beha'aloscha", "Sh'lach", "Korach", "Chukas", "Balak", "Pinchas", "Matos", "Masei",
			"Devarim", "Vaeschanan", "Eikev", "Re'eh", "Shoftim", "Ki Seitzei", "Ki Savo", "Nitzavim", "Vayeilech",
			"Ha'Azinu", "Vayakhel Pekudei", "Tazria Metzora", "Achrei Mos Kedoshim", "Behar Bechukosai",
			"Chukas Balak", "Matos Masei", "Nitzavim Vayeilech" };

	/**
	 * @return the list of transliterated Parshios
	 */
	public String[] getTransliteratedParshiosList() {
		return transliteratedParshios;
	}

	/**
	 * @param transliteratedParshios the transliterated Parshios to set
	 */
	public void setTransliteratedParshiosList(String[] transliteratedParshios) {
		this.transliteratedParshios = transliteratedParshios;
	}

	/**
	 * Unicode list of Hebrew parshiyos.
	 */
	private static final String[] hebrewParshiyos = { "\u05D1\u05E8\u05D0\u05E9\u05D9\u05EA", "\u05E0\u05D7",
			"\u05DC\u05DA \u05DC\u05DA", "\u05D5\u05D9\u05E8\u05D0", "\u05D7\u05D9\u05D9 \u05E9\u05E8\u05D4",
			"\u05EA\u05D5\u05DC\u05D3\u05D5\u05EA", "\u05D5\u05D9\u05E6\u05D0", "\u05D5\u05D9\u05E9\u05DC\u05D7",
			"\u05D5\u05D9\u05E9\u05D1", "\u05DE\u05E7\u05E5", "\u05D5\u05D9\u05D2\u05E9", "\u05D5\u05D9\u05D7\u05D9",

			"\u05E9\u05DE\u05D5\u05EA", "\u05D5\u05D0\u05E8\u05D0", "\u05D1\u05D0", "\u05D1\u05E9\u05DC\u05D7",
			"\u05D9\u05EA\u05E8\u05D5", "\u05DE\u05E9\u05E4\u05D8\u05D9\u05DD", "\u05EA\u05E8\u05D5\u05DE\u05D4",
			"\u05EA\u05E6\u05D5\u05D4", "\u05DB\u05D9 \u05EA\u05E9\u05D0", "\u05D5\u05D9\u05E7\u05D4\u05DC",
			"\u05E4\u05E7\u05D5\u05D3\u05D9",

			"\u05D5\u05D9\u05E7\u05E8\u05D0", "\u05E6\u05D5", "\u05E9\u05DE\u05D9\u05E0\u05D9",
			"\u05EA\u05D6\u05E8\u05D9\u05E2", "\u05DE\u05E6\u05E8\u05E2",
			"\u05D0\u05D7\u05E8\u05D9 \u05DE\u05D5\u05EA", "\u05E7\u05D3\u05D5\u05E9\u05D9\u05DD",
			"\u05D0\u05DE\u05D5\u05E8", "\u05D1\u05D4\u05E8", "\u05D1\u05D7\u05E7\u05EA\u05D9",

			"\u05D1\u05DE\u05D3\u05D1\u05E8", "\u05E0\u05E9\u05D0", "\u05D1\u05D4\u05E2\u05DC\u05EA\u05DA",
			"\u05E9\u05DC\u05D7 \u05DC\u05DA", "\u05E7\u05E8\u05D7", "\u05D7\u05D5\u05E7\u05EA", "\u05D1\u05DC\u05E7",
			"\u05E4\u05D9\u05E0\u05D7\u05E1", "\u05DE\u05D8\u05D5\u05EA", "\u05DE\u05E1\u05E2\u05D9",

			"\u05D3\u05D1\u05E8\u05D9\u05DD", "\u05D5\u05D0\u05EA\u05D7\u05E0\u05DF", "\u05E2\u05E7\u05D1",
			"\u05E8\u05D0\u05D4", "\u05E9\u05D5\u05E4\u05D8\u05D9\u05DD", "\u05DB\u05D9 \u05EA\u05E6\u05D0",
			"\u05DB\u05D9 \u05EA\u05D1\u05D5\u05D0", "\u05E0\u05D9\u05E6\u05D1\u05D9\u05DD",
			"\u05D5\u05D9\u05DC\u05DA", "\u05D4\u05D0\u05D6\u05D9\u05E0\u05D5",

			"\u05D5\u05D9\u05E7\u05D4\u05DC \u05E4\u05E7\u05D5\u05D3\u05D9",
			"\u05EA\u05D6\u05E8\u05D9\u05E2 \u05DE\u05E6\u05E8\u05E2",
			"\u05D0\u05D7\u05E8\u05D9 \u05DE\u05D5\u05EA \u05E7\u05D3\u05D5\u05E9\u05D9\u05DD",
			"\u05D1\u05D4\u05E8 \u05D1\u05D7\u05E7\u05EA\u05D9", "\u05D7\u05D5\u05E7\u05EA \u05D1\u05DC\u05E7",
			"\u05DE\u05D8\u05D5\u05EA \u05DE\u05E1\u05E2\u05D9",
			"\u05E0\u05D9\u05E6\u05D1\u05D9\u05DD \u05D5\u05D9\u05DC\u05DA" };

	/**
	 * @return the includeYom
	 */
	public boolean isIncludeYom() {
		return includeYom;
	}

	/**
	 * @param includeYom
	 *            the includeYom to set
	 */
	public void setIncludeYom(boolean includeYom) {
		this.includeYom = includeYom;
	}

	/**
	 * Unicode list of Hebrew days of week.
	 */
	private static final String[] hebrewDaysOfWeek = { "\u05E8\u05D0\u05E9\u05D5\u05DF", "\u05E9\u05E0\u05D9",
			"\u05E9\u05DC\u05D9\u05E9\u05D9", "\u05E8\u05D1\u05D9\u05E2\u05D9", "\u05D7\u05DE\u05D9\u05E9\u05D9",
			"\u05E9\u05E9\u05D9", "\u05E9\u05D1\u05EA" };

	/**
	 * FIXME: add docs
	 * 
	 * @param jewishDate
	 * @return
	 */
	public String formatDayOfWeek(JewishDate jewishDate) {
		if (hebrewFormat) {
			StringBuffer sb = new StringBuffer(includeYom ? "\u05D9\u05D5\u05DD " : "");
			sb.append(longWeekFormat ? hebrewDaysOfWeek[jewishDate.getDayOfWeek() - 1] : formatHebrewNumber(jewishDate
					.getDayOfWeek()));
			return sb.toString();
		} else {
			return new SimpleDateFormat("EEEE").format(jewishDate.getTime());
		}
	}

	/**
	 * If the formatter is set to format in Hebrew, returns a string of the current parsha(ios) in Hebrew for example
	 * &#x05D1;&#x05E8;&#x05D0;&#x05E9;&#x05D9;&#x05EA; or &#x05E0;&#x05D9;&#x05E6;&#x05D1;&#x05D9;&#x05DD;
	 * &#x05D5;&#x05D9;&#x05DC;&#x05DA; or an empty string if there are none. If not set to Hebrew, it returns a string
	 * of the parsha(ios) transliterated into Latin chars. The default uses Ashkenazi pronunciation in typical American
	 * English spelling, for example Bereshis or Nitzavim Vayeilech or an empty string if there are none.
	 * 
	 * @param jewishCalendar
	 * @return today's parsha(ios) in Hebrew for example, if the formatter is set to format in Hebrew, returns a string
	 *         of the current parsha(ios) in Hebrew for example &#x05D1;&#x05E8;&#x05D0;&#x05E9;&#x05D9;&#x05EA; or
	 *         &#x05E0;&#x05D9;&#x05E6;&#x05D1;&#x05D9;&#x05DD; &#x05D5;&#x05D9;&#x05DC;&#x05DA; or an empty string if
	 *         there are none. If not set to Hebrew, it returns a string of the parsha(ios) transliterated into Latin
	 *         chars. The default uses Ashkenazi pronunciation in typical American English spelling, for example
	 *         Bereshis or Nitzavim Vayeilech or an empty string if there are none.
	 */
	public String formatParsha(JewishCalendar jewishCalendar) {
		int index = jewishCalendar.getParshaIndex();
		return index == -1 ? "" : hebrewFormat ? hebrewParshiyos[index] : transliteratedParshios[index];
	}

	/**
	 * Returns a string of the parsha(ios) transliterated into Latin chars. The default uses Ashkenazi pronunciation in
	 * typical American English spelling, for example Bereshis or Nitzavim Vayeilech or an empty string if there are
	 * none.
	 * 
	 * @param jewishCalendar
	 * @return a string of the parsha(ios) transliterated into Latin chars. The default uses Ashkenazi pronunciation in
	 *         typical American English spelling, for example Bereshis or Nitzavim Vayeilech or an empty string if there
	 *         are none.
	 */
	// private String getTransliteratedParsha(JewishCalendar jewishCalendar) {
	// return getTransliteratedParsha(jewishCalendar.getParshaIndex());
	// }

	/**
	 * Returns whether the class is set to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and
	 * numbers. When true and output would look like &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;&#x5F3;. When set to false, this output would display as &#x5DB&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;.
	 * 
	 * @return true if set to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and numbers.
	 */
	public boolean isUseGershGershayim() {
		return useGershGershayim;
	}

	/**
	 * Sets whether to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and numbers. The default
	 * value is true and output would look like &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;&#x5F3;. When set to false, this output would display as &#x5DB&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;.
	 * 
	 * @param useGershGershayim
	 *            set to false to omit the Geresh &#x5F3; and Gershayim &#x5F4; in formatting
	 */
	public void setUseGershGershayim(boolean useGershGershayim) {
		this.useGershGershayim = useGershGershayim;
	}

	/**
	 * Returns whether the class is set to use the thousands digit when formatting. When formatting a Hebrew Year,
	 * traditionally the thousands digit is omitted and output for a year such as 5729 (1969 Gregorian) would be
	 * calculated for 729 and format as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;. When set to true the long format year such
	 * as &#x5D4;&#x5F3; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8; for 5729/1969 is returned.
	 * 
	 * @return true if set to use the the thousands digit when formatting Hebrew dates and numbers.
	 */
	public boolean isUseLongHebrewYears() {
		return useLonghebrewYears;
	}

	/**
	 * When formatting a Hebrew Year, traditionally the thousands digit is omitted and output for a year such as 5729
	 * (1969 Gregorian) would be calculated for 729 and format as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;. This method
	 * allows setting this to true to return the long format year such as &#x5D4;&#x5F3;
	 * &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8; for 5729/1969.
	 * 
	 * @param useLongHebrewYears
	 *            Set this to true to use the long formatting
	 */
	public void setUseLongHebrewYears(boolean useLongHebrewYears) {
		this.useLonghebrewYears = useLongHebrewYears;
	}

	/**
	 * Formats the Jewish date. If the formatter is set to Hebrew, it will format in the form, "day Month year" for
	 * example &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;, and the format
	 * "21 Shevat, 5729" if not.
	 * 
	 * @param jewishDate
	 *            the JewishDate to be formatted
	 * @param hebrew
	 *            format the date in Hebrew
	 * @return the formatted date. If the formatter is set to Hebrew, it will format in the form, "day Month year" for
	 *         example &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;, and the format
	 *         "21 Shevat, 5729" if not.
	 */
	public String format(JewishDate jewishDate) {
		if (this.hebrewFormat) {
			return formatHebrewNumber(jewishDate.getJewishDayOfMonth()) + " " + formatMonth(jewishDate) + " "
					+ formatHebrewNumber(jewishDate.getJewishYear());
		} else {
			return jewishDate.getJewishDayOfMonth() + " " + formatMonth(jewishDate) + ", " + jewishDate.getJewishYear();
		}
	}

	/**
	 * Returns a string of the current Hebrew month such as "Tishrei". Returns a string of the current Hebrew month such
	 * as "&#x5D0;&#x5D3;&#x5E8; &#x5D1;&#x5F3;".
	 */
	public String formatMonth(JewishDate jewishDate) {
		if (this.hebrewFormat) {
			if (JewishDate.isJewishLeapYear(jewishDate.getJewishYear()) && jewishDate.getJewishMonth() == 12) {
				return transliteratedMonths[13]; // return Adar I and not Adar in a leap year
			} else {
				return transliteratedMonths[jewishDate.getJewishMonth() - 1];
			}
		} else {
			if (JewishDate.isJewishLeapYear(jewishDate.getJewishYear()) && jewishDate.getJewishMonth() == 12) {
				return hebrewMonths[13] + (useGershGershayim ? GERESH : ""); // return Adar I and not Adar in a leap
																				// year
			} else if (JewishDate.isJewishLeapYear(jewishDate.getJewishYear()) && jewishDate.getJewishMonth() == 13) {
				return hebrewMonths[12] + (useGershGershayim ? GERESH : "");
			} else {
				return hebrewMonths[jewishDate.getJewishMonth() - 1];
			}
		}
	}

	/**
	 * Returns a String of the Omer day in the form "Omer X" or "Lag BaOmer" or an empty string if there is no Omer this
	 * day. FIXME - Is this method really required?
	 * 
	 * Returns a String of the Omer day in the form &#x5DC;&#x5F4;&#x5D2; &#x5D1;&#x05E2;&#x05D5;&#x05DE;&#x5E8; or an
	 * empty string if there is no Omer this day. The default formatting has a &#x5D1;&#x5F3; prefix that would output
	 * &#x5D1;&#x05E2;&#x05D5;&#x05DE;&#x5E8;, but this can be set via the {@link #setHebrewOmerPrefix(String)} method
	 * to use a &#x5DC; and output &#x5DC;&#x5F4;&#x5D2; &#x5DC;&#x05E2;&#x05D5;&#x05DE;&#x5E8;. All days of the Omer
	 * use the same format
	 */
	public String formatOmer(JewishCalendar jewishCalendar) {
		if (this.hebrewFormat) {
			int omer = jewishCalendar.getDayOfOmer();
			if (omer == Integer.MIN_VALUE) {
				return "";
			} else {
				return formatHebrewNumber(omer) + " " + hebrewOmerPrefix + "\u05E2\u05D5\u05DE\u05E8";
			}
		} else {
			int omer = jewishCalendar.getDayOfOmer();
			if (omer == Integer.MIN_VALUE) {
				return "";
			} else if (omer == 33) { // if lag b'omer
				return "Lag BaOmer";
			} else {
				return "Omer " + omer;
			}
		}
	}

	/**
	 * Returns a Hebrew formatted string of a number. The method can calculate from 0 - 9999.
	 * <ul>
	 * <li>Single digit numbers such as 3, 30 and 100 will be returned with a &#x5F3; (<a
	 * href="http://en.wikipedia.org/wiki/Geresh">Geresh</a>) appended as at the end. For example &#x5D2;&#x5F3;,
	 * &#x5DC;&#x5F3; and &#x5E7;&#x5F3;</li>
	 * <li>multi digit numbers such as 21 and 769 will be returned with a &#x5F4; (<a
	 * href="http://en.wikipedia.org/wiki/Gershayim">Gershayim</a>) between the second to last and last letters. For
	 * example &#x5DB;&#x5F4;&#x5D0;, &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;</li>
	 * <li>15 and 16 will be returned as &#x5D8;&#x5F4;&#x5D5; and &#x5D8;&#x5F4;&#x5D6;</li>
	 * <li>Single digit numbers (years assumed) such as 6000 (%1000=0) will be returned as &#x5D5;&#x5F3;
	 * &#x5D0;&#x5DC;&#x5E4;&#x5D9;&#x5DD;</li>
	 * <li>0 will return &#x5D0;&#x5E4;&#x05E1;</li>
	 * </ul>
	 * 
	 * @param number
	 *            the number to be formatted. It will trow an IllegalArgumentException if the number is < 0 or > 9999.
	 * @return the Hebrew formatted number such as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;
	 * 
	 */
	public String formatHebrewNumber(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("negative numbers can't be formatted");
		} else if (number > 9999) {
			throw new IllegalArgumentException("numbers > 9999 can't be formatted");
		}

		String ALAFIM = "\u05D0\u05DC\u05E4\u05D9\u05DD";
		String EFES = "\u05D0\u05E4\u05E1";

		String[] jHundreds = new String[] { "", "\u05E7", "\u05E8", "\u05E9", "\u05EA", "\u05EA\u05E7", "\u05EA\u05E8",
				"\u05EA\u05E9", "\u05EA\u05EA", "\u05EA\u05EA\u05E7" };
		String[] jTens = new String[] { "", "\u05D9", "\u05DB", "\u05DC", "\u05DE", "\u05E0", "\u05E1", "\u05E2",
				"\u05E4", "\u05E6" };
		String[] jTenEnds = new String[] { "", "\u05D9", "\u05DA", "\u05DC", "\u05DD", "\u05DF", "\u05E1", "\u05E2",
				"\u05E3", "\u05E5" };
		String[] tavTaz = new String[] { "\u05D8\u05D5", "\u05D8\u05D6" };
		String[] jOnes = new String[] { "", "\u05D0", "\u05D1", "\u05D2", "\u05D3", "\u05D4", "\u05D5", "\u05D6",
				"\u05D7", "\u05D8" };

		if (number == 0) { // do we realy need this? Should it be applicable to a date?
			return EFES;
		}
		int shortNumber = number % 1000; // discard thousands
		// next check for all possible single Hebrew digit years
		boolean singleDigitNumber = (shortNumber < 11 || (shortNumber < 100 && shortNumber % 10 == 0) || (shortNumber <= 400 && shortNumber % 100 == 0));
		int thousands = number / 1000; // get # thousands
		StringBuffer returnBuffer = new StringBuffer();
		// append thousands to String
		if (number % 1000 == 0) { // in year is 5000, 4000 etc
			returnBuffer.append(jOnes[thousands]);
			if (isUseGershGershayim()) {
				returnBuffer.append(GERESH);
			}
			returnBuffer.append(" ");
			returnBuffer.append(ALAFIM); // add # of thousands plus word thousand (overide alafim boolean)
			return returnBuffer.toString();
		} else if (useLonghebrewYears && number >= 1000) { // if alafim boolean display thousands
			returnBuffer.append(jOnes[thousands]);
			if (isUseGershGershayim()) {
				returnBuffer.append(GERESH); // append thousands quote
			}
			returnBuffer.append(" ");
		}
		number = number % 1000; // remove 1000s
		int hundreds = number / 100; // # of hundreds
		returnBuffer.append(jHundreds[hundreds]); // add hundreds to String
		number = number % 100; // remove 100s
		if (number == 15) { // special case 15
			returnBuffer.append(tavTaz[0]);
		} else if (number == 16) { // special case 16
			returnBuffer.append(tavTaz[1]);
		} else {
			int tens = number / 10;
			if (number % 10 == 0) { // if evenly divisable by 10
				if (singleDigitNumber == false) {
					returnBuffer.append(jTenEnds[tens]); // end letters so years like 5750 will end with an end nun
				} else {
					returnBuffer.append(jTens[tens]); // standard letters so years like 5050 will end with a regular nun
				}
			} else {
				returnBuffer.append(jTens[tens]);
				number = number % 10;
				returnBuffer.append(jOnes[number]);
			}
		}
		if (isUseGershGershayim()) {
			if (singleDigitNumber == true) {
				returnBuffer.append(GERESH); // append single quote
			} else { // append double quote before last digit
				returnBuffer.insert(returnBuffer.length() - 1, GERSHAYIM);
			}
		}
		return returnBuffer.toString();
	}
}