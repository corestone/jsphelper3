package chk.jsphelper.util;

import static chk.jsphelper.util.LunarCalendarUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LunarCalendarUtilTest
{
	@Test
	public void test ()
	{
		assertTrue(checkYunYear(2012));
		assertEquals("19720703", lunar2Solar("19720523", false));
		assertEquals("19700308", solar2Lunar("19700413"));
	}
}
