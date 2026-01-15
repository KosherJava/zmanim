package com.kosherjava.zmanim;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.kosherjava.zmanim.util.GeoLocation;

/**
 * Tests for the AstronomicalCalendar class.
 */
public class AstronomicalCalendarTest {

    /**
     * Regression test for anti-meridian noon calculation bug.
     * 
     * This test verifies that solar noon (chatzos) is calculated on the correct
     * date for locations near the anti-meridian (-180°/+180° longitude). The bug
     * was in the getDateFromTime() method where the condition for NOON date
     * adjustment was "localTimeHours + hours > 24" which could never be true.
     * 
     * The fix changed it to "localTimeHours + hours < 0" to properly detect when
     * UTC noon wraps to the next day, requiring a day adjustment forward.
     * 
     * Test case from random testing that originally failed:
     * - Date: 2033-01-10
     * - Location: (-7.459981245782188, -179.78845309054608), Elevation:
     * 18.99769634799005
     * - Timezone: Etc/GMT+12 (UTC-12)
     * 
     * Before fix: Noon was calculated for 2033-01-09 (wrong day)
     * After fix: Noon should be calculated for 2033-01-10 (correct day)
     */
    @Test
    public void testAntiMeridianNoonCalculation() {
        // Create location near anti-meridian
        double latitude = -7.459981245782188;
        double longitude = -179.78845309054608;
        double elevation = 18.99769634799005;
        TimeZone timeZone = TimeZone.getTimeZone("Etc/GMT+12"); // UTC-12

        GeoLocation location = new GeoLocation("Test Location", latitude, longitude, elevation, timeZone);
        AstronomicalCalendar calendar = new AstronomicalCalendar(location);

        // Set the test date: January 10, 2033
        calendar.getCalendar().set(Calendar.YEAR, 2033);
        calendar.getCalendar().set(Calendar.MONTH, Calendar.JANUARY);
        calendar.getCalendar().set(Calendar.DAY_OF_MONTH, 10);
        calendar.getCalendar().set(Calendar.HOUR_OF_DAY, 14);
        calendar.getCalendar().set(Calendar.MINUTE, 57);
        calendar.getCalendar().set(Calendar.SECOND, 26);

        // Get solar noon (sun transit)
        Date sunTransit = calendar.getSunTransit();

        Assert.assertNotNull("Sun transit should not be null", sunTransit);

        // Create a calendar to check the date
        Calendar resultCalendar = Calendar.getInstance(timeZone);
        resultCalendar.setTime(sunTransit);

        // The calculated noon should be on January 10, not January 9
        Assert.assertEquals("Solar noon year should match input year",
                2033, resultCalendar.get(Calendar.YEAR));
        Assert.assertEquals("Solar noon month should match input month",
                Calendar.JANUARY, resultCalendar.get(Calendar.MONTH));
        Assert.assertEquals("Solar noon day should match input day (was off by 1 day before fix)",
                10, resultCalendar.get(Calendar.DAY_OF_MONTH));

        System.out.println("Input date: 2033-01-10");
        System.out.println("Calculated sun transit: " + sunTransit);
        System.out.println("Transit date components: " +
                resultCalendar.get(Calendar.YEAR) + "-" +
                (resultCalendar.get(Calendar.MONTH) + 1) + "-" +
                resultCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Regression test for eastern anti-meridian noon calculation bug.
     * 
     * This test verifies the opposite case from testAntiMeridianNoonCalculation,
     * where the location is on the EASTERN side of the anti-meridian (positive
     * longitude near +180°).
     * 
     * The original bug had two parts:
     * 1. The condition "localTimeHours + hours > 24" was correct but used -1
     * (subtract day) which was wrong
     * 2. Only handled one side of anti-meridian, not both
     * 
     * The fix uses: (localTimeHours + hours < 0 || localTimeHours + hours > 24)
     * and adds +1 day for BOTH cases.
     * 
     * Test case from random testing that originally failed:
     * - Date: 1933-11-21
     * - Location: (45.272612853681636, 178.99188812570947), Elevation:
     * 77.9172657062239
     * - Timezone: Pacific/Auckland (approximately GMT+12)
     * 
     * Before fix: Noon was calculated for 1933-11-22 (wrong day, +1 day error)
     * After fix: Noon should be calculated for 1933-11-21 (correct day)
     */
    @Test
    public void testEasternAntiMeridianNoonCalculation() {
        // Create location near eastern anti-meridian
        double latitude = 45.272612853681636;
        double longitude = 178.99188812570947;
        double elevation = 77.9172657062239;
        TimeZone timeZone = TimeZone.getTimeZone("Pacific/Auckland"); // GMT+12/+13

        GeoLocation location = new GeoLocation("Test Location", latitude, longitude, elevation, timeZone);
        AstronomicalCalendar calendar = new AstronomicalCalendar(location);

        // Set the test date: November 21, 1933
        calendar.getCalendar().set(Calendar.YEAR, 1933);
        calendar.getCalendar().set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.getCalendar().set(Calendar.DAY_OF_MONTH, 21);
        calendar.getCalendar().set(Calendar.HOUR_OF_DAY, 11);
        calendar.getCalendar().set(Calendar.MINUTE, 47);
        calendar.getCalendar().set(Calendar.SECOND, 40);

        // Get solar noon (sun transit)
        Date sunTransit = calendar.getSunTransit();

        Assert.assertNotNull("Sun transit should not be null", sunTransit);

        // Create a calendar to check the date
        Calendar resultCalendar = Calendar.getInstance(timeZone);
        resultCalendar.setTime(sunTransit);

        // The calculated noon should be on November 21, not November 22
        Assert.assertEquals("Solar noon year should match input year",
                1933, resultCalendar.get(Calendar.YEAR));
        Assert.assertEquals("Solar noon month should match input month",
                Calendar.NOVEMBER, resultCalendar.get(Calendar.MONTH));
        Assert.assertEquals("Solar noon day should match input day (was off by +1 day before fix)",
                21, resultCalendar.get(Calendar.DAY_OF_MONTH));

        System.out.println("Input date: 1933-11-21");
        System.out.println("Calculated sun transit: " + sunTransit);
        System.out.println("Transit date components: " +
                resultCalendar.get(Calendar.YEAR) + "-" +
                (resultCalendar.get(Calendar.MONTH) + 1) + "-" +
                resultCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Additional test for anti-meridian with various longitudes.
     * Tests that noon always falls on the correct date regardless of longitude.
     */
    @Test
    public void testNoonDateConsistency() {
        // Test various longitudes near both sides of the anti-meridian
        double[] testLongitudes = {
                -179.5, // Just west of anti-meridian
                -179.0,
                178.0, // Just east of anti-meridian
                178.5,
                179.0,
                179.5
        };

        for (double longitude : testLongitudes) {
            TimeZone tz = TimeZone.getTimeZone("Etc/GMT+12");
            GeoLocation location = new GeoLocation("Test", 0.0, longitude, 0.0, tz);
            AstronomicalCalendar calendar = new AstronomicalCalendar(location);

            // Set a specific date
            calendar.getCalendar().set(2024, Calendar.JUNE, 15, 12, 0, 0);
            int inputDay = calendar.getCalendar().get(Calendar.DAY_OF_MONTH);

            Date sunTransit = calendar.getSunTransit();
            Assert.assertNotNull("Sun transit should not be null for longitude " + longitude, sunTransit);

            Calendar result = Calendar.getInstance(tz);
            result.setTime(sunTransit);
            int resultDay = result.get(Calendar.DAY_OF_MONTH);

            Assert.assertEquals(
                    "Solar noon day should match input day for longitude " + longitude,
                    inputDay, resultDay);
        }
    }
}
