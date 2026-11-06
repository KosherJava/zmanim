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

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

/**
 * {@link ZmanimFormatter#formatInstant(Instant, ZoneId)} must format in the {@code ZoneId} passed to it, not in the formatter's
 * own zone.
 */
public class ZmanimFormatterFormatInstantTest {
	private static final Instant INSTANT = Instant.parse("2020-03-25T21:37:09Z");

	@Test
	public void formatsInTheRequestedZone() {
		ZmanimFormatter formatter = new ZmanimFormatter(ZoneId.of("UTC"));
		assertEquals("9:37:09", formatter.formatInstant(INSTANT, ZoneId.of("UTC")));
		assertEquals("5:37:09", formatter.formatInstant(INSTANT, ZoneId.of("America/New_York"))); // EDT, UTC-4
		assertEquals("11:37:09", formatter.formatInstant(INSTANT, ZoneId.of("Pacific/Kiritimati"))); // UTC+14, the next day
	}

	@Test
	public void worksWithAFormatterSetWithoutAZone() {
		ZmanimFormatter formatter = new ZmanimFormatter(ZoneId.of("UTC"));
		formatter.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		assertEquals("2020-03-26 08:37", formatter.formatInstant(INSTANT, ZoneId.of("Australia/Sydney"))); // AEDT, UTC+11
	}
}
