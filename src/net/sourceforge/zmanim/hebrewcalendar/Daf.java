/*
 * Zmanim Java API
 * Copyright (C) 2011 Eliyahu Hershfeld
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

/**
 * An Object representing a Daf in the Daf Yomi cycle.
 * 
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.0.1
 */
public class Daf {
	private int masechtaNumber;
	private int daf;

	private static String[] masechtosBavliTransliterated = { "Berachos", "Shabbos", "Eruvin", "Pesachim", "Shekalim",
			"Yoma", "Sukkah", "Beitzah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah", "Yevamos",
			"Kesubos", "Nedarim", "Nazir", "Sotah", "Gitin", "Kiddushin", "Bava Kamma", "Bava Metzia", "Bava Basra",
			"Sanhedrin", "Makkos", "Shevuos", "Avodah Zarah", "Horiyos", "Zevachim", "Menachos", "Chullin", "Bechoros",
			"Arachin", "Temurah", "Kerisos", "Meilah", "Kinnim", "Tamid", "Midos", "Niddah" };

	private static String[] masechtosBavli = { "\u05D1\u05E8\u05DB\u05D5\u05EA", "\u05E9\u05D1\u05EA",
			"\u05E2\u05D9\u05E8\u05D5\u05D1\u05D9\u05DF", "\u05E4\u05E1\u05D7\u05D9\u05DD",
			"\u05E9\u05E7\u05DC\u05D9\u05DD", "\u05D9\u05D5\u05DE\u05D0", "\u05E1\u05D5\u05DB\u05D4",
			"\u05D1\u05D9\u05E6\u05D4", "\u05E8\u05D0\u05E9 \u05D4\u05E9\u05E0\u05D4",
			"\u05EA\u05E2\u05E0\u05D9\u05EA", "\u05DE\u05D2\u05D9\u05DC\u05D4",
			"\u05DE\u05D5\u05E2\u05D3 \u05E7\u05D8\u05DF", "\u05D7\u05D2\u05D9\u05D2\u05D4",
			"\u05D9\u05D1\u05DE\u05D5\u05EA", "\u05DB\u05EA\u05D5\u05D1\u05D5\u05EA", "\u05E0\u05D3\u05E8\u05D9\u05DD",
			"\u05E0\u05D6\u05D9\u05E8", "\u05E1\u05D5\u05D8\u05D4", "\u05D2\u05D9\u05D8\u05D9\u05DF",
			"\u05E7\u05D9\u05D3\u05D5\u05E9\u05D9\u05DF", "\u05D1\u05D1\u05D0 \u05E7\u05DE\u05D0",
			"\u05D1\u05D1\u05D0 \u05DE\u05E6\u05D9\u05E2\u05D0", "\u05D1\u05D1\u05D0 \u05D1\u05EA\u05E8\u05D0",
			"\u05E1\u05E0\u05D4\u05D3\u05E8\u05D9\u05DF", "\u05DE\u05DB\u05D5\u05EA",
			"\u05E9\u05D1\u05D5\u05E2\u05D5\u05EA", "\u05E2\u05D1\u05D5\u05D3\u05D4 \u05D6\u05E8\u05D4",
			"\u05D4\u05D5\u05E8\u05D9\u05D5\u05EA", "\u05D6\u05D1\u05D7\u05D9\u05DD", "\u05DE\u05E0\u05D7\u05D5\u05EA",
			"\u05D7\u05D5\u05DC\u05D9\u05DF", "\u05D1\u05DB\u05D5\u05E8\u05D5\u05EA", "\u05E2\u05E8\u05DB\u05D9\u05DF",
			"\u05EA\u05DE\u05D5\u05E8\u05D4", "\u05DB\u05E8\u05D9\u05EA\u05D5\u05EA", "\u05DE\u05E2\u05D9\u05DC\u05D4",
			"\u05EA\u05DE\u05D9\u05D3", "\u05E7\u05D9\u05E0\u05D9\u05DD", "\u05DE\u05D9\u05D3\u05D5\u05EA",
			"\u05E0\u05D3\u05D4" };

	/**
	 * @return the masechtaNumber
	 */
	public int getMasechtaNumber() {
		return masechtaNumber;
	}

	/**
	 * Set the masechta number in the order of teh Daf Yomi. The sequence is: Berachos, Shabbos, Eruvin, Pesachim,
	 * Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah, Yevamos, Kesubos, Nedarim,
	 * Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin, Makkos, Shevuos, Avodah Zarah,
	 * Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah, Kinnim, Tamid, Midos and
	 * Niddah.
	 * 
	 * @param masechtaNumber
	 *            the masechtaNumber to set
	 */
	public void setMasechtaNumber(int masechtaNumber) {
		this.masechtaNumber = masechtaNumber;
	}

	/**
	 * Constructor that creates a Daf setting the {@link #setMasechtaNumber(int) masechta Number} and
	 * {@link #setDaf(int) daf Number}
	 * 
	 * @param masechtaNumber
	 * @param daf
	 */
	public Daf(int masechtaNumber, int daf) {
		this.masechtaNumber = masechtaNumber;
		this.daf = daf;
	}

	/**
	 * Returns the daf (page number) of the Daf Yomi
	 * @return the daf (page number) of the Daf Yomi
	 */
	public int getDaf() {
		return daf;
	}

	/**
	 * sets the daf (page number) of the Daf Yomi
	 * @param daf daf (page number)
	 */
	public void setDaf(int daf) {
		this.daf = daf;
	}

	/**
	 * Returns the transliterated name of the masechta (tractate) of the Daf Yomi. The list of mashechtos is: Berachos,
	 * Shabbos, Eruvin, Pesachim, Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah,
	 * Yevamos, Kesubos, Nedarim, Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin,
	 * Makkos, Shevuos, Avodah Zarah, Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah,
	 * Kinnim, Tamid, Midos and Niddah.
	 * 
	 * @return the transliterated name of the masechta (tractate) of the Daf Yomi such as Berachos.
	 */
	public String getMasechtaTransliterated() {
		return masechtosBavliTransliterated[masechtaNumber];
	}

	/**
	 * Returns the masechta (tractate) of the Daf Yomi in Hebrew, It will return
	 * &#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA; for Berachos.
	 * 
	 * @return the masechta (tractate) of the Daf Yomi in Hebrew, It will return
	 *         &#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA; for Berachos.
	 */
	public String getMasechta() {
		return masechtosBavli[masechtaNumber];
	}
}