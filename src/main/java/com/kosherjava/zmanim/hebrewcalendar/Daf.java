/*
 * Zmanim Java API
 * Copyright (C) 2011 - 2020 Eliyahu Hershfeld
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

/**
 * An Object representing a Daf in the Daf Yomi cycle.
 * 
 * @author &copy; Eliyahu Hershfeld 2011 - 2020
 */
public class Daf {
	/**
	 * {@link #getMasechtaNumber()} and {@link #setMasechtaNumber(int)}.
	 */
	private int masechtaNumber;
	
	/**
	 * See {@link #getDaf()} and {@link #setDaf(int)}.
	 */
	private int daf;

	/**
	 * See {@link #getMasechtaTransliterated()} and {@link #setMasechtaTransliterated(String[])}.
	 */
	private static String[] masechtosBavliTransliterated = { "Berachos", "Shabbos", "Eruvin", "Pesachim", "Shekalim",
			"Yoma", "Sukkah", "Beitzah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah", "Yevamos",
			"Kesubos", "Nedarim", "Nazir", "Sotah", "Gitin", "Kiddushin", "Bava Kamma", "Bava Metzia", "Bava Basra",
			"Sanhedrin", "Makkos", "Shevuos", "Avodah Zarah", "Horiyos", "Zevachim", "Menachos", "Chullin", "Bechoros",
			"Arachin", "Temurah", "Kerisos", "Meilah", "Kinnim", "Tamid", "Midos", "Niddah" };

	/**
	 * See {@link #getMasechta()}.
	 */
	private static final String[] masechtosBavli = { "\u05D1\u05E8\u05DB\u05D5\u05EA", "\u05E9\u05D1\u05EA",
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
			"\u05E7\u05D9\u05E0\u05D9\u05DD", "\u05EA\u05DE\u05D9\u05D3", "\u05DE\u05D9\u05D3\u05D5\u05EA",
			"\u05E0\u05D3\u05D4" };
	
	/**
	 * See {@link #getYerushlmiMasechtaTransliterated()}.
	 */
	private static String[] masechtosYerushalmiTransliterated = { "Berachos", "Pe'ah", "Demai", "Kilayim", "Shevi'is",
			"Terumos", "Ma'asros", "Ma'aser Sheni", "Chalah", "Orlah", "Bikurim", "Shabbos", "Eruvin", "Pesachim",
			"Beitzah", "Rosh Hashanah", "Yoma", "Sukah", "Ta'anis", "Shekalim", "Megilah", "Chagigah", "Moed Katan",
			"Yevamos", "Kesuvos", "Sotah", "Nedarim", "Nazir", "Gitin", "Kidushin", "Bava Kama", "Bava Metzia",
			"Bava Basra", "Sanhedrin", "Makos", "Shevuos", "Avodah Zarah", "Horayos", "Nidah", "No Daf Today" };
	
	/**
	 * See {@link #getYerushalmiMasechta()}.
	 */
	private static final String[] masechtosYerushlmi = { "\u05d1\u05e8\u05db\u05d5\u05ea","\u05e4\u05d9\u05d0\u05d4",
			"\u05d3\u05de\u05d0\u05d9","\u05db\u05dc\u05d0\u05d9\u05d9\u05dd","\u05e9\u05d1\u05d9\u05e2\u05d9\u05ea",
			"\u05ea\u05e8\u05d5\u05de\u05d5\u05ea","\u05de\u05e2\u05e9\u05e8\u05d5\u05ea","\u05de\u05e2\u05e9\u05e8 \u05e9\u05e0\u05d9",
			"\u05d7\u05dc\u05d4","\u05e2\u05d5\u05e8\u05dc\u05d4","\u05d1\u05d9\u05db\u05d5\u05e8\u05d9\u05dd",
			"\u05e9\u05d1\u05ea","\u05e2\u05d9\u05e8\u05d5\u05d1\u05d9\u05df","\u05e4\u05e1\u05d7\u05d9\u05dd",
			"\u05d1\u05d9\u05e6\u05d4","\u05e8\u05d0\u05e9 \u05d4\u05e9\u05e0\u05d4","\u05d9\u05d5\u05de\u05d0",
			"\u05e1\u05d5\u05db\u05d4","\u05ea\u05e2\u05e0\u05d9\u05ea","\u05e9\u05e7\u05dc\u05d9\u05dd","\u05de\u05d2\u05d9\u05dc\u05d4",
			"\u05d7\u05d2\u05d9\u05d2\u05d4","\u05de\u05d5\u05e2\u05d3 \u05e7\u05d8\u05df","\u05d9\u05d1\u05de\u05d5\u05ea",
			"\u05db\u05ea\u05d5\u05d1\u05d5\u05ea","\u05e1\u05d5\u05d8\u05d4","\u05e0\u05d3\u05e8\u05d9\u05dd","\u05e0\u05d6\u05d9\u05e8",
			"\u05d2\u05d9\u05d8\u05d9\u05df","\u05e7\u05d9\u05d3\u05d5\u05e9\u05d9\u05df","\u05d1\u05d1\u05d0 \u05e7\u05de\u05d0",
			"\u05d1\u05d1\u05d0 \u05de\u05e6\u05d9\u05e2\u05d0","\u05d1\u05d1\u05d0 \u05d1\u05ea\u05e8\u05d0",
			"\u05e9\u05d1\u05d5\u05e2\u05d5\u05ea","\u05de\u05db\u05d5\u05ea","\u05e1\u05e0\u05d4\u05d3\u05e8\u05d9\u05df",
			"\u05e2\u05d1\u05d5\u05d3\u05d4 \u05d6\u05e8\u05d4","\u05d4\u05d5\u05e8\u05d9\u05d5\u05ea","\u05e0\u05d9\u05d3\u05d4",
			"\u05d0\u05d9\u05df \u05d3\u05e3 \u05d4\u05d9\u05d5\u05dd" };

	/**
	 * gets the masechta number of the currently set Daf. The sequence is: Berachos, Shabbos, Eruvin, Pesachim,
	 * Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah, Yevamos, Kesubos, Nedarim,
	 * Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin, Makkos, Shevuos, Avodah Zarah,
	 * Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah, Kinnim, Tamid, Midos and
	 * Niddah.
	 * @return the masechtaNumber
	 * @see #setMasechtaNumber(int)
	 */
	public int getMasechtaNumber() {
		return masechtaNumber;
	}

	/**
	 * Set the masechta number in the order of the Daf Yomi. The sequence is: Berachos, Shabbos, Eruvin, Pesachim,
	 * Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah, Yevamos, Kesubos, Nedarim,
	 * Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin, Makkos, Shevuos, Avodah Zarah,
	 * Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah, Kinnim, Tamid, Midos and
	 * Niddah.
	 * 
	 * @param masechtaNumber
	 *            the masechtaNumber in the order of the Daf Yomi to set
	 */
	public void setMasechtaNumber(int masechtaNumber) {
		this.masechtaNumber = masechtaNumber;
	}

	/**
	 * Constructor that creates a Daf setting the {@link #setMasechtaNumber(int) masechta Number} and
	 * {@link #setDaf(int) daf Number}
	 * 
	 * @param masechtaNumber the masechtaNumber in the order of the Daf Yomi to set
	 * @param daf the daf (page) number to set
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
	 * Sets the daf (page number) of the Daf Yomi
	 * @param daf the daf (page) number
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
	 * @see #setMasechtaTransliterated(String[])
	 */
	public String getMasechtaTransliterated() {
		return masechtosBavliTransliterated[masechtaNumber];
	}
	
	/**
	 * Setter method to allow overriding of the default list of masechtos transliterated into into Latin chars. The default
	 * uses Ashkenazi American English transliteration.
	 * 
	 * @param masechtosBavliTransliterated the list of transliterated Bavli masechtos to set.
	 * @see #getMasechtaTransliterated()
	 */
	public void setMasechtaTransliterated(String[] masechtosBavliTransliterated) {
		Daf.masechtosBavliTransliterated = masechtosBavliTransliterated;
	}

	/**
	 * Returns the masechta (tractate) of the Daf Yomi in Hebrew. The list is in the following format<br>
	 * <code>["&#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA;",
	 * "&#x05E9;&#x05D1;&#x05EA;", "&#x05E2;&#x05D9;&#x05E8;&#x05D5;&#x05D1;&#x05D9;&#x05DF;",
	 * "&#x05E4;&#x05E1;&#x05D7;&#x05D9;&#x05DD;", "&#x05E9;&#x05E7;&#x05DC;&#x05D9;&#x05DD;", "&#x05D9;&#x05D5;&#x05DE;&#x05D0;",
	 * "&#x05E1;&#x05D5;&#x05DB;&#x05D4;", "&#x05D1;&#x05D9;&#x05E6;&#x05D4;", "&#x05E8;&#x05D0;&#x05E9; &#x05D4;&#x05E9;&#x05E0;&#x05D4;",
	 * "&#x05EA;&#x05E2;&#x05E0;&#x05D9;&#x05EA;", "&#x05DE;&#x05D2;&#x05D9;&#x05DC;&#x05D4;", "&#x05DE;&#x05D5;&#x05E2;&#x05D3;
	 * &#x05E7;&#x05D8;&#x05DF;", "&#x05D7;&#x05D2;&#x05D9;&#x05D2;&#x05D4;", "&#x05D9;&#x05D1;&#x05DE;&#x05D5;&#x05EA;",
	 * "&#x05DB;&#x05EA;&#x05D5;&#x05D1;&#x05D5;&#x05EA;", "&#x05E0;&#x05D3;&#x05E8;&#x05D9;&#x05DD;","&#x05E0;&#x05D6;&#x05D9;&#x05E8;",
	 * "&#x05E1;&#x05D5;&#x05D8;&#x05D4;", "&#x05D2;&#x05D9;&#x05D8;&#x05D9;&#x05DF;", "&#x05E7;&#x05D9;&#x05D3;&#x05D5;&#x05E9;&#x05D9;&#x05DF;",
	 * "&#x05D1;&#x05D1;&#x05D0; &#x05E7;&#x05DE;&#x05D0;", "&#x05D1;&#x05D1;&#x05D0; &#x05DE;&#x05E6;&#x05D9;&#x05E2;&#x05D0;",
	 * "&#x05D1;&#x05D1;&#x05D0; &#x05D1;&#x05EA;&#x05E8;&#x05D0;", "&#x05E1;&#x05E0;&#x05D4;&#x05D3;&#x05E8;&#x05D9;&#x05DF;",
	 * "&#x05DE;&#x05DB;&#x05D5;&#x05EA;", "&#x05E9;&#x05D1;&#x05D5;&#x05E2;&#x05D5;&#x05EA;", "&#x05E2;&#x05D1;&#x05D5;&#x05D3;&#x05D4;
	 * &#x05D6;&#x05E8;&#x05D4;", "&#x05D4;&#x05D5;&#x05E8;&#x05D9;&#x05D5;&#x05EA;", "&#x05D6;&#x05D1;&#x05D7;&#x05D9;&#x05DD;",
	 * "&#x05DE;&#x05E0;&#x05D7;&#x05D5;&#x05EA;", "&#x05D7;&#x05D5;&#x05DC;&#x05D9;&#x05DF;", "&#x05D1;&#x05DB;&#x05D5;&#x05E8;&#x05D5;&#x05EA;",
	 * "&#x05E2;&#x05E8;&#x05DB;&#x05D9;&#x05DF;", "&#x05EA;&#x05DE;&#x05D5;&#x05E8;&#x05D4;", "&#x05DB;&#x05E8;&#x05D9;&#x05EA;&#x05D5;&#x05EA;",
	 * "&#x05DE;&#x05E2;&#x05D9;&#x05DC;&#x05D4;", "&#x05E7;&#x05D9;&#x05E0;&#x05D9;&#x05DD;", "&#x05EA;&#x05DE;&#x05D9;&#x05D3;",
	 * "&#x05DE;&#x05D9;&#x05D3;&#x05D5;&#x05EA;", "&#x05E0;&#x05D3;&#x05D4;"]</code>.
	 * 
	 * @return the masechta (tractate) of the Daf Yomi in Hebrew, It will return
	 *         &#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA; for Berachos.
	 */
	public String getMasechta() {
		return masechtosBavli[masechtaNumber];
	}

	/**
	 * Returns the transliterated name of the masechta (tractate) of the Daf Yomi in Yerushalmi. The list of mashechtos is: 
	 * Berachos, Pe'ah,  Demai,  Kilayim,  Shevi'is,  Terumos,  Ma'asros,  Ma'aser Sheni,  Chalah,  Orlah,  Bikurim, 
	 * Shabbos, Eruvin,  Pesachim,  Beitzah,  Rosh Hashanah,  Yoma,  Sukah,  Ta'anis,  Shekalim,  Megilah,  Chagigah, 
	 * Moed Katan,  Yevamos,  Kesuvos,  Sotah,  Nedarim,  Nazir,  Gitin,  Kidushin,  Bava Kama,  Bava Metzia,
	 * Bava Basra,  Sanhedrin,  Makos,  Shevuos,  Avodah Zarah,  Horayos,  Nidah and No Daf Today.
	 * 
	 * @return the transliterated name of the masechta (tractate) of the Daf Yomi such as Berachos.
	 */
	public String getYerushlmiMasechtaTransliterated() {
		return masechtosYerushalmiTransliterated[masechtaNumber];
	}
	
	/**
	 * Setter method to allow overriding of the default list of Yerushalmi masechtos transliterated into into Latin chars.
	 * The default uses Ashkenazi American English transliteration.
	 * 
	 * @param masechtosYerushalmiTransliterated the list of transliterated Yerushalmi masechtos to set.
	 */
	public void setYerushlmiMasechtaTransliterated(String[] masechtosYerushalmiTransliterated) {
		Daf.masechtosYerushalmiTransliterated = masechtosYerushalmiTransliterated;
	}

	/**
	 * Returns the Yerushlmi masechta (tractate) of the Daf Yomi in Hebrew, It will return
	 * &#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA; for Berachos.
	 * 
	 * @return the Yerushalmi masechta (tractate) of the Daf Yomi in Hebrew, It will return
	 *         &#x05D1;&#x05E8;&#x05DB;&#x05D5;&#x05EA; for Berachos.
	 */
	public String getYerushalmiMasechta() {
		return masechtosYerushlmi[masechtaNumber];
	}

}
