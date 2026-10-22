/**
 * This package contains classes that represent a <a href="https://en.wikipedia.org/wiki/Hebrew_calendar">Jewish Date/Calendar</a>,
 * and allows conversion between {@link JewishDate Jewish} and {@link java.time.LocalDate Gregorian dates}. The main calendar
 * classes {@link JewishCalendar} and {@link JewishDate} are based on <a href="https://www.linkedin.com/in/avromf/">Avrom Finkelstein's</a> code from 2002,
 * refactored to fit the Zmanim API. The parsha and season-based <em>tefila</em> change code was ported by Y. Paritcher from his
 * <a href="https://github.com/yparitcher/libzmanim">libzmanim</a> code.
 *
 * <h2>Design:</h2>
 * <ul>
 *   <li>{@link JewishDate} is the base class, allowing the maintenance of an instance of a Gregorian date along with the corresponding Jewish date.</li>
 *   <li>{@link JewishCalendar} extends JewishDate and adds calendar information: <em>Yamim Tovim</em>, fast days, <em>Rosh Chodesh</em>,
 *   <em>Chanukah</em>, the weekly <em>parsha</em> and special <em>parshiyos</em>, the <em>molad</em>, <em>tekufos</em>,
 *   <em>Birkas Hachamah</em> and <em>Daf Yomi</em>.</li>
 *   <li>{@link TefilaRules} is a utility class for various calendar based <em>tefila</em> rules.</li>
 *   <li>{@link HebrewDateFormatter} formats dates, <em>Yamim Tovim</em>, <em>parshiyos</em> and numbers, in Hebrew or transliterated,
 *   with options such as final-form letters and <em>geresh</em> / <em>gershayim</em>.</li>
 *   <li>{@link YomiCalculator} calculates the {@link Daf} Yomi Bavli for a given JewishCalendar and {@link YerushalmiYomiCalculator} does the same
 *   for Yerushalmi Yomi.</li>
 * </ul>
 *
 * <h2>Settings that change results:</h2>
 * <ul>
 *   <li>{@link JewishCalendar#setInIsrael(boolean)}: Israel or diaspora <em>Yom Tov</em> (one or two days) and <em>parshiyos</em>.</li>
 *   <li>{@link JewishCalendar#setIsMukafChoma(boolean)}: whether <em>Purim</em> is observed on <em>Shushan Purim</em> (walled cities).</li>
 *   <li>{@link JewishCalendar#setUseModernHolidays(boolean)}: whether modern Israeli holidays are included.</li>
 * </ul>
 * Instances are mutable and not thread-safe; use a separate instance per thread.
 *
 * @author &copy; Eliyahu Hershfeld 2011 - 2026
 */
package com.kosherjava.zmanim.hebrewcalendar;
