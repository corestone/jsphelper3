package chk.jsphelper.util;

import java.io.UnsupportedEncodingException;

/**
 * 문자열 관련 유틸리티 클래스이다
 * 
 * @author Corestone H. Kang
 * @since 1.0
 */
public final class StringUtil
{
	private static int ONE_BYTE_MAX = 0x007F;
	private static int ONE_BYTE_MIN = 0x0000;
	private static int SURROGATE_MAX = 0x10FFFF;
	private static int SURROGATE_MIN = 0x10000;
	private static int THREE_BYTE_MAX = 0xFFFF;
	private static int THREE_BYTE_MIN = 0x0800;
	private static int TWO_BYTE_MAX = 0x07FF;
	private static int TWO_BYTE_MIN = 0x0800;

	/**
	 * 문자열에서 2개 이상의 공백 문자을 하나의 공백문자로 만들어 내용을 압축하는 메소드이다.
	 * 
	 * @param str
	 *            - 압축할 문자열
	 * @return - 압축된 문자열
	 */
	public static String compressWhitespace (final String sValue)
	{
		if (StringUtil.isNullOrEmpty(sValue))
		{
			return sValue;
		}
		final String value = sValue.replaceAll("[\\s]+", " ");
		return value.trim();
	}

	public static String convSQLInjection (final String value)
	{
		if (StringUtil.isNullOrEmpty(value))
		{
			return "";
		}
		String rtnValue = value.replace("'", "''");
		rtnValue = rtnValue.replace("--", "- -");
		rtnValue = rtnValue.replace(";", "");
		rtnValue = rtnValue.replace("&", "");
		rtnValue = rtnValue.replace("+", "");
		rtnValue = rtnValue.replace("%", "");
		return rtnValue;
	}

	/**
	 * ASCII 기본 문자는 1씩 그외 한글 같은 문자는 2씩 계산해서 길이로 잘라주는 메소드이다.
	 * 
	 * @param sValue
	 *            - 자를 문자열
	 * @param iLength
	 *            - 자를 길이
	 * @return - 잘린 문자열
	 */
	public static String cutString (final String sValue, final int iLength)
	{
		if (StringUtil.isNullOrEmpty(sValue))
		{
			return sValue;
		}
		int calcLength = 0; // 계산되는 문자 길이
		final byte[] strByte = StringUtil.getUTF8Bytes(sValue);

		int endPos = strByte.length; // 마지막 바이트 위치
		int currByte = 0; // 현재까지 조사한 바이트 수

		for (int i = 0, z = sValue.length(); i < z; i++)
		{
			// 순차적으로 문자들을 가져옴.
			final char ch = sValue.charAt(i);

			// 이 문자가 몇 바이트로 구성된 UTF-8 코드인지를 검사하여 currByte에 누적 시킨다.
			final int thisCharLength = StringUtil.availibleByteNum(ch);

			calcLength += (thisCharLength == 1 ? 1 : 2);
			currByte += thisCharLength;

			// 현재까지 조사된 바이트가 iLength를 넘는다면 이전 단계 까지 누적된 바이트 까지를 유효한 바이트로 간주한다.
			if (calcLength > iLength)
			{
				endPos = currByte - thisCharLength;
				break;
			}
		}
		// 원래 문자열을 바이트로 가져와서 유효한 바이트 까지 배열 복사를 한다.
		final byte[] newStrByte = new byte[endPos];
		System.arraycopy(strByte, 0, newStrByte, 0, endPos);
		return StringUtil.makeStringWithUTF8Bytes(newStrByte);
	}

	/**
	 * 지정된 문자열 뒤에 빈공백을 채워 넣어 일정한 길이의 문자열을 만드는 메소드<br>
	 * 한글은 2바이트로 인식한다. (만약 지정된 문자열보다 길이가 작다면 글자가 짤려서 표시된다).
	 * 
	 * @param source
	 *            - 지정된 문자열이다.
	 * @param length
	 *            - 반환될 문자열의 길이
	 * @return - 길이만큼 늘였던지 줄였던지 길이가 픽스된 문자열
	 */
	public static String fixLength (final String source, final int length)
	{
		if (source == null)
		{
			return null;
		}
		int calcLength = StringUtil.strLength(source);
		if (calcLength <= length)
		{
			final char[] buf = new char[length - calcLength];
			for (int i = 0, z = buf.length; i < z; i++)
			{
				buf[i] = ' ';
			}
			return source + new String(buf);
		}
		else
		{
			String temp = StringUtil.cutString(source, length);
			calcLength = StringUtil.strLength(temp);
			if (calcLength < length)
			{
				temp = temp + " ";
			}
			return temp;
		}
	}

	/**
	 * 문자열이 모두 몇줄로 이루어진 문자열인지를 알아내는 메소드이다.<br>
	 * 개행문자의 갯수를 세어서 해당 문자의 줄 수를 체크해 낸다.
	 * 
	 * @param content
	 *            몇줄짜리 문장인지 알려고 하는 문자열
	 * @return 해당 문자 줄의 수
	 */
	public static int getLineNum (final String source)
	{
		if (source == null)
		{
			return -1;
		}
		int linenum = 1;
		for (int i = 0; i < source.length(); i++)
		{
			if ('\n' == source.charAt(i))
			{
				linenum++;
			}
		}
		return linenum;
	}

	/**
	 * 인자로 넘어온 문자열을 포맷에 맞게 변환한다.<br>
	 * Ex) "2004년 09월 09일", "####/##/##" ==> 2004/09/09<br>
	 * Ex) "7405261023919", "######-#******" ==> 740526-1******
	 * 
	 * @param value
	 *            변환될 문자열(숫자만 인식하므로 다른 문자는 다 삭제됨)
	 * @param format
	 *            변환방식
	 * @return 변환된 문자열
	 */
	public static String getNumberFormat (final String value, final String format)
	{
		if (StringUtil.isNullOrEmpty(value) || StringUtil.isNullOrEmpty(format))
		{
			return null;
		}
		final String temp = value.replaceAll("[\\D]+", "");
		final StringBuilder sb = new StringBuilder("");
		int cnt = 0;
		for (int i = 0; i < format.length(); i++)
		{
			final char c = format.charAt(i);
			if (c == '#')
			{
				sb.append(temp.substring(cnt, cnt + 1));
				cnt++;
			}
			else if (c == '*')
			{
				sb.append("*");
				cnt++;
			}
			else
			{
				sb.append(String.valueOf(c));
			}
		}
		return sb.toString();
	}

	public static String getOnlyKorean (final String value)
	{
		if (StringUtil.isNullOrEmpty(value))
		{
			return value;
		}
		return value.replaceAll("[^\\u3131-\\u318E\\uAC00-\\uD7A3]+", " ");
	}

	public static boolean isNullOrEmpty (final String str)
	{
		return (str == null) || (str.length() == 0);
	}

	/**
	 * 문자열을 UTF-8 방식으로 길이를 계산해서 잘라주는 메소드이다.
	 * 
	 * @param sValue
	 *            - 자를 문자열
	 * @param iLength
	 *            - 자를 길이
	 * @return - 잘린 문자열
	 */
	public static String[] splitUTF8 (final String sValue, final int iLength, final int size)
	{
		final String[] rtnValue = new String[size];
		if (StringUtil.isNullOrEmpty(sValue))
		{
			rtnValue[0] = sValue;
			return rtnValue;
		}
		final byte[] strByte = StringUtil.getUTF8Bytes(sValue);
		if (strByte.length <= iLength)
		{
			rtnValue[0] = sValue;
			return rtnValue;
		}

		rtnValue[0] = StringUtil.cutString(sValue, iLength);
		String nextString = sValue.replace(rtnValue[0], "");
		for (int i = 1; i < size; i++)
		{
			rtnValue[i] = StringUtil.cutString(nextString, iLength);
			nextString = nextString.replace(rtnValue[i], "");
		}
		return rtnValue;
	}

	/**
	 * 문자열의 길이를 구하는 메소드로 ASCII코드는 1로 한글 같은 나머지 문자는 2로 계산하여 반환한다.
	 * 
	 * @param sValue
	 *            - 길이를 알고 싶은 문자열
	 * @return 계산된 길이
	 */
	public static int strLength (final String sValue)
	{
		if (sValue == null)
		{
			return -1;
		}
		int calcLength = 0; // 계산되는 문자 길이
		for (int i = 0, z = sValue.length(); i < z; i++)
		{
			calcLength += (StringUtil.availibleByteNum(sValue.charAt(i)) == 1 ? 1 : 2);
		}
		return calcLength;
	}

	/**
	 * 문자열을 trim하는데 null이면 빈문자열로 변환해서 반환한다.
	 * 
	 * @param strObj
	 * @return
	 */
	public static String trim (final String strObj)
	{
		return (strObj == null) ? "" : strObj.trim();
	}

	/**
	 * 문자열을 trim하는데 null이거나 빈문자열이면 기본 문자열로 변환해서 반환한다.
	 * 
	 * @param strObj
	 * @param strDefault
	 * @return
	 */
	public static String trimDefault (final String strObj, final String strDefault)
	{
		return StringUtil.isNullOrEmpty(strObj) ? strDefault.trim() : strObj.trim();
	}

	/**
	 * 해당 문자가 UTF-8 인코딩 기준으로 Byte 수를 차지하는 문자인지를 반환하는 메소드이다.
	 * 
	 * @param c
	 *            - 문자코드값
	 * @return - 해당 문자가 몇 Byte 차지하는지에 대한 값
	 */
	private static int availibleByteNum (final char c)
	{
		final int digit = c;
		if ((StringUtil.ONE_BYTE_MIN <= digit) && (digit <= StringUtil.ONE_BYTE_MAX))
		{
			return 1;
		}
		else if ((StringUtil.TWO_BYTE_MIN <= digit) && (digit <= StringUtil.TWO_BYTE_MAX))
		{
			return 2;
		}
		else if ((StringUtil.THREE_BYTE_MIN <= digit) && (digit <= StringUtil.THREE_BYTE_MAX))
		{
			return 3;
		}
		else if ((StringUtil.SURROGATE_MIN <= digit) && (digit <= StringUtil.SURROGATE_MAX))
		{
			return 4;
		}
		return -1;
	}

	private static byte[] getUTF8Bytes (final String sValue)
	{
		try
		{
			return sValue.getBytes("UTF-8");
		}
		catch (final UnsupportedEncodingException uee)
		{

		}
		return null;
	}

	private static String makeStringWithUTF8Bytes (final byte[] bytes)
	{
		try
		{
			return new String(bytes, "UTF-8");
		}
		catch (final UnsupportedEncodingException uee)
		{

		}
		return null;
	}

	private StringUtil ()
	{

	}
}