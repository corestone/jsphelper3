package chk.jsphelper.object.sub;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.SqlBindDir;
import chk.jsphelper.object.enums.SqlBindType;

public class SqlBind
{
	private SqlBindDir dirEnum;
	private String initval;
	private SqlBindType typeEnum;
	private String value;

	public SqlBindDir getDirEnum ()
	{
		return this.dirEnum;
	}

	public String getInitval ()
	{
		return this.initval;
	}

	public SqlBindType getTypeEnum ()
	{
		return this.typeEnum;
	}

	public String getValue ()
	{
		return this.value;
	}

	public void setDir (final String dir)
	{
		if (dir.equals("in"))
		{
			this.dirEnum = SqlBindDir.IN;
		}
		else if (dir.equals("out"))
		{
			this.dirEnum = SqlBindDir.OUT;
		}
		else if (dir.equals("inout"))
		{
			this.dirEnum = SqlBindDir.INOUT;
		}
	}

	public void setInitval (final String initval)
	{
		this.initval = initval;
	}

	public void setType (final String type)
	{
		if (type.equals("int"))
		{
			this.typeEnum = SqlBindType.INT;
		}
		else if (type.equals("float"))
		{
			this.typeEnum = SqlBindType.FLOAT;
		}
		else if (type.equals("string"))
		{
			this.typeEnum = SqlBindType.STRING;
		}
		else if (type.equals("stream"))
		{
			this.typeEnum = SqlBindType.STREAM;
		}
		else if (type.equals("date"))
		{
			this.typeEnum = SqlBindType.DATE;
		}
	}

	public void setValue (final String value)
	{
		this.value = value;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}