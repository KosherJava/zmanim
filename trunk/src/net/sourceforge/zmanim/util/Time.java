/*
 * Zmanim Java API
 * Copyright (C) 2004-2007 Eliyahu Hershfeld
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc. 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA or connect to: http://www.fsf.org/copyleft/gpl.html
 */
package net.sourceforge.zmanim.util;

/**
 * A class that represents a numeric time. Times that represent a time of day
 * are stored as {@link java.util.Date}s in this API. The time class is used to
 * represent numeric time such as the time in hours, minutes, seconds and
 * milliseconds of a
 * {@link net.sourceforge.zmanim.AstronomicalCalendar#getTemporalHour() temporal hour}.
 *
 * @author &copy; Eliyahu Hershfeld 2004 - 2007
 * @version 0.9.0
 */
public class Time {
	private static final int SECOND_MILLIS = 1000;

	private static final int MINUTE_MILLIS = SECOND_MILLIS * 60;

	private static final int HOUR_MILLIS = MINUTE_MILLIS * 60;

	private int hours = 0;

	private int minutes = 0;

	private int seconds = 0;

	private int milliseconds = 0;

	private boolean isNegative = false;

	public Time(int hours, int minutes, int seconds, int milliseconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	public Time(double millis) {
		this((int) millis);
	}

	public Time(int millis) {
		if(millis < 0){
			isNegative = true;
			millis = Math.abs(millis);
		}
		hours = millis / HOUR_MILLIS;
		millis = millis - hours * HOUR_MILLIS;

		minutes = millis / MINUTE_MILLIS;
		millis = millis - minutes * MINUTE_MILLIS;

		seconds = millis / SECOND_MILLIS;
		millis = millis - seconds * SECOND_MILLIS;

		milliseconds = millis;
	}

	public boolean isNegative(){
		return isNegative;
	}
	public void setIsNegative(boolean isNegative){
		this.isNegative = isNegative;
	}

	/**
	 * @return Returns the hour.
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours
	 *            The hours to set.
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return Returns the minutes.
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes
	 *            The minutes to set.
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return Returns the seconds.
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds
	 *            The seconds to set.
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * @return Returns the milliseconds.
	 */
	public int getMilliseconds() {
		return milliseconds;
	}

	/**
	 * @param milliseconds
	 *            The milliseconds to set.
	 */
	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
	}

	public double getTime() {
		return hours * HOUR_MILLIS + minutes * MINUTE_MILLIS + seconds
				* SECOND_MILLIS + milliseconds;
	}

	public String toString() {
		return new ZmanimFormatter().format(this);
	}
}
