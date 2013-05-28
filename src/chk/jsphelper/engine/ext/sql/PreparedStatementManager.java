package chk.jsphelper.engine.ext.sql;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import chk.jsphelper.Constant;
import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.module.wrapper.MapWrapper;
import chk.jsphelper.object.sub.SqlBind;

public class PreparedStatementManager extends StatementManager
{
	private PreparedStatement ps = null;

	public PreparedStatementManager (final List<SqlBind> bindValue, final MapWrapper map)
	{
		super(bindValue, map);
	}

	public void addBatch () throws SQLException
	{
		this.ps.addBatch();
	}

	/**
	 * @param conn
	 * @param sql
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void bindingPS (final ConnWrapper conn, final String sql) throws SQLException, ParseException
	{
		this.paramIndex = 0;
		this.closeStatement(this.ps);
		this.ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < this.bindValue.size(); i++)
		{
			this.setBinderPS(i);
		}
	}

	/**
	 * @param conn
	 * @param sql
	 * @param index
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void bindingPS (final ConnWrapper conn, final String sql, final int index) throws SQLException, ParseException
	{
		this.paramIndex = index;
		this.closeStatement(this.ps);
		this.ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < this.bindValue.size(); i++)
		{
			this.setBinderPS(i);
		}
	}

	/**
	 * 프리페어드 세테이이트면트에 값을 바인드하는 메소드로 자동적으로 해당 페이지만 가지고 오는 페이지 관련 변수를 매핑한다.
	 * 
	 * @since 3.0
	 * @param ps
	 * @param pagingVar1
	 * @param pagingVar2
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void bindingPS4Paging (final ConnWrapper conn, final String sql, final int pagingVar1, final int pagingVar2) throws SQLException, ParseException
	{
		this.paramIndex = 0;
		this.closeStatement(this.ps);
		this.ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		int i = 0;
		for (i = 0; i < this.bindValue.size(); i++)
		{
			this.setBinderPS(i);
		}
		this.ps.setInt(++i, pagingVar1);
		this.ps.setInt(++i, pagingVar2);
	}

	public int[] executeBatch () throws SQLException
	{
		int[] returnValue;
		final long start = System.nanoTime();
		returnValue = this.ps.executeBatch();
		this.executeTime = System.nanoTime() - start;
		return returnValue;
	}

	public ResultSet executeQuery () throws SQLException
	{
		ResultSet returnValue;
		final long start = System.nanoTime();
		returnValue = this.ps.executeQuery();
		this.executeTime = System.nanoTime() - start;
		return returnValue;
	}

	public int executeUpdate () throws SQLException
	{
		int returnValue;
		final long start = System.nanoTime();
		returnValue = this.ps.executeUpdate();
		this.executeTime = System.nanoTime() - start;
		return returnValue;
	}

	/**
	 * @param index
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void setBinding (final int index) throws SQLException, ParseException
	{
		this.paramIndex = index;
		for (int i = 0; i < this.bindValue.size(); i++)
		{
			this.setBinderPS(i);
		}
	}

	private void setBinderPS (final int index) throws SQLException, ParseException
	{
		final SqlBind sb = this.bindValue.get(index);

		if (!this.map.containsKey(sb.getValue()) && (sb.getInitval() == null))
		{
			Constant.getLogger().error("PreparedStatement에 바인드할 {}변수가 파라미터에 입력되지 않았습니다.", sb.getValue());
		}

		final String value = sb.getValue();
		final String paramValue = this.getParamValue(value, index, sb.getInitval());
		this.parameterValues.add(paramValue);
		Constant.getLogger().info("바인드 {}번째 : {} 타입의 값 '{}'", new Object[] { (index + 1), sb.getTypeEnum().getSymbol(), paramValue });

		try
		{
			switch (sb.getTypeEnum())
			{
				case STRING :
					this.ps.setString(index + 1, paramValue);
					break;
				case INT :
					this.ps.setInt(index + 1, Integer.parseInt(paramValue));
					break;
				case DATE :
					this.ps.setDate(index + 1, this.string2Date(paramValue));
					break;
				case FLOAT :
					this.ps.setFloat(index + 1, Float.parseFloat(paramValue));
					break;
				case STREAM :
					final StringReader sr = new StringReader(paramValue);
					this.ps.setCharacterStream(index + 1, sr, paramValue.length());
					break;
			}
		}
		catch (final SQLException sqle)
		{
			Constant.getLogger().error("PreparedStatement에 '{}'변수를 바인드할 때 SQLException 예외가 발생하였습니다.", value);
			throw sqle;
		}
	}
}