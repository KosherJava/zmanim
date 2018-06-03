/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings({"MagicNumber"})
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 31);

		JewishDate hebrewDate = new JewishDate(cal);
		assertEquals(5771, hebrewDate.getJewishYear());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(26, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward();
		assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 28);
		hebrewDate.setDate(cal);
		assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(24, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward();
		assertEquals(Calendar.MARCH, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(25, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.APRIL, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.ADAR_II, hebrewDate.getJewishMonth());
		assertEquals(26, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.MAY, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.JUNE, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		assertEquals(28, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.JULY, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		assertEquals(29, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.AUGUST, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		assertEquals(1, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.AUGUST);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.SEPTEMBER, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		assertEquals(2, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.OCTOBER, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		assertEquals(3, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.NOVEMBER, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(5772, hebrewDate.getJewishYear());
		assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		assertEquals(4, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(Calendar.DECEMBER, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		assertEquals(5, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward();
		assertEquals(2012, hebrewDate.getGregorianYear());
		assertEquals(Calendar.JANUARY, hebrewDate.getGregorianMonth());
		assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		assertEquals(6, hebrewDate.getJewishDayOfMonth());
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);

		JewishDate hebrewDate = new JewishDate(cal);
		hebrewDate.back();
		assertEquals(2010, hebrewDate.getGregorianYear());
		assertEquals(Calendar.DECEMBER, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		assertEquals(24, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.YEAR, 2010);
		hebrewDate.setDate(cal);
		assertEquals(5771, hebrewDate.getJewishYear());
		assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		assertEquals(24, hebrewDate.getJewishDayOfMonth());
		hebrewDate.back();
		assertEquals(Calendar.NOVEMBER, hebrewDate.getGregorianMonth());
		assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		assertEquals(5771, hebrewDate.getJewishYear());
		assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.OCTOBER, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.SEPTEMBER, hebrewDate.getGregorianMonth());
		assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		assertEquals(22, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.AUGUST, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(5770, hebrewDate.getJewishYear());
		assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		assertEquals(21, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.AUGUST);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.JULY, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		assertEquals(20, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JULY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.JUNE, hebrewDate.getGregorianMonth());
		assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.TAMMUZ, hebrewDate.getJewishMonth());
		assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.MAY, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MAY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.APRIL, hebrewDate.getGregorianMonth());
		assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.MARCH, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(14, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		assertEquals(Calendar.JANUARY, hebrewDate.getGregorianMonth());
		assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

	}



} // End of UT_GregorianDateNavigation class
