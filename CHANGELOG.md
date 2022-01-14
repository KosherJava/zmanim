## [2.4.0](https://github.com/KosherJava/zmanim/compare/2.3.0...master) (future)

* Add `getSamuchLeMinchaKetana` _zman_.
* Deprecate `getSofZmanShmaFixedLocal()` and `getSofZmanTfilaFixedLocal()` with future plans of removal.
* Deprecate multiple "dangerous" _zmanim_ as an alert to developers, with plans on retaining them.

## [2.3.0](https://github.com/KosherJava/zmanim/compare/2.1.0...2.3.0) (2021-12-07)

* Fix an issue with sof _zman kiddush levana_ being off by an hour when the _molad_ is on one side of the DST change, and the _sof zman_ on the other.
* Add seasonal _davening_ based _zmanim_ including _Vesein Tal Umatar/ Vesein Berachah / Mashiv Haruach_.
* Add Rav Moshe Feinstein's _zmanim_ used in MTJ and Yeshiva of Staten Island.
* Refactor code for alos and _tzeis zmaniyos_ based time (ports to other languages can simplify things by doing the same).
* Fix Hebrew spelling of _Parshas Nitzavim_.

## [2.2.0] (2021-03-15)

* Added JewishCalendar.isTaanisBechoros().
* Updated Javadocs - document sources for `getFixedLocalChatzos()` and clarify _Yerushalmi Yomi_ Start Date.

## [2.1.0] (2020-12-02)

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
