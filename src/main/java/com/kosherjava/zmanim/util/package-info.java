/**
 * Utility classes for the Zmanim API, including classes to calculate sun-based times, {@link GeoLocation location information}
 * and some {@link ZmanimFormatter formatting}. Four {@link AstronomicalCalculator calculator} implementations are included,
 * offering different trade-offs between accuracy and speed:
 * <ul>
 *   <li>{@link NOAACalculator NOAA} (the default): fast, with very good accuracy, using the short (abbreviated) algorithms from
 *   Jean Meeus's <em>Astronomical Algorithms</em> that NOAA's solar calculator is based on.</li>
 *   <li>{@link MeeusCalculator Meeus}: somewhat more accurate than NOAA, using the fuller solar theory from the same book.
 *   Its speed falls between NOAA and SPA.</li>
 *   <li>{@link SPACalculator SPA}: an implementation of NREL's Solar Position Algorithm, considered the most accurate. It is
 *   significantly slower, which matters only when calculating large numbers of <em>zmanim</em>.</li>
 *   <li>{@link SunTimesCalculator USNO}: the older US Naval Observatory algorithm. It is no longer considered very accurate
 *   and is kept mainly for compatibility.</li>
 * </ul>
 * In practice, the differences between the NOAA, Meeus and SPA calculators are usually a few seconds or less. Atmospheric
 * refraction near the horizon varies with temperature, pressure and weather, and can shift the actual time of sunrise or sunset
 * by a minute or more, so accuracy to the second is largely academic.
 * <p>
 * Rise, set, noon and midnight results are cached by the calculators by default, so repeated <em>zmanim</em> for the same day
 * are not recalculated; see {@link AstronomicalCalculator#setUseResultCache(boolean)}.
 *
 * @author &copy; Eliyahu Hershfeld 2004 - 2026
 */
package com.kosherjava.zmanim.util;
