package chk.jsphelper.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.Constant;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.object.sub.SqlBind;
import chk.jsphelper.object.sub.SqlClob;
import chk.jsphelper.util.StringUtil;

public class Sql implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.SQL;
	protected final List<SqlBind> binds = new ArrayList<SqlBind>();
	protected final List<Map<String, String>> clobs = new ArrayList<Map<String, String>>();
	protected String datasource;
	protected String description;
	protected String id;
	protected boolean paging;
	protected String query;
	protected String srcenc = "UTF-8";
	protected String trgenc = "UTF-8";

	/**
	 * 
	 */
	public void addBind (final SqlBind bind)
	{
		this.binds.add(bind);
	}

	/**
	 * 
	 */
	public void addClob (final SqlClob clob)
	{
		final Map<String, String> hm = new HashMap<String, String>();
		hm.put("FIELD", clob.getField());
		hm.put("VALUE", clob.getValue());
		this.clobs.add(hm);
	}

	/**
	 * @return
	 */
	public List<SqlBind> getBinds ()
	{
		return this.binds;
	}

	/**
	 * @return
	 */
	public List<Map<String, String>> getClobs ()
	{
		return this.clobs;
	}

	/**
	 * @return
	 */
	public String getDatasourceName ()
	{
		return this.datasource;
	}

	/**
	 * 오브젝트의 설명을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 설명인 description
	 */
	public final String getDescription ()
	{
		return this.description;
	}

	/**
	 * 오브젝트의 id를 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 id
	 */
	public final String getId ()
	{
		return this.id;
	}

	public ObjectType getObjectType ()
	{
		return Sql.TYPE;
	}

	/**
	 * @return
	 */
	public String getQuery ()
	{
		return this.query;
	}

	/**
	 * 오브젝트의 소스 인코딩을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 소스 인코딩
	 */
	public String getSrcenc ()
	{
		return this.srcenc;
	}

	/**
	 * 오브젝트의 타겟 인코딩을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 타겟 인코딩
	 */
	public String getTrgenc ()
	{
		return this.trgenc;
	}

	/**
	 * @return
	 */
	public boolean isPaging ()
	{
		return this.paging;
	}

	/**
	 * @param datasource
	 */
	public void setDatasource (final String datasource)
	{
		this.datasource = datasource;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param paging
	 */
	public void setPaging (final boolean paging)
	{
		this.paging = paging;
	}

	/**
	 * @param query
	 */
	public void setQuery (final String query)
	{
		this.query = "true".equals(Constant.getValue("Object.Compress", "false")) ? StringUtil.compressWhitespace(query) : query;
	}

	/**
	 * 오브젝트의 소스 인코딩을 지정하는 메소드이다.
	 * 
	 * @param srcenc
	 *            - 소스 인코딩
	 */
	public void setSrcenc (final String srcenc)
	{
		this.srcenc = srcenc;
	}

	/**
	 * 오브젝트의 타겟 인코딩을 지정하는 메소드이다.
	 * 
	 * @param trgenc
	 *            - 타겟 인코딩
	 */
	public void setTrgenc (final String trgenc)
	{
		this.trgenc = trgenc;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}