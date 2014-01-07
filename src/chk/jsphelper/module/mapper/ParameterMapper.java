package chk.jsphelper.module.mapper;

import chk.jsphelper.Constant;
import chk.jsphelper.module.wrapper.MapStringsAdapter;
import chk.jsphelper.object.enums.SqlBindDir;
import chk.jsphelper.object.sub.SqlBind;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Corestone H. Kang
 */
public class ParameterMapper
{
	private static final String CLOSE_TAG = "}";
	private static final String OPEN_TAG = "${";
	private final MapStringsAdapter map;
	private int mappingSize = 1;
	private String errorKey;

	/**
	 * @param map
	 */
	public ParameterMapper (final MapStringsAdapter map)
	{
		if (map == null)
		{
			Constant.getLogger().error("파라미터가 null이라서 파라미터 매퍼를 생성할 때 문제가 발생합니다.");
			this.map = new MapStringsAdapter();
		}
		else
		{
			this.map = map;
		}
	}

	/**
	 * 파라미터의 값의 배열의 크기를 체크하여 매핑될 파라미터의 크기를 결정하는 메소드이다.<br>
	 * 파라미터를 매핑하기 전에 이 메소드 아니면 paramCheckQuery을 반드시 실행해야 2개 이상의 파라미터가 매핑된다.
	 * 
	 * @throws Exception
	 */
	public void checkParam () throws Exception
	{
		int max = getParamSize(this.map);
		if (max == -1)
		{
			throw new Exception("[" + errorKey + "] 파라미터의 값 배열의 크기가 다른 파라미터의 배열크기와 다릅니다.");
		}
		this.mappingSize = max;
		Constant.getLogger().debug("매핑된 파라미터의 배열 크기는 {}입니다.", this.mappingSize);
	}

	/**
	 * 쿼리에 바인드를 시킬 변수값들을 체크하는 메소드이다.<br>
	 * 파라미터를 매핑하기 전에 이 메소드 아니면 paramCheck을 반드시 실행해야 2개 이상의 파라미터가 매핑된다.<br>
	 * 단지 쿼리만이 아니라 bind태그의 value값을 가지고도 체크를 해야 하기 때문에 다른 체크 메소드와는 별로로 만들어서 작업을 한다.
	 * 
	 * @param query
	 * @param binds
	 * @throws Exception
	 */
	public void checkParamQuery (final String query, final List<SqlBind> binds) throws Exception
	{
		final String[] queryBinds = this.getTagInText(query);
		int max = 1;

		for (final String key : queryBinds)
		{
			int calcMax = calcParamSize(this.map, key, max);
			switch (calcMax)
			{
				case 0:
					throw new Exception("Query에 바인드할 " + key + "변수가 파라미터에 입력되지 않았습니다.");
				case -1:
					throw new Exception("[" + key + "] 파라미터의 값 배열의 크기가 다른 파라미터의 배열크기와 다릅니다.");
				default:
					max = calcMax;
			}
		}

		for (final SqlBind sb : binds)
		{
			if (SqlBindDir.IN.equals(sb.getDirEnum()) || SqlBindDir.INOUT.equals(sb.getDirEnum()))
			{
				int calcMax = calcParamSize(this.map, sb.getValue(), max);
				switch (calcMax)
				{
					case 0:
						throw new Exception("Query에 바인드할 " + sb.getValue() + "변수가 파라미터에 입력되지 않았습니다.");
					case -1:
						throw new Exception("[" + sb.getValue() + "] 파라미터의 값 배열의 크기가 다른 파라미터의 배열크기와 다릅니다.");
					default:
						max = calcMax;
				}
			}
		}
		this.mappingSize = max;
		Constant.getLogger().debug("매핑된 파라미터의 배열 크기는 {}입니다.", this.mappingSize);
	}

	/**
	 * @param text
	 * @return - 문자열에서 Parameter 객체의 문자열 치환이 필요한 부분을 치환한 결과 문자열
	 */
	public String convertMappingText (final String text)
	{
		final String[] map = this.getTagInText(text);
		String rtnText = text;

		for (final String key : map)
		{
			if (this.map.get(key) == null)
			{
				Constant.getLogger().error("[{}] 에 해당하는 파라미터가 없어서 오브젝트의 값을 매핑할 수 없습니다.", key);
			}
			rtnText = StringUtils.replace(rtnText, ParameterMapper.OPEN_TAG + key + ParameterMapper.CLOSE_TAG, this.map.get(key)[0]);
		}
		return rtnText;
	}

	/**
	 * @param text
	 * @return - 문자열에서 Parameter의 값으로 치환할 문자열이 배열로 되어 있을 때 해당 배열로 치환한 결과 문자열 배열
	 */
	public String[] convertMappingTexts (final String text)
	{
		boolean isEquals = true;
		final String[] map = this.getTagInText(text);
		final String[] rtnText = new String[this.mappingSize];

		for (int i = 0; i < rtnText.length; i++)
		{
			rtnText[i] = text;
		}

		for (final String key : map)
		{
			for (int j = 0; j < rtnText.length; j++)
			{
				if (this.map.get(key) == null)
				{
					Constant.getLogger().error("[{}] 에 해당하는 파라미터가 없어서 오브젝트의 값을 매핑할 수 없습니다.", key);
				}
				if (this.map.get(key).length == 1)
				{
					rtnText[j] = StringUtils.replace(rtnText[j], ParameterMapper.OPEN_TAG + key + ParameterMapper.CLOSE_TAG, this.map.get(key)[0]);
				}
				else
				{
					rtnText[j] = StringUtils.replace(rtnText[j], ParameterMapper.OPEN_TAG + key + ParameterMapper.CLOSE_TAG, this.map.get(key)[j]);
				}
			}
		}
		// 만약 매핑된 값이 전부 다 같다면 크기 1의 배열로 반환한다
		for (int i = 1; i < rtnText.length; i++)
		{
			if (!rtnText[i - 1].equals(rtnText[i]))
			{
				return rtnText;
			}
		}
		return new String[] { rtnText[0] };
	}

	/**
	 * @return - 매핑되는 파라미터의 배열 사이즈
	 */
	public int getMappingSize ()
	{
		return this.mappingSize;
	}

	/**
	 * 문자열에서 '${', '}'로 쌓여 있는 변수명의 값들을 배열형태로 던져주는 메소드이다.
	 * 
	 * @param str
	 * @return
	 */
	private String[] getTagInText (final String str)
	{
		final String[] s = StringUtils.substringsBetween(str, ParameterMapper.OPEN_TAG, ParameterMapper.CLOSE_TAG);
		return (s == null) ? ArrayUtils.EMPTY_STRING_ARRAY : s;
	}

	private int getParamSize(Map<String, String[]> map)
	{
		int max = 1;
		for (String key : map.keySet())
		{
			int calcMax = calcParamSize(map, key, max);
			if (calcMax < 1)
			{
				return calcMax;
			}
			max = calcMax;
		}
		return max;
	}

	private int calcParamSize(Map<String, String[]> map, String key, int max)
	{
		final String[] values = map.get(key);
		if (values == null)
		{
			this.errorKey = key;
			return 0;
		}
		else if (values.length != max && max == 1)
		{
			max = values.length;
		}
		else
		{
			this.errorKey = key;
			return -1;
		}
		return max;
	}
}