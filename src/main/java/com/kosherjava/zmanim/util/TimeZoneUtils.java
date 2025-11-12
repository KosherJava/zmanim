/*
 * Zmanim Java API
 * Copyright (C) 2004-2025 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility class for timezone-related calculations using java.time APIs.
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2025
 */
public class TimeZoneUtils {
	
	/**
	 * Gets the timezone offset in milliseconds at a specific instant using java.time APIs.
	 * This method correctly handles Daylight Saving Time (DST) changes by calculating the offset
	 * at the specific date/time represented by the Calendar.
	 * 
	 * @param calendar the Calendar containing the date/time and timezone to calculate the offset for
	 * @return the timezone offset in milliseconds
	 */
	public static long getTimezoneOffsetAt(Calendar calendar) {
		TimeZone timeZone = calendar.getTimeZone();
		long unixTimestampMillis = calendar.getTimeInMillis();
		ZoneId zoneId = timeZone.toZoneId();
		Instant instant = Instant.ofEpochMilli(unixTimestampMillis);
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		return zonedDateTime.getOffset().getTotalSeconds() * 1000;
	}
	
	/**
	 * Adds one day to the given Calendar by converting it to a ZonedDateTime,
	 * adding a day, and converting it back to a Calendar.
	 * The returned Calendar will have the same TimeZone as the input Calendar.
	 * 
	 * @param calendar the Calendar to add a day to
	 * @return a new Calendar with one day added, preserving the original TimeZone
	 */
	public static Calendar addDay(Calendar calendar) {
		TimeZone timeZone = calendar.getTimeZone();
		long unixTimestampMillis = calendar.getTimeInMillis();
		ZoneId zoneId = timeZone.toZoneId();
		Instant instant = Instant.ofEpochMilli(unixTimestampMillis);
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		ZonedDateTime nextDay = zonedDateTime.plusDays(1);
		Instant nextDayInstant = nextDay.toInstant();
		Calendar result = Calendar.getInstance(timeZone);
		result.setTimeInMillis(nextDayInstant.toEpochMilli());
		return result;
	}
}

