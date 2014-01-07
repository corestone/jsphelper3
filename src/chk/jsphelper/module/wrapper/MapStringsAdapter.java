package chk.jsphelper.module.wrapper;

import chk.jsphelper.Constant;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author Corestone
 */
public class MapStringsAdapter implements Map<String, String[]>
{
	/**
	 * 
	 */
	protected final Map<String, String[]> m;

	/**
	 * 
	 */
	public MapStringsAdapter()
	{
		this.m = new HashMap<String, String[]>();
	}

	/**
	 * 인자로 HashMap을 받는 생성자이다. HashMap의 각 값들을 파라미터의 값들로 세팅하여 준다.
	 * 
	 * @param map
	 *            - 파라미터에 세팅할 값이 들어있는 HashMap
	 */
	public MapStringsAdapter(final Map<String, String[]> map)
	{
		this.m = new HashMap<String, String[]>();
		this.m.putAll(map);
	}

	public void clear ()
	{
		this.m.clear();
	}

	public boolean containsKey (final Object key)
	{
		if (key instanceof String)
		{
			return this.m.containsKey(key);
		}
		Constant.getLogger().error("Parameter의 키 '{}'는 String 형이 아닙니다.", key);
		return false;
	}

	public boolean containsValue (final Object value)
	{
		if (value instanceof String[])
		{
			return this.m.containsValue(value);
		}
		Constant.getLogger().error("Parameter의 값 '{}'는 String[] 형이 아닙니다.", value);
		return false;
	}

	public Set<Entry<String, String[]>> entrySet ()
	{
		return this.m.entrySet();
	}

	public String[] get (final Object key)
	{
		if (key instanceof String)
		{
			return this.m.get(key);
		}
		Constant.getLogger().error("Parameter의 키 '{}'는 String 형이 아닙니다.", key);
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}

	public boolean isEmpty ()
	{
		return this.m.isEmpty();
	}

	public Set<String> keySet ()
	{
		return this.m.keySet();
	}

	/**
	 * 파라미터에 값을 넣어주는 메소드이다. '_' 로 시작되는 키는 시스템에서 예약된 키이므로 키 이름으로 사용하지 말아야 한다.
	 * 
	 * @param key
	 *            - 키 이름 ('_'로 시작되는 이름은 시스템용이므로 사용하지 말 것)
	 * @param value
	 *            - 키에 해당하는 문자열 배열 값
	 */
	public String[] put (final String key, final String[] value)
	{
		if ((key.indexOf('_') == 0))
		{
			Constant.getLogger().error("{}는 _로 시작되는 파라미터 키로 시스템에서 사용되는 키이기 때문에 입력이 되지 않았습니다.", key);
			return value;
		}
		return this.m.put(key, value);
	}

	public void putAll (final Map<? extends String, ? extends String[]> map)
	{
		this.m.putAll(map);
	}

	public String[] remove (final Object key)
	{
		if (key instanceof String)
		{
			return this.m.remove(key);
		}
		Constant.getLogger().error("Parameter의 키 '{}'는 String 형이 아닙니다.", key);
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}

	public int size ()
	{
		return this.m.size();
	}

	@Override
	public String toString ()
	{
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sub = new StringBuilder();
		for (Map.Entry<String, String[]> entry : m.entrySet())
		{
			sb.append("\n[").append(entry.getKey()).append("] {");

			sub.setLength(0);
			for (int j = 0, z = entry.getValue().length; j < z; j++)
			{
				sub.append(", ").append(j).append("=").append(entry.getValue()[j]);
			}
			sb.append(sub.substring(2)).append("}");
		}
		return sb.substring(1);
	}

	public Collection<String[]> values ()
	{
		return this.m.values();
	}
}