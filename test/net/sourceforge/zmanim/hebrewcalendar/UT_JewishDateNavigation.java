/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_JewishDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, JewishDate.NISSAN, 1);
		assertEquals(5, jewishDate.getGregorianDayOfMonth());
		assertEquals(Calendar.APRIL, jewishDate.getGregorianMonth());
		assertEquals(2011, jewishDate.getGregorianYear());
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishDate class spun through a never-ending loop...

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, JewishDate.TISHREI, 1);
		assertEquals(9, jewishDate.getGregorianDayOfMonth());
		assertEquals(Calendar.SEPTEMBER, jewishDate.getGregorianMonth());
		assertEquals(2010, jewishDate.getGregorianYear());
	}


} // End of UT_JewishDateNavigation class
