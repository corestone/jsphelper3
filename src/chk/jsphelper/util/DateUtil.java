package chk.jsphelper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * 그리니치 첨문대 기준으로 나올 때에는 자바옵션에 -Duser.timezone=Asia/Seoul 를 추가해 주면 된다.
 * 
 * @author Corestone
 */
public class DateUtil
{
	private static final Calendar calendar = Calendar.getInstance();
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private static final SimpleDateFormat sdfSystem = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());

	/**
	 * 특정 시간 문자열에서 특정 시간단위의 특정간격만큼 계산을 하여 yyyy-MM-dd 형식의 10자리 일자문자열로 반한하는 메소드.<br>
	 * 제일 처음 인자값에는 일자를 나타내는 문자열이 들어가는데 yyyyMMdd 형식이나 yyyy-MM-dd 형식 두가지를 지원한다.
	 * 
	 * <pre>
	 * addDate('19991218', Calendar.DAY_OF_YARE, -28) = '1999-11-20'
	 * </pre>
	 * 
	 * @param date
	 *            계산할 초기 일자 문자열
	 * @param unit
	 *            시간 단위로 Calendar의 시간단위 필드값을 사용한다.
	 * @param value
	 *            현재시간에서 시간단위로 이동할 값으로 (+)는 미래 (-)는 과거를 뜻함
	 * @return 계산된 일자 문자열
	 */
	public static String addDate (final String date, final int unit, final int value)
	{
		if (StringUtil.isNullOrEmpty(date))
		{
			return date;
		}
		String datetime = null;
		final Date d = DateUtil.string2Date(date);

		final Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(d.getTime());
		cal.add(unit, value);
		datetime = DateUtil.sdfDate.format(cal.getTime());
		return datetime;
	}

	public static String addDateWithToday (final int unit, final int value)
	{
		return DateUtil.addDate(DateUtil.getCurrentDateTime(), unit, value);
	}

	/**
	 * 한국시간 기준으로 현재일자를 yyyyMMddHHssSSS 형식으로 반환하는 메소드이다.
	 * 
	 * @return 한국시간 기준 현재 시간 17자리
	 */
	public static String getCurrentDateTime ()
	{
		DateUtil.calendar.setTimeInMillis(System.currentTimeMillis());
		return DateUtil.sdfSystem.format(DateUtil.calendar.getTime());
	}

	/**
	 * 처음일시와 마지막일시 사이의 간격을 시간포맷 형식으로 지정한 문자열 형태로 반환하는 메소드이다.
	 * 그리니치 첨문대 기준으로 나올 때에는 자바옵션에 -Duser.timezone=Asia/Seoul 를 추가해 주면 된다.
	 * 
	 * @param fromdate
	 *            - 시작일시 (yyyyMMddHHmmss형식)
	 * @param todate
	 *            - 끝일시 (yyyyMMddHHmmss형식)
	 * @param format
	 *            - 시간 포맷
	 * @return - (끝일시 - 시작일시)을 계산한 시간 값을 포맷에 맞추어 변환한 문자열
	 * @throws Exception
	 */
	public static String getDateGapFormat (final String fromdate, final String todate, final String format)
	{
		if (StringUtil.isNullOrEmpty(fromdate) || StringUtil.isNullOrEmpty(todate))
		{
			return null;
		}
		final Date from = DateUtil.string2Date(fromdate);
		final Date to = DateUtil.string2Date(todate);

		final Calendar gap = Calendar.getInstance();
		gap.setTimeInMillis(to.getTime() - from.getTime());

		final SimpleDateFormat fmtDate = new SimpleDateFormat(format);
		return fmtDate.format(gap.getTime());
	}

	/**
	 * 두 날짜의 간격을 일자로 반환하는 메소드이다.
	 * 
	 * @param fromdate
	 *            - 시작일(yyyyMMdd 형식)
	 * @param todate
	 *            - 끝일(yyyyMMdd 형식)
	 * @return - (끝일 - 시작일)으로 계산한 일수
	 * @throws Exception
	 */
	public static long getDayGap (final String fromdate, final String todate)
	{
		if (StringUtil.isNullOrEmpty(fromdate) || StringUtil.isNullOrEmpty(todate))
		{
			return 0;
		}
		final Date from = DateUtil.string2Date(fromdate);
		final Date to = DateUtil.string2Date(todate);

		return (to.getTime() - from.getTime()) / 0x5265c00L;
	}

	/**
	 * 현재 시간에서 입력받은 인자값이 몇초 지났는지를 반환하는 메소드이다.
	 * 
	 * @param startTime
	 *            - 시간값
	 * @return - 현재와의 차이 결과
	 */
	public static String getExecutedTime (final long startTime)
	{
		final double d = (System.nanoTime() - startTime) / 1000000000D;
		return Double.toString(d) + "초";
	}

	public static Date string2Date (final String theTime)
	{
		try
		{
			final String onlyNumber = theTime.replaceAll("\\D+", "");
			final String digit17 = StringUtils.rightPad(onlyNumber, 17, "0");
			return DateUtil.sdfSystem.parse(digit17);
		}
		catch (final ParseException pe)
		{
			final Calendar c = Calendar.getInstance();
			c.setTimeInMillis(0);
			return c.getTime();
		}
	}

	private DateUtil ()
	{
	}
}