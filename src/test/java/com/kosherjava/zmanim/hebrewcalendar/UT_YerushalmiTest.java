package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UT_YerushalmiTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {
		JewishCalendar jewishCalendar = new JewishCalendar(5777, JewishDate.ELUL, 10);
		assertEquals(8, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(29, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectDaf2() {
		JewishCalendar jewishCalendar = new JewishCalendar(5744, JewishDate.KISLEV, 1);
		assertEquals(26, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(32, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}
	
	@Test
	public void testCorrectDaf3() {
		JewishCalendar jewishCalendar = new JewishCalendar(5782, JewishDate.SIVAN, 1);
		assertEquals(15, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(33, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectSpecialDate() {
		JewishCalendar jewishCalendar = new JewishCalendar(5775, JewishDate.TISHREI, 10);
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5783, JewishDate.AV, 9);
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5775, JewishDate.AV, 10);// 9 Av delayed to Sunday 10 Av
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

}
