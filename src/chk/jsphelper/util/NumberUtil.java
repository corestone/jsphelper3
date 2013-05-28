package chk.jsphelper.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class NumberUtil
{
	private static NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

	/**
	 * 숫자형 문자열을 로케이션의 포맷에 맞추어서 출력하는 메소드이다.
	 * 
	 * @param src
	 *            - 숫자형 문자열
	 * @return - 컴마가 찍혀서 반환된 숫자형 문자열
	 */
	public static String formatNumber (final String num)
	{
		if (num.replaceAll("[\\d|\\.]+", "").length() == 0)
		{
			return NumberUtil.nf.format(Double.parseDouble(num));
		}
		else
		{
			return "0";
		}
	}

	/**
	 * 숫자형 문자열을 로케이션의 포맷에 맞추어서 출력하는 메소드이다.<br>
	 * 두번째 인자를 통해 소수점 출력자리수를 정할 수 있다.
	 * 
	 * @param src
	 *            - 숫자형의 문자열
	 * @param slen
	 *            - 출력할 소수점 자리수
	 * @return - 컴마가 찍혀서 반환된 숫자형 문자열
	 */
	public static String formatNumber (final String num, final int slen)
	{
		String formatValue = null;
		NumberUtil.nf.setMaximumFractionDigits(slen);
		NumberUtil.nf.setMinimumFractionDigits(slen);
		if (num.replaceAll("[\\d|\\.]+", "").length() == 0)
		{
			formatValue = NumberUtil.nf.format(Double.parseDouble(num));
		}
		else
		{
			formatValue = StringUtils.rightPad("0.", slen + 2, "0");
		}
		NumberUtil.nf.setMaximumFractionDigits(3);
		NumberUtil.nf.setMinimumFractionDigits(0);
		return formatValue;
	}

	/**
	 * 숫자 배열에서 평균값을 구하는 메소드이다.
	 * 
	 * @param data
	 *            - 평균값을 구하려는 숫자 배열
	 * @return - 숫자 배열의 평균값
	 */
	public static double getAverage (final double[] data)
	{
		double sum = 0;
		double mean = 0;
		for (final double element : data)
		{
			sum += element;
		}
		mean = sum / data.length;
		return mean;
	}

	/**
	 * 영어의 찻수 숫자형식으로 변환하여 반환한다.
	 * 
	 * <pre>
	 * 1 -&gt; 1st
	 * 2 -&gt; 2nd
	 * 3 -&gt; 3rd
	 * 4 -&gt; 4th
	 *  ......
	 * </pre>
	 * 
	 * @param iValue
	 *            - 변환할 숫자
	 * @return 변환된 값
	 */
	public static String getEnNumber (final int iValue)
	{
		if (iValue < 1)
		{
			return "";
		}
		switch (iValue)
		{
			case 1 :
				return "1st";
			case 2 :
				return "2nd";
			case 3 :
				return "3rd";
			default :
				return String.valueOf(iValue) + "th";
		}
	}

	/**
	 * 숫자 배열에서 표준편차를 구하는 메소드이다.
	 * 
	 * @param data
	 *            - 표준편차를 구하려는 숫자 배열
	 * @return - 숫자 배열의 표준편차
	 */
	public static double getStanDev (final double[] data)
	{
		double ss = 0;
		double var = 0;
		final double average = NumberUtil.getAverage(data);
		for (final double element : data)
		{
			ss += (element - average) * (element - average);
		}
		var = ss / (data.length - 1);
		return var;
	}

	/**
	 * 그리드에서 컬럽값이 엑셀 형식의 알파벳으로 되어 있어 프로그램적으로 접근하기 불편하기 때문에 해당 컬럼의 인덱스를 가지고 컬럼명을 알아내는 메소드가 필요해서 만들었다.
	 * 
	 * @param colIndex
	 *            - 컬럼 인덱스 첫값은 1이다.
	 * @return 엑셀에서의 컬럼명이 된다. 알파벳 한자리에서 세자리의 문자열이다.
	 */
	public static String int2Col (final int colIndex)
	{
		final int lastPlus = (colIndex / 26) >= 1 ? 1 : 0;
		final char chs[] = new char[3];
		int count = 0;
		final int secondIndex = ((colIndex - 1) % (26 * 26)) / 26;
		final int thirdIndex = (colIndex - 27) / (26 * 26);
		if (thirdIndex > 0)
		{
			chs[count++] = (char) (64 + thirdIndex);
		}
		if (secondIndex > 0)
		{
			chs[count++] = (char) (64 + secondIndex);
		}
		chs[count++] = (char) (64 + ((colIndex - 1) % 26) + lastPlus);
		return new String(chs, 0, count);
	}

	/**
	 * 문자열이 숫자형(Double)으로 변환이 가능한 문자열인지 아닌지를 판단하는 메소드이다.
	 * 
	 * @param str
	 *            - 검증할 문자열
	 * @return - true : 숫자형 문자열 / false : 숫자로 변환이 안되는 문자열
	 */
	public static boolean isDoule (final String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	/**
	 * 문자열이 숫자형(Integer)으로 변환이 가능한 문자열인지 아닌지를 판단하는 메소드이다.
	 * 
	 * @param str
	 *            - 검증할 문자열
	 * @return - true : 숫자형 문자열 / false : 숫자로 변환이 안되는 문자열
	 */
	public static boolean isInteger (final String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	/**
	 * 숫자를 일정한 크기의 문자열로 바꾸어주는 메소드로 앞의 빈공간을 '0'으로 채워준다.
	 * 
	 * @param value
	 *            변환할 숫자
	 * @param length
	 *            숫자의 길이
	 * @return 길이에 맞게 앞에 '0'이 채워진 문자열
	 */
	public static String setNumberLength (final long value, final int length)
	{
		final StringBuilder sbValue = new StringBuilder("1");
		for (int i = 0; i < length; i++)
		{
			sbValue.append("0");
		}
		final String sValue = Long.toString(Long.parseLong(sbValue.toString()) + value);
		return sValue.substring(sValue.length() - length);
	}

	/**
	 * 숫자형 문자열의 길이를 맞추는 메소드이다.<br>
	 * 만약 문자열의 길이가 지정된 길이보다 작으면 '0'을 앞에 채워서 길이를 맞추가 된다.
	 * 
	 * @param value
	 *            - 숫자형 문자열
	 * @param length
	 *            - 길이
	 * @return - 길이를 맞춘 숫자형 문자열
	 */
	public static String setNumberLength (final String value, final int length)
	{
		return NumberUtil.setNumberLength(Long.parseLong(StringUtil.trimDefault(value, "0")), length);
	}

	private NumberUtil ()
	{
	}
}