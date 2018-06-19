/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static net.sourceforge.zmanim.hebrewcalendar.JewishDate.TISHREI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Validate the days in a Hebrew month (in various types of years) are correct.
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_DaysInJewishMonth {

	@Test
	public void daysInMonthsInHaserYear() {

		assertHaser(5773);
		assertHaser(5777);
		assertHaser(5781);
		assertHaserLeap(5784);
		assertHaserLeap(5790);
		assertHaserLeap(5793);
	}


	@Test
	public void daysInMonthsInQesidrahYear() {

		assertQesidrah(5769);
		assertQesidrah(5772);
		assertQesidrah(5778);
		assertQesidrah(5786);
		assertQesidrah(5789);
		assertQesidrah(5792);

		assertQesidrahLeap(5782);
	}


	@Test
	public void daysInMonthsInShalemYear() {

		assertShalem(5770);
		assertShalem(5780);
		assertShalem(5783);
		assertShalem(5785);
		assertShalem(5788);
		assertShalem(5791);
		assertShalem(5794);

		assertShalemLeap(5771);
		assertShalemLeap(5774);
		assertShalemLeap(5776);
		assertShalemLeap(5779);
		assertShalemLeap(5787);
		assertShalemLeap(5795);
	}


	private void assertHaser(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertFalse(jewishDate.isCheshvanLong());
		assertTrue(jewishDate.isKislevShort());
	}


	private void assertHaserLeap(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertHaser(year);
		assertTrue(jewishDate.isJewishLeapYear());
	}


	private void assertQesidrah(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertFalse(jewishDate.isCheshvanLong(  ));
		assertFalse(jewishDate.isKislevShort(  ));
	}


	private void assertQesidrahLeap(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertQesidrah(year);
		assertTrue(jewishDate.isJewishLeapYear(  ));
	}


	private void assertShalem(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertTrue(jewishDate.isCheshvanLong(  ));
		assertFalse(jewishDate.isKislevShort(  ));
	}


	private void assertShalemLeap(int year) {
		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishYear(year);

		assertShalem(year);
		assertTrue(jewishDate.isJewishLeapYear(  ));
	}

	@Test
	public void earlyGregorian() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(1582, Calendar.OCTOBER, 15);
		JewishDate jewishDate = new JewishDate(gregorianCalendar);
		assertEquals(Calendar.FRIDAY, gregorianCalendar.get(Calendar.DAY_OF_WEEK));

		assertEquals(5343, jewishDate.getJewishYear());
		assertEquals(TISHREI, jewishDate.getJewishMonth());
		assertEquals(19, jewishDate.getJewishDayOfMonth());
	}

} // End of UT_DaysInJewishMonth class
