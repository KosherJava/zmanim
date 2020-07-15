/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

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
		Assert.assertEquals(5, jewishDate.getGregorianDayOfMonth());
		Assert.assertEquals(3, jewishDate.getGregorianMonth());
		Assert.assertEquals(2011, jewishDate.getGregorianYear());
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishDate class spun through a never-ending loop...

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, 7, 1);
		Assert.assertEquals(9, jewishDate.getGregorianDayOfMonth());
		Assert.assertEquals(8, jewishDate.getGregorianMonth());
		Assert.assertEquals(2010, jewishDate.getGregorianYear());
	}


} // End of UT_JewishDateNavigation class
