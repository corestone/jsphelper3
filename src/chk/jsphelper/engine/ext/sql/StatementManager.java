package chk.jsphelper.engine.ext.sql;

import chk.jsphelper.module.wrapper.MapStringsAdapter;
import chk.jsphelper.object.sub.SqlBind;
import chk.jsphelper.util.DateUtil;
import chk.jsphelper.util.NumberUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class StatementManager
{
	protected final List<SqlBind> bindValue;
	protected long executeTime = 0L;
	protected final MapStringsAdapter map;
	protected final List<String> parameterValues;
	protected int paramIndex;
	protected String sqlString = "";

	public StatementManager (final List<SqlBind> bindValue, final MapStringsAdapter map)
	{
		this.parameterValues = new ArrayList<String>();
		this.bindValue = bindValue;
		this.map = map;
	}

	public void closeStatement (Statement stmt)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (final SQLException e)
			{
			}
			stmt = null;
		}
		this.parameterValues.clear();
	}

	/**
	 * @return - 쿼리가 실행된 시간
	 */
	public String getExecuteTime ()
	{
		final double d = (this.executeTime / 1000000000D);
		return NumberUtil.formatNumber(Double.toString(d), 4) + "초";
	}

	public String getQueryString ()
	{
		final StringBuilder sb = new StringBuilder();
		int qMarkCount = 0;
		// PreparedStatement는 ?,?,? 형태로 작성됨으로, '?'를 기준으로 문자열을 분해한다.
		final StringTokenizer tok = new StringTokenizer(this.sqlString + " ", "?");
		while (tok.hasMoreTokens())
		{
			final String oneChunk = tok.nextToken();
			// 저장될 문자열 Buffer에 값부분의 '?'를 제외한 나머지 String을 buffer에 추가한다.
			sb.append(oneChunk);
			// parameterValues arrayList에 저장했던 값을 불러와 buffer에 추가한다.
			String value;
			if ((qMarkCount + 1) < this.map.size())
			{
				value = this.parameterValues.get(1 + qMarkCount++);
			}
			else
			{
				value = "";
			}
			sb.append(value);
		}
		// 완성된 sql문을 리턴한다.
		return sb.toString().trim();
	}

	protected String getParamValue (final String value, final int index, final String defaultValue)
	{
		String paramValue;
		if ((this.map.get(value) == null) || (this.map.get(value).length == 0))
		{
			// default 값이 "_index"일 경우 1부터 시작하는 인덱스 값이 자동으로 입력된다.
			if (defaultValue.equals("_index"))
			{
				paramValue = String.valueOf(this.paramIndex + 1);
			}
			else
			{
				paramValue = defaultValue;
			}
		}
		else if (this.map.get(value).length == 1)
		{
			paramValue = this.map.get(value)[0];
		}
		else
		{
			paramValue = this.map.get(value)[this.paramIndex];
		}
		return paramValue;
	}

	protected void saveQueryParamValue (final int position, final Object value)
	{
		String strValue;
		// String이나 Date는 'value' 형태로, null값은 null로, 기타값은 value 형태로 변환해준다.
		if (value == null)
		{
			strValue = "null";
		}
		else if ((value instanceof String) || (value instanceof Date))
		{
			strValue = "'" + value + "'";
		}
		else
		{
			strValue = value.toString();
		}
		// 선택한 position 보다 ArrayList의 크기가 작은경우, ArrayList의 size를 추가한다.
		while (position >= this.parameterValues.size())
		{
			this.parameterValues.add(null);
		}
		// arrayList에 값을 저장한 strValue 를 해당 index에 설정한다.
		this.parameterValues.set(position, strValue);
	}

	protected Date string2Date (final String value)
	{
		final java.util.Date d = DateUtil.string2Date(value);
		return new java.sql.Date(d.getTime());
	}
}