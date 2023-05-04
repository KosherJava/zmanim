package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class YomiCalculatorTest {
    private static final HebrewDateFormatter hdf = new HebrewDateFormatter();

    static {
        hdf.setHebrewFormat(true);
    }

    @Test
    public void testCorrectDaf1() {
        JewishCalendar jewishCalendar = new JewishCalendar(5685, JewishDate.KISLEV, 12);
        Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
        Assert.assertEquals(5, daf.getMasechtaNumber());
        Assert.assertEquals(2, daf.getDaf());
        System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiBavli()));
    }

    @Test
    public void testCorrectDaf2() {
        JewishCalendar jewishCalendar = new JewishCalendar(5736, JewishDate.ELUL, 26);
        Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
        Assert.assertEquals(4, daf.getMasechtaNumber());
        Assert.assertEquals(14, daf.getDaf());
        System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiBavli()));
    }

    @Test
    public void testCorrectDaf3() {
        JewishCalendar jewishCalendar = new JewishCalendar(5777, JewishDate.ELUL, 10);
        Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
        Assert.assertEquals(23, daf.getMasechtaNumber());
        Assert.assertEquals(47, daf.getDaf());
        System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiBavli()));
    }

    @Test
    public void TestCycle11New() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String[][] table = null;
        String[] entry = null;
        table = YerushalmiNewCycle11.newtable;
        try {
            for (int i = 0; i < table.length; i++) {
                entry = table[i];
                Date date = formatter.parse(entry[0]);
                String name = entry[1];
                int page = Integer.parseInt(entry[2]);
                JewishCalendar jcal = new JewishCalendar(date);
                Daf daf = YerushalmiYomiCalculator.getDafYomiYerushalmiNew(jcal);
                String dname = daf.getYerushlmiMasechtaTransliterated();
                if (!dname.equals(name) || !(page == daf.getDaf())) {

                    System.out.println(entry[0]);
                    System.out.println(date);
                    System.out.println(jcal.getLocalDate());
                    System.out.println(name);
                    System.out.println(page);
                    System.out.println(daf.getMasechtaTransliterated());
                    System.out.println(daf.getDaf());
                    System.out.println(daf);
                }
				assert(dname.equals(name));
                assert(page == daf.getDaf());

            }
        } catch (Exception e) {
            System.out.println("Error proceccing");
            assert (false);
        }
    }

    @Test
    public void TestCycle11Orig() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String[][] table = null;
        String[] entry = null;
        table = YerushalmiOrigCycle11.newtable;
        try {
            for (int i = 0; i < table.length; i++) {
                entry = table[i];

                Date date = formatter.parse(entry[0]);
                String name = entry[1];
                int page = Integer.parseInt(entry[2]);
                JewishCalendar jcal = new JewishCalendar(date);
                Daf daf = YerushalmiYomiCalculator.getDafYomiYerushalmi(jcal);
				String dname = daf.getYerushlmiMasechtaTransliterated();
                if (!dname.equals(name) || !(page == daf.getDaf())) {

                    System.out.println(date);
                    System.out.println("[" + name + "]");
                    //System.out.println(toHex(name));
                    System.out.println(page);
                    System.out.println(dname);
                    System.out.println(daf.getDaf());
                    System.out.println(hdf.formatDafYomiYerushalmi(jcal.getDafYomiYerushalmi()));
                }

                assert(dname.equals(name));
                assert (page == daf.getDaf());
            }
        } catch (Exception e) {
            System.out.println("Error proceccing");
            assert (false);
        }
    }
}
