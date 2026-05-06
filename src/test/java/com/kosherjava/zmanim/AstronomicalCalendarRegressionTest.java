package com.kosherjava.zmanim;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import com.kosherjava.zmanim.util.GeoLocation;

public class AstronomicalCalendarRegressionTest {

	@Test
	public void localMeanTimeForLateEveningStaysOnRequestedLocalDate() {
		GeoLocation geoLocation = new GeoLocation("Chicago", 41.8781, -87.6298, ZoneId.of("America/Chicago"));
		AstronomicalCalendar astronomicalCalendar = new AstronomicalCalendar(geoLocation);
		LocalDate localDate = LocalDate.of(2026, 6, 1);
		astronomicalCalendar.setLocalDate(localDate);

		Instant actual = astronomicalCalendar.getLocalMeanTime(LocalTime.of(23, 0));
		ZonedDateTime civilTime = ZonedDateTime.of(localDate, LocalTime.of(23, 0), geoLocation.getZoneId());
		Instant expected = civilTime.toInstant().minusMillis(geoLocation.getLocalMeanTimeOffset(civilTime.toInstant()));

		assertEquals(expected, actual);
		assertEquals(localDate, actual.atZone(geoLocation.getZoneId()).toLocalDate());
	}

	@Test
	public void localMeanTimeUsesOffsetAtRequestedTimeOnDstTransitionDate() {
		GeoLocation geoLocation = new GeoLocation("New York", 40.7128, -74.0060, ZoneId.of("America/New_York"));
		AstronomicalCalendar astronomicalCalendar = new AstronomicalCalendar(geoLocation);
		LocalDate localDate = LocalDate.of(2026, 3, 8);
		astronomicalCalendar.setLocalDate(localDate);

		Instant actual = astronomicalCalendar.getLocalMeanTime(LocalTime.NOON);
		ZonedDateTime civilTime = ZonedDateTime.of(localDate, LocalTime.NOON, geoLocation.getZoneId());
		Instant expected = civilTime.toInstant().minusMillis(geoLocation.getLocalMeanTimeOffset(civilTime.toInstant()));

		assertEquals(expected, actual);
		assertEquals(LocalTime.of(12, 56, 1, 440_000_000), actual.atZone(geoLocation.getZoneId()).toLocalTime());
	}
}
