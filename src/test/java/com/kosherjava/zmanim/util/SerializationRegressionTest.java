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
		long expectedEpochMillis = Math.round(utcSunrise * 3600000);
		long actualEpochMillis = astronomicalCalendar.getSunrise().toEpochMilli()
				% (24 * 3600000);

		assertTrue(expectedEpochMillis % 1000 != 0);
		assertFalse(actualEpochMillis % 1000 == 0);
		assertTrue(Math.abs(expectedEpochMillis - actualEpochMillis) < 1000);
	}

	/** A location name with XML / JSON special characters, e.g. a name with an ampersand or a quote. */
	private static final String SPECIAL_NAME = "Ta\"l & <Ma'alot> \\ x";

	private static void assertWellFormedXml(String xml) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
	}

	@Test
	public void toXmlEscapesSpecialCharacters() throws Exception {
		GeoLocation geoLocation = new GeoLocation(SPECIAL_NAME, 40.0828, -74.2094, ZoneId.of("America/New_York"));
		com.kosherjava.zmanim.ComprehensiveZmanimCalendar calendar = new com.kosherjava.zmanim.ComprehensiveZmanimCalendar(geoLocation);
		String xml = ZmanimFormatter.toXML(calendar);
		assertWellFormedXml(xml);
		assertTrue(xml.contains("location=\"Ta&quot;l &amp; &lt;Ma&apos;alot&gt; \\ x\""));

		assertWellFormedXml(geoLocation.toXML());
		Zman zman = new Zman(Instant.parse("2026-03-23T10:56:01.123Z"), geoLocation, "Alos & <Tzais>");
		zman.setDescription("16.1\u00b0 \"degrees\"");
		assertWellFormedXml(zman.toXML());
	}

	@Test
	public void toJsonEscapesSpecialCharacters() {
		GeoLocation geoLocation = new GeoLocation(SPECIAL_NAME + "\n", 40.0828, -74.2094, ZoneId.of("America/New_York"));
		String json = ZmanimFormatter.toJSON(new com.kosherjava.zmanim.ComprehensiveZmanimCalendar(geoLocation));
		assertTrue(json.contains("\"location\":\"Ta\\\"l & <Ma'alot> \\\\ x\\n\","));
	}
}
