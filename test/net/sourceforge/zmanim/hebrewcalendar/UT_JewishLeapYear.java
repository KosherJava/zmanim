/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Verify correct calculations of when a Hebrew leap year occurs.
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_JewishLeapYear {


	@Test
	public void isLeapYear() {

		shouldBeLeapYear(5160);
		shouldNotBeLeapYear(5536);

		shouldNotBeLeapYear(5770);
		shouldBeLeapYear(5771);
		shouldNotBeLeapYear(5772);
		shouldNotBeLeapYear(5773);
		shouldBeLeapYear(5774);
		shouldNotBeLeapYear(5775);
		shouldBeLeapYear(5776);
		shouldNotBeLeapYear(5777);
		shouldNotBeLeapYear(5778);
		shouldBeLeapYear(5779);
		shouldNotBeLeapYear(5780);
		shouldNotBeLeapYear(5781);
		shouldBeLeapYear(5782);
		shouldNotBeLeapYear(5783);
		shouldBeLeapYear(5784);
		shouldNotBeLeapYear(5785);
		shouldNotBeLeapYear(5786);
		shouldBeLeapYear(5787);
		shouldNotBeLeapYear(5788);
		shouldNotBeLeapYear(5789);
		shouldBeLeapYear(5790);
		shouldNotBeLeapYear(5791);
		shouldNotBeLeapYear(5792);
		shouldBeLeapYear(5793);
		shouldNotBeLeapYear(5794);
		shouldBeLeapYear(5795);
	}


	private void shouldBeLeapYear(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertTrue(jewishDate.isJewishLeapYear(  ));
	}


	private void shouldNotBeLeapYear(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertFalse(jewishDate.isJewishLeapYear(  ));
	}

	@Test
	public void icu() {
		for (int y = 0; y < 10000; y++) {
			assertEquals(isLeapYearICU(y), JewishDate.isJewishLeapYear(y));
		}
	}

	/** android.icu.util.HebrewCalendar */
	public static boolean isLeapYearICU(int year) {
		int x = (year * 12 + 17) % 19;
		return x >= ((x < 0) ? -7 : 12);
	}

} // End of UT_JewishLeapYear class
