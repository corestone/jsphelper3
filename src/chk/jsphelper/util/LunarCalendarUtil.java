package chk.jsphelper.util;

import java.text.NumberFormat;

public class LunarCalendarUtil
{
	private final static int DayOfMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // 양력 월별 일수
	// 1 : 작(29일), 2 : 큰(30일), 3 : 작작(윤달 - 29일,29일), 4 : 작큰(윤달 - 29일,30일), 5 : 큰작(윤달 - 30일,29일), 6 : 큰큰(윤달 - 30일,30일)
	private static final int LunarData[][] = {
	/* 1900 ~ 1910 까지 */
	{ 1, 2, 1, 1, 2, 1, 2, 5, 2, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 355 }, // 1900년
	{ 1, 2, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 383 }, // 1900년
	{ 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 2, 4, 1, 2, 1, 2, 1, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 354 }, // 1900년
	{ 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 355 }, // 1900년
	{ 1, 5, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 354 }, // 1900년
	/* 1911 ~ 1920 까지 */
	{ 2, 1, 2, 1, 1, 5, 1, 2, 2, 1, 2, 2, 384 }, // 1900년
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 354 }, // 1900년
	{ 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 354 }, // 1900년
	{ 2, 2, 1, 2, 5, 1, 2, 1, 2, 1, 1, 2, 384 }, // 1900년
	{ 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 354 }, // 1900년
	{ 2, 3, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 384 }, // 1900년
	{ 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 5, 2, 2, 1, 2, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 354 }, // 1900년
	/* 1921 ~ 1930 까지 */
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 354 }, // 1900년
	{ 2, 1, 2, 2, 3, 2, 1, 1, 2, 1, 2, 2, 384 }, // 1900년
	{ 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 354 }, // 1900년
	{ 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 354 }, // 1900년
	{ 2, 1, 2, 5, 2, 1, 2, 2, 1, 2, 1, 2, 385 }, // 1900년
	{ 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 355 }, // 1900년
	{ 1, 5, 1, 2, 1, 1, 2, 2, 1, 2, 2, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 354 }, // 1900년
	{ 1, 2, 2, 1, 1, 5, 1, 2, 1, 2, 2, 1, 383 }, // 1900년
	/* 1931 ~ 1940 까지 */
	{ 2, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 354 }, // 1900년
	{ 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 2, 1, 6, 1, 2, 1, 2, 1, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 4, 1, 2, 1, 2, 1, 2, 2, 2, 1, 384 }, // 1900년
	{ 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 354 }, // 1900년
	{ 2, 2, 1, 1, 2, 1, 4, 1, 2, 2, 2, 1, 384 }, // 1900년
	{ 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 354 }, // 1900년
	{ 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 354 }, // 1900년
	/* 1941 ~ 1950 까지 */
	{ 2, 2, 1, 2, 2, 4, 1, 1, 2, 1, 2, 1, 384 }, // 1900년
	{ 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 355 }, // 1900년
	{ 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 355 }, // 1900년
	{ 1, 1, 2, 4, 1, 2, 1, 2, 2, 1, 2, 2, 384 }, // 1900년
	{ 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 354 }, // 1900년
	{ 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 354 }, // 1900년
	{ 2, 5, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 384 }, // 1900년
	{ 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 354 }, // 1900년
	{ 2, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2, 384 }, // 1900년
	{ 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 354 }, // 1900년
	/* 1951 ~ 1960 까지 */
	{ 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 1, 2, 4, 2, 1, 2, 1, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2, 355 }, // 1900년
	{ 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 354 }, // 1900년
	{ 2, 1, 4, 1, 1, 2, 1, 2, 1, 2, 2, 2, 384 }, // 1900년
	{ 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 354 }, // 1900년
	{ 2, 1, 2, 1, 2, 1, 1, 5, 2, 1, 2, 2, 384 }, // 1900년
	{ 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 354 }, // 1900년
	{ 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 354 }, // 1900년
	{ 2, 1, 2, 1, 2, 5, 2, 1, 2, 1, 2, 1, 384 }, // 1900년
	/* 1961 ~ 1970 까지 */
	{ 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 2, 3, 2, 1, 2, 1, 2, 2, 2, 1, 384 }, // 1900년
	{ 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 355 }, // 1900년
	{ 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 353 }, // 1900년
	{ 2, 2, 5, 2, 1, 1, 2, 1, 1, 2, 2, 1, 384 }, // 1900년
	{ 2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 2, 1, 2, 1, 5, 2, 1, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 354 }, // 1900년
	{ 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 355 }, // 1900년
	/* 1971 ~ 1980 까지 */
	{ 1, 2, 1, 1, 5, 2, 1, 2, 2, 2, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 1, 354 }, // 1900년
	{ 2, 2, 1, 5, 1, 2, 1, 1, 2, 2, 1, 2, 384 }, // 1900년
	{ 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 354 }, // 1900년
	{ 2, 2, 1, 2, 1, 2, 1, 5, 2, 1, 1, 2, 384 }, // 1900년
	{ 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 354 }, // 1900년
	{ 2, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 355 }, // 1900년
	{ 2, 1, 1, 2, 1, 6, 1, 2, 2, 1, 2, 1, 384 }, // 1900년
	{ 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 355 }, // 1900년
	/* 1981 ~ 1990 까지 */
	{ 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 354 }, // 1900년
	{ 2, 1, 2, 3, 2, 1, 1, 2, 2, 1, 2, 2, 384 }, // 1900년
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 354 }, // 1900년
	{ 2, 1, 2, 2, 1, 1, 2, 1, 1, 5, 2, 2, 384 }, // 1900년
	{ 1, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 354 }, // 1900년
	{ 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 354 }, // 1900년
	{ 2, 1, 2, 2, 1, 5, 2, 2, 1, 2, 1, 2, 385 }, // 1900년
	{ 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 2, 355 }, // 1900년
	{ 1, 2, 1, 1, 5, 1, 2, 1, 2, 2, 2, 2, 384 }, // 1900년
	/* 1991 ~ 2000 까지 */
	{ 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 354 }, // 1900년
	{ 1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 354 }, // 1900년
	{ 1, 2, 5, 2, 1, 2, 1, 1, 2, 1, 2, 1, 383 }, // 1900년
	{ 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 355 }, // 1900년
	{ 1, 2, 2, 1, 2, 2, 1, 5, 2, 1, 1, 2, 384 }, // 1900년
	{ 1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 355 }, // 1900년
	{ 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 1900년
	{ 2, 1, 1, 2, 3, 2, 2, 1, 2, 2, 2, 1, 384 }, // 1900년
	{ 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 354 }, // 1900년
	{ 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 354 }, // 2000년
	/* 2001 ~ 2010 까지 */
	{ 2, 2, 2, 3, 2, 1, 1, 2, 1, 2, 1, 2, 384 }, // 2000년
	{ 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 354 }, // 2000년
	{ 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 355 }, // 2000년
	{ 1, 5, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 384 }, // 2000년
	{ 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 1, 354 }, // 2000년
	{ 2, 1, 2, 1, 2, 1, 5, 2, 2, 1, 2, 2, 385 }, // 2000년
	{ 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 354 }, // 2000년
	{ 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 354 }, // 2000년
	{ 2, 2, 1, 1, 5, 1, 2, 1, 2, 1, 2, 2, 384 }, // 2000년
	{ 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 354 }, // 2000년
	/* 2011 ~ 2020 까지 */
	{ 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 354 }, // 2000년
	{ 2, 1, 6, 2, 1, 2, 1, 1, 2, 1, 2, 1, 384 }, // 2000년
	{ 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 355 }, // 2000년
	{ 1, 2, 1, 2, 1, 2, 1, 2, 5, 2, 1, 2, 384 }, // 2000년
	{ 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 2, 355 }, // 2000년
	{ 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 354 }, // 2000년
	{ 2, 1, 1, 2, 3, 2, 1, 2, 1, 2, 2, 2, 384 }, // 2000년
	{ 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 354 }, // 2000년
	{ 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 354 }, // 2000년
	{ 2, 1, 2, 5, 2, 1, 1, 2, 1, 2, 1, 2, 384 }, // 2000년
	/* 2021 ~ 2030 까지 */
	{ 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 354 }, // 2000년
	{ 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 355 }, // 2000년
	{ 1, 5, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 384 }, // 2000년
	{ 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 354 }, // 2000년
	{ 2, 1, 2, 1, 1, 5, 2, 1, 2, 2, 2, 1, 384 }, // 2000년
	{ 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 355 }, // 2000년
	{ 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 354 }, // 2000년
	{ 1, 2, 2, 1, 5, 1, 2, 1, 1, 2, 2, 1, 383 }, // 2000년
	{ 2, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 2, 355 }, // 2000년
	{ 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 354 }, // 2000년
	/* 2031 ~ 2040 까지 */
	{ 2, 1, 5, 2, 1, 2, 2, 1, 2, 1, 2, 1, 384 }, // 2000년
	{ 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 355 }, // 2000년
	{ 1, 2, 1, 1, 2, 1, 5, 2, 2, 2, 1, 2, 384 }, // 2000년
	{ 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 354 }, // 2000년
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 354 }, // 2000년
	{ 2, 2, 1, 2, 1, 4, 1, 1, 2, 1, 2, 2, 384 }, // 2000년
	{ 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 354 }, // 2000년
	{ 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 354 }, // 2000년
	{ 2, 2, 1, 2, 5, 2, 1, 2, 1, 2, 1, 1, 384 }, // 2000년
	{ 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 355 }, // 2000년
	/* 2041 ~ 2050 까지 */
	{ 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 355 }, // 2000년
	{ 1, 5, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 384 }, // 2000년
	{ 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 354 }, // 2000년
	{ 2, 1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 2, 384 }, // 2000년
	{ 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 354 }, // 2000년
	{ 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 354 }, // 2000년
	{ 2, 1, 2, 2, 4, 1, 2, 1, 1, 2, 1, 2, 384 }, // 2000년
	{ 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 1, 1, 353 }, // 2000년
	{ 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 355 }, // 2000년
	{ 2, 1, 4, 1, 2, 1, 2, 2, 1, 2, 2, 1, 384 } }; // 2000년
	private final static int LunarDataNumberDay[] = { 29, 30, 58, 59, 59, 60 }; // 인덱스 숫자의 합

	/**
	 * 해당 연도가 윤년인지 여부를 체크한다.
	 * 
	 * @param Year
	 *            -
	 * @return -
	 */
	public static boolean checkYunYear (final int Year)
	{
		if (((Year % 4) == 0) && (((Year % 100) != 0) || ((Year % 400) == 0)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 음력을 양력으로 변환하는 메소드이다.
	 * 
	 * @param TranseDay
	 * @param leapyes
	 * @return
	 */
	public static String lunar2Solar (final String TranseDay, final boolean leapyes)
	{
		String sValue = "";

		final int lyear = Integer.parseInt(TranseDay.substring(0, 4));
		final int lmonth = Integer.parseInt(TranseDay.substring(4, 6));
		final int lday = Integer.parseInt(TranseDay.substring(6, 8));

		final long coutnAllDay = LunarCalendarUtil.countLunarDay(lyear, lmonth, lday, leapyes);

		sValue = LunarCalendarUtil.countToDateForSolar(coutnAllDay);
		return sValue;
	}

	/**
	 * 양력을 음력으로 변환하는 메소드이다.
	 * 
	 * @param TranseDay
	 * @return
	 */
	public static String solar2Lunar (final String TranseDay)
	{
		String sValue = "";

		final int lyear = Integer.parseInt(TranseDay.substring(0, 4));
		final int lmonth = Integer.parseInt(TranseDay.substring(4, 6));
		final int lday = Integer.parseInt(TranseDay.substring(6, 8));

		final long coutnAllDay = LunarCalendarUtil.countSolarDay(lyear, lmonth, lday);

		sValue = LunarCalendarUtil.countToDateForLunar(coutnAllDay);

		return sValue;
	}

	/**
	 * 음력의 총날짜수를 돌려준다. 만약 돌려줄수 없는 날이라면 0을 돌려준다
	 * 
	 * @param Year
	 *            -
	 * @param Month
	 *            -
	 * @param Day
	 *            -
	 * @param Leap
	 *            -
	 * @return -
	 */
	private static long countLunarDay (int Year, final int Month, final int Day, final boolean Leap)
	{
		long AllCount = 0;
		long ResultValue = 0;
		int i = 0;
		Year -= 1900;
		AllCount += LunarCalendarUtil.countSolarDay(1900, 1, 30);

		if (Year >= 0)
		{
			for (i = 0; i <= (Year - 1); i++)
			{
				AllCount += LunarCalendarUtil.LunarData[i][12];
			}
			for (i = 0; i <= (Month - 2); i++)
			{
				AllCount += LunarCalendarUtil.LunarDataNumberDay[LunarCalendarUtil.LunarData[Year][i] - 1];
			}
			if (!Leap)
			{
				AllCount += Day;
			}
			else
			{
				if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 1) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 2))
				{
					AllCount += Day;
				}
				else if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 3) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 4))
				{
					AllCount += Day + 29;
				}
				else if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 5) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 6))
				{
					AllCount += Day + 30;
				}
			}
			ResultValue = AllCount;
		}
		else
		{
			ResultValue = 0;
		}
		return ResultValue;
	}

	/* 특징 : 양력의 총 날짜수를 돌려준다 */
	/**
	 * 양력의 총 날짜수를 돌려준다
	 * 
	 * @param Year
	 *            -
	 * @param Month
	 *            -
	 * @param Day
	 *            -
	 * @return -
	 */
	private static long countSolarDay (final int Year, final int Month, final int Day)
	{
		int i, j = 0;
		long AllCount = 366;
		for (i = 1; i <= (Year - 1); i++)
		{
			if (LunarCalendarUtil.checkYunYear(i))
			{
				AllCount += 366;
			}
			else
			{
				AllCount += 365;
			}
		}
		for (j = 1; j <= (Month - 1); j++)
		{
			if (j == 2)
			{
				if (LunarCalendarUtil.checkYunYear(Year))
				{
					AllCount += 29;
				}
				else
				{
					AllCount += LunarCalendarUtil.DayOfMonth[j - 1];
				}
			}
			else
			{
				AllCount += LunarCalendarUtil.DayOfMonth[j - 1];
			}
		}
		AllCount += Day;
		return AllCount;
	}

	/**
	 * 총날짜를 가지고 가지고 음력 날짜을 변환해서 반환한다.
	 * 
	 * @param AllCountDay
	 * @return
	 */
	private static String countToDateForLunar (final long AllCountDay)
	{
		long AllCount = 0;
		int Year, Month, Day = 0;
		// {LDNC : Lunar Data Number Count}
		boolean RepeatStop;

		Year = 0;
		Month = 1;
		Day = 0;
		RepeatStop = false;

		AllCount = AllCountDay;
		AllCount -= LunarCalendarUtil.countSolarDay(1900, 1, 30);

		do
		{
			if (AllCount > LunarCalendarUtil.LunarData[Year][12])
			{
				AllCount -= LunarCalendarUtil.LunarData[Year][12];
				Year += 1; // 년 계산
			}
			else
			{
				if (AllCount > LunarCalendarUtil.LunarDataNumberDay[LunarCalendarUtil.LunarData[Year][Month - 1] - 1])
				{
					// 월계산
					AllCount -= LunarCalendarUtil.LunarDataNumberDay[LunarCalendarUtil.LunarData[Year][Month - 1] - 1];
					Month += 1;
				}
				else
				{
					if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 1) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 2))
					{
						Day = Integer.parseInt(Long.toString(AllCount));
					}
					else if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 3) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 4))
					{
						if (AllCount <= 29)
						{
							Day = Integer.parseInt(Long.toString(AllCount));
						}
						else
						{
							Day = Integer.parseInt(Long.toString(AllCount)) - 29;
						}
					}
					else if ((LunarCalendarUtil.LunarData[Year][Month - 1] == 5) || (LunarCalendarUtil.LunarData[Year][Month - 1] == 6))
					{
						if (AllCount <= 30)
						{
							Day = Integer.parseInt(Long.toString(AllCount));
						}
						else
						{
							Day = Integer.parseInt(Long.toString(AllCount)) - 30;
						}
					}
					RepeatStop = true;
				}
			}
		}
		while (!RepeatStop);

		final NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(2);

		final String returnLunar = Long.toString(Year + 1900) + nf.format(Integer.parseInt(Long.toString(Month))) + nf.format(Integer.parseInt(Long.toString(Day)));
		return returnLunar;
	}

	/**
	 * 총날짜를 가지고 가지고 양력 날짜을 변환해서 반환한다.
	 * 
	 * @param AllCountDay
	 * @return
	 */
	private static String countToDateForSolar (long AllCountDay)
	{
		int Year, Month = 0;
		boolean YearRepeatStop, MonthRepeatStop;
		YearRepeatStop = false;
		MonthRepeatStop = false;
		Year = 0;
		Month = 1;
		do
		{
			if (LunarCalendarUtil.checkYunYear(Year))
			{
				if (AllCountDay > 366)
				{
					AllCountDay -= 366;
					Year += 1;
				}
				else
				{
					YearRepeatStop = true;
				}
			}
			else
			{
				if (AllCountDay > 365)
				{
					AllCountDay -= 365;
					Year += 1;
				}
				else
				{
					YearRepeatStop = true;
				}
			}
		}
		while (!YearRepeatStop);

		do
		{
			if (Month == 2)
			{
				if (LunarCalendarUtil.checkYunYear(Year))
				{
					if (AllCountDay > 29)
					{
						AllCountDay -= 29;
						Month += 1;
					}
					else
					{
						MonthRepeatStop = true;
					}
				}
				else
				{
					if (AllCountDay > 28)
					{
						AllCountDay -= 28;
						Month += 1;
					}
					else
					{
						MonthRepeatStop = true;
					}
				}
			}
			else
			{
				if (AllCountDay > LunarCalendarUtil.DayOfMonth[Month - 1])
				{
					AllCountDay -= LunarCalendarUtil.DayOfMonth[Month - 1];
					Month += 1;
				}
				else
				{
					MonthRepeatStop = true;
				}
			}
		}
		while (!MonthRepeatStop);

		final NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(2);

		final String returnLunar = Long.toString(Year) + nf.format(Integer.parseInt(Long.toString(Month))) + nf.format(Integer.parseInt(Long.toString(AllCountDay)));
		return returnLunar;
	}
}