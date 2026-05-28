/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

        LocalDate localDate = LocalDate.of(2011, Month.JANUARY, 31);

		JewishDate hebrewDate = new JewishDate(localDate);
		assertEquals(5771, hebrewDate.getJewishYear());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(26, hebrewDate.getJewishDayOfMonth());

		hebrewDate.plusDays(1);
		assertEquals(Month.FEBRUARY, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(27, hebrewDate.getJewishDayOfMonth());
        
        localDate = LocalDate.of(2011, Month.FEBRUARY, 28);
        hebrewDate = new JewishDate(localDate);
		assertEquals(Month.FEBRUARY, hebrewDate.getLocalDate().getMonth());
		assertEquals(28, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(24, hebrewDate.getJewishDayOfMonth());

		hebrewDate.plusDays(1);
		assertEquals(Month.MARCH, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(25, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.MARCH, 31);
        hebrewDate = new JewishDate(localDate);
		hebrewDate.plusDays(1);
		assertEquals(Month.APRIL, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.ADAR_II, hebrewDate.getJewishMonth());
		assertEquals(26, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.APRIL, 30);
        hebrewDate = new JewishDate(localDate);
		hebrewDate.plusDays(1);
		assertEquals(Month.MAY, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		assertEquals(27, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.MAY, 31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.JUNE, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		assertEquals(28, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.JUNE, 30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.JULY, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		assertEquals(29, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.JULY, 31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.AUGUST, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		assertEquals(1, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.AUGUST, 31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.SEPTEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		assertEquals(2, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.SEPTEMBER, 30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.OCTOBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		assertEquals(3, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.OCTOBER, 31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.NOVEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(5772, hebrewDate.getJewishYear());
		assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		assertEquals(4, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.NOVEMBER, 30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(Month.DECEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		assertEquals(5, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.DECEMBER, 31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		assertEquals(2012, hebrewDate.getLocalDate().getYear());
		assertEquals(Month.JANUARY, hebrewDate.getLocalDate().getMonth());
		assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		assertEquals(6, hebrewDate.getJewishDayOfMonth());
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		LocalDate localDate = LocalDate.of(2011, Month.JANUARY, 1);
		JewishDate hebrewDate = new JewishDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(2010, hebrewDate.getLocalDate().getYear());
		assertEquals(Month.DECEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		assertEquals(24, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.DECEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.NOVEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		assertEquals(23, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.NOVEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.OCTOBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		assertEquals(23, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.OCTOBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.SEPTEMBER, hebrewDate.getLocalDate().getMonth());
		assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		assertEquals(22, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.SEPTEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.AUGUST, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(5770, hebrewDate.getJewishYear());
		assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		assertEquals(21, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.AUGUST, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.JULY, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		assertEquals(20, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.JULY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.JUNE, hebrewDate.getLocalDate().getMonth());
		assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.TAMMUZ, hebrewDate.getJewishMonth());
		assertEquals(18, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.JUNE, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.MAY, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		assertEquals(18, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.MAY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.APRIL, hebrewDate.getLocalDate().getMonth());
		assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.APRIL, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.MARCH, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.MARCH, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.FEBRUARY, hebrewDate.getLocalDate().getMonth());
		assertEquals(28, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		assertEquals(14, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.FEBRUARY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		assertEquals(Month.JANUARY, hebrewDate.getLocalDate().getMonth());
		assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		assertEquals(16, hebrewDate.getJewishDayOfMonth());

	}



} // End of UT_GregorianDateNavigation class
