package chk.jsphelper.module.mapper;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.engine.ext.sql.DbDataUtil;
import chk.jsphelper.module.wrapper.MapStringsAdapter;
import chk.jsphelper.util.StringUtil;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Corestone
 */
public class DataMapper
{
	private ResultSet rs = null;

	/**
	 * @param rs
	 */
	public DataMapper (final ResultSet rs)
	{
		this.rs = rs;
	}

	/**
	 * @return - 생성된 DataList 객체
	 * @throws Exception
	 */
	public DataList createDataList () throws Exception
	{
		if (this.rs == null)
		{
			return null;
		}
		final ResultSetMetaData rsmd = this.rs.getMetaData();
		final String[] fields = new String[rsmd.getColumnCount()];
		for (int i = 0, z = rsmd.getColumnCount(); i < z; i++)
		{
			fields[i] = rsmd.getColumnName(i + 1);
		}
		final DataList dl = new DataList(fields);

		while (this.rs.next())
		{
			final Map<String, String> data = new HashMap<String, String>();
			for (int i = 0, z = rsmd.getColumnCount(); i < z; i++)
			{
				final Object value = this.rs.getObject(i + 1);
				String svalue = null;
				// 값이 null 이면 빈 문자열로 세팅하여 준다.
				if (value == null)
				{
					svalue = "";
					data.put(fields[i], svalue);
					continue;
				}
				// 값이 CLOB 타입이면 그 값에 마
				if (value instanceof Clob)
				{
					svalue = DbDataUtil.getClobData(((Clob) value).getCharacterStream());
				}
				else
				{
					svalue = String.valueOf(value);
				}
				data.put(fields[i], svalue);
			}
			dl.addData(data);
		}
		return dl;
	}

	public DataList setOutParameter (final Map<String, String> data) throws Exception
	{
		final String[] fields = new String[data.size()];
		int i = 0;
		for (String param : data.keySet())
		{
			fields[i++] = param;
		}
		final DataList dl = new DataList(fields);
        dl.addData(data);
		return dl;
	}

	/**
	 * @param returnKeys
	 * @param returnFields
	 * @param param
	 * @param dl
	 */
	public void setReturnParam (final List<String> returnKeys, final List<String> returnFields, final MapStringsAdapter param, final DataList dl)
	{
		if ((returnKeys.size() > 0) && (returnFields.size() > 0))
		{
			if (dl.size() > 0)
			{
				for (int j = 0, y = returnKeys.size(); j < y; j++)
				{
					final String field = returnFields.get(j);
					final String key = returnKeys.get(j);
					param.put(StringUtil.trimDefault(key, field), dl.getFieldDatas(field));
					Constant.getLogger().debug("파라미터 '{}'에 '{}' 등 {}개의 데이타가 입력되었습니다.", new Object[] { key, dl.getFieldDatas(field)[0], dl.getFieldDatas(field).length });
				}
			}
			else
			{
				Constant.getLogger().warn("DataList의 레코드가 하나도 없어서 Parameter에 값을 입력하지 못했습니다.");
			}
		}
	}
}