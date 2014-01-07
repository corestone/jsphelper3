package chk.jsphelper.util;

import static chk.jsphelper.util.ByteUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ByteUtilTest
{
	@Test
	public void testEqualsBytes ()
	{
		assertTrue(equalsBytes(null, null));
		assertFalse(equalsBytes(null, new byte[] {}));
		assertFalse(equalsBytes(new byte[] {}, null));
		assertTrue(equalsBytes("a".getBytes(), "a".getBytes()));
		assertFalse(equalsBytes("abc".getBytes(), "abcc".getBytes()));
		assertTrue(equalsBytes("abcf".getBytes(), "abcf".getBytes()));
	}

	@Test
	public void testToBytes ()
	{
		assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10 }, int2Bytes(16));
		assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10 }, long2Bytes(16));
	}
}
