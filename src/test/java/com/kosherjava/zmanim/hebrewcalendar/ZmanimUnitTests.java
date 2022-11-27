package com.kosherjava.zmanim.hebrewcalendar;

import static com.kosherjava.zmanim.AstronomicalCalendar.GEOMETRIC_ZENITH;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.kosherjava.zmanim.ComplexZmanimCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.util.NOAACalculator;
import com.kosherjava.zmanim.util.SunTimesCalculator;

public class ZmanimUnitTests {

    @Test
    public void sofZmanKidushLevana_Jerusalem() {
        // Jerusalem, Israel, 2019-May
        TimeZone tz = TimeZone.getTimeZone("Asia/Jerusalem");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2019, Calendar.MAY, 1);
        GeoLocation location = new GeoLocation("test", 31.76904, 35.21633, tz);
        ComplexZmanimCalendar zcal = new ComplexZmanimCalendar(location);
        zcal.setUseElevation(true);
        zcal.setCalendar(cal);
        Date date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNull(date);

        cal.set(2019, Calendar.MAY, 19);
        zcal.setCalendar(cal);
        date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNotNull(date);
        assertEquals(1558246265170L, date.getTime());
        cal.setTime(date);
        assertEquals(cal.get(Calendar.YEAR), 2019);
        assertEquals(cal.get(Calendar.MONTH), Calendar.MAY);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), 19);
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), 9);
        assertEquals(cal.get(Calendar.MINUTE), 11);
        assertEquals(cal.get(Calendar.SECOND), 5);
    }

    @Test
    public void sofZmanKidushLevana_NewJersey() {
        // Teaneck, NJ, USA, 2019-May
        TimeZone tz = TimeZone.getTimeZone("EDT");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2019, Calendar.MAY, 1);
        GeoLocation location = new GeoLocation("test", 40.896027, -74.0128627, tz);
        ComplexZmanimCalendar zcal = new ComplexZmanimCalendar(location);
        zcal.setUseElevation(true);
        zcal.setCalendar(cal);
        Date date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNull(date);

        cal.set(2019, Calendar.MAY, 19);
        zcal.setCalendar(cal);
        date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNotNull(date);
        assertEquals(1558246265170L, date.getTime());
        cal.setTime(date);
        assertEquals(cal.get(Calendar.YEAR), 2019);
        assertEquals(cal.get(Calendar.MONTH), Calendar.MAY);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), 19);
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), 6);
        assertEquals(cal.get(Calendar.MINUTE), 11);
        assertEquals(cal.get(Calendar.SECOND), 5);
    }

    @Test
    public void dawn_NewYork() {
        AstronomicalCalculator calculator = new SunTimesCalculator();
        dawn_NewYork(calculator);
        calculator = new NOAACalculator();
        dawn_NewYork(calculator);
    }

    private void dawn_NewYork(AstronomicalCalculator calculator) {
        // New York, USA, 2021-June
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        Calendar cal = Calendar.getInstance(tz);
        GeoLocation location = new GeoLocation("test", 40.7, -74.0, 10.0, tz);
        ComplexZmanimCalendar zcal = new ComplexZmanimCalendar(location);
        zcal.setAstronomicalCalculator(calculator);
        zcal.setUseElevation(true);

        Date date;
        double offsetZenith;
        cal.set(2021, Calendar.JUNE, 1);

        for (int o = 0; o <= 30; o++) {
            offsetZenith = GEOMETRIC_ZENITH + o;
            for (int d = 1; d <= 366; d++) {
                cal.set(Calendar.DAY_OF_YEAR, d);
                zcal.setCalendar(cal);
                date = zcal.getSunriseOffsetByDegrees(offsetZenith);
                assertNotNull("day=" + d + " zenith=" + offsetZenith, date);
            }
        }
    }
}

