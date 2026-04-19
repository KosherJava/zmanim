/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.*;

import java.time.LocalDate;
import java.time.Month;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

        LocalDate localDate = LocalDate.of(2011, Month.JANUARY,31);

		JewishDate hebrewDate = new JewishDate(localDate);
		Assert.assertEquals(5771, hebrewDate.getJewishYear());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

		hebrewDate.plusDays(1);
		Assert.assertEquals(2, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());
        
        localDate = LocalDate.of(2011, Month.FEBRUARY,28);
        hebrewDate = new JewishDate(localDate);
		Assert.assertEquals(2, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(28, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		hebrewDate.plusDays(1);
		Assert.assertEquals(3, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(25, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.MARCH,31);
        hebrewDate = new JewishDate(localDate);
		hebrewDate.plusDays(1);
		Assert.assertEquals(4, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(13, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.APRIL,30);
        hebrewDate = new JewishDate(localDate);
		hebrewDate.plusDays(1);
		Assert.assertEquals(5, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(1, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.MAY,31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(6, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(2, hebrewDate.getJewishMonth());
		Assert.assertEquals(28, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.JUNE,30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(7, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(3, hebrewDate.getJewishMonth());
		Assert.assertEquals(29, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.JULY,31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(8, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(5, hebrewDate.getJewishMonth());
		Assert.assertEquals(1, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.AUGUST,31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(9, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(6, hebrewDate.getJewishMonth());
		Assert.assertEquals(2, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.SEPTEMBER,30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(10, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(7, hebrewDate.getJewishMonth());
		Assert.assertEquals(3, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.OCTOBER,31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(11, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(5772, hebrewDate.getJewishYear());
		Assert.assertEquals(8, hebrewDate.getJewishMonth());
		Assert.assertEquals(4, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.NOVEMBER,30);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(12, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(9, hebrewDate.getJewishMonth());
		Assert.assertEquals(5, hebrewDate.getJewishDayOfMonth());

        localDate = LocalDate.of(2011, Month.DECEMBER,31);
        hebrewDate = new JewishDate(localDate);
        hebrewDate.plusDays(1);
		Assert.assertEquals(2012, hebrewDate.getLocalDate().getYear());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(1, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(10, hebrewDate.getJewishMonth());
		Assert.assertEquals(6, hebrewDate.getJewishDayOfMonth());
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		LocalDate localDate = LocalDate.of(2011, Month.JANUARY, 1);
		JewishDate hebrewDate = new JewishDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(2010, hebrewDate.getLocalDate().getYear());
		Assert.assertEquals(12, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(10, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.DECEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(11, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(9, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.NOVEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(10, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(8, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.OCTOBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(9, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(7, hebrewDate.getJewishMonth());
		Assert.assertEquals(22, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.SEPTEMBER, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(8, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(5770, hebrewDate.getJewishYear());
		Assert.assertEquals(6, hebrewDate.getJewishMonth());
		Assert.assertEquals(21, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.AUGUST, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(7, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(5, hebrewDate.getJewishMonth());
		Assert.assertEquals(20, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.JULY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(6, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(4, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.JUNE, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(5, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(3, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.MAY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(4, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(30, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(2, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.APRIL, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(3, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(1, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.MARCH, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(2, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(28, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(12, hebrewDate.getJewishMonth());
		Assert.assertEquals(14, hebrewDate.getJewishDayOfMonth());

		localDate = LocalDate.of(2010, Month.FEBRUARY, 1);
		hebrewDate.setGregorianDate(localDate);
		hebrewDate.minusDays(1);
		Assert.assertEquals(1, hebrewDate.getLocalDate().getMonthValue());
		Assert.assertEquals(31, hebrewDate.getLocalDate().getDayOfMonth());
		Assert.assertEquals(11, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

	}



} // End of UT_GregorianDateNavigation class
