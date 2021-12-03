/**
 * The <a href="https://kosherjava.com">KosherJava</a> Zmanim library is an API for a specialized calendar that can calculate different
 * astronomical times including sunrise and sunset and Jewish <em><a href="https://en.wikipedia.org/wiki/Zmanim">zmanim</a></em> or religious
 * times for prayers and other Jewish religious duties. These classes extend the {@link java.util.GregorianCalendar} and can therefore use the
 * standard Calendar functionality to change dates etc. For non religious astronomical / solar calculations such as <a href=
 * "https://en.wikipedia.org/wiki/Sunrise">sunrise</a>, <a href="https://en.wikipedia.org/wiki/Sunset">sunset</a> and <a href=
 * "https://en.wikipedia.org/wiki/Twilight">twilight</a>, use the {@link AstronomicalCalendar}. The {@link ZmanimCalendar} contains the most
 * commonly used zmanim or religious time calculations. For a much more extensive list of <em>zmanim</em> use the {@link ComplexZmanimCalendar}.
 * <b>Note:</b> It is important to read the technical notes on top of the {@link com.kosherjava.zmanim.util.AstronomicalCalculator} documentation.
 * <h2>Disclaimer:</h2> I did my best to get accurate results using standardized astronomical calculations. Please use care when using the library
 * since people rely on the zmanim calculations for <em><a href="https://en.wikipedia.org/wiki/Halakha">halacha lemaaseh</a></em>.
 * @author &copy; Eliyahu Hershfeld 2004 - 2021
 */
package com.kosherjava.zmanim;
