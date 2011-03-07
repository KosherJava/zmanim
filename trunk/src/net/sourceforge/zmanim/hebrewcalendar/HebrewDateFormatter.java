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

/**
 * The HebrewDateFormatter class formats a {@link JewishDate}.
 * 
 * This class is part of a refactoring of <a
 * href="http://www.facebook.com/avromf">Avrom Finkelstien's</a> HebrewDate
 * class that removes formatting code from the core HebrewDate class.
 * TODO: add additional formatting options including Hebrew formatting support.
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.1
 */
public class HebrewDateFormatter {
	private static final String[] hebrewMonths = { "Nissan", "Iyar", "Sivan",
			"Tamuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves",
			"Shevat", "Adar", "Adar II" };
	
	// parsha names in both Ashkenazi and Sephardi pronunciation
	// Somewhat redundant (don't you think?)
//	private static final String[][] Prounouncewdparshios = { { "Bereshit", "Bereshis" },
//			{ "Noach", "Noach" }, { "Lech-Lecha", "Lech-Lecha" },
//			{ "Vayera", "Vayera" }, { "Chayei Sara", "Chayei Sara" },
//			{ "Toldot", "Toldos" }, { "Vayetzei", "Vayetzei" },
//			{ "Vayishlach", "Vayishlach" }, { "Vayeshev", "Vayeshev" },
//			{ "Miketz", "Miketz" }, { "Vayigash", "Vayigash" },
//			{ "Vayechi", "Vayechi" }, { "Shemot", "Shemos" },
//			{ "Vaera", "Vaera" }, { "Bo", "Bo" }, { "Beshalach", "Beshalach" },
//			{ "Yitro", "Yisro" }, { "Mishpatim", "Mishpatim" },
//			{ "Terumah", "Terumah" }, { "Tetzaveh", "Tetzaveh" },
//			{ "Ki Tisa", "Ki Sisa" }, { "Vayakhel", "Vayakhel" },
//			{ "Pekudei", "Pekudei" }, { "Vayikra", "Vayikra" },
//			{ "Tzav", "Tzav" }, { "Shmini", "Shmini" }, { "Tazria", "Sazria" },
//			{ "Metzora", "Metzora" }, { "Achrei Mot", "Achrei Mos" },
//			{ "Kedoshim", "Kedoshim" }, { "Emor", "Emor" },
//			{ "Behar", "Behar" }, { "Bechukotai", "Bechukosai" },
//			{ "Bamidbar", "Bamidbar" }, { "Nasso", "Nasso" },
//			{ "Beha'alotcha", "Beha'aloscha" }, { "Sh'lach", "Sh'lach" },
//			{ "Korach", "Korach" }, { "Chukat", "Chukas" },
//			{ "Balak", "Balak" }, { "Pinchas", "Pinchas" },
//			{ "Matot", "Matos" }, { "Masei", "Masei" },
//			{ "Devarim", "Devarim" }, { "Vaetchanan", "Vaeschanan" },
//			{ "Eikev", "Eikev" }, { "Re'eh", "Re'eh" },
//			{ "Shoftim", "Shoftim" }, { "Ki Teitzei", "Ki Seitzei" },
//			{ "Ki Tavo", "Ki Savo" }, { "Nitzavim", "Nitzavim" },
//			{ "Vayeilech", "Vayeilech" }, { "Ha'Azinu", "Ha'Azinu" },
//			{ "Vayakhel Pekudei", "Vayakhel Pekudei" },
//			{ "Tazria Metzora", "Sazria Metzora" },
//			{ "Achrei Mot Kedoshim", "Achrei Mos Kedoshim" },
//			{ "Behar Bechukotai", "Behar Bechukosai" },
//			{ "Chukat Balak", "Chukas Balak" },
//			{ "Matot Masei", "Matos Masei" },
//			{ "Nitzavim Vayeilech", "Nitzavim Vayeilech" } };

	/**
	 * Returns a string containing the Hebrew date in the form,
	 * "day Month, year" e.g. "21 Shevat, 5729"
	 */
	public static String getHebrewDateAsString(JewishDate hd) {
		return hd.getJewishDayOfMonth() + " " + getHebrewMonthAsString(hd) + ", " + hd.getJewishYear();
	}

	/**
	 * returns a string of the current Hebrew month such as "Tishrei".
	 */
	public static String getHebrewMonthAsString(JewishDate hd) {
		// if it is a leap year and 12th month //
		if (JewishDate.isJewishLeapYear(hd.getJewishYear()) && hd.getJewishMonth() == 12) {
			return "Adar I";
		} else {
			return hebrewMonths[hd.getJewishMonth() - 1];
		}
	}
	
	/**
	 * returns a String of the Omer day in the form "Omer X" or "Lag B'Omer" or
	 * an empty string if there is no Omer this day.
	 * FIXME - Is this method really required?
	 */
	public static String getOmerAsString(JewishDate hd) {
		int omer = hd.getDayOfOmer();

		// if not Omer day //
		if (omer == 0) {
			return "";

		} else if (omer == 33) { // if lag b'omer
			return "Lag B'Omer";
		} else {
			return "Omer " + omer;
		}
	}
}