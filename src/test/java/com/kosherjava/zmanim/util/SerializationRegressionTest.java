package com.kosherjava.zmanim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

import com.kosherjava.zmanim.AstronomicalCalendar;

public class SerializationRegressionTest {

	@Test
	public void zmanToXmlSerializesInstantUsingGeoLocationZone() {
		GeoLocation geoLocation = new GeoLocation("Lakewood", 40.0828, -74.2094, ZoneId.of("America/New_York"));
		Zman zman = new Zman(Instant.parse("2026-03-23T10:56:01.123Z"), geoLocation, "Sunrise");

		String xml = zman.toXML();

		assertTrue(xml.contains("<Zman>2026-03-23T06:56:01.123</Zman>"));
	}

	@Test
	public void zmanWithoutGeoLocationStillSerializesAndFormats() {
		Zman zman = new Zman(Instant.parse("2026-03-23T10:56:01.123Z"), "Sunrise");

		String xml = zman.toXML();
		String text = zman.toString();

		assertTrue(xml.contains("<Zman>2026-03-23T10:56:01.123</Zman>"));
		assertTrue(text.contains("GeoLocation:\tnull"));
	}

	@Test
	public void astronomicalCalendarPreservesSubSecondPrecision() {
		AstronomicalCalendar astronomicalCalendar = new AstronomicalCalendar(
				new GeoLocation("Lakewood", 40.0828, -74.2094, ZoneId.of("America/New_York")));

		double utcSunrise = astronomicalCalendar.getUTCSunrise(AstronomicalCalendar.GEOMETRIC_ZENITH);
		long expectedEpochMillis = Math.round(utcSunrise * AstronomicalCalendar.HOUR_MILLIS);
		long actualEpochMillis = astronomicalCalendar.getSunriseWithElevation().toEpochMilli()
				% (24 * AstronomicalCalendar.HOUR_MILLIS);

		assertTrue(expectedEpochMillis % 1000 != 0);
		assertFalse(actualEpochMillis % 1000 == 0);
		assertTrue(Math.abs(expectedEpochMillis - actualEpochMillis) < 1000);
	}

	@Test
	public void negativeTimePreservesSignInValueAndFormatting() {
		Time time = new Time(-90_500);

		assertEquals(-90_500d, time.getTime(), 0.0);
		assertEquals("-0:01:30.500", time.toString());
	}
}
