/**
 * The <a href="https://kosherjava.com">KosherJava</a> Zmanim library is an API for a specialized calendar that can calculate different
 * astronomical times including sunrise and sunset and Jewish <em><a href="https://en.wikipedia.org/wiki/Zmanim">zmanim</a></em> or religious
 * times for prayers and other Jewish religious duties. The calendars are built on {@code java.time}: the date is set with
 * {@link com.kosherjava.zmanim.AstronomicalCalendar#setLocalDate(java.time.LocalDate) setLocalDate(LocalDate)}, and <em>zmanim</em> are
 * returned as {@link java.time.Instant}s (or {@link java.time.Duration}s for temporal hours / <em>sha'os zmaniyos</em<). For non-religious
 * astronomical / solar calculations such as <a href="https://en.wikipedia.org/wiki/Sunrise">sunrise</a>,
 * <a href="https://en.wikipedia.org/wiki/Sunset">sunset</a> and <a href="https://en.wikipedia.org/wiki/Twilight">twilight</a>, use the
 * {@link com.kosherjava.zmanim.AstronomicalCalendar}. The {@link com.kosherjava.zmanim.ZmanimCalendar} contains the most commonly used zmanim or
 * religious time calculations. For a much more extensive list of <em>zmanim</em> use the {@link com.kosherjava.zmanim.ComprehensiveZmanimCalendar}.
 * <b>Note:</b> It is important to read the technical notes on top of the {@link com.kosherjava.zmanim.util.AstronomicalCalculator} documentation.
 * <h2>Disclaimer:</h2> I did my best to get accurate results using standardized astronomical calculations. Please use care when using the library
 * since people rely on the zmanim calculations for <em><a href="https://en.wikipedia.org/wiki/Halakha">halacha lemaaseh</a></em>.
 * @author &copy; Eliyahu Hershfeld 2004 - 2026
 */
package com.kosherjava.zmanim;
