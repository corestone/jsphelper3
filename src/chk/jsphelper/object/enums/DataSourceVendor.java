package chk.jsphelper.object.enums;

/**
 * @author Corestone
 */
public enum DataSourceVendor implements Symbolic
{
	/**
	 * DB2 DBMS
	 */
	DB2 ("DB2"),
	/**
	 * MS-SQL DBMS
	 */
	MSSQL ("MS-SQL"),
	/**
	 * MySQL DBMS
	 */
	MYSQL ("MySQL"),
	/**
	 * Oracle DBMS
	 */
	ORACLE ("Oracle");

	private String vendorValue = "";

	private DataSourceVendor (final String vendor)
	{
		this.vendorValue = vendor;
	}

	public String getSymbol ()
	{
		return this.vendorValue;
	}
}