/*
 * Zmanim Java API
 * Copyright (C) 2004-2014 Eliyahu Hershfeld
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
package net.sourceforge.zmanim;

/**
 * Enumeration of <em>shaah zmanis</em> (temporal hour).
 *
 * @author &copy; Moshe Waisberg 2017
 */
public enum ShaahZmanis {

    /** According to the opinion of the <em>GRA</em> and the <em>Baal Hatanya</em>. */
    GRA,
    /** According to the opinion of the Magen Avraham. */
    MGA,
    /** Calculated using a dip of 60 minutes. */
    MINUTES_60,
    /** Calculated using a dip of 72 minutes. */
    MINUTES_72,
    /** Calculated using a dip of 90 minutes. */
    MINUTES_90,
    /** Calculated using a dip of 96 minutes. */
    MINUTES_96,
    /** Calculated using a dip of 120 minutes. */
    MINUTES_120,
    /** Calculated using a dip of 16.1&deg;. */
    DEGREES_16POINT1,
    /** Calculated using a dip of 18&deg;. */
    DEGREES_18,
    /** Calculated using a dip of 19.8&deg;. */
    DEGREES_19POINT8,
    /** Calculated using a dip of 26&deg;. */
    DEGREES_26
}
