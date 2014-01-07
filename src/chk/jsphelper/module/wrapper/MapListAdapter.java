package chk.jsphelper.module.wrapper;

import chk.jsphelper.Constant;

import java.util.*;

public class MapListAdapter implements Map<String, List<String>>
{
	protected final Map<String, List<String>> m;

	public MapListAdapter()
	{
		this.m = new LinkedHashMap<String, List<String>>();
	}

	public boolean add (final Object key, final String value)
	{
		return this.m.get(key) != null && this.m.get(key).add(value);
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
		for (Map.Entry<? extends String, ? extends List<String>> entry : value.entrySet ())
		{
			this.m.put(entry.getKey(), entry.getValue());
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