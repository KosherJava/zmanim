## 3.1.0 (future)
  * `GeoLocation` Adds support for [eastern and western horizon elevation for sunrise and sunset](https://github.com/KosherJava/zmanim/commit/cb2b1835ae875c5fb2fbd3b35ae18f523ab93bfe).
  * [Significant rewrite of the SPACalculator](https://github.com/KosherJava/zmanim/commit/7cab372113391de800acaa73a12bb2f0e410c762) to more closely match other implementations, while boosting performance.
  * Calculators now [cache sunrise, sunset, noon and midnight results](https://github.com/KosherJava/zmanim/compare/11fd247^...32e514b), which significantly speeds up generating many zmanim. For example, a day’s calendar needs sunrise several times (for sunrise itself, sof zman krias shema GRA, and others); it is now calculated once. Cached results are only reused for identical inputs, so results are exactly the same as without caching. Caching is on by default and can be turned off with `setUseResultCache(false)`.
  * The [SPA](https://github.com/KosherJava/zmanim/commit/11fd247e352a666189ec195ab5e8101f778bdeb8) and [Meeus](https://github.com/KosherJava/zmanim/commit/32e514b2d7ddd4e7b1d306f7125ff66808a511df) calculators are faster, because they now reuse the parts of each day’s calculation that are the same for every zman. The SPA calculator was also brought in line with the reference implementation, and now matches it to within about 2 ms. It also now correctly reports no event on polar days where it previously could return an incorrect time. In addition, SPA no longer evaluates its full, expensive solar-position series at every step of a calculation. It evaluates the series at a few points per day, interpolates between them, and verifies every result against the full calculation, falling back to it when needed. This makes it several times faster than the reference implementation, with no loss of accuracy.
  * `NOAACalculator`, `MeeusCalculator`: [Check solar midnight before reporting no event](https://github.com/KosherJava/zmanim/commit/ac7b4fb50e2a3dce6f052624e4f8327988c79890).
  * `NOAACalculator`, `MeeusCalculator`: [Don't miss zmanim the sun only just reaches](https://github.com/KosherJava/zmanim/commit/0bd78932fe9b42e38ecd5ceab036c90d96fc3d99).
  * `ComprehensiveZmanimCalendar`: Return `null` for zmanim that fall outside the day on very short polar days, instead of an invalid time:
    * `getMinchaGedola30Minutes()` and `getMinchaGedolaGRAFixedLocalChatzos30Minutes()` when the result is after sunset (also avoids a `NullPointerException` during polar night).
    * `getSofZmanShma3HoursBeforeChatzos()` and `getSofZmanTfila2HoursBeforeChatzos()` when the result is before sunrise.
    * `getFixedLocalChatzosHayom()` when fixed local chatzos is before sunrise or after sunset, along with the half-day zmanim based on it.
    * `getBainHashmashosYereim18Minutes()`, `getBainHashmashosYereim16Point875Minutes()` and `getBainHashmashosYereim13Point5Minutes()` when the result is before plag hamincha GRA.
    * `getMinchaKetanaAteretTorah()`, `getPlagHaminchaAteretTorah()`, `getMinchaKetanaAhavatShalom()` and `getPlagAhavatShalom()` when the result is after sunset.
  * `ComprehensiveZmanimCalendar`: `getMinchaGedolaGreaterThan30()` and `getMinchaGedolaAhavatShalom()` fall back to the regular mincha gedola when 30 minutes after chatzos is after sunset.
  * `ZmanimCalendar`
    * `getCandleLighting()` returns `null` when candle lighting would be before plag hamincha GRA (very short winter days).
    * `isAssurBemelacha()` now throws a descriptive `IllegalStateException` when a day boundary it needs (sunset on erev Shabbos/Yom Tov, tzais on Shabbos/Yom Tov) can't be determined, such as in polar regions, instead of a `NullPointerException`. Results are unchanged wherever the boundaries exist.
  * `ZmanimFormatter`: Fix a thread-safety issue in duration formatting (shared static `DecimalFormat` instances are now per-instance).

## [3.0.0](https://github.com/KosherJava/zmanim/compare/2.5.0...3.0.0) (2026-10-21)
### Includes breaking changes
* Remove deprecated methods flagged for removal, including
  * `ComprehensiveZmanimcalendar` class
    * `getSofZmanShmaKolEliyahu()`
    * `getTzaisGeonim3Point65Degrees()` - was prior to 13.5 minutes in Yerushalayim at the equinox/equilux.
    * `getTzaisGeonim3Point676Degrees()` - was prior to 13.5 minutes in Yerushalayim at the equinox/equilux.
    * `getSofZmanTfilahAteretTorah()` - use the `getSofZmanTfilaAteretTorah()`
    * `getSofZmanShmaFixedLocal()`
    * `getSofZmanTfilaFixedLocal()`
    * `getMinchaGedolaBaalHatanyaGreaterThan30()` should now be calculated via `ZmanimCalendar.getMinchaGedolaGreaterThan30(Instant minchaGedolaBaalHatanya)`
    * `getFixedLocalChatzosBasedZmanim(Date, Date, double)`
      * was replaced by the more generic [`ZmanimCalendar.getHalfDayBasedZman(Date, Date, double)`](https://github.com/KosherJava/zmanim/commit/b4983499e6e938bf3ee84661d3fcbf98dc4da438) method
    * Move `getSunriseBaalHatanya()` and `getSunsetBaalHatanya()` to the parent `ZmanimCalendar` class.
    * various mispelled [`getBainHasmashos()`](https://github.com/KosherJava/zmanim/commit/838ed7f98f526bd5885f2bc865ca450ba6f7123b) based zmanim (use the properly spelled getBainHasmashos versions
  * `JewishCalendar` class
    * `isVeseinTalUmatarStartDate()` - use `TefilaRules.isVeseinTalUmatarStartDate()`
    * `isVeseinTalUmatarStartingTonight()` - use `TefilaRules.isVeseinTalUmatarStartingTonight()`
    * `isVeseinTalUmatarRecited()` - use `TefilaRiles.isVeseinTalUmatarRecited()`
    * `isVeseinBerachaRecited()` - use `TefilaRules.isVeseinBerachaRecited()`
    * `isMashivHaruachStartDate` - use `TefilaRules.isMashivHaruachStartDate()`
    * `isMashivHaruachEndDate()` - use `TefilaRules.isMashivHaruachEndDate()`
    * `isMashivHaruachRecited()` - use `TefilaRules.isMashivHaruachRecited()`
    * `isMoridHatalRecited()` - use `TefilaRules.isMoridHatalRecited()`
* Remove deprecated redundant [GeoLocationUtils](https://github.com/KosherJava/zmanim/blob/master/src/main/java/com/kosherjava/zmanim/util/GeoLocationUtils.java). Use the GeoLocation class instead.
* Remove the com.kosherjava.util.Time class. Now that Sha'os zmaniyos use the java.time.Duration class, this was obsolete.
* UTF-8 is here. A number of clases have Hebrew in the documentation and code (especially the HebrewDateFormatter class).
* Move some methods from `ComprehensiveZmanimCalendar` to the parent `ZaminCalendar` classes.
  * `getShaahZmanis72Minutes()`
  * `getAlos16Point1Degrees()`
  * `getSofZmanShmaMGA72Minutes()`
  * `getSofZmanTfilaMGA72Minutes()`
  * `getTzaisGeonim8Point5Degrees()`
  * `getZmanisBasedOffset(double)`
* `ZmanimCalendar` class rename some generically named method names to be more specific. The actual zmanim were not changed.
    * rename `getTzais()` -> `getTzaisGeonim8Point5Degrees()`
    * `getAlosHashachar()` -> `getAlos16Point1Degrees()`
    * `getSofZmanShmaMGA()` -> `getSofZmanShmaMGA72Minutes()`
    * `getSofZmanTfilaMGA()` -> `getSofZmanTfilaMGA72Minutes()`
    * `getMinchaGedola()` -> `getMinchaGedolaGRA()`
    * `getMinchaKetana()` -> `getMinchaKetanaGRA()`
    * `getPlagHamincha()` -> `getPlagHaminchaGRA()`
    * `getShaahZmanisMGA() ` -> `getShaahZmanis72Minutes()`
    * `getAlos72()` -> `getAlos72Minutes()`
    * `getTzais72()` -> `getTzais72Minutes()`
    * `getShaahZmanisGra()` -> `getShaahZmanisGRA()`
    * `getAlos120()` -> `getAlos120Minutes()`
    * `getAlos96()` -> `getAlos96Minutes()`
    * `getAlos90()` -> `getAlos90Minutes()`
    * `getAlos72()` -> `getAlos72Minutes()`
    * `getAlos60()` -> `getAlos60Minutes()`
    * `getTzais50()` -> `getTzais50Minutes()`
    * `getTzais60()` -> `getTzais60Minutes()`
    * `getTzais72()` -> `getTzais72Minutes()`
    * `getTzais90()` -> `getTzais90Minutes()`
    * `getTzais96()` -> `getTzais96Minutes()`
    * `getTzais120()` -> `getTzais120Minutes()`
    * `getChatzos()` -> `getChatzosHayom()`
    * `getChatzosAsHalfDay()` -> `getChatzosHayomAsHalfDay()`
    * `isAssurBemlacha()` -> `isAssurBemelacha()`
* Rename some classes the confusingly named `ComplexZmanimCalendar` to `ComrehensiveZmanimCalendar`.
* Move "legacy" classes to `java.time` equivelants
  * All zmanim now return `Instant`s insead of `Date` objects.
  * Replace `SimpledateFormat` with `DateTimeFormatter`
  * `Calendar` and `GregorianCalendar` were replaced with `LocalDate` or `ZonedDateTime`.
  * `TimeZone` was replace with `ZoneId`
  * `GeoLocation.getLocalMeanTimeOffset()` and `getAntimeridianAdjustment()` now takes in `Instant` parameter.
  * Remove the no longer necessary `NOAACalculator.adjustHourForTimeZone(Calendar)`
* `getSofZman*Chametz*` times will retun null if it is not _Erev Pesach_.
* Move ErevPesach zmanim _calculations_ from the `ComprehensiveZmanimCaledar` to generic methods in the parent `ZmanimCalendar` class. Nothing changed in the signatures of the methods in the `ComprehensivZemanimCalendar` class.
* Increase the minimum supported JRE version from version 8 (the code currently almost certainly works on 6 today) to Java 11 or higher.
* Tweaked logic in `AstronomicalCalendar.getInstantFromTime()` to address issues near the dateline.
* Improve null handling in `ComprehensiveZmanimCalendar.getMoladBasedTime()`
* Add `ComprehensiveZmanimCalendar.getMisheyakir12Point85Degrees()`
* Add  `getMinchaGedolaGreaterThan30(Instant)` that allows it to work with any mincha gedola claculation.
* `ComprehensiveZmanimcalendar` rename some methods.
  * getMinchaGedolaGreaterThan30()`-> `getMinchaGedolaGRAGreaterThan30()`
  * `getShaahZmanisAlos16Point1ToTzais3Point8()` -> `getShaahZmanisAlos16Point1DegreesToTzaisGeonim3Point8Degrees()`
  * `getShaahZmanisAlos16Point1ToTzais3Point7()` -> `getShaahZmanisAlos16Point1DegreesToTzaisGeonim3Point7Degrees()`
  * `getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees()` -> `getSofZmanShmaAlos16Point1DegreesToTzaisGeonim7Point083Degrees()`
  * `getPlagAlos16Point1ToTzaisGeonim7Point083Degrees(`) -> `getPlagAlos16Point1DegreesToTzaisGeonim7Point083Degrees()`
* Change / remove `ComprehensiveZmanimcalendar` zmanim that were too early.
  * `getTzaisGeonim4Point37Degrees()` -> `getTzaisGeonim4Point42Degrees()`.
  * `getTzaisGeonim4Point61Degrees()` -> `getTzaisGeonim4Point66Degrees()`
  * Remove `getTzaisGeonim5Point88Degrees()` since it is a drop too early
* `ZmanimCalendar` [Astronomical Chatzos based changes](https://github.com/KosherJava/zmanim/commit/c523424b327f173d70f024bdf207ccae0413d487):
  * Add `getChatzosHalayla()`
  * Add setting `useAstronomicalChatzos` (defaulted to true) to keep the mistaken compat break introduced in the v2.5.0 release.
  * Add setting `useAstronomicalChatzosForOtherZmanim` (defaulted to false).
  * Add `getChatzosAsHalfDay()` to retain the old behavior of chatzos being halfway between sunrise and sunset.
  * Use `useAstronomicalChatzos` to control if `getChatzos()` returns `getSunTransit()` (astronomical chatzos) or getChatzosAsHalfDay().
  * Add `getHalfDayBasedZman(Date startOfHalfDay, Date endOfHalfDay, double hours)` to allow other zmanim to be impacted by chatzos.
  * Use `useAstronomicalChatzosForOtherZmanim`.
  * `ZmanimCalendar` - add utility method [`getPercentOfShaahZmanisFromDegrees(double degrees, boolean sunset)`](https://github.com/KosherJava/zmanim/commit/60d1f09322835835035afa507ac2dc852f1cb033) to simplify zmaniyos time calculations. This allows calculations of various percentage of the day zmanim calculations.
* Remove code duplication in `NOAACalculator.getUTCSunrise()` and `.getUTCSunset()`
* Use Astronomical Chatzos Halayla (as opposed as halfway between sunset and sunrise or 12 hours after chatzos hayom)
  * `AstronomicalCalculator` - [add `getSunLowerTransit()`](https://github.com/KosherJava/zmanim/commit/a76a3b65aeb45912bfdb02ce354f74bb97a9d9b2)
  * `AstronomicalCalculator` - [add abstract method `getUTCMidnight()`](https://github.com/KosherJava/zmanim/commit/f1904b12393c48b069d1333a7397fce66804958d)
  * `NOAACalculator` - [implement `getUTCMidnight()`](https://github.com/KosherJava/zmanim/commit/b93eea3388bfdcc2dd526bbcb1be37ddb88fee08)
  * `AstronomicalCalculator` - [add abstract method `getUTCMidnight()`](https://github.com/KosherJava/zmanim/commit/1223dd0b6ad2b492818aacc5eb478747989e0ace)
* `ComprehensiveZmanimCalendar` - [significant updates](https://github.com/KosherJava/zmanim/commit/46800aa750ac56c2da9bc55fbf976ea1a092221d)
  * Started coding some zmanim to use the half-day zmanim config.
  * Change `getFixedLocalChatzosBasedZmanim()` in favor of `getHalfDayBasedZman()` in the parent `ZmanimCalendar class. 
  * `getFixedLocalChatzos()` now just calls the new getLocalMeanTime(12.0) in the grandparent AstronomicalCalendar class.
  * Remove [`getSolarMidnight()`] that was [added to the `AstronomicalCalendar`](https://github.com/KosherJava/zmanim/commit/a4535717353eb77da10b6951e4a627b10258ac9e) grandparent class.
  * Undeprecate `getPlagAlosToSunset()` since it is not a zman that can be too late.
  * Add [`getSofZmanAchilasChametzMGA72MinutesZmanis()` and `getSofZmanBiurChametzMGA72MinutesZmanis()`](https://github.com/KosherJava/zmanim/commit/c444fd3d1ae327560158b5f11c918a59c4eff55e)
  * [Add null checks in `getMinchaGedolaAhavatShalom()`](https://github.com/KosherJava/zmanim/commit/93f441f1ff87d4669c91b596eed157c9cf448bca)
  * [Fix `getAlos60()` to use `getElevationAdjustedSunrise()`](https://github.com/KosherJava/zmanim/commit/f5a5b2c68e1f0e2f9f4fbdd2cc585085f2914b74)
  * Update Tefila method to Use [Consistent Spelling](https://github.com/KosherJava/zmanim/commit/bca6ddb85542683f229d905636a06fbfc66fbe03).
  * Add [`getSunsetOrWesternmostSolarAzimuth()` and `getSunriseOrEasternmostSolarAzimuth()`](https://github.com/KosherJava/zmanim/commit/ab9a903e94c33d31f1ce006a6102cdfa259786ad) methods.
  * Add [`getPolarPlagHaminchaBenIshChai()` and `getPolarStartOfDayTeshuvosVehanhagos()`](https://github.com/KosherJava/zmanim/commit/61a0b38ad295044ca39c884e1bda99a2b6644425)
  * Add [`getPolarStartOfDayTeshuvosVehanhagos()`](https://github.com/KosherJava/zmanim/commit/df53b1d73e0a2c9bfb3be1da748e605a6f5b5b47)
* `zmanimFormatter now formats `Duration`s and removed support for formatting longs and Time.
* `HebrewdateFormatter`
  * add method [`formatParsha(JewishCalendar.Parsha parsha)`](https://github.com/KosherJava/zmanim/commit/ee3347b04bf0f4221bc8aa71af59437cd7533f72) to allow formatting of a parsha retrieved from `JewishCalendar.getUpcomingParsha()`.
  * Add `getHebrewMonthList()` and `setHebrewMonthList(String[])`. This allows overriding the default month of Chesvan to Marcheshvan etc.
  * Throw an `IllegalArgumentException` if the array length of months != 14
  * Remove the experimental and private `formatMolad(long moladChalakim)`
  * Fixed a bug that ignored the leading 0 format.
  * [Fix NullPointer in HebrewDateFormatter week formatting](https://github.com/KosherJava/zmanim/commit/6cef302f4ac815941c1f61765f2749d698f86042)
* `TefilaRules`
  * [add `isMizmorLesodaRecited()`](https://github.com/KosherJava/zmanim/commit/2cde42644dc72a49b3e4228244bc79cc276e138e)
  * fix [Tachanun is not recited on Erev Rosh Hashana](https://github.com/KosherJava/zmanim/commit/0b6b95cfdebd14f19078875564b87068ed2623c4)
* `JewishCalendar`
  * [`isYomTov()` now returns true for 20 Nissan](https://github.com/KosherJava/zmanim/commit/4e5abe6e98d5404f41da519f2f902b3af1e58e30)
  * [add missing brace to `isYomTov()` and simplify logic](https://github.com/KosherJava/zmanim/commit/e34fc879313b045f35e70b5947e2c2e20a4364c5)
  * `getDaysInJewishMonth(int,int)` and `getLastDayOfGregorianMonth(int,int)` now have parameter order as year, month (it had been month, year).
  * `isJewishLeapYear(int)` is now public.
  * [Rename `isHavdalahRecited()` -> `isTonightMutarBemelacha()`](https://github.com/KosherJava/zmanim/commit/61a0b38ad295044ca39c884e1bda99a2b6644425)
  * Added plus and minus methods (matching the `java.time.LocalDate` class) for years, months and days.
  * [Add `getTekufa()` and `getTekufaAsInstant()`](https://github.com/KosherJava/zmanim/commit/ad91c0a65633d2b3f0f9745f84c56946436a7f64)
  * [Rename `Parsha` to `Parshah`](https://github.com/KosherJava/zmanim/commit/df53b1d73e0a2c9bfb3be1da748e605a6f5b5b47). This was done in formatters as well.
* `GeoLocation`
  * [add NaN validation to `setLatitude` and `setLongitude`](https://github.com/KosherJava/zmanim/commit/d064715ebeaead29a01ec673f3885ee9bd9c78b4)
  * [Remove DMS versions of setLatitude and setLongitude](https://github.com/KosherJava/zmanim/commit/910ee42b91de350007dfc3b832897bc263893b89)
* `NOAACalculator` - [fix Solar Azimuth and Elevation](https://github.com/KosherJava/zmanim/commit/860f1939c25b38dd4d23adb1772b12ccbc71fc76)
* `AstronomicalCalculator` Add some methods
  * `getSolarAzimuth((Instant, GeoLocation)` - the first parameter was a ZonedDateTime earlier in the rewrite
  * `getSolarElevation(Instant, GeoLocation)` - the first parameter was a ZonedDateTime earlier in the rewrite
  * [`getTimeAtAzimuth90Or270(double)`](https://github.com/KosherJava/zmanim/commit/7287a6ab54eb76324b442ea56ffbd56973f0429f)
  * [Use and Default to Using The sun's apparent angular semi-diameter for that day](https://github.com/KosherJava/zmanim/commit/911d58ea09996560c3f2804e612fb433bcd0dee2)
  * [Rename `getSolarRadius(LocalDate)` -> `getApparentSolarRadius(LocalDate)`](https://github.com/KosherJava/zmanim/commit/d002497e76ef5fda06c0c6bdb812492d0b87856d) to avoid confusion
  * Rename a`stronomicalApparentSolarRadius` -> `apparentSolarRadius`
* `AstronomicalCalendar`
  * [Fix null handling in `getSunTransit(Date,Date)`](https://github.com/KosherJava/zmanim/commit/8221e2895cbab62b037c16de1711f9faacd78a7b)
  * [Deprecate `getSunriseSolarDipFromOffset` and `getSunsetSolarDipFromOffset`](https://github.com/KosherJava/zmanim/commit/0ce858258bff15c11235b1f1063d2eb0ef22b994)
  * [Pass proper parameter to `getDateFromTime` in `getLocalMeanTime`](https://github.com/KosherJava/zmanim/commit/da7e888299c27622e1786af7d517f620060a38e0)
  * [Add `getLocalMeanTime()`](https://github.com/KosherJava/zmanim/commit/14bcdc085011ccce327f69d6a001772c0581fcc2).
  * [Move `getSolarMidnight()`](https://github.com/KosherJava/zmanim/commit/a4535717353eb77da10b6951e4a627b10258ac9e) to the  parent class where it belongs.
  * [Correct USNO noon calculation](https://github.com/KosherJava/zmanim/commit/3735c92289a66039b24d7e2b470955b5297f0ca5) in some locations where it was sometimes 12 hours off.
  * [Add `getTimeAtAzimuth(double azimuth)`](https://github.com/KosherJava/zmanim/commit/05a3bc3cdb2dbc88ffca89be0828f50abf62969f)
  * All sha'os zmaniyos that had returned long (and a Long.MIN_VALUE when N/A) now return java.time.Duration (and a null when N/A)
  * [Add two new more accurate and slower calculators](https://github.com/KosherJava/zmanim/commit/0b033da4375a8b341a69031f05977573d4556394)
    * `MeeusCalculator` - a calculator that implements the higher accuracy Jean Meeus algorithmn (the existing NOAACalculator uses the low accuracy implementation)
    * `SPACalculator` - a higher accuracy calculator
  * [Deprecate the USNO-Based `SunTimesCalculator`](https://github.com/KosherJava/zmanim/commit/340a1524af55748b41e4f0b780387eff41710617) that is not as accurate as other calculators.

## [2.5.0](https://github.com/KosherJava/zmanim/compare/2.4.0...2.5.0) (2023-06-09)

* Update `ComplexZmanimCalendar.getSolarMidnight()` to support astronomocal midnight that works even in the Arctic/Antarctic.
* Add special Shabbasos/Parshiyos Shuva, Shira, Hagadol, Chazon and Nachamu
* Fix isYomTov() should return false on Erev Shavuos.
* Correct spelling of Bein Hashmashos methods the the `ComplexZmanimCalendar` (was missing the second H).
* Various Daf Yomi Yerushalmi fixes including:
  * Correct calculation of the _daf_ number.
  * Correct the order of transliterated Yerushalmi _masechtos_.
  * Correct the Hebrew spelling of the _masechta_ Kilayim.
* Added  number of IS methods such as is `isYomKippur()`, `isSuccos()`, `isPesach()` etc. to the `JewishCalendar` class.
* Add `isAlHanissimRecited(JewishCalendar)` and `isYaalehVeyavoRecited(JewishCalendar)` to the `TefilaRules` class.
* Clarify documentation to explain that isMacharChodesh() Refers to the Haftorah

## [2.4.0](https://github.com/KosherJava/zmanim/compare/2.3.0...2.4.0) (2022-11-27)

* JewishCalendar.getUpcomingParshah() that will return the upcoming _Parsha_ regardless of the day of week.
* Change YerushalmiYomiCalculator to return null on Yom Kippur and Tisha Be'Av when there is no Daf.
* Add some Luach Ahavat Shalom Zmanim
* Add _BeHaB_ to the `JewishCalendar`class
* Add _Yom Kippur Katan_ and _Isru Chag_ to the `JewishCalendar`class.
* Add the `TefilaRules` class, a utility class for info like:
  * is _vesain tal umatar_ recited etc.
  * is _tachanun_ recited by _shacharis_ or _mincha_.
  * Is _hallel_ or _hallel shalem_ recited
* Deprecate the _tefila_ rules methods that existed in JewishCalendar class in favor of using the ones in the `TefilaRules` class.
* Add `getSamuchLeMinchaKetana` _zman_.
* Deprecate `getSofZmanShmaFixedLocal()` and `getSofZmanTfilaFixedLocal()` with future plans of removal.
* Deprecate multiple "dangerous" _zmanim_ as an alert to developers, with plans on retaining them.

## [2.3.0](https://github.com/KosherJava/zmanim/compare/98d704...2.3.0) (2021-12-07)

* Fix an issue with sof _zman kiddush levana_ being off by an hour when the _molad_ is on one side of the DST change, and the _sof zman_ on the other.
* Add seasonal _davening_ based _zmanim_ including _Vesein Tal Umatar/ Vesein Berachah / Mashiv Haruach_.
* Add Rav Moshe Feinstein's _zmanim_ used in MTJ and Yeshiva of Staten Island.
* Refactor code for alos and _tzeis zmaniyos_ based time (ports to other languages can simplify things by doing the same).
* Fix Hebrew spelling of _Parshas Nitzavim_.

## [2.2.0](https://github.com/KosherJava/zmanim/compare/2.1.0...98d704) (2021-03-15)

* Added JewishCalendar.isTaanisBechoros().
* Updated Javadocs - document sources for `getFixedLocalChatzos()` and clarify _Yerushalmi Yomi_ Start Date.

## [2.1.0](https://github.com/KosherJava/zmanim/compare/8ffa53b9a...2.1.0) (2020-12-02)

* Added six variants of the Yereim's _bain hashmashos zmanim_.
* `AstronomicalCalculator.getRefraction()` and `.getSolarRadius()` now have public access.
* Deprecate the `GeoLocationUtils` class. All of its functionality is in the `GeoLocation` class.
* Updated JavaDocs (no more errors or warnings).
* Added Lag Ba'omer.
* Added Shushan Purim Katan.
* Added `Daf.setMasechtaTransliterated(String[] masechtosBavliTransliterated)` and `Daf.setYerushlmiMasechtaTransliterated(String[] masechtosYerushalmiTransliterated)`.
* Simplify and reduce code duplication in `ZmanimCalendar` generic _zmanim_ calculations.
* Fix `AstronomicalCalendar` `getSunriseSolarDipFromOffset()` and `getSunsetSolarDipFromOffset` (they are still inefficient) to properly allow calculations before and after sun rise/set.
* Change some Hebrew lists that are not expected to change to be final.

## [2.0.3] (2020-10-01)
* Semver change (just a versioning change).

## [2.02] (2020-09-30)
* Fix JavaDoc references to new package structure.

## [2.01] (2020-09-29)
* Fix #160 `isShabbosMevorchim` should return false for the month of Tishrei.
* Fix #161 a mistake in `Zman.toString()`.
* Fix java 6 compilation issues.

## [2.0] (2020-08-03)

* Changed package structure to `com.kosherjava.zmanim` from `net.sourceforge.zmanim`.
* Added Maven and Gradle support.
* Use DST for TimeZone display name (#150).
* Convert `formatMolad()` to static.
* Convert `getTimeOffset()` to static.
* Pass alos and tzais parameters for `TchilasZmanKidushLevana3Days`.
* Historical _daf yomi_ dates should be final.
* Add _Birkas Hachama_, update documentation.
* Update formatter class for Enums in `JewishCalendar`.


## Older Changes (since 1.3)

* Default calculator changed from USNO to NOAA.
* Remove the redundant `ZmanimCalculator` class (backwards breaking if you used this calculator).
* Support optional elevation adjustments for zmanim besides sunrise and sunset.
* Added multiple alternative zmanim .
* Added Baal Hatanya _zmanim_.
* Replaced GPL parsha code with an LGPL kosher version.
* Added JSON serialization / output (was previously limited to XML).
* Add _Daf Yomi Yerishalmi_.
* Many `JewishCalendar` related tweaks and enhancements.
* Many minor bug fixes and enhancements.

See [GitHub Commits](https://github.com/KosherJava/zmanim/commits/master) for more details.

