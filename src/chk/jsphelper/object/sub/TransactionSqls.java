package chk.jsphelper.object.sub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionSqls
{
	private int pageindex;
	private final List<String> paramKeys = new ArrayList<String>();
	private final List<String> paramValues = new ArrayList<String>();
	private final List<String> returnFields = new ArrayList<String>();
	private final List<String> returnKeys = new ArrayList<String>();
	private String sql;

	public void addParams (final TransactionSqlsParam sqlParams)
	{
		this.paramKeys.add(sqlParams.getKey());
		this.paramValues.add(sqlParams.getValue());
	}

	public void addReturns (final TransactionSqlsReturn sqlReturns)
	{
		this.returnKeys.add(sqlReturns.getKey());
		this.returnFields.add(sqlReturns.getField());
	}

	public int getPageindex ()
	{
		return this.pageindex;
	}

	public List<String> getParamKeys ()
	{
		return this.paramKeys;
	}

	public List<String> getParamValues ()
	{
		return this.paramValues;
	}

	public List<String> getReturnFields ()
	{
		return this.returnFields;
	}

	public List<String> getReturnKeys ()
	{
		return this.returnKeys;
	}

	public String getSql ()
	{
		return this.sql;
	}

	public void setPageindex (final int pageindex)
	{
		this.pageindex = pageindex;
	}

	public void setSql (final String sql)
	{
		this.sql = sql;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}