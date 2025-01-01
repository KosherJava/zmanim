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
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import java.util.TimeZone;

/**
 * A class that represents a numeric time. Times that represent a time of day are stored as {@link java.util.Date}s in
 * this API. The time class is used to represent numeric time such as the time in hours, minutes, seconds and
 * milliseconds of a {@link com.kosherjava.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour}.
 * 
 * @author &copy; Eliyahu Hershfeld 2004 - 2025
 */
public class Time {
	/** milliseconds in a second. */
	private static final int SECOND_MILLIS = 1000;

	/** milliseconds in a minute. */
	private static final int MINUTE_MILLIS = SECOND_MILLIS * 60;

	/** milliseconds in an hour. */
	private static final int HOUR_MILLIS = MINUTE_MILLIS * 60;

	/**
	 * The hour.
	 * @see #getHours()
	 */
	private int hours;

	/**
	 * The minutes.
	 * @see #getMinutes()
	 */
	private int minutes;

	/**
	 * The seconds.
	 * @see #getSeconds()
	 */
	private int seconds;

	/**
	 * The milliseconds.
	 * @see #getMilliseconds()
	 */
	private int milliseconds;

	/**
	 * Is the time negative
	 * @see #isNegative()
	 * @see #setIsNegative(boolean)
	 */
	private boolean isNegative = false;

	/**
	 * Constructor with parameters for the hours, minutes, seconds and milliseconds.
	 * 
	 * @param hours the hours to set
	 * @param minutes the minutes to set
	 * @param seconds the seconds to set
	 * @param milliseconds the milliseconds to set
	 */
	public Time(int hours, int minutes, int seconds, int milliseconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	/**
	 * Constructor with a parameter for milliseconds. This constructor casts the milliseconds to an int and
	 * calls {@link #Time(int)}
	 * @param millis the milliseconds to set the object with.
	 */
	public Time(double millis) {
		this((int) millis);
	}

	/**
	 * A constructor that sets the time from milliseconds. The milliseconds are converted to hours, minutes,
	 * seconds and milliseconds. If the milliseconds are negative it will call {@link#setIsNegative(boolean)}.
	 * @param millis the milliseconds to set.
	 */
	public Time(int millis) {
		int adjustedMillis = millis;
		if (adjustedMillis < 0) {
			this.isNegative = true;
			adjustedMillis = Math.abs(adjustedMillis);
		}
		this.hours = adjustedMillis / HOUR_MILLIS;
		adjustedMillis = adjustedMillis - this.hours * HOUR_MILLIS;

		this.minutes = adjustedMillis / MINUTE_MILLIS;
		adjustedMillis = adjustedMillis - this.minutes * MINUTE_MILLIS;

		this.seconds = adjustedMillis / SECOND_MILLIS;
		adjustedMillis = adjustedMillis - this.seconds * SECOND_MILLIS;

		this.milliseconds = adjustedMillis;
	}

	/**
	 * Does the time represent a negative time, such as using this to subtract time from another Time.
	 * @return if the time is negative.
	 */
	public boolean isNegative() {
		return this.isNegative;
	}

	/**
	 * Set this to represent a negative time.
	 * @param isNegative that the Time represents negative time
	 */
	public void setIsNegative(boolean isNegative) {
		this.isNegative = isNegative;
	}

	/**
	 * Get the hour.
	 * @return Returns the hour.
	 */
	public int getHours() {
		return this.hours;
	}

	/**
	 * Set the hour.
	 * @param hours
	 *            The hours to set.
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * Get the minutes.
	 * @return Returns the minutes.
	 */
	public int getMinutes() {
		return this.minutes;
	}

	/**
	 * Set the minutes.
	 * @param minutes
	 *            The minutes to set.
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * Get the seconds.
	 * @return Returns the seconds.
	 */
	public int getSeconds() {
		return this.seconds;
	}

	/**
	 * Set the seconds.
	 * @param seconds
	 *            The seconds to set.
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * Get the milliseconds.
	 * @return Returns the milliseconds.
	 */
	public int getMilliseconds() {
		return this.milliseconds;
	}

	/**
	 * Set the milliseconds.
	 * @param milliseconds
	 *            The milliseconds to set.
	 */
	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
	}

	/**
	 * Returns the time in milliseconds by converting hours, minutes and seconds into milliseconds.
	 * @return the time in milliseconds
	 */
	public double getTime() {
		return this.hours * HOUR_MILLIS + this.minutes * MINUTE_MILLIS + this.seconds * SECOND_MILLIS
				+ this.milliseconds;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ZmanimFormatter(TimeZone.getTimeZone("UTC")).format(this);
	}
}
