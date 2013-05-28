package chk.jsphelper.module.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author Corestone
 */
public class ListAdapter implements List<Map<String, String>>
{
	/**
	 * 
	 */
	protected final List<Map<String, String>> datas;

	/**
	 * 
	 */
	public ListAdapter ()
	{
		this.datas = new ArrayList<Map<String, String>>();
	}

	public void add (final int index, final Map<String, String> element)
	{
		this.datas.add(index, element);
	}

	public boolean add (final Map<String, String> element)
	{
		return this.datas.add(element);
	}

	public boolean addAll (final Collection<? extends Map<String, String>> records)
	{
		return this.datas.addAll(records);
	}

	public boolean addAll (final int index, final Collection<? extends Map<String, String>> c)
	{
		return this.datas.addAll(index, c);
	}

	/**
	 * 객체에 담겨져 있는 값들을 모두 지워 초기화 하는 메소드이다.
	 */
	public void clear ()
	{
		this.datas.clear();
	}

	public boolean contains (final Object object)
	{
		if (object instanceof Map<?, ?>)
		{
			return this.datas.contains(object);
		}
		return false;
	}

	public boolean containsAll (final Collection<?> records)
	{
		return records.containsAll(records);
	}

	public Map<String, String> get (final int index)
	{
		return this.datas.get(index);
	}

	public int indexOf (final Object o)
	{
		return this.datas.indexOf(o);
	}

	public boolean isEmpty ()
	{
		return this.datas.isEmpty();
	}

	public Iterator<Map<String, String>> iterator ()
	{
		return this.datas.iterator();
	}

	public int lastIndexOf (final Object o)
	{
		return this.datas.lastIndexOf(o);
	}

	public ListIterator<Map<String, String>> listIterator ()
	{
		return this.datas.listIterator();
	}

	public ListIterator<Map<String, String>> listIterator (final int index)
	{
		return this.datas.listIterator(index);
	}

	public Map<String, String> remove (final int index)
	{
		return this.datas.remove(index);
	}

	public boolean remove (final Object object)
	{
		return this.datas.remove(object);
	}

	public boolean removeAll (final Collection<?> records)
	{
		return this.datas.removeAll(records);
	}

	public boolean retainAll (final Collection<?> records)
	{
		return this.datas.retainAll(records);
	}

	public Map<String, String> set (final int index, final Map<String, String> element)
	{
		return this.datas.set(index, element);
	}

	/**
	 * 객체에 담겨진 레코드의 갯수를 반환하는 메소드이다.
	 * 
	 * @return - 레코드의 갯수
	 */
	public int size ()
	{
		return this.datas.size();
	}

	public List<Map<String, String>> subList (final int fromIndex, final int toIndex)
	{
		return this.datas.subList(fromIndex, toIndex);
	}

	/**
	 * 해당 객체에 담겨진 값들을 2차원 배열로 반환하는 메소드이다.
	 * 
	 * @return - 값들을 담은 2차원 배열
	 */
	public String[][] toArray ()
	{
		final int colSize = this.datas.get(1).size();
		final String[][] sa = new String[this.size()][colSize];
		for (int i = 0, z = this.size(); i < z; i++)
		{
			final Map<String, String> m = this.get(i);
			final Iterator<String> iter = m.keySet().iterator();
			int j = 0;
			while (iter.hasNext())
			{
				final String key = iter.next();
				sa[i][j] = m.get(key);
				j++;
			}
		}
		return sa;
	}

	public <T> T[] toArray (final T[] array)
	{
		return this.datas.toArray(array);
	}

	@Override
	public String toString ()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.datas.size(); i++)
		{
			sb.append("\n").append(this.datas.get(i));
		}
		return sb.substring(1);
	}
}