package chk.jsphelper.object.enums;

/**
 * @author Corestone
 */
public enum SqlBindDir implements Symbolic
{
	/**
	 * 입력 파라미터
	 */
	IN ("In"),
	/**
	 * 입력과 출력을 동시에 하는 파라미터
	 */
	INOUT ("In/Out"),
	/**
	 * 출력 파라미터
	 */
	OUT ("Out");

	private String dirValue = "";

	private SqlBindDir (final String dir)
	{
		this.dirValue = dir;
	}

	public String getSymbol ()
	{
		return this.dirValue;
	}
}