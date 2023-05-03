package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.Assert;
import org.junit.Test;

public class YomiCalculatorTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {
		JewishCalendar jewishCalendar = new JewishCalendar(5685, JewishDate.KISLEV, 12);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(5, daf.getMasechtaNumber());
		Assert.assertEquals(2, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}

	@Test
	public void testCorrectDaf2() {
		JewishCalendar jewishCalendar = new JewishCalendar(5736, JewishDate.ELUL, 26);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(4, daf.getMasechtaNumber());
		Assert.assertEquals(14, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}
	
	@Test
	public void testCorrectDaf3() {
		JewishCalendar jewishCalendar = new JewishCalendar(5777, JewishDate.ELUL, 10);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(23, daf.getMasechtaNumber());
		Assert.assertEquals(47, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}
}
