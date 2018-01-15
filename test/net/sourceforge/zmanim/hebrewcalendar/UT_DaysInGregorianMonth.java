/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Verify the calculation of the number of days in a month. Not too hard...just the rules about when February
 *  has 28 or 29 days...
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_DaysInGregorianMonth {


	@Test
	public void testDaysInMonth() {

		JewishDate hebrewDate = new JewishDate();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		hebrewDate.setDate(cal);

		assertDaysInMonth(false, hebrewDate);
	}



	@Test
	public void testDaysInMonthLeapYear() {

		JewishDate hebrewDate = new JewishDate();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		hebrewDate.setDate(cal);

		assertDaysInMonth(true, hebrewDate);
	}


	@Test
	public void testDaysInMonth100Year() {

		JewishDate hebrewDate = new JewishDate();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2100);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		hebrewDate.setDate(cal);

		assertDaysInMonth(false, hebrewDate);
	}


	@Test
	public void testDaysInMonth400Year() {

		JewishDate hebrewDate = new JewishDate();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		hebrewDate.setDate(cal);

		assertDaysInMonth(true, hebrewDate);
	}


	private void assertDaysInMonth(
		boolean     febIsLeap,
		JewishDate  hebrewDate
	) {

		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_JANUARY));
		assertEquals(febIsLeap ? 29 : 28, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_FEBRUARY));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_MARCH));
		assertEquals(30, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_APRIL));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_MAY));
		assertEquals(30, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_JUNE));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_JULY));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_AUGUST));
		assertEquals(30, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_SEPTEMBER));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_OCTOBER));
		assertEquals(30, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_NOVEMBER));
		assertEquals(31, hebrewDate.getLastDayOfGregorianMonth(JewishDate.MONTH_DECEMBER));
	}


} // End of UT_DaysInGregorianMonth class
