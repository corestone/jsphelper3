package chk.jsphelper.util;

import static org.junit.Assert.*;

import static chk.jsphelper.util.ColorUtil.*;

import java.awt.Color;

import org.junit.Test;

public class ColorUtilTest
{

	@Test
		public void testColor2Web ()
		{
			assertEquals("#ffffffff", color2Web(new Color(255,255,255)));
		}

	@Test
	public void testMoreColor()
	{
		assertEquals("#333333", moreColor("#666", MODE.DARK));
		assertEquals("#999999", moreColor("#333", MODE.LIGHT));
	}

	@Test
	public void testWeb2Color ()
	{
		assertEquals(new Color(255,255,255), web2Color("#ffffffff"));
	}
}