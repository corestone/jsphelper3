package chk.jsphelper.util;

import static chk.jsphelper.util.StringUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringUtilTest
{

	@Test
	public void testCompressWhitespace ()
	{
		assertNull(compressWhitespace(null));
		assertEquals("", compressWhitespace(""));
		assertEquals("", compressWhitespace(" 	\n"));
		assertEquals("c $ h 7 k", compressWhitespace("c  	$   h 	7  k "));
		assertEquals("한글 은", compressWhitespace("	한글	은  "));
		assertEquals("c $h 7 k", compressWhitespace("c	$h	 7 \n k"));
		assertEquals("한 글 은", compressWhitespace(" 	한\n글  	은	"));
	}

	@Test
	public void testCutString ()
	{
		assertNull(cutString(null, 10));
		assertEquals("", cutString("", 0));
		assertEquals("", cutString("", 10));
		assertEquals("", cutString("abc", 0));
		assertEquals("", cutString("한글", 0));
		assertEquals("abcd", cutString("abcdefg", 4));
		assertEquals("ab c", cutString("ab cd ef g", 4));
		assertEquals("한글은 되나", cutString("한글은 되나", 100));
	}

	@Test
	public void testStrLength ()
	{
		assertEquals(-1, strLength(null));
		assertEquals(3, strLength("123"));
		assertEquals(6, strLength("한글은"));
		assertEquals(9, strLength("한글 은 1"));
	}

	@Test
	public void testFixLength ()
	{
		assertNull(fixLength(null, 10));
		assertEquals("   ", fixLength("", 3));
		assertEquals("100   ", fixLength("100", 6));
		assertEquals("10", fixLength("100", 2));
		assertEquals("한글은", fixLength("한글은 되나", 6));
		assertEquals("한글 ", fixLength("한글은 되나", 5));
		assertEquals("한글은 되나    ", fixLength("한글은 되나", 15));
	}

	@Test
	public void testGetNumberFormat ()
	{
		assertNull(getNumberFormat(null, ""));
		assertNull(getNumberFormat("", null));
		assertNull(getNumberFormat("", ""));
		assertNull(getNumberFormat("99", null));
		assertNull(getNumberFormat(null, "99"));
		assertNull(getNumberFormat("", "99"));
		assertNull(getNumberFormat("99", ""));
		assertEquals("9", getNumberFormat("99", "#"));
		assertEquals("9-8", getNumberFormat("98", "#-#"));
		assertEquals("9*7", getNumberFormat("987", "#*#"));
		assertEquals("9/*/7", getNumberFormat("987", "#/*/#"));
		assertEquals("9/*/7", getNumberFormat("한글9들87", "#/*/#"));
	}

	@Test
	public void testGetLineNum ()
	{
		assertEquals(-1, getLineNum(null));
		assertEquals(1, getLineNum(""));
		assertEquals(2, getLineNum("\n"));
		assertEquals(1, getLineNum("asdfsdf"));
		assertEquals(2, getLineNum("\nasdfsdf"));
		assertEquals(2, getLineNum("asdfsdf\n"));
		assertEquals(2, getLineNum("asdf\nsdf"));
		assertEquals(2, getLineNum("한글\n은 제대로 되나"));
		assertEquals(3, getLineNum("한글\n\n은 제대로 되나"));
	}

	@Test
	public void testGetOnlyKorean ()
	{
		assertNull(getOnlyKorean(null));
		assertEquals("", getOnlyKorean(""));
		assertEquals(" ", getOnlyKorean("123aB@% "));
		assertEquals("한글과 스페이스", getOnlyKorean("한글과 스페이스"));
		assertEquals("한글과 ", getOnlyKorean("한글과 123456"));
		assertEquals(" 년 월 일", getOnlyKorean("2004년\n 09월 09일"));
		assertEquals("똠뷁", getOnlyKorean("똠뷁"));
	}

	@Test
	public void testTrim ()
	{
		assertEquals("", trim(null));
		assertEquals("123aB@%", trim("123aB@% "));
		assertEquals("123aB@%", trim(" 123aB@% \n	"));
		assertEquals("한글과 스페이스", trim("		한글과 스페이스\n\n"));
	}

	@Test
	public void testTrimDefault ()
	{
		assertEquals("", trimDefault(null, ""));
		assertEquals("1", trimDefault(null, "1"));
		assertEquals("2", trimDefault("", "2"));
		assertEquals("2 d", trimDefault("", "2 d "));
		assertEquals("c 5", trimDefault("	c 5 \n", "2"));
	}
}
