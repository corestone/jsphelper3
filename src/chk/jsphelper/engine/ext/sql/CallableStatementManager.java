package chk.jsphelper.engine.ext.sql;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import chk.jsphelper.Constant;
import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.module.wrapper.MapWrapper;
import chk.jsphelper.object.enums.SqlBindDir;
import chk.jsphelper.object.sub.SqlBind;

public class CallableStatementManager extends StatementManager
{
	private CallableStatement cs = null;
	private String[] outParameter;

	public CallableStatementManager (final List<SqlBind> bindValue, final MapWrapper map)
	{
		super(bindValue, map);
	}

	/**
	 * @param cs
	 * @param index
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void bindingCS (final ConnWrapper conn, final String sql, final int index) throws SQLException, ParseException
	{
		this.paramIndex = index;
		this.outParameter = new String[this.bindValue.size() + 1];
		this.cs = conn.prepareCall("{" + sql + "}");
		for (int i = 0; i < this.bindValue.size(); i++)
		{
			this.setBinderCS(i);
		}
	}

	public boolean execute () throws SQLException
	{
		boolean returnValue;
		final long start = System.nanoTime();
		returnValue = this.cs.execute();
		this.executeTime = System.nanoTime() - start;
		return returnValue;
	}

	/**
	 * 쿼리 결과값에서 out파라미터로 세팅된 값들을 반환하는 메소드
	 * 
	 * @return
	 */
	public String[] getOutParameter ()
	{
		return this.outParameter;
	}

	public String getString (final String parameterName) throws SQLException
	{
		return this.cs.getString(parameterName);
	}

	private void setBinderCS (final int index) throws SQLException, ParseException
	{
		final SqlBind sb = this.bindValue.get(index);
		final String value = sb.getValue();
		if (!SqlBindDir.OUT.equals(sb.getDirEnum()) && !this.map.containsKey(value))
		{
			Constant.getLogger().error("CallableStatement 바인드할 {}변수가 파라미터에 입력되지 않았습니다.", value);
			return;
		}

		final String paramValue = this.getParamValue(value, index, sb.getInitval());
		this.parameterValues.add(paramValue);
		Constant.getLogger().info("바인드 {}번째 : {} 타입의 값 '{}'", new Object[] { (index + 1), sb.getTypeEnum().getSymbol(), paramValue });

		try
		{
			switch (sb.getTypeEnum())
			{
				case STRING :
					switch (sb.getDirEnum())
					{
						case IN :
							this.cs.setString(index + 1, paramValue);
							break;
						case OUT :
							this.outParameter[index + 1] = "STRING";
							this.cs.registerOutParameter(index + 1, java.sql.Types.VARCHAR);
							break;
						case INOUT :
							this.cs.setString(index + 1, paramValue);
							this.outParameter[index + 1] = "STRING";
							this.cs.registerOutParameter(index + 1, java.sql.Types.VARCHAR);
					}
					break;
				case INT :
					switch (sb.getDirEnum())
					{
						case IN :
							this.cs.setInt(index + 1, Integer.parseInt(paramValue));
							break;
						case OUT :
							this.outParameter[index + 1] = "INT";
							this.cs.registerOutParameter(index + 1, java.sql.Types.INTEGER);
							break;
						case INOUT :
							this.cs.setInt(index + 1, Integer.parseInt(paramValue));
							this.outParameter[index + 1] = "INT";
							this.cs.registerOutParameter(index + 1, java.sql.Types.INTEGER);
					}
					break;
				case DATE :
					switch (sb.getDirEnum())
					{
						case IN :
							this.cs.setDate(index + 1, this.string2Date(paramValue));
							break;
						case OUT :
							this.outParameter[index + 1] = "DATE";
							this.cs.registerOutParameter(index + 1, java.sql.Types.DATE);
							break;
						case INOUT :
							this.cs.setDate(index + 1, this.string2Date(paramValue));
							this.outParameter[index + 1] = "DATE";
							this.cs.registerOutParameter(index + 1, java.sql.Types.DATE);
					}
					break;
				case FLOAT :
					switch (sb.getDirEnum())
					{
						case IN :
							this.cs.setFloat(index + 1, Float.parseFloat(paramValue));
							break;
						case OUT :
							this.outParameter[index + 1] = "FLOAT";
							this.cs.registerOutParameter(index + 1, java.sql.Types.FLOAT);
							break;
						case INOUT :
							this.cs.setFloat(index + 1, Float.parseFloat(paramValue));
							this.outParameter[index + 1] = "FLOAT";
							this.cs.registerOutParameter(index + 1, java.sql.Types.FLOAT);
					}
					break;
				case STREAM :
					switch (sb.getDirEnum())
					{
						case IN :
							this.cs.setCharacterStream(index + 1, new StringReader(paramValue), paramValue.length());
							break;
						case OUT :
							this.outParameter[index + 1] = "STREAM";
							this.cs.registerOutParameter(index + 1, java.sql.Types.LONGVARCHAR);
							break;
						case INOUT :
							this.cs.setCharacterStream(index + 1, new StringReader(paramValue), paramValue.length());
							this.outParameter[index + 1] = "STREAM";
							this.cs.registerOutParameter(index + 1, java.sql.Types.LONGVARCHAR);
					}
					break;
			}
		}
		catch (final SQLException sqle)
		{
			Constant.getLogger().error("CallableStatement에 '{}'변수를 바인드할 때 SQLException 예외가 발생하였습니다.", value);
			throw sqle;
		}
	}
}