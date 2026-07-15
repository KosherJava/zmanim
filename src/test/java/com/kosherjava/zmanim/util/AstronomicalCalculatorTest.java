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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Coverage for the {@link AstronomicalCalculator} abstract base: the pluggable-calculator contract ({@code getDefault},
 * naming, {@code equals}/{@code clone}), the refraction / solar-radius / earth-radius configuration, the date-based
 * {@link AstronomicalCalculator#getApparentSolarRadius(LocalDate) apparent solar radius}, and the shared degree-based
 * trig helpers. The trig helpers are <b>Tier-A</b> checks verified against {@link Math}; this test lives in the
 * {@code util} package so it can reach those {@code protected static} helpers.
 *
 * @author Test coverage
 */
public class AstronomicalCalculatorTest {

	@Test
	public void getDefaultReturnsNoaaCalculator() {
		AstronomicalCalculator calculator = AstronomicalCalculator.getDefault();
		assertTrue(calculator instanceof NOAACalculator);
	}

	@Test
	public void calculatorNames() {
		assertEquals("US National Oceanic and Atmospheric Administration Algorithm",
				new NOAACalculator().getCalculatorName());
		assertEquals("US Naval Almanac Algorithm", new SunTimesCalculator().getCalculatorName());
		assertEquals("Jean Meeus Higher-Accuracy (VSOP87) Algorithm", new MeeusCalculator().getCalculatorName());
		assertEquals("NREL Solar Position Algorithm", new SPACalculator().getCalculatorName());
	}

	@Test
	public void defaultConfiguration() {
		AstronomicalCalculator calculator = new NOAACalculator();
		assertEquals(34 / 60d, calculator.getRefraction(), 0);
		assertEquals(16 / 60d, calculator.getSolarRadius(), 0);
		assertEquals(6371.0088, calculator.getEarthRadius(), 0);
		assertTrue(calculator.isUseApparentSolarRadius());
	}

	@Test
	public void refractionAndEarthRadiusSetters() {
		AstronomicalCalculator calculator = new NOAACalculator();
		calculator.setRefraction(0.5);
		calculator.setEarthRadius(6371.0);
		assertEquals(0.5, calculator.getRefraction(), 0);
		assertEquals(6371.0, calculator.getEarthRadius(), 0);
	}

	@Test
	public void setSolarRadiusDisablesApparentRadius() {
		AstronomicalCalculator calculator = new NOAACalculator();
		calculator.setSolarRadius(16 / 60d);
		assertEquals(16 / 60d, calculator.getSolarRadius(), 0);
		assertFalse(calculator.isUseApparentSolarRadius());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setSolarRadiusRejectsNegative() {
		new NOAACalculator().setSolarRadius(-1);
	}

	@Test
	public void apparentSolarRadius() {
		AstronomicalCalculator calculator = new NOAACalculator();
		// null date falls back to the fixed 16' radius
		assertEquals(16 / 60d, calculator.getApparentSolarRadius(null), 0);
		// table is keyed by day-of-year against the (common-year) reference year 2050
		assertEquals(0.27108024, calculator.getApparentSolarRadius(LocalDate.of(2050, 1, 1)), 1e-8);
		// the perihelion (~Jan 3) is near the annual maximum, the aphelion (~Jul 5) near the minimum
		assertTrue(calculator.getApparentSolarRadius(LocalDate.of(2020, 1, 3))
				> calculator.getApparentSolarRadius(LocalDate.of(2020, 7, 5)));
		// Feb 29 resolves to Feb 28's value via withYear(2050)
		assertEquals(calculator.getApparentSolarRadius(LocalDate.of(2020, 2, 28)),
				calculator.getApparentSolarRadius(LocalDate.of(2020, 2, 29)), 0);
	}

	@Test
	public void equalsAndHashCode() {
		AstronomicalCalculator a = new NOAACalculator();
		AstronomicalCalculator b = new NOAACalculator();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());

		b.setRefraction(0.5);
		assertNotEquals(a, b);

		// different concrete classes with identical settings are not equal
		assertNotEquals(new NOAACalculator(), new SunTimesCalculator());
	}

	@Test
	public void cloneProducesEqualIndependentCopy() {
		AstronomicalCalculator original = new NOAACalculator();
		AstronomicalCalculator clone = original.clone();
		assertNotSame(original, clone);
		assertEquals(original, clone);

		clone.setRefraction(0.1);
		assertNotEquals(original, clone);
	}

	/*
	 * Tier-A: the degree-based trig helpers must agree with java.lang.Math. They are protected static, reachable here
	 * because this test shares the com.kosherjava.zmanim.util package.
	 */
	@Test
	public void degreeTrigHelpersMatchMath() {
		double[] angles = {0, 30, 45, 60, 90, 123.456, -17.5, 200};
		for (double angle : angles) {
			assertEquals("sin " + angle, Math.sin(Math.toRadians(angle)),
					AstronomicalCalculator.sinDegrees(angle), 1e-12);
			assertEquals("cos " + angle, Math.cos(Math.toRadians(angle)),
					AstronomicalCalculator.cosDegrees(angle), 1e-12);
			assertEquals("tan " + angle, Math.tan(Math.toRadians(angle)),
					AstronomicalCalculator.tanDegrees(angle), 1e-9);
		}
		double[] ratios = {-1, -0.5, 0, 0.25, 0.5, 1};
		for (double ratio : ratios) {
			assertEquals("asin " + ratio, Math.toDegrees(Math.asin(ratio)),
					AstronomicalCalculator.asinDegrees(ratio), 1e-12);
			assertEquals("acos " + ratio, Math.toDegrees(Math.acos(ratio)),
					AstronomicalCalculator.acosDegrees(ratio), 1e-12);
		}
	}
}
