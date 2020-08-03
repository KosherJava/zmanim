package com.kosherjava.zmanim.hebrewcalendar;

import java.util.Calendar;

import org.junit.*;

import junit.framework.TestCase;
public class UT_YerushalmiTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {

		JewishCalendar jewishCalendar = new JewishCalendar(5777,6,10);
		Assert.assertEquals(8, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(29, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectDaf2() {
		

		JewishCalendar jewishCalendar = new JewishCalendar(5744,9,1);
		Assert.assertEquals(26, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(32, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}
	
	@Test
	public void testCorrectDaf3() {

		JewishCalendar jewishCalendar = new JewishCalendar(5782,3,1);
		Assert.assertEquals(15, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(33, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectSpecialDate() {

		JewishCalendar jewishCalendar = new JewishCalendar(5775,7,10);
		Assert.assertEquals(0, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(39, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

}
