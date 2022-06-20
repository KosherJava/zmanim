package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertTrue;

import com.kosherjava.zmanim.ComplexZmanimCalendar;
import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.util.GeoLocation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

public class ShaahZmanisTest {

	@Test
	public void testTemporalHour() {
		ZmanimCalendar calendar = new ComplexZmanimCalendar();
		long hour = calendar.getTemporalHour();
		assertTrue(hour > 0);
	}

	@Test
	public void testShaahZmanis() {
		Calendar gcal = Calendar.getInstance();
		gcal.set(2022, Calendar.JUNE, 22);
		GeoLocation location = new GeoLocation("test", 0.0, 0.0, TimeZone.getTimeZone("UTC"));
		ComplexZmanimCalendar calendar = new ComplexZmanimCalendar(location);
		calendar.setCalendar(gcal);
		long hour;
		long delta = 1000;

		hour = calendar.getShaahZmanis120Minutes();
		assertEquals(4836877, hour, delta);
		hour = calendar.getShaahZmanis120MinutesZmanis();
		assertEquals(4849169, hour, delta);
		hour = calendar.getShaahZmanis16Point1Degrees();
		assertEquals(4304341, hour, delta);
		hour = calendar.getShaahZmanis18Degrees();
		assertEquals(4387928, hour, delta);
		hour = calendar.getShaahZmanis19Point8Degrees();
		assertEquals(4467292, hour, delta);
		hour = calendar.getShaahZmanis26Degrees();
		assertEquals(4742324, hour, delta);
		hour = calendar.getShaahZmanis60Minutes();
		assertEquals(4236877, hour, delta);
		hour = calendar.getShaahZmanis72Minutes();
		assertEquals(4356877, hour, delta);
		hour = calendar.getShaahZmanis72MinutesZmanis();
		assertEquals(4364252, hour, delta);
		hour = calendar.getShaahZmanis90Minutes();
		assertEquals(4536877, hour, delta);
		hour = calendar.getShaahZmanis90MinutesZmanis();
		assertEquals(4546096, hour, delta);
		hour = calendar.getShaahZmanis96Minutes();
		assertEquals(4596877, hour, delta);
		hour = calendar.getShaahZmanis96MinutesZmanis();
		assertEquals(4606710, hour, delta);
		hour = calendar.getShaahZmanisAteretTorah();
		assertEquals(4200564, hour, delta);
		hour = calendar.getShaahZmanisBaalHatanya();
		assertEquals(3669565, hour, delta);
		hour = calendar.getShaahZmanisGra();
		assertEquals(3636877, hour, delta);
		hour = calendar.getShaahZmanisMGA();
		assertEquals(4356877, hour, delta);
	}

	private static void assertEquals(long expected, long actual, long delta) {
		Assert.assertEquals((float) expected, (float) actual, (float) delta);
	}
}
