/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

/**
 * A comprehensive regression net over every zero-argument zman method of {@link ComprehensiveZmanimCalendar}. Because
 * the zmanim here encode particular halachic opinions (rather than a single verifiable astronomical value), these are
 * pinned against the library's <b>current</b> output for a fixed fixture (Lakewood, NJ on 2017-10-17): any future change
 * that shifts a result will be caught. Values that are {@code null} for this date (e.g. the chametz and Kidush Levana
 * times, which only apply on specific days) are asserted to remain {@code null}.
 * <p>
 * The test reflects over the calendar's public {@code get*}/{@code is*} methods returning {@link Instant} or
 * {@link Duration} and compares each to the golden table below. It also asserts that the <em>set</em> of such methods
 * matches the table, so a newly added or removed zman method is flagged (and its expected value must be recorded here).
 * The golden values were generated from the library itself; regenerate with {@code RegressionValueDumper} if the
 * fixture or a calculation intentionally changes.
 *
 * @author Test coverage
 */
public class ComprehensiveZmanimCalendarTest {

	private static final LocalDate FIXTURE_DATE = LocalDate.of(2017, 10, 17);

	/** Golden output: method name -&gt; expected {@link Instant}/{@link Duration} string, or {@code null}. */
	private static final Object[][] EXPECTED = {
		{ "getAlos120Minutes", "2017-10-17T09:09:51.403184642Z" },
		{ "getAlos120Zmanis", "2017-10-17T09:19:10.103476730Z" },
		{ "getAlos16Point1Degrees", "2017-10-17T09:49:30.219906135Z" },
		{ "getAlos18Degrees", "2017-10-17T09:39:33.523030241Z" },
		{ "getAlos19Degrees", "2017-10-17T09:34:19.165263704Z" },
		{ "getAlos19Point8Degrees", "2017-10-17T09:30:07.461349050Z" },
		{ "getAlos26Degrees", "2017-10-17T08:57:25.521762580Z" },
		{ "getAlos60Minutes", "2017-10-17T10:09:51.403184642Z" },
		{ "getAlos72Minutes", "2017-10-17T09:57:51.403184642Z" },
		{ "getAlos72Zmanis", "2017-10-17T10:03:26.623359895Z" },
		{ "getAlos90Minutes", "2017-10-17T09:39:51.403184642Z" },
		{ "getAlos90Zmanis", "2017-10-17T09:46:50.428403708Z" },
		{ "getAlos96Minutes", "2017-10-17T09:33:51.403184642Z" },
		{ "getAlos96Zmanis", "2017-10-17T09:41:18.363418313Z" },
		{ "getAlosBaalHatanya", "2017-10-17T09:45:19.050001055Z" },
		{ "getBainHashmashosRT13Point24Degrees", "2017-10-17T23:19:17.914866983Z" },
		{ "getBainHashmashosRT13Point5MinutesBefore7Point083Degrees", "2017-10-17T22:33:33.446858525Z" },
		{ "getBainHashmashosRT2Stars", "2017-10-17T22:41:41.407497564Z" },
		{ "getBainHashmashosRT58Point5Minutes", "2017-10-17T23:12:29.201432122Z" },
		{ "getBainHashmashosYereim13Point5Minutes", "2017-10-17T22:00:29.201432122Z" },
		{ "getBainHashmashosYereim16Point875Minutes", "2017-10-17T21:57:06.701432122Z" },
		{ "getBainHashmashosYereim18Minutes", "2017-10-17T21:55:59.201432122Z" },
		{ "getBainHashmashosYereim2Point1Degrees", "2017-10-17T21:58:15.259245832Z" },
		{ "getBainHashmashosYereim2Point8Degrees", "2017-10-17T21:54:28.539077180Z" },
		{ "getBainHashmashosYereim3Point05Degrees", "2017-10-17T21:53:07.410874002Z" },
		{ "getBeginAstronomicalTwilight", "2017-10-17T09:39:33.523030241Z" },
		{ "getBeginCivilTwilight", "2017-10-17T10:42:27.439221886Z" },
		{ "getBeginNauticalTwilight", "2017-10-17T10:10:57.242471901Z" },
		{ "getCandleLighting", "2017-10-17T21:55:59.201432122Z" },
		{ "getChatzosHalayla", "2017-10-18T04:42:06.833038724Z" },
		{ "getChatzosHayom", "2017-10-17T16:42:12.781249470Z" },
		{ "getChatzosHayomAsHalfDay", "2017-10-17T16:41:55.302308378Z" },
		{ "getEndAstronomicalTwilight", "2017-10-17T23:44:09.707296295Z" },
		{ "getEndCivilTwilight", "2017-10-17T22:41:21.435143220Z" },
		{ "getEndNauticalTwilight", "2017-10-17T23:12:49.151356447Z" },
		{ "getFixedLocalChatzosHayom", "2017-10-17T16:56:57.605832Z" },
		{ "getMinchaGedola16Point1Degrees", "2017-10-17T17:16:13.988705527Z" },
		{ "getMinchaGedola30Minutes", "2017-10-17T17:12:12.781249470Z" },
		{ "getMinchaGedola72Minutes", "2017-10-17T17:15:35.627235356Z" },
		{ "getMinchaGedolaAhavatShalom", "2017-10-17T17:13:52.057872856Z" },
		{ "getMinchaGedolaAteretTorah", "2017-10-17T17:00:49.269815683Z" },
		{ "getMinchaGedolaBaalHatanya", "2017-10-17T17:09:55.476611672Z" },
		{ "getMinchaGedolaGRA", "2017-10-17T17:09:35.627235356Z" },
		{ "getMinchaGedolaGRAFixedLocalChatzos30Minutes", "2017-10-17T17:26:57.605832Z" },
		{ "getMinchaGedolaGRAGreaterThan30", "2017-10-17T17:12:12.781249470Z" },
		{ "getMinchaKetana16Point1Degrees", "2017-10-17T20:42:24.958920631Z" },
		{ "getMinchaKetana72Minutes", "2017-10-17T20:37:37.576797224Z" },
		{ "getMinchaKetanaAhavatShalom", "2017-10-17T19:51:21.615491489Z" },
		{ "getMinchaKetanaAteretTorah", "2017-10-17T20:13:27.414333739Z" },
		{ "getMinchaKetanaBaalHatanya", "2017-10-17T19:57:57.216239627Z" },
		{ "getMinchaKetanaGRA", "2017-10-17T19:55:37.576797224Z" },
		{ "getMinchaKetanaGRAFixedLocalChatzosToSunset", "2017-10-17T20:01:53.536598735Z" },
		{ "getMisheyakir10Point2Degrees", "2017-10-17T10:20:22.960720797Z" },
		{ "getMisheyakir11Degrees", "2017-10-17T10:16:11.433376090Z" },
		{ "getMisheyakir11Point5Degrees", "2017-10-17T10:13:34.311221155Z" },
		{ "getMisheyakir12Point85Degrees", "2017-10-17T10:06:30.324568897Z" },
		{ "getMisheyakir7Point65Degrees", "2017-10-17T10:33:46.155182911Z" },
		{ "getMisheyakir9Point5Degrees", "2017-10-17T10:24:03.203710034Z" },
		{ "getPlagAhavatShalom", "2017-10-17T21:10:33.114910614Z" },
		{ "getPlagAlos16Point1DegreesToTzaisGeonim7Point083Degrees", "2017-10-17T21:26:03.735717649Z" },
		{ "getPlagAlosToSunset", "2017-10-17T20:56:26.182523158Z" },
		{ "getPlagHamincha120Minutes", "2017-10-17T22:39:48.389114669Z" },
		{ "getPlagHamincha120MinutesZmanis", "2017-10-17T22:32:26.084716766Z" },
		{ "getPlagHamincha16Point1Degrees", "2017-10-17T22:08:19.529843591Z" },
		{ "getPlagHamincha18Degrees", "2017-10-17T22:16:10.938101909Z" },
		{ "getPlagHamincha19Point8Degrees", "2017-10-17T22:23:38.065797743Z" },
		{ "getPlagHamincha26Degrees", "2017-10-17T22:49:27.074658511Z" },
		{ "getPlagHamincha60Minutes", "2017-10-17T21:52:18.389114669Z" },
		{ "getPlagHamincha72Minutes", "2017-10-17T22:01:48.389114669Z" },
		{ "getPlagHamincha72MinutesZmanis", "2017-10-17T21:57:23.006475925Z" },
		{ "getPlagHamincha90Minutes", "2017-10-17T22:16:03.389114669Z" },
		{ "getPlagHamincha90MinutesZmanis", "2017-10-17T22:10:31.660816241Z" },
		{ "getPlagHamincha96Minutes", "2017-10-17T22:20:48.389114669Z" },
		{ "getPlagHamincha96MinutesZmanis", "2017-10-17T22:14:54.545596351Z" },
		{ "getPlagHaminchaAteretTorah", "2017-10-17T21:33:43.307882929Z" },
		{ "getPlagHaminchaBaalHatanya", "2017-10-17T21:07:57.941084608Z" },
		{ "getPlagHaminchaGRA", "2017-10-17T21:04:48.389114669Z" },
		{ "getPlagHaminchaGRAFixedLocalChatzosToSunset", "2017-10-17T21:07:56.369015426Z" },
		{ "getPolarPlagHaminchaBenIshChai", null },
		{ "getPolarPlagHaminchaTeshuvosVehanhagos", null },
		{ "getPolarStartOfDayTeshuvosVehanhagos", null },
		{ "getPolarSunriseBenIshChai", null },
		{ "getPolarSunsetBenIshChai", null },
		{ "getSamuchLeMinchaKetana16Point1Degrees", "2017-10-17T20:08:03.130551447Z" },
		{ "getSamuchLeMinchaKetana72Minutes", "2017-10-17T20:03:57.251870246Z" },
		{ "getSamuchLeMinchaKetanaGRA", "2017-10-17T19:27:57.251870246Z" },
		{ "getSeaLevelSunrise", "2017-10-17T11:09:51.403184642Z" },
		{ "getSeaLevelSunset", "2017-10-17T22:13:59.201432122Z" },
		{ "getShaahZmanis120Minutes", "PT1H15M20.649853956S" },
		{ "getShaahZmanis120MinutesZmanis", "PT1H13M47.533138608S" },
		{ "getShaahZmanis16Point1Degrees", "PT1H8M43.656738368S" },
		{ "getShaahZmanis18Degrees", "PT1H10M23.015355504S" },
		{ "getShaahZmanis19Point8Degrees", "PT1H11M57.265530111S" },
		{ "getShaahZmanis26Degrees", "PT1H17M23.865385668S" },
		{ "getShaahZmanis60Minutes", "PT1H5M20.649853956S" },
		{ "getShaahZmanis72Minutes", "PT1H7M20.649853956S" },
		{ "getShaahZmanis72MinutesZmanis", "PT1H6M24.779824747S" },
		{ "getShaahZmanis90Minutes", "PT1H10M20.649853956S" },
		{ "getShaahZmanis90MinutesZmanis", "PT1H9M10.812317445S" },
		{ "getShaahZmanis96Minutes", "PT1H11M20.649853956S" },
		{ "getShaahZmanis96MinutesZmanis", "PT1H10M6.156481678S" },
		{ "getShaahZmanisAlos16Point1DegreesToTzaisGeonim3Point7Degrees", "PT1H3M18.553246773S" },
		{ "getShaahZmanisAlos16Point1DegreesToTzaisGeonim3Point8Degrees", "PT1H3M21.1995353S" },
		{ "getShaahZmanisAlos16Point1DegreesToTzaisGeonim7Point083Degrees", "PT1H4M47.768912699S" },
		{ "getShaahZmanisAteretTorah", "PT1H4M12.714839352S" },
		{ "getShaahZmanisBaalHatanya", "PT56M0.579875985S" },
		{ "getShaahZmanisGRA", "PT55M20.649853956S" },
		{ "getSofZmanAchilasChametzBaalHatanya", null },
		{ "getSofZmanAchilasChametzGRA", null },
		{ "getSofZmanAchilasChametzMGA16Point1Degrees", null },
		{ "getSofZmanAchilasChametzMGA72Minutes", null },
		{ "getSofZmanAchilasChametzMGA72MinutesZmanis", null },
		{ "getSofZmanBiurChametzBaalHatanya", null },
		{ "getSofZmanBiurChametzGRA", null },
		{ "getSofZmanBiurChametzMGA16Point1Degrees", null },
		{ "getSofZmanBiurChametzMGA72Minutes", null },
		{ "getSofZmanBiurChametzMGA72MinutesZmanis", null },
		{ "getSofZmanKidushLevana15Days", null },
		{ "getSofZmanKidushLevanaBetweenMoldos", null },
		{ "getSofZmanShma3HoursBeforeChatzos", "2017-10-17T13:42:12.781249470Z" },
		{ "getSofZmanShmaAlos16Point1DegreesToTzaisGeonim7Point083Degrees", "2017-10-17T13:03:53.526644232Z" },
		{ "getSofZmanShmaAlos16Point1ToSunset", "2017-10-17T12:55:37.465287630Z" },
		{ "getSofZmanShmaAteretTorah", "2017-10-17T13:16:04.767877951Z" },
		{ "getSofZmanShmaBaalHatanya", "2017-10-17T13:53:53.447045725Z" },
		{ "getSofZmanShmaGRA", "2017-10-17T13:55:53.352746510Z" },
		{ "getSofZmanShmaGRASunriseToFixedLocalChatzos", "2017-10-17T14:03:24.504508319Z" },
		{ "getSofZmanShmaMGA120Minutes", "2017-10-17T12:55:53.352746510Z" },
		{ "getSofZmanShmaMGA16Point1Degrees", "2017-10-17T13:15:41.190121239Z" },
		{ "getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos", "2017-10-17T13:23:13.912869066Z" },
		{ "getSofZmanShmaMGA18Degrees", "2017-10-17T13:10:42.569096753Z" },
		{ "getSofZmanShmaMGA18DegreesToFixedLocalChatzos", "2017-10-17T13:18:15.564431120Z" },
		{ "getSofZmanShmaMGA19Point8Degrees", "2017-10-17T13:05:59.257939383Z" },
		{ "getSofZmanShmaMGA72Minutes", "2017-10-17T13:19:53.352746510Z" },
		{ "getSofZmanShmaMGA72MinutesToFixedLocalChatzos", "2017-10-17T13:27:24.504508319Z" },
		{ "getSofZmanShmaMGA72MinutesZmanis", "2017-10-17T13:22:40.962834136Z" },
		{ "getSofZmanShmaMGA90Minutes", "2017-10-17T13:10:53.352746510Z" },
		{ "getSofZmanShmaMGA90MinutesToFixedLocalChatzos", "2017-10-17T13:18:24.504508319Z" },
		{ "getSofZmanShmaMGA90MinutesZmanis", "2017-10-17T13:14:22.865356043Z" },
		{ "getSofZmanShmaMGA96Minutes", "2017-10-17T13:07:53.352746510Z" },
		{ "getSofZmanShmaMGA96MinutesZmanis", "2017-10-17T13:11:36.832863347Z" },
		{ "getSofZmanTfila2HoursBeforeChatzos", "2017-10-17T14:42:12.781249470Z" },
		{ "getSofZmanTfilaAteretTorah", "2017-10-17T14:20:17.482717303Z" },
		{ "getSofZmanTfilaBaalHatanya", "2017-10-17T14:49:54.026921710Z" },
		{ "getSofZmanTfilaGRA", "2017-10-17T14:51:14.002600466Z" },
		{ "getSofZmanTfilaGRASunriseToFixedLocalChatzos", "2017-10-17T15:01:15.538282878Z" },
		{ "getSofZmanTfilaMGA120Minutes", "2017-10-17T14:11:14.002600466Z" },
		{ "getSofZmanTfilaMGA16Point1Degrees", "2017-10-17T14:24:24.846859607Z" },
		{ "getSofZmanTfilaMGA18Degrees", "2017-10-17T14:21:05.584452257Z" },
		{ "getSofZmanTfilaMGA19Point8Degrees", "2017-10-17T14:17:56.523469494Z" },
		{ "getSofZmanTfilaMGA72Minutes", "2017-10-17T14:27:14.002600466Z" },
		{ "getSofZmanTfilaMGA72MinutesZmanis", "2017-10-17T14:29:05.742658883Z" },
		{ "getSofZmanTfilaMGA90Minutes", "2017-10-17T14:21:14.002600466Z" },
		{ "getSofZmanTfilaMGA90MinutesZmanis", "2017-10-17T14:23:33.677673488Z" },
		{ "getSofZmanTfilaMGA96Minutes", "2017-10-17T14:19:14.002600466Z" },
		{ "getSofZmanTfilaMGA96MinutesZmanis", "2017-10-17T14:21:42.989345025Z" },
		{ "getSolarMidnight", "2017-10-18T04:42:06.833038724Z" },
		{ "getSunTransit", "2017-10-17T16:42:12.781249470Z" },
		{ "getSunrise", "2017-10-17T11:09:11.571783718Z" },
		{ "getSunset", "2017-10-17T22:14:38.994862349Z" },
		{ "getTchilasZmanKidushLevana3Days", null },
		{ "getTchilasZmanKidushLevana7Days", null },
		{ "getTemporalHour", "PT55M20.649853956S" },
		{ "getTzais120Minutes", "2017-10-18T00:13:59.201432122Z" },
		{ "getTzais120Zmanis", "2017-10-18T00:04:40.501140034Z" },
		{ "getTzais16Point1Degrees", "2017-10-17T23:34:14.100766559Z" },
		{ "getTzais18Degrees", "2017-10-17T23:44:09.707296295Z" },
		{ "getTzais19Point8Degrees", "2017-10-17T23:53:34.647710389Z" },
		{ "getTzais26Degrees", "2017-10-18T00:26:11.906390602Z" },
		{ "getTzais50Minutes", "2017-10-17T23:03:59.201432122Z" },
		{ "getTzais60Minutes", "2017-10-17T23:13:59.201432122Z" },
		{ "getTzais72Minutes", "2017-10-17T23:25:59.201432122Z" },
		{ "getTzais72Zmanis", "2017-10-17T23:20:23.981256869Z" },
		{ "getTzais90Minutes", "2017-10-17T23:43:59.201432122Z" },
		{ "getTzais90Zmanis", "2017-10-17T23:37:00.176213056Z" },
		{ "getTzais96Minutes", "2017-10-17T23:49:59.201432122Z" },
		{ "getTzais96Zmanis", "2017-10-17T23:42:32.241198451Z" },
		{ "getTzaisAteretTorah", "2017-10-17T22:53:59.201432122Z" },
		{ "getTzaisBaalHatanya", "2017-10-17T22:41:21.435143220Z" },
		{ "getTzaisGeonim3Point7Degrees", "2017-10-17T22:29:12.858867416Z" },
		{ "getTzaisGeonim3Point8Degrees", "2017-10-17T22:29:44.614329739Z" },
		{ "getTzaisGeonim4Point42Degrees", "2017-10-17T22:33:01.330786672Z" },
		{ "getTzaisGeonim4Point66Degrees", "2017-10-17T22:34:17.404148028Z" },
		{ "getTzaisGeonim4Point8Degrees", "2017-10-17T22:35:01.761613150Z" },
		{ "getTzaisGeonim5Point95Degrees", "2017-10-17T22:41:05.633594744Z" },
		{ "getTzaisGeonim6Point45Degrees", "2017-10-17T22:43:43.582328133Z" },
		{ "getTzaisGeonim7Point083Degrees", "2017-10-17T22:47:03.446858525Z" },
		{ "getTzaisGeonim7Point67Degrees", "2017-10-17T22:50:08.395485971Z" },
		{ "getTzaisGeonim8Point5Degrees", "2017-10-17T22:54:29.772724455Z" },
		{ "getTzaisGeonim9Point3Degrees", "2017-10-17T22:58:41.421314719Z" },
		{ "getTzaisGeonim9Point75Degrees", "2017-10-17T23:01:02.865384009Z" },
		{ "getZmanMolad", null },
	};

	private ComprehensiveZmanimCalendar fixtureCalendar() {
		ComprehensiveZmanimCalendar calendar = new ComprehensiveZmanimCalendar(TestLocations.lakewood());
		calendar.setLocalDate(FIXTURE_DATE);
		return calendar;
	}

	/** Invokes {@code methodName} on the calendar and returns its value as a comparable string, or {@code null}. */
	private String actual(ComprehensiveZmanimCalendar calendar, String methodName) throws Exception {
		Object value = calendar.getClass().getMethod(methodName).invoke(calendar);
		if (value == null) {
			return null;
		}
		return value instanceof Instant ? ((Instant) value).toString() : ((Duration) value).toString();
	}

	@Test
	public void allZmanimMatchGoldenOutput() throws Exception {
		ComprehensiveZmanimCalendar calendar = fixtureCalendar();

		Map<String, String> expected = new LinkedHashMap<>();
		for (Object[] row : EXPECTED) {
			expected.put((String) row[0], (String) row[1]);
		}

		for (Map.Entry<String, String> entry : expected.entrySet()) {
			assertEquals(entry.getKey(), entry.getValue(), actual(calendar, entry.getKey()));
		}
	}

	/**
	 * Guards the golden table against drift: the set of zero-argument {@code get*}/{@code is*} methods returning an
	 * {@link Instant} or {@link Duration} must exactly match the recorded table. If a zman method is added or removed,
	 * this fails and the table above must be updated (regenerate with {@code RegressionValueDumper}).
	 */
	@Test
	public void goldenTableCoversEveryZmanMethod() {
		TreeSet<String> reflected = new TreeSet<>();
		for (Method method : ComprehensiveZmanimCalendar.class.getMethods()) {
			if (method.getParameterCount() != 0) {
				continue;
			}
			if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				continue;
			}
			Class<?> returnType = method.getReturnType();
			if (returnType == Instant.class || returnType == Duration.class) {
				reflected.add(method.getName());
			}
		}

		TreeSet<String> recorded = new TreeSet<>();
		for (Object[] row : EXPECTED) {
			recorded.add((String) row[0]);
		}

		assertEquals(recorded, reflected);
	}
}
