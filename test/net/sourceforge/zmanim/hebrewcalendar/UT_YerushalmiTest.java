package net.sourceforge.zmanim.hebrewcalendar;

import java.util.Calendar;

import org.junit.*;

import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

public class UT_YerushalmiTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {

		JewishCalendar jewishCalendar = new JewishCalendar(5777,6,10);
		assertEquals(8, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(29, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectDaf2() {
		

		JewishCalendar jewishCalendar = new JewishCalendar(5744,9,1);
		assertEquals(26, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(32, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}
	
	@Test
	public void testCorrectDaf3() {

		JewishCalendar jewishCalendar = new JewishCalendar(5782,3,1);
		assertEquals(15, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(33, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectSpecialDate() {

		JewishCalendar jewishCalendar = new JewishCalendar(5775,7,10);
		assertEquals(0, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(39, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

}
