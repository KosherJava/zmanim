/*
 * Zmanim Java API
 * Copyright © 2004-2026 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

/**
 * An invalid {@link ZmanimFormatter#setTimeFormat(int)} must throw without changing how the formatter formats.
 */
public class ZmanimFormatterTimeFormatTest {
	private static final Duration NINETY_MINUTES = Duration.ofMinutes(90);

	private static void setInvalid(ZmanimFormatter formatter, int format) {
		try {
			formatter.setTimeFormat(format);
			fail("expected IllegalArgumentException for " + format);
		} catch (IllegalArgumentException expected) {
			// expected
		}
	}

	@Test
	public void invalidFormatLeavesXsdFormatInPlace() {
		ZmanimFormatter formatter = new ZmanimFormatter(ZmanimFormatter.XSD_DURATION_FORMAT, DateTimeFormatter.ofPattern("h:mm:ss"),
				ZoneId.of("America/New_York"));
		assertEquals("PT1H30M", formatter.format(NINETY_MINUTES));
		setInvalid(formatter, 5);
		assertEquals("PT1H30M", formatter.format(NINETY_MINUTES));
	}

	@Test
	public void invalidFormatLeavesSexagesimalFormatInPlace() {
		ZmanimFormatter formatter = new ZmanimFormatter(ZoneId.of("America/New_York"));
		formatter.setTimeFormat(ZmanimFormatter.SEXAGESIMAL_FORMAT);
		setInvalid(formatter, -1);
		assertEquals("1:30", formatter.format(NINETY_MINUTES));
		formatter.setTimeFormat(ZmanimFormatter.XSD_DURATION_FORMAT);
		assertEquals("PT1H30M", formatter.format(NINETY_MINUTES));
	}
}
