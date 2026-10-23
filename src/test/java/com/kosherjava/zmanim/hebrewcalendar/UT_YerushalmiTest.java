package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;
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
		JewishCalendar jewishCalendar = new JewishCalendar(5777,JewishCalendar.ELUL,10);
		Assert.assertEquals(8, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(29, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectDaf2() {
		JewishCalendar jewishCalendar = new JewishCalendar(5744,JewishCalendar.KISLEV,1);
		Assert.assertEquals(26, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(32, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}
	
	@Test
	public void testCorrectDaf3() {
		JewishCalendar jewishCalendar = new JewishCalendar(5782,JewishCalendar.SIVAN,1);
		Assert.assertEquals(15, jewishCalendar.getDafYomiYerushalmi().getDaf());
		Assert.assertEquals(33, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectSpecialDate() {
		JewishCalendar jewishCalendar = new JewishCalendar(5775,JewishCalendar.TISHREI,10);
		Assert.assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5783,JewishCalendar.AV,9);
		Assert.assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5775,JewishCalendar.AV,10);// 9 Av delayed to Sunday 10 Av
		Assert.assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	/**
	 * The cycle ending in 2172 is extended for Tisha B'Av 5932. It still has every daf, ending with Niddah 13 on
	 * 7 August 2172.
	 */
	@Test
	public void testCycleExtendedForTishaBav() {
		assertDaf(LocalDate.of(2172, 8, 6), 38, 12);
		assertDaf(LocalDate.of(2172, 8, 7), 38, 13);
		assertDaf(LocalDate.of(2172, 8, 8), 0, 1);
	}

	/**
	 * Tisha B'Av 6240 falls the day before the last daf of a cycle. The cycle still has every daf, ending with
	 * Niddah 13 on 16 August 2480, and the next one starts with Berachos 1.
	 */
	@Test
	public void testCycleWithTishaBavBeforeItsLastDaf() {
		assertDaf(LocalDate.of(2480, 8, 14), 38, 12);
		Assert.assertNull(new JewishCalendar(LocalDate.of(2480, 8, 15)).getDafYomiYerushalmi());
		assertDaf(LocalDate.of(2480, 8, 16), 38, 13);
		assertDaf(LocalDate.of(2480, 8, 17), 0, 1);
	}

	private static void assertDaf(LocalDate date, int masechtaNumber, int daf) {
		Daf dafYomi = new JewishCalendar(date).getDafYomiYerushalmi();
		Assert.assertEquals(date.toString(), masechtaNumber, dafYomi.getMasechtaNumber());
		Assert.assertEquals(date.toString(), daf, dafYomi.getDaf());
	}

}
