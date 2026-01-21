package com.kosherjava.zmanim.hebrewcalendar;

import static com.kosherjava.zmanim.AstronomicalCalendar.GEOMETRIC_ZENITH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.kosherjava.zmanim.ComplexZmanimCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.util.NOAACalculator;
import com.kosherjava.zmanim.util.SunTimesCalculator;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ZmanimTest {

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
    public void dawn_NewYork() throws Exception {
        dawn_NewYork(new SunTimesCalculator(), "dawn_new_york_sun.csv");
        dawn_NewYork(new NOAACalculator(), "dawn_new_york_noa.csv");
    }

    private void dawn_NewYork(AstronomicalCalculator calculator, String name) throws Exception {
        Class<?> clazz = calculator.getClass();
        InputStream res = clazz.getResourceAsStream("/" + name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(res));
        String line = reader.readLine();
        Map<Integer, Map<Integer, Long>> dates = new HashMap<>();
        Map<Integer, Long> m;

        while (line != null) {
            if (line.isEmpty()) continue;
            String[] tokens = line.split(",");
            int deg = Integer.parseInt(tokens[0]);
            int day = Integer.parseInt(tokens[1]);
            long t = Long.parseLong(tokens[2]);
            m = dates.get(deg);
            if (m == null) {
                m = new HashMap<>();
                dates.put(deg, m);
            }
            m.put(day, t);
            line = reader.readLine();
        }

        // New York, USA, 2021-June
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        Calendar cal = Calendar.getInstance(tz);
        GeoLocation location = new GeoLocation("test", 40.7, -74.0, 10.0, tz);
        ComplexZmanimCalendar zcal = new ComplexZmanimCalendar(location);
        zcal.setAstronomicalCalculator(calculator);
        zcal.setUseElevation(true);

        Date date;
        Long dateExpected;
        double zenith;
        cal.set(2021, Calendar.JUNE, 1);

        for (int deg = 0; deg <= 30; deg++) {
            zenith = GEOMETRIC_ZENITH + deg;
            m = dates.get(deg);
            for (int day = 1; day <= 366; day++) {
                cal.set(Calendar.DAY_OF_YEAR, day);
                zcal.setCalendar(cal);
                date = zcal.getSunriseOffsetByDegrees(zenith);
                if (date != null) {
                    System.out.println(deg + "," + day + "," + date.getTime());
                }
                assertNotNull("deg=" + deg + " day=" + day, date);
                dateExpected = m.get(day);
                if (dateExpected != null) {
                    assertEquals("deg=" + deg + " day=" + day, dateExpected.longValue(), date.getTime());
                }
            }
        }
    }
}
