/*
 * Zmanim Java API
 * Copyright © 2011 - 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */

package com.kosherjava.zmanim.hebrewcalendar;

/**
 * An Object representing a <em>daf</em> (page) in the <a href="https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> cycle.
 * 
 * @author © Eliyahu Hershfeld 2011 - 2026
 */
public class Daf {
	/**
	 * See {@link #getMasechtaNumber()} and {@link #setMasechtaNumber(int)}.
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
	private static final String[] masechtosBavli = { "ברכות", "שבת", "עירובין", "פסחים", "שקלים", "יומא", "סוכה", "ביצה", "ראש השנה",
			"תענית", "מגילה", "מועד קטן", "חגיגה", "יבמות", "כתובות", "נדרים", "נזיר", "סוטה", "גיטין", "קידושין", "בבא קמא", "בבא מציעא",
			"בבא בתרא", "סנהדרין", "מכות", "שבועות", "עבודה זרה", "הוריות", "זבחים", "מנחות", "חולין", "בכורות", "ערכין", "תמורה", "כריתות",
			"מעילה", "קינים", "תמיד", "מידות", "נדה" };
	
	/**
	 * See {@link #getYerushalmiMasechtaTransliterated()}.
	 */
	private static String[] masechtosYerushalmiTransliterated = { "Berachos", "Pe'ah", "Demai", "Kilayim", "Shevi'is",
			"Terumos", "Ma'asros", "Ma'aser Sheni", "Chalah", "Orlah", "Bikurim", "Shabbos", "Eruvin", "Pesachim",
			"Beitzah", "Rosh Hashanah", "Yoma", "Sukah", "Ta'anis", "Shekalim", "Megilah", "Chagigah", "Moed Katan",
			"Yevamos", "Kesuvos", "Sotah", "Nedarim", "Nazir", "Gitin", "Kidushin", "Bava Kama", "Bava Metzia",
			"Bava Basra", "Shevuos", "Makos", "Sanhedrin", "Avodah Zarah", "Horayos", "Nidah", "No Daf Today" };
	
	/**
	 * See {@link #getYerushalmiMasechta()}.
	 */
	private static final String[] masechtosYerushalmi = { "ברכות", "פיאה", "דמאי", "כלאים", "שביעית", "תרומות", "מעשרות","מעשר שני",
			"חלה", "עורלה", "ביכורים", "שבת", "עירובין", "פסחים", "ביצה", "ראש השנה", "יומא", "סוכה", "תענית", "שקלים", "מגילה", "חגיגה",
			"מועד קטן", "יבמות", "כתובות", "סוטה", "נדרים", "נזיר", "גיטין", "קידושין", "בבא קמא", "בבא מציעא", "בבא בתרא", "שבועות", "מכות",
			"סנהדרין", "עבודה זרה", "הוריות", "נידה", "אין דף היום" };

	/**
	 * Gets the <em>masechta</em> number of the currently set <em>Daf</em>. The sequence is: Berachos, Shabbos, Eruvin,
	 * Pesachim, Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah, Yevamos, Kesubos,
	 * Nedarim, Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin, Makkos, Shevuos, Avodah
	 * Zarah, Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah, Kinnim, Tamid, Midos and
	 * Niddah.
	 * @return the masechtaNumber.
	 * @see #setMasechtaNumber(int)
	 */
	public int getMasechtaNumber() {
		return masechtaNumber;
	}

	/**
	 * Set the <em>masechta</em> number in the order of the Daf Yomi. The sequence is: Berachos, Shabbos, Eruvin, Pesachim,
	 * Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan, Chagigah, Yevamos, Kesubos, Nedarim,
	 * Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin, Makkos, Shevuos, Avodah Zarah,
	 * Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah, Kinnim, Tamid, Midos and
	 * Niddah.
	 * 
	 * @param masechtaNumber
	 *            the <em>masechta</em> number in the order of the Daf Yomi to set.
	 */
	public void setMasechtaNumber(int masechtaNumber) {
		this.masechtaNumber = masechtaNumber;
	}

	/**
	 * Constructor that creates a Daf setting the {@link #setMasechtaNumber(int) <em>masechta</em> number} and
	 * {@link #setDaf(int) <em>daf</em> number}.
	 * 
	 * @param masechtaNumber the <em>masechta</em> number in the order of the Daf Yomi to set as the current <em>masechta</em>.
	 * @param daf the <em>daf</em> (page) number to set.
	 */
	public Daf(int masechtaNumber, int daf) {
		this.masechtaNumber = masechtaNumber;
		this.daf = daf;
	}

	/**
	 * Returns the <em>daf</em> (page) number of the Daf Yomi.
	 * @return the <em>daf</em> (page) number of the Daf Yomi.
	 */
	public int getDaf() {
		return daf;
	}

	/**
	 * Sets the <em>daf</em> (page) number of the Daf Yomi.
	 * @param daf the <em>daf</em> (page) number.
	 */
	public void setDaf(int daf) {
		this.daf = daf;
	}

	/**
	 * Returns the transliterated name of the <em>masechta</em> (tractate) of the Daf Yomi. The list of <em>masechtos</em>
	 * is: Berachos, Shabbos, Eruvin, Pesachim, Shekalim, Yoma, Sukkah, Beitzah, Rosh Hashana, Taanis, Megillah, Moed Katan,
	 * Chagigah, Yevamos, Kesubos, Nedarim, Nazir, Sotah, Gitin, Kiddushin, Bava Kamma, Bava Metzia, Bava Basra, Sanhedrin,
	 * Makkos, Shevuos, Avodah Zarah, Horiyos, Zevachim, Menachos, Chullin, Bechoros, Arachin, Temurah, Kerisos, Meilah,
	 * Kinnim, Tamid, Midos and Niddah.
	 * 
	 * @return the transliterated name of the <em>masechta</em> (tractate) of the Daf Yomi such as Berachos.
	 * @see #setMasechtaTransliterated(String[])
	 */
	public String getMasechtaTransliterated() {
		return masechtosBavliTransliterated[masechtaNumber];
	}
	
	/**
	 * Setter method to allow overriding of the default list of <em>masechtos</em> transliterated into Latin chars.
	 * The default values use Ashkenazi American English transliteration.
	 * 
	 * @param masechtosBavliTransliterated the list of transliterated Bavli <em>masechtos</em> to set.
	 * @see #getMasechtaTransliterated()
	 */
	public void setMasechtaTransliterated(String[] masechtosBavliTransliterated) {
		Daf.masechtosBavliTransliterated = masechtosBavliTransliterated;
	}

	/**
	 * Returns the <em>masechta</em> (tractate) of the Daf Yomi in Hebrew. The list is in the following format<br>
	 * <code>["ברכות", "שבת", "עירובין", "פסחים", "שקלים", "יומא", "סוכה", "ביצה", "ראש השנה", "תענית", "מגילה", "מועד קטן", "חגיגה", "יבמות", "כתובות", "נדרים","נזיר", "סוטה", "גיטין", 
	 * "קידושין", "בבא קמא", "בבא מציעא", "בבא בתרא", "סנהדרין", "מכות", "שבועות", "עבודה זרה", "הוריות", "זבחים", "מנחות", "חולין", "בכורות", "ערכין", "תמורה", "כריתות", "מעילה", 
	 * "קינים", "תמיד", "מידות", "נדה"]</code>.
	 * 
	 * @return the <em>masechta</em> (tractate) of the Daf Yomi in Hebrew. As an example, it will return ברכות for Berachos.
	 */
	public String getMasechta() {
		return masechtosBavli[masechtaNumber];
	}

	/**
	 * Returns the transliterated name of the <em>masechta</em> (tractate) of the Daf Yomi in Yerushalmi. The list of
	 * <em>masechtos</em> is:
	 * Berachos, Pe'ah, Demai, Kilayim, Shevi'is, Terumos, Ma'asros, Ma'aser Sheni, Chalah, Orlah, Bikurim, 
	 * Shabbos, Eruvin, Pesachim, Beitzah, Rosh Hashanah, Yoma, Sukah, Ta'anis, Shekalim, Megilah, Chagigah, 
	 * Moed Katan, Yevamos, Kesuvos, Sotah, Nedarim, Nazir, Gitin, Kidushin, Bava Kama, Bava Metzia,
	 * Bava Basra, Shevuos, Makos, Sanhedrin, Avodah Zarah, Horayos, Nidah and No Daf Today.
	 * 
	 * @return the transliterated name of the <em>masechta</em> (tractate) of the Daf Yomi such as Berachos.
	 */
	public String getYerushalmiMasechtaTransliterated() {
		return masechtosYerushalmiTransliterated[masechtaNumber];
	}
	
	/**
	 * Setter method to allow overriding of the default list of Yerushalmi <em>masechtos</em> transliterated into into Latin chars.
	 * The default uses Ashkenazi American English transliteration.
	 * 
	 * @param masechtosYerushalmiTransliterated the list of transliterated Yerushalmi <em>masechtos</em> to set.
	 */
	public void setYerushalmiMasechtaTransliterated(String[] masechtosYerushalmiTransliterated) {
		Daf.masechtosYerushalmiTransliterated = masechtosYerushalmiTransliterated;
	}
	
	/**
	 * Getter method to allow retrieving the list of Yerushalmi <em>masechtos</em> transliterated into into Latin chars.
	 * The default uses Ashkenazi American English transliteration.
	 * 
	 * @return the array of transliterated <em>masechta</em> (tractate) names of the Daf Yomi Yerushalmi.
	 */
	public static String[] getYerushalmiMasechtosTransliterated() {
		return masechtosYerushalmiTransliterated;
	}
	
	/**
	 * Getter method to allow retrieving the list of Yerushalmi <em>masechtos</em>.
	 * 
	 * @return the array of Hebrew <em>masechta</em> (tractate) names of the Daf Yomi Yerushalmi.
	 */
	public static String[] getYerushalmiMasechtos() {
		return masechtosYerushalmi;
	}

	/**
	 * Returns the Yerushalmi <em>masechta</em> (tractate) of the Daf Yomi in Hebrew. As an example, it will return ברכות for Berachos.
	 * 
	 * @return the Yerushalmi <em>masechta</em> (tractate) of the Daf Yomi in Hebrew. As an example, it will return ברכות for Berachos.
	 */
	public String getYerushalmiMasechta() {
		return masechtosYerushalmi[masechtaNumber];
	}
}
