package chk.jsphelper.util;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.module.wrapper.MapStringsAdapter;
import chk.jsphelper.object.Message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ConvertUtil
{
	private ConvertUtil()
	{

	}

	/**
	 * DataList의 데이타를 json형식의 문자열로 변환하는 메소드이다.
	 * 
	 * @param dl
	 *            - 변환할 DataList
	 * @return - json 데이타 문자열
	 */
	public static String dataList2JSON (final DataList dl)
	{
		final StringBuilder sb = new StringBuilder("[");
		while (dl.next())
		{
			sb.append(",\n\t{");
			int j = 0;
			for (final int y = dl.getFieldCount(); j < (y - 1); j++)
			{
				sb.append("'").append(dl.getFieldName(j)).append("' : '").append(dl.getString(dl.getFieldName(j))).append("', ");
			}
			sb.append("'").append(dl.getFieldName(j)).append("' : '").append(dl.getString(dl.getFieldName(j))).append("'}");
		}
		sb.append("]");
		return sb.delete(1, 2).toString();
	}

	/**
	 * DataList의 데이타를 Map<String, String[]> 형태로 변환하는 메소드이다.<br>
	 * 이것은 나중에 Parameter로 활용될 수 있다.
	 * 
	 * @param dl
	 *            - 변환할 DataList
	 * @return - 변환된 Map 클래스
	 */
	public static Map<String, String[]> dataList2Map (final DataList dl)
	{
		final Map<String, String[]> map = new MapStringsAdapter();
		for (int i = 0; i < dl.getFieldCount(); i++)
		{
			map.put(dl.getFieldName(i), dl.getFieldData(i + 1));
		}
		return map;
	}

	/**
	 * DB Table에 담겨진 메시지를 Message 오브젝트에 반영하는 메소드이다
	 * 
	 * @param dl
	 *            - 변환할 DataList (첫번째 필드 : 메시지 id, 두번째 필드 : 메시지 설명, 세번째 필드 이상 : 언어별 메시지)
	 * @param langCode
	 *            - 변환할 메시지의 언어코드. 쿼리의 두번째 순서 부터의 언어 코드의 순서와 일치해야 한다
	 */
	public static boolean dataList2Message (final DataList dl, final String[] langCode)
	{
		try
		{
			final ObjectFactory of = new ObjectFactory();
			while (dl.next())
			{
				final Message message = new Message();
				message.setId(dl.getString(1));
				message.setDescription(dl.getString(2));
				for (int j = 2, y = dl.getFieldCount(); j < y; j++)
				{
					message.addString(langCode[j - 2], dl.getString(j + 1));
				}
				of.putObject(message);
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * DataList 의 데이타를 xml 형태의 문자열로 변환하는 메소드이다.
	 * 
	 * @param dl
	 *            - 변환할 DataList
	 * @return 변환된 xml 형태의 문자열
	 */
	public static String dataList2XML (final DataList dl)
	{
		final StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<list>\n");
		int i = 0;
		while (dl.next())
		{
			sb.append("\t<data row=\"").append(++i).append("\">\n");
			for (int j = 0, y = dl.getFieldCount(); j < y; j++)
			{
				sb.append("\t\t<").append(dl.getFieldName(j)).append("><![CDATA[").append(dl.getString(dl.getFieldName(j))).append("]]></").append(dl.getFieldName(j)).append(">\n");
			}
			sb.append("\t</data>\n");
		}
		sb.append("</list>");
		return sb.toString();
	}

	/**
	 * Map<String, String[]> 형식의 데이타를 json형태의 문자열로 반환하는 메소드이다.
	 * 
	 * @param map
	 *            - 변환활 Map
	 * @return - 변환된 JSON 문자열
	 */
	public static String map2JSON (final Map<String, String[]> map)
	{
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : map.entrySet())
		{
			final String[] values = entry.getValue();
			sb.append(",\n\t\"").append(entry.getKey()).append("\" : [");
			final StringBuilder sb1 = new StringBuilder();
			for (final String value : values)
			{
				sb1.append(", \"").append(value).append("\"");
			}
			sb.append(sb1.substring(2)).append("]");
		}
		return "{" + sb.substring(map.size() == 0 ? 0 : 1) + "\n}";
	}

	/**
	 * 데이타의 가로 세로의 값들을 피봇 시키는 메소드이다.<br>
	 * 단 필드명은 피봇될 수 없으므로 1, 2, 3, ... 식으로 지정된다.
	 * 
	 * @return - 피봇된 레코드셋 객체
	 */
	public static DataList pivotDataList (final DataList dl)
	{
		final String[] fields = new String[dl.size()];
		for (int i = 0, z = dl.size(); i < z; i++)
		{
			fields[i] = Integer.toString(i + 1);
		}
		final DataList rtnDL = new DataList(fields);

		for (int i = 0, z = dl.getFieldCount(); i < z; i++)
		{
			final Map<String, String> data = new HashMap<String, String>();
			final Iterator<String> keys = dl.keySet().iterator();
			int j = 0;
			while (keys.hasNext())
			{
				final String key = keys.next();
				data.put(fields[j++], dl.get(key, i));
			}
			rtnDL.addData(data);
		}
		return rtnDL;
	}
}