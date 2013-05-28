package chk.jsphelper.util;

import static org.junit.Assert.*;
import static chk.jsphelper.util.NumberUtil.*;

import org.junit.Test;

public class NumberUtilTest
{
	@Test
	public void testFormatNumber ()
	{
		assertEquals("2,002,123.557", formatNumber("2002123.5566"));
		assertEquals("2,002,123.5500", formatNumber("2002123.55", 4));
		assertEquals("0.00000", formatNumber("2002123.55차", 5));
	}

	@Test
	public void testGetAverage ()
	{
		final double[] data = new double[] {8,9,10,11,12};
		assertTrue(getAverage(data) == 10);
	}

	@Test
	public void testGetEnNumber ()
	{
		assertEquals("1st", getEnNumber(1));
		assertEquals("2nd", getEnNumber(2));
		assertEquals("3rd", getEnNumber(3));
		assertEquals("4th", getEnNumber(4));
		assertEquals("10th", getEnNumber(10));
	}

	@Test
	public void testGetStanDev ()
	{
		final double[] data = new double[] {8,9,10,11,12};
		assertTrue(getStanDev(data) == 2.5);
	}

	@Test
	public void testInt2Col ()
	{
		assertEquals("Z", int2Col(26));
		assertEquals("AA", int2Col(27));
		assertEquals("AU", int2Col(47));
		assertEquals("AAA", int2Col(26*27+1));
		assertEquals("XFD", int2Col(16384));
	}

	public void testSetNumberLength ()
	{
		assertEquals("01552", setNumberLength(1552, 5));
		assertEquals("01552", setNumberLength("1552", 5));
		assertEquals("00000", setNumberLength(0, 5));
		assertEquals("000", setNumberLength("0", 3));
		assertEquals("1552", setNumberLength(1552, 3));
	}
}