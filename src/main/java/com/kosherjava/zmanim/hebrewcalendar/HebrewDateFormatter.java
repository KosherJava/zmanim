/*
 * Zmanim Java API
 * Copyright © 2011 - 2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.hebrewcalendar;

import java.time.format.DateTimeFormatter;
import java.util.EnumMap;

/**
 * The HebrewDateFormatter class formats a {@link JewishDate}. The class formats Jewish dates, numbers, <em>Daf Yomi</em>
 * (<em>Bavli</em> and <em>Yerushalmi</em>), the <em>Omer</em>, <em>Parshas Hashavua</em> (including the special <em>parshiyos</em>
 * of <em>Shekalim</em>, <em>Zachor</em>, <em>Parah</em> and <em>Hachodesh</em>), <em>Yomim Tovim</em> in Hebrew or Latin chars, and
 * has various settings. Sample full date output includes (using various options):
 * <ul>
 * <li>21 Shevat, 5729</li>
 * <li>כא שבט תשכט</li>
 * <li>כ״א שבט ה׳תשכ״ט</li>
 * <li>כ״א שבט תש״פ or כ״א שבט תש״ף</li>
 * <li>כ׳ שבט ו׳ אלפים</li>
 * </ul>
 * 
 * @see JewishDate
 * @see JewishCalendar
 * @author &copy; Eliyahu Hershfeld 2011 - 2026
 */
public class HebrewDateFormatter {
	
	/**
	 * See {@link #isHebrewFormat()} and {@link #setHebrewFormat(boolean)}.
	 */
	private boolean hebrewFormat = false;
	
	/**
	 * See {@link #isUseLongHebrewYears()} and {@link #setUseLongHebrewYears(boolean)}.
	 */
	private boolean useLonghebrewYears = false;
	
	/**
	 * See {@link #isUseGershGershayim()} and {@link #setUseGershGershayim(boolean)}.
	 */
	private boolean useGershGershayim = true;
	
	/**
	 * See {@link #isLongWeekFormat()} and {@link #setLongWeekFormat(boolean)}.
	 */
	private boolean longWeekFormat = true;
	
	/**
	 * See {@link #isUseFinalFormLetters()} and {@link #setUseFinalFormLetters(boolean)}.
	 */
	private boolean useFinalFormLetters = false;
	
	/**
	 * The internal DateFormat. See {@link #isLongWeekFormat()} and {@link #setLongWeekFormat(boolean)}.
	 */
	private DateTimeFormatter weekFormat;
	
	/**
	 * List of transliterated parshiyos using the default <em>Ashkenazi</em> pronunciation. For information on the format, see
	 * {@link #getTransliteratedParshiosList()}.
	 * 
	 * @see #getTransliteratedParshiosList()
	 * @see #setTransliteratedParshiosList(EnumMap)
	 * @see #HebrewDateFormatter() where the map is initially set.
	 */
	private EnumMap<JewishCalendar.Parsha, String> transliteratedParshaMap;
	
	/**
	 * An {@link EnumMap} of Hebrew <em>parshiyos</em>. The list includes double and special <em>parshiyos</em> and contains<br>
	 * <code>&rlm;בראשית, נח, לך לך,וירא, חיי שרה,תולדות, ויצא, וישלח,וישב, מקץ, ויגש, ויחי,שמות, וארא, בא, בשלח,יתרו, משפטים, תרומה,תצוה, כי תשא, ויקהל,פקודי, 
	 * ויקרא, צו,שמיני, תזריע, מצרע,אחרי מות, קדושים,אמור, בהר, בחקתי,במדבר, נשא, בהעלתך,שלח לך, קרח, חוקת, בלק,פינחס, מטות, מסעי,דברים, ואתחנן, עקב,ראה, שופטים, כי תצא,כי
	 * תבוא, נצבים, וילך,האזינו, וזאת הברכה,ויקהל פקודי, תזריעמצרע, אחרי מותקדושים, בהר בחקתי,חוקת בלק, מטות מסעי,נצבים וילך,
	 * שקלים,זכור, פרה, החדש,שובה,שירה,הגדול,חזון,נחמו</code>
	 */
	private final EnumMap<JewishCalendar.Parsha, String> hebrewParshaMap;
	
	/**
	 * Default constructor sets the {@link EnumMap}s of Hebrew and default transliterated parshiyos.
	 */
	public HebrewDateFormatter() {
		weekFormat = DateTimeFormatter.ofPattern("EEEE");
		transliteratedParshaMap = new EnumMap<>(JewishCalendar.Parsha.class);
		transliteratedParshaMap.put(JewishCalendar.Parsha.NONE, "");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BERESHIS, "Bereshis");
		transliteratedParshaMap.put(JewishCalendar.Parsha.NOACH, "Noach");
		transliteratedParshaMap.put(JewishCalendar.Parsha.LECH_LECHA, "Lech Lecha");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYERA, "Vayera");
		transliteratedParshaMap.put(JewishCalendar.Parsha.CHAYEI_SARA, "Chayei Sara");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TOLDOS, "Toldos");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYETZEI, "Vayetzei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYISHLACH, "Vayishlach");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYESHEV, "Vayeshev");
		transliteratedParshaMap.put(JewishCalendar.Parsha.MIKETZ, "Miketz");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYIGASH, "Vayigash");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYECHI, "Vayechi");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHEMOS, "Shemos");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAERA, "Vaera");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BO, "Bo");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BESHALACH, "Beshalach");
		transliteratedParshaMap.put(JewishCalendar.Parsha.YISRO, "Yisro");
		transliteratedParshaMap.put(JewishCalendar.Parsha.MISHPATIM, "Mishpatim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TERUMAH, "Terumah");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TETZAVEH, "Tetzaveh");
		transliteratedParshaMap.put(JewishCalendar.Parsha.KI_SISA, "Ki Sisa");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYAKHEL, "Vayakhel");
		transliteratedParshaMap.put(JewishCalendar.Parsha.PEKUDEI, "Pekudei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYIKRA, "Vayikra");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TZAV, "Tzav");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHMINI, "Shmini");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TAZRIA, "Tazria");
		transliteratedParshaMap.put(JewishCalendar.Parsha.METZORA, "Metzora");
		transliteratedParshaMap.put(JewishCalendar.Parsha.ACHREI_MOS, "Achrei Mos");
		transliteratedParshaMap.put(JewishCalendar.Parsha.KEDOSHIM, "Kedoshim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.EMOR, "Emor");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BEHAR, "Behar");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BECHUKOSAI, "Bechukosai");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BAMIDBAR, "Bamidbar");
		transliteratedParshaMap.put(JewishCalendar.Parsha.NASSO, "Nasso");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BEHAALOSCHA, "Beha'aloscha");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHLACH, "Sh'lach");
		transliteratedParshaMap.put(JewishCalendar.Parsha.KORACH, "Korach");
		transliteratedParshaMap.put(JewishCalendar.Parsha.CHUKAS, "Chukas");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BALAK, "Balak");
		transliteratedParshaMap.put(JewishCalendar.Parsha.PINCHAS, "Pinchas");
		transliteratedParshaMap.put(JewishCalendar.Parsha.MATOS, "Matos");
		transliteratedParshaMap.put(JewishCalendar.Parsha.MASEI, "Masei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.DEVARIM, "Devarim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAESCHANAN, "Vaeschanan");
		transliteratedParshaMap.put(JewishCalendar.Parsha.EIKEV, "Eikev");
		transliteratedParshaMap.put(JewishCalendar.Parsha.REEH, "Re'eh");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHOFTIM, "Shoftim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.KI_SEITZEI, "Ki Seitzei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.KI_SAVO, "Ki Savo");
		transliteratedParshaMap.put(JewishCalendar.Parsha.NITZAVIM, "Nitzavim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYEILECH, "Vayeilech");
		transliteratedParshaMap.put(JewishCalendar.Parsha.HAAZINU, "Ha'Azinu");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VZOS_HABERACHA, "Vezos Habracha");
		transliteratedParshaMap.put(JewishCalendar.Parsha.VAYAKHEL_PEKUDEI, "Vayakhel Pekudei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.TAZRIA_METZORA, "Tazria Metzora");
		transliteratedParshaMap.put(JewishCalendar.Parsha.ACHREI_MOS_KEDOSHIM, "Achrei Mos Kedoshim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.BEHAR_BECHUKOSAI, "Behar Bechukosai");
		transliteratedParshaMap.put(JewishCalendar.Parsha.CHUKAS_BALAK, "Chukas Balak");
		transliteratedParshaMap.put(JewishCalendar.Parsha.MATOS_MASEI, "Matos Masei");
		transliteratedParshaMap.put(JewishCalendar.Parsha.NITZAVIM_VAYEILECH, "Nitzavim Vayeilech");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHKALIM, "Shekalim");
		transliteratedParshaMap.put(JewishCalendar.Parsha.ZACHOR, "Zachor");
		transliteratedParshaMap.put(JewishCalendar.Parsha.PARA, "Parah");
		transliteratedParshaMap.put(JewishCalendar.Parsha.HACHODESH, "Hachodesh");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHUVA, "Shuva");
		transliteratedParshaMap.put(JewishCalendar.Parsha.SHIRA, "Shira");
		transliteratedParshaMap.put(JewishCalendar.Parsha.HAGADOL, "Hagadol");
		transliteratedParshaMap.put(JewishCalendar.Parsha.CHAZON, "Chazon");
		transliteratedParshaMap.put(JewishCalendar.Parsha.NACHAMU, "Nachamu");
		
		hebrewParshaMap = new EnumMap<>(JewishCalendar.Parsha.class);
		hebrewParshaMap.put(JewishCalendar.Parsha.NONE, "");
		hebrewParshaMap.put(JewishCalendar.Parsha.BERESHIS, "בראשית");
		hebrewParshaMap.put(JewishCalendar.Parsha.NOACH, "נח");
		hebrewParshaMap.put(JewishCalendar.Parsha.LECH_LECHA, "לך לך");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYERA, "וירא");
		hebrewParshaMap.put(JewishCalendar.Parsha.CHAYEI_SARA, "חיי שרה");
		hebrewParshaMap.put(JewishCalendar.Parsha.TOLDOS, "תולדות");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYETZEI, "ויצא");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYISHLACH, "וישלח");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYESHEV, "וישב");
		hebrewParshaMap.put(JewishCalendar.Parsha.MIKETZ, "מקץ");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYIGASH, "ויגש");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYECHI, "ויחי");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHEMOS, "שמות");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAERA, "וארא");
		hebrewParshaMap.put(JewishCalendar.Parsha.BO, "בא");
		hebrewParshaMap.put(JewishCalendar.Parsha.BESHALACH, "בשלח");
		hebrewParshaMap.put(JewishCalendar.Parsha.YISRO, "יתרו");
		hebrewParshaMap.put(JewishCalendar.Parsha.MISHPATIM, "משפטים");
		hebrewParshaMap.put(JewishCalendar.Parsha.TERUMAH, "תרומה");
		hebrewParshaMap.put(JewishCalendar.Parsha.TETZAVEH, "תצוה");
		hebrewParshaMap.put(JewishCalendar.Parsha.KI_SISA, "כי תשא");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYAKHEL, "ויקהל");
		hebrewParshaMap.put(JewishCalendar.Parsha.PEKUDEI, "פקודי");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYIKRA, "ויקרא");
		hebrewParshaMap.put(JewishCalendar.Parsha.TZAV, "צו");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHMINI, "שמיני");
		hebrewParshaMap.put(JewishCalendar.Parsha.TAZRIA, "תזריע");
		hebrewParshaMap.put(JewishCalendar.Parsha.METZORA, "מצרע");
		hebrewParshaMap.put(JewishCalendar.Parsha.ACHREI_MOS, "אחרי מות");
		hebrewParshaMap.put(JewishCalendar.Parsha.KEDOSHIM, "קדושים");
		hebrewParshaMap.put(JewishCalendar.Parsha.EMOR, "אמור");
		hebrewParshaMap.put(JewishCalendar.Parsha.BEHAR, "בהר");
		hebrewParshaMap.put(JewishCalendar.Parsha.BECHUKOSAI, "בחקתי");
		hebrewParshaMap.put(JewishCalendar.Parsha.BAMIDBAR, "במדבר");
		hebrewParshaMap.put(JewishCalendar.Parsha.NASSO, "נשא");
		hebrewParshaMap.put(JewishCalendar.Parsha.BEHAALOSCHA, "בהעלתך");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHLACH, "שלח לך");
		hebrewParshaMap.put(JewishCalendar.Parsha.KORACH, "קרח");
		hebrewParshaMap.put(JewishCalendar.Parsha.CHUKAS, "חוקת");
		hebrewParshaMap.put(JewishCalendar.Parsha.BALAK, "בלק");
		hebrewParshaMap.put(JewishCalendar.Parsha.PINCHAS, "פינחס");
		hebrewParshaMap.put(JewishCalendar.Parsha.MATOS, "מטות");
		hebrewParshaMap.put(JewishCalendar.Parsha.MASEI, "מסעי");
		hebrewParshaMap.put(JewishCalendar.Parsha.DEVARIM, "דברים");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAESCHANAN, "ואתחנן");
		hebrewParshaMap.put(JewishCalendar.Parsha.EIKEV, "עקב");
		hebrewParshaMap.put(JewishCalendar.Parsha.REEH, "ראה");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHOFTIM, "שופטים");
		hebrewParshaMap.put(JewishCalendar.Parsha.KI_SEITZEI, "כי תצא");
		hebrewParshaMap.put(JewishCalendar.Parsha.KI_SAVO, "כי תבוא");
		hebrewParshaMap.put(JewishCalendar.Parsha.NITZAVIM, "נצבים");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYEILECH, "וילך");
		hebrewParshaMap.put(JewishCalendar.Parsha.HAAZINU, "האזינו");
		hebrewParshaMap.put(JewishCalendar.Parsha.VZOS_HABERACHA, "וזאת הברכה");
		hebrewParshaMap.put(JewishCalendar.Parsha.VAYAKHEL_PEKUDEI, "ויקהל פקודי");
		hebrewParshaMap.put(JewishCalendar.Parsha.TAZRIA_METZORA, "תזריע מצרע");
		hebrewParshaMap.put(JewishCalendar.Parsha.ACHREI_MOS_KEDOSHIM, "אחרי מות קדושים");
		hebrewParshaMap.put(JewishCalendar.Parsha.BEHAR_BECHUKOSAI, "בהר בחקתי");
		hebrewParshaMap.put(JewishCalendar.Parsha.CHUKAS_BALAK, "חוקת בלק");
		hebrewParshaMap.put(JewishCalendar.Parsha.MATOS_MASEI, "מטות מסעי");
		hebrewParshaMap.put(JewishCalendar.Parsha.NITZAVIM_VAYEILECH, "נצבים וילך");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHKALIM, "שקלים");
		hebrewParshaMap.put(JewishCalendar.Parsha.ZACHOR, "זכור");
		hebrewParshaMap.put(JewishCalendar.Parsha.PARA, "פרה");
		hebrewParshaMap.put(JewishCalendar.Parsha.HACHODESH, "החדש");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHUVA, "שובה");
		hebrewParshaMap.put(JewishCalendar.Parsha.SHIRA, "שירה");
		hebrewParshaMap.put(JewishCalendar.Parsha.HAGADOL, "הגדול");
		hebrewParshaMap.put(JewishCalendar.Parsha.CHAZON, "חזון");
		hebrewParshaMap.put(JewishCalendar.Parsha.NACHAMU, "נחמו");
	}

	/**
	 * Returns if the {@link #formatDayOfWeek(JewishDate)} will use the long format such as ראשון or short such as א when formatting
	 * the day of week in {@link #isHebrewFormat() Hebrew}.
	 * 
	 * @return the longWeekFormat
	 * @see #setLongWeekFormat(boolean)
	 * @see #formatDayOfWeek(JewishDate)
	 */
	public boolean isLongWeekFormat() {
		return longWeekFormat;
	}

	/**
	 * Setting to control if the {@link #formatDayOfWeek(JewishDate)} will use the long format such as ראשון or short such as א when
	 * formatting the day of week in {@link #isHebrewFormat() Hebrew}.
	 * 
	 * @param longWeekFormat the longWeekFormat to set
	 */
	public void setLongWeekFormat(boolean longWeekFormat) {
		this.longWeekFormat = longWeekFormat;
		if (longWeekFormat) {
			weekFormat = DateTimeFormatter.ofPattern("EEEE");
		} else {
			weekFormat = DateTimeFormatter.ofPattern("EEE");
		}
	}

	/**
	 * The <a href="https://en.wikipedia.org/wiki/Geresh#Punctuation_mark">gersh</a> character is the ׳ char that is similar to a
	 * single quote and is used in formatting Hebrew numbers.
	 */
	private static final String GERESH = "׳";
	
	/**
	 * The <a href="https://en.wikipedia.org/wiki/Gershayim#Punctuation_mark">gershyim</a> character is the ״ char that is similar
	 * to a double quote and is used in formatting Hebrew numbers.
	 */
	private static final String GERSHAYIM = "״";
	
	/**
	 * Transliterated month names that default to <code>["Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan",
	 * "Kislev", "Teves", "Shevat", "Adar", "Adar II", "Adar I" ]</code>.
	 * @see #getTransliteratedMonthList()
	 * @see #setTransliteratedMonthList(String[])
	 */
	private String[] transliteratedMonths = { "Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan",
			"Kislev", "Teves", "Shevat", "Adar", "Adar II", "Adar I" };
	
	/**
	 * The Hebrew omer prefix charachter. It defaults to ב producing בעומר, but can be set to ל to produce לעומר (or any other prefix).
	 * @see #getHebrewOmerPrefix()
	 * @see #setHebrewOmerPrefix(String)
	 */
	private String hebrewOmerPrefix = "ב";

	/**
	 * The default value for formatting "Shabbos" (Saturday) when transliterated.
	 * @see #getTransliteratedShabbosDayOfWeek()
	 * @see #setTransliteratedShabbosDayOfWeek(String)
	 */
	private String transliteratedShabbosDayOfWeek = "Shabbos";

	/**
	 * Returns the day of Shabbos transliterated into Latin chars. The default uses Ashkenazi pronunciation "Shabbos". This can be
	 * overwritten using the {@link #setTransliteratedShabbosDayOfWeek(String)}. It is uesd by {@link #formatDayOfWeek(JewishDate)}.
	 * 
	 * @return the transliteratedShabbos. The default list of months uses Ashkenazi pronunciation "Shabbos".
	 * @see #setTransliteratedShabbosDayOfWeek(String)
	 * @see #formatDayOfWeek(JewishDate)
	 */
	public String getTransliteratedShabbosDayOfWeek() {
		return transliteratedShabbosDayOfWeek;
	}

	/**
	 * Setter to override the default transliterated name of "Shabbos" to alternate spelling such as "Shabbat" used by
	 * the {@link #formatDayOfWeek(JewishDate)}.
	 * 
	 * @param transliteratedShabbos the transliteratedShabbos to set
	 * @see #getTransliteratedShabbosDayOfWeek()
	 * @see #formatDayOfWeek(JewishDate)
	 */
	public void setTransliteratedShabbosDayOfWeek(String transliteratedShabbos) {
		this.transliteratedShabbosDayOfWeek = transliteratedShabbos;
	}

	/**
	 * See {@link #getTransliteratedHolidayList()} and {@link #setTransliteratedHolidayList(String[])}.
	 */
	private String[] transliteratedHolidays = {"Erev Pesach", "Pesach", "Chol Hamoed Pesach", "Pesach Sheni",
			"Erev Shavuos", "Shavuos", "Seventeenth of Tammuz", "Tishah B'Av", "Tu B'Av", "Erev Rosh Hashana",
			"Rosh Hashana", "Fast of Gedalyah", "Erev Yom Kippur", "Yom Kippur", "Erev Succos", "Succos",
			"Chol Hamoed Succos", "Hoshana Rabbah", "Shemini Atzeres", "Simchas Torah", "Erev Chanukah", "Chanukah",
			"Tenth of Teves", "Tu B'Shvat", "Fast of Esther", "Purim", "Shushan Purim", "Purim Katan", "Rosh Chodesh",
			"Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim", "Lag B'Omer", "Shushan Purim Katan",
			"Isru Chag"};

	/**
	 * Returns the array of <em>Yomim Tovim</em> (holidays) transliterated into Latin chars. This is used by the {@link
	 * #formatYomTov(JewishCalendar)} when formatting the <em>Yom Tov</em> String. The default list of months usesnAshkenazi
	 * pronunciation in typical American English spelling. The default list is currently
	 * <code>["Erev Pesach", "Pesach", "Chol Hamoed Pesach", "Pesach Sheni", "Erev Shavuos", "Shavuos", "Seventeenth of Tammuz",
	 * "Tishah B'Av", "Tu B'Av", "Erev Rosh Hashana", "Rosh Hashana", "Fast of Gedalyah", "Erev Yom Kippur", "Yom Kippur", "Erev
	 * Succos", "Succos", "Chol Hamoed Succos", "Hoshana Rabbah", "Shemini Atzeres", "Simchas Torah", "Erev Chanukah", "Chanukah",
	 * "Tenth of Teves", "Tu B'Shvat", "Fast of Esther", "Purim", "Shushan Purim",m"Purim Katan", "Rosh Chodesh", "Yom HaShoah",
	 * "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim", "Lag B'Omer", "Shushan Purim Katan", "Isru Chag"]</code>.
	 * 
	 * @return the array of transliterated <em>Yomim Tovim</em> (holidays). 
	 * @see #setTransliteratedMonthList(String[])
	 * @see #formatYomTov(JewishCalendar)
	 * @see #isHebrewFormat()
	 */
	public String[] getTransliteratedHolidayList() {
		return transliteratedHolidays;
	}

	/**
	 * Sets the array of <em>Yomim Tovim</em> (holidays) transliterated into Latin chars. This is used by the
	 * {@link #formatYomTov(JewishCalendar)} when formatting the <em>Yom Tov</em> String. The list uses the following order and uses
	 * the spelling as follows. 
	 * <code>["Erev Pesach", "Pesach", "Chol Hamoed Pesach", "Pesach Sheni", "Erev Shavuos", "Shavuos", "Seventeenth of Tammuz",
	 * "Tishah B'Av", "Tu B'Av", "Erev Rosh Hashana", "Rosh Hashana", "Fast of Gedalyah", "Erev Yom Kippur", "Yom Kippur", "Erev
	 * Succos", "Succos", "Chol Hamoed Succos", "Hoshana Rabbah", "Shemini Atzeres", "Simchas Torah", "Erev Chanukah", "Chanukah",
	 * "Tenth of Teves", "Tu B'Shvat", "Fast of Esther", "Purim", "Shushan Purim", "Purim Katan", "Rosh Chodesh", "Yom HaShoah",
	 * "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim", "Lag B'Omer", "Shushan Purim Katan", "Isru Chag"]</code>.
	 * 
	 * @param transliteratedHolidays the transliteratedHolidays to set. Ensure that the sequence exactly matches the list returned
	 *         by the default.
	 */
	public void setTransliteratedHolidayList(String[] transliteratedHolidays) {
		this.transliteratedHolidays = transliteratedHolidays;
	}

	/**
	 * Hebrew <em>Yomim Tovim</em> (holidays) array in the following format.<br>
	 * <code>&rlm;["ערב פסח", "פסח", "חול המועד פסח", "פסח שני", "ערב שבועות", "שבועות", "שבעה עשר בתמוז", "תשעה באב", "ט״ו באב", "ערב ראש השנה", "ראש השנה",
	 * "צום גדליה", "ערב יום כיפור", "יום כיפור", "ערב סוכות", "סוכות", "חול המועד סוכות", "הושענא רבה", "שמיני עצרת", "שמחת תורה", "ערב חנוכה", "חנוכה", "עשרה בטבת",
	 * "ט״ו בשבט", "תענית אסתר", "פורים", "שושן פורים", "פורים קטן", "ראש חודש", "יום השואה", "יום הזיכרון", "יום העצמאות", "יום ירושלים", "ל״ג בעומר", "שושן פורים קטן"]</code>
	 */
	private final String[] hebrewHolidays = { "ערב פסח", "פסח",
			"חול המועד פסח",
			"פסח שני", "ערב שבועות",
			"שבועות",
			"שבעה עשר בתמוז",
			"תשעה באב", "ט״ו באב",
			"ערב ראש השנה",
			"ראש השנה", "צום גדליה",
			"ערב יום כיפור",
			"יום כיפור", "ערב סוכות",
			"סוכות",
			"חול המועד סוכות",
			"הושענא רבה",
			"שמיני עצרת",
			"שמחת תורה", "ערב חנוכה",
			"חנוכה", "עשרה בטבת",
			"ט״ו בשבט", "תענית אסתר",
			"פורים", "שושן פורים",
			"פורים קטן", "ראש חודש",
			"יום השואה",
			"יום הזיכרון",
			"יום העצמאות",
			"יום ירושלים",
			"ל״ג בעומר",
			"שושן פורים קטן",
			"אסרו חג"};

	/** The transliterated <em>tekufa</em> names.*/
	private final String[] transliteratedTekufaNames = new String[] {"Tishrei", "Teves", "Nissan", "Tammuz"};
	
	/** The <em>tekufa</em> names.*/
	private final String[] tekufaNames = new String[] {"תשרי", "טבת", "ניסן", "תמוז"};
	
	/**
	 * Formats the <em>Yom Tov</em> (holiday) in Hebrew or transliterated Latin characters.
	 * 
	 * @param jewishCalendar the JewishCalendar
	 * @return the formatted <em>Yom Tov</em> (holiday) or an empty String if the day is not a <em>Yom Tov</em> (holiday).
	 * @see #isHebrewFormat()
	 */
	public String formatYomTov(JewishCalendar jewishCalendar) {
		int index = jewishCalendar.getYomTovIndex();
		if (index == JewishCalendar.CHANUKAH) {
			int dayOfChanukah = jewishCalendar.getDayOfChanukah();
			return hebrewFormat ? (formatHebrewNumber(dayOfChanukah) + " " + hebrewHolidays[index])
					: (transliteratedHolidays[index] + " " + dayOfChanukah);
		}
		return index == -1 ? "" : hebrewFormat ? hebrewHolidays[index] : transliteratedHolidays[index];
	}

	/**
	 * Formats a day as Rosh Chodesh in the format of in the format of ראש חודש שבט or Rosh Chodesh Shevat. If it is not Rosh Chodesh,
	 * an empty <code>String</code> will be returned.
	 * @param jewishCalendar the JewishCalendar
	 * @return The formatted <code>String</code> in the format of ראש חודש שבט or Rosh Chodesh Shevat. If it is not Rosh Chodesh, an
	 *         empty <code>String</code> will be returned.
	 */
	public String formatRoshChodesh(JewishCalendar jewishCalendar) {
		if (!jewishCalendar.isRoshChodesh()) {
			return "";
		}
		int month = jewishCalendar.getJewishMonth();
		if (jewishCalendar.getJewishDayOfMonth() == 30) {
			if (month < JewishCalendar.ADAR || (month == JewishCalendar.ADAR && jewishCalendar.isJewishLeapYear())) {
				month++;
			} else { // roll to Nissan
				month = JewishCalendar.NISSAN;
			}
		}

		// This method is only about formatting, so we shouldn't make any changes to the params passed in...
		jewishCalendar = (JewishCalendar) jewishCalendar.clone();
		jewishCalendar.setJewishMonth(month);
		String formattedRoshChodesh = hebrewFormat ? hebrewHolidays[JewishCalendar.ROSH_CHODESH]
				: transliteratedHolidays[JewishCalendar.ROSH_CHODESH];
		formattedRoshChodesh += " " + formatMonth(jewishCalendar);
		return formattedRoshChodesh;
	}

	/**
	 * Returns if the formatter is set to use Hebrew formatting in the various formatting methods.
	 * 
	 * @return the hebrewFormat
	 * @see #setHebrewFormat(boolean)
	 * @see #format(JewishDate)
	 * @see #formatDayOfWeek(JewishDate)
	 * @see #formatMonth(JewishDate)
	 * @see #formatOmer(JewishCalendar)
	 * @see #formatYomTov(JewishCalendar)
	 */
	public boolean isHebrewFormat() {
		return hebrewFormat;
	}

	/**
	 * Sets the formatter to format in Hebrew in the various formatting methods.
	 * 
	 * @param hebrewFormat <code>true</code> to format in Hebrew.
	 * @see #isHebrewFormat()
	 * @see #format(JewishDate)
	 * @see #formatDayOfWeek(JewishDate)
	 * @see #formatMonth(JewishDate)
	 * @see #formatOmer(JewishCalendar)
	 * @see #formatYomTov(JewishCalendar)
	 */
	public void setHebrewFormat(boolean hebrewFormat) {
		this.hebrewFormat = hebrewFormat;
	}

	/**
	 * Returns the Hebrew Omer prefix. By default it is the letter ב producing בעומר, but it can be set to ל to produce לעומר (or any
	 * other prefix) using the {@link #setHebrewOmerPrefix(String)}.
	 * 
	 * @return the hebrewOmerPrefix
	 * @see #hebrewOmerPrefix
	 * @see #setHebrewOmerPrefix(String)
	 * @see #formatOmer(JewishCalendar)
	 */
	public String getHebrewOmerPrefix() {
		return hebrewOmerPrefix;
	}

	/**
	 * Method to set the Hebrew Omer prefix. By default it is the letter ב producing בעומר, but it can be set to ל to format it לעומר
	 * (or any other prefix).
	 * @param hebrewOmerPrefix the hebrewOmerPrefix to set. You can set it to ל to produce to לעומר.
	 * @see #hebrewOmerPrefix
	 * @see #getHebrewOmerPrefix()
	 * @see #formatOmer(JewishCalendar)
	 */
	public void setHebrewOmerPrefix(String hebrewOmerPrefix) {
		this.hebrewOmerPrefix = hebrewOmerPrefix;
	}
	
	/**
	 * Returns the Hebrew array of months in the order of<br><code>&rlm;["ניסן", "אייר", "סיון", "תמוז", "אב", "אלול", "תשרי", "חשון", "כסלו", "טבת", "שבט", "אדר", 
	 * "אדר ב", "אדר א"]</code>. This list has a length of 14 starting with "ניסן" and ending with 3 variations of Adar -
	 * "אדר", "אדר ב", "אדר א".
	 * @return the array of Hebrew months.
	 * @see #hebrewMonths
	 * @see #setHebrewMonthList(String[])
	 */
	public String[] getHebrewMonthList() {
		return hebrewMonths;
	}
	
	/**
	 * Setter method to allow overriding of the default list of Hebrew month names. This allows changing things such as the default
	 * month name of חשון to מרחשון, etc. This list expects a length of 14 starting with "ניסן" and ending with 3 variations of Adar -
	 * "אדר", "אדר ב", "אדר".
	 * 
	 * @param hebrewMonths the array of Hebrew months beginning in "ניסן" and ending in "אדר", "אדר ב", "אדר א"
	 * @see #getHebrewMonthList()
	 */
	public void setHebrewMonthList(String[] hebrewMonths) {
		if(hebrewMonths.length !=14) {
			throw new IllegalArgumentException("The Hebrew month array must have a length of 14.");
		}
		this.hebrewMonths = hebrewMonths;
	}

	/**
	 * Returns the array of months transliterated into Latin chars. The default list of months uses Ashkenazi pronunciation in
	 * typical American English spelling. This list has a length of 14 with 3 variations for Adar - "Adar", "Adar II", "Adar I".
	 * The array of months beginn in Nissan and end in "Adar", "Adar II", "Adar I". The default list is
	 * <code>["Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves", "Shevat", "Adar",
	 * "Adar II", "Adar I"]</code>.
	 * 
	 * @return the array of 14 month names beginning in Nissan and ending in "Adar", "Adar II", "Adar I".
	 * @see #setTransliteratedMonthList(String[])
	 */
	public String[] getTransliteratedMonthList() {
		return transliteratedMonths;
	}

	/**
	 * Setter method to allow overriding of the default list of months transliterated into Latin chars. The default list uses
	 * Ashkenazi American English transliteration. The array of 14 transliterated month names begin in "Nissan" and end in the
	 * 3 Adar variations - "Adar", "Adar II", "Adar I". The default list is
	 * <code>["Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves", "Shevat", "Adar",
	 * "Adar II", "Adar I"]</code>.
	 * 
	 * @param transliteratedMonths the array of 14 month names beginning in Nissan and ending in "Adar", "Adar II", "Adar I".
	 * @see #getTransliteratedMonthList()
	 */
	public void setTransliteratedMonthList(String[] transliteratedMonths) {
		if(transliteratedHolidays.length !=14) {
			throw new IllegalArgumentException("The transliterated month array must have a length of 14.");
		}
		this.transliteratedMonths = transliteratedMonths;
	}

	/**
	 * List of Hebrew months. The list has* a length of 14 starting with "ניסן" and ending with the 3 variations of Adar -
	 * "אדר", "אדר ב", "אדר א".
	 * 
	 * @see #getHebrewMonthList()
	 * @see #setHebrewMonthList(String[])
	 * @see #formatMonth(JewishDate)
	 */
	private String[] hebrewMonths = { "ניסן", "אייר",
			"סיון", "תמוז", "אב", "אלול",
			"תשרי", "חשון", "כסלו",
			"טבת", "שבט", "אדר", "אדר ב",
			"אדר א" };

	/**
	 * Unicode list of Hebrew days of week in the format of <code>&rlm;["ראשון", "שני", "שלישי", "רביעי", "חמישי", "ששי", "שבת"]</code>
	 */
	private static final String[] hebrewDaysOfWeek = { "ראשון", "שני", "שלישי", "רביעי", "חמישי", "ששי", "שבת" };

	/**
	 * Formats the day of week. If {@link #isHebrewFormat() Hebrew formatting} is set, it will display in the format ראשון etc. If
	 * Hebrew formatting is not in use it will return it in the format of Sunday etc. There are various formatting options that will
	 * affect the output.
	 * 
	 * @param jewishDate the JewishDate Object
	 * @return the formatted day of week
	 * @see #isHebrewFormat()
	 * @see #isLongWeekFormat()
	 */
	public String formatDayOfWeek(JewishDate jewishDate) {
		if (hebrewFormat) {
			if (isLongWeekFormat()) {
				return hebrewDaysOfWeek[jewishDate.getDayOfWeek() - 1];
			} else {
				if (jewishDate.getDayOfWeek() == 7) {
					return formatHebrewNumber(300);
				} else {
					return formatHebrewNumber(jewishDate.getDayOfWeek());
				}
			}
		} else {
			if (jewishDate.getDayOfWeek() == 7) {
				if (isLongWeekFormat()) {
					return getTransliteratedShabbosDayOfWeek();
				} else {
					return getTransliteratedShabbosDayOfWeek().substring(0,3);
				}
			} else {
				return weekFormat.format(jewishDate.getLocalDate());
			}
		}
	}

	/**
	 * Returns whether the class is set to use the Geresh ׳ and Gershayim ״ in formatting Hebrew dates and numbers. When true and
	 * output would look like כ״א שבט תש״כ (or כ״א שבט תש״ך). When set to false, this output would display as כא שבט תשכ.
	 * 
	 * @return true if set to use the Geresh ׳ and Gershayim ״ in formatting Hebrew dates and numbers.
	 */
	public boolean isUseGershGershayim() {
		return useGershGershayim;
	}

	/**
	 * Sets whether to use the Geresh ׳ and Gershayim ״ in formatting Hebrew dates and numbers. The default value is true and output
	 * would look like כ״א שבט תש״כ (or כ״א שבט תש״ך). When set to false, this output would display as כא שבט תשכ (or כא שבט תשך).
	 * Single digit days or month or years such as כ׳ שבט ו׳ אלפים show the use of the Geresh.
	 * 
	 * @param useGershGershayim set this to false to omit the Geresh ׳ and Gershayim ״ in formatting
	 */
	public void setUseGershGershayim(boolean useGershGershayim) {
		this.useGershGershayim = useGershGershayim;
	}

	/**
	 * Returns whether the class is set to use the מנצפ״ך letters when formatting years ending in 20, 40, 50, 80 and 90 to produce
	 * תש״פ if false or תש״ף if true. Traditionally non-final form letters are used, so the year 5780 would be formatted as תש״פ if
	 * the default false is used here. If this returns true, the format תש״ף would be used.
	 * 
	 * @return true if set to use final form letters when formatting Hebrew years. The default value is false.
	 */
	public boolean isUseFinalFormLetters() {
		return useFinalFormLetters;
	}

	/**
	 * When formatting a Hebrew Year, traditionally years ending in 20, 40, 50, 80 and 90 are formatted using non-final form letters
	 * for example תש״פ for the year 5780. Setting this to true (the default is false) will use the final form letters for מנצפ״ך and
	 * will format the year 5780 as תש״ף.
	 * 
	 * @param useFinalFormLetters Set this to true to use final form letters when formatting Hebrew years.
	 */
	public void setUseFinalFormLetters(boolean useFinalFormLetters) {
		this.useFinalFormLetters = useFinalFormLetters;
	}

	/**
	 * Returns whether the class is set to use the thousands digit when formatting a Hebrew Year. Traditionally the thousands digit
	 * is omitted and output for a year such as 5729 (1969 Gregorian) would be calculated as 729 and formatted as תשכ״ט. When set to
	 * true the long format year such,  as ה׳ תשכ״ט for 5729/1969 is returned.
	 * 
	 * @return true if set to use the thousands digit when formatting Hebrew dates and numbers.
	 */
	public boolean isUseLongHebrewYears() {
		return useLonghebrewYears;
	}

	/**
	 * When formatting a Hebrew Year, traditionally the thousands digit is omitted and output for a year such as 5729 (1969 
	 * Gregorian) would be calculated for 729 and format as תשכ״ט. This method allows setting this to true to return the long format
	 * year such as ה׳ תשכ״ט for 5729/1969.
	 * 
	 * @param useLongHebrewYears Set this to true to use the long formatting
	 */
	public void setUseLongHebrewYears(boolean useLongHebrewYears) {
		this.useLonghebrewYears = useLongHebrewYears;
	}
	/**
	 * Formats the Jewish date. If the formatter is set to Hebrew, it will format in the form, "day Month year" for example
	 * כ״א שבט תשכ״ט, and the format "21 Shevat, 5729" if not.
	 * 
	 * @param jewishDate the JewishDate to be formatted
	 * @return the formatted date. If the formatter is set to Hebrew, it will format in the form, "day Month year" as כ״א שבט תשכ״ט,
	 *         and "21 Shevat, 5729" if not.
	 */
	public String format(JewishDate jewishDate) {
		if (isHebrewFormat()) {
			return formatHebrewNumber(jewishDate.getJewishDayOfMonth()) + " " + formatMonth(jewishDate) + " "
					+ formatHebrewNumber(jewishDate.getJewishYear());
		} else {
			return jewishDate.getJewishDayOfMonth() + " " + formatMonth(jewishDate) + ", " + jewishDate.getJewishYear();
		}
	}

	/**
	 * Returns a string of the current Hebrew month formatted as "אדר ב׳" or "Adar II" depending on how {@link #isHebrewFormat()}
	 * is set.
	 * 
	 * @param jewishDate the JewishDate to format
	 * @return the formatted month name formatted as "אדר ב׳" or "Adar II" depending on how {@link #isHebrewFormat()} is set.
	 * @see #isHebrewFormat()
	 * @see #setHebrewFormat(boolean)
	 * @see #getTransliteratedMonthList()
	 * @see #setTransliteratedMonthList(String[])
	 */
	public String formatMonth(JewishDate jewishDate) {
		final int month = jewishDate.getJewishMonth();
		if (isHebrewFormat()) {
			if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR) {
				return hebrewMonths[JewishDate.ADAR_II] + (useGershGershayim ? GERESH : ""); // return Adar I, not Adar in a leap year
			} else if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR_II) {
				return hebrewMonths[JewishDate.ADAR] + (useGershGershayim ? GERESH : "");
			} else {
				return hebrewMonths[month - 1];
			}
		} else {
			if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR) {
				return transliteratedMonths[JewishDate.ADAR_II]; // return Adar I, not Adar in a leap year
			} else {
				return transliteratedMonths[month - 1];
			}
		}
	}

	/**
	 * Returns a String of the Omer day in the form ל״ג בעומר if Hebrew Format is set, or "Omer X" or "Lag B'Omer" if not. An empty
	 * string if there is no Omer this day.
	 * 
	 * @param jewishCalendar the JewishCalendar to be formatted
	 * @return a String of the Omer day in the form or an empty string if there is no Omer this day. The default formatting has a
	 * ב prefix that would output בעומר, but this can be set via the {@link #setHebrewOmerPrefix(String)} method to use a ל and
	 *         output ל״ג לעומר.
	 * @see #isHebrewFormat()
	 * @see #getHebrewOmerPrefix()
	 * @see #setHebrewOmerPrefix(String)
	 */
	public String formatOmer(JewishCalendar jewishCalendar) {
		int omer = jewishCalendar.getDayOfOmer();
		if (omer == -1) {
			return "";
		}
		if (hebrewFormat) {
			return formatHebrewNumber(omer) + " " + hebrewOmerPrefix + "עומר";
		} else {
			if (omer == 33) { // if Lag B'Omer
				return transliteratedHolidays[33];
			} else {
				return "Omer " + omer;
			}
		}
	}

	/**
	 * Returns the kviah in the traditional 3 letter Hebrew format where the first letter represents the day of week of Rosh Hashana,
	 * the second letter represents the lengths of Cheshvan and Kislev ({@link JewishDate#SHELAIMIM Shelaimim} , {@link
	 * JewishDate#KESIDRAN Kesidran} or {@link JewishDate#CHASERIM Chaserim}) and the 3rd letter represents the day of week of Pesach.
	 * For example 5729 (1969) would return בשה (Rosh Hashana on Monday, Shelaimim, and Pesach on Thursday), while 5771 (2011) would
	 * return השג (Rosh Hashana on Thursday, Shelaimim, and Pesach on Tuesday).
	 * 
	 * @param jewishYear the Jewish year
	 * @return the Hebrew String such as בשה for 5729 (1969) and השג for 5771 (2011).
	 */
	public String getFormattedKviah(int jewishYear) {
		JewishDate jewishDate = new JewishDate(jewishYear, JewishDate.TISHREI, 1); // set date to Rosh Hashana
		int kviah = jewishDate.getCheshvanKislevKviah();
		int roshHashanaDayOfWeek = jewishDate.getDayOfWeek();
		String returnValue = formatHebrewNumber(roshHashanaDayOfWeek);
		returnValue += (kviah == JewishDate.CHASERIM ? "ח" : kviah == JewishDate.SHELAIMIM ? "ש" : "כ");
		jewishDate.setJewishDate(jewishYear, JewishDate.NISSAN, 15); // set to Pesach of the given year
		int pesachDayOfWeek = jewishDate.getDayOfWeek();
		returnValue += formatHebrewNumber(pesachDayOfWeek);
		returnValue = returnValue.replaceAll(GERESH, "");// geresh is never used in the kviah format
		// boolean isLeapYear = JewishDate.isJewishLeapYear(jewishYear);
		// for efficiency we can avoid the expensive recalculation of the pesach day of week by adding 1 day to Rosh
		// Hashana for a 353-day year, 2 for a 354-day year, 3 for a 355 or 383-day year, 4 for a 384-day year and 5 for
		// a 385-day year
		return returnValue;
	}

	/**
	 * Formats the <a href="https://en.wikipedia.org/wiki/Daf_Yomi">Daf Yomi</a> Bavli in the format of "עירובין נ״ב" if {@link
	 * #isHebrewFormat()} is set to <code>true</code>, or the transliterated format of "Eruvin 52" if set to <code>false</code>.
	 * @param daf the Daf to be formatted.
	 * @return the formatted daf.
	 */
	public String formatDafYomiBavli(Daf daf) {
		if (hebrewFormat) {
			return daf.getMasechta() + " " + formatHebrewNumber(daf.getDaf());
		} else {
			return daf.getMasechtaTransliterated() + " " + daf.getDaf();
		}
	}
	
	/**
	 * Formats the <a href="https://en.wikipedia.org/wiki/Jerusalem_Talmud#Daf_Yomi_Yerushalmi">Daf Yomi Yerushalmi</a> in the format
	 * of "עירובין נ״ב" in {@link #isHebrewFormat()} is set to <code>true</code>, or the transliterated format of "Eruvin 52" if set to
	 * <code>false</code>.
	 * 
	 * @param daf the Daf to be formatted.
	 * @return the formatted daf.
	 */
	public String formatDafYomiYerushalmi(Daf daf) {
		if (daf == null) {
			if (hebrewFormat) {
				return Daf.getYerushalmiMasechtos()[39];
			} else {
				return Daf.getYerushalmiMasechtosTransliterated()[39];
			}
		}
		if (hebrewFormat) {			
			return daf.getYerushalmiMasechta() + " " + formatHebrewNumber(daf.getDaf());
		} else {
			return daf.getYerushalmiMasechtaTransliterated() + " " + daf.getDaf();
		}
	}

	/**
	 * Formats the Mishna Yomi reading.
	 * @param mishnaYomi the Mishna Yomi to be formatted.
	 * @return the formatted Mishna Yomi.
	 */
	public String formatMishnaYomi(MishnaYomi mishnaYomi) {
		String start = (hebrewFormat ? mishnaYomi.getMasechta() + " " + formatHebrewNumber(mishnaYomi.getStartChapter())
				+ ": " + formatPlainHebrewNumber(mishnaYomi.getStartMishna()) : mishnaYomi.getMasechtaTransliterated() + " "
				+ mishnaYomi.getStartChapter() + ":" + mishnaYomi.getStartMishna());
		if (mishnaYomi.getStartMasechtaNumber() == mishnaYomi.getEndMasechtaNumber()
				&& mishnaYomi.getStartChapter() == mishnaYomi.getEndChapter()) {
			return start + "-" + (hebrewFormat ? formatPlainHebrewNumber(mishnaYomi.getEndMishna()) : mishnaYomi.getEndMishna());
		}
		return start + "-" + (hebrewFormat ? mishnaYomi.getEndMasechta() + " " + formatHebrewNumber(mishnaYomi.getEndChapter())
				+ ": " + formatPlainHebrewNumber(mishnaYomi.getEndMishna()) : mishnaYomi.getEndMasechtaTransliterated() + " "
				+ mishnaYomi.getEndChapter() + ":" + mishnaYomi.getEndMishna());
	}

	/**
	 * Formats the daily Rambam reading.
	 * @param rambamYomi the Rambam Yomi to be formatted.
	 * @return the formatted Rambam Yomi.
	 */
	public String formatRambamYomi(RambamYomi rambamYomi) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rambamYomi.getReadingCount(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			String start = rambamYomi.getStartChapter(i);
			String end = rambamYomi.getEndChapter(i);
			if (hebrewFormat) {
				sb.append(formatRambamHebrewName(rambamYomi.getBookNumber(i)));
				sb.append(start.equals(end) ? " פרק " : " פרקים ");
				sb.append(formatRambamChapter(start));
				if (!start.equals(end)) {
					sb.append("-").append(formatRambamChapter(end));
				}
			} else {
				sb.append(rambamYomi.getName(i)).append(" ").append(start);
				if (!start.equals(end)) {
					sb.append("-").append(end);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Formats the daily Tehillim reading.
	 * @param tehillimYomi the daily Tehillim to be formatted.
	 * @return the formatted daily Tehillim.
	 */
	public String formatDailyTehillim(TehillimYomi tehillimYomi) {
		if (hebrewFormat) {
			return "תהילים " + formatHebrewNumber(tehillimYomi.getStartChapter())
					+ (tehillimYomi.getStartChapter() == tehillimYomi.getEndChapter() ? "" : "-" + formatHebrewNumber(tehillimYomi.getEndChapter()));
		}
		return "Psalms " + tehillimYomi.getStartChapter()
				+ (tehillimYomi.getStartChapter() == tehillimYomi.getEndChapter() ? "" : "-" + tehillimYomi.getEndChapter());
	}

	/**
	 * Formats the Tanach Yomi reading.
	 * @param tanachYomi the Tanach Yomi to be formatted.
	 * @return the formatted Tanach Yomi.
	 */
	public String formatTanachYomi(TanachYomi tanachYomi) {
		if (tanachYomi == null) {
			return "";
		}
		if (hebrewFormat) {
			String[] parts = tanachYomi.getSeder().split("\\.");
			String seder = " ס׳ " + formatHebrewNumber(Integer.parseInt(parts[0]));
			if (parts.length > 1) {
				seder += " " + formatHebrewNumber(Integer.parseInt(parts[1]));
			}
			return tanachYomi.getHebrewBook() + seder;
		}
		return tanachYomi.getBook() + " Seder " + tanachYomi.getSeder();
	}

	/**
	 * Formats the Kitzur Shulchan Aruch Yomi reading.
	 * @param kitzurYomi the Kitzur Shulchan Aruch Yomi to be formatted.
	 * @return the formatted Kitzur Shulchan Aruch Yomi.
	 */
	public String formatKitzurShulchanAruchYomi(KitzurShulchanAruchYomi kitzurYomi) {
		if (kitzurYomi == null) {
			return "";
		}
		if (kitzurYomi.getStart().equals(kitzurYomi.getEnd())) {
			return formatKitzurRef(kitzurYomi.getStart());
		}
		String[] start = kitzurYomi.getStart().split(":");
		String[] end = kitzurYomi.getEnd().split(":");
		if (start.length > 1 && end.length > 1 && start[0].equals(end[0])) {
			return formatKitzurRef(kitzurYomi.getStart()) + "-" + (hebrewFormat ? formatPlainHebrewToken(end[1]) : end[1]);
		}
		return formatKitzurRef(kitzurYomi.getStart()) + (hebrewFormat ? " - " : "-") + formatKitzurRef(kitzurYomi.getEnd());
	}

	/**
	 * Formats the Shemiras HaLashon Yomi reading.
	 * @param shemirasYomi the Shemiras HaLashon Yomi to be formatted.
	 * @return the formatted Shemiras HaLashon Yomi.
	 */
	public String formatShemirasHaLashonYomi(ShemirasHaLashonYomi shemirasYomi) {
		if (shemirasYomi == null) {
			return "";
		}
		if (hebrewFormat) {
			if ("x".equals(shemirasYomi.getSection())) {
				return formatShemirasKlalRef(shemirasYomi.getStart()) + (shemirasYomi.getStart().equals(shemirasYomi.getEnd()) ? ""
						: (sameShemirasKlal(shemirasYomi.getStart(), shemirasYomi.getEnd()) ? "-" + shemirasHalacha(shemirasYomi.getEnd())
								: " - " + formatShemirasKlalRef(shemirasYomi.getEnd())));
			}
			return formatShemirasSection(shemirasYomi.getSection()) + " " + formatShemirasSectionRef(shemirasYomi.getStart())
					+ (shemirasYomi.getStart().equals(shemirasYomi.getEnd()) ? "" : formatShemirasSectionRangeEnd(shemirasYomi.getStart(), shemirasYomi.getEnd()));
		}
		if ("x".equals(shemirasYomi.getSection())) {
			return formatShemirasEnglishKlalRef(shemirasYomi.getStart()) + (shemirasYomi.getStart().equals(shemirasYomi.getEnd()) ? ""
					: (sameShemirasKlal(shemirasYomi.getStart(), shemirasYomi.getEnd()) ? "-" + shemirasHalachaEnglish(shemirasYomi.getEnd())
							: " - " + formatShemirasEnglishKlalRef(shemirasYomi.getEnd())));
		}
		return "Shemirat HaLashon, " + shemirasYomi.getSection() + " " + shemirasYomi.getStart()
				+ (shemirasYomi.getStart().equals(shemirasYomi.getEnd()) ? "" : "-" + shemirasYomi.getEnd());
	}

	private String formatKitzurRef(String ref) {
		String[] parts = ref.split(":");
		if (hebrewFormat) {
			return formatHebrewToken(parts[0]) + (parts.length > 1 ? ": " + formatPlainHebrewToken(parts[1]) : "");
		}
		return ref;
	}

	private String formatRambamHebrewName(int bookNumber) {
		String name = LimudYomiData.RAMBAM_NAMES_HEBREW[bookNumber];
		if (bookNumber < 4 || "סדר התפילה".equals(name)) {
			return name;
		}
		return "הלכות " + name;
	}

	private String formatRambamChapter(String chapter) {
		String[] range = chapter.split("-");
		if (range.length == 2) {
			return formatRambamChapterPart(range[0]) + "-" + formatRambamChapterPart(range[1]);
		}
		return formatRambamChapterPart(chapter);
	}

	private String formatRambamChapterPart(String chapter) {
		String[] parts = chapter.split(":");
		if (parts.length == 2) {
			return formatPlainHebrewNumber(Integer.parseInt(parts[0])) + ":" + formatPlainHebrewNumber(Integer.parseInt(parts[1]));
		}
		return formatPlainHebrewNumber(Integer.parseInt(chapter));
	}

	private String formatShemirasKlalRef(String ref) {
		String[] parts = ref.split("\\.");
		return "כלל " + formatHebrewNumber(Integer.parseInt(parts[0])) + " " + (parts.length > 1 ? formatPlainHebrewToken(parts[1]) : "");
	}

	private String formatShemirasEnglishKlalRef(String ref) {
		String[] parts = ref.split("\\.");
		return "Klal " + parts[0] + " Halacha " + (parts.length > 1 ? parts[1] : "");
	}

	private String formatShemirasSection(String section) {
		if ("Hakdamah".equals(section)) {
			return "הקדמה";
		}
		if ("Shar Hazechira".equals(section)) {
			return "שער הזכירה";
		}
		if ("Shar Hatvuna".equals(section)) {
			return "שער התבונה";
		}
		if ("Shar Hatorah".equals(section)) {
			return "שער התורה";
		}
		if ("Chasimas Hasefer".equals(section)) {
			return "חתימת הספר";
		}
		return section;
	}

	private String formatShemirasSectionRef(String ref) {
		if (ref.indexOf('.') >= 0) {
			return formatShemirasKlalRef(ref);
		}
		return formatPlainHebrewToken(ref);
	}

	private String formatShemirasSectionRangeEnd(String start, String end) {
		if (start.indexOf('.') >= 0 && end.indexOf('.') >= 0) {
			return sameShemirasKlal(start, end) ? "-" + shemirasHalacha(end) : " - " + formatShemirasKlalRef(end);
		}
		return "-" + formatPlainHebrewToken(end);
	}

	private boolean sameShemirasKlal(String start, String end) {
		return start.split("\\.")[0].equals(end.split("\\.")[0]);
	}

	private String shemirasHalacha(String ref) {
		String[] parts = ref.split("\\.");
		return parts.length > 1 ? formatPlainHebrewToken(parts[1]) : "";
	}

	private String shemirasHalachaEnglish(String ref) {
		String[] parts = ref.split("\\.");
		return parts.length > 1 ? parts[1] : "";
	}

	private String formatHebrewToken(String token) {
		if ("E".equals(token)) {
			return "סוף";
		}
		if ("Shmita".equals(token)) {
			return "שמיטה";
		}
		if ("Klalim".equals(token)) {
			return "כללים";
		}
		try {
			return formatHebrewNumber(Integer.parseInt(token));
		} catch (NumberFormatException e) {
			return token;
		}
	}

	private String formatPlainHebrewToken(String token) {
		if ("E".equals(token)) {
			return "סוף";
		}
		if ("Shmita".equals(token)) {
			return "שמיטה";
		}
		if ("Klalim".equals(token)) {
			return "כללים";
		}
		try {
			return formatPlainHebrewNumber(Integer.parseInt(token));
		} catch (NumberFormatException e) {
			return token;
		}
	}

	private String formatPlainHebrewNumber(int number) {
		return formatHebrewNumber(number).replace(GERESH, "").replace(GERSHAYIM, "");
	}

	/**
	 * Returns a Hebrew formatted string of a number. The method can calculate from 0 to 9999.
	 * <ul>
	 * <li>Single digit numbers such as 3, 30 and 100 will be returned with a ׳ (<a
	 * href="http://en.wikipedia.org/wiki/Geresh">Geresh</a>) appended as at the end. For example ג׳, ל׳ and ק׳</li>
	 * <li>multi digit numbers such as 21 and 769 will be returned with a ״ (<a
	 * href="http://en.wikipedia.org/wiki/Gershayim">Gershayim</a>) between the second to last and last letters. For
	 * example כ״א, תשכ״ט</li>
	 * <li>15 and 16 will be returned as ט״ו and ט״ז</li>
	 * <li>Single digit numbers (years assumed) such as 6000 (%1000=0) will be returned as ו׳אלפים</li>
	 * <li>0 will return אפס</li>
	 * </ul>
	 * 
	 * @param number the number to be formatted. It will throw an IllegalArgumentException if the number is &lt; 0 or &gt; 9999.
	 * @return the Hebrew formatted number such as תשכ״ט
	 * @see #isUseFinalFormLetters()
	 * @see #isUseGershGershayim()
	 * @see #isHebrewFormat()
	 * 
	 */
	public String formatHebrewNumber(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("negative numbers can't be formatted");
		} else if (number > 9999) {
			throw new IllegalArgumentException("numbers > 9999 can't be formatted");
		}

		String ALAFIM = "אלפים";
		String EFES = "אפס";

		String[] jHundreds = new String[] { "", "ק", "ר", "ש", "ת", "תק", "תר", "תש", "תת", "תתק" };
		String[] jTens = new String[] { "", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ" };
		String[] jTenEnds = new String[] { "", "י", "ך", "ל", "ם", "ן", "ס", "ע", "ף", "ץ" };
		String[] tavTaz = new String[] { "טו", "טז" };
		String[] jOnes = new String[] { "", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט" };

		if (number == 0) { // do we really need this? Should it be applicable to a date?
			return EFES;
		}
		int shortNumber = number % 1000; // discard thousands
		// next check for all possible single Hebrew digit years
		boolean singleDigitNumber = (shortNumber < 11 || (shortNumber < 100 && shortNumber % 10 == 0) ||
				(shortNumber <= 400 && shortNumber % 100 == 0));
		int thousands = number / 1000; // get # thousands
		StringBuilder sb = new StringBuilder();
		// append thousands to String
		if (number % 1000 == 0) { // in year is 5000, 4000 etc
			sb.append(jOnes[thousands]);
			if (isUseGershGershayim()) {
				sb.append(GERESH);
			}
			sb.append(" ");
			sb.append(ALAFIM); // add # of thousands plus the word "thousand" (override alafim boolean)
			return sb.toString();
		} else if (useLonghebrewYears && number >= 1000) { // if alafim boolean display thousands
			sb.append(jOnes[thousands]);
			if (isUseGershGershayim()) {
				sb.append(GERESH); // append thousands quote
			}
			sb.append(" ");
		}
		number = number % 1000; // remove 1000s
		int hundreds = number / 100; // # of hundreds
		sb.append(jHundreds[hundreds]); // add hundreds to String
		number = number % 100; // remove 100s
		if (number == 15) { // special case 15
			sb.append(tavTaz[0]);
		} else if (number == 16) { // special case 16
			sb.append(tavTaz[1]);
		} else {
			int tens = number / 10;
			if (number % 10 == 0) { // if evenly divisible by 10
				if (!singleDigitNumber) {
					if (isUseFinalFormLetters()) {
						sb.append(jTenEnds[tens]); // years like 5780 will end with a final form ף
					} else {
						sb.append(jTens[tens]); // years like 5780 will end with a regular פ
					}
				} else {
					sb.append(jTens[tens]); // standard letters so years like 5050 will end with a regular nun
				}
			} else {
				sb.append(jTens[tens]);
				number = number % 10;
				sb.append(jOnes[number]);
			}
		}
		if (isUseGershGershayim()) {
			if (singleDigitNumber) {
				sb.append(GERESH); // append single quote
			} else { // append double quote before last digit
				sb.insert(sb.length() - 1, GERSHAYIM);
			}
		}
		return sb.toString();
	}	

	/**
	 * Returns the map of transliterated parshiyos used by this formatter. This list using the default <em>Ashkenazi</em>
	 * pronunciation. This list can be overridden (for <em>Sephardi</em> English transliteration for example) by setting the
	 * {@link #setTransliteratedParshiosList(EnumMap)}. The list includes double and special <em>parshiyos</em> in the following
	 * order and spelling "<em>Bereshis, Noach, Lech Lecha, Vayera, Chayei Sara, Toldos, Vayetzei, Vayishlach, Vayeshev, Miketz,
	 * Vayigash, Vayechi, Shemos, Vaera, Bo, Beshalach, Yisro, Mishpatim, Terumah, Tetzaveh, Ki Sisa, Vayakhel, Pekudei, Vayikra,
	 * Tzav, Shmini, Tazria, Metzora, Achrei Mos, Kedoshim, Emor, Behar, Bechukosai, Bamidbar, Nasso, Beha'aloscha, Sh'lach,
	 * Korach, Chukas, Balak, Pinchas, Matos, Masei, Devarim, Vaeschanan, Eikev, Re'eh, Shoftim, Ki Seitzei, Ki Savo, Nitzavim,
	 * Vayeilech, Ha'Azinu, Vezos Habracha, Vayakhel Pekudei, Tazria Metzora, Achrei Mos Kedoshim, Behar Bechukosai, Chukas Balak,
	 * Matos Masei, Nitzavim Vayeilech, Shekalim, Zachor, Parah, Hachodesh,Shuva, Shira, Hagadol, Chazon, Nachamu</em>".
	 * 
	 * @return the map of transliterated Parshios
	 * @see #setTransliteratedParshiosList(EnumMap)
	 * @see #formatParsha(JewishCalendar)
	 * @see #formatParsha(JewishCalendar.Parsha)
	 */
	public EnumMap<JewishCalendar.Parsha, String> getTransliteratedParshiosList() {
		return transliteratedParshaMap;
	}

	/**
	 * Setter method to allow overriding of the default list of parshiyos transliterated into Latin chars. The
	 * default uses Ashkenazi American English transliteration.
	 * 
	 * @param transliteratedParshaMap the transliterated Parshios as an EnumMap to set
	 * @see #getTransliteratedParshiosList() for information on the format.
	 */
	public void setTransliteratedParshiosList(EnumMap<JewishCalendar.Parsha, String> transliteratedParshaMap) {
		this.transliteratedParshaMap = transliteratedParshaMap;
	}
	
	/**
	 * Returns a String with the name of the current parsha(ios). This method gets the current <em>parsha</em> by calling {@link
	 * JewishCalendar#getParshah()} that does not return a <em>parsha</em> for any non-<em>Shabbos</em> or a <em>Shabbos</em> that
	 * occurs on a <em>Yom Tov</em>, and will return an empty <code>String</code> in those cases. If the class {@link
	 * #isHebrewFormat() is set to format in Hebrew} it will return a <code>String</code> of the current parsha(ios) in Hebrew for
	 * example בראשית or נצבים וילך for a double parsha, or an empty string will be returned if there is not parsha that week. If not set
	 * to Hebrew, it returns a string of the parsha(ios) transliterated into Latin chars. The default uses Ashkenazi pronunciation
	 * in typical American English spelling, for example Bereshis, Nitzavim Vayeilech for a double parsha, An empty string if there
	 * are none.
	 * 
	 * @param jewishCalendar the JewishCalendar Object
	 * @return today's parsha(ios) in Hebrew for example, if the formatter is set to format in Hebrew, returns a string of the current
	 *         parsha(ios) in Hebrew for example בראשית or נצבים וילך, for a double parsha or an empty <code>String</code> if there is
	 *         no parsha that week. If not set to Hebrew, it returns a string of the parsha(ios) transliterated into Latin chars. The
	 *         default uses Ashkenazi pronunciation in typical American English spelling, for example Bereshis, Nitzavim Vayeilech for
	 *         a double parsha, or an empty <code>String</code> if there are none.
	 * @see #formatParsha(JewishCalendar)
	 * @see #isHebrewFormat()
	 * @see JewishCalendar#getParshah()
	 */
	public String formatParsha(JewishCalendar jewishCalendar) {
		JewishCalendar.Parsha parsha =  jewishCalendar.getParshah();
		return formatParsha(parsha);
	}

	/**
	 * Returns a <code>String</code> with the name of the current parsha(ios). This method overloads {@link
	 * #formatParsha(JewishCalendar)} and unlike that method, it will format the <em>parsha</em> passed to this method regardless of
	 * the day of week. This is the way to format a <em>parsha</em> retrieved from calling
	 * {@link JewishCalendar#getUpcomingParshah()}.
	 *
	 * @param parsha a JewishCalendar.Parsha object
	 * @return today's parsha(ios) in Hebrew for example, if the formatter is set to format in Hebrew, returns a <code>String</code>
	 *         of the current parsha(ios) in Hebrew for example בראשית or נצבים וילך for a double parsha, or an empty <code>String</code>
	 *         if there is no parsha that week. If not set to Hebrew, it returns a string of the parsha(ios) transliterated into
	 *         Latin chars. The default uses Ashkenazi pronunciation in typical American English spelling, for example Bereshis,  or
	 *         Nitzavim Vayeilech for a double parsha, or an empty string if there are none.
	 * @see #formatParsha(JewishCalendar)
	 * @see JewishCalendar#getUpcomingParshah()
	 */
	public String formatParsha(JewishCalendar.Parsha parsha) {
		return hebrewFormat ? hebrewParshaMap.get(parsha) : transliteratedParshaMap.get(parsha);
	}
	
	/**
	 * Returns a String with the name of the current special parsha of Shekalim, Zachor, Parah or Hachodesh or an empty String for a
	 * non-special parsha. If the formatter is set to format in Hebrew, it returns a string of the current special parsha in Hebrew,
	 * for example שקלים, זכור, פרה or החדש, or an empty <code>string</code> if the date is not a special parsha. If not set to Hebrew,
	 * it returns a string of the special parsha transliterated into Latin chars. The default uses Ashkenazi pronunciation in typical
	 * American English spelling Shekalim, Zachor, Parah or Hachodesh.
	 * 
	 * @param jewishCalendar the JewishCalendar Object
	 * @return today's special parsha. If the formatter is set to format in Hebrew, returns a string of the current special parsha in
	 *         Hebrew for in the format of שקלים, זכור, פרה or החדש or an empty string if there are none. If not set to Hebrew, it
	 *         returns a string of the special parsha transliterated into Latin chars. The default uses Ashkenazi pronunciation in
	 *         typical American English spelling of Shekalim, Zachor, Parah or Hachodesh. An empty string if there are none.
	 */
	public String formatSpecialParsha(JewishCalendar jewishCalendar) {
		JewishCalendar.Parsha specialParsha =  jewishCalendar.getSpecialShabbos();
		return hebrewFormat ? hebrewParshaMap.get(specialParsha) : transliteratedParshaMap.get(specialParsha);
	}
	
	/**
	 * Returns a the formatted <em>tekufa</em> name if it is the day of the <em>tekufa</em> event, or an empty {@code String} if it
	 * is not.
	 * @param jewishCalendar the {@code JewishCalendar} to format the <em>tekufa</em> name for.
	 * @return a {@code String} with the name of the upcoming tekufa/season, in the format of "תקופת תשרי" if {@link #isHebrewFormat()}
	 *         is set to {@code true}, or "Tekufas Tishrei" if set to {@code false} or an empty string on a day without a
	 *         <em>tekufa</em> event.
	 */
	public String formatTekufaName(JewishCalendar jewishCalendar) {
		double INITIAL_TEKUFA_OFFSET = 12.625;  // the number of days Tekufas Tishrei occurs before JEWISH_EPOCH
		double days = JewishDate.getJewishCalendarElapsedDays(jewishCalendar.getJewishYear()) + jewishCalendar.getDaysSinceStartOfJewishYear() + INITIAL_TEKUFA_OFFSET - 1;  // total days since first Tekufas Tishrei event

		double solarDaysElapsed = days % 365.25;  // total days elapsed since start of solar year
		int currentTekufaNumber = (int) (solarDaysElapsed / 91.3125);  // the current quarter of the solar year
		double tekufaDaysElapsed = solarDaysElapsed % 91.3125;  // the number of days that have passed since a tekufa event
		if (tekufaDaysElapsed > 0 && tekufaDaysElapsed <= 1) {  // if the tekufa happens in the upcoming 24 hours
			return isHebrewFormat() ? "תקופת " + tekufaNames[currentTekufaNumber] : "Tekufas " + transliteratedTekufaNames[currentTekufaNumber];//0 for Tishrei, 1 for Tevet, 2, for Nissan, 3 for Tammuz
		} else {
			return "";
		}
	}
}
