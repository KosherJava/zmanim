package com.kosherjava.zmanim;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import com.kosherjava.zmanim.util.GeoLocation;

/**
 * Not a test (no "Test" suffix, so Surefire ignores it). A throwaway generator that dumps the library's current output
 * for every zero-arg zman method so the values can be baked into the regression tests. Run via:
 * {@code java -cp target/test-classes:target/classes com.kosherjava.zmanim.RegressionValueDumper &lt;class&gt;}, or with a
 * Jewish date to exercise the date-gated zmanim:
 * {@code ... RegressionValueDumper comprehensive jewish &lt;year&gt; &lt;month&gt; &lt;day&gt;}.
 */
public final class RegressionValueDumper {

	public static void main(String[] args) throws Exception {
		String which = args.length > 0 ? args[0] : "comprehensive";
		GeoLocation lakewood = TestLocations.lakewood();
		LocalDate date = LocalDate.of(2017, 10, 17);
		if (args.length >= 5 && "jewish".equals(args[1])) {
			date = new JewishDate(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]))
					.getLocalDate();
		}
		if (args.length >= 3 && "scan".equals(args[1])) {
			// scan <methodName> [startIsoDate] [days] [location]: print each date where the method is non-null
			String methodName = args[2];
			LocalDate start = args.length >= 4 ? LocalDate.parse(args[3]) : LocalDate.of(2017, 10, 1);
			int days = args.length >= 5 ? Integer.parseInt(args[4]) : 40;
			GeoLocation scanLocation = args.length >= 6 && "norway".equals(args[5])
					? TestLocations.norway(java.time.ZoneId.of("Europe/Oslo")) : lakewood;
			ComprehensiveZmanimCalendar scanCal = new ComprehensiveZmanimCalendar(scanLocation);
			for (int i = 0; i < days; i++) {
				LocalDate d = start.plusDays(i);
				scanCal.setLocalDate(d);
				Object value = scanCal.getClass().getMethod(methodName).invoke(scanCal);
				if (value != null) {
					System.out.println(d + "\t" + methodName + "\t" + value);
				}
			}
			return;
		}

		AstronomicalCalendar calendar;
		switch (which) {
			case "astronomical":
				calendar = new AstronomicalCalendar(lakewood);
				break;
			case "zmanim":
				calendar = new ZmanimCalendar(lakewood);
				break;
			default:
				calendar = new ComprehensiveZmanimCalendar(lakewood);
				break;
		}
		calendar.setLocalDate(date);

		List<String> names = new ArrayList<>();
		for (Method method : calendar.getClass().getMethods()) {
			if (method.getParameterCount() != 0) {
				continue;
			}
			if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				continue;
			}
			Class<?> returnType = method.getReturnType();
			if (returnType != Instant.class && returnType != Duration.class) {
				continue;
			}
			names.add(method.getName());
		}
		names.sort(String::compareTo);

		for (String name : names) {
			Object value = calendar.getClass().getMethod(name).invoke(calendar);
			String formatted;
			if (value == null) {
				formatted = "null";
			} else if (value instanceof Instant) {
				formatted = ((Instant) value).toString();
			} else {
				formatted = ((Duration) value).toString();
			}
			System.out.println(name + "\t" + formatted);
		}
		System.out.println("# total: " + names.size());
	}

	private RegressionValueDumper() {
	}
}
