/**
 * This package contain classes that represent a <a href="https://en.wikipedia.org/wiki/Hebrew_calendar">Jewish Date/Calendar</a>,
 * and allows conversion between {@link com.kosherjava.zmanim.hebrewcalendar.JewishDate Jewish} and {@link com.ibm.icu.util.GregorianCalendar Gregorian dates}. The main calendar
 * classes {@link com.kosherjava.zmanim.hebrewcalendar.JewishCalendar} and {@link com.kosherjava.zmanim.hebrewcalendar.JewishDate}
 * are based on <a href="http://www.facebook.com/avromf">Avrom Finkelstien's</a> code,
 * refactored to fit the Zmanim API. The parsha and season-based <em>tefila</em> change code was ported by Y. Paritcher from his
 * <a href="https://github.com/yparitcher/libzmanim">libzmanim</a> code.
 * 
 * <h2>Design:</h2>
 * <ul>
 *   <li>{@link com.kosherjava.zmanim.hebrewcalendar.JewishDate} is the base class, allowing the maintenance of an instance of a Gregorian date along with the corresponding Jewish date.</li>
 *   <li>{@link com.kosherjava.zmanim.hebrewcalendar.JewishCalendar} extends JewishDate and adds some methods related to the calendar.</li>
 *   <li>{@link com.kosherjava.zmanim.hebrewcalendar.TefilaRules} is a utility class for various calendar based <em>tefila</em> rules.</li>
 *   <li>{@link com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter} defines the basics for taking a JewishCalendar and formatting the dates.</li>
 *   <li>{@link com.kosherjava.zmanim.hebrewcalendar.YomiCalculator} calculates the {@link com.kosherjava.zmanim.hebrewcalendar.Daf} Yomi Bavli for a given JewishCalendar and {@link com.kosherjava.zmanim.hebrewcalendar.YerushalmiYomiCalculator} does the same
 *   for Yerushalmi Yomi.</li>
 * </ul>
 * @author &copy; Eliyahu Hershfeld 2011 - 2022
 */
package com.kosherjava.zmanim.hebrewcalendar;
