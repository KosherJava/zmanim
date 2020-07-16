/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 31);

		JewishDate hebrewDate = new JewishDate(cal);
		Assert.assertEquals(5771, hebrewDate.getJewishYear());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(1, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 28);
		hebrewDate.setDate(cal);
		Assert.assertEquals(1, hebrewDate.getGregorianMonth());
		Assert.assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(2, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(25, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(3, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(13, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(4, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(1, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(5, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(2, hebrewDate.getJewishMonth());
		Assert.assertEquals(28, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(6, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(3, hebrewDate.getJewishMonth());
		Assert.assertEquals(29, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(7, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5, hebrewDate.getJewishMonth());
		Assert.assertEquals(1, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.AUGUST);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(8, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(6, hebrewDate.getJewishMonth());
		Assert.assertEquals(2, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(9, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(7, hebrewDate.getJewishMonth());
		Assert.assertEquals(3, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(10, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5772, hebrewDate.getJewishYear());
		Assert.assertEquals(8, hebrewDate.getJewishMonth());
		Assert.assertEquals(4, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(11, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(9, hebrewDate.getJewishMonth());
		Assert.assertEquals(5, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(2012, hebrewDate.getGregorianYear());
		Assert.assertEquals(0, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(10, hebrewDate.getJewishMonth());
		Assert.assertEquals(6, hebrewDate.getJewishDayOfMonth());
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);

		JewishDate hebrewDate = new JewishDate(cal);
		hebrewDate.back();
		Assert.assertEquals(2010, hebrewDate.getGregorianYear());
		Assert.assertEquals(11, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(10, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.YEAR, 2010);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(10, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(9, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(9, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(8, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(8, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(7, hebrewDate.getJewishMonth());
		Assert.assertEquals(22, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(7, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5770, hebrewDate.getJewishYear());
		Assert.assertEquals(6, hebrewDate.getJewishMonth());
		Assert.assertEquals(21, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.AUGUST);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(6, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5, hebrewDate.getJewishMonth());
		Assert.assertEquals(20, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JULY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(5, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(4, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(4, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(3, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MAY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(3, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(2, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(2, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(1, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(1, hebrewDate.getGregorianMonth());
		Assert.assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(14, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(0, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

	}



} // End of UT_GregorianDateNavigation class
