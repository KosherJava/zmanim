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
		Assert.assertEquals( jewishDate.getGregorianMonth(), Calendar.APRIL );
		Assert.assertEquals( jewishDate.getGregorianYear(), 2011 );


	}


} // End of UT_JewishDateNavigation class
