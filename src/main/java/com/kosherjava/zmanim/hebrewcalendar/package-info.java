/**
 * This package contain classes that represent a <a href="https://en.wikipedia.org/wiki/Hebrew_calendar">Jewish Date/Calendar</a>,
 * and allows conversion between {@link JewishDate Jewish} and {@link java.util.GregorianCalendar Gregorian dates}. It is loosely
 * based on <a href="http://www.facebook.com/avromf">Avrom Finkelstien's</a> code, refactored to fit the Zmanim API.
 * 
 * <h3>Design:</h3>
 * <ul>
 *   <li>{@link JewishDate} is the base class, allowing the maintainance of an instance of a Gregorian date along with the corresponding Jewish date.
 *   <li>{@link JewishCalendar} extends JewishDate and adds some methods related to the calendar
 *   <li>{@link HebrewDateFormatter} defines the basics for taking a JewishCalendar and formatting the dates.
 *   <li>{@link YomiCalculator} calculates the {@link Daf} Yomi Bavli for a given JewishCalendar and {@link YerushalmiYomiCalculator}.
 * </ul>
 * @author &copy; Eliyahu Hershfeld 2011 - 2020
 */
package com.kosherjava.zmanim.hebrewcalendar;
