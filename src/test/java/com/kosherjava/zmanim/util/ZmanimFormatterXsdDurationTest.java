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

import java.time.Duration;
import java.time.ZoneId;

import javax.xml.datatype.DatatypeFactory;

import org.junit.Test;

/**
 * {@link ZmanimFormatter#formatXSDDurationTime(Duration)} must produce a valid {@code xsd:duration}, including for negative
 * durations (for example a negative offset, or a <em>sha'ah zmanis</em> computed from a start after the end).
 */
public class ZmanimFormatterXsdDurationTest {
	private final ZmanimFormatter formatter = new ZmanimFormatter(ZoneId.of("America/New_York"));

	@Test
	public void positiveDurationsAreUnchanged() {
		assertEquals("PT1H30M", formatter.formatXSDDurationTime(Duration.ofMinutes(90)));
		assertEquals("PT0S", formatter.formatXSDDurationTime(Duration.ZERO));
		assertEquals("", formatter.formatXSDDurationTime(null));
	}

	@Test
	public void negativeDurationsHaveALeadingSign() throws Exception {
		assertEquals("-PT1H15M3S", formatter.formatXSDDurationTime(Duration.ofMinutes(-75).minusSeconds(3)));
		assertEquals("-PT0.5S", formatter.formatXSDDurationTime(Duration.ofMillis(-500)));

		// and the result is a valid xsd:duration with the same value
		Duration negative = Duration.ofHours(-1).minusMinutes(15).minusMillis(250);
		String text = formatter.formatXSDDurationTime(negative);
		assertEquals(negative.toMillis(), DatatypeFactory.newInstance().newDuration(text).getTimeInMillis(new java.util.Date(0)));
	}

	@Test
	public void formatUsesTheXsdFormatForNegativeDurations() {
		ZmanimFormatter xsd = new ZmanimFormatter(ZmanimFormatter.XSD_DURATION_FORMAT,
				java.time.format.DateTimeFormatter.ofPattern("h:mm:ss"), ZoneId.of("America/New_York"));
		assertEquals("-PT30M", xsd.format(Duration.ofMinutes(-30)));
	}
}
