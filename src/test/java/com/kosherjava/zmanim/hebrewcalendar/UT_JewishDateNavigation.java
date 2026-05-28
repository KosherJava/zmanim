/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 *
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_JewishDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, JewishDate.NISSAN, 1);
        LocalDate localDate = jewishDate.getLocalDate();
		assertEquals(5, localDate.getDayOfMonth());
		assertEquals(Month.APRIL, localDate.getMonth());
		assertEquals(2011, localDate.getYear());
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishDate class spun through a never-ending loop...

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, JewishDate.TISHREI, 1);
        LocalDate localDate = jewishDate.getLocalDate();
		assertEquals(9, localDate.getDayOfMonth());
		assertEquals(Month.SEPTEMBER, localDate.getMonth());
		assertEquals(2010, localDate.getYear());
	}

	@Test
	public void addYearsUsesConfiguredAdarMonthWhenMovingToLeapYear() {

		JewishDate adarAlephDate = new JewishDate();
		adarAlephDate.setJewishDate(5783, JewishDate.ADAR, 10);
		adarAlephDate.plusYears(1, true);
		assertEquals(5784, adarAlephDate.getJewishYear());
		assertEquals(JewishDate.ADAR, adarAlephDate.getJewishMonth());
		assertEquals(10, adarAlephDate.getJewishDayOfMonth());

		JewishDate adarBeisDate = new JewishDate();
		adarBeisDate.setJewishDate(5783, JewishDate.ADAR, 10);
		adarBeisDate.plusYears(1, false);
		assertEquals(5784, adarBeisDate.getJewishYear());
		assertEquals(JewishDate.ADAR_II, adarBeisDate.getJewishMonth());
		assertEquals(10, adarBeisDate.getJewishDayOfMonth());
	}


} // End of UT_JewishDateNavigation class
