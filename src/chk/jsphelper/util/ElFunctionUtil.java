package chk.jsphelper.util;

public final class ElFunctionUtil
{
	public static String oper3 (final boolean condition, final String trueValue, final String falseValue)
	{
		return condition ? trueValue : falseValue;
	}

	private ElFunctionUtil ()
	{
	}
}