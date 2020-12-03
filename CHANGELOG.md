## [2.1.0] (2020-12-02)

* Added six variants of the Yereim's bain hashmashos zmanim.
* AstronomicalCalculator.getRefraction() and .getSolarRadius() now have public access.
* Deprecate the GeoLocationUtils class. All of its functionality is in the GeoLocation class.
* Updated JavaDocs (no more errors or warnings).
* Added Lag Ba'omer.
* Added Shushan Purim Katan.
* Added Daf.setMasechtaTransliterated(String[] masechtosBavliTransliterated) and Daf.setYerushlmiMasechtaTransliterated(String[] masechtosYerushalmiTransliterated).
* Simplify and reduce code duplication in ZmanimCalendar generic zmanim calculations.
* Fix AstronomicalCalendar getSunriseSolarDipFromOffset() and getSunsetSolarDipFromOffset (they are still inefficient) to properly allow claculations  before and after sun rise/set.
* Change some Hebrew lists that are not expected to change to be final.

## [2.0.3] (2020-10-01)
* Semver change (just a versioning change).

## [2.02] (2020-09-30)
* Fix JavaDoc references to new package structure.

## [2.01] (2020-09-29)
* Fix #160 isShabbosMevorchim should return false for the month of Tishrei.
* Fix #161 a mistake in Zman.toString().
* Fix java 6 compilation issues.

## [2.0] (2020-08-03)

* Changed package structure to com.kosherjava.zmanim from net.sourceforge.zmanim.
* Added Maven and Gradle support.
* Use DST for TimeZone display name (#150).
* Convert formatMolad() to static.
* Convert getTimeOffset() to static.
* Pass alos and tzais parameters for TchilasZmanKidushLevana3Days.
* Historical daf yomi dates should be final.
* Add Birkas Hachama, update documentation.
* Update formatter class for Enums in JewishCalendar.


## Older Code
See [GitHub Commits](https://github.com/KosherJava/zmanim/commits/master) for more.
