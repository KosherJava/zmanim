package com.kosherjava.zmanim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.ZoneId;

import org.junit.Test;

import com.kosherjava.zmanim.util.GeoLocation;

public class CalendarEqualityRegressionTest {

	@Test
	public void astronomicalCalendarCloneRemainsEqual() {
		AstronomicalCalendar astronomicalCalendar = new AstronomicalCalendar(
				new GeoLocation("Lakewood", 40.0828, -74.2094, ZoneId.of("America/New_York")));
		astronomicalCalendar.getAstronomicalCalculator().setRefraction(0.6);
		astronomicalCalendar.getAstronomicalCalculator().setSolarRadius(0.3);
		astronomicalCalendar.getAstronomicalCalculator().setEarthRadius(6400);

		AstronomicalCalendar clone = (AstronomicalCalendar) astronomicalCalendar.clone();

		assertEquals(astronomicalCalendar, clone);
		assertEquals(astronomicalCalendar.hashCode(), clone.hashCode());
	}

	@Test
	public void zmanimCalendarEqualityIncludesZmanimSettings() {
		ZmanimCalendar zmanimCalendar = new ZmanimCalendar(
				new GeoLocation("Lakewood", 40.0828, -74.2094, ZoneId.of("America/New_York")));
		ZmanimCalendar clone = (ZmanimCalendar) zmanimCalendar.clone();

		assertEquals(zmanimCalendar, clone);
		assertEquals(zmanimCalendar.hashCode(), clone.hashCode());

		clone.setUseElevation(true);
		clone.setUseAstronomicalChatzosForOtherZmanim(true);
		clone.setCandleLightingOffset(40);

		assertFalse(zmanimCalendar.equals(clone));
	}

	@Test
	public void comprehensiveZmanimCalendarEqualityIncludesAteretTorahOffset() {
		ComprehensiveZmanimCalendar comprehensiveZmanimCalendar = new ComprehensiveZmanimCalendar(
				new GeoLocation("Lakewood", 40.0828, -74.2094, ZoneId.of("America/New_York")));
		ComprehensiveZmanimCalendar clone = (ComprehensiveZmanimCalendar) comprehensiveZmanimCalendar.clone();

		assertEquals(comprehensiveZmanimCalendar, clone);
		assertEquals(comprehensiveZmanimCalendar.hashCode(), clone.hashCode());

		clone.setAteretTorahSunsetOffset(30);

		assertFalse(comprehensiveZmanimCalendar.equals(clone));
	}
}
