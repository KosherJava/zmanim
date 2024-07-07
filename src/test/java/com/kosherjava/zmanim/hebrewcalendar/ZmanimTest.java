package com.kosherjava.zmanim.hebrewcalendar;

import static com.kosherjava.zmanim.AstronomicalCalendar.GEOMETRIC_ZENITH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.kosherjava.zmanim.ComprehensiveZmanimCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;
import com.kosherjava.zmanim.util.NOAACalculator;
import com.kosherjava.zmanim.util.SunTimesCalculator;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ZmanimTest {

    @Test
    public void sofZmanKidushLevana_Jerusalem() {
        // Jerusalem, Israel, 2019-May
        ZoneId tz = TimeZone.getTimeZone("Asia/Jerusalem").toZoneId();
        GeoLocation location = new GeoLocation("test", 31.76904, 35.21633, tz);
        ComprehensiveZmanimCalendar zcal = new ComprehensiveZmanimCalendar(location);
        zcal.setUseElevation(true);
        zcal.setLocalDate(LocalDate.of(2019, Month.MAY, 1));
        Instant date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNull(date);

        zcal.setLocalDate(LocalDate.of(2019, Month.MAY, 19));
        date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNotNull(date);
        assertEquals(1558246265170L, date.toEpochMilli());
        LocalDateTime cal = LocalDateTime.ofInstant(date, tz);
        assertEquals(cal.getYear(), 2019);
        assertEquals(cal.getMonth(), Month.MAY);
        assertEquals(cal.getDayOfMonth(), 19);
        assertEquals(cal.getHour(), 9);
        assertEquals(cal.getMinute(), 11);
        assertEquals(cal.getSecond(), 5);
    }

    @Test
    public void sofZmanKidushLevana_NewJersey() {
        // Teaneck, NJ, USA, 2019-May
        ZoneId tz = TimeZone.getTimeZone("EDT").toZoneId();
        GeoLocation location = new GeoLocation("test", 40.896027, -74.0128627, tz);
        ComprehensiveZmanimCalendar zcal = new ComprehensiveZmanimCalendar(location);
        zcal.setUseElevation(true);
        zcal.setLocalDate(LocalDate.of(2019, Month.MAY, 1));
        Instant date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNull(date);

        zcal.setLocalDate(LocalDate.of(2019, Month.MAY, 19));
        date = zcal.getSofZmanKidushLevanaBetweenMoldos();
        assertNotNull(date);
        assertEquals(1558246265170L, date.toEpochMilli());
        LocalDateTime cal = LocalDateTime.ofInstant(date, tz);
        assertEquals(cal.getYear(), 2019);
        assertEquals(cal.getMonth(), Month.MAY);
        assertEquals(cal.getDayOfMonth(), 19);
        assertEquals(cal.getHour(), 6);
        assertEquals(cal.getMinute(), 11);
        assertEquals(cal.getSecond(), 5);
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
        GeoLocation location = new GeoLocation("test", 40.7, -74.0, 10.0, tz.toZoneId());
        ComprehensiveZmanimCalendar zcal = new ComprehensiveZmanimCalendar(location);
        zcal.setAstronomicalCalculator(calculator);
        zcal.setUseElevation(true);

        LocalDate cal = LocalDate.of(2021, Month.JUNE, 1);
        Instant date;
        Long dateExpected;
        double zenith;

        for (int deg = 0; deg <= 30; deg++) {
            zenith = GEOMETRIC_ZENITH + deg;
            m = dates.get(deg);
            for (int day = 1; day <= 365; day++) {
                zcal.setLocalDate(cal.withDayOfYear(day));
                date = zcal.getSunriseOffsetByDegrees(zenith);
                if (date != null) {
                    System.out.println(deg + "," + day + "," + date.toEpochMilli());
                }
                //FIXME assertNotNull("deg=" + deg + " day=" + day, date);
                dateExpected = m.get(day);
                if (dateExpected != null) {
                    assertEquals("deg=" + deg + " day=" + day, dateExpected.longValue(), date.toEpochMilli());
                }
            }
        }
    }
}
