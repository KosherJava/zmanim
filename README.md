KosherJava Zmanim API
=====================

The Zmanim library is an API for a specialized calendar that can calculate different astronomical
times including sunrise and sunset and Jewish _zmanim_ or religious times for prayers and other
Jewish religious dutuies.

These classes extend GregorianCalendar and can therefore
use the standard Calendar functionality to change dates etc. For non religious astronomical / solar
calculations use the [AstronomicalCalendar](./src/net/sourceforge/zmanim/AstronomicalCalendar.java).

The ZmanimCalendar contains the most common zmanim or religious time calculations. For a much more
extensive list of _zmanim_ use the ComplexZmanimCalendar.
This class contains the main functionality of the Zmanim library.

For a basic set of instructions on the use of the API, see [How to Use the Zmanim API](http://www.kosherjava.com/zmanim-project/how-to-use-the-zmanim-api/), [zmanim code samples](http://www.kosherjava.com/tag/code-sample/) and the [KosherJava FAQ](http://www.kosherjava.com/tag/faq/).

License
-------
The library is released under the [LGPL 2.1 license](http://www.kosherjava.com/2011/05/09/kosherjava-zmanim-api-released-under-the-lgpl-license/).

Ports to Other Languages
------------------------
The KosherJava Zmanim API has benn ported to:
* Objective-C / Swift - https://github.com/MosheBerman/KosherCocoa
* .NET - https://github.com/Yitzchok/Zmanim
* JavaScript / TypeScript -  https://github.com/BehindTheMath/KosherZmanim


Disclaimer:
-----------
__While I did my best to get accurate results, please double check before relying on these zmanim for <em>halacha lemaaseh</em>__.
