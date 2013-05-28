package chk.jsphelper.object;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.DataSourceVendor;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.object.sub.DataSourceProperty;

public class DataSource implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType._DATASOURCE;
	private String description;
	private String driver;
	private String id;
	private int idlesize = 16;
	private int interval = 50;
	private int maxsize = 32;
	private final Map<String, String> property = new HashMap<String, String>();
	private String sql;
	private String url;
	private DataSourceVendor vendorEnum;

	/**
	 * 
	 */
	public void addProperty (final DataSourceProperty dsp)
	{
		this.property.put(dsp.getName(), dsp.getValue());
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
	 * @return
	 */
	public String getDriver ()
	{
		return this.driver;
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

	/**
	 * @return
	 */
	public int getIdlesize ()
	{
		if ((this.idlesize != 0) && (this.idlesize < 2))
		{
			return 2;
		}
		else
		{
			return this.idlesize;
		}
	}

	/**
	 * @return
	 */
	public int getInterval ()
	{
		return this.interval;
	}

	/**
	 * @return
	 */
	public int getMaxsize ()
	{
		if ((this.maxsize != 0) && (this.maxsize < 4))
		{
			return 4;
		}
		else
		{
			return this.maxsize;
		}
	}

	public ObjectType getObjectType ()
	{
		return DataSource.TYPE;
	}

	/**
	 * @return
	 */
	public Map<String, String> getProperty ()
	{
		return this.property;
	}

	/**
	 * @return
	 */
	public String getSql ()
	{
		return this.sql;
	}

	/**
	 * @return
	 */
	public String getUrl ()
	{
		return this.url;
	}

	/**
	 * @return
	 */
	public DataSourceVendor getVendorType ()
	{
		return this.vendorEnum;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	/**
	 * @param driver
	 */
	public void setDriver (final String driver)
	{
		this.driver = driver;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param idleSize
	 */
	public void setIdlesize (final int idlesize)
	{
		this.idlesize = idlesize;
	}

	/**
	 * @param interval
	 */
	public void setInterval (final int interval)
	{
		if (interval < 30)
		{
			this.interval = 30;
		}
		else
		{
			this.interval = interval;
		}
	}

	/**
	 * @param maxSize
	 */
	public void setMaxsize (final int maxsize)
	{
		this.maxsize = maxsize;
	}

	/**
	 * @param sql
	 */
	public void setSql (final String sql)
	{
		this.sql = sql;
	}

	/**
	 * @param url
	 */
	public void setUrl (final String url)
	{
		this.url = url;
	}

	/**
	 * @param vendor
	 */
	public void setVendor (final String vendor)
	{
		System.out.println("################################## " + vendor);
		if (vendor.equals("oracle"))
		{
			this.vendorEnum = DataSourceVendor.ORACLE;
		}
		else if (vendor.equals("mssql"))
		{
			this.vendorEnum = DataSourceVendor.MSSQL;
		}
		else if (vendor.equals("mysql"))
		{
			this.vendorEnum = DataSourceVendor.MYSQL;
		}
		else if (vendor.equals("db2"))
		{
			this.vendorEnum = DataSourceVendor.DB2;
		}
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}