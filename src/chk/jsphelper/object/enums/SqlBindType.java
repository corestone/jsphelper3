package chk.jsphelper.object.enums;

/**
 * @author Corestone
 */
public enum SqlBindType implements Symbolic
{
	/**
	 * 날짜와 시간
	 */
	DATE ("date"),
	/**
	 * 실수형의 숫자
	 */
	FLOAT ("float"),
	/**
	 * 정수형의 숫자
	 */
	INT ("int"),
	/**
	 * CLOB 형태의 값
	 */
	STREAM ("stream"),
	/**
	 * 문자열
	 */
	STRING ("string");

	private String typeValue = "";

	private SqlBindType (final String type)
	{
		this.typeValue = type;
	}

	public String getSymbol ()
	{
		return this.typeValue;
	}
}