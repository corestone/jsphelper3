package chk.jsphelper.engine.ext.sql;

import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.ResultSet;

import chk.jsphelper.Constant;

/**
 * @author Corestone
 */
public class DataUtil
{
	/**
	 * @param reader
	 * @return - CLOB 데이타의 값을
	 * @throws Exception
	 */
	public static String getClobData (final Reader reader) throws Exception
	{
		final StringBuilder data = new StringBuilder();
		final char[] buf = new char[1024];
		int cnt = 0;
		if (null != reader)
		{
			try
			{
				while ((cnt = reader.read(buf)) != -1)
				{
					data.append(buf, 0, cnt);
				}
			}
			catch (final Exception e)
			{
				Constant.getLogger().error("CLOB 타입의 필드에서 값을 읽어올 때 {} 예외가 발생하였습니다.", e.getClass().getName());
				throw e;
			}
		}
		return data.toString();
	}

	/**
	 * @param rs
	 * @param field
	 * @param data
	 * @throws Exception
	 */
	public static void setClobData (final ResultSet rs, final String field, final String data) throws Exception
	{
		try
		{
			final Clob clob = rs.getClob(field);
			final Writer writer = clob.setCharacterStream(0);
			writer.write(data);
			writer.close();
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("CLOB 타입의 [{}]필드에 값을 입력할 때 {} 예외가 발생하였습니다.", new String[] { field, e.getClass().getName() });
			throw e;
		}
	}

	private DataUtil ()
	{

	}
}