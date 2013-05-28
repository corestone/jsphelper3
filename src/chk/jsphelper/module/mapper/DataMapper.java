package chk.jsphelper.module.mapper;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.engine.ext.sql.DataUtil;
import chk.jsphelper.module.wrapper.MapWrapper;
import chk.jsphelper.util.StringUtil;

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
	 * @param srcEnc
	 * @param trgEnc
	 * @return - 생성된 DataList 객체
	 * @throws Exception
	 */
	public DataList createDataList (final String srcEnc, final String trgEnc) throws Exception
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
					svalue = DataUtil.getClobData(((Clob) value).getCharacterStream());
				}
				else
				{
					svalue = String.valueOf(value);
				}
				if ((srcEnc != null) && (trgEnc != null) && !srcEnc.equals(trgEnc))
				{
					Constant.getLogger().debug("캐릭터 셋이 {}인 데이타 '{}'을 캐릭터 셋이 {}인 데이타 '{}'로 변경합니다.", new Object[] { srcEnc, svalue, trgEnc, new String(svalue.getBytes(srcEnc), trgEnc) });
					svalue = new String(svalue.getBytes(srcEnc), trgEnc);
				}
				data.put(fields[i], svalue);
			}
			dl.addData(data);
		}
		return dl;
	}

	/**
	 * @param parameter
	 * @param srcEnc
	 * @param trgEnc
	 * @return Out파라미터에 해당하는 값을 DataList로 변환하여 반환된 오브젝트
	 * @throws Exception
	 */
	public DataList setOutParameter (final Map<String, String> data, final String srcEnc, final String trgEnc) throws Exception
	{
		final String[] fields = new String[data.size()];
		int i = 0;
		final Iterator<String> iter = data.keySet().iterator();
		while (iter.hasNext())
		{
			fields[i++] = iter.next();
		}
		final DataList dl = new DataList(fields);
		if ((srcEnc != null) && (trgEnc != null) && !srcEnc.equals(trgEnc))
		{
			for (i = 0; i < fields.length; i++)
			{
				Constant.getLogger().debug("캐릭터 셋이 {}인 데이타 '{}'을 캐릭터 셋이 {}인 데이타 '{}'로 변경합니다.", new Object[] { srcEnc, data.get(fields[i]), trgEnc, new String(data.get(fields[i]).getBytes(srcEnc), trgEnc) });
				data.put(fields[i], new String(data.get(fields[i]).getBytes(srcEnc), trgEnc));
			}
		}
		else
		{
			dl.addData(data);
		}
		return dl;
	}

	/**
	 * @param returnKeys
	 * @param returnFields
	 * @param param
	 * @param dl
	 */
	public void setReturnParam (final List<String> returnKeys, final List<String> returnFields, final MapWrapper param, final DataList dl)
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