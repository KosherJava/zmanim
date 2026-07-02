package com.kosherjava.zmanim.hebrewcalendar;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class LimudYomiCalculatorTest {
  private final HebrewDateFormatter formatter = new HebrewDateFormatter();
  private final HebrewDateFormatter hebrewFormatter = new HebrewDateFormatter();

  public LimudYomiCalculatorTest() {
    hebrewFormatter.setHebrewFormat(true);
  }

  @Test
  public void testMishnaYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2020, 9, 21));
    Assert.assertEquals("Kelim 11:7-8", formatter.formatMishnaYomi(calendar.getMishnaYomi()));
    Assert.assertEquals(
        "כלים י״א: ז-ח", hebrewFormatter.formatMishnaYomi(calendar.getMishnaYomi()));
  }

  @Test
  public void testMishnaYomiHebrewFifteen() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2020, 10, 18));
    Assert.assertEquals("Kelim 17:14-15", formatter.formatMishnaYomi(calendar.getMishnaYomi()));
    Assert.assertEquals(
        "כלים י״ז: י״ד-ט״ו", hebrewFormatter.formatMishnaYomi(calendar.getMishnaYomi()));
  }

  @Test
  public void testRambamYomiOneChapter() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2020, 11, 11));
    Assert.assertEquals("Sabbath 17", formatter.formatRambamYomi(calendar.getRambamYomi()));
    Assert.assertEquals(
        "הלכות שבת פרק יז", hebrewFormatter.formatRambamYomi(calendar.getRambamYomi()));
  }

  @Test
  public void testRambamYomiIntroAndSpecialChapters() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(1984, 4, 29));
    Assert.assertEquals(
        "Transmission of the Oral Law 1-21", formatter.formatRambamYomi(calendar.getRambamYomi()));
    Assert.assertEquals(
        "מסירת תורה שבעל פה פרק א-כא", hebrewFormatter.formatRambamYomi(calendar.getRambamYomi()));

    calendar = new JewishCalendar(LocalDate.of(2023, 8, 8));
    Assert.assertEquals(
        "The Order of Prayer 4-5", formatter.formatRambamYomi(calendar.getRambamYomi()));
    Assert.assertEquals(
        "סדר התפילה פרקים ד-ה", hebrewFormatter.formatRambamYomi(calendar.getRambamYomi()));
  }

  @Test
  public void testRambamYomiThreeChapters() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 7, 29));
    Assert.assertEquals(
        "Gifts to the Poor 8-10", formatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));
    Assert.assertEquals(
        "הלכות מתנות עניים פרקים ח-י",
        hebrewFormatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));
  }

  @Test
  public void testRambamYomiThreeChapterSpecialCases() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(1984, 4, 29));
    Assert.assertEquals(
        "Transmission of the Oral Law 1-45",
        formatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));
    Assert.assertEquals(
        "מסירת תורה שבעל פה פרקים א-מה",
        hebrewFormatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));

    calendar = new JewishCalendar(LocalDate.of(2020, 9, 3));
    Assert.assertEquals(
        "Leavened and Unleavened Bread 8-9, Shofar, Sukkah and Lulav 1-2",
        formatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));
    Assert.assertEquals(
        "הלכות חמץ ומצה פרקים ח-ט, הלכות שופר וסוכה ולולב פרקים א-ב",
        hebrewFormatter.formatRambamYomi(calendar.getRambamYomi3Chapters()));
  }

  @Test
  public void testDailyTehillim() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 1, 3));
    Assert.assertEquals(
        "Psalms 106-107", formatter.formatDailyTehillim(calendar.getDailyTehillim()));
    Assert.assertEquals(
        "תהילים ק״ו-ק״ז", hebrewFormatter.formatDailyTehillim(calendar.getDailyTehillim()));
  }

  @Test
  public void testTanachYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 6, 9));
    Assert.assertEquals("Psalms Seder 3", formatter.formatTanachYomi(calendar.getTanachYomi()));
    Assert.assertEquals("תהלים ס׳ ג׳", hebrewFormatter.formatTanachYomi(calendar.getTanachYomi()));
  }

  @Test
  public void testKitzurShulchanAruchYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 7, 9));
    Assert.assertEquals(
        "161:18-162:5",
        formatter.formatKitzurShulchanAruchYomi(calendar.getKitzurShulchanAruchYomi()));
    Assert.assertEquals(
        "קס״א: יח - קס״ב: ה",
        hebrewFormatter.formatKitzurShulchanAruchYomi(calendar.getKitzurShulchanAruchYomi()));
  }

  @Test
  public void testShemirasHaLashonYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 6, 26));
    Assert.assertEquals(
        "Shemirat HaLashon, Part 2, Perek 9 Halacha 8-10",
        formatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
    Assert.assertEquals(
        "שמירת הלשון, חלק ב׳, פרק ט׳ הלכה ח-י",
        hebrewFormatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
  }

  @Test
  public void testShemirasHaLashonSectionYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 10, 3));
    Assert.assertEquals(
        "Shemirat HaLashon, Part 1, Hakdamah, 1-2",
        formatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
    Assert.assertEquals(
        "שמירת הלשון, חלק א׳, הקדמה, א-ב",
        hebrewFormatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
  }

  @Test
  public void testShemirasHaLashonShaarYomi() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2024, 10, 13));
    Assert.assertEquals(
        "Shemirat HaLashon, Part 1, Shaar HaZechirah, Perek 1 Halacha 1-4",
        formatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
    Assert.assertEquals(
        "שמירת הלשון, חלק א׳, שער הזכירה, פרק א׳ הלכה א-ד",
        hebrewFormatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
  }
}
