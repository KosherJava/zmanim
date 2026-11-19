package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Checks the Gregorian date that {@link JewishDate#getLocalDate()} returns, including dates before January 1, 0001.
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_GregorianDateConversion {

	/** The absolute date of the day of the molad tohu, as in {@link JewishDate}. */
	private static final int JEWISH_EPOCH = -1373429;

	private static final long CHALAKIM_PER_DAY = 25920;

	private static final LocalDate JANUARY_1_0001 = LocalDate.of(1, 1, 1);

	/** The absolute date of a Gregorian date, where January 1, 0001 is day 1. */
	private static int absDate(LocalDate date) {
		return (int) ChronoUnit.DAYS.between(JANUARY_1_0001, date) + 1;
	}

	/** A {@code JewishDate} set to the given absolute date, using the molad constructor, which accepts any day. */
	private static JewishDate jewishDateAt(int absDate) {
		return new JewishDate((absDate - JEWISH_EPOCH) * CHALAKIM_PER_DAY);
	}

	private static void assertConverts(LocalDate date) {
		JewishDate jewishDate = jewishDateAt(absDate(date));
		Assert.assertEquals(absDate(date), jewishDate.getAbsDate());
		Assert.assertEquals(date, jewishDate.getLocalDate());
	}

	@Test
	public void moladOfTishrei3000() {
		// The molad of Tishrei 3000 is on Wednesday, August 24, 762 BCE (year -761)
		JewishDate molad = new JewishDate(28391472360L);
		Assert.assertEquals(LocalDate.of(-761, 8, 24), molad.getLocalDate());
	}

	@Test
	public void absDateMatchesTheGregorianConstructor() {
		LocalDate[] dates = { LocalDate.of(1, 1, 1), LocalDate.of(1, 12, 31), LocalDate.of(2000, 2, 29),
				LocalDate.of(2026, 11, 18) };
		for (LocalDate date : dates) {
			Assert.assertEquals(absDate(date), new JewishDate(date).getAbsDate());
		}
	}

	@Test
	public void aroundJanuary1Year1() {
		assertConverts(LocalDate.of(1, 1, 2));
		assertConverts(LocalDate.of(1, 1, 1));
		assertConverts(LocalDate.of(0, 12, 31));
		assertConverts(LocalDate.of(0, 1, 1));
		assertConverts(LocalDate.of(-1, 12, 31));
	}

	@Test
	public void leapDaysBeforeYear1() {
		assertConverts(LocalDate.of(0, 2, 29));
		assertConverts(LocalDate.of(0, 3, 1));
		assertConverts(LocalDate.of(-4, 2, 29));
		assertConverts(LocalDate.of(-100, 2, 28));
		assertConverts(LocalDate.of(-100, 3, 1));
		assertConverts(LocalDate.of(-400, 2, 29));
	}

	@Test
	public void roshHashanaOfYear1() {
		// Rosh Hashana of the year 1 is on Monday, September 7, 3761 BCE (year -3760)
		JewishDate roshHashana = jewishDateAt(absDate(LocalDate.of(-3760, 9, 7)));
		Assert.assertEquals(1, roshHashana.getJewishYear());
		Assert.assertEquals(JewishDate.TISHREI, roshHashana.getJewishMonth());
		Assert.assertEquals(1, roshHashana.getJewishDayOfMonth());
		Assert.assertEquals(LocalDate.of(-3760, 9, 7), roshHashana.getLocalDate());
	}

	@Test
	public void leapDaysAfterYear1() {
		assertConverts(LocalDate.of(4, 2, 29));
		assertConverts(LocalDate.of(1600, 2, 29));
		assertConverts(LocalDate.of(1900, 2, 28));
		assertConverts(LocalDate.of(1900, 3, 1));
		assertConverts(LocalDate.of(2000, 2, 29));
	}

	@Test
	public void firstAndLastDayOfEveryYear() {
		for (int year = -3760; year <= 9999; year++) {
			if (year > -3760) {
				assertConverts(LocalDate.of(year, 1, 1));
			}
			assertConverts(LocalDate.of(year, 12, 31));
		}
	}
}
