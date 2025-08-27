KosherJava Zmanim API
=====================

The Zmanim library is an API for a specialized calendar that can calculate different astronomical
times including sunrise and sunset and Jewish _zmanim_ or religious times for prayers and other
Jewish religious duties.

These classes extend GregorianCalendar and can therefore
use the standard Calendar functionality to change dates etc. For non religious astronomical / solar
calculations use the [AstronomicalCalendar](./src/main/java/com/kosherjava/zmanim/AstronomicalCalendar.java).

The ZmanimCalendar contains the most common zmanim or religious time calculations. For a much more
extensive list of _zmanim_ use the ComplexZmanimCalendar.
This class contains the main functionality of the Zmanim library.

For a basic set of instructions on the use of the API, see [How to Use the Zmanim API](https://kosherjava.com/zmanim-project/how-to-use-the-zmanim-api/), [zmanim code samples](https://kosherjava.com/tag/code-sample/) and the [KosherJava FAQ](https://kosherjava.com/tag/faq/). See the <a href="https://kosherjava.com">KosherJava Zmanim site</a> for additional information.

# Get Started
To add KosherJava as a dependency to your project, add the following dependency:

#### Maven
Add the following to your `pom.xml` file:
```xml
<dependency>
  <groupId>com.kosherjava</groupId>
  <artifactId>zmanim</artifactId>
  <version>2.5.0</version>
</dependency>
```

#### Gradle
Add the following to your `build.gradle` file:
```groovy
implementation group: 'com.kosherjava', name: 'zmanim', version: '2.5.0'
```
Or if you have `build.gradle.kts` file:

```kotlin
implementation("com.kosherjava:zmanim:2.5.0")
```


License
-------
The library is released under the [LGPL 2.1 license](https://kosherjava.com/2011/05/09/kosherjava-zmanim-api-released-under-the-lgpl-license/).

Ports to Other Languages
------------------------
The KosherJava Zmanim API has been ported to:
* .NET - https://github.com/Yitzchok/Zmanim
* C - https://github.com/yparitcher/libzmanim
* Dart / Flutter - https://github.com/yakir8/kosher_dart
* Go - https://github.com/vlipovetskii/go-zmanim
* JavaScript - https://github.com/yparitcher/zmanJS
* Kotlin - https://github.com/Sternbach-Software/KosherKotlin
* Monkey C - https://github.com/5E7EN/Garmin-ZmanIQ/tree/rebuild-zmaniq/source/utils/zmanim
* Objective-C - https://github.com/MosheBerman/KosherCocoa
* PHP - https://github.com/zachweix/PhpZmanim/
* Python - https://github.com/pinnymz/python-zmanim & https://pypi.org/project/zmanim/
* Ruby - https://github.com/pinnymz/ruby-zmanim
* Rust - https://github.com/dickermoshe/zmanim-core & https://github.com/YSCohen/rust-zmanim (WIP)
* Scala - https://github.com/nafg/jewish-date
* SQL - https://github.com/BehindTheMath/zmanim-sql (alpha quality).
* Swift - https://github.com/Elyahu41/KosherSwift & https://github.com/DanielSmith1239/KosherSwift
* TypeScript - https://github.com/BehindTheMath/KosherZmanim
* VB6/VBA (Visual Basic) - https://github.com/diaphone1/vbzmanim

ZmanCode Desktop App
------------------------
The .NET port was used to create a desktop app that is available at https://github.com/NykUser/MyZman.

Disclaimer:
-----------
__While I did my best to get accurate results, please double check before relying on these zmanim for <em>halacha lemaaseh</em>__.

------------------------
[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/KosherJava/zmanim?color=eed6af&label=KosherJava&logo=github)](https://search.maven.org/artifact/com.kosherjava/zmanim)
[![GitHub](https://img.shields.io/github/license/KosherJava/zmanim?color=eed6af&logo=gnu)](https://github.com/KosherJava/zmanim/blob/master/LICENSE)
[![GitHub last commit](https://img.shields.io/github/last-commit/KosherJava/zmanim?logo=github)](https://github.com/KosherJava/zmanim/commits/master)
[![CodeQL](https://github.com/KosherJava/zmanim/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/KosherJava/zmanim/actions/workflows/codeql-analysis.yml)
[![Run unit tests](https://github.com/KosherJava/zmanim/actions/workflows/pull_request_worklow.yml/badge.svg)](https://github.com/KosherJava/zmanim/actions/workflows/pull_request_worklow.yml)
