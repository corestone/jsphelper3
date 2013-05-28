package chk.jsphelper.object.enums;

/**
 * @author Corestone
 */
public enum ExcelDataType implements Symbolic
{
	/**
	 * DataList Object
	 */
	DATALIST ("DataList"),
	/**
	 * DB Table
	 */
	DB ("DB"),
	/**
	 * Excel File
	 */
	EXCEL ("Excel");

	private String typeValue = "";

	private ExcelDataType (final String value)
	{
		this.typeValue = value;
	}

	public String getSymbol ()
	{
		return this.typeValue;
	}
}
