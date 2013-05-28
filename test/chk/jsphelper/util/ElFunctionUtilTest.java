package chk.jsphelper.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElFunctionUtilTest
{

	@Test
	public void testOper3 ()
	{
		assertEquals("T", ElFunctionUtil.oper3(true, "T", "F"));
		assertEquals("F", ElFunctionUtil.oper3(1 == 2, "T", "F"));
	}

}
