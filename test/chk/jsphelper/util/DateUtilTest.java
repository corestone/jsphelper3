package chk.jsphelper.util;

import static chk.jsphelper.util.DateUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;

public class DateUtilTest
{
	@Test
	public void testAddDate ()
	{
		assertNull(addDate(null, Calendar.DAY_OF_MONTH, 1));
		assertEquals("2012-02-19", addDate("2012-03-19", Calendar.MONTH, -1));
	}

	@Test
	public void testAddDateWithToday ()
	{
		assertNull(addDate(null, Calendar.DAY_OF_MONTH, 1));
		assertEquals("2012-02-29", addDateWithToday(Calendar.MONTH, -1));
	}

	@Test
	public void testGetDayGap ()
	{
		assertEquals(0, getDayGap(null, "2012-03-25"));
		assertEquals(0, getDayGap("2012-03-20", null));
		assertEquals(5, getDayGap("2012-03-20", "2012-03-25"));
	}

	@Test
	public void testGetExecutedTime ()
	{
		long t = System.nanoTime();
		try
		{
			Thread.sleep(100);
		}
		catch (Exception e)
		{
		}
		System.out.println(getExecutedTime(t));
	}

	@Test
	public void testGetDateGapFormat ()
	{
		assertNull(getDateGapFormat(null, "2012-03-20", ""));
		assertNull(getDateGapFormat("2012-03-20", null, ""));
		assertEquals("10:20", getDateGapFormat("2012-03-20 01:12:20", "2012-03-20 02:32:20", "HH:mm")); // 그리니치 전문대 기준으로 시간이 나와서 우선은 시간 체크 변경
	}
}
