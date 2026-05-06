/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;

/**
 *
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_JewishDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, 1, 1);
        LocalDate localDate = jewishDate.getLocalDate();
		Assert.assertEquals(5, localDate.getDayOfMonth());
		Assert.assertEquals(Month.APRIL, localDate.getMonth());
		Assert.assertEquals(2011, localDate.getYear());
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishDate class spun through a never-ending loop...

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, 7, 1);
        LocalDate localDate = jewishDate.getLocalDate();
		Assert.assertEquals(9, localDate.getDayOfMonth());
		Assert.assertEquals(Month.SEPTEMBER, localDate.getMonth());
		Assert.assertEquals(2010, localDate.getYear());
	}

	@Test
	public void addYearsUsesConfiguredAdarMonthWhenMovingToLeapYear() {

		JewishDate adarAlephDate = new JewishDate();
		adarAlephDate.setJewishDate(5783, JewishDate.ADAR, 10);
		adarAlephDate.plusYears(1, true);
		Assert.assertEquals(5784, adarAlephDate.getJewishYear());
		Assert.assertEquals(JewishDate.ADAR, adarAlephDate.getJewishMonth());
		Assert.assertEquals(10, adarAlephDate.getJewishDayOfMonth());

		JewishDate adarBeisDate = new JewishDate();
		adarBeisDate.setJewishDate(5783, JewishDate.ADAR, 10);
		adarBeisDate.plusYears(1, false);
		Assert.assertEquals(5784, adarBeisDate.getJewishYear());
		Assert.assertEquals(JewishDate.ADAR_II, adarBeisDate.getJewishMonth());
		Assert.assertEquals(10, adarBeisDate.getJewishDayOfMonth());
	}


} // End of UT_JewishDateNavigation class
