/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

/**
 * Coverage for {@link GeoLocation}. The geodesic (Vincenty) and rhumb-line results are a <b>Tier-A independently
 * verifiable</b> anchor: the coordinates are the classic Ordnance Survey reference points (Cornwall and Scotland). The
 * rest is regression / behavioral coverage of construction, validation, the local-mean-time / antimeridian helpers, and
 * {@code equals}/{@code hashCode}/{@code clone}/{@code toXML}.
 *
 * @author Test coverage
 */
public class GeoLocationTest {

	private static final ZoneId NY = ZoneId.of("America/New_York");
	private static final Instant JAN_1_2017 = Instant.parse("2017-01-01T00:00:00Z");

	@Test
	public void constructWithDefaults() {
		GeoLocation geoLocation = new GeoLocation();
		assertEquals(51.4772, geoLocation.getLatitude(), 0);
		assertEquals(0.0, geoLocation.getLongitude(), 0);
		assertEquals(ZoneId.of("GMT"), geoLocation.getZoneId());
		assertEquals(0.0, geoLocation.getElevation(), 0);
		assertEquals("Greenwich, England", geoLocation.getLocationName());
	}

	@Test
	public void constructWithData() {
		GeoLocation geoLocation = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		assertEquals(40.0828, geoLocation.getLatitude(), 0);
		assertEquals(-74.2094, geoLocation.getLongitude(), 0);
		assertEquals(NY, geoLocation.getZoneId());
		assertEquals(20.0, geoLocation.getElevation(), 0);
		assertEquals("Lakewood, NJ", geoLocation.getLocationName());
	}

	@Test
	public void settersUpdateValues() {
		GeoLocation geoLocation = new GeoLocation();
		geoLocation.setLatitude(41.1181036);
		geoLocation.setLongitude(-74.2094);
		geoLocation.setZoneId(NY);
		geoLocation.setElevation(20);
		geoLocation.setLocationName("Lakewood, NJ");

		assertEquals(41.1181036, geoLocation.getLatitude(), 0);
		assertEquals(-74.2094, geoLocation.getLongitude(), 0);
		assertEquals(NY, geoLocation.getZoneId());
		assertEquals(20.0, geoLocation.getElevation(), 0);
		assertEquals("Lakewood, NJ", geoLocation.getLocationName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setLatitudeRejectsOutOfRange() {
		new GeoLocation().setLatitude(90.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setLatitudeRejectsNaN() {
		new GeoLocation().setLatitude(Double.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setLongitudeRejectsOutOfRange() {
		new GeoLocation().setLongitude(-180.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setLongitudeRejectsNaN() {
		new GeoLocation().setLongitude(Double.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setElevationRejectsNegative() {
		new GeoLocation().setElevation(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setElevationRejectsInfinite() {
		new GeoLocation().setElevation(Double.POSITIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorValidatesLatitude() {
		new GeoLocation("bad", 100, 0, ZoneId.of("GMT"));
	}

	@Test
	public void getLocalMeanTimeOffset() {
		assertEquals(0L, new GeoLocation().getLocalMeanTimeOffset(JAN_1_2017));

		GeoLocation lakewood = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		assertEquals(189744L, lakewood.getLocalMeanTimeOffset(JAN_1_2017));
	}

	@Test
	public void antimeridianAdjustment() {
		assertEquals(0, new GeoLocation().getAntimeridianAdjustment(JAN_1_2017));

		GeoLocation ny = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		assertEquals(0, ny.getAntimeridianAdjustment(JAN_1_2017));

		// Apia, Samoa (UTC+14 in January) is well east of its longitude-implied zone: roll the date back a day.
		GeoLocation apia = new GeoLocation("Apia, Samoa", -13.8599098, -171.8031745, 1858, ZoneId.of("Pacific/Apia"));
		assertEquals(-1, apia.getAntimeridianAdjustment(JAN_1_2017));

		// A location east of the dateline forced onto a UTC-12 zone: roll the date forward a day.
		GeoLocation westOfLine = new GeoLocation("GMT +12", -13.8599098, 179, 0, ZoneId.of("Etc/GMT+12"));
		assertEquals(1, westOfLine.getAntimeridianAdjustment(JAN_1_2017));
	}

	/*
	 * Tier-A: Vincenty and rhumb-line results against the classic Ordnance Survey reference points.
	 */
	@Test
	public void vincentyFormulae() {
		GeoLocation pointA = new GeoLocation("A", 50.06632222222222, -5.71475, ZoneId.of("Etc/GMT+12"));
		GeoLocation pointB = new GeoLocation("B", 58.64402222222222, -3.0700944444444445, ZoneId.of("Etc/GMT+12"));

		assertEquals(9.14186191, pointA.getGeodesicInitialBearing(pointB), 1e-8);
		assertEquals(11.29720127, pointA.getGeodesicFinalBearing(pointB), 1e-8);
		assertEquals(969954.11445043, pointA.getGeodesicDistance(pointB), 1e-5);
	}

	@Test
	public void geodesicDistanceForCoincidentPointsIsZero() {
		GeoLocation point = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, NY);
		assertEquals(0.0, point.getGeodesicDistance(point), 0);
	}

	@Test
	public void rhumbLine() {
		GeoLocation pointA = new GeoLocation("A", 50.06638888888889, -5.714722222222222, ZoneId.of("Etc/GMT+12"));
		GeoLocation pointB = new GeoLocation("B", 58.64388888888889, -3.07, ZoneId.of("Etc/GMT+12"));

		assertEquals(10.14069288, pointA.getRhumbLineBearing(pointB), 1e-8);
		assertEquals(969995.8368008, pointA.getRhumbLineDistance(pointB), 1e-5);
	}

	@Test
	public void equalsAndHashCode() {
		GeoLocation a = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		GeoLocation b = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());

		GeoLocation c = new GeoLocation("Lakewood, NJ", 41.0, -74.2094, 20, NY);
		assertNotEquals(a, c);
	}

	@Test
	public void cloneProducesIndependentCopy() {
		GeoLocation geoLocation = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		GeoLocation copy = (GeoLocation) geoLocation.clone();

		assertNotSame(geoLocation, copy);
		assertEquals(geoLocation, copy);

		copy.setLatitude(10);
		assertEquals(40.0828, geoLocation.getLatitude(), 0);
		assertEquals(10.0, copy.getLatitude(), 0);
	}

	@Test
	public void toXmlContainsFields() {
		GeoLocation geoLocation = new GeoLocation("Lakewood, NJ", 40.0828, -74.2094, 20, NY);
		String xml = geoLocation.toXML();
		assertTrue(xml.contains("<LocationName>Lakewood, NJ</LocationName>"));
		assertTrue(xml.contains("<Latitude>40.0828</Latitude>"));
		assertTrue(xml.contains("<Longitude>-74.2094</Longitude>"));
		assertTrue(xml.contains("<Elevation>20.0 Meters</Elevation>"));
		assertTrue(xml.contains("<TimezoneName>America/New_York</TimezoneName>"));
	}
}
