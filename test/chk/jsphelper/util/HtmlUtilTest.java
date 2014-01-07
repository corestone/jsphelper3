package chk.jsphelper.util;

import static chk.jsphelper.util.HtmlUtil.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HtmlUtilTest
{
	@Test
	public void testCutTitle ()
	{
		String test_ko = "한글 테스트 문자열입니다.";
		assertEquals("<span title='한글 테스트 문자열입니다.'>한글 테스..</span>", cutTitle(test_ko, 10));
	}

	@Test
	public void testEncHTML ()
	{
		assertEquals("test<br/>love", encHTML("test\nlove"));
		assertEquals("test.jsp?id=test&amp;pw=test", encHTML("test.jsp?id=test&pw=test"));
		assertEquals("&lt;title&gt;", encHTML("<title>"));
		assertEquals("test<br/>love", encHTML("test\nlove"));
	}

	@Test
	public void testRemoveTag ()
	{
		assertEquals("타이틀", removeTag("<title>타이틀</title>"));
		assertEquals("입니다", removeTag("입니다<br /><script type=\"text/javascript\">alert('chk');</script>"));
		assertEquals("입니다", removeTag("<dt>입니다<div id=dif><style type=\"text/css\">dif {color:fff;}</style></div>"));
	}

	@Test
	public void testLine2Br ()
	{
		assertEquals("test<br/>love", line2Br("test\nlove"));
	}
}
