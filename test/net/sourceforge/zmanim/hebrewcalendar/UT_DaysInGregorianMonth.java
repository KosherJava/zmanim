/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

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

		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.JANUARY) );
		Assert.assertEquals( febIsLeap ? 29 : 28, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.FEBRUARY ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.MARCH ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.APRIL ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.MAY ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.JUNE ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.JULY ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.AUGUST ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.SEPTEMBER ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.OCTOBER ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.NOVEMBER ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 + Calendar.DECEMBER ) );
	}


} // End of UT_DaysInGregorianMonth class
