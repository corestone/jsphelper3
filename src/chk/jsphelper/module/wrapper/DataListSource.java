package chk.jsphelper.module.wrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chk.jsphelper.Constant;

public class DataListSource implements Map<String, List<String>>
{
	protected final Map<String, List<String>> m;

	public DataListSource ()
	{
		this.m = new LinkedHashMap<String, List<String>>();
	}

	public boolean add (final Object key, final String value)
	{
		if (this.m.get(key) == null)
		{
			return false;
		}
		return this.m.get(key).add(value);
	}

	public void clear ()
	{
		this.m.clear();
	}

	public boolean containsKey (final Object key)
	{
		return this.m.containsKey(key);
	}

	public boolean containsValue (final Object value)
	{
		return this.m.containsValue(value);
	}

	public Set<Entry<String, List<String>>> entrySet ()
	{
		return this.m.entrySet();
	}

	public List<String> get (final Object key)
	{
		return this.m.get(key);
	}

	public String get (final Object key, final int index)
	{
		if (key == null)
		{
			Constant.getLogger().warn("키에 null이 입력되어 null을 반환합니다.");
			return null;
		}
		if (!this.m.containsKey(key))
		{
			Constant.getLogger().warn("'{}'키에 해당하는 값이 없어서 \"\"를 반환합니다.", key);
			return "";
		}
		if (this.m.get(key).size() <= index)
		{
			Constant.getLogger().warn("해당 DataList의 {}필드에는 {}번째 데이타가 없어서 마지막 데이타를 반환합니다.", new Object[] { key, index });
			return this.m.get(key).get(this.m.get(key).size() - 1);
		}
		return this.m.get(key).get(index);
	}

	public boolean isEmpty ()
	{
		return this.m.isEmpty();
	}

	public Set<String> keySet ()
	{
		return this.m.keySet();
	}

	public List<String> put (final String key, final List<String> value)
	{
		return this.m.put(key, value);
	}

	public void putAll (final Map<? extends String, ? extends List<String>> value)
	{
		final Iterator<? extends String> keys = value.keySet().iterator();
		while (keys.hasNext())
		{
			final String key = keys.next();
			this.m.put(key, value.get(key));
		}
	}

	public List<String> remove (final Object key)
	{
		return this.m.remove(key);
	}

	public int size ()
	{
		return this.m.size();
	}

	public Collection<List<String>> values ()
	{
		return this.m.values();
	}
}