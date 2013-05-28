package chk.jsphelper.engine.ext.sql;

import chk.jsphelper.object.enums.Symbolic;

public enum DmlType implements Symbolic
{
	ETC ("Etc"), EXECUTE ("Execute"), PROCEDURE ("Procedure"), SELECT ("Select");

	private final String typeValue;

	private DmlType (final String type)
	{
		this.typeValue = type;
	}

	public String getSymbol ()
	{
		return this.typeValue;
	}
}