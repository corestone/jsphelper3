package chk.jsphelper.util;

import static chk.jsphelper.util.EncryptUtil.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EncryptUtilTest
{
	private String test_ko = "테스트할 한글 문자열";
	private String test_en = "Corestone H. Kang";

	@Test
	public void testEncryptDecrypt ()
	{
		try
		{
			String enc_ko = encrypt(test_ko);
			assertEquals(test_ko, decrypt(enc_ko));

			String enc_en = encrypt(test_en);
			assertEquals(test_en, decrypt(enc_en));
		}
		catch (Exception e)
		{

		}
	}

	@Test
	public void testBase64EncodeDecode ()
	{
		try
		{
			String enc_en = getBase64Encode(test_en.getBytes());
			assertEquals(test_en, new String(getBase64Decode(enc_en)));
		}
		catch (Exception e)
		{

		}
	}

	@Test
	public void testOneSideEcrypt ()
	{
		System.out.println(test_ko + " [getMD5] : " + getMD5(test_ko));
		System.out.println(test_ko + " [getDoubleMD5] : " + getDoubleMD5(test_ko));
		System.out.println(test_ko + " [oneSideEcrypt] : " + oneSideEcrypt(test_ko));
		System.out.println(test_en + " [getMD5] : " + getMD5(test_en));
		System.out.println(test_en + " [getDoubleMD5] : " + getDoubleMD5(test_en));
		System.out.println(test_en + " [oneSideEcrypt] : " + oneSideEcrypt(test_en));
	}
}
