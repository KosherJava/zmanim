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
  public void testRambamYomiOneChapter() {
    JewishCalendar calendar = new JewishCalendar(LocalDate.of(2020, 11, 11));
    Assert.assertEquals("Sabbath 17", formatter.formatRambamYomi(calendar.getRambamYomi()));
    Assert.assertEquals(
        "הלכות שבת פרק יז", hebrewFormatter.formatRambamYomi(calendar.getRambamYomi()));
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
        "Book II 9.8-9.10",
        formatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
    Assert.assertEquals(
        "כלל ט׳ ח-י",
        hebrewFormatter.formatShemirasHaLashonYomi(calendar.getShemirasHaLashonYomi()));
  }
}
