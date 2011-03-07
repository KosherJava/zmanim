/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_JewishDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate( 5711, 1, 1 );
		Assert.assertEquals( jewishDate.getGregorianDayOfMonth(), 5 );
		Assert.assertEquals( jewishDate.getGregorianMonth(), 4 );
		Assert.assertEquals( jewishDate.getGregorianYear(), 2011 );
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishDate class spun through a never-ending loop...

		JewishDate jewishDate = new JewishDate();
		jewishDate.setJewishDate(5771, 7, 1);
		Assert.assertEquals( jewishDate.getGregorianDayOfMonth(), 9 );
		Assert.assertEquals( jewishDate.getGregorianMonth(), 9 );
		Assert.assertEquals( jewishDate.getGregorianYear(), 2010 );
	}


} // End of UT_JewishDateNavigation class
