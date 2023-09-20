## 3.0.0 (future)
### Includes breaking changes
* Remove deprecated methods flagged for removal.
* Remove deprecated classes such as the redundant [GeoLocationUtils](https://github.com/KosherJava/zmanim/blob/master/src/main/java/com/kosherjava/zmanim/util/GeoLocationUtils.java).
* Possibly rename some classes such as the confusingly named [ComplexZmanimCalendar](https://github.com/KosherJava/zmanim/blob/master/src/main/java/com/kosherjava/zmanim/ComplexZmanimCalendar.java).
* `getSofZman*Chametz*` times will retun null if it is not _Erev Pesach_.
* Possibly increase the minimum supported JRE version from version 8 (the code currently almost certainly works on 6 today).
* ...

## [2.6.0](https://github.com/KosherJava/zmanim/compare/2.5.0...master) (future)

* Add method [HebrewdateFormatter.formatParsha(JewishCalendar.Parsha parsha)](https://github.com/KosherJava/zmanim/commit/ee3347b04bf0f4221bc8aa71af59437cd7533f72) to allow formatting of a parsha retrieved from JewishCalendar.getUpcomingParshah().
* Update Tefila method to Use [Consistent Spelling](https://github.com/KosherJava/zmanim/commit/bca6ddb85542683f229d905636a06fbfc66fbe03).
* Add [AstronomicalCalendar.getLocalMeanTime()](https://github.com/KosherJava/zmanim/commit/14bcdc085011ccce327f69d6a001772c0581fcc2).
* Add utility method [ZmanimCalendar.getPercentOfShaahZmanisFromDegrees(double degrees, boolean sunset)](https://github.com/KosherJava/zmanim/commit/60d1f09322835835035afa507ac2dc852f1cb033) to simplify zmaniyos time calculations. This allows calculations of various percentage of the day zmanim calculations.
* [Move getSolarMidnight](https://github.com/KosherJava/zmanim/commit/a4535717353eb77da10b6951e4a627b10258ac9e) to the AstronomicalCalendar parent class where it belongs.
* [Correct USNO noon calculation](https://github.com/KosherJava/zmanim/commit/3735c92289a66039b24d7e2b470955b5297f0ca5) in some locations where it was sometimes 12 hours off.
* ...

## [2.5.0](https://github.com/KosherJava/zmanim/compare/2.4.0...2.5.0) (2023-06-09)

* Update `ComplexZmanimCalendar.getSolarMidnight()` to support astronomocal midnight that works even in the Arctic/Antarctic.
* Add special Shabbasos/Parshiyos Shuva, Shira, Hagadol, Chazon and Nachamu
* Fix isYomTov() should return false on Erev Shavuos.
* Correct spelling of Bein Hashmashos methods the the `ComplexZmanimCalendar` (was missing the second H).
* Various Daf Yomi Yerushalmi fixes including:
  * Correct calculation of the _daf_ number.
  * Correct the order of transliterated Yerushalmi _masechtos_.
  * Correct the Hebrew spelling of the _masechta_ Kilayim.
* * Added  number of IS methods such as is `isYomKippur()`, `isSuccos()`, `isPesach()` etc. to the `JewishCalendar` class.
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
